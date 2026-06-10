package com.tup.reservasi.controller.rest;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.tup.reservasi.dto.AccessIssueRequest;
import com.tup.reservasi.entity.Mahasiswa;
import com.tup.reservasi.entity.Reservation;
import com.tup.reservasi.entity.Room;
import com.tup.reservasi.entity.Satpam;
import com.tup.reservasi.enums.ReservationStatus;
import com.tup.reservasi.repository.AccessRecordRepository;
import com.tup.reservasi.repository.ReservationRepository;
import com.tup.reservasi.repository.RoomRepository;
import com.tup.reservasi.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AccessRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private AccessRecordRepository accessRecordRepository;


    private Satpam satpam;
    private Mahasiswa mahasiswa;
    private Room room;

    @BeforeEach
    void setUp() {
        accessRecordRepository.deleteAll();
        reservationRepository.deleteAll();
        userRepository.deleteAll();
        roomRepository.deleteAll();

        satpam = new Satpam("stm", "Satpam Starter", "satpam@starter.local", "0811111", "hash", "Pagi", "Pos 1");
        userRepository.save(satpam);

        mahasiswa = new Mahasiswa("mhs", "Mahasiswa Starter", "mhs@starter.local", "0822222", "hash", "NIM-001", "IF", 2026);
        userRepository.save(mahasiswa);

        room = new Room("R-001", "Ruang Kelas 1", "Gedung A", 30, true);
        roomRepository.save(room);
    }

    @Test
    @WithMockUser(username = "stm", roles = {"SATPAM"})
    void testCheckInAndCheckOutFlow() throws Exception {
        Reservation reservation = new Reservation(mahasiswa, room, LocalDate.now(), LocalTime.of(18, 0), LocalTime.of(20, 0), "Rapat");
        reservation.ajukan();
        reservation.ubahStatus(ReservationStatus.APPROVED);
        reservation = reservationRepository.save(reservation);

        // check-in
        mockMvc.perform(post("/api/access/" + reservation.getReservationId() + "/check-in"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").value(reservation.getReservationId()))
                .andExpect(jsonPath("$.satpamId").value("stm"))
                .andExpect(jsonPath("$.checkInTime").isNotEmpty());

        // check-out
        mockMvc.perform(post("/api/access/" + reservation.getReservationId() + "/check-out"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").value(reservation.getReservationId()))
                .andExpect(jsonPath("$.checkOutTime").isNotEmpty());
    }

    @Test
    @WithMockUser(username = "stm", roles = {"SATPAM"})
    void testReportIssue() throws Exception {
        Reservation reservation = new Reservation(mahasiswa, room, LocalDate.now(), LocalTime.of(18, 0), LocalTime.of(20, 0), "Rapat");
        reservation.ajukan();
        reservation.ubahStatus(ReservationStatus.APPROVED);
        reservation = reservationRepository.save(reservation);

        // check-in first
        mockMvc.perform(post("/api/access/" + reservation.getReservationId() + "/check-in"))
                .andExpect(status().isOk());

        String json = "{\"reservationId\":\"" + reservation.getReservationId() + "\",\"satpamId\":null,\"catatanPelanggaran\":\"AC bocor dan kursi rusak\"}";

        mockMvc.perform(post("/api/access/report-issue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.catatanPelanggaran").value("AC bocor dan kursi rusak"));
    }

    @Test
    @WithMockUser(username = "stm", roles = {"SATPAM"})
    void testGetRecords() throws Exception {
        Reservation reservation = new Reservation(mahasiswa, room, LocalDate.now(), LocalTime.of(18, 0), LocalTime.of(20, 0), "Rapat");
        reservation.ajukan();
        reservation.ubahStatus(ReservationStatus.APPROVED);
        reservation = reservationRepository.save(reservation);

        mockMvc.perform(post("/api/access/" + reservation.getReservationId() + "/check-in"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/access"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(get("/api/access/belum-checkout"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
