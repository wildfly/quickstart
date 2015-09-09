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
 * Provide a way to change the Theme dynamically. This will watch for the button click of the different theme options
 * in the Theming popup.
 *
 * @author Joshua Wilson
 */
$( document ).on( "pagecreate", function(mainEvent) {
    //Initialize the vars in the beginning so that you will always have access to them.
    var getCurrentTime = CONTACTS.util.getCurrentTime,
        theme,
        changeTheme;

    console.log(getCurrentTime() + " [js/theming.js] (document -> pagecreate) - start");

    // Dynamically set the theme based on the option selected in the popup.
    $('#theme-button-a').click(function() {
    	console.log(getCurrentTime() + " [js/theming.js] (#theme-button-a -> click) - start");
        theme = 'a';
        changeTheme();
        console.log(getCurrentTime() + " [js/theming.js] (#theme-button-a -> click) - end");
    });

    // Dynamically set the theme based on the option selected in the popup.
    $('#theme-button-b').click(function() {
    	console.log(getCurrentTime() + " [js/theming.js] (#theme-button-b -> click) - start");
    	theme = 'b';
    	changeTheme();
    	console.log(getCurrentTime() + " [js/theming.js] (#theme-button-b -> click) - end");
    });

    // Dynamically set the theme based on the option selected in the popup.
    $('#theme-button-c').click(function() {
    	console.log(getCurrentTime() + " [js/theming.js] (#theme-button-c -> click) - start");
    	theme = 'c';
    	changeTheme();
    	console.log(getCurrentTime() + " [js/theming.js] (#theme-button-c -> click) - end");
    });

    // Dynamically set the theme based on the option selected in the popup.
    $('#theme-button-d').click(function() {
    	console.log(getCurrentTime() + " [js/theming.js] (#theme-button-d -> click) - start");
    	theme = 'd';
    	changeTheme();
    	console.log(getCurrentTime() + " [js/theming.js] (#theme-button-d -> click) - end");
    });

    // Dynamically set the theme based on the option selected in the popup.
    $('#theme-button-e').click(function() {
    	console.log(getCurrentTime() + " [js/theming.js] (#theme-button-e -> click) - start");
    	theme = 'e';
    	changeTheme();
    	console.log(getCurrentTime() + " [js/theming.js] (#theme-button-e -> click) - end");
    });

    // Dynamically set the theme based on the option selected in the popup.
    $('#theme-button-f').click(function() {
    	console.log(getCurrentTime() + " [js/theming.js] (#theme-button-f -> click) - start");
    	theme = 'f';
    	changeTheme();
    	console.log(getCurrentTime() + " [js/theming.js] (#theme-button-f -> click) - end");
    });

    // Dynamically set the theme based on the option selected in the popup.
    $('#theme-button-g').click(function() {
    	console.log(getCurrentTime() + " [js/theming.js] (#theme-button-g -> click) - start");
    	theme = 'g';
    	changeTheme();
    	console.log(getCurrentTime() + " [js/theming.js] (#theme-button-g -> click) - end");
    });

    changeTheme = function() {
    	console.log(getCurrentTime() + " [js/theming.js] (changeTheme) - theme = " + theme);

    	// Keep the submit buttons in the alternate theme for contrast.
    	if (theme !== 'a') {
    		$('.ui-btn-b').removeClass('ui-btn-b').addClass('ui-btn-a');
    	} else {
    		$('.ui-btn-a').removeClass('ui-btn-a').addClass('ui-btn-b');
    	}

    	// Go through all the Pages and remove whatever they they currently have and apply the selected theme.
        $('[data-role="page"]').removeClass('ui-page-theme-a ui-page-theme-b ui-page-theme-c ui-page-theme-d ui-page-theme-e')
                               .addClass('ui-page-theme-' + theme);
    }


    console.log(getCurrentTime() + " [js/theming.js] (document -> pagecreate) - end");
});


