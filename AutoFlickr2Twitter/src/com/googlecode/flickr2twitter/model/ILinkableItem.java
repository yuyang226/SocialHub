/**
 * 
 */
package com.googlecode.flickr2twitter.model;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public interface ILinkableItem extends IItem {
	
	public void setUrl(String url);
	public String getUrl();

}
