/**
 * 
 */
package com.googlecode.flickr2twitter.intf;

import java.util.List;

import com.googlecode.flickr2twitter.datastore.model.GlobalServiceConfiguration;
import com.googlecode.flickr2twitter.datastore.model.GlobalSourceApplicationService;
import com.googlecode.flickr2twitter.datastore.model.UserSourceServiceConfig;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public interface ISourceServiceProvider<T> extends  
IServiceProvider<GlobalSourceApplicationService>{
	public static final String KEY_TOKEN = "token";
	public static final String KEY_OAUTHTOKEN = "oauth_token";
	
	
	/**
	 * Get the list of latest items
	 * @param currentTime the time in long with UTC timezone
	 * @return
	 * @throws Exception
	 */
	public List<T> getLatestItems(GlobalServiceConfiguration globalConfig, 
			GlobalSourceApplicationService globalSvcConfig, 
			UserSourceServiceConfig sourceService, 
			long currentTime) throws Exception;

}
