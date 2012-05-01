/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual
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
package org.jboss.as.quickstarts.payment.events;

import java.math.BigDecimal;
import java.util.Date;


public class PaymentEvent {

	private PaymentTypeEnum type;  //credit or debit
	private BigDecimal amount;
	private Date datetime;
	
	
	public BigDecimal getAmount() {
		return amount;
	}
	public PaymentTypeEnum getType() {
		return type;
	}
	public void setType(PaymentTypeEnum type) {
		this.type = type;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Date getDatetime() {
		return datetime;
	}
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public String toString(){
		return "EVT:"+getDatetime()+":$"+getAmount()+":"+getType();
	}
	
	
	
}
