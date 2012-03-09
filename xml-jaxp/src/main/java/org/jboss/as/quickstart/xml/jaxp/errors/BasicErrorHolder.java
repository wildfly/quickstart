/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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

package org.jboss.as.quickstart.xml.jaxp.errors;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 * Bean used to store information about errors from other beans. Since in CDI beans we dont want to be aware of real RE, we need
 * to abstract error handling. Its purpose is just to make error messages available, so we can display.
 * 
 * @author baranowb
 * 
 */
@SessionScoped
@Named(value="errorHolder")
public class BasicErrorHolder implements ErrorHolder{

    /**
     * 
     */
    private static final long serialVersionUID = 7522998068777439073L;
    private List<Error> errorsList = new ArrayList<Error>();

    @Override
    public void addErrorMessage(String msg, Throwable t) {
        Error error = new Error(msg, t);
        this.errorsList.add(error);
    }

    @Override
    public int getErrorCount() {
        return this.errorsList.size();
    }

    @Override
    public List<Error> getErrorMessages() {
        return new ArrayList<Error>(this.errorsList);
    }

    @Override
    public void clear() {
        this.errorsList.clear();
    }    
}
