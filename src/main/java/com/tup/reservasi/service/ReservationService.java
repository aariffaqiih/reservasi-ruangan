package com.tup.reservasi.service;

/*
 * Penanggung jawab: Ali Abdul Fattah 'Alim Kautsar.
 *
 * Arahan dari class-diagram:
 * - Service menyimpan/mengelola:
 *   reservations: List<Reservation>
 *   rooms: List<Room>
 * - Behaviour yang perlu dibuat:
 *   createReservation(): Reservation
 *   validateAvailability(): boolean
 *   cancelReservation(): boolean
 *   getReservationHistory(): List<Reservation>
 * - Aturan yang perlu dipikirkan saat coding:
 *   cek Room aktif sebelum reservasi dibuat.
 *   cek tanggal, jamMulai, jamSelesai agar tidak bentrok.
 *   gunakan Reservation.validasiWaktu() untuk validasi jam.
 *   gunakan Reservation.isCanBeCancelled() sebelum membatalkan.
 */

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tup.reservasi.dto.ReservationCancelRequest;
import com.tup.reservasi.dto.ReservationRequest;
import com.tup.reservasi.entity.Mahasiswa;
import com.tup.reservasi.entity.Reservation;
import com.tup.reservasi.entity.Room;
import com.tup.reservasi.entity.User;
import com.tup.reservasi.enums.ReservationStatus;
import com.tup.reservasi.exception.ReservationException;
import com.tup.reservasi.repository.ReservationRepository;
import com.tup.reservasi.repository.RoomRepository;
import com.tup.reservasi.repository.UserRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    // Hari kerja: reservasi hanya boleh di luar jam kuliah 07:00-17:00
    private static final LocalTime LECTURE_START = LocalTime.of(7, 0);
    private static final LocalTime LECTURE_END = LocalTime.of(17, 0);

    public ReservationService(
            ReservationRepository reservationRepository,
            RoomRepository roomRepository,
            UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Reservation createReservation(ReservationRequest request) {
        if (request == null) {
            throw new ReservationException("Request tidak boleh kosong");
        }

        // 1. Cek User/Mahasiswa
        User user = userRepository.findById(request.getMahasiswaId())
                .orElseThrow(() -> new ReservationException("Mahasiswa tidak ditemukan"));
        if (!(user instanceof Mahasiswa mahasiswa)) {
            throw new ReservationException("User bukan Mahasiswa");
        }

        // 2. Cek Room aktif
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ReservationException("Ruangan tidak ditemukan"));
        if (!room.isStatusAktif()) {
            throw new ReservationException("Ruangan tidak aktif");
        }

        // 3. Validasi Waktu
        if (!request.validasiWaktu()) {
            throw new ReservationException("Jam reservasi tidak valid (Jam mulai harus sebelum jam selesai)");
        }

        LocalDate tanggal = request.getTanggal();
        LocalTime jamMulai = request.getJamMulai();
        LocalTime jamSelesai = request.getJamSelesai();

        if (tanggal == null || tanggal.isBefore(LocalDate.now())) {
            throw new ReservationException("Tanggal reservasi tidak boleh di masa lalu");
        }

        // Hari kerja: reservasi hanya boleh di luar jam kuliah 07:00-17:00
        DayOfWeek dayOfWeek = tanggal.getDayOfWeek();
        if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
            boolean isOutsideLectureHours = jamSelesai.isBefore(LECTURE_START) || jamSelesai.equals(LECTURE_START)
                    || jamMulai.isAfter(LECTURE_END) || jamMulai.equals(LECTURE_END);
            if (!isOutsideLectureHours) {
                throw new ReservationException("Pada hari kerja, reservasi hanya boleh dilakukan di luar jam kuliah 07:00-17:00");
            }
        }

        // Validasi bentrok
        if (!validateAvailability(room.getRoomId(), tanggal, jamMulai, jamSelesai)) {
            throw new ReservationException("Jadwal bentrok dengan reservasi lain");
        }

        // 4. Buat Reservasi
        Reservation reservation = new Reservation(
                mahasiswa,
                room,
                tanggal,
                jamMulai,
                jamSelesai,
                request.getTujuan()
        );

        // Gunakan Reservation.validasiWaktu() untuk validasi internal jam
        if (!reservation.validasiWaktu()) {
            throw new ReservationException("Validasi waktu internal reservasi gagal");
        }

        // Ajukan reservasi (pindah status ke PENDING)
        reservation.ajukan();

        return reservationRepository.save(reservation);
    }

    public boolean validateAvailability(String roomId, LocalDate tanggal, LocalTime jamMulai, LocalTime jamSelesai) {
        if (roomId == null || tanggal == null || jamMulai == null || jamSelesai == null || !jamMulai.isBefore(jamSelesai)) {
            return false;
        }

        // Status yang memblokir jadwal: PENDING, APPROVED, ACTIVE
        Collection<ReservationStatus> blockingStatuses = List.of(
                ReservationStatus.PENDING,
                ReservationStatus.APPROVED,
                ReservationStatus.ACTIVE
        );

        List<Reservation> overlapping = reservationRepository.findOverlappingReservations(
                roomId,
                tanggal,
                jamMulai,
                jamSelesai,
                blockingStatuses
        );

        return overlapping.isEmpty();
    }

    @Transactional
    public boolean cancelReservation(ReservationCancelRequest request) {
        if (request == null || request.getReservationId() == null) {
            throw new ReservationException("Request pembatalan tidak valid");
        }

        Reservation reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(() -> new ReservationException("Reservasi tidak ditemukan"));

        if (!reservation.isCanBeCancelled()) {
            throw new ReservationException("Reservasi tidak dapat dibatalkan pada status " + reservation.getStatus());
        }

        String alasan = request.hasAlasanPembatalan() ? request.getAlasanPembatalan() : "Dibatalkan oleh mahasiswa";
        reservation.batalkan(alasan);
        reservationRepository.save(reservation);
        return true;
    }

    public List<Reservation> getReservationHistory(String mahasiswaId) {
        if (mahasiswaId == null) {
            throw new ReservationException("ID mahasiswa tidak boleh kosong");
        }
        return reservationRepository.findByMahasiswaId(mahasiswaId);
    }

    public List<Reservation> getReservationHistory() {
        return reservationRepository.findAll();
    }

    public Optional<Reservation> getReservationById(String reservationId) {
        if (reservationId == null) {
            return Optional.empty();
        }
        return reservationRepository.findById(reservationId);
    }
}
