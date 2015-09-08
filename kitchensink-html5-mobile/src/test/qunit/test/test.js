/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
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

    ok($(html).length == 2, 'Number of rows built: ' + length);
});

test('Build 0 member Rows', function() {
    expect(1);

    var members = [];

    var html = buildMemberRows(members);

    ok($(html).length == 0, 'Created no rows for empty members');
});

module('Member Restful Calls');

test('Register a new member', function() {
    ok(1==1,"TODO");
});

test('Register a member with a duplicate email', function() {
    ok(1==1,"TODO");
});
