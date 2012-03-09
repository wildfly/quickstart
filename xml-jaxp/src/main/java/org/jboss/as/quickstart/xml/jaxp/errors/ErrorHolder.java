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

import java.io.Serializable;
import java.util.List;

/**
 * Error holder interface declaration. This hides how errors are stored.
 * 
 * @author baranowb
 * 
 */
public interface ErrorHolder extends Serializable{
    /**
     * Adds error to storage.
     * @param msg
     * @param t
     */
    public void addErrorMessage(String msg, Throwable t);

    /**
     * Returns current number of errors
     * @return
     */
    public int getErrorCount();
    /**
     * Return string representation of errors. This method will clear error storage.
     * @return
     */
    public List<Error> getErrorMessages();
    
    /**
     * Removes all errors stored 
     */
    public void clear();
}
