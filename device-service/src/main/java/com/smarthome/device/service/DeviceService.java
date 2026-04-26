package com.smarthome.device.service;

import com.smarthome.device.dto.DeviceCommandDTO;
import com.smarthome.device.dto.DeviceDTO;
import com.smarthome.device.dto.RoomDTO;
import com.smarthome.device.model.Device;
import com.smarthome.device.model.Room;
import com.smarthome.device.repository.DeviceRepository;
import com.smarthome.device.repository.RoomRepository;
import com.smarthome.device.websocket.WebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeviceService {
    private final DeviceRepository deviceRepository;
    private final RoomRepository roomRepository;
    private final EventLogService eventLogService;
    private final WebSocketHandler webSocketHandler;
    //
    // Комнаты

    public List<RoomDTO> getAllRooms() {
        return roomRepository.findAll()
                .stream()
                .map(this::toRoomDTO)
                .collect(Collectors.toList());
    }

    public RoomDTO createRoom(RoomDTO dto) {
        if (roomRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Комната с таким именем уже существует");
        }
        Room room = new Room();
        room.setName(dto.getName());
        room.setDescription(dto.getDescription());
        return toRoomDTO(roomRepository.save(room));
    }

    public RoomDTO updateRoom(Long id, RoomDTO dto) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Комната не найдена"));
        room.setName(dto.getName());
        room.setDescription(dto.getDescription());
        return toRoomDTO(roomRepository.save(room));
    }

    public void deleteRoom(Long id) {
        roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Комната не найдена"));
        roomRepository.deleteById(id);
    }

    // Устройства

    public List<DeviceDTO> getAllDevices() {
        return deviceRepository.findAll()
                .stream()
                .map(this::toDeviceDTO)
                .collect(Collectors.toList());
    }

    public List<DeviceDTO> getDevicesByRoom(Long roomId) {
        return deviceRepository.findByRoomIdRoom(roomId)
                .stream()
                .map(this::toDeviceDTO)
                .collect(Collectors.toList());
    }

    public DeviceDTO createDevice(DeviceDTO dto) {
        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new RuntimeException("Комната не найдена"));
        Device device = new Device();
        device.setName(dto.getName());
        device.setType(dto.getType());
        device.setStatus("OFF");
        device.setValue(dto.getValue());
        device.setRoom(room);
        return toDeviceDTO(deviceRepository.save(device));
    }

    public DeviceDTO updateDevice(Long id, DeviceDTO dto) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Устройство не найдено"));
        device.setName(dto.getName());
        device.setType(dto.getType());
        return toDeviceDTO(deviceRepository.save(device));
    }

    public void deleteDevice(Long id) {
        deviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Устройство не найдено"));
        deviceRepository.deleteById(id);
    }

    public DeviceDTO executeCommand(Long id, DeviceCommandDTO command, Long userId) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Устройство не найдено"));

        String oldStatus = device.getStatus();
        String oldValue = device.getValue();

        device.setStatus(command.getAction());
        if (command.getValue() != null) {
            device.setValue(command.getValue());
        }

        deviceRepository.save(device);

        eventLogService.logEvent(device, userId, command.getAction(), oldStatus, command.getAction());

        DeviceDTO dto = toDeviceDTO(device);
        webSocketHandler.sendDeviceUpdate(dto);

        return dto;
    }

    public void updateDeviceFromMqtt(Long deviceId, String status, String value) {
        deviceRepository.findById(deviceId).ifPresent(device -> {
            String oldStatus = device.getStatus();
            device.setStatus(status);
            device.setValue(value);
            deviceRepository.save(device);

            eventLogService.logEvent(device, null, "MQTT_UPDATE", oldStatus, status);

            DeviceDTO dto = toDeviceDTO(device);
            webSocketHandler.sendDeviceUpdate(dto);
        });
    }

    // Маппинг

    private DeviceDTO toDeviceDTO(Device device) {
        DeviceDTO dto = new DeviceDTO();
        dto.setIdDevice(device.getIdDevice());
        dto.setName(device.getName());
        dto.setType(device.getType());
        dto.setStatus(device.getStatus());
        dto.setValue(device.getValue());
        dto.setRoomId(device.getRoom().getIdRoom());
        dto.setRoomName(device.getRoom().getName());
        return dto;
    }

    private RoomDTO toRoomDTO(Room room) {
        RoomDTO dto = new RoomDTO();
        dto.setIdRoom(room.getIdRoom());
        dto.setName(room.getName());
        dto.setDescription(room.getDescription());
        return dto;
    }
}
