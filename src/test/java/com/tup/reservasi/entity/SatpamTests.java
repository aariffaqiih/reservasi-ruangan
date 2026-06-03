package com.tup.reservasi.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

/*
 * Penanggung jawab: Tadzkiroh Aziziyah Haqia.
 *
 * Arahan test dari class-diagram:
 * - Uji atribut Satpam:
 *   shift, posJaga.
 * - Uji behaviour:
 *   konfirmasiCheckIn() menghasilkan AccessRecord dengan checkInTime.
 *   konfirmasiCheckOut() menghasilkan AccessRecord dengan checkOutTime.
 *   catatKendala() menyimpan catatan kendala.
 *   receiveNotification() menerima notifikasi.
 */
class SatpamTests {

    @Test
    void testAtributSatpam() {
        Satpam satpam = new Satpam("S-001", "Satpam Satu", "satpam@example.com", "0812345678", "hash", "Pagi", "Pos 1");
        
        assertEquals("Pagi", satpam.getShift());
        assertEquals("Pos 1", satpam.getPosJaga());
        
        satpam.setShift("  Malam  ");
        satpam.setPosJaga("  Pos Utara  ");
        
        assertEquals("Malam", satpam.getShift());
        assertEquals("Pos Utara", satpam.getPosJaga());
    }

    @Test
    void testKonfirmasiCheckIn() {
        Satpam satpam = new Satpam("S-001", "Satpam Satu", "satpam@example.com", "0812345678", "hash", "Pagi", "Pos 1");
        Reservation reservation = sampleReservation();
        
        AccessRecord record = satpam.konfirmasiCheckIn(reservation);
        
        assertNotNull(record);
        assertEquals(reservation, record.getReservation());
        assertEquals(satpam, record.getSatpam());
        assertNotNull(record.getCheckInTime());
        assertNull(record.getCheckOutTime());
    }

    @Test
    void testKonfirmasiCheckOut() {
        Satpam satpam = new Satpam("S-001", "Satpam Satu", "satpam@example.com", "0812345678", "hash", "Pagi", "Pos 1");
        Reservation reservation = sampleReservation();
        AccessRecord record = new AccessRecord(reservation, satpam);
        
        AccessRecord updatedRecord = satpam.konfirmasiCheckOut(record);
        
        assertNotNull(updatedRecord);
        assertNotNull(updatedRecord.getCheckOutTime());
    }

    @Test
    void testCatatKendala() {
        Satpam satpam = new Satpam("S-001", "Satpam Satu", "satpam@example.com", "0812345678", "hash", "Pagi", "Pos 1");
        Reservation reservation = sampleReservation();
        AccessRecord record = new AccessRecord(reservation, satpam);
        
        satpam.catatKendala(record, "  Kunci terlambat dikembalikan  ");
        
        assertEquals("Kunci terlambat dikembalikan", record.getCatatanPelanggaran());
    }

    @Test
    void testReceiveNotification() {
        Satpam satpam = new Satpam();
        
        assertEquals(0, satpam.getJumlahNotifikasiDiterima());
        assertNull(satpam.getNotifikasiTerakhir());
        
        satpam.receiveNotification("  Ada penyusup  ");
        
        assertEquals(1, satpam.getJumlahNotifikasiDiterima());
        assertEquals("Ada penyusup", satpam.getNotifikasiTerakhir());
    }

    private Reservation sampleReservation() {
        Mahasiswa mahasiswa = new Mahasiswa("M-001", "Mhs Satu", "mhs@example.com", "0812", "hash", "NIM-001", "IF", 2026);
        Room room = new Room("R-001", "Ruang Seminar", "A", 40, true);
        return new Reservation(mahasiswa, room, LocalDate.now(), LocalTime.of(8, 0), LocalTime.of(10, 0), "Rapat");
    }
}
