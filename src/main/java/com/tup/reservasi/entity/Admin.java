package com.tup.reservasi.entity;

import com.tup.reservasi.auth.UserRole;
import com.tup.reservasi.enums.ReservationStatus;

/*
 * Penanggung jawab: Atha Muyassar.
 *
 * Arahan dari class-diagram:
 * - Admin harus extends User.
 * - Admin mengimplementasikan behaviour Notifiable.
 * - Atribut khusus Admin:
 *   unitKerja: String
 * - Behaviour yang perlu dibuat:
 *   verifikasiReservasi(): boolean
 *   setujuiReservasi(): Approval
 *   tolakReservasi(): Approval
 *   mintaRevisiData(): Approval
 *   receiveNotification()
 * - Catatan relasi:
 *   Satu Admin menangani 0..* Approval.
 *   Setiap Approval ditangani oleh tepat 1 Admin.
 */

public class Admin extends User implements Notifiable {

    private String unitKerja;
    private int jumlahNotifikasiDiterima;
    private String notifikasiTerakhir;

    public Admin() {
        super();
    }

    public Admin(String id, String nama, String email, String noHp, String passwordHash, String unitKerja) {
        super(id, nama, email, noHp, passwordHash);
        this.unitKerja = normalizeText(unitKerja);
    }

    public boolean verifikasiReservasi(Reservation reservation) {
        return reservation != null
                && reservation.getStatus() == ReservationStatus.PENDING
                && reservation.validasiWaktu();
    }

    public boolean setujuiReservasi(Reservation reservation, String catatan) {
        return prosesKeputusan(reservation, ReservationStatus.APPROVED);
    }

    public boolean tolakReservasi(Reservation reservation, String catatan) {
        return prosesKeputusan(reservation, ReservationStatus.REJECTED);
    }

    public boolean mintaRevisiData(Reservation reservation, String catatan) {
        return prosesKeputusan(reservation, ReservationStatus.REJECTED);
    }

    @Override
    public void receiveNotification(String pesan) {
        jumlahNotifikasiDiterima++;
        notifikasiTerakhir = normalizeText(pesan);
    }

    public UserRole getRole() {
        return UserRole.ADMIN;
    }

    public String getDashboardPath() {
        return "/admin/dashboard";
    }

    public String getUnitKerja() {
        return unitKerja;
    }

    public void setUnitKerja(String unitKerja) {
        this.unitKerja = normalizeText(unitKerja);
    }

    public int getJumlahNotifikasiDiterima() {
        return jumlahNotifikasiDiterima;
    }

    public String getNotifikasiTerakhir() {
        return notifikasiTerakhir;
    }

    private boolean prosesKeputusan(Reservation reservation, ReservationStatus statusBaru) {
        if (!verifikasiReservasi(reservation)) {
            return false;
        }

        reservation.ubahStatus(statusBaru);
        return true;
    }

    private static String normalizeText(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
