/**
 * 
 */
package com.googlecode.flickr2twitter.datastore.model;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 * 
 */
@PersistenceCapable
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class GlobalApplicationConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String providerId;

	@Persistent
	private String appName = "Flickr";

	@Persistent
	private String description = "The world's leading online phone album service";

	@Persistent
	private String authPagePath;
	
	@Persistent
	private String configPagePath;

	@Persistent
	private String imagePath;

	/**
	 * 
	 */
	public GlobalApplicationConfig() {
		super();
	}

	/**
	 * @return the providerId
	 */
	public String getProviderId() {
		return providerId;
	}

	/**
	 * @param providerId
	 *            the providerId to set
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
	 * @param appName
	 *            the appName to set
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
	 * @param description
	 *            the description to set
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
	 * @param authPagePath
	 *            the authPagePath to set
	 */
	public void setAuthPagePath(String authPagePath) {
		this.authPagePath = authPagePath;
	}

	/**
	 * @return the imagePath
	 */
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * @param imagePath
	 *            the imagePath to set
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
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
	public void setConfigPagePath(String configPathPath) {
		this.configPagePath = configPathPath;
	}
	

}
