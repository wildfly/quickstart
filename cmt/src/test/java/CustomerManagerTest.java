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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.quickstarts.cmt.controller.CustomerManager;
import org.jboss.as.quickstarts.cmt.ejb.CustomerManagerEJB;
import org.jboss.as.quickstarts.cmt.ejb.CustomerManagerEJBImpl;
import org.jboss.as.quickstarts.cmt.model.Customer;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class CustomerManagerTest {
    @Inject
    private CustomerManager customerManager;

    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(mappedName = "java:/queue/test")
    private Queue queue;

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(CustomerManager.class, CustomerManagerEJB.class, CustomerManagerEJBImpl.class, Customer.class)
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void checkListCustomers() throws Exception {
        Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageConsumer messageConsumer = session.createConsumer(queue);
        connection.start();
        assertNull(messageConsumer.receive(1000));
        List<Customer> firstList = customerManager.getCustomers();

        // Create a customer
        customerManager.addCustomer("Test" + System.currentTimeMillis());
        List<Customer> secondList = customerManager.getCustomers();
        // Create a different customer
        customerManager.addCustomer("Test" + System.currentTimeMillis());
        List<Customer> thirdList = customerManager.getCustomers();

        // Check that the list size increased
        assertTrue(secondList.size() == firstList.size() + 1);
        assertTrue(thirdList.size() == secondList.size() + 1);

        assertNotNull(messageConsumer.receive(1000));
        assertNotNull(messageConsumer.receive(1000));
        assertNull(messageConsumer.receive(1000));
        connection.close();
    }

    @Test
    public void checkDuplicateCustomers() throws Exception {
        Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageConsumer messageConsumer = session.createConsumer(queue);
        connection.start();
        assertNull(messageConsumer.receive(1000));
        List<Customer> firstList = customerManager.getCustomers();
        String duplicateCustomerName = "Test" + System.currentTimeMillis();

        // Create a customer
        customerManager.addCustomer(duplicateCustomerName);
        List<Customer> secondList = customerManager.getCustomers();
        // Create the same customer
        customerManager.addCustomer(duplicateCustomerName);
        List<Customer> thirdList = customerManager.getCustomers();

        // Check that the list size increased where appropriate
        assertTrue(secondList.size() == firstList.size() + 1);
        assertTrue(thirdList.size() == secondList.size());

        assertNotNull(messageConsumer.receive(1000));
        assertNull(messageConsumer.receive(1000));
        connection.close();
    }

    @Test
    public void checkRollbackCustomer() throws Exception {
        Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageConsumer messageConsumer = session.createConsumer(queue);
        connection.start();
        assertNull(messageConsumer.receive(1000));
        List<Customer> firstList = customerManager.getCustomers();

        UserTransaction ut = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
        ut.begin();
        // Create a customer
        customerManager.addCustomer("Test" + System.currentTimeMillis());

        ut.rollback();

        assertNull(messageConsumer.receive(1000));
        connection.close();

        List<Customer> secondList = customerManager.getCustomers();
        // Check that the list size is not increased
        assertTrue(secondList.size() == firstList.size());
    }

}