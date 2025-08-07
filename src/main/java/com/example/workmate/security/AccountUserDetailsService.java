package com.example.workmate.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.workmate.repository.AccountRepository;

@Service
public class AccountUserDetailsService implements UserDetailsService {
	
	private final AccountRepository accountRepository;

	@Autowired
	public AccountUserDetailsService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
		return accountRepository.findByLoginId(loginId)
				.map(AccountUserDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("ログインIDが見つかりません：" + loginId));
		
	}
}