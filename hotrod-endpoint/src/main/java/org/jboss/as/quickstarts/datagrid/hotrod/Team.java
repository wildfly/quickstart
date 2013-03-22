/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
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
package org.jboss.as.quickstarts.datagrid.hotrod;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Gencur
 */
public class Team implements Serializable {

    private static final long serialVersionUID = -181403229462007401L;

    private String teamName;
    private List<String> players;

    public Team(String teamName) {
        this.teamName = teamName;
        players = new ArrayList<String>();
    }

    public void addPlayer(String name) {
        players.add(name);
    }

    public void removePlayer(String name) {
        players.remove(name);
    }

    public List<String> getPlayers() {
        return players;
    }

    public String getName() {
        return teamName;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder("=== Team: " + teamName + " ===\n");
        b.append("Players:\n");
        for (String player : players) {
            b.append("- " + player + "\n");
        }
        return b.toString();
    }
}