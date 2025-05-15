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

package org.eclipse.ecsp.devicemessage.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.moquette.broker.Server;
import org.apache.commons.io.IOUtils;
import org.eclipse.ecsp.devicemessage.exception.ExceptionResponse;
import org.eclipse.ecsp.devicemessage.mqtt.DeviceAssociation;
import org.eclipse.ecsp.devicemessage.mqtt.DeviceAssociationHandlerInterceptor;
import org.eclipse.ecsp.devicemessage.mqtt.DeviceAssociationServiceClient;
import org.eclipse.ecsp.devicemessage.service.DeviceMessageService;
import org.eclipse.ecsp.testutils.CommonTestBase;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * DeviceMessage Controller Test Class.
 */
@SuppressWarnings("checkstyle:MemberName")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@TestPropertySource(properties = {"spring.profiles.active = test"})
public class DeviceMessageControllerTest extends CommonTestBase {

    private static Server embedMQTTServer;
    @InjectMocks
    @Autowired
    DeviceAssociationHandlerInterceptor deviceAssociationHandlerInterceptor;
    @Autowired
    private TestRestTemplate restTemplate;
    @Mock
    private DeviceAssociationServiceClient deviceAssociationServiceClient;

    /**
     * Before Class.
     *
     * @throws IOException   ioException
     * @throws MqttException mqttException.
     */
    @BeforeClass
    public static void setupClass() throws IOException, MqttException {
        embedMQTTServer = new Server();
        Properties configProps = new Properties();
        readProperties(configProps);
        embedMQTTServer.startServer(configProps);
    }

    /**
     * Read mqtt properties.
     *
     * @param props properties.
     * @throws IOException ioException.
     */
    private static void readProperties(Properties props) throws IOException {
        String mqttPropertiesFile = "moquette.conf";
        InputStream inputStream =
            DeviceMessageControllerTest.class.getClassLoader().getResourceAsStream(mqttPropertiesFile);
        if (null != inputStream) {
            props.load(inputStream);
        } else {
            throw new FileNotFoundException("mqtt Property file : " + mqttPropertiesFile + " not found");
        }
    }

    @AfterClass
    public static void teardownClass() {
        embedMQTTServer.stopServer();
    }

    /**
     * Before method.
     *
     * @throws Exception ex.
     */
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        restTemplate.getRestTemplate().setErrorHandler(new DefaultResponseErrorHandler());
        Mockito.when(deviceAssociationServiceClient.getDevices(Mockito.anyString()))
            .thenReturn(getDeviceAssociationList());
    }

    /*@Test
    public void testDeviceMessageThroughOauth() {
        String deviceId = "HADEFO67845OU";
        HttpHeaders httpHeaders = this.createHeaderForOuth();

        try {

            String requestJson =
                IOUtils.toString(DeviceMessageControllerTest.class.getResourceAsStream("/input-data.json"),
                    "UTF-8");
            ResponseEntity<String> response = restTemplate.exchange("/v1/devices/" + deviceId + "/config",
                HttpMethod.POST, new HttpEntity<Object>(requestJson, httpHeaders), String.class);
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
        } catch (Exception e) {
            assertTrue(false);
        }

    }*/

    @Test(expected = HttpClientErrorException.class)
    public void testDeviceMessageInvalidDeviceId() throws IOException {
        String deviceId = "HADEFO67845O-";
        HttpHeaders httpHeaders = this.createHeaderForOuth();
        String requestJson = IOUtils.toString(DeviceMessageControllerTest.class.getResourceAsStream("/input-data.json"),
            "UTF-8");
        ResponseEntity<ExceptionResponse> response = restTemplate.exchange("/v1/devices/" + deviceId + "/config",
            HttpMethod.POST, new HttpEntity<Object>(requestJson, httpHeaders), ExceptionResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());

    }

    /*@Test
    public void testDeviceMessageForDelete() throws IOException {
        String deviceId = "HADEFO678459";
        HttpHeaders httpHeaders = this.createHeaderForOuth();
        String requestJson =
            IOUtils.toString(DeviceMessageControllerTest.class.getResourceAsStream("/input-data-delete.json"),
                "UTF-8");
        ResponseEntity<ExceptionResponse> response = restTemplate.exchange("/v1/devices/" + deviceId + "/config",
            HttpMethod.POST, new HttpEntity<Object>(requestJson, httpHeaders), ExceptionResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }*/

    @Test(expected = HttpClientErrorException.class)
    public void testDeviceMessageInvalidVersion() throws IOException {
        String deviceId = "HADEFO67845O";
        HttpHeaders httpHeaders = this.createHeaderForOuth();
        String requestJson = IOUtils.toString(DeviceMessageControllerTest.class.getResourceAsStream("/input-data.json"),
            "UTF-8");
        ResponseEntity<ExceptionResponse> response = restTemplate.exchange("/v12/devices/" + deviceId + "/config",
            HttpMethod.POST, new HttpEntity<Object>(requestJson, httpHeaders), ExceptionResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    /**
     * create Header.
     *
     * @return HttpHeaders.
     */
    private HttpHeaders createHeaderForOuth() {
        String token = "Sampletoken";
        HttpHeaders headers = new HttpHeaders() {
            {
                set("Authorization", "Bearer " + token);
            }
        };
        headers.add("Content-Type", "application/json");
        return headers;
    }

    /**
     * Get IntrospectResponse.
     *
     * @param valid valid.
     * @return Map.
     * @throws JsonProcessingException jsonException.
     * @throws IOException             ioException.
     */
    private Map getIntrospectResponse(boolean valid) throws JsonProcessingException, IOException {
        String response = "";
        if (valid) {
            response =
                "{\"active\":true,\"token_type\":\"Bearer\",\"exp\":1506409315,"
                    + "\"iat\":1506405715,\"client_id\":\"DBVtCiAlROF_8KUR9ExPlZa8Whoa\",\"username\":\"x@y.z\"}";
        } else {
            response = "{\"active\":false}";
        }
        return new ObjectMapper().readValue(response, Map.class);
    }

    private Optional<List<DeviceAssociation>> getDeviceAssociationList() {
        DeviceAssociation deviceAssociation1 = new DeviceAssociation();
        deviceAssociation1.setAssociationId("1");
        List<DeviceAssociation> associationList = new ArrayList<>();
        deviceAssociation1.setHarmanId("HADEFO67845OU");
        deviceAssociation1.setAssociationStatus("ASSOCIATED");
        associationList.add(deviceAssociation1);

        DeviceAssociation deviceAssociation2 = new DeviceAssociation();
        deviceAssociation2.setAssociationId("2");
        deviceAssociation2.setHarmanId("HU4U7AVVDI4P14");
        deviceAssociation2.setAssociationStatus("ASSOCIATED");
        associationList.add(deviceAssociation2);

        DeviceAssociation deviceAssociation3 = new DeviceAssociation();
        deviceAssociation3.setAssociationId("3");
        deviceAssociation3.setHarmanId("HUBIIDLFQAC476");
        deviceAssociation3.setAssociationStatus("DISASSOCIATED");
        associationList.add(deviceAssociation3);
        return Optional.of(associationList);
    }

    @Test
    public void testGetdevices() throws Exception {
        Optional<List<DeviceAssociation>> list = deviceAssociationServiceClient.getDevices("Devices");
        assertNotNull(list);
    }

    @Test
    public void testInvokeConfigureSslSocketFactory() throws Exception {
        try {
            DeviceMessageService.configureSslSocketFactory();
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void testIfMqttClientNumberisDestroyed() throws Exception {
        DeviceMessageService service = new DeviceMessageService();
        try {
            service.setMqttQos(service.getMqttQos());
            service.destroy();
            
            assertEquals(0, service.getClients().length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
