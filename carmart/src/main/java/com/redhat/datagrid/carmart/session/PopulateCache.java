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
package com.redhat.datagrid.carmart.session;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import org.infinispan.api.BasicCache;
import com.redhat.datagrid.carmart.model.Car;
import com.redhat.datagrid.carmart.model.Car.CarType;
import com.redhat.datagrid.carmart.model.Car.Country;

/**
 * Populates a cache with initial data.
 * 
 * @author Martin Gencur
 * 
 */
@Startup
@Singleton
public class PopulateCache {
    private Logger log = Logger.getLogger(this.getClass().getName());

    @Inject
    private CacheContainerProvider provider;

    @PostConstruct
    public void startup() {
        BasicCache<String, Object> cars = provider.getCacheContainer().getCache(CarManager.CACHE_NAME);
        List<String> carNumbers = new ArrayList<String>();

        try {
            Car c = new Car("Ford Focus", 1.6, CarType.COMBI, "white", "FML 23-25", Country.CZECH_REPUBLIC);
            carNumbers.add(c.getNumberPlate());
            cars.put(CarManager.encode(c.getNumberPlate()), c);
            c = new Car("BMW X3", 2.0, CarType.SEDAN, "gray", "1P3 2632", Country.CZECH_REPUBLIC);
            carNumbers.add(c.getNumberPlate());
            cars.put(CarManager.encode(c.getNumberPlate()), c);
            c = new Car("Ford Mondeo", 2.2, CarType.COMBI, "blue", "1B2 1111", Country.USA);
            carNumbers.add(c.getNumberPlate());
            cars.put(CarManager.encode(c.getNumberPlate()), c);
            c = new Car("Mazda MX-5", 1.8, CarType.CABRIO, "red", "6T4 2526", Country.USA);
            carNumbers.add(c.getNumberPlate());
            cars.put(CarManager.encode(c.getNumberPlate()), c);
            c = new Car("VW Golf", 1.6, CarType.HATCHBACK, "yellow", "2B2 4946", Country.GERMANY);
            carNumbers.add(c.getNumberPlate());
            cars.put(CarManager.encode(c.getNumberPlate()), c);
            // insert a list of cars' number plates
            cars.put(CarManager.CAR_NUMBERS_KEY, carNumbers);
        } catch (Exception e) {
            log.warning("An exception occured while populating the database!");
        }

        log.info("Successfully imported data!");
    }
}
