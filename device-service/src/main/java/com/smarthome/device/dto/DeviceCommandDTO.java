package com.smarthome.device.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceCommandDTO {

    private Long deviceId;
    private String action;
    private String value;
}