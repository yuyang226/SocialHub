<%@ page language="java" import="java.util.logging.*,com.googlecode.flickr2twitter.intf.*,com.googlecode.flickr2twitter.datastore.*,com.googlecode.flickr2twitter.datastore.model.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<%
	Logger log = Logger.getLogger(request.getRequestURI());
	String userEmail = request.getParameter("openid.ext1.value.email");
	String userFirstName = request.getParameter("openid.ext1.value.firstname");
	String userLastName = request.getParameter("openid.ext1.value.lastname");
	log.info(new StringBuffer("userEmail=").append(userEmail).append(", firstName=")
			.append(userFirstName).append(", lastName=").append(userLastName).toString());
	if (userEmail != null) {
		try {
			User user = MyPersistenceManagerFactory.getOpenIdLoginUser(userEmail);
			if (user == null) {
				//not a registered user
				log.info("New open ID user, try to automatically register->" + userEmail);
				StringBuffer fullName = new StringBuffer();
				if (userFirstName != null) {
					fullName.append(userFirstName);
				}
				if (userLastName != null) {
					if (fullName.length() > 0) {
						fullName.append(" ");
					}
					fullName.append(userLastName);
				}
				
				user = MyPersistenceManagerFactory.createNewUser(userEmail, 
						"openid", fullName.toString());
			}
			
			StringBuffer url = new StringBuffer();
			url.append("socialhub-app://google?");
			url.append("userEmail=");
			url.append(userEmail);
			response.sendRedirect(url.toString());
		} catch (Exception e) {
			log.throwing(request.getRequestURI(), request.getRequestURI(), e);
		}
	}
	
%>
