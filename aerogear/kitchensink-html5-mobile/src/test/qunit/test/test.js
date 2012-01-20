/*
Unit tests that cover basic functionality of app.js.
 */

module('Member Row Construction');

test('Build 2 Member Rows', function() {
   expect(1);

   var members = [{"email": "jane.doe@company.com", "id": 1, "name": "Jane Doe", "phoneNumber": "12312312311"},{"email": "john.doe@company.com", "id": 0, "name": "John Doe", "phoneNumber": "2125551212"}];

   var html = buildMemberRows(members);
   var length = $(html).length;
   ok(length == 2, 'Number of rows built: ' + length);
});

test('Build 0 member Rows', function() {
  expect(1);

   var members = [];

   var html = buildMemberRows(members);

   ok(html == '', 'Created no rows for empty members');
});

module('Member Restful Calls');

test('Request current member list', function() {
  ok(1==1,"TODO");
});

test('Register a new member', function() {
  ok(1==1,"TODO");
});

test('Register a member with a duplicate email', function() {
  ok(1==1,"TODO");
});