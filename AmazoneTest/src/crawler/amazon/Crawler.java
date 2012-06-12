/**********************************************************************************************
 * Copyright 2009 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file 
 * except in compliance with the License. A copy of the License is located at
 *
 *       http://aws.amazon.com/apache2.0/
 *
 * or in the "LICENSE.txt" file accompanying this file. This file is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under the License. 
 *
 * ********************************************************************************************
 *
 *  Amazon Product Advertising API
 *  Signed Requests Sample Code
 *
 *  API Version: 2009-03-31
 *
 */

package crawler.amazon;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/*
 * This class shows how to make a simple authenticated ItemLookup call to the
 * Amazon Product Advertising API.
 * 
 * See the README.html that came with this sample for instructions on
 * configuring and running the sample.
 */
public class Crawler {

	/*
	 * Your AWS Access Key ID, as taken from the AWS Your Account page.
	 */
	private static final String AWS_ACCESS_KEY_ID;

	/*
	 * Your AWS Secret Key corresponding to the above ID, as taken from the AWS
	 * Your Account page.
	 */
	private static final String AWS_SECRET_KEY;

	/*
	 * Use one of the following end-points, according to the region you are
	 * interested in:
	 * 
	 * US: ecs.amazonaws.com CA: ecs.amazonaws.ca UK: ecs.amazonaws.co.uk DE:
	 * ecs.amazonaws.de FR: ecs.amazonaws.fr JP: ecs.amazonaws.jp
	 */
	private static final String ENDPOINT = "ecs.amazonaws.com";

	private static final Properties config = new Properties();
	private static final String CONFIG_FILE_LOCATION = "../AMAZONEID.txt";
	private static final String PERL_CMD;
	private static final String OUT_FOLDER = "./data/"; // TODO duplicate

	static {
		File propsFile = new File(CONFIG_FILE_LOCATION);
		FileInputStream fis;
		try {
			fis = new FileInputStream(propsFile);
			config.load(fis);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Failed to open configuration!!!!");
		}
		AWS_ACCESS_KEY_ID = config.getProperty("AWS_ACCESS_KEY_ID");
		AWS_SECRET_KEY = config.getProperty("AWS_SECRET_KEY");
		PERL_CMD = config.getProperty("PERL_CMD");
	}

	public static void main(String[] args) {
		SignedRequestsHelper helper;
		try {
			helper = SignedRequestsHelper.getInstance(ENDPOINT,
					AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		for (int page = 1; page < 31; page++) { // iterating over pages
			Map<String, String> searchParams = getSearchParams(page);
			String requestUrl = helper.sign(searchParams);
			System.out.println("At search page: " + page );

			// iterate over all items in page
			NodeList ids = null;
			NodeList titles = null;
			try {
				ids = fetchItemIDs(requestUrl);
				titles = fetchTitles(requestUrl);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			for (int i = 0; i < ids.getLength(); i++) {
				try {
					String itemId = ids.item(i).getTextContent();
					System.out.println("At item: " + itemId );
					
					Map<String, String> itemLookupParams = getItemLookupParams(itemId);
					requestUrl = helper.sign(itemLookupParams);
					
					File dir = new File(OUT_FOLDER);
					if (!dir.isDirectory()) dir.mkdirs();
					File descriptionFile = new File(dir,itemId + "_desc.txt");
					if (descriptionFile.exists()) continue;
					if (descriptionFile.exists()) descriptionFile.delete();
					descriptionFile.createNewFile();
					BufferedWriter bw = new BufferedWriter(new FileWriter(descriptionFile));
					bw.write(titles.item(i).getTextContent() + "\n");
					bw.write(fetchDescription(requestUrl) + "\n");
					bw.close();
					new ReviewExtractor(itemId, PERL_CMD);// handles reviews
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("failed id: " + ids.item(i).getTextContent() );
				}
			}
		}
	}

	private static Map<String, String> getSearchParams(int page) {
		Map<String, String> searchParams = new HashMap<String, String>();
		searchParams.put("Service", "AWSECommerceService");
		searchParams.put("Version", "2011-08-01");
		searchParams.put("Operation", "ItemSearch");
		searchParams.put("SearchIndex", "Electronics");
		searchParams.put("Keywords", "camera");
		searchParams.put("BrowseNode", "502394"); // narrows the search to actual cameras
		searchParams.put("AssociateTag", "YourAssociateTagHere");
		searchParams.put("ItemPage", Integer.toString(page));
		return searchParams;
	}

	private static Map<String, String> getItemLookupParams(String itemId) {
		Map<String, String> itemLookupParams = new HashMap<String, String>();
		itemLookupParams.put("Service", "AWSECommerceService");
		itemLookupParams.put("Version", "2011-08-01");
		itemLookupParams.put("Operation", "ItemLookup");
		itemLookupParams.put("ResponseGroup", "EditorialReview");
		itemLookupParams.put("AssociateTag", "YourAssociateTagHere");
		itemLookupParams.put("ItemId", itemId);
		return itemLookupParams;
	}

	private static String fetchDescription(String requestUrl) throws Exception {
		String content = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(requestUrl);
		Node contentNode = doc.getElementsByTagName("Content").item(0);
		content = contentNode.getTextContent();
		return content;
	}

	private static NodeList fetchTitles(String requestUrl) throws Exception {
		NodeList titleNodes = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(requestUrl);
		titleNodes = doc.getElementsByTagName("Title");
		return titleNodes;
	}

	private static NodeList fetchItemIDs(String requestUrl) throws Exception {
		NodeList ids = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(requestUrl);
		ids = doc.getElementsByTagName("ASIN");
		return ids;
	}
	
}
