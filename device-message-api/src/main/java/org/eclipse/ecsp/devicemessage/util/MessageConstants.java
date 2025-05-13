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

package org.eclipse.ecsp.devicemessage.util;

/**
 * This class contains constant values used in the device message API.
 */
public final class MessageConstants {

    /**
     * Invalid Param.
     */
    public static final String INVALID_PARAM_VALUE = "Invalid parameter value : ";

    /**
     * Invalid device-id.
     */
    public static final String INVALID_DEVICE_ID_MSG = "Received invalid device id in URI";

    /**
     * Unsupported api version.
     */
    public static final String UNSUPPORTED_API_VERSION_MSG = "Unsupported api version received in URI";

    private MessageConstants() {

    }
}