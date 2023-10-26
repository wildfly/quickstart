/*
 * Copyright 2023 JBoss by Red Hat.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.ejb_security_context_propagation;

import org.junit.Test;
import org.wildfly.security.auth.client.AuthenticationConfiguration;
import org.wildfly.security.auth.client.AuthenticationContext;
import org.wildfly.security.auth.client.MatchRule;
import org.wildfly.security.sasl.SaslMechanismSelector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

/**
 * The functional integration testing.
 * @author emartins
 * @author <a href="mailto:sguilhen@redhat.com">Stefan Guilhen</a>
 *
 */
public class SecurityContextPropagationIT {

    @Test
    public void testSecurityContextPropagation() throws NamingException {
        // we assume standard dist, where EJBs are at ejb:/ejb-security-context-propagation/, if no SERVER_HOST or server.host in env/system props
        final boolean standardDist = System.getenv("SERVER_HOST") == null && System.getProperty("server.host") == null;
        System.out.println("standardDist: "+standardDist);
        // invoke the intermediate bean using the identity configured in wildfly-config.xml
        invokeIntermediateBean(standardDist);
        // now lets programmatically setup an authentication context to switch users before invoking the intermediate bean
        AuthenticationConfiguration superUser = AuthenticationConfiguration.empty().setSaslMechanismSelector(SaslMechanismSelector.NONE.addMechanism("PLAIN")).
                useName("quickstartAdmin").usePassword("adminPwd1!");
        final AuthenticationContext authCtx = AuthenticationContext.empty().with(MatchRule.ALL, superUser);
        AuthenticationContext.getContextManager().setThreadDefault(authCtx);
        invokeIntermediateBean(standardDist);
    }

    private static void invokeIntermediateBean(boolean standardDist) throws NamingException {
        final Hashtable<String, String> jndiProperties = new Hashtable<>();
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        jndiProperties.put(Context.PROVIDER_URL, "remote+http://localhost:8080");
        final Context context = new InitialContext(jndiProperties);

        IntermediateEJBRemote intermediate = (IntermediateEJBRemote) context.lookup("ejb:/"+(standardDist?"ejb-security-context-propagation":"ROOT")+"/IntermediateEJB!"
                + IntermediateEJBRemote.class.getName());
        System.out.println("\n\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");
        System.out.println(intermediate.makeRemoteCalls());
        System.out.println("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n\n");
    }

}
