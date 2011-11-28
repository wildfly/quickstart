/*
 * Copyright 2011 Red Hat, Inc. and/or its affiliates.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA
 */
package com.redhat.datagrid.memcached;

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