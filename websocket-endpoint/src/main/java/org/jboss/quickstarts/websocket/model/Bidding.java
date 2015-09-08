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
package org.jboss.quickstarts.websocket.model;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.TreeSet;

//This class represents the Bidding of an item
public class Bidding {

    static final long ONE_MINUTE_IN_MILLIS = 60000;// millisecs

    private Item item = null;

    private BidStatus bidStatus = BidStatus.NOT_STARTED;

    private Date dueDate = null;

    private Integer currentPrice = null;

    private Integer secondsLeft = null;

    // The bids uses a comparator to order it based on the its date.
    private Set<Bid> bids = new TreeSet<Bid>(new Comparator<Bid>() {

        @Override
        public int compare(Bid o1, Bid o2) {
            return o2.getDateTime().compareTo(o1.getDateTime());
        }
    });

    public Bidding(Item item, Integer currentPrice) {
        this.item = item;
        this.currentPrice = currentPrice;
    }

    public BidStatus getBidStatus() {
        return bidStatus;
    }

    public Item getItem() {
        return item;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public Set<Bid> getBids() {
        return bids;
    }

    public void addBid(Bid bid) {
        this.getBids().add(bid);
        // Just update the price if the bidding is not SOLD or EXPIRED
        if (!bidStatus.equals(BidStatus.SOLD) || !bidStatus.equals(BidStatus.EXPIRED)) {
            this.currentPrice = getCurrentPrice() + bid.getValue();
        }
        // Update the status and due date if the bidding had not started yet
        if (getBidStatus().equals(BidStatus.NOT_STARTED)) {
            this.bidStatus = BidStatus.STARTED;
            long now = new Date().getTime();
            this.dueDate = new Date(now + (1 * ONE_MINUTE_IN_MILLIS));
        }
        // change the status to SOLD if the bidding is more than the buyNowPrice
        if (getCurrentPrice() > getItem().getBuyNowPrice()) {
            bidStatus = BidStatus.SOLD;
        }
    }

    public void expire() {
        this.bidStatus = BidStatus.EXPIRED;
    }

    public Integer getCurrentPrice() {
        return currentPrice;
    }

    // Sell the item using its item "buy now" price
    public void buyItNow() {
        if (getBidStatus().equals(BidStatus.STARTED) || getBidStatus().equals(BidStatus.NOT_STARTED)) {
            bidStatus = BidStatus.SOLD;
            currentPrice = item.getBuyNowPrice();
        }
    }

    // calculate how much seconds left to the bidding to become EXPIRED
    public Integer getSecondsLeft() {
        if (getBidStatus().equals(BidStatus.STARTED)) {
            Calendar now = new GregorianCalendar();
            secondsLeft = (int) ((getDueDate().getTime() - now.getTime().getTime()) / 1000L);
            return secondsLeft;
        } else {
            return null;
        }
    }

}
