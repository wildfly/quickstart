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
package org.jboss.as.quickstarts.wsba.participantcompletion.simple;

/**
 * Exception used to communicate a general error in the SetService.
 *
 * @author paul.robinson@redhat.com, 2012-01-04
 */
public class SetServiceException extends Exception {

    /** Default value included to remove warning. **/
    private static final long serialVersionUID = 1L;

    public SetServiceException(String message) {
        super(message);
    }

    public SetServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
