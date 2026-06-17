package com.tup.reservasi.controller.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tup.reservasi.entity.Notification;
import com.tup.reservasi.service.NotificationService;

/*
 * Penanggung jawab: Ajda Mutiara Zahra - 103112400210.
 * Modul: Notification.
 */
@RestController
@RequestMapping("/api/notifications")
public class NotificationRestController {

    private final NotificationService notificationService;

    public NotificationRestController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        return ResponseEntity.ok(this.notificationService.getAllNotifications());
    }

    @GetMapping("/{notificationId}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Long notificationId) {
        try {
            return ResponseEntity.ok(this.notificationService.getNotificationById(notificationId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        Notification notificationCreated = this.notificationService.createNotification(notification);
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationCreated);
    }

    @PutMapping("/{notificationId}")
    public ResponseEntity<Notification> updateNotification(@PathVariable Long notificationId,
            @RequestBody Notification notification) {
        try {
            return ResponseEntity.ok(this.notificationService.updateNotification(notificationId, notification));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{notificationId}/read")
    public ResponseEntity<Notification> tandaiDibaca(@PathVariable Long notificationId) {
        try {
            return ResponseEntity.ok(this.notificationService.tandaiDibaca(notificationId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Notification> patchTandaiDibaca(@PathVariable Long notificationId) {
        try {
            return ResponseEntity.ok(this.notificationService.tandaiDibaca(notificationId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long notificationId) {
        try {
            this.notificationService.deleteNotification(notificationId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
