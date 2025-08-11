package com.example.workmate.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.workmate.domain.Account;
import com.example.workmate.repository.AccountRepository;

@Service // サービス層のBeanとして登録
public class AccountUserDetailsService implements UserDetailsService {
	
	private final AccountRepository accountRepository;

	// コンストラクタでリポジトリを受け取る
	public AccountUserDetailsService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
		// loginIdでDB検索
		Optional<Account> accountOpt = accountRepository.findByLoginId(loginId);
		
		// 見つからなければ例外を投げる(Spring Securityに認証失敗と認識される)
		Account account = accountOpt.orElseThrow(() -> 
			new UsernameNotFoundException("ログインIDが見つかりません：" + loginId)
		);
		
		// DBから取得したAccountをSpring Security用のUserDetailsに変換
		return new AccountUserDetails(account);
	}
	
}