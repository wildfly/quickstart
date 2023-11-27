/*
 * Copyright 2022 Red Hat, Inc.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wildfly.quickstarts.todos;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Emmanuel Hugonnet (c) 2022 Red Hat, Inc.
 */
public class RemoteToDoIT {

    private static final String REST_TARGET_URL = "http://localhost:8080/todo-backend";

    private String getServerHost() {
        String host = System.getenv("SERVER_HOST");
        if (host == null) {
            host = System.getProperty("server.host");
        }
        return host;
    }

    URL getRequestUrl() {
        String host = getServerHost();
        if (host == null) {
            host = REST_TARGET_URL;
        }
        try {
            return new URI(host).toURL();
        } catch (URISyntaxException | MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Test
    public void testCRUD() throws Exception {
        WebTarget client = ((ResteasyClientBuilder) ClientBuilder.newBuilder()).setFollowRedirects(true).build().target(getRequestUrl().toURI());

        GenericType<List<ToDo>> todosListType = new GenericType<List<ToDo>>() {
        };
        List<ToDo> allTodos = client.request().get(todosListType);
        int initialSize = allTodos.size();

        ToDo toDo = new ToDo();
        toDo.setTitle("My First ToDo");
        toDo.setOrder(1);
        ToDo persistedTodo = client.request().post(Entity.entity(toDo, MediaType.APPLICATION_JSON_TYPE), ToDo.class);
        assertNotNull(persistedTodo.getId());

        allTodos = client.request().get(todosListType);
        assertEquals(initialSize+1, allTodos.size());
        ToDo fetchedToDo = allTodos.get(initialSize);
        assertEquals(toDo.getTitle(), fetchedToDo.getTitle());

        client.request().delete();

        allTodos = client.request().get(todosListType);
        assertEquals(0, allTodos.size());
    }
}
