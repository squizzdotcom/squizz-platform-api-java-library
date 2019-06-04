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
 * Shows an example of creating a organisation session with the SQUIZZ.com platform's API, then imports Maker Model Mapping data into the platform against a specified organisation
 */
public class APIv1ExampleRunnerImportOrgESDDataMakerModelMappings
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
		
		//import make model mapping data if the API was successfully created
		if(apiOrgSession.sessionExists())
		{
			//create maker model mapping records
			ArrayList<ESDRecordMakerModelMapping> makerModelMappingRecords = new ArrayList<>();
			ESDRecordMakerModelMapping makerModelMappingRecord = new ESDRecordMakerModelMapping();
			makerModelMappingRecord.keyMakerModelID = "2";
			makerModelMappingRecord.keyCategoryID = "CAR-TYRE";
			makerModelMappingRecord.keyProductID = "CAR-TYRE-LONG-LASTING";
			makerModelMappingRecord.quantity = 4;
			makerModelMappingRecord.attributes = new ArrayList<>();

			//add attribute value records against the model mapping record
			ESDRecordAttributeValue attributeValueRecord = new ESDRecordAttributeValue();
			attributeValueRecord.keyAttributeProfileID = "MAKEMODELCAR";
			attributeValueRecord.keyAttributeID = "MMCAR-WHEELSIZE-RADIUS-INCH";
			attributeValueRecord.numberValue = 21;
			makerModelMappingRecord.attributes.add(attributeValueRecord);

			//add 2nd attribute value to the model mapping
			attributeValueRecord = new ESDRecordAttributeValue();
			attributeValueRecord.keyAttributeProfileID = "MAKEMODELCAR";
			attributeValueRecord.keyAttributeID = "MMCAR-WHEELSIZE-TREAD";
			attributeValueRecord.stringValue = "All Weather";
			makerModelMappingRecord.attributes.add(attributeValueRecord);

			//add mapping record to the list of model mappings
			makerModelMappingRecords.add(makerModelMappingRecord);

			//create 2nd maker model mapping record
			makerModelMappingRecord = new ESDRecordMakerModelMapping();
			makerModelMappingRecord.keyMakerModelID = "2";
			makerModelMappingRecord.keyCategoryID = "CAR-TYRE";
			makerModelMappingRecord.keyProductID = "CAR-TYRE-CHEAP";
			makerModelMappingRecord.quantity = 4;
			makerModelMappingRecord.attributes = new ArrayList<>();

			//add attribute value records against the model mapping record
			attributeValueRecord = new ESDRecordAttributeValue();
			attributeValueRecord.keyAttributeProfileID = "MAKEMODELCAR";
			attributeValueRecord.keyAttributeID = "MMCAR-WHEELSIZE-RADIUS-INCH";
			attributeValueRecord.numberValue = 20;
			makerModelMappingRecord.attributes.add(attributeValueRecord);

			//add 2nd attribute value to the model mapping
			attributeValueRecord = new ESDRecordAttributeValue();
			attributeValueRecord.keyAttributeProfileID = "MAKEMODELCAR";
			attributeValueRecord.keyAttributeID = "MMCAR-WHEELSIZE-TREAD";
			attributeValueRecord.stringValue = "BITUMEN";
			makerModelMappingRecord.attributes.add(attributeValueRecord);

			//add 2nd mapping record to the list of model mappings
			makerModelMappingRecords.add(makerModelMappingRecord);
            
            //create a hashmap containing configurations of the organisation maker model mapping data
            HashMap<String, String> configs = new HashMap<>();
            
            //add a dataFields attribute that contains a comma delimited list of maker model mapping record fields that the API is allowed to insert and update in the platform
            configs.put("dataFields", "keyMakerModelID,keyCategoryID,keyProductID,quantity,attributes");
            
            //create a Ecommerce Standards Document that stores an array of maker model records
            ESDocumentMakerModelMapping makerModelMappingESD = new ESDocumentMakerModelMapping(ESDocumentConstants.RESULT_SUCCESS, "successfully obtained data", makerModelMappingRecords.toArray(new ESDRecordMakerModelMapping[0]), configs);
			
			//after 30 seconds give up on waiting for a response from the API when creating the notification
			int timeoutMilliseconds = 30000;
			
			//call the platform's API to import in the organisation's maker model mapping data
			APIv1EndpointResponseESD endpointResponseESD = APIv1EndpointOrgImportESDocument.call(apiOrgSession, timeoutMilliseconds, APIv1EndpointOrgImportESDocument.IMPORT_TYPE_ID_MAKER_MODEL_MAPPINGS, makerModelMappingESD);
			
			//check that the data successfully imported
			if(endpointResponseESD.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS)){
                System.out.println("SUCCESS - maker model mapping data successfully imported into the platform against the organisation");
            }else{
                System.out.println("FAIL - maker model mapping data failed to be imported into the platform against the organisation. Reason: " + endpointResponseESD.result_message  + " Error Code: " + endpointResponseESD.result_code);
            }
		}
		
		//next steps
		//call other API endpoints...
		//destroy API session when done
		apiOrgSession.destroyOrgSession();
    }
}
