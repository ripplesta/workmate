package com.example.workmate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/greet")
    public String greet(Model model) {
        model.addAttribute("message", "こんにちは、Spring BootとThymeleaf!");
        return "hello"; // resources/templates/hello.html を表示
    }
}