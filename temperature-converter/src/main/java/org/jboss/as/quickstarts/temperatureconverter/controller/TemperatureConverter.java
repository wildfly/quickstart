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
