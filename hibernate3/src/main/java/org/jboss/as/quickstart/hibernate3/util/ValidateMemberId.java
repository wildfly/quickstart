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
