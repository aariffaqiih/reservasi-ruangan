package com.tup.reservasi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tup.reservasi.entity.Notifiable;
import com.tup.reservasi.entity.Notification;
import com.tup.reservasi.entity.Reservation;
import com.tup.reservasi.repository.NotificationRepository;

/*
 * Penanggung jawab: Ajda Mutiara Zahra - 103112400210.
 * Modul: NotificationService.
 */
@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final List<Notification> notifications = new ArrayList<>();

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Transactional(readOnly = true)
    public List<Notification> getAllNotifications() {
        return this.notificationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Notification getNotificationById(Long notificationId) {
        return this.notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification tidak ditemukan"));
    }

    public Notification sendNotification(Notifiable penerima, String pesan) {
        Notification notification = new Notification();
        notification.setPenerima(penerima);
        notification.setPesan(pesan);
        notification.kirim();
        Notification saved = this.notificationRepository.save(notification);
        this.notifications.add(saved);
        return saved;
    }

    public Notification sendStatusUpdate(Notifiable penerima, Reservation reservation, String pesan) {
        Notification notification = new Notification();
        notification.setPenerima(penerima);
        notification.setReservation(reservation);
        notification.setPesan(pesan);
        notification.kirim();
        Notification saved = this.notificationRepository.save(notification);
        this.notifications.add(saved);
        return saved;
    }

    public Notification createNotification(Notification notification) {
        notification.kirim();
        Notification saved = this.notificationRepository.save(notification);
        this.notifications.add(saved);
        return saved;
    }

    public Notification updateNotification(Long notificationId, Notification updatedData) {
        Notification notificationExisting = getNotificationById(notificationId);
        notificationExisting.setPenerima(updatedData.getPenerima());
        notificationExisting.setReservation(updatedData.getReservation());
        notificationExisting.setPesan(updatedData.getPesan());
        notificationExisting.setStatusBaca(updatedData.isStatusBaca());
        notificationExisting.setCreatedAt(updatedData.getCreatedAt());
        return this.notificationRepository.save(notificationExisting);
    }

    public Notification tandaiDibaca(Long notificationId) {
        Notification notification = this.notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification tidak ditemukan"));
        notification.tandaiDibaca();
        return this.notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public List<Notification> getNotifications() {
        return this.notifications;
    }

    public void deleteNotification(Long notificationId) {
        Notification notification = getNotificationById(notificationId);
        this.notificationRepository.delete(notification);
    }
}
