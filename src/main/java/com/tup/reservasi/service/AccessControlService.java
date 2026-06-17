package com.tup.reservasi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tup.reservasi.entity.AccessRecord;
import com.tup.reservasi.entity.Reservation;
import com.tup.reservasi.entity.Satpam;
import com.tup.reservasi.entity.User;
import com.tup.reservasi.enums.ReservationStatus;
import com.tup.reservasi.repository.AccessRecordRepository;
import com.tup.reservasi.repository.ReservationRepository;
import com.tup.reservasi.repository.UserRepository;

/*
 * Penanggung jawab: Tadzkiroh Aziziyah Haqia - 103112400242.
 * Modul: AccessControlService.
 */
@Service
@Transactional
public class AccessControlService {

    private final AccessRecordRepository accessRecordRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final List<AccessRecord> accessRecords = new ArrayList<>();

    public AccessControlService(AccessRecordRepository accessRecordRepository,
            ReservationRepository reservationRepository,
            UserRepository userRepository,
            NotificationService notificationService) {
        this.accessRecordRepository = accessRecordRepository;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @Transactional(readOnly = true)
    public List<AccessRecord> getAllRecords() {
        return this.accessRecordRepository.findAll();
    }

    @Transactional(readOnly = true)
    public AccessRecord getRecordById(Long recordId) {
        return this.accessRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("AccessRecord tidak ditemukan"));
    }

    @Transactional(readOnly = true)
    public List<AccessRecord> getBelumCheckOut() {
        return this.accessRecordRepository.findByCheckOutTimeIsNull();
    }

    public AccessRecord checkIn(Satpam satpam, Reservation reservation) {
        validateReservationCanCheckIn(reservation);
        AccessRecord record = satpam.konfirmasiCheckIn(reservation);
        if (reservation != null && reservation.getAccessRecord() == record) {
            reservation.setAccessRecord(null);
        }
        unlinkRecordFromSatpam(satpam, record);
        AccessRecord saved = this.accessRecordRepository.save(record);
        linkSavedRecord(satpam, saved);
        this.accessRecords.add(saved);
        if (saved.getReservation() != null && saved.getReservation().getMahasiswa() != null) {
            this.notificationService.sendStatusUpdate(
                    saved.getReservation().getMahasiswa(),
                    saved.getReservation(),
                    "Check-in ruang berhasil dikonfirmasi");
        }
        return saved;
    }

    public AccessRecord checkIn(Long satpamId, Long reservationId) {
        return checkIn(getSatpam(satpamId), getReservation(reservationId));
    }

    public AccessRecord checkOut(Satpam satpam, Long recordId) {
        AccessRecord record = this.accessRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("AccessRecord tidak ditemukan"));
        validateRecordCanCheckOut(satpam, record);
        satpam.konfirmasiCheckOut(record);
        AccessRecord saved = this.accessRecordRepository.save(record);
        linkSavedRecord(satpam, saved);
        if (saved.getReservation() != null && saved.getReservation().getMahasiswa() != null) {
            this.notificationService.sendStatusUpdate(
                    saved.getReservation().getMahasiswa(),
                    saved.getReservation(),
                    "Check-out ruang berhasil dikonfirmasi");
        }
        return saved;
    }

    public AccessRecord checkOut(Long satpamId, Long recordId) {
        return checkOut(getSatpam(satpamId), recordId);
    }

    public void reportIssue(Satpam satpam, Long recordId, String deskripsi) {
        AccessRecord record = this.accessRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("AccessRecord tidak ditemukan"));
        satpam.catatKendala(record, deskripsi);
        this.accessRecordRepository.save(record);
        if (record.getReservation() != null && record.getReservation().getMahasiswa() != null) {
            this.notificationService.sendStatusUpdate(
                    record.getReservation().getMahasiswa(),
                    record.getReservation(),
                    "Kendala akses ruang dicatat: " + deskripsi);
        }
    }

    public void reportIssue(Long satpamId, Long recordId, String deskripsi) {
        reportIssue(getSatpam(satpamId), recordId, deskripsi);
    }

    public AccessRecord createRecord(AccessRecord record) {
        AccessRecord saved = this.accessRecordRepository.save(record);
        this.accessRecords.add(saved);
        return saved;
    }

    public AccessRecord updateRecord(Long recordId, AccessRecord updatedData) {
        AccessRecord recordExisting = getRecordById(recordId);
        recordExisting.setReservation(updatedData.getReservation());
        recordExisting.setSatpam(updatedData.getSatpam());
        recordExisting.setCheckInTime(updatedData.getCheckInTime());
        recordExisting.setCheckOutTime(updatedData.getCheckOutTime());
        recordExisting.setCatatanPelanggaran(updatedData.getCatatanPelanggaran());
        return this.accessRecordRepository.save(recordExisting);
    }

    private void unlinkRecordFromSatpam(Satpam satpam, AccessRecord record) {
        if (satpam == null || satpam.getAccessRecords() == null) {
            return;
        }
        satpam.getAccessRecords().removeIf(existing -> existing == record);
    }

    private void linkSavedRecord(Satpam satpam, AccessRecord saved) {
        if (saved.getReservation() != null) {
            saved.getReservation().setAccessRecord(saved);
            this.reservationRepository.save(saved.getReservation());
        }
        if (satpam != null && satpam.getAccessRecords() != null) {
            List<AccessRecord> satpamRecords = satpam.getAccessRecords();
            for (int i = 0; i < satpamRecords.size(); i++) {
                AccessRecord existing = satpamRecords.get(i);
                if (existing == saved || isSameRecord(existing, saved)) {
                    satpamRecords.set(i, saved);
                    return;
                }
            }
            satpamRecords.add(saved);
        }
    }

    private boolean isSameRecord(AccessRecord first, AccessRecord second) {
        if (first == second) {
            return true;
        }
        return first != null
                && second != null
                && first.getRecordId() != null
                && first.getRecordId().equals(second.getRecordId());
    }

    private void validateReservationCanCheckIn(Reservation reservation) {
        if (reservation == null) {
            throw new RuntimeException("Reservasi tidak ditemukan");
        }
        if (reservation.getStatus() != ReservationStatus.APPROVED) {
            throw new RuntimeException("Reservasi hanya dapat check-in jika status APPROVED");
        }
        if (reservation.getReservationId() != null
                && this.accessRecordRepository.findByReservation_ReservationId(reservation.getReservationId()).isPresent()) {
            throw new RuntimeException("Reservasi sudah memiliki AccessRecord");
        }
    }

    private void validateRecordCanCheckOut(Satpam satpam, AccessRecord record) {
        if (record.getCheckOutTime() != null) {
            throw new RuntimeException("AccessRecord sudah check-out");
        }
        if (record.getSatpam() == null || satpam == null || satpam.getId() == null
                || !satpam.getId().equals(record.getSatpam().getId())) {
            throw new RuntimeException("Check-out hanya dapat dilakukan oleh satpam yang melakukan check-in");
        }
    }

    private Satpam getSatpam(Long satpamId) {
        User user = this.userRepository.findById(satpamId)
                .orElseThrow(() -> new RuntimeException("Satpam tidak ditemukan"));
        if (!(user instanceof Satpam satpam)) {
            throw new RuntimeException("User bukan Satpam");
        }
        return satpam;
    }

    private Reservation getReservation(Long reservationId) {
        return this.reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservasi tidak ditemukan"));
    }

    @Transactional(readOnly = true)
    public List<AccessRecord> getAccessRecords() {
        return this.accessRecords;
    }

    public void deleteRecord(Long recordId) {
        AccessRecord record = getRecordById(recordId);
        Reservation reservation = record.getReservation();
        Satpam satpam = record.getSatpam();
        if (reservation != null && isSameRecord(reservation.getAccessRecord(), record)) {
            reservation.setAccessRecord(null);
        }
        unlinkRecordFromSatpam(satpam, record);
        record.setReservation(null);
        record.setSatpam(null);
        this.accessRecords.removeIf(existing -> isSameRecord(existing, record));
        this.accessRecordRepository.delete(record);
    }
}
