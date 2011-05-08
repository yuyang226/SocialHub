<%@ page language="java"
	import="com.googlecode.flickr2twitter.intf.*,com.googlecode.flickr2twitter.impl.flickr.*,com.googlecode.flickr2twitter.exceptions.*,com.googlecode.flickr2twitter.core.*,java.util.logging.*,com.googlecode.flickr2twitter.datastore.*,com.googlecode.flickr2twitter.datastore.model.*,com.googlecode.flickr2twitter.servlet.*,java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<%
	String currentProviderID = null;
	currentProviderID = "flickr";
	Logger log = Logger.getLogger(SourceServiceProviderFlickr.CALLBACK_URL);
	User user = (User) session
			.getAttribute(UserAccountServlet.PARA_SESSION_USER);
	Map<String, Object> currentData = (Map<String, Object>) session
			.getAttribute(currentProviderID);
	String frob = request.getParameter(SourceServiceProviderFlickr.KEY_FROB);
	log.info("Flickr OAuth Frob: " + frob);
	if (frob != null) {
		currentData.put(SourceServiceProviderFlickr.KEY_FROB, frob);
	}
	if (currentData == null || user == null) {
		session.setAttribute("message",
				"Authorize not done. Please login first.");
		response.sendRedirect("/index.jsp");

	} else {
		String msg = null;
		try {
			String retMsg = ((IServiceAuthorizer)ServiceFactory.getSourceServiceProvider(
					currentProviderID)).readyAuthorization(
					user.getUserId().getEmail(), currentData);
			log.info(retMsg);
		} catch (TokenAlreadyRegisteredException ex) {
			request.getSession().removeAttribute(currentProviderID);
			msg = "Account \""
					+ ex.getUserName()
					+ "\" is already authorized. Token for this account is:"
					+ ex.getToken();
		} catch (Exception e) {
			msg = e.toString();
		}
		if (msg != null) {
			log.warning(msg);
			session.setAttribute("message", msg);
		}
		response.sendRedirect(SourceServiceProviderFlickr.POST_AUTH_PAGE);
	}
%>
