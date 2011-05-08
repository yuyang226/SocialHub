package com.googlecode.flickr2twitter.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.httpclient.HttpException;
import org.json.JSONObject;

import com.googlecode.flickr2twitter.core.GlobalDefaultConfiguration;
import com.googlecode.flickr2twitter.impl.facebook.TargetServiceProviderFacebook;
import com.googlecode.flickr2twitter.org.apache.commons.lang3.StringUtils;

public class FacebookUtil {

	private static final Logger log = Logger.getLogger(FacebookUtil.class
			.getName());

	public static final String APP_ID;

	public static final String APP_SECRET_KEY;

	public static final String APP_SECRET;

	public static final String REDIRECT_URI_HOST;

	static {
		Properties appProperties = GlobalDefaultConfiguration.getInstance()
				.getProperties();

		APP_ID = appProperties.getProperty("APP_ID");

		APP_SECRET_KEY = appProperties.getProperty("APP_SECRET_KEY");

		APP_SECRET = appProperties.getProperty("APP_SECRET");

		REDIRECT_URI_HOST = appProperties.getProperty("REDIRECT_URI_HOST");

	}

	public static final String TOKEN_PARAM = "access_token";

	public static final String AUTH_URL = "https://www.facebook.com/dialog/oauth?client_id="
			+ APP_ID
			+ "&redirect_uri={0}&&scope=status_update,publish_stream,offline_access";

	public static final String TOKEN_URL = "https://graph.facebook.com/oauth/access_token?"
			+ "client_id="
			+ APP_ID
			+ "&redirect_uri={0}&"
			+ "client_secret="
			+ APP_SECRET + "&" + "code={1}";

	public static final String POST_STATUS_URL = "https://api.facebook.com/method/status.set?"
			+ "status={0}&" + TOKEN_PARAM + "={1}&format=json";

	public static final String USER_DETAIL_URL = "https://graph.facebook.com/me?access_token={0}";

	public static String gaePostMessage(String message, String token)
			throws HttpException, IOException {
		// https://api.facebook.com/method/status.set?status=asdfasdfasdfasdfasdfasdfasdfasdf&access_token=199812620030608|2920b2600e1a0ac4f29428f4-100001872430428|tMMmzsAv_4noicwf6nQakNULrCQ&format=json
		log.info("Trying to update user status using token: \"" + token
				+ "\". Message is" + message);
		StringBuffer sb = new StringBuffer();
		message = URLEncoder.encode(message, "UTF-8");
		try {
			String fullURL = MessageFormat.format(POST_STATUS_URL, message,
					token);

			log.info("Post message url is: " + fullURL);

			URL url = new URL(fullURL);
			log.info("URL Created");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					url.openStream()));
			log.info("Stream opened");
			String line;

			while ((line = reader.readLine()) != null) {
				log.info("Read line...");
				sb.append(line + "\r\n");
			}
			reader.close();
			log.info("Facebook post message success: " + message);
			String ret = sb.toString();
			log.info(ret);
			return ret;
		} catch (Exception e) {
			log.warning("Exception found during post facebook status: "
					+ e.toString());
		}

		return null;
	}

	public static String gaeGetToken(String code) {

		StringBuffer sb = new StringBuffer();

		try {
			log.info("Trying to generate user token using code " + code);

			String fullURL = MessageFormat.format(TOKEN_URL, REDIRECT_URI_HOST
					+ TargetServiceProviderFacebook.CALLBACK_URL, code);

			log.info("Token Generation url: " + fullURL);

			URL url = new URL(fullURL);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String line;

			while ((line = reader.readLine()) != null) {
				sb.append(line + "\r\n");
			}
			reader.close();

		} catch (Exception e) {
			log.warning("Got exception while retriving token from fb:"
					+ e.toString());
		}

		String tokenString = StringUtils.trim(sb.toString());
		log.info("Token String from facebook: " + tokenString);
		String[] parameters = tokenString.split("&");
		for (String paraPair : parameters) {
			int index = paraPair.indexOf("=");
			String key = paraPair.substring(0, index);
			log.info("A key from token string: " + key);
			if (TOKEN_PARAM.equals(key) == false) {
				continue;
			}
			String value = "";
			if (paraPair.length() > index) {
				value = paraPair.substring(index + 1);
			}
			log.info("Token Value is: " + value);
			return StringUtils.trim(value.trim());
		}

		return null;
	}

	/**
	 * get user details from facebook
	 * 
	 * @param token
	 * @return a string array: user id, user name
	 */
	public static String[] gaeGetUserDetails(String token) {

		StringBuffer sb = new StringBuffer();

		try {
			log.info("Trying to get user name using token " + token);

			String fullURL = MessageFormat.format(USER_DETAIL_URL, token);

			log.info("User Detail url: " + fullURL);

			URL url = new URL(fullURL);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String line;

			while ((line = reader.readLine()) != null) {
				sb.append(line + "\r\n");
			}
			reader.close();

			String userDetailStr = sb.toString();
			log.info("User Details from facebook:\r\n " + userDetailStr);

			JSONObject jsonDetails = new JSONObject(userDetailStr);
			String id = jsonDetails.getString("id");
			String name = jsonDetails.getString("name");
			return new String[] { id, name };
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new String[] { "FailedToRetrieveUserID", "DefaultFBUsername" };
	}

}
