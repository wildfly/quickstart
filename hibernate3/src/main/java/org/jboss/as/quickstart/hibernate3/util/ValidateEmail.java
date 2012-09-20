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
        //Make sure it's not empty
        if (((String) emailAddress).isEmpty()) {
            FacesMessage facesMessage = new FacesMessage();
            facesMessage.setSummary("Email address is required.");
            facesMessage.setDetail("Email address is required.");
            facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage("reg:email", facesMessage);
            throw new ValidatorException(facesMessage);
        }
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
