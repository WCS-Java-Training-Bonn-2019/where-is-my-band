package com.wildcodeschool.sea.bonn.whereismyband.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Getter
@Setter
@ToString
public class Band {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// database id
	private Long id;
	private String name;
	// picture to be done
	// private Address address;
	private String email;
	private String phone;
	
	@ManyToMany
	@JoinTable(name = "band_plays_genre", 
			  joinColumns = @JoinColumn(name = "band_id"), 
			  inverseJoinColumns = @JoinColumn(name = "genre_id"))
	private Set<Genre> favoriteGenres = new HashSet<>();
	
	@OneToOne
	@JoinColumn(name = "address_id")
	private Address address;
		
}
