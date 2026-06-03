-- Penanggung jawab: seluruh anggota kelompok.
USE reservasi_ruang;

SELECT 'users' AS table_name, COUNT(*) AS row_count FROM users
UNION ALL SELECT 'rooms', COUNT(*) FROM rooms
UNION ALL SELECT 'reservations', COUNT(*) FROM reservations
UNION ALL SELECT 'approvals', COUNT(*) FROM approvals
UNION ALL SELECT 'access_records', COUNT(*) FROM access_records
UNION ALL SELECT 'notifications', COUNT(*) FROM notifications;

SELECT status, COUNT(*) AS total
FROM reservations
GROUP BY status
ORDER BY status;

SELECT
    (SELECT COUNT(*)
     FROM reservations r
     LEFT JOIN users u ON u.id = r.mahasiswa_id
     WHERE u.id IS NULL) AS orphan_reservation_users,
    (SELECT COUNT(*)
     FROM reservations r
     LEFT JOIN rooms rm ON rm.room_id = r.room_id
     WHERE rm.room_id IS NULL) AS orphan_reservation_rooms,
    (SELECT COUNT(*)
     FROM approvals a
     LEFT JOIN reservations r ON r.reservation_id = a.reservation_id
     WHERE r.reservation_id IS NULL) AS orphan_approvals,
    (SELECT COUNT(*)
     FROM access_records ar
     LEFT JOIN reservations r ON r.reservation_id = ar.reservation_id
     WHERE r.reservation_id IS NULL) AS orphan_access_records,
    (SELECT COUNT(*)
     FROM notifications n
     LEFT JOIN users u ON u.id = n.penerima_id
     WHERE u.id IS NULL) AS orphan_notification_users;

SELECT 'reservation_owner_roles' AS audit, COUNT(*) AS invalid_count
FROM reservations r
JOIN users u ON u.id = r.mahasiswa_id
WHERE u.role <> 'MAHASISWA'
UNION ALL
SELECT 'approval_actor_roles', COUNT(*)
FROM approvals a
JOIN users u ON u.id = a.admin_id
WHERE u.role <> 'ADMIN'
UNION ALL
SELECT 'access_actor_roles', COUNT(*)
FROM access_records ar
JOIN users u ON u.id = ar.satpam_id
WHERE u.role <> 'SATPAM';
