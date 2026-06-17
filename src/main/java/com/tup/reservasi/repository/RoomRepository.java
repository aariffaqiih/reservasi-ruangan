package com.tup.reservasi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tup.reservasi.entity.Room;

/*
 * Penanggung jawab: Ali Abdul Fattah 'Alim Kautsar - 103112400213.
 * Modul: Room.
 */
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByStatusAktif(boolean statusAktif);

    List<Room> findByGedung(String gedung);

    List<Room> findByKapasitasGreaterThanEqual(int kapasitas);
}
