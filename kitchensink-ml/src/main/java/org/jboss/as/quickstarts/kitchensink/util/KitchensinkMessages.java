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
package org.jboss.as.quickstarts.kitchensink.util;

import javax.faces.context.FacesContext;

import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageBundle;
import org.jboss.logging.Messages;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
@MessageBundle(projectCode = "")
public interface KitchensinkMessages {

    KitchensinkMessages MESSAGES = Messages.getBundle(KitchensinkMessages.class, FacesContext.getCurrentInstance()
        .getViewRoot().getLocale());

    @Message("Registered!")
    String registeredMessage();

    @Message("Successfully registered!")
    String registerSuccessfulMessage();

    @Message("Registration failed:")
    String registerFailMessage();

    @Message("Registration failed. See server log for more information.")
    String defaultErrorMessage();
}
