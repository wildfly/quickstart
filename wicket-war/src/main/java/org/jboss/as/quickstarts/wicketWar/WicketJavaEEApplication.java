/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.wicketWar;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.jboss.as.quickstarts.wicketWar.pages.InsertContact;
import org.jboss.as.quickstarts.wicketWar.pages.ListContacts;
import org.wicketstuff.javaee.injection.JavaEEComponentInjector;
import org.wicketstuff.javaee.naming.global.ModuleJndiNamingStrategy;


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
        getComponentInstantiationListeners().add(new JavaEEComponentInjector(this, new ModuleJndiNamingStrategy()));
        mountPage("/insert", InsertContact.class);
    }
    
}
