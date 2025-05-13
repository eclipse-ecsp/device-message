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

package org.eclipse.ecsp.devicemessage.testsuite;

import org.eclipse.ecsp.devicemessage.controller.DeviceMessageControllerTest;
import org.eclipse.ecsp.devicemessage.entities.DeviceShadowTest;
import org.eclipse.ecsp.devicemessage.mqtt.DeviceAssociationTest;
import org.eclipse.ecsp.devicemessage.util.ObjectUtilsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test Suite Class.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    DeviceMessageControllerTest.class, DeviceAssociationTest.class, ObjectUtilsTest.class, DeviceShadowTest.class
})
public class TestSuite {
}

