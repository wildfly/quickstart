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

package org.jboss.as.quickstarts.ejbinwar.ejb;

import javax.ejb.Stateful;

/**
 * A simple Hello World EJB. The EJB does not use an interface.
 *
 * @author paul.robinson@redhat.com, 2011-12-21
 */
@Stateful
public class GreeterEJB
{
    /**
     * This method takes a name and returns a personalised greeting.
     *
     * @param name the name of the person to be greeted
     * @return the personalised greeting.
     */
	public String sayHello(String name) {
		return "Hello " + name;
	}
}
