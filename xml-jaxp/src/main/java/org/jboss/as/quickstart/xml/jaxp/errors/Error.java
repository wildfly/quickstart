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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Simple class which represents some sort of error.
 * @author baranowb
 * 
 */
public class Error {
    private Throwable t;
    private String message;

    /**
     * @param e
     * @param message
     */
    public Error(String message, Throwable t) {
        super();
        this.t = t;
        this.message = message;
    }

    public String getException()
    {
        if(this.t!=null)
        {
            return this.t.getMessage();
        }
        
        return null;
    }
    
    public String getExceptionTrace() {
        if (this.t != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            this.t.printStackTrace(ps);
            String stackTrace = new String(baos.toByteArray());
            return stackTrace;
        } else {
            return null;
        }
    }

    public String getMessage() {
        return message;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Error\n");
        if (this.message != null)
            sb.append("-> ").append(this.message).append("\n");
        if (this.t != null) {
            String stackTrace = getException();
            sb.append("-> ").append(stackTrace).append("\n");
        }

        return sb.toString();
    }

}