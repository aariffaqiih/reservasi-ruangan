package com.tup.reservasi.entity;

import java.time.LocalDateTime;
import com.tup.reservasi.auth.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

/*
 * Penanggung jawab: Tadzkiroh Aziziyah Haqia.
 *
 * Arahan dari class-diagram:
 * - Satpam harus extends User.
 * - Satpam mengimplementasikan behaviour Notifiable.
 * - Atribut khusus Satpam:
 *   shift: String
 *   posJaga: String
 * - Behaviour yang perlu dibuat:
 *   konfirmasiCheckIn(): AccessRecord
 *   konfirmasiCheckOut(): AccessRecord
 *   catatKendala()
 *   receiveNotification()
 * - Catatan relasi:
 *   Satu Satpam dapat menangani 0..* AccessRecord.
 *   Setiap AccessRecord ditangani oleh tepat 1 Satpam.
 */
@Entity
@DiscriminatorValue("SATPAM")
public class Satpam extends User implements Notifiable {

    @Column(length = 50)
    private String shift;

    @Column(name = "pos_jaga", length = 100)
    private String posJaga;

    @Transient
    private int jumlahNotifikasiDiterima;

    @Transient
    private String notifikasiTerakhir;

    public Satpam() {
        super();
        setRole(UserRole.SATPAM);
    }

    public Satpam(String id, String nama, String email, String noHp, String passwordHash, String shift, String posJaga) {
        super(id, nama, email, noHp, passwordHash);
        this.shift = normalizeText(shift);
        this.posJaga = normalizeText(posJaga);
        setRole(UserRole.SATPAM);
    }

    public AccessRecord konfirmasiCheckIn(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservasi tidak boleh kosong");
        }
        AccessRecord record = new AccessRecord();
        record.setReservation(reservation);
        record.setSatpam(this);
        record.checkIn();
        return record;
    }

    public AccessRecord konfirmasiCheckOut(AccessRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("AccessRecord tidak boleh kosong");
        }
        record.checkOut();
        return record;
    }

    public void catatKendala(AccessRecord record, String deskripsi) {
        if (record == null) {
            throw new IllegalArgumentException("AccessRecord tidak boleh kosong");
        }
        record.laporkanKendala(deskripsi);
    }

    @Override
    public void receiveNotification(String pesan) {
        this.jumlahNotifikasiDiterima++;
        this.notifikasiTerakhir = normalizeText(pesan);
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = normalizeText(shift);
    }

    public String getPosJaga() {
        return posJaga;
    }

    public void setPosJaga(String posJaga) {
        this.posJaga = normalizeText(posJaga);
    }

    public int getJumlahNotifikasiDiterima() {
        return jumlahNotifikasiDiterima;
    }

    public String getNotifikasiTerakhir() {
        return notifikasiTerakhir;
    }

    private static String normalizeText(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
