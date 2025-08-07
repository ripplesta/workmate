package com.example.workmate.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.workmate.security.AccountUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
	@Autowired
	private final AccountUserDetailsService userDetailsService;
	
	 @Bean
	    public PasswordEncoder passwordEncoder(){
	        return new BCryptPasswordEncoder();
	    }
	
	public SecurityConfig(AccountUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}
	
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/home", "/login", "/register", "/images/**", "/css/**", "/js/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                //.usernameParameter("loginId")
                //.passwordParameter("password")
                .defaultSuccessUrl("/dashboard")
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/home")
                .permitAll()
            );
        return http.build();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(
    		HttpSecurity http,
    		PasswordEncoder passwordEncoder,
    		UserDetailsService userDetailsService) throws Exception {
    	
    	return http.getSharedObject(AuthenticationManagerBuilder.class)
    			.userDetailsService(userDetailsService)
    			.passwordEncoder(passwordEncoder)
    			.and()
    			.build();
    }
    
}