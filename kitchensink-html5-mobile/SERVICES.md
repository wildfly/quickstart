kitchensink-html5-mobile: JAX-RS Endpoint Documentation 
=======================================================
Author: Jay Balunas

This example supports various RESTFul end points which also includes JSONP support for cross domain requests. 

MemberService End Points
------------------------

### List all members
#### /rest/members

* Request type: GET
* Return type: XML
* Response example:

```xml
<?xml version="1.0" encoding="UTF-8"standalone="yes"?>
<collection>
    <member>
        <email>jane.smith@mailinator.com</email>
        <id>1</id>
        <name>Jane Smith</name>
        <phoneNumber>2125551212</phoneNumber>
    </member>
    <member>
        <email>john.smith@mailinator.com</email>
        <id>0</id>
        <name>John Smith</name>
        <phoneNumber>2125551212</phoneNumber>
    </member>
</collection>
```

#### /rest/members/json

* Request type: GET
* Return type: JSON
* Response example:

```javascript
[{"id":1,"name":"Jane Smith","email":"jane.smith@mailinator.com","phoneNumber":"2125551212"},{"id":0,"name":"John Smith","email":"john.smith@mailinator.com","phoneNumber":"2125551212"}]
```

### Create a new members

#### /rest/members

* Request type: POST
* Request type: application/x-www-form-urlencoded
* Request parameters: name, email, phoneNumber
* Return type: JSON
* Response example (if validation error)
 * Collection of `<field name>:<error msg>` for each error

```JavaScript
{"email":"Email taken"}
```

#### /rest/members/new?name=&email=&phoneNumber=
* Request type: GET
* Query parameters: name, email, phoneNumber
* Return type: JSON
* Response example: See POST example above

### Get one member by ID

#### /rest/members/\<id>
* Request type: GET
* Return type: XML
* Response example:

```xml
<?xml version="1.0" encoding="UTF-8"standalone="yes"?>
<member>
    <email>john.smith@mailinator.com</email>
    <id>0</id>
    <name>John Smith</name>
    <phoneNumber>2125551212</phoneNumber>
</member>
```

#### /rest/members/\<id>/json
* Request type: GET
* Return type: JSON
* Response example:

```javascript
{"id":0,"name":"John Smith","email":"john.smith@mailinator.com","phoneNumber":"2125551212"}
```

JSONP Support
-------------

[JSONP](http://en.wikipedia.org/wiki/JSONP) is a technique for allowing cross domain requests from services.  It pads (what the "P" stands for) the service response with a JavaScript callback function that can then access the data on the client.

In order for a request to be padded the following things must be true:

* The request is one of the GET JSON request from above
 * POST requests are not support for JSONP requests
* A query parameter named *jsonpcallback* is defined
 * The value of this parameter is the method that will be defined in the padding

For example:

  http://\<domain>/rest/members/json?jsonpcallback=mycallback
  
See jQuery's documentation for client access examples:

* http://api.jquery.com/jQuery.getJSON/
* http://api.jquery.com/jQuery.ajax/
