/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
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
package org.jboss.as.quickstarts.kitchensinkjsp.controller;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.as.quickstarts.kitchensinkjsp.model.Member;

// The @Stateful annotation eliminates the need for manual transaction demarcation
@Stateful
// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://www.cdi-spec.org/faq/#accordion6
@Model
public class MemberRegistration {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<Member> memberEventSrc;

    private Member newMember;

    @Produces
    @Named
    public Member getNewMember() {

        log.info("getNewMember: called" + newMember);
        return newMember;

    }

    public void register() throws Exception {

        try {

            log.info("Registering " + newMember.getName());
            em.persist(newMember);
            memberEventSrc.fire(newMember);
            initNewMember();
        } catch (Exception e) {
            Throwable t = e;
            while ((t.getCause()) != null) {
                t = t.getCause();
            }
            log.info("Exception:" + t.getMessage());
            throw ((Exception) t);
        }

    }

    @PostConstruct
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void initNewMember() {
        newMember = new Member();
        log.info("@PostConstruct:initNewMember called");
    }
}
