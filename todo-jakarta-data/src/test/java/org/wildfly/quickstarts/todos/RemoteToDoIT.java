/*
 * Copyright The WildFly Authors
 * SPDX-License-Identifier: Apache-2.0
 */
package org.wildfly.quickstarts.todos;

import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.client.Client;
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

import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 *
 * @author Emmanuel Hugonnet (c) 2022 Red Hat, Inc.
 */
public class RemoteToDoIT {

    private static final String REST_TARGET_URL = "http://localhost:8080";

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
            return new URI(host+"/todo-jakarta-data").toURL();
        } catch (URISyntaxException | MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    URL getRequestUrl(Long id) {
        String host = getServerHost();
        if (host == null) {
            host = REST_TARGET_URL;
        }
        try {
            return new URI(host+"/todo-jakarta-data/" + id).toURL();
        } catch (URISyntaxException | MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Test
    public void testCRUD() throws Exception {
        try (Client client = ((ResteasyClientBuilder) ClientBuilder.newBuilder()).setFollowRedirects(true).build()) {
            WebTarget rootTarget = client.target(getRequestUrl().toURI());

            GenericType<List<ToDo>> todosListType = new GenericType<>() {
            };
            List<ToDo> allTodos = rootTarget.request().get(todosListType);
            int initialSize = allTodos.size();

            ToDo toDo = new ToDo();
            toDo.setTitle("My First ToDo");
            toDo.setOrder(1);
            ToDo persistedTodo = rootTarget.request().post(Entity.entity(toDo, MediaType.APPLICATION_JSON_TYPE), ToDo.class);
            Long id = persistedTodo.getId();
            assertNotNull(id);

            allTodos = rootTarget.request().get(todosListType);
            assertEquals(initialSize + 1, allTodos.size());
            ToDo fetchedToDo = allTodos.get(initialSize);
            assertEquals(toDo.getTitle(), fetchedToDo.getTitle());

            try (Client idClient = ((ResteasyClientBuilder) ClientBuilder.newBuilder()).setFollowRedirects(true).build()) {
                WebTarget idTarget = idClient.target(getRequestUrl(id).toURI());
                toDo.setTitle(null); // to demonstrate that null fields in the update ToDo result in the existing value being kept
                toDo.setCompleted(true);
                try (Response response = idTarget.request().build(HttpMethod.PATCH, Entity.entity(toDo, MediaType.APPLICATION_JSON_TYPE)).invoke()) {
                    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
                    ToDo updated = response.readEntity(ToDo.class);
                    assertEquals(1, updated.getOrder());
                    assertEquals("My First ToDo", updated.getTitle());
                    assertTrue(updated.isCompleted());
                }
                persistedTodo = idTarget.request().get(ToDo.class);
                assertEquals(1, persistedTodo.getOrder());
                assertEquals("My First ToDo", persistedTodo.getTitle());
                assertTrue(persistedTodo.isCompleted());

                // Delete a single ToDo, the one with the id we added
                try (Response response = idTarget.request().delete()) {
                    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
                }
            }

            allTodos = rootTarget.request().get(todosListType);
            assertEquals(initialSize, allTodos.size());

            toDo = new ToDo();
            toDo.setTitle("My Second ToDo");
            toDo.setOrder(2);
            persistedTodo = rootTarget.request().post(Entity.entity(toDo, MediaType.APPLICATION_JSON_TYPE), ToDo.class);
            id = persistedTodo.getId();
            assertNotNull(id);

            allTodos = rootTarget.request().get(todosListType);
            assertEquals(initialSize + 1, allTodos.size());
            fetchedToDo = allTodos.get(initialSize);
            assertEquals(toDo.getTitle(), fetchedToDo.getTitle());

            // Delete all ToDos
            try (Response response = rootTarget.request().delete()) {
                assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
            }

            allTodos = rootTarget.request().get(todosListType);
            assertEquals(0, allTodos.size());
        }
    }
}
