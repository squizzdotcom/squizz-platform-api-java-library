/**
* Copyright (C) 2017 Squizz PTY LTD
* This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
*/
package org.squizz.api.v1.example;

import org.squizz.api.v1.*;
import org.squizz.api.v1.APIv1Constants;
import org.squizz.api.v1.APIv1OrgSession;
import org.squizz.api.v1.endpoint.*;

/**
 * Shows an example of creating a organisation session with the SQUIZZ.com platform's API, then creating an organisation notification
 */
public class APIv1ExampleRunnerCreateOrgNotification 
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
		
		//create organisation notification if the API was successfully created
		if(apiOrgSession.sessionExists())
		{
			//set the notification category that the organisation will display under in the platform, in this case the sales order category
			String notifyCategory = APIv1EndpointOrgCreateNotification.NOTIFY_CATEGORY_ORDER_SALE;
			
			//after 20 seconds give up on waiting for a response from the API when creating the notification
			int timeoutMilliseconds = 20000;
			
			//set the message that will appear in the notification, note the placeholders {1} and {2} that will be replaced with data values
			String message = "A new {1} was created in {2} Website";
			
			//set labels and links to place within the placeholders of the message
			String[] linkLabels = new String[]{"Sales Order","Acme Industries"};
			String[] linkURLs = new String[]{"","http://www.example.com/acmeindustries"};
			
			//call the platform's API to create the organistion notification and have people assigned to organisation's notification category receive it
			APIv1EndpointResponseESD endpointResponseESD = APIv1EndpointOrgCreateNotification.call(apiOrgSession, timeoutMilliseconds, notifyCategory, message, linkURLs, linkLabels);
			
			if(endpointResponseESD.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS)){
                System.out.println("SUCCESS - organisation notification successfully created in the platform");
            }else{
                System.out.println("FAIL - organisation notification failed to be created. Reason: " + endpointResponseESD.result_message  + " Error Code: " + endpointResponseESD.result_code);
            }
		}
		
		//next steps
		//call other API endpoints...
		//destroy API session when done...
    }
}