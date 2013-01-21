/*! AeroGear JavaScript Library - v1.0.0-M2 - 2013-01-21
* https://github.com/aerogear/aerogear-js
* JBoss, Home of Professional Open Source
* Copyright Red Hat, Inc., and individual contributors
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
    The AeroGear namespace provides a way to encapsulate the library's properties and methods away from the global namespace
    @namespace
 */
var AeroGear = {};

/**
    AeroGear.Core is a base for all of the library modules to extend. It is not to be instantiated and will throw an error when attempted
    @class
    @private
 */
AeroGear.Core = function() {
    // Prevent instantiation of this base class
    if ( this instanceof AeroGear.Core ) {
        throw "Invalid instantiation of base class AeroGear.Core";
    }

    /**
        This function is used internally by pipeline, datamanager, etc. to add a new Object (pipe, store, etc.) to the respective collection.
        @method
        @param {String|Array|Object} config - This can be a variety of types specifying how to create the object
        @returns {Object} The object containing the collection that was updated
     */
    this.add = function ( config ) {
        var i,
            current,
            collection = this[ this.collectionName ] || {};

        if ( !config ) {
            return this;
        } else if ( typeof config === "string" ) {
            // config is a string so use default adapter type
            collection[ config ] = AeroGear[ this.lib ].adapters[ this.type ]( config );
        } else if ( AeroGear.isArray( config ) ) {
            // config is an array so loop through each item in the array
            for ( i = 0; i < config.length; i++ ) {
                current = config[ i ];

                if ( typeof current === "string" ) {
                    collection[ current ] = AeroGear[ this.lib ].adapters[ this.type ]( current );
                } else {
                    collection[ current.name ] = AeroGear[ this.lib ].adapters[ current.type || this.type ]( current.name, current.settings || {} );
                }
            }
        } else {
            // config is an object so use that signature
            collection[ config.name ] = AeroGear[ this.lib ].adapters[ config.type || this.type ]( config.name, config.settings || {} );
        }

        // reset the collection instance
        this[ this.collectionName ] = collection;

        return this;
    };
    /**
        This function is used internally by pipeline, datamanager, etc. to remove an Object (pipe, store, etc.) from the respective collection.
        @method
        @param {String|String[]|Object[]|Object} config - This can be a variety of types specifying how to remove the object
        @returns {Object} The object containing the collection that was updated
     */
    this.remove = function( config ) {
        var i,
            current,
            collection = this[ this.collectionName ] || {};

        if ( typeof config === "string" ) {
            // config is a string so delete that item by name
            delete collection[ config ];
        } else if ( AeroGear.isArray( config ) ) {
            // config is an array so loop through each item in the array
            for ( i = 0; i < config.length; i++ ) {
                current = config[ i ];

                if ( typeof current === "string" ) {
                    delete collection[ current ];
                } else {
                    delete collection[ current.name ];
                }
            }
        } else if ( config ) {
            // config is an object so use that signature
            delete collection[ config.name ];
        }

        // reset the collection instance
        this[ this.collectionName ] = collection;

        return this;
    };
};

/**
    Utility function to test if an object is an Array
    @private
    @method
    @param {Object} obj - This can be any object to test
*/
AeroGear.isArray = function( obj ) {
    return ({}).toString.call( obj ) === "[object Array]";
};

(function( AeroGear, $, undefined ) {
    /**
        Wrapper utility around jQuery.ajax to preform some custom actions
        @private
        @method
        @param {Object} caller - the AeroGear object (pipe, datamanager, etc.) that is calling AeroGear.ajax
        @param {Object} options - settings for jQuery.ajax
     */
    AeroGear.ajax = function( caller, options ) {
        var deferred = $.Deferred( function() {
            var that = this,
                settings = $.extend( {}, {
                    contentType: "application/json",
                    dataType: "json"
                }, options );

            this.done( settings.success );
            this.fail( settings.error );
            this.always( settings.complete );

            var ajaxSettings = $.extend( {}, settings, {
                success: function( data, textStatus, jqXHR ) {
                    that.resolve( typeof data === "string" && ajaxSettings.dataType === "json" ? JSON.parse( data ) : data, textStatus, jqXHR );
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    if ( ajaxSettings.dataType === "json" ) {
                        try {
                            jqXHR.responseJSON = JSON.parse( jqXHR.responseText );
                        } catch( error ) {}
                    }
                    that.reject( jqXHR, textStatus, errorThrown );
                },
                complete: function( jqXHR, textStatus ) {
                    that.resolve( jqXHR, textStatus );
                }
            });

            if ( ajaxSettings.contentType === "application/json" && ajaxSettings.data && ( ajaxSettings.type === "POST" || ajaxSettings.type === "PUT" ) ) {
                ajaxSettings.data = JSON.stringify( ajaxSettings.data );
            }

            if ( AeroGear.Auth && !caller.isAuthenticated() ) {
                this.reject( "auth", "Error: Authentication Required" );
            } else if ( caller.addAuthIdentifier ) {
                $.ajax( caller.addAuthIdentifier( ajaxSettings ) );
            } else {
                $.ajax( ajaxSettings );
            }
        });

        var promise = deferred.promise();

        promise.success = deferred.done;
        promise.error = deferred.fail;
        promise.complete = deferred.always;

        return promise;
    };
})( AeroGear, jQuery );

//     node-uuid/uuid.js
//
//     Copyright (c) 2010 Robert Kieffer
//     Dual licensed under the MIT and GPL licenses.
//     Documentation and details at https://github.com/broofa/node-uuid
(function() {
  var _global = this;

  // Unique ID creation requires a high quality random # generator, but
  // Math.random() does not guarantee "cryptographic quality".  So we feature
  // detect for more robust APIs, normalizing each method to return 128-bits
  // (16 bytes) of random data.
  var mathRNG, nodeRNG, whatwgRNG;

  // Math.random()-based RNG.  All platforms, very fast, unknown quality
  var _rndBytes = new Array(16);
  mathRNG = function() {
    var r, b = _rndBytes, i = 0;

    for (var i = 0, r; i < 16; i++) {
      if ((i & 0x03) == 0) r = Math.random() * 0x100000000;
      b[i] = r >>> ((i & 0x03) << 3) & 0xff;
    }

    return b;
  }

  // WHATWG crypto-based RNG - http://wiki.whatwg.org/wiki/Crypto
  // WebKit only (currently), moderately fast, high quality
  if (_global.crypto && crypto.getRandomValues) {
    var _rnds = new Uint32Array(4);
    whatwgRNG = function() {
      crypto.getRandomValues(_rnds);

      for (var c = 0 ; c < 16; c++) {
        _rndBytes[c] = _rnds[c >> 2] >>> ((c & 0x03) * 8) & 0xff;
      }
      return _rndBytes;
    }
  }

  // Node.js crypto-based RNG - http://nodejs.org/docs/v0.6.2/api/crypto.html
  // Node.js only, moderately fast, high quality
  try {
    var _rb = require('crypto').randomBytes;
    nodeRNG = _rb && function() {
      return _rb(16);
    };
  } catch (e) {}

  // Select RNG with best quality
  var _rng = nodeRNG || whatwgRNG || mathRNG;

  // Buffer class to use
  var BufferClass = typeof(Buffer) == 'function' ? Buffer : Array;

  // Maps for number <-> hex string conversion
  var _byteToHex = [];
  var _hexToByte = {};
  for (var i = 0; i < 256; i++) {
    _byteToHex[i] = (i + 0x100).toString(16).substr(1);
    _hexToByte[_byteToHex[i]] = i;
  }

  // **`parse()` - Parse a UUID into it's component bytes**
  function parse(s, buf, offset) {
    var i = (buf && offset) || 0, ii = 0;

    buf = buf || [];
    s.toLowerCase().replace(/[0-9a-f]{2}/g, function(byte) {
      if (ii < 16) { // Don't overflow!
        buf[i + ii++] = _hexToByte[byte];
      }
    });

    // Zero out remaining bytes if string was short
    while (ii < 16) {
      buf[i + ii++] = 0;
    }

    return buf;
  }

  // **`unparse()` - Convert UUID byte array (ala parse()) into a string**
  function unparse(buf, offset) {
    var i = offset || 0, bth = _byteToHex;
    return  bth[buf[i++]] + bth[buf[i++]] +
            bth[buf[i++]] + bth[buf[i++]] + '-' +
            bth[buf[i++]] + bth[buf[i++]] + '-' +
            bth[buf[i++]] + bth[buf[i++]] + '-' +
            bth[buf[i++]] + bth[buf[i++]] + '-' +
            bth[buf[i++]] + bth[buf[i++]] +
            bth[buf[i++]] + bth[buf[i++]] +
            bth[buf[i++]] + bth[buf[i++]];
  }

  // **`v1()` - Generate time-based UUID**
  //
  // Inspired by https://github.com/LiosK/UUID.js
  // and http://docs.python.org/library/uuid.html

  // random #'s we need to init node and clockseq
  var _seedBytes = _rng();

  // Per 4.5, create and 48-bit node id, (47 random bits + multicast bit = 1)
  var _nodeId = [
    _seedBytes[0] | 0x01,
    _seedBytes[1], _seedBytes[2], _seedBytes[3], _seedBytes[4], _seedBytes[5]
  ];

  // Per 4.2.2, randomize (14 bit) clockseq
  var _clockseq = (_seedBytes[6] << 8 | _seedBytes[7]) & 0x3fff;

  // Previous uuid creation time
  var _lastMSecs = 0, _lastNSecs = 0;

  // See https://github.com/broofa/node-uuid for API details
  function v1(options, buf, offset) {
    var i = buf && offset || 0;
    var b = buf || [];

    options = options || {};

    var clockseq = options.clockseq != null ? options.clockseq : _clockseq;

    // UUID timestamps are 100 nano-second units since the Gregorian epoch,
    // (1582-10-15 00:00).  JSNumbers aren't precise enough for this, so
    // time is handled internally as 'msecs' (integer milliseconds) and 'nsecs'
    // (100-nanoseconds offset from msecs) since unix epoch, 1970-01-01 00:00.
    var msecs = options.msecs != null ? options.msecs : new Date().getTime();

    // Per 4.2.1.2, use count of uuid's generated during the current clock
    // cycle to simulate higher resolution clock
    var nsecs = options.nsecs != null ? options.nsecs : _lastNSecs + 1;

    // Time since last uuid creation (in msecs)
    var dt = (msecs - _lastMSecs) + (nsecs - _lastNSecs)/10000;

    // Per 4.2.1.2, Bump clockseq on clock regression
    if (dt < 0 && options.clockseq == null) {
      clockseq = clockseq + 1 & 0x3fff;
    }

    // Reset nsecs if clock regresses (new clockseq) or we've moved onto a new
    // time interval
    if ((dt < 0 || msecs > _lastMSecs) && options.nsecs == null) {
      nsecs = 0;
    }

    // Per 4.2.1.2 Throw error if too many uuids are requested
    if (nsecs >= 10000) {
      throw new Error('uuid.v1(): Can\'t create more than 10M uuids/sec');
    }

    _lastMSecs = msecs;
    _lastNSecs = nsecs;
    _clockseq = clockseq;

    // Per 4.1.4 - Convert from unix epoch to Gregorian epoch
    msecs += 12219292800000;

    // `time_low`
    var tl = ((msecs & 0xfffffff) * 10000 + nsecs) % 0x100000000;
    b[i++] = tl >>> 24 & 0xff;
    b[i++] = tl >>> 16 & 0xff;
    b[i++] = tl >>> 8 & 0xff;
    b[i++] = tl & 0xff;

    // `time_mid`
    var tmh = (msecs / 0x100000000 * 10000) & 0xfffffff;
    b[i++] = tmh >>> 8 & 0xff;
    b[i++] = tmh & 0xff;

    // `time_high_and_version`
    b[i++] = tmh >>> 24 & 0xf | 0x10; // include version
    b[i++] = tmh >>> 16 & 0xff;

    // `clock_seq_hi_and_reserved` (Per 4.2.2 - include variant)
    b[i++] = clockseq >>> 8 | 0x80;

    // `clock_seq_low`
    b[i++] = clockseq & 0xff;

    // `node`
    var node = options.node || _nodeId;
    for (var n = 0; n < 6; n++) {
      b[i + n] = node[n];
    }

    return buf ? buf : unparse(b);
  }

  // **`v4()` - Generate random UUID**

  // See https://github.com/broofa/node-uuid for API details
  function v4(options, buf, offset) {
    // Deprecated - 'format' argument, as supported in v1.2
    var i = buf && offset || 0;

    if (typeof(options) == 'string') {
      buf = options == 'binary' ? new BufferClass(16) : null;
      options = null;
    }
    options = options || {};

    var rnds = options.random || (options.rng || _rng)();

    // Per 4.4, set bits for version and `clock_seq_hi_and_reserved`
    rnds[6] = (rnds[6] & 0x0f) | 0x40;
    rnds[8] = (rnds[8] & 0x3f) | 0x80;

    // Copy bytes to buffer, if provided
    if (buf) {
      for (var ii = 0; ii < 16; ii++) {
        buf[i + ii] = rnds[ii];
      }
    }

    return buf || unparse(rnds);
  }

  // Export public API
  var uuid = v4;
  uuid.v1 = v1;
  uuid.v4 = v4;
  uuid.parse = parse;
  uuid.unparse = unparse;
  uuid.BufferClass = BufferClass;

  // Export RNG options
  uuid.mathRNG = mathRNG;
  uuid.nodeRNG = nodeRNG;
  uuid.whatwgRNG = whatwgRNG;

  if (typeof(module) != 'undefined') {
    // Play nice with node.js
    module.exports = uuid;
  } else {
    // Play nice with browsers
    var _previousRoot = _global.uuid;

    // **`noConflict()` - (browser only) to reset global 'uuid' var**
    uuid.noConflict = function() {
      _global.uuid = _previousRoot;
      return uuid;
    }
    _global.uuid = uuid;
  }
}());

(function( AeroGear, $, undefined ) {
    /**
        The AeroGear.Pipeline provides a persistence API that is protocol agnostic and does not depend on any certain data model. Through the use of adapters, this library provides common methods like read, save and delete that will just work.
        @class
        @augments AeroGear.Core
        @param {String|Array|Object} [config] - A configuration for the pipe(s) being created along with the Pipeline. If an object or array containing objects is used, the objects can have the following properties:
        @param {String} config.name - the name that the pipe will later be referenced by
        @param {String} [config.type="rest"] - the type of pipe as determined by the adapter used
        @param {String} [config.recordId="id"] - the identifier used to denote the unique id for each record in the data associated with this pipe
        @param {Object} [config.settings={}] - the settings to be passed to the adapter
        @returns {Object} pipeline - The created Pipeline containing any pipes that may have been created
        @example
        // Create an empty Pipeline
        var pl = AeroGear.Pipeline();

        // Create a single pipe using the default adapter
        var pl2 = AeroGear.Pipeline( "tasks" );

        // Create multiple pipes using the default adapter
        var pl3 = AeroGear.Pipeline( [ "tasks", "projects" ] );
     */
    AeroGear.Pipeline = function( config ) {
        // Allow instantiation without using new
        if ( !( this instanceof AeroGear.Pipeline ) ) {
            return new AeroGear.Pipeline( config );
        }

        // Super constructor
        AeroGear.Core.call( this );

        this.lib = "Pipeline";
        this.type = config ? config.type || "Rest" : "Rest";

        /**
            The name used to reference the collection of pipe instances created from the adapters
            @memberOf AeroGear.Pipeline
            @type Object
            @default pipes
         */
        this.collectionName = "pipes";

        this.add( config );
    };

    AeroGear.Pipeline.prototype = AeroGear.Core;
    AeroGear.Pipeline.constructor = AeroGear.Pipeline;

    /**
        The adapters object is provided so that adapters can be added to the AeroGear.Pipeline namespace dynamically and still be accessible to the add method
        @augments AeroGear.Pipeline
     */
    AeroGear.Pipeline.adapters = {};
})( AeroGear, jQuery );

(function( AeroGear, $, uuid, undefined ) {
    /**
        The REST adapter is the default type used when creating a new pipe. It uses jQuery.ajax to communicate with the server. By default, the RESTful endpoint used by this pipe is the app's current context, followed by the pipe name. For example, if the app is running on http://mysite.com/myApp, then a pipe named `tasks` would use http://mysite.com/myApp/tasks as its REST endpoint.
        @constructs AeroGear.Pipeline.adapters.Rest
        @param {String} pipeName - the name used to reference this particular pipe
        @param {Object} [settings={}] - the settings to be passed to the adapter
        @param {Object} [settings.authenticator=null] - the AeroGear.auth object used to pass credentials to a secure endpoint
        @param {String} [settings.baseURL] - defines the base URL to use for an endpoint
        @param {String} [settings.endpoint=pipename] - overrides the default naming of the endpoint which uses the pipeName
        @param {String} [settings.recordId="id"] - the name of the field used to uniquely identify a "record" in the data
        @returns {Object} The created pipe
     */
    AeroGear.Pipeline.adapters.Rest = function( pipeName, settings ) {
        // Allow instantiation without using new
        if ( !( this instanceof AeroGear.Pipeline.adapters.Rest ) ) {
            return new AeroGear.Pipeline.adapters.Rest( pipeName, settings );
        }

        settings = settings || {};

        // Private Instance vars
        var endpoint = settings.endpoint || pipeName,
            ajaxSettings = {
                // use the pipeName as the default rest endpoint
                url: settings.baseURL ? settings.baseURL + endpoint : endpoint
            },
            recordId = settings.recordId || "id",
            authenticator = settings.authenticator || null,
            type = "Rest";

        // Privileged Methods
        /**
            Return whether or not the client should consider itself authenticated. Of course, the server may have removed access so that will have to be handled when a request is made
            @private
            @augments Rest
            @returns {Boolean}
         */
        this.isAuthenticated = function() {
            return authenticator ? authenticator.isAuthenticated() : true;
        };

        /**
            Adds the auth token to the headers and returns the modified version of the settings
            @private
            @augments Rest
            @param {Object} settings - the settings object that will have the auth identifier added
            @returns {Object} Settings extended with auth identifier
         */
        this.addAuthIdentifier = function( settings ) {
            return authenticator ? authenticator.addAuthIdentifier( settings ) : settings;
        };

        /**
            Removes the stored token effectively telling the client it must re-authenticate with the server
            @private
            @augments Rest
         */
        this.deauthorize = function() {
            if ( authenticator ) {
                authenticator.deauthorize();
            }
        };

        /**
            Returns the value of the private ajaxSettings var
            @private
            @augments Rest
            @returns {Object}
         */
        this.getAjaxSettings = function() {
            return ajaxSettings;
        };

        /**
            Returns the value of the private recordId var
            @private
            @augments Rest
            @returns {String}
         */
        this.getRecordId = function() {
            return recordId;
        };
    };

    // Public Methods
    /**
        Reads data from the specified endpoint
        @param {Object} [options={}] - Additional options
        @param {Function} [options.complete] - a callback to be called when the result of the request to the server is complete, regardless of success
        @param {Object} [options.query] - a hash of key/value pairs that can be passed to the server as additional information for use when determining what data to return
        @param {Object} [options.id] - the value to append to the endpoint URL,  should be the same as the pipelines recordId
        @param {Function} [options.error] - a callback to be called when the request to the server results in an error
        @param {Object} [options.statusCode] - a collection of status codes and callbacks to fire when the request to the server returns on of those codes. For more info see the statusCode option on the <a href="http://api.jquery.com/jQuery.ajax/">jQuery.ajax page</a>.
        @param {Function} [options.success] - a callback to be called when the result of the request to the server is successful
        @param {Mixed} [options.jsonp] - Turns jsonp on/off for reads, Set to true, or an object with options
        @param {String} [options.jsonp.callback] - Override the callback function name in a jsonp request. This value will be used instead of 'callback' in the 'callback=?' part of the query string in the url
        @param {String} [options.jsonp.customCallback] - Specify the callback function name for a JSONP request. This value will be used instead of the random name automatically generated by jQuery
        @returns {Object} A deferred implementing the promise interface similar to the jqXHR created by jQuery.ajax
        @example
        var myPipe = AeroGear.Pipeline( "tasks" ).pipes[ 0 ];

        // Get a set of key/value pairs of all data on the server associated with this pipe
        var allData = myPipe.read();

        // A data object can be passed to filter the data and in the case of REST,
        // this object is converted to query string parameters which the server can use.
        // The values would be determined by what the server is expecting
        var filteredData = myPipe.read({
            data: {
                limit: 10,
                date: "2012-08-01"
                ...
            }
        });
     */
    AeroGear.Pipeline.adapters.Rest.prototype.read = function( options ) {
        var that = this,
            recordId = this.getRecordId(),
            ajaxSettings = this.getAjaxSettings(),
            url,
            success,
            error,
            extraOptions;

        options = options || {};

        if ( options[ recordId ] ) {
            url = ajaxSettings.url + "/" + options[ recordId ];
        } else {
            url = ajaxSettings.url;
        }

        success = function( data ) {
            if ( options.success ) {
                options.success.apply( this, arguments );
            }
        };
        error = function( type, errorMessage ) {
            if ( options.error ) {
                options.error.apply( this, arguments );
            }
        };
        extraOptions = {
            type: "GET",
            data: options.query,
            success: success,
            error: error,
            url: url,
            statusCode: options.statusCode,
            complete: options.complete
        };

        if( options.jsonp ) {
            extraOptions.dataType = "jsonp";
            extraOptions.jsonp = options.jsonp.callback ? options.jsonp.callback : "callback";
            if( options.jsonp.customCallback ) {
                extraOptions.jsonpCallback = options.jsonp.customCallback;
            }
        }

        return AeroGear.ajax( this, $.extend( {}, this.getAjaxSettings(), extraOptions ) );
    };

    /**
        Save data asynchronously to the server. If this is a new object (doesn't have a record identifier provided by the server), the data is created on the server (POST) and then that record is sent back to the client including the new server-assigned id, otherwise, the data on the server is updated (PUT).
        @param {Object} data - For new data, this will be an object representing the data to be saved to the server. For updating data, a hash of key/value pairs one of which must be the `recordId` you set during creation of the pipe representing the identifier the server will use to update this record and then any other number of pairs representing the data. The data object is then stringified and passed to the server to be processed.
        @param {Object} [options={}] - Additional options
        @param {Function} [options.complete] - a callback to be called when the result of the request to the server is complete, regardless of success
        @param {Function} [options.error] - a callback to be called when the request to the server results in an error
        @param {Object} [options.statusCode] - a collection of status codes and callbacks to fire when the request to the server returns on of those codes. For more info see the statusCode option on the <a href="http://api.jquery.com/jQuery.ajax/">jQuery.ajax page</a>.
        @param {Function} [options.success] - a callback to be called when the result of the request to the server is successful
        @returns {Object} A deferred implementing the promise interface similar to the jqXHR created by jQuery.ajax
        @example
        var myPipe = AeroGear.Pipeline( "tasks" ).pipes[ 0 ];

        // Store a new task
        myPipe.save({
            title: "Created Task",
            date: "2012-07-13",
            ...
        });

        // Pass a success and error callback, in this case using the REST pipe and jQuery.ajax so the functions take the same parameters.
        myPipe.save({
            title: "Another Created Task",
            date: "2012-07-13",
            ...
        },
        {
            success: function( data, textStatus, jqXHR ) {
                console.log( "Success" );
            },
            error: function( jqXHR, textStatus, errorThrown ) {
                console.log( "Error" );
            }
        });

        // Update an existing piece of data
        var toUpdate = myPipe.data[ 0 ];
        toUpdate.data.title = "Updated Task";
        myPipe.save( toUpdate );
     */
    AeroGear.Pipeline.adapters.Rest.prototype.save = function( data, options ) {
        var that = this,
            recordId = this.getRecordId(),
            ajaxSettings = this.getAjaxSettings(),
            type,
            url,
            success,
            error,
            extraOptions;

        data = data || {};
        options = options || {};
        type = data[ recordId ] ? "PUT" : "POST";

        if ( data[ recordId ] ) {
            url = ajaxSettings.url + "/" + data[ recordId ];
        } else {
            url = ajaxSettings.url;
        }

        success = function( data ) {
            if ( options.success ) {
                options.success.apply( this, arguments );
            }
        };
        error = function( type, errorMessage ) {
            if ( options.error ) {
                options.error.apply( this, arguments );
            }
        };
        extraOptions = {
            data: data,
            type: type,
            url: url,
            success: success,
            error: error,
            statusCode: options.statusCode,
            complete: options.complete
        };

        return AeroGear.ajax( this, $.extend( {}, ajaxSettings, extraOptions ) );
    };

    /**
        Remove data asynchronously from the server. Passing nothing will inform the server to remove all data at this pipe's endpoint.
        @param {String|Object} [data] - A variety of objects can be passed to specify the item(s) to remove
        @param {Object} [options={}] - Additional options
        @param {Function} [options.complete] - a callback to be called when the result of the request to the server is complete, regardless of success
        @param {Function} [options.error] - a callback to be called when the request to the server results in an error
        @param {Object} [options.statusCode] - a collection of status codes and callbacks to fire when the request to the server returns on of those codes. For more info see the statusCode option on the <a href="http://api.jquery.com/jQuery.ajax/">jQuery.ajax page</a>.
        @param {Function} [options.success] - a callback to be called when the result of the request to the server is successful
        @returns {Object} A deferred implementing the promise interface similar to the jqXHR created by jQuery.ajax
        @example
        var myPipe = AeroGear.Pipeline( "tasks" ).pipes[ 0 ];

        // Store a new task
        myPipe.save({
            title: "Created Task"
        });

        // Store another new task
        myPipe.save({
            title: "Another Created Task"
        });

        // Store one more new task
        myPipe.save({
            title: "And Another Created Task"
        });

        // Remove a particular item from the server by its id
        var toRemove = myPipe.data[ 0 ];
        myPipe.remove( toRemove.id );

        // Remove an item from the server using the data object
        toRemove = myPipe.data[ 0 ];
        myPipe.remove( toRemove );

        // Delete all remaining data from the server associated with this pipe
        myPipe.remove();
     */
    AeroGear.Pipeline.adapters.Rest.prototype.remove = function( toRemove, options ) {
        var that = this,
            recordId = this.getRecordId(),
            ajaxSettings = this.getAjaxSettings(),
            delPath = "",
            delId,
            url,
            success,
            error,
            extraOptions;

        if ( typeof toRemove === "string" || typeof toRemove === "number" ) {
            delId = toRemove;
        } else if ( toRemove && toRemove[ recordId ] ) {
            delId = toRemove[ recordId ];
        } else if ( toRemove && !options ) {
            // No remove item specified so treat as options
            options = toRemove;
        }

        options = options || {};

        delPath = delId ? "/" + delId : "";
        url = ajaxSettings.url + delPath;

        success = function( data ) {
            if ( options.success ) {
                options.success.apply( this, arguments );
            }
        };
        error = function( type, errorMessage ) {
            if ( options.error ) {
                options.error.apply( this, arguments );
            }
        };
        extraOptions = {
            type: "DELETE",
            url: url,
            success: success,
            error: error,
            statusCode: options.statusCode,
            complete: options.complete
        };

        return AeroGear.ajax( this, $.extend( {}, ajaxSettings, extraOptions ) );
    };
})( AeroGear, jQuery, uuid );

(function( AeroGear, $, undefined ) {
    /**
        A collection of data connections (stores) and their corresponding data models. This object provides a standard way to interact with client side data no matter the data format or storage mechanism used.
        @class
        @augments AeroGear.Core
        @param {String|Array|Object} [config] - A configuration for the store(s) being created along with the DataManager. If an object or array containing objects is used, the objects can have the following properties:
        @param {String} config.name - the name that the store will later be referenced by
        @param {String} [config.type="memory"] - the type of store as determined by the adapter used
        @param {String} [config.recordId="id"] - the identifier used to denote the unique id for each record in the data associated with this store
        @param {Object} [config.settings={}] - the settings to be passed to the adapter
        @returns {object} dataManager - The created DataManager containing any stores that may have been created
        @example
        // Create an empty DataManager
        var dm = AeroGear.DataManager();

        // Create a single store using the default adapter
        var dm2 = AeroGear.DataManager( "tasks" );

        // Create multiple stores using the default adapter
        var dm3 = AeroGear.DataManager( [ "tasks", "projects" ] );
     */
    AeroGear.DataManager = function( config ) {
        // Allow instantiation without using new
        if ( !( this instanceof AeroGear.DataManager ) ) {
            return new AeroGear.DataManager( config );
        }

        // Super Constructor
        AeroGear.Core.call( this );

        this.lib = "DataManager";
        this.type = config ? config.type || "Memory" : "Memory";

        /**
            The name used to reference the collection of data store instances created from the adapters
            @memberOf AeroGear.DataManager
            @type Object
            @default stores
         */
        this.collectionName = "stores";

        this.add( config );
    };

    AeroGear.DataManager.prototype = AeroGear.Core;
    AeroGear.DataManager.constructor = AeroGear.DataManager;

    /**
        The adapters object is provided so that adapters can be added to the AeroGear.DataManager namespace dynamically and still be accessible to the add method
        @augments AeroGear.DataManager
     */
    AeroGear.DataManager.adapters = {};

    // Constants
    AeroGear.DataManager.STATUS_NEW = 1;
    AeroGear.DataManager.STATUS_MODIFIED = 2;
    AeroGear.DataManager.STATUS_REMOVED = 0;
})( AeroGear, jQuery );

(function( AeroGear, $, uuid, undefined ) {
    /**
        The Memory adapter is the default type used when creating a new store. Data is simply stored in a data var and is lost on unload (close window, leave page, etc.)
        @constructs AeroGear.DataManager.adapters.Memory
        @param {String} storeName - the name used to reference this particular store
        @param {Object} [settings={}] - the settings to be passed to the adapter
        @param {String} [settings.recordId="id"] - the name of the field used to uniquely identify a "record" in the data
        @param {Boolean} [settings.dataSync=false] - if true, any pipes associated with this store will attempt to keep the data in sync with the server (coming soon)
        @returns {Object} The created store
     */
    AeroGear.DataManager.adapters.Memory = function( storeName, settings ) {
        // Allow instantiation without using new
        if ( !( this instanceof AeroGear.DataManager.adapters.Memory ) ) {
            return new AeroGear.DataManager.adapters.Memory( storeName, settings );
        }

        settings = settings || {};

        // Private Instance vars
        var recordId = settings.recordId ? settings.recordId : "id",
            type = "Memory",
            data = null,
            dataSync = !!settings.dataSync;

        // Privileged Methods
        /**
            Returns the value of the private recordId var
            @private
            @augments Memory
            @returns {String}
         */
        this.getRecordId = function() {
            return recordId;
        };

        /**
            Returns the value of the private data var, filtered by sync status if necessary
            @private
            @augments Memory
            @param {Boolean} [dataSyncBypass] - get all data no matter it's sync status
            @returns {Array}
         */
        this.getData = function( dataSyncBypass ) {
            var activeData = [],
                item,
                syncStatus;

            if ( dataSync && !dataSyncBypass ) {
                for ( item in data ) {
                    syncStatus = data[ item ][ "ag-sync-status" ];
                    if ( syncStatus !== AeroGear.DataManager.STATUS_REMOVED ) {
                        activeData.push( data[ item ] );
                    }
                }
                return activeData;
            } else {
                return data;
            }
        };

        /**
            Sets the value of the private data var
            @private
            @augments Memory
         */
        this.setData = function( newData ) {
            if ( dataSync ) {
                for ( var item in newData ) {
                    newData[ item ][ "ag-sync-status" ] = AeroGear.DataManager.STATUS_NEW;
                }
            }
            data = newData;
        };

        /**
            Empties the value of the private data var
            @private
            @augments Memory
         */
        this.emptyData = function() {
            if ( dataSync ) {
                for ( var item in data ) {
                    data[ item ][ "ag-sync-status" ] = AeroGear.DataManager.STATUS_REMOVED;
                }
            } else {
                data = null;
            }
        };

        /**
            Adds a record to the store's data set
            @private
            @augments Memory
         */
        this.addDataRecord = function( record ) {
            data = data || [];
            if ( dataSync ) {
                record[ "ag-sync-status" ] = AeroGear.DataManager.STATUS_NEW;
                record.id = record.id || uuid();
            }
            data.push( record );
        };

        /**
            Adds a record to the store's data set
            @private
            @augments Memory
         */
        this.updateDataRecord = function( index, record ) {
            if ( dataSync ) {
                record[ "ag-sync-status" ] = AeroGear.DataManager.STATUS_MODIFIED;
            }
            data[ index ] = record;
        };

        /**
            Removes a single record from the store's data set
            @private
            @augments Memory
         */
        this.removeDataRecord = function( index ) {
            if ( dataSync ) {
                data[ index ][ "ag-sync-status" ] = AeroGear.DataManager.STATUS_REMOVED;
            } else {
                data.splice( index, 1 );
            }
        };

        /**
            Returns the value of the private dataSync var
            @private
            @augments Memory
            @returns {Boolean}
         */
        this.getDataSync = function() {
            return dataSync;
        };

        /**
            Little utility used to compare nested object values in the filter method
            @private
            @augments Memory
            @param {String} nestedKey - Filter key to test
            @param {Object} nestedFilter - Filter object to test
            @param {Object} nestedValue - Value object to test
            @returns {Boolean}
         */
        this.traverseObjects = function( nestedKey, nestedFilter, nestedValue ) {
            while ( typeof nestedFilter === "object" ) {
                if ( nestedValue ) {
                    // Value contains this key so continue checking down the object tree
                    nestedKey = Object.keys( nestedFilter )[ 0 ];
                    nestedFilter = nestedFilter[ nestedKey ];
                    nestedValue = nestedValue[ nestedKey ];
                } else {
                    break;
                }
            }
            if ( nestedFilter === nestedValue ) {
                return true;
            } else {
                return false;
            }
        };
    };

    // Public Methods
    /**
        Read data from a store
        @param {String|Number} [id] - Usually a String or Number representing a single "record" in the data set or if no id is specified, all data is returned
        @returns {Array} Returns data from the store, optionally filtered by an id
        @example
        var dm = AeroGear.DataManager( "tasks" ).stores[ 0 ];

        // Get an array of all data in the store
        var allData = dm.read();
     */
    AeroGear.DataManager.adapters.Memory.prototype.read = function( id ) {
        var filter = {};
        filter[ this.getRecordId() ] = id;
        return id ? this.filter( filter ) : this.getData();
    };

    /**
        Saves data to the store, optionally clearing and resetting the data
        @param {Object|Array} data - An object or array of objects representing the data to be saved to the server. When doing an update, one of the key/value pairs in the object to update must be the `recordId` you set during creation of the store representing the unique identifier for a "record" in the data set.
        @param {Boolean} [reset] - If true, this will empty the current data and set it to the data being saved
        @returns {Array} Returns the updated data from the store
        @example
        var dm = AeroGear.DataManager( "tasks" ).stores[ 0 ];

        // Store a new task
        dm.save({
            title: "Created Task",
            date: "2012-07-13",
            ...
        });

        // Update an existing piece of data
        var toUpdate = dm.read()[ 0 ];
        toUpdate.data.title = "Updated Task";
        dm.save( toUpdate );
     */
    AeroGear.DataManager.adapters.Memory.prototype.save = function( data, reset ) {
        var itemFound = false;

        data = AeroGear.isArray( data ) ? data : [ data ];

        if ( reset ) {
            this.setData( data );
        } else {
            if ( this.getData() ) {
                for ( var i = 0; i < data.length; i++ ) {
                    for( var item in this.getData() ) {
                        if ( this.getData()[ item ][ this.getRecordId() ] === data[ i ][ this.getRecordId() ] ) {
                            this.updateDataRecord( item, data[ i ] );
                            itemFound = true;
                            break;
                        }
                    }
                    if ( !itemFound ) {
                        this.addDataRecord( data[ i ] );
                    }

                    itemFound = false;
                }
            } else {
                this.setData( data );
            }
        }

        return this.getData();
    };

    /**
        Removes data from the store
        @param {String|Object|Array} toRemove - A variety of objects can be passed to remove to specify the item or if nothing is provided, all data is removed
        @returns {Array} Returns the updated data from the store
        @example
        var dm = AeroGear.DataManager( "tasks" ).stores[ 0 ];

        // Store a new task
        dm.save({
            title: "Created Task"
        });

        // Store another new task
        dm.save({
            title: "Another Created Task"
        });

        // Store one more new task
        dm.save({
            title: "And Another Created Task"
        });

        // Remove a particular item from the store by its id
        var toRemove = dm.read()[ 0 ];
        dm.remove( toRemove.id );

        // Remove an item from the store using the data object
        toRemove = dm.read()[ 0 ];
        dm.remove( toRemove );

        // Delete all remaining data from the store
        dm.remove();
     */
    AeroGear.DataManager.adapters.Memory.prototype.remove = function( toRemove ) {
        if ( !toRemove ) {
            // empty data array and return
            this.emptyData();
            return this.getData();
        } else {
            toRemove = AeroGear.isArray( toRemove ) ? toRemove : [ toRemove ];
        }
        var delId,
            data,
            item;

        for ( var i = 0; i < toRemove.length; i++ ) {
            if ( typeof toRemove[ i ] === "string" || typeof toRemove[ i ] === "number" ) {
                delId = toRemove[ i ];
            } else if ( toRemove ) {
                delId = toRemove[ i ][ this.getRecordId() ];
            } else {
                // Missing record id so just skip this item in the arrray
                continue;
            }

            data = this.getData( true );
            for( item in data ) {
                if ( data[ item ][ this.getRecordId() ] === delId ) {
                    this.removeDataRecord( item );
                }
            }
        }

        return this.getData();
    };

    /**
        Filter the current store's data
        @param {Object} [filterParameters] - An object containing key value pairs on which to filter the store's data. To filter a single parameter on multiple values, the value can be an object containing a data key with an Array of values to filter on and its own matchAny key that will override the global matchAny for that specific filter parameter.
        @param {Boolean} [matchAny] - When true, an item is included in the output if any of the filter parameters is matched.
        @returns {Array} Returns a filtered array of data objects based on the contents of the store's data object and the filter parameters. This method only returns a copy of the data and leaves the original data object intact.
        @example
        var dm = AeroGear.DataManager( "tasks" ).stores[ 0 ];

        // An object can be passed to filter the data
        var filteredData = dm.filter({
            date: "2012-08-01"
            ...
        });
     */
    AeroGear.DataManager.adapters.Memory.prototype.filter = function( filterParameters, matchAny ) {
        var filtered, key, j, k, l, nestedKey, nestedFilter, nestedValue,
            that = this;

        if ( !filterParameters ) {
            filtered = this.getData() || [];
            return filtered;
        }

        filtered = this.getData().filter( function( value, index, array) {
            var match = matchAny ? false : true,
                keys = Object.keys( filterParameters ),
                filterObj, paramMatch, paramResult;

            for ( key = 0; key < keys.length; key++ ) {
                if ( filterParameters[ keys[ key ] ].data ) {
                    // Parameter value is an object
                    filterObj = filterParameters[ keys[ key ] ];
                    paramResult = filterObj.matchAny ? false : true;

                    for ( j = 0; j < filterObj.data.length; j++ ) {
                        if( AeroGear.isArray( value[ keys[ key ] ] ) ) {
                            if( value[ keys [ key ] ].length ) {
                                if( $( value[ keys ] ).not( filterObj.data ).length === 0 && $( filterObj.data ).not( value[ keys ] ).length === 0 ) {
                                    paramResult = true;
                                    break;
                                } else {
                                    for( k = 0; k < value[ keys[ key ] ].length; k++ ) {
                                        if ( filterObj.matchAny && filterObj.data[ j ] === value[ keys[ key ] ][ k ] ) {
                                            // At least one value must match and this one does so return true
                                            paramResult = true;
                                            if( matchAny ) {
                                                break;
                                            } else {
                                                for( l = 0; l < value[ keys[ key ] ].length; l++ ) {
                                                    if( !matchAny && filterObj.data[ j ] !== value[ keys[ key ] ][ l ] ) {
                                                        // All must match but this one doesn't so return false
                                                        paramResult = false;
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        if ( !filterObj.matchAny && filterObj.data[ j ] !== value[ keys[ key ] ][ k ] ) {
                                            // All must match but this one doesn't so return false
                                            paramResult = false;
                                            break;
                                        }
                                    }
                                }
                            } else {
                                paramResult = false;
                            }
                        } else {
                            if ( typeof filterObj.data[ j ] === "object" ) {
                                if ( filterObj.matchAny && that.traverseObjects( keys[ key ], filterObj.data[ j ], value[ keys[ key ] ] ) ) {
                                    // At least one value must match and this one does so return true
                                    paramResult = true;
                                    break;
                                }
                                if ( !filterObj.matchAny && !that.traverseObjects( keys[ key ], filterObj.data[ j ], value[ keys[ key ] ] ) ) {
                                    // All must match but this one doesn't so return false
                                    paramResult = false;
                                    break;
                                }
                            } else {
                                if ( filterObj.matchAny && filterObj.data[ j ] === value[ keys[ key ] ] ) {
                                    // At least one value must match and this one does so return true
                                    paramResult = true;
                                    break;
                                }
                                if ( !filterObj.matchAny && filterObj.data[ j ] !== value[ keys[ key ] ] ) {
                                    // All must match but this one doesn't so return false
                                    paramResult = false;
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    // Filter on parameter value
                    if( AeroGear.isArray( value[ keys[ key ] ] ) ) {
                        paramResult = matchAny ? false: true;

                        if( value[ keys[ key ] ].length ) {
                            for(j = 0; j < value[ keys[ key ] ].length; j++ ) {
                                if( matchAny && filterParameters[ keys[ key ] ] === value[ keys[ key ] ][ j ]  ) {
                                    //at least one must match and this one does so return true
                                    paramResult = true;
                                    break;
                                }
                                if( !matchAny && filterParameters[ keys[ key ] ] !== value[ keys[ key ] ][ j ] ) {
                                    //All must match but this one doesn't so return false
                                    paramResult = false;
                                    break;
                                }
                            }
                        } else {
                            paramResult = false;
                        }
                    } else {
                        if ( typeof filterParameters[ keys[ key ] ] === "object" ) {
                            paramResult = that.traverseObjects( keys[ key ], filterParameters[ keys[ key ] ], value[ keys[ key ] ] );
                        } else {
                            paramResult = filterParameters[ keys[ key ] ] === value[ keys[ key ] ] ? true : false;
                        }
                    }
                }

                if ( matchAny && paramResult ) {
                    // At least one item must match and this one does so return true
                    match = true;
                    break;
                }
                if ( !matchAny && !paramResult ) {
                    // All must match but this one doesn't so return false
                    match = false;
                    break;
                }
            }

            return match;
        });

        return filtered;
    };
})( AeroGear, jQuery, uuid );

(function( AeroGear, $, uuid, undefined ) {
    /**
        The SessionLocal adapter extends the Memory adapter to store data in either session or local storage which makes it a little more persistent than memory
        @constructs AeroGear.DataManager.adapters.SessionLocal
        @mixes AeroGear.DataManager.adapters.Memory
        @param {String} storeName - the name used to reference this particular store
        @param {Object} [settings={}] - the settings to be passed to the adapter
        @param {Boolean} [settings.dataSync=false] - if true, any pipes associated with this store will attempt to keep the data in sync with the server (coming soon)
        @param {String} [settings.recordId="id"] - the name of the field used to uniquely identify a "record" in the data
        @param {String} [settings.storageType="sessionStorage"] - the type of store can either be sessionStorage or localStorage
        @returns {Object} The created store
     */
    AeroGear.DataManager.adapters.SessionLocal = function( storeName, settings ) {
        // Allow instantiation without using new
        if ( !( this instanceof AeroGear.DataManager.adapters.SessionLocal ) ) {
            return new AeroGear.DataManager.adapters.SessionLocal( storeName, settings );
        }

        AeroGear.DataManager.adapters.Memory.apply( this, arguments );

        // Private Instance vars
        var data = null,
            type = "SessionLocal",
            storeType = settings.storageType || "sessionStorage",
            name = storeName,
            dataSync = settings.dataSync,
            appContext = document.location.pathname.replace(/[\/\.]/g,"-"),
            storeKey = name + appContext,
            currentData = JSON.parse( window[ storeType ].getItem( storeKey ) );

        // Initialize data from the persistent store if it exists
        if ( currentData ) {
            AeroGear.DataManager.adapters.Memory.prototype.save.call( this, currentData, true );
        }

        // Privileged Methods
        /**
            Returns the value of the private storeType var
            @private
            @augments SessionLocal
            @returns {String}
         */
        this.getStoreType = function() {
            return storeType;
        };

        /**
            Returns the value of the private storeKey var
            @private
            @augments SessionLocal
            @returns {String}
         */
        this.getStoreKey = function() {
            return storeKey;
        };
    };

    // Inherit from the Memory adapter
    AeroGear.DataManager.adapters.SessionLocal.prototype = Object.create( new AeroGear.DataManager.adapters.Memory(), {
        // Public Methods
        save: {
            value: function( data, options ) {
                // Call the super method
                AeroGear.DataManager.adapters.Memory.prototype.save.apply( this, arguments );

                // Sync changes to persistent store
                try {
                    window[ this.getStoreType() ].setItem( this.getStoreKey(), JSON.stringify( this.getData() ) );
                    if ( options && options.storageSuccess ) {
                        options.storageSuccess( data );
                    }
                } catch( error ) {
                    if ( options && options.storageError ) {
                        options.storageError( error, data );
                    } else {
                        throw error;
                    }
                }
            }, enumerable: true, configurable: true, writable: true
        },
        remove: {
            value: function( toRemove, options ) {
                // Call the super method
                AeroGear.DataManager.adapters.Memory.prototype.remove.apply( this, arguments );

                // Sync changes to persistent store
                window[ this.getStoreType() ].setItem( this.getStoreKey(), JSON.stringify( this.getData() ) );
            }, enumerable: true, configurable: true, writable: true
        }
    });
})( AeroGear, jQuery, uuid );

(function( AeroGear, $, undefined ) {
    /**
        The AeroGear.Auth namespace provides an authentication and enrollment API. Through the use of adapters, this library provides common methods like enroll, login and logout that will just work.
        @class
        @augments AeroGear.Core
        @param {String|Array|Object} [config] - A configuration for the modules(s) being created along with the authenticator. If an object or array containing objects is used, the objects can have the following properties:
        @param {String} config.name - the name that the module will later be referenced by
        @param {String} [config.type="rest"] - the type of module as determined by the adapter used
        @param {Object} [config.settings={}] - the settings to be passed to the adapter
        @returns {Object} The created authenticator containing any auth modules that may have been created
        @example
        // Create an empty authenticator
        var auth = AeroGear.Auth();

        // Create a single module using the default adapter
        var auth2 = AeroGear.Auth( "myAuth" );

        // Create multiple modules using the default adapter
        var auth3 = AeroGear.Auth( [ "someAuth", "anotherAuth" ] );
     */
    AeroGear.Auth = function( config ) {
        // Allow instantiation without using new
        if ( !( this instanceof AeroGear.Auth ) ) {
            return new AeroGear.Auth( config );
        }
        // Super Constructor
        AeroGear.Core.call( this );

        this.lib = "Auth";
        this.type = config ? config.type || "Rest" : "Rest";

        /**
            The name used to reference the collection of authentication module instances created from the adapters
            @memberOf AeroGear.Auth
            @type Object
            @default modules
         */
        this.collectionName = "modules";

        this.add( config );
    };

    AeroGear.Auth.prototype = AeroGear.Core;
    AeroGear.Auth.constructor = AeroGear.Auth;

    /**
        The adapters object is provided so that adapters can be added to the AeroGear.Auth namespace dynamically and still be accessible to the add method
        @augments AeroGear.Auth
     */
    AeroGear.Auth.adapters = {};
})( AeroGear, jQuery );

(function( AeroGear, $, undefined ) {
    /**
        The REST adapter is the default type used when creating a new authentication module. It uses jQuery.ajax to communicate with the server.
        @constructs AeroGear.Auth.adapters.Rest
        @param {String} moduleName - the name used to reference this particular auth module
        @param {Object} [settings={}] - the settings to be passed to the adapter
        @param {Boolean} [settings.agAuth] - True if this adapter should use AeroGear's token based authentication model
        @param {String} [settings.baseURL] - defines the base URL to use for an endpoint
        @param {Object} [settings.endpoints={}] - a set of REST endpoints that correspond to the different public methods including enroll, login and logout
        @param {String} [settings.tokenName="Auth-Token"] - defines the name used for the token header when using agAuth
        @returns {Object} The created auth module
     */
    AeroGear.Auth.adapters.Rest = function( moduleName, settings ) {
        // Allow instantiation without using new
        if ( !( this instanceof AeroGear.Auth.adapters.Rest ) ) {
            return new AeroGear.Auth.adapters.Rest( moduleName, settings );
        }

        settings = settings || {};

        // Private Instance vars
        var endpoints = settings.endpoints || {},
            type = "Rest",
            name = moduleName,
            agAuth = !!settings.agAuth,
            baseURL = settings.baseURL,
            tokenName = settings.tokenName || "Auth-Token";

        // Privileged methods
        /**
            Return whether or not the client should consider itself authenticated. Of course, the server may have removed access so that will have to be handled when a request is made
            @private
            @augments Rest
            @returns {Boolean}
         */
        this.isAuthenticated = function() {
            if ( agAuth ) {
                return !!sessionStorage.getItem( "ag-auth-" + name );
            } else {
                // For the default (rest) adapter, we assume if not using agAuth then session so auth will be handled server side
                return true;
            }
        };

        /**
            Adds the auth token to the headers and returns the modified version of the settings
            @private
            @augments Rest
            @param {Object} settings - the settings object that will have the auth identifier added
            @returns {Object} Settings extended with auth identifier
         */
        this.addAuthIdentifier = function( settings ) {
            settings.headers = {};
            settings.headers[ tokenName ] = sessionStorage.getItem( "ag-auth-" + name );
            return $.extend( {}, settings );
        };

        /**
            Removes the stored token effectively telling the client it must re-authenticate with the server
            @private
            @augments Rest
         */
        this.deauthorize = function() {
            sessionStorage.removeItem( "ag-auth-" + name );
        };


        /**
            Returns the value of the private settings var
            @private
            @augments Rest
         */
        this.getSettings = function() {
            return settings;
        };


        /**
            Returns the value of the private settings var
            @private
            @augments Rest
         */
        this.getEndpoints = function() {
            return endpoints;
        };

        /**
            Returns the value of the private name var
            @private
            @augments Rest
         */
        this.getName = function() {
            return name;
        };

        /**
            Returns the value of the private agAuth var which determines whether or not the AeroGear style authentication token should be used
            @private
            @augments Rest
         */
        this.getAGAuth = function() {
            return agAuth;
        };

        /**
            Returns the value of the private baseURL var
            @private
            @augments Rest
         */
        this.getBaseURL = function() {
            return baseURL;
        };

        /**
            Returns the value of the private tokenName var
            @private
            @augments Rest
         */
        this.getTokenName = function() {
            return tokenName;
        };
    };

    //Public Methods
    /**
        Enroll a new user in the authentication system
        @param {Object} data - User profile to enroll
        @param {Object} [options={}] - Options to pass to the enroll method
        @param {String} [options.baseURL] - defines the base URL to use for an endpoint
        @param {String} [options.contentType] - set the content type for the AJAX request (defaults to application/json when using agAuth)
        @param {String} [options.dataType] - specify the data expected to be returned by the server (defaults to json when using agAuth)
        @param {Function} [options.error] - callback to be executed if the AJAX request results in an error
        @param {Function} [options.success] - callback to be executed if the AJAX request results in success
        @returns {Object} The jqXHR created by jQuery.ajax
        @example
        var auth = AeroGear.Auth( "userAuth" ).modules[ 0 ],
            data = { userName: "user", password: "abc123", name: "John" };

        // Enroll a new user
        auth.enroll( data );
     */
    AeroGear.Auth.adapters.Rest.prototype.enroll = function( data, options ) {
        options = options || {};

        var that = this,
            name = this.getName(),
            tokenName = this.getTokenName(),
            baseURL = this.getBaseURL(),
            endpoints = this.getEndpoints(),
            agAuth = this.getAGAuth(),
            success = function( data, textStatus, jqXHR ) {
                sessionStorage.setItem( "ag-auth-" + name, that.getAGAuth() ? jqXHR.getResponseHeader( tokenName ) : "true" );

                if ( options.success ) {
                    options.success.apply( this, arguments );
                }
            },
            error = function( jqXHR, textStatus, errorThrown ) {
                var args;

                try {
                    jqXHR.responseJSON = JSON.parse( jqXHR.responseText );
                    args = [ jqXHR, textStatus, errorThrown ];
                } catch( error ) {
                    args = arguments;
                }

                if ( options.error ) {
                    options.error.apply( this, args );
                }
            },
            extraOptions = {
                success: success,
                error: error,
                data: data
            },
            url = "";

        if ( options.contentType ) {
            extraOptions.contentType = options.contentType;
        } else if ( agAuth ) {
            extraOptions.contentType = "application/json";
        }
        if ( options.dataType ) {
            extraOptions.dataType = options.dataType;
        } else if ( agAuth ) {
            extraOptions.dataType = "json";
        }
        if ( options.baseURL ) {
            url = options.baseURL;
        } else if ( baseURL ) {
            url = baseURL;
        }
        if ( endpoints.enroll ) {
            url += endpoints.enroll;
        } else {
            url += "auth/enroll";
        }
        if ( url.length ) {
            extraOptions.url = url;
        }

        return $.ajax( $.extend( {}, this.getSettings(), { type: "POST" }, extraOptions ) );
    };

    /**
        Authenticate a user
        @param {Object} data - A set of key value pairs representing the user's credentials
        @param {Object} [options={}] - An object containing key/value pairs representing options
        @param {String} [options.baseURL] - defines the base URL to use for an endpoint
        @param {String} [options.contentType] - set the content type for the AJAX request (defaults to application/json when using agAuth)
        @param {String} [options.dataType] - specify the data expected to be returned by the server (defaults to json when using agAuth)
        @param {Function} [options.error] - callback to be executed if the AJAX request results in an error
        @param {String} [options.success] - callback to be executed if the AJAX request results in success
        @returns {Object} The jqXHR created by jQuery.ajax
        @example
        var auth = AeroGear.Auth( "userAuth" ).modules[ 0 ],
            data = { userName: "user", password: "abc123" };

        // Enroll a new user
        auth.login( data );
     */
    AeroGear.Auth.adapters.Rest.prototype.login = function( data, options ) {
        options = options || {};

        var that = this,
            name = this.getName(),
            tokenName = this.getTokenName(),
            baseURL = this.getBaseURL(),
            endpoints = this.getEndpoints(),
            agAuth = this.getAGAuth(),
            success = function( data, textStatus, jqXHR ) {
                sessionStorage.setItem( "ag-auth-" + name, that.getAGAuth() ? jqXHR.getResponseHeader( tokenName ) : "true" );

                if ( options.success ) {
                    options.success.apply( this, arguments );
                }
            },
            error = function( jqXHR, textStatus, errorThrown ) {
                var args;

                try {
                    jqXHR.responseJSON = JSON.parse( jqXHR.responseText );
                    args = [ jqXHR, textStatus, errorThrown ];
                } catch( error ) {
                    args = arguments;
                }

                if ( options.error ) {
                    options.error.apply( this, args );
                }
            },
            extraOptions = {
                success: success,
                error: error,
                data: data
            },
            url = "";

        if ( options.contentType ) {
            extraOptions.contentType = options.contentType;
        } else if ( agAuth ) {
            extraOptions.contentType = "application/json";
        }
        if ( options.dataType ) {
            extraOptions.dataType = options.dataType;
        } else if ( agAuth ) {
            extraOptions.dataType = "json";
        }
        if ( options.baseURL ) {
            url = options.baseURL;
        } else if ( baseURL ) {
            url = baseURL;
        }
        if ( endpoints.login ) {
            url += endpoints.login;
        } else {
            url += "auth/login";
        }
        if ( url.length ) {
            extraOptions.url = url;
        }

        return $.ajax( $.extend( {}, this.getSettings(), { type: "POST" }, extraOptions ) );
    };

    /**
        End a user's authenticated session
        @param {Object} [options={}] - An object containing key/value pairs representing options
        @param {String} [options.baseURL] - defines the base URL to use for an endpoint
        @param {Function} [options.error] - callback to be executed if the AJAX request results in an error
        @param {String} [options.success] - callback to be executed if the AJAX request results in success
        @returns {Object} The jqXHR created by jQuery.ajax
        @example
        var auth = AeroGear.Auth( "userAuth" ).modules[ 0 ];

        // Enroll a new user
        auth.logout();
     */
    AeroGear.Auth.adapters.Rest.prototype.logout = function( options ) {
        options = options || {};

        var that = this,
            name = this.getName(),
            tokenName = this.getTokenName(),
            baseURL = this.getBaseURL(),
            endpoints = this.getEndpoints(),
            success = function( data, textStatus, jqXHR ) {
                that.deauthorize();

                if ( options.success ) {
                    options.success.apply( this, arguments );
                }
            },
            error = function( jqXHR, textStatus, errorThrown ) {
                var args;

                try {
                    jqXHR.responseJSON = JSON.parse( jqXHR.responseText );
                    args = [ jqXHR, textStatus, errorThrown ];
                } catch( error ) {
                    args = arguments;
                }

                if ( options.error ) {
                    options.error.apply( this, args );
                }
            },
            extraOptions = {
                success: success,
                error: error
            },
            url = "";

        if ( options.baseURL ) {
            url = options.baseURL;
        } else if ( baseURL ) {
            url = baseURL;
        }
        if ( endpoints.logout ) {
            url += endpoints.logout;
        } else {
            url += "auth/logout";
        }
        if ( url.length ) {
            extraOptions.url = url;
        }

        if ( this.isAuthenticated() ) {
            extraOptions.headers = {};
            extraOptions.headers[ tokenName ] = sessionStorage.getItem( "ag-auth-" + name );
        }

        return $.ajax( $.extend( {}, this.getSettings(), { type: "POST" }, extraOptions ) );
    };
})( AeroGear, jQuery );
