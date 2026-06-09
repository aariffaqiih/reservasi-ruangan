package com.tup.reservasi.service;

/*
 * Penanggung jawab: Tadzkiroh Aziziyah Haqia.
 *
 * Arahan dari class-diagram:
 * - Service menyimpan/mengelola:
 *   accessRecords: List<AccessRecord>
 * - Behaviour yang perlu dibuat:
 *   checkIn(): AccessRecord
 *   checkOut(): AccessRecord
 *   reportIssue()
 * - Aturan yang perlu dipikirkan saat coding:
 *   checkIn membuat/mengisi AccessRecord dengan reservation, satpam, dan checkInTime.
 *   checkOut mengisi checkOutTime untuk AccessRecord yang sudah check-in.
 *   reportIssue mengisi catatanPelanggaran atau catatan kendala.
 *   proses akses hanya untuk Reservation yang sudah valid/disetujui.
 */

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tup.reservasi.dto.AccessIssueRequest;
import com.tup.reservasi.entity.AccessRecord;
import com.tup.reservasi.entity.Reservation;
import com.tup.reservasi.entity.Satpam;
import com.tup.reservasi.enums.ReservationStatus;
import com.tup.reservasi.exception.ReservationException;
import com.tup.reservasi.repository.AccessRecordRepository;
import com.tup.reservasi.repository.ReservationRepository;
import com.tup.reservasi.repository.UserRepository;

@Service
public class AccessControlService {

    private final AccessRecordRepository accessRecordRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    public AccessControlService(
            AccessRecordRepository accessRecordRepository,
            ReservationRepository reservationRepository,
            UserRepository userRepository) {
        this.accessRecordRepository = accessRecordRepository;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public AccessRecord checkIn(String reservationId, String satpamId) {
        if (reservationId == null || reservationId.isBlank()) {
            throw new IllegalArgumentException("ID reservasi tidak boleh kosong");
        }
        if (satpamId == null || satpamId.isBlank()) {
            throw new IllegalArgumentException("ID satpam tidak boleh kosong");
        }

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException("Reservasi tidak ditemukan"));

        if (reservation.getStatus() != ReservationStatus.APPROVED) {
            throw new ReservationException("Reservasi belum disetujui, tidak dapat check-in");
        }

        boolean sudahCheckIn = accessRecordRepository.findByReservationId(reservationId).isPresent();
        if (sudahCheckIn) {
            throw new ReservationException("Reservasi ini sudah melakukan check-in");
        }

        Satpam satpam = (Satpam) userRepository.findById(satpamId)
                .orElseThrow(() -> new IllegalArgumentException("Satpam tidak ditemukan"));

        AccessRecord record = satpam.konfirmasiCheckIn(reservation);
        return accessRecordRepository.save(record);
    }

    @Transactional
    public AccessRecord checkOut(String reservationId, String satpamId) {
        if (reservationId == null || reservationId.isBlank()) {
            throw new IllegalArgumentException("ID reservasi tidak boleh kosong");
        }

        AccessRecord record = accessRecordRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new ReservationException("Data check-in untuk reservasi ini tidak ditemukan"));

        if (record.getCheckInTime() == null) {
            throw new ReservationException("Tidak dapat check-out sebelum check-in");
        }

        if (record.getCheckOutTime() != null) {
            throw new ReservationException("Reservasi ini sudah melakukan check-out");
        }

        Satpam satpam = (Satpam) userRepository.findById(satpamId)
                .orElseThrow(() -> new IllegalArgumentException("Satpam tidak ditemukan"));

        satpam.konfirmasiCheckOut(record);
        return accessRecordRepository.save(record);
    }

    @Transactional
    public AccessRecord reportIssue(AccessIssueRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request tidak boleh kosong");
        }

        AccessRecord record = accessRecordRepository.findByReservationId(request.getReservationId())
                .orElseThrow(() -> new ReservationException("Data check-in untuk reservasi ini tidak ditemukan"));

        Satpam satpam = (Satpam) userRepository.findById(request.getSatpamId())
                .orElseThrow(() -> new IllegalArgumentException("Satpam tidak ditemukan"));

        satpam.catatKendala(record, request.getCatatanPelanggaran());
        return accessRecordRepository.save(record);
    }

    public List<AccessRecord> getAllRecords() {
        return accessRecordRepository.findAll();
    }

    public List<AccessRecord> getRecordsBySatpam(String satpamId) {
        return accessRecordRepository.findBySatpamId(satpamId);
    }

    public List<AccessRecord> getBelumCheckOut() {
        return accessRecordRepository.findBelumCheckOut();
    }

    public List<AccessRecord> getRecordsWithKendala() {
        return accessRecordRepository.findWithKendala();
    }
}
