/**
 * 
 */
package com.googlecode.flickr2twitter.impl.youtube.model;

import java.util.Date;

import com.google.gdata.data.youtube.VideoEntry;
import com.googlecode.flickr2twitter.model.IVideo;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class YoutubeVideo implements IVideo {
	private VideoEntry entry;
	private Date datePosted;
	private Date dateTaken;

	/**
	 * 
	 */
	public YoutubeVideo(VideoEntry entry) {
		super();
		this.entry = entry;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.IPhoto#getDatePosted()
	 */
	@Override
	public Date getDatePosted()  {
		if (this.datePosted == null) {
			this.datePosted = new Date(entry.getPublished().getValue());
		}
		return this.datePosted;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.IPhoto#getDateTaken()
	 */
	@Override
	public Date getDateTaken() {
		if (this.dateTaken == null) {
			this.dateTaken = new Date(entry.getUpdated().getValue());
		}
		return this.dateTaken;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.IPhoto#getUrl()
	 */
	@Override
	public String getUrl() {
		return entry.getHtmlLink().getHref();
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
		return null;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.IItem#getId()
	 */
	@Override
	public String getId() {
		return entry.getId();
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.IItem#getTitle()
	 */
	@Override
	public String getTitle() {
		return entry.getTitle().getPlainText();
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

}
