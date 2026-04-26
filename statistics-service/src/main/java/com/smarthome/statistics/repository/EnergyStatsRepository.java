package com.smarthome.statistics.repository;

import com.smarthome.statistics.model.EnergyStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EnergyStatsRepository extends JpaRepository<EnergyStats, Long> {

    List<EnergyStats> findByIdDeviceOrderByRecordedAtDesc(Long deviceId);

    List<EnergyStats> findByRecordedAtBetweenOrderByRecordedAtDesc(
            LocalDateTime from, LocalDateTime to);

    List<EnergyStats> findByIdDeviceAndRecordedAtBetweenOrderByRecordedAtDesc(
            Long deviceId, LocalDateTime from, LocalDateTime to);
}