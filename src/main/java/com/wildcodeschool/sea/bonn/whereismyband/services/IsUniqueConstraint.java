//package com.wildcodeschool.sea.bonn.whereismyband.services;
//
//import static java.lang.annotation.ElementType.FIELD;
//import static java.lang.annotation.RetentionPolicy.RUNTIME;
//
//import java.lang.annotation.Documented;
//import java.lang.annotation.Retention;
//import java.lang.annotation.Target;
//
//import javax.validation.Constraint;
//import javax.validation.Payload;
//
//@Target( FIELD )
//@Retention(RUNTIME)
//@Constraint(validatedBy = IsUniqueValidator.class)
//@Documented
////@Repeatable(List.class)
//public @interface IsUniqueConstraint  {
//
//	String message() default "Bereits vorhanden, bitte eine andere Zeichenkette wählen.";
//
//	Class<?>[] groups() default { };
//
//	Class<? extends Payload>[] payload() default { };
//
//}
//
