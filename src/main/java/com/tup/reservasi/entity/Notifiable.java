package com.tup.reservasi.entity;

/*
 * Penanggung jawab: Atha Muyassar.
 *
 * Arahan dari class-diagram:
 * - Notifiable adalah kontrak untuk objek yang dapat menerima notifikasi.
 * - Behaviour yang perlu dibuat:
 *   receiveNotification(): void
 */

public interface Notifiable {

    void receiveNotification(String pesan);

    default void receiveNotification() {
        receiveNotification(null);
    }
}
