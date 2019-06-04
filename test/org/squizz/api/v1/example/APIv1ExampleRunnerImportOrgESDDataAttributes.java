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
 * Shows an example of creating a organisation session with the SQUIZZ.com platform's API, then imports Attribute data into the platform against a specified organisation
 */
public class APIv1ExampleRunnerImportOrgESDDataAttributes
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
		
		//import attribute data if the API was successfully created
		if(apiOrgSession.sessionExists())
		{
			//create attribute profile records
			ArrayList<ESDRecordAttributeProfile> attributeProfileRecords = new ArrayList<>();

			//create first attribute profile record
			ESDRecordAttributeProfile attributeProfileRecord = new ESDRecordAttributeProfile();
			attributeProfileRecord.keyAttributeProfileID = "PAP002";
			attributeProfileRecord.name = "Clothing Styling";
			attributeProfileRecord.description = "View the styling details of clothes";
			attributeProfileRecords.add(attributeProfileRecord);
			ArrayList<ESDRecordAttribute> attributes = new ArrayList<>();

			//add attribute record to the 1st attribute profile
			ESDRecordAttribute attributeRecord = new ESDRecordAttribute();
			attributeRecord.keyAttributeID = "PAP002-1";
			attributeRecord.name = "Colour";
			attributeRecord.dataType = ESDRecordAttribute.DATA_TYPE_STRING;
			attributes.add(attributeRecord);

			//add 2nd attribute record to the 1st attribute profile
			attributeRecord = new ESDRecordAttribute();
			attributeRecord.keyAttributeID = "PAP002-2";
			attributeRecord.name = "Size";
			attributeRecord.dataType = ESDRecordAttribute.DATA_TYPE_NUMBER;
			attributes.add(attributeRecord);

			//add 3rd attribute record to the 1st attribute profile
			attributeRecord = new ESDRecordAttribute();
			attributeRecord.keyAttributeID = "PAP002-3";
			attributeRecord.name = "Texture";
			attributeRecord.dataType = ESDRecordAttribute.DATA_TYPE_STRING;
			attributes.add(attributeRecord);
			
			//add list of attributes to the attribute profile
			attributeProfileRecord.attributes = attributes.toArray(new ESDRecordAttribute[0]);
			attributes.clear();

			//create 2nd attribute profile record
			attributeProfileRecord = new ESDRecordAttributeProfile();
			attributeProfileRecord.keyAttributeProfileID = "MAKEMODELCAR";
			attributeProfileRecord.name = "Make/Model Vehicle Details";
			attributeProfileRecord.description = "Details about the characteristics of automotive vehicles";
			attributeProfileRecords.add(attributeProfileRecord);

			//add 1st attribute record to the 2nd attribute profile
			attributeRecord = new ESDRecordAttribute();
			attributeRecord.keyAttributeID = "MMCAR-TYPE";
			attributeRecord.name = "Vehicle Type";
			attributeRecord.dataType = ESDRecordAttribute.DATA_TYPE_STRING;
			attributes.add(attributeRecord);

			//add 2nd attribute record to the 2nd attribute profile
			attributeRecord = new ESDRecordAttribute();
			attributeRecord.keyAttributeID = "MMCAR-ENGINE-CYLINDERS";
			attributeRecord.name = "Number of Cyclinders";
			attributeRecord.dataType = ESDRecordAttribute.DATA_TYPE_NUMBER;
			attributes.add(attributeRecord);

			//add 3rd attribute record to the 2nd attribute profile
			attributeRecord = new ESDRecordAttribute();
			attributeRecord.keyAttributeID = "MMCAR-FUEL-TANK-LITRES";
			attributeRecord.name = "Fuel Tank Size (Litres)";
			attributeRecord.dataType = ESDRecordAttribute.DATA_TYPE_NUMBER;
			attributes.add(attributeRecord);

			//add 4th attribute record to the 2nd attribute profile
			attributeRecord = new ESDRecordAttribute();
			attributeRecord.keyAttributeID = "MMCAR-WHEELSIZE-RADIUS-INCH";
			attributeRecord.name = "Wheel Size (Inches)";
			attributeRecord.dataType = ESDRecordAttribute.DATA_TYPE_NUMBER;
			attributes.add(attributeRecord);

			//add 5th attribute record to the 2nd attribute profile
			attributeRecord = new ESDRecordAttribute();
			attributeRecord.keyAttributeID = "MMCAR-WHEELSIZE-TREAD";
			attributeRecord.name = "Tyre Tread";
			attributeRecord.dataType = ESDRecordAttribute.DATA_TYPE_STRING;
			attributes.add(attributeRecord);
			
			//add the list of attributes to the 2nd attribute profile
			attributeProfileRecord.attributes = attributes.toArray(new ESDRecordAttribute[0]);
			attributes.clear();

			//create product attribute values array
			ArrayList<ESDRecordAttributeValue> productAttributeValueRecords = new ArrayList<>();

			//set attribute values for products, for product PROD-001 set the clothing attributes colour=red, size=8 and 10, texture = soft
			ESDRecordAttributeValue attributeValueRecord = new ESDRecordAttributeValue();
			attributeValueRecord.keyProductID = "PROD-001";
			attributeValueRecord.keyAttributeProfileID = "PAP002";
			attributeValueRecord.keyAttributeID = "PAP002-1";
			attributeValueRecord.stringValue = "Red";
			productAttributeValueRecords.add(attributeValueRecord);

			attributeValueRecord = new ESDRecordAttributeValue();
			attributeValueRecord.keyProductID = "PROD-001";
			attributeValueRecord.keyAttributeProfileID = "PAP002";
			attributeValueRecord.keyAttributeID = "PAP002-2";
			attributeValueRecord.numberValue = 8;
			productAttributeValueRecords.add(attributeValueRecord);

			attributeValueRecord = new ESDRecordAttributeValue();
			attributeValueRecord.keyProductID = "PROD-001";
			attributeValueRecord.keyAttributeProfileID = "PAP002";
			attributeValueRecord.keyAttributeID = "PAP002-2";
			attributeValueRecord.numberValue = 10;
			productAttributeValueRecords.add(attributeValueRecord);

			attributeValueRecord = new ESDRecordAttributeValue();
			attributeValueRecord.keyProductID = "PROD-001";
			attributeValueRecord.keyAttributeProfileID = "PAP002";
			attributeValueRecord.keyAttributeID = "PAP002-3";
			attributeValueRecord.stringValue = "soft";
			productAttributeValueRecords.add(attributeValueRecord);
            
            //create a hashmap containing configurations of the organisation attribute data
            HashMap<String, String> configs = new HashMap<>();
            
            //add a dataFields attribute that contains a comma delimited list of attribute record fields that the API is allowed to insert and update in the platform
            configs.put("dataFields", "keyProductID,keyAttributeProfileID,keyAttributeID,stringValue,numberValue");
            
            //create a Ecommerce Standards Document that stores an array of attribute records
            ESDocumentAttribute attributeESD = new ESDocumentAttribute(ESDocumentConstants.RESULT_SUCCESS, "successfully obtained data", attributeProfileRecords.toArray(new ESDRecordAttributeProfile[0]), productAttributeValueRecords.toArray(new ESDRecordAttributeValue[0]), configs);
			
			//after 30 seconds give up on waiting for a response from the API when creating the notification
			int timeoutMilliseconds = 30000;
			
			//call the platform's API to import in the organisation's attribute data
			APIv1EndpointResponseESD endpointResponseESD = APIv1EndpointOrgImportESDocument.call(apiOrgSession, timeoutMilliseconds, APIv1EndpointOrgImportESDocument.IMPORT_TYPE_ID_ATTRIBUTES, attributeESD);
			
			//check that the data successfully imported
			if(endpointResponseESD.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS)){
                System.out.println("SUCCESS - attribute data successfully imported into the platform against the organisation");
            }else{
                System.out.println("FAIL - attribute data failed to be imported into the platform against the organisation. Reason: " + endpointResponseESD.result_message  + " Error Code: " + endpointResponseESD.result_code);
            }
		}
		
		//next steps
		//call other API endpoints...
		//destroy API session when done
		apiOrgSession.destroyOrgSession();
    }
}
