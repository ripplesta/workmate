package com.example.workmate.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.workmate.domain.Account;
import com.example.workmate.repository.AccountRepository;

@Service
public class AccountUserDetailsService implements UserDetailsService {
	
	private final AccountRepository accountRepository;

	
	public AccountUserDetailsService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
		Optional<Account> accountOpt = accountRepository.findByLoginId(loginId);
		
		Account account = accountOpt.orElseThrow(() -> 
			new UsernameNotFoundException("ログインIDが見つかりません：" + loginId)
		);
		
		return new AccountUserDetails(account);
	}
	
}