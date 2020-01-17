package com.wildcodeschool.sea.bonn.whereismyband.entity;

import java.sql.Date;
import java.util.ArrayList;

public class BandSearch {

	private Gender genderSelected;
	private String firstName;
	private String lastName;
	private Date birthday;
	private int postCode;
	private String city;
	private ArrayList<Instrument> instrumentsSelected;
	private ArrayList<SkillLevel> instrumentSkills;
	private ArrayList<Genre> genresSelected;

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
		 this.instrumentsSelected = new ArrayList<Instrument>();
		 this.genresSelected = new ArrayList<Genre>();
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

	public ArrayList<Instrument> getInstrumentsSelected() {
		return instrumentsSelected;
	}

	public void setInstrumentsSelected(ArrayList<Instrument> instrumentsSelected) {
		this.instrumentsSelected = instrumentsSelected;
	}

	public ArrayList<Genre> getGenresSelected() {
		return genresSelected;
	}

	public void setGenresSelected(ArrayList<Genre> genresSelected) {
		this.genresSelected = genresSelected;
	}

	public ArrayList<SkillLevel> getInstrumentSkills() {
		return instrumentSkills;
	}

	public void setInstrumentSkills(ArrayList<SkillLevel> instrumentSkills) {
		this.instrumentSkills = instrumentSkills;
	}

	@Override
	public String toString() {
		return "BandSearch [genderSelected=" + genderSelected + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", birthday=" + birthday + ", postCode=" + postCode + ", city=" + city + ", instrumentsSelected="
				+ instrumentsSelected + ", instrumentSkills=" + instrumentSkills + ", genresSelected=" + genresSelected
				+ "]";
	}

}
