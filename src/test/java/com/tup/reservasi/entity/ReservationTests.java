package com.tup.reservasi.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import com.tup.reservasi.enums.ReservationStatus;

/*
 * Penanggung jawab: 'Aarif Rahmaan Jalaluddin Faqiih.
 *
 * Arahan test dari class-diagram:
 * - Uji atribut Reservation:
 *   reservationId, mahasiswa, room, tanggal, jamMulai, jamSelesai,
 *   tujuan, status, createdAt, cancelledAt, approval, accessRecord.
 * - Uji behaviour:
 *   ajukan() mengatur status awal.
 *   ubahStatus() mengubah status sesuai alur.
 *   batalkan() mengisi cancelledAt.
 *   validasiWaktu() menolak jam selesai sebelum/sama dengan jam mulai.
 *   isCanBeCancelled() sesuai status reservasi.
 */

class ReservationTests {

    @Test
    void constructorMengisiDataAwalReservation() {
        Mahasiswa mahasiswa = sampleMahasiswa();
        Room room = sampleRoom();
        LocalDate tanggal = LocalDate.now().plusDays(1);
        LocalTime jamMulai = LocalTime.of(18, 0);
        LocalTime jamSelesai = LocalTime.of(20, 0);

        Reservation reservation = new Reservation(mahasiswa, room, tanggal, jamMulai, jamSelesai,
                "  Rapat organisasi  ");

        assertEquals(mahasiswa, reservation.getMahasiswa());
        assertEquals("MHS-001", reservation.getMahasiswaId());
        assertEquals(room, reservation.getRoom());
        assertEquals("R-001", reservation.getRoomId());
        assertEquals(tanggal, reservation.getTanggal());
        assertEquals(jamMulai, reservation.getJamMulai());
        assertEquals(jamSelesai, reservation.getJamSelesai());
        assertEquals("Rapat organisasi", reservation.getTujuan());
        assertEquals(ReservationStatus.DRAFT, reservation.getStatus());
        assertNull(reservation.getCreatedAt());
        assertNull(reservation.getCancelledAt());
    }

    @Test
    void prePersistMengisiIdCreatedAtDanStatusDefault() {
        Reservation reservation = new Reservation();

        reservation.prePersist();

        assertNotNull(reservation.getReservationId());
        assertFalse(reservation.getReservationId().isBlank());
        assertNotNull(reservation.getCreatedAt());
        assertEquals(ReservationStatus.DRAFT, reservation.getStatus());
    }

    @Test
    void ajukanMengubahStatusDraftMenjadiPending() {
        Reservation reservation = sampleReservation();

        reservation.ajukan();

        assertEquals(ReservationStatus.PENDING, reservation.getStatus());
    }

    @Test
    void ubahStatusMengikutiAlurYangValid() {
        Reservation reservation = sampleReservation();

        reservation.ubahStatus(ReservationStatus.PENDING);
        reservation.ubahStatus(ReservationStatus.APPROVED);
        reservation.ubahStatus(ReservationStatus.ACTIVE);
        reservation.ubahStatus(ReservationStatus.COMPLETED);

        assertEquals(ReservationStatus.COMPLETED, reservation.getStatus());
    }

    @Test
    void ubahStatusMenolakAlurYangTidakValid() {
        Reservation reservation = sampleReservation();

        assertThrows(IllegalStateException.class, () -> reservation.ubahStatus(ReservationStatus.ACTIVE));
        assertEquals(ReservationStatus.DRAFT, reservation.getStatus());
    }

    @Test
    void ubahStatusMenolakStatusKosong() {
        Reservation reservation = sampleReservation();

        assertThrows(IllegalArgumentException.class, () -> reservation.ubahStatus(null));
    }

    @Test
    void batalkanMengubahStatusDanMengisiDataPembatalan() {
        Reservation reservation = sampleReservation();
        reservation.ajukan();

        reservation.batalkan("  Jadwal kegiatan berubah  ");

        assertEquals(ReservationStatus.CANCELLED, reservation.getStatus());
        assertNotNull(reservation.getCancelledAt());
        assertEquals("Jadwal kegiatan berubah", reservation.getAlasanPembatalan());
        assertFalse(reservation.isCanBeCancelled());
    }

    @Test
    void batalkanDitolakJikaStatusTidakMengizinkan() {
        Reservation reservation = restoreDenganStatus(ReservationStatus.ACTIVE);

        assertThrows(IllegalStateException.class, () -> reservation.batalkan("Tidak jadi"));
        assertEquals(ReservationStatus.ACTIVE, reservation.getStatus());
        assertNull(reservation.getCancelledAt());
    }

    @Test
    void validasiWaktuBenarJikaJamMulaiSebelumJamSelesai() {
        Reservation reservation = sampleReservation();

        assertTrue(reservation.validasiWaktu());
    }

    @Test
    void validasiWaktuSalahJikaJamSelesaiSamaAtauSebelumJamMulai() {
        Reservation jamSama = sampleReservation();
        jamSama.setJamMulai(LocalTime.of(19, 0));
        jamSama.setJamSelesai(LocalTime.of(19, 0));

        Reservation jamSelesaiLebihAwal = sampleReservation();
        jamSelesaiLebihAwal.setJamMulai(LocalTime.of(20, 0));
        jamSelesaiLebihAwal.setJamSelesai(LocalTime.of(19, 0));

        assertFalse(jamSama.validasiWaktu());
        assertFalse(jamSelesaiLebihAwal.validasiWaktu());
    }

    @Test
    void isCanBeCancelledMengikutiStatusReservation() {
        assertTrue(restoreDenganStatus(ReservationStatus.DRAFT).isCanBeCancelled());
        assertTrue(restoreDenganStatus(ReservationStatus.PENDING).isCanBeCancelled());
        assertTrue(restoreDenganStatus(ReservationStatus.APPROVED).isCanBeCancelled());
        assertFalse(restoreDenganStatus(ReservationStatus.ACTIVE).isCanBeCancelled());
        assertFalse(restoreDenganStatus(ReservationStatus.COMPLETED).isCanBeCancelled());
        assertFalse(restoreDenganStatus(ReservationStatus.CANCELLED).isCanBeCancelled());
    }

    @Test
    void overlapsBenarJikaTanggalSamaDanJamBertabrakan() {
        Reservation reservation = sampleReservation();

        assertTrue(reservation.overlaps(
                reservation.getTanggal(),
                LocalTime.of(19, 0),
                LocalTime.of(21, 0)));
    }

    @Test
    void overlapsSalahJikaTanggalBerbedaAtauJamBersebelahan() {
        Reservation reservation = sampleReservation();

        assertFalse(reservation.overlaps(
                reservation.getTanggal().plusDays(1),
                LocalTime.of(19, 0),
                LocalTime.of(21, 0)));
        assertFalse(reservation.overlaps(
                reservation.getTanggal(),
                LocalTime.of(20, 0),
                LocalTime.of(21, 0)));
    }

    @Test
    void restoreMengembalikanReservationDariDataDatabase() {
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime cancelledAt = LocalDateTime.now();

        Reservation reservation = Reservation.restore(
                "RSV-001",
                "MHS-001",
                "R-001",
                LocalDate.of(2026, 6, 10),
                LocalTime.of(18, 0),
                LocalTime.of(20, 0),
                "  Diskusi tugas  ",
                ReservationStatus.CANCELLED,
                createdAt,
                cancelledAt,
                "  Dibatalkan mahasiswa  ");

        assertEquals("RSV-001", reservation.getReservationId());
        assertEquals("MHS-001", reservation.getMahasiswaId());
        assertEquals("R-001", reservation.getRoomId());
        assertEquals(LocalDate.of(2026, 6, 10), reservation.getTanggal());
        assertEquals(LocalTime.of(18, 0), reservation.getJamMulai());
        assertEquals(LocalTime.of(20, 0), reservation.getJamSelesai());
        assertEquals("Diskusi tugas", reservation.getTujuan());
        assertEquals(ReservationStatus.CANCELLED, reservation.getStatus());
        assertEquals(createdAt, reservation.getCreatedAt());
        assertEquals(cancelledAt, reservation.getCancelledAt());
        assertEquals("Dibatalkan mahasiswa", reservation.getAlasanPembatalan());
    }

    private Reservation sampleReservation() {
        return new Reservation(
                sampleMahasiswa(),
                sampleRoom(),
                LocalDate.now().plusDays(1),
                LocalTime.of(18, 0),
                LocalTime.of(20, 0),
                "Rapat organisasi");
    }

    private Reservation restoreDenganStatus(ReservationStatus status) {
        return Reservation.restore(
                "RSV-001",
                "MHS-001",
                "R-001",
                LocalDate.now().plusDays(1),
                LocalTime.of(18, 0),
                LocalTime.of(20, 0),
                "Rapat organisasi",
                status,
                LocalDateTime.now(),
                null,
                null);
    }

    private Mahasiswa sampleMahasiswa() {
        return new Mahasiswa(
                "MHS-001",
                "Mahasiswa Satu",
                "mhs@example.com",
                "081234567890",
                "hash",
                "2026001",
                "Teknik Informatika",
                2026);
    }

    private Room sampleRoom() {
        return new Room("R-001", "Ruang Seminar", "A", 40, true);
    }
}
