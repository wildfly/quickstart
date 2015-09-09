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
/*
Unit tests that cover basic functionality of app.js.
 */

module('Member Row Construction');

test('Build 2 Member Rows', function() {
    expect(1);

    var members = [{"email": "jane.doe@company.com", "id": 1, "name": "Jane Doe", "phoneNumber": "12312312311"},{"email": "john.doe@company.com", "id": 0, "name": "John Doe", "phoneNumber": "2125551212"}];

    var html = buildMemberRows(members);

    ok($(html).length == 2, 'Number of rows built: ' + $(html).length);
});

test('Build 0 member Rows', function() {
    expect(1);

    var members = [];

    var html = buildMemberRows(members);

    ok($(html).length == 0, 'Created no rows for empty members');
});

module('Member Restful Calls');

asyncTest('Register a new member', function() {
    expect(2);

    // Create necessary DOM elements
    $('<form name="reg" id="reg"><div id="formMsgs"></div></form>').appendTo('#qunit-fixture');

    // Override jQuery's ajax function to capture the data sent by the client
    var options = null;
    $.ajax = function(param) {
        if(param.data) {
            options = param;
        }
    };

    // Invoke the function to test
    var memberData = {"email": "john.doe@company.com", "name": "John Doe", "phoneNumber": "2125551212"};
    registerMember(memberData);

    // Invoke the success callback to simulate successful registration
    options.success({"email": "john.doe@company.com", "id": 0, "name": "John Doe", "phoneNumber": "2125551212"});

    setTimeout(function() {
        // Execute assertions
        equal(options.data, JSON.stringify(memberData));
        equal($('#formMsgs').html(), '<span class="success">Member Registered</span>');
        start();
    }, 1000);
});

asyncTest('Register a member with a duplicate email', function() {
    expect(2);

    // Create necessary DOM elements
    $('<form name="reg" id="reg">' +
        '   <div class="ui-field-contain">' +
        '       <label for="email">Email:</label>' +
        '       <input type="email" name="email" id="email" placeholder="Your Email" required/>' +
        '   </div>' +
        '   <div id="formMsgs"></div>' +
        '</form>').appendTo('#qunit-fixture');

    // Override jQuery's ajax function to capture the data sent by the client
    var options = null;
    $.ajax = function(param) {
        if(param.data) {
            options = param;
        }
    };

    // Invoke the function to test
    var memberData = {"email": "john.doe@company.com", "name": "John Doe", "phoneNumber": "2125551212"};
    registerMember(memberData);

    // Invoke the failure callback to simulate a registration failure due to duplicate email
    options.error({"status": 409, "responseText": "{\"email\":\"Email taken\"}" });

    setTimeout(function() {
        // Execute assertions
        equal(options.data, JSON.stringify(memberData));
        equal($('form span').html(), 'Email taken');
        start();
    }, 1000);
});
