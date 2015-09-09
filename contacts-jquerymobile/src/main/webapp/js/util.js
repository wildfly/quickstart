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
CONTACTS.namespace('CONTACTS.util.getCurrentDate');
CONTACTS.namespace('CONTACTS.util.getCurrentTime');
CONTACTS.namespace('CONTACTS.util.getCurrentDateTime');
CONTACTS.namespace('CONTACTS.util.convertMillisToDate');
CONTACTS.namespace('CONTACTS.util.convertDateStringToOffsetUTC');

/**
 * Abstract away generic functions that are used by all.
 *
 * @author Joshua Wilson
 */
CONTACTS.util.getCurrentDate = function() {
    var d = new Date();
    var month = d.getMonth()+1;
    var day = d.getDate();

    var output = d.getFullYear() + '-' +
        (month<10 ? '0' : '') + month + '-' +
        (day<10 ? '0' : '') + day;

    return output;
};

CONTACTS.util.getCurrentTime = function() {
    var d = new Date();
    var hour = d.getHours();
    var min = d.getMinutes();
    var sec = d.getSeconds();
    var millisec = d.getMilliseconds();

    var output = (hour<10 ? '0' : '') + hour + ":" +
                 (min<10 ? '0' : '') + min + ":" +
                 (sec<10 ? '0' : '') + sec + "," +
                 (millisec<10 ? '0' : (millisec<100 ? '0' : '')) + millisec;

    return output;
};

CONTACTS.util.getCurrentDateTime = function() {
    var output = CONTACTS.util.getCurrentDate() + ' ' + CONTACTS.util.getCurrentTime();
    return output;
};

/**
 * The database stores the Date in Milliseconds from the epoch.  We need to convert that into a readable date.
 */
CONTACTS.util.convertMillisToDate = function(milliseconds) {
    console.log(CONTACTS.util.getCurrentTime() + " [js/util.js] (convertMillisToDate) - milliseconds passed in = " + milliseconds);
    var d = new Date(milliseconds);
    console.log(CONTACTS.util.getCurrentTime() + " [js/util.js] (convertMillisToDate) - date after converting milliseconds passed in = " + d);

    // Must add 1 due to zero based array of months.
    var month = d.getMonth()+1;
    var day = d.getDate();

    // Create a String of the date but add in zeros where needed so that month and day take up 2 fields.
    var output = d.getFullYear() + '-' +
        (month<10 ? '0' : '') + month + '-' +
        (day<10 ? '0' : '') + day;

    return output;
};

/**
 * Convert a local date String to a Date type and then apply the timezone offset to it so that it keeps the right time.
 * Without this the time stored would be as if it were recorded in UTC.
 */
CONTACTS.util.convertDateStringToOffsetUTC = function(utcDate) {
    console.log(CONTACTS.util.getCurrentTime() + " [js/util.js] (convertDateStringToOffsetUTC) - date String passed in = " + utcDate);

    // The date passed in is a String, this makes it a Date object/type.
    var d = new Date(utcDate);
    console.log(CONTACTS.util.getCurrentTime() + " [js/util.js] (convertDateStringToOffsetUTC) - date after converting " +
            "String to the Date type (displayed in local time) = " + d);

    // Get the Timezone Offset, in minutes, from UTC to local. This will either be a negative or positive number depending
    // on your timezone.
    var offsetInMin = d.getTimezoneOffset();
    console.log(CONTACTS.util.getCurrentTime() + " [js/util.js] (convertDateStringToOffsetUTC) - offset in Minutes = " + offsetInMin);

    // We need the Offset in Milliseconds if we are going to add it more Milliseconds.
    var offsetInMillis = offsetInMin * 60 * 1000;
    console.log(CONTACTS.util.getCurrentTime() + " [js/util.js] (convertDateStringToOffsetUTC) - offset in Milliseconds = " + offsetInMillis);

    // Add the Offset to the UTC time so that we can store the actual time that was intended to be recorded.
    var offsetUTCDateTime = new Date(d.getTime() + offsetInMillis);
    console.log(CONTACTS.util.getCurrentTime() + " [js/util.js] (convertDateStringToOffsetUTC) - date after applying timezone " +
            "offset to UTC Date (displayed in local time) = " + offsetUTCDateTime);

    return offsetUTCDateTime;
};

