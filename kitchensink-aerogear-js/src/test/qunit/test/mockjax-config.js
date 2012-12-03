(function( $ ) {
// Use mockjax to intercept the rest calls and return data to the tests
// Clean up any mocks from previous tests first
$.mockjaxClear();

// read mocks
$.mockjax({
    url: "rest/members",
    type: "GET",
    headers: {
        "Content-Type": "application/json"
    },

    responseText: [
        {
            id: 1,
            email: "jane.doe@company.com",
            name: "Jane Doe",
            phoneNumber: "12312312311"
        }
    ]
});

})( jQuery );
