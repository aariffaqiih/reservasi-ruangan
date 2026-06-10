package com.tup.reservasi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tup.reservasi.entity.Mahasiswa;
import com.tup.reservasi.entity.Notification;
import com.tup.reservasi.entity.Reservation;
import com.tup.reservasi.repository.NotificationRepository;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTests {

    private NotificationService notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    private Mahasiswa mahasiswa;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationService(notificationRepository);
        mahasiswa = new Mahasiswa("MHS-001", "Dina", "dina@example.com", "08333", "hash", "NIM-001", "IF", 2025);
    }

    @Test
    void testSendNotification() {
        String pesan = "Pesan Notifikasi";
        
        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> {
            Notification n = invocation.getArgument(0);
            return n;
        });

        Notification result = notificationService.sendNotification(mahasiswa, pesan);

        assertNotNull(result);
        assertEquals(mahasiswa, result.getPenerima());
        assertEquals(pesan, result.getPesan());
        assertFalse(result.isStatusBaca());
        assertNotNull(result.getCreatedAt());
        
        assertEquals(1, mahasiswa.getJumlahNotifikasiDiterima());
        assertEquals(pesan, mahasiswa.getNotifikasiTerakhir());

        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void testSendStatusUpdate() {
        String pesan = "Status Reservasi Diperbarui";
        Reservation dummyReservation = new Reservation();
        
        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> {
            Notification n = invocation.getArgument(0);
            return n;
        });

        Notification result = notificationService.sendStatusUpdate(mahasiswa, pesan, dummyReservation);

        assertNotNull(result);
        assertEquals(mahasiswa, result.getPenerima());
        assertEquals(pesan, result.getPesan());
        assertEquals(dummyReservation, result.getReservation());
        assertFalse(result.isStatusBaca());
        assertNotNull(result.getCreatedAt());

        assertEquals(1, mahasiswa.getJumlahNotifikasiDiterima());
        assertEquals(pesan, mahasiswa.getNotifikasiTerakhir());

        verify(notificationRepository).save(any(Notification.class));
    }
}
