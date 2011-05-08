<%@ page language="java"
	import="com.googlecode.flickr2twitter.intf.*,com.googlecode.flickr2twitter.impl.twitter.*,com.googlecode.flickr2twitter.exceptions.*,com.googlecode.flickr2twitter.core.*,java.util.logging.*,com.google.gdata.client.http.AuthSubUtil,com.googlecode.flickr2twitter.datastore.*,com.googlecode.flickr2twitter.datastore.model.*,com.googlecode.flickr2twitter.servlet.*,java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<%
	String currentProviderID = AbstractServiceProviderTwitter.ID;
	User user = (User)session.getAttribute(UserAccountServlet.PARA_SESSION_USER);
	Map<String, Object> currentData = (Map<String, Object>) session
			.getAttribute(currentProviderID);
	Logger log = Logger.getLogger(AbstractServiceProviderTwitter.CALLBACK_URL);
	log.info(String.valueOf(request.getParameterMap()));
	boolean isSource = true;
	String token = request.getParameter(AbstractServiceProviderTwitter.KEY_TOKEN);
	String oauth_verifier = request.getParameter(AbstractServiceProviderTwitter.KEY_OAUTH_VERIFIER);
	log.info("Twitter Access Token: " + token + "; OAuth Verifier: " + oauth_verifier);
	if (currentData == null || user == null || token == null) {
		session.setAttribute("message","Authorize not done. Please login first.");
		response.sendRedirect("/index.jsp");
	} else {
		currentData.put(AbstractServiceProviderTwitter.KEY_TOKEN, token);
		currentData.put(AbstractServiceProviderTwitter.KEY_OAUTH_VERIFIER, oauth_verifier);
		try {
			String retMsg = null;
			if (isSource == true) {
				retMsg = ((IServiceAuthorizer)ServiceFactory.getSourceServiceProvider(currentProviderID))
				.readyAuthorization(user.getUserId().getEmail(), currentData);
			} else {
				retMsg = ServiceFactory.getTargetServiceProvider(currentProviderID)
				.readyAuthorization(user.getUserId().getEmail(), currentData);
			}
			
			log.info(retMsg);
		} catch (TokenAlreadyRegisteredException ex) {
			request.getSession().removeAttribute(currentProviderID);
			log.warning("Account \"" + ex.getUserName()
					+ "\" is already authorized. Token for this account is:"
					+ ex.getToken());
		} catch (Exception e) {
			log.warning(e.toString());
		}
		response.sendRedirect(AbstractServiceProviderTwitter.POST_AUTH_PAGE);
	}
%>
