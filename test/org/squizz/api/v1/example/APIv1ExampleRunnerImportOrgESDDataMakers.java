/**
* Copyright (C) 2019 Squizz PTY LTD
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
 * Shows an example of creating a organisation session with the SQUIZZ.com platform's API, then imports Maker data into the platform against a specified organisation
 */
public class APIv1ExampleRunnerImportOrgESDDataMakers
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
		
		//import make data if the API was successfully created
		if(apiOrgSession.sessionExists())
		{
			//create maker records
			ArrayList<ESDRecordMaker> makerRecords = new ArrayList<>();
			
			//create 1st maker record
			ESDRecordMaker makerRecord = new ESDRecordMaker();
			makerRecord.keyMakerID = "1";
			makerRecord.makerCode = "CAR1";
			makerRecord.name = "Car Manufacturer X";
			makerRecord.makerSearchCode = "Car-Manufacturer-X-Sedans-Wagons-Trucks";
			makerRecord.groupClass = "POPULAR CARS";
			makerRecord.ordering = 3;
			makerRecord.establishedDate = 1449132083087l;
			makerRecord.orgName = "Car Manufacturer X";
			makerRecord.authorityNumbers = new String[]{"988776643221"};
			makerRecord.authorityNumberLabels = new String[]{"Australian Business Number"};
			makerRecord.authorityNumberTypes = new int[]{ESDocumentConstants.AUTHORITY_NUM_AUS_ABN};
			makerRecords.add(makerRecord);

			//add 2nd maker record
			makerRecord = new ESDRecordMaker();
			makerRecord.keyMakerID = "2";
			makerRecord.makerCode = "CAR2";
			makerRecord.name = "Car Manufacturer A";
			makerRecord.makerSearchCode = "Car-Manufacturer-A";
			makerRecord.groupClass = "POPULAR CARS";
			makerRecord.ordering = 2;
			makerRecord.establishedDate = 1449132083084l;
			makerRecord.orgName = "Car Manufacturer A";
			makerRecord.authorityNumbers = new String[]{"123456789 1234"};
			makerRecord.authorityNumberLabels = new String[]{"Australian Business Number"};
			makerRecord.authorityNumberTypes = new int[]{ESDocumentConstants.AUTHORITY_NUM_AUS_ABN};
			makerRecords.add(makerRecord);

			//add 3rd maker record
			makerRecord = new ESDRecordMaker();
			makerRecord.keyMakerID = "3";
			makerRecord.makerCode = "CAR3";
			makerRecord.name = "Car Manufacturer B";
			makerRecord.makerSearchCode = "Car-Manufacturer-B-Sedans-Wagons";
			makerRecord.groupClass = "CUSTOM CARS";
			makerRecord.ordering = 1;
			makerRecord.establishedDate = 1449132083085l;
			makerRecord.orgName = "Car Manufacturer B";
			makerRecord.authorityNumbers = new String[]{"98877664322"};
			makerRecord.authorityNumberLabels = new String[]{"Australian Business Number"};
			makerRecord.authorityNumberTypes = new int[]{ESDocumentConstants.AUTHORITY_NUM_AUS_ABN};
			makerRecords.add(makerRecord);
            
            //create a hashmap containing configurations of the organisation maker data
            HashMap<String, String> configs = new HashMap<>();
            
            //add a dataFields attribute that contains a comma delimited list of maker record fields that the API is allowed to insert and update in the platform
            configs.put("dataFields", "keyMakerID,makerCode,name,makerSearchCode,groupClass,ordering,establishedDate,orgName,authorityNumbers,authorityNumberLabels,authorityNumberTypes");
            
            //create a Ecommerce Standards Document that stores an array of maker records
            ESDocumentMaker makerESD = new ESDocumentMaker(ESDocumentConstants.RESULT_SUCCESS, "successfully obtained data", makerRecords.toArray(new ESDRecordMaker[0]), configs);
			
			//after 30 seconds give up on waiting for a response from the API when creating the notification
			int timeoutMilliseconds = 30000;
			
			//call the platform's API to import in the organisation's maker data
			APIv1EndpointResponseESD endpointResponseESD = APIv1EndpointOrgImportESDocument.call(apiOrgSession, timeoutMilliseconds, APIv1EndpointOrgImportESDocument.IMPORT_TYPE_ID_MAKERS, makerESD);
			
			//check that the data successfully imported
			if(endpointResponseESD.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS)){
                System.out.println("SUCCESS - maker organisation data successfully imported into the platform");
            }else{
                System.out.println("FAIL - maker organisation data failed to be imported into the platform. Reason: " + endpointResponseESD.result_message  + " Error Code: " + endpointResponseESD.result_code);
            }
		}
		
		//next steps
		//call other API endpoints...
		//destroy API session when done
		apiOrgSession.destroyOrgSession();
    }
}
