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
	
	//Spring Securityを導入したためこのPostは必要なくなった
	
	//Postでアクセスしたときにこのメソッドが実行される
//	@PostMapping("/login")
//	public String login(@ModelAttribute @Validated LoginForm loginForm, BindingResult bindingResult, HttpSession session, Model model) {
//		
//		
//		//入力エラーがあれば戻る
//		if(bindingResult.hasErrors()) {
//			return "login";
//		}
//		
//		//ログインしたときにDBに保存されているそのログインIDと同じものの情報をインスタンスに格納
//		Optional<Account> optionalAccount = accountRepository.findByLoginId(loginForm.getLoginId());
//
//		
//		if(optionalAccount.isPresent()){
//			Account account = optionalAccount.get();
//			
//			
//			//入力されたパスワードとDBに保存されているパスワードを照合してtrueならアカウント情報をセッションに保存してメニュー画面へ
//			if(account.getPassword().equals(loginForm.getPassword())) {
//				session.setAttribute("loginUser", account);
//				return "redirect:/dashboard";
//			}
//		}
//		
//		//上のifがtrueでなければログインエラーを返し遷移しない
//		model.addAttribute("loginError", "ユーザー名またはパスワードが違います");
//		return "login";	
//	}
	
	@GetMapping("/logout")
	public String logout() {
		//ログアウトするときにログアウトしますかと表示するかログアウト画面に遷移させようか考えている
		return "logout";
	}

}
