package com.example.workmate.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.workmate.domain.Account;

public class AccountUserDetails implements UserDetails {
	
	private final Account account;
	
	public AccountUserDetails(Account account) {
		this.account = account;
	}
	
	public Account getAccount() {
		return account;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList(); //権限は現時点では使用しないため空リスト
	}

	@Override
	public String getPassword(){
		return account.getPassword();
	}
	
	@Override
	public String getUsername(){
		return account.getLoginId();
	}

	//今は全てtrueにして問題なし
	@Override
	public boolean isAccountNonExpired(){
		return true; //アカウント有効期限チェックをしない
	}

	@Override
	 public boolean isAccountNonLocked(){
		return true; //アカウントロックチェックをしない
	}

	@Override
	public boolean isCredentialsNonExpired(){
		return true; //資格情報有効期限チェックをしない
	}

	@Override
	public boolean isEnabled(){
		return true; //アカウント有効か常にtrue
	}

}