package org.jboss.as.quickstarts.cdi.injection;

import org.jboss.as.quickstarts.cdi.injection.qualifier.English;

/**
 * A simple implementation for the English language.
 *
 * The {@link English} qualifier tells CDI this is a special instance of
 * the {@link TranslateService}.
 *
 * @author Jason Porter
 *
 */
@English
public class EnglishTranslateService implements TranslateService {

   @Override
   public String hello() {
     return "Hello";
   }

}
