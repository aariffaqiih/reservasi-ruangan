package com.tup.reservasi.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tup.reservasi.entity.Reservation;
import com.tup.reservasi.enums.ReservationStatus;

/*
 * Penanggung jawab: Ali Abdul Fattah 'Alim Kautsar - 103112400213.
 * Modul: ReservationService dan Room.
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByMahasiswa_Id(Long mahasiswaId);

    List<Reservation> findByRoom_RoomIdAndTanggal(Long roomId, LocalDate tanggal);

    List<Reservation> findByStatus(ReservationStatus status);

    @Modifying
    @Query("DELETE FROM Reservation r WHERE r.reservationId = :reservationId")
    void deleteByReservationId(@Param("reservationId") Long reservationId);

    @Query("SELECT r FROM Reservation r WHERE r.room.roomId = :roomId "
            + "AND r.tanggal = :tanggal "
            + "AND r.status IN :statuses "
            + "AND r.jamMulai < :jamSelesai "
            + "AND r.jamSelesai > :jamMulai")
    List<Reservation> findOverlappingReservations(
            @Param("roomId") Long roomId,
            @Param("tanggal") LocalDate tanggal,
            @Param("jamMulai") LocalTime jamMulai,
            @Param("jamSelesai") LocalTime jamSelesai,
            @Param("statuses") Collection<ReservationStatus> statuses);
}
