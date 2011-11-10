package org.jboss.as.quickstarts.kitchensink.controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.as.quickstarts.kitchensink.model.Member;

// The @Stateful annotation eliminates the need for manual transaction demarcation
@Stateful
// The @Model stereotype is a convenience mechanism to make this a
// request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation
@Model
public class MemberRegistration {

	@Inject
	private Logger log;

	@Inject
	private EntityManager em;

	@Inject
	private Event<Member> memberEventSrc;

	private Member newMember;

	// Text that begins the Exception message when you try to add a duplicate email address 
	private static final String DUPLICATE_KEY_ERROR = "Unique index or primary key violation";

	@Produces
	@Named
	/**
	 * Returns a new Member instance
	 * @return		The new member
	 */
	public Member getNewMember() {
		return newMember;
	}

	/**
	 * Adds the data entered on the form to the database.
	 * 
	 */
	public void register()  {
		log.info("Registering " + newMember.getName());

		// Clear any previous error message
		newMember.setErrorMsg(null);
		try {
			em.persist(newMember);
			memberEventSrc.fire(newMember);
			initNewMember();
		} catch (javax.persistence.PersistenceException jpEx) {
			log.log(Level.SEVERE, jpEx.getLocalizedMessage(), jpEx);
			// Find the cause of this exception to get the true error. Default to
			// this exception message in the event that there is no root cause.
			String msgCause = jpEx.getLocalizedMessage();
			Throwable eCause = jpEx.getCause();
			if (eCause != null) {
				msgCause = eCause.getLocalizedMessage();
			}			
			ResourceBundle resourceBundle = ResourceBundle.getBundle(
					"quickstarts.bundle.Resources", FacesContext
							.getCurrentInstance().getViewRoot().getLocale());
			// Set up a buffer for the error
			StringBuilder errorMessage = new StringBuilder(512);
			if (msgCause.startsWith(DUPLICATE_KEY_ERROR)) {
				// This is an obvious error: entering an email address already on record
				errorMessage.append((String) resourceBundle.getObject("duplicateKeyMsg"));
			} else {
				// This error needs more details
				errorMessage.append((String) resourceBundle.getObject("registerFailMsg"));
				errorMessage.append(System.getProperty("line.separator"));
				errorMessage.append(msgCause);
			}
			// Set the error text in the form 
			newMember.setErrorMsg(errorMessage.toString());
			// Notify the form of the error.
			addErrorMessage(errorMessage.toString());
		} catch (Exception e) {
			log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			ResourceBundle resourceBundle = ResourceBundle.getBundle(
					"quickstarts.bundle.Resources", FacesContext
							.getCurrentInstance().getViewRoot().getLocale());
			String errorMessage = (String) resourceBundle.getObject("unknownErrorMsg");
			// Set the error text in the form 
			newMember.setErrorMsg(errorMessage);
			// Notify the form of the error.
			addErrorMessage(errorMessage);
		}
	}

	@PostConstruct
	public void initNewMember() {
		newMember = new Member();
	}

	/**
	 * Let the JSF know that an error occurred by adding the 
	 * error message to the FacesContext
	 * 
	 * @param errorMessage		The message describing the error 
	 */
	public void addErrorMessage(final String errorMessage) {
		UIComponent errorMessageId = findComponent(FacesContext
				.getCurrentInstance().getViewRoot(), "errorMessageId");
		String clientId = errorMessageId.getClientId(FacesContext
				.getCurrentInstance());
		final FacesMessage message = new FacesMessage();
		message.setSeverity(FacesMessage.SEVERITY_ERROR);
		message.setSummary(errorMessage);
		FacesContext.getCurrentInstance().addMessage(clientId, message);

	}

	/**
	 * Find the component in the JSF form.
	 * @param parent	The parent component
	 * @param id		The ID of the component we are looking for
	 * @return			The component with the matching ID
	 */
	private UIComponent findComponent(UIComponent parent, String id) {
		System.out.println("findComponent: parent = " + parent + ", id = " + id);
		if (id.equals(parent.getId())) {
			return parent;
		}
		Iterator<UIComponent> children = parent.getFacetsAndChildren();
		while (children.hasNext()) {
			UIComponent child = (UIComponent) children.next();
			UIComponent found = findComponent(child, id);
			if (found != null) {
				return found;
			}
		}
		return null;
	}

}
