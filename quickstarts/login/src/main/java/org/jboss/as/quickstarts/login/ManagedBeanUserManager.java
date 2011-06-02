package org.jboss.as.quickstarts.login;

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

@Named("userManager")
@RequestScoped
public class ManagedBeanUserManager implements UserManager {

	@Inject
	private transient Logger logger;

	@Inject
	private EntityManager userDatabase;

	@Inject
	private UserTransaction utx;

	private User newUser = new User();

	@SuppressWarnings("unchecked")
	@Produces
	@Named
	@RequestScoped
	public List<User> getUsers() throws Exception {
		try {
			try {
				utx.begin();
				return userDatabase.createQuery("select u from User u")
						.getResultList();
			} finally {
				utx.commit();
			}
		} catch (Exception e) {
			utx.rollback();
			throw e;
		}
	}

	public String addUser() throws Exception {
		try {
			try {
				utx.begin();
				userDatabase.persist(newUser);
				logger.info("Added " + newUser);
			} finally {
				utx.commit();
			}
		} catch (Exception e) {
			utx.rollback();
			throw e;
		}
		return "userAdded";
	}

	public User findUser(String username, String password) throws Exception {
		try {
			try {
				utx.begin();
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
			} finally {
				utx.commit();
			}
		} catch (Exception e) {
			utx.rollback();
			throw e;
		}
	}

	@Produces
	@RequestScoped
	@Named
	public User getNewUser() {
		return newUser;
	}

}
