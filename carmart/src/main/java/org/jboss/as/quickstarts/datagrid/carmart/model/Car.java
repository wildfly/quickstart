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
package org.jboss.as.quickstarts.datagrid.carmart.model;

import java.io.Serializable;

/**
 * Represents a car in the car mart. Car objects are stored in the cache.
 * 
 * @author Martin Gencur
 * 
 */
public class Car implements Serializable {

    private static final long serialVersionUID = 188164481825309731L;

    public enum CarType {
        SEDAN, HATCHBACK, COMBI, CABRIO, ROADSTER
    }

    public enum Country {
        CZECH_REPUBLIC, USA, GERMANY
    }

    public Car() {
    }

    public Car(String brand, double displacement, CarType type, String color, String numberPlate, Country country) {
        this.brand = brand;
        this.displacement = displacement;
        this.type = type;
        this.color = color;
        this.numberPlate = numberPlate;
        this.country = country;
    }

    private String brand;
    private double displacement;
    private CarType type;
    private String color;
    private String numberPlate;
    private Country country;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public CarType getType() {
        return type;
    }

    public void setType(CarType type) {
        this.type = type;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getDisplacement() {
        return displacement;
    }

    public void setDisplacement(double displacement) {
        this.displacement = displacement;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Country getCountry() {
        return country;
    }
}