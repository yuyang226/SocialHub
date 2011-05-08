/**
 * 
 */
package com.googlecode.flickr2twitter.impl.ebay;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import com.googlecode.flickr2twitter.org.apache.commons.lang3.StringUtils;

/**
 * @author hochen
 * 
 */
public class URLHelper {
	
	public static URL buildUrl(
			boolean isSSL,
			String host, 
			int port, 
			String path,
			Map<String, String> parameters) throws MalformedURLException {
		// see: AuthUtilities.getSignature()
		// AuthUtilities.addAuthToken(parameters);

		StringBuilder buffer = new StringBuilder();
		
		if (isSSL) {
			buffer.append("https://");
		} else {
			buffer.append("http://");
		}

		buffer.append(host);
		
		if (port > 0) {
			buffer.append(":");
			buffer.append(port);
		}
		
		if (path == null) {
			path = "/";
		}
		buffer.append(path);

		if (parameters == null || parameters.isEmpty()) {
			return new URL(buffer.toString());
		}
		
		boolean first = true;
		for (Map.Entry<String, String> entry : parameters.entrySet()) {
			if (entry != null && entry.getKey() != null) {
				if (first) {
					buffer.append("?");
					first = false;
				}
				buffer.append(entry.getKey());
				buffer.append("=");
				buffer.append(entry.getValue());
				buffer.append("&");
				
			}
		}
		
		String urlPath = buffer.toString();
		if (urlPath.endsWith("&")) {
			urlPath = StringUtils.substringBeforeLast(urlPath, "&");
		}

		return new URL(urlPath);
	}
}
