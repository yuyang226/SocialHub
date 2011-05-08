/**
 * 
 */
package com.googlecode.flickr2twitter.impl.twitter;

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import twitter4j.GeoLocation;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.PropertyConfiguration;
import twitter4j.http.AccessToken;
import twitter4j.http.Authorization;
import twitter4j.http.OAuthAuthorization;

import com.googlecode.flickr2twitter.core.GlobalDefaultConfiguration;
import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory;
import com.googlecode.flickr2twitter.datastore.model.GlobalTargetApplicationService;
import com.googlecode.flickr2twitter.datastore.model.UserTargetServiceConfig;
import com.googlecode.flickr2twitter.intf.ITargetServiceProvider;
import com.googlecode.flickr2twitter.model.IDescriptiveItem;
import com.googlecode.flickr2twitter.model.IGeoItem;
import com.googlecode.flickr2twitter.model.IItem;
import com.googlecode.flickr2twitter.model.IItemList;
import com.googlecode.flickr2twitter.model.ILinkableItem;
import com.googlecode.flickr2twitter.model.IMedia;
import com.googlecode.flickr2twitter.model.IPhoto;
import com.googlecode.flickr2twitter.model.IShortUrl;
import com.googlecode.flickr2twitter.urlshorteners.BitLyUtils;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 * 
 */
public class TargetServiceProviderTwitter extends AbstractServiceProviderTwitter
<GlobalTargetApplicationService, UserTargetServiceConfig> 
implements ITargetServiceProvider {

	private static final Logger log = Logger.getLogger(TargetServiceProviderTwitter.class
			.getName());

	/**
	 * 
	 */
	public TargetServiceProviderTwitter() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.googlecode.flickr2twitter.intf.ITargetServiceProvider#postUpdate
	 * (com.googlecode.flickr2twitter.model.IItem)
	 */
	@Override
	public void postUpdate(GlobalTargetApplicationService globalAppConfig,
			UserTargetServiceConfig targetConfig, List<IItemList<IItem>> items)
			throws Exception {
		// The factory instance is re-useable and thread safe.
		AccessToken accessToken = new AccessToken(
				targetConfig.getServiceAccessToken(),
				targetConfig.getServiceTokenSecret());
		PropertyConfiguration conf = new PropertyConfiguration(new Properties());

		Authorization auth = new OAuthAuthorization(conf,
				globalAppConfig.getTargetAppConsumerId(),
				globalAppConfig.getTargetAppConsumerSecret(), accessToken);
		Twitter twitter = new TwitterFactory().getInstance(auth);
		
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
						} else if (photo.getUrl().length() > 15){
							url = BitLyUtils.shortenUrl(photo.getUrl());
						}
						message += " " + url;
					} else if (item instanceof IMedia) {
						IMedia media = (IMedia) item;
						message = "My new post: " + media.getTitle();
						String url = media.getUrl();
						if (media instanceof IShortUrl) {
							url = ((IShortUrl) media).getShortUrl();
						} else if (media.getUrl().length() > 15){
							url = BitLyUtils.shortenUrl(media.getUrl());
						}
						message += " " + url;
					} else if (item instanceof IDescriptiveItem) {
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
							Status status = geoLoc == null ? 
												twitter.updateStatus(message) : 
												twitter.updateStatus(message, geoLoc);
									log.info("Successfully updated the status ["
											+ status.getText() + "] to user @"
											+ targetConfig.getServiceUserName());
						} catch (TwitterException e) {
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
	 * @see com.googlecode.flickr2twitter.intf.IServiceProvider#
	 * createDefaultGlobalApplicationConfig()
	 */
	@Override
	public GlobalTargetApplicationService createDefaultGlobalApplicationConfig() {
		GlobalTargetApplicationService result = new GlobalTargetApplicationService();
		result.setAppName(DISPLAY_NAME);
		result.setProviderId(ID);
		result.setDescription("The world's leading online micro-blog service");
		result.setTargetAppConsumerId(GlobalDefaultConfiguration.getInstance()
				.getTwitterConsumerId());
		result.setTargetAppConsumerSecret(GlobalDefaultConfiguration
				.getInstance().getTwitterConsumerSecret());
		result.setAuthPagePath(CALLBACK_URL + "?" + KEY_SOURCE + "=" + Boolean.FALSE);
		result.setImagePath("/services/twitter/images/twitter_100.gif");
		return result;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.impl.twitter.AbstractServiceProviderTwitter#createNewUserServiceConfig()
	 */
	@Override
	protected UserTargetServiceConfig createNewUserServiceConfig() {
		return new UserTargetServiceConfig();
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.impl.twitter.AbstractServiceProviderTwitter#getGlobalApplicationConfig()
	 */
	@Override
	protected GlobalTargetApplicationService getGlobalApplicationConfig() {
		return MyPersistenceManagerFactory.getGlobalTargetAppService(ID);
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.impl.twitter.AbstractServiceProviderTwitter#getUserServiceConfigs(java.lang.String)
	 */
	@Override
	protected List<UserTargetServiceConfig> getUserServiceConfigs(
			String userEmail) {
		return MyPersistenceManagerFactory.getUserTargetServices(userEmail);
	}
}
