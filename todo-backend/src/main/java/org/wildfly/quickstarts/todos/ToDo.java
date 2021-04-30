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

import static java.util.Optional.ofNullable;
import static javax.persistence.GenerationType.IDENTITY;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.ws.rs.core.UriBuilder;

@Entity
public class ToDo {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String title;
    private Boolean completed = false;
    @Column(name = "\"order\"")
    private int order;
    private URL url;

    public ToDo() {

    }

    public ToDo(String title) {
        this.title = title;
    }

    public ToDo(Long id, String title, Boolean completed, int order, URL url) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.order = order;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Boolean getCompleted(){
        return this.completed == null ? false : this.completed;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public URL getUrl() throws MalformedURLException, URISyntaxException {
        if (this.id != null) {
            return UriBuilder.fromUri(url.toURI()).scheme(url.getProtocol()).path(id.toString()).build().toURL();
        }
        return this.url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public void update(ToDo newTodo) {
        this.title = ofNullable(newTodo.title).orElse(title);
        this.completed = ofNullable(newTodo.completed).orElse(completed);
        this.order = ofNullable(newTodo.order).orElse(order);
        this.url = ofNullable(newTodo.url).orElse(url);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToDo toDo = (ToDo) o;
        return id == toDo.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ToDo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", completed=" + completed +
                ", order=" + order +
                ", url=" + url +
                '}';
    }
}
