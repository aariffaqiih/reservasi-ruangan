package com.tup.reservasi.service;

/*
 * Penanggung jawab: Ajda Mutiara Zahra.
 *
 * Arahan dari class-diagram:
 * - Service menyimpan/mengelola:
 *   notifications: List<Notification>
 * - Behaviour yang perlu dibuat:
 *   sendNotification(): Notification
 *   sendStatusUpdate(): Notification
 * - Aturan yang perlu dipikirkan saat coding:
 *   Notification berisi penerima, pesan, statusBaca, dan createdAt.
 *   sendStatusUpdate dipakai saat status Reservation atau Approval berubah.
 *   penerima harus objek yang dapat receiveNotification().
 */
