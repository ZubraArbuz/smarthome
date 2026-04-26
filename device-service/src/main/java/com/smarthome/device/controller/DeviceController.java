package com.smarthome.device.controller;

import com.smarthome.device.dto.DeviceCommandDTO;
import com.smarthome.device.dto.DeviceDTO;
import com.smarthome.device.dto.EventLogDTO;
import com.smarthome.device.service.DeviceService;
import com.smarthome.device.service.EventLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DeviceController {

    private final DeviceService deviceService;
    private final EventLogService eventLogService;

    @GetMapping
    public ResponseEntity<List<DeviceDTO>> getAllDevices() {
        return ResponseEntity.ok(deviceService.getAllDevices());
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<DeviceDTO>> getDevicesByRoom(@PathVariable Long roomId) {
        return ResponseEntity.ok(deviceService.getDevicesByRoom(roomId));
    }

    @PostMapping
    public ResponseEntity<DeviceDTO> createDevice(@RequestBody DeviceDTO dto) {
        return ResponseEntity.ok(deviceService.createDevice(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeviceDTO> updateDevice(@PathVariable Long id,
                                                  @RequestBody DeviceDTO dto) {
        return ResponseEntity.ok(deviceService.updateDevice(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/command")
    public ResponseEntity<DeviceDTO> executeCommand(@PathVariable Long id,
                                                    @RequestBody DeviceCommandDTO command,
                                                    Authentication authentication) {
        Long userId = null;
        return ResponseEntity.ok(deviceService.executeCommand(id, command, userId));
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventLogDTO>> getAllEvents() {
        return ResponseEntity.ok(eventLogService.getAllEvents());
    }

    @GetMapping("/{id}/events")
    public ResponseEntity<List<EventLogDTO>> getEventsByDevice(@PathVariable Long id) {
        return ResponseEntity.ok(eventLogService.getEventsByDevice(id));
    }
}