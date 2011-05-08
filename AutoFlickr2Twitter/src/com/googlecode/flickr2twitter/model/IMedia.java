/**
 * 
 */
package com.googlecode.flickr2twitter.model;

import java.util.Date;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public interface IMedia extends ILinkableItem {
	
	
	public void setDateTaken(Date dateTaken);
	public Date getDateTaken();
	
}
