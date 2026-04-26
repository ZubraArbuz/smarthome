package com.smarthome.device.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDTO {

    private Long idDevice;
    private String name;
    private String type;
    private String status;
    private String value;
    private Long roomId;
    private String roomName;
}