<%@ page language="java"
	import="com.googlecode.flickr2twitter.intf.*,com.googlecode.flickr2twitter.impl.youtube.*,com.googlecode.flickr2twitter.exceptions.*,com.googlecode.flickr2twitter.core.*,java.util.logging.*,com.google.gdata.client.http.AuthSubUtil,com.googlecode.flickr2twitter.datastore.*,com.googlecode.flickr2twitter.datastore.model.*,com.googlecode.flickr2twitter.servlet.*,java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<%
	String currentProviderID = SourceServiceProviderYoutube.ID;
	User user = (User)session.getAttribute(UserAccountServlet.PARA_SESSION_USER);
	Map<String, Object> currentData = (Map<String, Object>) session
			.getAttribute(currentProviderID);
	
	String token = request.getParameter(SourceServiceProviderYoutube.KEY_TOKEN);
	
	Logger log = Logger.getLogger(SourceServiceProviderYoutube.CALLBACK_URL);
	log.info("Youtube returned Params: " + request.getParameterMap());
	log.info("Youtube Access Token: " + token);
	if (currentData == null || user == null || token == null) {
		session.setAttribute("message","Authorize not done. Please login first.");
		response.sendRedirect("/index.jsp");
	} else {
		
		String sessionToken = 
			AuthSubUtil.exchangeForSessionToken(token, null);
		currentData.put(SourceServiceProviderYoutube.KEY_TOKEN, sessionToken);
		try {
			String retMsg = ((IServiceAuthorizer)ServiceFactory.getSourceServiceProvider(currentProviderID))
						.readyAuthorization(user.getUserId().getEmail(), currentData);
			

			log.info(retMsg);
		} catch (TokenAlreadyRegisteredException ex) {
			request.getSession().removeAttribute(currentProviderID);
			log.warning("Account \"" + ex.getUserName()
					+ "\" is already authorized. Token for this account is:"
					+ ex.getToken());
		} catch (Exception e) {
			log.warning(e.toString());
		}
		response.sendRedirect(SourceServiceProviderYoutube.POST_AUTH_PAGE);
	}
%>
