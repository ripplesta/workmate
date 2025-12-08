package com.example.workmate.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.workmate.security.AccountUserDetails;

@Controller
public class LoginController {
	
	//@Autowired
	//private AccountRepository accountRepository;
	
    //このページにアクセスした時にこのメソッドが実行される
	//Spring Securityの導入に伴い大幅に記述を変更した
	@GetMapping("/login")
	public String login(@AuthenticationPrincipal AccountUserDetails userDetails) {
		if(userDetails != null) {
			return "redirect:/dashboard";
		}
		
		return "login";
	}
	
	
	@GetMapping("/logout-success")
	public String logoutSuccesh() {
		return "logout";
	}

}
