package com.example.workmate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginForm {
	
	//ログインIDとパスワードに空欄と指定の文字数以外だとエラーになる処理を付与
	@NotBlank(message = "ログインIDを入力してください")
	@Size(min = 3, max = 30, message = "ログインIDは3～30文字で入力してください")
	private String loginId;
	@NotBlank(message = "パスワードを入力してください")
	@Size(min = 6, message = "パスワードは6文字以上で入力してください")
	private String password;
	private String mail;
	private String userName;
	
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	
}
