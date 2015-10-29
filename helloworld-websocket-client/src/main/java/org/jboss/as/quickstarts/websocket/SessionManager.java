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
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import javax.enterprise.context.*;
import javax.enterprise.event.*;
import javax.inject.*;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * @author <a href="http://monospacesoftware.com">Paul Cowan</a>
 */

@Named
@ApplicationScoped
public class SessionManager implements Serializable {

  private static final long serialVersionUID = 1L;

  private ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();

  public void put(Session session) {
    sessions.put(session.getId(), session);
  }
  
  public Session get(String id) {
    return sessions.get(id);
  }
  
  public void close(String id) {
    sessions.remove(id);
  }

}
