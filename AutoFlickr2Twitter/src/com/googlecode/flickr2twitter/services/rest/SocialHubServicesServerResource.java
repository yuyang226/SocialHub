/**
 * 
 */
package com.googlecode.flickr2twitter.services.rest;

import java.util.Arrays;
import java.util.logging.Logger;

import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory;
import com.googlecode.flickr2twitter.datastore.model.UserSourceServiceConfig;
import com.googlecode.flickr2twitter.datastore.model.UserTargetServiceConfig;
import com.googlecode.flickr2twitter.impl.ebay.SourceServiceProviderEbayKeywords;
import com.googlecode.flickr2twitter.impl.twitter.AbstractServiceProviderTwitter;
import com.googlecode.flickr2twitter.org.apache.commons.lang3.StringUtils;
import com.googlecode.flickr2twitter.services.rest.models.ISociaHubServicesResource;
import com.googlecode.flickr2twitter.servlet.EbayConfigKeywordsServlet;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class SocialHubServicesServerResource extends ServerResource implements ISociaHubServicesResource {
	private static final Logger log = Logger.getLogger(SocialHubServicesServerResource.class.getName());
	
	/**
	 * 
	 */
	public SocialHubServicesServerResource() {
		super();
	}
	
	@Post
	public void addUserServiceConfig(String data) {
		log.info("user source/target service config data->" + data);
		if (data != null) {
			try {
				String[] values = StringUtils.split(data, "/");
				log.info("Received datas->" + Arrays.asList(values));
				if (values.length >= 6 && values[0].equalsIgnoreCase(
						AbstractServiceProviderTwitter.ID)) {
					String providerId = values[0];
					String userEmail = values[1];
					String token = values[2];
					String tokenSecret = values[3];
					String userId = values[4];
					String screenName = values[5];
					
					UserTargetServiceConfig targetConfig = new UserTargetServiceConfig();
					targetConfig.setServiceProviderId(providerId);
					targetConfig.setUserEmail(userEmail);
					targetConfig.setServiceUserId(userId);
					targetConfig.setUserSiteUrl("http://twitter.com/" + screenName);
					targetConfig.setServiceUserName(screenName);
					targetConfig.setServiceAccessToken(token);
					targetConfig.setServiceTokenSecret(tokenSecret);
					log.info("Adding new user target config to database->" + targetConfig);
					MyPersistenceManagerFactory.addTargetServiceApp(userEmail, targetConfig);
				} else if (values.length >= 3 && values[0].equalsIgnoreCase(
						SourceServiceProviderEbayKeywords.ID)) {
					String providerId = values[0];
					String userEmail = values[1];
					String keywords = values[2];
					
					UserSourceServiceConfig sourceConfig = new UserSourceServiceConfig();
					sourceConfig.setServiceProviderId(providerId);
					sourceConfig.setUserEmail(userEmail);
					sourceConfig.setServiceAccessToken(keywords);
					sourceConfig.setServiceUserId(keywords);
					String userSiteUrl = new EbayConfigKeywordsServlet().buildEbayUserSearchUrl(false, keywords, null, null);
					sourceConfig.setUserSiteUrl(userSiteUrl);
					sourceConfig.setServiceUserName(keywords);
					log.info("Adding new user target config to database->" + sourceConfig);
					MyPersistenceManagerFactory.addSourceServiceApp(userEmail, sourceConfig);
				} else {
					log.info("Unsupported service provider->" + data);
				}
			} catch (Exception e) {
				log.throwing(SocialHubServicesServerResource.class.getName(),
						"addUserTargetServiceConfig", e);
			}
		}
	}


}

