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

package org.eclipse.ecsp.devicemessage.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.eclipse.ecsp.devicemessage.exception.AuthenticationException;
import org.eclipse.ecsp.devicemessage.exception.AuthorizationException;
import org.eclipse.ecsp.devicemessage.exception.ExceptionResponse;
import org.eclipse.ecsp.utils.logger.IgniteLogger;
import org.eclipse.ecsp.utils.logger.IgniteLoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * This class serves as the controller advice for handling exceptions in the DeviceMessageController.
 * It provides exception handling methods for various types of exceptions that can occur during the
 * execution of the controller methods.
 */
@RestControllerAdvice
public class DeviceMessageControllerAdvice {

    private static final IgniteLogger LOGGER = IgniteLoggerFactory.getLogger(DeviceMessageControllerAdvice.class);

    /**
     * Handles exceptions thrown by the controller.
     *
     * @param e the exception to handle
     * @return a ResponseEntity containing a string representation of the error message
     */
    @ExceptionHandler
    public ResponseEntity<String> handleExceptions(Exception e) {
        return genericHandle(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles exceptions of type IllegalArgumentException.
     *
     * @param e the IllegalArgumentException to handle
     * @return a ResponseEntity containing a String response and HTTP status code
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleExceptions(IllegalArgumentException e) {
        return genericHandle(e, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles exceptions of type ValidationException.
     *
     * @param e the ValidationException to handle
     * @return a ResponseEntity containing the error message and HTTP status code
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleExceptions(ValidationException e) {
        return genericHandle(e, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles exceptions of type AuthorizationException.
     *
     * @param e The AuthorizationException to handle.
     * @return A ResponseEntity containing a String response and HTTP status code.
     */
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<String> handleExceptions(AuthorizationException e) {
        return genericHandle(e, HttpStatus.FORBIDDEN);
    }

    /**
     * Handles exceptions of type AuthenticationException.
     *
     * @param e the AuthenticationException to handle
     * @return a ResponseEntity containing a String response and HTTP status code
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleExceptions(AuthenticationException e) {
        return genericHandle(e, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles the given exception and returns a ResponseEntity with a JSON message and the specified HTTP status.
     *
     * @param e      the exception to handle
     * @param status the HTTP status to include in the ResponseEntity
     * @return a ResponseEntity containing a JSON message and the specified HTTP status
     */
    private ResponseEntity<String> genericHandle(Exception e, HttpStatus status) {
        LOGGER.error("Exception encountered", e);
        String jsonMessage = "{\"message\":\"" + e.getMessage() + "\"}";
        return new ResponseEntity<>(jsonMessage, status);
    }


    /**
     * Handles ConstraintViolationException and returns a ResponseEntity with an ExceptionResponse.
     *
     * @param ex The ConstraintViolationException to be handled.
     * @return A ResponseEntity containing an ExceptionResponse with the error message.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse> handleConstraintViolationExceptions(
            final ConstraintViolationException ex) {

        StringBuilder builder = new StringBuilder();
        for (ConstraintViolation failure : ex.getConstraintViolations()) {
            builder.append(failure.getMessage());
        }
        ExceptionResponse response = new ExceptionResponse();
        response.setMessage(builder.toString());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}