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
package org.wildfly.quickstarts.todos;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("")
@RequestScoped
public class ToDoController {

    @Inject
    private ToDoDAO todoDAO;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(APPLICATION_JSON)
    public List<ToDo> getAllTodos(){
        return todoDAO.findAll();
    }

    @GET
    @Path("{id}")
    @Produces(APPLICATION_JSON)
    public ToDo getTodoFrom(@PathParam("id") long id) {
        Optional<ToDo> todo = todoDAO.findById(id);
        return todo.orElseThrow(() -> new NotFoundException("ToDo does not exist!"));
    }

    @DELETE
    @Transactional
    public void deleteAllTodos(){
        todoDAO.findAll().forEach(todo -> todoDAO.remove(todo));
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteTodoFrom(@PathParam("id") long id) {
        Optional<ToDo> optional = todoDAO.findById(id);
        ToDo todo = optional.orElseThrow(() -> new NotFoundException("ToDo does not exist!"));
        todoDAO.remove(todo);
        return Response.ok().build();
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Transactional
    public ToDo addTodo(ToDo todo) throws MalformedURLException {
        todo.setUrl(uriInfo.getAbsolutePathBuilder().scheme("https").build().toURL());
        todoDAO.insert(todo);
        return todo;
    }

    @PATCH
    @Path("{id}")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    @Transactional
    public ToDo updateTodo(@PathParam("id") long id, ToDo update) {
        return todoDAO.update(id, update).orElseThrow(() -> new NotFoundException("ToDo does not exist!"));
    }
}