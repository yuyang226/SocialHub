<%@ page language="java"
	import="com.googlecode.flickr2twitter.impl.ebay.*,java.util.*"
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
		String keywords = request
				.getParameter(EbayConfigKeywordsServlet.PARA_SEARCH_KEYWORDS);
		String minPrice = request
		.getParameter(EbayConfigKeywordsServlet.PARA_SEARCH_PRICE_LOW);
		minPrice = minPrice != null ? minPrice : "";
		String maxPrice = request
		.getParameter(EbayConfigKeywordsServlet.PARA_SEARCH_PRICE_HIGH);
		maxPrice = maxPrice != null ? maxPrice : "";
		boolean enableNotifySupression = Boolean.valueOf(request
		.getParameter(EbayConfigKeywordsServlet.PARA_SEARCH_MAX_NOTIFICATION));
		boolean hasKeywords = (keywords != null) && (keywords.length() > 0);
		boolean isSandbox = Boolean.valueOf(request
				.getParameter(EbayConfigServlet.PARA_SANDBOX));
	%>
	<h1>Enter the eBay <%if (isSandbox) {%>Sandbox <% } %>keywords you want to search and follow</h1>
	<hr />
	<div id="middle">
		<form action="/ebay_config_keywords.jsp?sandbox=<%=isSandbox%>"
			method="post" name="searchebaykeywords">
		<table class="border_table">
			<tr>
				<td class="first_ebay">Keywords:</td>
				<td><input type="text"
					name="<%=EbayConfigKeywordsServlet.PARA_SEARCH_KEYWORDS%>" <% if (hasKeywords) {%> value="<%=keywords %>"<%} %>/></td>
			</tr>
			<tr>
				<td class="td.first_ebay">Price:</td>
				<td>from $ <input type="text" size="8"
					name="<%=EbayConfigKeywordsServlet.PARA_SEARCH_PRICE_LOW%>" value="<%=minPrice %>"/> to $ <input
					type="text" size="8"
					name="<%=EbayConfigKeywordsServlet.PARA_SEARCH_PRICE_HIGH%>" value="<%=maxPrice %>"/></td>
			</tr>
			<tr>
				<td/>
				<td><a href="#" onclick="searchebaykeywords.submit();"><img
					src="/images/button_search.png" alt=""/></a></td>
			</tr>
		</table>
		</form>
		<%
			if (hasKeywords) {
				FindItemsDAO findItemsDao = new FindItemsDAO();
				List<EbayItem> items = isSandbox ? findItemsDao
						.findItemsByKeywordsFromSandbox(keywords, minPrice, maxPrice, 10)
						: findItemsDao.findItemsByKeywordsFromProduction(
								keywords, minPrice, maxPrice, 10);
				// show user details if found a user
				if (items != null && items.size() >0 ) {
		%>
		
		<h1>Search Result for '<%=keywords%>'</h1>
		<form action="/ebayConfigkeywords?sandbox=<%=isSandbox%>" method="post"
			name="showebaykeywords"><input type="hidden"
			value="<%=keywords%>"
			name="<%=EbayConfigKeywordsServlet.PARA_KEYWORDS%>" />
			<input type="hidden"
			value="<%=minPrice%>"
			name="<%=EbayConfigKeywordsServlet.PARA_SEARCH_PRICE_LOW%>" />
			<input type="hidden"
			value="<%=maxPrice%>"
			name="<%=EbayConfigKeywordsServlet.PARA_SEARCH_PRICE_HIGH%>" />
		<table class="border_table">
			<tr>
				<td class="first_ebay">Suppress Notification:</td>
				<td><input type="checkbox"
					name="<%=EbayConfigKeywordsServlet.PARA_NOTIFICATION_ONCE_A_DAY%>"
					value="true"> only receive one message per day</td>
			</tr>
			<tr>
				<td class="first_ebay"/>
				<td><a href="#" onclick="showebaykeywords.submit();"><img
					src="/images/button_submit.png" alt=""/></a></td>
			</tr>
			<%
				for (EbayItem item : items) {
			%>
			<tr>
				<%
					String imgUrl = item.getGalleryURL();
					if(isSandbox) {
					    imgUrl = "/images/sample.png";
					}
					if (imgUrl != null) {
				%>
				<td><img src="<%=imgUrl%>"
					alt="<%=item.getTitle()%>" /></a></td>
				<%
					} else {
				%>
				<td />
				<%
					}
				%>
				<td><a href="<%=item.getViewItemURL()%>"><%=item.getTitle()%></a></td>
				<% if (item.getCurrentPrice() != null) { %>
					<td>$<%=item.getCurrentPrice()%></td>
				<%} else if (item.getBuyItNowPrice() != null) { %>
					<td>$<%=item.getBuyItNowPrice()%></td>
				<%} %>
			</tr>
			<%
				}
			%>
		</table>
		</form>
		<%
			} else {
					// notify user that there is no search result
		%>
		<h1>Search Result</h1>
		<form action="/ebayConfigkeywords?sandbox=<%=isSandbox%>" method="post"
			name="showebaykeywords"><input type="hidden"
			value="<%=keywords%>"
			name="<%=EbayConfigKeywordsServlet.PARA_KEYWORDS%>" />
			<input type="hidden"
			value="<%=minPrice%>"
			name="<%=EbayConfigKeywordsServlet.PARA_SEARCH_PRICE_LOW%>" />
			<input type="hidden"
			value="<%=maxPrice%>"
			name="<%=EbayConfigKeywordsServlet.PARA_SEARCH_PRICE_HIGH%>" />
		<table class="border_table">
			<tr>
				<td class="first_ebay">Suppress Notification:</td>
				<td><input type="checkbox"
					name="<%=EbayConfigKeywordsServlet.PARA_NOTIFICATION_ONCE_A_DAY%>"
					value="true"> only receive one message per day</td>
			</tr>
			<tr>
				<td class="first_ebay"/>
				<td><a href="#" onclick="showebaykeywords.submit();"><img
					src="/images/button_submit.png" alt=""/></a></td>
			</tr>
			<tr>
				<td class="first_ebay">No Items Found for Keywords:</td>
				<td><%=keywords%></td>
			</tr>
		</table>
		</form>
		<%
				}
			}
		%>
	</div>
	<%@ include file="footer.jsp"%></div>
</div>
</body>
</html>