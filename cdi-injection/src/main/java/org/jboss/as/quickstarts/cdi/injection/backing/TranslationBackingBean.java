package org.jboss.as.quickstarts.cdi.injection.backing;

import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.as.quickstarts.cdi.injection.TranslateService;
import org.jboss.as.quickstarts.cdi.injection.qualifier.English;
import org.jboss.as.quickstarts.cdi.injection.qualifier.Spanish;

/**
 * Simple JSF backing bean, demonstrating CDI injection and qualifiers.
 *
 * Also note, this bean has a name different than the default name.
 *
 * @author Jason Porter
 */
@Named("translation")
public class TranslationBackingBean {

    /*
        Both of these injections is of the same base type: TranslationService, however,
        CDI is using the qualifiers to help narrow which of the two implementations
        should be injected.
     */

    @Inject @Spanish
    private TranslateService spanishTranslateService;

    @Inject @English
    private TranslateService englishTranslateService;

    public String getSpanishHello() {
        return spanishTranslateService.hello();
    }

    public String getEnglishHello() {
        return englishTranslateService.hello();
    }
}
