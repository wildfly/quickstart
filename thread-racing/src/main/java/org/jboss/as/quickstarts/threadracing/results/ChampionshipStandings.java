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
package org.jboss.as.quickstarts.threadracing.results;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The championship standings provides a sorted list of (racer's name, points) pairs, representing the how racers stand in the championship, the set of all races run.
 *
 * The first entry is the current championship leader.
 *
 * The Race position to championship points mapping is:
 * 1st place    -> 4 points
 * 2nd place    -> 3 points
 * 3rd place    -> 2 points
 * 4th place    -> 1 points
 *
 * @author Eduardo Martins
 */
public class ChampionshipStandings {

    /**
     * the mapping racer's name --> racer's sum of points obtained in all added race results.
     */
    private final Map<String, Integer> racerPointsTotals = new HashMap<>();

    /**
     * Adds all race results.
     * @param raceResults
     * @return
     */
    public ChampionshipStandings addAll(RaceResults raceResults) {
        for (RaceResult raceResult : raceResults.findAll()) {
            add(raceResult);
        }
        return this;
    }

    /**
     * Adds a race result.
     * @param raceResult
     * @return
     */
    public ChampionshipStandings add(RaceResult raceResult) {
        processRacerResult(raceResult.getRacer1Name(), raceResult.getRacer1Position(), racerPointsTotals);
        processRacerResult(raceResult.getRacer2Name(), raceResult.getRacer2Position(), racerPointsTotals);
        processRacerResult(raceResult.getRacer3Name(), raceResult.getRacer3Position(), racerPointsTotals);
        processRacerResult(raceResult.getRacer4Name(), raceResult.getRacer4Position(), racerPointsTotals);
        return this;
    }

    /**
     * Updates the racer's totals with a race's position.
     * @param racerName
     * @param racerPosition
     * @param racerPointsTotals
     */
    private void processRacerResult(String racerName, int racerPosition, Map<String, Integer> racerPointsTotals) {
        final int racerPoints;
        if (racerPosition == 1) {
            racerPoints = 4;
        } else if (racerPosition == 2) {
            racerPoints = 3;
        } else if (racerPosition == 3) {
            racerPoints = 2;
        } else {
            racerPoints = 1;
        }
        final Integer racerPointsTotal = racerPointsTotals.get(racerName);
        if (racerPointsTotal == null) {
            racerPointsTotals.put(racerName, racerPoints);
        } else {
            racerPointsTotals.put(racerName, racerPoints + racerPointsTotal);
        }
    }

    /**
     * Builds the championship standings list. The list is ordered by racer's points, being the first entry the racer currently with more points. Racers with same points will be sorted by name.
     * @return
     */
    public List<Entry> getEntryList() {
        final List<Entry> entries = new ArrayList<>();
        for (Map.Entry<String, Integer> racerPointsTotal : racerPointsTotals.entrySet()) {
            final String racer = racerPointsTotal.getKey();
            final Integer points = racerPointsTotal.getValue();
            entries.add(new Entry(racer, points));
        }
        Collections.sort(entries);
        return entries;
    }

    /**
     * An entry of the championship standings.
     */
    public static class Entry implements Comparable<Entry> {

        private final String name;
        private final Integer points;

        /**
         *
         * @param name
         * @param points
         */
        private Entry(String name, Integer points) {
            this.name = name;
            this.points = points;
        }

        /**
         *
         * @return
         */
        public String getName() {
            return name;
        }

        /**
         *
         * @return
         */
        public int getPoints() {
            return points;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Entry racer = (Entry) o;
            if (!name.equals(racer.name))
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }

        @Override
        public int compareTo(Entry o) {
            int result = -this.points.compareTo(o.points);
            if (result == 0) {
                result = this.name.compareTo(o.name);
            }
            return result;
        }
    }
}
