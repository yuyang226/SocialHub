/**
 * 
 */
package com.googlecode.flickr2twitter.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class ItemList implements IItemList<IItem> {
	private String listTitle;
	private List<IItem> items = new ArrayList<IItem>();

	/**
	 * 
	 */
	public ItemList() {
		super();
	}

	public ItemList(String listTitle) {
		super();
		this.listTitle = listTitle;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.IItemList#addItem(com.googlecode.flickr2twitter.model.IItem)
	 */
	@Override
	public boolean addItem(IItem item) {
		return items.add(item);
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.IItemList#getItems()
	 */
	@Override
	public List<IItem> getItems() {
		return this.items;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.IItemList#setItems(java.util.List)
	 */
	@Override
	public void setItems(List<IItem> items) {
		this.items = items;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.IItemList#getListTitle()
	 */
	@Override
	public String getListTitle() {
		return this.listTitle;
	}
	

}
