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
 * (C) 2012,
 * @author Sherif Makary Red Hat MW SA.*/


package org.jboss.as.quickstarts.ejb_security;

import javax.ejb.Stateless;
import org.jboss.as.quickstarts.ejb_security.SecuredEJB;
import java.security.Principal;
import javax.ejb.SessionContext;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;

/**
 * <p>
 * Simple secured ejb
 * using ejb security annotations
 * </p>
 * 
 * @author Sherif Makary 
 * 
 */

@Stateless
public class SecuredEJBBean implements SecuredEJB {

	private Principal principal= null;
			
	//Inject Session Context
	@Resource	SessionContext ctx;
		
	/**
	 * <p>
	 * sample Secured ejb method using security annotations
	 * </p>
	 * 
	 */	
		
	@Override
	@RolesAllowed({"gooduser"})
	public String getSecurityInfo()
	{
		//Session context injected using the resource annotation
		principal = ctx.getCallerPrincipal();
	
		return principal.toString();
	}
}
