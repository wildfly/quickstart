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


import java.util.List;
import java.util.Optional;

import jakarta.ejb.Stateful;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Stateful
public class ToDoDAOImpl implements ToDoDAO {

    @Inject
    private EntityManager em;

    @Override
    public List<ToDo> findAll() {
        TypedQuery<ToDo> query = em.createQuery("SELECT t FROM ToDo t", ToDo.class);
        return query.getResultList();
    }

    @Override
    public Optional<ToDo> findById(Long id) {
        ToDo toDo = em.find(ToDo.class, id);
        return Optional.ofNullable(toDo);
    }

    @Override
    public void remove(ToDo todo) {
        em.remove(todo);
    }

    @Override
    public void insert(ToDo todo) {
        em.persist(todo);
    }

    @Override
    public Optional<ToDo> update(Long id, ToDo newTodo) {
        Optional<ToDo> optional = findById(id);
        if (optional.isPresent()) {
            optional.get().update(newTodo);
            return optional;
        }
        return Optional.empty();
    }
}
