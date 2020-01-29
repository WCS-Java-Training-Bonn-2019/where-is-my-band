package com.wildcodeschool.sea.bonn.whereismyband.entity;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
	private ArrayList<Genre> favoriteGenres;	
	
	public Band() {
	}
	
}
