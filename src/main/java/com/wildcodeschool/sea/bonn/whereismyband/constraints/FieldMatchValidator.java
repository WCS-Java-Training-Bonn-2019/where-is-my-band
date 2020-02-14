package com.wildcodeschool.sea.bonn.whereismyband.constraints;

//Quelle: https://memorynotfound.com/field-matching-bean-validation-annotation-example/

import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

    private String firstFieldName;
    private String secondFieldName;
    private String message;

    // KrillMi, 08.02.2020
    // Einlesen der Annotationsparameter für die beiden Attributnamen und die message
    // Methode wird vor der Verwendung aufgerufen mit der Annotation als Parameter
    @Override
    public void initialize(final FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
        message = constraintAnnotation.message();
    }

    // KrillMi, 08.02.2020
    // Methode wird zur Laufzeit zur Überprüfung des jeweiligen Objektpärchens aufgerufen
    // Übergeben wird ein Objekt der  Klasse, die mit Fieldmatch annotiert wurde
    // Die beiden zu vergleichenden Werte werden über BeanUtils.getProperty ausgelesen
    @Override
    public boolean isValid(final Object objectToBeValidated, final ConstraintValidatorContext context) {
        boolean valid = true;
        try
        {
            final Object firstAttribute = BeanUtils.getProperty(objectToBeValidated, firstFieldName);
            final Object secondAttribute = BeanUtils.getProperty(objectToBeValidated, secondFieldName);

            valid =  firstAttribute == null && secondAttribute == null 
            		|| firstAttribute != null && firstAttribute.equals(secondAttribute);
        }
        catch (final Exception ignore)
        {
            // ignore
        }

        if (!valid){
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(firstFieldName)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }

        return valid;
    }
}