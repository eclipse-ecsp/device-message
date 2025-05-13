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

import org.eclipse.ecsp.devicemessage.util.Constants;
import org.junit.Assert;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

/**
 * DeviceShadow Test Class.
 */
public class DeviceShadowTest {

    @Test
    public void testEqualObjObj() {
        DeviceShadow deviceShadow1 = getDeviceShadow();
        Assert.assertEquals(deviceShadow1, deviceShadow1);
    }

    @Test
    public void testEqualObj1Null() {
        DeviceShadow deviceShadow1 = getEmptyDeviceShadow();
        Assert.assertNotEquals(true, deviceShadow1.equals(null));
    }

    @Test
    public void testEqualObjDiff() {
        DeviceShadow deviceShadow1 = getEmptyDeviceShadow();
        Assert.assertNotEquals(true, deviceShadow1.equals(new Object()));
    }

    @Test
    public void testEqualEmptyEmpty() {
        DeviceShadow deviceShadow1 = getEmptyDeviceShadow();
        DeviceShadow deviceShadow2 = getEmptyDeviceShadow();
        Assert.assertEquals(deviceShadow1, deviceShadow2);
    }

    @Test
    public void testEqualObjEmpty() {
        DeviceShadow deviceShadow1 = getDeviceShadow();
        Assert.assertNotEquals(true, deviceShadow1.equals(getEmptyDeviceShadow()));
    }

    @Test
    public void testEqualObj1Obj2() {
        DeviceShadow deviceShadow1 = getDeviceShadow();
        DeviceShadow deviceShadow2 = getDeviceShadow();
        Assert.assertEquals(deviceShadow1, deviceShadow2);
    }

    @Test
    public void testEqualIdNull() {
        DeviceShadow deviceShadow1 = getEmptyDeviceShadow();
        DeviceShadow deviceShadow2 = getEmptyDeviceShadow();
        deviceShadow2.setId("NotNUll");
        Assert.assertNotEquals(true, deviceShadow1.equals(deviceShadow2));
    }

    @Test
    public void testEqualIdsDifferent() {
        DeviceShadow deviceShadow1 = getEmptyDeviceShadow();
        DeviceShadow deviceShadow2 = getEmptyDeviceShadow();
        deviceShadow1.setId("Id1");
        deviceShadow2.setId("Id2");
        Assert.assertNotEquals(true, deviceShadow1.equals(deviceShadow2));
    }

    @Test
    public void testEqualPayloadNull() {
        DeviceShadow deviceShadow1 = getEmptyDeviceShadow();
        DeviceShadow deviceShadow2 = getEmptyDeviceShadow();
        deviceShadow1.setPayload(null);
        Assert.assertNotEquals(true, deviceShadow1.equals(deviceShadow2));
    }

    @Test
    public void testEqualPdidNull() {
        DeviceShadow deviceShadow1 = getEmptyDeviceShadow();
        DeviceShadow deviceShadow2 = getEmptyDeviceShadow();
        deviceShadow1.setPdid(null);
        deviceShadow2.setPdid("pdid2");
        Assert.assertNotEquals(true, deviceShadow1.equals(deviceShadow2));
    }

    @Test
    public void testEqualPdidDifferent() {
        DeviceShadow deviceShadow1 = getEmptyDeviceShadow();
        DeviceShadow deviceShadow2 = getEmptyDeviceShadow();
        deviceShadow1.setPdid("Pdid1");
        deviceShadow2.setPdid("Pdid2");
        Assert.assertNotEquals(true, deviceShadow1.equals(deviceShadow2));
    }

    @Test
    public void testEqualTimestampDiffrent() {
        DeviceShadow deviceShadow1 = getEmptyDeviceShadow();
        DeviceShadow deviceShadow2 = getEmptyDeviceShadow();
        deviceShadow2.setUploadTimeStamp(System.currentTimeMillis());
        Assert.assertNotEquals(true, deviceShadow1.equals(deviceShadow2));
    }

    @Test
    public void testHashcodeSame() {

        DeviceShadow deviceShadow1 = getDeviceShadow();
        DeviceShadow deviceShadow2 = getDeviceShadow();
        deviceShadow1.toString();
        Assert.assertEquals(deviceShadow1.hashCode(), deviceShadow2.hashCode());
    }


    @Test
    public void testHashcodeDifferent() {
        DeviceShadow deviceShadow1 = getDeviceShadow();
        deviceShadow1.setId(null);
        deviceShadow1.setDeleted(true);
        DeviceShadow deviceShadow2 = getDeviceShadow();
        deviceShadow1.setPayload(null);
        deviceShadow1.setPdid(null);
        Assert.assertNotEquals(true, deviceShadow1.equals(deviceShadow2));
    }

    private DeviceShadow getEmptyDeviceShadow() {
        return new DeviceShadow();
    }

    private DeviceShadow getDeviceShadow() {
        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put("ShadowKey1", "ShadowValue1");
        payload.put("domain", "SpeedLimitAlert");
        return new DeviceShadow("HU1234567890", 0, payload, false,
            (String) payload.get(Constants.DOMAIN));
    }

    @Test
    public void testDeviceShadowObject() throws Exception {
        try {
            DeviceShadow deviceShadow = getDeviceShadow();
            deviceShadow.setId("101");
            deviceShadow.setPayloadDomain("test");
            Assert.assertEquals(0, deviceShadow.getUploadTimeStamp());
            Assert.assertFalse(deviceShadow.isDeleted());
            Assert.assertNotNull(deviceShadow.getPayload());
            Assert.assertNotNull(deviceShadow.getPayloadDomain());
            Assert.assertEquals("test", deviceShadow.getPayloadDomain());
            Assert.assertEquals("101", deviceShadow.getId());
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }

}

