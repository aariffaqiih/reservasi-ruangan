package com.tup.reservasi.repository;

/*
 * Penanggung jawab: 'Aarif Rahmaan Jalaluddin Faqiih.
 *
 * Arahan repository:
 * - Siapkan akses data untuk Approval.
 * - Field pencarian utama:
 *   approvalId, reservation, admin, keputusan, reviewedAt.
 * - Query yang kemungkinan dibutuhkan:
 *   cari approval berdasarkan Reservation.
 *   daftar approval berdasarkan Admin.
 *   daftar approval berdasarkan keputusan.
 */

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tup.reservasi.entity.Approval;
import com.tup.reservasi.enums.ApprovalDecision;

public interface ApprovalRepository extends JpaRepository<Approval, String> {

    List<Approval> findByReservationId(String reservationId);

    Optional<Approval> findFirstByReservationIdOrderByReviewedAtDesc(String reservationId);

    List<Approval> findByAdminId(String adminId);

    List<Approval> findByKeputusan(ApprovalDecision keputusan);

    List<Approval> findAllByOrderByReviewedAtDesc();
}
