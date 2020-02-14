package com.wildcodeschool.sea.bonn.whereismyband.entity;

import javax.validation.constraints.NotBlank;

import com.wildcodeschool.sea.bonn.whereismyband.constraints.FieldMatch;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FieldMatch.List({
    @FieldMatch(first = "password", second = "passwordRepeated", message = "Die Passwortfelder sind nicht identisch. Bitte überprüfen!!!"),
})
public class RegistrationForm extends MusicianForm{
	
	public RegistrationForm() {
		super();
	}

	@NotBlank
	private String password;

	@NotBlank
	private String passwordRepeated;
}
