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

import jakarta.ejb.Local;

@Local
public interface ToDoDAO {

    List<ToDo> findAll();

    Optional<ToDo> findById(Long id);

    void remove(ToDo todo);

    void insert(ToDo todo);

    /**
     * Patch the {@code ToDo} with the given id, if one exists. "Patch" here is in the
     * general sense of the HTTP PATCH method, where parts of an existing resource are modified
     * based on a provided entity, but others are left as is. Here if the provided {@code patch}
     * {@code ToDo} has a non-null value for a field, this object's field is updated to match,
     * but for null fields this object's current value is retained.
     *
     * @param id the id of the ToDo to update. Cannot be {@code null}
     * @param patch a ToDo to persist if one with the id exists. Cannot be {@code null}
     * @return an {@link Optional} containing the updated ToDo if a ToDo with {@code id} existed
     */
    Optional<ToDo> patch(Long id, ToDo patch);
}