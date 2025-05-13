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

import org.junit.Assert;
import org.junit.Test;

/**
 * ObjectUtils Test class.
 */
public class ObjectUtilsTest {

    @Test
    public void testRequireNonEmpty() {
        String obj = "TestObject";
        String msg = "TestObject is null";
        String actualObj = ObjectUtils.requireNonEmpty(obj, msg);
        Assert.assertEquals(obj, actualObj);

    }

    @Test(expected = RuntimeException.class)
    public void testRequireNonEmptyNeg() {
        String obj = "";
        String msg = "TestObject is null";
        ObjectUtils.requireNonEmpty(obj, msg);
    }
}
