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
 * Shows an example of creating a organisation session with the SQUIZZ.com platform's API, then retrieves Category data from a connected organisation in the platform
 */
public class APIv1ExampleRunnerRetrieveOrgESDDataCategories
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
		
		//retrieve product data, since it is used to find details of products assigned to categories
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
		
		//retrieve category data. Initially index it so that lookups can be done between linked categories, regardless of the order in which category records were obtained
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
				
				//call the platform's API to retrieve the organisation's category data
				APIv1EndpointResponseESD endpointResponseESD = APIv1EndpointOrgRetrieveESDocument.call(apiOrgSession, timeoutMilliseconds, APIv1EndpointOrgRetrieveESDocument.RETRIEVE_TYPE_ID_CATEGORIES, supplierOrgID, "", recordsMaxAmount, recordsStartIndex, "");

				System.out.println("Attempt made to obtain category data. Page number: "+pageNumber);

				//check that the data successfully retrieved
				if(endpointResponseESD.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS))
				{	
					System.out.println("SUCCESS - maker model mapping data successfully obtained from the platform for page number: "+pageNumber);
					
					ESDocumentCategory esDocumentCategory = (ESDocumentCategory)endpointResponseESD.esDocument;
					
					//check that records have been placed into the standards document
					if(esDocumentCategory.dataRecords != null)
					{
						System.out.println("Category Records Returned: " + esDocumentCategory.totalDataRecords);
						
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
		
		//output the details of each category
		if(result.equals("SUCCESS"))
		{
			System.out.println("Outputting category data.");
			
			//iterate through and output each category previously obtained
			for(String keyCategoryID: categoriesRecordIndex.keySet())
			{
				ESDRecordCategory categoryRecord = categoriesRecordIndex.get(keyCategoryID);
				String parentCategoryCode = "";

				//find the parent category that the category may be assigned to
				if(categoriesRecordIndex.containsKey(categoryRecord.keyCategoryParentID)){
					parentCategoryCode = categoriesRecordIndex.get(categoryRecord.keyCategoryParentID).categoryCode;
				}

				//output details of the category record
				System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
				System.out.println("     Category Record #: " + recordNumber);
				System.out.println("       Key Category ID: " + categoryRecord.keyCategoryID);
				System.out.println("         Category Code: " + categoryRecord.categoryCode);
				System.out.println("         Category Name: " + categoryRecord.name);
				System.out.println("Key Category Parent ID: " + categoryRecord.keyCategoryParentID);
				System.out.println("  Parent Category Code: " + parentCategoryCode);
				System.out.println("          Description1: " + categoryRecord.description1);
				System.out.println("          Description2: " + categoryRecord.description2);
				System.out.println("          Description3: " + categoryRecord.description3);
				System.out.println("          Description4: " + categoryRecord.description4);
				System.out.println("            Meta Title: " + categoryRecord.metaTitle);
				System.out.println("         Meta Keywords: " + categoryRecord.metaKeywords);
				System.out.println("      Meta Description: " + categoryRecord.metaDescription);
				System.out.println("              Ordering: " + categoryRecord.ordering);
				
				//check if the category contains any products
				if(categoryRecord.keyProductIDs != null && categoryRecord.keyProductIDs.length > 0)
				{
					System.out.println("        Products Count: " + categoryRecord.keyProductIDs.length);

					//output each product assigned to the category
					int productCount = 0;
					for (String keyProductID: categoryRecord.keyProductIDs)
					{
						productCount++;
						
						//check that the product has been previously obtained
						if(productsRecordIndex.containsKey(keyProductID))
						{
							ESDRecordProduct productRecord = productsRecordIndex.get(keyProductID);

							//output details of the product assigned to the category
							System.out.println();
							System.out.println("             Product #: " + productCount);
							System.out.println("        Key Product ID: " + keyProductID);
							System.out.println("          Product Code: " + productRecord.productCode);
							System.out.println("          Product Name: " + productRecord.name);
						}
					}
				}

				recordNumber++;
			}
		}
		
		//next steps
		//call other API endpoints...
		//destroy API session when done
		apiOrgSession.destroyOrgSession();
    }
}
