/**
 * 
 */
package com.googlecode.flickr2twitter.services.rest;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import com.googlecode.flickr2twitter.core.ServiceFactory;
import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory;
import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory.Permission;
import com.googlecode.flickr2twitter.datastore.model.GlobalSourceApplicationService;
import com.googlecode.flickr2twitter.datastore.model.GlobalTargetApplicationService;
import com.googlecode.flickr2twitter.datastore.model.User;
import com.googlecode.flickr2twitter.datastore.model.UserSourceServiceConfig;
import com.googlecode.flickr2twitter.datastore.model.UserTargetServiceConfig;
import com.googlecode.flickr2twitter.intf.ISourceServiceProvider;
import com.googlecode.flickr2twitter.intf.ITargetServiceProvider;
import com.googlecode.flickr2twitter.model.IItem;
import com.googlecode.flickr2twitter.services.rest.models.GlobalApplicationConfigModel;
import com.googlecode.flickr2twitter.services.rest.models.GlobalApplicationConfigModelList;
import com.googlecode.flickr2twitter.services.rest.models.GlobalSourceApplicationServiceModel;
import com.googlecode.flickr2twitter.services.rest.models.GlobalTargetApplicationServiceModel;
import com.googlecode.flickr2twitter.services.rest.models.ISociaHubResource;
import com.googlecode.flickr2twitter.services.rest.models.UserModel;
import com.googlecode.flickr2twitter.services.rest.models.UserSourceServiceConfigModel;
import com.googlecode.flickr2twitter.services.rest.models.UserTargetServiceConfigModel;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class SocialHubServerResource extends ServerResource implements ISociaHubResource {
	private static final Logger log = Logger.getLogger(SocialHubServerResource.class.getName());
	
	/**
	 * 
	 */
	public SocialHubServerResource() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.services.rest.models.ISociaHubResource#getSupportedServiceProviders()
	 */
	@Get
	public GlobalApplicationConfigModelList getSupportedServiceProviders() {
		final List<GlobalApplicationConfigModel> data = new ArrayList<GlobalApplicationConfigModel>();
		data.addAll(getSupportedSourceServiceProviders());
		data.addAll(getSupportedTargetServiceProviders());
		GlobalApplicationConfigModelList list = new GlobalApplicationConfigModelList(data);
		log.info("Retrieving supported service providers -> " + list);
		return list;
	}

	@Post
	public UserModel retrieve(String userEmail) {
		log.info("Retrieving user information for -> " + userEmail);
		User user = MyPersistenceManagerFactory.getUser(userEmail);
		if (user != null) {
			UserModel model = new UserModel(user.getUserId().getEmail(), 
					user.getPassword(), user.getPermission(), user.getScreenName());
			model.setSourceServices(convertSourceData(
					MyPersistenceManagerFactory.getUserSourceServices(userEmail)));
			model.setTargetServices(convertTargetData(
					MyPersistenceManagerFactory.getUserTargetServices(userEmail)));
			return model;
		}
		return null;
	}

	@Put
	public boolean registerNewUser(UserModel user) {
		if (MyPersistenceManagerFactory.createNewUser(user.getUserId(), 
				user.getPassword(), user.getScreenName(), Permission.valueOf(user.getPermission())) != null)
			return true;
		
		return false;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.services.rest.models.ISociaHubResource#login(java.lang.String, java.lang.String)
	 */
	@Post
	public UserModel login(String userEmail, String password) {
		User user = null;
		log.info("user logging in for -> " + userEmail);
		user = MyPersistenceManagerFactory.getLoginUser(userEmail, password);
		if (user != null) {
			UserModel model = new UserModel(user.getUserId().getEmail(), 
					user.getPassword(), user.getPermission(), user.getScreenName());
			model.setSourceServices(convertSourceData(
					MyPersistenceManagerFactory.getUserSourceServices(userEmail)));
			model.setTargetServices(convertTargetData(
					MyPersistenceManagerFactory.getUserTargetServices(userEmail)));
			return model;
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.services.rest.models.ISociaHubResource#openidLogin(java.lang.String, java.lang.String)
	 */
	@Post
	public UserModel openidLogin(String userEmail) {
		log.info("Retrieving opendid user information for -> " + userEmail);
		User user = MyPersistenceManagerFactory.getOpenIdLoginUser(userEmail);
		if (user != null) {
			return new UserModel(user.getUserId().getEmail(), 
					user.getPassword(), user.getPermission(), user.getScreenName());
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.services.rest.models.ISociaHubResource#remove(com.googlecode.flickr2twitter.services.rest.models.UserModel)
	 */
	@Delete
	public boolean remove(UserModel user) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.services.rest.models.ISociaHubResource#getSupportedTargetServiceProviders()
	 */
	private List<GlobalTargetApplicationServiceModel> getSupportedTargetServiceProviders() {
		final List<GlobalTargetApplicationServiceModel> data = 
			new ArrayList<GlobalTargetApplicationServiceModel>();
		for (ITargetServiceProvider targetProvider : ServiceFactory.getAllTargetProviders()) {
			GlobalTargetApplicationService globalTargetService = 
				MyPersistenceManagerFactory.getGlobalTargetAppService(targetProvider.getId());
			if (globalTargetService != null) {
				data.add(new GlobalTargetApplicationServiceModel(targetProvider.getId(),
						globalTargetService.getAppName(), globalTargetService.getDescription(), 
						globalTargetService.getAuthPagePath(), globalTargetService.getConfigPagePath(), 
						globalTargetService.getImagePath(), globalTargetService.getTargetAppConsumerId(), 
						globalTargetService.getTargetAppConsumerSecret()));
			}
		}
		
		return data;
	}
	


	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.services.rest.models.ISociaHubResource#getSupportedSourceServiceProviders()
	 */
	private List<GlobalSourceApplicationServiceModel> getSupportedSourceServiceProviders() {
		final List<GlobalSourceApplicationServiceModel> data = 
			new ArrayList<GlobalSourceApplicationServiceModel>();
		for (ISourceServiceProvider<IItem> srcProvider : ServiceFactory.getAllSourceProviders()) {
			GlobalSourceApplicationService globalSrcService = 
				MyPersistenceManagerFactory.getGlobalSourceAppService(srcProvider.getId());
			if (globalSrcService != null) {
				data.add(new GlobalSourceApplicationServiceModel(srcProvider.getId(),
						globalSrcService.getAppName(), globalSrcService.getDescription(), 
						globalSrcService.getAuthPagePath(), globalSrcService.getConfigPagePath(), 
						globalSrcService.getImagePath(), globalSrcService.getSourceAppApiKey(), 
						globalSrcService.getSourceAppSecret()));
			}
		}
		return data;
	}
	
	public List<UserSourceServiceConfigModel> convertSourceData(
			List<UserSourceServiceConfig> userConfigs) {
		List<UserSourceServiceConfigModel> data = new ArrayList<UserSourceServiceConfigModel>();
		for (UserSourceServiceConfig userConfig : userConfigs) {
			try {
				data.add(new UserSourceServiceConfigModel(userConfig.getUserEmail(), userConfig.getServiceUserId(), 
						userConfig.getServiceUserName(), userConfig.getUserSiteUrl(), userConfig.getServiceProviderId(), 
						userConfig.getAdditionalParameters(), userConfig.isEnabled(), userConfig.getServiceAccessToken()));
			} catch (UnsupportedEncodingException e) {
				log.throwing(this.getClass().getName(), "convertSourceData", e);
			}
		}
		return data;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.services.rest.models.ISociaHubResource#getUserSourceServiceConfigs(java.lang.String)
	 */
	@Post
	public List<UserSourceServiceConfigModel> getUserSourceServiceConfigs(
			String userEmail) {
		return convertSourceData(MyPersistenceManagerFactory.getUserSourceServices(userEmail));
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.services.rest.models.ISociaHubResource#addUserSourceServiceConfig(java.lang.String, com.googlecode.flickr2twitter.services.rest.models.UserSourceServiceConfigModel)
	 */
	@Post
	public void addUserSourceServiceConfig(String userEmail,
			UserSourceServiceConfigModel sourceServiceConfig) {
		if (sourceServiceConfig != null) {
			UserSourceServiceConfig srcService = new UserSourceServiceConfig();
			srcService.setServiceUserId(sourceServiceConfig.getServiceUserId());
			srcService.setServiceUserName(sourceServiceConfig.getServiceUserName());
			srcService.setServiceAccessToken(sourceServiceConfig.getServiceAccessToken());
			srcService.setServiceProviderId(sourceServiceConfig.getServiceProviderId());
			srcService.setUserEmail(userEmail);
			srcService.setUserSiteUrl(sourceServiceConfig.getUserSiteUrl());
			try {
				srcService.setAdditionalParameters(sourceServiceConfig.getAdditionalParameters());
			} catch (UnsupportedEncodingException e) {
				log.throwing(this.getClass().getName(), "addUserSourceServiceConfig", e);
			}
			MyPersistenceManagerFactory.addSourceServiceApp(userEmail, srcService);
		}
	}
	
	public List<UserTargetServiceConfigModel> convertTargetData(
			List<UserTargetServiceConfig> userConfigs) {
		List<UserTargetServiceConfigModel> data = new ArrayList<UserTargetServiceConfigModel>();
		for (UserTargetServiceConfig userConfig : userConfigs) {
			try {
				data.add(new UserTargetServiceConfigModel(userConfig.getUserEmail(), userConfig.getServiceUserId(), 
						userConfig.getServiceUserName(), userConfig.getUserSiteUrl(), userConfig.getServiceProviderId(), 
						userConfig.getAdditionalParameters(), userConfig.isEnabled(), userConfig.getServiceAccessToken(), 
						userConfig.getServiceTokenSecret()));
			} catch (UnsupportedEncodingException e) {
				log.throwing(this.getClass().getName(), "convertTargetData", e);
			}
		}
		return data;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.services.rest.models.ISociaHubResource#getUserTargetServiceConfigs(java.lang.String)
	 */
	@Post
	public List<UserTargetServiceConfigModel> getUserTargetServiceConfigs(
			String userEmail) {
		return convertTargetData(MyPersistenceManagerFactory.getUserTargetServices(userEmail));
	}
	@Put
	public void addUserTargetServiceConfig(UserTargetServiceConfigModel targetModel) {
		log.info("Saving user target service config->" + targetModel);
		if (targetModel != null) {
			UserTargetServiceConfig targetService = new UserTargetServiceConfig();
			targetService.setServiceUserId(targetModel.getServiceUserId());
			targetService.setServiceUserName(targetModel.getServiceUserName());
			targetService.setServiceProviderId(targetModel.getServiceProviderId());
			targetService.setUserEmail(targetModel.getUserEmail());
			try {
				targetService.setAdditionalParameters(targetModel.getAdditionalParameters());
			} catch (UnsupportedEncodingException e) {
				log.throwing(this.getClass().getName(), "addUserTargetServiceConfig", e);
			}
			targetService.setUserSiteUrl(targetModel.getUserSiteUrl());
			targetService.setServiceAccessToken(targetModel.getServiceAccessToken());
			targetService.setServiceTokenSecret(targetModel.getServiceTokenSecret());
			MyPersistenceManagerFactory.addTargetServiceApp(targetModel.getUserEmail(), targetService);
		}
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.services.rest.models.ISociaHubResource#addUserTargetServiceConfig(java.lang.String, com.googlecode.flickr2twitter.services.rest.models.UserTargetServiceConfigModel)
	 */
	
	/*public void addUserTargetServiceConfig(String userEmail,
			UserTargetServiceConfigModel targetServiceConfig) {
		log.info("Saving user target service config->" + targetServiceConfig);
		if (targetServiceConfig != null) {
			UserTargetServiceConfig targetService = new UserTargetServiceConfig();
			targetService.setServiceUserId(targetServiceConfig.getServiceUserId());
			targetService.setServiceUserName(targetServiceConfig.getServiceUserName());
			targetService.setServiceAccessToken(targetServiceConfig.getServiceAccessToken());
			targetService.setServiceProviderId(targetServiceConfig.getServiceProviderId());
			targetService.setUserEmail(userEmail);
			targetService.setUserSiteUrl(targetServiceConfig.getUserSiteUrl());
			targetService.setServiceAccessToken(targetServiceConfig.getServiceAccessToken());
			targetService.setServiceTokenSecret(targetServiceConfig.getServiceTokenSecret());
			try {
				targetService.setAdditionalParameters(targetServiceConfig.getAdditionalParameters());
			} catch (UnsupportedEncodingException e) {
				log.throwing(this.getClass().getName(), "addUserTargetServiceConfig", e);
			}
			MyPersistenceManagerFactory.addTargetServiceApp(userEmail, targetService);
		}
	}*/

	

}
