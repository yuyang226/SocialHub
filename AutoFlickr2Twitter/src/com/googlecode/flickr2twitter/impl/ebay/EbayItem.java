/**
 * 
 */
package com.googlecode.flickr2twitter.impl.ebay;

import java.util.Date;

/**
 * @author hochen
 * 
 */
public class EbayItem {

	private final String sellerId;
	private long itemId;
	private String title;
	private String description;
	private String galleryURL;
	private String viewItemURL;
	private Date startTime;
	private Date endTime;
	private Double currentPrice;
	private Double buyItNowPrice;

	public EbayItem(String sellerId) {
		this.sellerId = sellerId;
	}

	/**
	 * @return the itemId
	 */
	public long getItemId() {
		return itemId;
	}

	/**
	 * @param itemId
	 *            the itemId to set
	 */
	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the galleryURL
	 */
	public String getGalleryURL() {
		return galleryURL;
	}

	/**
	 * @param galleryURL
	 *            the galleryURL to set
	 */
	public void setGalleryURL(String galleryURL) {
		this.galleryURL = galleryURL;
	}

	/**
	 * @return the viewItemURL
	 */
	public String getViewItemURL() {
		return viewItemURL;
	}

	/**
	 * @param viewItemURL
	 *            the viewItemURL to set
	 */
	public void setViewItemURL(String viewItemURL) {
		this.viewItemURL = viewItemURL;
	}

	public String getSellerId() {
		return sellerId;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the currentPrice
	 */
	public Double getCurrentPrice() {
		return currentPrice;
	}

	/**
	 * @param currentPrice the currentPrice to set
	 */
	public void setCurrentPrice(Double currentPrice) {
		this.currentPrice = currentPrice;
	}

	/**
	 * @return the buyItNowPrice
	 */
	public Double getBuyItNowPrice() {
		return buyItNowPrice;
	}

	/**
	 * @param buyItNowPrice the buyItNowPrice to set
	 */
	public void setBuyItNowPrice(Double buyItNowPrice) {
		this.buyItNowPrice = buyItNowPrice;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EbayItem [sellerId=" + sellerId + ", itemId=" + itemId
				+ ", title=" + title + ", description=" + description
				+ ", galleryURL=" + galleryURL + ", viewItemURL=" + viewItemURL
				+ ", startTime=" + startTime + ", endTime=" + endTime
				+ ", currentPrice=" + currentPrice + ", buyItNowPrice="
				+ buyItNowPrice + "]";
	}




}
