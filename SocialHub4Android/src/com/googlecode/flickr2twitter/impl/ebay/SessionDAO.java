/**
 * 
 */
package com.googlecode.flickr2twitter.impl.ebay;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
public class SessionDAO {
	
	private static final String GET_SESSION_ID_REQUEST = 
		"<?xml version=\"1.0\" encoding=\"utf-8\"?> \n"+
		"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"> "+
		"  <soapenv:Header> \n"+
		"    <RequesterCredentials soapenv:mustUnderstand=\"0\" xmlns=\"urn:ebay:apis:eBLBaseComponents\"> \n"+
		"      <ebl:Credentials xmlns:ebl=\"urn:ebay:apis:eBLBaseComponents\"> \n"+
		"        <ebl:DevId>%s</ebl:DevId> \n"+
		"        <ebl:AppId>%s</ebl:AppId> \n"+
		"        <ebl:AuthCert>%s</ebl:AuthCert> \n"+
		"      </ebl:Credentials> \n"+
		"    </RequesterCredentials> \n"+
		"  </soapenv:Header> \n"+
		"  <soapenv:Body> \n"+
		"    <GetSessionIDRequest xmlns=\"urn:ebay:apis:eBLBaseComponents\"> \n"+
		"      <Version>711</Version> \n"+
		"      <RuName>%s</RuName> \n"+
		"    </GetSessionIDRequest> \n"+
		"  </soapenv:Body> \n"+
		"</soapenv:Envelope>\n";

	
	

	public String getSessionId(
			String appId,
			String devId,
			String cert,
			String ruName
	) throws IOException, SAXException {

		Map<String, String> parameters = new HashMap<String, String>();
		
		parameters.put("siteid", "0");
		parameters.put("callname", "GetSessionID");
		parameters.put("appid", appId);
		parameters.put("client", "Nemo");

		
		URL url = URLHelper.buildUrl(true, "api.ebay.com", -1, "/wsapi", parameters);
		
		// URL url = URLHelper.buildUrl(false, "eazye.qa.ebay.com", 8080, "/wsapi", parameters);

		
		String requestXml = String.format(GET_SESSION_ID_REQUEST, devId, appId, cert, ruName );
		
		System.out.println(url);
		System.out.println(requestXml);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("SOAPAction", "");
		conn.setRequestProperty("Content-Type", "text/xml");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");

		
		
		DataOutputStream out = new DataOutputStream(
				conn.getOutputStream());
		out.writeBytes(requestXml);
		out.flush ();
		out.close();

		
		conn.connect();

		InputStream in = null;
		Document document = null;
		try {

			System.out.println(conn.getResponseCode());
			
			if (conn.getResponseCode() >= 400) {
				in = conn.getErrorStream();
				System.out.println(getInputStream(in));
			} else {
				in = conn.getInputStream();
				DocumentBuilderFactory builderFactory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = builderFactory.newDocumentBuilder();

				document = builder.parse(in);
			}

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

		NodeList nodeList = document.getElementsByTagName("SessionID");

		if (nodeList != null) {

			for (int i = 0; i < nodeList.getLength(); i++) {

				Node node = nodeList.item(i);

				String sessionId = getNodeValue(node);
				
				if (sessionId != null) {
					return sessionId;
				}
				

			}

		}

		return null;
	}
	
	private String getInputStream(InputStream is) throws IOException {
		   BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		    String line = "";
		    StringBuilder builder = new StringBuilder();
		    while((line = reader.readLine()) != null) {
		        builder.append(line);
		    }
		    return builder.toString();

	}

	private String getNodeValue(Node node) {

		if (node == null || node.getFirstChild() == null) {
			return null;
		}

		return node.getFirstChild().getNodeValue();

	}

	public static void main(String args[]) throws Exception {
		SessionDAO sessionDao = new SessionDAO();
		System.out.println(
				sessionDao.getSessionId(
				"eBay929a8-96bf-4ad8-a71c-94de77a7c9e", 
				"e19bd10f-316e-480c-b82f-e96b2719c27b", 
				"ee7e26be-a5a2-4c58-ae3c-0a867f8471fa", 
				"eBay-eBay929a8-96bf--yobbsc"));
		
//		System.out.println(
//				sessionDao.getSessionId(
//				"HochenNotDev", 
//				"HochenNotApp", 
//				"HochenNotCert", 
//				"hochen-HochenNotApp-faqyrf"));

	}

}
