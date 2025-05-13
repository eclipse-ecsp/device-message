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

import org.eclipse.ecsp.devicemessage.dao.impl.DeviceShadowRepositoryImpl;
import org.eclipse.ecsp.devicemessage.entities.DeviceShadow;
import org.eclipse.ecsp.devicemessage.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;

/**
 * This class represents a service for managing device shadows.
 * It provides methods for saving device shadows.
 */
@Service
public class DeviceShadowService {

    @Autowired
    DeviceShadowRepositoryImpl deviceShadowRepository;

    /**
     * Saves the device shadow for the specified device ID and payload.
     *
     * @param pdid    the device ID
     * @param payload the payload containing the device shadow data
     */
    public void save(String pdid, Map<String, Object> payload) {
        DeviceShadow deviceShadow = new DeviceShadow(pdid, System.currentTimeMillis(), payload, false,
            (String) payload.get(Constants.DOMAIN));
        deviceShadowRepository.save(deviceShadow);
    }
}