package com.wildcodeschool.sea.bonn.whereismyband.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.wildcodeschool.sea.bonn.whereismyband.services.ImageService;

public class JPEGValidator implements
ConstraintValidator<JPEGConstraint, byte[]> {

	ImageService imageService;
	
	@Override
	public void initialize(JPEGConstraint image) {
	}

	@Override
	public boolean isValid(byte[] image,
			ConstraintValidatorContext cxt) {
		
		if (image != null ) {
			return imageService.getImageType(image) == "JPEG";
		} else
			return true;
	}

}