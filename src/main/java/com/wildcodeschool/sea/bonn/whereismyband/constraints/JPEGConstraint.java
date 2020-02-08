package com.wildcodeschool.sea.bonn.whereismyband.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

// Siehe https://www.baeldung.com/spring-mvc-custom-validator
// Zu annotieren über @JPEGConstraint

@Documented
@Constraint(validatedBy = JPEGValidator.class)
@Target( { ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface JPEGConstraint {
    String message() default "Bild hat kein JPEG-Format!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}