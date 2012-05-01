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
 * A simple managed bean that is used to invoke the TemperatureConvertEJB and store the response. The response is obtained by
 * invoking getTemperature().
 * 
 * Code borrowed and modified from another quickstart written by Paul Robinson
 * 
 * @author Bruce Wolfe
 */

package org.jboss.as.quickstarts.temperatureconverter.controller;

import org.jboss.as.quickstarts.temperatureconverter.ejb.TemperatureConvertEJB;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.inject.Inject;
import java.io.Serializable;

@Named("temperatureConverter")
@SessionScoped
public class TemperatureConverter implements Serializable {
    private static final long serialVersionUID = 1785201108L;
    /**
     * Injected TemperatureConvertEJB client
     */
    @Inject
    private TemperatureConvertEJB temperatureConvertEJB;
    /**
     * Stores the response from the call to temperatureConvertEJB.convert()
     */
    private String temperature;
    private String sourceTemperature = "0.0";
    private String defaultScale = "C";

    /**
     * Invoke temperatureConvertEJB.convert() and store the temperature
     * 
     * @param sourceTemperature The temperature to be converted
     * @param defaultScale The default source temperature scale
     */
    public void convert() {
        temperature = temperatureConvertEJB.convert(sourceTemperature, defaultScale);
    }

    public String getSourceTemperature() {
        return sourceTemperature;
    }

    public void setSourceTemperature(String sourceTemperature) {
        this.sourceTemperature = sourceTemperature;
    }

    public String getDefaultScale() {
        return defaultScale;
    }

    public void setDefaultScale(String defaultScale) {
        this.defaultScale = defaultScale;
    }

    public String getTemperature() {
        return temperature;
    }

}
