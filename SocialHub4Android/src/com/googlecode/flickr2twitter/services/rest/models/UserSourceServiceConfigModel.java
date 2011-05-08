/**
 * 
 */
package com.googlecode.flickr2twitter.services.rest.models;

import java.util.Map;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class UserSourceServiceConfigModel extends UserServiceConfigModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String serviceAccessToken;

	/**
	 * 
	 */
	public UserSourceServiceConfigModel() {
		super();
	}

	/**
	 * @param userEmail
	 * @param serviceUserId
	 * @param serviceUserName
	 * @param userSiteUrl
	 * @param serviceProviderId
	 * @param additionalParamsPersitent
	 * @param additionalParameters
	 * @param enabled
	 */
	public UserSourceServiceConfigModel(String userEmail, String serviceUserId,
			String serviceUserName, String userSiteUrl,
			String serviceProviderId, 
			Map<String, String> additionalParameters, boolean enabled, String serviceAccessToken) {
		super(userEmail, serviceUserId, serviceUserName, userSiteUrl,
				serviceProviderId, additionalParameters, enabled);
		this.serviceAccessToken = serviceAccessToken;
	}

	/**
	 * @return the serviceAccessToken
	 */
	public String getServiceAccessToken() {
		return serviceAccessToken;
	}

	/**
	 * @param serviceAccessToken the serviceAccessToken to set
	 */
	public void setServiceAccessToken(String serviceAccessToken) {
		this.serviceAccessToken = serviceAccessToken;
	}

	/* (non-Javadoc)
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
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof UserSourceServiceConfigModel))
			return false;
		UserSourceServiceConfigModel other = (UserSourceServiceConfigModel) obj;
		if (serviceAccessToken == null) {
			if (other.serviceAccessToken != null)
				return false;
		} else if (!serviceAccessToken.equals(other.serviceAccessToken))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserSourceServiceConfigModel [serviceAccessToken="
				+ serviceAccessToken + ", toString()=" + super.toString() + "]";
	}

	
}
