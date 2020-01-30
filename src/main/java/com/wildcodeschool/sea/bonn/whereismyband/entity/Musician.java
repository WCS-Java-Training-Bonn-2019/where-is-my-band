package com.wildcodeschool.sea.bonn.whereismyband.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Musician {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String firstName;
	private String lastName;
	private String description;

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

}
