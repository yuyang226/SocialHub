/**
 * 
 */
package com.googlecode.flickr2twitter.impl.youtube;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.api.client.googleapis.auth.authsub.AuthSubSingleUseTokenRequestUrl;
import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.youtube.UserProfileEntry;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory;
import com.googlecode.flickr2twitter.datastore.model.GlobalServiceConfiguration;
import com.googlecode.flickr2twitter.datastore.model.GlobalSourceApplicationService;
import com.googlecode.flickr2twitter.datastore.model.User;
import com.googlecode.flickr2twitter.datastore.model.UserSourceServiceConfig;
import com.googlecode.flickr2twitter.exceptions.TokenAlreadyRegisteredException;
import com.googlecode.flickr2twitter.impl.youtube.model.YoutubeVideo;
import com.googlecode.flickr2twitter.intf.BaseSourceProvider;
import com.googlecode.flickr2twitter.intf.IServiceAuthorizer;
import com.googlecode.flickr2twitter.model.IVideo;
import com.googlecode.flickr2twitter.org.apache.commons.lang3.StringUtils;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class SourceServiceProviderYoutube extends BaseSourceProvider<IVideo> implements  IServiceAuthorizer {
	public static final String ID = "youtube";
	public static final String DISPLAY_NAME = "Youtube";
	
	private static final Logger log = Logger.getLogger(SourceServiceProviderYoutube.class.getName());
	
	public static final String HOSTED_DOMAIN = "flickr2twitter.googlecode.com";
	public static final String CONSUMER_KEY = "anonymous";
	public static final String CONSUMER_SECRET = "anonymous";
	public static final String DEVELOPER_KEY = "AI39si6wIdMuEtAq17ijpj4kZYjPgIb8YxIqycgPndFSkVD_56i80mrfSt0RvnWcbgTZ9GvGFLwBTCc1_ASa5HSwiauftNVY1A";
	
	public static final String USER_ID_DEFAULT = "default";
	public static final String URL_YOUTUBE = "http://www.youtube.com/";
	public static final String URL_USER = "http://gdata.youtube.com/feeds/api/users/default";
	public static final String URL_ACTIVITIES = "http://gdata.youtube.com/feeds/api/users/default/uploads";
	private static final String SCOPE = "http://gdata.youtube.com";
	public static final String CALLBACK_URL = "youtubecallback.jsp";
	
	/**
	 * 
	 */
	public SourceServiceProviderYoutube() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.intf.ISourceServiceProvider#getLatestItems(com.googlecode.flickr2twitter.datastore.model.GlobalServiceConfiguration, com.googlecode.flickr2twitter.datastore.model.UserSourceServiceConfig)
	 */
	@Override
	public List<IVideo> getLatestItems(GlobalServiceConfiguration globalConfig,
			GlobalSourceApplicationService globalSvcConfig, 
			UserSourceServiceConfig sourceService, 
			long currentTime) throws Exception {
		YouTubeService youtubeService = new YouTubeService(HOSTED_DOMAIN, 
				globalSvcConfig.getSourceAppApiKey());
		String sessionToken = sourceService.getServiceAccessToken();
		youtubeService.setAuthSubToken(sessionToken);
		
		Date pastTime = sourceService.getLastUpdateTime();
		if (pastTime == null) {
			Calendar past = getFromTime(globalConfig, currentTime);
			pastTime = past.getTime();
		}
			
		URL feedUrl = new URL(URL_ACTIVITIES);
		VideoFeed videoFeed = youtubeService.getFeed(feedUrl, VideoFeed.class);

		log.info("Retrieve recent activities for youtube user " + sourceService.getServiceUserId());
		List<IVideo> videos = new ArrayList<IVideo>();
		log.info("Trying to find videos uploaded for user " + sourceService.getServiceUserId()
				+ " after " + pastTime.toString() + " from "
				+ videoFeed.getEntries().size() + " new posts");
		for (VideoEntry entry : videoFeed.getEntries()) {
			YoutubeVideo video = new YoutubeVideo(entry);
			log.fine("processing photo: " + video.getTitle()
					+ ", date uploaded: " + video.getDatePosted());
			//TODO check whether the photo is private
			if (
					//entry.isDraft() == false && 
					video.getDatePosted().after(pastTime)) {
				log.info(video.getTitle() + ", URL: " + video.getUrl()
						+ ", date uploaded: " + video.getDatePosted());
				videos.add(video);
			}
		}

		return videos;
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
		String token = String.valueOf(data.get(KEY_TOKEN));
		
		YouTubeService youtubeService = new YouTubeService(HOSTED_DOMAIN);
		youtubeService.setAuthSubToken(token, null);
		URL profileUrl = new URL(URL_USER);
		UserProfileEntry profileEntry = youtubeService.getEntry(profileUrl, UserProfileEntry.class);
		
		String userId = profileEntry.getUsername();
		String fullName = "";
		if (profileEntry.getLastName() != null) {
			fullName = profileEntry.getLastName();
		}
		if (profileEntry.getFirstName() != null) {
			fullName += profileEntry.getFirstName();
		}
		if (StringUtils.isBlank(fullName)) {
			fullName = userId;
		}
		String userSite = profileEntry.getHtmlLink().getHref();
		StringBuffer buf = new StringBuffer();
		buf.append("Authentication success\n");
		// This token can be used until the user revokes it.
		buf.append("Token: " + token);
		buf.append("\n");
		buf.append("UserId: " + userId);
		buf.append("\n");
		buf.append("Realname: " + profileEntry.getLastName());
		buf.append("\n");
		buf.append("User Site: " + userSite);
		
		for (UserSourceServiceConfig service : MyPersistenceManagerFactory
				.getUserSourceServices(user)) {
			if (token.equals(service.getServiceAccessToken())) {
				throw new TokenAlreadyRegisteredException(token, userEmail);
			}
		}
		
		UserSourceServiceConfig serviceConfig = new UserSourceServiceConfig();
		serviceConfig.setServiceUserId(userId);
		serviceConfig.setServiceUserName(fullName);
		serviceConfig.setServiceAccessToken(token);
		serviceConfig.setServiceProviderId(ID);
		serviceConfig.setUserEmail(userEmail);
		serviceConfig.setUserSiteUrl(userSite);
		
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
		//authorizeUrl.secure = 1;
		String authorizationUrl = authorizeUrl.build();
		
		log.info("Youtube Authorization URL: " + authorizationUrl);
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
		result.setDescription("The Google's online video-sharing service");
		result.setSourceAppApiKey(DEVELOPER_KEY);
		result.setSourceAppSecret(CONSUMER_SECRET);
		result.setAuthPagePath(CALLBACK_URL);
		result.setImagePath("/services/youtube/images/youtube_100.gif");
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
