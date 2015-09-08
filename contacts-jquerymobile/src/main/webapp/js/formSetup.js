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
CONTACTS.namespace('CONTACTS.validation.displayServerSideErrors');
CONTACTS.namespace('CONTACTS.validation.validateName');
CONTACTS.namespace('CONTACTS.validation.formEmail');
CONTACTS.namespace('CONTACTS.validation.validateEmailUniqueness');
CONTACTS.namespace('CONTACTS.validation.addContactsFormValidator');
CONTACTS.namespace('CONTACTS.validation.editContactsFormValidator');

/**
 * Configure the HTML forms to hide the standard form buttons (they will be displayed in the footer). And to set up
 * the actions to take place when they are clicked.
 *
 * @author Joshua Wilson
 */
$( document ).on( "pagecreate", function(mainEvent) {
    //Initialize the vars in the beginning so that you will always have access to them.
    var getCurrentTime = CONTACTS.util.getCurrentTime;

    /*
     * The "pagebeforeshow" event will delay this function until everything is set up.
     *
     * The "e.handled" if statement used here and elsewhere is meant to keep jqm from running this code multiple times
     * for one display.
     */

    // Hide the actual form buttons so that we can use proxy buttons in the footer.
    $('#contacts-add-page').on( "pagebeforeshow", function(e) {
        if(e.handled !== true) {
            console.log(getCurrentTime() + " [js/formSetup.js] (#contacts-add-page -> pagebeforeshow) - start");

            $('#submit-add-btn').parent().hide();
            $('#clear-add-btn').parent().hide();
            $('#cancel-add-btn').parent().hide();

            e.handled = true;
            console.log(getCurrentTime() + " [js/formSetup.js] (#contacts-add-page -> pagebeforeshow) - end");
            // Add in a line to visually see when we are done.
            console.log("-----------------------------Create Page---------------------------------------");
        }
    });

    // Hide the actual form buttons so that we can use proxy buttons in the footer.
    $('#contacts-edit-page').on( "pagebeforeshow", function(e) {
        if(e.handled !== true) {
            console.log(getCurrentTime() + " [js/formSetup.js] (#contacts-edit-page -> pagebeforeshow) - start");

            $('#submit-edit-btn').parent().hide();
            $('#reset-edit-btn').parent().hide();
            $('#cancel-edit-btn').parent().hide();

            e.handled = true;
            console.log(getCurrentTime() + " [js/formSetup.js] (#contacts-edit-page -> pagebeforeshow) - end");
        }
    });

    // The Clear button will remove data and validation marks but leave you in the form.
    // The Cancel button will clear the form and return you to the main page.
    //   The Cancel button has a link on it and that is what differentiates them. Otherwise they both need to clear the form.
    $('#clear-add-btn, #cancel-add-btn').on("click", function(e) {
        if(e.handled !== true) {
            console.log(getCurrentTime() + " [js/formSetup.js] (#clear-add-btn, #cancel-add-btn -> on click) - start");

            // Remove errors display as a part of the validation system.
            CONTACTS.validation.addContactsFormValidator.resetForm();

            // Reset this flag when the form passes validation.
            if (this.id === "cancel-add-btn") {
                CONTACTS.validation.formEmail = null;
            }

            // Remove any errors that are not a part of the validation system.
            $('.invalid').remove();

            e.handled = true;
            console.log(getCurrentTime() + " [js/formSetup.js] (#clear-add-btn, #cancel-add-btn -> on click) - end");
        }
    });

    // This Reset button will remove unsaved changes and validation marks while leaving you in the form.
    $('#reset-edit-btn').on("click", function(e) {
        if(e.handled !== true) {
            console.log(getCurrentTime() + " [js/formSetup.js] (#reset-edit-btn -> on click) - start");

            // Get the contact ID before we wipe the form.
            var contactID = $('input#contacts-edit-input-id').attr('value');

            // Remove errors display as a part of the validation system.
            CONTACTS.validation.editContactsFormValidator.resetForm();

            // Remove any errors that are not a part of the validation system.
            $('.invalid').remove();

            // Since we are only "reseting" the form we need to put back the original data.
            CONTACTS.app.getContactById(contactID);

            e.handled = true;
            console.log(getCurrentTime() + " [js/formSetup.js] (#reset-edit-btn -> on click) - end");
        }
    });

    // This Cancel button will clear the form and return you to the main page.
    $('#cancel-edit-btn').on("click", function(e) {
        if(e.handled !== true) {
            console.log(getCurrentTime() + " [js/formSetup.js] (#cancel-edit-btn -> on click) - start");

            // Remove errors display as a part of the validation system.
//            editValidator.resetForm();
            CONTACTS.validation.editContactsFormValidator.resetForm();

            // Reset this flag when the form passes validation.
            CONTACTS.validation.formEmail = null;

            // Remove any errors that are not a part of the validation system.
            $('.invalid').remove();

            e.handled = true;
            console.log(getCurrentTime() + " [js/formSetup.js] (#cancel-edit-btn -> on click) - end");
        }
    });
});

