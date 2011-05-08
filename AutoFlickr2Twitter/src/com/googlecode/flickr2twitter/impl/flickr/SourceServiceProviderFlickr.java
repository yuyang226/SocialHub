/**
 * 
 */
package com.googlecode.flickr2twitter.impl.flickr;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Logger;

import org.xml.sax.SAXException;

import com.googlecode.flickr2twitter.com.aetrion.flickr.Flickr;
import com.googlecode.flickr2twitter.com.aetrion.flickr.FlickrException;
import com.googlecode.flickr2twitter.com.aetrion.flickr.REST;
import com.googlecode.flickr2twitter.com.aetrion.flickr.RequestContext;
import com.googlecode.flickr2twitter.com.aetrion.flickr.auth.Auth;
import com.googlecode.flickr2twitter.com.aetrion.flickr.auth.AuthInterface;
import com.googlecode.flickr2twitter.com.aetrion.flickr.auth.Permission;
import com.googlecode.flickr2twitter.com.aetrion.flickr.photos.Extras;
import com.googlecode.flickr2twitter.com.aetrion.flickr.photos.Photo;
import com.googlecode.flickr2twitter.com.aetrion.flickr.photos.PhotoList;
import com.googlecode.flickr2twitter.com.aetrion.flickr.photos.PhotosInterface;
import com.googlecode.flickr2twitter.com.aetrion.flickr.tags.Tag;
import com.googlecode.flickr2twitter.core.GlobalDefaultConfiguration;
import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory;
import com.googlecode.flickr2twitter.datastore.model.GlobalServiceConfiguration;
import com.googlecode.flickr2twitter.datastore.model.GlobalSourceApplicationService;
import com.googlecode.flickr2twitter.datastore.model.User;
import com.googlecode.flickr2twitter.datastore.model.UserSourceServiceConfig;
import com.googlecode.flickr2twitter.exceptions.TokenAlreadyRegisteredException;
import com.googlecode.flickr2twitter.intf.IServiceAuthorizer;
import com.googlecode.flickr2twitter.intf.ISourceServiceProvider;
import com.googlecode.flickr2twitter.model.IPhoto;
import com.googlecode.flickr2twitter.org.apache.commons.lang3.StringUtils;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 * 
 */
public class SourceServiceProviderFlickr implements
		ISourceServiceProvider<IPhoto>, IServiceAuthorizer {
	public static final String ID = "flickr";
	public static final String DISPLAY_NAME = "Flickr";
	public static final String KEY_FROB = "frob";
	public static final String KEY_FILTER_TAGS = "filter_tags";
	public static final String TAGS_DELIMITER = ",";
	public static final String TIMEZONE_CST = "CST";
	public static final String TIMEZONE_GMT = "GMT";
	
	public static final String CALLBACK_URL = "flickrcallback.jsp";
	private static final Logger log = Logger.getLogger(SourceServiceProviderFlickr.class.getName());

	/**
	 * 
	 */
	public SourceServiceProviderFlickr() {
		super();
	}

	private List<IPhoto> showRecentPhotos(Flickr f, UserSourceServiceConfig sourceService, 
			long interval, long currentTime) throws IOException, SAXException, FlickrException {
		String userId = sourceService.getServiceUserId();
		String token = sourceService.getServiceAccessToken();
		RequestContext requestContext = RequestContext.getRequestContext();
		Auth auth = new Auth();
		auth.setPermission(Permission.READ);
		auth.setToken(token);
		requestContext.setAuth(auth);
		List<IPhoto> photos = new ArrayList<IPhoto>();
		List<String> filterTags = new ArrayList<String>();
		Map<String, String> additionalParams = sourceService.getAdditionalParameters();
		if (additionalParams.containsKey(KEY_FILTER_TAGS)) {
			String filterTrags = additionalParams.get(KEY_FILTER_TAGS);
			if (filterTrags != null) {
				filterTags.addAll(Arrays.asList(StringUtils.split(
						filterTrags.trim(), TAGS_DELIMITER)));
			}
		}
		
		PhotosInterface photosFace = f.getPhotosInterface();
		Set<String> extras = new HashSet<String>(2);
		extras.add(Extras.DATE_UPLOAD);
		extras.add(Extras.LAST_UPDATE);
		extras.add(Extras.GEO);
		if (filterTags.isEmpty() == false) {
			extras.add(Extras.TAGS);
		}
		
		Date pastTime = sourceService.getLastUpdateTime();
		Calendar past = Calendar.getInstance(TimeZone.getTimeZone(TIMEZONE_GMT));
		if (pastTime == null) {
			Calendar cstTime = Calendar.getInstance(TimeZone.getTimeZone(TIMEZONE_GMT));
			cstTime.setTimeInMillis(currentTime);
			log.info("Converted current time: " + cstTime.getTime());

			//		Calendar past = getFromTime(globalConfig, currentTime);

			long newTime = cstTime.getTime().getTime() - interval;
			past.setTimeInMillis(newTime);
			pastTime = past.getTime();
		} else {
			past.setTimeInMillis(pastTime.getTime());
			pastTime = past.getTime();
		}
		
		PhotoList list = photosFace.recentlyUpdated(pastTime, extras, 100,
				1);
		
		log.info("Trying to find photos uploaded for user " + userId
				+ " after " + pastTime.toString() + " from "
				+ list.getTotal() + " new photos");
		for (Object obj : list) {
			if (obj instanceof Photo) {
				Photo photo = (Photo) obj;
				
				log.info("processing photo: " + photo.getTitle()
						+ ", date uploaded: " + photo.getDatePosted());
				if (photo.isPublicFlag() && photo.getDatePosted().after(pastTime)) {
					if (!filterTags.isEmpty() && containsTags(filterTags, photo.getTags()) == false) {
						log.warning("Photo does not contains the required tags, contained tags are: " 
								+ photo.getTags());
					} else {
						log.info(photo.getTitle() + ", URL: " + photo.getUrl()
								+ ", date uploaded: " + photo.getDatePosted()
								+ ", GEO: " + photo.getGeoData());
						photos.add(photo);
					}
				} else {
					log.warning("private photo will not be posted: " + photo.getTitle());
				}
			}
		}
		return photos;
	}
	
	private static boolean containsTags(List<String> filterTags, Collection<Tag> photoTags) {
		if (photoTags == null)
			return false;
		
		int matchCount = 0;
		
		for (Tag tag : photoTags) {
			if (filterTags.contains(tag.getValue())) {
				matchCount++;
			}
			if (matchCount == filterTags.size()) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.googlecode.flickr2twitter.intf.ISourceServiceProvider#getId
	 * ()
	 */
	@Override
	public String getId() {
		return ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.googlecode.flickr2twitter.intf.ISourceServiceProvider#
	 * getLatestItems()
	 */
	@Override
	public List<IPhoto> getLatestItems(GlobalServiceConfiguration globalConfig,
			GlobalSourceApplicationService globalSvcConfig, 
			UserSourceServiceConfig sourceService, 
			long currentTime) throws Exception {
		if (globalSvcConfig == null
				|| ID.equalsIgnoreCase(globalSvcConfig.getProviderId()) == false) {
			throw new IllegalArgumentException(
					"Invalid source service provider: " + globalSvcConfig);
		}
		REST transport = new REST();
		Flickr f = new Flickr(globalSvcConfig.getSourceAppApiKey(),
				globalSvcConfig.getSourceAppSecret(), transport);
		transport.setAllowCache(false);

		Flickr.debugRequest = false;
		Flickr.debugStream = false;
		return showRecentPhotos(f, sourceService,
				globalConfig.getMinUploadTime(), currentTime);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.googlecode.flickr2twitter.intf.ISourceServiceProvider#storeToken
	 * (com.googlecode.flickr2twitter.intf.IDataStoreService)
	 */
	@Override
	public String readyAuthorization(String userEmail, Map<String, Object> data)
			throws Exception {
		if (data == null || data.containsKey(KEY_FROB) == false) {
			throw new IllegalArgumentException("Invalid data: " + data);
		}
		User user = MyPersistenceManagerFactory.getUser(userEmail);
		if (user == null) {
			throw new IllegalArgumentException(
					"Can not find the specified user: " + userEmail);
		}
		Flickr f = new Flickr(GlobalDefaultConfiguration.getInstance()
				.getFlickrApiKey(), GlobalDefaultConfiguration.getInstance()
				.getFlickrSecret(), new REST());
		String frob = String.valueOf(data.get(KEY_FROB));
		AuthInterface authInterface = f.getAuthInterface();
		Auth auth = authInterface.getToken(frob);
		StringBuffer buf = new StringBuffer();
		buf.append("Authentication success\n");
		// This token can be used until the user revokes it.
		buf.append("Token: " + auth.getToken());
		buf.append("\n");
		buf.append("nsid: " + auth.getUser().getId());
		buf.append("\n");
		buf.append("Realname: " + auth.getUser().getRealName());
		buf.append("\n");
		buf.append("User Site: " + auth.getUser().getPhotosurl());
		buf.append("\n");
		buf.append("Username: " + auth.getUser().getUsername());
		buf.append("\n");
		buf.append("Permission: " + auth.getPermission().getType());

		String userId = auth.getUser().getId();
		for (UserSourceServiceConfig service : MyPersistenceManagerFactory
				.getUserSourceServices(user)) {
			if (auth.getToken().equals(service.getServiceAccessToken())) {
				throw new TokenAlreadyRegisteredException(auth.getToken(), auth
						.getUser().getUsername());
			}
		}
		UserSourceServiceConfig serviceConfig = new UserSourceServiceConfig();
		serviceConfig.setServiceUserId(userId);
		serviceConfig.setServiceUserName(auth.getUser().getUsername());
		serviceConfig.setServiceAccessToken(auth.getToken());
		serviceConfig.setServiceProviderId(ID);
		serviceConfig.setUserEmail(userEmail);
		com.googlecode.flickr2twitter.com.aetrion.flickr.people.User flickrUser = 
			f.getPeopleInterface().getInfo(userId);
		if (flickrUser != null) {
			serviceConfig.setUserSiteUrl(flickrUser.getPhotosurl());
		}
		
		serviceConfig.addAddtionalParameter(KEY_FILTER_TAGS, "");
		
		MyPersistenceManagerFactory.addSourceServiceApp(userEmail, serviceConfig);

		return buf.toString();
	}
	
	public static void setFilterTags(UserSourceServiceConfig serviceConfig, String filterTags) {
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.googlecode.flickr2twitter.intf.ISourceServiceProvider#
	 * requestAuthorization()
	 */
	@Override
	public Map<String, Object> requestAuthorization(String baseUrl) throws Exception {
		GlobalSourceApplicationService globalAppConfig = MyPersistenceManagerFactory
				.getGlobalSourceAppService(ID);
		if (globalAppConfig == null
				|| ID.equalsIgnoreCase(globalAppConfig.getProviderId()) == false) {
			throw new IllegalArgumentException(
					"Invalid source service provider: " + globalAppConfig);
		}
		Map<String, Object> result = new HashMap<String, Object>();

		Flickr f = new Flickr(globalAppConfig.getSourceAppApiKey(),
				globalAppConfig.getSourceAppSecret(), new REST());
		AuthInterface authInterface = f.getAuthInterface();

		String frob = authInterface.getFrob();
		if (baseUrl.endsWith("/oauth")) {
			baseUrl = StringUtils.left(baseUrl, baseUrl.length() - "/oauth".length());
		}
		String nextUrl = baseUrl + "/" + CALLBACK_URL;
		URL url = authInterface.buildAuthenticationUrl(Permission.READ, frob, nextUrl);
		log.info("frob: " + frob + ", Token URL: " + url.toExternalForm());
		result.put(KEY_FROB, frob);
		result.put("url", url.toExternalForm());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.googlecode.flickr2twitter.intf.IServiceProvider#
	 * createDefaultGlobalApplicationConfig()
	 */
	@Override
	public GlobalSourceApplicationService createDefaultGlobalApplicationConfig() {
		GlobalSourceApplicationService result = new GlobalSourceApplicationService();
		result.setAppName(DISPLAY_NAME);
		result.setProviderId(ID);
		result.setDescription("The world's leading online photo storage service");
		result.setSourceAppApiKey(GlobalDefaultConfiguration.getInstance()
				.getFlickrApiKey());
		result.setSourceAppSecret(GlobalDefaultConfiguration.getInstance()
				.getFlickrSecret());
		result.setAuthPagePath(CALLBACK_URL);
		result.setImagePath("/services/flickr/images/flickr_100.gif");
		return result;
	}

}
