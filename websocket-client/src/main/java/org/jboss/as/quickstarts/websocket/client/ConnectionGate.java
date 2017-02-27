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
package org.jboss.as.quickstarts.websocket.client;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * Block when disconnected. Do not block when connected. Release blocks upon connection established. Toggle between connected
 * and disconnected.
 * <p>
 * Based on org.infinispan.util.concurrent.ReclosableLatch
 *
 * @author <a href="http://monospacesoftware.com">Paul Cowan</a>
 */

public class ConnectionGate {

    private static final class ReclosableLatch extends AbstractQueuedSynchronizer {

        private static final long serialVersionUID = 1;

        // the following states are used in the AQS.
        private static final int OPEN_STATE = 0, CLOSED_STATE = 1;

        ReclosableLatch() {
            setState(CLOSED_STATE);
        }

        ReclosableLatch(boolean defaultOpen) {
            setState(defaultOpen ? OPEN_STATE : CLOSED_STATE);
        }

        @Override
        public int tryAcquireShared(int ignored) {
            // return 1 if we allow the requestor to proceed, -1 if we want the requestor to block.
            return getState() == OPEN_STATE ? 1 : -1;
        }

        @Override
        public boolean tryReleaseShared(int state) {
            // used as a mechanism to set the state of the Sync.
            setState(state);
            return true;
        }

        public void open() {
            // do not use setState() directly since this won't notify parked threads.
            releaseShared(OPEN_STATE);
        }

        public void close() {
            // do not use setState() directly since this won't notify parked threads.
            releaseShared(CLOSED_STATE);
        }

        public boolean isOpened() {
            return getState() == OPEN_STATE;
        }

        public void await() throws InterruptedException {
            acquireSharedInterruptibly(1); // the 1 is a dummy value that is not used.
        }

        public boolean await(long time, TimeUnit unit) throws InterruptedException {
            return tryAcquireSharedNanos(1, unit.toNanos(time)); // the 1 is a dummy value that is not used.
        }

        @Override
        public String toString() {
            int s = getState();
            String q = hasQueuedThreads() ? "non" : "";
            return "ReclosableLatch [State = " + s + ", " + q + "empty queue]";
        }
    }

    private final ReclosableLatch latch;

    public ConnectionGate() {
        latch = new ReclosableLatch();
    }

    public ConnectionGate(boolean initiallyConnected) {
        latch = new ReclosableLatch(initiallyConnected);
    }

    public void connected() {
        latch.open();
    }

    public void disconnected() {
        latch.close();
    }

    public void waitForConnection() throws InterruptedException {
        latch.await();
    }

    public boolean waitForConnection(long time, TimeUnit unit) throws InterruptedException {
        return latch.await(time, unit);
    }

    public boolean isConnected() {
        return latch.isOpened();
    }
}
