/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
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
package com.redhat.datagrid.hotrod;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;

/**
 * @author Martin Gencur
 */
public class FootballManager {
    
    private static final String EDG_HOST = "edg.host"; 
    private static final String HOTROD_PORT = "edg.hotrod.port"; 
    private static final String PROPERTIES_FILE = "edg.properties";
    private static final String msgTeamMissing = "The specified team \"%s\" does not exist, choose next operation\n";
    private static final String msgEnterTeamName = "Enter team name: ";
    private static final String initialPrompt = "Choose action:\n" +
                                                "============= \n" +
                                                "at  -  add a team\n" +
                                                "ap  -  add a player to a team\n" +
                                                "rt  -  remove a team\n" +
                                                "rp  -  remove a player from a team\n" +
                                                "p   -  print all teams and players\n" +
                                                "q   -  quit\n" ;
    private static final String teamsKey = "teams";
    
    private Console con;
    private RemoteCache<String, Object> cache;
    
    public FootballManager(Console con) {
        this.con = con;
        cache = new RemoteCacheManager(edgProperty(EDG_HOST) + ":" + edgProperty(HOTROD_PORT)).getCache("teams");
        Team t = new Team("Barcelona");
        t.addPlayer("Messi");
        t.addPlayer("Pedro");
        t.addPlayer("Puyol");
        cache.put(t.getName(), t);
        List<String> teams = new ArrayList<String>();
        teams.add(t.getName());
        cache.put(teamsKey, teams);
    }
    
    public void addTeam() {
        String teamName = con.readLine(msgEnterTeamName);
        @SuppressWarnings("unchecked")
        List<String> teams = (List<String>) cache.get(teamsKey);
        if (teams == null) {
            teams = new ArrayList<String>();
        }
        Team t = new Team(teamName);
        cache.put(teamName, t);
        teams.add(teamName);
        //maintain a list of teams under common key
        cache.put(teamsKey, teams);
    }
    
    public void addPlayers() {
        String teamName = con.readLine(msgEnterTeamName);
        String playerName = null;
        Team t = (Team) cache.get(teamName);
        if (t != null) {
            while (!(playerName = con.readLine("Enter player's name (to stop adding, type \"q\"): ")).equals("q")) {
                t.addPlayer(playerName);
            }
            cache.put(teamName, t);
        } else {
            con.printf(msgTeamMissing, teamName);
        }
    }
    
    public void removePlayer() {
        String playerName = con.readLine("Enter player's name: ");
        String teamName = con.readLine("Enter player's team: ");
        Team t = (Team) cache.get(teamName);
        if (t != null) {
            t.removePlayer(playerName);
            cache.put(teamName, t);
        } else {
            con.printf(msgTeamMissing, teamName);
        }
    }
    
    public void removeTeam() {
        String teamName = con.readLine(msgEnterTeamName);
        Team t = (Team) cache.get(teamName);
        if (t != null) {
            cache.remove(teamName);
            @SuppressWarnings("unchecked")
            List<String> teams = (List<String>) cache.get(teamsKey);
            if (teams != null) {
                teams.remove(teamName);
            }
            cache.put(teamsKey, teams);
        } else {
            con.printf(msgTeamMissing, teamName);
        }
    }
    
    public void printTeams() {
        @SuppressWarnings("unchecked")
        List<String> teams = (List<String>) cache.get(teamsKey);
        if (teams != null) {
            for (String teamName : teams) {
                con.printf(cache.get(teamName).toString());
            }
        }
    }
    
    public static void main(String[] args) {
        Console con = System.console();
        FootballManager manager = new FootballManager(System.console());
        con.printf(initialPrompt);

        while (true) {
            String action = con.readLine(">");
            if ("at".equals(action)) {
                manager.addTeam();
            } else if ("ap".equals(action)) {
                manager.addPlayers();
            } else if ("rt".equals(action)) {
                manager.removeTeam();
            } else if ("rp".equals(action)) {
                manager.removePlayer();
            } else if ("p".equals(action)) {
                manager.printTeams();
            } else if ("q".equals(action)) {
                break;
            }
        }
    }
    
    public static String edgProperty(String name) {
        Properties props = new Properties();
        try { 
            props.load(FootballManager.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        return props.getProperty(name);
    }
}
