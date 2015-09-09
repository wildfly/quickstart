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
package org.jboss.as.quickstarts.interapp.appB;

import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.as.quickstarts.interapp.shared.Bar;
import org.jboss.as.quickstarts.interapp.shared.Foo;

/**
 * <p>
 * JSF Controller class that allows manipulation of Foo and Bar.
 * </p>
 * <p>
 * Note that whilst EJBs are used to provide inter application communication, this is not apparent to consumers of Foo and Bar,
 * which use CDI style injection.
 * </p>
 *
 * @author Pete Muir
 *
 */
@Named
public class ControllerB {

    @Inject
    private Foo foo;

    @Inject
    private Bar bar;

    public String getFoo() {
        return foo.getName();
    }

    public void setFoo(String name) {
        foo.setName(name);
    }

    public String getBar() {
        return bar.getName();
    }

    public void setBar(String name) {
        bar.setName(name);
    }

    public void sendAndUpdate() {
        // No-op
    }
}
