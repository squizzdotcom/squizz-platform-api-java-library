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
 * Shows an example of creating a organisation session with the SQUIZZ.com platform's API, then imports Category data into the platform against a specified organisation
 */
public class APIv1ExampleRunnerImportOrgESDDataCategories
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
		
		//import category data if the API was successfully created
		if(apiOrgSession.sessionExists())
		{
			//create category records list
			ArrayList<ESDRecordCategory> categoryRecords = new ArrayList<>();

			//create first category record (a top tier category)
			ESDRecordCategory categoryRecord = new ESDRecordCategory();
			categoryRecord.keyCategoryID = "2";
			categoryRecord.categoryCode = "Home and Stationery";

			//add category record to the list of categories
			categoryRecords.add(categoryRecord);

			//create 2nd category record (child of the Home and Stationery category above)
			categoryRecord = new ESDRecordCategory();
			categoryRecord.keyCategoryID = "123";
			categoryRecord.categoryCode = "tables-chairs";
			categoryRecord.keyCategoryParentID = "2";
			categoryRecord.name = "Tables and Chairs";
			categoryRecord.description1 = "View our extensive range of tables and chairs";
			categoryRecord.description2 = "Range includes products from the ESD designers";
			categoryRecord.description3 = "";
			categoryRecord.description4 = "";
			categoryRecord.metaTitle = "Tables and Chairs From ESD Designers";
			categoryRecord.metaKeywords = "tables chairs esd furniture designers";
			categoryRecord.metaDescription = "Tables and chairs from the ESD designers";
			categoryRecord.ordering = 2;
			categoryRecord.keyProductIDs = new String[]{"TAB-1","53432","CHAIR-5"};

			//add 2nd category record to the list of categories
			categoryRecords.add(categoryRecord);

			//create 3rd category record (also a child of the Home and Stationery category above)
			categoryRecord = new ESDRecordCategory();
			categoryRecord.keyCategoryID = "124";
			categoryRecord.categoryCode = "paper";
			categoryRecord.keyCategoryParentID = "2";
			categoryRecord.name = "Paper Products";
			categoryRecord.description1 = "View our extensive range of paper";
			categoryRecord.description2 = "Range includes paper only sources from sustainable environments";
			categoryRecord.description3 = "";
			categoryRecord.description4 = "";
			categoryRecord.metaTitle = "Paper Products";
			categoryRecord.metaKeywords = "paper products environmental";
			categoryRecord.metaDescription = "Paper products from sustainable environments";
			categoryRecord.ordering = 1;
			categoryRecord.keyProductIDs = new String[]{"PROD-001","PROD-002"};

			//create 4th category record (used for make/model)
			categoryRecord = new ESDRecordCategory();
			categoryRecord.keyCategoryID = "CAR-TYRE";
			categoryRecord.categoryCode = "car-tyres";
			categoryRecord.name = "Car Tyres";
			categoryRecord.description1 = "View our extensive range of car types";
			categoryRecord.description2 = "Range includes car types of all types";
			categoryRecord.description3 = "";
			categoryRecord.description4 = "";
			categoryRecord.metaTitle = "Car Tyres";
			categoryRecord.metaKeywords = "Car Tyres Rubber Premium";
			categoryRecord.metaDescription = "Premium rubber car tyres";
			categoryRecord.ordering = 4;
			categoryRecord.keyProductIDs = new String[]{"CAR-TYRE-CHEAP","CAR-TYRE-LONG-LASTING"};

			//add 3rd category record to the list of categories
			categoryRecords.add(categoryRecord);
            
            //create a hashmap containing configurations of the organisation category data
            HashMap<String, String> configs = new HashMap<>();
            
            //add a dataFields attribute that contains a comma delimited list of category record fields that the API is allowed to insert and update in the platform
            configs.put("dataFields", "keyCategoryID,categoryCode,keyCategoryParentID,name,description1,description2,description3,description4,metaTitle,metaKeywords,metaDescription,ordering,keyProductIDs");
            
            //create a Ecommerce Standards Document that stores an array of category records
            ESDocumentCategory categoryESD = new ESDocumentCategory(ESDocumentConstants.RESULT_SUCCESS, "successfully obtained data", categoryRecords.toArray(new ESDRecordCategory[0]), configs);
			
			//after 30 seconds give up on waiting for a response from the API when creating the notification
			int timeoutMilliseconds = 30000;
			
			//call the platform's API to import in the organisation's category data
			APIv1EndpointResponseESD endpointResponseESD = APIv1EndpointOrgImportESDocument.call(apiOrgSession, timeoutMilliseconds, APIv1EndpointOrgImportESDocument.IMPORT_TYPE_ID_CATEGORIES, categoryESD);
			
			//check that the data successfully imported
			if(endpointResponseESD.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS)){
                System.out.println("SUCCESS - category data successfully imported into the platform against the organisation");
            }else{
                System.out.println("FAIL - category data failed to be imported into the platform against the organisation. Reason: " + endpointResponseESD.result_message  + " Error Code: " + endpointResponseESD.result_code);
            }
		}
		
		//next steps
		//call other API endpoints...
		//destroy API session when done
		apiOrgSession.destroyOrgSession();
    }
}
