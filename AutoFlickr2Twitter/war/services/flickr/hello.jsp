<HTML>
<BODY>
Going to include hello.jsp...
<BR>
<%!Date theDate = new Date();

	Date getDate() {
		System.out.println("In getDate() method");
		return theDate;
	}%>
	Hello!  The time is now <%= getDate() %>
	<p>&nbsp;</p>
<%@ include file="auth_source_flickr.jsp"%>
</BODY>
</HTML>