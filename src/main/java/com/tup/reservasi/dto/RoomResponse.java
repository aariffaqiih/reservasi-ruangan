package com.tup.reservasi.dto;

/*
 * Penanggung jawab: Ali Abdul Fattah 'Alim Kautsar.
 *
 * Arahan DTO response:
 * - Data Room yang dikirim ke halaman/API:
 *   roomId: String
 *   namaRuang: String
 *   gedung: String
 *   kapasitas: int
 *   statusAktif: boolean
 *   infoRuang: String bila memakai getInfoRuang().
 */

public class RoomResponse {

    private String roomId;
    private String namaRuang;
    private String gedung;
    private int kapasitas;
    private boolean statusAktif;
    private String infoRuang;

    public RoomResponse() {
    }

    public RoomResponse(String roomId, String namaRuang, String gedung, int kapasitas, boolean statusAktif, String infoRuang) {
        this.roomId = roomId;
        this.namaRuang = namaRuang;
        this.gedung = gedung;
        this.kapasitas = kapasitas;
        this.statusAktif = statusAktif;
        this.infoRuang = infoRuang;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getNamaRuang() {
        return namaRuang;
    }

    public void setNamaRuang(String namaRuang) {
        this.namaRuang = namaRuang;
    }

    public String getGedung() {
        return gedung;
    }

    public void setGedung(String gedung) {
        this.gedung = gedung;
    }

    public int getKapasitas() {
        return kapasitas;
    }

    public void setKapasitas(int kapasitas) {
        this.kapasitas = kapasitas;
    }

    public boolean isStatusAktif() {
        return statusAktif;
    }

    public void setStatusAktif(boolean statusAktif) {
        this.statusAktif = statusAktif;
    }

    public String getInfoRuang() {
        return infoRuang;
    }

    public void setInfoRuang(String infoRuang) {
        this.infoRuang = infoRuang;
    }
}
