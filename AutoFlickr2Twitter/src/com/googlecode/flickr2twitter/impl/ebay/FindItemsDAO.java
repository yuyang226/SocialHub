/**
 * 
 */
package com.googlecode.flickr2twitter.impl.ebay;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.googlecode.flickr2twitter.org.apache.commons.lang3.StringUtils;


/**
 * @author hochen
 * 
 */
public class FindItemsDAO {

	// private String sellerId = "eforcity";
	
	private static final String APP_ID_SANDBOX= "eBayb1609-29f8-4684-aadb-6ba5a05a182";
	private static final String APP_ID_PROD = "eBay929a8-96bf-4ad8-a71c-94de77a7c9e";
	
	String EBAY_SANDBOX_SERVICE_SERVER = "svcs.sandbox.ebay.com";
	String EBAY_SANDBOX_SERVICE_PATH = "/services/search/FindingService/v1";
	
	String EBAY_SERVICE_SERVER = "svcs.ebay.com";
	String EBAY_SERVICE_PATH = "/services/search/FindingService/v1";
	
	public List<EbayItem> findItemsByKeywordsFromSandbox(
			String keywords,
			String minPrice,
			String maxPrice,
			int entriesPerPage) throws IOException, SAXException {
		
		return findItemsByKeywords(true, null, keywords, minPrice, maxPrice, entriesPerPage);
	}
	
	public List<EbayItem> findItemsByKeywordsFromProduction(
			String keywords,
			String minPrice,
			String maxPrice,
			int entriesPerPage) throws IOException, SAXException {
		
		return findItemsByKeywords(false, null, keywords, minPrice, maxPrice, entriesPerPage);
	}
	
	public URL buildSearchItemsUrl(
			boolean isSandbox, 
			String keywords,
			String minPrice,
			String maxPrice,
			int entriesPerPage) throws MalformedURLException {
		
		
		
		Map<String, String> parameters = generateSearchParameters(isSandbox, keywords, null, minPrice, maxPrice, entriesPerPage);
		URL url = null;
		if (isSandbox) {
			url = URLHelper.buildUrl(
					false, 
					EBAY_SANDBOX_SERVICE_SERVER, 
					80,
					EBAY_SANDBOX_SERVICE_PATH, 
					parameters);
		} else {
			url = URLHelper.buildUrl(
				false, 
				EBAY_SERVICE_SERVER, 
				80,
				EBAY_SERVICE_PATH, 
				parameters);
		}
		
		return url;
	}
	
	public String urlEncode(String keywords) {
		try {
			String encoding = System.getProperty("file.encoding");
			if (StringUtils.isNotBlank(encoding)) {
				keywords = URLEncoder.encode(keywords, encoding);
			}
		} catch (Exception e) {
			//ignore
			e.printStackTrace();
		}
		return keywords;
	}
	
	private Map<String, String> generateSearchParameters(
			boolean isSandbox, 
			String keywords, 
			String sellerId, 
			String minPrice,
			String maxPrice,
			int entriesPerPage) {
		Map<String, String> parameters = new LinkedHashMap<String, String>();
		
		if (isSandbox) {
			parameters.put(urlEncode("SECURITY-APPNAME"), APP_ID_SANDBOX);
		} else {
			parameters.put(urlEncode("SECURITY-APPNAME"), APP_ID_PROD);
		}
		
		parameters.put(urlEncode("OPERATION-NAME"), "findItemsByKeywords");
		parameters.put(urlEncode("SERVICE-VERSION"), "1.9.0");
		parameters.put(urlEncode("RESPONSE-DATA-FORMAT"), "XML");
		parameters.put(urlEncode("REST-PAYLOAD"), "");
		
		parameters.put("paginationInput.entriesPerPage", String.valueOf(entriesPerPage));
		parameters.put("keywords", urlEncode(keywords));
		
		int i=0;
		
		if (sellerId != null) {
			parameters.put(urlEncode("itemFilter["+i +"].name"), "Seller");
			parameters.put(urlEncode("itemFilter["+i +"].value"), urlEncode(sellerId));
			i++;
		}
		
		
		Double minPrice2 = null;
		try {
			if (minPrice != null) {
				minPrice2 = new Double(minPrice);
			}
		} catch (NumberFormatException nfe) {

		}
		
		Double maxPrice2 = null;
		try {
			if (maxPrice != null) {
				maxPrice2 = new Double(maxPrice);
			}
		} catch (NumberFormatException nfe) {

		}
		
		if (minPrice2 != null) {
			parameters.put(urlEncode("itemFilter["+i +"].name"), "MinPrice");
			parameters.put(urlEncode("itemFilter["+i +"].value"), urlEncode(String.valueOf(minPrice2)));
			i++;
		}
		
		if (maxPrice2 != null) {
			parameters.put(urlEncode("itemFilter["+ i +"].name"), "MaxPrice");
			parameters.put(urlEncode("itemFilter["+ i +"].value"), urlEncode(String.valueOf(maxPrice2)));
			i++;
		}
		
		parameters.put("sortOrder", "StartTimeNewest");
		parameters.put("descriptionSearch", "true");
		return parameters;
		
	}
	
	public List<EbayItem> findItemsByKeywords(
			boolean isSandbox,
			String sellerId,
			String keywords,
			String minPrice,
			String maxPrice,
			int entriesPerPage) throws IOException, SAXException {
		URL url = buildSearchItemsUrl(isSandbox, keywords, minPrice, maxPrice, entriesPerPage);

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
		} catch (Exception e) {
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

		NodeList nodeList = document.getElementsByTagName("item");
		List<EbayItem> lstItem = new ArrayList<EbayItem>();
		if (nodeList != null) {
			
			for (int i = 0; i < nodeList.getLength(); i++) {

				Node node = nodeList.item(i);

				NodeList itemRootNode = node.getChildNodes();
				EbayItem item = new EbayItem(sellerId);
				lstItem.add(item);
				generateItem(itemRootNode, item);

			}
		}

		return lstItem;
	}
	
	private void generateItem(NodeList itemNode, EbayItem item) {
		
		if (itemNode == null || item == null) {
			return;
		}
		
		for (int k = 0; k < itemNode.getLength(); k++) {

			Node firstLevelNode = itemNode.item(k);
			if (itemNode != null) {

				String nodeName = firstLevelNode.getNodeName();
				String nodeValue = getNodeValue(firstLevelNode);
				
				if (nodeName != null) {
					if ("itemId".equals(nodeName) && nodeValue != null) {
						try {
							item.setItemId(Long.parseLong(nodeValue));
						} catch (NumberFormatException nfe) {

						}
					} else if ("title".equals(nodeName)) {
						item.setTitle(nodeValue);
					} else if ("viewItemURL".equals(nodeName)) {
						item.setViewItemURL(nodeValue);
					} else if ("galleryURL".equals(nodeName)) {
						item.setGalleryURL(nodeValue);
					} else if ("listingInfo".equals(nodeName)) {
						generateItem(firstLevelNode.getChildNodes(), item);
					} else if ("startTime".equals(nodeName)) {
						item.setStartTime(toDate(nodeValue));
					} else if ("endTime".equals(nodeName)) {
						item.setEndTime(toDate(nodeValue));
					} else if ("convertedBuyItNowPrice".equals(nodeName) && nodeValue != null) {
						try {
							item.setBuyItNowPrice(Double.parseDouble(nodeValue));
						} catch (NumberFormatException nfe) {

						}
					} else if ("sellingStatus".equals(nodeName)) {
						generateItem(firstLevelNode.getChildNodes(), item);
					} else if ("convertedCurrentPrice".equals(nodeName) && nodeValue != null) {
						try {
							item.setCurrentPrice(Double.parseDouble(nodeValue));
						} catch (NumberFormatException nfe) {

						}
					}
				}

			}

		}
	}
	
	private Date toDate(String strDate) {
		if (strDate == null) {
			return null;
		}
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		
			return sdf.parse(strDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getNodeValue(Node node) {

		if (node == null || node.getFirstChild() == null) {
			return null;
		}

		return node.getFirstChild().getNodeValue();

	}

	public static void main(String args[]) throws Exception {
		FindItemsDAO ItemDAO = new FindItemsDAO();
		//System.out.println(ItemDAO.findItemsByKeywords(false,"eforcity","iphone", 10));
		System.out.println(ItemDAO.findItemsByKeywords(false,null,"nikon d80", "100.00111", "", 10));
		
	}

}
