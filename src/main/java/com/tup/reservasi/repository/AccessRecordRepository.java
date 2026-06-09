package com.tup.reservasi.repository;

/*
 * Penanggung jawab: Tadzkiroh Aziziyah Haqia.
 *
 * Arahan repository:
 * - Siapkan akses data untuk AccessRecord.
 * - Field pencarian utama:
 *   recordId, reservation, satpam, checkInTime, checkOutTime, catatanPelanggaran.
 * - Query yang kemungkinan dibutuhkan:
 *   cari AccessRecord berdasarkan Reservation.
 *   daftar AccessRecord berdasarkan Satpam.
 *   daftar AccessRecord yang belum check-out.
 *   daftar catatan kendala/pelanggaran.
 */

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tup.reservasi.entity.AccessRecord;

public interface AccessRecordRepository extends JpaRepository<AccessRecord, String> {

    Optional<AccessRecord> findByReservationId(String reservationId);

    List<AccessRecord> findBySatpamId(String satpamId);

    @Query("SELECT r FROM AccessRecord r WHERE r.checkOutTime IS NULL")
    List<AccessRecord> findBelumCheckOut();

    @Query("SELECT r FROM AccessRecord r WHERE r.catatanPelanggaran IS NOT NULL")
    List<AccessRecord> findWithKendala();
}
