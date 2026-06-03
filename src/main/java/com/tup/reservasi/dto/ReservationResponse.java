package com.tup.reservasi.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.tup.reservasi.entity.Mahasiswa;
import com.tup.reservasi.entity.Reservation;
import com.tup.reservasi.entity.Room;
import com.tup.reservasi.enums.ReservationStatus;

/*
 * Penanggung jawab: 'Aarif Rahmaan Jalaluddin Faqiih.
 *
 * Arahan DTO response:
 * - Data Reservation yang dikirim:
 *   reservationId: String
 *   mahasiswa: ringkasan Mahasiswa
 *   room: ringkasan Room
 *   tanggal: LocalDate
 *   jamMulai: LocalTime
 *   jamSelesai: LocalTime
 *   tujuan: String
 *   status: ReservationStatus
 *   createdAt: LocalDateTime
 *   cancelledAt: LocalDateTime
 *   approval: ringkasan Approval bila ada
 *   accessRecord: ringkasan AccessRecord bila ada
 */

public class ReservationResponse {

    private String reservationId;

    private String mahasiswaId;
    private UserResponse mahasiswa;

    private String roomId;
    private RoomResponse room;

    private LocalDate tanggal;
    private LocalTime jamMulai;
    private LocalTime jamSelesai;
    private String tujuan;

    private ReservationStatus status;
    private String statusLabel;

    private LocalDateTime createdAt;
    private LocalDateTime cancelledAt;
    private String alasanPembatalan;

    private boolean canBeCancelled;
    private boolean canCheckIn;
    private boolean canCheckOut;

    private ApprovalSummary approval;
    private AccessRecordSummary accessRecord;

    public ReservationResponse() {
    }

    public ReservationResponse(String reservationId, String mahasiswaId, UserResponse mahasiswa, String roomId,
            RoomResponse room, LocalDate tanggal, LocalTime jamMulai, LocalTime jamSelesai, String tujuan,
            ReservationStatus status, String statusLabel, LocalDateTime createdAt, LocalDateTime cancelledAt,
            String alasanPembatalan, boolean canBeCancelled, boolean canCheckIn, boolean canCheckOut,
            ApprovalSummary approval, AccessRecordSummary accessRecord) {
        this.reservationId = reservationId;
        this.mahasiswaId = mahasiswaId;
        this.mahasiswa = mahasiswa;
        this.roomId = roomId;
        this.room = room;
        this.tanggal = tanggal;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
        this.tujuan = tujuan;
        this.status = status;
        this.statusLabel = statusLabel;
        this.createdAt = createdAt;
        this.cancelledAt = cancelledAt;
        this.alasanPembatalan = alasanPembatalan;
        this.canBeCancelled = canBeCancelled;
        this.canCheckIn = canCheckIn;
        this.canCheckOut = canCheckOut;
        this.approval = approval;
        this.accessRecord = accessRecord;
    }

    public static ReservationResponse from(Reservation reservation) {
        if (reservation == null) {
            return null;
        }

        ReservationStatus status = reservation.getStatus();

        ReservationResponse response = new ReservationResponse();
        response.setReservationId(reservation.getReservationId());
        response.setMahasiswaId(reservation.getMahasiswaId());
        response.setMahasiswa(toUserResponse(reservation.getMahasiswa()));
        response.setRoomId(reservation.getRoomId());
        response.setRoom(toRoomResponse(reservation.getRoom()));
        response.setTanggal(reservation.getTanggal());
        response.setJamMulai(reservation.getJamMulai());
        response.setJamSelesai(reservation.getJamSelesai());
        response.setTujuan(reservation.getTujuan());
        response.setStatus(status);
        response.setStatusLabel(status == null ? null : status.getLabel());
        response.setCreatedAt(reservation.getCreatedAt());
        response.setCancelledAt(reservation.getCancelledAt());
        response.setAlasanPembatalan(reservation.getAlasanPembatalan());
        response.setCanBeCancelled(status != null && status.canBeCancelled());
        response.setCanCheckIn(status != null && status.canCheckIn());
        response.setCanCheckOut(status != null && status.canCheckOut());
        return response;
    }

    private static UserResponse toUserResponse(Mahasiswa mahasiswa) {
        if (mahasiswa == null) {
            return null;
        }

        UserResponse response = new UserResponse();
        response.setId(mahasiswa.getId());
        response.setNama(mahasiswa.getNama());
        response.setEmail(mahasiswa.getEmail());
        response.setNoHp(mahasiswa.getNoHp());
        response.setNim(mahasiswa.getNim());
        response.setProdi(mahasiswa.getProdi());
        response.setAngkatan(mahasiswa.getAngkatan());
        return response;
    }

    private static RoomResponse toRoomResponse(Room room) {
        if (room == null) {
            return null;
        }

        return new RoomResponse(
                room.getRoomId(),
                room.getNamaRuang(),
                room.getGedung(),
                room.getKapasitas(),
                room.isStatusAktif(),
                room.getInfoRuang());
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getMahasiswaId() {
        return mahasiswaId;
    }

    public void setMahasiswaId(String mahasiswaId) {
        this.mahasiswaId = mahasiswaId;
    }

    public UserResponse getMahasiswa() {
        return mahasiswa;
    }

    public void setMahasiswa(UserResponse mahasiswa) {
        this.mahasiswa = mahasiswa;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public RoomResponse getRoom() {
        return room;
    }

    public void setRoom(RoomResponse room) {
        this.room = room;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDate tanggal) {
        this.tanggal = tanggal;
    }

    public LocalTime getJamMulai() {
        return jamMulai;
    }

    public void setJamMulai(LocalTime jamMulai) {
        this.jamMulai = jamMulai;
    }

    public LocalTime getJamSelesai() {
        return jamSelesai;
    }

    public void setJamSelesai(LocalTime jamSelesai) {
        this.jamSelesai = jamSelesai;
    }

    public String getTujuan() {
        return tujuan;
    }

    public void setTujuan(String tujuan) {
        this.tujuan = tujuan;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public String getAlasanPembatalan() {
        return alasanPembatalan;
    }

    public void setAlasanPembatalan(String alasanPembatalan) {
        this.alasanPembatalan = alasanPembatalan;
    }

    public boolean isCanBeCancelled() {
        return canBeCancelled;
    }

    public void setCanBeCancelled(boolean canBeCancelled) {
        this.canBeCancelled = canBeCancelled;
    }

    public boolean isCanCheckIn() {
        return canCheckIn;
    }

    public void setCanCheckIn(boolean canCheckIn) {
        this.canCheckIn = canCheckIn;
    }

    public boolean isCanCheckOut() {
        return canCheckOut;
    }

    public void setCanCheckOut(boolean canCheckOut) {
        this.canCheckOut = canCheckOut;
    }

    public ApprovalSummary getApproval() {
        return approval;
    }

    public void setApproval(ApprovalSummary approval) {
        this.approval = approval;
    }

    public AccessRecordSummary getAccessRecord() {
        return accessRecord;
    }

    public void setAccessRecord(AccessRecordSummary accessRecord) {
        this.accessRecord = accessRecord;
    }

    public static class ApprovalSummary {

        private String approvalId;
        private String reservationId;
        private String adminId;
        private String keputusan;
        private String catatan;
        private LocalDateTime reviewedAt;

        public ApprovalSummary() {
        }

        public ApprovalSummary(String approvalId, String reservationId, String adminId,
                String keputusan, String catatan, LocalDateTime reviewedAt) {
            this.approvalId = approvalId;
            this.reservationId = reservationId;
            this.adminId = adminId;
            this.keputusan = keputusan;
            this.catatan = catatan;
            this.reviewedAt = reviewedAt;
        }

        public String getApprovalId() {
            return approvalId;
        }

        public void setApprovalId(String approvalId) {
            this.approvalId = approvalId;
        }

        public String getReservationId() {
            return reservationId;
        }

        public void setReservationId(String reservationId) {
            this.reservationId = reservationId;
        }

        public String getAdminId() {
            return adminId;
        }

        public void setAdminId(String adminId) {
            this.adminId = adminId;
        }

        public String getKeputusan() {
            return keputusan;
        }

        public void setKeputusan(String keputusan) {
            this.keputusan = keputusan;
        }

        public String getCatatan() {
            return catatan;
        }

        public void setCatatan(String catatan) {
            this.catatan = catatan;
        }

        public LocalDateTime getReviewedAt() {
            return reviewedAt;
        }

        public void setReviewedAt(LocalDateTime reviewedAt) {
            this.reviewedAt = reviewedAt;
        }
    }

    public static class AccessRecordSummary {

        private String recordId;
        private String reservationId;
        private String satpamId;
        private LocalDateTime checkInTime;
        private LocalDateTime checkOutTime;
        private String catatanPelanggaran;

        public AccessRecordSummary() {
        }

        public AccessRecordSummary(String recordId, String reservationId, String satpamId,
                LocalDateTime checkInTime, LocalDateTime checkOutTime, String catatanPelanggaran) {
            this.recordId = recordId;
            this.reservationId = reservationId;
            this.satpamId = satpamId;
            this.checkInTime = checkInTime;
            this.checkOutTime = checkOutTime;
            this.catatanPelanggaran = catatanPelanggaran;
        }

        public String getRecordId() {
            return recordId;
        }

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }

        public String getReservationId() {
            return reservationId;
        }

        public void setReservationId(String reservationId) {
            this.reservationId = reservationId;
        }

        public String getSatpamId() {
            return satpamId;
        }

        public void setSatpamId(String satpamId) {
            this.satpamId = satpamId;
        }

        public LocalDateTime getCheckInTime() {
            return checkInTime;
        }

        public void setCheckInTime(LocalDateTime checkInTime) {
            this.checkInTime = checkInTime;
        }

        public LocalDateTime getCheckOutTime() {
            return checkOutTime;
        }

        public void setCheckOutTime(LocalDateTime checkOutTime) {
            this.checkOutTime = checkOutTime;
        }

        public String getCatatanPelanggaran() {
            return catatanPelanggaran;
        }

        public void setCatatanPelanggaran(String catatanPelanggaran) {
            this.catatanPelanggaran = catatanPelanggaran;
        }
    }
}
