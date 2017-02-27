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
package org.jboss.as.quickstarts.bean_validation_custom_constraint;

import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Simple tests for Bean Validator using custom constraints. Arquillian deploys an WAR archive to the application server, which
 * constructs Validator object.
 *
 * This object is injected into the tests so user can verify the validators are working. This example does not touch validation
 * on database layer, e.g. it is not validating uniqueness constraint for email field.
 *
 *
 * @author <a href="https://community.jboss.org/people/giriraj.sharma27">Giriraj Sharma</a>
 *
 */
@RunWith(Arquillian.class)
public class MyPersonIT {

    /**
     * Constructs a deployment archive
     *
     * @return the deployment archive
     */
    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addClasses(Person.class, PersonAddress.class, Address.class, AddressValidator.class)
            // enable JPA
            .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
            // add sample data
            .addAsResource("import.sql")
            // enable CDI
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            // Deploy our test datasource
            .addAsWebInfResource("test-ds.xml", "test-ds.xml");
    }

    // Get configured validator directly from JBoss EAP environment
    @Inject
    Validator validator;

    /**
     * Tests an empty member registration, e.g. violation of:
     *
     * <ul>
     * <li>@NotNull</li>
     * <li>@Size</li>
     * <li>@Address</li>
     * </ul>
     */

    /**
     * Tests a valid Person and correct address
     */
    @Test
    public void testCorrectAddress() {
        Set<ConstraintViolation<Person>> violations = validator.validate(createValidPerson());

        Assert.assertEquals("No violations were found", 0, violations.size());
    }

    /**
     * Validating the model data which has correct values. Tests {@code @Address} constraint
     */
    @Test
    public void testAddressViolation() {
        Person person = createValidPerson();
        validateAddressConstraints(person);

        // Setting city field of address.
        person.getPersonAddress().setCity("Carolina");
        validateAddressConstraints(person);

        // Setting pin code equal to valid length of 6 characters.
        person.getPersonAddress().setPinCode("123456");
        person.getPersonAddress().setCity("Auckland");
        validateAddressConstraints(person);

        // Setting country name with valid length of more than 4 characters
        person.getPersonAddress().setPinCode("123456");
        person.getPersonAddress().setCountry("Mexico");
        validateAddressConstraints(person);

    }

    private void validateAddressConstraints(Person person) {
        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        for (ConstraintViolation<Person> violation : violations) {
            Assert.assertEquals("One violation was found", 1, violations.size());
            Assert.assertEquals("Address Field  was invalid", violation.getMessage(), violation.getInvalidValue());
        }
    }

    private Person createValidPerson() {
        PersonAddress address = new PersonAddress("#12, 4th Main", "XYZ Layout", "Bangalore", "Karnataka", "India", "56004554");
        Person person = new Person("John", "Smith", address);
        return person;
    }
}
