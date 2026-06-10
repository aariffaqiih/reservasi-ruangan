package com.tup.reservasi.controller.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tup.reservasi.dto.NotificationRequest;
import com.tup.reservasi.dto.NotificationResponse;
import com.tup.reservasi.entity.Notifiable;
import com.tup.reservasi.entity.Notification;
import com.tup.reservasi.entity.User;
import com.tup.reservasi.repository.UserRepository;
import com.tup.reservasi.service.NotificationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/notifications")
public class NotificationRestController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public NotificationRestController(NotificationService notificationService, UserRepository userRepository) {
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<NotificationResponse> getNotifications(Authentication authentication) {
        if (authentication == null) {
            throw new IllegalStateException("Pengguna belum terautentikasi");
        }
        return notificationService.getNotificationsForPenerima(authentication.getName())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @PostMapping
    public ResponseEntity<NotificationResponse> sendNotification(@Valid @RequestBody NotificationRequest request) {
        User penerima = userRepository.findById(request.getPenerimaId())
                .orElseThrow(() -> new IllegalArgumentException("Penerima dengan ID " + request.getPenerimaId() + " tidak ditemukan"));

        if (!(penerima instanceof Notifiable)) {
            throw new IllegalArgumentException("Penerima tidak dapat menerima notifikasi");
        }

        Notification notification = notificationService.sendNotification((Notifiable) penerima, request.getPesan());
        return new ResponseEntity<>(toResponse(notification), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable String id) {
        notificationService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

    private NotificationResponse toResponse(Notification notification) {
        return new NotificationResponse(
                notification.getNotificationId(),
                notification.getPenerimaId(),
                notification.getPesan(),
                notification.isStatusBaca(),
                notification.getCreatedAt()
        );
    }
}
