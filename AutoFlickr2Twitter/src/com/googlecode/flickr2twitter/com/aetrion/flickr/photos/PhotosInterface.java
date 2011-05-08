/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.googlecode.flickr2twitter.com.aetrion.flickr.photos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.googlecode.flickr2twitter.com.aetrion.flickr.FlickrException;
import com.googlecode.flickr2twitter.com.aetrion.flickr.Parameter;
import com.googlecode.flickr2twitter.com.aetrion.flickr.Response;
import com.googlecode.flickr2twitter.com.aetrion.flickr.Transport;
import com.googlecode.flickr2twitter.com.aetrion.flickr.auth.AuthUtilities;
import com.googlecode.flickr2twitter.com.aetrion.flickr.util.StringUtilities;


/**
 * Interface for working with Flickr Photos.
 *
 * @author Anthony Eden
 * @version $Id: PhotosInterface.java,v 1.51 2010/07/20 20:11:16 x-mago Exp $
 */
public class PhotosInterface {
	private static final long serialVersionUID = 12L;

    public static final String METHOD_GET_RECENT = "flickr.photos.getRecent";
    public static final String METHOD_RECENTLY_UPDATED ="flickr.photos.recentlyUpdated";

    private String apiKey;
    private String sharedSecret;
    private Transport transport;

    public PhotosInterface(String apiKey, String sharedSecret, Transport transport) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transport = transport;
    }

    /**
     * Get a collection of recent photos.
     *
     * This method does not require authentication.
     *
     * @see com.aetrion.flickr.photos.Extras
     * @param extras Set of extra-fields
     * @param perPage The number of photos per page
     * @param page The page offset
     * @return A collection of Photo objects
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public PhotoList getRecent(Set<String> extras, int perPage, int page) throws IOException, SAXException, FlickrException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_RECENT));
        parameters.add(new Parameter("api_key", apiKey));

        if (extras != null && !extras.isEmpty()) {
            parameters.add(new Parameter(Extras.KEY_EXTRAS, StringUtilities.join(extras, ",")));
        }
        if (perPage > 0) {
            parameters.add(new Parameter("per_page", perPage));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", page));
        }

        Response response = transport.get(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element photosElement = response.getPayload();
        PhotoList photos = PhotoUtils.createPhotoList(photosElement);
        return photos;
    }

    /**
     * Return a list of your photos that have been recently created or which have been recently modified. 
     * Recently modified may mean that the photo's metadata (title, description, tags) may have been changed or a comment has been added (or just modified somehow :-)
     *
     * This method requires authentication with 'read' permission.
     *
     * @see com.aetrion.flickr.photos.Extras
     * @param minDate Date indicating the date from which modifications should be compared. Must be given.
     * @param extras A set of Strings controlling the extra information to fetch for each returned record. Currently supported fields are: license, date_upload, date_taken, owner_name, icon_server, original_format, last_update, geo. Set to null or an empty set to not specify any extras.
     * @param perPage Number of photos to return per page. If this argument is 0, it defaults to 100. The maximum allowed value is 500.
     * @param page The page of results to return. If this argument is 0, it defaults to 1.
     * @return a list of photos
     * @throws SAXException 
     * @throws IOException 
     * @throws FlickrException
     */
    public PhotoList recentlyUpdated(Date minDate, Set<String> extras, int perPage, int page) throws IOException, SAXException, FlickrException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_RECENTLY_UPDATED));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("min_date", minDate.getTime() / 1000L));

        if (extras != null && !extras.isEmpty()) {
            parameters.add(new Parameter("extras", StringUtilities.join(extras, ",")));
        }
        if (perPage > 0) {
            parameters.add(new Parameter("per_page", perPage));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", page));
        }
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        Response response = transport.get(transport.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element photosElement = response.getPayload();
        PhotoList photos = PhotoUtils.createPhotoList(photosElement);
        return photos;
    }
}
