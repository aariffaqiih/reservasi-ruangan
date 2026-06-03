package com.tup.reservasi.repository;

/*
 * Penanggung jawab: Ali Abdul Fattah 'Alim Kautsar.
 *
 * Arahan repository:
 * - Siapkan akses data untuk Reservation.
 * - Field pencarian utama:
 *   reservationId, mahasiswa, room, tanggal, jamMulai, jamSelesai, status.
 * - Query yang kemungkinan dibutuhkan:
 *   riwayat reservasi per Mahasiswa.
 *   daftar reservasi per Room dan tanggal.
 *   cek bentrok jam untuk validateAvailability().
 *   daftar reservasi berdasarkan status.
 */

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tup.reservasi.entity.Reservation;
import com.tup.reservasi.enums.ReservationStatus;

public interface ReservationRepository extends JpaRepository<Reservation, String> {

    List<Reservation> findByMahasiswaId(String mahasiswaId);

    List<Reservation> findByRoomIdAndTanggal(String roomId, LocalDate tanggal);

    List<Reservation> findByStatus(ReservationStatus status);

    List<Reservation> findByRoomIdAndTanggalAndStatusIn(
            String roomId,
            LocalDate tanggal,
            Collection<ReservationStatus> statuses
    );

    @Query("SELECT r FROM Reservation r WHERE r.roomId = :roomId " +
           "AND r.tanggal = :tanggal " +
           "AND r.status IN :statuses " +
           "AND r.jamMulai < :jamSelesai " +
           "AND r.jamSelesai > :jamMulai")
    List<Reservation> findOverlappingReservations(
            @Param("roomId") String roomId,
            @Param("tanggal") LocalDate tanggal,
            @Param("jamMulai") LocalTime jamMulai,
            @Param("jamSelesai") LocalTime jamSelesai,
            @Param("statuses") Collection<ReservationStatus> statuses
    );
}
