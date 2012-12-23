/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
        Throwable throwable = (Throwable)  FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("javax.servlet.error.exception");
        StringBuilder builder = new StringBuilder();
        builder.append(throwable.getMessage()).append("\n");
        for (StackTraceElement element : throwable.getStackTrace()) {
            builder.append(element).append("\n");
        }
        return builder.toString();
    }

}
