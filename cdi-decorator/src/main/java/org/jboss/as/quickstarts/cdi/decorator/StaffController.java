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
package org.jboss.as.quickstarts.cdi.decorator;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Simple JSF backing bean, demonstrating CDI injection.
 *
 * Bean name overridden to "staff" to be accessible from view with this name.
 *
 * The {@link Inject} says that it is CDI bean and should be injected.
 *
 * @author Ievgen Shulga
 */
@Named(value = "staff")
public class StaffController {

    @Inject
    private StaffService staffService;

    public String getPosition() {
        return staffService.getStaff().getPosition();
    }

    public Integer getBonus() {
        return staffService.getStaff().getBonus();
    }
}
