package com.wildcodeschool.sea.bonn.whereismyband.entity;

public class Address {
	
	// database id
	private Long id;
	private int postCode;
	private String city;

	public Address(Long id, int zipCode, String city) {
		super();
		this.id = id;
		this.postCode = zipCode;
		this.city = city;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getPostCode() {
		return postCode;
	}
	public void setZipCode(int zipCode) {
		this.postCode = zipCode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}

	
}
