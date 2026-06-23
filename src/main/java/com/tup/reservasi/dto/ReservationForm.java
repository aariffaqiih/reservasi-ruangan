package com.tup.reservasi.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationForm {

    @NotNull(message = "ID Mahasiswa wajib diisi")
    private Long mahasiswaId;

    @NotNull(message = "Ruang wajib dipilih")
    private Long roomId;

    @NotNull(message = "Tanggal wajib diisi")
    private LocalDate tanggal;

    @NotNull(message = "Jam mulai wajib diisi")
    private LocalTime jamMulai;

    @NotNull(message = "Jam selesai wajib diisi")
    private LocalTime jamSelesai;

    @NotBlank(message = "Tujuan wajib diisi")
    @Size(max = 255, message = "Tujuan maksimal 255 karakter")
    private String tujuan;

    @AssertTrue(message = "Jam selesai harus setelah jam mulai")
    public boolean isJamSelesaiValid() {
        if (jamMulai == null || jamSelesai == null) {
            return true;
        }
        return jamSelesai.isAfter(jamMulai);
    }
}
