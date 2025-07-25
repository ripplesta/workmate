package com.example.workmate.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.workmate.domain.Account;

@Controller
public class DashboardController {
	
	@GetMapping("/dashboard")
	public String dashboard(HttpSession session, Model model){
		Account loginUser = (Account)session.getAttribute("loginUser");
		if(loginUser == null) {
			return "redirect:/home";
		}
		model.addAttribute("user", loginUser);
		return "dashboard";
	}

}
