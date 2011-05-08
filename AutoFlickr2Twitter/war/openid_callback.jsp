<%@ page language="java" import="java.util.logging.*,com.googlecode.flickr2twitter.intf.*,com.googlecode.flickr2twitter.datastore.*,com.googlecode.flickr2twitter.datastore.model.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<%
	Logger log = Logger.getLogger(request.getRequestURI());
	String openidIdentity = request.getParameter("openid.identity");
	boolean isYahoo = openidIdentity.indexOf("yahoo") >= 0;
	String userEmail = isYahoo ? request
			.getParameter("openid.ax.value.email") : request
			.getParameter("openid.ext1.value.email");

	String userFirstName = request
			.getParameter("openid.ext1.value.firstname");
	String userLastName = request
			.getParameter("openid.ext1.value.lastname");
	String fullName = request.getParameter("openid.ax.value.fullname");
	log.info(new StringBuffer("userEmail=").append(userEmail)
			.append(", firstName=").append(userFirstName)
			.append(", lastName=").append(userLastName).toString());
	if (userEmail != null) {
		try {
			User user = MyPersistenceManagerFactory
					.getOpenIdLoginUser(userEmail);
			if (user == null) {
				//not a registered user
				log.info("New open ID user, try to automatically register->"
						+ userEmail);

				if (fullName == null || fullName.length() == 0) {
					StringBuffer data = new StringBuffer();
					if (userFirstName != null) {
						data.append(userFirstName);
					}
					if (userLastName != null) {
						if (data.length() > 0) {
							data.append(" ");
						}
						data.append(userLastName);
					}
					fullName = data.toString();
				}

				user = MyPersistenceManagerFactory.createNewUser(
						userEmail, "openid", fullName);
			}

			StringBuffer url = new StringBuffer();
			url.append("socialhub-app://");
			String openidProviderId = isYahoo ? "yahoo"
					: "google";
			url.append(openidProviderId);
			url.append("?userEmail=");
			url.append(userEmail);
			response.sendRedirect(url.toString());
		} catch (Exception e) {
			log.throwing(request.getRequestURI(),
					request.getRequestURI(), e);
		}
	}
%>
