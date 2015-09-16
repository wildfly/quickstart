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
package org.jboss.as.quickstarts.threadracing;

import org.jboss.as.quickstarts.threadracing.results.ChampionshipStandings;
import org.jboss.as.quickstarts.threadracing.results.RaceResult;

import java.util.List;

/**
 * The broadcaster provides the real time race progress updates, and the race result as well.
 *
 * @author Eduardo Martins
 */
public interface RaceBroadcaster {

    /**
     * Starts the race's broadcast.
     */
    void start();

    /**
     * Broadcasts the most famous words of car racing.
     */
    void startYourEngines();

    /**
     * Broadcasts the race start.
     */
    void readySetGo();

    /**
     * Broadcasts a race progress update.
     * @param msg
     */
    void raceProgress(String msg);

    /**
     * Broadcasts the race's end.
     */
    void raceEnd();

    /**
     * Broadcasts the race's result.
     * @param result
     */
    void raceResult(RaceResult result);

    /**
     * Broadcasts the championship standings.
     * @param entries
     */
    void championshipStandings(List<ChampionshipStandings.Entry> entries);

}
