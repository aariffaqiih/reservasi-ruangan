package com.tup.reservasi.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tup.reservasi.entity.Mahasiswa;
import com.tup.reservasi.entity.Reservation;
import com.tup.reservasi.entity.Room;
import com.tup.reservasi.entity.User;
import com.tup.reservasi.enums.ReservationStatus;
import com.tup.reservasi.repository.AccessRecordRepository;
import com.tup.reservasi.repository.ApprovalRepository;
import com.tup.reservasi.repository.NotificationRepository;
import com.tup.reservasi.repository.ReservationRepository;
import com.tup.reservasi.repository.RoomRepository;
import com.tup.reservasi.repository.UserRepository;

/*
 * Penanggung jawab: Ali Abdul Fattah 'Alim Kautsar - 103112400213.
 * Modul: ReservationService.
 */
@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final ApprovalRepository approvalRepository;
    private final AccessRecordRepository accessRecordRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final List<Reservation> reservations = new ArrayList<>();
    private final List<Room> rooms = new ArrayList<>();

    public ReservationService(ReservationRepository reservationRepository,
            RoomRepository roomRepository,
            UserRepository userRepository,
            ApprovalRepository approvalRepository,
            AccessRecordRepository accessRecordRepository,
            NotificationRepository notificationRepository,
            NotificationService notificationService) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.approvalRepository = approvalRepository;
        this.accessRecordRepository = accessRecordRepository;
        this.notificationRepository = notificationRepository;
        this.notificationService = notificationService;
    }

    @Transactional(readOnly = true)
    public List<Reservation> getAllReservations() {
        return this.reservationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Reservation getReservationById(Long reservationId) {
        return this.reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservasi tidak ditemukan"));
    }

    public Reservation createReservation(Mahasiswa mahasiswa, Room room, LocalDate tanggal,
            LocalTime jamMulai, LocalTime jamSelesai, String tujuan) {
        if (!validateAvailability(room, tanggal, jamMulai, jamSelesai)) {
            throw new RuntimeException("Ruang tidak tersedia");
        }
        Reservation reservation = mahasiswa.ajukanReservasi(room, tanggal, jamMulai, jamSelesai, tujuan);
        Reservation saved = this.reservationRepository.save(reservation);
        this.reservations.add(saved);
        this.notificationService.sendStatusUpdate(mahasiswa, saved, "Reservasi berhasil diajukan");
        return saved;
    }

    public Reservation createReservation(Reservation reservation) {
        resolveReservationRelations(reservation);
        if (reservation.getRoom() != null
                && !validateAvailability(
                        reservation.getRoom(),
                        reservation.getTanggal(),
                        reservation.getJamMulai(),
                        reservation.getJamSelesai())) {
            throw new RuntimeException("Ruang tidak tersedia");
        }
        reservation.ajukan();
        Reservation saved = this.reservationRepository.save(reservation);
        this.reservations.add(saved);
        if (saved.getMahasiswa() != null) {
            this.notificationService.sendStatusUpdate(saved.getMahasiswa(), saved, "Reservasi berhasil diajukan");
        }
        return saved;
    }

    public Reservation updateReservation(Long reservationId, Reservation updatedData) {
        resolveReservationRelations(updatedData);
        Reservation reservationExisting = getReservationById(reservationId);
        reservationExisting.setMahasiswa(updatedData.getMahasiswa());
        reservationExisting.setRoom(updatedData.getRoom());
        reservationExisting.setTanggal(updatedData.getTanggal());
        reservationExisting.setJamMulai(updatedData.getJamMulai());
        reservationExisting.setJamSelesai(updatedData.getJamSelesai());
        reservationExisting.setTujuan(updatedData.getTujuan());
        reservationExisting.setStatus(updatedData.getStatus());
        return this.reservationRepository.save(reservationExisting);
    }

    public void deleteReservation(Long reservationId) {
        if (!this.reservationRepository.existsById(reservationId)) {
            throw new RuntimeException("Reservasi tidak ditemukan");
        }
        deleteReservationRelations(reservationId);
        this.reservationRepository.deleteByReservationId(reservationId);
    }

    public Reservation createReservation(Long mahasiswaId, Long roomId, LocalDate tanggal,
            LocalTime jamMulai, LocalTime jamSelesai, String tujuan) {
        User user = this.userRepository.findById(mahasiswaId)
                .orElseThrow(() -> new RuntimeException("Mahasiswa tidak ditemukan"));
        if (!(user instanceof Mahasiswa mahasiswa)) {
            throw new RuntimeException("User bukan Mahasiswa");
        }
        Room room = this.roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room tidak ditemukan"));
        return createReservation(mahasiswa, room, tanggal, jamMulai, jamSelesai, tujuan);
    }

    public boolean validateAvailability(Room room, LocalDate tanggal, LocalTime jamMulai, LocalTime jamSelesai) {
        if (room == null || !room.isStatusAktif()) {
            return false;
        }
        if (tanggal == null || jamMulai == null || jamSelesai == null || !jamSelesai.isAfter(jamMulai)) {
            return false;
        }
        List<ReservationStatus> statuses = List.of(
                ReservationStatus.PENDING,
                ReservationStatus.APPROVED,
                ReservationStatus.ACTIVE);
        return this.reservationRepository
                .findOverlappingReservations(room.getRoomId(), tanggal, jamMulai, jamSelesai, statuses)
                .isEmpty();
    }

    public boolean cancelReservation(Long reservationId, String alasan) {
        Reservation reservation = getReservationById(reservationId);
        if (!reservation.isCanBeCancelled()) {
            return false;
        }
        reservation.batalkan(alasan);
        this.reservationRepository.save(reservation);
        if (reservation.getMahasiswa() != null) {
            this.notificationService.sendStatusUpdate(reservation.getMahasiswa(), reservation, "Reservasi dibatalkan");
        }
        return true;
    }

    @Transactional(readOnly = true)
    public List<Reservation> getReservationHistory(Long userId) {
        return this.reservationRepository.findByMahasiswa_Id(userId);
    }

    public List<Reservation> getReservations() {
        return this.reservations;
    }

    public List<Room> getRooms() {
        this.rooms.clear();
        this.rooms.addAll(this.roomRepository.findAll());
        return this.rooms;
    }

    private void resolveReservationRelations(Reservation reservation) {
        if (reservation == null) {
            return;
        }
        reservation.setMahasiswa(resolveMahasiswa(reservation.getMahasiswa()));
        reservation.setRoom(resolveRoom(reservation.getRoom()));
    }

    private Mahasiswa resolveMahasiswa(Mahasiswa mahasiswa) {
        if (mahasiswa == null || mahasiswa.getId() == null) {
            return mahasiswa;
        }
        User user = this.userRepository.findById(mahasiswa.getId())
                .orElseThrow(() -> new RuntimeException("Mahasiswa tidak ditemukan"));
        if (!(user instanceof Mahasiswa resolvedMahasiswa)) {
            throw new RuntimeException("User bukan Mahasiswa");
        }
        return resolvedMahasiswa;
    }

    private Room resolveRoom(Room room) {
        if (room == null || room.getRoomId() == null) {
            return room;
        }
        return this.roomRepository.findById(room.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room tidak ditemukan"));
    }

    private void deleteReservationRelations(Long reservationId) {
        this.notificationRepository.deleteByReservationId(reservationId);
        this.accessRecordRepository.deleteByReservationId(reservationId);
        this.approvalRepository.deleteByReservationId(reservationId);
    }
}
