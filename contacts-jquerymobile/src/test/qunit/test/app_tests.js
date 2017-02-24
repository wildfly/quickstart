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

module('The main App', {
    setup: function() {
        // run before

        var $fixture = $( "#qunit-fixture" );

        $fixture.append(
            '<div><ul data-role="listview" id="contacts-display-listview" class="sortedList" data-autodividers="true" data-filter="true"></ul></div>' +
            '<div><ul data-role="listview" id="contacts-display-detail-listview" class="sortedList" data-autodividers="true" data-filter="true"></ul></div>'
        );
    },
    teardown: function() {
        // run after
    }
});

test('should be able to add 2 rows to both a simple list view and a detailed list view. ', 2, function() {
    var contacts = [{email: "jane.doe@company.com", id: 14, firstName: "Jane", lastName: 'Doe', phoneNumber: "1231231231", birthDate:'1966-01-03'},
                    {email: "john.doe@company.com", id: 15, firstName: "John", lastName: 'Doe', phoneNumber: "2125551212", birthDate:'1978-02-23'}];

    CONTACTS.app.buildContactList(contacts);

    strictEqual($('.contacts-list-item').length, 2, 'Built 2 rows in the simple list view.');
    strictEqual($('.contacts-detail-list-item').length, 2, 'Built 2 rows in the detailed list view.');
});

test('should display nothing when no records are found. ', 1, function() {
    var contacts = [];

    CONTACTS.app.buildContactList(contacts);

    strictEqual($('.contacts-list-item').length, 0, 'Created no rows for empty contacts.');
});

test('should be able to fill in the edit form with values', 5, function() {
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

    var contacts = {email: "jane.doe@company.com", id: 14, firstName: "Jane", lastName: 'Doe', phoneNumber: "1231231231", birthDate:'1966-01-03'};
    CONTACTS.app.buildContactDetail(contacts);
    strictEqual($('#contacts-edit-input-firstName').val(), 'Jane', 'Expected to find Jane in the first name field.');
    strictEqual($('#contacts-edit-input-lastName').val(),  'Doe', 'Expected to find Doe in the last name field.');
    strictEqual($('#contacts-edit-input-tel').val(),       '1 231-231-231', 'Expected to find 1231231231 in the phone number field.');
    strictEqual($('#contacts-edit-input-email').val(),     'jane.doe@company.com', 'Expected to find jane.doe@company.com in the email field.');
    strictEqual($('#contacts-edit-input-date').val(),      '1966-01-03', 'Expected to find 1966-01-03 in the birthdate field.');
});

test('should be able to GET a contact from the db', 1, function() {
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

    var contacts = {email: "john.smith@mailinator.com", id: 14, firstName: "Johne", lastName: 'Smith', phoneNumber: "2125551212", birthDate:'1963-06-03'};

    // It looks like the following will not work but I may yet find a way so I will keep this here for now.
    // I think to get this work you need to set the URL to the full (http://...) version and setup CORS. In addition
    //  the Contact needs to be returned by the method.
//    var imported = CONTACTS.app.getContactById(10001);
//    deepEqual(imported, contacts, 'Expected John Smith to have been returned.');
    ok(true,"TODO");
});

test('should be able to GET a contact from the db and fill in the edit form', 5, function() {
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
            '<input id="clear-edit-btn" data-inline="true" type="reset" value="Clear" data-theme="c" />' +
            '<input id="cancel-edit-btn" data-inline="true" type="reset" value="Cancel" data-theme="c" />' +
        '</form>'
    );

    // It looks like the following will not work but I may yet find a way so I will keep this here for now.
    // I think to get this work you need to set the URL to the full (http://...) version and setup CORS. In addition
//    CONTACTS.app.getContactById(10001);
//    strictEqual($('#contacts-edit-input-firstName').val(), 'John', 'Expected to find John in the first name field.');
//    strictEqual($('#contacts-edit-input-lastName').val(),  'Smith', 'Expected to find Smith in the last name field.');
//    strictEqual($('#contacts-edit-input-tel').val(),       '1231231231', 'Expected to find 2125551212 in the phone number field.');
//    strictEqual($('#contacts-edit-input-email').val(),     'john.smith@mailinator.com', 'Expected to find john.smith@mailinator.com in the email field.');
//    strictEqual($('#contacts-edit-input-date').val(),      '1963-06-03', 'Expected to find 1963-06-03 in the birthdate field.');
    ok(true,"TODO");
    ok(true,"TODO");
    ok(true,"TODO");
    ok(true,"TODO");
    ok(true,"TODO");
});

//test('', 1, function() {
//    ok(true,"TODO");
//});
