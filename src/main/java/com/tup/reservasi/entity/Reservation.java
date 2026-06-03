package com.tup.reservasi.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import com.tup.reservasi.enums.ReservationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/*
 * Penanggung jawab: 'Aarif Rahmaan Jalaluddin Faqiih.
 *
 * Reservation menjadi pusat alur reservasi:
 * DRAFT -> PENDING -> APPROVED/REJECTED -> ACTIVE -> COMPLETED.
 * Pembatalan hanya boleh dari DRAFT, PENDING, atau APPROVED.
 */
@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @Column(name = "reservation_id", nullable = false, length = 36)
    private String reservationId;

    /*
     * Relasi object disiapkan untuk kebutuhan domain.
     * ID disimpan eksplisit agar class ini tetap aman saat Mahasiswa dan Room
     * belum sepenuhnya menjadi JPA entity final.
     */
    @Transient
    private Mahasiswa mahasiswa;

    @Column(name = "mahasiswa_id", nullable = false, length = 50)
    private String mahasiswaId;

    @Transient
    private Room room;

    @Column(name = "room_id", nullable = false, length = 50)
    private String roomId;

    @NotNull(message = "Tanggal reservasi tidak boleh kosong")
    @Column(name = "tanggal", nullable = false)
    private LocalDate tanggal;

    @NotNull(message = "Jam mulai tidak boleh kosong")
    @Column(name = "jam_mulai", nullable = false)
    private LocalTime jamMulai;

    @NotNull(message = "Jam selesai tidak boleh kosong")
    @Column(name = "jam_selesai", nullable = false)
    private LocalTime jamSelesai;

    @NotBlank(message = "Tujuan reservasi tidak boleh kosong")
    @Size(max = 255, message = "Tujuan reservasi maksimal 255 karakter")
    @Column(name = "tujuan", nullable = false, length = 255)
    private String tujuan;

    @NotNull(message = "Status reservasi tidak boleh kosong")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ReservationStatus status = ReservationStatus.DRAFT;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Size(max = 255, message = "Alasan pembatalan maksimal 255 karakter")
    @Column(name = "alasan_pembatalan", length = 255)
    private String alasanPembatalan;

    public Reservation() {
    }

    public Reservation(Mahasiswa mahasiswa, Room room, LocalDate tanggal, LocalTime jamMulai, LocalTime jamSelesai,
            String tujuan) {
        setMahasiswa(mahasiswa);
        setRoom(room);
        this.tanggal = tanggal;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
        this.tujuan = normalizeText(tujuan);
        this.status = ReservationStatus.DRAFT;
    }

    public static Reservation restore(String reservationId, String mahasiswaId, String roomId, LocalDate tanggal,
            LocalTime jamMulai, LocalTime jamSelesai, String tujuan, ReservationStatus status,
            LocalDateTime createdAt, LocalDateTime cancelledAt, String alasanPembatalan) {
        Reservation reservation = new Reservation();
        reservation.reservationId = reservationId;
        reservation.mahasiswaId = mahasiswaId;
        reservation.roomId = roomId;
        reservation.tanggal = tanggal;
        reservation.jamMulai = jamMulai;
        reservation.jamSelesai = jamSelesai;
        reservation.tujuan = normalizeText(tujuan);
        reservation.status = status == null ? ReservationStatus.DRAFT : status;
        reservation.createdAt = createdAt;
        reservation.cancelledAt = cancelledAt;
        reservation.alasanPembatalan = normalizeText(alasanPembatalan);
        return reservation;
    }

    @PrePersist
    public void prePersist() {
        if (reservationId == null || reservationId.isBlank()) {
            reservationId = UUID.randomUUID().toString();
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null) {
            status = ReservationStatus.DRAFT;
        }
    }

    public void ajukan() {
        ubahStatus(ReservationStatus.PENDING);
    }

    public void ubahStatus(ReservationStatus statusBaru) {
        if (statusBaru == null) {
            throw new IllegalArgumentException("Status baru tidak boleh kosong");
        }
        if (!status.canMoveTo(statusBaru)) {
            throw new IllegalStateException("Transisi status tidak valid: " + status + " -> " + statusBaru);
        }
        status = statusBaru;
    }

    public void batalkan(String alasan) {
        if (!isCanBeCancelled()) {
            throw new IllegalStateException("Reservasi dengan status " + status + " tidak dapat dibatalkan");
        }
        status = ReservationStatus.CANCELLED;
        cancelledAt = LocalDateTime.now();
        alasanPembatalan = normalizeText(alasan);
    }

    public boolean validasiWaktu() {
        return tanggal != null
                && jamMulai != null
                && jamSelesai != null
                && jamMulai.isBefore(jamSelesai);
    }

    public boolean isCanBeCancelled() {
        return status != null && status.canBeCancelled();
    }

    public boolean overlaps(LocalDate tanggalLain, LocalTime mulaiLain, LocalTime selesaiLain) {
        if (tanggalLain == null || mulaiLain == null || selesaiLain == null || !validasiWaktu()) {
            return false;
        }

        return tanggal.equals(tanggalLain)
                && jamMulai.isBefore(selesaiLain)
                && mulaiLain.isBefore(jamSelesai);
    }

    public boolean blocksRoomSchedule() {
        return status != null && status.blocksRoomSchedule();
    }

    public String getReservationId() {
        return reservationId;
    }

    public Mahasiswa getMahasiswa() {
        return mahasiswa;
    }

    public void setMahasiswa(Mahasiswa mahasiswa) {
        this.mahasiswa = mahasiswa;
        this.mahasiswaId = mahasiswa == null ? null : mahasiswa.getId();
    }

    public String getMahasiswaId() {
        return mahasiswaId;
    }

    public void setMahasiswaId(String mahasiswaId) {
        this.mahasiswaId = mahasiswaId;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
        this.roomId = room == null ? null : room.getRoomId();
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDate tanggal) {
        this.tanggal = tanggal;
    }

    public LocalTime getJamMulai() {
        return jamMulai;
    }

    public void setJamMulai(LocalTime jamMulai) {
        this.jamMulai = jamMulai;
    }

    public LocalTime getJamSelesai() {
        return jamSelesai;
    }

    public void setJamSelesai(LocalTime jamSelesai) {
        this.jamSelesai = jamSelesai;
    }

    public String getTujuan() {
        return tujuan;
    }

    public void setTujuan(String tujuan) {
        this.tujuan = normalizeText(tujuan);
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public String getAlasanPembatalan() {
        return alasanPembatalan;
    }

    private static String normalizeText(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
