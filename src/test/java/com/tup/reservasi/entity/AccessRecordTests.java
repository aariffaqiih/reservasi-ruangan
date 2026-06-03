package com.tup.reservasi.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

/*
 * Penanggung jawab: Tadzkiroh Aziziyah Haqia.
 *
 * Arahan test dari class-diagram:
 * - Uji atribut AccessRecord:
 *   recordId, reservation, satpam, checkInTime, checkOutTime, catatanPelanggaran.
 * - Uji behaviour:
 *   checkIn() mengisi checkInTime.
 *   checkOut() mengisi checkOutTime.
 *   laporkanKendala() mengisi catatanPelanggaran.
 */
class AccessRecordTests {

    @Test
    void testAtributAccessRecord() {
        Reservation reservation = sampleReservation();
        Satpam satpam = sampleSatpam();
        
        AccessRecord record = new AccessRecord(reservation, satpam);
        
        assertEquals(reservation, record.getReservation());
        assertEquals(reservation.getReservationId(), record.getReservationId());
        assertEquals(satpam, record.getSatpam());
        assertEquals(satpam.getId(), record.getSatpamId());
        
        assertNull(record.getCheckInTime());
        assertNull(record.getCheckOutTime());
        assertNull(record.getCatatanPelanggaran());
        
        record.setRecordId("REC-123");
        assertEquals("REC-123", record.getRecordId());
        
        LocalDateTime now = LocalDateTime.now();
        record.setCheckInTime(now);
        record.setCheckOutTime(now.plusHours(2));
        record.setCatatanPelanggaran("  AC Bocor  ");
        
        assertEquals(now, record.getCheckInTime());
        assertEquals(now.plusHours(2), record.getCheckOutTime());
        assertEquals("AC Bocor", record.getCatatanPelanggaran());
    }

    @Test
    void testCheckIn() {
        AccessRecord record = new AccessRecord();
        assertNull(record.getCheckInTime());
        
        record.checkIn();
        
        assertNotNull(record.getCheckInTime());
    }

    @Test
    void testCheckOut() {
        AccessRecord record = new AccessRecord();
        assertNull(record.getCheckOutTime());
        
        record.checkOut();
        
        assertNotNull(record.getCheckOutTime());
    }

    @Test
    void testLaporkanKendala() {
        AccessRecord record = new AccessRecord();
        
        record.laporkanKendala("  Lampu mati  ");
        assertEquals("Lampu mati", record.getCatatanPelanggaran());
        
        record.laporkanKendala();
        assertEquals("Kendala tidak spesifik", record.getCatatanPelanggaran());
        
        assertThrows(IllegalArgumentException.class, () -> record.laporkanKendala(null));
        assertThrows(IllegalArgumentException.class, () -> record.laporkanKendala("   "));
    }

    @Test
    void testRestore() {
        LocalDateTime checkIn = LocalDateTime.now().minusHours(2);
        LocalDateTime checkOut = LocalDateTime.now().minusHours(1);
        
        AccessRecord record = AccessRecord.restore(
                "REC-001",
                "RSV-001",
                "STM-001",
                checkIn,
                checkOut,
                "  Pintu rusak  "
        );
        
        assertEquals("REC-001", record.getRecordId());
        assertEquals("RSV-001", record.getReservationId());
        assertEquals("STM-001", record.getSatpamId());
        assertEquals(checkIn, record.getCheckInTime());
        assertEquals(checkOut, record.getCheckOutTime());
        assertEquals("Pintu rusak", record.getCatatanPelanggaran());
    }

    private Reservation sampleReservation() {
        Mahasiswa mahasiswa = new Mahasiswa("M-001", "Mhs Satu", "mhs@example.com", "0812", "hash", "NIM-001", "IF", 2026);
        Room room = new Room("R-001", "Ruang Seminar", "A", 40, true);
        return new Reservation(mahasiswa, room, LocalDate.now(), LocalTime.of(8, 0), LocalTime.of(10, 0), "Rapat");
    }

    private Satpam sampleSatpam() {
        return new Satpam("S-001", "Satpam Satu", "satpam@example.com", "0812345678", "hash", "Pagi", "Pos 1");
    }
}
