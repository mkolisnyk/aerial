package com.github.mkolisnyk.aerial.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.mkolisnyk.aerial.core.params.AerialSourceType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE })
public @interface Aerial {
    AerialSourceType inputType() default AerialSourceType.FILE;
    String source();
    String destination();
    String[] additionalParams() default { };
}
