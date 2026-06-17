package com.tup.reservasi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tup.reservasi.dto.MahasiswaProfileForm;
import com.tup.reservasi.dto.ReservationForm;
import com.tup.reservasi.entity.Mahasiswa;
import com.tup.reservasi.entity.User;
import com.tup.reservasi.service.NotificationService;
import com.tup.reservasi.service.ReservationService;
import com.tup.reservasi.service.RoomService;
import com.tup.reservasi.service.UserService;

import jakarta.validation.Valid;

/*
 * Penanggung jawab: Amelia Sofiana Makharomi - 103112400233.
 * Modul: Mahasiswa.
 */
@Controller
@RequestMapping("/mahasiswa")
public class MahasiswaController {

    private final ReservationService reservationService;
    private final RoomService roomService;
    private final UserService userService;
    private final NotificationService notificationService;

    public MahasiswaController(ReservationService reservationService,
            RoomService roomService,
            UserService userService,
            NotificationService notificationService) {
        this.reservationService = reservationService;
        this.roomService = roomService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @GetMapping
    public String mahasiswaHome() {
        return "redirect:/mahasiswa/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("rooms", this.roomService.getAllRooms());
        model.addAttribute("reservations", this.reservationService.getAllReservations());
        model.addAttribute("notifications", this.notificationService.getAllNotifications());
        return "mahasiswa/dashboard";
    }

    @GetMapping("/rooms")
    public String rooms(Model model) {
        model.addAttribute("rooms", this.roomService.getAllRooms());
        return "rooms/list";
    }

    @GetMapping("/reservations")
    public String reservations(@RequestParam(required = false) Long mahasiswaId, Model model) {
        if (mahasiswaId == null) {
            model.addAttribute("reservations", this.reservationService.getAllReservations());
        } else {
            model.addAttribute("reservations", this.reservationService.getReservationHistory(mahasiswaId));
        }
        return "reservations/list";
    }

    @GetMapping("/reservations/form")
    public String reservationForm(Model model) {
        model.addAttribute("reservation", new ReservationForm());
        prepareReservationForm(model);
        return "reservations/form";
    }

    @PostMapping("/reservations")
    public String createReservation(@Valid @ModelAttribute("reservation") ReservationForm reservation,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttrs) {
        if (bindingResult.hasErrors()) {
            prepareReservationForm(model);
            return "reservations/form";
        }

        try {
            this.reservationService.createReservation(
                    reservation.getMahasiswaId(),
                    reservation.getRoomId(),
                    reservation.getTanggal(),
                    reservation.getJamMulai(),
                    reservation.getJamSelesai(),
                    reservation.getTujuan());
        } catch (RuntimeException e) {
            bindingResult.reject("reservation.failed", e.getMessage());
            prepareReservationForm(model);
            return "reservations/form";
        }
        redirectAttrs.addFlashAttribute("sukses", "Reservasi berhasil diajukan");
        return "redirect:/mahasiswa/reservations";
    }

    @GetMapping("/notifications")
    public String notifications(Model model) {
        model.addAttribute("notifications", this.notificationService.getAllNotifications());
        return "notifications/list";
    }

    @GetMapping("/profile/{id}")
    public String profile(@PathVariable Long id, Model model) {
        User user;
        try {
            user = this.userService.getUserById(id);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "users/profile";
        }
        if (!(user instanceof Mahasiswa mahasiswa)) {
            model.addAttribute("error", "User bukan Mahasiswa");
            return "users/profile";
        }
        model.addAttribute("profile", MahasiswaProfileForm.from(mahasiswa));
        return "users/profile";
    }

    @PostMapping("/profile/{id}")
    public String updateProfile(@PathVariable Long id,
            @Valid @ModelAttribute("profile") MahasiswaProfileForm profile,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttrs) {
        if (bindingResult.hasErrors()) {
            profile.setId(id);
            model.addAttribute("profile", profile);
            return "users/profile";
        }

        try {
            profile.setId(id);
            this.userService.updateMahasiswa(id, profile.toMahasiswa());
        } catch (RuntimeException e) {
            bindingResult.reject("profile.failed", e.getMessage());
            profile.setId(id);
            model.addAttribute("profile", profile);
            return "users/profile";
        }
        redirectAttrs.addFlashAttribute("sukses", "Profil berhasil diubah");
        return "redirect:/mahasiswa/profile/" + id;
    }

    private void prepareReservationForm(Model model) {
        model.addAttribute("rooms", this.roomService.getActiveRooms());
    }
}
