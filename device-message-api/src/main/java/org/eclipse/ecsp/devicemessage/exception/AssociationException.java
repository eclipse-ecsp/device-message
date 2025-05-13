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

package org.eclipse.ecsp.devicemessage.exception;

import java.io.Serial;

/**
 * Exception thrown when there is an error related to association in the application.
 */
public class AssociationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 2780754371832899272L;

    /**
     * Constructs a new AssociationException with the specified detail message.
     *
     * @param message the detail message
     */
    public AssociationException(String message) {
        super(message);
    }

    /**
     * Constructs a new AssociationException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public AssociationException(String message, Throwable cause) {
        super(message, cause);
    }
}