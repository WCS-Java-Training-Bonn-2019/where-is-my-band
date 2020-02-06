package com.wildcodeschool.sea.bonn.whereismyband.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.wildcodeschool.sea.bonn.whereismyband.services.PostCodeConstraint;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Address {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@PostCodeConstraint(message="Ungültige Postleitzahl!!!")
	private String postCode;
	private String city;
	
	@OneToOne(mappedBy = "address")
	private Band band;
			
}

