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
import java.util.ArrayList;
import java.util.HashMap;

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
            APIv1EndpointResponse endpointResponse = APIv1EndpointOrgCreateNotification.call(apiOrgSession, timeoutMilliseconds, notifyCategory, message, linkURLs, linkLabels);
            
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
            APIv1EndpointResponse endpointResponse = APIv1EndpointOrgValidateSecurityCertificate.call(apiOrgSession, timeoutMilliseconds, orgSecurityCertificateID);
            
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
            
            //System.out.println("Enter Security Certificate ID:");
            //String orgSecurityCertificateID = reader.readLine();
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
            
            //call API endpoint in the SQUIZZ.com platform to validate the organisation's security certificate
            System.out.println("Call API to import organisation data stored in an ESDocument");
            APIv1EndpointResponse endpointResponse = APIv1EndpointOrgImportESDocument.call(apiOrgSession, timeoutMilliseconds, APIv1EndpointOrgImportESDocument.IMPORT_TYPE_ID_TAXCODES, taxcodeESD);
            
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
}
