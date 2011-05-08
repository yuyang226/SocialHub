/**
 * 
 */
package com.googlecode.flickr2twitter.services.rest.models;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class UserServiceConfigModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userEmail;
	
	private String serviceUserId;
	
	private String serviceUserName;
	
	private String userSiteUrl;
	
	private String serviceProviderId;
	
	private Map<String, String> additionalParameters;
	
	private boolean enabled = true;

	/**
	 * 
	 */
	public UserServiceConfigModel() {
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
	public UserServiceConfigModel(String userEmail, String serviceUserId,
			String serviceUserName, String userSiteUrl,
			String serviceProviderId, 
			Map<String, String> additionalParameters, boolean enabled) {
		super();
		this.userEmail = userEmail;
		this.serviceUserId = serviceUserId;
		this.serviceUserName = serviceUserName;
		this.userSiteUrl = userSiteUrl;
		this.serviceProviderId = serviceProviderId;
		this.additionalParameters = additionalParameters;
		this.enabled = enabled;
	}

	/**
	 * @return the userEmail
	 */
	public String getUserEmail() {
		return userEmail;
	}

	/**
	 * @param userEmail the userEmail to set
	 */
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	/**
	 * @return the serviceUserId
	 */
	public String getServiceUserId() {
		return serviceUserId;
	}

	/**
	 * @param serviceUserId the serviceUserId to set
	 */
	public void setServiceUserId(String serviceUserId) {
		this.serviceUserId = serviceUserId;
	}

	/**
	 * @return the serviceUserName
	 */
	public String getServiceUserName() {
		return serviceUserName;
	}

	/**
	 * @param serviceUserName the serviceUserName to set
	 */
	public void setServiceUserName(String serviceUserName) {
		this.serviceUserName = serviceUserName;
	}

	/**
	 * @return the userSiteUrl
	 */
	public String getUserSiteUrl() {
		return userSiteUrl;
	}

	/**
	 * @param userSiteUrl the userSiteUrl to set
	 */
	public void setUserSiteUrl(String userSiteUrl) {
		this.userSiteUrl = userSiteUrl;
	}

	/**
	 * @return the serviceProviderId
	 */
	public String getServiceProviderId() {
		return serviceProviderId;
	}

	/**
	 * @param serviceProviderId the serviceProviderId to set
	 */
	public void setServiceProviderId(String serviceProviderId) {
		this.serviceProviderId = serviceProviderId;
	}

	/**
	 * @return the additionalParameters
	 */
	public Map<String, String> getAdditionalParameters() {
		return additionalParameters;
	}

	/**
	 * @param additionalParameters the additionalParameters to set
	 */
	public void setAdditionalParameters(Map<String, String> additionalParameters) {
		this.additionalParameters = additionalParameters;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((additionalParameters == null) ? 0 : additionalParameters
						.hashCode());
		result = prime * result + (enabled ? 1231 : 1237);
		result = prime
				* result
				+ ((serviceProviderId == null) ? 0 : serviceProviderId
						.hashCode());
		result = prime * result
				+ ((serviceUserId == null) ? 0 : serviceUserId.hashCode());
		result = prime * result
				+ ((serviceUserName == null) ? 0 : serviceUserName.hashCode());
		result = prime * result
				+ ((userEmail == null) ? 0 : userEmail.hashCode());
		result = prime * result
				+ ((userSiteUrl == null) ? 0 : userSiteUrl.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UserServiceConfigModel))
			return false;
		UserServiceConfigModel other = (UserServiceConfigModel) obj;
		if (additionalParameters == null) {
			if (other.additionalParameters != null)
				return false;
		} else if (!additionalParameters.equals(other.additionalParameters))
			return false;
		if (enabled != other.enabled)
			return false;
		if (serviceProviderId == null) {
			if (other.serviceProviderId != null)
				return false;
		} else if (!serviceProviderId.equals(other.serviceProviderId))
			return false;
		if (serviceUserId == null) {
			if (other.serviceUserId != null)
				return false;
		} else if (!serviceUserId.equals(other.serviceUserId))
			return false;
		if (serviceUserName == null) {
			if (other.serviceUserName != null)
				return false;
		} else if (!serviceUserName.equals(other.serviceUserName))
			return false;
		if (userEmail == null) {
			if (other.userEmail != null)
				return false;
		} else if (!userEmail.equals(other.userEmail))
			return false;
		if (userSiteUrl == null) {
			if (other.userSiteUrl != null)
				return false;
		} else if (!userSiteUrl.equals(other.userSiteUrl))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserServiceConfigModel [userEmail=" + userEmail
				+ ", serviceUserId=" + serviceUserId + ", serviceUserName="
				+ serviceUserName + ", userSiteUrl=" + userSiteUrl
				+ ", serviceProviderId=" + serviceProviderId
				+ ", additionalParameters=" + additionalParameters
				+ ", enabled=" + enabled + "]";
	}
	

}
