package com.example.workmate.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.workmate.domain.Account;

@Controller
public class HomeController {
	
	@GetMapping("/home")
	public String showHome(HttpSession session) {
		Account loginUser = (Account)session.getAttribute("loginUser");
		
		if(loginUser != null) {
			return "redirect:/dashboard";
		}
		//resources/templates/home.htmlにアクセスする
		return "home";
	}

}
