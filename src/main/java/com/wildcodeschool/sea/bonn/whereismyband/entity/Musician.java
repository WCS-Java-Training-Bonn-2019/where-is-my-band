package com.wildcodeschool.sea.bonn.whereismyband.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

import com.wildcodeschool.sea.bonn.whereismyband.services.PhoneNumberConstraint;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Musician {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Der Vorname muss angegeben werden!")
	private String firstName;

	@NotBlank(message = "Der Nachname muss angegeben werden!")
	private String lastName;

	private String description;

	@PhoneNumberConstraint(message = "Bitte eine gültige Telefonnummer eingeben!")
	private String phone;

	@NotNull(message = "Eine Email-Adresse als Benutzername muss angegeben werden!")
	private String email;	

	private String password;

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
}
