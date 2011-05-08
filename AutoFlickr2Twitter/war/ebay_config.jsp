<%@ page language="java"
	import="com.googlecode.flickr2twitter.impl.ebay.*"
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
				String ebayUserID = request
						.getParameter(EbayConfigServlet.PARA_SEARCH_SELLER_ID);
				boolean hasUserID = (ebayUserID != null) && (ebayUserID.length() > 0);
				boolean isSandbox = Boolean.valueOf(request.getParameter(EbayConfigServlet.PARA_SANDBOX));
			%>
		<h1>Set the eBay <%if (isSandbox) {%>Sandbox <% } %>Seller's ID you want to follow</h1>
		<hr/>
		<div id="middle">
			<form action="/ebay_config.jsp?sandbox=<%= isSandbox %>" method="post" name="searchebayuser">
				<table class="border_table">
					<tr>
						<td class="first_ebay"">Seller ID:</td>
						<td><input type="text"
							name="<%=EbayConfigServlet.PARA_SEARCH_SELLER_ID%>" <% if (hasUserID) {%> value="<%=ebayUserID %>"<%} %>/></td>
					</tr>
					<tr>
						<td/>
						<td><a href="#" onclick="searchebayuser.submit();"><img src="/images/button_search.png" alt=""/></a></td>
					</tr>
				</table>
			</form>
			<%
				if (hasUserID) {
					GetUserProfileDAO getUserProfileDAO = new GetUserProfileDAO();
					EbayUser ebayUser = getUserProfileDAO.getUserProfile(isSandbox,
							ebayUserID);
					// show user details if found a user
					if (ebayUser != null) {
			%>
			<h1>Seller Profile</h1>
			<form action="/ebayConfig?sandbox=<%= isSandbox %>" method="post" name="showebayuserprofile">
				<input type="hidden" value="<%=ebayUserID%>" name="<%=EbayConfigServlet.PARA_SELLER_ID%>"/>
				<table class="border_table">
					<tr>
						<td class="first_ebay">Seller ID:</td>
						<td><%=ebayUserID%></td>
					</tr>
					<% if (ebayUser.getStoreName() != null) { %>
					<tr>
						<td>Store Name:</td>
						<td><%=ebayUser.getStoreName()%></td>
					</tr>
					<tr>
						<td>Store URL:</td>
						<td><a href="<%=ebayUser.getStoreURL() %>"><%=ebayUser.getStoreURL() %></a></td>
					</tr>
					<% } %>
					<tr>
						<td>Selling Items:</td>
						<td><a href="<%=ebayUser.getSellerItemsURL()%>"><%=ebayUser.getSellerItemsURL()%></a></td>
					</tr>
					<tr>
						<td>My World:</td>
						<td><a href="<%=ebayUser.getMyWorldURL()%>"><%=ebayUser.getMyWorldURL()%></a></td>
					</tr>
					<tr>
						<td/>
						<td><a href="#" onclick="showebayuserprofile.submit();"><img src="/images/button_submit.png" alt=""/></a></td>
					</tr>
				</table>
			</form>
			<%
					} else {
					// notify user that there is no search result
			%>
			<h1>Seller Profile</h1>
			<table class="border_table">
				<tr>
					<td class="first_ebay">No Seller Found for ID:</td>
					<td><%=ebayUserID %></td>
				</tr>
			</table>
			<%
					}
				}
			%>
		</div>
	</div>
	<%@ include file="footer.jsp"%>
</div>
</body>
</html>