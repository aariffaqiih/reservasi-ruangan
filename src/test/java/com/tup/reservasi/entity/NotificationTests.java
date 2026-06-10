package com.tup.reservasi.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class NotificationTests {

    @Test
    void testAtributNotification() {
        Mahasiswa mahasiswa = new Mahasiswa("MHS-001", "Dina", "dina@example.com", "08333", "hash", "NIM-001", "IF", 2025);
        Notification notification = new Notification(mahasiswa, "Pesan Uji");

        assertNull(notification.getNotificationId());
        assertEquals("Pesan Uji", notification.getPesan());
        assertFalse(notification.isStatusBaca());
        assertNotNull(notification.getCreatedAt());
        assertEquals(mahasiswa, notification.getPenerima());
        assertEquals("MHS-001", notification.getPenerimaId());
        assertEquals("Dina", notification.getPenerimaNama());
    }

    @Test
    void testKirimNotifikasi() {
        Mahasiswa mahasiswa = new Mahasiswa("MHS-001", "Dina", "dina@example.com", "08333", "hash", "NIM-001", "IF", 2025);
        Notification notification = new Notification(mahasiswa, "Pesan Notifikasi Baru");
        
        notification.kirim();
        
        assertEquals(1, mahasiswa.getJumlahNotifikasiDiterima());
        assertEquals("Pesan Notifikasi Baru", mahasiswa.getNotifikasiTerakhir());
    }

    @Test
    void testTandaiDibaca() {
        Mahasiswa mahasiswa = new Mahasiswa("MHS-001", "Dina", "dina@example.com", "08333", "hash", "NIM-001", "IF", 2025);
        Notification notification = new Notification(mahasiswa, "Pesan Uji");
        
        assertFalse(notification.isStatusBaca());
        notification.tandaiDibaca();
        assertTrue(notification.isStatusBaca());
    }

    @Test
    void testAtributNotificationWithReservation() {
        Mahasiswa mahasiswa = new Mahasiswa("MHS-001", "Dina", "dina@example.com", "08333", "hash", "NIM-001", "IF", 2025);
        Room room = new Room("RM-001", "Lab Komputer", "B", 30, true);
        java.time.LocalDate tanggal = java.time.LocalDate.now();
        java.time.LocalTime mulai = java.time.LocalTime.of(9, 0);
        java.time.LocalTime selesai = java.time.LocalTime.of(11, 0);
        Reservation reservation = new Reservation(mahasiswa, room, tanggal, mulai, selesai, "Seminar");
        
        Notification notification = new Notification(mahasiswa, "Ada reservasi", reservation);
        
        assertEquals(mahasiswa, notification.getPenerima());
        assertEquals("Ada reservasi", notification.getPesan());
        assertEquals(reservation, notification.getReservation());
    }
}
