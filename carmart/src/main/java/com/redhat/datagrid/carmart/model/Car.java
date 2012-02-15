/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat Middleware LLC, and individual contributors
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
package com.redhat.datagrid.carmart.model;

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