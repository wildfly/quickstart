/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the &quot;License&quot;);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.as.quickstart.cdi.veto;

import java.util.logging.Logger;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.jboss.as.quickstart.cdi.veto.model.Car;

/**
 * Producer for the {@link Car} entity.
 */
public class CarManager {
    @Inject
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    private Long id;

    private Logger log = Logger.getLogger(CarManager.class.getSimpleName());

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns either a new instance or a managed instance of {@link Car}.
     * The produced entity should be dependent scoped to avoid incompatible proxies between
     * JPA and CDI.
     */
    @Produces
    public Car getCar() {
        if (id == null) {
            log.info("Returning new instance of Car");
            return new Car();
        }
        log.info("Finding instance of Car with id " + id);
        return em.find(Car.class, id);
    }

    public void save(Car car) {
        try {
            utx.begin();
            em.joinTransaction();
            em.persist(car);
            utx.commit();
        } catch (NotSupportedException e) {
            log.severe("Transaction Error: " + e.getMessage());
        } catch (SystemException e) {
            log.severe("Transaction Error: " + e.getMessage());
        } catch (HeuristicRollbackException e) {
            log.severe("Transaction Error: " + e.getMessage());
        } catch (RollbackException e) {
            log.severe("Transaction Error: " + e.getMessage());
        } catch (HeuristicMixedException e) {
            log.severe("Transaction Error: " + e.getMessage());
        }
    }
}
