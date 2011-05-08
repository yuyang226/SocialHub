<%@ page language="java"%>
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
			if(!signedIn) {
		%>
		<h1>Welcome to SocialHub</h1>
		<hr/>
		<br/>
		<div id="left">
			<form action="/userOperation" method="post" name="frmSign">
				<table id="index_signin" class="border_table">
					<tr>
						<td class="first">User Name:</td>
						<td><input type="text" name="<%=UserAccountServlet.PARA_EMAIL%>"></input></td>
					</tr>
					<tr>
						<td>Password:</td>
						<td><input type="password" name="<%=UserAccountServlet.PARA_PASSWORD%>"></input><input
							type="hidden" name="<%=UserAccountServlet.PARA_OPT%>" value="<%=UserAccountServlet.OPT_LOGIN%>"></input></td>
	
					</tr>
					<tr>
						<td></td>
						<td>
							<a href="#" onclick="frmSign.submit();"><img src="/images/button_signin.png" alt=""/></a>
						</td>
					</tr>
				</table>
			</form>
			<img id="or" src="/images/label_or.png" alt=""/>
		</div>
		<div id="right">
			<table class="border_table">
				<tr class="first">
					<td>Sign In by OpenID</td>
				</tr>
				<tr>
					<td>
						<a href="/openid?op=Google" ><img border="0" src="/images/openid_google.png" alt="Google"/></a>
					</td>
				</tr>
				<tr>
					<td>
						<a href="/openid?op=Yahoo" ><img border="0" src="/images/openid_yahoo.png" alt="Yahoo"/></a>
					</td>
				</tr>
			</table>
		</div>
		<%
		} else { //user already signed in.
		%>
		<h1>Welcome to SocialHub, <%=user.getScreenName()%>.</h1>
		<% if (user.getLastLoginTime() != null) { %>
		<p>Last successful login time: <%=String.valueOf(user.getLastLoginTime())%>.</p>
		<% } %>
		<hr/>
		<p>Now you can <a href="/authorize.jsp">authorize</a> your source and target account if you have not done so, or you can <a href="/user_admin.jsp">manage</a> your accounts.</p>
		<p>
			<a href="logout.jsp"><img src="/images/button_logout.png" alt=""/></a>
		</p>
		<%
		}
		%>
	</div>
	<%@ include file="footer.jsp"%>
</div>
</body>
</html>