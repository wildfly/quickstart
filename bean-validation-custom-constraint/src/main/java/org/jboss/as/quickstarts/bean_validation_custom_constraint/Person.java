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

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "person")
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "person_id")
    private Long personId;

    /* Asserts that the annotated string, collection, map or array is not null or empty. */
    @NotNull
    /*
     * The size of the field or property is evaluated and must match the specified boundaries.If the field or property is a
     * String, the size of the string is evaluated.If the field or property is a Collection, the size of the Collection is
     * evaluated.If the field or property is a Map, the size of the Map is evaluated.If the field or property is an array, the
     * size of the array is evaluated.Use one of the optional max or min elements to specify the boundaries.
     */
    @Size(min = 4)
    private String firstName;

    @NotNull
    @Size(min = 4)
    private String lastName;

    // Custom Constraint @Address for bean validation
    @NotNull
    @Address
    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL)
    private PersonAddress personAddress;

    public Person() {

    }

    public Person(String firstName, String lastName, PersonAddress address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personAddress = address;
    }

    public Long getId() {
        return personId;
    }

    public void setId(Long id) {
        this.personId = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public PersonAddress getPersonAddress() {
        return personAddress;
    }

    public void setPersonAddress(PersonAddress personAddress) {
        this.personAddress = personAddress;
    }

}
