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
 * The following is taken from "JavaScript Patterns" by Stoyan Stefanov
 *
 * As the complexity of a program grows and some parts of code get split into different files and included
 * conditionally, it becomes unsafe to just assume that your code is the first to define a certain namespace
 * or a property inside of it. Some of the properties your're adding to the namespace may already exist, and
 * you could be overwriting them. Therefore before adding a property or creating a namespace, it's best to
 * check first that it doesn't already exist, as shown in this example:
 *
 *         // unsafe
 *         var CONTACTS = {};
 *
 *         // better
 *         if (typeof CONTACTS === "undefined") {
 *             var CONTACTS = {};
 *         }
 *
 *         // or shorter
 *         var CONTACTS = CONTACTS || {};
 *
 * The module pattern is widely used because it provides structure and helps organize your code as it grows.
 * Unlike other languages, JavaScript doesn't have special syntax for packages, but the module pattern provides
 * the tools to create self-contained decoupled pieces of code, which can be treated as black boxes of functionality
 * and added, replaced, or removed according to the (ever-changing requirements of the software you're writing.
 *
 * @author Joshua Wilson
 */

var CONTACTS = CONTACTS || {};

CONTACTS.namespace = function (ns_string) {
    var parts = ns_string.split('.'),
        parent = CONTACTS,
        i;

    // Strip redundant leading global
    if (parts[0] === "CONTACTS") {
        parts = parts.slice(1);
    }

    for (i=0; i < parts.length; i += 1) {
        // Create a property if it doesn't exist
        if  (typeof parent[parts[i]] === "undefined") {
            parent[parts[i]] = {};
        }

        parent = parent[parts[i]];
    }
    return parent;
};
