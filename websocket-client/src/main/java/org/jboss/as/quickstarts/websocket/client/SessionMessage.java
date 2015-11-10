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

import static java.lang.String.format;

import java.io.Serializable;

/**
 * @author <a href="http://monospacesoftware.com">Paul Cowan</a>
 */

public class SessionMessage implements Serializable {

  private static final long serialVersionUID = 1L;

  private String sessionId;
  private String text;

  public SessionMessage() {

  }

  public SessionMessage(String sessionId, String text) {
    this.sessionId = sessionId;
    this.text = text;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String toString() {
    return format("%s:%s", sessionId, text);
  }
  
}
