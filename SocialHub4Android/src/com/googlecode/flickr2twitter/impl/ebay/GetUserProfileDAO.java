/**
 * 
 */
package com.googlecode.flickr2twitter.impl.ebay;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author hochen
 * 
 */
public class GetUserProfileDAO {
	
	
	String EBAY_SANDBOX_SHOPPING_API_SERVER = "open.api.sandbox.ebay.com";
	String EBAY_SANDBOX_SHOPPING_API_PATH = "/shopping";
	
	String EBAY_SHOPPING_API_SERVER = "open.api.ebay.com";
	String EBAY_SHOPPING_API_PATH = "/shopping";
	
	private static final String EBAY_APPID_PRODUCTION = "eBay929a8-96bf-4ad8-a71c-94de77a7c9e";
	private static final String EBAY_APPID_SANDBOX = "eBayb1609-29f8-4684-aadb-6ba5a05a182";
	
	public EbayUser getUserProfile(
			boolean isSandbox,
			String userId
	) throws IOException, SAXException {
		return getUserProfile(isSandbox, isSandbox ? EBAY_APPID_SANDBOX : EBAY_APPID_PRODUCTION, userId);
	}

	public EbayUser getUserProfile(
			boolean isSandbox,
			String appId,
			String userId
	) throws IOException, SAXException {

		Map<String, String> parameters = new HashMap<String, String>();
		
		
		parameters.put("callname", "GetUserProfile");
		parameters.put("responseencoding", "XML");
		parameters.put("appid", appId);
		parameters.put("siteid", "0");
		parameters.put("UserID", userId);
		parameters.put("IncludeSelector", "Details");
		parameters.put("version", "707");

		URL url = null;
		
		if (isSandbox) {
			url = URLHelper.buildUrl(false, EBAY_SANDBOX_SHOPPING_API_SERVER, -1, EBAY_SANDBOX_SHOPPING_API_PATH, parameters);
		} else {
			url = URLHelper.buildUrl(false, EBAY_SHOPPING_API_SERVER, -1, EBAY_SHOPPING_API_PATH, parameters);
		}
			
			

		System.out.println(url);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");

		conn.connect();

		InputStream in = null;
		Document document = null;
		try {

			in = conn.getInputStream();

			DocumentBuilderFactory builderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();

			document = builder.parse(in);

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {

				}
			}
		}

		if (document == null) {
			return null;
		}

		NodeList nodeList = document.getElementsByTagName("User");
		
		EbayUser user = null;
		if (nodeList != null && nodeList.getLength() > 0) {
			user = new EbayUser();
			Node userNode = nodeList.item(0);
			NodeList userFirstLevelNodes = userNode.getChildNodes();
			generateUser(userFirstLevelNodes, user);
			
			
		}
		


		return user;
	}
	
	
	
	
	private void generateUser(NodeList userFirstLevelNodes, EbayUser user) {
		
		if (userFirstLevelNodes == null || user == null) {
			return;
		}
		
		for (int k = 0; k < userFirstLevelNodes.getLength(); k++) {

			Node userFirstLevelNode = userFirstLevelNodes.item(k);
			if (userFirstLevelNode != null) {

				String nodeName = userFirstLevelNode.getNodeName();
				String nodeValue = getNodeValue(userFirstLevelNode);
				
				if (nodeName != null) {
					if ("UserID".equals(nodeName)) {
						user.setUserId(nodeValue);
					} else if ("FeedbackRatingStar".equals(nodeName)) {
						user.setFeedbackRatingStar(nodeValue);
					} else if ("FeedbackScore".equals(nodeName)) {
						user.setFeedbackScore(nodeValue);
					} else if ("RegistrationDate".equals(nodeName)) {
						user.setRegistrationDate(nodeValue);
					} else if ("Status".equals(nodeName)) {
						user.setStatus(nodeValue);
					} else if ("StoreURL".equals(nodeName)) {
						user.setStoreURL(nodeValue);
					} else if ("StoreName".equals(nodeName)) {
						user.setStoreName(nodeValue);
					} else if ("SellerItemsURL".equals(nodeName)) {
						user.setSellerItemsURL(nodeValue);
					} else if ("AboutMeURL".equals(nodeName)) {
						user.setAboutMeURL(nodeValue);
					} else if ("MyWorldURL".equals(nodeName)) {
						user.setMyWorldURL(nodeValue);
					} else if ("ReviewsAndGuidesURL".equals(nodeName)) {
						user.setReviewsAndGuidesURL(nodeValue);
					} else if ("PositiveFeedbackPercent".equals(nodeName)) {
						user.setPositiveFeedbackPercent(nodeValue);
					} else if ("TopRatedSeller".equals(nodeName)) {
						user.setTopRatedSeller(nodeValue);
					}
				}

			}

		}
	}
	
	

	private String getNodeValue(Node node) {

		if (node == null || node.getFirstChild() == null) {
			return null;
		}

		return node.getFirstChild().getNodeValue();

	}

	public static void main(String args[]) throws Exception {
		
		GetUserProfileDAO getUserProfileDAO = new GetUserProfileDAO();
		
		// production
		//System.out.println(getUserProfileDAO.getUserProfile(false, "nemo.chen"));
		
		// sandbox
		System.out.println(getUserProfileDAO.getUserProfile(true, "TESTUSER_socialhub"));
		
		
	
	}

}
