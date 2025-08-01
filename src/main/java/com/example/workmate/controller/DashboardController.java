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

		//ログインしたユーザー情報を取得
		Account loginUser = (Account)session.getAttribute("loginUser");
		//ログインしていなかったらホーム画面にリダイレクト
		if(loginUser == null) {
			return "redirect:/home";
		}
		//ログインしたユーザー情報をhtml側に送る
		model.addAttribute("user", loginUser);
		return "dashboard";
	}

}
