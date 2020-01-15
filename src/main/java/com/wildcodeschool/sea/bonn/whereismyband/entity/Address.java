package com.wildcodeschool.sea.bonn.whereismyband.entity;

public class Address {
	
	// database id
	private Long id;
	private int zipCode;
	private String city;

	public Address(Long id, int zipCode, String city) {
		super();
		this.id = id;
		this.zipCode = zipCode;
		this.city = city;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getZipCode() {
		return zipCode;
	}
	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}

	
}
