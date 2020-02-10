package com.wildcodeschool.sea.bonn.whereismyband;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.wildcodeschool.sea.bonn.whereismyband.services.UserDetailsServiceImpl;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	private final UserDetailsServiceImpl userDetailsServiceImpl;
	private final PasswordEncoder passwordEncoder;
	
	public WebSecurityConfig(UserDetailsServiceImpl userDetailsServiceImpl, PasswordEncoder passwordEncoder) {
		this.userDetailsServiceImpl = userDetailsServiceImpl;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/", "/images/**", "/css/**", "/register").permitAll()
				.antMatchers("/musician/**", "/band/**", "/search/**").hasAnyRole("ADMIN", "MUSICIAN")
				.anyRequest().hasAnyRole("ADMIN")
				.and()
			.formLogin()
				.loginPage("/login")
				.defaultSuccessUrl("/", true)
				.permitAll()
				.and()
			.logout()
				.logoutSuccessUrl("/")
				.and()
			.httpBasic();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder);
	}
}
