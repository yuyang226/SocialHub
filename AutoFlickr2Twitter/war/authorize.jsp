<%@ page language="java"
	import="com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory, com.googlecode.flickr2twitter.datastore.*,com.googlecode.flickr2twitter.datastore.model.*,com.googlecode.flickr2twitter.servlet.*,java.util.*,com.googlecode.flickr2twitter.core.*,com.googlecode.flickr2twitter.model.*,com.googlecode.flickr2twitter.intf.*,com.googlecode.flickr2twitter.sina.weibo4j.*,com.googlecode.flickr2twitter.sina.weibo4j.http.*"
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
	<div id="content">
		<%
			if(user != null && signedIn) {
				boolean isAdmin = MyPersistenceManagerFactory.Permission.ADMIN.name().equals(user.getPermission());
		%>
		<h1>Authorize/Configure Source and Target Accounts</h1>
		<p>
			Click icons below to allow us to access your data in different sites. You're supposed to authorize at least one source account and one target account.  
		</p>
		<hr/>
		<div id="middle">
			<h3 class="source">Source</h3>
			<br/>
			<%
				List<UserSourceServiceConfig> sourceSvcs = MyPersistenceManagerFactory
						.getUserSourceServices(user);
				List<UserTargetServiceConfig> targetSvcs = MyPersistenceManagerFactory
						.getUserTargetServices(user);
	
				String currentProviderID = null;
				Collection<ISourceServiceProvider<IItem>> sources = ServiceFactory
						.getAllSourceProviders();
				for (ISourceServiceProvider<IItem> sourceProvider : sources) {
					if (sourceProvider instanceof IAdminServiceProvider && isAdmin == false) {
						continue;
					}
					currentProviderID = sourceProvider.getId();
					GlobalSourceApplicationService sourceApp = MyPersistenceManagerFactory
							.getGlobalSourceAppService(sourceProvider.getId());
				%>
				<% if (sourceProvider instanceof IServiceAuthorizer) {%>
				<a class="source" href="/oauth?<%=OAuthServlet.PARA_OPT%>=<%=OAuthServlet.OPT_AUTH_SOURCE%>&<%=OAuthServlet.PARA_PROVIDER_ID%>=<%=currentProviderID%>"
					target="_new"><img src="<%=sourceApp.getImagePath()%>" alt="<%=sourceApp.getAppName()%>"/></a>
				<% } else if (sourceProvider instanceof IConfigurableService) {
					IConfigurableService configService = (IConfigurableService)sourceProvider;
				%>
				<a class="source" href="<%=configService.getConfigPagePath()%>"
					target="_new"><img src="<%=sourceApp.getImagePath()%>" alt="<%=sourceApp.getAppName()%>"/></a>
				<%
				   }
				}
			%>
			<p/>
			<h3 class="target">Target</h3>
			<br/>
			<%
				Collection<ITargetServiceProvider> targets = ServiceFactory
						.getAllTargetProviders();
				for (ITargetServiceProvider targetProvider : targets) {
					if (targetProvider instanceof IAdminServiceProvider && isAdmin == false) {
						continue;
					}
					currentProviderID = targetProvider.getId();
					Map<String, Object> currentData = (Map<String, Object>) session
							.getAttribute(currentProviderID);
	
					GlobalTargetApplicationService targetApp = MyPersistenceManagerFactory
							.getGlobalTargetAppService(targetProvider.getId());
				%>
				<a class="target" href="/oauth?<%=OAuthServlet.PARA_OPT%>=<%=OAuthServlet.OPT_AUTH_TARGET%>&<%=OAuthServlet.PARA_PROVIDER_ID%>=<%=currentProviderID%>"
				target="_new"><img src="<%=targetApp.getImagePath()%>" alt="<%=targetApp.getAppName()%>"/></a>
				<%
				}
			%>
		<%
		} else { //user not signed.
			response.sendRedirect("index.jsp");
		}
		%>
	</div>
	<%@ include file="footer.jsp"%>
</div>
</body>
</html>