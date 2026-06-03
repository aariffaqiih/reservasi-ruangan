package com.tup.reservasi.dto;

/*
 * Penanggung jawab: Ali Abdul Fattah 'Alim Kautsar.
 *
 * Arahan DTO request:
 * - Data perubahan status Room:
 *   roomId: String
 *   statusAktif: boolean
 * - Behaviour terkait:
 *   aktifkan(), nonaktifkan(), ubahStatusAktif().
 */

public class RoomStatusRequest {

    private String roomId;
    private boolean statusAktif;

    public RoomStatusRequest() {
    }

    public RoomStatusRequest(String roomId, boolean statusAktif) {
        this.roomId = roomId;
        this.statusAktif = statusAktif;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public boolean isStatusAktif() {
        return statusAktif;
    }

    public void setStatusAktif(boolean statusAktif) {
        this.statusAktif = statusAktif;
    }
}
