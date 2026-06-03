package com.tup.reservasi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/*
 * Penanggung jawab: Ali Abdul Fattah 'Alim Kautsar.
 *
 * Arahan DTO request:
 * - Data Room yang perlu diinput:
 *   namaRuang: String
 *   gedung: String
 *   kapasitas: int
 *   statusAktif: boolean
 * - Behaviour terkait:
 *   aktifkan(), nonaktifkan(), ubahStatusAktif(), getInfoRuang().
 */

public class RoomRequest {

    @NotBlank(message = "Nama ruang tidak boleh kosong")
    @Size(max = 100, message = "Nama ruang maksimal 100 karakter")
    private String namaRuang;

    @NotBlank(message = "Gedung tidak boleh kosong")
    @Size(max = 100, message = "Gedung maksimal 100 karakter")
    private String gedung;

    @Min(value = 1, message = "Kapasitas minimal 1")
    private int kapasitas;

    private boolean statusAktif;

    public RoomRequest() {
    }

    public RoomRequest(String namaRuang, String gedung, int kapasitas, boolean statusAktif) {
        this.namaRuang = namaRuang;
        this.gedung = gedung;
        this.kapasitas = kapasitas;
        this.statusAktif = statusAktif;
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
}
