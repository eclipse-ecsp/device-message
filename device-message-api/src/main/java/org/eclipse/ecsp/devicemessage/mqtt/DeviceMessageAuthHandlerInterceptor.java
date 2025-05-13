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

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.core5.http.HttpHeaders;
import org.eclipse.ecsp.devicemessage.exception.AuthenticationException;
import org.eclipse.ecsp.devicemessage.util.Constants;
import org.eclipse.ecsp.utils.logger.IgniteLogger;
import org.eclipse.ecsp.utils.logger.IgniteLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Interceptor class for handling device message authentication.
 * This class implements the HandlerInterceptor interface and provides methods for pre-handle authentication logic.
 * It validates the authorization header, decrypts the bearer token, and validates the user.
 * The class also provides methods for handling post-handle and after-completion logic, which are not supported
 * and throw UnsupportedOperationException.
 */
@Getter
@SuppressWarnings("checkstyle:MemberName")
public class DeviceMessageAuthHandlerInterceptor implements HandlerInterceptor {

    private static final String BEARER = "Bearer";
    private static final IgniteLogger LOGGER = IgniteLoggerFactory
            .getLogger(DeviceMessageAuthHandlerInterceptor.class);

    @Autowired
    @Setter
    private OAuth2ServiceClient oAuth2ServiceClient;

    @Value("${wso2.tenant.suffix}")
    private String wso2TenantSuffix;

    /*
     * This is added to enable testing through mockito.
     */

    /**
     * This method is called before the execution of a request handler method.
     * It performs authentication and authorization checks based on the provided
     * HTTP Authorization header.
     *
     * @param request  the HttpServletRequest object representing the incoming request
     * @param response the HttpServletResponse object representing the outgoing response
     * @param handler  the handler object that will handle the request
     * @return true if the request is allowed to proceed, false otherwise
     * @throws Exception if an error occurs during authentication or authorization
     *                   or if the Authorization header is missing or in an unsupported format
     */
    @SuppressWarnings("rawtypes")
    @Override
    @Timed(name = "showAll-timed")
    @ExceptionMetered
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        boolean validated = false;
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        LOGGER.debug("APISecurityHandlerInterceptor authorizationHeader {}", authorizationHeader);

        // Check if the HTTP Authorization header is present and formatted
        // correctly
        if (authorizationHeader == null) {
            LOGGER.error("Authorization header must be provided or format is incorrect");
            throw new AuthenticationException("Authorization header must be provided or format is incorrect");
        }

        try {
            String[] splitHeader = authorizationHeader.trim().split(" +");
            if (splitHeader.length != Constants.AUTH_HEADER_NUM_SPLITS) {
                throw new Exception("Unsupported Authorization header: " + authorizationHeader);
            } else if (splitHeader[0].equals(BEARER)) {
                // Ignite1.1 format (Bearer)
                Map decyptedToken = decryptBearerToken(splitHeader[1]);
                validated = (boolean) decyptedToken.get(Constants.ACTIVE);
                if (!validated) {
                    throw new AuthenticationException("invalid authorization token");
                } else {
                    validateBearerUser(request.getRequestURI(), decyptedToken);
                }
            } else {
                throw new Exception("Unsupported Authorization header: " + authorizationHeader);
            }
        } catch (AuthenticationException e) {
            LOGGER.error("Authentication failed : ", e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Authentication failed for token : " + authorizationHeader, e);
            throw new AuthenticationException(
                    "Authentication failed for token: " + authorizationHeader + "." + e.getMessage());
        }
        return validated;
    }

    /**
     * Validates the bearer user by extracting the username from the decrypted token and removing the suffix.
     *
     * @param requestUri The request URI.
     * @param decyptedToken The decrypted token containing the username.
     */
    @SuppressWarnings("rawtypes")
    private void validateBearerUser(String requestUri, Map decyptedToken) {
        String userName = null;
        if (null != decyptedToken.get(Constants.USERNAME)) {
            userName = decyptedToken.get(Constants.USERNAME).toString();
            /*
             * user name will be like - username@carbon.super, remove
             * suffix @carbon.super
             */
            userName = StringUtils.removeEnd(userName, wso2TenantSuffix);
        }
        validateUser(requestUri, userName);
    }

    /**
     * Validates the user by comparing the username from the request URI with the username from the token.
     * If the usernames do not match, an AuthenticationException is thrown.
     *
     * @param requestUri The request URI.
     * @param userNameFromToken The username extracted from the token.
     * @throws AuthenticationException If the username in the request is invalid.
     */
    private void validateUser(String requestUri, String userNameFromToken) {
        String userNameFromUri = getUserName(requestUri);
        LOGGER.debug("username present in request : {} , username present in token :{}", userNameFromUri,
                userNameFromToken);
        if (StringUtils.isNotBlank(userNameFromUri)
                && !StringUtils.equalsIgnoreCase(userNameFromToken, userNameFromUri)) {
            LOGGER.error("Username present in token : {}  not matched with username present in request :{} ",
                    userNameFromToken,
                    userNameFromUri);
            throw new AuthenticationException("Invalid username in the request");
        }
    }

    /**
     * Decrypts the provided bearer token and returns the decrypted information as a map.
     *
     * @param token The bearer token to decrypt.
     * @return A map containing the decrypted information.
     * @throws AuthenticationException If the provided token is null or empty.
     */
    @SuppressWarnings("rawtypes")
    public Map decryptBearerToken(String token) {
        if (StringUtils.isBlank(token)) {
            LOGGER.error("oAuth2Validate: Authorization token is null or empty");
            throw new AuthenticationException("Authorization token is null or empty");
        }
        LOGGER.debug("oAuth2Validate: Validating the token : {}", token);
        return oAuth2ServiceClient.doOAuth2Introspect(token.trim());
    }

    /**
     * Extracts the username from the given URI.
     *
     * @param uri The URI from which to extract the username.
     * @return The extracted username, or null if no username is found.
     */
    private String getUserName(String uri) {
        String userName = null;
        Pattern pattern = Pattern.compile("/users/(.*?)/");
        Matcher matcher = pattern.matcher(uri);
        if (matcher.find()) {
            userName = matcher.group(1);
        }
        return userName;
    }

    /**
     * This method is called after the handler has finished processing the request.
     * It is used to perform any post-processing tasks or cleanup operations.
     *
     * @param arg0  the HttpServletRequest object representing the current request
     * @param arg1 the HttpServletResponse object representing the current response
     * @param arg2  the handler object that was executed
     * @param arg3       the exception (if any) thrown during the execution of the handler
     * @throws Exception if an error occurs during the after completion process
     */
    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
        throw new UnsupportedOperationException();
    }

    /**
     * This method is called after the handler has been executed, but before the view is rendered.
     * It can be used to manipulate the ModelAndView object or perform any post-processing tasks.
     *
     * @param arg0  the HttpServletRequest object
     * @param arg1 the HttpServletResponse object
     * @param arg2  the handler object
     * @param arg3 the ModelAndView object
     * @throws Exception if an exception occurs during post handling
     */
    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
        throw new UnsupportedOperationException();
    }
}