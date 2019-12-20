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

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.as.quickstarts.ejb.entity.CallerUser;
import org.jboss.as.quickstarts.ejb.server.RemoteBeanInterface;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * EJB which runs the remote calls to the EJBs.
 * We use the EJB here for benefit of automatic transaction management.
 */
@Stateless
public class RemoteBeanCaller {
    private static final Logger log = Logger.getLogger(RemoteBeanCaller.class);

    @PersistenceContext
    private EntityManager em;

    /**
     * <p>
     * The method calls the remote EJB {@link Stateless} endpoint.
     * </p>
     * <p>
     * To lookup the remote endpoint is used remote outbound connection defined in the <code>standalone.xml</code> configuration file.
     * The deployment links to the configuration by descriptor <code>WEB-INF/jboss-ejb-client.xml</code>.
     * </p>
     *
     * <p>
     * The demonstration shows what happens when several subsequent calls are run while no transaction is started.
     * With one remote server all requests go to that one instance of <code>server2</code>.
     * It makes a difference if there are two (or more) remote servers clustered (<code>server2</code> is run in HA of more instances).
     * <br/>
     * There is defined no transaction context, the remote call should be load-balanced by EJB client
     * and the first call goes to the first remote server and the second call goes to the second remote server.
     * </p>
     *
     * @return list of strings as return values from the remote beans,
     *         in this case the return values are hostnames and the jboss node names of the remote application server
     * @throws NamingException when remote lookup fails
     */
    @TransactionAttribute(value = TransactionAttributeType.NOT_SUPPORTED)
    public List<String> remoteOutboundStatelessNoTxBeanCall() throws NamingException {
        log.debugf("Calling with outbound connection without transaction to StatelessBean.successOnCall()");

        RemoteBeanInterface bean = RemoteLookupHelper.lookupRemoteEJBOutbound("StatelessBean", RemoteBeanInterface.class, false);
        List<String> callResponses = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            callResponses.add(bean.successOnCall());
        }
        return callResponses;
    }

    /**
     * <p>
     * The method calls the remote EJB {@link Stateless} endpoint.
     * </p>
     * <p>
     * To lookup the remote endpoint is used remote outbound connection defined in the <code>standalone.xml</code> configuration file.
     * The deployment links to the configuration by descriptor <code>WEB-INF/jboss-ejb-client.xml</code>.
     * </p>
     *
     * <p>
     * The demonstration shows what happens when two subsequent calls are run within started transaction.<br/>
     * (The default {@link TransactionAttribute} value is {@link TransactionAttributeType#REQUIRED}.)
     * </p>
     * <p>
     * There is no difference with one remote server (started only one instance of <code>server2</code>).
     * It makes a difference if there are two remote servers clustered (<code>server2</code> is run in HA with two instances).
     * <br/>
     * In such case the remote call will reach the same server for both remote ejb invocations.
     * It's because of the transaction affinity which has to be provided by EJB client.
     * </p>
     *
     * @return list of strings as return values from the remote beans,
     *         in this case the return value is a hostname and the jboss node name of the remote application server
     * @throws NamingException when remote lookup fails
     */
    public List<String> remoteOutboundStatelessBeanCall() throws NamingException {
        log.debugf("Calling with outbound connection with transaction to StatelessBean.successOnCall()");

        em.persist(new CallerUser("Bilbo", "Baggins"));

        RemoteBeanInterface bean = RemoteLookupHelper.lookupRemoteEJBOutbound("StatelessBean", RemoteBeanInterface.class, false);
        return Arrays.asList(
            bean.successOnCall(),
            bean.successOnCall()
        );
    }

    /**
     * <p>
     * The method calls the remote EJB {@link Stateless} endpoint.<br/>
     * The invocation runs over EJB remoting where first call is run over HTTP,
     * then the HTTP upgrade requests the change to remoting protocol
     * </p>
     * <p>
     * To lookup the remote endpoint is used direct definition of the hostname, port and credentials
     * here in code. More specifically at time when {@link javax.naming.InitialContext} is defined,
     * see {@link RemoteLookupHelper#lookupRemoteEJBDirect(String, Class, boolean, String, int, String, String, boolean)}.
     * </p>
     * <p>
     * For details on the call processing see {@link #remoteOutboundStatelessBeanCall()}.
     * </p>
     *
     * @return list of strings as return values from the remote beans,
     *         in this case the return values are hostname and the jboss node names of the remote application server
     * @throws NamingException when remote lookup fails
     */
    public List<String> directLookupStatelessBeanOverEjbRemotingCall() throws NamingException {
        log.debugf("Calling direct lookup with transaction to StatelessBean.successOnCall()");

        String remoteHost = System.getProperty("remote.server.host");
        int remotePort = Integer.getInteger("remote.server.port", 0);
        String remoteUsername = System.getProperty("remote.server.username");
        String remotePassword = System.getProperty("remote.server.password");

        RemoteBeanInterface bean = RemoteLookupHelper.lookupRemoteEJBDirect("StatelessBean", RemoteBeanInterface.class, false,
                remoteHost, remotePort, remoteUsername, remotePassword, false);
        return Arrays.asList(
                bean.successOnCall(),
                bean.successOnCall()
        );
    }

    /**
     * This is the same invocation as for {@link #directLookupStatelessBeanOverEjbRemotingCall}.
     * The difference is that there is used the HTTP protocol for EJB calls. Each invocation will be a HTTP request.
     *
     * @return list of strings as return values from the remote beans,
     *         in this case the return values are hostname and the jboss node names of the remote application server
     * @throws NamingException when remote lookup fails
     */
    public List<String> directLookupStatelessBeanOverHttpCall() throws NamingException {
        log.debugf("Calling direct lookup with transaction to StatelessBean.successOnCall()");

        String remoteHost = System.getProperty("remote.server.host");
        int remotePort = Integer.getInteger("remote.server.port", 0);
        String remoteUsername = System.getProperty("remote.server.username");
        String remotePassword = System.getProperty("remote.server.password");

        RemoteBeanInterface bean = RemoteLookupHelper.lookupRemoteEJBDirect("StatelessBean", RemoteBeanInterface.class, false,
                remoteHost, remotePort, remoteUsername, remotePassword, true);
        return Arrays.asList(
                bean.successOnCall(),
                bean.successOnCall()
        );
    }

    /**
     * <p>
     * The method calls the remote EJB {@link javax.ejb.Stateful} endpoint.
     * </p>
     * <p>
     * To lookup the remote endpoint is used remote outbound connection defined in the <code>standalone.xml</code> configuration file.
     * The deployment links to the configuration by descriptor <code>WEB-INF/jboss-ejb-client.xml</code>.
     * </p>
     *
     * <p>
     * This demonstrates what happens on two subsequent calls to the stateful bean when no transaction is started.
     * In fact, for stateful bean the demonstrated behaviour makes no difference if the transaction is or is not started.
     * </p>
     * <p>
     * As there are processed two remote EJB invocations and we talk about stateful bean the EJB client has to ensure
     * that both calls ends at the same remote server.
     * The demonstration does not make much sense when there is started only one instance of the remote server (<code>server2</code>).
     * But in case there are two remote servers started (two instances of <code>server2</code> are run)
     * then the second EJB invocation has to be directed to the same server as the first one.
     * </p>
     *
     * @return list of strings as return values from the remote beans,
     *         in this case the return value are hostnames and the jboss node names of the remote application server
     * @throws NamingException when remote lookup fails
     */
    @TransactionAttribute(value = TransactionAttributeType.NOT_SUPPORTED)
    public List<String> remoteOutboundStatefulNoTxBeanCall() throws NamingException {
        log.debugf("Calling with outbound connection without transaction to StatefulBean.successOnCall()");

        RemoteBeanInterface bean = RemoteLookupHelper.lookupRemoteEJBOutbound("StatefulBean", RemoteBeanInterface.class, true);
        return Arrays.asList(
                bean.successOnCall(),
                bean.successOnCall()
        );
    }

    /**
     * <p>
     * The method calls the remote EJB {@link Stateless} endpoint.
     * </p>
     * <p>
     * To lookup the remote endpoint is used remote outbound connection defined in the <code>standalone.xml</code> configuration file.
     * The deployment links to the configuration by descriptor <code>WEB-INF/jboss-ejb-client.xml</code>.
     * </p>
     *
     * <p>
     * The method demonstrates the need of transaction recovery processing in case of some types of failures.
     * This one simulates an intermittent network failure happening on finishing the remote transaction.
     * </p>
     * <p>
     * The failure is invoked at time when transaction is decided to by committed by the two-phase protocol.
     * The remote call returns success despite the fact the remote server has not closed all the resources with commit.
     * The first attempt to commit fails and it's then the responsibility of recovery manager to finish
     * this transaction with another attempt of committing the remote transaction.
     * </p>
     * <p>
     * The recovery manager runs periodically, by default every 2 minutes. For making the recovery faster
     * we may force the recovery to be processed. This could be done if the recovery listener is enabled
     * (see <code>remote-configuration.cli</code> script). Then it's possible to send command 'SCAN'
     * to socket at port <code>4712</code>). The unfinished transaction is then finished.
     * </p>
     *
     * @return hostname of server as String that the invocation goes to
     * @throws NamingException when remote lookup fails
     */
    public String remoteOutboundStatelessBeanFail() throws NamingException {
        log.debugf("Calling with failure to StatelessBean.failOnCall()");

        em.persist(new CallerUser("Fell", "Rider"));

        RemoteBeanInterface bean = RemoteLookupHelper.lookupRemoteEJBOutbound("StatelessBean", RemoteBeanInterface.class, false);
        return bean.failOnCall();
    }
}
