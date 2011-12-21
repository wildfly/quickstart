/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates,
 * and individual contributors as indicated by the @author tags.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 * (C) 2011,
 * @author JBoss, by Red Hat.
 */

package org.jboss.as.quickstarts.ejbinwar.controller;

import org.jboss.as.quickstarts.ejbinwar.ejb.GreeterEJB;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * A simple managed bean that is used to invoke the GreeterEJB and store the response. The response is obtained by
 * invoking getMessage().
 *
 * @author paul.robinson@redhat.com, 2011-12-21
 */
@Named("greeter")
@SessionScoped
public class Greeter implements Serializable {

	/**
	 * Injected GreeterEJB client
	 */
	@EJB
	private GreeterEJB greeterEJB;

	/**
	 * Stores the response from the call to greeterEJB.sayHello(...)
	 */
	private String message;

	/**
	 * Invoke greeterEJB.sayHello(...) and store the message
     *
     * @param name The name of the person to be greeted
	 */
	public void setName(String name) {
		message = greeterEJB.sayHello(name);
	}

    /**
     * Get the greeting message, customized with the name of the person to be greeted.
     *
     * @return message. The greeting message.
     */
	public String getMessage() {
		return message;
	}

}
