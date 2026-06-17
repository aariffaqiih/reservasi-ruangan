package com.tup.reservasi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tup.reservasi.entity.Room;
import com.tup.reservasi.repository.RoomRepository;

/*
 * Penanggung jawab: Ali Abdul Fattah 'Alim Kautsar - 103112400213.
 * Modul: Room.
 */
@Service
@Transactional
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Transactional(readOnly = true)
    public List<Room> getAllRooms() {
        return this.roomRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Room getRoomById(Long roomId) {
        return this.roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room tidak ditemukan"));
    }

    public Room createRoom(Room room) {
        return this.roomRepository.save(room);
    }

    public Room updateRoom(Long roomId, Room updatedData) {
        Room roomExisting = getRoomById(roomId);
        roomExisting.setNamaRuang(updatedData.getNamaRuang());
        roomExisting.setGedung(updatedData.getGedung());
        roomExisting.setKapasitas(updatedData.getKapasitas());
        roomExisting.setStatusAktif(updatedData.isStatusAktif());
        return this.roomRepository.save(roomExisting);
    }

    public void deleteRoom(Long roomId) {
        Room roomExisting = getRoomById(roomId);
        this.roomRepository.delete(roomExisting);
    }

    public Room updateRoomStatus(Long roomId, boolean statusAktif) {
        Room roomExisting = getRoomById(roomId);
        roomExisting.ubahStatusAktif(statusAktif);
        return this.roomRepository.save(roomExisting);
    }

    @Transactional(readOnly = true)
    public List<Room> getActiveRooms() {
        return this.roomRepository.findByStatusAktif(true);
    }

    @Transactional(readOnly = true)
    public List<Room> getRoomsByGedung(String gedung) {
        return this.roomRepository.findByGedung(gedung);
    }

    @Transactional(readOnly = true)
    public List<Room> getRoomsByKapasitasMinimal(int kapasitas) {
        return this.roomRepository.findByKapasitasGreaterThanEqual(kapasitas);
    }
}
