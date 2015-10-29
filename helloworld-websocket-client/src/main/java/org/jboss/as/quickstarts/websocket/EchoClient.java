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

package org.jboss.as.quickstarts.websocket;

import static java.lang.String.format;

import java.io.Serializable;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.*;
import javax.inject.*;
import javax.websocket.*;

/**
 * @author <a href="http://monospacesoftware.com">Paul Cowan</a>
 */

@Named
@ApplicationScoped
@ClientEndpoint(encoders = SessionMessageEncoder.class, decoders = SessionMessageDecoder.class)
public class EchoClient implements Serializable {

  private static final long serialVersionUID = 1L;

  private Logger log = Logger.getLogger(EchoClient.class.getName());

  @Inject
  @Frontend
  private Event<SessionMessage> frontendEvent;

  private String echoServerUrl = "ws://echo.websocket.org";

  private Session session;

  private static AtomicInteger idCounter = new AtomicInteger();
  private int id;

  private boolean connecting = false;
  private Object connectingLock = new Object();
  
  public EchoClient() {
    id = idCounter.incrementAndGet();
    log.info(format("%s new EchoClient", id));
  }
  
  // CountdownLatch work well for this but can not be reset
  // Use a Phaser?
  private void waitForConnect() {
    synchronized(connectingLock) {
      if (connecting) {
        try {
          connectingLock.wait();
        } catch (InterruptedException e) {
        }
      }
    }
  }

  @PostConstruct
  public void init() {
    synchronized(connectingLock) {
      connecting = true;
      try {
        log.info(format("%s connecting to backend %s", id, echoServerUrl));
        ContainerProvider.getWebSocketContainer().connectToServer(this, URI.create(echoServerUrl));
      } catch (Exception e) {
        log.log(Level.SEVERE, format("%s failed to connect to backend %s: %s", id, echoServerUrl, e.getMessage()), e);
      }
    }
  }

  @OnOpen
  public void onOpen(Session session) {
    synchronized(connectingLock) {
      log.info(format("%s opened backend session %s", id, session.getId()));
      this.session = session;
      connecting = false;
      connectingLock.notifyAll();
    }
  }
  
  @OnClose
  public void onClose(Session session) {
    log.info(format("%s closed backend session %s", id, session.getId()));
    init(); // reconnect
  }

  @OnError
  public void onError(Throwable t, Session session) {
    log.log(Level.WARNING, format("%s error from backend session %s", id, session.getId()), t);
    // reconnect?
  }

  public void sendMessage(@Observes @Backend SessionMessage message) throws Exception {
    waitForConnect();
    if (session == null) {
      log.warning(format("%s failed to send frontend session %s message, not connected to backend", id, message.getSessionId()));
      return;
    }
    log.info(format("%s send message from frontend session %s to backend session %s", id, message.getSessionId(), session.getId()));
    session.getBasicRemote().sendObject(message);
  }

  @OnMessage
  public void receiveMessage(SessionMessage message) {
    log.info(format("%s receive message for frontend session %s from backend session %s", id, message.getSessionId(), session.getId()));
    frontendEvent.fire(message);
  }

}
