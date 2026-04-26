package com.smarthome.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnergyStatsDTO {

    private Long idStat;
    private Long idDevice;
    private Double consumption;
    private LocalDateTime recordedAt;
}