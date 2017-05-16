/**
* Copyright (C) 2017 Squizz PTY LTD
* This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
*/
package org.squizz.api.v1.example;

import org.squizz.api.v1.endpoint.APIv1EndpointOrgRetrieveESDocument;
import org.squizz.api.v1.*;
import org.squizz.api.v1.endpoint.*;
import org.esd.EcommerceStandardsDocuments.*;

/**
 * Shows an example of creating a organisation session with the SQUIZZ.com platform's API, then retrieves Ecommerce dataa from a conencted organisation in the platform
 */
public class APIv1ExampleRunnerRetrieveOrgESDData 
{
    public static void main(String[] args)
    {
        //check that the required arguments have been given
        if(args.length < 4){
            System.out.println("Set the following arguments: [orgID] [orgAPIKey] [orgAPIPass] [supplierOrgID] [(optional)customerAccountCode]");
            return;
        }
        
		//obtain or load in an organisation's API credentials, in this example from command line arguments
		String orgID = args[0];
		String orgAPIKey = args[1];
		String orgAPIPass = args[2];
        int sessionTimeoutMilliseconds = 20000;
		
		//create an API session instance
		APIv1OrgSession apiOrgSession = new APIv1OrgSession(orgID, orgAPIKey, orgAPIPass, sessionTimeoutMilliseconds, APIv1Constants.SUPPORTED_LOCALES_EN_AU);
		
		//call the platform's API to request that a session is created
		APIv1EndpointResponse endpointResponse = apiOrgSession.createOrgSession();
		
		//check if the organisation's credentials were correct and that a session was created in the platform's API
		if(endpointResponse.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS))
		{
			//session has been created so now can call other API endpoints
			System.out.println("SUCCESS - API session has successfully been created.");
		}
		else
		{
			//session failed to be created
			System.out.println("FAIL - API session failed to be created. Reason: " + endpointResponse.result_message  + " Error Code: " + endpointResponse.result_code);
		}
		
		//import organisation data if the API was successfully created
		if(apiOrgSession.sessionExists())
		{   
			//after 60 seconds give up on waiting for a response from the API when creating the notification
			int timeoutMilliseconds = 60000;
            
            //specify the supplier organisation to get data from based on its ID within the platform, in this example the ID comes from command line arguments
            String supplierOrgID = args[3];
            
            //optionally get the customer account code if required based on the type of data being retrived, in this example the account code comes from command line arguments
            String customerAccountCode = (args.length > 4? args[4]: "");
			
			//call the platform's API to import in the organisation's data, which for this example is product pricing
			APIv1EndpointResponseESD endpointResponseESD = APIv1EndpointOrgRetrieveESDocument.call(apiOrgSession, timeoutMilliseconds, APIv1EndpointOrgRetrieveESDocument.RETRIEVE_TYPE_ID_PRICING, supplierOrgID, customerAccountCode);
            ESDocumentPrice esDocumentPrice = (ESDocumentPrice)endpointResponseESD.esDocument;
			
			//check that the data successfully imported
			if(endpointResponseESD.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS)){
                System.out.println("SUCCESS - organisation data successfully obtained from the platform");
                System.out.println("\nPrice Records Returned: " + esDocumentPrice.totalDataRecords);
                
                //check that records have been placed into the standards document
                if(esDocumentPrice.dataRecords != null){
                    System.out.println("Price Records:");
                    
                    //iterate through each price record stored within the standards document
                    int i=0;
                    for(ESDRecordPrice priceRecord: esDocumentPrice.dataRecords)
                    {    
                        //output details of the price record
                        System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
                        System.out.println("  Price Record #: " + i);
                        System.out.println("  Key Product ID: " + priceRecord.keyProductID);
                        System.out.println("           Price: " + priceRecord.price);
                        System.out.println("        Quantity: " + priceRecord.quantity);
                        if(priceRecord.taxRate != 0){
                            System.out.println("      Tax Rate: " + priceRecord.taxRate);
                        }
                        System.out.println("Key Sell Unit ID: " + priceRecord.keySellUnitID);
                        System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
                                
                        i++;
                    }
                }
            }else{
                System.out.println("FAIL - organisation data failed to be obtained from the platform. Reason: " + endpointResponseESD.result_message  + " Error Code: " + endpointResponseESD.result_code);
            }
		}
		
		//next steps
		//call other API endpoints...
		//destroy API session when done...
    }
}
