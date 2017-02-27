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
package org.jboss.as.quickstarts.ejb_security_plus;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Collection;
import java.util.Properties;

import javax.security.auth.Subject;

import org.jboss.as.security.api.ConnectionSecurityContext;
import org.jboss.as.security.api.ContextStateCache;
import org.jboss.security.SecurityContext;
import org.jboss.security.SecurityContextAssociation;
import org.jboss.security.SecurityContextFactory;

/**
 * Security actions for this package only.
 *
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
final class SecurityActions {

    private SecurityActions() {
    }

    /*
     * ConnectionSecurityContext Actions
     */

    static Collection<Principal> getConnectionPrincipals() {
        return connectionSecurityContextActions().getConnectionPrincipals();
    }

    static ContextStateCache pushIdentity(final Principal principal, final Object credential) throws Exception {
        return connectionSecurityContextActions().pushIdentity(principal, credential);
    }

    static void popIdentity(final ContextStateCache stateCache) {
        connectionSecurityContextActions().popIdentity(stateCache);
    }

    private static ConnectionSecurityContextActions connectionSecurityContextActions() {
        return System.getSecurityManager() == null ? ConnectionSecurityContextActions.NON_PRIVILEGED : ConnectionSecurityContextActions.PRIVILEGED;
    }

    private interface ConnectionSecurityContextActions {

        Collection<Principal> getConnectionPrincipals();

        ContextStateCache pushIdentity(Principal principal, Object credential) throws Exception;

        void popIdentity(ContextStateCache stateCache);

        ConnectionSecurityContextActions NON_PRIVILEGED = new ConnectionSecurityContextActions() {

            public Collection<Principal> getConnectionPrincipals() {
                return ConnectionSecurityContext.getConnectionPrincipals();
            }

            @Override
            public ContextStateCache pushIdentity(Principal principal, Object credential) throws Exception {
                return ConnectionSecurityContext.pushIdentity(principal, credential);
            }

            @Override
            public void popIdentity(ContextStateCache stateCache) {
                ConnectionSecurityContext.popIdentity(stateCache);
            }

        };

        ConnectionSecurityContextActions PRIVILEGED = new ConnectionSecurityContextActions() {

            PrivilegedAction<Collection<Principal>> GET_CONNECTION_PRINCIPALS_ACTION = new PrivilegedAction<Collection<Principal>>() {

                @Override
                public Collection<Principal> run() {
                    return NON_PRIVILEGED.getConnectionPrincipals();
                }
            };

            public Collection<Principal> getConnectionPrincipals() {
                return AccessController.doPrivileged(GET_CONNECTION_PRINCIPALS_ACTION);
            }

            @Override
            public ContextStateCache pushIdentity(Principal principal, Object credential) throws Exception {
                try {
                    return AccessController.doPrivileged(new PrivilegedExceptionAction<ContextStateCache>() {

                        @Override
                        public ContextStateCache run() throws Exception {
                            return NON_PRIVILEGED.pushIdentity(principal, credential);
                        }
                    });
                } catch (PrivilegedActionException e) {
                    throw e.getException();
                }
            }

            @Override
            public void popIdentity(ContextStateCache stateCache) {
                AccessController.doPrivileged(new PrivilegedAction<Void>() {

                    @Override
                    public Void run() {
                        NON_PRIVILEGED.popIdentity(stateCache);
                        return null;
                    }
                });

            }

        };

    }

    /*
     * SecurityContext Actions
     */

    static SecurityContext securityContextSetPrincipalCredential(Principal principal, Object credential)
        throws Exception {
        return securityContextActions().setPrincipalCredential(principal, credential);
    }

    static Principal securityContextGetPrincipal() {
        return securityContextActions().getPrincipal();
    }

    static Object securityContextGetCredential() {
        return securityContextActions().getCredential();
    }

    private static SecurityContextActions securityContextActions() {
        return System.getSecurityManager() == null ? SecurityContextActions.NON_PRIVILEGED : SecurityContextActions.PRIVILEGED;
    }

    private interface SecurityContextActions {

        SecurityContext setPrincipalCredential(Principal principal, Object credential) throws Exception;

        Principal getPrincipal();

        Object getCredential();

        void set(SecurityContext securityContext);

        SecurityContextActions NON_PRIVILEGED = new SecurityContextActions() {

            @Override
            public SecurityContext setPrincipalCredential(Principal principal, Object credential) throws Exception {
                SecurityContext current = SecurityContextAssociation.getSecurityContext();

                SecurityContext nextContext = SecurityContextFactory.createSecurityContext(principal, credential,
                    new Subject(), "USER_DELEGATION");
                SecurityContextAssociation.setSecurityContext(nextContext);

                return current;

            }

            @Override
            public Principal getPrincipal() {
                return SecurityContextAssociation.getPrincipal();
            }

            @Override
            public Object getCredential() {
                return SecurityContextAssociation.getCredential();
            }

            @Override
            public void set(SecurityContext securityContext) {
                SecurityContextAssociation.setSecurityContext(securityContext);
            }

        };

        SecurityContextActions PRIVILEGED = new SecurityContextActions() {

            PrivilegedAction<Principal> GET_PRINCIPAL_ACTION = new PrivilegedAction<Principal>() {

                @Override
                public Principal run() {
                    return NON_PRIVILEGED.getPrincipal();
                }
            };

            PrivilegedAction<Object> GET_CREDENTIAL_ACTION = new PrivilegedAction<Object>() {

                @Override
                public Object run() {
                    return NON_PRIVILEGED.getCredential();
                }
            };

            @Override
            public SecurityContext setPrincipalCredential(Principal principal, Object credential) throws Exception {
                try {
                    return AccessController.doPrivileged(new PrivilegedExceptionAction<SecurityContext>() {

                        @Override
                        public SecurityContext run() throws Exception {
                            return NON_PRIVILEGED.setPrincipalCredential(principal, credential);
                        }
                    });
                } catch (PrivilegedActionException e) {
                    throw e.getException();
                }

            }

            @Override
            public Principal getPrincipal() {
                return AccessController.doPrivileged(GET_PRINCIPAL_ACTION);
            }

            @Override
            public Object getCredential() {
                return AccessController.doPrivileged(GET_CREDENTIAL_ACTION);
            }

            @Override
            public void set(SecurityContext securityContext) {
                AccessController.doPrivileged(new PrivilegedAction<Void>() {

                    @Override
                    public Void run() {
                        NON_PRIVILEGED.set(securityContext);
                        return null;
                    }
                });
            }

        };

    }

    /*
     * ClassLoader Actions
     */

    static ClassLoader getContextClassLoader() {
        return (System.getSecurityManager() == null ? ContextClassLoaderAction.NON_PRIVILEGED
            : ContextClassLoaderAction.PRIVILEGED).getContextClassLoader();
    }

    private interface ContextClassLoaderAction {

        ClassLoader getContextClassLoader();

        ContextClassLoaderAction NON_PRIVILEGED = new ContextClassLoaderAction() {

            public ClassLoader getContextClassLoader() {
                return Thread.currentThread().getContextClassLoader();
            }
        };

        ContextClassLoaderAction PRIVILEGED = new ContextClassLoaderAction() {

            private PrivilegedAction<ClassLoader> GET_CONTEXT_CLASS_LOADER = new PrivilegedAction<ClassLoader>() {

                public ClassLoader run() {
                    return NON_PRIVILEGED.getContextClassLoader();
                }
            };

            public ClassLoader getContextClassLoader() {
                return AccessController.doPrivileged(GET_CONTEXT_CLASS_LOADER);
            }
        };
    }

    static Properties loadProperties(String name) throws IOException {
        return propertiesAction().loadProperties(name);
    }

    static PropertiesAction propertiesAction() {
        return System.getSecurityManager() == null ? PropertiesAction.NON_PRIVILEGED : PropertiesAction.PRIVILEGED;
    }

    private interface PropertiesAction {

        Properties loadProperties(String name) throws IOException;

        PropertiesAction NON_PRIVILEGED = new PropertiesAction() {

            @Override
            public Properties loadProperties(final String name) throws IOException {
                ClassLoader classLoader = SecurityActions.getContextClassLoader();
                URL url = classLoader.getResource(name);
                try (InputStream is = url.openStream()) {
                    Properties props = new Properties();
                    props.load(is);
                    return props;

                }
            }
        };

        PropertiesAction PRIVILEGED = new PropertiesAction() {

            @Override
            public Properties loadProperties(final String name) throws IOException {
                try {
                    return AccessController.doPrivileged(new PrivilegedExceptionAction<Properties>() {

                        @Override
                        public Properties run() throws IOException {
                            return NON_PRIVILEGED.loadProperties(name);
                        }
                    });
                } catch (PrivilegedActionException e) {
                    throw (IOException) e.getException();
                }
            }
        };
    }
}
