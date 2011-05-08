/**
 * 
 */
package com.googlecode.flickr2twitter.impl.email;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.appengine.api.mail.MailServiceFactory;
import com.google.appengine.api.mail.MailService.Message;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory;
import com.googlecode.flickr2twitter.datastore.model.GlobalTargetApplicationService;
import com.googlecode.flickr2twitter.datastore.model.UserTargetServiceConfig;
import com.googlecode.flickr2twitter.intf.IServiceProvider;
import com.googlecode.flickr2twitter.intf.ITargetServiceProvider;
import com.googlecode.flickr2twitter.model.IItem;
import com.googlecode.flickr2twitter.model.IItemList;
import com.googlecode.flickr2twitter.model.IPhoto;
import com.googlecode.flickr2twitter.org.apache.commons.lang3.StringUtils;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class TargetServiceProviderEmail implements ITargetServiceProvider, 
IServiceProvider<GlobalTargetApplicationService> {
	public static final String ID = "email";
	public static final String DISPLAY_NAME = "Email";
	public static final String TIMEZONE_CST = "CST";
	private static final Logger log = Logger.getLogger(TargetServiceProviderEmail.class.getName());
	
	/**
	 * 
	 */
	public TargetServiceProviderEmail() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.intf.ITargetServiceProvider#postUpdate(com.googlecode.flickr2twitter.datastore.model.GlobalTargetApplicationService, com.googlecode.flickr2twitter.datastore.model.UserTargetServiceConfig, java.util.List)
	 */
	@Override
	public void postUpdate(GlobalTargetApplicationService globalAppConfig,
			UserTargetServiceConfig targetConfig, List<IItemList<IItem>> items)
	throws Exception {
		UserService userService = UserServiceFactory.getUserService();
		if (userService == null || userService.getCurrentUser() == null) {
			throw new IllegalArgumentException("Can not get the current Google account user");
		}
		log.info("Current User: " + userService.getCurrentUser());
		String email = userService.getCurrentUser().getEmail();
		log.info("Admin user email:" + email);
	    
		Message msg = new Message();
		msg.setReplyTo(globalAppConfig.getTargetAppConsumerId());
		msg.setSender(email);
		
		msg.setSubject("[flickr2twi] flickr2twitter just found some new updates!");
		
		msg.setTo(StringUtils.split(targetConfig.getServiceUserId(), ","));
		StringBuffer buf = new StringBuffer();
		for (IItemList<IItem> itemList : items) {
			log.info("Processing items from: " + itemList.getListTitle());
			if (itemList.getItems().isEmpty() == false) {
				buf.append("<p>");
				buf.append("<b>");
				buf.append(itemList.getListTitle());
				buf.append("</b><br><br>");

				for (IItem item : itemList.getItems()) {
					log.info("Posting message -> " + item + " for "
							+ targetConfig.getServiceUserName());
					buf.append(item.getTitle());
					if (StringUtils.isNotBlank(item.getDescription())) {
						buf.append(": ");
						buf.append(item.getDescription());
					}
					if (item instanceof IPhoto) {
						IPhoto photo = (IPhoto) item;
						buf.append(". <a href=\"");
						buf.append(photo.getUrl());
						buf.append("\">");
						buf.append(photo.getTitle());
						buf.append("</a>");
					}
					buf.append("<br>");
				}
				buf.append("</p>");
			}
		}
		msg.setHtmlBody(buf.toString());
		MailServiceFactory.getMailService().send(msg);
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.intf.IServiceAuthorizer#readyAuthorization(java.lang.String, java.util.Map)
	 */
	@Override
	public String readyAuthorization(String userEmail, Map<String, Object> data)
			throws Exception {
		UserService userService = UserServiceFactory.getUserService();
		if (userService == null || userService.getCurrentUser() == null) {
			throw new IllegalArgumentException("Can not get the current Google account user");
		}
		log.info("Current User: " + userService.getCurrentUser());
		String email = userService.getCurrentUser().getEmail();
		log.info("Admin user email:" + email);
		
		UserTargetServiceConfig service = new UserTargetServiceConfig();
		service.setServiceProviderId(ID);
		service.setServiceAccessToken("");
		service.setServiceTokenSecret("");
		service.setServiceUserId(email);
		service.setUserEmail(userEmail);
		service.setServiceUserName(email);
		service.setUserSiteUrl(email);
		MyPersistenceManagerFactory.addTargetServiceApp(userEmail, service);
		return "";
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.intf.IServiceAuthorizer#requestAuthorization(java.lang.String)
	 */
	@Override
	public Map<String, Object> requestAuthorization(String baseUrl)
			throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		UserService userService = UserServiceFactory.getUserService();
		
		if (userService != null) {
			com.google.appengine.api.users.User user = userService.getCurrentUser();
			if (user != null) {
				data.put("email", user.getEmail());
			} else {
				data.put("url", userService.createLoginURL(baseUrl));
			}
		}
		
		return data;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.intf.IServiceProvider#createDefaultGlobalApplicationConfig()
	 */
	@Override
	public GlobalTargetApplicationService createDefaultGlobalApplicationConfig() {
		GlobalTargetApplicationService result = new GlobalTargetApplicationService();
		result.setAppName(DISPLAY_NAME);
		result.setProviderId(ID);
		result.setDescription("The Email target service");
		result.setTargetAppConsumerId("flickr2twitter@googlegroups.com");
		result.setTargetAppConsumerSecret("flickr2twitter@googlegroups.com");
		result.setAuthPagePath(null); // TODO set the default auth page path
		result.setImagePath("/services/email/images/gmail_icon_50.png");
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
