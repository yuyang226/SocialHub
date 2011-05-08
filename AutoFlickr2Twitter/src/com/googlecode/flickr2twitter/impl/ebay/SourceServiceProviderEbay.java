/**
 * 
 */
package com.googlecode.flickr2twitter.impl.ebay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;

import com.googlecode.flickr2twitter.datastore.model.GlobalServiceConfiguration;
import com.googlecode.flickr2twitter.datastore.model.GlobalSourceApplicationService;
import com.googlecode.flickr2twitter.datastore.model.UserSourceServiceConfig;
import com.googlecode.flickr2twitter.impl.flickr.SourceServiceProviderFlickr;
import com.googlecode.flickr2twitter.intf.BaseSourceProvider;
import com.googlecode.flickr2twitter.intf.IConfigurableService;
import com.googlecode.flickr2twitter.model.IItem;

/**
 * @author yayu
 *
 */
public class SourceServiceProviderEbay extends BaseSourceProvider<IItem>implements 
		IConfigurableService {
	public static final String ID = "ebay";
	public static final String DISPLAY_NAME = "eBay";
	public static final String PAGE_NAME_CONFIG = "ebay_config.jsp";

	private static final Logger log = Logger.getLogger(SourceServiceProviderEbay.class.getName());
	/**
	 * 
	 */
	public SourceServiceProviderEbay() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.intf.IConfigurableService#getConfigPagePath()
	 */
	@Override
	public String getConfigPagePath() {
		return PAGE_NAME_CONFIG;
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public GlobalSourceApplicationService createDefaultGlobalApplicationConfig() {
		GlobalSourceApplicationService result = new GlobalSourceApplicationService();
		result.setAppName(getDisplayName());
		result.setProviderId(getId());
		result.setDescription("The world's leading e-commerce site");
		result.setSourceAppApiKey("no_app_api_key");
		result.setSourceAppSecret("no_app_api_secret");
		result.setAuthPagePath(null);
		result.setConfigPagePath(getConfigPagePath());
		result.setImagePath(getImagePath());
		return result;
	}

	@Override
	public List<IItem> getLatestItems(GlobalServiceConfiguration globalConfig,
			GlobalSourceApplicationService globalSvcConfig,
			UserSourceServiceConfig sourceService, long currentTime)
			throws Exception {
		
		String sellerId = sourceService.getServiceUserId();
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone(
				SourceServiceProviderFlickr.TIMEZONE_GMT));
		now.setTimeInMillis(currentTime);
		log.info("Converted current time: " + now.getTime());
		
		Date pastTime = sourceService.getLastUpdateTime();
		if (pastTime == null) {
			Calendar past = Calendar.getInstance(TimeZone.getTimeZone(SourceServiceProviderFlickr.TIMEZONE_GMT));
			long newTime = now.getTime().getTime() - globalConfig.getMinUploadTime();
			past.setTimeInMillis(newTime);
			pastTime = past.getTime();
		}
		
		String env = isSandbox() ? "sandbox " : "";
		log.info("Fetching latest listing for eBay " + env + "user->" + sellerId 
				+ " from " + pastTime + " to " + now.getTime());
		List<EbayItem> ebayItems = isSandbox() ? dao.getSellerListFromSandBox(sellerId, pastTime, now.getTime())
				: dao.getSellerListFromProduction(sellerId, pastTime, now.getTime());
		
		log.info("found " + ebayItems.size() + " items updated recently");
		
		return convert(ebayItems);
	}

	List<IItem> convert(List<EbayItem> ebayItems) {
		List<IItem> items = new ArrayList<IItem>();
		for(EbayItem each :ebayItems) {
			IItem itm = new EbayItemAdapter(each);
			items.add(itm);
		}
		
		return items;
	}
	
	protected String getDisplayName() {
		return DISPLAY_NAME;
	}
	
	protected String getImagePath() {
		return "/services/ebay/images/ebay_100.gif";
	}
	
	protected boolean isSandbox() {
		return false;
	}

	private GetSellerListDAO dao = new GetSellerListDAO();
}
