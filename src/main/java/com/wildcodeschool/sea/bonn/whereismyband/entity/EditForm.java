package com.wildcodeschool.sea.bonn.whereismyband.entity;

import com.wildcodeschool.sea.bonn.whereismyband.constraints.FieldMatch;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FieldMatch.List({
    @FieldMatch(first = "password", second = "passwordRepeated", message = "Die Passwortfelder sind nicht identisch. Bitte überprüfen!!!"),
})
public class EditForm extends MusicianForm {

	public EditForm() {
		super();
	}
	
	private String password;

	private String passwordRepeated;
}
