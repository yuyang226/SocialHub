/**
 * 
 */
package com.googlecode.flickr2twitter.impl.twitter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;

import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory;
import com.googlecode.flickr2twitter.datastore.model.GlobalApplicationConfig;
import com.googlecode.flickr2twitter.datastore.model.GlobalSourceApplicationService;
import com.googlecode.flickr2twitter.datastore.model.GlobalTargetApplicationService;
import com.googlecode.flickr2twitter.datastore.model.UserServiceConfig;
import com.googlecode.flickr2twitter.datastore.model.UserSourceServiceConfig;
import com.googlecode.flickr2twitter.datastore.model.UserTargetServiceConfig;
import com.googlecode.flickr2twitter.intf.IServiceAuthorizer;
import com.googlecode.flickr2twitter.org.apache.commons.lang3.StringUtils;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public abstract class AbstractServiceProviderTwitter<M extends GlobalApplicationConfig, N extends UserServiceConfig> 
implements IServiceAuthorizer {
	private static final Logger log = Logger.getLogger(AbstractServiceProviderTwitter.class
			.getName());
	public static final String ID = "twitter";
	public static final String DISPLAY_NAME = "Twitter";
	public static final String KEY_TOKEN = "oauth_token";
	public static final String KEY_TOKEN_SECRET = "secret";
	public static final String KEY_OAUTH_VERIFIER = "oauth_verifier";
	public static final String KEY_SOURCE = "source";
	public static final String CALLBACK_URL = "twittercallback.jsp";
	
	/**
	 * 
	 */
	public AbstractServiceProviderTwitter() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.googlecode.flickr2twitter.intf.IServiceAuthorizer#
	 * readyAuthorization(java.lang.String, java.util.Map)
	 */
	@Override
	public String readyAuthorization(String userEmail, Map<String, Object> data)
			throws Exception {
		log.info("User ready for Twitter authorization->" + userEmail + ", data: " + data);
		if (data == null || data.containsKey(KEY_TOKEN) == false 
				|| data.containsKey(KEY_TOKEN_SECRET) == false 
				|| data.containsKey(KEY_OAUTH_VERIFIER) == false) {
			throw new IllegalArgumentException("Invalid data: " + data);
		}
		StringBuffer buf = new StringBuffer();
		
		GlobalApplicationConfig globalAppConfig = getGlobalApplicationConfig();
		log.info("Twitter Global Application Config Data: " + globalAppConfig);
		String consumerId = null;
		String consumerSecret = null;
		if (globalAppConfig instanceof GlobalSourceApplicationService) {
			GlobalSourceApplicationService sourceAppConfig = 
				(GlobalSourceApplicationService)globalAppConfig;
			consumerId = sourceAppConfig.getSourceAppApiKey();
			consumerSecret = sourceAppConfig.getSourceAppSecret();
		} else {
			GlobalTargetApplicationService tagetAppConfig = 
				(GlobalTargetApplicationService)globalAppConfig;
			consumerId = tagetAppConfig.getTargetAppConsumerId();
			consumerSecret = tagetAppConfig.getTargetAppConsumerSecret();
		}
		
		Twitter twitter = new TwitterFactory().getOAuthAuthorizedInstance(
				consumerId,
				consumerSecret);
		log.info("Initialized Twitter client: " + twitter);

		String token = String.valueOf(data.get(KEY_TOKEN));
		String secret = String.valueOf(data.get(KEY_TOKEN_SECRET));
		String oauthVerifier = String.valueOf(data.get(KEY_OAUTH_VERIFIER));
		RequestToken requestToken = new RequestToken(token, secret);
		AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, oauthVerifier);
		
		buf.append(" User Id: " + accessToken.getUserId());
		buf.append(" User Screen Name: " + accessToken.getScreenName());
		buf.append(" Access Token: " + accessToken.getToken());
		buf.append(" Token Secret: " + accessToken.getTokenSecret());
		log.info("Twitter User Data: " + buf);
		for (UserServiceConfig service : getUserServiceConfigs(userEmail)) {
			String aToken = accessToken.getToken();
			boolean duplicate = false;
			if (service instanceof UserSourceServiceConfig) {
				duplicate = aToken.equals(((UserSourceServiceConfig)service).getServiceAccessToken());
			} else {
				duplicate = aToken.equals(((UserTargetServiceConfig)service).getServiceAccessToken());
			}
			if (duplicate == true) {
				throw new IllegalArgumentException("Token already registered: "
						+ accessToken.getToken());
			}
		}
		UserServiceConfig serviceConfig = createNewUserServiceConfig();
		serviceConfig.setServiceProviderId(ID);
		serviceConfig.setUserEmail(userEmail);
		serviceConfig.setServiceUserId(String.valueOf(accessToken.getUserId()));
		serviceConfig.setServiceUserName(accessToken.getScreenName());
		serviceConfig.setUserSiteUrl("http://twitter.com/"
				+ accessToken.getScreenName());
		if (serviceConfig instanceof UserSourceServiceConfig) {
			UserSourceServiceConfig srcConfig = (UserSourceServiceConfig) serviceConfig;
			srcConfig.setServiceAccessToken(accessToken.getToken());
			srcConfig.addAddtionalParameter(KEY_TOKEN_SECRET, accessToken.getTokenSecret());
			MyPersistenceManagerFactory.addSourceServiceApp(userEmail, srcConfig);
		} else {
			UserTargetServiceConfig targetConfig = (UserTargetServiceConfig) serviceConfig;
			targetConfig.setServiceAccessToken(accessToken.getToken());
			targetConfig.setServiceTokenSecret(accessToken.getTokenSecret());
			log.info("Adding new user target config to database->" + targetConfig);
			MyPersistenceManagerFactory.addTargetServiceApp(userEmail, targetConfig);
		}
		return buf.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.googlecode.flickr2twitter.intf.IServiceAuthorizer#
	 * requestAuthorization()
	 */
	@Override
	public Map<String, Object> requestAuthorization(String baseUrl) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			GlobalApplicationConfig globalAppConfig = getGlobalApplicationConfig();
			Twitter twitter = new TwitterFactory().getInstance();
			if (globalAppConfig instanceof GlobalSourceApplicationService) {
				GlobalSourceApplicationService sourceAppConfig = 
					(GlobalSourceApplicationService)globalAppConfig;
				twitter.setOAuthConsumer(sourceAppConfig.getSourceAppApiKey(),
						sourceAppConfig.getSourceAppSecret());
			} else {
				GlobalTargetApplicationService tagetAppConfig = 
					(GlobalTargetApplicationService)globalAppConfig;
				twitter.setOAuthConsumer(tagetAppConfig.getTargetAppConsumerId(),
						tagetAppConfig.getTargetAppConsumerSecret());
			}
			
			if (baseUrl.endsWith("/oauth")) {
				baseUrl = StringUtils.left(baseUrl, baseUrl.length() - "/oauth".length());
			}
			String callbackUrl = baseUrl + "/" + globalAppConfig.getAuthPagePath();
			RequestToken requestToken = twitter.getOAuthRequestToken(callbackUrl);
			log.info("Authentication URL: " + requestToken.getAuthenticationURL());
			log.info("Authorization URL: " + requestToken.getAuthorizationURL());
			result.put("url", requestToken.getAuthorizationURL());
			result.put("token", requestToken.getToken());
			result.put("secret", requestToken.getTokenSecret());
		} catch (TwitterException e) {
			throw e;
		}
		return result;
	}
	
	public String getId() {
		return ID;
	}
	
	protected abstract M getGlobalApplicationConfig();
	
	protected abstract List<N> getUserServiceConfigs(String userEmail);
	
	protected abstract N createNewUserServiceConfig();

}
