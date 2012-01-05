package org.jboss.as.quickstarts.payment.handler;
/**
 * 
 * @author Elvadas-Nono
 *
 */


import javax.enterprise.event.Observes;

import org.jboss.as.quickstarts.payment.events.PaymentEvent;
import org.jboss.as.quickstarts.payment.qualifiers.Debit;

public interface IDebitEventObserver {
	
	public void onDebitPaymentEvent(@Observes @Debit PaymentEvent event);

}
