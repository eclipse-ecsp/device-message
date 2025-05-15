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

package org.eclipse.ecsp.devicemessage.service;

import org.eclipse.ecsp.devicemessage.domain.Config;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * DeviceMessageService Test class.
 */
@RunWith(MockitoJUnitRunner.class)
public class DeviceMessageServiceTest {
    private static final long TIMESTAMP = 123L;
    @InjectMocks
    private DeviceMessageService deviceMessageService;

    @Before
    public void setupBefore() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
        // do nothing
    }

    @Test
    public void publishTest() throws Exception {

        Config data1 = new Config();
        data1.setBizTransactionId("bizId");
        data1.setDomain("domain");
        data1.setCommand("command");
        data1.setVersion("version1");
        data1.setData("123data");

        MqttTopic config = MqttTopic.CONFIG;
        String deviceId = "abc";
        DeviceMessageService test = new DeviceMessageService();
        DeviceMessageService spytest = Mockito.spy(test);
        Mockito.doNothing().when(spytest).publishData(Mockito.any(), Mockito.any(), Mockito.any());

        Assertions.assertDoesNotThrow(() -> spytest.publish(config, deviceId, data1, TIMESTAMP));
    }
}
