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

package org.jboss.as.quickstarts.ejb.server;


import java.net.InetAddress;
import java.net.UnknownHostException;

import org.jboss.logging.Logger;

import javax.naming.InitialContext;
import jakarta.transaction.Status;
import jakarta.transaction.TransactionManager;

/**
 * Utility class with generic conversions and info getters.
 */
public final class InfoUtils {
    private static final Logger log = Logger.getLogger(InfoUtils.class);

    private InfoUtils() throws IllegalAccessException {
        throw new IllegalAccessException("utility class, do not instantiate");
    }

    /**
     * To get information about hostname and jboss node name.
     *
     * @return string with information about hostname and jboss node name
     */
    public static String getHostInfo() {
        String currentIpHost = "<<unknown hostname>>";
        try {
            InetAddress ip = InetAddress.getLocalHost();
            currentIpHost = ip.toString();
        } catch (UnknownHostException e) {
            log.warnf("Cannot get current IP and hostname. Cause: %s", e);
        }
        String jbossNodeName = System.getProperty("jboss.node.name");
        return String.format("host: %s, jboss node name: %s", currentIpHost, jbossNodeName);
    }

    /**
     * Converting transaction status code as int to string representation,
     * see {@link jakarta.transaction.Status}.
     *
     * @param status  status code as integer
     * @return string representation of the transaction status code
     */
    public static String transactionStatusAsString(int status) {
        switch (status) {
            case jakarta.transaction.Status.STATUS_ACTIVE:
                return "jakarta.transaction.Status.STATUS_ACTIVE";
            case jakarta.transaction.Status.STATUS_COMMITTED:
                return "jakarta.transaction.Status.STATUS_COMMITTED";
            case jakarta.transaction.Status.STATUS_MARKED_ROLLBACK:
                return "jakarta.transaction.Status.STATUS_MARKED_ROLLBACK";
            case jakarta.transaction.Status.STATUS_NO_TRANSACTION:
                return "jakarta.transaction.Status.STATUS_NO_TRANSACTION";
            case jakarta.transaction.Status.STATUS_PREPARED:
                return "jakarta.transaction.Status.STATUS_PREPARED";
            case jakarta.transaction.Status.STATUS_PREPARING:
                return "jakarta.transaction.Status.STATUS_PREPARING";
            case jakarta.transaction.Status.STATUS_ROLLEDBACK:
                return "jakarta.transaction.Status.STATUS_ROLLEDBACK";
            case jakarta.transaction.Status.STATUS_ROLLING_BACK:
                return "jakarta.transaction.Status.STATUS_ROLLING_BACK";
            case jakarta.transaction.Status.STATUS_UNKNOWN:
            default:
                return "jakarta.transaction.Status.STATUS_UNKNOWN";
        }
    }

    public static String getTransactionStatus() {
        int statusCode = Status.STATUS_UNKNOWN;

        try {
            InitialContext ctx = new InitialContext();
            TransactionManager tm = (TransactionManager) ctx.lookup("java:/TransactionManager");
            statusCode = tm.getStatus();
        } catch (Exception e) {
            log.warnf(e, "Cannot get transaction manager at JNDI binding 'java:/TransactionManager'");
            return "error to obtain transaction manager";
        }

        return transactionStatusAsString(statusCode);
    }
}
