package com.googlecode.flickr2twitter.impl.flickr;

import com.googlecode.flickr2twitter.model.IPhoto;

/**
 * http://www.flickr.com/groups/api/discuss/72157616713786392/
 * http://dl.dropbox.com/u/1844215/FlickrBaseEncoder.java
 *
 */
public class FlickrBaseEncoder {
	protected static String alphabetString = "123456789abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ";
	protected static char[] alphabet = alphabetString.toCharArray();
	protected static int base_count = alphabet.length;
	public static final String FLICKR_SHORT_URL_PREFIX = "http://flic.kr/p/";

	public static String getShortUrl(IPhoto photo) {
		String url = photo.getUrl();
		String id = photo.getId();
		if (id == null || id.length() == 0) {
			int index = url.lastIndexOf("/");
			if (index > 0) {
				id = url.substring(index + 1, url.length()).trim();
			} else {
				return url;
			}
		}
		String suffix = encode(Long.parseLong(id));
		url = FLICKR_SHORT_URL_PREFIX + suffix;
		return url;
	}

	public static String encode(long num){
		String result = "";
		long div;
		int mod = 0;

		while (num >= base_count) {
			div = num/base_count;
			mod = (int)(num-(base_count*(long)div));
			result = alphabet[mod] + result;
			num = (long)div;
		}
		if (num>0){
			result = alphabet[(int)num] + result;
		}
		return result;
	}

	public static long decode(String link){
		long result= 0;
		long multi = 1;
		while (link.length() > 0) {
			String digit = link.substring(link.length()-1);
			result = result + multi * alphabetString.lastIndexOf(digit);
			multi = multi * base_count;
			link = link.substring(0, link.length()-1);
		}
		return result;
	}
}
