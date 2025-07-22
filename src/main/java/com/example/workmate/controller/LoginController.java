package com.example.workmate.controller;

import com.example.workmate.domain.Account;

@Controller
public class LoginController {
	
	@Autowired
	private AccountRepository accountRepository;
	
	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("loginForm", new LoginForm());
		return "login";
	}
	
	@PostMapping("/login")
	public String login(@ModelAttribute LoginForm loginForm, HttpSession session, Model model) {
		OptionalAccount,isPresent()){
			Account account = optionalAccount.get();
			
			
			if(account.getPassword().equals(loginForm.getPassword())) {
				session.setA
			}
		}
	}

}
