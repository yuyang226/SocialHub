/**
 * 
 */
package com.googlecode.flickr2twitter.services.rest.models;

import java.io.Serializable;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class GlobalApplicationConfigModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String providerId;

	private String appName;

	private String description;

	private String authPagePath;
	
	private String configPagePath;

	private String imagePath;

	/**
	 * 
	 */
	public GlobalApplicationConfigModel() {
		super();
	}

	/**
	 * @param providerId
	 * @param appName
	 * @param description
	 * @param authPagePath
	 * @param configPagePath
	 * @param imagePath
	 */
	public GlobalApplicationConfigModel(String providerId, String appName,
			String description, String authPagePath, String configPagePath,
			String imagePath) {
		super();
		this.providerId = providerId;
		this.appName = appName;
		this.description = description;
		this.authPagePath = authPagePath;
		this.configPagePath = configPagePath;
		this.imagePath = imagePath;
	}

	/**
	 * @return the providerId
	 */
	public String getProviderId() {
		return providerId;
	}

	/**
	 * @param providerId the providerId to set
	 */
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	/**
	 * @return the appName
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * @param appName the appName to set
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the authPagePath
	 */
	public String getAuthPagePath() {
		return authPagePath;
	}

	/**
	 * @param authPagePath the authPagePath to set
	 */
	public void setAuthPagePath(String authPagePath) {
		this.authPagePath = authPagePath;
	}

	/**
	 * @return the configPagePath
	 */
	public String getConfigPagePath() {
		return configPagePath;
	}

	/**
	 * @param configPagePath the configPagePath to set
	 */
	public void setConfigPagePath(String configPagePath) {
		this.configPagePath = configPagePath;
	}

	/**
	 * @return the imagePath
	 */
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * @param imagePath the imagePath to set
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appName == null) ? 0 : appName.hashCode());
		result = prime * result
				+ ((authPagePath == null) ? 0 : authPagePath.hashCode());
		result = prime * result
				+ ((configPagePath == null) ? 0 : configPagePath.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((imagePath == null) ? 0 : imagePath.hashCode());
		result = prime * result
				+ ((providerId == null) ? 0 : providerId.hashCode());
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
		if (!(obj instanceof GlobalApplicationConfigModel))
			return false;
		GlobalApplicationConfigModel other = (GlobalApplicationConfigModel) obj;
		if (appName == null) {
			if (other.appName != null)
				return false;
		} else if (!appName.equals(other.appName))
			return false;
		if (authPagePath == null) {
			if (other.authPagePath != null)
				return false;
		} else if (!authPagePath.equals(other.authPagePath))
			return false;
		if (configPagePath == null) {
			if (other.configPagePath != null)
				return false;
		} else if (!configPagePath.equals(other.configPagePath))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (imagePath == null) {
			if (other.imagePath != null)
				return false;
		} else if (!imagePath.equals(other.imagePath))
			return false;
		if (providerId == null) {
			if (other.providerId != null)
				return false;
		} else if (!providerId.equals(other.providerId))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GlobalApplicationConfigModel [providerId=" + providerId
				+ ", appName=" + appName + ", description=" + description
				+ ", authPagePath=" + authPagePath + ", configPagePath="
				+ configPagePath + ", imagePath=" + imagePath + "]";
	}

}
