package com.tup.reservasi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tup.reservasi.entity.Notifiable;
import com.tup.reservasi.entity.Notification;
import com.tup.reservasi.entity.Reservation;
import com.tup.reservasi.repository.NotificationRepository;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final List<Notification> notifications = new ArrayList<>();

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification sendNotification(Notifiable penerima, String pesan) {
        return sendNotification(penerima, pesan, null);
    }

    @Transactional
    public Notification sendNotification(Notifiable penerima, String pesan, Reservation reservation) {
        if (penerima == null) {
            throw new IllegalArgumentException("Penerima tidak boleh kosong");
        }
        Notification notification = new Notification(penerima, pesan, reservation);
        notification.kirim();
        Notification saved = notificationRepository.save(notification);
        notifications.add(saved);
        return saved;
    }

    @Transactional
    public Notification sendStatusUpdate(Notifiable penerima, String pesan, Reservation reservation) {
        return sendNotification(penerima, pesan, reservation);
    }

    public List<Notification> getNotifications() {
        return List.copyOf(notifications);
    }

    public List<Notification> getNotificationsForPenerima(String penerimaId) {
        return notificationRepository.findByPenerimaIdOrderByCreatedAtDesc(penerimaId);
    }

    public List<Notification> getUnreadNotificationsForPenerima(String penerimaId) {
        return notificationRepository.findByPenerimaIdAndStatusBacaOrderByCreatedAtDesc(penerimaId, false);
    }

    @Transactional
    public void markAsRead(String notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.tandaiDibaca();
            notificationRepository.save(notification);
        });
    }
}
