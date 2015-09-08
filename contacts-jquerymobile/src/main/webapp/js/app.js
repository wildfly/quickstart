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
CONTACTS.namespace('CONTACTS.app.getContacts');
CONTACTS.namespace('CONTACTS.app.buildContactList');
CONTACTS.namespace('CONTACTS.app.getContactById');
CONTACTS.namespace('CONTACTS.app.buildContactDetail');
CONTACTS.namespace('CONTACTS.app.restEndpoint');

CONTACTS.app.restEndpoint = 'rest/contacts';

/**
 * It is recommended to bind to this event instead of DOM ready() because this will work regardless of whether
 * the page is loaded directly or if the content is pulled into another page as part of the Ajax navigation system.
 *
 * The first thing you learn in jQuery is to call code inside the $(document).ready() function so everything
 * will execute as soon as the DOM is loaded. However, in jQuery Mobile, Ajax is used to load the contents of
 * each page into the DOM as you navigate, and the DOM ready handler only executes for the first page.
 * To execute code whenever a new page is loaded and created, you can bind to the pagecreate event.
 *
 *
 * These functions perform the GET. They display the list, detailed list, and fill in the update form.
 *
 * @author Joshua Wilson
 */
$( document ).on( "pagecreate", function(mainEvent) {
    //Initialize the vars in the beginning so that you will always have access to them.
    var getCurrentTime = CONTACTS.util.getCurrentTime,
        restEndpoint = CONTACTS.app.restEndpoint;

    console.log(getCurrentTime() + " [js/app.js] (document -> pagecreate) - start");

    /*
     * Make sure the Contacts list gets populated but only once.
     *
     * The "pagebeforeshow" event will delay this function until everything is set up.
     *
     * Because of the interesting jQM loading architecture, multiple event triggering is a constant problem.
     * The "e.handled" if statement used here and elsewhere is meant to keep jQM from running this code multiple
     * times for one display.
     */
    $('#contacts-list-page').on( "pagebeforeshow", function(e) {
        if(e.handled !== true) {
            console.log(getCurrentTime() + " [js/app.js] (#contacts-list-page -> pagebeforeshow) - start");

            // Fetches the initial Contact data.
            CONTACTS.app.getContacts();

            e.handled = true;
            console.log(getCurrentTime() + " [js/app.js] (#contacts-list-page -> pagebeforeshow) - end");
        }
    });

    // This is called on 'pagebeforeshow' above and by the CONTACTS.submissions
    // Uses JAX-RS GET to retrieve current contact list.
    CONTACTS.app.getContacts = function () {
        console.log(getCurrentTime() + " [js/app.js] (getContacts) - start");
        var jqxhr = $.ajax({
            url: restEndpoint,
            cache: false,
            type: "GET"
        }).done(function(data, textStatus, jqXHR) {
            console.log(getCurrentTime() + " [js/app.js] (getContacts) - succes on ajax call");
            CONTACTS.app.buildContactList(data);
        }).fail(function(jqXHR, textStatus, errorThrown) {
            console.log(getCurrentTime() + " [js/app.js] (getContacts) - error in ajax - " +
                        " - jqXHR = " + jqXHR.status +
                        " - textStatus = " + textStatus +
                        " - errorThrown = " + errorThrown);
        });
        console.log(getCurrentTime() + " [js/app.js] (getContacts) - end");
    };

    // This is called by CONTACTS.app.getContacts.
    // Display contact list on page one.
    CONTACTS.app.buildContactList = function (contacts) {
        console.log(getCurrentTime() + " [js/app.js] (buildContactList) - start");
        var contactList = "",
            contactDetailList = "";

        // The data from the AJAX call is not sorted alphabetically, this will fix that.
        contacts.sort(function(a,b){
              var aName = a.firstName.toLowerCase() + a.lastName.toLowerCase();
              var bName = b.firstName.toLowerCase() + b.lastName.toLowerCase();
              return ((aName < bName) ? -1 : ((aName > bName) ? 1 : 0));
        });

        // Pull the info out of the Data returned from the AJAX request and create the HTML to be placed on the page.
        $.each(contacts, function(index, contact) {
            // Create the HTML for the List only view.
            contactList = contactList.concat(
                "<li id=list-contact-ID-" + contact.id.toString() + " class=contacts-list-item >" +
                    "<a href='#contacts-edit-page' >" + contact.firstName.toString() + " " + contact.lastName.toString() + "</a>" +
                "</li>");
            // Create the HTML for the Detailed List view.
            contactDetailList = contactDetailList.concat(
                "<li id=detail-contact-ID-" + contact.id.toString() + " class=contacts-detail-list-item >" +
                    "<a href='#contacts-edit-page' >" + contact.firstName.toString() + " " + contact.lastName.toString() + "</a>" +
                    "<div class='detialedList'>" +
                        "<p><strong>" + contact.email.toString() + "</strong></p>" +
                        "<p>" + contact.phoneNumber.toString() + "</p>" +
                        "<p>" + contact.birthDate.toString() + "</p>" +
                    "</div>" +
                 "</li>");
        });

        // Start with a clean list element otherwise we would have repeats.
        $('#contacts-display-listview').empty();

        // Check if it is already initialized or not, refresh the list in case it is initialized otherwise trigger create.
        if ( $('#contacts-display-listview').hasClass('ui-listview')) {
            console.log(getCurrentTime() + " [js/app.js] (#contacts-display-listview - hasClass ui-listview) - append.listview - start");
            $('#contacts-display-listview').append(contactList).listview("refresh", true);
            console.log(getCurrentTime() + " [js/app.js] (#contacts-display-listview - hasClass ui-listview) - append.listview - end");
        }
        else {
            console.log(getCurrentTime() + " [js/app.js] (#contacts-display-listview - !hasClass ui-listview) - append.trigger - start");
            $('#contacts-display-listview').append(contactList).enhanceWithin();
            console.log(getCurrentTime() + " [js/app.js] (#contacts-display-listview - !hasClass ui-listview) - append.trigger - end");
        }

        // Start with a clean list element otherwise we would have repeats.
        $('#contacts-display-detail-listview').empty();

        // check if it is already initialized or not, refresh the list in case it is initialized otherwise trigger create
        if ( $('#contacts-display-detail-listview').hasClass('ui-listview')) {
            console.log(getCurrentTime() + " [js/app.js] (#contacts-display-detail-listview - hasClass ui-listview) - append.listview - start");
            $('#contacts-display-detail-listview').append(contactDetailList).listview("refresh", true);
            console.log(getCurrentTime() + " [js/app.js] (#contacts-display-detail-listview - hasClass ui-listview) - append.listview - end");
        }
        else {
            console.log(getCurrentTime() + " [js/app.js] (#contacts-display-detail-listview - !hasClass ui-listview) - append.trigger - start");
            $('#contacts-display-detail-listview').append(contactDetailList).enhanceWithin();
            console.log(getCurrentTime() + " [js/app.js] (#contacts-display-detail-listview - !hasClass ui-listview) - append.trigger - end");
        }

        // Attach onclick event to each row of the contact list that will open up the contact info to be edited.
        $('.contacts-list-item').on("click", function(event){
            if(event.handled !== true) {
                console.log(getCurrentTime() + " [js/app.js] (.contacts-display-listview -> on click) - start");

                CONTACTS.app.getContactById($(this).attr("id").split("list-contact-ID-").pop());

                event.handled = true;
                console.log(getCurrentTime() + " [js/app.js] (.contacts-display-listview -> on click) - end");
            }
        });

        // Attach onclick event to each row of the contact list detailed page that will open up the contact info to be edited.
        $('li.contacts-detail-list-item').on("click", function(event){
            if(event.handled !== true) {
                console.log(getCurrentTime() + " [js/app.js] (li.contacts-display-listview -> on click) - start");

                CONTACTS.app.getContactById($(this).attr("id").split("detail-contact-ID-").pop());

                // Turn the whole <li> into a link.
                $("body").pagecontainer("change", "#contacts-edit-page");

                event.handled = true;
                console.log(getCurrentTime() + " [js/app.js] (li.contacts-display-listview -> on click) - end");
            }
        });

        console.log(getCurrentTime() + " [js/app.js] (buildContactList) - end");
        // Add in a line to visually see when we are done.
        console.log("-----------------------------List Page---------------------------------------");
    };

    // This is called by the on click event list above.
    // Retrieve employee detail based on employee id.
    CONTACTS.app.getContactById = function (contactID) {
        console.log(getCurrentTime() + " [js/app.js] (getContactById) - start");
        console.log(getCurrentTime() + " [js/app.js] (getContactById) - contactID = " + contactID);

        var jqxhr = $.ajax({
            url: restEndpoint + "/" + contactID.toString(),
            cache: false,
            type: "GET"
        }).done(function(data, textStatus, jqXHR) {
            console.log(getCurrentTime() + " [js/app.js] (getContactById) - success on ajax call");
            CONTACTS.app.buildContactDetail(data);
        }).fail(function(jqXHR, textStatus, errorThrown) {
            console.log(getCurrentTime() + " [js/app.js] (getContactById) - error in ajax" +
                        " - jqXHR = " + jqXHR.status +
                        " - textStatus = " + textStatus +
                        " - errorThrown = " + errorThrown);
        });
        console.log(getCurrentTime() + " [js/app.js] (getContactById) - end");
    };

    // This is called by CONTACTS.app.getContactById.
    // Display contact detail for editing on the Edit page.
    CONTACTS.app.buildContactDetail = function(contact) {
        console.log(getCurrentTime() + " [js/app.js] (buildContactDetail) - start");

        // The intl-Tel-Input plugin stores the lasted used country code and uses it to predetermin the flag to be
        // displayed. So we remove the plugin before the data gets loaded in the Edit form and then initialize it after.
        $("#contacts-edit-input-tel").intlTelInput("destroy");

        // Put each field value in the text input on the page.
        $('#contacts-edit-input-firstName').val(contact.firstName);
        $('#contacts-edit-input-lastName').val(contact.lastName);
        $('#contacts-edit-input-tel').val(contact.phoneNumber);
        $('#contacts-edit-input-email').val(contact.email);
        $('#contacts-edit-input-date').val(contact.birthDate);
        $('#contacts-edit-input-id').val(contact.id);

        // The intl-Tel-Input plugin needs to be initialized everytime the data gets loaded into the Edit form so that
        // it will correctly validate it and display the correct flag.
        $('#contacts-edit-input-tel').intlTelInput({nationalMode:false});

        console.log(getCurrentTime() + " [js/app.js] (buildContactDetail) - end");
        // Add in a line to visually see when we are done.
        console.log("-----------------------------Update Page---------------------------------------");
    };

    console.log(getCurrentTime() + " [js/app.js] (document -> pagecreate) - end");
});


