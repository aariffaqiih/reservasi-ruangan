package com.tup.reservasi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tup.reservasi.entity.Notification;

/*
 * Penanggung jawab: Ajda Mutiara Zahra - 103112400210.
 * Modul: Notification dan NotificationService.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n WHERE n.penerima.id = :penerimaId")
    List<Notification> findByPenerimaId(@Param("penerimaId") Long penerimaId);

    List<Notification> findByReservation_ReservationId(Long reservationId);

    @Modifying
    @Query("DELETE FROM Notification n WHERE n.reservation.reservationId = :reservationId")
    void deleteByReservationId(@Param("reservationId") Long reservationId);

    List<Notification> findByStatusBaca(boolean statusBaca);
}
