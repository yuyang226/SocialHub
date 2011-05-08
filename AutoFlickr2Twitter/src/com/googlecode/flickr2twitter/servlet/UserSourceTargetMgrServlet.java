package com.googlecode.flickr2twitter.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory;
import com.googlecode.flickr2twitter.datastore.model.User;

/**
 * Represents the servlet to manage the source and target service of a user.
 * @author Charles
 *
 */
public class UserSourceTargetMgrServlet extends HttpServlet {

	/**
	 * sid
	 */
	private static final long serialVersionUID = 6634584892950950227L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute(
				UserAccountServlet.PARA_SESSION_USER);
		if( user == null ) {
			throw new ServletException("Please sign in first.");
		}
		
		String accessToken = req.getParameter("at");
		String type = req.getParameter("type");
		
		try {
			MyPersistenceManagerFactory.enableDisableUserService(user, accessToken, Integer.parseInt(type));
		} catch (Exception e) {
			throw new ServletException(e);
		}
		
		resp.sendRedirect("user_admin.jsp");
	}
	
	

}
