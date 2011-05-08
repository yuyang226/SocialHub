<%@ page language="java"
	import="java.net.*,java.text.*,com.googlecode.flickr2twitter.utils.*,com.googlecode.flickr2twitter.impl.facebook.*,com.googlecode.flickr2twitter.intf.*,com.googlecode.flickr2twitter.exceptions.*,com.googlecode.flickr2twitter.core.*,java.util.logging.*,com.google.gdata.client.http.AuthSubUtil,com.googlecode.flickr2twitter.datastore.*,com.googlecode.flickr2twitter.datastore.model.*,com.googlecode.flickr2twitter.servlet.*,java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<%
	String currentProviderID = TargetServiceProviderFacebook.ID;
	User user = (User) session
			.getAttribute(UserAccountServlet.PARA_SESSION_USER);
	Map<String, Object> currentData = (Map<String, Object>) session
			.getAttribute(currentProviderID);
	Logger log = Logger
			.getLogger(TargetServiceProviderFacebook.CALLBACK_URL);
	log.info(String.valueOf(request.getParameterMap()));

	String token = request
			.getParameter(TargetServiceProviderFacebook.PARA_CODE);

	out.print("The first place Code is: " + token + "\r\n");

	log.info("Facebook Access Code: " + token);
	if (currentData == null || user == null || token == null) {
		session.setAttribute("message",
				"Authorize not done. Please login first.");
		response.sendRedirect("/index.jsp");
	} else {
		currentData.put(TargetServiceProviderFacebook.PARA_CODE, token);

		try {
			String retMsg = ServiceFactory.getTargetServiceProvider(
					currentProviderID).readyAuthorization(
					user.getUserId().getEmail(), currentData);

			// log.info(retMsg);
		} catch (TokenAlreadyRegisteredException ex) {
			request.getSession().removeAttribute(currentProviderID);
			log.warning("Account \""
					+ ex.getUserName()
					+ "\" is already authorized. Token for this account is:"
					+ ex.getToken());
		} catch (Exception e) {
			log.warning(e.toString());
		}

		response.sendRedirect(IServiceAuthorizer.POST_AUTH_PAGE);
	}
%>
