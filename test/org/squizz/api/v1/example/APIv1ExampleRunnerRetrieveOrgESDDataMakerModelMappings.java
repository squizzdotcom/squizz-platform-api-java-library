/**
* Copyright (C) 2019 Squizz PTY LTD
* This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
*/
package org.squizz.api.v1.example;

import java.util.Calendar;
import java.util.HashMap;
import org.squizz.api.v1.endpoint.APIv1EndpointOrgRetrieveESDocument;
import org.squizz.api.v1.*;
import org.squizz.api.v1.endpoint.*;
import org.esd.EcommerceStandardsDocuments.*;

/**
 * Shows an example of creating a organisation session with the SQUIZZ.com platform's API, then retrieves Maker Model Mapping data from a connected organisation in the platform
 */
public class APIv1ExampleRunnerRetrieveOrgESDDataMakerModelMappings
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
		int pageNumber = 0;
		Calendar calendar = Calendar.getInstance();
		String result = "FAIL";
		HashMap<String, ESDRecordMakerModel> makerModelsRecordIndex = new HashMap<>();
		HashMap<String, ESDRecordAttributeProfile> attributeProfilesRecordIndex = new HashMap<>();
		HashMap<String, ESDRecordAttribute> attributesRecordIndex = new HashMap<>();
		HashMap<String, ESDRecordProduct> productsRecordIndex = new HashMap<>();
		HashMap<String, ESDRecordCategory> categoriesRecordIndex = new HashMap<>();
		
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
		
		//retrieve organisation maker model data if the API was successfully created
		if(apiOrgSession.sessionExists())
		{
			System.out.println("Attempting to obtain maker model data.");
			
			//after 120 seconds give up on waiting for a response from the API
			int timeoutMilliseconds = 120000;

			//get the next page of records if needed
			while(getMoreRecords)
			{
				pageNumber++;
				getMoreRecords = false;
				
				//call the platform's API to retrieve the organisation's maker model data
				APIv1EndpointResponseESD endpointResponseESD = APIv1EndpointOrgRetrieveESDocument.call(apiOrgSession, timeoutMilliseconds, APIv1EndpointOrgRetrieveESDocument.RETRIEVE_TYPE_ID_MAKER_MODELS, supplierOrgID, "", recordsMaxAmount, recordsStartIndex, "");

				System.out.println("Attempt made to obtain maker model data. Page number: "+pageNumber);

				//check that the data successfully retrieved
				if(endpointResponseESD.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS))
				{	
					ESDocumentMakerModel esDocumentMakerModel = (ESDocumentMakerModel)endpointResponseESD.esDocument;
					
					//check that records have been placed into the standards document
					if(esDocumentMakerModel.dataRecords != null)
					{
						//iterate through each maker model record stored within the standards document
						for (ESDRecordMakerModel makerModelRecord: esDocumentMakerModel.dataRecords){
							makerModelsRecordIndex.putIfAbsent(makerModelRecord.keyMakerID, makerModelRecord);
						}

						//check if there are more records to retrieve
						if(esDocumentMakerModel.dataRecords.length >= recordsMaxAmount)
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
					System.out.println("FAIL - not all organisation maker model data could be obtained from the platform. Reason: " + endpointResponseESD.result_message  + " Error Code: " + endpointResponseESD.result_code);
				}
			}
		}
		
		//retrieve attribute data, since it may be used if maker model mappings contains attributes assigned to them
		if(result.equals("SUCCESS"))
		{
			System.out.println("Attempting to obtain attribute data.");
			
			result = "FAIL";
			recordsStartIndex = 0;
			
			//after 120 seconds give up on waiting for a response from the API
			int timeoutMilliseconds = 120000;

			//call the platform's API to retrieve the organisation's attribute data, ignore getting product attribute value data
			APIv1EndpointResponseESD endpointResponseESD = APIv1EndpointOrgRetrieveESDocument.call(apiOrgSession, timeoutMilliseconds, APIv1EndpointOrgRetrieveESDocument.RETRIEVE_TYPE_ID_ATTRIBUTES, supplierOrgID, "", recordsMaxAmount, recordsStartIndex, "ignore_products=Y");
			System.out.println("Attempt made to obtain attribute data.");

			//check that the data successfully retrieved
			if(endpointResponseESD.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS))
			{
				ESDocumentAttribute esDocumentAttribute = (ESDocumentAttribute)endpointResponseESD.esDocument;

				//check that attribute profile records have been placed into the standards document
				if(esDocumentAttribute.attributeProfiles != null)
				{
					//iterate through each attribute record stored within the standards document
					for (ESDRecordAttributeProfile attributeProfileRecord: esDocumentAttribute.attributeProfiles){
						attributeProfilesRecordIndex.putIfAbsent(attributeProfileRecord.keyAttributeProfileID, attributeProfileRecord);
						
						//iterate through each attribute assigned to the attribute profile
						for (ESDRecordAttribute attributeRecord: attributeProfileRecord.attributes){
							attributesRecordIndex.putIfAbsent(attributeRecord.keyAttributeID, attributeRecord);
						}
					}
				}else{
					System.out.println("No more records obtained.");
				}
				
				result = "SUCCESS";
			}else{
				result = "FAIL";
				System.out.println("FAIL - not all organisation attribute data could be obtained from the platform. Reason: " + endpointResponseESD.result_message  + " Error Code: " + endpointResponseESD.result_code);
			}
		}
		
		//retrieve product data, since it is used to find details of products assigned within make model mapping records
		if(result.equals("SUCCESS"))
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
		
		//retrieve category data, since it is used to find details of categories assigned within make model mapping records
		if(result.equals("SUCCESS"))
		{
			System.out.println("Attempting to obtain category data.");
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
				
				//call the platform's API to retrieve the organisation's category data, ignore getting products assigned to each category
				APIv1EndpointResponseESD endpointResponseESD = APIv1EndpointOrgRetrieveESDocument.call(apiOrgSession, timeoutMilliseconds, APIv1EndpointOrgRetrieveESDocument.RETRIEVE_TYPE_ID_CATEGORIES, supplierOrgID, "", recordsMaxAmount, recordsStartIndex, "ignore_products=Y");

				System.out.println("Attempt made to obtain category data. Page number: "+pageNumber);

				//check that the data successfully retrieved
				if(endpointResponseESD.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS))
				{	
					ESDocumentCategory esDocumentCategory = (ESDocumentCategory)endpointResponseESD.esDocument;
					
					//check that records have been placed into the standards document
					if(esDocumentCategory.dataRecords != null)
					{
						//iterate through each category record stored within the standards document
						for (ESDRecordCategory categoryRecord: esDocumentCategory.dataRecords){
							categoriesRecordIndex.putIfAbsent(categoryRecord.keyCategoryID, categoryRecord);
						}

						//check if there are more records to retrieve
						if(esDocumentCategory.dataRecords.length >= recordsMaxAmount)
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
					System.out.println("FAIL - not all organisation category data could be obtained from the platform. Reason: " + endpointResponseESD.result_message  + " Error Code: " + endpointResponseESD.result_code);
				}
			}
		}
		
		//retrieve organisation maker model mapping data if maker model, product, category and attribute data retrieved
		if(result.equals("SUCCESS"))
		{
			System.out.println("Attempting to obtain maker model mapping data.");
			
			recordsStartIndex = 0;
			pageNumber = 0;
			getMoreRecords = true;
			
			//after 60 seconds give up on waiting for a response from the API when creating the notification
			int timeoutMilliseconds = 60000;
			
			//get the next page of records if needed
			while(getMoreRecords)
			{
				getMoreRecords = false;
				pageNumber++;
				
				//call the platform's API to retrieve the organisation's maker model mapping data
				APIv1EndpointResponseESD endpointResponseESD = APIv1EndpointOrgRetrieveESDocument.call(apiOrgSession, timeoutMilliseconds, APIv1EndpointOrgRetrieveESDocument.RETRIEVE_TYPE_ID_MAKER_MODEL_MAPPINGS, supplierOrgID, "", recordsMaxAmount, recordsStartIndex, "");

				//check that the data successfully retrieved
				if(endpointResponseESD.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS)){
					System.out.println("SUCCESS - maker model mapping data successfully obtained from the platform for page number: "+pageNumber);

					//process and output maker model mapping records
					ESDocumentMakerModelMapping esDocumentMakerModelMapping = (ESDocumentMakerModelMapping)endpointResponseESD.esDocument;
					System.out.println("Maker Model Mapping Records Returned: " + esDocumentMakerModelMapping.totalDataRecords);

					//check that records have been placed into the standards document
					if(esDocumentMakerModelMapping.dataRecords != null){
						System.out.println("Maker Model Mapping Records:");

						//iterate through each maker model record stored within the standards document
						for(ESDRecordMakerModelMapping makerModelMappingRecord: esDocumentMakerModelMapping.dataRecords)
						{    
							recordNumber++;
							
							String modelCode = "";
							String modelName = "";
							String categoryCode = "";
							String categoryName = "";
							String productCode = "";
							String productName = "";

							//lookup the mapping's model and gets the model name and code
							if(makerModelsRecordIndex.containsKey(makerModelMappingRecord.keyMakerModelID)){
								modelCode = makerModelsRecordIndex.get(makerModelMappingRecord.keyMakerModelID).modelCode;
								modelName = makerModelsRecordIndex.get(makerModelMappingRecord.keyMakerModelID).name;
							}

							//lookup the mapping's category and gets the category name and code
							if(categoriesRecordIndex.containsKey(makerModelMappingRecord.keyCategoryID)){
								categoryCode = categoriesRecordIndex.get(makerModelMappingRecord.keyCategoryID).categoryCode;
								categoryName = categoriesRecordIndex.get(makerModelMappingRecord.keyCategoryID).name;
							}

							//lookup the mapping's product and gets the product name and code
							if(productsRecordIndex.containsKey(makerModelMappingRecord.keyProductID)){
								productCode = productsRecordIndex.get(makerModelMappingRecord.keyProductID).productCode;
								productName = productsRecordIndex.get(makerModelMappingRecord.keyProductID).name;
							}
							
							//output details of the maker model mapping record
							System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
							System.out.println("Maker Model Mapping Record #: " + recordNumber);
							System.out.println("          Key Maker Model ID: " + makerModelMappingRecord.keyMakerModelID);
							System.out.println("                  Model Code: " + modelCode);
							System.out.println("                  Model Name: " + modelName);
							System.out.println("             Key Category ID: " + makerModelMappingRecord.keyCategoryID);
							System.out.println("               Category Code: " + categoryCode);
							System.out.println("               Category Name: " + categoryName);
							System.out.println("              Key Product ID: " + makerModelMappingRecord.keyProductID);
							System.out.println("                Product Code: " + productCode);
							System.out.println("                Product Name: " + productName);
							System.out.println("                    Quantity: " + makerModelMappingRecord.quantity);
							
							//check if the mapping contains any attribute values
							if(makerModelMappingRecord.attributes.size() > 0)
							{
								//output attributes
								System.out.println("           Attributes Values: " + makerModelMappingRecord.attributes.size());

								//output each attribute value
								int attributValueCount = 0;
								for (ESDRecordAttributeValue attributeValueRecord: makerModelMappingRecord.attributes)
								{						
									attributValueCount++;
									
									//check that the attribute has been obtained
									if(attributesRecordIndex.containsKey(attributeValueRecord.keyAttributeID)){

										ESDRecordAttribute attributeRecord = attributesRecordIndex.get(attributeValueRecord.keyAttributeID);
										String attributeName = attributeRecord.name;
										String attributeDataType = attributeRecord.dataType;
										String attributeProfileName = "";
										String attributeValue = "";

										//get the name of the attribute profile that the attribute is linked to
										if(attributeProfilesRecordIndex.containsKey(attributeValueRecord.keyAttributeProfileID)){
											attributeProfileName = attributeProfilesRecordIndex.get(attributeValueRecord.keyAttributeProfileID).name;
										}
										
										// get the mapping's attribute value based on its data type
										if(attributeDataType.equals(ESDRecordAttribute.DATA_TYPE_NUMBER)){
											attributeValue = Double.toString(attributeValueRecord.numberValue);
										}else{
											attributeValue = attributeValueRecord.stringValue;
										}
										
										//out attribute value data
										System.out.println("");
										System.out.println("           Attribute Value #: " + attributValueCount);
										System.out.println("    Key Attribute Profile ID: " + attributeValueRecord.keyAttributeProfileID);
										System.out.println("      Attribute Profile Name: " + attributeProfileName);
										System.out.println("            Key Attribute ID: " + attributeValueRecord.keyAttributeID);
										System.out.println("              Attribute Name: " + attributeName);
										System.out.println("             Attribute Value: " + attributeValue);
									}
								}
							}

							System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
						}
						
						//check if there are more records to retrieve
						if(esDocumentMakerModelMapping.dataRecords.length >= recordsMaxAmount){
							recordsStartIndex = recordsStartIndex + recordsMaxAmount;
							getMoreRecords = true;
						}
					}
				}else{
					System.out.println("FAIL - not all organisation maker model mapping data could be obtained from the platform. Reason: " + endpointResponseESD.result_message  + " Error Code: " + endpointResponseESD.result_code);
				}
			}
		}
		
		//next steps
		//call other API endpoints...
		//destroy API session when done...
		apiOrgSession.destroyOrgSession();
    }
}
