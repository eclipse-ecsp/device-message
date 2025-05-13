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

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import java.util.concurrent.ExecutionException;

/**
 * The MqttBaseService interface provides methods for publishing data to an MQTT topic and
 * getting the MQTT Quality of Service (QoS) level.
 */
public interface MqttBaseService {

    /**
     * Publishes data to the specified MQTT topic.
     *
     * @param topic     The MQTT topic to publish the data to.
     * @param deviceId  The ID of the device.
     * @param message   The MQTT message to publish.
     * @throws InterruptedException If the current thread is interrupted while waiting.
     * @throws ExecutionException   If an error occurs during the execution of the publish operation.
     * @throws MqttException        If an error occurs while publishing the data.
     */
    public void publishData(MqttTopic topic, String deviceId, MqttMessage message)
        throws InterruptedException, ExecutionException, MqttException;

    /**
     * Gets the MQTT Quality of Service (QoS) level.
     *
     * @return The MQTT QoS level.
     */
    public int getMqttQos();
}
