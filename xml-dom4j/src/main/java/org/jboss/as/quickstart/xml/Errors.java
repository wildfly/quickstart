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
package org.jboss.as.quickstart.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

/**
 * Stores parsing errors
 *
 * @author baranowb
 *
 */
@SuppressWarnings("serial")
@SessionScoped
public class Errors implements Serializable {

    private List<Error> errorsList = new ArrayList<>();

    public void addErrorMessage(String severity, Exception e) {
        Error error = new Error(severity, e);
        this.errorsList.add(error);
    }

    @Produces
    @Named
    public List<Error> getErrorMessages() {
        return errorsList;
    }
}
