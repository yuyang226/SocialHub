/**
 * 
 */
package com.googlecode.flickr2twitter.services.rest.models;


/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class GlobalSourceApplicationServiceModel extends
		GlobalApplicationConfigModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String sourceAppApiKey;

	private String sourceAppSecret;

	/**
	 * 
	 */
	public GlobalSourceApplicationServiceModel() {
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
	public GlobalSourceApplicationServiceModel(String providerId,
			String appName, String description, String authPagePath,
			String configPagePath, String imagePath, String sourceAppApiKey, String sourceAppSecret) {
		super(providerId, appName, description, authPagePath, configPagePath,
				imagePath);
		this.sourceAppApiKey = sourceAppApiKey;
		this.sourceAppSecret = sourceAppSecret;
	}

	/**
	 * @return the sourceAppApiKey
	 */
	public String getSourceAppApiKey() {
		return sourceAppApiKey;
	}

	/**
	 * @param sourceAppApiKey the sourceAppApiKey to set
	 */
	public void setSourceAppApiKey(String sourceAppApiKey) {
		this.sourceAppApiKey = sourceAppApiKey;
	}

	/**
	 * @return the sourceAppSecret
	 */
	public String getSourceAppSecret() {
		return sourceAppSecret;
	}

	/**
	 * @param sourceAppSecret the sourceAppSecret to set
	 */
	public void setSourceAppSecret(String sourceAppSecret) {
		this.sourceAppSecret = sourceAppSecret;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((sourceAppApiKey == null) ? 0 : sourceAppApiKey.hashCode());
		result = prime * result
				+ ((sourceAppSecret == null) ? 0 : sourceAppSecret.hashCode());
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
		if (!(obj instanceof GlobalSourceApplicationServiceModel))
			return false;
		GlobalSourceApplicationServiceModel other = (GlobalSourceApplicationServiceModel) obj;
		if (sourceAppApiKey == null) {
			if (other.sourceAppApiKey != null)
				return false;
		} else if (!sourceAppApiKey.equals(other.sourceAppApiKey))
			return false;
		if (sourceAppSecret == null) {
			if (other.sourceAppSecret != null)
				return false;
		} else if (!sourceAppSecret.equals(other.sourceAppSecret))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GlobalSourceApplicationServiceModel [sourceAppApiKey="
				+ sourceAppApiKey + ", sourceAppSecret=" + sourceAppSecret
				+ ", toString()=" + super.toString() + "]";
	}
	
	

}
