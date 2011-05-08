package com.googlecode.flickr2twitter.servlet;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.log.Log;

import com.google.appengine.repackaged.com.google.common.base.StringUtil;
import com.googlecode.flickr2twitter.datastore.MessageDigestUtil;
import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory;
import com.googlecode.flickr2twitter.datastore.model.User;
import com.googlecode.flickr2twitter.org.apache.commons.lang3.StringUtils;

/**
 * @author Meng Zang (DeepNightTwo@gmail.com)
 * 
 */

public class UserAccountServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	// private static final Logger log =
	// Logger.getLogger(UserAccountServlet.class
	// .getName());

	public static final String OPT_ADD_USER = "Add_User";
	public static final String OPT_LOGIN = "Login";
	public static final String OPT_Change_Display_Name = "ChangeDisplayName";
	public static final String OPT_Change_Password = "ChangPassword";

	public static final String PARA_OPT = "operation";

	public static final String PARA_SESSION_USER = "user";

	public static final String PARA_KEY = "user_key";
	public static final String PARA_EMAIL = "user_email";
	public static final String PARA_PASSWORD = "user_password";
	public static final String PARA_SCREEN_NAME = "user_screenName";
	public static final String PARA_PERMISSION = "user_permission";

	public static final String PARA_PASSWORD_1 = "user_password_1";
	public static final String PARA_PASSWORD_2 = "user_password_2";

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		StringBuffer msg = new StringBuffer();

		resp.setContentType("text/plain");
		String operation = req.getParameter(PARA_OPT);

		try {
			if (OPT_ADD_USER.equalsIgnoreCase(operation)) {
				addUserAccount(req, resp, msg);
			} else if (OPT_LOGIN.equalsIgnoreCase(operation)) {
				doLogin(req, resp, msg);
			} else if (OPT_Change_Display_Name.equalsIgnoreCase(operation)) {
				updateUserDisplayName(req, resp, msg);
			} else if (OPT_Change_Password.equalsIgnoreCase(operation)) {
				updateUserPassord(req, resp, msg);
			}
		} catch (Exception ex) {
			msg.append("Exception occured:\n");
			msg.append(ex.getMessage());
		} finally {
			if (msg.length() > 0) {
				req.getSession().setAttribute("message", msg.toString());
			}
			resp.sendRedirect("/index.jsp");
		}
	}

	private void updateUserDisplayName(HttpServletRequest req,
			HttpServletResponse resp, StringBuffer msg) {
		User user = (User) req.getSession().getAttribute(
				UserAccountServlet.PARA_SESSION_USER);
		if (user == null) {
			msg.append("Please login first before change your display name.");
			return;
		}
		String screenName = req.getParameter(PARA_SCREEN_NAME);
		if (StringUtils.isBlank(screenName) == true) {
			msg.append("Display name could not be empty.");
			return;
		}
		user = MyPersistenceManagerFactory.updateUserDisplayName(user.getKey(),
				screenName);
		if (user == null) {
			msg.append("Update user display name failed due to database internal error.");
			return;
		}
		req.getSession().setAttribute(PARA_SESSION_USER, user);

		msg.append("Update display name to " + screenName);
	}

	private void updateUserPassord(HttpServletRequest req,
			HttpServletResponse resp, StringBuffer msg) {
		User user = (User) req.getSession().getAttribute(
				UserAccountServlet.PARA_SESSION_USER);

		String password = req.getParameter(PARA_PASSWORD);
		String password1 = req.getParameter(PARA_PASSWORD_1);
		String password2 = req.getParameter(PARA_PASSWORD_2);
		if (StringUtils.isBlank(password1) == true) {
			msg.append("Password could not be empty.");
			return;
		}

		try {
			if (user.getPassword().equals(
					MessageDigestUtil.getSHAPassword(password)) == false) {
				msg.append("Old password specified is incorrect.");
				return;
			}
		} catch (NoSuchAlgorithmException e) {
			msg.append("Unable to verify old password. Error message is :"
					+ e.getMessage());
			Log.warn(e);
		}

		if (StringUtils.equals(password1, password2) == false) {
			msg.append("New passwords don't match.");
			return;
		}

		user = MyPersistenceManagerFactory.updateUserPassword(user.getKey(),
				password1);
		if (user == null) {
			msg.append("Update user display name failed due to database internal error.");
			return;
		}
		req.getSession().setAttribute(PARA_SESSION_USER, user);
		msg.append("Password is changed successfully.");
	}

	private void addUserAccount(HttpServletRequest req,
			HttpServletResponse resp, StringBuffer msg) {
		String userEmail = req.getParameter(PARA_EMAIL);
		String password = req.getParameter(PARA_PASSWORD);
		String screenName = req.getParameter(PARA_SCREEN_NAME);

		if (StringUtil.isEmpty(userEmail) == true) {
			msg.append("User Email could not be empty! Creation is not successful.");
			return;
		}

		MyPersistenceManagerFactory.createNewUser(userEmail, password,
				screenName);
	}

	private void doLogin(HttpServletRequest req, HttpServletResponse resp,
			StringBuffer msg) {
		String userEmail = req.getParameter(PARA_EMAIL);
		String password = req.getParameter(PARA_PASSWORD);
		if (StringUtil.isEmpty(userEmail) == true) {
			msg.append("User Email could not be empty! Login failed.");
			return;
		}
		User user = MyPersistenceManagerFactory.getLoginUser(userEmail,
				password);
		if (user == null) {
			msg.append("User name and password not match! Login failed");
		} else {
			msg.append("Login Success!");
			req.getSession().setAttribute(PARA_SESSION_USER, user);
		}

	}

}
