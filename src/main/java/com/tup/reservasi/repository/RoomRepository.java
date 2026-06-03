package com.tup.reservasi.repository;

/*
 * Penanggung jawab: Ali Abdul Fattah 'Alim Kautsar.
 *
 * Arahan repository:
 * - Siapkan akses data untuk Room.
 * - Field pencarian utama:
 *   roomId, namaRuang, gedung, kapasitas, statusAktif.
 * - Query yang kemungkinan dibutuhkan:
 *   cari ruangan aktif.
 *   cari ruangan berdasarkan gedung.
 *   cari ruangan berdasarkan kapasitas minimum.
 */

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tup.reservasi.entity.Room;

public interface RoomRepository extends JpaRepository<Room, String> {

    List<Room> findByStatusAktif(boolean statusAktif);

    List<Room> findByGedung(String gedung);

    List<Room> findByKapasitasGreaterThanEqual(int kapasitas);

    List<Room> findByNamaRuang(String namaRuang);

}
