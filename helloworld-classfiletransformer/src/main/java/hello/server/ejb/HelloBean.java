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
package hello.server.ejb;

import java.util.logging.Logger;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 * A simple EJB that exposes a web service <code>view</code> (SOAP/WSDL).<br>
 * <br>
 *
 * @author <a href="mailto:moelholm@gmail.com">Nicky Moelholm</a>
 */
@Stateless
@WebService
@PermitAll
public class HelloBean implements Hello {

    // ------------------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------------------

    private static final Logger logger = Logger.getLogger(HelloBean.class.getName());

    // ------------------------------------------------------------------------
    // Public API
    // ------------------------------------------------------------------------

    public String sayHello(String caller) {

        logger.info(String.format("EJB method invoked from caller %s", caller));

        return String.format("Hello %s", caller);
    }
}
