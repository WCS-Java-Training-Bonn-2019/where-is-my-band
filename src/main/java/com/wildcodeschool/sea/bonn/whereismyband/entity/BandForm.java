package com.wildcodeschool.sea.bonn.whereismyband.entity;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.wildcodeschool.sea.bonn.whereismyband.constraints.JPEGConstraint;
import com.wildcodeschool.sea.bonn.whereismyband.constraints.PhoneNumberConstraint;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BandForm {

	public BandForm() {
		super();
	}
	
	@NotBlank
	private String name;
	
	private String description;
	
	@NotBlank
	private String email;
	
	@NotBlank
	private String city;
	
	@Pattern( regexp = "^[0-9]{5}$", message="Bitte eine fünfstellige Zahl eingeben!!!")
	private String postCode;
	
	@NotNull
	private List<Bandposition> bandPositions = new ArrayList<>();
	
	@PhoneNumberConstraint(message = "Bitte eine gültige Telefonnummer eingeben!")
	private String phone;
	
	@JPEGConstraint(message = "Datei enthält kein Bild im JPEG-Format!")
	private byte[] image;
}
