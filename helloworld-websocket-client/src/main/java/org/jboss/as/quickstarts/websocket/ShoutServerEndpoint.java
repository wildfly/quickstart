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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.*;

import javax.enterprise.context.*;
import javax.enterprise.event.*;
import javax.inject.*;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * @author <a href="http://jmesnil.net/">Jeff Mesnil</a> (c) 2014 Red Hat inc.
 * @author <a href="http://monospacesoftware.com">Paul Cowan</a>
 */

// javax.websocket does not play well with CDI; a new object is created for each new WebSocket regardless of bean scope
@ApplicationScoped
@ServerEndpoint("/shout") 
public class ShoutServerEndpoint implements Serializable {
  
  private static final long serialVersionUID = 1L;

  private Logger log = Logger.getLogger(ShoutServerEndpoint.class.getName());

  @Inject
  @Backend
  private Event<SessionMessage> backendEvent;

  @Inject // SessionManager can not be @SessionScoped because asynch responses from the backend websocket do not have the session
  private SessionManager sessionManager;
  
  private static AtomicInteger idCounter = new AtomicInteger();
  private int id;
  
  public ShoutServerEndpoint() {
    id = idCounter.incrementAndGet();
    log.info(format("%s new ShoutServerEndpoint", id));
  }

  @OnOpen
  public void onOpen(Session session) {
    log.info(format("%s opened frontend session %s", id, session.getId()));
    sessionManager.put(session);
  }

  @OnClose
  public void onClose(Session session) {
    log.info(format("%s closed frontend session %s", id, session.getId()));
    sessionManager.close(session.getId());
  }
  
  @OnError
  public void onError(Throwable t, Session session) {
    log.log(Level.WARNING, format("%s error from frontend session %s", id, session.getId()), t);
  }

  @OnMessage
  public void receiveMessage(String text, Session session) {
    SessionMessage message = new SessionMessage(session.getId(), text);
    log.info(format("%s receive from frontend session %s", id, session.getId()));
    backendEvent.fire(message);
  }

  public void sendMessage(@Observes @Frontend SessionMessage message) {
    Session session = sessionManager.get(message.getSessionId());
    if (session == null) {
      log.warning(format("%s frontend session %s not found", id, message.getSessionId()));
      return;
    }
    log.info(format("%s send to frontend session %s", id, session.getId()));
    session.getAsyncRemote().sendText(message.getText());
  }
}
