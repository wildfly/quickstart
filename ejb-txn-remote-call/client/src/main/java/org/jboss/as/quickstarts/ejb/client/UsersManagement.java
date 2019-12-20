/*
 * JBoss, Home of Professional Open Source
 * Copyright 2021, Red Hat, Inc. and/or its affiliates, and individual
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

package org.jboss.as.quickstarts.ejb.client;

import org.jboss.as.quickstarts.ejb.entity.CallerUser;
import org.jboss.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * EJB which runs the remote calls to the EJBs.
 * We use the EJB here for benefit of automatic transaction management.
 */
@Stateless
public class UsersManagement {
    private static final Logger log = Logger.getLogger(UsersManagement.class);
    private static final String tableFormatStringForPrint = "%5s%32s%32s%n";

    @PersistenceContext
    EntityManager em;

    public List<CallerUser> getUsers() {
        return em.createQuery(
                "SELECT u FROM " + CallerUser.class.getSimpleName() + " u").getResultList();
    }

    public String printUsers() {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format(tableFormatStringForPrint, "ID", "First Name", "Last Name"));
        sb.append(String.format(tableFormatStringForPrint, "---", "---", "---"));
        for(CallerUser user: getUsers()) {
            sb.append(String.format(tableFormatStringForPrint,
                    user.getId(), user.getFirstName(), user.getLastName()));
        }
        return sb.toString();
    }
}
