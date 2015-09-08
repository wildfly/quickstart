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
/**
 * Unit tests that cover basic functionality.
 */

module('Validation', {
    setup: function() {
        // run before
    },
    teardown: function() {
        // run after
    }
});

test('should only allow names with alpha characters, an apostrophe, or a dash in them.', 8, function() {
    // well formed names
    ok(CONTACTS.validation.validateName('john'), 'john is well formed');
    ok(CONTACTS.validation.validateName('John'), 'John is well formed');
    ok(CONTACTS.validation.validateName('smith-jones'), 'smith-jones is well formed');
    ok(CONTACTS.validation.validateName("O'Maley"), "O'Maley is well formed");

    // mal-formed names
    ok(!CONTACTS.validation.validateName('John Smith'), 'John Smith is not well formed. It has a space in it.');
    ok(!CONTACTS.validation.validateName('john3'), 'john3 is not well formed. No numbers allowed.');
    ok(!CONTACTS.validation.validateName('3'), '3 is not well formed. No numbers allowed.');
    ok(!CONTACTS.validation.validateName('john$'), 'john$ is not well formed. No specials allowed.');
});


module('Validation in the Add form', {
    setup: function() {
        // run before

        var $fixture = $( "#qunit-fixture" );

        $fixture.append(
            '<form name="contacts-add-form" id="contacts-add-form" class="contact_info" method="post" data-ajax="false">' +
                '<div>' +
                    '<label for="contacts-add-input-firstName">First Name:</label>' +
                    '<input name="firstName" id="contacts-add-input-firstName" class="name" data-clear-btn="true" value="" placeholder="Enter your first name." type="text"/>' +
                '</div>' +
                '<div>' +
                    '<label for="contacts-add-input-lastName">Last Name:</label>' +
                    '<input name="lastName" id="contacts-add-input-lastName" class="name" data-clear-btn="true" value="" placeholder="Enter your last name." type="text"/>' +
                '</div>' +
                '<div>' +
                    '<label for="contacts-add-input-tel">Phone:</label>' +
                    '<input name="phoneNumber" id="contacts-add-input-tel" class="phoneNumber" data-clear-btn="true" value="" type="tel"/>' +
                '</div>' +
                '<div>' +
                    '<label for="contacts-add-input-email">Email:</label>' +
                    '<input name="email" id="contacts-add-input-email" class="email" data-clear-btn="true" value="" type="email"/>' +
                '</div>' +
                '<div>' +
                    '<label for="contacts-add-input-date">Birth Date:</label>' +
                    '<input name="birthDate" id="contacts-add-input-date" class="birthDate" data-clear-btn="true" value="" type="date" min="1900-01-01"/>' +
                '</div>' +
                '<input id="submit-add-btn" data-inline="true" type="submit" value="Save" />' +
                '<input id="clear-add-btn" data-inline="true" type="reset" value="Clear" data-theme="c" />' +
                '<input id="cancel-add-btn" data-inline="true" type="reset" value="Cancel" data-theme="c" />' +
            '</form>'
        );
        CONTACTS.validation.runFormValidators();
    },
    teardown: function() {
        // run after
    }
});

test('should only allow names with alpha characters, an apostrophe, or a dash in them.', 8, function() {
    // well formed names
    ok(CONTACTS.validation.validateName('john'), 'john is well formed');
    ok(CONTACTS.validation.validateName('John'), 'John is well formed');
    ok(CONTACTS.validation.validateName('smith-jones'), 'smith-jones is well formed');
    ok(CONTACTS.validation.validateName("O'Maley"), "O'Maley is well formed");

    // mal-formed names
    ok(!CONTACTS.validation.validateName('John Smith'), 'John Smith is not well formed. It has a space in it.');
    ok(!CONTACTS.validation.validateName('john3'), 'john3 is not well formed. No numbers allowed.');
    ok(!CONTACTS.validation.validateName('3'), '3 is not well formed. No numbers allowed.');
    ok(!CONTACTS.validation.validateName('john$'), 'john$ is not well formed. No specials allowed.');
});

test('should be able to programmitcally insert a first name into the first name field. This is to make sure we can run the tests.', 1, function() {
    $('#contacts-add-input-firstName').val('johnytest')
    strictEqual($('#contacts-add-input-firstName').val(), "johnytest", 'The text "johnytest" was inserted.');
});

// First name tests
test('should display an error message in the first name field when the first name is not entered.', 1, function() {
    $('#contacts-add-input-firstName').val('')
    $('#contacts-add-input-lastName').val ('doe')
    $('#contacts-add-input-tel').val      ('+1 888-555-1212')
    $('#contacts-add-input-email').val    ('john.doe@abc.com')
    $('#contacts-add-input-date').val     ('1990-10-10')
    CONTACTS.validation.addContactsFormValidator.form();
    strictEqual($('label.error').text(), "Please specify a first name.", 'The first name was left empty.');
});

test('should display an error message in the first name field when the first name is not well formed.', 1, function() {
    $('#contacts-add-input-firstName').val('john3')
    $('#contacts-add-input-lastName').val ('doe')
    $('#contacts-add-input-tel').val      ('+1 888-555-1212')
    $('#contacts-add-input-email').val    ('john.doe@abc.com')
    $('#contacts-add-input-date').val     ('1990-10-10')
    CONTACTS.validation.addContactsFormValidator.form();
    strictEqual($('label.error').text(), "Please use a name without numbers or specials.", 'The first name was set to john3.');
});

test('should display an error message in the first name field when the first name is longer then 25 characters.', 1, function() {
    $('#contacts-add-input-firstName').val('johnssssssssssssssssssssss')
    $('#contacts-add-input-lastName').val ('doe')
    $('#contacts-add-input-tel').val      ('+1 888-555-1212')
    $('#contacts-add-input-email').val    ('john.doe@abc.com')
    $('#contacts-add-input-date').val     ('1990-10-10')
    CONTACTS.validation.addContactsFormValidator.form();
    strictEqual($('label.error').text(), "Please enter no more than 25 characters.", 'The first name was set to johnssssssssssssssssssssss.');
});

// Last name tests
test('should display an error message in the last name field when the last name is not entered.', 1, function() {
    $('#contacts-add-input-firstName').val('john')
    $('#contacts-add-input-lastName').val ('')
    $('#contacts-add-input-tel').val      ('+1 888-555-1212')
    $('#contacts-add-input-email').val    ('john.doe@abc.com')
    $('#contacts-add-input-date').val     ('1990-10-10')
    CONTACTS.validation.addContactsFormValidator.form();
    strictEqual($('label.error').text(), "Please specify a last name.", 'The last name was left empty.');
});

test('should display an error message in the last name field when the last name is not well formed.', 1, function() {
    $('#contacts-add-input-firstName').val('john')
    $('#contacts-add-input-lastName').val ('doe3')
    $('#contacts-add-input-tel').val      ('+1 888-555-1212')
    $('#contacts-add-input-email').val    ('john.doe@abc.com')
    $('#contacts-add-input-date').val     ('1990-10-10')
    CONTACTS.validation.addContactsFormValidator.form();
    strictEqual($('label.error').text(), "Please use a name without numbers or specials.", 'The last name was set to doe3.');
});

test('should display an error message in the last name field when the last name is longer then 25 characters.', 1, function() {
    $('#contacts-add-input-firstName').val('john')
    $('#contacts-add-input-lastName').val ('doesssssssssssssssssssssss')
    $('#contacts-add-input-tel').val      ('+1 888-555-1212')
    $('#contacts-add-input-email').val    ('john.doe@abc.com')
    $('#contacts-add-input-date').val     ('1990-10-10')
    CONTACTS.validation.addContactsFormValidator.form();
    strictEqual($('label.error').text(), "Please enter no more than 25 characters.", 'The last name was set to doesssssssssssssssssssssss.');
});

// Phone number tests
test('should display an error message in the phone number field when the phone number is not entered.', 1, function() {
    $('#contacts-add-input-firstName').val('john')
    $('#contacts-add-input-lastName').val ('doe')
    $('#contacts-add-input-tel').val      ('')
    $('#contacts-add-input-email').val    ('john.doe@abc.com')
    $('#contacts-add-input-date').val     ('1990-10-10')
    CONTACTS.validation.addContactsFormValidator.form();
    strictEqual($('label.error').text(), "Please enter a phone number.", 'The phone number was left empty.');
});

test('should display an error message in the phone number field when the phone number has anything other then digits.', 1, function() {
    $('#contacts-add-input-firstName').val('john')
    $('#contacts-add-input-lastName').val ('doe')
    $('#contacts-add-input-tel').val      ('+1 555-121')
    $('#contacts-add-input-email').val    ('john.doe@abc.com')
    $('#contacts-add-input-date').val     ('1990-10-10')
    CONTACTS.validation.addContactsFormValidator.form();
    strictEqual($('label.error').text(), "Please use a valid phone number with county code.", 'The phone number was set to 555-1212.');
});

// Email tests
test('should display an error message in the email field when the email is not entered.', 1, function() {
    $('#contacts-add-input-firstName').val('john')
    $('#contacts-add-input-lastName').val ('doe')
    $('#contacts-add-input-tel').val      ('+1 888-555-1212')
    $('#contacts-add-input-email').val    ('')
    $('#contacts-add-input-date').val     ('1990-10-10')
    CONTACTS.validation.addContactsFormValidator.form();
    strictEqual($('label.error').text(), "Please enter an e-mail.", 'The email was left empty.');
});

test('should display an error message in the email field when the email is not in the form of name@company.domain.', 1, function() {
    $('#contacts-add-input-firstName').val('john')
    $('#contacts-add-input-lastName').val ('doe')
    $('#contacts-add-input-tel').val      ('+1 888-555-1212')
    $('#contacts-add-input-email').val    ('john.doe@')
    $('#contacts-add-input-date').val     ('1990-10-10')
    CONTACTS.validation.addContactsFormValidator.form();
    strictEqual($('label.error').text(), "The email address must be in the format of name@company.domain.", 'The email was set to john.doe@.');
});

test('should display an error message in the email field when the email is not unique.', 1, function() {
    CONTACTS.validation.displayServerSideErrors("#contacts-add-form", {email:"That email is already used, please use a unique email"});
    strictEqual($('label.error').text(), "That email is already used, please use a unique email", 'The email was set to john.doe@abc.com.');
});

test('should continue to display an error message in the email field when the email is not unique.', 1, function() {
    $('#contacts-add-input-firstName').val('john')
    $('#contacts-add-input-lastName').val ('doe')
    $('#contacts-add-input-tel').val      ('+1 888-555-1212')
    $('#contacts-add-input-email').val    ('john.doe@abc.com')
    $('#contacts-add-input-date').val     ('1990-10-10')
    CONTACTS.validation.displayServerSideErrors("#contacts-add-form", {email:"That email is already used, please use a unique email"});
    CONTACTS.validation.formEmail = 'john.doe@abc.com';
    CONTACTS.validation.addContactsFormValidator.form();
    strictEqual($('label.error').text(), "That email is already used, please use a unique email.", 'The email was set to john.doe@abc.com.');
    CONTACTS.validation.formEmail = null;
});

// Birthdate tests
test('should display an error message in the birth date field when the birth date is not entered.', 1, function() {
    $('#contacts-add-input-firstName').val('john')
    $('#contacts-add-input-lastName').val ('doe')
    $('#contacts-add-input-tel').val      ('+1 888-555-1212')
    $('#contacts-add-input-email').val    ('john.doe@abc.com')
    $('#contacts-add-input-date').val     ('')
    CONTACTS.validation.addContactsFormValidator.form();
    strictEqual($('label.error').text(), "Please enter a valid birthdate.", 'The date was left empty.');
});

test('should display an error message in the birth date field when the birth date is given as a future date.', 1, function() {
    $('#contacts-add-input-firstName').val('john')
    $('#contacts-add-input-lastName').val ('doe')
    $('#contacts-add-input-tel').val      ('+1 888-555-1212')
    $('#contacts-add-input-email').val    ('john.doe@abc.com')
    $('#contacts-add-input-date').val     ('2020-10-10')
    $('.birthDate').attr('max', function() {
        return CONTACTS.util.getCurrentDate();
    });
    CONTACTS.validation.addContactsFormValidator.form();
    strictEqual($('label.error').text(), "Birthdates can not be in the future. Please choose one from the past. Unless they are a time traveler.", 'The date was set to 2020-10-10.');
});

test('should display an error message in the birth date field when the birth date is given as a date older then 1900.', 1, function() {
    $('#contacts-add-input-firstName').val('john')
    $('#contacts-add-input-lastName').val ('doe')
    $('#contacts-add-input-tel').val      ('+1 888-555-1212')
    $('#contacts-add-input-email').val    ('john.doe@abc.com')
    $('#contacts-add-input-date').val     ('1890-10-10')
    CONTACTS.validation.addContactsFormValidator.form();
    strictEqual($('label.error').text(), "Nobody is that old. Unless they are a vampire.", 'The date was set to 1890-10-10.');
});

test('should display an error message in the birth date field when the birth date is not in the format of yyyy/mm/dd or yyyy-mm-dd.', 1, function() {
    $('#contacts-add-input-firstName').val('john')
    $('#contacts-add-input-lastName').val ('doe')
    $('#contacts-add-input-tel').val      ('+1 888-555-1212')
    $('#contacts-add-input-email').val    ('john.doe@abc.com')
    $('#contacts-add-input-date').val     ('Oct 10, 1990')
    CONTACTS.validation.addContactsFormValidator.form();
    strictEqual($('label.error').text(), "Only valid date formats like yyyy-mm-dd. (hint: There are only 12 months and at most 31 days.)", 'The date was set to Oct 10, 1990.');
});


module('Validation in the Edit form', {
    setup: function() {
        // run before

        var $fixture = $( "#qunit-fixture" );

        $fixture.append(
            '<form name="contacts-edit-form" id="contacts-edit-form" class="contact_info" method="post" data-ajax="false">' +
                '<div>' +
                    '<label for="contacts-edit-input-firstName">First Name:</label>' +
                    '<input name="firstName" id="contacts-edit-input-firstName" class="name" data-clear-btn="true" value="" placeholder="Enter your first name." type="text"/>' +
                '</div>' +
                '<div>' +
                    '<label for="contacts-edit-input-lastName">Last Name:</label>' +
                    '<input name="lastName" id="contacts-edit-input-lastName" class="name" data-clear-btn="true" value="" placeholder="Enter your last name." type="text"/>' +
                '</div>' +
                '<div>' +
                    '<label for="contacts-edit-input-tel">Phone:</label>' +
                    '<input name="phoneNumber" id="contacts-edit-input-tel" class="phoneNumber" data-clear-btn="true" value="" type="tel"/>' +
                '</div>' +
                '<div>' +
                    '<label for="contacts-edit-input-email">Email:</label>' +
                    '<input name="email" id="contacts-edit-input-email" class="email" data-clear-btn="true" value="" type="email"/>' +
                '</div>' +
                '<div>' +
                    '<label for="contacts-edit-input-date">Birth Date:</label>' +
                    '<input name="birthDate" id="contacts-edit-input-date" class="birthDate" data-clear-btn="true" value="" type="date" min="1900-01-01"/>' +
                '</div>' +
                '<input id="submit-edit-btn" data-inline="true" type="submit" value="Save" />' +
                '<input id="reset-edit-btn" data-inline="true" type="reset" value="Reset" data-theme="c" />' +
                '<input id="cancel-edit-btn" data-inline="true" type="reset" value="Cancel" data-theme="c" />' +
            '</form>'
        );
        CONTACTS.validation.runFormValidators();
    },
    teardown: function() {
        // run after
    }
});

// First name tests
test('should display an error message in the first name field when the first name is not entered.', 1, function() {
    $('#contacts-edit-input-firstName').val('')
    $('#contacts-edit-input-lastName').val ('doe')
    $('#contacts-edit-input-tel').val      ('+1 888-555-1212')
    $('#contacts-edit-input-email').val    ('john.doe@abc.com')
    $('#contacts-edit-input-date').val     ('1990-10-10')
    CONTACTS.validation.editContactsFormValidator.form();
    strictEqual($('label.error').text(), "Please specify a first name.", 'The first name was left empty.');
});

test('should display an error message in the first name field when the first name is not well formed.', 1, function() {
    $('#contacts-edit-input-firstName').val('john3')
    $('#contacts-edit-input-lastName').val ('doe')
    $('#contacts-edit-input-tel').val      ('+1 888-555-1212')
    $('#contacts-edit-input-email').val    ('john.doe@abc.com')
    $('#contacts-edit-input-date').val     ('1990-10-10')
    CONTACTS.validation.editContactsFormValidator.form();
    strictEqual($('label.error').text(), "Please use a name without numbers or specials.", 'The first name was set to john3.');
});

test('should display an error message in the first name field when the first name is longer then 25 characters.', 1, function() {
    $('#contacts-edit-input-firstName').val('johnssssssssssssssssssssss')
    $('#contacts-edit-input-lastName').val ('doe')
    $('#contacts-edit-input-tel').val      ('+1 888-555-1212')
    $('#contacts-edit-input-email').val    ('john.doe@abc.com')
    $('#contacts-edit-input-date').val     ('1990-10-10')
    CONTACTS.validation.editContactsFormValidator.form();
    strictEqual($('label.error').text(), "Please enter no more than 25 characters.", 'The first name was set to johnssssssssssssssssssssss.');
});

// Last name tests
test('should display an error message in the last name field when the last name is not entered.', 1, function() {
    $('#contacts-edit-input-firstName').val('john')
    $('#contacts-edit-input-lastName').val ('')
    $('#contacts-edit-input-tel').val      ('+1 888-555-1212')
    $('#contacts-edit-input-email').val    ('john.doe@abc.com')
    $('#contacts-edit-input-date').val     ('1990-10-10')
    CONTACTS.validation.editContactsFormValidator.form();
    strictEqual($('label.error').text(), "Please specify a last name.", 'The last name was left empty.');
});

test('should display an error message in the last name field when the last name is not well formed.', 1, function() {
    $('#contacts-edit-input-firstName').val('john')
    $('#contacts-edit-input-lastName').val ('doe3')
    $('#contacts-edit-input-tel').val      ('+1 888-555-1212')
    $('#contacts-edit-input-email').val    ('john.doe@abc.com')
    $('#contacts-edit-input-date').val     ('1990-10-10')
    CONTACTS.validation.editContactsFormValidator.form();
    strictEqual($('label.error').text(), "Please use a name without numbers or specials.", 'The last name was set to doe3.');
});

test('should display an error message in the last name field when the last name is longer then 25 characters.', 1, function() {
    $('#contacts-edit-input-firstName').val('john')
    $('#contacts-edit-input-lastName').val ('doesssssssssssssssssssssss')
    $('#contacts-edit-input-tel').val      ('+1 888-555-1212')
    $('#contacts-edit-input-email').val    ('john.doe@abc.com')
    $('#contacts-edit-input-date').val     ('1990-10-10')
    CONTACTS.validation.editContactsFormValidator.form();
    strictEqual($('label.error').text(), "Please enter no more than 25 characters.", 'The last name was set to doesssssssssssssssssssssss.');
});

// Phone number tests
test('should display an error message in the phone number field when the phone number is not entered.', 1, function() {
    $('#contacts-edit-input-firstName').val('john')
    $('#contacts-edit-input-lastName').val ('doe')
    $('#contacts-edit-input-tel').val      ('')
    $('#contacts-edit-input-email').val    ('john.doe@abc.com')
    $('#contacts-edit-input-date').val     ('1990-10-10')
    CONTACTS.validation.editContactsFormValidator.form();
    strictEqual($('label.error').text(), "Please enter a phone number.", 'The phone number was left empty.');
});

test('should display an error message in the phone number field when the phone number has anything other then digits.', 1, function() {
    $('#contacts-edit-input-firstName').val('john')
    $('#contacts-edit-input-lastName').val ('doe')
    $('#contacts-edit-input-tel').val      ('555-1212')
    $('#contacts-edit-input-email').val    ('john.doe@abc.com')
    $('#contacts-edit-input-date').val     ('1990-10-10')
    CONTACTS.validation.editContactsFormValidator.form();
    strictEqual($('label.error').text(), "Please use a valid phone number with county code.", 'The phone number was set to 555-1212.');
});

// Email tests
test('should display an error message in the email field when the email is not entered.', 1, function() {
    $('#contacts-edit-input-firstName').val('john')
    $('#contacts-edit-input-lastName').val ('doe')
    $('#contacts-edit-input-tel').val      ('+1 888-555-1212')
    $('#contacts-edit-input-email').val    ('')
    $('#contacts-edit-input-date').val     ('1990-10-10')
    CONTACTS.validation.editContactsFormValidator.form();
    strictEqual($('label.error').text(), "Please enter an e-mail.", 'The email was left empty.');
});

test('should display an error message in the email field when the email is not in the form of name@company.domain.', 1, function() {
    $('#contacts-edit-input-firstName').val('john')
    $('#contacts-edit-input-lastName').val ('doe')
    $('#contacts-edit-input-tel').val      ('+1 888-555-1212')
    $('#contacts-edit-input-email').val    ('john.doe@')
    $('#contacts-edit-input-date').val     ('1990-10-10')
    CONTACTS.validation.editContactsFormValidator.form();
    strictEqual($('label.error').text(), "The email address must be in the format of name@company.domain.", 'The email was set to john.doe@.');
});

test('should display an error message in the email field when the email is not unique.', 1, function() {
    CONTACTS.validation.displayServerSideErrors("#contacts-edit-form", {email:"That email is already used, please use a unique email"});
    strictEqual($('label.error').text(), "That email is already used, please use a unique email", 'The email was set to john.doe@abc.com.');
});

test('should continue to display an error message in the email field when the email is not unique.', 1, function() {
    $('#contacts-edit-input-firstName').val('john')
    $('#contacts-edit-input-lastName').val ('doe')
    $('#contacts-edit-input-tel').val      ('+1 888-555-1212')
    $('#contacts-edit-input-email').val    ('john.doe@abc.com')
    $('#contacts-edit-input-date').val     ('1990-10-10')
    CONTACTS.validation.displayServerSideErrors("#contacts-edit-form", {email:"That email is already used, please use a unique email"});
    CONTACTS.validation.formEmail = 'john.doe@abc.com';
    CONTACTS.validation.editContactsFormValidator.form();
    strictEqual($('label.error').text(), "That email is already used, please use a unique email.", 'The email was set to john.doe@abc.com.');
    CONTACTS.validation.formEmail = null;
});

// Birthdate tests
test('should display an error message in the birth date field when the birth date is not entered.', 1, function() {
    $('#contacts-edit-input-firstName').val('john')
    $('#contacts-edit-input-lastName').val ('doe')
    $('#contacts-edit-input-tel').val      ('+1 888-555-1212')
    $('#contacts-edit-input-email').val    ('john.doe@abc.com')
    $('#contacts-edit-input-date').val     ('')
    CONTACTS.validation.editContactsFormValidator.form();
    strictEqual($('label.error').text(), "Please enter a valid birthdate.", 'The date was left empty.');
});

test('should display an error message in the birth date field when the birth date is given as a future date.', 1, function() {
    $('#contacts-edit-input-firstName').val('john')
    $('#contacts-edit-input-lastName').val ('doe')
    $('#contacts-edit-input-tel').val      ('+1 888-555-1212')
    $('#contacts-edit-input-email').val    ('john.doe@abc.com')
    $('#contacts-edit-input-date').val     ('2020-10-10')
    $('.birthDate').attr('max', function() {
        return CONTACTS.util.getCurrentDate();
    });
    CONTACTS.validation.editContactsFormValidator.form();
    strictEqual($('label.error').text(), "Birthdates can not be in the future. Please choose one from the past. Unless they are a time traveler.", 'The date was set to 2020-10-10.');
});

test('should display an error message in the birth date field when the birth date is given as a date older then 1900.', 1, function() {
    $('#contacts-edit-input-firstName').val('john')
    $('#contacts-edit-input-lastName').val ('doe')
    $('#contacts-edit-input-tel').val      ('+1 888-555-1212')
    $('#contacts-edit-input-email').val    ('john.doe@abc.com')
    $('#contacts-edit-input-date').val     ('1890-10-10')
    CONTACTS.validation.editContactsFormValidator.form();
    strictEqual($('label.error').text(), "Nobody is that old. Unless they are a vampire.", 'The date was set to 1890-10-10.');
});

test('should display an error message in the birth date field when the birth date is not in the format of yyyy/mm/dd or yyyy-mm-dd.', 1, function() {
    $('#contacts-edit-input-firstName').val('john')
    $('#contacts-edit-input-lastName').val ('doe')
    $('#contacts-edit-input-tel').val      ('+1 888-555-1212')
    $('#contacts-edit-input-email').val    ('john.doe@abc.com')
    $('#contacts-edit-input-date').val     ('Oct 10, 1990')
    CONTACTS.validation.editContactsFormValidator.form();
    strictEqual($('label.error').text(), "Only valid date formats like yyyy-mm-dd. (hint: There are only 12 months and at most 31 days.)", 'The date was set to Oct 10, 1990.');
});

//test('', 1, function() {
//    ok(true,"TODO");
//});

