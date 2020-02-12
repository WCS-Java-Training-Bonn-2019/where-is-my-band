package com.wildcodeschool.sea.bonn.whereismyband.controller;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Bandposition;

@Controller
public class LoginController {

	// Mapping für die Custom-LoginPage und Logout
	
//	@GetMapping("/login")
//	public String getLogin() {
//		return "login";
//	}
	
	@GetMapping("/logout")
	public String getLogout() {
		return "logout";
	}
	
	
	// Abfrage und Vorbereitung auf Ausgabe der letzten 3 angelegten Bandpositionen in der Nähe
	
	@GetMapping("/login")
	public String getLogin(Model model) {
		
//		Bandposition band = new Bandposition();
//		LocalDateTime local = LocalDateTime.now();
//		LocalDateTime past = local.minusDays(7);
//		LocalDateTime random = local.minusHours(24);
//		
//		LocalDateTime db = band.getLastCreated();
//		
//		if(db.compareTo(local) < 0 && db.compareTo(past) > 0) {
//			System.out.println("Erfolg");
//		}
		return "login";
	}
}
