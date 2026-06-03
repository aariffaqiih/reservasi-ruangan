package com.tup.demo.controller;

import com.tup.demo.entity.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentRestController {

    private static List<Student> listStudent = new ArrayList<>(List.of(
            new Student("Dany", "dany@mail.com"),
            new Student( "Candra", "candra@mail.com")
    ));

    @GetMapping
    public ResponseEntity<List<Student>> getAllProduk() {
        return ResponseEntity.ok(listStudent);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Student> getProdukByname(@PathVariable String name) {
        return listStudent.stream()
                .filter(s -> s.getName().equals(name))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Student> tambahSiswa(@RequestBody Student student) {
        listStudent.add(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(student);
    }

    @PutMapping("/{name}")
    public ResponseEntity<Student> updateStudent(@PathVariable String name, @RequestBody Student studentBaru) {
        for (Student s : listStudent) {
            if (s.getName().equals(name)) {
                s.setName(studentBaru.getName());
                s.setEmail(studentBaru.getEmail());
                return ResponseEntity.ok(studentBaru);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> hapusStudent(@PathVariable String name) {
        boolean berhasil = listStudent.removeIf(s -> s.getName().equals(name));
        if (berhasil) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }


}
