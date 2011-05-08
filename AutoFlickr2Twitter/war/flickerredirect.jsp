<%@ page language="java"
	import="java.util.logging.*,java.net.*,com.googlecode.flickr2twitter.datastore.*,com.googlecode.flickr2twitter.datastore.model.*,com.googlecode.flickr2twitter.servlet.*,java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>Redirecting Flickr OAuth Request</title>
<script type="text/javascript">

  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-19322812-2']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();

</script>
</head>

<%
	String extra = request.getParameter("extra");
	String frob = request.getParameter("frob");
	Logger log = Logger.getLogger("flickerredirect.jsp");
	log.info("Flickr Redirect Extra=" + extra + ", frob=" + frob);
	if (frob != null && extra != null) {
		String baseUrl = extra;
		if (baseUrl.contains("%")) {
			String encoding = System.getProperty("file.encoding");
			baseUrl = URLDecoder.decode(extra, encoding);
		}
		
		String url = baseUrl + "?frob=" + frob;
		log.info("Redirecting to->" + url);
		response.sendRedirect(url);
	}
%>
