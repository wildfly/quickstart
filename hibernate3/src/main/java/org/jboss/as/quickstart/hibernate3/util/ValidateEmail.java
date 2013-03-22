/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
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
package org.jboss.as.quickstart.hibernate3.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class ValidateEmail implements Validator {

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object emailObject) throws ValidatorException {
        String emailAddress = (String) emailObject;
        /*
         * //Make sure it's not empty if (((String) emailAddress).isEmpty()) { FacesMessage facesMessage = new FacesMessage();
         * facesMessage.setSummary("Email address is required."); facesMessage.setDetail("Email address is required.");
         * facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR); facesContext.addMessage("reg:email", facesMessage); throw new
         * ValidatorException(facesMessage); }
         */
        // They entered something. Now do a simple validation. We won't worry about all variations.
        Pattern emailPattern = Pattern.compile("[a-zA-Z]*[0-9]*@[a-zA-Z]*.[a-zA-Z]*");
        Matcher emailMatcher = emailPattern.matcher(emailAddress);
        if (!emailMatcher.matches()) {
            FacesMessage facesMessage = new FacesMessage();
            facesMessage.setSummary("Email address is is not valid.");
            facesMessage.setDetail("Email address is not valid.");
            facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage("reg:email", facesMessage);
            throw new ValidatorException(facesMessage);

        }
    }

}
