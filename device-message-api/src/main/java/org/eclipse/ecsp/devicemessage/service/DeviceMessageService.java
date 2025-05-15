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

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import com.codahale.metrics.Timer.Context;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.ecsp.devicemessage.domain.Config;
import org.eclipse.ecsp.devicemessage.util.Constants;
import org.eclipse.ecsp.utils.logger.IgniteLogger;
import org.eclipse.ecsp.utils.logger.IgniteLoggerFactory;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.net.ssl.SSLSocketFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * This class represents a service for handling device messages using MQTT protocol.
 * It implements the MqttBaseService interface and provides methods for publishing data to MQTT topics.
 * The service is initialized and configured using the properties specified in the application configuration file.
 * It also supports SSL/TLS encryption for secure communication.
 */
@Service(value = "mqttService")
@Getter
public class DeviceMessageService implements MqttBaseService, InitializingBean, DisposableBean {

    private static final String TO_DEVICE = "2d";

    private static final IgniteLogger LOGGER = IgniteLoggerFactory.getLogger(DeviceMessageService.class);
    private static final int MAX_CLIENTS = 24;
    @Value("${mqtt.broker.url}")
    private String brokerUrl;
    @Value("${mqtt.device.max.connections}")
    private int maxConnections;
    @Value("${mqtt.maxInflight}")
    private int maxInflight;
    @Value("${mqtt.user.name}")
    private String userName;
    @Value("${mqtt.user.password}")
    private String userPassword;
    @Value("${mqtt.config.qos}")
    private int mqttQos;
    @Value("${mqtt.topic.separator}")
    private String seperator;
    @Value("${mqtt.connection.ssl.enabled}")
    private boolean sslEnabled;
    @Value("${mqtt.persistence.base.dir}")
    private String mqttPersistenceBaseDir;
    @Value("${mqtt.prefix.enabled}")
    private boolean mqttPrefixEnabled;
    @Value("${mqtt.prefix.topic}")
    private String mqttPrefix;
    @Value("${mqtt.clientid}")
    private String clientId;
    
    @Autowired
    private MetricRegistry metricRegistry;

    private static final int KEEP_ALIVE_INTERVAL = 120;
    private static final int TIME_PERIOD = 5;
    private MqttAsyncClient[] clients = new MqttAsyncClient[MAX_CLIENTS];

    private MqttConnectOptions connOpt = null;

    private ObjectMapper objectMapper;

    /**
     * This class represents a service for handling device messages.
     * It provides methods for serializing and deserializing device messages.
     */
    public DeviceMessageService() {
        super();
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(Include.NON_NULL);
    }

    /**
     * Configures and returns the default SSL socket factory.
     *
     * @return The default SSL socket factory.
     */
    public static SSLSocketFactory configureSslSocketFactory() {
        return (SSLSocketFactory) SSLSocketFactory.getDefault();
    }

    /**
     * Publishes data to the specified MQTT topic for a given device.
     *
     * @param topic   The MQTT topic to publish the data to.
     * @param deviceId   The ID of the device.
     * @param message   The MQTT message to publish.
     * @throws InterruptedException   If the thread is interrupted while waiting for the operation to complete.
     * @throws ExecutionException   If an error occurs during the execution of the operation.
     * @throws MqttException   If an error occurs while publishing the message.
     */
    public void publishData(MqttTopic topic, String deviceId, MqttMessage message)
        throws InterruptedException, ExecutionException, MqttException {
        StringBuilder fullTopic = new StringBuilder();
        if (mqttPrefixEnabled) {
            fullTopic.append(mqttPrefix).append(seperator).append(deviceId).append(seperator).append(TO_DEVICE)
                .append(seperator).append(topic.toString().toLowerCase());
        } else {
            fullTopic.append(deviceId).append(seperator).append(TO_DEVICE).append(seperator)
                .append(topic.toString().toLowerCase());
        }
        int clientIndex = Math.abs(deviceId.hashCode() % MAX_CLIENTS);
        MqttAsyncClient mac = clients[clientIndex];
        Context c = metricRegistry.timer("mqtt-pub").time();
        synchronized (mac) {
            if (!mac.isConnected()) {
                connectToMqtt(mac);
            }
        }
        mac.publish(fullTopic.toString(), message);
        c.stop();
    }

    /**
     * This method is called after all bean properties have been set, and it is used to initialize the MQTT connection.
     * It configures the MQTT connection options, sets up SSL if enabled, sets the clean session flag,
     * sets the keep alive interval,
     * sets the username and password for authentication, creates multiple MQTT clients, connects them to the broker,
     * and starts a Slf4jReporter for reporting MQTT metrics.
     *
     * @throws Exception if an error occurs during initialization
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        connOpt = new MqttConnectOptions();
        if (sslEnabled) {
            connOpt.setSocketFactory(configureSslSocketFactory());
        }
        connOpt.setCleanSession(true);
        connOpt.setKeepAliveInterval(KEEP_ALIVE_INTERVAL);
        connOpt.setUserName(userName);
        connOpt.setPassword(userPassword.toCharArray());
        LOGGER.info("MQTT userPassword:{}", connOpt.getPassword());
        connOpt.setMaxInflight(maxInflight);
        boolean atleastOneConnected = false;
        for (int i = 0; i < MAX_CLIENTS; i++) {
            clients[i] = new MqttAsyncClient(brokerUrl, clientId + UUID.randomUUID() + " - " + i,
                new MqttDefaultFilePersistence(mqttPersistenceBaseDir));
            try {
                connectToMqtt(clients[i]);
                atleastOneConnected = true;
            } catch (MqttException me) {
                LOGGER.error("MqttException when connecting client number - {}", i, me);
            }
        }
        if (!atleastOneConnected) {
            LOGGER.error("None of the MqttClients could connect");
            throw new IllegalStateException("None of the MqttClients could connect. Is MQTT running?");
        }
        Slf4jReporter.forRegistry(metricRegistry).filter((String name, Metric metric) -> name.startsWith("mqtt"))
            .convertDurationsTo(TimeUnit.MILLISECONDS)
            .convertRatesTo(TimeUnit.SECONDS).build().start(TIME_PERIOD, TimeUnit.MINUTES);
    }

    /**
     * Connects to the MQTT broker using the provided MqttAsyncClient.
     *
     * @param mac The MqttAsyncClient to connect with.
     * @throws MqttException If an error occurs while connecting to the MQTT broker.
     */
    private void connectToMqtt(MqttAsyncClient mac) throws MqttException {
        LOGGER.info("mqttBrokerUrl = {} : ", brokerUrl);
        Context c = metricRegistry.timer("mqtt-conn").time();
        mac.connect(connOpt).waitForCompletion();
        c.stop();
        LOGGER.info("mqtt setup successful");
    }

    /**
     * Sets the MQTT Quality of Service (QoS) level.
     *
     * @param mqttQos the MQTT QoS level to set
     */
    public void setMqttQos(int mqttQos) {
        this.mqttQos = mqttQos;
    }

    /**
     * Destroys the MQTT clients and disconnects them from the server.
     * This method is called when the service is being destroyed.
     * It iterates over the clients array and disconnects each client from the MQTT server.
     * If an exception occurs during the disconnection process, it logs an error message.
     *
     * @throws Exception if an error occurs during the destruction process.
     */
    @Override
    public void destroy() throws Exception {
        for (int i = 0; i < MAX_CLIENTS; i++) {
            try {
                clients[i].disconnect().waitForCompletion();
                clients[i].close();
            } catch (MqttException me) {
                LOGGER.error("Exception when destroying mqtt client number -{}", i, me);
            }
        }
    }

    /**
     * Publishes a MQTT message with the provided configuration, device ID, data, and timestamp.
     *
     * @param config     The MQTT topic configuration.
     * @param deviceId   The ID of the device.
     * @param data       The configuration data.
     * @param timestamp  The timestamp of the message.
     * @throws JsonProcessingException  If there is an error processing the JSON data.
     * @throws MqttException            If there is an error with the MQTT connection.
     * @throws ExecutionException       If there is an error executing the MQTT publish.
     * @throws InterruptedException   If the MQTT publish is interrupted.
     */
    public void publish(MqttTopic config, String deviceId, Config data, long timestamp)
        throws JsonProcessingException, MqttException, ExecutionException, InterruptedException {
        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put(Constants.DOMAIN, data.getDomain());
        msgMap.put(Constants.COMMAND, data.getCommand());
        if (StringUtils.isNotEmpty(data.getVersion())) {
            msgMap.put(Constants.VERSION, data.getVersion());
        }
        msgMap.put(Constants.DATA, data.getData());
        if (StringUtils.isNotEmpty(data.getBizTransactionId())) {
            msgMap.put(Constants.BIZ_TRANSACTION_ID, data.getBizTransactionId());
        }
        LOGGER.debug("Publishing mqtt msg at:{}", timestamp);
        MqttMessage message = new MqttMessage();
        msgMap.put(Constants.TIMESTAMP, timestamp);
        byte[] payLoad = objectMapper.writeValueAsBytes(msgMap);
        LOGGER.debug("Writing payload {} to MQTT for device {}", payLoad, deviceId);
        message.setPayload(payLoad);
        message.setQos(getMqttQos());

        publishData(config, deviceId, message);
    }
}