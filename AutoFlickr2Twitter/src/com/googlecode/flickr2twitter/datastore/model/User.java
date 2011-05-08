/**
 * 
 */
package com.googlecode.flickr2twitter.datastore.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;
import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory.Permission;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
@PersistenceCapable
public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	/**
	 * this should be the email address
	 */
	@Persistent
	private Email userId;
	
	@Persistent
	private String password;
	
	@Persistent
	private String permission = Permission.NORMAL.name();
	
	/**
	 * an optional field
	 */
	@Persistent
	private String screenName;
	
	@Persistent
	private List<UserSourceServiceConfig> sourceServices;
	
	@Persistent
	private List<UserTargetServiceConfig> targetServices;
	
	@Persistent
	private Date lastLoginTime;

	/**
	 * 
	 */
	public User() {
		super();
	}

	/**
	 * @param userId
	 * @param password
	 * @param screenName
	 */
	public User(Email userId, String password, String screenName) {
		super();
		this.userId = userId;
		this.password = password;
		this.screenName = screenName;
	}
	
	/**
	 * @param userId
	 * @param password
	 * @param screenName
	 */
	public User(String userId, String password, String screenName) {
		super();
		this.userId = new Email(userId);
		this.password = password;
		this.screenName = screenName;
	}

	public Key getKey() {
		return key;
	}

	/**
	 * @return the userId
	 */
	public Email getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Email userId) {
		this.userId = userId;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the screenName
	 */
	public String getScreenName() {
		return screenName;
	}

	/**
	 * @param screenName the screenName to set
	 */
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	/**
	 * @return the permission
	 */
	public String getPermission() {
		return permission;
	}

	/**
	 * @param permission the permission to set
	 */
	public void setPermission(String permission) {
		this.permission = permission;
	}

	public boolean addSourceService(UserSourceServiceConfig e) {
		if (sourceServices == null)
			sourceServices = new ArrayList<UserSourceServiceConfig>();
		return sourceServices.add(e);
	}

	public boolean addSourceServices(Collection<? extends UserSourceServiceConfig> c) {
		if (sourceServices == null)
			sourceServices = new ArrayList<UserSourceServiceConfig>();
		return sourceServices.addAll(c);
	}

	public List<UserSourceServiceConfig> getSourceServices() {
		if (sourceServices == null)
			sourceServices = new ArrayList<UserSourceServiceConfig>();
		return sourceServices;
	}

	public void setSourceServices(List<UserSourceServiceConfig> sourceServices) {
		this.sourceServices = sourceServices;
	}

	public List<UserTargetServiceConfig> getTargetServices() {
		if (targetServices == null)
			targetServices = new ArrayList<UserTargetServiceConfig>();
		return targetServices;
	}

	public void setTargetServices(List<UserTargetServiceConfig> targetServices) {
		this.targetServices = targetServices;
	}

	public boolean addTargetService(UserTargetServiceConfig e) {
		if (targetServices == null)
			targetServices = new ArrayList<UserTargetServiceConfig>();
		return targetServices.add(e);
	}
	
	public boolean addTargetServices(Collection<? extends UserTargetServiceConfig> c) {
		if (targetServices == null)
			targetServices = new ArrayList<UserTargetServiceConfig>();
		return targetServices.addAll(c);
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
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
		if (!(obj instanceof User))
			return false;
		User other = (User) obj;
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
		return "User [" + (userId != null ? "userId=" + userId.getEmail() + ", " : "")
				+ (password != null ? "password=" + password + ", " : "")
				+ (permission != null ? "permission=" + permission + ", " : "")
				+ (screenName != null ? "screenName=" + screenName : "") + "]";
	}

}
