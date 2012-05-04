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
/**
 * A simple SLSB EJB. The EJB does not use an interface.
 * 
 * @author Bruce Wolfe
 */

package org.jboss.as.quickstarts.temperatureconverter.ejb;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;


@Stateless
public class TemperatureConvertEJB {
    /**
     * This method takes a temperature in Celsius or Fahrenheit and converts it to the other value.
     * 
     * @param sourceTemperature The temperature to convert from, assumes format of "xx.x C|F"
     * @param defaultScale Either "C" for Celsius or "F" for Fahrenheit.
     * @return convertedTemperature Converted temperature.
     */
    public String convert(String sourceTemperature, String defaultScale) {

        final long ABSOLUTE_ZERO_C = -273150;  // Celsius value * 1000
        final long ABSOLUTE_ZERO_F = -459670;  // Fahrenheit value * 1000
        long temperatureToConvert;
        double convertedTemperature = 0;
        String convertScale;
        String convertToScale = " Err";
        String conversionError = convertedTemperature + convertToScale;

        /* Set up the use of RegEx to extract the temperature and scale (if passed).
         * NB: Could get more clever and insist on C|F being after the number, and a single char...
         *     but that is an exercise for someone else to try */
        Pattern temperaturePattern = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+");
        Pattern scalePattern = Pattern.compile("[CF]", Pattern.CASE_INSENSITIVE);
        Matcher extractTemperature = temperaturePattern.matcher(sourceTemperature);
        Matcher extractScale = scalePattern.matcher(sourceTemperature);

        // Use the scale included with the sourceTemperature OR the defaultScale provided.
        if (extractScale.find()) {
            convertScale = extractScale.group();
        } else {
            // Make sure defaultScale has a valid value also
            convertScale = extractScale.reset(defaultScale).find() ? extractScale.group() : "";
        }

        // Check we were passed a scale. Should never occur in current JSF > ManagedBean > SLSB model.
        if (convertScale.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("You must provide a valid temperature scale- 'C|F'"));
            return conversionError;
        }

        // Extract temperatureToConvert from the sourceTemperature. Prepare for 3 decimal places.
        if (extractTemperature.find()) {
            temperatureToConvert = (long)(Double.parseDouble(extractTemperature.group()) * 1000);
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("You must provide a valid temperature to convert- 'XX.XXX'"));
            return conversionError;
        }

        // Convert our Temperature
        if (convertScale.equalsIgnoreCase("C")) {                       // Celsius to Fahrenheit
           // Easter egg for Absolute Zero.
            if (temperatureToConvert < ABSOLUTE_ZERO_C) {
                convertToScale = " \u2109 (below Absolute Zero!)";
            } else if (temperatureToConvert == ABSOLUTE_ZERO_C) {
                convertToScale = " \u2109 (Absolute Zero!)";
            } else {
                convertToScale = " \u2109";    
            }
            convertedTemperature = temperatureToConvert * 9 / 5 + 32000;
            convertedTemperature /= 1000;                               // Result to 3 decimal places
        } else if (convertScale.equalsIgnoreCase("F")) {                // Fahrenheit to Celsius
            // Easter egg for Absolute Zero.
            if (temperatureToConvert < ABSOLUTE_ZERO_F) {
                convertToScale = " \u2103 (below Absolute Zero!)";
            } else if (temperatureToConvert == ABSOLUTE_ZERO_F) {
                convertToScale = " \u2103 (Absolute Zero!)";
            } else {
                convertToScale = " \u2103";
            }
            convertedTemperature = (temperatureToConvert - 32000) * 5 / 9;
            convertedTemperature /= 1000;                               // Result to 3 decimal places
        } else {                                                        // Should never get here!
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("This is embarrassing - this error should NOT occur!"));
            return conversionError;
        }

        // Return our converted temperature as a string with the scale appended.
        return convertedTemperature + convertToScale;
    }
}
