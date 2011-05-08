/**
 * 
 */
package com.googlecode.flickr2twitter.impl.picasa.model;

import java.util.Date;

import com.google.gdata.data.photos.PhotoEntry;
import com.google.gdata.util.ServiceException;
import com.googlecode.flickr2twitter.model.IGeoItem;
import com.googlecode.flickr2twitter.model.IPhoto;
import com.googlecode.flickr2twitter.model.ItemGeoData;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class PicasaPhoto implements IPhoto, IGeoItem {
	private PhotoEntry photo;
	private ItemGeoData geoData;
	private Date datePosted;

	/**
	 * 
	 */
	public PicasaPhoto(PhotoEntry photo) {
		super();
		this.photo = photo;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.IPhoto#getDatePosted()
	 */
	@Override
	public Date getDatePosted()  {
		if (this.datePosted == null) {
			this.datePosted = new Date(photo.getPublished().getValue());
		}
		return this.datePosted;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.IPhoto#getDateTaken()
	 */
	@Override
	public Date getDateTaken() {
		try {
			return photo.getTimestamp();
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.IPhoto#getUrl()
	 */
	@Override
	public String getUrl() {
		return photo.getHtmlLink().getHref();
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.IPhoto#setDatePosted(java.util.Date)
	 */
	@Override
	public void setDatePosted(Date datePosted) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.IPhoto#setDateTaken(java.util.Date)
	 */
	@Override
	public void setDateTaken(Date dateTaken) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.IPhoto#setUrl(java.lang.String)
	 */
	@Override
	public void setUrl(String url) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.IItem#getDescription()
	 */
	@Override
	public String getDescription() {
		return photo.getDescription().getPlainText();
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.IItem#getId()
	 */
	@Override
	public String getId() {
		return photo.getId();
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.IItem#getTitle()
	 */
	@Override
	public String getTitle() {
		return photo.getTitle().getPlainText();
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.IItem#setDescription(java.lang.String)
	 */
//	@Override
	public void setDescription(String description) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.IItem#setId(java.lang.String)
	 */
//	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.IItem#setTitle(java.lang.String)
	 */
//	@Override
	public void setTitle(String title) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.IGeoItem#getGeoData()
	 */
	@Override
	public ItemGeoData getGeoData() {
		if (this.geoData == null && photo.getGeoLocation() != null) {
			this.geoData = new ItemGeoData(photo.getGeoLocation().getLongitude(), 
					photo.getGeoLocation().getLatitude());
		}
		return this.geoData;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.IGeoItem#setGeoData(com.googlecode.flickr2twitter.model.ItemGeoData)
	 */
	@Override
	public void setGeoData(ItemGeoData geoData) {
		// TODO Auto-generated method stub
		
	}

}
