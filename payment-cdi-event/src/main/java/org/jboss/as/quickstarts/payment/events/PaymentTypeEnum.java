package org.jboss.as.quickstarts.payment.events;

import java.util.HashMap;
import java.util.Map;

public enum PaymentTypeEnum {
	
	CREDIT("1"),
	DEBIT("2");
	
	private final String value;

	
	static Map <String,PaymentTypeEnum> map = new HashMap<String,PaymentTypeEnum>();
	
	static {
	for (PaymentTypeEnum paymentType : PaymentTypeEnum.values()) {
	    map.put(paymentType.getValue(), paymentType);
	}
	}		
	
	private PaymentTypeEnum (String value){
		this.value=value;
	}
	
	public String getValue(){
		return value;
	}
	
	public static PaymentTypeEnum fromString(String value){
		return map.get(value);
	}
}
