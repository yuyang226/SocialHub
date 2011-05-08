/**
 * 
 */
package com.googlecode.flickr2twitter.services.rest.models;

import java.util.Map;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class UserTargetServiceConfigModel extends UserServiceConfigModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String serviceAccessToken;

	private String serviceTokenSecret;

	/**
	 * 
	 */
	public UserTargetServiceConfigModel() {
		super();
	}

	/**
	 * @param userEmail
	 * @param serviceUserId
	 * @param serviceUserName
	 * @param userSiteUrl
	 * @param serviceProviderId
	 * @param additionalParameters
	 * @param enabled
	 */
	public UserTargetServiceConfigModel(String userEmail, String serviceUserId,
			String serviceUserName, String userSiteUrl,
			String serviceProviderId, 
			Map<String, String> additionalParameters, boolean enabled, 
			String serviceAccessToken, String serviceTokenSecret) {
		super(userEmail, serviceUserId, serviceUserName, userSiteUrl,
				serviceProviderId, 
				additionalParameters, enabled);
		this.serviceAccessToken = serviceAccessToken;
		this.serviceTokenSecret = serviceTokenSecret;
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

	/**
	 * @return the serviceTokenSecret
	 */
	public String getServiceTokenSecret() {
		return serviceTokenSecret;
	}

	/**
	 * @param serviceTokenSecret the serviceTokenSecret to set
	 */
	public void setServiceTokenSecret(String serviceTokenSecret) {
		this.serviceTokenSecret = serviceTokenSecret;
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
		result = prime
				* result
				+ ((serviceTokenSecret == null) ? 0 : serviceTokenSecret
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
		if (!(obj instanceof UserTargetServiceConfigModel))
			return false;
		UserTargetServiceConfigModel other = (UserTargetServiceConfigModel) obj;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserTargetServiceConfigModel [serviceAccessToken="
				+ serviceAccessToken + ", serviceTokenSecret="
				+ serviceTokenSecret + ", toString()=" + super.toString() + "]";
	}
	
}
