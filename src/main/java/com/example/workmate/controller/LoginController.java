package com.example.workmate.controller;

import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.workmate.domain.Account;
import com.example.workmate.dto.LoginForm;
import com.example.workmate.repository.AccountRepository;

@Controller
public class LoginController {
	
	@Autowired
	private AccountRepository accountRepository;
	
　//このページにアクセスした時にこのメソッドが実行される
	@GetMapping("/login")
	public String login(Model model) {
		
		model.addAttribute("loginForm", new LoginForm());
		return "login";
	}
	//Postでアクセスしたときにこのメソッドが実行される
	@PostMapping("/login")
	public String login(@ModelAttribute @Validated LoginForm loginForm, BindingResult bindingResult, HttpSession session, Model model) {
		
		//入力エラーがあれば戻る
		if(bindingResult.hasErrors()) {
			return "login";
		}
		//ログインしたときにDBに保存されているそのログインIDと同じものの情報をインスタンスに格納
		Optional<Account> optionalAccount = accountRepository.findByLoginId(loginForm.getLoginId());
		
		if(optionalAccount.isPresent()){
			Account account = optionalAccount.get();
			
			
			//入力されたパスワードとDBに保存されているパスワードを照合してtrueならアカウント情報をセッションに保存してメニュー画面へ
			if(account.getPassword().equals(loginForm.getPassword())) {
				session.setAttribute("loginUser", account);
				return "redirect:/dashboard";
			}
		}
		//上のifがtrueでなければログインエラーを返し遷移しない
		model.addAttribute("loginError", "ユーザー名またはパスワードが違います");
		return "login";	
	}
	//ログアウトすることときにinvalidateでセッション情報を破棄する
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
　//ログアウトするときにログアウトしますかと表示するかログアウト画面に遷移させようか考えている
		return "redirect:/login";
	}

}
