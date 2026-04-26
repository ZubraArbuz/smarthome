package com.smarthome.device.service;

import com.smarthome.device.dto.EventLogDTO;
import com.smarthome.device.model.Device;
import com.smarthome.device.model.EventLog;
import com.smarthome.device.repository.EventLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventLogService {
    private final EventLogRepository eventLogRepository;
    public void logEvent(Device device, Long userId, String action,
                         String oldValue, String newValue){
        EventLog log = new EventLog();
        log.setDevice(device);
        log.setIdUser(userId);
        log.setAction(action);
        log.setOldValue(oldValue);
        log.setNewValue(newValue);
        eventLogRepository.save(log);
    }

    public List<EventLogDTO> getAllEvents() {
        return eventLogRepository.findAllByOrderByTimestampDesc()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<EventLogDTO> getEventsByDevice(Long deviceId) {
        return eventLogRepository.findByDeviceIdDeviceOrderByTimestampDesc(deviceId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private EventLogDTO toDTO(EventLog log){
        EventLogDTO dto = new EventLogDTO();
        dto.setIdEvent(log.getIdEvent());
        dto.setDeviceId(log.getDevice().getIdDevice());
        dto.setDeviceName(log.getDevice().getName());
        dto.setIdUser(log.getIdUser());
        dto.setAction(log.getAction());
        dto.setOldValue(log.getOldValue());
        dto.setNewValue(log.getNewValue());
        dto.setTimestamp(log.getTimestamp());
        return dto;
    }
}
