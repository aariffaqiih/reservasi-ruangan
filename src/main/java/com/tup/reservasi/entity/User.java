package com.tup.reservasi.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Penanggung jawab: Amelia Sofiana Makharomi - 103112400233.
 * Modul: User.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "jenis")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Mahasiswa.class, name = "mahasiswa"),
        @JsonSubTypes.Type(value = Admin.class, name = "admin"),
        @JsonSubTypes.Type(value = Satpam.class, name = "satpam")
})
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nama", nullable = false, length = 100)
    private String nama;

    @Column(name = "email", nullable = false, unique = true, length = 120)
    private String email;

    @Column(name = "no_hp", length = 20, unique = true)
    private String noHp;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @JsonIgnore
    @OneToMany(mappedBy = "penerima")
    private List<Notification> notifications = new ArrayList<>();

    @Transient
    private boolean loggedIn;

    public boolean login(String email, String password) {
        this.loggedIn = this.email != null
                && this.email.equals(email)
                && this.passwordHash != null
                && this.passwordHash.equals(password);
        return this.loggedIn;
    }

    public void logout() {
        this.loggedIn = false;
    }

    public void ubahProfil(String nama, String email, String noHp) {
        this.nama = nama;
        this.email = email;
        this.noHp = noHp;
    }
}
