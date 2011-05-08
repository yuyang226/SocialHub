/**
 * 
 */
package com.googlecode.flickr2twitter.com.aetrion.flickr.util;

import com.googlecode.flickr2twitter.com.aetrion.flickr.auth.Auth;

/**
 * Defines an interface for possibly persistent storage of token information.
 * 
 * @author Matthew MacKenzie
 * @version $Id: AuthStore.java,v 1.2 2007/09/09 17:15:57 x-mago Exp $
 */
public interface AuthStore {
	
	/**
	 * Retrieve Auth for a given NSID.
	 * @param nsid NSID
	 * @return Auth
	 */
	Auth retrieve(String nsid);
	
	/**
	 * Retrieve all Auth objects being stored.
	 *
	 * @return Auth objects
	 */
	Auth[] retrieveAll();
	
	/**
	 * Clear out the store.
	 *
	 */
	void clearAll();
	
	/**
	 * Clear for a given NSID.
	 * @param nsid
	 */
	void clear(String nsid);
}
