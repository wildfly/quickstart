package org.jboss.as.quickstarts.login;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Named("userManager")
@RequestScoped
@Alternative
@Stateful
public class EJBUserManager implements UserManager {

	@Inject
	private transient Logger logger;

	@Inject
	private EntityManager userDatabase;

	private User newUser = new User();

	@SuppressWarnings("unchecked")
	@Produces
	@Named
	@RequestScoped
	@Alternative
	public List<User> getUsers() throws Exception {
		return userDatabase.createQuery("select u from User u").getResultList();
	}

	public String addUser() throws Exception {
		userDatabase.persist(newUser);
		logger.info("Added " + newUser);
		return "userAdded";
	}

	public User findUser(String username, String password) throws Exception {
		@SuppressWarnings("unchecked")
		List<User> results = userDatabase
				.createQuery(
						"select u from User u where u.username=:username and u.password=:password")
				.setParameter("username", username)
				.setParameter("password", password).getResultList();
		if (results.isEmpty()) {
			return null;
		} else if (results.size() > 1) {
			throw new IllegalStateException(
					"Cannot have more than one user with the same username!");
		} else {
			return results.get(0);
		}
	}

	@Produces
	@RequestScoped
	@Named
	@Alternative
	public User getNewUser() {
		return newUser;
	}

}
