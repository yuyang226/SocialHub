/**
 * 
 */
package com.googlecode.flickr2twitter.services.rest.models;

import java.io.Serializable;
import java.util.List;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class GlobalApplicationConfigModelList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<GlobalApplicationConfigModel> globalAppConfigModels;

	/**
	 * 
	 */
	public GlobalApplicationConfigModelList() {
		super();
	}

	/**
	 * @param globalAppConfigModels
	 */
	public GlobalApplicationConfigModelList(
			List<GlobalApplicationConfigModel> globalAppConfigModels) {
		super();
		this.globalAppConfigModels = globalAppConfigModels;
	}

	/**
	 * @return the globalAppConfigModels
	 */
	public List<GlobalApplicationConfigModel> getGlobalAppConfigModels() {
		return globalAppConfigModels;
	}

	/**
	 * @param globalAppConfigModels the globalAppConfigModels to set
	 */
	public void setGlobalAppConfigModels(
			List<GlobalApplicationConfigModel> globalAppConfigModels) {
		this.globalAppConfigModels = globalAppConfigModels;
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
				+ ((globalAppConfigModels == null) ? 0 : globalAppConfigModels
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
		if (obj == null)
			return false;
		if (!(obj instanceof GlobalApplicationConfigModelList))
			return false;
		GlobalApplicationConfigModelList other = (GlobalApplicationConfigModelList) obj;
		if (globalAppConfigModels == null) {
			if (other.globalAppConfigModels != null)
				return false;
		} else if (!globalAppConfigModels.equals(other.globalAppConfigModels))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GlobalApplicationConfigModelList [globalAppConfigModels="
				+ globalAppConfigModels + "]";
	}

}
