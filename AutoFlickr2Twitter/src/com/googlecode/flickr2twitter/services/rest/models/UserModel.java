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
public class UserModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * this should be the email address
	 */
	private String userId;
	
	private String password;
	
	private String permission;
	
	private String screenName;
	
	private List<UserSourceServiceConfigModel> sourceServices;
	
	private List<UserTargetServiceConfigModel> targetServices;

	/**
	 * 
	 */
	public UserModel() {
		super();
	}

	public UserModel(String userId, String password, String screenName) {
		this(userId, password, "NORMAL", screenName);
	}


	public UserModel(String userId, String password, String permission,
			String screenName) {
		super();
		this.userId = userId;
		this.password = password;
		this.permission = permission;
		this.screenName = screenName;
	}

	/**
	 * @param userId
	 * @param password
	 * @param permission
	 * @param screenName
	 * @param sourceServices
	 * @param targetServices
	 */
	public UserModel(String userId, String password, String permission,
			String screenName,
			List<UserSourceServiceConfigModel> sourceServices,
			List<UserTargetServiceConfigModel> targetServices) {
		super();
		this.userId = userId;
		this.password = password;
		this.permission = permission;
		this.screenName = screenName;
		this.sourceServices = sourceServices;
		this.targetServices = targetServices;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	/**
	 * @return the sourceServices
	 */
	public List<UserSourceServiceConfigModel> getSourceServices() {
		return sourceServices;
	}

	/**
	 * @param sourceServices the sourceServices to set
	 */
	public void setSourceServices(List<UserSourceServiceConfigModel> sourceServices) {
		this.sourceServices = sourceServices;
	}

	/**
	 * @return the targetServices
	 */
	public List<UserTargetServiceConfigModel> getTargetServices() {
		return targetServices;
	}

	/**
	 * @param targetServices the targetServices to set
	 */
	public void setTargetServices(List<UserTargetServiceConfigModel> targetServices) {
		this.targetServices = targetServices;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((permission == null) ? 0 : permission.hashCode());
		result = prime * result
				+ ((screenName == null) ? 0 : screenName.hashCode());
		result = prime * result
				+ ((sourceServices == null) ? 0 : sourceServices.hashCode());
		result = prime * result
				+ ((targetServices == null) ? 0 : targetServices.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		if (!(obj instanceof UserModel))
			return false;
		UserModel other = (UserModel) obj;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (permission == null) {
			if (other.permission != null)
				return false;
		} else if (!permission.equals(other.permission))
			return false;
		if (screenName == null) {
			if (other.screenName != null)
				return false;
		} else if (!screenName.equals(other.screenName))
			return false;
		if (sourceServices == null) {
			if (other.sourceServices != null)
				return false;
		} else if (!sourceServices.equals(other.sourceServices))
			return false;
		if (targetServices == null) {
			if (other.targetServices != null)
				return false;
		} else if (!targetServices.equals(other.targetServices))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserModel [userId=" + userId + ", password=" + password
				+ ", permission=" + permission + ", screenName=" + screenName
				+ ", sourceServices=" + sourceServices + ", targetServices="
				+ targetServices + "]";
	}

}
