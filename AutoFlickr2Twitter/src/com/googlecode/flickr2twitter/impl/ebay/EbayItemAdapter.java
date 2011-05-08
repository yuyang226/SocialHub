package com.googlecode.flickr2twitter.impl.ebay;

import java.util.Date;

import com.googlecode.flickr2twitter.model.ILinkableItem;
import com.googlecode.flickr2twitter.model.IShortUrl;
import com.googlecode.flickr2twitter.urlshorteners.BitLyUtils;

/**
 * @author John Liu(zhhong.liu@gmail.com)
 *
 */
public class EbayItemAdapter implements ILinkableItem, IShortUrl{

	private final EbayItem ebayItem;
	private String shortUrl;
	
	public EbayItemAdapter(EbayItem item) {
		this.ebayItem = item;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.ILinkableItem#setUrl(java.lang.String)
	 */
	@Override
	public void setUrl(String url) {
		this.ebayItem.setViewItemURL(url);
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.ILinkableItem#getUrl()
	 */
	@Override
	public String getUrl() {
		return ebayItem.getViewItemURL();
	}

	@Override
	public String getId() {
		return String.valueOf(ebayItem.getItemId());
	}

	@Override
	public String getTitle() {
		return ebayItem.getTitle();
	}

	@Override
	public String getDescription() {
		return ebayItem.getDescription();
	}

	@Override
	public void setDatePosted(Date datePosted) {
		ebayItem.setStartTime(datePosted);
	}

	@Override
	public Date getDatePosted() {
		return ebayItem.getStartTime();
	}

	@Override
	public String getShortUrl() {
		if (shortUrl == null && ebayItem.getViewItemURL() != null) {
			this.shortUrl = BitLyUtils.shortenUrl(ebayItem.getViewItemURL());
		}
		return shortUrl;
	}
	
}
