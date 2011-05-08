<%@ page import="java.util.*" %>

<HTML>
<BODY>
<%
	// This is a scriptlet.  Notice that the "date"
	// variable we declare here is available in the
	// embedded expression later on.
	System.out.println("Evaluating date now");
	Date date = new Date();
%>
Hello!  The time is now
<%
	out.println(date);
	out.println("<BR>Your machine's address is ");
	out.println(request.getRemoteHost());
%>

<TABLE BORDER=2>
<%
	for (Object object : System.getProperties().keySet()) {
		String key = String.valueOf(object);
		%>
        <TR>
        <TD><%=key%></TD>
        <TD><%=System.getProperty(key)%></TD>
        </TR>
        <%
	}
%>
</TABLE>

</BODY>
</HTML>