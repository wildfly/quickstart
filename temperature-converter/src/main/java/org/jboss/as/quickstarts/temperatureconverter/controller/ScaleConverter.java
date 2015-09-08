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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

import org.jboss.as.quickstarts.temperatureconverter.ejb.Scale;

/**
 * A JSF converter that can handle the {@link Scale} enum.
 *
 * @author Pete Muir
 *
 */
@Named
public class ScaleConverter implements Converter {

    public Scale[] getScales() {
        return Scale.values();
    }

    @Override
    public Object getAsObject(FacesContext ctx, UIComponent cmp, String value) {
        return Scale.valueOf(value);
    }

    @Override
    public String getAsString(FacesContext ctx, UIComponent cmp, Object value) {
        if (value instanceof Scale) {
            return ((Scale) value).name();
        } else {
            throw new IllegalStateException();
        }
    }

}
