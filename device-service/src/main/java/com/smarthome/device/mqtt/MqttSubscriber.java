package com.smarthome.device.mqtt;

import com.smarthome.device.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Slf4j
@Component
public class MqttSubscriber {

    private final DeviceService deviceService;

    @Value("${mqtt.broker.url}")
    private String brokerUrl;

    @Value("${mqtt.client.id}")
    private String clientId;

    @Value("${mqtt.topic.pattern}")
    private String topicPattern;

    public MqttSubscriber(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostConstruct
    public void connect() {
        try {
            MqttClient client = new MqttClient(brokerUrl, clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);

            client.connect(options);
            log.info("MQTT подключён к брокеру: {}", brokerUrl);

            client.subscribe("home/+/+/state", (topic, message) -> {
                String payload = new String(message.getPayload());
                log.info("MQTT получено: topic={}, payload={}", topic, payload);

                // Парсим topic: home/{roomId}/{deviceId}/state
                String[] parts = topic.split("/");
                if (parts.length == 4) {
                    Long deviceId = Long.parseLong(parts[2]);
                    deviceService.updateDeviceFromMqtt(deviceId, payload, null);
                }
            });

            client.subscribe("home/+/+/value", (topic, message) -> {
                String payload = new String(message.getPayload());
                log.info("MQTT получено: topic={}, payload={}", topic, payload);

                String[] parts = topic.split("/");
                if (parts.length == 4) {
                    Long deviceId = Long.parseLong(parts[2]);
                    deviceService.updateDeviceFromMqtt(deviceId, null, payload);
                }
            });

        } catch (MqttException e) {
            log.error("Ошибка подключения к MQTT: {}", e.getMessage());
        }
    }
}