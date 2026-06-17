package com.tup.reservasi.controller.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tup.reservasi.entity.Room;
import com.tup.reservasi.service.RoomService;

/*
 * Penanggung jawab: Ali Abdul Fattah 'Alim Kautsar - 103112400213.
 * Modul: Room.
 */
@RestController
@RequestMapping("/api/rooms")
public class RoomRestController {

    private final RoomService roomService;

    public RoomRestController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(this.roomService.getAllRooms());
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long roomId) {
        try {
            return ResponseEntity.ok(this.roomService.getRoomById(roomId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/gedung/{gedung}")
    public ResponseEntity<List<Room>> getRoomsByGedung(@PathVariable String gedung) {
        return ResponseEntity.ok(this.roomService.getRoomsByGedung(gedung));
    }

    @GetMapping("/kapasitas")
    public ResponseEntity<List<Room>> getRoomsByKapasitasMinimal(@RequestParam int minimal) {
        return ResponseEntity.ok(this.roomService.getRoomsByKapasitasMinimal(minimal));
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        Room roomCreated = this.roomService.createRoom(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(roomCreated);
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long roomId, @RequestBody Room room) {
        try {
            return ResponseEntity.ok(this.roomService.updateRoom(roomId, room));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{roomId}/status")
    public ResponseEntity<Room> updateRoomStatus(@PathVariable Long roomId,
            @RequestParam boolean statusAktif) {
        try {
            return ResponseEntity.ok(this.roomService.updateRoomStatus(roomId, statusAktif));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
    }
}
