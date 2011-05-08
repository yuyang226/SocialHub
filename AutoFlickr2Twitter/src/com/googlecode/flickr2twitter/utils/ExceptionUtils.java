/**
 * 
 */
package com.googlecode.flickr2twitter.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public final class ExceptionUtils {

	/**
	 * 
	 */
	private ExceptionUtils() {
		super();
	}

	public static String converToString(Throwable t) {
		StringWriter sw = null;
		PrintWriter pw = null;
		try {
			sw = new StringWriter();
			pw = new PrintWriter(sw);
			t.printStackTrace(pw);
			return sw.toString();
		} finally {
			if (sw != null) {
				try {
					sw.close();
				} catch (IOException e) {
					//ignore
				}
			}
			if (pw != null) {
				pw.close();
			}
		}
	}

}
