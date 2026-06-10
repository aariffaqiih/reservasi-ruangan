package com.tup.reservasi.dto;

import jakarta.validation.constraints.NotBlank;

public class NotificationRequest {

    @NotBlank(message = "ID penerima tidak boleh kosong")
    private String penerimaId;

    private String penerimaRole;

    @NotBlank(message = "Pesan tidak boleh kosong")
    private String pesan;

    public NotificationRequest() {
    }

    public NotificationRequest(String penerimaId, String penerimaRole, String pesan) {
        this.penerimaId = penerimaId;
        this.penerimaRole = penerimaRole;
        this.pesan = pesan;
    }

    public String getPenerimaId() {
        return penerimaId;
    }

    public void setPenerimaId(String penerimaId) {
        this.penerimaId = penerimaId;
    }

    public String getPenerimaRole() {
        return penerimaRole;
    }

    public void setPenerimaRole(String penerimaRole) {
        this.penerimaRole = penerimaRole;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }
}
