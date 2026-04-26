package com.smarthome.statistics.service;

import com.smarthome.statistics.dto.EnergyStatsDTO;
import com.smarthome.statistics.model.EnergyStats;
import com.smarthome.statistics.repository.EnergyStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final EnergyStatsRepository energyStatsRepository;

    public void saveStats(Long deviceId, Double consumption) {
        EnergyStats stats = new EnergyStats();
        stats.setIdDevice(deviceId);
        stats.setConsumption(consumption);
        energyStatsRepository.save(stats);
    }

    public List<EnergyStatsDTO> getAllStats() {
        return energyStatsRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<EnergyStatsDTO> getStatsByDevice(Long deviceId) {
        return energyStatsRepository.findByIdDeviceOrderByRecordedAtDesc(deviceId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<EnergyStatsDTO> getStatsByPeriod(LocalDateTime from, LocalDateTime to) {
        return energyStatsRepository.findByRecordedAtBetweenOrderByRecordedAtDesc(from, to)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<EnergyStatsDTO> getStatsByDeviceAndPeriod(Long deviceId,
                                                          LocalDateTime from,
                                                          LocalDateTime to) {
        return energyStatsRepository
                .findByIdDeviceAndRecordedAtBetweenOrderByRecordedAtDesc(deviceId, from, to)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private EnergyStatsDTO toDTO(EnergyStats stats) {
        EnergyStatsDTO dto = new EnergyStatsDTO();
        dto.setIdStat(stats.getIdStat());
        dto.setIdDevice(stats.getIdDevice());
        dto.setConsumption(stats.getConsumption());
        dto.setRecordedAt(stats.getRecordedAt());
        return dto;
    }
}