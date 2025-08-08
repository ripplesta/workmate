package com.example.workmate.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.workmate.domain.Account;
import com.example.workmate.security.AccountUserDetails;

@Controller
public class DashboardController {
	
	@GetMapping("/dashboard")
	public String dashboard(@AuthenticationPrincipal AccountUserDetails userDetails, Model model){

		//ログインしたユーザー情報を取得
		Account loginUser = userDetails.getAccount();
		//ログインしていなかったらホーム画面にリダイレクト
		if(loginUser == null) {
			return "redirect:/home";
		}
		//ログインしたユーザー情報をhtml側に送る
		model.addAttribute("user", loginUser);
		return "dashboard";
	}

}
