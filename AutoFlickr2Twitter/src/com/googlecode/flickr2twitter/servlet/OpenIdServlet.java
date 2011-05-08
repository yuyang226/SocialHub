package com.googlecode.flickr2twitter.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.expressme.openid.Association;
import org.expressme.openid.Authentication;
import org.expressme.openid.Endpoint;
import org.expressme.openid.OpenIdException;
import org.expressme.openid.OpenIdManager;

import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory;
import com.googlecode.flickr2twitter.datastore.model.User;
import com.googlecode.flickr2twitter.org.apache.commons.lang3.StringUtils;

/**
 * Sample servlet using JOpenID.
 * 
 * @author Michael Liao (askxuefeng@gmail.com)
 */
public class OpenIdServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(OpenIdServlet.class
			.getName());
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final long ONE_HOUR = 3600000L;
	public static final long TWO_HOUR = ONE_HOUR * 2L;
	public static final String ATTR_MAC = "openid_mac";
	public static final String ATTR_ALIAS = "openid_alias";
	public static final String ATTR_OP = "op";
	public static final String ATTR_OPENID_RESPONSE_NONCE = "openid.response_nonce";
	public static final String ID_GOOGLE = "Google";
	public static final String ID_YAHOO = "Yahoo";

    private OpenIdManager manager;

    @Override
    public void init() throws ServletException {
        super.init();
        manager = new OpenIdManager();
    }

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		String baseurl = request.getRequestURL().toString();
		log.fine("Base URL: " + baseurl);
		manager.setRealm(StringUtils.substringBeforeLast(baseurl, "/"));
        manager.setReturnTo(baseurl);
		String op = request.getParameter(ATTR_OP);
		if (op == null) {
			log.info("check sign on result from Google or Yahoo");
			checkNonce(request.getParameter(ATTR_OPENID_RESPONSE_NONCE));
			// get authentication:
			byte[] mac_key = (byte[]) request.getSession().getAttribute(ATTR_MAC);
			String alias = (String) request.getSession().getAttribute(ATTR_ALIAS);
			Authentication authentication = manager.getAuthentication(request, mac_key, alias);
			response.setContentType("text/html; charset=UTF-8");
			String userEmail = authentication.getEmail();
			User user = MyPersistenceManagerFactory.getOpenIdLoginUser(userEmail);
			if (user == null) {
				//not a registered user
				log.info("New open ID user, try to automatically register->" + userEmail);
				user = MyPersistenceManagerFactory.createNewUser(userEmail, "openid", authentication.getFullname());
			}
			request.getSession().setAttribute(UserAccountServlet.PARA_SESSION_USER, user);
			response.sendRedirect("/index.jsp");
			return;
		}
		if (op.equals(ID_GOOGLE) || op.equals(ID_YAHOO)) {
			log.info("redirect to " + op + " sign on page");
			Endpoint endpoint = manager.lookupEndpoint(op);
			Association association = manager.lookupAssociation(endpoint);
			request.getSession().setAttribute(ATTR_MAC, association.getRawMacKey());
			request.getSession().setAttribute(ATTR_ALIAS, endpoint.getAlias());
			String url = manager.getAuthenticationUrl(endpoint, association);
			response.sendRedirect(url);
		}
		else {
			throw new ServletException("Unsupported OP: " + op);
		}
	}

    void checkNonce(String nonce) {
        // check response_nonce to prevent replay-attack:
        if (nonce==null || nonce.length()<20)
            throw new OpenIdException("Verify failed.");
        // make sure the time of server is correct:
        long nonceTime = getNonceTime(nonce);
        long diff = Math.abs(System.currentTimeMillis() - nonceTime);
        if (diff > ONE_HOUR)
            throw new OpenIdException("Bad nonce time.");
    }

    long getNonceTime(String nonce) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .parse(nonce.substring(0, 19) + "+0000")
                    .getTime();
        }
        catch(ParseException e) {
            throw new OpenIdException("Bad nonce time.");
        }
    }
}
