/**
 * 
 */
package com.googlecode.flickr2twitter.intf;

/**
 * This is used for those services that support configuration page for more advanced options.
 * @author yayu
 *
 */
public interface IConfigurableService {
	
	/**
	 * @return the relative path to the configuration page
	 */
	public String getConfigPagePath();

}
