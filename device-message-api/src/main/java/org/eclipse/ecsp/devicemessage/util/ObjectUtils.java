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

import java.util.Objects;

/**
 * The ObjectUtils class provides utility methods for working with objects.
 */
public class ObjectUtils {
    private ObjectUtils() {

    }

    /**
     * Checks if the given object is non-empty.
     *
     * @param obj      the object to check
     * @param errorMsg the error message to be thrown if the object is empty
     * @param <T>      the type of the object
     * @return the non-empty object
     * @throws RuntimeException if the object is empty
     */
    public static <T> T requireNonEmpty(T obj, String errorMsg) {
        Objects.requireNonNull(obj, errorMsg);
        if (obj instanceof String str && (str.isEmpty())) {
            throw new RuntimeException(errorMsg);
        }
        return obj;
    }
}