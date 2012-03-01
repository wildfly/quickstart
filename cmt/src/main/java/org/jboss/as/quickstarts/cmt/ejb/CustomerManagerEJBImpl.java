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
package org.jboss.as.quickstarts.cmt.ejb;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.jboss.as.quickstarts.cmt.controller.CustomerManager;
import org.jboss.as.quickstarts.cmt.model.Customer;

@Stateless
public class CustomerManagerEJBImpl implements CustomerManagerEJB {
    private Logger logger = Logger.getLogger(CustomerManager.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    @Resource(mappedName = "java:/JmsXA")
    private ConnectionFactory connectionFactory;

    @Resource(mappedName = "java:/queue/CMTQueue")
    private Queue queue;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public int createCustomer(String name) throws Exception {
        Customer c1 = new Customer();
        c1.setName(name);
        entityManager.persist(c1);

        Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer messageProducer = session.createProducer(queue);
        connection.start();
        TextMessage message = session.createTextMessage();
        message.setText("Created customer named: " + name + " with ID: " + c1.getId());
        messageProducer.send(message);
        connection.close();

        return c1.getId();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @SuppressWarnings("unchecked")
    public List<Customer> listCustomers() throws NamingException, NotSupportedException, SystemException, SecurityException,
            IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
        return entityManager.createQuery("select c from Customer c").getResultList();
    }
}
