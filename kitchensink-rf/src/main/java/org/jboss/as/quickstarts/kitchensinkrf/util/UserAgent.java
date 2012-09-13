/**
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
 **/
package org.jboss.as.quickstarts.kitchensinkrf.util;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @author jbalunas@redhat.com
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@Model
public class UserAgent implements Serializable {

    private static final long serialVersionUID = 1L;
    private UAgentInfo uAgentInfo;

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        String userAgentStr = request.getHeader("user-agent");
        String httpAccept = request.getHeader("Accept");
        uAgentInfo = new UAgentInfo(userAgentStr, httpAccept);
    }

    public boolean isPhone() {
        //Detects a whole tier of phones that support similar functionality as the iphone
        return uAgentInfo.detectTierIphone();
    }

    public boolean isTablet() {
        // Will detect iPads, Xooms, Blackberry tablets, but not Galaxy - they use a strange user-agent
        return uAgentInfo.detectTierTablet();
    }

    public boolean isMobile() {
        return isPhone() || isTablet();
    }
}