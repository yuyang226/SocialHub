/**
 * 
 */
package com.googlecode.flickr2twitter.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.googlecode.flickr2twitter.com.aetrion.flickr.util.IOUtilities;

/**
 * @author yayu
 * 
 */
public final class GlobalDefaultConfiguration {
	public static String KEY_FLICKR_APIKEY = "apiKey";
	public static String KEY_FLICKR_SECRET = "secret";
	
	public static String KEY_TWITTER_CONSUMERID = "consumerId";
	public static String KEY_TWITTER_CONSUMERSECRET = "consumerSecret";
	public static String KEY_TWITTER_ACCESSTOKEN = "accessToken"; 
	public static String KEY_TWITTER_TOKENSECRET = "tokenSecret";
	
	public static String KEY_UPDATE_INTERVAL = "interval";
	
	private String flickrApiKey;
	private String flickrSecret;
	
	private String twitterConsumerId;
	private String twitterConsumerSecret;
	
	private long interval = 600000L; //10 mins
	
	// System initialize related property keys
	public static String KEY_ADMIN_EMAIL = "adminEmail";
	public static String KEY_ADMIN_DISPLAY_NAME = "adminDisplayName";
	public static String KEY_ADMIN_PASSWORD = "adminPassword";
	
	private Properties properties = null;
	
	private static final GlobalDefaultConfiguration INSTANCE;
	static {
		INSTANCE = new GlobalDefaultConfiguration();
	}
	
	public static GlobalDefaultConfiguration getInstance() {
		return INSTANCE;
	}
	
	/**
	 * 
	 */
	private GlobalDefaultConfiguration() {
		super();
		try {
			init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void init() throws IOException {
		InputStream in = null;
        try {
            in = getClass().getResourceAsStream("setup.properties");
            properties = new Properties();
            properties.load(in);
        } finally {
            IOUtilities.close(in);
        }
        this.flickrApiKey = properties.getProperty(KEY_FLICKR_APIKEY, null);
        this.flickrSecret = properties.getProperty(KEY_FLICKR_SECRET, null);
        
        this.twitterConsumerId = properties.getProperty(KEY_TWITTER_CONSUMERID, null);
        this.twitterConsumerSecret = properties.getProperty(KEY_TWITTER_CONSUMERSECRET, null);
        
        try {
        	this.interval = Long.parseLong(properties.getProperty(KEY_UPDATE_INTERVAL));
        } catch (Exception e) {
        	//ignore
        }
	}

	public String getFlickrApiKey() {
		return flickrApiKey;
	}

	public String getFlickrSecret() {
		return flickrSecret;
	}

	public String getTwitterConsumerId() {
		return twitterConsumerId;
	}

	public String getTwitterConsumerSecret() {
		return twitterConsumerSecret;
	}

	public Properties getProperties() {
		return properties;
	}

	public long getInterval() {
		return interval;
	}

}
