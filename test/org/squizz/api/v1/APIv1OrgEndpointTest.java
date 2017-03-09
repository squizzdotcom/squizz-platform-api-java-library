/**
* Copyright (C) 2017 Squizz PTY LTD
* This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
*/
package org.squizz.api.v1;

import org.squizz.api.v1.endpoint.APIv1EndpointResponse;
import org.squizz.api.v1.endpoint.APIv1EndpointOrgCreateNotification;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.squizz.api.v1.endpoint.APIv1EndpointOrgImportESDocument;
import org.squizz.api.v1.endpoint.APIv1EndpointOrgValidateSecurityCertificate;
import EcommerceStandardsDocuments.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import org.squizz.api.v1.endpoint.APIv1EndpointOrgProcurePurchaseOrderFromSupplier;
import org.squizz.api.v1.endpoint.APIv1EndpointResponseESD;

/**
 * Tests calling the SQUIZZ.com platform's API endpoints related to organisation notifications
 */
public class APIv1OrgEndpointTest 
{
    /**
     * tests sending an organisation notification within a platform's API
     * @param testNumber number of the test being performed
     * @param apiOrgSession existing API organisation session
     */
    public static void testEndpointCreateOrganisationNotification(int testNumber, APIv1OrgSession apiOrgSession)
    {
        System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
        System.out.println("Test "+testNumber+" - Create Organisation Notification Endpoint");
        System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        try{
            //get the organisation API credentials
            System.out.println("Enter Endpoint Timeout (Milliseconds):");
            int timeoutMilliseconds = Integer.parseInt(reader.readLine());
            
            System.out.println("Enter Notification Category ("+APIv1EndpointOrgCreateNotification.NOTIFY_CATEGORY_ACCOUNT+","+APIv1EndpointOrgCreateNotification.NOTIFY_CATEGORY_FEED+","+APIv1EndpointOrgCreateNotification.NOTIFY_CATEGORY_ORDER_PURCHASE+","+APIv1EndpointOrgCreateNotification.NOTIFY_CATEGORY_ORDER_SALE+","+APIv1EndpointOrgCreateNotification.NOTIFY_CATEGORY_ORG+"):");
            String notifyCategory = reader.readLine();
            
            System.out.println("Enter Notification Message:");
            String message = reader.readLine();
            
            System.out.println("Enter message placeholder links (separate links by commas):");
            String[] linkURLs = reader.readLine().split(",");
            
            System.out.println("Enter message placeholder labels (separate labels by commas):");
            String[] linkLabels = reader.readLine().split(",");
            
            //call API endpoint in the SQUIZZ.com platform to validate the existing session
            System.out.println("Call API to send organisation notification");
            APIv1EndpointResponseESD endpointResponse = APIv1EndpointOrgCreateNotification.call(apiOrgSession, timeoutMilliseconds, notifyCategory, message, linkURLs, linkLabels);
            
            //check the result of validating the session
            if(endpointResponse.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS)){
                System.out.println("SUCCESS - organisation notification successfully created in the platform");
                System.out.println("Session ID - " + apiOrgSession.getSessionID());
                System.out.println("Session API Version - " + apiOrgSession.getAPIVersion());
                System.out.println("Session Exists - " + apiOrgSession.sessionExists());
            }else{
                System.out.println("FAIL - organisation notification failed to be created.");
                System.out.println("Endpoint Result: " + endpointResponse.result);
                System.out.println("Endpoint Result Code: " + endpointResponse.result_code);
                System.out.println("Endpoint Result Message: " + endpointResponse.result_message);
            }
        }catch(Exception ex){
            System.out.println("An error occurred when performing the test. Error: " + ex.getLocalizedMessage());
        }
        
        System.out.println("Test "+testNumber+" - Finished");
    }
    
    /**
     * tests validating an organisation's security certificate created in the platform
     * @param testNumber number of the test being performed
     * @param apiOrgSession existing API organisation session
     */
    public static void testEndpointOrgValidateSecurityCertificate(int testNumber, APIv1OrgSession apiOrgSession)
    {
        System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
        System.out.println("Test "+testNumber+" - Validate Organisation Security Certificate Endpoint");
        System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        try{
            //get the organisation API credentials
            System.out.println("Enter Endpoint Timeout (Milliseconds):");
            int timeoutMilliseconds = Integer.parseInt(reader.readLine());
            
            System.out.println("Enter Security Certificate ID:");
            String orgSecurityCertificateID = reader.readLine();
            
            //call API endpoint in the SQUIZZ.com platform to validate the organisation's security certificate
            System.out.println("Call API to send organisation notification");
            APIv1EndpointResponseESD endpointResponse = APIv1EndpointOrgValidateSecurityCertificate.call(apiOrgSession, timeoutMilliseconds, orgSecurityCertificateID);
            
            //check the result of validating the certificate
            if(endpointResponse.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS)){
                System.out.println("SUCCESS - organisation security certificate has been validated.");
                System.out.println("Session ID - " + apiOrgSession.getSessionID());
                System.out.println("Session API Version - " + apiOrgSession.getAPIVersion());
                System.out.println("Session Exists - " + apiOrgSession.sessionExists());
            }else{
                System.out.println("FAIL - organisation security certificate could not be validated.");
                System.out.println("Endpoint Result: " + endpointResponse.result);
                System.out.println("Endpoint Result Code: " + endpointResponse.result_code);
                System.out.println("Endpoint Result Message: " + endpointResponse.result_message);
            }
        }catch(Exception ex){
            System.out.println("An error occurred when performing the test. Error: " + ex.getLocalizedMessage());
        }
        
        System.out.println("Test "+testNumber+" - Finished");
    }
    
    /**
     * tests importing organisation data into the SQUIZZ.com API where the data has been set within an Ecommerce Standards Document
     * @param testNumber number of the test being performed
     * @param apiOrgSession existing API organisation session
     */
    public static void testEndpointOrgImportESDocument(int testNumber, APIv1OrgSession apiOrgSession)
    {
        System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
        System.out.println("Test "+testNumber+" - Import Organisation ESDocument Endpoint");
        System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        try{
            //get the time to set to timeout the endpoint request
            System.out.println("Enter Endpoint Timeout (Milliseconds):");
            int timeoutMilliseconds = Integer.parseInt(reader.readLine());
            
            System.out.println("Testing Importing Taxcode data");
            
            //create taxcode records
            ArrayList<ESDRecordTaxcode> taxcodeRecords = new ArrayList<ESDRecordTaxcode>();
            ESDRecordTaxcode taxcodeRecord = new ESDRecordTaxcode();
            taxcodeRecord.keyTaxcodeID = "1";
            taxcodeRecord.taxcode = "GST";
            taxcodeRecord.taxcodeLabel = "GST";
            taxcodeRecord.description = "Goods And Services Tax";
            taxcodeRecord.taxcodePercentageRate = 10;
            taxcodeRecords.add(taxcodeRecord);
            
            taxcodeRecord = new ESDRecordTaxcode();
            taxcodeRecord.keyTaxcodeID = "2";
            taxcodeRecord.taxcode = "FREE";
            taxcodeRecord.taxcodeLabel = "Tax Free";
            taxcodeRecord.description = "Free from any Taxes";
            taxcodeRecord.taxcodePercentageRate = 0;
            taxcodeRecords.add(taxcodeRecord);
            
            taxcodeRecord = new ESDRecordTaxcode();
            taxcodeRecord.keyTaxcodeID = "3";
            taxcodeRecord.taxcode = "NZGST";
            taxcodeRecord.taxcodeLabel = "New Zealand GST Tax";
            taxcodeRecord.description = "New Zealand Goods and Services Tax";
            taxcodeRecord.taxcodePercentageRate = 15;
            taxcodeRecords.add(taxcodeRecord);
            
            HashMap<String, String> configs = new HashMap<String, String>();
            configs.put("dataFields", "keyTaxcodeID,taxcode,taxcodeLabel,description,taxcodePercentageRate");
            ESDocumentTaxcode taxcodeESD = new ESDocumentTaxcode(ESDocumentConstants.RESULT_SUCCESS, "successfully obtained data", taxcodeRecords.toArray(new ESDRecordTaxcode[0]), configs);
            
            //call API endpoint in the SQUIZZ.com platform to import the ESD organisational data
            System.out.println("Call API to import organisation data stored in an ESDocument");
            APIv1EndpointResponseESD endpointResponse = APIv1EndpointOrgImportESDocument.call(apiOrgSession, timeoutMilliseconds, APIv1EndpointOrgImportESDocument.IMPORT_TYPE_ID_TAXCODES, taxcodeESD);
            
            //check the result of validating the certificate
            if(endpointResponse.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS)){
                System.out.println("SUCCESS - organisation ESDocument data has successfully been imported.");
                System.out.println("Session ID - " + apiOrgSession.getSessionID());
                System.out.println("Session API Version - " + apiOrgSession.getAPIVersion());
                System.out.println("Session Exists - " + apiOrgSession.sessionExists());
            }else{
                System.out.println("FAIL - organisation ESDocument data failed to import.");
                System.out.println("Endpoint Result: " + endpointResponse.result);
                System.out.println("Endpoint Result Code: " + endpointResponse.result_code);
                System.out.println("Endpoint Result Message: " + endpointResponse.result_message);
            }
        }catch(Exception ex){
            System.out.println("An error occurred when performing the test. Error: " + ex.getLocalizedMessage());
        }
        
        System.out.println("Test "+testNumber+" - Finished");
    }
    
    /**
     * tests importing one or more purchase orders into the SQUIZZ.com API and having the orders sent to a supplier organisation for purchasing and dispatch
     * @param testNumber number of the test being performed
     * @param apiOrgSession existing API organisation session
     */
    public static void testEndpointOrgProcurePurchaseOrderFromSupplier(int testNumber, APIv1OrgSession apiOrgSession)
    {
        ESDocumentOrderSale esDocumentOrderSale = null;
                
        System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
        System.out.println("Test "+testNumber+" - Import Procure Purchase Order From Supplier Organisation Endpoint");
        System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        try{
            //get the time to set to timeout the endpoint request
            System.out.println("Enter Endpoint Timeout (Milliseconds):");
            int timeoutMilliseconds = Integer.parseInt(reader.readLine());
            
            //get the supplier organisation ID
            System.out.println("Enter Supplier Organisation ID:");
            String supplierOrgID = reader.readLine();
            
            //get the customer account code
            System.out.println("Enter Customer Account Code:");
            String customerAccountCode = reader.readLine();
            System.out.println("Testing procuring purchase order data");
            
            //get the purchase order data
            System.out.println("Enter Purchase Order Record (in ESD record JSON form)");
            String purchaseOrderRecordJSON = reader.readLine();
            
            //deserialize purchase order record
            ObjectMapper jsonMapper = new ObjectMapper();
            jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            ESDRecordOrderPurchase purchaseOrderRecord = jsonMapper.readValue(purchaseOrderRecordJSON, ESDRecordOrderPurchase.class);
            
            //create purchase order records
            ArrayList<ESDRecordOrderPurchase> purchaseOrderRecords = new ArrayList<>();
            purchaseOrderRecords.add(purchaseOrderRecord);
            
            //create ESD purchase order document
            HashMap<String, String> configs = new HashMap<>();
            configs.put("dataFields", "");
            ESDocumentOrderPurchase orderPurchaseESD = new ESDocumentOrderPurchase(ESDocumentConstants.RESULT_SUCCESS, "successfully obtained data", purchaseOrderRecords.toArray(new ESDRecordOrderPurchase[0]), configs);
            
            //call API endpoint in the SQUIZZ.com platform to send purchase order and have it processed by the supplier organisation
            System.out.println("Call API to import organisation data stored in an ESDocument");
            APIv1EndpointResponseESD endpointResponse = APIv1EndpointOrgProcurePurchaseOrderFromSupplier.call(apiOrgSession, timeoutMilliseconds, supplierOrgID, customerAccountCode, orderPurchaseESD);
            esDocumentOrderSale = (ESDocumentOrderSale)endpointResponse.esDocument;
            
            //check the result of procuring the purchase orders
            if(endpointResponse.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS)){
                System.out.println("SUCCESS - organisation purchase orders have successfully been sent to supplier organisation.");
                System.out.println("Session ID - " + apiOrgSession.getSessionID());
                System.out.println("Session API Version - " + apiOrgSession.getAPIVersion());
                System.out.println("Session Exists - " + apiOrgSession.sessionExists());
                
                //iterate through each of the returned sales orders and output the details of the sales orders
                if(esDocumentOrderSale.dataRecords != null){
                    for (ESDRecordOrderSale salesOrderRecord : esDocumentOrderSale.dataRecords){
                        System.out.println("\nSales Order Returned, Order Details: ");
                        System.out.println("Sales Order Code: " + salesOrderRecord.salesOrderCode);
                        System.out.println("Sales Order Total Cost: " + salesOrderRecord.totalPriceIncTax + " (" + salesOrderRecord.currencyISOCode + ")");
                        System.out.println("Sales Order Total Taxes: " + salesOrderRecord.totalTax + " (" + salesOrderRecord.currencyISOCode + ")");
                        System.out.println("Sales Order Customer Account: " + salesOrderRecord.customerAccountCode);
                        System.out.println("Sales Order Total Lines: " + salesOrderRecord.totalLines);
                    }
                }
            }else{
                System.out.println("FAIL - organisation purchase orders failed to be processed");
                System.out.println("Endpoint Result: " + endpointResponse.result);
                System.out.println("Endpoint Result Code: " + endpointResponse.result_code);
                System.out.println("Endpoint Result Message: " + endpointResponse.result_message);
                
                //if one or more products in the purchase order could not match a product for the supplier organisation then find out the order lines caused the problem
                if(endpointResponse.result_code.equals(APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_ORDER_PRODUCT_NOT_MAPPED) && esDocumentOrderSale != null)
                {   
                    if(esDocumentOrderSale.configs.containsKey(APIv1EndpointResponseESD.ESD_CONFIG_ORDERS_WITH_UNMAPPED_LINES))
                    {
                        //get comma separated list of order record indicies and line indicies that indicate the unmapped order lines
                        String unmappedOrderLineCSV = esDocumentOrderSale.configs.get(APIv1EndpointResponseESD.ESD_CONFIG_ORDERS_WITH_UNMAPPED_LINES);
                        
                        //get the index of the order record and line that contained the unmapped product
                        if(!unmappedOrderLineCSV.trim().isEmpty()){
                            String[] unmappedOrderLineIndices = unmappedOrderLineCSV.trim().split(",");
                            
                            //iterate through each order-line index
                            for(int i=0; i < unmappedOrderLineIndices.length; i++){
                                //get order index and line index
                                String[] orderLineIndex = unmappedOrderLineIndices[i].split(":");
                                if(orderLineIndex.length == 2){
                                    try{
                                        int orderIndex = Integer.parseInt(orderLineIndex[0]);
                                        int lineIndex = Integer.parseInt(orderLineIndex[1]);
                                        
                                        //check that the order can be found that contains the problematic line
                                        if(orderIndex < orderPurchaseESD.dataRecords.length && lineIndex < orderPurchaseESD.dataRecords[orderIndex].lines.size()){
                                            System.out.println("For purchase order: "+ orderPurchaseESD.dataRecords[orderIndex].purchaseOrderCode + " a matching supplier product for line number: " + (lineIndex+1) + " could not be found.");
                                        }
                                        
                                    }catch(Exception ex){
                                        System.out.println("Error when trying to specify the purchase order that contains an unmapped order line. Error: " + ex.getLocalizedMessage());
                                    }
                                }
                            }
                        }
                    }
                }
                //if one or more products in the purchase order could not be priced by the supplier organisation then find the order line that caused the problem
                else if(endpointResponse.result_code.equals(APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_ORDER_MAPPED_PRODUCT_PRICE_NOT_FOUND) && esDocumentOrderSale != null)
                {
                    if(esDocumentOrderSale.configs.containsKey(APIv1EndpointResponseESD.ESD_CONFIG_ORDERS_WITH_UNPRICED_LINES))
                    {
                        //get comma separated list of order record indicies and line indicies that indicate the unpriced order lines
                        String unmappedOrderLineCSV = esDocumentOrderSale.configs.get(APIv1EndpointResponseESD.ESD_CONFIG_ORDERS_WITH_UNPRICED_LINES);
                        
                        //get the index of the order record and line that contained the unpriced product
                        if(!unmappedOrderLineCSV.trim().isEmpty()){
                            String[] unmappedOrderLineIndices = unmappedOrderLineCSV.trim().split(",");
                            
                            //iterate through each order-line index
                            for(int i=0; i < unmappedOrderLineIndices.length; i++){
                                //get order index and line index
                                String[] orderLineIndex = unmappedOrderLineIndices[i].split(":");
                                if(orderLineIndex.length == 2){
                                    try{
                                        int orderIndex = Integer.parseInt(orderLineIndex[0]);
                                        int lineIndex = Integer.parseInt(orderLineIndex[1]);
                                        
                                        //check that the order can be found that contains the problematic line
                                        if(orderIndex < orderPurchaseESD.dataRecords.length && lineIndex < orderPurchaseESD.dataRecords[orderIndex].lines.size()){
                                            System.out.println("For purchase order: "+ orderPurchaseESD.dataRecords[orderIndex].purchaseOrderCode + " the supplier has not set pricing for line number: " + (lineIndex+1));
                                        }
                                        
                                    }catch(Exception ex){
                                        System.out.println("Error when trying to specify the purchase order that contains an unpriced order line. Error: " + ex.getLocalizedMessage());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }catch(Exception ex){
            System.out.println("An error occurred when performing the test. Error: " + ex.getLocalizedMessage());
        }
        
        System.out.println("Test "+testNumber+" - Finished");
    }
}
