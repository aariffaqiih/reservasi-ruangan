package com.tup.reservasi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tup.reservasi.entity.AccessRecord;

/*
 * Penanggung jawab: Tadzkiroh Aziziyah Haqia - 103112400242.
 * Modul: AccessRecord dan AccessControlService.
 */
@Repository
public interface AccessRecordRepository extends JpaRepository<AccessRecord, Long> {

    Optional<AccessRecord> findByReservation_ReservationId(Long reservationId);

    @Modifying
    @Query("DELETE FROM AccessRecord a WHERE a.reservation.reservationId = :reservationId")
    void deleteByReservationId(@Param("reservationId") Long reservationId);

    List<AccessRecord> findBySatpam_Id(Long satpamId);

    List<AccessRecord> findByCheckOutTimeIsNull();
}
