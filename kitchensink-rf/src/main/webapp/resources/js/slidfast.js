/*!
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *
 *  Author: Wesley Hales
 *
 *  version: 1.0.0.Alpha
 */

(function(window,document,undefined){

    var slidfast = (function(){

        var slidfast = function(startupOptions){
            options = startupOptions;
            return new slidfast.core.init();
        },

        options,

        defaultPageID = "",

        callback = null;

        backButtonID = "",

        focusPage =  null,
        focusPage =  null,

        isReady = false;

        slidfast.core = slidfast.prototype = {

            constructor: slidfast,

            start: function() {
                if(options){
                    try{
                        defaultPageID = options.defaultPageID;
                        if(options.callback){
                            callback = window[options.callback];
                        }
                        backButtonID = options.backButtonID;
                    }catch(e){
                        console.log('Problem with configuration options.');
                        console.log(e);
                        alert('Problem with configuration options.');
                    }
                }
                try{
                    slidfast.core.hideURLBar();
                    slidfast.core.locationChange();
                }catch(e){
                    console.log('You must define the page ID and location hash as default parameters. \n Error:' + e);
                    console.log(e);
                    alert('You must define the page ID and location hash as default parameters. \n Error:' + e);
                }
            },

            hideURLBar: function() {
                //hide the url bar on mobile devices
                setTimeout(scrollTo, 100, 0, 1)
            },

            slideTo :   function (id) {
                if(!focusPage) {
                    focusPage = document.getElementById(defaultPageID);
                }

                //1.)the page we are bringing into focus dictates how
                // the current page will exit. So let's see what classes
                // our incoming page is using. We know it will have stage[right|left|etc...]
                var classes = document.getElementById(id).className.split(' ');

                //2.)decide if the incoming page is assigned to right or left
                // (-1 if no match)
                var stageType = classes.indexOf('stage-left');

                //3.) decide how this focused page should exit.
                if (stageType > 0) {
                    focusPage.className = 'page transition stage-right';
                } else {
                    focusPage.className = 'page transition stage-left';
                }

                //4. refresh/set the variable
                focusPage = document.getElementById(id);

                //5. Bring in the new page and set the global.
                focusPage.className = 'page transition stage-center';

                slidfast.core.hideURLBar();
            },

            init: function() {

                window.addEventListener('load', function(e) {
                    isReady = true;
                    slidfast.core.start(defaultPageID);
                }, false);

                window.addEventListener('hashchange', function(e) {
                    slidfast.core.locationChange();
                }, false);

                return slidfast.core;

            },

            locationChange: function() {
                var locationHash = location.hash;
                if (locationHash === "#" + defaultPageID || locationHash == '') {
                    slidfast.core.slideTo(defaultPageID);
                    location.hash = defaultPageID;

                    //todo - instead of manually tracking backbutton, lets dispatch an event
                    //this is hard-coded for now
                    if(backButtonID){
                        //we're on the default page, so no need for back button
                        document.getElementById(backButtonID).className = 'hide-button';
                    }
                } else {

                    var hashArray = locationHash.split(':');
                    var param1;
                    var param2;
                    if (hashArray.length === 2) {
                        param1 = hashArray[0].replace('#', '');
                        param2 = hashArray[1];
                    }else{
                        param1 = locationHash.replace('#', '');
                        param2 = null;
                    }
                    //todo accomodate more (or less) than 2 level breadcrumb

                    if(callback != null){
                        try{
                        //after location changes, call this method supplied in config
                        //if using RichFaces, this would be the method name supplied in the
                        //a4j:jsFunction component...
                        callback(param1, param2);
                        }catch(e){
                           console.log('you must define an a4j:jsFunction component with name=' + callback);
                           console.log(e);
                           alert('you must define an a4j:jsFunction component with name=' + callback);
                        }
                    }else{
                        //attempt to slide to a page id if not using a callback
                        slidfast.core.slideTo(param1);
                    }

                    //todo - instead of manually tracking backbutton, lets dispatch an event
                    if(backButtonID){
                        //show the back button and attach functions
                        document.getElementById(backButtonID).className = 'basic-button left-header-button';
                        document.getElementById(backButtonID).onclick = function() {
                            //todo should (possibly) go back in history
                            location.hash = defaultPageID;
                        };
                    }

                }
            },

            flip: function() {
                //get a handle on the flippable region
                var front = document.getElementById('front');
                var back = document.getElementById('back');

                //just a simple way to see what the state is
                var classes = front.className.split(' ');
                var flipped = classes.indexOf('flipped');

                if (flipped >= 0) {
                    //already flipped, so return to original
                    front.className = 'normal';
                    back.className = 'flipped';
                } else {
                    //do the flip
                    front.className = 'flipped';
                    back.className = 'normal';

                }
            }

        };
        slidfast.core.init.prototype = slidfast.core;
        return slidfast;

    })();

window.slidfast = slidfast;
})(window,document);



