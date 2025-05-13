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

import org.eclipse.ecsp.utils.logger.IgniteLogger;
import org.eclipse.ecsp.utils.logger.IgniteLoggerFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * DeviceAssociation Test class.
 */
public class DeviceAssociationTest {
    private static final IgniteLogger LOGGER = IgniteLoggerFactory.getLogger(DeviceAssociationTest.class);
    private static final String DEVICE_ID = "HADEFO67845OU";

    @Test
    public void testEqual() {

        DeviceAssociation deviceAssociation1 = new DeviceAssociation();
        deviceAssociation1.setAssociationId("1");
        deviceAssociation1.setHarmanId(DEVICE_ID);
        deviceAssociation1.setAssociationStatus("ASSOCIATED");

        DeviceAssociation deviceAssociation2 = new DeviceAssociation();
        deviceAssociation2.setAssociationId("1");
        deviceAssociation2.setHarmanId(DEVICE_ID);
        deviceAssociation2.setAssociationStatus("ASSOCIATED");

        Assert.assertEquals(deviceAssociation1.getAssociationId(), deviceAssociation2.getAssociationId());
        Assert.assertEquals(deviceAssociation1.getHarmanId(), deviceAssociation2.getHarmanId());
        Assert.assertEquals(deviceAssociation1.getAssociationStatus(), deviceAssociation2.getAssociationStatus());

        Assert.assertEquals(deviceAssociation1, deviceAssociation1);
        Assert.assertNotEquals(true, deviceAssociation1.equals(null));

        Assert.assertNotEquals(true, deviceAssociation1.equals("DifferentClass"));

        Assert.assertEquals(deviceAssociation1, deviceAssociation2);

        deviceAssociation1.setAssociationId(null);
        Assert.assertNotEquals(true, deviceAssociation1.equals(deviceAssociation2));

        deviceAssociation1.setAssociationId("Different");
        Assert.assertNotEquals(true, deviceAssociation1.equals(deviceAssociation2));
        deviceAssociation1.setAssociationId("1");

        deviceAssociation1.setAssociationStatus(null);
        Assert.assertNotEquals(true, deviceAssociation1.equals(deviceAssociation2));

        deviceAssociation1.setAssociationStatus("different");
        Assert.assertNotEquals(true, deviceAssociation1.equals(deviceAssociation2));

        deviceAssociation1.setAssociationStatus("ASSOCIATED");
        deviceAssociation1.setHarmanId(null);
        Assert.assertNotEquals(true, deviceAssociation1.equals(deviceAssociation2));

        deviceAssociation1.setHarmanId("Different");
        Assert.assertNotEquals(true, deviceAssociation1.equals(deviceAssociation2));
    }

    @Test
    public void testHashcode() {
        DeviceAssociation deviceAssociation1 = new DeviceAssociation();
        deviceAssociation1.setAssociationId("1");
        deviceAssociation1.setHarmanId(DEVICE_ID);
        deviceAssociation1.setAssociationStatus("ASSOCIATED");

        DeviceAssociation deviceAssociation2 = new DeviceAssociation();
        deviceAssociation2.setAssociationId("1");
        deviceAssociation2.setHarmanId(DEVICE_ID);
        deviceAssociation2.setAssociationStatus("ASSOCIATED");

        Assert.assertEquals(deviceAssociation1.hashCode(), deviceAssociation2.hashCode());

        deviceAssociation2.setAssociationId("2");
        Assert.assertNotEquals(true, deviceAssociation1.hashCode() == deviceAssociation2.hashCode());
    }

    @Test
    public void testIfBothDeviceAssociationObjectshaveEqualValues() {
        DeviceAssociation deviceAssociation1 = new DeviceAssociation();
        deviceAssociation1.setAssociationId("1");
        deviceAssociation1.setHarmanId(DEVICE_ID);
        deviceAssociation1.setAssociationStatus("ASSOCIATED");
        DeviceAssociation deviceAssociation2 = new DeviceAssociation();
        deviceAssociation2.setAssociationId("1");
        deviceAssociation2.setHarmanId(DEVICE_ID);
        deviceAssociation2.setAssociationStatus("ASSOCIATED");
        Assert.assertEquals(deviceAssociation1, deviceAssociation2);
    }

    @Test
    public void testEqualDeviceAssociationObjectsDiff() {
        DeviceAssociation deviceAssociation1 = new DeviceAssociation();
        Assert.assertNotEquals(deviceAssociation1, new Object());
    }

    @Test
    public void testIfBothDeviceAssociationObjectsAreEmpty() {
        DeviceAssociation deviceAssociation1 = new DeviceAssociation();
        DeviceAssociation deviceAssociation2 = new DeviceAssociation();
        Assert.assertEquals(deviceAssociation1, deviceAssociation2);
    }

    @Test
    public void testSameHashcode() {

        DeviceAssociation deviceAssociation1 = new DeviceAssociation();
        DeviceAssociation deviceAssociation2 = new DeviceAssociation();
        System.out.println(deviceAssociation1.hashCode() == deviceAssociation2.hashCode());
        Assert.assertEquals(deviceAssociation1.hashCode(), deviceAssociation2.hashCode());
    }

    @Test
    public void testDifferentHashcode() {

        DeviceAssociation deviceAssociation1 = new DeviceAssociation();
        deviceAssociation1.setAssociationId("1");
        deviceAssociation1.setHarmanId(DEVICE_ID);
        DeviceAssociation deviceAssociation2 = new DeviceAssociation();
        deviceAssociation1.setAssociationStatus("ASSOCIATED");
        LOGGER.info("deviceAssociation1 {}", deviceAssociation1);
        LOGGER.info("deviceAssociation2 {}", deviceAssociation2);
        Assert.assertNotEquals(true, deviceAssociation1.hashCode() == deviceAssociation2.hashCode());
    }
}

