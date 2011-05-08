/**
 * 
 */
package com.googlecode.flickr2twitter.impl.ebay;

/**
 * @author yayu
 *
 */
public class SourceServiceProviderEbaySandbox extends SourceServiceProviderEbay {
	public static final String ID = "ebay_sandbox";
	public static final String DISPLAY_NAME = "eBay - Sandbox";
	public static final String PAGE_NAME_CONFIG = "ebay_config.jsp?sandbox=true";

	/**
	 * 
	 */
	public SourceServiceProviderEbaySandbox() {
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
		return "/services/ebay/images/ebay_100_sandbox.gif";
	}
	
	@Override
	public boolean isSandbox() {
		return true;
	}

}
