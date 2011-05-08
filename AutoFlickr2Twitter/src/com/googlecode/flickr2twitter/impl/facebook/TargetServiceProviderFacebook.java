package com.googlecode.flickr2twitter.impl.facebook;

import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory;
import com.googlecode.flickr2twitter.datastore.model.GlobalTargetApplicationService;
import com.googlecode.flickr2twitter.datastore.model.User;
import com.googlecode.flickr2twitter.datastore.model.UserTargetServiceConfig;
import com.googlecode.flickr2twitter.intf.ITargetServiceProvider;
import com.googlecode.flickr2twitter.model.IDescriptiveItem;
import com.googlecode.flickr2twitter.model.IItem;
import com.googlecode.flickr2twitter.model.IItemList;
import com.googlecode.flickr2twitter.model.ILinkableItem;
import com.googlecode.flickr2twitter.model.IMedia;
import com.googlecode.flickr2twitter.model.IPhoto;
import com.googlecode.flickr2twitter.model.IShortUrl;
import com.googlecode.flickr2twitter.org.apache.commons.lang3.StringUtils;
import com.googlecode.flickr2twitter.urlshorteners.BitLyUtils;
import com.googlecode.flickr2twitter.utils.FacebookUtil;

public class TargetServiceProviderFacebook implements ITargetServiceProvider {

	private static final Logger log = Logger
			.getLogger(TargetServiceProviderFacebook.class.getName());

	public static final String ID = "facebook";
	public static final String DISPLAY_NAME = "Facebook";
	public static final String CALLBACK_URL = "facebookcallback.jsp";
	public static final String PARA_CODE = "code";

	@Override
	public Map<String, Object> requestAuthorization(String baseUrl)
			throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		if (baseUrl.endsWith("/oauth")) {
			baseUrl = StringUtils.left(baseUrl,
					baseUrl.length() - "oauth".length());
		}

		if (baseUrl.endsWith("/") == false) {
			baseUrl += "/";
		}
		String callbackURL = URLEncoder.encode(baseUrl + CALLBACK_URL, "UTF-8");
		String facebookautURL = MessageFormat.format(FacebookUtil.AUTH_URL,
				callbackURL);
		log.info("Facebook auth URL: " + facebookautURL);
		result.put("url", facebookautURL);
		return result;
	}

	@Override
	public String readyAuthorization(String userEmail, Map<String, Object> data)
			throws Exception {
		log.info("Ready Authing facebook....");
		log.info("User Email: " + userEmail);

		if (data == null || data.containsKey(PARA_CODE) == false) {
			throw new IllegalArgumentException("Invalid data: " + data);
		}

		User user = MyPersistenceManagerFactory.getUser(userEmail);
		if (user == null) {
			throw new IllegalArgumentException(
					"Can not find the specified user: " + userEmail);
		}

		String code = (String) data.get(PARA_CODE);

		log.info("code: " + code);

		StringBuffer buf = new StringBuffer();

		log.info("Trying to retrieve user details from facebook....");
		String token = FacebookUtil.gaeGetToken(code);
		String[] userDetails = FacebookUtil.gaeGetUserDetails(token);

		log.info("User details from facebook user id:\"" + userDetails[0]
				+ "\", user name:\"" + userDetails[1] + "\"");

		log.info("Trying to delete old facebook auth data for facebook user id:\""
				+ userDetails[0] + "\", user name:\"" + userDetails[1] + "\"");

		boolean deletedOldUser = MyPersistenceManagerFactory
				.deleteOldUserTargetSource(userDetails[0], userEmail);

		if (deletedOldUser == true) {
			log.info("Found and deleted old user.");
		} else {
			log.info("No old user found.");
		}

		buf.append("Facebook Auth Code: " + code);

		log.info("Writing data to database...");
		UserTargetServiceConfig service = new UserTargetServiceConfig();
		service.setServiceProviderId(ID);
		service.setServiceAccessToken(token);
		service.setServiceTokenSecret(code);
		service.setServiceUserId(userDetails[0]);
		service.setServiceUserName(userDetails[1]);
		service.setUserSiteUrl("http://www.facebook.com");

		service.setUserEmail(user.getUserId().getEmail());
		MyPersistenceManagerFactory.addTargetServiceApp(userEmail, service);
		log.info("Writing data to database done!");

		buf.append("Facebook Authentication success\n");
		// This token can be used until the user revokes it.

		// ----------------------
		String retMsg = buf.toString();
		log.info(retMsg);
		return retMsg;
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public GlobalTargetApplicationService createDefaultGlobalApplicationConfig() {
		GlobalTargetApplicationService result = new GlobalTargetApplicationService();
		result.setAppName(DISPLAY_NAME);
		result.setProviderId(ID);
		result.setDescription("Facebook Status service");
		result.setTargetAppConsumerId(FacebookUtil.APP_ID);
		result.setTargetAppConsumerSecret(FacebookUtil.APP_SECRET);
		result.setAuthPagePath(CALLBACK_URL);
		result.setImagePath("/services/facebook/images/facebook_100.gif");
		return result;
	}

	@Override
	public void postUpdate(GlobalTargetApplicationService globalAppConfig,
			UserTargetServiceConfig targetConfig, List<IItemList<IItem>> items)
			throws Exception {
		if (items == null || items.size() == 0) {
			return;
		}

		String token = targetConfig.getServiceAccessToken();
		if (token == null || token.length() == 0) {
			log.info("Failed To Retrieve Facebook Token!");
		}

		for (IItemList<IItem> itemList : items) {
			log.info("Processing items from: " + itemList.getListTitle());
			for (IItem item : itemList.getItems()) {
				log.info("Posting message -> " + item + " for "
						+ targetConfig.getServiceUserName());

				String message = null;
				if (item instanceof IPhoto) {
					IPhoto photo = (IPhoto) item;
					message = "My new photo: " + photo.getTitle();
					String url = photo.getUrl();
					message += " " + url;
				} else if (item instanceof IMedia) {
					IMedia media = (IMedia) item;
					message = "My new post: " + media.getTitle();
					String url = media.getUrl();
					message += " " + url;
				} else if (item instanceof IDescriptiveItem) {
					IDescriptiveItem ditem = (IDescriptiveItem) item;
					message = ditem.getTitle();
					message += " " + ditem.getUrl();
				} else if (item instanceof ILinkableItem) {
					ILinkableItem litem = (ILinkableItem) item;
					message = "My new item: " + item.getTitle();
					String url = litem.getUrl();
					if (litem instanceof IShortUrl) {
						url = ((IShortUrl) litem).getShortUrl();
					}
					
					message += " " + url;
				} else {
					message = item.getTitle();
				}
				if (message != null) {
					try {
						FacebookUtil.gaePostMessage(message, token);
					} catch (Exception e) {
						log.warning("Failed posting message ->" + message
								+ ". Cause: " + e);
					}
				}
			}
		}

	}

}
