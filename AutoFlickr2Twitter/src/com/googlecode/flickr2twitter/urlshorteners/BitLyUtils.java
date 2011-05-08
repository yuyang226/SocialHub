/**
 * 
 */
package com.googlecode.flickr2twitter.urlshorteners;

import java.net.URL;

import com.rosaloves.bitlyj.Bitly;
import com.rosaloves.bitlyj.Bitly.Provider;


/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public final class BitLyUtils {
	public static final String BITLY_USER = "yuyang226";
	
	public static final String BITLY_APIKEY = "R_027cc1c8fa96596bf8001b1e4cf8357e";
	
	private static Provider bitly;

	/**
	 * 
	 */
	private BitLyUtils() {
		super();
	}
	
	private static Provider getBitLyProvider() {
		if (bitly == null) {
			bitly = Bitly.as(BITLY_USER, BITLY_APIKEY);
		}
		return bitly;
	}
	
	public static String shortenUrl(String longUrl) {
		return getBitLyProvider().call(Bitly.shorten(longUrl)).getShortUrl();
	}
	
	public static String shortenUrl(URL longUrl) {
		return getBitLyProvider().call(Bitly.shorten(longUrl.toExternalForm())).getShortUrl();
	}

}
