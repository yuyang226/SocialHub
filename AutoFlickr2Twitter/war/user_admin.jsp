<%@ page language="java"
	import="com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory.Permission,com.googlecode.flickr2twitter.datastore.*,com.googlecode.flickr2twitter.datastore.model.*,com.googlecode.flickr2twitter.servlet.*,java.util.*,com.googlecode.flickr2twitter.core.*,com.googlecode.flickr2twitter.model.*,com.googlecode.flickr2twitter.intf.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="stylesheets/site.css" rel="stylesheet" type="text/css" />
<link href="stylesheets/style.css" rel="stylesheet" type="text/css" />
<link href="stylesheets/content.css" rel="stylesheet" type="text/css" />
<title>SocialHub</title>
</head>

<body>
<div id="container">
	<%@ include file="header.jsp"%>
	
	<%
		if(signedIn == false || user == null) {
			response.sendRedirect("index.jsp");
			return;
		}
		
		// 20110316#bishen: force to reload source/target configs by user email 
		List<UserSourceServiceConfig> sourceSvcs = MyPersistenceManagerFactory
				.getUserSourceServices(user.getUserId().getEmail());
		List<UserTargetServiceConfig> targetSvcs = MyPersistenceManagerFactory
				.getUserTargetServices(user.getUserId().getEmail());
	%>
	<div id="content">
		<h1>Manage Accounts</h1>
		<p>
			With all authorized accounts listed here, you can either disable/enable them or delete the accounts permanently.
		</p>
		<hr/>
		<div id="middle">
			<h3 class="source">Source</h3>
			<br/>
			<table class="no_border_table">
			<%
				boolean odd = true;
				if ( sourceSvcs.size() == 0 ) {
			%>
				<tr>
					<td class="first" bgcolor="#DAFF7F"/>
					<td bgcolor="#DAFF7F"/>
					<td bgcolor="#DAFF7F"/>
				</tr>
			<%
				odd = false;
				}
				for( UserSourceServiceConfig src : sourceSvcs ) {
			%>
				<tr>
					<td class="first" <%if(odd) {%>bgcolor="#DAFF7F"<%}else{%>bgcolor="#FFE97F"<%}%>>
						<a target="_new" href="<%=src.getUserSiteUrl()%>"><%=src.getServiceUserName()%>@<%=src.getServiceProviderId().toUpperCase()%></a>
					</td>
					<td <%if(odd) {%>bgcolor="#DAFF7F"<%}else{%>bgcolor="#FFE97F"<%}%>>
						<a href="srctgtmgr?at=<%=src.getServiceAccessToken()%>&type=0"><%=src.isEnabled()?"Disable":"Enable"%></a>
					</td>
					<td <%if(odd) {%>bgcolor="#DAFF7F"<%}else{%>bgcolor="#FFE97F"<%}%>>
						<a href="delete?at=<%=src.getServiceAccessToken()%>&type=0">Delete</a>
					</td>
				</tr>
			<%
				odd = !odd;
				}
			%>
			</table>
			<p/>
			<h3 class="target">Target</h3>
			<br/>
			<table class="no_border_table">
			<%
				odd = true;
				if ( targetSvcs.size() == 0 ) {
			%>
				<tr>
					<td class="first" bgcolor="#DAFF7F"/>
					<td bgcolor="#DAFF7F"/>
					<td bgcolor="#DAFF7F"/>
				</tr>
			<%
				odd = false;
				}
				for( UserTargetServiceConfig tgt : targetSvcs ) {
			%>
				<tr>
					<td class="first" <%if(odd) {%>bgcolor="#DAFF7F"<%}else{%>bgcolor="#FFE97F"<%}%>>
						<a target="_new" href="<%=tgt.getUserSiteUrl()%>"><%=tgt.getServiceUserName()%>@<%=tgt.getServiceProviderId().toUpperCase()%></a>
					</td>
					<td <%if(odd) {%>bgcolor="#DAFF7F"<%}else{%>bgcolor="#FFE97F"<%}%>>
						<a href="srctgtmgr?at=<%=tgt.getServiceAccessToken()%>&type=1"><%=tgt.isEnabled()?"Disable":"Enable"%></a>
					</td>
					<td <%if(odd) {%>bgcolor="#DAFF7F"<%}else{%>bgcolor="#FFE97F"<%}%>>
						<a href="delete?at=<%=tgt.getServiceAccessToken()%>&type=1">Delete</a>
					</td>
				</tr>
			<%
				odd = !odd;
				}
			%>
			</table>
		</div>
	</div>
	<%@ include file="footer.jsp"%>
</div>
</body>
</html>