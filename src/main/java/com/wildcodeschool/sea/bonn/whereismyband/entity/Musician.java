package com.wildcodeschool.sea.bonn.whereismyband.entity;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Musician implements UserDetails {
	
	private static final long serialVersionUID = 2854259156697777966L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String firstName;
	private String lastName;
	private String description;
	
	@Column(unique = true)
	private String username;
	private String password;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate birthday;
	
	@ManyToOne
	@JoinColumn(name = "gender_id")
	private Gender gender;
	
	@OneToOne
	@JoinColumn(name = "address_id")
	private Address address;
	
	@ManyToMany
	@JoinTable(name = "musician_likes_genre", 
			  joinColumns = @JoinColumn(name = "musician_id"), 
			  inverseJoinColumns = @JoinColumn(name = "genre_id"))
	private Set<Genre> favoriteGenres = new HashSet<>();
	
	@ManyToMany
	@JoinTable(name = "musician_plays_instrument", 
			  joinColumns = @JoinColumn(name = "musician_id"), 
			  inverseJoinColumns = @JoinColumn(name = "instrument_id"))
	private Set<Instrument> instruments = new HashSet<>();
	
	@OneToMany (mappedBy = "owner")
	private Set<Band> bands = new HashSet<>();


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_MUSICIAN");
		return Collections.singletonList(authority);
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
