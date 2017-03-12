/**
* Copyright (C) 2017 Squizz PTY LTD
* This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
*/
package org.squizz.api.v1;

import org.squizz.api.v1.endpoint.APIv1EndpointResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Locale;
import org.esd.EcommerceStandardsDocuments.ESDocumentConstants;


/**
 * Tests calling the SQUIZZ.com platform's API for testing session endpoints with the library
 */
public class APIv1OrgSessionTest 
{
    /**
     * tests creating a new session for an organisation within a platform's API
     * @param testNumber number of the test being performed
     * @return API org session or null if the session could not be created
     */
    public static APIv1OrgSession testCreateAPIOrgSession(int testNumber)
    {
        System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
        System.out.println("Test "+testNumber+" - Create API Organisation Session");
        System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
        APIv1OrgSession apiOrgSession = null;
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        try{
            //get the organisation API credentials
            System.out.println("Enter Organisation ID:");
            String orgID = reader.readLine();
            
            System.out.println("Enter Organisation API Key:");
            String orgAPIKey = reader.readLine();
            
            System.out.println("Enter Organisation API Password:");
            String orgAPIPass = reader.readLine();
            
            //initialise API session object
            apiOrgSession = new APIv1OrgSession(orgID, orgAPIKey, orgAPIPass, 0, APIv1Constants.SUPPORTED_LOCALES_EN_AU);
            
            //call API endpoint in the SQUIZZ.com platform to create a new session
            System.out.println("Call API to create session for the organisation");
            APIv1EndpointResponse endpointResponse = apiOrgSession.createOrgSession();
            
            //check if the endpoint was created
            if(endpointResponse.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS)){
                System.out.println("SUCCESS - API session successfully created.");
                System.out.println("Session ID - " + apiOrgSession.getSessionID());
                System.out.println("Session API Version - " + apiOrgSession.getAPIVersion());
                System.out.println("Session Exists - " + apiOrgSession.sessionExists());
            }else{
                System.out.println("FAIL - API session failed to be created.");
                System.out.println("Endpoint Result: " + endpointResponse.result);
                System.out.println("Endpoint Result Code: " + endpointResponse.result_code);
                System.out.println("Endpoint Result Message: " + endpointResponse.result_message);
            }
        }catch(Exception ex){
            System.out.println("An error occurred when performing the test. Error: " + ex.getLocalizedMessage());
        }
        
        System.out.println("Test "+testNumber+" - Finished");
        
        return apiOrgSession;
    }
    
    /**
     * tests validating an existing session for an organisation within a platform's API
     * @param testNumber number of the test being performed
     * @param apiOrgSession existing API organisation session
     */
    public static void testValidateAPIOrgSession(int testNumber, APIv1OrgSession apiOrgSession)
    {
        System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
        System.out.println("Test "+testNumber+" - Validate API Organisation Session");
        System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        try{
            //call API endpoint in the SQUIZZ.com platform to validate the existing session
            System.out.println("Call API to validate existing session for the organisation");
            APIv1EndpointResponse endpointResponse = apiOrgSession.validateOrgSession();
            
            //check the result of validating the session
            if(endpointResponse.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS)){
                System.out.println("SUCCESS - API session successfully validated.");
                System.out.println("Session ID - " + apiOrgSession.getSessionID());
                System.out.println("Session API Version - " + apiOrgSession.getAPIVersion());
                System.out.println("Session Exists - " + apiOrgSession.sessionExists());
            }else{
                System.out.println("FAIL - API session failed to be validated.");
                System.out.println("Endpoint Result: " + endpointResponse.result);
                System.out.println("Endpoint Result Code: " + endpointResponse.result_code);
                System.out.println("Endpoint Result Message: " + endpointResponse.result_message);
            }
        }catch(Exception ex){
            System.out.println("An error occurred when performing the test. Error: " + ex.getLocalizedMessage());
        }
        
        System.out.println("Test "+testNumber+" - Finished");
    }
    
    /**
     * tests destroying an existing session for an organisation within a platform's API
     * @param testNumber number of the test being performed
     * @param apiOrgSession existing API organisation session
     */
    public static void testDestroyAPIOrgSession(int testNumber, APIv1OrgSession apiOrgSession)
    {
        System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
        System.out.println("Test "+testNumber+" - Destroy API Organisation Session");
        System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        try{
            //call API endpoint in the SQUIZZ.com platform to validate the existing session
            System.out.println("Call API to destroy existing session for the organisation");
            APIv1EndpointResponse endpointResponse = apiOrgSession.destroyOrgSession();
            
            //check the result of validating the session
            if(endpointResponse.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS)){
                System.out.println("SUCCESS - API session successfully destroyed.");
                System.out.println("Session ID - " + apiOrgSession.getSessionID());
                System.out.println("Session API Version - " + apiOrgSession.getAPIVersion());
                System.out.println("Session Exists - " + apiOrgSession.sessionExists());
            }else{
                System.out.println("FAIL - API session failed to be destroyed.");
                System.out.println("Endpoint Result: " + endpointResponse.result);
                System.out.println("Endpoint Result Code: " + endpointResponse.result_code);
                System.out.println("Endpoint Result Message: " + endpointResponse.result_message);
            }
        }catch(Exception ex){
            System.out.println("An error occurred when performing the test. Error: " + ex.getLocalizedMessage());
        }
        
        System.out.println("Test "+testNumber+" - Finished");
    }
    
    /**
     * tests validating and creating an existing session for an organisation within a platform's API
     * @param testNumber number of the test being performed
     * @param apiOrgSession existing API organisation session
     */
    public static void testValidateCreateAPIOrgSession(int testNumber, APIv1OrgSession apiOrgSession)
    {
        System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
        System.out.println("Test "+testNumber+" - Validate/Create API Organisation Session");
        System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        try{
            //call API endpoint in the SQUIZZ.com platform to validate the existing session
            System.out.println("Call API to validate/create existing session for the organisation");
            APIv1EndpointResponse endpointResponse = apiOrgSession.validateCreateOrgSession();
            
            //check the result of validating the session
            if(endpointResponse.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS)){
                System.out.println("SUCCESS - API session successfully validated.");
                System.out.println("Session ID - " + apiOrgSession.getSessionID());
                System.out.println("Session API Version - " + apiOrgSession.getAPIVersion());
                System.out.println("Session Exists - " + apiOrgSession.sessionExists());
            }else{
                System.out.println("FAIL - API session failed to be validated.");
                System.out.println("Endpoint Result: " + endpointResponse.result);
                System.out.println("Endpoint Result Code: " + endpointResponse.result_code);
                System.out.println("Endpoint Result Message: " + endpointResponse.result_message);
            }
        }catch(Exception ex){
            System.out.println("An error occurred when performing the test. Error: " + ex.getLocalizedMessage());
        }
        
        System.out.println("Test "+testNumber+" - Finished");
    }
}
