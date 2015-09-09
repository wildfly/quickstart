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
package org.jboss.as.quickstarts.temperatureconverter.ejb;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A domain object that can store a temperature and scale. Additionally, it can parse a string to a temperature and scale.
 *
 * @author Pete Muir
 * @author Bruce Wolfe
 *
 */
public class Temperature {

    public static final double ABSOLUTE_ZERO_C = -273.150;
    public static final double ABSOLUTE_ZERO_F = -459.670;

    /*
     * Create a regular expression to extract the temperature and scale (if passed).
     */
    private static Pattern PATTERN = Pattern.compile("^([-+]?\\d*\\.?\\d+)\\s*([cCfF]?)");

    /**
     * Parse a string, with an optional scale suffix. If no scale suffix is on the string, the defaultScale will be used.
     *
     * @param temperature the temperature to parse
     * @param defaultScale the default scale to use
     */
    public static Temperature parse(String temperature, Scale defaultScale) {
        double t;
        Scale s;

        /**
         * Extract temperature and scale
         */
        Matcher matcher = PATTERN.matcher(temperature);

        if (matcher.find()) {
            // Extract the temperature
            t = Double.parseDouble(matcher.group(1));

            // Use the scale included with the sourceTemperature OR the defaultScale provided.
            if (!matcher.group(2).isEmpty()) {
                try {
                    s = Scale.valueOfAbbreviation(matcher.group(2));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("You must provide a valid temperature to convert.");
                }
            } else {
                s = defaultScale;
            }
        } else {
            throw new IllegalArgumentException("You must provide a valid temperature to convert.");
        }

        return new Temperature(t, s);
    }

    private final double temperature;
    private final Scale scale;

    public Temperature(double temperature, Scale scale) {
        this.temperature = temperature;
        this.scale = scale;
    }

    public Scale getScale() {
        return scale;
    }

    public double getTemperature() {
        return temperature;
    }

    @Override
    public String toString() {
        return new DecimalFormat("###.###").format(temperature) + " " + scale.getSymbol();
    }

}
