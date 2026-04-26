package com.smarthome.device.repository;

import com.smarthome.device.model.Device;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByRoomIdRoom(Long roomId);
    List<Device> findByType(String type);
    List<Device> findByStatus(String status);
}
