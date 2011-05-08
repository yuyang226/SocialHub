/**
 * 
 */
package com.googlecode.flickr2twitter.model;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class ItemGeoData {
	private static final long serialVersionUID = 1L;
	
	private double longitude;
    private double latitude;

	/**
	 * 
	 */
	public ItemGeoData() {
		super();
	}
	
	public ItemGeoData(String longitudeStr, String latitudeStr) {
        longitude = Double.parseDouble(longitudeStr);
        latitude = Double.parseDouble(latitudeStr);
    }
	
	public ItemGeoData(double longitudeStr, double latitudeStr) {
        longitude = longitudeStr;
        latitude = latitudeStr;
    }
	
	public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

	@Override
	public String toString() {
		return "GeoData [latitude=" + latitude + ", longitude=" + longitude
				+ "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		if (!(obj instanceof ItemGeoData))
			return false;
		ItemGeoData other = (ItemGeoData) obj;
		if (Double.doubleToLongBits(latitude) != Double
				.doubleToLongBits(other.latitude))
			return false;
		if (Double.doubleToLongBits(longitude) != Double
				.doubleToLongBits(other.longitude))
			return false;
		return true;
	}

}
