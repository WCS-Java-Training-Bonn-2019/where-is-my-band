package com.wildcodeschool.sea.bonn.whereismyband.entity;

import java.sql.Date;

public class BandSearch {

	private Gender genderSelected;
	private String firstName;
	private String lastName;
	private Date birthday;
	private int postCode;
	private String city;

	public BandSearch(Gender genderSelected, String firstName, String lastName, Date birthday, int postCode, String city) {
		super();
		this.genderSelected = genderSelected;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthday = birthday;
		this.postCode = postCode;
		this.city = city;
	}

	public BandSearch() {
		 this.genderSelected = new Gender();		
	}

	public Gender getGenderSelected() {
		return genderSelected;
	}
	public void setGenderSelected(Gender genderSelected) {
		this.genderSelected = genderSelected;
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
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public int getPostCode() {
		return postCode;
	}
	public void setPostCode(int postCode) {
		this.postCode = postCode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "BandSearch [genderSelected=" + genderSelected.getId() + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", birthday=" + birthday + ", postCode=" + postCode + ", city=" + city + "]";
	}

}
