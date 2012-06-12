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

package com.amazon.advertising.api.sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
public class ItemLookupSample {

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
	private static final String PERL_CMD = "C:/strawberry/perl/bin/perl.exe";
	private static final String DOWNLOAD_REVIEWS_SCRIPTS = "./src/scripts/perl/downloadAmazonReviews.pl";
	private static final String EXTRACT_REVIEWS_SCRIPTS = "./src/scripts/perl/extractAmazonReviews.pl";
	private static final String outPath = "./amazonreviews/";

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

		// Parameters for searching cameras
		String requestUrl = null;
		Map<String, String> searchParams = new HashMap<String, String>();
		searchParams.put("Service", "AWSECommerceService");
		searchParams.put("Version", "2011-08-01");
		searchParams.put("Operation", "ItemSearch");
		searchParams.put("SearchIndex", "Electronics");
		searchParams.put("Keywords", "camera");
		searchParams.put("BrowseNode", "502394"); // narrows the search to
													// actual cameras
		searchParams.put("AssociateTag", "YourAssociateTagHere");

		Map<String, String> itemLookupParams = new HashMap<String, String>();
		itemLookupParams.put("Service", "AWSECommerceService");
		itemLookupParams.put("Version", "2011-08-01");
		itemLookupParams.put("Operation", "ItemLookup");
		itemLookupParams.put("ResponseGroup", "EditorialReview");
		itemLookupParams.put("AssociateTag", "YourAssociateTagHere");

		for (int page = 1; page < 3; page++) { // iterating over pages
			searchParams.put("ItemPage", Integer.toString(page));
			requestUrl = helper.sign(searchParams);
//			System.out.println("Signed search request is \"" + requestUrl + "\"");

			// iterate over all items in page
			NodeList ids = fetchItemIDs(requestUrl);
			NodeList titles = fetchTitles(requestUrl);
			for (int i = 0; i < ids.getLength(); i++) {
				System.out.println(ids.item(i).getTextContent());
				System.out.println(titles.item(i).getTextContent());
				itemLookupParams.put("ItemId", ids.item(i).getTextContent());
				requestUrl = helper.sign(itemLookupParams);
				System.out.println(fetchDescription(requestUrl));
//				extractProductReviews(ids.item(i).getTextContent(),System.out);
				break;
			}
		}
	}

	private static String fetchDescription(String requestUrl) {
		String content = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(requestUrl);
			Node contentNode = doc.getElementsByTagName("Content").item(0);
			content = contentNode.getTextContent();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return content;
	}

	private static NodeList fetchTitles(String requestUrl) {
		NodeList titleNodes = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(requestUrl);
			titleNodes = doc.getElementsByTagName("Title");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return titleNodes;
	}

	private static NodeList fetchItemIDs(String requestUrl) {
		NodeList ids = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(requestUrl);
			ids = doc.getElementsByTagName("ASIN");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return ids;
	}
	
	private static void extractProductReviews(String ProducID, OutputStream destanation)
			throws Exception {
		Process p = Runtime.getRuntime().exec(
				new String[] { PERL_CMD, DOWNLOAD_REVIEWS_SCRIPTS, ProducID });
		pipeOutput(p);
		p.waitFor();
		File outFolder = new File(outPath + ProducID);
		for (String file : outFolder.list()) {
			Process p2 = Runtime.getRuntime().exec(
					new String[] { PERL_CMD, EXTRACT_REVIEWS_SCRIPTS,
							outFolder.getPath() + "/" + file });
			pipe(p2.getInputStream(), destanation);
		}
	}

	private static void pipeOutput(Process process) {
		pipe(process.getErrorStream(), System.err);
		pipe(process.getInputStream(), System.out);
	}

	private static void pipe(final InputStream src, final OutputStream dest) {
		new Thread(new Runnable() {
			public void run() {
				try {
					byte[] buffer = new byte[1024];
					for (int n = 0; n != -1; n = src.read(buffer)) {
						dest.write(buffer, 0, n);
					}
				} catch (IOException e) { // just exit
				}
			}
		}).start();
	}

}
