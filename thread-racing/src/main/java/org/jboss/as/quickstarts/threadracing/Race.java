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
import org.jboss.as.quickstarts.threadracing.results.RaceResults;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The awesome Jakarta EE thread race. It's the core of the app logic, but has no usage of Jakarta EE technologies, thus of limited interest to study.
 *
 * @author Eduardo Martins
 */
public class Race {

    /**
     * a barrier used to sync racers for the race start
     */
    private CyclicBarrier startBarrier;

    /**
     * a count down latch used by the race to know when all racers finished/aborted
     */
    private CountDownLatch endCountDownLatch;

    /**
     * provides the positions to racer's who finish the race, the initial value is the first position, which increments on a racer finish
     */
    private AtomicInteger donePosition;

    /**
     * provides the position to racer's who abandon the race, the initial value is the last possible position, which decrements on a racer abort
     */
    private AtomicInteger abortedPosition;

    /**
     * the race result
     */
    private RaceResult result;

    /**
     * the race's environment
     */
    private final Map<String, String> environment;

    /**
     * the race's broadcaster
     */
    private final RaceBroadcaster broadcaster;

    /**
     * the past race results
     */
    private final RaceResults results;

    /**
     * the racer #1
     */
    private final Racer racer1;

    /**
     * the racer #2
     */
    private final Racer racer2;

    /**
     * the racer #3
     */
    private final Racer racer3;

    /**
     * the racer #4
     */
    private final Racer racer4;

    /**
     * Creates a new race with the specified racers and environment.
     * @param racer1
     * @param racer2
     * @param racer3
     * @param racer4
     * @param environment
     * @param broadcaster the broadcaster that will be used to update fans about the race progress.
     * @param results the race results
     */
    public Race(Racer racer1, Racer racer2, Racer racer3, Racer racer4, Map<String, String> environment, RaceBroadcaster broadcaster, RaceResults results) {
        this.racer1 = racer1;
        this.racer2 = racer2;
        this.racer3 = racer3;
        this.racer4 = racer4;
        this.environment = environment;
        this.broadcaster = broadcaster;
        this.results = results;
    }

    /**
     * Starts the race.
     * @throws Exception if there is an unexpected issue with the race, such as racers taking too long being ready to start, or not finishing the race in time.
     */
    public synchronized void run() throws Exception {
        reset();
        broadcaster.start();
        registerRacers();
        startEngines();
        startRace();
        awaitEnd();
        processResult();
    }

    /**
     * Resets the race state.
     */
    private void reset() {
        startBarrier = new CyclicBarrier(5);
        endCountDownLatch = new CountDownLatch(4);
        donePosition = new AtomicInteger(1);
        abortedPosition = new AtomicInteger(4);
        result = new RaceResult();
    }

    /**
     * Registers all racers.
     */
    private void registerRacers() {
        racer1.setRegistration(new Registration(racer1, 1));
        racer2.setRegistration(new Registration(racer2, 2));
        racer3.setRegistration(new Registration(racer3, 3));
        racer4.setRegistration(new Registration(racer4, 4));
    }

    /**
     * Starts racer's engines.
     */
    private void startEngines() {
        broadcaster.startYourEngines();
        racer1.startEngine();
        racer2.startEngine();
        racer3.startEngine();
        racer4.startEngine();
    }

    /**
     * Starts the race. Note that the race only starts when all racers are ready, i.e. all waiting at the start barrier, and for that to happen there is a 30 seconds timeout.
     * @throws Exception if the race start has expired
     */
    private void startRace() throws Exception {
        broadcaster.readySetGo();
        startBarrier.await(30, TimeUnit.SECONDS);
    }

    /**
     * Awaits the race to finish. If the race is still in progress this method will block and wait for the race to end. The wait has a timeout of 90 seconds.
     * @throws Exception if the race end has expired.
     */
    private void awaitEnd() throws Exception {
        endCountDownLatch.await(90, TimeUnit.SECONDS);
        broadcaster.raceEnd();
    }

    /**
     * Process the race result.
     */
    private void processResult() {
        results.add(result);
        broadcaster.raceResult(result);
        broadcaster.championshipStandings(new ChampionshipStandings().addAll(results).getEntryList());
    }

    /**
     * The racer's registration.
     */
    public class Registration {

        private final int number;

        /**
         * the registration's racer
         */
        private final Racer racer;

        /**
         *
         * @param racer
         */
        private Registration(Racer racer, int number) {
            this.number = number;
            this.racer = racer;
            broadcast("joins the race.");
        }

        /**
         * The racer is reader to start the race.
         * @throws Exception
         */
        public void ready() throws Exception {
            startBarrier.await(30, TimeUnit.SECONDS);
        }

        /**
         * The racer has finished the race.
         */
        public void done() {
            int racerPosition = donePosition.getAndIncrement();
            broadcast("finished the race.");
            result.setPosition(this, racerPosition);
            endCountDownLatch.countDown();
        }

        /**
         * The racer has aborted the race.
         */
        public void aborted(Throwable t) {
            int racerPosition = abortedPosition.getAndDecrement();
            t.printStackTrace();
            broadcast("aborted the race. Reason: " + (t != null ? t.getMessage() : "N/A)"));
            result.setPosition(this, racerPosition);
            endCountDownLatch.countDown();
        }

        /**
         * Retrieves the race's environment.
         * @return
         */
        public Map<String, String> getEnvironment() {
            return environment;
        }

        /**
         * Retrieves the registered racer.
         * @return
         */
        public Racer getRacer() {
            return racer;
        }

        /**
         * Retrieves the registration number.
         * @return
         */
        public int getNumber() {
            return number;
        }

        /**
         * Broadcasts a msg with respect to a race event.
         * @param msg
         */
        public void broadcast(String msg) {
            broadcaster.raceProgress(racer.getName() + ' ' + msg);
        }
    }
}
