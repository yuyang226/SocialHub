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
public class GlobalTargetApplicationService extends GlobalApplicationConfig
		implements Serializable {

	private static final long serialVersionUID = 1L;

	@Persistent
	private String targetAppConsumerId;

	@Persistent
	private String targetAppConsumerSecret;

	/**
	 * 
	 */
	public GlobalTargetApplicationService() {
		super();
	}

	/**
	 * @return the targetAppConsumerId
	 */
	public String getTargetAppConsumerId() {
		return targetAppConsumerId;
	}

	/**
	 * @param targetAppConsumerId
	 *            the targetAppConsumerId to set
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
	 * @param targetAppConsumerSecret
	 *            the targetAppConsumerSecret to set
	 */
	public void setTargetAppConsumerSecret(String targetAppConsumerSecret) {
		this.targetAppConsumerSecret = targetAppConsumerSecret;
	}

}
