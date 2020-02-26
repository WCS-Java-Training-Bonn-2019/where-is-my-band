package com.wildcodeschool.sea.bonn.whereismyband.entity;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.wildcodeschool.sea.bonn.whereismyband.constraints.PhoneNumberConstraint;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Musician implements UserDetails {
	
	private static final long serialVersionUID = 2854259156697777966L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Der Vorname muss angegeben werden!")
	private String firstName;

	@NotBlank(message = "Der Nachname muss angegeben werden!")
	private String lastName;

	private String description;
	
	@Column(unique = true)
	@NotNull(message = "Eine Email-Adresse als Benutzername muss angegeben werden!")
	private String username;
	private String password;

	@PhoneNumberConstraint(message = "Bitte eine gültige Telefonnummer eingeben!")
	private String phone;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@NotNull(message = "Bitte gib ein Geburtsdatum an!")
	private LocalDate birthday;

	@ManyToOne
	@JoinColumn(name = "gender_id")
	@NotNull(message = "Das Geschlecht muss angegeben werden!")
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
	@NotNull
	private Set<Instrument> instruments = new HashSet<>();

	@OneToMany (mappedBy = "owner")
	private Set<Band> bands = new HashSet<>();

	@Lob
	private byte[] image;
	
	public List<Genre> getFavoriteGenresSortedByName() {
		List<Genre> favoriteGenresSorted = new ArrayList<>(this.getFavoriteGenres());
		Collections.sort(favoriteGenresSorted);
		return favoriteGenresSorted;
	}

	public List<Instrument> getInstrumentsSortedByName() {
		List<Instrument> instrumentsSorted = new ArrayList<>(this.getInstruments());
		Collections.sort(instrumentsSorted);
		return instrumentsSorted;
	}
	
	public List<Band> getBandsSortedByName() {
		List<Band> bandsSorted = new ArrayList<>(this.getBands());
		Collections.sort(bandsSorted);
		return bandsSorted;
	}


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
	
	public int getAge() {
		return Period.between(getBirthday(), LocalDate.now()).getYears();
	}
}
