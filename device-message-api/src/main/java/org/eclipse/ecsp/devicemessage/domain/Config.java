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

package org.eclipse.ecsp.devicemessage.domain;

/**
 * The Config class represents a configuration object.
 */
public class Config {

    private Object data;
    private String domain;
    private String version;
    private String command;
    private long timestamp;
    private String requestId;
    private String bizTransactionId;

    /**
     * Gets the business transaction ID.
     *
     * @return The business transaction ID.
     */
    public String getBizTransactionId() {
        return bizTransactionId;
    }

    /**
     * Sets the business transaction ID.
     *
     * @param bizTransactionId The business transaction ID to set.
     */
    public void setBizTransactionId(String bizTransactionId) {
        this.bizTransactionId = bizTransactionId;
    }

    /**
     * Gets the request ID.
     *
     * @return The request ID.
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Sets the request ID.
     *
     * @param requestId The request ID to set.
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    /**
     * Gets the data.
     *
     * @return The data.
     */
    public Object getData() {
        return data;
    }

    /**
     * Sets the data.
     *
     * @param data The data to set.
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * Gets the domain.
     *
     * @return The domain.
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Sets the domain.
     *
     * @param domain The domain to set.
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * Gets the version.
     *
     * @return The version.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the version.
     *
     * @param version The version to set.
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Gets the command.
     *
     * @return The command.
     */
    public String getCommand() {
        return command;
    }

    /**
     * Sets the command.
     *
     * @param command The command to set.
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * Gets the timestamp.
     *
     * @return The timestamp.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp.
     *
     * @param timestamp The timestamp to set.
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}