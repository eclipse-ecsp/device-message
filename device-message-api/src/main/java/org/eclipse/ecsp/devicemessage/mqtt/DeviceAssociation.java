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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Represents a device association.
 * This class contains information about the association between a device and a Harman ID.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceAssociation {

    @JsonProperty("association_id")
    private String associationId;

    private String associationStatus;

    private String harmanId;

    /**
     * Sets the association ID.
     *
     * @param associationId The association ID to set.
     */
    public void setAssociationId(String associationId) {
        this.associationId = associationId;
    }

    /**
     * Sets the association status.
     *
     * @param associationStatus The association status to set.
     */
    public void setAssociationStatus(String associationStatus) {
        this.associationStatus = associationStatus;
    }

    /**
     * Sets the Harman ID.
     *
     * @param harmanId The Harman ID to set.
     */
    public void setHarmanId(String harmanId) {
        this.harmanId = harmanId;
    }

    /**
     * Returns a string representation of the DeviceAssociation object.
     *
     * @return A string representation of the DeviceAssociation object.
     */
    @Override
    public String toString() {
        return "DeviceAssociation [associationId=" + associationId + ", associationStatus=" + associationStatus
                + ", harmanId=" + harmanId
                + "]";
    }

    /**
     * Returns the hash code value for the DeviceAssociation object.
     *
     * @return The hash code value for the DeviceAssociation object.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((associationId == null) ? 0 : associationId.hashCode());
        result = prime * result + ((associationStatus == null) ? 0 : associationStatus.hashCode());
        result = prime * result + ((harmanId == null) ? 0 : harmanId.hashCode());
        return result;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param obj The reference object with which to compare.
     * @return true if this object is the same as the obj argument; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DeviceAssociation other = (DeviceAssociation) obj;
        if (associationId == null) {
            if (other.associationId != null) {
                return false;
            }
        } else if (!associationId.equals(other.associationId)) {
            return false;
        }
        if (associationStatus == null) {
            if (other.associationStatus != null) {
                return false;
            }
        } else if (!associationStatus.equals(other.associationStatus)) {
            return false;
        }
        if (harmanId == null) {
            return other.harmanId == null;
        } else {
            return harmanId.equals(other.harmanId);
        }
    }
}