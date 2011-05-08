package com.googlecode.flickr2twitter.datastore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Mark Zang(deepnighttwo@gmail.com)
 *
 */
public class MessageDigestUtil {

	/**
	 * Get the SHA value for given password. We must make sure the String
	 * instance is using the same encoding.
	 * 
	 * @param originalPassword
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String getSHAPassword(String originalPassword)
			throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA");
		md.update(originalPassword.getBytes());
		byte[] bytes = md.digest();
		String encryption = bytes2Hex(bytes);
		return encryption;
	}

	private static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}

}
