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
package org.jboss.as.quickstarts.wsba.participantcompletion.simple.jaxws;

import org.jboss.as.quickstarts.wsba.participantcompletion.simple.AlreadyInSetException;
import org.jboss.as.quickstarts.wsba.participantcompletion.simple.SetServiceException;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Interface implemented by SetServiceBA Web service and Client stub.
 *
 * The Web service represents a simple set collection and this interface provides the basic methods for accessing it.
 *
 * @author paul.robinson@redhat.com, 2011-12-21
 */
@WebService(name = "SetServiceBA", targetNamespace = "http://www.jboss.org/jboss-jdf/jboss-as-quickstart/helloworld/wsba/participantcompletion/set")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface SetServiceBA {
    /**
     * Add a value to the set
     *
     * @param value Value to add to the set.
     * @throws AlreadyInSetException if the item is already in the set.
     * @throws SetServiceException if an error occurred during the adding of the item to the set.
     */
    @WebMethod
    void addValueToSet(String value) throws AlreadyInSetException, SetServiceException;

    /**
     * Query the set to see if it contains a particular value.
     *
     * @param value the value to check for.
     * @return true if the value was present, false otherwise.
     */
    @WebMethod
    boolean isInSet(String value);

    /**
     * Empty the set
     */
    @WebMethod
    void clear();

}
