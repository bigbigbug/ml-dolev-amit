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
     *      US: ecs.amazonaws.com 
     *      CA: ecs.amazonaws.ca 
     *      UK: ecs.amazonaws.co.uk 
     *      DE: ecs.amazonaws.de 
     *      FR: ecs.amazonaws.fr 
     *      JP: ecs.amazonaws.jp
     * 
     */
    private static final String ENDPOINT = "ecs.amazonaws.com";

    private static final Properties config = new Properties();
    private static final String CONFIG_FILE_LOCATION = "../AMAZONEID.txt";
    
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
        /*
         * Set up the signed requests helper 
         */
        SignedRequestsHelper helper;
        try {
            helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        
        String requestUrl = null;
        String title = null;

        /* The helper can sign requests in two forms - map form and string form */
        
        /*
         * Here is an example in map form, where the request parameters are stored in a map.
         */
        System.out.println("Map form example:");
        Map<String, String> params = new HashMap<String, String>();
//        params.put("Service", "AWSECommerceService");
//        params.put("Version", "2009-03-31");
//        params.put("Operation", "ItemLookup");
//        params.put("ItemId", ITEM_ID);
//        params.put("ResponseGroup", "Small");
        
        params.put("Service","AWSECommerceService");
        params.put("Version","2011-08-01");
		params.put("Operation","ItemSearch");
		params.put("SearchIndex","Books");
		params.put("Keywords","harry+potter");
		params.put("AssociateTag","YourAssociateTagHere");

        requestUrl = helper.sign(params);
        System.out.println("Signed Request is \"" + requestUrl + "\"");

        title = fetchTitle(requestUrl);
        System.out.println("Signed Title is \"" + title + "\"");
        System.out.println();

        NodeList ids = fetchItemIDs(requestUrl);
        for (int i = 0; i < ids.getLength(); i++) {
			System.out.println(ids.item(i).getTextContent());
			params = new HashMap<String, String>();
	        params.put("Operation", "ItemLookup");
	        params.put("ItemId", ids.item(i).getTextContent());	        
	        params.put("Service","AWSECommerceService");
	        params.put("Version","2011-08-01");
			params.put("AssociateTag","YourAssociateTagHere");
			requestUrl = helper.sign(params);
	        System.out.println("Signed Request is \"" + requestUrl + "\"");

	        title = fetchTitle(requestUrl);
	        System.out.println("Signed Title is \"" + title + "\"");
		}
    }

    /*
     * Utility function to fetch the response from the service and extract the
     * title from the XML.
     */
    private static String fetchTitle(String requestUrl) {
        String title = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(requestUrl);
            Node titleNode = doc.getElementsByTagName("Title").item(0);
            title = titleNode.getTextContent();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return title;
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

}
