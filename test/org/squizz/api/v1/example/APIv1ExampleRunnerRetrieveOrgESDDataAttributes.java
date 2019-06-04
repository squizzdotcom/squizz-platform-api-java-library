/**
* Copyright (C) 2019 Squizz PTY LTD
* This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
*/
package org.squizz.api.v1.example;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import org.squizz.api.v1.endpoint.APIv1EndpointOrgRetrieveESDocument;
import org.squizz.api.v1.*;
import org.squizz.api.v1.endpoint.*;
import org.esd.EcommerceStandardsDocuments.*;

/**
 * Shows an example of creating a organisation session with the SQUIZZ.com platform's API, then retrieves Attribute data from a connected organisation in the platform
 */
public class APIv1ExampleRunnerRetrieveOrgESDDataAttributes
{
    public static void main(String[] args)
    {
        //check that the required arguments have been given
        if(args.length < 4){
            System.out.println("Set the following arguments: [orgID] [orgAPIKey] [orgAPIPass] [supplierOrgID]");
            return;
        }
        
		//obtain or load in an organisation's API credentials, in this example from command line arguments
		String orgID = args[0];
		String orgAPIKey = args[1];
		String orgAPIPass = args[2];
        int sessionTimeoutMilliseconds = 20000;
		int recordsMaxAmount = 5000;
		int recordsStartIndex = 0;
		boolean getMoreRecords = true;
		int recordNumber = 0;
		int attributeProfileRecordNumber = 1;
		int attributesRecordNumber = 1;
		int pageNumber = 0;
		Calendar calendar = Calendar.getInstance();
		String result = "FAIL";
		HashMap<String, ESDRecordAttributeProfile> attributeProfilesRecordIndex = new HashMap<>();
		HashMap<String, ESDRecordAttribute> attributesRecordIndex = new HashMap<>();
		HashMap<String, ESDRecordProduct> productsRecordIndex = new HashMap<>();
		
		//specify the supplier organisation to get data from based on its ID within the platform, in this example the ID comes from command line arguments
		String supplierOrgID = args[3];
		
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
		
		//retrieve product data, since it is used to find details of products associated to retrieved attribute values
		if(apiOrgSession.sessionExists())
		{
			System.out.println("Attempting to obtain product data.");
			recordsStartIndex = 0;
			pageNumber = 0;
			getMoreRecords = true;
			
			//after 120 seconds give up on waiting for a response from the API
			int timeoutMilliseconds = 120000;

			//get the next page of records if needed
			while(getMoreRecords)
			{
				pageNumber++;
				getMoreRecords = false;
				
				//call the platform's API to retrieve the organisation's product data
				APIv1EndpointResponseESD endpointResponseESD = APIv1EndpointOrgRetrieveESDocument.call(apiOrgSession, timeoutMilliseconds, APIv1EndpointOrgRetrieveESDocument.RETRIEVE_TYPE_ID_PRODUCTS, supplierOrgID, "", recordsMaxAmount, recordsStartIndex, "");

				System.out.println("Attempt made to obtain product data. Page number: "+pageNumber);

				//check that the data successfully retrieved
				if(endpointResponseESD.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS))
				{	
					ESDocumentProduct esDocumentProduct = (ESDocumentProduct)endpointResponseESD.esDocument;
					
					//check that records have been placed into the standards document
					if(esDocumentProduct.dataRecords != null)
					{
						//iterate through each product record stored within the standards document
						for (ESDRecordProduct productRecord: esDocumentProduct.dataRecords){
							productsRecordIndex.putIfAbsent(productRecord.keyProductID, productRecord);
						}

						//check if there are more records to retrieve
						if(esDocumentProduct.dataRecords.length >= recordsMaxAmount)
						{
							recordsStartIndex = recordsStartIndex + recordsMaxAmount;
							getMoreRecords = true;
						}
					}else{
						System.out.println("No more records obtained. Page number: "+pageNumber);
					}
					
					result = "SUCCESS";
				}else{
					result = "FAIL";
					System.out.println("FAIL - not all organisation product data could be obtained from the platform. Reason: " + endpointResponseESD.result_message  + " Error Code: " + endpointResponseESD.result_code);
				}
			}
		}
		
		//retrieve attribute data if a session was successfully created
		if(result.equals("SUCCESS"))
		{
			System.out.println("Attempting to obtain attribute data.");
			recordsStartIndex = 0;
			getMoreRecords = true;
			
			//after 120 seconds give up on waiting for a response from the API
			int timeoutMilliseconds = 120000;

			//get the next page of records if needed
			while(getMoreRecords)
			{
				getMoreRecords = false;
				pageNumber++;
				
				//call the platform's API to retrieve the organisation's attribute data
				APIv1EndpointResponseESD endpointResponseESD = APIv1EndpointOrgRetrieveESDocument.call(apiOrgSession, timeoutMilliseconds, APIv1EndpointOrgRetrieveESDocument.RETRIEVE_TYPE_ID_ATTRIBUTES, supplierOrgID, "", recordsMaxAmount, recordsStartIndex, "");
				System.out.println("SUCCESS - attribute data successfully obtained from the platform for page number: "+pageNumber);

				//check that the data successfully retrieved
				if(endpointResponseESD.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS))
				{
					ESDocumentAttribute esDocumentAttribute = (ESDocumentAttribute)endpointResponseESD.esDocument;

					//check that attribute profile records have been placed into the standards document
					if(recordsStartIndex == 0 && esDocumentAttribute.attributeProfiles != null)
					{
						//iterate through each attribute record stored within the standards document
						for (ESDRecordAttributeProfile attributeProfileRecord: esDocumentAttribute.attributeProfiles){
							attributeProfilesRecordIndex.putIfAbsent(attributeProfileRecord.keyAttributeProfileID, attributeProfileRecord);
							
							System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
							System.out.println("Key Attribute Profile ID: " + attributeProfileRecord.keyAttributeProfileID);
							System.out.println("            Profile Name: " + attributeProfileRecord.name);

							//iterate through each attribute assigned to the attribute profile
							for (ESDRecordAttribute attributeRecord: attributeProfileRecord.attributes){
								attributesRecordIndex.putIfAbsent(attributeRecord.keyAttributeID, attributeRecord);
								
								System.out.println();
								System.out.println("        Key Attribute ID: " + attributeRecord.keyAttributeID);
								System.out.println("          Attribute Name: " + attributeRecord.name);
								System.out.println("     Attribute Data Type: " + attributeRecord.dataType);
							}
							
							System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
						}
					}else{
						System.out.println("No more records obtained.");
					}
					
					//check that product attribute value records have been placed into the standards document
					if(esDocumentAttribute.dataRecords != null)
					{
						//iterate through each attribute value record stored within the standards document
						for (ESDRecordAttributeValue attributeValueRecord: esDocumentAttribute.dataRecords)
						{
							String attributeValue;
							String productName = "";
							String productCode = "";
							String attributeName = "";
							String attributeProfileName = "";
							String attributeDataType = "";

							//lookup the product assigned to the attribute value and gets its name (or any other product details you wish)
							if(productsRecordIndex.containsKey(attributeValueRecord.keyProductID)){
								productCode = productsRecordIndex.get(attributeValueRecord.keyProductID).productCode;
								productName = productsRecordIndex.get(attributeValueRecord.keyProductID).name;
							}

							//get details of the attribute profile the attribute value is linked to
							if(attributeProfilesRecordIndex.containsKey(attributeValueRecord.keyAttributeProfileID)){
								attributeProfileName = attributeProfilesRecordIndex.get(attributeValueRecord.keyAttributeProfileID).name;
							}

							//get details of the attribute that the attribute value is linked to
							if(attributesRecordIndex.containsKey(attributeValueRecord.keyAttributeID)){
								attributeName = attributesRecordIndex.get(attributeValueRecord.keyAttributeID).name;
								attributeDataType = attributesRecordIndex.get(attributeValueRecord.keyAttributeID).dataType;
							}

							// get the attribute value based on the attribute's data type
							if(attributeDataType.equalsIgnoreCase(ESDRecordAttribute.DATA_TYPE_NUMBER)){
								attributeValue = Double.toString(attributeValueRecord.numberValue);
							}else{
								attributeValue = attributeValueRecord.stringValue;
							}
							
							//output details of the product attribute value record
							System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
							System.out.println("Product Attribute Value Record #: " + recordNumber);
							System.out.println("                  Key Product ID: " + attributeValueRecord.keyProductID);
							System.out.println("                    Product Code: " + productCode);
							System.out.println("                    Product Name: " + productName);
							System.out.println("        Key Attribute Profile ID: " + attributeValueRecord.keyAttributeProfileID);
							System.out.println("                    Profile Name: " + attributeProfileName);
							System.out.println("                Key Attribute ID: " + attributeValueRecord.keyAttributeID);
							System.out.println("                  Attribute Name: " + attributeName);
							System.out.println("                 Attribute Value: " + attributeValue);

							recordNumber++;
						}

						//check if there are more records to retrieve
						if(esDocumentAttribute.dataRecords.length >= recordsMaxAmount)
						{
							recordsStartIndex = recordsStartIndex + recordsMaxAmount;
							getMoreRecords = true;
						}
					}

					result = "SUCCESS";
				}else{
					result = "FAIL";
					System.out.println("FAIL - not all organisation attribute data could be obtained from the platform. Reason: " + endpointResponseESD.result_message  + " Error Code: " + endpointResponseESD.result_code);
				}
			}
		}
		
		//next steps
		//call other API endpoints...
		//destroy API session when done...
		apiOrgSession.destroyOrgSession();
    }
}
