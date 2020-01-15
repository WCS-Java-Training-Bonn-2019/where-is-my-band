package com.wildcodeschool.sea.bonn.whereismyband.entity;

import java.util.ArrayList;
import java.util.Date;

public class Musician {
	
	// database id
	private Long id;
	private String firstName;
	private String lastName;
	private Date birthdate;
	private Gender gender;
	// attribute for picture to be done
	private ArrayList<Genre> favoriteGenres;
	private ArrayList<Address> addresses;
	private ArrayList<Instrument> instrumentsPlayed;
	// bandpositions held to be done

	public Long getId() {
		return id;
	}
	public ArrayList<Genre> getFavoriteGenres() {
		return favoriteGenres;
	}
	public void setFavoriteGenres(ArrayList<Genre> favoriteGenres) {
		this.favoriteGenres = favoriteGenres;
	}
	public ArrayList<Address> getAddresses() {
		return addresses;
	}
	public void setAddresses(ArrayList<Address> addresses) {
		this.addresses = addresses;
	}
	public ArrayList<Instrument> getInstrumentsPlayed() {
		return instrumentsPlayed;
	}
	public void setInstruments(ArrayList<Instrument> instruments) {
		this.instrumentsPlayed = instruments;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Date getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	

}
