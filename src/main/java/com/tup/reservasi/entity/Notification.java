package com.tup.reservasi.entity;

/*
 * Penanggung jawab: Ajda Mutiara Zahra.
 *
 * Arahan dari class-diagram:
 * - Atribut yang perlu disiapkan:
 *   notificationId: String
 *   penerima: Notifiable
 *   pesan: String
 *   statusBaca: boolean
 *   createdAt: LocalDateTime
 * - Behaviour yang perlu dibuat:
 *   kirim()
 *   tandaiDibaca()
 * - Catatan relasi:
 *   Notification dikirim ke objek penerima yang memenuhi kontrak Notifiable.
 *   NotificationService menyimpan/mengelola 0..* Notification.
 */
