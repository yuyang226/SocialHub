/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.googlecode.flickr2twitter.com.aetrion.flickr.people;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.googlecode.flickr2twitter.com.aetrion.flickr.FlickrException;
import com.googlecode.flickr2twitter.com.aetrion.flickr.Parameter;
import com.googlecode.flickr2twitter.com.aetrion.flickr.Response;
import com.googlecode.flickr2twitter.com.aetrion.flickr.Transport;
import com.googlecode.flickr2twitter.com.aetrion.flickr.auth.AuthUtilities;
import com.googlecode.flickr2twitter.com.aetrion.flickr.photos.Extras;
import com.googlecode.flickr2twitter.com.aetrion.flickr.photos.PhotoList;
import com.googlecode.flickr2twitter.com.aetrion.flickr.photos.PhotoUtils;
import com.googlecode.flickr2twitter.com.aetrion.flickr.util.StringUtilities;
import com.googlecode.flickr2twitter.com.aetrion.flickr.util.XMLUtilities;

/**
 * Interface for finding Flickr users.
 *
 * @author Anthony Eden
 * @version $Id: PeopleInterface.java,v 1.28 2010/09/12 20:13:57 x-mago Exp $
 */
public class PeopleInterface {

    public static final String METHOD_FIND_BY_EMAIL = "flickr.people.findByEmail";
    public static final String METHOD_FIND_BY_USERNAME = "flickr.people.findByUsername";
    public static final String METHOD_GET_INFO = "flickr.people.getInfo";
    public static final String METHOD_GET_ONLINE_LIST = "flickr.people.getOnlineList";
    public static final String METHOD_GET_PUBLIC_PHOTOS = "flickr.people.getPublicPhotos";

    private String apiKey;
    private String sharedSecret;
    private Transport transportAPI;

    public PeopleInterface(
        String apiKey,
        String sharedSecret,
        Transport transportAPI
    ) {
        this.apiKey = apiKey;
        this.sharedSecret = sharedSecret;
        this.transportAPI = transportAPI;
    }

    /**
     * Find the user by their email address.
     *
     * This method does not require authentication.
     *
     * @param email The email address
     * @return The User
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public User findByEmail(String email) throws IOException, SAXException, FlickrException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_FIND_BY_EMAIL));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("find_email", email));

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element userElement = response.getPayload();
        User user = new User();
        user.setId(userElement.getAttribute("nsid"));
        user.setUsername(XMLUtilities.getChildValue(userElement, "username"));
        return user;
    }

    /**
     * Find a User by the username.
     *
     * This method does not require authentication.
     *
     * @param username The username
     * @return The User object
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public User findByUsername(String username) throws IOException, SAXException, FlickrException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_FIND_BY_USERNAME));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("username", username));

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element userElement = response.getPayload();
        User user = new User();
        user.setId(userElement.getAttribute("nsid"));
        user.setUsername(XMLUtilities.getChildValue(userElement, "username"));
        return user;
    }

    /**
     * Get info about the specified user.
     *
     * This method does not require authentication.
     *
     * @param userId The user ID
     * @return The User object
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public User getInfo(String userId) throws IOException, SAXException, FlickrException {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_INFO));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("user_id", userId));
        parameters.add(
            new Parameter(
                "api_sig",
                AuthUtilities.getSignature(sharedSecret, parameters)
            )
        );

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element userElement = response.getPayload();
        User user = new User();
        user.setId(userElement.getAttribute("nsid"));
        user.setAdmin("1".equals(userElement.getAttribute("isadmin")));
        user.setPro("1".equals(userElement.getAttribute("ispro")));
        user.setIconFarm(userElement.getAttribute("iconfarm"));
        user.setIconServer(userElement.getAttribute("iconserver"));
        user.setRevContact("1".equals(userElement.getAttribute("revcontact")));
        user.setRevFriend("1".equals(userElement.getAttribute("revfriend")));
        user.setRevFamily("1".equals(userElement.getAttribute("revfamily")));
        user.setUsername(XMLUtilities.getChildValue(userElement, "username"));
        user.setRealName(XMLUtilities.getChildValue(userElement, "realname"));
        user.setLocation(XMLUtilities.getChildValue(userElement, "location"));
        user.setMbox_sha1sum(XMLUtilities.getChildValue(userElement, "mbox_sha1sum"));
        user.setPhotosurl(XMLUtilities.getChildValue(userElement, "photosurl"));
        user.setProfileurl(XMLUtilities.getChildValue(userElement, "profileurl"));
        user.setMobileurl(XMLUtilities.getChildValue(userElement, "mobileurl"));

        Element photosElement = XMLUtilities.getChild(userElement, "photos");
        user.setPhotosFirstDate(XMLUtilities.getChildValue(photosElement, "firstdate"));
        user.setPhotosFirstDateTaken(XMLUtilities.getChildValue(photosElement, "firstdatetaken"));
        user.setPhotosCount(XMLUtilities.getChildValue(photosElement, "count"));

        return user;
    }

    public PhotoList getPublicPhotos(String userId, int perPage, int page)
    throws IOException, SAXException, FlickrException {
        return getPublicPhotos(userId, Extras.MIN_EXTRAS, perPage, page);
    }

    /**
     * Get a collection of public photos for the specified user ID.
     *
     * This method does not require authentication.
     *
     * @see com.googlecode.flickr2twitter.com.aetrion.flickr.photos.Extras
     * @param userId The User ID
     * @param extras Set of extra-attributes to include (may be null)
     * @param perPage The number of photos per page
     * @param page The page offset
     * @return The PhotoList collection
     * @throws IOException
     * @throws SAXException
     * @throws FlickrException
     */
    public PhotoList getPublicPhotos(String userId, Set extras, int perPage, int page) throws IOException, SAXException,
            FlickrException {
        PhotoList photos = new PhotoList();

        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("method", METHOD_GET_PUBLIC_PHOTOS));
        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("user_id", userId));

        if (perPage > 0) {
            parameters.add(new Parameter("per_page", "" + perPage));
        }
        if (page > 0) {
            parameters.add(new Parameter("page", "" + page));
        }

        if (extras != null) {
            parameters.add(new Parameter(Extras.KEY_EXTRAS, StringUtilities.join(extras, ",")));
        }

        Response response = transportAPI.get(transportAPI.getPath(), parameters);
        if (response.isError()) {
            throw new FlickrException(response.getErrorCode(), response.getErrorMessage());
        }
        Element photosElement = response.getPayload();
        photos.setPage(photosElement.getAttribute("page"));
		photos.setPages(photosElement.getAttribute("pages"));
		photos.setPerPage(photosElement.getAttribute("perpage"));
		photos.setTotal(photosElement.getAttribute("total"));

        NodeList photoNodes = photosElement.getElementsByTagName("photo");
        for (int i = 0; i < photoNodes.getLength(); i++) {
            Element photoElement = (Element) photoNodes.item(i);
            photos.add(PhotoUtils.createPhoto(photoElement));
        }
        return photos;
    }
}
