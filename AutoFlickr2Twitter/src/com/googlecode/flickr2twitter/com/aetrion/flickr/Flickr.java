/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.googlecode.flickr2twitter.com.aetrion.flickr;

import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import com.googlecode.flickr2twitter.com.aetrion.flickr.auth.Auth;
import com.googlecode.flickr2twitter.com.aetrion.flickr.auth.AuthInterface;
import com.googlecode.flickr2twitter.com.aetrion.flickr.people.PeopleInterface;
import com.googlecode.flickr2twitter.com.aetrion.flickr.photos.PhotosInterface;

/**
 * Main entry point for the Flickrj API.
 * This class is used to acquire Interface classes which wrap the Flickr API.<p>
 *
 * If you registered API keys, you find them with the shared secret at your
 * <a href="http://www.flickr.com/services/api/registered_keys.gne">list of API keys</a><p>
 *
 * The user who authenticates himself, can manage this permissions at
 * <a href="http://www.flickr.com/services/auth/list.gne">his list of Third-party applications</a>
 * (You -> Your account -> Extending Flickr -> Account Links -> edit).
 *
 * @author Anthony Eden
 * @version $Id: Flickr.java,v 1.45 2009/06/23 21:51:25 x-mago Exp $
 */
public class Flickr {

    /**
     * The default endpoint host.
     */
    public static final String DEFAULT_HOST = "api.flickr.com";

    /**
     * Set to true to enable response debugging (print the response stream)
     */
    public static boolean debugStream = false;

    /**
     * Set to true to enable request debugging (print the request stream, used for "post")
     */
    public static boolean debugRequest = false;

    /**
     * If set to true, trace messages will be printed to STDOUT.
     */
    public static boolean tracing = false;

    private String apiKey;
    private String sharedSecret;
    private Transport transport;
    private Auth auth;

    private AuthInterface authInterface;
    private PhotosInterface photosInterface;
    private PeopleInterface peopleInterface;

    /**
     * @see com.aetrion.flickr.photos.PhotosInterface#setContentType(String, String)
     * @see com.aetrion.flickr.prefs.PrefsInterface#getContentType()
     * @see com.aetrion.flickr.uploader.UploadMetaData#setContentType(String)
     */
    public static final String CONTENTTYPE_PHOTO = "1";

    /**
     * @see com.aetrion.flickr.photos.PhotosInterface#setContentType(String, String)
     * @see com.aetrion.flickr.prefs.PrefsInterface#getContentType()
     * @see com.aetrion.flickr.uploader.UploadMetaData#setContentType(String)
     */
    public static final String CONTENTTYPE_SCREENSHOT = "2";

    /**
     * @see com.aetrion.flickr.photos.PhotosInterface#setContentType(String, String)
     * @see com.aetrion.flickr.prefs.PrefsInterface#getContentType()
     * @see com.aetrion.flickr.uploader.UploadMetaData#setContentType(String)
     */
    public static final String CONTENTTYPE_OTHER = "3";

    /**
     * The lowest accuracy for bounding-box searches.
     *
     * @see com.googlecode.flickr2twitter.com.aetrion.flickr.photos.SearchParameters#setAccuracy(int)
     */
    public static final int ACCURACY_WORLD = 1;

    /**
     * @see com.googlecode.flickr2twitter.com.aetrion.flickr.photos.SearchParameters#setAccuracy(int)
     */
    public static final int ACCURACY_COUNTRY = 3;

    /**
     * @see com.googlecode.flickr2twitter.com.aetrion.flickr.photos.SearchParameters#setAccuracy(int)
     */
    public static final int ACCURACY_REGION = 6;

    /**
     * @see com.googlecode.flickr2twitter.com.aetrion.flickr.photos.SearchParameters#setAccuracy(int)
     */
    public static final int ACCURACY_CITY = 11;

    /**
     * The highest accuracy for bounding-box searches.
     *
     * @see com.googlecode.flickr2twitter.com.aetrion.flickr.photos.SearchParameters#setAccuracy(int)
     */
    public static final int ACCURACY_STREET = 16;

    /**
     * @see com.aetrion.flickr.photos.PhotosInterface#setSafetyLevel(String, String, Boolean)
     * @see com.aetrion.flickr.prefs.PrefsInterface#getSafetyLevel()
     * @see com.aetrion.flickr.uploader.UploadMetaData#setSafetyLevel(String)
	 * @see com.googlecode.flickr2twitter.com.aetrion.flickr.photos.SearchParameters#setSafeSearch(String)
     */
    public static final String SAFETYLEVEL_SAFE = "1";
    /**
     * @see com.aetrion.flickr.photos.PhotosInterface#setSafetyLevel(String, String, Boolean)
     * @see com.aetrion.flickr.prefs.PrefsInterface#getSafetyLevel()
     * @see com.aetrion.flickr.uploader.UploadMetaData#setSafetyLevel(String)
	 * @see com.googlecode.flickr2twitter.com.aetrion.flickr.photos.SearchParameters#setSafeSearch(String)
     */
    public static final String SAFETYLEVEL_MODERATE = "2";
    /**
     * @see com.aetrion.flickr.photos.PhotosInterface#setSafetyLevel(String, String, Boolean)
     * @see com.aetrion.flickr.prefs.PrefsInterface#getSafetyLevel()
     * @see com.aetrion.flickr.uploader.UploadMetaData#setSafetyLevel(String)
	 * @see com.googlecode.flickr2twitter.com.aetrion.flickr.photos.SearchParameters#setSafeSearch(String)
     */
    public static final String SAFETYLEVEL_RESTRICTED = "3";

    /**
     * @see com.aetrion.flickr.photosets.PhotosetsInterface#getPhotos(String, Set, int, int, int)
     * @see com.aetrion.flickr.prefs.PrefsInterface#getPrivacy()
     * @see com.aetrion.flickr.prefs.PrefsInterface#getGeoPerms()
     */
    public static final int PRIVACY_LEVEL_NO_FILTER = 0;
    /**
     * @see com.aetrion.flickr.photosets.PhotosetsInterface#getPhotos(String, Set, int, int, int)
     * @see com.aetrion.flickr.prefs.PrefsInterface#getPrivacy()
     * @see com.aetrion.flickr.prefs.PrefsInterface#getGeoPerms()
     */
    public static final int PRIVACY_LEVEL_PUBLIC = 1;
    /**
     * @see com.aetrion.flickr.photosets.PhotosetsInterface#getPhotos(String, Set, int, int, int)
     * @see com.aetrion.flickr.prefs.PrefsInterface#getPrivacy()
     * @see com.aetrion.flickr.prefs.PrefsInterface#getGeoPerms()
     */
    public static final int PRIVACY_LEVEL_FRIENDS = 2;
    /**
     * @see com.aetrion.flickr.photosets.PhotosetsInterface#getPhotos(String, Set, int, int, int)
     * @see com.aetrion.flickr.prefs.PrefsInterface#getPrivacy()
     * @see com.aetrion.flickr.prefs.PrefsInterface#getGeoPerms()
     */
    public static final int PRIVACY_LEVEL_FAMILY = 3;
    /**
     * @see com.aetrion.flickr.photosets.PhotosetsInterface#getPhotos(String, Set, int, int, int)
     * @see com.aetrion.flickr.prefs.PrefsInterface#getPrivacy()
     * @see com.aetrion.flickr.prefs.PrefsInterface#getGeoPerms()
     */
    public static final int PRIVACY_LEVEL_FRIENDS_FAMILY = 4;
    /**
     * @see com.aetrion.flickr.photosets.PhotosetsInterface#getPhotos(String, Set, int, int, int)
     * @see com.aetrion.flickr.prefs.PrefsInterface#getPrivacy()
     * @see com.aetrion.flickr.prefs.PrefsInterface#getGeoPerms()
     */
    public static final int PRIVACY_LEVEL_PRIVATE = 5;

    /**
     * Construct a new Flickr gateway instance.  Defaults to a REST transport.
     *
     * @param apiKey The API key, must be non-null
     */
    public Flickr(String apiKey) {
        setApiKey(apiKey);
        try {
            setTransport(new REST(DEFAULT_HOST));
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Construct a new Flickr gateway instance.
     *
     * @param apiKey The API key, must be non-null
     * @param transport The transport (REST or SOAP), must be non-null
     */
    public Flickr(String apiKey, Transport transport) {
        setApiKey(apiKey);
        setTransport(transport);
    }

    /**
     * Construct a new Flickr gateway instance.
     *
     * @param apiKey The API key, must be non-null
     * @param sharedSecret
     * @param transport
     */
    public Flickr(String apiKey, String sharedSecret, Transport transport) {
        setApiKey(apiKey);
        setSharedSecret(sharedSecret);
        setTransport(transport);
    }

    /**
     * Get the API key.
     *
     * @return The API key
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Set the API key to use which must not be null.
     *
     * @param apiKey The API key which cannot be null
     */
    public void setApiKey(String apiKey) {
        if (apiKey == null) {
            throw new IllegalArgumentException("API key must not be null");
        }
        this.apiKey = apiKey;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    /**
     * Get the Auth-object.
     *
     * @return The Auth-object
     */
    public Auth getAuth() {
        return auth;
    }

    /**
     * Get the Shared-Secret.
     *
     * @return The Shared-Secret
     */
    public String getSharedSecret() {
        return sharedSecret;
    }

    /**
     * Set the Shared-Secret to use which must not be null.
     *
     * @param sharedSecret The Shared-Secret which cannot be null
     */
    public void setSharedSecret(String sharedSecret) {
        if (sharedSecret == null) {
            throw new IllegalArgumentException("Shared-Secret must not be null");
        }
        this.sharedSecret = sharedSecret;
    }

    /**
     * Get the Transport interface.
     *
     * @return The Tranport interface
     */
    public Transport getTransport() {
        return transport;
    }

    /**
     * Set the Transport which must not be null.
     *
     * @param transport
     */
    public void setTransport(Transport transport) {
        if (transport == null) {
            throw new IllegalArgumentException("Transport must not be null");
        }
        this.transport = transport;
    }

    /**
     * Get the AuthInterface.
     *
     * @return The AuthInterface
     */
    public AuthInterface getAuthInterface() {
        if (authInterface == null) {
            authInterface = new AuthInterface(apiKey, sharedSecret, transport);
        }
        return authInterface;
    }


    public PeopleInterface getPeopleInterface() {
        if (peopleInterface == null) {
            peopleInterface = new PeopleInterface(apiKey, sharedSecret, transport);
        }
        return peopleInterface;
    }
    
    public PhotosInterface getPhotosInterface() {
        if (photosInterface == null) {
            photosInterface = new PhotosInterface(apiKey, sharedSecret, transport);
        }
        return photosInterface;
    }

}
