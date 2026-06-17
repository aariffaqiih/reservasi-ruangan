package com.tup.reservasi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tup.reservasi.entity.Admin;
import com.tup.reservasi.entity.Mahasiswa;
import com.tup.reservasi.entity.Satpam;
import com.tup.reservasi.entity.User;
import com.tup.reservasi.repository.UserRepository;

/*
 * Penanggung jawab: Amelia Sofiana Makharomi - 103112400233.
 * Modul: User dan Mahasiswa.
 */
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return this.userRepository.findById(id).orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByNoHp(String noHp) {
        return this.userRepository.findByNoHp(noHp);
    }

    @Transactional(readOnly = true)
    public List<User> getUserByNamaContaining(String nama) {
        return this.userRepository.findByNamaContaining(nama);
    }

    public User createUser(User user) {
        return this.userRepository.save(user);
    }

    public User updateUser(Long id, User updatedData) {
        User userExisting = getUserById(id);
        userExisting.ubahProfil(updatedData.getNama(), updatedData.getEmail(), updatedData.getNoHp());
        return this.userRepository.save(userExisting);
    }

    public Mahasiswa updateMahasiswa(Long id, Mahasiswa updatedData) {
        User userExisting = getUserById(id);
        if (!(userExisting instanceof Mahasiswa mahasiswaExisting)) {
            throw new RuntimeException("User bukan Mahasiswa");
        }
        mahasiswaExisting.ubahProfil(updatedData.getNama(), updatedData.getEmail(), updatedData.getNoHp());
        if (updatedData.getNim() != null) {
            mahasiswaExisting.setNim(updatedData.getNim());
        }
        if (updatedData.getProdi() != null) {
            mahasiswaExisting.setProdi(updatedData.getProdi());
        }
        if (updatedData.getAngkatan() > 0) {
            mahasiswaExisting.setAngkatan(updatedData.getAngkatan());
        }
        return this.userRepository.save(mahasiswaExisting);
    }

    public Admin updateAdmin(Long id, Admin updatedData) {
        User userExisting = getUserById(id);
        if (!(userExisting instanceof Admin adminExisting)) {
            throw new RuntimeException("User bukan Admin");
        }
        adminExisting.ubahProfil(updatedData.getNama(), updatedData.getEmail(), updatedData.getNoHp());
        adminExisting.setUnitKerja(updatedData.getUnitKerja());
        return this.userRepository.save(adminExisting);
    }

    public Satpam updateSatpam(Long id, Satpam updatedData) {
        User userExisting = getUserById(id);
        if (!(userExisting instanceof Satpam satpamExisting)) {
            throw new RuntimeException("User bukan Satpam");
        }
        satpamExisting.ubahProfil(updatedData.getNama(), updatedData.getEmail(), updatedData.getNoHp());
        satpamExisting.setPosJaga(updatedData.getPosJaga());
        satpamExisting.setShift(updatedData.getShift());
        return this.userRepository.save(satpamExisting);
    }

    public void deleteUser(Long id) {
        User userExisting = getUserById(id);
        this.userRepository.delete(userExisting);
    }

    @Transactional(readOnly = true)
    public Long countUsers() {
        return this.userRepository.count();
    }

    @Transactional(readOnly = true)
    public boolean login(String email, String password) {
        return this.userRepository.findByEmail(email)
                .map(user -> user.login(email, password))
                .orElse(false);
    }

}
