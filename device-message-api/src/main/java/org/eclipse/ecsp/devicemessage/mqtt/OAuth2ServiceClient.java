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
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.util.Map;

/**
 * This class represents a client for OAuth2 service.
 * It provides methods to perform OAuth2 token introspection.
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
@Service
public class OAuth2ServiceClient {

    /**
     * Logger Ref.
     */
    private static final IgniteLogger LOGGER = IgniteLoggerFactory.getLogger(OAuth2ServiceClient.class);
    private static ObjectMapper mapper = new ObjectMapper();
    /**
     * OAuth2 Service Base URL.
     */
    @Value("${OAuth2.base.url}")
    private String baseUrl;

    /**
     * Authorization header token to be passed for the introspect api.
     */
    @Value("${OAuth2.basic.auth.header}")
    private String basicAuthHeader;

    /**
     * OAuth2 introspect method.
     */
    @Value("${OAuth2.intropsect.method}")
    private String introspectMethod;

    /**
     * Rest Template Ref.
     */
    @Autowired
    private RestTemplate restTemplate;

    /**
     * The complete url for introspect api.
     */
    private String introspectUrl;

    /**
     * Performs OAuth2 token introspection.
     *
     * @param token The token to be validated.
     * @return A map containing the response from the introspection endpoint.
     */
    public Map doOAuth2Introspect(String token) {
        Map responseMap = null;

        try {
            LOGGER.debug("doOAuth2Introspect: Validating the token: {}", token);
            String requestJson = "token=" + token;
            HttpEntity<String> entity = new HttpEntity(requestJson, createHeaders());
            ResponseEntity<String> response = restTemplate.exchange(introspectUrl, HttpMethod.POST,
                entity, String.class);

            if (response.getStatusCode().equals(HttpStatus.OK)) {
                String responseMsg = response.getBody();
                responseMap = mapper.readValue(responseMsg, Map.class);
                LOGGER.debug("doOAuth2Introspect: post response is {}:", responseMsg);
            } else {
                LOGGER.info(
                    "doOAuth2Introspect: Failed validating the token with response code: {} Response is {}: ",
                    token, response.getStatusCodeValue(), response.getBody());
            }

        } catch (IOException e) {
            LOGGER.error("doOAuth2Introspect: Caught IOException while validating the token: " + token, e);
        }
        return responseMap;
    }

    /**
     * Creates and returns the HTTP headers for the OAuth2ServiceClient.
     *
     * @return the HttpHeaders object containing the necessary headers
     */
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(Constants.AUTH_HEADER_KEY, "Basic " + basicAuthHeader);
        headers.add(Constants.CONTENT_TYPE, Constants.APPLICATION_X_WWW_FORM_URLENCODED);
        return headers;
    }

    /**
     * Constructs the introspect URL by combining the base URL and the introspect method.
     * The constructed URL is stored in the 'introspectUrl' variable.
     */
    @PostConstruct
    private void getIntrospectUrl() {
        introspectUrl = baseUrl + Constants.URL_SEPARATOR + introspectMethod;
    }
}