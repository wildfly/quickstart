package org.jboss.as.quickstarts.kitchensink.web;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.qualifiers.NewMember;

/**
 * <p>
 * Registration bean holds the new member entity and exposes it to view layer.
 * </p>
 *
 * <p>
 * It contains JSF-specific logic for handling view transition.
 * </p>
 *
 * @author Lukas Fryc
 */
@SessionScoped
@Named("registration")
public class RegistrationBean implements Serializable {

    private static final long serialVersionUID = -1L;

    private Member member = new Member();

    /**
     * Exposes new member entity to the other beans and view layer.
     */
    @Produces
    @NewMember
    @Named
    public Member getNewMember() {
        return member;
    }

    @Inject
    Logger logger;

    /**
     * <p>
     * This method should invoke persistence layer, but in this sample it only logs successfuly registration.
     * </p>
     *
     * <p>
     * Then it contains JSF-specific code which adds message about successful registration.
     * </p>
     *
     * <p>
     * Messages are then saved to the flash scope to be endure between redirect.
     * </p>
     *
     * <p>
     * At the end, the outcome is provided to redirect to index page.
     * </p>
     */
    public String proceed() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Flash flash = facesContext.getExternalContext().getFlash();

        // save the member to the database
        // e.g.: entityManager.persist(member);

        logger.info("registered member '" + member.getEmail() + "'");

        // add message
        facesContext
                .addMessage("registrationWizard", new FacesMessage("Hello " + member.getName() + ", you have been successfully registered"));

        // setup JSF to keep message to next request (using flash-scope)
        flash.setKeepMessages(true);

        // reset the member registration data
        member = new Member();

        // redirect to index page
        return "index?faces-redirect=true";
    }
}
