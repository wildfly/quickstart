/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2020, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.wildfly.quickstarts.microprofile.reactive.messaging.test;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FAILED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OUTCOME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_ATTRIBUTE_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RESULT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUCCESS;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import org.jboss.as.arquillian.api.ServerSetupTask;
import org.jboss.as.arquillian.container.ManagementClient;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;
import org.junit.Assert;
import org.xnio.IoUtils;

/**
 * @author <a href="mailto:kabir.khan@jboss.com">Kabir Khan</a>
 */
public class EnableReactiveExtensionsSetupTask implements ServerSetupTask {
    private static final String MODULE_REACTIVE_MESSAGING = "org.wildfly.extension.microprofile.reactive-messaging-smallrye";
    private static final String MODULE_REACTIVE_STREAMS_OPERATORS = "org.wildfly.extension.microprofile.reactive-streams-operators-smallrye";
    private static final String SUBSYSTEM_REACTIVE_MESSAGING = "microprofile-reactive-messaging-smallrye";
    private static final String SUBSYSTEM_REACTIVE_STREAMS_OPERATORS = "microprofile-reactive-streams-operators-smallrye";
    public static final int TIMEOUT = 30000;
    public static final String EXTENSION = "extension";
    public static final String SUBSYSTEM = "subsystem";

    List<ModelNode> removeOps = new ArrayList<>();

    public EnableReactiveExtensionsSetupTask() {
    }

    @Override
    public void setup(ManagementClient managementClient, String containerId) throws Exception {
        boolean rsoExt = !containsChild(managementClient, EXTENSION, MODULE_REACTIVE_STREAMS_OPERATORS);
        boolean rsoSs = !containsChild(managementClient, SUBSYSTEM, SUBSYSTEM_REACTIVE_STREAMS_OPERATORS);
        boolean rmExt = !containsChild(managementClient, EXTENSION, MODULE_REACTIVE_MESSAGING);
        boolean rmSs = !containsChild(managementClient, SUBSYSTEM, SUBSYSTEM_REACTIVE_MESSAGING);

        if (rsoExt) {
            addExtension(managementClient, MODULE_REACTIVE_STREAMS_OPERATORS);
            removeOps.add(createRemoveExtension(MODULE_REACTIVE_STREAMS_OPERATORS));
        }
        if (rmExt) {
            addExtension(managementClient, MODULE_REACTIVE_MESSAGING);
            removeOps.add(createRemoveExtension(MODULE_REACTIVE_MESSAGING));
        }
        if (rsoSs) {
            addSubsystem(managementClient, SUBSYSTEM_REACTIVE_STREAMS_OPERATORS);
            removeOps.add(createRemoveSubsystem(SUBSYSTEM_REACTIVE_STREAMS_OPERATORS));
        }
        if (rmSs) {
            addSubsystem(managementClient, SUBSYSTEM_REACTIVE_MESSAGING);
            removeOps.add(createRemoveSubsystem(SUBSYSTEM_REACTIVE_MESSAGING));
        }

        reloadIfRequired(managementClient);
    }

    @Override
    public void tearDown(ManagementClient managementClient, String s) throws Exception {
        for (ListIterator<ModelNode> it = removeOps.listIterator(removeOps.size()); it.hasPrevious(); ) {
            ModelNode op = it.previous();
            executeOperation(managementClient, op);
        }

        reloadIfRequired(managementClient);
    }

    private boolean containsChild(ManagementClient managementClient, String childType, String childName) throws Exception {
        ModelNode op = new ModelNode();
        op.get("operation").set("read-children-names");
        op.get("child-type").set(childType);
        ModelNode result = executeOperation(managementClient, op);
        List<ModelNode> names = result.asList();
        for (ModelNode name : names) {
            if (name.asString().equals(childName)) {
                return true;
            }
        }
        return false;
    }

    private void addExtension(ManagementClient managementClient, String name) throws Exception {
        ModelNode op = createOperation(PathAddress.pathAddress(EXTENSION, name), "add");
        executeOperation(managementClient, op);
    }

    private void addSubsystem(ManagementClient managementClient, String name) throws Exception {
        ModelNode op = createOperation(PathAddress.pathAddress(SUBSYSTEM, name), "add");
        executeOperation(managementClient, op);
    }

    private ModelNode createRemoveExtension(String name) throws Exception {
        return createOperation(PathAddress.pathAddress("extension", name), "remove");
    }

    private ModelNode createRemoveSubsystem(String name) throws Exception {
        return createOperation(PathAddress.pathAddress("subsystem", name), "remove");
    }

    private ModelNode createOperation(PathAddress address, String operationName) throws Exception {
        ModelNode op = new ModelNode();
        op.get("address").set(address.toModelNode());
        op.get("operation").set(operationName);
        return op;
    }

    private ModelNode executeOperation(ManagementClient managementClient, ModelNode op) throws Exception{
        ModelNode result = managementClient.getControllerClient().execute(op);
        if (!result.get("outcome").asString().equals("success")) {
            throw new IllegalStateException(result.asString());
        }
        return result.get("result");
    }

    private void reloadIfRequired(final ManagementClient managementClient) throws Exception {
        String runningState = getContainerRunningState(managementClient);
        if ("reload-required".equalsIgnoreCase(runningState)) {
            executeReloadAndWaitForCompletion(managementClient);
        } else {
            Assert.assertEquals("Server state 'running' is expected", "running", runningState);
        }
    }

    private String getContainerRunningState(ManagementClient managementClient) throws IOException {
        return getContainerRunningState(managementClient.getControllerClient());
    }

    /**
     * Gets the current value of the server root resource's {@code server-state} attribute.
     * @param modelControllerClient client to use to read the state
     * @return the server state. Will not be {@code null}.
     * @throws IOException if there is an IO problem reading the state
     */
    private String getContainerRunningState(ModelControllerClient modelControllerClient) throws IOException {
        ModelNode operation = new ModelNode();
        operation.get(OP_ADDR).setEmptyList();
        operation.get(OP).set(READ_ATTRIBUTE_OPERATION);
        operation.get(NAME).set("server-state");
        ModelNode rsp = modelControllerClient.execute(operation);
        return SUCCESS.equals(rsp.get(OUTCOME).asString()) ? rsp.get(RESULT).asString() : FAILED;
    }

    private void executeReloadAndWaitForCompletion(ManagementClient managementClient) {
        executeReload(managementClient.getControllerClient(), false, null);
        waitForLiveServerToReload(TIMEOUT, managementClient.getMgmtAddress(), managementClient.getMgmtPort());
    }

    private void executeReload(ModelControllerClient client, boolean adminOnly, String serverConfig) {
        ModelNode operation = new ModelNode();
        operation.get(OP_ADDR).setEmptyList();
        operation.get(OP).set("reload");
        operation.get("admin-only").set(adminOnly);
        if(serverConfig != null) {
            operation.get("server-config").set(serverConfig);
        }
        try {
            ModelNode result = client.execute(operation);
            if (!"success".equals(result.get(ClientConstants.OUTCOME).asString())) {
                fail("Reload operation didn't finish successfully: " + result.asString());
            }
        } catch(IOException e) {
            final Throwable cause = e.getCause();
            if (!(cause instanceof ExecutionException) && !(cause instanceof CancellationException)) {
                throw new RuntimeException(e);
            } // else ignore, this might happen if the channel gets closed before we got the response
        }
    }

    private void waitForLiveServerToReload(int timeout, String serverAddress, int serverPort) {
        int adjustedTimeout = timeout;
        long start = System.currentTimeMillis();
        ModelNode operation = new ModelNode();
        operation.get(OP_ADDR).setEmptyList();
        operation.get(OP).set(READ_ATTRIBUTE_OPERATION);
        operation.get(NAME).set("server-state");
        while (System.currentTimeMillis() - start < adjustedTimeout) {
            //do the sleep before we check, as the attribute state may not change instantly
            //also reload generally takes longer than 100ms anyway
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            try {
                ModelControllerClient liveClient = ModelControllerClient.Factory.create(
                        serverAddress, serverPort);
                try {
                    ModelNode result = liveClient.execute(operation);
                    if ("running" .equals(result.get(RESULT).asString())) {
                        return;
                    }
                } catch (IOException e) {
                    // ignore
                } finally {
                    IoUtils.safeClose(liveClient);
                }
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }
        fail("Live Server did not reload in the imparted time of " +
                adjustedTimeout + "(" + timeout + ") milliseconds");
    }


}
