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
package org.jboss.as.quickstarts.ejb.multi.server.app;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import org.jboss.logging.Logger;

/**
 * A simple JSF controller to show how the EJB invocation on different servers.
 *
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
@Model
public class JsfController {
    private static final Logger LOOGER = Logger.getLogger(JsfController.class);
    private EjbInvocation invocation;

    /**
     * Inject the 'standard' bean.<br/>
     * The simple @EJB injection is valid only if the MainApp is unique within the same application EAR archive.
     * Since there is a MainAppSContextBean using same interface, we can use this @EJB approach and specify
     * the name and interface as shown here to specify which exact reference should be injected.<br/>
     * The beanInterface and the mappedName can be used in this case, but it is not necessary if the beanName is unique and implement only one interface.
     */
    @EJB(beanName = "MainAppBean", beanInterface = MainApp.class)
    MainApp mainApp;

    /**
     * Inject a different bean implementation of the same interface.<br/>
     * Or use the @Resource annotation with the lookup name only.
     */
    @Resource(mappedName = "ejb:ejb-multi-server-app-main/ejb/MainAppSContextBean!org.jboss.as.quickstarts.ejb.multi.server.app.MainApp")
    MainApp mainAppScopedContext;

    /**
     * Injection with @EJB is not possible for foreign application in a different server. For this we can use @Resource.<br/>
     * Lookup is introduced in Java EE6, so there are compiler or runtime problems if a Java version is used which not contain
     * the <code>javax.annotation.Resource</code> <code>lookup</code>.
     * Therefore a fix/workaround is necessary to be able to compile.
     * See <a href="http://jaitechwriteups.blogspot.co.uk/2011/02/resource-and-new-lookup-attribute-how.html">Jaikiran's technical blog<a>
     */
    @Resource(lookup = "ejb:ejb-multi-server-app-one/ejb//AppOneBean!org.jboss.as.quickstarts.ejb.multi.server.app.AppOne")
    AppOne oneApp;

    /**
     * Injection with @EJB is not possible for a foreign application in a different server. For this we can use @Resource.
     * Here, we use <code>mappedName</code>, which was available prior to Java EE 7, to avoid compilation errors.
     */
    @Resource(mappedName = "ejb:ejb-multi-server-app-two/ejb//AppTwoBean!org.jboss.as.quickstarts.ejb.multi.server.app.AppTwo")
    AppTwo twoApp;

    /**
     * Initialize the controller.
     */
    @PostConstruct
    public void initForm() {
        this.invocation = new EjbInvocation();
    }

    @Produces
    @Named
    public EjbInvocation getInvocation() {
        return this.invocation;
    }

    public void callEJBMainLocal() {
        LOOGER.info("Try to invoke the local MainApp to log the given text and get the invocation results. Proxy=" + mainApp);
        this.invocation.setResult(mainApp.invokeAll(this.invocation.getText()));
    }

    public void callEJBMainScopedContextLocal() {
        LOOGER.info("Try to invoke the local MainAppSContext to log the given text and get the invocation results. Proxy=" + mainAppScopedContext);
        this.invocation.setResult(mainAppScopedContext.invokeAll(this.invocation.getText()));
    }

    public void callEJBAppOneRemote() {
        LOOGER.info("Try to invoke the remote AppOne to log the given text and get the invocation results. Proxy=" + oneApp);
        this.invocation.setResult(oneApp.invoke(this.invocation.getText()));
    }

    public void callEJBAppTwoRemote() {
        LOOGER.info("Try to invoke the remote AppTwo to log the given text and get the invocation results. Proxy=" + twoApp);
        this.invocation.setResult(twoApp.invoke(this.invocation.getText()));
    }
}
