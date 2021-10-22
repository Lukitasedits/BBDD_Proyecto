package org.lukitasedits.animecalificaciones.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Descriptible {
    boolean isId() default false;
    boolean isString() default false;
    boolean isRelacional() default false;
}
