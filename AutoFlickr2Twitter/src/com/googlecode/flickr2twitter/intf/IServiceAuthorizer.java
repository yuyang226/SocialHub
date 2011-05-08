/**
 * 
 */
package com.googlecode.flickr2twitter.intf;

import java.util.Map;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public interface IServiceAuthorizer {
	public static final String POST_AUTH_PAGE = "/user_admin.jsp";
	
	/**
	 * Request for authorizing the user's source service
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> requestAuthorization(String baseUrl) throws Exception;
	
	/**
	 * Request a token and store it in the given data store service.
	 * 
	 * @param datastore
	 * @return
	 * @throws Exception
	 */
	public String readyAuthorization(String userEmail, Map<String, Object> data) throws Exception;

}
