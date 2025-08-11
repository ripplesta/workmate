package com.example.workmate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.workmate.security.AccountUserDetailsService;

@Configuration  //Springの設定クラスであることを示す
@EnableWebSecurity //Spring SecurityのWebセキュリティ機能を有効化
public class SecurityConfig {
    
	private final AccountUserDetailsService userDetailsService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		// パスワードをBCryptでハッシュ化して保存&認証時にも使う
		return new BCryptPasswordEncoder();
	}
	// コンストラクタでUserDetailsServiceを受け取る
	public SecurityConfig(AccountUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}
	
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        	// URLごとにアクセス制御設定
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/home", "/login", "/register", "/images/**", "/css/**", "/js/**").permitAll()
                .anyRequest().authenticated() // それ以外はログイン必須
            )
            // ログインフォームの設定
            .formLogin(form -> form
                .loginPage("/login") // ログインページのURL
                .loginProcessingUrl("/login") // 認証処理のURL(POST先)
                .usernameParameter("loginId") // フォームのname属性が"loginId"の値をユーザー名として使用
                .passwordParameter("password") // パスワードのフォームname属性
                .defaultSuccessUrl("/dashboard",true) // ログイン成功時の遷移先(trueで常に固定)
                .failureUrl("/login?error=true") // ログイン失敗時の遷移先
                .permitAll()
            )
            // ログアウト設定
            .logout(logout -> logout
                .logoutUrl("/logout") // ログアウト処理のURL
                .logoutSuccessUrl("/login?logout") // ログアウト成功時の遷移先
                .permitAll()
            );
        return http.build(); // 設定を反映
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
    	// Spring Securityにユーザー情報取得方法とパスワードのハッシュ化方式を登録
    	DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    	authProvider.setUserDetailsService(userDetailsService); // ユーザー情報取得用サービスをセット
    	authProvider.setPasswordEncoder(passwordEncoder()); // パスワード比較的のエンコード方法をセット
    	return authProvider;
    }
    
    
}