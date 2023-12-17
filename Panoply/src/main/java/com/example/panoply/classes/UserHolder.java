package com.example.panoply.classes;

/**
 * @author Ayoob Florival
 */
public final class UserHolder {

	private final static UserHolder INSTANCE = new UserHolder();
	private User user;

	private UserHolder() {
	}

	/**
	 * Instance getter
	 *
	 * @return Instance of userholder
	 */
	public static UserHolder getINSTANCE() {
		return INSTANCE;
	}

	/**
	 * getter for the instance user
	 *
	 * @return instance user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * setter for the instance user
	 *
	 * @param user user object to which to hold in UserHolder
	 */
	public void setUser(User user) {
		this.user = user;
	}
}
