package com.wildcodeschool.sea.bonn.whereismyband.entity;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Band {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// database id
	private Long id;
	private String name;
	// picture to be done
	private String address;
	private String email;
	private String phone;
	private ArrayList<Genre> favoriteGenres;	
	
	public Band() {
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public ArrayList<Genre> getFavoriteGenres() {
		return favoriteGenres;
	}
	public void setFavoriteGenres(ArrayList<Genre> favoriteGenres) {
		this.favoriteGenres = favoriteGenres;
	}

}
