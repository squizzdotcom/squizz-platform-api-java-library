/**
* Copyright (C) 2019 Squizz PTY LTD
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
 * Shows an example of creating a organisation session with the SQUIZZ.com platform's API, then retrieves Maker data from a conencted organisation in the platform
 */
public class APIv1ExampleRunnerRetrieveOrgESDDataMakers
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
		
		//retrieve organisation maker data if the API was successfully created
		if(apiOrgSession.sessionExists())
		{   
			//after 60 seconds give up on waiting for a response from the API when creating the notification
			int timeoutMilliseconds = 60000;
			
			//get the next page of records if needed
			while(getMoreRecords)
			{
				getMoreRecords = false;
				
				//call the platform's API to retrieve the organisation's maker data
				APIv1EndpointResponseESD endpointResponseESD = APIv1EndpointOrgRetrieveESDocument.call(apiOrgSession, timeoutMilliseconds, APIv1EndpointOrgRetrieveESDocument.RETRIEVE_TYPE_ID_MAKERS, supplierOrgID, "", recordsMaxAmount, recordsStartIndex, "");

				//check that the data successfully retrieved
				if(endpointResponseESD.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS)){
					System.out.println("SUCCESS - organisation data successfully obtained from the platform for page number: "+pageNumber);
					pageNumber++;

					//process and output maker records
					ESDocumentMaker esDocumentMaker = (ESDocumentMaker)endpointResponseESD.esDocument;
					System.out.println("Maker Records Returned: " + esDocumentMaker.totalDataRecords);

					//check that records have been placed into the standards document
					if(esDocumentMaker.dataRecords != null){
						System.out.println("Maker Records:");

						//iterate through each maker record stored within the standards document
						
						for(ESDRecordMaker makerRecord: esDocumentMaker.dataRecords)
						{    
							recordNumber++;
							
							//output details of the maker record
							System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
							System.out.println("       Maker Record #: " + recordNumber);
							System.out.println("         Key Maker ID: " + makerRecord.keyMakerID);
							System.out.println("           Maker Code: " + makerRecord.makerCode);
							System.out.println("           Maker Name: " + makerRecord.name);
							System.out.println("    Maker Search Code: " + makerRecord.makerSearchCode);
							System.out.println("             Ordering: " + makerRecord.ordering);
							System.out.println("          Group Class: " + makerRecord.groupClass);
							System.out.println("            Org. Name: " + makerRecord.orgName);
							System.out.println("      Authority Label: " + (makerRecord.authorityNumberLabels != null && makerRecord.authorityNumberLabels.length>0? makerRecord.authorityNumberLabels[0]: ""));
							System.out.println("     Authority Number: " + (makerRecord.authorityNumbers != null && makerRecord.authorityNumbers.length>0? makerRecord.authorityNumbers[0]: ""));
							System.out.println("Authority Number Type: " + (makerRecord.authorityNumberTypes != null && makerRecord.authorityNumberTypes.length>0? makerRecord.authorityNumberTypes[0]: ""));

							System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
						}
						
						//check if there are more records to retrieve
						if(esDocumentMaker.dataRecords.length >= recordsMaxAmount){
							recordsStartIndex = recordsStartIndex + recordsMaxAmount;
							getMoreRecords = true;
						}
					}
				}else{
					System.out.println("FAIL - not all organisation maker data could be obtained from the platform. Reason: " + endpointResponseESD.result_message  + " Error Code: " + endpointResponseESD.result_code);
				}
			}
		}
		
		//next steps
		//call other API endpoints...
		//destroy API session when done...
		apiOrgSession.destroyOrgSession();
    }
}
