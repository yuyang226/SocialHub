/**
 * 
 */
package com.googlecode.flickr2twitter.model;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public abstract class Item implements IItem {
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String title;
	private String description;

	/**
	 * 
	 */
	public Item() {
		super();
	}

	public Item(String id, String title, String description) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.IItem#getTitle()
	 */
	public String getTitle() {
		return title;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.IItem#setTitle(java.lang.String)
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.IItem#getDescription()
	 */
	public String getDescription() {
		return description;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.IItem#setDescription(java.lang.String)
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if ((obj instanceof Item) == false)
			return false;
		Item other = (Item) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Item [description=" + description + ", id=" + id + ", title="
				+ title + "]";
	}

}
