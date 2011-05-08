/**
 * 
 */
package com.googlecode.flickr2twitter.impl.ebay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import twitter4j.internal.logging.Logger;

import com.googlecode.flickr2twitter.datastore.model.GlobalServiceConfiguration;
import com.googlecode.flickr2twitter.datastore.model.GlobalSourceApplicationService;
import com.googlecode.flickr2twitter.datastore.model.UserSourceServiceConfig;
import com.googlecode.flickr2twitter.impl.flickr.SourceServiceProviderFlickr;
import com.googlecode.flickr2twitter.model.IItem;

/**
 * @author yayu
 *
 */
public class SourceServiceProviderEbayKeywords extends
		SourceServiceProviderEbay {
	public static final String ID = "ebay_keywords";
	public static final String DISPLAY_NAME = "eBay Keywords";
	public static final String PAGE_NAME_CONFIG = "ebay_config_keywords.jsp";
	public static final String KEY_MIN_PRICE = "minPrice";
	public static final String KEY_MAX_PRICE = "maxPrice";
	public static final String KEY_MAX_NOTIFICATION = "maxNotification";
	private static final Logger log = Logger.getLogger(SourceServiceProviderEbayKeywords.class);
	
	private FindItemsDAO dao = new FindItemsDAO();

	/**
	 * 
	 */
	public SourceServiceProviderEbayKeywords() {
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
	public List<IItem> getLatestItems(GlobalServiceConfiguration globalConfig,
			GlobalSourceApplicationService globalSvcConfig,
			UserSourceServiceConfig sourceService, long currentTime)
			throws Exception {
		
		String keywords = sourceService.getServiceUserId();
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
		log.info("Fetching latest listing for eBay keywords " + env + "->" + keywords 
				+ " from " + pastTime + " to " + now.getTime());
		String minPrice = sourceService.getAdditionalParameters().get(KEY_MIN_PRICE);
		String maxPrice = sourceService.getAdditionalParameters().get(KEY_MAX_PRICE);
		List<EbayItem> ebayItems = isSandbox() ? dao.findItemsByKeywordsFromSandbox(keywords, 
				minPrice, maxPrice, 1) 
				: dao.findItemsByKeywordsFromProduction(keywords, minPrice, maxPrice, 1);
		
		log.info("found " + ebayItems.size() + " items being listed recently");
		List<IItem> items = new ArrayList<IItem>(1);
		if (ebayItems.isEmpty() == false) {
			EbayItem ebayItem = ebayItems.get(0);
			log.info("The most recent listed ebay item->" + ebayItem);
			if (ebayItem.getStartTime().after(pastTime)) {
				items.add(new EbayKeywordsItem(sourceService.getUserSiteUrl(), keywords, 
					"Found new listing for keywords: " + keywords, null));
			}
		}
		
		return items;
	}

	List<IItem> convert(List<EbayItem> ebayItems) {
		List<IItem> items = new ArrayList<IItem>();
		for(EbayItem each :ebayItems) {
			IItem itm = new EbayItemAdapter(each);
			items.add(itm);
		}
		
		return items;
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
	
	protected String getDisplayName() {
		return DISPLAY_NAME;
	}
	
	protected String getImagePath() {
		return "/services/ebay/images/ebay_keywords_100.gif";
	}
	
	protected boolean isSandbox() {
		return false;
	}

}
