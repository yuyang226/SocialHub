/**
 * 
 */
package com.googlecode.flickr2twitter.intf;

import java.util.List;

import com.googlecode.flickr2twitter.datastore.model.GlobalTargetApplicationService;
import com.googlecode.flickr2twitter.datastore.model.UserTargetServiceConfig;
import com.googlecode.flickr2twitter.model.IItem;
import com.googlecode.flickr2twitter.model.IItemList;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public interface ITargetServiceProvider extends IServiceAuthorizer, 
IServiceProvider<GlobalTargetApplicationService> {
	
	/**
	 * Post an update for a new item
	 * @param item
	 * @throws Exception
	 */
	public void postUpdate(GlobalTargetApplicationService globalAppConfig, 
			UserTargetServiceConfig targetConfig, List<IItemList<IItem>> items) throws Exception;
}
