/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.redhat.datagrid.carmart.jsf;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import org.jboss.seam.faces.validation.InputField;

import com.redhat.datagrid.carmart.model.Car.Country;

/**
 * Validates correct format of a registration number. Uses Seam Faces' validation to validate the 
 * format based on multiple form fields.
 * 
 * @author Martin Gencur
 * 
 */
@FacesValidator("carValidator")
public class CarValidator implements Validator {
    private static Pattern czPattern1 = Pattern.compile("\\d[a-zA-Z]\\d\\s\\d{4}"); // e.g. "1B1 1216"
    private static Pattern czPattern2 = Pattern.compile("[a-zA-Z]{3}\\s\\d{2}-\\d{2}"); // e.g. "FML 24-27"

    @Inject
    @InputField
    private String numberPlate;

    @Inject
    @InputField
    private Country country;

    @Override
    public void validate(FacesContext arg0, UIComponent arg1, Object arg2) throws ValidatorException {
        String outcome = validateNumberPlate(country, numberPlate);
        if (outcome != null) {
            throw new ValidatorException(new FacesMessage(outcome));
        }
    }

    private String validateNumberPlate(Country country, String numberPlate) {
        if (country.equals(Country.CZECH_REPUBLIC)) {
            Matcher m1 = czPattern1.matcher(numberPlate);
            Matcher m2 = czPattern2.matcher(numberPlate);
            if (!(m1.matches() || m2.matches())) {
                return "You must enter the car's number in a valid pattern (e.g. FML 23-65 or 1B2 6565)";
            }
        }
        return null; // pattern OK -> return null
    }
}
