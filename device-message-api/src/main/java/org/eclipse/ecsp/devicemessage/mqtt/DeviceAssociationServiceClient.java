/********************************************************************************
 * *******************************************************************************
 *  * Copyright (c) 2023-24 Harman International
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *  http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *
 *  * SPDX-License-Identifier: Apache-2.0
 *  *******************************************************************************
 *******************************************************************************/

package org.eclipse.ecsp.devicemessage.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.eclipse.ecsp.devicemessage.exception.AuthenticationException;
import org.eclipse.ecsp.devicemessage.exception.AuthorizationException;
import org.eclipse.ecsp.devicemessage.util.Constants;
import org.eclipse.ecsp.utils.logger.IgniteLogger;
import org.eclipse.ecsp.utils.logger.IgniteLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * This class represents a client for the Device Association Service.
 * It provides methods to interact with the service and retrieve user associations.
 */
@Service

public class DeviceAssociationServiceClient {

    private static final String USER_ASSOCIATIONS = "/user/associations/";
    private static final IgniteLogger LOGGER = IgniteLoggerFactory.getLogger(DeviceAssociationServiceClient.class);
    /**
     * Auth token prefix - Bearer constant.
     */
    private static final String BEARER = "Bearer";
    /**
     * association Service Base URL.
     */
    @Value("${device.association.base.url}")
    private String baseUrl;
    /**
     * association Service API version.
     */
    @Value("${device.association.base.url.version}")
    private String baseVersion;
    /**
     * Rest Template Ref.
     */
    @Autowired
    private RestTemplate restTemplate;

    /**
     * association service Url.
     */
    private String associationServiceUrl;

    /**
     * Retrieves the list of devices associated with the user.
     *
     * @param authHeader the authorization header containing the authentication token
     * @return an Optional containing the list of DeviceAssociation objects,
     *      or an empty Optional if no devices are associated.
     * @throws Exception if an error occurs while retrieving the devices
     */
    @SuppressWarnings("unchecked")
    public Optional<List<DeviceAssociation>> getDevices(String authHeader) throws Exception {
        String authToken = BEARER + Constants.SPACE + authHeader.substring(authHeader.indexOf(Constants.SPACE) + 1);

        try {
            ResponseEntity<String> response = restTemplate.exchange(associationServiceUrl, HttpMethod.GET,
                    new HttpEntity<Object>(createHeaders(authToken)), String.class);

            List<DeviceAssociation> devices = parseJsonAsList(Objects.requireNonNull(response.getBody()),
                    DeviceAssociation.class);

            LOGGER.debug("AssociationServiceClient  associated devices list size: {}",
                    (devices != null ? devices.size() : 0));

            return Optional.of(devices);

        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == (HttpStatus.UNAUTHORIZED)) {
                LOGGER.warn("Association api returned {} error.",
                        (HttpStatus.UNAUTHORIZED).value());
                throw new AuthenticationException("Authentication exception to retrieve user information");
            } else if (ex.getStatusCode() == (HttpStatus.FORBIDDEN)) {
                LOGGER.warn("Association api returned {} error. ",
                        (HttpStatus.UNAUTHORIZED).value());
                throw new AuthorizationException("Unauthorized to retreive user association");
            } else {
                LOGGER.warn("Associated service response code: {}", ex.getStatusCode());
                throw new Exception("Unknown exception while retrieving user associations");
            }

        } catch (Exception e) {
            LOGGER.warn("Associated service error message: {}", e.getMessage());
            throw new Exception("Unknown exception while retrieving user associations");

        }
    }

    /**
     * Parses the given JSON string into a list of objects of the specified class.
     *
     * @param body the JSON string to parse
     * @param clazz the class of the objects in the list
     * @return a list of objects of the specified class
     * @throws IOException if an error occurs during JSON parsing
     */
    private List<DeviceAssociation> parseJsonAsList(String body, Class clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(body.getBytes(),
                mapper.getTypeFactory().constructType(clazz));

    }

    /**
     * Creates and returns the HttpHeaders object with the specified authentication token.
     *
     * @param authToken The authentication token to be added to the headers.
     * @return The HttpHeaders object with the authentication token and content type set.
     */
    private HttpHeaders createHeaders(String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(Constants.AUTH_HEADER_KEY, authToken);
        headers.add(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);
        return headers;
    }

    /**
     * Retrieves the association URL by concatenating the base URL, version, and user associations endpoint.
     * The resulting URL is stored in the associationServiceUrl variable.
     */
    @PostConstruct
    private void getAssociationUrl() {
        associationServiceUrl = baseUrl + Constants.URL_SEPARATOR + baseVersion + USER_ASSOCIATIONS;
    }
}
