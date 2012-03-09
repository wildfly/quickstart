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

package org.jboss.as.quickstart.xml.schema;

import java.net.MalformedURLException;
import java.net.URL;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.jboss.as.quickstart.xml.annotation.SchemaURL;

/**
 * Simple CDI producer. Creates instance of "@SchmeURL" which will be provided by container to parser implementation.
 * 
 * @author baranowb
 * 
 */
public class SchemaURLFactory {
    
    /*
     * CDI factory method. Annotated with: 
     * - Produces - this indicates that method should be called to produces CDI bean if it is required.
     * - SchemaYURL - indicates that this method produces bean of this type.
     */
    @Produces
    @SchemaURL
    public URL createSchemaURL() throws MalformedURLException {
        URL url = new URL("http://localhost:8080/jboss-as-xml-jaxp/xsd/catalog.xsd");
        return url;
    }

}
