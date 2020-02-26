package com.wildcodeschool.sea.bonn.whereismyband;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
// set property file to be used here (to be saved in /src/main/resources)
@PropertySource("classpath:mysql-application.properties")
public class WhereismybandApplication {

	public static void main(String[] args) {
		SpringApplication.run(WhereismybandApplication.class, args);
	}
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
