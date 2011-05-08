/**
 * 
 */
package com.googlecode.flickr2twitter.impl.ebay;

import java.io.Serializable;

/**
 * @author hochen
 *
 */
public class EbayUser implements Serializable{


	private static final long serialVersionUID = 482763379225490714L;
	
	private String userId;
	private String feedbackRatingStar;
	private String feedbackScore;
	private String registrationDate;
	private String status;
	private String storeURL;
	private String storeName;
	private String sellerItemsURL;
	private String aboutMeURL;
	private String reviewsAndGuidesURL;
	private String feedbackDetailsURL;
	private String positiveFeedbackPercent;
	private String topRatedSeller;
	private String myWorldURL;
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the feedbackRatingStar
	 */
	public String getFeedbackRatingStar() {
		return feedbackRatingStar;
	}
	/**
	 * @param feedbackRatingStar the feedbackRatingStar to set
	 */
	public void setFeedbackRatingStar(String feedbackRatingStar) {
		this.feedbackRatingStar = feedbackRatingStar;
	}
	/**
	 * @return the feedbackScore
	 */
	public String getFeedbackScore() {
		return feedbackScore;
	}
	/**
	 * @param feedbackScore the feedbackScore to set
	 */
	public void setFeedbackScore(String feedbackScore) {
		this.feedbackScore = feedbackScore;
	}
	/**
	 * @return the registrationDate
	 */
	public String getRegistrationDate() {
		return registrationDate;
	}
	/**
	 * @param registrationDate the registrationDate to set
	 */
	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the storeURL
	 */
	public String getStoreURL() {
		return storeURL;
	}
	/**
	 * @param storeURL the storeURL to set
	 */
	public void setStoreURL(String storeURL) {
		this.storeURL = storeURL;
	}
	/**
	 * @return the storeName
	 */
	public String getStoreName() {
		return storeName;
	}
	/**
	 * @param storeName the storeName to set
	 */
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	/**
	 * @return the sellerItemsURL
	 */
	public String getSellerItemsURL() {
		return sellerItemsURL;
	}
	/**
	 * @param sellerItemsURL the sellerItemsURL to set
	 */
	public void setSellerItemsURL(String sellerItemsURL) {
		this.sellerItemsURL = sellerItemsURL;
	}
	/**
	 * @return the aboutMeURL
	 */
	public String getAboutMeURL() {
		return aboutMeURL;
	}
	/**
	 * @param aboutMeURL the aboutMeURL to set
	 */
	public void setAboutMeURL(String aboutMeURL) {
		this.aboutMeURL = aboutMeURL;
	}
	/**
	 * @return the reviewsAndGuidesURL
	 */
	public String getReviewsAndGuidesURL() {
		return reviewsAndGuidesURL;
	}
	/**
	 * @param reviewsAndGuidesURL the reviewsAndGuidesURL to set
	 */
	public void setReviewsAndGuidesURL(String reviewsAndGuidesURL) {
		this.reviewsAndGuidesURL = reviewsAndGuidesURL;
	}
	/**
	 * @return the feedbackDetailsURL
	 */
	public String getFeedbackDetailsURL() {
		return feedbackDetailsURL;
	}
	/**
	 * @param feedbackDetailsURL the feedbackDetailsURL to set
	 */
	public void setFeedbackDetailsURL(String feedbackDetailsURL) {
		this.feedbackDetailsURL = feedbackDetailsURL;
	}
	/**
	 * @return the positiveFeedbackPercent
	 */
	public String getPositiveFeedbackPercent() {
		return positiveFeedbackPercent;
	}
	/**
	 * @param positiveFeedbackPercent the positiveFeedbackPercent to set
	 */
	public void setPositiveFeedbackPercent(String positiveFeedbackPercent) {
		this.positiveFeedbackPercent = positiveFeedbackPercent;
	}
	/**
	 * @return the topRatedSeller
	 */
	public String getTopRatedSeller() {
		return topRatedSeller;
	}
	/**
	 * @param topRatedSeller the topRatedSeller to set
	 */
	public void setTopRatedSeller(String topRatedSeller) {
		this.topRatedSeller = topRatedSeller;
	}
	
	
	
	/**
	 * @return the myWorldURL
	 */
	public String getMyWorldURL() {
		return myWorldURL;
	}
	/**
	 * @param myWorldURL the myWorldURL to set
	 */
	public void setMyWorldURL(String myWorldURL) {
		this.myWorldURL = myWorldURL;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((aboutMeURL == null) ? 0 : aboutMeURL.hashCode());
		result = prime
				* result
				+ ((feedbackDetailsURL == null) ? 0 : feedbackDetailsURL
						.hashCode());
		result = prime
				* result
				+ ((feedbackRatingStar == null) ? 0 : feedbackRatingStar
						.hashCode());
		result = prime * result
				+ ((feedbackScore == null) ? 0 : feedbackScore.hashCode());
		result = prime * result
				+ ((myWorldURL == null) ? 0 : myWorldURL.hashCode());
		result = prime
				* result
				+ ((positiveFeedbackPercent == null) ? 0
						: positiveFeedbackPercent.hashCode());
		result = prime
				* result
				+ ((registrationDate == null) ? 0 : registrationDate.hashCode());
		result = prime
				* result
				+ ((reviewsAndGuidesURL == null) ? 0 : reviewsAndGuidesURL
						.hashCode());
		result = prime * result
				+ ((sellerItemsURL == null) ? 0 : sellerItemsURL.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result
				+ ((storeName == null) ? 0 : storeName.hashCode());
		result = prime * result
				+ ((storeURL == null) ? 0 : storeURL.hashCode());
		result = prime * result
				+ ((topRatedSeller == null) ? 0 : topRatedSeller.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EbayUser other = (EbayUser) obj;
		if (aboutMeURL == null) {
			if (other.aboutMeURL != null)
				return false;
		} else if (!aboutMeURL.equals(other.aboutMeURL))
			return false;
		if (feedbackDetailsURL == null) {
			if (other.feedbackDetailsURL != null)
				return false;
		} else if (!feedbackDetailsURL.equals(other.feedbackDetailsURL))
			return false;
		if (feedbackRatingStar == null) {
			if (other.feedbackRatingStar != null)
				return false;
		} else if (!feedbackRatingStar.equals(other.feedbackRatingStar))
			return false;
		if (feedbackScore == null) {
			if (other.feedbackScore != null)
				return false;
		} else if (!feedbackScore.equals(other.feedbackScore))
			return false;
		if (myWorldURL == null) {
			if (other.myWorldURL != null)
				return false;
		} else if (!myWorldURL.equals(other.myWorldURL))
			return false;
		if (positiveFeedbackPercent == null) {
			if (other.positiveFeedbackPercent != null)
				return false;
		} else if (!positiveFeedbackPercent
				.equals(other.positiveFeedbackPercent))
			return false;
		if (registrationDate == null) {
			if (other.registrationDate != null)
				return false;
		} else if (!registrationDate.equals(other.registrationDate))
			return false;
		if (reviewsAndGuidesURL == null) {
			if (other.reviewsAndGuidesURL != null)
				return false;
		} else if (!reviewsAndGuidesURL.equals(other.reviewsAndGuidesURL))
			return false;
		if (sellerItemsURL == null) {
			if (other.sellerItemsURL != null)
				return false;
		} else if (!sellerItemsURL.equals(other.sellerItemsURL))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (storeName == null) {
			if (other.storeName != null)
				return false;
		} else if (!storeName.equals(other.storeName))
			return false;
		if (storeURL == null) {
			if (other.storeURL != null)
				return false;
		} else if (!storeURL.equals(other.storeURL))
			return false;
		if (topRatedSeller == null) {
			if (other.topRatedSeller != null)
				return false;
		} else if (!topRatedSeller.equals(other.topRatedSeller))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [userId=" + userId + ", feedbackRatingStar="
				+ feedbackRatingStar + ", feedbackScore=" + feedbackScore
				+ ", registrationDate=" + registrationDate + ", status="
				+ status + ", storeURL=" + storeURL + ", storeName="
				+ storeName + ", sellerItemsURL=" + sellerItemsURL
				+ ", aboutMeURL=" + aboutMeURL + ", reviewsAndGuidesURL="
				+ reviewsAndGuidesURL + ", feedbackDetailsURL="
				+ feedbackDetailsURL + ", positiveFeedbackPercent="
				+ positiveFeedbackPercent + ", topRatedSeller="
				+ topRatedSeller + ", myWorldURL=" + myWorldURL + "]";
	}

	


}
