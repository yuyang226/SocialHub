package com.googlecode.flickr2twitter.servlet;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory;
import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory.Permission;
import com.googlecode.flickr2twitter.datastore.model.User;

public class UserPasswordMigration extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger
			.getLogger(UserPasswordMigration.class.getName());

	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log.warning("Starting to user migration...");

		User userSession = (User) req.getSession().getAttribute(
				UserAccountServlet.PARA_SESSION_USER);
		if (Permission.ADMIN.name().equals(userSession.getPermission()) == false) {
			req.getSession()
					.setAttribute(
							"message",
							"U must be kidding me! Only admin is allowd to do this. "
									+ "U knows too much, boy. Be a lamp and follow the rules of this system :-)");
			resp.sendRedirect("/index.jsp");
			return;
		}

		StringBuffer msg = new StringBuffer();
		List<User> allUsers = MyPersistenceManagerFactory.getAllUsers();

		for (User user : allUsers) {
			String password = user.getPassword();
			// not migrate if it seems this user had migrated.
			if (password.length() == 40) {
				String ignoreMsg = "User " + user.getScreenName() + "("
						+ user.getUserId().getEmail()
						+ ") 's password will not be migrated."
						+ " According to the length of user's password, "
						+ "it seems already been migreated.\r\n";
				msg.append(ignoreMsg);
				log.warning(ignoreMsg);
				continue;
			}
			// String passwordSHA = null;
			// try {
			// passwordSHA = MessageDigestUtil.getSHAPassword(password);
			// } catch (NoSuchAlgorithmException e) {
			// String failMsg = "Fails to generated SHA for "
			// + user.getScreenName() + "("
			// + user.getUserId().getEmail() + "). Error message:"
			// + e.getLocalizedMessage() + "\r\n";
			// log.warning(failMsg);
			// msg.append(failMsg);
			// e.printStackTrace();
			// continue;
			// }
			MyPersistenceManagerFactory.updateUserPassword(user.getKey(),
					password);
			String sucessMsg = "Password migration for user "
					+ user.getScreenName() + "(" + user.getUserId().getEmail()
					+ ") is done successfully!\r\n";
			log.warning(sucessMsg);
			msg.append(sucessMsg);
		}

		log.warning("Migration finished...");
		if (msg.length() > 0) {
			req.getSession().setAttribute("message", msg.toString());
		}
		resp.sendRedirect("/index.jsp");
	}
}
