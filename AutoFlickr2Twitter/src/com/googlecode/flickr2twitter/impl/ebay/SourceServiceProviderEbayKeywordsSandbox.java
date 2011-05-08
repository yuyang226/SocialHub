/**
 * 
 */
package com.googlecode.flickr2twitter.impl.ebay;

import twitter4j.internal.logging.Logger;

/**
 * @author yayu
 *
 */
public class SourceServiceProviderEbayKeywordsSandbox extends
		SourceServiceProviderEbayKeywords {
	public static final String ID = "ebay_keywords_sandbox";
	public static final String DISPLAY_NAME = "eBay Keywords - Sandbox";
	public static final String PAGE_NAME_CONFIG = "ebay_config_keywords.jsp?sandbox=true";
	
	/**
	 * 
	 */
	public SourceServiceProviderEbayKeywordsSandbox() {
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
	protected String getDisplayName() {
		return DISPLAY_NAME;
	}
	
	protected String getImagePath() {
		return "/services/ebay/images/ebay_keywords_100_sandbox.gif";
	}
	
	@Override
	public boolean isSandbox() {
		return true;
	}

}
