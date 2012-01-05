package org.jboss.as.quickstarts.payment.qualifiers;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Qualifier
@Target({ElementType.METHOD,ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Credit {
}
