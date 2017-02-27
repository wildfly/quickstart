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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.jboss.as.quickstarts.threadracing.Race;

/**
 * A race result, a JPA entity.
 *
 * @author Eduardo Martins
 */
@Entity
@Table(name = "THREAD_RACE_RESULTS")
@NamedQueries({
        @NamedQuery(name = "RaceResult.findAll", query = "SELECT e FROM RaceResult e")
})
public class RaceResult implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column
    private String racer1Name;

    @Column
    private int racer1Position;

    @Column
    private String racer2Name;

    @Column
    private int racer2Position;

    @Column
    private String racer3Name;

    @Column
    private int racer3Position;

    @Column
    private String racer4Name;

    @Column
    private int racer4Position;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRacer1Name() {
        return racer1Name;
    }

    public void setRacer1Name(String racer1Name) {
        this.racer1Name = racer1Name;
    }

    public int getRacer1Position() {
        return racer1Position;
    }

    public void setRacer1Position(int racer1Position) {
        this.racer1Position = racer1Position;
    }

    public String getRacer2Name() {
        return racer2Name;
    }

    public void setRacer2Name(String racer2Name) {
        this.racer2Name = racer2Name;
    }

    public int getRacer2Position() {
        return racer2Position;
    }

    public void setRacer2Position(int racer2Position) {
        this.racer2Position = racer2Position;
    }

    public String getRacer3Name() {
        return racer3Name;
    }

    public void setRacer3Name(String racer3Name) {
        this.racer3Name = racer3Name;
    }

    public int getRacer3Position() {
        return racer3Position;
    }

    public void setRacer3Position(int racer3Position) {
        this.racer3Position = racer3Position;
    }

    public String getRacer4Name() {
        return racer4Name;
    }

    public void setRacer4Name(String racer4Name) {
        this.racer4Name = racer4Name;
    }

    public int getRacer4Position() {
        return racer4Position;
    }

    public void setRacer4Position(int racer4Position) {
        this.racer4Position = racer4Position;
    }

    /**
     * Sets the result for the provided registration.
     *
     * @param registration
     * @param position
     */
    public void setPosition(Race.Registration registration, int position) {
        switch (registration.getNumber()) {
            case 1:
                setRacer1Name(registration.getRacer().getName());
                setRacer1Position(position);
                break;
            case 2:
                setRacer2Name(registration.getRacer().getName());
                setRacer2Position(position);
                break;
            case 3:
                setRacer3Name(registration.getRacer().getName());
                setRacer3Position(position);
                break;
            case 4:
                setRacer4Name(registration.getRacer().getName());
                setRacer4Position(position);
                break;
            default:
                throw new IllegalStateException("registration number must be 1-4");
        }
    }

    /**
     * Retrieves a list with racers ordered by race position.
     *
     * @return
     */
    public List<String> getSortedRacers() {
        List<String> result = new ArrayList<>();
        result.add(getRacerWithPosition(1));
        result.add(getRacerWithPosition(2));
        result.add(getRacerWithPosition(3));
        result.add(getRacerWithPosition(4));
        return result;
    }

    /**
     * Retrieves the racer with the specified position
     *
     * @param i
     * @return
     */
    public String getRacerWithPosition(int i) {
        if (racer1Position == i) {
            return racer1Name;
        } else if (racer2Position == i) {
            return racer2Name;
        } else if (racer3Position == i) {
            return racer3Name;
        } else if (racer4Position == i) {
            return racer4Name;
        } else {
            return null;
        }
    }
}
