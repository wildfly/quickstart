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
package org.wildfly.quickstarts.todos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.arquillian.api.ContainerResource;
import org.jboss.as.arquillian.container.ManagementClient;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
@RunAsClient
public class ToDoIT {

    @ContainerResource
    private ManagementClient managementClient;

    @Test
    public void testCRUD() throws Exception {

        WebTarget client = ClientBuilder.newClient().target(managementClient.getWebUri());

        GenericType<List<ToDo>> todosListType = new GenericType<List<ToDo>>() {};
        List<ToDo> allTodos = client.request().get(todosListType);
        assertEquals(0, allTodos.size());

        ToDo toDo = new ToDo();
        toDo.setTitle("My First ToDo");
        toDo.setOrder(1);
        ToDo persistedTodo = client.request().post(Entity.entity(toDo, MediaType.APPLICATION_JSON_TYPE), ToDo.class);
        assertNotNull(persistedTodo.getId());

        allTodos = client.request().get(todosListType);
        assertEquals(1, allTodos.size());
        ToDo fetchedToDo = allTodos.get(0);
        assertEquals(toDo.getTitle(), fetchedToDo.getTitle());

        client.request().delete();

        allTodos = client.request().get(todosListType);
        assertEquals(0, allTodos.size());
    }

}
