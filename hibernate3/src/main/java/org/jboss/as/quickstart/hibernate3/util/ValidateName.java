package org.jboss.as.quickstart.hibernate3.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class ValidateName implements Validator {


    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object nameObject) throws ValidatorException {
        String nameRegistered = (String) nameObject;
        
        // The name can only contain letters and spaces.
        Pattern namePattern = Pattern.compile("[A-Za-z ]*");
        Matcher nameMatcher = namePattern.matcher(nameRegistered);
        if (!nameMatcher.matches()) {
            FacesMessage facesMessage = new FacesMessage();
            facesMessage.setSummary("Name is not valid.");
            facesMessage.setDetail("Name must contain only letters and spaces.");
            facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage("reg:name", facesMessage);
            throw new ValidatorException(facesMessage);         
        }
        
    }

}
