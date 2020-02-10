package com.wildcodeschool.sea.bonn.whereismyband.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.wildcodeschool.sea.bonn.whereismyband.services.ImageService;

public class JPEGValidator implements
ConstraintValidator<JPEGConstraint, byte[]> {

	private ImageService imageService;
	
	@Autowired
	public void setImageService(ImageService imageService) {
		this.imageService = imageService;
	}

	@Override
	public void initialize(JPEGConstraint image) {
	}

	@Override
	public boolean isValid(byte[] imageArray,
			ConstraintValidatorContext validatorContext) {
		
		// initialize result variable
		boolean valid = false;
		
		// if image was passed
		if (imageArray.length > 0 ) {

			valid = imageService.getImageType(imageArray) == "JPEG";
		
		} else {
			valid = true;
		}
				
		return valid;

	}

}