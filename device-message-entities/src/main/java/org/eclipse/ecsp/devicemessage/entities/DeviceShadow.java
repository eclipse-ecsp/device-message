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

package org.eclipse.ecsp.devicemessage.entities;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Field;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Index;
import dev.morphia.annotations.IndexOptions;
import dev.morphia.annotations.Indexes;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.ecsp.entities.AbstractIgniteEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a device shadow entity.
 * This class is used to store information about a device shadow.
 */
@Getter
@Setter
@Entity("deviceshadow")
@Indexes({@Index(options = @IndexOptions(name = "deviceshadow_compound_index_1"),
        fields = {@Field("pdid"), @Field("payloadDomain")})
})
public class DeviceShadow extends AbstractIgniteEntity {

    private static final int HASH_CODE_PRIME_NUMBER = 31;
    private static final int HASH_CODE_RESULT_NUMBER = 1;
    private static final int TIMESTAMP_COMPARISON_NUMBER = 32;
    private static final int IS_DELETED_TRUE_NUMBER = 1231;
    private static final int IS_DELETED_FALSE_NUMBER = 1237;


    /**
     * mongo _id.
     */
    @Id
    private String id;

    /**
     * device id.
     */
    private String pdid;

    /**
     * upload TimeStamp.
     */
    private long uploadTimeStamp;

    /**
     * isDeleted.
     */
    private boolean isDeleted;

    /**
     * payload.
     */

    private Map<String, Object> payload = new HashMap<>();

    /**
     * domain.
     */

    private String payloadDomain;

    /**
     * super constructor.
     */
    public DeviceShadow() {
        super();
    }

    /**
     * collection constructor.
     *
     * @param pdid            pdid.
     * @param uploadTimeStamp upload time stamp.
     * @param payload         payload.
     */
    public DeviceShadow(String pdid, long uploadTimeStamp, Map<String, Object> payload, boolean isDeleted,
                        String payloadDomain) {
        super();
        this.pdid = pdid;
        this.uploadTimeStamp = uploadTimeStamp;
        this.payload = payload;
        this.isDeleted = isDeleted;
        this.payloadDomain = payloadDomain;
    }

    /**
     * Returns the hash code value for this object.
     * The hash code is calculated based on the object's id, isDeleted flag, payload, pdid, and uploadTimeStamp.
     *
     * @return the hash code value for this object
     */
    @Override
    public int hashCode() {
        final int prime = HASH_CODE_PRIME_NUMBER;
        int result = HASH_CODE_RESULT_NUMBER;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + (isDeleted ? IS_DELETED_TRUE_NUMBER : IS_DELETED_FALSE_NUMBER);
        result = prime * result + ((payload == null) ? 0 : payload.hashCode());
        result = prime * result + ((pdid == null) ? 0 : pdid.hashCode());
        result = prime * result + (int) (uploadTimeStamp ^ (uploadTimeStamp >>> TIMESTAMP_COMPARISON_NUMBER));
        return result;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param obj the reference object with which to compare
     * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise
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
        DeviceShadow other = (DeviceShadow) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (isDeleted != other.isDeleted) {
            return false;
        }
        if (payload == null) {
            if (other.payload != null) {
                return false;
            }
        } else if (!payload.equals(other.payload)) {
            return false;
        }
        if (pdid == null) {
            if (other.pdid != null) {
                return false;
            }
        } else if (!pdid.equals(other.pdid)) {
            return false;
        }
        return uploadTimeStamp == other.uploadTimeStamp;
    }

    /**
     * Returns a string representation of the DeviceShadow object.
     *
     * @return A string representation of the DeviceShadow object.
     */
    @Override
    public String toString() {
        return "DeviceShadow [id=" + id + ", pdid=" + pdid + ", uploadTimeStamp=" + uploadTimeStamp + ", isDeleted="
                + isDeleted
                + ", payload=" + payload + "]";
    }
}