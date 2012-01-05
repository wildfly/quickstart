package org.jboss.as.quickstarts.payment.handler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.as.quickstarts.payment.events.PaymentEvent;
import org.jboss.as.quickstarts.payment.qualifiers.Credit;
import org.jboss.as.quickstarts.payment.qualifiers.Debit;

@SessionScoped
public class PaymentHandler implements Serializable,ICreditEventObserver, IDebitEventObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private Logger logger;
	
	List<PaymentEvent> payments=new ArrayList<PaymentEvent>();
	
	@Produces
	@Named
	public List<PaymentEvent> getPayments() {
		return payments;
	}



	public void onCreditPaymentEvent(@Observes @Credit PaymentEvent event){

		logger.info("Processing the credit operation "+event);
		payments.add(event);
	}

	
	
	@Override
	public void onDebitPaymentEvent(@Observes @Debit PaymentEvent event) {
		logger.info("Processing the debit operation "+event);
		payments.add(event);
		
	}

}
