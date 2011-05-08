/**
 * 
 */
package com.googlecode.flickr2twitter.impl.picasa;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Logger;

import com.google.api.client.googleapis.auth.authsub.AuthSubSingleUseTokenRequestUrl;
import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.Person;
import com.google.gdata.data.photos.AlbumFeed;
import com.google.gdata.data.photos.PhotoEntry;
import com.google.gdata.data.photos.UserFeed;
import com.googlecode.flickr2twitter.core.ServiceRunner;
import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory;
import com.googlecode.flickr2twitter.datastore.model.GlobalServiceConfiguration;
import com.googlecode.flickr2twitter.datastore.model.GlobalSourceApplicationService;
import com.googlecode.flickr2twitter.datastore.model.User;
import com.googlecode.flickr2twitter.datastore.model.UserSourceServiceConfig;
import com.googlecode.flickr2twitter.exceptions.TokenAlreadyRegisteredException;
import com.googlecode.flickr2twitter.impl.picasa.model.PicasaPhoto;
import com.googlecode.flickr2twitter.intf.IServiceAuthorizer;
import com.googlecode.flickr2twitter.intf.ISourceServiceProvider;
import com.googlecode.flickr2twitter.model.IPhoto;
import com.googlecode.flickr2twitter.org.apache.commons.lang3.StringUtils;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class SourceServiceProviderPicasa implements ISourceServiceProvider<IPhoto>, IServiceAuthorizer {
	public static final String ID = "picasa";
	public static final String DISPLAY_NAME = "Picasa Web Album";
	
	public static final String KEY_TOKEN = "token";
	private static final Logger log = Logger.getLogger(SourceServiceProviderPicasa.class.getName());
	
	public static final String HOSTED_DOMAIN = "flickr2twitter.googlecode.com";
	public static final String CONSUMER_KEY = "anonymous";
	public static final String CONSUMER_SECRET = "anonymous";
	
	public static final String USER_ID_DEFAULT = "default";
	public static final String URL_PICASAWEB = "http://picasaweb.google.com/";
	public static final String URL_ALBUM = "http://picasaweb.google.com/data/feed/api/user/default?kind=album";
	public static final String URL_ACTIVITIES = "http://picasaweb.google.com/data/feed/api/user/default?kind=photo&max-results=25";
	private static final String SCOPE = "http://picasaweb.google.com/data";
	public static final String CALLBACK_URL = "picasacallback.jsp";
	
	/**
	 * 
	 */
	public SourceServiceProviderPicasa() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.intf.ISourceServiceProvider#getLatestItems(com.googlecode.flickr2twitter.datastore.model.GlobalServiceConfiguration, com.googlecode.flickr2twitter.datastore.model.UserSourceServiceConfig)
	 */
	@Override
	public List<IPhoto> getLatestItems(GlobalServiceConfiguration globalConfig,
			GlobalSourceApplicationService globalSvcConfig, 
			UserSourceServiceConfig sourceService, 
			long currentTime) throws Exception {
		PicasawebService webService = new PicasawebService(HOSTED_DOMAIN);
		String sessionToken = sourceService.getServiceAccessToken();
		webService.setAuthSubToken(sessionToken, null);
		URL feedUrl = new URL(URL_ACTIVITIES);

		Date pastTime = sourceService.getLastUpdateTime();
		if (pastTime == null) {
			Calendar past = Calendar.getInstance(TimeZone.getTimeZone(ServiceRunner.TIMEZONE_UTC));
			long newTime = currentTime - globalConfig.getMinUploadTime();
			past.setTimeInMillis(newTime);
			pastTime = past.getTime();
		}

		AlbumFeed feed = webService.getFeed(feedUrl, AlbumFeed.class);
		log.info("Trying to find photos uploaded for user " + sourceService.getServiceUserId()
				+ " after " + pastTime.toString() + " from "
				+ feed.getPhotoEntries().size() + " new photos");
		List<IPhoto> photos = new ArrayList<IPhoto>();
		
		for(PhotoEntry photo : feed.getPhotoEntries()) {
			PicasaPhoto pPhoto = new PicasaPhoto(photo);
			log.fine("processing photo: " + photo.getTitle().getPlainText()
					+ ", date uploaded: " + pPhoto.getDatePosted());
			//TODO check whether the photo is private
			if (pPhoto.getDatePosted().after(pastTime)) {
				
					log.info(photo.getTitle() + ", URL: " + pPhoto.getUrl()
							+ ", date uploaded: " + pPhoto.getDatePosted()
							+ ", GEO: " + pPhoto.getGeoData());
					photos.add(pPhoto);
			}
		}
		return photos;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.intf.IServiceAuthorizer#readyAuthorization(java.lang.String, java.util.Map)
	 */
	@Override
	public String readyAuthorization(String userEmail, Map<String, Object> data)
			throws Exception {
		if (data == null || data.containsKey(KEY_TOKEN) == false) {
			throw new IllegalArgumentException("Invalid data: " + data);
		}
		User user = MyPersistenceManagerFactory.getUser(userEmail);
		if (user == null) {
			throw new IllegalArgumentException(
					"Can not find the specified user: " + userEmail);
		}
		String token = String.valueOf(data.get("token"));
		
		PicasawebService webService = new PicasawebService(HOSTED_DOMAIN);
		webService.setAuthSubToken(token, null);
		URL feedUrl = new URL(URL_ALBUM);
		UserFeed myUserFeed = webService.getFeed(feedUrl, UserFeed.class);
		List<Person> persons = myUserFeed.getAuthors();
		Person person = null;
		if (persons.isEmpty() == false) {
			person = persons.get(0);
		}
		
		String userId = USER_ID_DEFAULT;
		if (person.getUri() != null && person.getUri().startsWith(URL_PICASAWEB)) {
			userId = StringUtils.substringAfterLast(person.getUri(), "/");
		}
		
		StringBuffer buf = new StringBuffer();
		buf.append("Authentication success\n");
		// This token can be used until the user revokes it.
		buf.append("Token: " + token);
		buf.append("\n");
		buf.append("UserId: " + userId);
		buf.append("\n");
		buf.append("Realname: " + person.getName());
		buf.append("\n");
		buf.append("User Site: " + person.getUri());
		
		for (UserSourceServiceConfig service : MyPersistenceManagerFactory
				.getUserSourceServices(user)) {
			if (token.equals(service.getServiceAccessToken())) {
				throw new TokenAlreadyRegisteredException(token, userEmail);
			}
		}
		
		
		UserSourceServiceConfig serviceConfig = new UserSourceServiceConfig();
		serviceConfig.setServiceUserId(userId);
		serviceConfig.setServiceUserName(person != null ? person.getName() : USER_ID_DEFAULT);
		serviceConfig.setServiceAccessToken(token);
		serviceConfig.setServiceProviderId(ID);
		serviceConfig.setUserEmail(userEmail);
		
		if (person != null) {
			serviceConfig.setUserSiteUrl(person.getUri());
		}
		
		MyPersistenceManagerFactory.addSourceServiceApp(userEmail, serviceConfig);

		return buf.toString();
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.intf.IServiceAuthorizer#requestAuthorization()
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
		if (baseUrl.endsWith("/oauth")) {
			baseUrl = StringUtils.left(baseUrl, baseUrl.length() - "/oauth".length());
		}
		String nextUrl = baseUrl + "/" + CALLBACK_URL;
		String scope = SCOPE;
		AuthSubSingleUseTokenRequestUrl authorizeUrl = new AuthSubSingleUseTokenRequestUrl();
		authorizeUrl.hostedDomain = HOSTED_DOMAIN;
		authorizeUrl.nextUrl = nextUrl;
		authorizeUrl.scope = scope;
		authorizeUrl.session = 1;
		String authorizationUrl = authorizeUrl.build();
		
		log.info("Picasa Authorization URL: " + authorizationUrl);
		result.put("url", authorizationUrl);
		return result;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.intf.IServiceProvider#createDefaultGlobalApplicationConfig()
	 */
	@Override
	public GlobalSourceApplicationService createDefaultGlobalApplicationConfig() {
		GlobalSourceApplicationService result = new GlobalSourceApplicationService();
		result.setAppName(DISPLAY_NAME);
		result.setProviderId(ID);
		result.setDescription("The Google's online photo storage service");
		result.setSourceAppApiKey(CONSUMER_KEY);
		result.setSourceAppSecret(CONSUMER_SECRET);
		result.setAuthPagePath(CALLBACK_URL);
		result.setImagePath("/services/picasa/images/picasa_100.gif");
		return result;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.intf.IServiceProvider#getId()
	 */
	@Override
	public String getId() {
		return ID;
	}

}
