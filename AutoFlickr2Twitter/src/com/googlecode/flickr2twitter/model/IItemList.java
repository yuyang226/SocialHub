/**
 * 
 */
package com.googlecode.flickr2twitter.model;

import java.util.List;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public interface IItemList<T extends IItem> {
	
	public String getListTitle();
	
	public List<T> getItems();

	public void setItems(List<T> items);
	
	public boolean addItem(T item);
}
