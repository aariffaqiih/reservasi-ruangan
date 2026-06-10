package com.tup.reservasi.controller.rest;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import tools.jackson.databind.ObjectMapper;
import com.tup.reservasi.dto.NotificationRequest;
import com.tup.reservasi.entity.Mahasiswa;
import com.tup.reservasi.entity.Notification;
import com.tup.reservasi.repository.NotificationRepository;
import com.tup.reservasi.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class NotificationRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    private Mahasiswa mahasiswa;

    @BeforeEach
    void setUp() {
        notificationRepository.deleteAll();
        userRepository.deleteAll();

        mahasiswa = new Mahasiswa("mhs", "Mahasiswa Starter", "mhs@starter.local", "0822222", "hash", "NIM-001", "IF", 2026);
        userRepository.save(mahasiswa);
    }

    @Test
    @WithMockUser(username = "mhs", roles = {"MAHASISWA"})
    void testGetNotifications() throws Exception {
        Notification notification = new Notification(mahasiswa, "Pesan Baru");
        notificationRepository.save(notification);

        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].pesan").value("Pesan Baru"))
                .andExpect(jsonPath("$[0].penerimaId").value("mhs"));
    }

    @Test
    @WithMockUser(username = "adm", roles = {"ADMIN"})
    void testSendNotification() throws Exception {
        NotificationRequest request = new NotificationRequest("mhs", "MAHASISWA", "Info Penting");

        mockMvc.perform(post("/api/notifications")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pesan").value("Info Penting"))
                .andExpect(jsonPath("$.penerimaId").value("mhs"));
    }

    @Test
    @WithMockUser(username = "mhs", roles = {"MAHASISWA"})
    void testMarkAsRead() throws Exception {
        Notification notification = new Notification(mahasiswa, "Pesan Baru");
        notification = notificationRepository.save(notification);

        mockMvc.perform(put("/api/notifications/" + notification.getNotificationId() + "/read")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
