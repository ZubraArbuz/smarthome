package com.smarthome.statistics.mqtt;

import com.smarthome.statistics.service.StatisticsService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StatsMqttSubscriber {

    private final StatisticsService statisticsService;

    @Value("${mqtt.broker.url}")
    private String brokerUrl;

    @Value("${mqtt.client.id}")
    private String clientId;

    public StatsMqttSubscriber(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @PostConstruct
    public void connect() {
        try {
            MqttClient client = new MqttClient(brokerUrl, clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);

            client.connect(options);
            log.info("Stats MQTT подключён к брокеру: {}", brokerUrl);

            client.subscribe("home/+/+/value", (topic, message) -> {
                String payload = new String(message.getPayload());
                log.info("Stats MQTT получено: topic={}, payload={}", topic, payload);

                String[] parts = topic.split("/");
                if (parts.length == 4) {
                    try {
                        Long deviceId = Long.parseLong(parts[2]);
                        Double consumption = Double.parseDouble(payload);
                        statisticsService.saveStats(deviceId, consumption);
                    } catch (NumberFormatException e) {
                        log.warn("Не удалось распарсить данные: {}", payload);
                    }
                }
            });

        } catch (MqttException e) {
            log.error("Ошибка подключения Stats MQTT: {}", e.getMessage());
        }
    }
}