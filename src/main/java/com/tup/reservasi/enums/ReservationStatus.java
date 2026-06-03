package com.tup.reservasi.enums;

/*
 * Penanggung jawab: 'Aarif Rahmaan Jalaluddin Faqiih.
 *
 * Status mengikuti BLUEPRINT.txt:
 * DRAFT -> PENDING/CANCELLED
 * PENDING -> APPROVED/REJECTED/CANCELLED
 * APPROVED -> ACTIVE/CANCELLED
 * ACTIVE -> COMPLETED
 */
public enum ReservationStatus {

    DRAFT("Draft"),
    PENDING("Menunggu Persetujuan"),
    APPROVED("Disetujui"),
    REJECTED("Ditolak"),
    ACTIVE("Sedang Digunakan"),
    COMPLETED("Selesai"),
    CANCELLED("Dibatalkan");

    private final String label;

    ReservationStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public boolean canBeSubmitted() {
        return this == DRAFT;
    }

    public boolean canBeReviewed() {
        return this == PENDING;
    }

    public boolean canBeCancelled() {
        return this == DRAFT || this == PENDING || this == APPROVED;
    }

    public boolean canCheckIn() {
        return this == APPROVED;
    }

    public boolean canCheckOut() {
        return this == ACTIVE;
    }

    public boolean blocksRoomSchedule() {
        return this == PENDING || this == APPROVED || this == ACTIVE;
    }

    public boolean isFinalStatus() {
        return this == REJECTED || this == COMPLETED || this == CANCELLED;
    }

    public boolean canMoveTo(ReservationStatus nextStatus) {
        if (nextStatus == null) {
            return false;
        }

        return switch (this) {
            case DRAFT -> nextStatus == PENDING || nextStatus == CANCELLED;
            case PENDING -> nextStatus == APPROVED || nextStatus == REJECTED || nextStatus == CANCELLED;
            case APPROVED -> nextStatus == ACTIVE || nextStatus == CANCELLED;
            case ACTIVE -> nextStatus == COMPLETED;
            case REJECTED, COMPLETED, CANCELLED -> false;
        };
    }
}
