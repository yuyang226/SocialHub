<%@ page language="java"
	import="com.googlecode.flickr2twitter.datastore.*,com.googlecode.flickr2twitter.datastore.model.*,com.googlecode.flickr2twitter.servlet.*,com.googlecode.flickr2twitter.core.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
<%
	session.removeAttribute(UserAccountServlet.PARA_SESSION_USER);
	response.sendRedirect("index.jsp");
%>