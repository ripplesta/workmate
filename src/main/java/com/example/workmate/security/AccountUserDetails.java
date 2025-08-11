package com.example.workmate.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.workmate.domain.Account;

/**
 * Spring Securityで利用するユーザー情報を保持するクラス 
 * UserDetailsインターフェースを実装することで、
 * Securityが認証処理に必要な情報(ユーザー名、パスワード、権限など)を取得できるようになる
 */
public class AccountUserDetails implements UserDetails {
	
	// 実際のアカウント情報を保持
	private final Account account;
	
	// コンストラクタでAccountを受け取って保持
	public AccountUserDetails(Account account) {
		this.account = account;
	}
	
	// アプリ側でAccount情報に直接アクセスしたい場合用
	public Account getAccount() {
		return account;
	}

	/**
	 * ユーザーが持つ権限を帰す
	 * 今回は管理権限を行わないため、空のリストを返す
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList(); //権限は現時点では使用しないため空リスト
	}

	// ユーザーのパスワードを返す(DBに保存されているハッシュ化済みのパスワードが返る)
	@Override
	public String getPassword(){
		return account.getPassword();
	}
	
	// ユーザー名(ここではログインID)を返す
	@Override
	public String getUsername(){
		return account.getLoginId();
	}

	/**
	 * アカウントが期限切れでないかどうか
	 * trueなら有効
	 * 今回は常にtrueを返す(有効期限チェックはしない)
	 */
	@Override
	public boolean isAccountNonExpired(){
		return true; //アカウント有効期限チェックをしない
	}

	/**
	 * アカウントがロックされていないかどうか
	 * trueならロックされていない
	 * 今回は常にtrue
	 */
	@Override
	 public boolean isAccountNonLocked(){
		return true; //アカウントロックチェックをしない
	}

	/**
	 * 資格情報(パスワード)が期限切れでないかどうか
	 * trueなら有効
	 * 今回は常にtrue
	 */
	@Override
	public boolean isCredentialsNonExpired(){
		return true; //資格情報有効期限チェックをしない
	}

	/**
	 * アカウントが有効かどうか
	 * trueなら有効
	 * 今回は常にtrue
	 */
	@Override
	public boolean isEnabled(){
		return true; //アカウント有効か常にtrue
	}

}