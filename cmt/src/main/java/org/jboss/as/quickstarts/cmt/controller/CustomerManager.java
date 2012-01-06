/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates,
 * and individual contributors as indicated by the @author tags.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 * (C) 2011,
 * @author JBoss, by Red Hat.
 */
package org.jboss.as.quickstarts.cmt.controller;

import java.util.List;
import java.util.logging.Logger;

import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.jboss.as.quickstarts.cmt.ejb.CustomerManagerEJB;
import org.jboss.as.quickstarts.cmt.model.Customer;

@Named("customerManager")
@RequestScoped
public class CustomerManager {
    private Logger logger = Logger.getLogger(CustomerManager.class.getName());

    @Inject
    private CustomerManagerEJB customerManager;

    public List<Customer> getCustomers() throws SecurityException, IllegalStateException, NamingException,
            NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
        return customerManager.listCustomers();
    }

    public String addCustomer(String name) {
        try {
            customerManager.createCustomer(name);
            return "customerAdded";
        } catch (Exception e) {
            logger.warning("Caught a duplicate: " + e.getMessage());
            // Transaction will be marked rollback only anyway utx.rollback();
            return "customerDuplicate";
        }
    }
}
