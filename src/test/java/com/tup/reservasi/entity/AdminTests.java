package com.tup.reservasi.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import com.tup.reservasi.auth.UserRole;
import com.tup.reservasi.enums.ReservationStatus;

/*
 * Penanggung jawab: Atha Muyassar.
 *
 * Arahan test dari class-diagram:
 * - Uji atribut Admin:
 *   unitKerja.
 * - Uji behaviour:
 *   verifikasiReservasi() mengembalikan boolean.
 *   setujuiReservasi() menghasilkan Approval setuju.
 *   tolakReservasi() menghasilkan Approval tolak.
 *   mintaRevisiData() menghasilkan Approval revisi.
 *   receiveNotification() menerima notifikasi.
 */

class AdminTests {

    @Test
    void constructorMengisiAtributUserDanUnitKerja() {
        Admin admin = sampleAdmin();

        assertEquals("ADM-001", admin.getId());
        assertEquals("Admin Satu", admin.getNama());
        assertEquals("admin@example.com", admin.getEmail());
        assertEquals("081234567891", admin.getNoHp());
        assertEquals("hash", admin.getPasswordHash());
        assertEquals("Sarpras", admin.getUnitKerja());
    }

    @Test
    void adminAdalahUserDanNotifiable() {
        Admin admin = sampleAdmin();

        assertInstanceOf(User.class, admin);
        assertInstanceOf(Notifiable.class, admin);
    }

    @Test
    void setUnitKerjaMerapikanInputKosong() {
        Admin admin = sampleAdmin();

        admin.setUnitKerja("  Akademik  ");
        assertEquals("Akademik", admin.getUnitKerja());

        admin.setUnitKerja("   ");
        assertNull(admin.getUnitKerja());
    }

    @Test
    void getRoleDanDashboardPathMengembalikanDataAdmin() {
        Admin admin = sampleAdmin();

        assertEquals(UserRole.ADMIN, admin.getRole());
        assertEquals("/admin/dashboard", admin.getDashboardPath());
    }

    @Test
    void verifikasiReservasiBenarUntukReservationPendingDanWaktuValid() {
        Admin admin = sampleAdmin();
        Reservation reservation = samplePendingReservation();

        assertTrue(admin.verifikasiReservasi(reservation));
    }

    @Test
    void verifikasiReservasiSalahUntukReservationKosongStatusBukanPendingAtauWaktuTidakValid() {
        Admin admin = sampleAdmin();
        Reservation draftReservation = sampleReservation();
        Reservation waktuTidakValid = samplePendingReservation();
        waktuTidakValid.setJamSelesai(LocalTime.of(18, 0));

        assertFalse(admin.verifikasiReservasi(null));
        assertFalse(admin.verifikasiReservasi(draftReservation));
        assertFalse(admin.verifikasiReservasi(waktuTidakValid));
    }

    @Test
    void setujuiReservasiMengubahStatusPendingMenjadiApproved() {
        Admin admin = sampleAdmin();
        Reservation reservation = samplePendingReservation();

        boolean berhasil = admin.setujuiReservasi(reservation, "Data lengkap");

        assertTrue(berhasil);
        assertEquals(ReservationStatus.APPROVED, reservation.getStatus());
    }

    @Test
    void tolakReservasiMengubahStatusPendingMenjadiRejected() {
        Admin admin = sampleAdmin();
        Reservation reservation = samplePendingReservation();

        boolean berhasil = admin.tolakReservasi(reservation, "Ruang tidak sesuai");

        assertTrue(berhasil);
        assertEquals(ReservationStatus.REJECTED, reservation.getStatus());
    }

    @Test
    void mintaRevisiDataMengubahStatusPendingMenjadiRejected() {
        Admin admin = sampleAdmin();
        Reservation reservation = samplePendingReservation();

        boolean berhasil = admin.mintaRevisiData(reservation, "Tujuan reservasi perlu diperjelas");

        assertTrue(berhasil);
        assertEquals(ReservationStatus.REJECTED, reservation.getStatus());
    }

    @Test
    void keputusanAdminDitolakJikaReservationBelumPending() {
        Admin admin = sampleAdmin();
        Reservation reservation = sampleReservation();

        boolean berhasil = admin.setujuiReservasi(reservation, "Belum diajukan");

        assertFalse(berhasil);
        assertEquals(ReservationStatus.DRAFT, reservation.getStatus());
    }

    @Test
    void receiveNotificationMencatatJumlahDanPesanTerakhir() {
        Admin admin = sampleAdmin();

        admin.receiveNotification("  Ada reservasi baru  ");
        admin.receiveNotification();

        assertEquals(2, admin.getJumlahNotifikasiDiterima());
        assertNull(admin.getNotifikasiTerakhir());
    }

    private Admin sampleAdmin() {
        return new Admin(
                "ADM-001",
                "Admin Satu",
                "admin@example.com",
                "081234567891",
                "hash",
                "  Sarpras  ");
    }

    private Reservation samplePendingReservation() {
        Reservation reservation = sampleReservation();
        reservation.ajukan();
        return reservation;
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
