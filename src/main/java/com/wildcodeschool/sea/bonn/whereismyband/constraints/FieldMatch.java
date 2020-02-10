package com.wildcodeschool.sea.bonn.whereismyband.constraints;

// Quelle: https://memorynotfound.com/field-matching-bean-validation-annotation-example/

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

// KrillMi, 08.02.2020:
// Quelle: https://memorynotfound.com/field-matching-bean-validation-annotation-example/
// Verwendung: 
// - @FieldMatch(first = "password", second = "passwordRepeated", message = "XXX")
// oder
// - @Fieldmatch.List ({ @FieldMatch(...), @FieldMatch(...) })
// 
// Target: Annotation darf auf Typ [Klassenebene] verwendet werden
// Retention: Annotation ist zur Laufzeit sichtbar (z.B. via Reflection fuer Spring)
// Constraint: gibt die Klasse an, die die Logik der Annotation implementiert
// Documented: Annotation wird auch in generiertes JavaDoc aufgenommen

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = FieldMatchValidator.class)
@Documented
public @interface FieldMatch
{
    // KrillMi => es folgen die Elemente/Attribute der Annotation
	String message() default "The fields must match";

	// KrillMi => first & second nehmen die Attribbute auf, die gleich sein müssen
    String first();
    String second();

	// KrillMi => zwei Elemente zur Wahrung der Spring-Kompatibilität
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    // KrillMi => in einer Liste können auch mehrere Attribute anotiert werden
    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List
    {
        FieldMatch[] value();
    }
}