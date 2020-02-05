package com.wildcodeschool.sea.bonn.whereismyband.services;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PostCodeValidator implements
ConstraintValidator<PostCodeConstraint, String> {

  @Override
  public void initialize(PostCodeConstraint phoneNumber) {
  }

  @Override
  public boolean isValid(String postCode,
    ConstraintValidatorContext cxt) {
      return postCode != null && postCode.matches("^((0(1\\d\\d[1-9])|([2-9]\\d\\d\\d))|(?(?=^(^9{5}))|[1-9]\\d{4}))$")
        && (postCode.length() > 4) && (postCode.length() < 6);
  }

}