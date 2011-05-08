/**
 * 
 */
package com.googlecode.flickr2twitter.services.rest.models;

import java.util.List;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;


/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public interface ISociaHubResource {
	
	/**
	 * @return
	 */
	@Get
	public GlobalApplicationConfigModelList getSupportedServiceProviders();
	
	@Post
	public List<UserSourceServiceConfigModel> getUserSourceServiceConfigs(String userEmail);
	
	@Post
	public void addUserSourceServiceConfig(String userEmail, 
			UserSourceServiceConfigModel sourceServiceConfig);
	
	@Post
	public List<UserTargetServiceConfigModel> getUserTargetServiceConfigs(String userEmail);

	/**
	 * @param userEmail
	 * @return a more complete data of a user
	 */
	@Post
	public UserModel retrieve(String userEmail);
	
	/**
	 * @param userEmail
	 * @param password
	 * @return the returned UserModel might not have objects of user source/target service models.
	 */
	@Post
	public UserModel login(String userEmail, String password);
	
	@Post
	public UserModel openidLogin(String userEmail);

	@Post
	public boolean registerNewUser(UserModel user);

	@Delete
	public boolean remove(UserModel user);

}
