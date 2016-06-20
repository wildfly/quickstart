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
package org.jboss.as.quickstarts.wicketEar.war;

import org.apache.wicket.Page;
import org.apache.wicket.cdi.CdiConfiguration;
import org.apache.wicket.protocol.http.WebApplication;
import org.jboss.as.quickstarts.wicketEar.war.pages.InsertContact;
import org.jboss.as.quickstarts.wicketEar.war.pages.ListContacts;

/**
 *
 * @author Ondrej Zizka
 */
public class WicketJavaEEApplication extends WebApplication {

    @Override
    public Class<? extends Page> getHomePage() {
        return ListContacts.class;
    }

    @Override
    protected void init() {
        super.init();

        // Configure CDI, disabling Conversations as we aren't using them
        new CdiConfiguration().configure(this);

        // Mount the InsertContact page at /insert
        mountPage("/insert", InsertContact.class);
    }

}
