/**
 * 
 */
package com.googlecode.flickr2twitter.services.rest;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;


/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class SocialHubRestletApplication extends Application {

	/**
	 * 
	 */
	public SocialHubRestletApplication() {
		super();
	}

	/**
	 * @param context
	 */
	public SocialHubRestletApplication(Context context) {
		super(context);
	}
	
	 /**
	 * Creates a root Restlet that will receive all incoming calls.
	 */
	@Override
	public synchronized Restlet createRoot() {
	    // Create a router Restlet that routes each call to a
	    // new Resource
	    Router router = new Router(getContext());

	    router.attach("/user", SocialHubServerResource.class);
	    router.attach("/services", SocialHubServicesServerResource.class);
	    router.attach("/eBaySeller", SocialHubEbaySellerResource.class);

	    return router;
	}

}
