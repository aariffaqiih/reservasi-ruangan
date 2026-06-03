package com.tup.reservasi.controller.rest;

/* ---------------------------------------------------------------
 * Penanggung jawab: Ali Abdul Fattah 'Alim Kautsar.
 *
 * REST controller untuk entitas Room. Menyajikan endpoint CRUD serta
 * operasi aktivasi/non‑aktivasi. Semua logika bisnis didelegasikan ke
 * {@link com.tup.reservasi.service.RoomService}.
 * --------------------------------------------------------------- */

import com.tup.reservasi.dto.RoomRequest;
import com.tup.reservasi.dto.RoomResponse;
import com.tup.reservasi.dto.RoomStatusRequest;
import com.tup.reservasi.entity.Room;
import com.tup.reservasi.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/rooms")
public class RoomRestController {

    private final RoomService roomService;

    public RoomRestController(RoomService roomService) {
        this.roomService = roomService;
    }

    // ---------------------------------------------------------------------
    // CREATE
    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody RoomRequest request) {
        Room room = toEntity(request);
        Room saved = roomService.createRoom(room);
        return new ResponseEntity<>(toResponse(saved), HttpStatus.CREATED);
    }

    // ---------------------------------------------------------------------
    // READ – all rooms
    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        List<RoomResponse> rooms = roomService.getAllRooms()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(rooms);
    }

    // READ – single room by ID
    @GetMapping("/{roomId}")
    public ResponseEntity<RoomResponse> getRoom(@PathVariable String roomId) {
        Optional<Room> opt = roomService.getRoomById(roomId);
        return opt.map(room -> ResponseEntity.ok(toResponse(room)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ---------------------------------------------------------------------
    // UPDATE – full replacement
    @PutMapping("/{roomId}")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable String roomId,
                                                   @Valid @RequestBody RoomRequest request) {
        // Ensure the entity has the correct ID
        Room updated = toEntity(request);
        updated.setRoomId(roomId);
        Room saved = roomService.updateRoom(updated);
        return ResponseEntity.ok(toResponse(saved));
    }

    // ---------------------------------------------------------------------
    // DELETE
    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable String roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.noContent().build();
    }

    // ---------------------------------------------------------------------
    // ACTIVATE / DEACTIVATE via status request
    @PatchMapping("/{roomId}/status")
    public ResponseEntity<Void> changeStatus(@PathVariable String roomId,
                                            @RequestBody RoomStatusRequest request) {
        if (request.isStatusAktif()) {
            roomService.activateRoom(roomId);
        } else {
            roomService.deactivateRoom(roomId);
        }
        return ResponseEntity.ok().build();
    }

    // ---------------------------------------------------------------------
    // Utility conversion methods
    private Room toEntity(RoomRequest request) {
        return new Room(null,
                request.getNamaRuang(),
                request.getGedung(),
                request.getKapasitas(),
                request.isStatusAktif());
    }

    private RoomResponse toResponse(Room room) {
        return new RoomResponse(room.getRoomId(),
                room.getNamaRuang(),
                room.getGedung(),
                room.getKapasitas(),
                room.isStatusAktif(),
                room.getInfoRuang());
    }
}
