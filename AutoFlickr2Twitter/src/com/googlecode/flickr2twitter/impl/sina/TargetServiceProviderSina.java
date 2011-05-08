/**
 * 
 */
package com.googlecode.flickr2twitter.impl.sina;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import twitter4j.GeoLocation;

import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory;
import com.googlecode.flickr2twitter.datastore.model.GlobalTargetApplicationService;
import com.googlecode.flickr2twitter.datastore.model.UserTargetServiceConfig;
import com.googlecode.flickr2twitter.intf.ITargetServiceProvider;
import com.googlecode.flickr2twitter.model.IDescriptiveItem;
import com.googlecode.flickr2twitter.model.IGeoItem;
import com.googlecode.flickr2twitter.model.IItem;
import com.googlecode.flickr2twitter.model.IItemList;
import com.googlecode.flickr2twitter.model.ILinkableItem;
import com.googlecode.flickr2twitter.model.IPhoto;
import com.googlecode.flickr2twitter.model.IShortUrl;
import com.googlecode.flickr2twitter.model.IVideo;
import com.googlecode.flickr2twitter.org.apache.commons.lang3.StringUtils;
import com.googlecode.flickr2twitter.sina.weibo4j.User;
import com.googlecode.flickr2twitter.sina.weibo4j.Weibo;
import com.googlecode.flickr2twitter.sina.weibo4j.http.RequestToken;
import com.googlecode.flickr2twitter.urlshorteners.BitLyUtils;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 * 
 */
public class TargetServiceProviderSina implements ITargetServiceProvider {
	public static final String ID = "sina";
	public static final String DISPLAY_NAME = "Sina";
	public static final String CALLBACK_URL = "sinacallback";

	private static final Logger log = Logger
			.getLogger(TargetServiceProviderSina.class.getName());

	/**
	 * 
	 */
	public TargetServiceProviderSina() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.googlecode.flickr2twitter.intf.ITargetServiceProvider#getId
	 * ()
	 */
	@Override
	public String getId() {
		return ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.googlecode.flickr2twitter.intf.ITargetServiceProvider#postUpdate
	 * (com.googlecode.flickr2twitter.datastore.model.
	 * GlobalTargetApplicationService,
	 * com.googlecode.flickr2twitter.datastore
	 * .model.UserTargetServiceConfig, java.util.List)
	 */
	@Override
	public void postUpdate(GlobalTargetApplicationService globalAppConfig,
			UserTargetServiceConfig targetConfig, List<IItemList<IItem>> items)
			throws Exception {
		// api key and secret
		System.setProperty("weibo4j.oauth.consumerKey", globalAppConfig.getTargetAppConsumerId());
		System.setProperty("weibo4j.oauth.consumerSecret",
				globalAppConfig.getTargetAppConsumerSecret());

		Weibo weibo = new Weibo();

		weibo.setToken(targetConfig.getServiceAccessToken(),
				targetConfig.getServiceTokenSecret());
		for (IItemList<IItem> itemList : items) {
			log.info("Processing items from: " + itemList.getListTitle());
			for (IItem item : itemList.getItems()) {
				log.info("Posting message -> " + item + " for "
						+ targetConfig.getServiceUserName());

				GeoLocation geoLoc = null;
				if (item instanceof IGeoItem) {
					if (((IGeoItem) item).getGeoData() != null) {
						IGeoItem geoItem = (IGeoItem) item;
						geoLoc = new GeoLocation(
								geoItem.getGeoData().getLatitude(), geoItem
								.getGeoData().getLongitude());
					}
				}
				String message = null;
				if (item instanceof IPhoto) {
					IPhoto photo = (IPhoto) item;
					message = "My new photo: " + photo.getTitle();
					String url = photo.getUrl();
					if (photo instanceof IShortUrl) {
						url = ((IShortUrl) photo).getShortUrl();
					}/* 
					//no need to shorten the URL because sina would do that anyway
					else if (photo.getUrl().length() > 15){
						url = BitLyUtils.shortenUrl(photo.getUrl());
					}*/
					message += " " + url;
				} else if (item instanceof IVideo) {
					IVideo media = (IVideo) item;
					message = "My new video: " + media.getTitle();
					message += " " + media.getUrl();
				}  else if (item instanceof IDescriptiveItem) {
					IDescriptiveItem ditem = (IDescriptiveItem)item;
					message = ditem.getTitle();
					String url = ditem.getUrl();
					if (ditem instanceof IShortUrl) {
						url = ((IShortUrl) ditem).getShortUrl();
					} else if (ditem.getUrl().length() > 15){
						url = BitLyUtils.shortenUrl(ditem.getUrl());
					}
					message += " " + url;
				} else if (item instanceof ILinkableItem) {
					ILinkableItem litem = (ILinkableItem) item;
					message = "My new item: " + item.getTitle();
					String url = litem.getUrl();
					if (litem instanceof IShortUrl) {
						url = ((IShortUrl) litem).getShortUrl();
					} else if (litem.getUrl().length() > 15){
						url = BitLyUtils.shortenUrl(litem.getUrl());
					}
					message += " " + url;
				} else {
					message = item.getTitle();
				}
				if (message != null) {
					try {
						if (geoLoc != null) {
							weibo.updateStatus(message, geoLoc.getLatitude(),
									geoLoc.getLongitude());
						} else {
							weibo.updateStatus(message);
						}
					} catch (Exception e) {
						log.warning("Failed posting message ->" + message
								+ ". Cause: " + e);
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.gmail.yuyang226.autoflickr2twitter.intf.IServiceAuthorizer#
	 * readyAuthorization(java.lang.String, java.util.Map)
	 */
	@Override
	public String readyAuthorization(String userEmail, Map<String, Object> data)
			throws Exception {
		if (data == null || data.containsKey("token") == false) {
			throw new IllegalArgumentException("Invalid data: " + data);
		}
		StringBuffer buf = new StringBuffer();

		String token = String.valueOf(data.get("token"));
		String secret = String.valueOf(data.get("secret"));

		for (UserTargetServiceConfig service : MyPersistenceManagerFactory
				.getUserTargetServices(userEmail)) {
			if (token.equals(service.getServiceAccessToken())
					&& secret.equals(service.getServiceTokenSecret())) {
				throw new IllegalArgumentException(
						"Target already registered: " + ID);
			}
		}
		
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
		System.setProperty("weibo4j.oauth.consumerSecret",
				Weibo.CONSUMER_SECRET);
		String userId = String.valueOf(data.get("userId"));
		Weibo weibo = new Weibo();
		weibo.setToken(token,
				secret);
		User user = weibo.showUser(userId);
		String screenName = user.getScreenName();
		UserTargetServiceConfig service = new UserTargetServiceConfig();
		service.setServiceProviderId(ID);
		service.setServiceAccessToken(token);
		service.setServiceTokenSecret(secret);
		service.setServiceUserId(userId);
		service.setUserEmail(userEmail);
		service.setServiceUserName(screenName);
		service.setUserSiteUrl(user.getURL() != null ? user.getURL().toExternalForm() : "http://t.sina.com.cn/" + userId);
		MyPersistenceManagerFactory.addTargetServiceApp(userEmail, service);

		buf.append(ID).append(token).append(secret);
		return buf.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.gmail.yuyang226.autoflickr2twitter.intf.IServiceAuthorizer#
	 * requestAuthorization()
	 */
	@Override
	public Map<String, Object> requestAuthorization(String baseUrl) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		GlobalTargetApplicationService globalAppConfig = MyPersistenceManagerFactory
		.getGlobalTargetAppService(ID);
		if (globalAppConfig == null
				|| ID.equalsIgnoreCase(globalAppConfig.getProviderId()) == false) {
			throw new IllegalArgumentException(
					"Invalid source service provider: " + globalAppConfig);
		}
		if (baseUrl.endsWith("/oauth")) {
			baseUrl = StringUtils.left(baseUrl, baseUrl.length() - "/oauth".length());
		}
		String backUrl = baseUrl + "/" + CALLBACK_URL;
		
		System.setProperty("weibo4j.oauth.consumerKey", globalAppConfig.getTargetAppConsumerId());
		System.setProperty("weibo4j.oauth.consumerSecret",
				globalAppConfig.getTargetAppConsumerSecret());
	    Weibo weibo = new Weibo();
		RequestToken requestToken = weibo.getOAuthRequestToken(backUrl);

		System.out.println("Got request token.");
		System.out.println("Request token: " + requestToken.getToken());
		System.out.println("Request token secret: "
				+ requestToken.getTokenSecret());
		
		String url = requestToken.getAuthorizationURL();
		result.put("token", requestToken.getToken());
		result.put("secret", requestToken.getTokenSecret());
		log.info("Sina Authorization URL: " + url);
		result.put("url", url);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.googlecode.flickr2twitter.intf.IServiceProvider#
	 * createDefaultGlobalApplicationConfig()
	 */
	@Override
	public GlobalTargetApplicationService createDefaultGlobalApplicationConfig() {
		GlobalTargetApplicationService result = new GlobalTargetApplicationService();
		result.setAppName(DISPLAY_NAME);
		result.setProviderId(ID);
		result.setDescription("The MaLeGeBi's leading online micro-blog service");
		result.setTargetAppConsumerId(Weibo.CONSUMER_KEY);
		result.setTargetAppConsumerSecret(Weibo.CONSUMER_SECRET);
		result.setAuthPagePath(CALLBACK_URL);
		result.setImagePath("/services/sina/images/sina_100.gif");
		return result;
	}
}
