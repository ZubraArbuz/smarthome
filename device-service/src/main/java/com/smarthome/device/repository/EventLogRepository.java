package com.smarthome.device.repository;

import com.smarthome.device.model.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventLogRepository extends JpaRepository<EventLog, Long> {
    List<EventLog> findByDeviceIdDeviceOrderByTimestampDesc(Long deviceId);
    List<EventLog> findAllByOrderByTimestampDesc();
}
