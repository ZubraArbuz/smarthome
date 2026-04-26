package com.smarthome.statistics.controller;

import com.smarthome.statistics.dto.EnergyStatsDTO;
import com.smarthome.statistics.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<List<EnergyStatsDTO>> getAllStats() {
        return ResponseEntity.ok(statisticsService.getAllStats());
    }

    @GetMapping("/device/{deviceId}")
    public ResponseEntity<List<EnergyStatsDTO>> getStatsByDevice(
            @PathVariable Long deviceId) {
        return ResponseEntity.ok(statisticsService.getStatsByDevice(deviceId));
    }

    @GetMapping("/period")
    public ResponseEntity<List<EnergyStatsDTO>> getStatsByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(statisticsService.getStatsByPeriod(from, to));
    }

    @GetMapping("/device/{deviceId}/period")
    public ResponseEntity<List<EnergyStatsDTO>> getStatsByDeviceAndPeriod(
            @PathVariable Long deviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(statisticsService.getStatsByDeviceAndPeriod(deviceId, from, to));
    }
}