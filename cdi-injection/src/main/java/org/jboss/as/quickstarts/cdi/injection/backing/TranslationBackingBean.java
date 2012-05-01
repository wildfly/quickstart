/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the 
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
