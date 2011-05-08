package com.googlecode.flickr2twitter.com.aetrion.flickr.photos;

import com.googlecode.flickr2twitter.model.ItemGeoData;


/**
 * A geographic position.
 *
 * @author mago
 * @version $Id: GeoData.java,v 1.4 2009/07/23 20:41:03 x-mago Exp $
 */
public class GeoData extends ItemGeoData{
    private static final long serialVersionUID = 12L;
    private int accuracy;

    public GeoData() {
        super();
    }

    public GeoData(String longitudeStr, String latitudeStr, String accuracyStr) {
        super(longitudeStr, latitudeStr);
        accuracy = Integer.parseInt(accuracyStr);
    }

    public int getAccuracy() {
        return accuracy;
    }

    /**
     * Set the accuracy level.<p>
     *
     * World level is 1, Country is ~3, Region ~6, City ~11, Street ~16.
     *
     * @param accuracy
     * @see com.googlecode.flickr2twitter.com.aetrion.flickr.Flickr#ACCURACY_WORLD
     * @see com.googlecode.flickr2twitter.com.aetrion.flickr.Flickr#ACCURACY_COUNTRY
     * @see com.googlecode.flickr2twitter.com.aetrion.flickr.Flickr#ACCURACY_REGION
     * @see com.googlecode.flickr2twitter.com.aetrion.flickr.Flickr#ACCURACY_CITY
     * @see com.googlecode.flickr2twitter.com.aetrion.flickr.Flickr#ACCURACY_STREET
     */
    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public String toString() {
        return super.toString() + " accuracy=" + accuracy + "]";
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + accuracy;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof GeoData))
			return false;
		GeoData other = (GeoData) obj;
		if (accuracy != other.accuracy)
			return false;
		return true;
	}

	
}
