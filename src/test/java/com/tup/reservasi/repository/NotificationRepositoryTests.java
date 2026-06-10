package com.tup.reservasi.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.tup.reservasi.entity.Mahasiswa;
import com.tup.reservasi.entity.Notification;

@DataJpaTest
@ActiveProfiles("test")
class NotificationRepositoryTests {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    private Mahasiswa mahasiswaSatu;
    private Mahasiswa mahasiswaDua;

    @BeforeEach
    void setUp() {
        mahasiswaSatu = new Mahasiswa("MHS-001", "Dina", "dina@example.com", "08333", "hash", "NIM-001", "IF", 2025);
        userRepository.save(mahasiswaSatu);

        mahasiswaDua = new Mahasiswa("MHS-002", "Budi", "budi@example.com", "08444", "hash", "NIM-002", "SI", 2026);
        userRepository.save(mahasiswaDua);
    }

    @Test
    void testDaftarNotificationPerPenerima() {
        Notification n1 = new Notification(mahasiswaSatu, "Pesan Dina 1");
        Notification n2 = new Notification(mahasiswaSatu, "Pesan Dina 2");
        Notification n3 = new Notification(mahasiswaDua, "Pesan Budi 1");

        notificationRepository.save(n1);
        notificationRepository.save(n2);
        notificationRepository.save(n3);

        List<Notification> resultSatu = notificationRepository.findByPenerimaId(mahasiswaSatu.getId());
        assertEquals(2, resultSatu.size());

        List<Notification> resultDua = notificationRepository.findByPenerimaId(mahasiswaDua.getId());
        assertEquals(1, resultDua.size());
    }

    @Test
    void testDaftarNotificationBelumDibaca() {
        Notification n1 = new Notification(mahasiswaSatu, "Pesan 1");
        n1.tandaiDibaca(); // sudah dibaca
        Notification n2 = new Notification(mahasiswaSatu, "Pesan 2"); // belum dibaca

        notificationRepository.save(n1);
        notificationRepository.save(n2);

        List<Notification> result = notificationRepository.findByPenerimaIdAndStatusBaca(mahasiswaSatu.getId(), false);
        assertEquals(1, result.size());
        assertEquals("Pesan 2", result.get(0).getPesan());
    }

    @Test
    void testUrutanNotificationTerbaru() {
        Notification n1 = new Notification(mahasiswaSatu, "Pesan Lama");
        notificationRepository.save(n1);

        n1.setCreatedAt(LocalDateTime.now().minusDays(1));
        notificationRepository.save(n1);

        Notification n2 = new Notification(mahasiswaSatu, "Pesan Baru");
        n2.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(n2);

        List<Notification> result = notificationRepository.findByPenerimaIdOrderByCreatedAtDesc(mahasiswaSatu.getId());
        assertEquals(2, result.size());
        assertEquals("Pesan Baru", result.get(0).getPesan());
        assertEquals("Pesan Lama", result.get(1).getPesan());
    }
}
