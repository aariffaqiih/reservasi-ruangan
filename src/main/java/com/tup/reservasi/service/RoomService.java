package com.tup.reservasi.service;

/*
 * Penanggung jawab: Ali Abdul Fattah 'Alim Kautsar.
 *
 * Service ini mengelola entitas Room. Semua logika bisnis
 * (validasi, perubahan status, dll) berada di sini, sehingga
 * controller tetap tipis dan hanya berurusan dengan HTTP.
 */

import com.tup.reservasi.entity.Room;
import com.tup.reservasi.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    /* ------------------------------------------------------------------ */
    // CREATE
    @Transactional
    public Room createRoom(Room room) {
        // Simple creation; additional validation can be added later.
        validateRoom(room);
        return roomRepository.save(room);
    }

    /* ------------------------------------------------------------------ */
    // READ
    public Optional<Room> getRoomById(String roomId) {
        return roomRepository.findById(roomId);
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public List<Room> getActiveRooms() {
        return roomRepository.findByStatusAktif(true);
    }

    public List<Room> findByGedung(String gedung) {
        return roomRepository.findByGedung(gedung);
    }

    public List<Room> findByKapasitasGreaterThanEqual(int kapasitas) {
        return roomRepository.findByKapasitasGreaterThanEqual(kapasitas);
    }

    public List<Room> findByNamaRuang(String namaRuang) {
        return roomRepository.findByNamaRuang(namaRuang);
    }

    /* ------------------------------------------------------------------ */
    // UPDATE
    @Transactional
    public Room updateRoom(Room updatedRoom) {
        validateRoom(updatedRoom);
        Room existing = roomRepository.findById(updatedRoom.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        existing.setNamaRuang(updatedRoom.getNamaRuang());
        existing.setGedung(updatedRoom.getGedung());
        existing.setKapasitas(updatedRoom.getKapasitas());
        existing.setStatusAktif(updatedRoom.isStatusAktif());
        return roomRepository.save(existing);
    }

    @Transactional
    public void activateRoom(String roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        room.aktifkan();
        roomRepository.save(room);
    }

    @Transactional
    public void deactivateRoom(String roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        room.nonaktifkan();
        roomRepository.save(room);
    }

    /* ------------------------------------------------------------------ */
    // DELETE
    @Transactional
    public void deleteRoom(String roomId) {
        if (!roomRepository.existsById(roomId)) {
            throw new IllegalArgumentException("Room not found");
        }
        roomRepository.deleteById(roomId);
    }

    /* ------------------------------------------------------------------ */
    // UTILITIES
    public String getInfoRuang(String roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        return room.getInfoRuang();
    }
    private void validateRoom(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("Room tidak boleh kosong");
        }
        if (room.getNamaRuang() == null || room.getNamaRuang().isBlank()) {
            throw new IllegalArgumentException("Nama ruang tidak boleh kosong");
        }
        if (room.getGedung() == null || room.getGedung().isBlank()) {
            throw new IllegalArgumentException("Gedung tidak boleh kosong");
        }
        if (room.getKapasitas() < 1) {
            throw new IllegalArgumentException("Kapasitas minimal 1");
        }
    }
}
