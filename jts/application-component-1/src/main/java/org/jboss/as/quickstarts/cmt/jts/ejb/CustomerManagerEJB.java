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
package org.jboss.as.quickstarts.cmt.jts.ejb;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.jboss.as.quickstarts.cmt.model.Customer;

@Stateless
public class CustomerManagerEJB {

    @PersistenceContext
    private EntityManager entityManager;

    @EJB(lookup = "corbaname:iiop:localhost:3628#jts-quickstart/InvoiceManagerEJBImpl")
    private InvoiceManagerEJBHome invoiceManagerHome;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void createCustomer(String name) throws RemoteException, JMSException {

        Customer c1 = new Customer();
        c1.setName(name);
        entityManager.persist(c1);

        final InvoiceManagerEJB invoiceManager = invoiceManagerHome.create();
        invoiceManager.createInvoice(name);
    }

    /**
     * List all the customers.
     * 
     * @return
     * @throws NamingException
     * @throws NotSupportedException
     * @throws SystemException
     * @throws SecurityException
     * @throws IllegalStateException
     * @throws RollbackException
     * @throws HeuristicMixedException
     * @throws HeuristicRollbackException
     */
    @TransactionAttribute(TransactionAttributeType.NEVER)
    @SuppressWarnings("unchecked")
    public List<Customer> listCustomers() {
        return entityManager.createQuery("select c from Customer c").getResultList();
    }
}
