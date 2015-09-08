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
CONTACTS.namespace('CONTACTS.validation.validateBirthDate');
CONTACTS.namespace('CONTACTS.validation.validatePhone');
CONTACTS.namespace('CONTACTS.validation.validateName');
CONTACTS.namespace('CONTACTS.validation.formEmail');
CONTACTS.namespace('CONTACTS.validation.validateEmailUniqueness');
CONTACTS.namespace('CONTACTS.validation.runFormValidators');
CONTACTS.namespace('CONTACTS.validation.addContactsFormValidator');
CONTACTS.namespace('CONTACTS.validation.editContactsFormValidator');

/**
 * jQuery Mobile and moblie devices do not currently support HTML5 form validation.  Therefore, basic things like
 * checking if a value had only Alpha characters would either need to be done on the server or with some custom JavaScript.
 *
 * I think it is best to validate the UI in the UI whenever possible so that you don't keep going back and forth
 * over the network doing validation.  That said, there are two basic options when it comes to validating fields
 * in JavaScript: You role your own or use a library.  We are using the jQuery Validator library.  It is one of the
 * oldest plugins in jQuery history and is still actively supported.  As you will see below it works very nicely.
 *
 * If jQuery Mobile ever gets HTML5 form validation to work then we should consider using that instead or in addition
 * to the Validation plugin.
 *
 * These functions control the form validation.
 *
 * @author Joshua Wilson
 */
$(document).ready(function() {
    //Initialize the vars in the beginning so that you will always have access to them.
    var getCurrentTime = CONTACTS.util.getCurrentTime;

    /**
     * We try to catch the format errors while on the client but some things require a database lookup.  There is
     * exception handling on the server side and the exceptions send back the errors through the form AJAX call. In
     * the submit handler we catch those errors thrown by the server.  They are in turn sent to this method, which
     * will display them in the appropriate field(s).
     *
     * This is only displayed on submit.  If you want to use this validation during the form edits you will need to
     * add that to the validation process, just as it has been done for the email uniqueness test.
     *
     * @param pageID = the page the error will be displayed on
     * @param errorMsg = a JS object in JSON with the field name and the message to be displayed
     */
    CONTACTS.validation.displayServerSideErrors = function (pageID, errorMsg) {
        var validator = $( pageID ).validate();

        // Look at each error 'key' and determine where to display the message.
        $.each(errorMsg, function(index, val) {
            // Look for a form input with the 'key' from the error.
            var inputElementExist = $(pageID + " input[name='" + index + "']");
            // If the form input exists then apply the error message to that input, if not display it at the top of the form.
            if (inputElementExist.length == 1) {
                // Build singular errorMsg object.
                var errorObj = {};
                errorObj[index] = val;
                // Display the error message in the form input.
                validator.showErrors(errorObj);
            } else {
                // Display the error message at the top of the form.
                $('<div class="invalid">' + val + '</div>').prependTo($(pageID));
            }
        });
    };

    // Add a 'max' attribute to the Birth Date fields to set the maximum allowed date to today.
    $('.birthDate').attr('max', function() {
        return CONTACTS.util.getCurrentDate();
    });

    /**
     * Check if the birthdate fits the regex pattern of YYYY-DD-MM.
     */
    CONTACTS.validation.validateBirthDate = function(date) {
        var parseBirthDate = /^([1-2][0,9][0-9][0-9])[-](0[1-9]|1[0-2])[-](0[1-9]|[12][0-9]|3[01])$/;
        var match = parseBirthDate.exec(date);
        // Check to makes sure the date is a valid calendar date.
        if(match) {
            // Pull out the parts of the date passed in.
            var y = parseInt(match[1], 10);
            var m = parseInt(match[2], 10);
            var d = parseInt(match[3], 10);
            // Pass those date parts into a new date.
            var date = new Date(y, m - 1, d);
            // Check to see if the new date matches the original date.  If something like Feb 31 was used originally,
            // it will automatically get converted to Mar 02.  These dates would not match.
            return (date.getFullYear() == y && date.getMonth() + 1 == m && date.getDate() == d);
        } else {
            // The date passed into the regex did not get parsed.
            return false;
        }
    };

    // Create a custom method for the Validator to check if a 'birth date' is formatted correctly.
    $.validator.addMethod("birthdate", function(value, element) {
        return this.optional(element) || CONTACTS.validation.validateBirthDate(value);
    }, "Only valid date formats like yyyy-mm-dd. (hint: There are only 12 months and at most 31 days.)");

    /**
     * Check if the phone number is a valid format.
     */
    CONTACTS.validation.validatePhone = function(phone, divID) {
        // Get the ID of the phoneNumber field that is being validated, either from the Add or Edit form.
    	var tel = $(divID);
        // Check if anything has been entered in the phone field.
//        if ($.trim(tel.val())) {
        	// Check if the number is valid.
            if (tel.intlTelInput("isValidNumber")) {
                return true;
            } else {
                return false;
            }
//        } else {
//        	// Nothing is in the field, let the Require validation handle it.
//        	return true;
//        }
    };

    // Create a custom method for the Validator to check if a 'phoneNumber' is formatted correctly.
    $.validator.addMethod("intlTel", function(value, element, param) {
        return this.optional(element) || CONTACTS.validation.validatePhone(value, param);
    }, "Please use a valid phone number with county code.");

    /**
     * Check if the name fits the regex pattern of a Alpha characters only.
     */
    CONTACTS.validation.validateName = function(name) {
        var parseName = /^([A-Za-z-']+)$/;
        return parseName.test(name);
    };

    // Create a custom method for the Validator to check if a 'name' is formatted correctly.
    $.validator.addMethod("personName", function(value, element) {
        return this.optional(element) || CONTACTS.validation.validateName(value);
    }, "Please use a name without numbers or specials.");

    /**
     * Compare the email in the form to the one returned from the server.  If they match then the user is still using
     * an email that is already in use.
     * (hint: it is only returned after the submit is fired, otherwise it is null)
     */
    CONTACTS.validation.validateEmailUniqueness = function(email) {
        if (CONTACTS.validation.formEmail === email){
            return false;
        } else {
            return true;
        }
    };

    // Create a custom method for the Validator to check if an email is already used.
    $.validator.addMethod("emailUnique", function(value, element) {
        return this.optional(element) || CONTACTS.validation.validateEmailUniqueness(value);
    }, "That email is already used, please use a unique email.");

    /**
     *  We need a way to apply the form validation in cases where the forms don't exist yet, like the unit tests.
     */
    CONTACTS.validation.runFormValidators = function() {
        // Turn on the intl-Tel-Input validation for the Add form. Turn it on for the Edit form in the app.js buildContactDetail.
        $('#contacts-add-input-tel').intlTelInput({nationalMode:false});

        // Set up the validator for the 'add' form.
        // NOTE: I tried setting it up to use the form class but then it only applied the validation to the first form.
        //       It appears that the plugin only works when it is given only 1 form at a time.
        CONTACTS.validation.addContactsFormValidator = $('#contacts-add-form').validate({
            rules: {
                firstName: {
                    required: true,
                    // This is the custom validator created above to make sure that the String only has letter, -, or '.
                    personName: true,
                    maxlength: 25
                },
                lastName: {
                    required: true,
                    // This is the custom validator created above to make sure that the String only has letter, -, or '.
                    personName: true,
                    maxlength: 25
                },
                phoneNumber: {
                    required: true,
                    intlTel: {
                    	param: "#contacts-add-input-tel"
                    }
//                    phoneUS: true
                },
                email: {
                    required: true,
                    email: true,
                    // This is the custom validator created above to make sure that the email is not being used in another contact.
                    emailUnique: true
                },
                birthDate: {
                    required: true,
                    date: false,
                    // This is the custom validator created above to make sure that the birthdate is YYYY-MM-DD.
                    birthdate : true
                }
            },
            messages: {
                firstName: {
                    required: "Please specify a first name."
                },
                lastName: {
                    required: "Please specify a last name."
                },
                phoneNumber: {
                    required: "Please enter a phone number."
//                    phoneUS: "Please use a standard US formats. And remember the area code and prefix may not start with 1."
                },
                email: {
                    required: "Please enter an e-mail.",
                    email: "The email address must be in the format of name@company.domain."
                },
                birthDate: {
                    required: "Please enter a valid birthdate.",
                    max: "Birthdates can not be in the future. Please choose one from the past. Unless they are a time traveler.",
                    min: "Nobody is that old. Unless they are a vampire."
                }
            }
        });

        /**
         * Set up the validator for the 'edit' form.
         * NOTE: I tried setting it up to use the form class but then it only applied the validation to the first form.
         *       It appears that the plugin only works when it is given only 1 form at a time.
         */
        CONTACTS.validation.editContactsFormValidator = $('#contacts-edit-form').validate({
            rules: {
                firstName: {
                    required: true,
                    // This is the custom validator created above to make sure that the String only has letter, -, or '.
                    personName: true,
                    maxlength: 25
                },
                lastName: {
                    required: true,
                    // This is the custom validator created above to make sure that the String only has letter, -, or '.
                    personName: true,
                    maxlength: 25
                },
                phoneNumber: {
                    required: true,
                    intlTel: {
                    	param: "#contacts-edit-input-tel"
                    }
//                    phoneUS: true
                },
                email: {
                    required: true,
                    email: true,
                    // This is the custom validator created above to make sure that the email is not being used in another contact.
                    emailUnique: true
                },
                birthDate: {
                    required: true,
                    date: false,
                    // This is the custom validator created above to make sure that the birthdate is YYYY-MM-DD.
                    birthdate : true
                }
            },
            messages: {
                firstName: {
                    required: "Please specify a first name."
                },
                lastName: {
                    required: "Please specify a last name."
                },
                phoneNumber: {
                    required: "Please enter a phone number."
//                    phoneUS: "Please use a standard US formats. And remember the area code and prefix may not start with 1."
                },
                email: {
                    required: "Please enter an e-mail.",
                    email: "The email address must be in the format of name@company.domain."
                },
                birthDate: {
                    required: "Please enter a valid birthdate.",
                    max: "Birthdates can not be in the future. Please choose one from the past. Unless they are a time traveler.",
                    min: "Nobody is that old. Unless they are a vampire."
                }
            }
        });
    };

    /**
     * Run the form validators.
     */
    CONTACTS.validation.runFormValidators();
});

