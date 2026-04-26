package com.smarthome.device.controller;

import com.smarthome.device.dto.RoomDTO;
import com.smarthome.device.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RoomController {

    private final DeviceService deviceService;

    @GetMapping
    public ResponseEntity<List<RoomDTO>> getAllRooms() {
        return ResponseEntity.ok(deviceService.getAllRooms());
    }

    @PostMapping
    public ResponseEntity<RoomDTO> createRoom(@RequestBody RoomDTO dto) {
        return ResponseEntity.ok(deviceService.createRoom(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomDTO> updateRoom(@PathVariable Long id,
                                              @RequestBody RoomDTO dto) {
        return ResponseEntity.ok(deviceService.updateRoom(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        deviceService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}