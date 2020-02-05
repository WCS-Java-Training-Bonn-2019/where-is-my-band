package com.wildcodeschool.sea.bonn.whereismyband.services;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements
ConstraintValidator<PhoneNumberConstraint, String> {

  @Override
  public void initialize(PhoneNumberConstraint phoneNumber) {
  }

  @Override
  public boolean isValid(String phoneNumber,
    ConstraintValidatorContext cxt) {
      return phoneNumber != null && phoneNumber.matches("^(((((((00|\\+)49[ \\-/]?)|0)[1-9][0-9]{1,4})[ \\-/]?)|((((00|\\+)49\\()|\\(0)[1-9][0-9]{1,4}\\)[ \\-/]?))[0-9]{1,7}([ \\-/]?[0-9]{1,5})?)$")
        && (phoneNumber.length() > 5) && (phoneNumber.length() < 14);
  }

}