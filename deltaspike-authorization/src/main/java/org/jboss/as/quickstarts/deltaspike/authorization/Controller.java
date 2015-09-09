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
package org.jboss.as.quickstarts.deltaspike.authorization;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * The secured controller restricts access to certain method
 *
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 *
 */
// Expose the bean to EL
@Named
public class Controller {

    @Inject
    private FacesContext facesContext;

    //This method is allowed only to users with employee role
    @EmployeeAllowed
    public void employeeMethod() {
        facesContext.addMessage(null, new FacesMessage("You executed a @EmployeeAllowed method"));
    }

    //This method is allowed only to users with admin role
    @AdminAllowed
    public void adminMethod() {
        facesContext.addMessage(null, new FacesMessage("You executed a @AdminAllowed method"));
    }

    //Invalidate the session and send a redirect to index.html
    public void logout() throws IOException {
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        session.invalidate();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.sendRedirect("index.html");
        facesContext.responseComplete();
    }

    //This method return the stack trace string from the Exception
    public String getStackTrace() {
        Throwable throwable = (Throwable) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("javax.servlet.error.exception");
        StringBuilder builder = new StringBuilder();
        builder.append(throwable.getMessage()).append("\n");
        for (StackTraceElement element : throwable.getStackTrace()) {
            builder.append(element).append("\n");
        }
        return builder.toString();
    }

}
