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
package org.jboss.as.quickstarts.batch.job;

import java.util.logging.Logger;

import javax.batch.api.chunk.ItemProcessor;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.as.quickstarts.batch.model.Contact;

//This class will update the contact to the proper case and apply a mask to the phone number
@Named("contactFormatter")
public class ContactsFormatter implements ItemProcessor {

    @Inject
    private Logger log;

    @Override
    public Object processItem(Object item) throws Exception {
        Contact c = (Contact) item;
        // Update the name to use just the first letter as upper case
        String name = c.getName();
        String newName = Character.toUpperCase(name.charAt(0)) + name.substring(1).toLowerCase();
        c.setName(newName);

        // Apply mask to the phone number
        String phone = c.getPhone();
        String newPhone = phone.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1)-$2-$3");
        c.setPhone(newPhone);
        log.info(String.format("Register #%d - Changing name %s -> %s | phone  %s -> %s", c.getId(), name, newName, phone, newPhone));
        return c;
    }

}
