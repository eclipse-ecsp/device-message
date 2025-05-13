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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.core5.http.HttpHeaders;
import org.eclipse.ecsp.devicemessage.exception.AssociationException;
import org.eclipse.ecsp.devicemessage.util.Constants;
import org.eclipse.ecsp.utils.logger.IgniteLogger;
import org.eclipse.ecsp.utils.logger.IgniteLoggerFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Interceptor for handling device association requests.
 * This class implements the HandlerInterceptor interface and provides methods for pre-handle
 * and after-completion actions.
 * It checks if the device ID in the request URL is associated with the user.
 */
public class DeviceAssociationHandlerInterceptor implements HandlerInterceptor {

    private static final IgniteLogger LOGGER = IgniteLoggerFactory.getLogger(DeviceAssociationHandlerInterceptor.class);

    @Autowired
    private DeviceAssociationServiceClient associationServiceClient;

    /**
     * Extracts the device ID from the given URI.
     *
     * @param uri The URI from which to extract the device ID.
     * @return The extracted device ID, or null if no device ID is found.
     */
    private String getDeviceId(String uri) {
        String subStr = null;
        Pattern pattern = Pattern.compile("/devices/(.*?)/");
        Matcher matcher = pattern.matcher(uri);
        if (matcher.find()) {
            subStr = matcher.group(1);
        }
        return subStr;
    }

    /**
     * This method is called before the execution of a request handler method.
     * It checks if the request contains a valid device ID in the URL and verifies the association.
     *
     * @param request  the HttpServletRequest object representing the incoming request
     * @param response the HttpServletResponse object representing the outgoing response
     * @param handler  the handler object that will handle the request
     * @return true if the request should continue to be processed, false otherwise
     * @throws Exception if an error occurs during the preHandle process
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {

        // Example uri is
        String uri = request.getRequestURI();
        String deviceId = getDeviceId(uri);

        if (null == deviceId) {
            LOGGER.warn("No device id in the URL to check the association.");
            return true;
        } else {
            return associationExists(request.getHeader(HttpHeaders.AUTHORIZATION), deviceId);
        }
    }

    /**
     * Checks if an association exists between a user and a device.
     *
     * @param authorizationHeader The authorization header containing the user's credentials.
     * @param deviceId The ID of the device to check association for.
     * @return {@code true} if the device is associated with the user, {@code false} otherwise.
     * @throws Exception If there is an error while checking the association.
     */
    protected boolean associationExists(String authorizationHeader, String deviceId) throws Exception {
        // getting all devices for a user
        List<DeviceAssociation> devices =
            associationServiceClient.getDevices(authorizationHeader).orElse(Collections.emptyList());

        for (DeviceAssociation device : devices) {
            if (StringUtils.equals(device.getHarmanId(), deviceId)
                && StringUtils.equals(device.getAssociationStatus(), Constants.ASSOCIATED)) {
                LOGGER.debug("deviceId {} is associated with the user", deviceId);
                return true;
            }
        }
        LOGGER.error("deviceId {} is not associated with the user", deviceId);
        throw new AssociationException(String.format("deviceId : %s is not associated with the user", deviceId));
    }

    /**
     * This method is called after the completion of a request handling process.
     * It is invoked after the DispatcherServlet has rendered the view.
     * This method can be used for resource cleanup or logging purposes.
     *
     * @param arg0 The HttpServletRequest object representing the current request.
     * @param arg1 The HttpServletResponse object representing the current response.
     * @param arg2 The handler object that was used to handle the request.
     * @param arg3 An Exception object representing any exception that occurred during the request handling process.
     * @throws Exception if an exception occurs during the cleanup process.
     */
    @Override
    public void afterCompletion(@NotNull HttpServletRequest arg0,
                                @NotNull HttpServletResponse arg1, @NotNull Object arg2, Exception arg3)
        throws Exception {
        throw new UnsupportedOperationException();
    }

    /**
     * This method is called after the handler has been executed, allowing the interceptor to
     * post-process the request and response. This method is typically used for cleanup tasks
     * or modifying the response before it is sent back to the client.
     *
     * @param arg0  the current HTTP request
     * @param arg1 the current HTTP response
     * @param arg2  the handler (controller method) that was executed
     * @param arg3 the ModelAndView object that represents the view and model for the response
     * @throws Exception if an error occurs during post-processing
     */
    @Override
    public void postHandle(@NotNull HttpServletRequest arg0,
                           @NotNull HttpServletResponse arg1, @NotNull Object arg2, ModelAndView arg3)
        throws Exception {
        throw new UnsupportedOperationException();
    }
}
