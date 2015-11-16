/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2015, Red Hat, Inc., and individual contributors
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

package org.jboss.as.quickstarts.websocket.client;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * Block when disconnected.
 * Do not block when connected.
 * Release blocks upon connection established.
 * Toggle between connected and disconnected.
 * 
 * Based on org.infinispan.util.concurrent.ReclosableLatch
 * 
 * @author <a href="http://monospacesoftware.com">Paul Cowan</a>
 */

public class ConnectionGate {

  private static final class ReclosableLatch extends AbstractQueuedSynchronizer {

    private static final long serialVersionUID = 1;

    // the following states are used in the AQS.
    private static final int OPEN_STATE = 0, CLOSED_STATE = 1;

    public ReclosableLatch() {
      setState(CLOSED_STATE);
    }

    public ReclosableLatch(boolean defaultOpen) {
      setState(defaultOpen ? OPEN_STATE : CLOSED_STATE);
    }

    @Override
    public final int tryAcquireShared(int ignored) {
      // return 1 if we allow the requestor to proceed, -1 if we want the requestor to block.
      return getState() == OPEN_STATE ? 1 : -1;
    }

    @Override
    public final boolean tryReleaseShared(int state) {
      // used as a mechanism to set the state of the Sync.
      setState(state);
      return true;
    }

    public final void open() {
      // do not use setState() directly since this won't notify parked threads.
      releaseShared(OPEN_STATE);
    }

    public final void close() {
      // do not use setState() directly since this won't notify parked threads.
      releaseShared(CLOSED_STATE);
    }

    public boolean isOpened() {
      return getState() == OPEN_STATE;
    }

    public final void await() throws InterruptedException {
      acquireSharedInterruptibly(1); // the 1 is a dummy value that is not used.
    }

    public final boolean await(long time, TimeUnit unit) throws InterruptedException {
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
