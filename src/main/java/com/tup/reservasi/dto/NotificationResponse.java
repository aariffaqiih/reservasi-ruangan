package com.tup.reservasi.dto;

import java.time.LocalDateTime;

public class NotificationResponse {

    private String notificationId;
    private String penerimaId;
    private String pesan;
    private boolean statusBaca;
    private LocalDateTime createdAt;

    public NotificationResponse() {
    }

    public NotificationResponse(String notificationId, String penerimaId, String pesan, boolean statusBaca, LocalDateTime createdAt) {
        this.notificationId = notificationId;
        this.penerimaId = penerimaId;
        this.pesan = pesan;
        this.statusBaca = statusBaca;
        this.createdAt = createdAt;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getPenerimaId() {
        return penerimaId;
    }

    public void setPenerimaId(String penerimaId) {
        this.penerimaId = penerimaId;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public boolean isStatusBaca() {
        return statusBaca;
    }

    public void setStatusBaca(boolean statusBaca) {
        this.statusBaca = statusBaca;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
