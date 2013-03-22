/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
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
        // Detects a whole tier of phones that support similar functionality as the iphone
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