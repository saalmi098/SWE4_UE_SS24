package swe4.mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Retention gibt an, wann die Annotation sichtbar ist
// SOURCE: wird nur im Source Code verwendet, nicht im .class File (nur der Compiler sieht sie)
// CLASS: wird im .class File gespeichert, aber nicht zur Laufzeit verfügbar
// RUNTIME: wird im .class File gespeichert und zur Laufzeit verfügbar
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
    String name() default "";
}
