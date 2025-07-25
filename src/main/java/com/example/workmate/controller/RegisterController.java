package com.example.workmate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.workmate.domain.Account;
import com.example.workmate.dto.RegisterForm;
import com.example.workmate.repository.AccountRepository;

@Controller
public class RegisterController {
	
	@Autowired
	private AccountRepository accountRepository;
	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("registerForm", new RegisterForm());
		return "register";
	}
	
	@PostMapping("/register")
	public String registerUser(@ModelAttribute RegisterForm registerForm, Model model) {
		if(accountRepository.findByLoginId(registerForm.getLoginId()).isPresent()) {
			model.addAttribute("errorMessage", "このログインIDは使用されています");
			return "register";
		}
		
		Account newUser = new Account();
		newUser.setLoginId(registerForm.getLoginId());
		newUser.setPassword(registerForm.getPassword());
		newUser.setMail(registerForm.getMail());
		newUser.setUserName(registerForm.getUserName());
		
		accountRepository.save(newUser);
		
		//ここは一旦ホームにしているが登録が完了しましたという画面を挟む予定
		return "redirect:/home"; 
	}
	
}