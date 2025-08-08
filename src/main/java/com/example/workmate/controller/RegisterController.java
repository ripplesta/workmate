package com.example.workmate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.workmate.domain.Account;
import com.example.workmate.dto.RegisterForm;
import com.example.workmate.repository.AccountRepository;
import com.example.workmate.security.AccountUserDetails;

@Controller
public class RegisterController {
	
	@Autowired
	private AccountRepository accountRepository;
	
	//セキュリティのためパスワードをハッシュ化する準備
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/register")
	public String register(@AuthenticationPrincipal AccountUserDetails userDetails, Model model) {
		if(userDetails != null) {
			return "redirect:/dashboard";
		}
		model.addAttribute("registerForm", new RegisterForm());
		return "register";
	}
	
	@PostMapping("/register")
	public String registerUser(@ModelAttribute RegisterForm registerForm, Model model) {
		//入力された情報のログインIDがDBに保存されているログインIDと重複していたらエラーメッセージを表示する
		boolean loginIdExists = accountRepository.findByLoginId(registerForm.getLoginId()).isPresent();
		if(loginIdExists) {
			model.addAttribute("errorMessage", "このログインIDは使用されています");
			return "register";
		}
		
		//入力データをインスタンスに格納
		Account newUser = new Account();
		newUser.setLoginId(registerForm.getLoginId());
		//セキュリティを強化するためにパスワードをハッシュ化して登録
		newUser.setPassword(passwordEncoder.encode(registerForm.getPassword()));
		newUser.setMail(registerForm.getMail());
		newUser.setUserName(registerForm.getUserName());
		
		//入っているデータをDBに保存
		accountRepository.save(newUser);
		
		//ここは一旦ホームにしているが登録が完了しましたという画面を挟む予定
		return "redirect:/home"; 
	}
	
}