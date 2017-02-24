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
// Define any routes for the app
// Note that this app is a single page app, and each partial is routed to using the URL fragment. For example, to select the 'home' route, the URL is http://localhost:8080/jboss-as-kitchensink-angularjs/#/home
angular.module('kitchensink', ['ngRoute','membersService']).config(
        [ '$httpProvider', '$routeProvider', function($httpProvider, $routeProvider) {
            /*
             * Use a HTTP interceptor to add a nonce to every request to prevent MSIE from caching responses.
             */
            $httpProvider.interceptors.push('ajaxNonceInterceptor');

            $routeProvider.
            // if URL fragment is /home, then load the home partial, with the
            // MembersCtrl controller
            when('/home', {
                templateUrl : 'partials/home.html',
                controller : MembersCtrl
            // Add a default route
            }).otherwise({
                redirectTo : '/home'
            });
        } ])
    .factory('ajaxNonceInterceptor', function() {
        // This interceptor is equivalent to the behavior induced by $.ajaxSetup({cache:false});

        var param_start = /\?/;

        return {

            request : function(config) {
                if (config.method == 'GET') {
                    // Add a query parameter named '_' to the URL, with a value equal to the current timestamp
                    config.url += (param_start.test(config.url) ? "&" : "?") + '_=' + new Date().getTime();
                }
                return config;
            }
        }
    });