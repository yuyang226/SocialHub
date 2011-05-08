package com.googlecode.flickr2twitter.sina.weibo4j.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.flickr2twitter.datastore.model.User;
import com.googlecode.flickr2twitter.impl.sina.TargetServiceProviderSina;
import com.googlecode.flickr2twitter.servlet.UserAccountServlet;
import com.googlecode.flickr2twitter.sina.weibo4j.Weibo;
import com.googlecode.flickr2twitter.sina.weibo4j.WeiboException;

public class SinaSignInCallBackServlet extends HttpServlet {

	private static final Logger log = Logger
			.getLogger(SinaSignInCallBackServlet.class.getName());
	private static final long serialVersionUID = -1016731113426658126L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
//		//get user information from session
		User currentUser = (User) req.getSession().getAttribute(UserAccountServlet.PARA_SESSION_USER);
		
		if( currentUser == null ) {
			resp.sendRedirect("index.jsp");
			return;
		}
		String email = currentUser.getUserId().getEmail();

		// Get the verifier;
		String verifier = req.getParameter("oauth_verifier");
		String oauthToken = req.getParameter("oauth_token");
		log.info("SINA oauth_token = " + oauthToken + ", oauth_verifier = " + verifier);

		// Request token and secret
		Map<String, Object> currentData = (Map<String, Object>) req.getSession()
		.getAttribute(TargetServiceProviderSina.ID);
		if (currentData.containsKey("token") == false || currentData.containsKey("secret") == false) {
			resp.getWriter().println("Error to get request token from cookie.");
			return;
		}
		String requestToken = String.valueOf(currentData.get("token")); //req.getParameter("t"); //token
		String requestTokenSecret = String.valueOf(currentData.get("secret")); //req.getParameter("s"); //secret

		if (verifier == null) {
			resp.getWriter().println("Error to get the verifier.");
			return;
		}

		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
		System.setProperty("weibo4j.oauth.consumerSecret",
				Weibo.CONSUMER_SECRET);

		Weibo weibo = new Weibo();
		try {
			AccessToken accessToken = weibo.getOAuthAccessToken(requestToken, requestTokenSecret,
					verifier);
			if (accessToken == null) {
				throw new ServletException("Error to get access token.");
			} else {
				//save the access token into db
				TargetServiceProviderSina tps = new TargetServiceProviderSina();
				Map<String,Object> data = new HashMap<String,Object>();
				data.put("token",accessToken.getToken());
				data.put("secret", accessToken.getTokenSecret());
				data.put("userId", accessToken.getUserId());
				try {
					tps.readyAuthorization(email, data);
					resp.sendRedirect("/authorize.jsp");
				} catch (Exception e) {
					log.log(Level.WARNING, e.getMessage());
					throw new ServletException("You've already authorize sina.");
				}
				
				return;
			}
		} catch (WeiboException e) {
			resp.getWriter().println("Error: " + e.getMessage());
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req,resp);
	}
}
