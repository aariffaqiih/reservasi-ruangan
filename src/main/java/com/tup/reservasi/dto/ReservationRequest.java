package com.tup.reservasi.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tup.reservasi.entity.Mahasiswa;
import com.tup.reservasi.entity.Reservation;
import com.tup.reservasi.entity.Room;
import com.tup.reservasi.enums.ReservationStatus;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReservationRequest {

    private Long reservationId;
    private Long mahasiswaId;
    private Long roomId;
    private Ref mahasiswa;
    private Ref room;
    private LocalDate tanggal;
    private LocalTime jamMulai;
    private LocalTime jamSelesai;
    private String tujuan;
    private ReservationStatus status;

    public Reservation toReservation() {
        Reservation reservation = new Reservation();
        reservation.setReservationId(reservationId);
        reservation.setMahasiswa(toMahasiswa());
        reservation.setRoom(toRoom());
        reservation.setTanggal(tanggal);
        reservation.setJamMulai(jamMulai);
        reservation.setJamSelesai(jamSelesai);
        reservation.setTujuan(tujuan);
        reservation.setStatus(status);
        return reservation;
    }

    private Mahasiswa toMahasiswa() {
        Long id = mahasiswaId;
        if (id == null && mahasiswa != null) {
            id = mahasiswa.getId();
        }
        if (id == null) {
            return null;
        }
        Mahasiswa result = new Mahasiswa();
        result.setId(id);
        return result;
    }

    private Room toRoom() {
        Long id = roomId;
        if (id == null && room != null) {
            id = room.getRoomId();
        }
        if (id == null) {
            return null;
        }
        Room result = new Room();
        result.setRoomId(id);
        return result;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Ref {
        private Long id;
        private Long roomId;
    }
}
