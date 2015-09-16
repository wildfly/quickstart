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

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class ValidateMemberId implements Validator {

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object memberIdObject) throws ValidatorException {
        // The id can only contain positive long values.
        try {
            long memberId = (Long) memberIdObject;
        } catch (Exception e) {
            FacesMessage facesMessage = new FacesMessage();
            facesMessage.setSummary("Member Id is not valid.");
            facesMessage.setDetail("Member Id must be an positive number.");
            facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage("reg:id", facesMessage);
            throw new ValidatorException(facesMessage);
        }
    }

}
