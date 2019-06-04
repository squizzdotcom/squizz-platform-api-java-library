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
 * Shows an example of creating a organisation session with the SQUIZZ.com platform's API, then imports Maker Model data into the platform against a specified organisation
 */
public class APIv1ExampleRunnerImportOrgESDDataMakerModels
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
		
		//import make model data if the API was successfully created
		if(apiOrgSession.sessionExists())
		{
			//create maker model records
			ArrayList<ESDRecordMakerModel> makerModelRecords = new ArrayList<>();
			ESDRecordMakerModel makerModelRecord = new ESDRecordMakerModel();
			makerModelRecord.keyMakerModelID = "2";
			makerModelRecord.keyMakerID = "2";
			makerModelRecord.modelCode = "SEDAN1";
			makerModelRecord.modelSubCode = "1ABC";
			makerModelRecord.name = "Sahara Luxury Sedan 2016";
			makerModelRecord.modelSearchCode = "Car-Manufacturer-A-Saraha-Luxury-Sedan-2016";
			makerModelRecord.groupClass = "SEDAN";
			makerModelRecord.releasedDate = 1456750800000l;
			makerModelRecord.createdDate = 1430748000000l;
			makerModelRecord.attributes = new ArrayList<>();

			//add attribute value records against the model record
			ESDRecordAttributeValue attributeValueRecord = new ESDRecordAttributeValue();
			attributeValueRecord.keyAttributeProfileID = "MAKEMODELCAR";
			attributeValueRecord.keyAttributeID = "MMCAR-TYPE";
			attributeValueRecord.stringValue = "Sedan";
			makerModelRecord.attributes.add(attributeValueRecord);

			//add 2nd attribute value to the model
			attributeValueRecord = new ESDRecordAttributeValue();
			attributeValueRecord.keyAttributeProfileID = "MAKEMODELCAR";
			attributeValueRecord.keyAttributeID = "MMCAR-ENGINE-CYLINDERS";
			attributeValueRecord.numberValue = 4;
			makerModelRecord.attributes.add(attributeValueRecord);

			//add 3rd attribute value to the model
			attributeValueRecord = new ESDRecordAttributeValue();
			attributeValueRecord.keyAttributeProfileID = "MAKEMODELCAR";
			attributeValueRecord.keyAttributeID = "MMCAR-FUEL-TANK-LITRES";
			attributeValueRecord.numberValue = 80.5;
			makerModelRecord.attributes.add(attributeValueRecord);

			//add model record to the list of models
			makerModelRecords.add(makerModelRecord);

			//create 2nd maker model record
			makerModelRecord = new ESDRecordMakerModel();
			makerModelRecord.keyMakerModelID = "3";
			makerModelRecord.keyMakerID = "2";
			makerModelRecord.modelCode = "TRUCK22";
			makerModelRecord.modelSubCode = "EX";
			makerModelRecord.name = "City Truck 2016";
			makerModelRecord.modelSearchCode = "Car-Manufacturer-A-City-Truck-2016";
			makerModelRecord.groupClass = "TRUCK";
			makerModelRecord.releasedDate = 1456750800000l;
			makerModelRecord.createdDate = 1430748000000l;
			makerModelRecord.attributes  = new ArrayList<>();

			//add attribute value records against the model record
			attributeValueRecord = new ESDRecordAttributeValue();
			attributeValueRecord.keyAttributeProfileID = "MAKEMODELCAR";
			attributeValueRecord.keyAttributeID = "MMCAR-TYPE";
			attributeValueRecord.stringValue = "Truck";
			makerModelRecord.attributes.add(attributeValueRecord);

			//add 2nd attribute value to the model
			attributeValueRecord = new ESDRecordAttributeValue();
			attributeValueRecord.keyAttributeProfileID = "MAKEMODELCAR";
			attributeValueRecord.keyAttributeID = "MMCAR-ENGINE-CYLINDERS";
			attributeValueRecord.numberValue = 6;
			makerModelRecord.attributes.add(attributeValueRecord);

			//add 3rd attribute value to the model
			attributeValueRecord = new ESDRecordAttributeValue();
			attributeValueRecord.keyAttributeProfileID = "MAKEMODELCAR";
			attributeValueRecord.keyAttributeID = "MMCAR-FUEL-TANK-LITRES";
			attributeValueRecord.numberValue = 140;
			makerModelRecord.attributes.add(attributeValueRecord);
			
			//add the 2nd model record to the list of models
			makerModelRecords.add(makerModelRecord);
			
            
            //create a hashmap containing configurations of the organisation maker model data
            HashMap<String, String> configs = new HashMap<>();
            
            //add a dataFields attribute that contains a comma delimited list of maker model record fields that the API is allowed to insert and update in the platform
            configs.put("dataFields", "keyMakerModelID,keyMakerID,modelCode,modelSubCode,name,modelSearchCode,groupClass,releasedDate,createdDate,attributes");
            
            //create a Ecommerce Standards Document that stores an array of maker model records
            ESDocumentMakerModel makerModelESD = new ESDocumentMakerModel(ESDocumentConstants.RESULT_SUCCESS, "successfully obtained data", makerModelRecords.toArray(new ESDRecordMakerModel[0]), configs);
			
			//after 30 seconds give up on waiting for a response from the API when creating the notification
			int timeoutMilliseconds = 30000;
			
			//call the platform's API to import in the organisation's maker model data
			APIv1EndpointResponseESD endpointResponseESD = APIv1EndpointOrgImportESDocument.call(apiOrgSession, timeoutMilliseconds, APIv1EndpointOrgImportESDocument.IMPORT_TYPE_ID_MAKER_MODELS, makerModelESD);
			
			//check that the data successfully imported
			if(endpointResponseESD.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS)){
                System.out.println("SUCCESS - maker model data successfully imported into the platform against the organisation");
            }else{
                System.out.println("FAIL - maker model data failed to be imported into the platform against the organisation. Reason: " + endpointResponseESD.result_message  + " Error Code: " + endpointResponseESD.result_code);
            }
		}
		
		//next steps
		//call other API endpoints...
		//destroy API session when done
		apiOrgSession.destroyOrgSession();
    }
}
