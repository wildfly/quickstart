/*
Core JavaScript functionality for the application.  Performs the required
Restful calls, validates return values, and populates the member table.
 */

/* Get the member template */
function getMemberTemplate() {
	$.ajax({
        url: "resources/tmpl/member.tmpl",
        dataType: "html",
        success: function( data ) {
            $( "head" ).append( data );
            updateMemberTable();
        }
    });
}

/* Builds the updated table for the member list */
function buildMemberRows(members) {
	return _.template( $( "#member-tmpl" ).html(), {"members": members});
}

/* Uses JAX-RS GET to retrieve current member list */
function updateMemberTable() {
   $.ajax({
	   url: "rest/members/json",
	   cache: false,
	   success: function(data) {
            $('#members').empty().append(buildMemberRows(data));
       },
       error: function(error) {
            //console.log("error updating table -" + error.status);
       }
   });
}

/*
Attempts to register a new member using a JAX-RS POST.  The callbacks
the refresh the member table, or process JAX-RS response codes to update
the validation errors.
 */
function registerMember(formValues) {
   //clear existing  msgs
   $('span.invalid').remove();
   $('span.success').remove();

   $.post('rest/members', formValues,
         function(data) {
            //console.log("Member registered");

            //clear input fields
            $('#reg')[0].reset();

            //mark success on the registration form
            $('#formMsgs').append($('<span class="success">Member Registered</span>'));

            updateMemberTable();
         }).error(function(error) {
            if ((error.status == 409) || (error.status == 400)) {
               //console.log("Validation error registering user!");

               var errorMsg = JSON.parse(error.responseText);

               $.each(errorMsg, function(index, val){
                  $('<span class="invalid">' + val + '</span>')
                        .insertAfter($('#' + index));
               });
            } else {
               //console.log("error - unknown server issue");
               $('#formMsgs').append($('<span class="invalid">Unknown server error</span>'));
            }
         });
}