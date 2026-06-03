package com.belajar.perpustakaan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApiDocsController {

    @GetMapping("/api-docs")
    public String apiDocs(Model model) {
        model.addAttribute("pageTitle", "REST API Docs");
        return "api-docs";
    }
}
