/**
 * 
 */
package com.googlecode.flickr2twitter.exceptions;

/**
 * @author Meng Zang (DeepNightTwo@gmail.com)
 * 
 */
public class TokenAlreadyRegisteredException extends Exception {

	private static final long serialVersionUID = 1L;

	private String token;

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token
	 *            the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	private String userName;

	public TokenAlreadyRegisteredException(String token, String userName) {
		super();
		this.token = token;
		this.userName = userName;
	}

	public TokenAlreadyRegisteredException(String message, Throwable cause) {
		super(message, cause);
	}

	public TokenAlreadyRegisteredException(String message) {
		super(message);
	}

	public TokenAlreadyRegisteredException(Throwable cause) {
		super(cause);
	}

}
