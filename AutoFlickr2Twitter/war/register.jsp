<%@ page language="java" import="com.googlecode.flickr2twitter.datastore.*,
                                                                 com.googlecode.flickr2twitter.datastore.model.*,
                                                                 com.googlecode.flickr2twitter.servlet.*"
        contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="stylesheets/site.css" rel="stylesheet" type="text/css" />
<link href="stylesheets/style.css" rel="stylesheet" type="text/css" />
<link href="stylesheets/content.css" rel="stylesheet" type="text/css" />
<title>SocialHub - Register New User</title>
</head>

<body>
<%request.setAttribute("checkLogin", false); %>

<div id="container">
	<%@ include file="header.jsp"%>
	<div id="content">
		<%
			if(signedIn) {
		%>
		<h1>Create a User Account</h1>
		<hr/>
		<div id="left">
			<form action="/userOperation" method="post" name="frmRegister">
				<table class="border_table">
				        <tr>
				                <td class="first">User Name:</td>
				                <td><input type="text" name="<%=UserAccountServlet.PARA_EMAIL%>"></input></td>
				        </tr>
				        <tr>
				                <td>Display Name:</td>
				                <td><input type="text" name="<%=UserAccountServlet.PARA_SCREEN_NAME%>"></input></td>
				        </tr>
				        <tr>
				                <td>Password:</td>
				                <td><input type="password" name="<%=UserAccountServlet.PARA_PASSWORD%>"></input>
				                <input type="hidden" name="<%=UserAccountServlet.PARA_OPT%>" value="<%=UserAccountServlet.OPT_ADD_USER%>"></input></td>
				        </tr>
				        <tr>
				        		<td/>
				                <td><a href="#" onclick="frmRegister.submit();"><img src="/images/button_register.png" alt=""/></a></td>
				        </tr>
				</table>
			</form>
		</div>
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
