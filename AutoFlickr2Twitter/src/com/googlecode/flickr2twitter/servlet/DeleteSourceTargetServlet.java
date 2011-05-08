package com.googlecode.flickr2twitter.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory;
import com.googlecode.flickr2twitter.datastore.model.User;

public class DeleteSourceTargetServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger
			.getLogger(DeleteSourceTargetServlet.class.getName());

	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute(
				UserAccountServlet.PARA_SESSION_USER);
		if (user == null) {
			throw new ServletException("Please sign in first.");
		}

		String accessToken = req.getParameter("at");
		String type = req.getParameter("type");

		try {
			MyPersistenceManagerFactory.deleteUserService(user, accessToken,
					Integer.parseInt(type));
		} catch (Exception e) {
			throw new ServletException(e);
		}

		resp.sendRedirect("user_admin.jsp");
	}

}
