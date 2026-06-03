package com.tup.demo.controller;

import com.tup.demo.entity.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HalloController {

    private List<Student> studentList=new ArrayList<>();

    @RequestMapping(method = RequestMethod.GET, value = "/" )
    public String hallo(Model model) {
        model.addAttribute("students",studentList);
        return "index";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/register" )
    public String register() {
        return "register";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register" )
    public String processRegister(@RequestParam String name,
                                  @RequestParam String email) {

        Student student=new Student();
        student.setName(name);
        student.setEmail(email);
        studentList.add(student);

        return "redirect:/";
    }
}
