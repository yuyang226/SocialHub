/**
 * 
 */
package com.googlecode.flickr2twitter.datastore.model;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 * 
 */
@PersistenceCapable
public class UserTargetServiceConfig extends UserServiceConfig implements
		Serializable {

	private static final long serialVersionUID = 1L;

	@Persistent
	private String serviceAccessToken;

	@Persistent
	private String serviceTokenSecret;

	/**
	 * 
	 */
	public UserTargetServiceConfig() {
		super();
	}

	public String getServiceAccessToken() {
		return serviceAccessToken;
	}

	public void setServiceAccessToken(String serviceAccessToken) {
		this.serviceAccessToken = serviceAccessToken;
	}

	public String getServiceTokenSecret() {
		return serviceTokenSecret;
	}

	public void setServiceTokenSecret(String serviceTokenSecret) {
		this.serviceTokenSecret = serviceTokenSecret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((serviceAccessToken == null) ? 0 : serviceAccessToken
						.hashCode());
		result = prime
				* result
				+ ((serviceTokenSecret == null) ? 0 : serviceTokenSecret
						.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof UserTargetServiceConfig))
			return false;
		UserTargetServiceConfig other = (UserTargetServiceConfig) obj;
		if (serviceAccessToken == null) {
			if (other.serviceAccessToken != null)
				return false;
		} else if (!serviceAccessToken.equals(other.serviceAccessToken))
			return false;
		if (serviceTokenSecret == null) {
			if (other.serviceTokenSecret != null)
				return false;
		} else if (!serviceTokenSecret.equals(other.serviceTokenSecret))
			return false;
		return true;
	}

}
