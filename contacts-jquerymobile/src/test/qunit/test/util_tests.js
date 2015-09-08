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
/**
 * Unit tests that cover basic functionality.
 */

// ------------------------------------------------------------------------------

module('Util', {
    setup: function() {
        // run before
    },
    teardown: function() {
        // run after
    }
});

test('should be able to provide the current date', 1, function() {
    // I'm not sure how to test this without reproducing the exact code here, then what is the point?

    var d = new Date();
    var month = d.getMonth()+1;
    var day = d.getDate();

    var output = d.getFullYear() + '-' +
        (month<10 ? '0' : '') + month + '-' +
        (day<10 ? '0' : '') + day;

    strictEqual(CONTACTS.util.getCurrentDate(), output, 'The date was provide.');
});

test('should be able to provide the current time', 1, function() {
    // I'm not sure how to test this without reproducing the exact code here, then what is the point

    // Also how do you test if it is providing the same exact time if you are going to the milliseconds?  They should
    //  always be different but apparently they are not. I guess both operations are done in the same millisecond.
    // This still may fail on a slower computer.

    var d = new Date();
    var hour = d.getHours();
    var min = d.getMinutes();
    var sec = d.getSeconds();
    var millisec = d.getMilliseconds();

    var output = (hour<10 ? '0' : '') + hour + ":" +
                 (min<10 ? '0' : '') + min + ":" +
                 (sec<10 ? '0' : '') + sec + "," +
                 (millisec<10 ? '0' : (millisec<100 ? '0' : '')) + millisec;

    strictEqual(CONTACTS.util.getCurrentTime(), output, 'The time was provide.');
});

test('should be able to provide the current date time', 1, function() {
    // I'm not sure how to test this without reproducing the exact code here, then what is the point

    // Also how do you test if it is providing the same exact time if you are going to the milliseconds?  They should
    //  always be different but apparently they are not. I guess both operations are done in the same millisecond.
    // This still may fail on a slower computer.

    var d = new Date();
    var month = d.getMonth()+1;
    var day = d.getDate();

    var hour = d.getHours();
    var min = d.getMinutes();
    var sec = d.getSeconds();
    var millisec = d.getMilliseconds();

    var output = d.getFullYear() + '-' +
                 (month<10 ? '0' : '') + month + '-' +
                 (day<10 ? '0' : '') + day + ' ' +
                 (hour<10 ? '0' : '') + hour + ":" +
                 (min<10 ? '0' : '') + min + ":" +
                 (sec<10 ? '0' : '') + sec + "," +
                 (millisec<10 ? '0' : (millisec<100 ? '0' : '')) + millisec;

    strictEqual(CONTACTS.util.getCurrentDateTime(), output, 'The date time was provide.');
});

//test('', 1, function() {
//    ok(true,"TODO");
//});
