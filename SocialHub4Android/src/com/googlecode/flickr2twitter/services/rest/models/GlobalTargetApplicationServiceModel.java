/**
 * 
 */
package com.googlecode.flickr2twitter.services.rest.models;


/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class GlobalTargetApplicationServiceModel extends
		GlobalApplicationConfigModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String targetAppConsumerId;

	private String targetAppConsumerSecret;

	/**
	 * 
	 */
	public GlobalTargetApplicationServiceModel() {
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
	public GlobalTargetApplicationServiceModel(String providerId,
			String appName, String description, String authPagePath,
			String configPagePath, String imagePath, String targetAppConsumerId,
			String targetAppConsumerSecret) {
		super(providerId, appName, description, authPagePath, configPagePath, imagePath);
		this.targetAppConsumerId = targetAppConsumerId;
		this.targetAppConsumerSecret = targetAppConsumerSecret;
	}

	/**
	 * @return the targetAppConsumerId
	 */
	public String getTargetAppConsumerId() {
		return targetAppConsumerId;
	}

	/**
	 * @param targetAppConsumerId the targetAppConsumerId to set
	 */
	public void setTargetAppConsumerId(String targetAppConsumerId) {
		this.targetAppConsumerId = targetAppConsumerId;
	}

	/**
	 * @return the targetAppConsumerSecret
	 */
	public String getTargetAppConsumerSecret() {
		return targetAppConsumerSecret;
	}

	/**
	 * @param targetAppConsumerSecret the targetAppConsumerSecret to set
	 */
	public void setTargetAppConsumerSecret(String targetAppConsumerSecret) {
		this.targetAppConsumerSecret = targetAppConsumerSecret;
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
				+ ((targetAppConsumerId == null) ? 0 : targetAppConsumerId
						.hashCode());
		result = prime
				* result
				+ ((targetAppConsumerSecret == null) ? 0
						: targetAppConsumerSecret.hashCode());
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
		if (!(obj instanceof GlobalTargetApplicationServiceModel))
			return false;
		GlobalTargetApplicationServiceModel other = (GlobalTargetApplicationServiceModel) obj;
		if (targetAppConsumerId == null) {
			if (other.targetAppConsumerId != null)
				return false;
		} else if (!targetAppConsumerId.equals(other.targetAppConsumerId))
			return false;
		if (targetAppConsumerSecret == null) {
			if (other.targetAppConsumerSecret != null)
				return false;
		} else if (!targetAppConsumerSecret
				.equals(other.targetAppConsumerSecret))
			return false;
		return true;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GlobalTargetApplicationServiceModel [targetAppConsumerId="
				+ targetAppConsumerId + ", targetAppConsumerSecret="
				+ targetAppConsumerSecret + ", toString()=" + super.toString()
				+ "]";
	}


}
