/** ******************************************************************************
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

package org.eclipse.ecsp.devicemessage.controller;

import com.codahale.metrics.annotation.Counted;
import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.ecsp.devicemessage.annotation.ApiVersion1;
import org.eclipse.ecsp.devicemessage.annotation.ValidDeviceId;
import org.eclipse.ecsp.devicemessage.domain.Config;
import org.eclipse.ecsp.devicemessage.service.DeviceMessageService;
import org.eclipse.ecsp.devicemessage.service.DeviceShadowService;
import org.eclipse.ecsp.devicemessage.service.MqttTopic;
import org.eclipse.ecsp.security.Security;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


/**
 * This class is the controller for handling device message operations.
 * It provides endpoints for publishing messages to MQTT and saving device shadow.
 */
@RestController
@Validated
@RequestMapping(value = "/{version}/devices")
public class DeviceMessageController {

    @Autowired
    private DeviceMessageService mqttService;

    @Autowired
    private DeviceShadowService deviceShadowService;

    /**
     * publish messgae.
     *
     * @param deviceId deviceId.
     * @param version version.
     * @param message message.
     * @throws MqttException mqttException.
     * @throws ExecutionException executionException.
     * @throws JsonProcessingException jsonException.
     * @throws InterruptedException interruptedException.
     */
    @Timed(name = "showAll-timed")
    @ExceptionMetered
    @Counted(name = "showAll-counted")
    @PostMapping(value = "/{device_id}/config", consumes = MediaType.APPLICATION_JSON_VALUE)

    @Operation(summary = "POST /{version}/devices/{deviceId}/config", description = "API used to send messages to MQTT",
        responses = { @ApiResponse(responseCode = "200", description = "Success",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                array = @ArraySchema(schema = @Schema(implementation = Config.class))))})
    @SecurityRequirement(name = Security.JWT_AUTH_VALIDATOR, scopes = {"DeviceMessaging"})
    public void publishMessage(@ValidDeviceId @PathVariable("device_id") String deviceId,
                               @ApiVersion1 @PathVariable("version") String version,
                               @RequestBody Config message)
        throws MqttException, ExecutionException, JsonProcessingException, InterruptedException {

        long timestamp = System.currentTimeMillis();
        mqttService.publish(MqttTopic.CONFIG, deviceId, message, timestamp);

        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put("domain", message.getDomain());
        msgMap.put("command", message.getCommand());
        if (StringUtils.isNotEmpty(message.getVersion())) {
            msgMap.put("version", message.getVersion());
        }
        if (message.getData() != null) {
            msgMap.put("data", message.getData());
        }
        if (StringUtils.isNotEmpty(message.getBizTransactionId())) {
            msgMap.put("BizTransactionId", message.getBizTransactionId());
        }

        msgMap.put("timestamp", timestamp);
        deviceShadowService.save(deviceId, msgMap);
    }
}