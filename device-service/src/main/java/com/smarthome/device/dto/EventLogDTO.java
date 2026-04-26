package com.smarthome.device.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventLogDTO {

    private Long idEvent;
    private Long deviceId;
    private String deviceName;
    private Long idUser;
    private String action;
    private String oldValue;
    private String newValue;
    private LocalDateTime timestamp;
}