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
package org.jboss.as.quickstarts.cdi.service;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * JSF backing bean that demonstrates CDI injection.
 *
 * Bean name overridden to "itemBean" to be accessible from view with this name.
 *
 * @author Ievgen Shulga
 */
@Named("itemBean")
@RequestScoped
public class ItemBean {
    @Inject
    private ItemServiceBean itemService;
    @Inject
    private FacesContext context;
    private List<Item> items;
    private List<String> itemHistory;
    private String name;
    private static final String EMPTY_STRING = "";

    public void add() {
        if (name == EMPTY_STRING) {
            FacesMessage fm = new FacesMessage("Name can't be empty");
            context.addMessage(fm.getSummary(), fm);
            refresh();
            return;
        }
        Item item = new Item();
        item.setName(name);
        itemService.create(item);
        name = EMPTY_STRING;
        refresh();
    }

    private void refresh() {
        itemHistory = History.getItemHistory();
        items = itemService.getList();
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getItemHistory() {
        return itemHistory;
    }

    public void setItemHistory(List<String> itemHistory) {
        this.itemHistory = itemHistory;
    }

}
