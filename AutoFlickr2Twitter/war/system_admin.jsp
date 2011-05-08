<%@ page language="java"
	import="com.googlecode.flickr2twitter.datastore.*,
	com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory.Permission,
	com.googlecode.flickr2twitter.datastore.model.*,
	com.googlecode.flickr2twitter.servlet.*,
	java.util.*,
	com.googlecode.flickr2twitter.core.*,
	com.googlecode.flickr2twitter.model.*,
	com.googlecode.flickr2twitter.intf.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<link href="stylesheets/site.css" rel="stylesheet" type="text/css" />
<link href="stylesheets/style.css" rel="stylesheet" type="text/css" />
<link href="stylesheets/content.css" rel="stylesheet" type="text/css" />
<title>Twitter the World - Authorize your Source and Target</title>
</head>
<body>
<%
	request.setAttribute("checkLogin", true);
%>
<%@ include file="/header.jsp"%>
<%
	if (Permission.ADMIN.equals(user.getPermission()) == false) {
		// not admin, return;
		return;
	}
%>
<%@ include file="/footer.jsp"%>
</body>
</html>