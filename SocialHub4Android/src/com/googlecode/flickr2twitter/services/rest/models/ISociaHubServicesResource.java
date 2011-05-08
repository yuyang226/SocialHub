/**
 * 
 */
package com.googlecode.flickr2twitter.services.rest.models;

import org.restlet.resource.Post;


/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public interface ISociaHubServicesResource {
	
	/**
	 * The format for twitter is {providerId}/{userEmail}/{token}/{tokenSecret}/{verifier}
	 * The format for eBay keywords {providerId}/{userEmail}/{keywords}
	 * 
	 * @param data 
	 */
	@Post
	public void addUserServiceConfig(String data);


}
