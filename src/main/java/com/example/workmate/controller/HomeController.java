package com.example.workmate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	@GetMapping("/home")
	public String showHome() {
		//Account loginUser = (Account)session.getAttribute("loginUser");
		
		//if(loginUser != null) {
		//	return "redirect:/dashboard";
		//}
		//resources/templates/home.htmlにアクセスする
		return "home";
	}

}
