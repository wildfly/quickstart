/*
 * Copyright The WildFly Authors
 * SPDX-License-Identifier: Apache-2.0
 */
package org.wildfly.quickstarts.todos;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import jakarta.data.repository.Delete;
import jakarta.data.repository.Find;
import jakarta.data.repository.Insert;
import jakarta.data.repository.Repository;
import jakarta.data.repository.Update;

@Repository
public interface ToDoRepository {

    @Find
    List<ToDo> findAll();

    @Find
    Optional<ToDo> find(Long id);

    @Delete
    void remove(ToDo todo);

    @Insert
    void insert(ToDo todo);

    @Update
    ToDo update(ToDo todo);

    /**
     * Patch the {@code ToDo} with the given id, if one exists. "Patch" here is in the
     * general sense of the HTTP PATCH method, where parts of an existing resource are modified
     * based on a provided entity, but others are left as is. Here if the provided {@code patch}
     * {@code ToDo} has a non-null value for a field, this object's field is updated to match,
     * but for null fields this object's current value is retained.
     *
     * @param id the id of the ToDo to update. Cannot be {@code null}
     * @param patch a ToDo whose non-null properties should be persisted if one with the id exists. Cannot be {@code null}
     * @return an {@link Optional} containing the updated ToDo if a ToDo with {@code id} existed
     */
    default Optional<ToDo> patch(Long id, ToDo patch) {
        requireNonNull(id, "Null id");
        requireNonNull(patch, "Null patch");
        Optional<ToDo> optional = find(id);
        if (optional.isPresent()) {
            ToDo existing  = optional.get();
            existing.update(patch);
            optional = Optional.of(update(existing));
        }
        return optional;
    }
}