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
package org.jboss.quickstarts.contact;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the contacts table.
 *
 * @author Joshua Wilson
 *
 */
public class ContactValidator {
    @Inject
    private Validator validator;

    @Inject
    private ContactRepository crud;

    /**
     * <p>
     * Validates the given Contact variable and throws validation exceptions based on the type of error. If the error is standard
     * bean validation errors then it will throw a ConstraintValidationException with the set of the constraints violated.
     * </p>
     * <p>
     * If the error is caused because an existing contact with the same email is registered it throws a regular validation
     * exception so that it can be interpreted separately.
     * </p>
     *
     * @param contact Contact to be validated
     * @throws ConstraintViolationException If Bean Validation errors exist
     * @throws ValidationException If contact with the same email already exists
     */
    void validateContact(Contact contact) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Contact>> violations = validator.validate(contact);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<>(violations));
        }

        // Check the uniqueness of the email address
        if (emailAlreadyExists(contact.getEmail(), contact.getId())) {
            throw new ValidationException("Unique Email Violation");
        }
    }

    /**
     * Checks if a contact with the same email address is already registered. This is the only way to easily capture the
     * "@UniqueConstraint(columnNames = "email")" constraint from the Contact class.
     *
     * Since Update will being using an email that is already in the database we need to make sure that it is the email
     * from the record being updated.
     *
     * @param email The email to check
     * @param id
     * @return True if the email already exists, and false otherwise
     */
    boolean emailAlreadyExists(String email, Long id) {
        Contact contact = null;
        Contact contactWithID = null;
        try {
            contact = crud.findByEmail(email);
        } catch (NoResultException e) {
            // ignore
        }

        if (contact != null && id != null) {
            try {
                contactWithID = crud.findById(id);
                if (contactWithID != null && contactWithID.getEmail().equals(email)) {
                    contact = null;
                }
            } catch (NoResultException e) {
                // ignore
            }
        }
        return contact != null;
    }
}
