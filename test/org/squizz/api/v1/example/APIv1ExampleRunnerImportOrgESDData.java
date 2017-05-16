/**
* Copyright (C) 2017 Squizz PTY LTD
* This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
*/
package org.squizz.api.v1.example;

import org.squizz.api.v1.*;
import org.squizz.api.v1.endpoint.*;
import org.esd.EcommerceStandardsDocuments.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Shows an example of creating a organisation session with the SQUIZZ.com platform's API, then imports organisation data into the platform
 */
public class APIv1ExampleRunnerImportOrgESDData 
{
	public static void main(String[] args)
    {
        //check that the required arguments have been given
        if(args.length < 3){
            System.out.println("Set the following arguments: [orgID] [orgAPIKey] [orgAPIPass]");
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
			//create taxcode records
            ArrayList<ESDRecordTaxcode> taxcodeRecords = new ArrayList<>();
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
            taxcodeRecord.description = "Free from Any Taxes";
            taxcodeRecord.taxcodePercentageRate = 0;
            taxcodeRecords.add(taxcodeRecord);
            
            taxcodeRecord = new ESDRecordTaxcode();
            taxcodeRecord.keyTaxcodeID = "3";
            taxcodeRecord.taxcode = "NZGST";
            taxcodeRecord.taxcodeLabel = "New Zealand GST Tax";
            taxcodeRecord.description = "New Zealand Goods and Services Tax";
            taxcodeRecord.taxcodePercentageRate = 15;
            taxcodeRecords.add(taxcodeRecord);
            
            //create a hashmap containing configurations of the organisation taxcode data
            HashMap<String, String> configs = new HashMap<>();
            
            //add a dataFields attribute that contains a comma delimited list of tacode record fields that the API is allowed to insert, update in the platform
            configs.put("dataFields", "keyTaxcodeID,taxcode,taxcodeLabel,description,taxcodePercentageRate");
            
            //create a Ecommerce Standards Document that stores an array of taxcode records
            ESDocumentTaxcode taxcodeESD = new ESDocumentTaxcode(ESDocumentConstants.RESULT_SUCCESS, "successfully obtained data", taxcodeRecords.toArray(new ESDRecordTaxcode[0]), configs);
			
			//after 30 seconds give up on waiting for a response from the API when creating the notification
			int timeoutMilliseconds = 30000;
			
			//call the platform's API to import in the organisation's data
			APIv1EndpointResponseESD endpointResponseESD = APIv1EndpointOrgImportESDocument.call(apiOrgSession, timeoutMilliseconds, APIv1EndpointOrgImportESDocument.IMPORT_TYPE_ID_TAXCODES, taxcodeESD);
			
			//check that the data successfully imported
			if(endpointResponseESD.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS)){
                System.out.println("SUCCESS - organisation data successfully imported into the platform");
            }else{
                System.out.println("FAIL - organisation data failed to be imported into the platform. Reason: " + endpointResponseESD.result_message  + " Error Code: " + endpointResponseESD.result_code);
            }
		}
		
		//next steps
		//call other API endpoints...
		//destroy API session when done...
    }
}
