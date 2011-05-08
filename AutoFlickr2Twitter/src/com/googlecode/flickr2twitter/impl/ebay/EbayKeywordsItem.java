/**
 * 
 */
package com.googlecode.flickr2twitter.impl.ebay;

import java.util.Date;

import com.googlecode.flickr2twitter.model.IDescriptiveItem;
import com.googlecode.flickr2twitter.model.IItem;
import com.googlecode.flickr2twitter.model.ILinkableItem;

/**
 * @author yayu
 *
 */
public class EbayKeywordsItem implements IItem, ILinkableItem, IDescriptiveItem {
	private String url;
	private String id;
	private String title;
	private String description;
	private Date datetime;
	
	public EbayKeywordsItem() {
		super();
	}
	
	public EbayKeywordsItem(String url, String id, String title,
			String description) {
		super();
		this.url = url;
		this.id = id;
		this.title = title;
		this.description = description;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	
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
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EbayKeywordsItem other = (EbayKeywordsItem) obj;
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
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EbayKeywordsItem [url=" + url + ", id=" + id + ", title="
				+ title + ", description=" + description + "]";
	}

	@Override
	public String getSelfDescription() {
		return title;
	}

	@Override
	public void setDatePosted(Date datePosted) {
		this.datetime = datePosted;
	}

	@Override
	public Date getDatePosted() {
		return datetime;
	}

	
}
