/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.googlecode.flickr2twitter.com.aetrion.flickr.photos;

import java.util.HashSet;
import java.util.Set;

/**
 * Extra-attributes for Photo-requests.
 *
 * @author Anthony Eden
 * @version $Id: Extras.java,v 1.12 2009/07/23 20:41:03 x-mago Exp $
 */
public class Extras {
	private static final long serialVersionUID = 12L;
    public static final String KEY_EXTRAS = "extras";

    public static final String DATE_UPLOAD = "date_upload";
    public static final String DATE_TAKEN = "date_taken";
    public static final String OWNER_NAME = "owner_name";
    public static final String TAGS = "tags";
    public static final String LAST_UPDATE = "last_update";
    public static final String GEO = "geo";

    /**
     * Set of all extra-arguments. Used for requesting lists of photos.
     *
     * @see com.aetrion.flickr.groups.pools.PoolsInterface#getPhotos(String, String[], Set, int, int)
     * @see com.aetrion.flickr.panda.PandaInterface#getPhotos(com.aetrion.flickr.panda.Panda, Set, int, int)
     * @see com.googlecode.flickr2twitter.com.aetrion.flickr.people.PeopleInterface#getPublicPhotos(String, Set, int, int)
     * @see com.aetrion.flickr.photosets.PhotosetsInterface#getPhotos(String, Set, int, int, int)
     * @see com.aetrion.flickr.photos.PhotosInterface#getContactsPublicPhotos(String, Set, int, boolean, boolean, boolean)
     * @see com.aetrion.flickr.photos.PhotosInterface#getWithGeoData(java.util.Date, java.util.Date, java.util.Date, java.util.Date, int, String, Set, int, int)
     * @see com.aetrion.flickr.photos.PhotosInterface#getWithoutGeoData(java.util.Date, java.util.Date, java.util.Date, java.util.Date, int, String, Set, int, int)
     * @see com.aetrion.flickr.photos.PhotosInterface#recentlyUpdated(java.util.Date, Set, int, int)
     * @see com.googlecode.flickr2twitter.com.aetrion.flickr.photos.SearchParameters#setExtras(Set)
     * @see com.aetrion.flickr.photos.geo.GeoInterface#photosForLocation(GeoData, Set, int, int)
     * @see com.aetrion.flickr.interestingness.InterestingnessInterface#getList(java.util.Date, Set, int, int)
     * @see com.aetrion.flickr.favorites.FavoritesInterface#getList(String, int, int, Set)
     */
    public static final Set<String> ALL_EXTRAS = new HashSet<String>();

    /**
     * Minimal Set of extra-arguments. Used by convenience-methods
     * that request lists of photos.
     *
     * @see com.aetrion.flickr.groups.pools.PoolsInterface#getPhotos(String, String[], Set, int, int)
     * @see com.aetrion.flickr.panda.PandaInterface#getPhotos(com.aetrion.flickr.panda.Panda, Set, int, int)
     * @see com.googlecode.flickr2twitter.com.aetrion.flickr.people.PeopleInterface#getPublicPhotos(String, Set, int, int)
     * @see com.aetrion.flickr.photosets.PhotosetsInterface#getPhotos(String, Set, int, int, int)
     * @see com.aetrion.flickr.photos.PhotosInterface#getContactsPublicPhotos(String, Set, int, boolean, boolean, boolean)
     * @see com.aetrion.flickr.photos.PhotosInterface#getWithGeoData(java.util.Date, java.util.Date, java.util.Date, java.util.Date, int, String, Set, int, int)
     * @see com.aetrion.flickr.photos.PhotosInterface#getWithoutGeoData(java.util.Date, java.util.Date, java.util.Date, java.util.Date, int, String, Set, int, int)
     * @see com.aetrion.flickr.photos.PhotosInterface#recentlyUpdated(java.util.Date, Set, int, int)
     * @see com.aetrion.flickr.photos.geo.GeoInterface#photosForLocation(GeoData, Set, int, int)
     * @see com.aetrion.flickr.interestingness.InterestingnessInterface#getList(java.util.Date, Set, int, int)
     * @see com.aetrion.flickr.favorites.FavoritesInterface#getList(String, int, int, Set)
     */
    public static final Set<String> MIN_EXTRAS = new HashSet<String>();

    static {
        ALL_EXTRAS.add(DATE_TAKEN);
        ALL_EXTRAS.add(DATE_UPLOAD);
        ALL_EXTRAS.add(LAST_UPDATE);
        ALL_EXTRAS.add(OWNER_NAME);
        ALL_EXTRAS.add(GEO);
        ALL_EXTRAS.add(TAGS);
    }

    static {
        MIN_EXTRAS.add(OWNER_NAME);
    }

    /**
     * No-op constructor.
     */
    private Extras() {
    }

}
