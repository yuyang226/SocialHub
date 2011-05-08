/**
 * 
 */
package com.googlecode.flickr2twitter.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.flickr2twitter.core.ServiceFactory;
import com.googlecode.flickr2twitter.core.ServiceRunner;
import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory;
import com.googlecode.flickr2twitter.datastore.model.GlobalServiceConfiguration;
import com.googlecode.flickr2twitter.datastore.model.GlobalSourceApplicationService;
import com.googlecode.flickr2twitter.datastore.model.GlobalTargetApplicationService;
import com.googlecode.flickr2twitter.datastore.model.UserSourceServiceConfig;
import com.googlecode.flickr2twitter.datastore.model.UserTargetServiceConfig;
import com.googlecode.flickr2twitter.intf.ISourceServiceProvider;
import com.googlecode.flickr2twitter.intf.ITargetServiceProvider;
import com.googlecode.flickr2twitter.model.IItem;
import com.googlecode.flickr2twitter.model.IItemList;
import com.googlecode.flickr2twitter.model.ItemList;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public final class ServiceWorkerServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(ServiceWorkerServlet.class.getName());
	
	public static final String QUEUE_NAME_WORKER = "service-worker";

	/**
	 * 
	 */
	public ServiceWorkerServlet() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userEmail = request.getParameter(ServiceRunner.KEY_USER);
		String time = request.getParameter(ServiceRunner.KEY_TIMESTAMP);
		String inter = request.getParameter(ServiceRunner.KEY_INTERNVAL);
		long currentTime = -1;
		long interval = -1;
		try {
			currentTime = Long.parseLong(time);
		} catch (NumberFormatException e) {
			log.warning("Invalid timestamp - " + e.toString());
			return;
		}
		
		try {
			interval = Long.parseLong(inter);
		} catch (NumberFormatException e) {
			log.warning("Invalid time interval - " + e.toString());
			interval = MyPersistenceManagerFactory.getGlobalConfiguration().getMinUploadTime();
		}
		checkUser(userEmail, interval, currentTime);
	}

	private static void checkUser(String userEmail, long interval, long currentTime) {
		/*User user = MyPersistenceManagerFactory.getUser(userEmail);
		if (user == null) {
			log.warning("Can not find the specified user =>" + userEmail);
			return;
		}*/
		GlobalServiceConfiguration globalConfig = new GlobalServiceConfiguration();
		globalConfig.setMinUploadTime(interval);
		log.info("Retrieving latest updates for user: " + userEmail);
		final List<IItemList<IItem>> itemLists = new ArrayList<IItemList<IItem>>();
		boolean isEmpty = true;
		for (UserSourceServiceConfig source : MyPersistenceManagerFactory.getUserSourceServices(userEmail)) {
			ISourceServiceProvider<IItem> sourceProvider = 
				ServiceFactory.getSourceServiceProvider(source.getServiceProviderId());
			GlobalSourceApplicationService globalSvcConfig = 
				MyPersistenceManagerFactory.getGlobalSourceAppService(sourceProvider.getId());
			if (sourceProvider == null) {
				log.warning("Invalid source service provider configured: " + source.getServiceProviderId());
			} else if (source.isEnabled() == false){ 
				log.info("skip the disabled source service provider: " + source.getServiceProviderId());
			} else {
				try {
					IItemList<IItem> items = new ItemList(globalSvcConfig.getAppName());
					List<IItem> unsortedItems = sourceProvider.getLatestItems(globalConfig, globalSvcConfig, source, currentTime);
					
					
					if (unsortedItems.isEmpty() == false) {
						isEmpty = false;
						Collections.sort(unsortedItems, new Comparator<IItem>() {

							@Override
							public int compare(IItem o1, IItem o2) {
								if (o1.getDatePosted() != null && o2.getDatePosted() != null) {
									return o1.getDatePosted().compareTo(o2.getDatePosted());
								}
								return 0;
							}
						});
						IItem mostRecentItem = unsortedItems.get(unsortedItems.size() - 1);
						Date lastItemTime = mostRecentItem.getDatePosted() != null ? mostRecentItem.getDatePosted() : new Date(currentTime);
						try {
							MyPersistenceManagerFactory.updateLastSourceTime(source, lastItemTime);
						} catch (Exception e) {
							log.warning("Failed to update last item time->" + e);
						}
					}
					
					items.setItems(unsortedItems);
					itemLists.add(items);
				} catch (Exception e) {
					log.throwing(ServiceRunner.class.getName(), "", e);
				}
			}
		}
		if (isEmpty) {
			log.info("No recent updates found for user: " + userEmail);
		} else {
			for (UserTargetServiceConfig target : MyPersistenceManagerFactory.getUserTargetServices(userEmail)) {
				ITargetServiceProvider targetProvider = 
					ServiceFactory.getTargetServiceProvider(target.getServiceProviderId());
				GlobalTargetApplicationService globalAppConfig = 
					MyPersistenceManagerFactory.getGlobalTargetAppService(target.getServiceProviderId());
				if (targetProvider == null || globalAppConfig == null) {
					log.warning("Invalid target service provider configured: " + target.getServiceProviderId());
				}  else if (target.isEnabled() == false){ 
					log.info("skip the disabled target service provider: " + target.getServiceProviderId());
				} else {
					try {
						targetProvider.postUpdate(globalAppConfig, target, itemLists);
					} catch (Exception e) {
						log.throwing(ServiceRunner.class.getName(), "", e);
					}
				}
			}
		}
	}

}
