/**
 * 
 */
package com.googlecode.flickr2twitter.services.rest.models;

import java.io.Serializable;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class GlobalServiceConfigurationModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long minUploadTime = 602000L;

	/**
	 * 
	 */
	public GlobalServiceConfigurationModel() {
		super();
	}

	/**
	 * @param minUploadTime
	 */
	public GlobalServiceConfigurationModel(long minUploadTime) {
		super();
		this.minUploadTime = minUploadTime;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (int) (minUploadTime ^ (minUploadTime >>> 32));
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
		if (!(obj instanceof GlobalServiceConfigurationModel))
			return false;
		GlobalServiceConfigurationModel other = (GlobalServiceConfigurationModel) obj;
		if (minUploadTime != other.minUploadTime)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GlobalServiceConfigurationModel [minUploadTime="
				+ minUploadTime + "]";
	}

}
