package com.tup.reservasi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tup.reservasi.entity.Approval;
import com.tup.reservasi.enums.ApprovalDecision;

/*
 * Penanggung jawab: Aarif Rahmaan Jalaluddin Faqiih - 103112430182.
 * Modul: Approval dan ApprovalService.
 */
@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Long> {

    List<Approval> findByReservation_ReservationId(Long reservationId);

    @Modifying
    @Query("DELETE FROM Approval a WHERE a.reservation.reservationId = :reservationId")
    void deleteByReservationId(@Param("reservationId") Long reservationId);

    List<Approval> findByAdmin_Id(Long adminId);

    List<Approval> findByKeputusan(ApprovalDecision keputusan);
}
