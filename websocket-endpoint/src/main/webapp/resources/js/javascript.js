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
function updateBid(bidding) {
    $('#title').text(bidding.item.title);
    $('#status').text(bidding.bidStatus);
    $('#description').text(bidding.item.description);
    $('#img').attr('src', bidding.item.imagePath);
    $('#buyItNowPrice').text(bidding.item.buyNowPrice);
    $('#currentPrice').text(
            'Current price: USD ' + bidding.currentPrice + '.00');
    var time = secondsToTime(bidding.secondsLeft);
    $('#secondsLeft').text(
            'Time left:  ' + time.h + 'h:' + time.m + 'm:' + time.s + 's');

    if (bidding.secondsLeft < 10 && bidding.secondsLeft >= 1) {
        $('#secondsLeft').attr('style', 'color: red');
    } else {
        $('#secondsLeft').removeAttr('style', 'color: red');
    }

    var status = $('#status').text();
    if (status == 'EXPIRED' || status == 'SOLD') {
        $('#buyItNowButton').attr("disabled", "disabled");
        $('#doBidButton').attr("disabled", "disabled");
        $('#status').attr('style', 'color: red');
    } else {
        $('#buyItNowButton').removeAttr("disabled", "disabled");
        $('#doBidButton').removeAttr("disabled", "disabled");
        $('#status').attr('style', 'color: blue');
    }
    updateTable(bidding.bids);
}

function updateTable(bids) {
    var tbody = $('#bids tbody');
    tbody.empty();
    if (bids.length === 0) {
        $('#bids thead').attr('style', 'visibility:hidden');
    } else {
        $('#bids thead').attr('style', 'visibility:visible');
        $.each(bids, function(i, bid) {
            var tr = $('<tr>');
            $('<td>').html(new Date(bid.dateTime)).appendTo(tr);
            $('<td>').html('USD ' + bid.value + '.00').appendTo(tr);
            $('<td>').html(bid.id).appendTo(tr);
            tbody.append(tr);
        });
    }
}

function secondsToTime(secs) {
    secs = Math.round(secs);
    var hours = Math.floor(secs / (60 * 60));

    var divisor_for_minutes = secs % (60 * 60);
    var minutes = Math.floor(divisor_for_minutes / 60);

    var divisor_for_seconds = divisor_for_minutes % 60;
    var seconds = Math.ceil(divisor_for_seconds);

    var obj = {
        "h" : hours,
        "m" : minutes,
        "s" : seconds
    };
    return obj;
}

function doBid(event) {
    // Check if form is valid
    if ($('#formBid')[0].checkValidity()) {
        // Get the bid value
        value = $('#bidValue').val();
        // Submit the value to Websocket endpoint
        sendWebSocketMessage("newBid", value);
    }
}

function waitForSocketConnection(socket, callback) {
    setTimeout(function() {
        if (socket.readyState === 1) {
            if (callback !== undefined) {
                callback();
            }
            return;
        } else {
            waitForSocketConnection(socket, callback);
        }
    }, 5);
};

function openWebSocket() {
    var loc = window.location
    var port = loc.port;
    // Openshift WS port is 8000
    if (loc.hostname.indexOf('rhcloud.com', 0) > 0) {
        port = 8000;
    }
    var wsProtocol = window.location.protocol == "https:" ? "wss" : "ws";
    var wsurl = wsProtocol + "://" + loc.hostname + ':' + port + loc.pathname
            + "/../bidsocket";
    wsocket = new WebSocket(wsurl);
    wsocket.onmessage = function(evt) {
        var json = JSON.parse(evt.data);
        console.log(json);
        updateBid(json);
    }
    wsocket.onerror = function(evt) {
        console.error(evt);
    }
    waitForSocketConnection(wsocket, function() {
        sendWebSocketMessage('connect');
    });
}

function sendWebSocketMessage(command, bidValue) {
    jsonMsg = "{\"command\": \"" + command + "\"";
    if (bidValue > 0) {
        jsonMsg += ", " + "\"bidValue\": " + bidValue;
    }
    jsonMsg += "}";
    wsocket.send(jsonMsg);
}

var wsocket;
$(document).ready(openWebSocket());
