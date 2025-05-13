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
 * This class contains constant values used in the application.
 */
public final class Constants {

    /**
     * The number of splits in the authorization header.
     */
    public static final int AUTH_HEADER_NUM_SPLITS = 2;

    /**
     * The value for "active".
     */
    public static final String ACTIVE = "active";

    /**
     * The value for "username".
     */
    public static final String USERNAME = "username";

    /**
     * The key for the authorization header.
     */
    public static final String AUTH_HEADER_KEY = "Authorization";

    /**
     * The value for "application/x-www-form-urlencoded".
     */
    public static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";

    /**
     * The key for the content type.
     */
    public static final String CONTENT_TYPE = "Content-Type";

    /**
     * The URL separator.
     */
    public static final String URL_SEPARATOR = "/";

    /**
     * The key for the timestamp.
     */
    public static final String TIMESTAMP = "timestamp";

    /**
     * The key for the data.
     */
    public static final String DATA = "data";

    /**
     * The key for the version.
     */
    public static final String VERSION = "version";

    /**
     * The key for the command.
     */
    public static final String COMMAND = "command";

    /**
     * The key for the domain.
     */
    public static final String DOMAIN = "domain";

    /**
     * The key for the BizTransactionId.
     */
    public static final String BIZ_TRANSACTION_ID = "BizTransactionId";

    /**
     * The value for "ASSOCIATED".
     */
    public static final String ASSOCIATED = "ASSOCIATED";

    /**
     * The value for "application/json".
     */
    public static final String APPLICATION_JSON = "application/json";

    /**
     * The space constant.
     */
    public static final String SPACE = " ";

    /**
     * The key for the MongoDB username.
     */
    public static final String VAULT_MONGO_USERNAME_KEY = "username";

    /**
     * The key for the MongoDB password.
     */
    public static final String VAULT_MONGO_PASS_KEY = "password";

    /**
     * The key for the MongoDB lease duration.
     */
    public static final String VAULT_MONGO_LEASE_DURATION = "lease_duration";

    /**
     * Private constructor to prevent instantiation of the class.
     */
    private Constants() {

    }
}