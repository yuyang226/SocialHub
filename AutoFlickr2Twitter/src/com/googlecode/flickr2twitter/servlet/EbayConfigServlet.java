/*
 * Created on Feb 19, 2011
 */

package com.googlecode.flickr2twitter.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.xml.sax.SAXException;

import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory;
import com.googlecode.flickr2twitter.datastore.model.User;
import com.googlecode.flickr2twitter.datastore.model.UserSourceServiceConfig;
import com.googlecode.flickr2twitter.impl.ebay.EbayUser;
import com.googlecode.flickr2twitter.impl.ebay.GetUserProfileDAO;
import com.googlecode.flickr2twitter.impl.ebay.SourceServiceProviderEbay;
import com.googlecode.flickr2twitter.impl.ebay.SourceServiceProviderEbaySandbox;

/**
 * @author Emac Shen (shen.bin.1983@gmail.com)
 */
public class EbayConfigServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public static final String PARA_SELLER_ID = "seller_id";
	public static final String PARA_SANDBOX = "sandbox";

	public static final String PARA_SEARCH_SELLER_ID = "search_seller_id";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute(
				UserAccountServlet.PARA_SESSION_USER);

		if (user == null) {
			req.getSession().setAttribute("message", "Please Login first!");
			resp.sendRedirect("/index.jsp");
			return;
		}

		String userEmail = user.getUserId().getEmail();
		String sellerId = req.getParameter(PARA_SELLER_ID);
		boolean isSandbox = Boolean.valueOf(req.getParameter(EbayConfigServlet.PARA_SANDBOX));
		
		registerNewSellerSourceServiceConfig(userEmail, sellerId, isSandbox);

		resp.sendRedirect("/user_admin.jsp");
	}

    /**
     * @param userEmail
     * @param sellerId
     * @param isSandbox
     * @throws IOException
     * @throws ServletException
     */
    public void registerNewSellerSourceServiceConfig(String userEmail, String sellerId, boolean isSandbox)
            throws IOException, ServletException
    {
        GetUserProfileDAO getUserProfileDAO = new GetUserProfileDAO();

		EbayUser ebayUser = null;
		try {
			ebayUser = getUserProfileDAO.getUserProfile(isSandbox, sellerId);
		} catch (SAXException e) {
			throw new ServletException("Unable to found user profile for id: "
					+ sellerId, e);
		}

		UserSourceServiceConfig serviceConfig = new UserSourceServiceConfig();
		serviceConfig.setServiceUserId(sellerId);
		serviceConfig.setServiceAccessToken(sellerId);

		String userDisplayName = sellerId;
		String storeName = ebayUser.getStoreName();
		if ((storeName != null) && (storeName.length() > 0)) {
			userDisplayName += " (" + storeName + ")";
		}
		serviceConfig.setServiceUserName(userDisplayName);
		serviceConfig.setServiceProviderId(isSandbox ? SourceServiceProviderEbaySandbox.ID 
				: SourceServiceProviderEbay.ID);
		serviceConfig.setUserEmail(userEmail);

		if (ebayUser.getStoreURL() != null) {
			serviceConfig.setUserSiteUrl(ebayUser.getStoreURL());
		} else {
			serviceConfig.setUserSiteUrl(ebayUser.getMyWorldURL());
		}
		MyPersistenceManagerFactory.addSourceServiceApp(userEmail,
				serviceConfig);
    }

}
