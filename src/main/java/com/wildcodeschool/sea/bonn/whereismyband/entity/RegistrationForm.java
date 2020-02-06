package com.wildcodeschool.sea.bonn.whereismyband.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.wildcodeschool.sea.bonn.whereismyband.constraints.FieldMatch;
import com.wildcodeschool.sea.bonn.whereismyband.constraints.PhoneNumberConstraint;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FieldMatch.List({
    @FieldMatch(first = "password", second = "passwordRepeated", message = "Die Passwortfelder müssen passen!!!"),
    @FieldMatch(first = "username", second = "usernameRepeated", message = "Die Benutzernamen müssen passen!!!")
})
public class RegistrationForm {

	@NotBlank
	private String firstName;

	@NotBlank
	private String lastName;

	private String description;

	@NotBlank
	private String username;

	@NotBlank
	private String usernameRepeated;
	
	@NotBlank
	private String password;

	@NotBlank
	private String passwordRepeated;

	@PhoneNumberConstraint(message = "Bitte eine gültige Telefonnummer eingeben!")
	private String phone;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@NotNull(message = "Bitte gib ein Geburtsdatum an!")
	private LocalDate birthday;

	@NotNull(message = "Das Geschlecht muss angegeben werden!")
	private Gender gender;

	@Size(min=5, max = 5)
	@Pattern( regexp = "^[0-9]{5}$", message="Ungültige Postleitzahl!!!")
	private String postCode;
	
	@NotBlank
	private String city;
	
	@NotNull
	private Set<Genre> genres = new HashSet<>();

	@NotNull
	private Set<Instrument> instruments = new HashSet<>();

	private byte[] image;

	public RegistrationForm() {
		super();
	}
	
}
