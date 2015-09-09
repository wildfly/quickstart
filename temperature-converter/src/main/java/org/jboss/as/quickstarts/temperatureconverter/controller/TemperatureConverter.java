/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
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
package org.jboss.as.quickstarts.temperatureconverter.controller;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.as.quickstarts.temperatureconverter.ejb.Scale;
import org.jboss.as.quickstarts.temperatureconverter.ejb.Temperature;
import org.jboss.as.quickstarts.temperatureconverter.ejb.TemperatureConvertEJB;

/**
 * A simple managed bean that is used to invoke the TemperatureConvertEJB and store the response. The response is obtained by
 * invoking temperatureConvertEJB.convert().
 *
 * Code borrowed and modified from another quickstart written by Paul Robinson
 *
 * @author Bruce Wolfe
 */
@SuppressWarnings("serial")
@Named("temperatureConverter")
@RequestScoped
public class TemperatureConverter implements Serializable {

    /*
     * Injected TemperatureConvertEJB client
     */
    @Inject
    private TemperatureConvertEJB temperatureConvertEJB;

    /*
     * Stores the response from the call to temperatureConvertEJB.convert()
     */
    private String temperature;

    private String sourceTemperature = "0.0";

    private Scale defaultScale = Scale.CELSIUS;

    /**
     * Invoke temperatureConvertEJB.convert() and store the temperature
     *
     * @param sourceTemperature The temperature to be converted
     * @param defaultScale The default source temperature scale
     */
    public void convert() {
        try {
            temperature = temperatureConvertEJB.convert(Temperature.parse(sourceTemperature, defaultScale)).toString();
        } catch (IllegalArgumentException e) {
            temperature = "Invalid temperature";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
        }
    }

    public String getSourceTemperature() {
        return sourceTemperature;
    }

    public void setSourceTemperature(String sourceTemperature) {
        this.sourceTemperature = sourceTemperature;
    }

    public Scale getDefaultScale() {
        return defaultScale;
    }

    public void setDefaultScale(Scale defaultScale) {
        this.defaultScale = defaultScale;
    }

    public String getTemperature() {
        return temperature;
    }

}
