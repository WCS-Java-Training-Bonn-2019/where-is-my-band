package com.wildcodeschool.sea.bonn.whereismyband.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Musician;
import com.wildcodeschool.sea.bonn.whereismyband.entity.user.AdminUserDetails;
import com.wildcodeschool.sea.bonn.whereismyband.repository.MusicianRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	private final MusicianRepository musicianRepository;
	private final String adminPassword;
	
	@Autowired
	public UserDetailsServiceImpl(MusicianRepository musicianRepository, @Value("${admin.password}") String adminPassword) {
		this.musicianRepository = musicianRepository;
		this.adminPassword = adminPassword;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if ("admin".equals(username)) {
			return getAdminUserDetails();
		}
		return loadMusicianByName(username);
	}
	
	private UserDetails getAdminUserDetails() {
		return new AdminUserDetails("admin", adminPassword);
	}
	
	private UserDetails loadMusicianByName(String username) {
		Optional<Musician> optionalMusician = musicianRepository.findByUsername(username);
		if (optionalMusician.isPresent()) {
			return optionalMusician.get();
		}
		throw new UsernameNotFoundException("User '" + username + "' not found.");
	}

}
