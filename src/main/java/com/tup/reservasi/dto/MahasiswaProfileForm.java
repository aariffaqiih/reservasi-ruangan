package com.tup.reservasi.dto;

import com.tup.reservasi.entity.Mahasiswa;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MahasiswaProfileForm {

    private Long id;

    @NotBlank(message = "Nama wajib diisi")
    @Size(min = 3, max = 100, message = "Nama harus 3-100 karakter")
    private String nama;

    @NotBlank(message = "Email wajib diisi")
    @Email(message = "Format email tidak valid")
    private String email;

    @Size(max = 20, message = "No HP maksimal 20 karakter")
    private String noHp;

    @Size(max = 30, message = "NIM maksimal 30 karakter")
    private String nim;

    @Size(max = 100, message = "Prodi maksimal 100 karakter")
    private String prodi;

    @Min(value = 1, message = "Angkatan harus lebih dari 0")
    private Integer angkatan;

    public static MahasiswaProfileForm from(Mahasiswa mahasiswa) {
        MahasiswaProfileForm form = new MahasiswaProfileForm();
        form.setId(mahasiswa.getId());
        form.setNama(mahasiswa.getNama());
        form.setEmail(mahasiswa.getEmail());
        form.setNoHp(mahasiswa.getNoHp());
        form.setNim(mahasiswa.getNim());
        form.setProdi(mahasiswa.getProdi());
        form.setAngkatan(mahasiswa.getAngkatan() > 0 ? mahasiswa.getAngkatan() : null);
        return form;
    }

    public Mahasiswa toMahasiswa() {
        Mahasiswa mahasiswa = new Mahasiswa();
        mahasiswa.setId(id);
        mahasiswa.setNama(nama);
        mahasiswa.setEmail(email);
        mahasiswa.setNoHp(noHp);
        mahasiswa.setNim(nim);
        mahasiswa.setProdi(prodi);
        mahasiswa.setAngkatan(angkatan == null ? 0 : angkatan);
        return mahasiswa;
    }
}
