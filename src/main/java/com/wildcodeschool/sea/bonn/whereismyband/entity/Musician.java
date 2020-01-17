package com.wildcodeschool.sea.bonn.whereismyband.entity;

import java.util.ArrayList;
import java.sql.Date;

public class Musician {
	
	// database id
	private Long id;
	private String firstName;
	private String lastName;
	private Date birthday;
	private Gender gender;
	// attribute for picture to be done
	private ArrayList<Genre> favoriteGenres;
	private ArrayList<Address> addresses;
	private ArrayList<Instrument> instrumentsPlayed;
	// bandpositions held to be done

	public Musician(Long id, String firstName, String lastName, Date birthdate, Gender gender,
			ArrayList<Genre> favoriteGenres, ArrayList<Address> addresses, ArrayList<Instrument> instrumentsPlayed) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthday = birthdate;
		this.gender = gender;
		this.favoriteGenres = favoriteGenres;
		this.addresses = addresses;
		this.instrumentsPlayed = instrumentsPlayed;
	}
	
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
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthdate(Date birthdate) {
		this.birthday = birthdate;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	

}
