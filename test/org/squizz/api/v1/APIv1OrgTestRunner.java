/**
* Copyright (C) 2017 Squizz PTY LTD
* This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
*/
package org.squizz.api.v1;

import EcommerceStandardsDocuments.ESDocumentConstants;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Entry point to run testing of version 1 of the SQUIZZ.com platform API library for organisations
 */
public class APIv1OrgTestRunner {
    public static final String CONSOLE_LINE = "==============================================";
    
    public static void main(String[] args)
    {
        int testNumber = 1;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println(CONSOLE_LINE);
        System.out.println("    SQUIZZ.com - The Connected Marketplace");
        System.out.println(CONSOLE_LINE);
        System.out.println("               SQUIZZ Pty Ltd");
        System.out.println(CONSOLE_LINE);
        System.out.println("Testing SQUIZZ.com API Java Library: version 1");
        System.out.println(CONSOLE_LINE);
        
        //test creating an organisation session within the platform's API
        APIv1OrgSession apiOrgSession = APIv1OrgSessionTest.testCreateAPIOrgSession(testNumber++);
        if(apiOrgSession != null && apiOrgSession.sessionExists()){
            
            //testing validating the created session
            APIv1OrgSessionTest.testValidateAPIOrgSession(testNumber++, apiOrgSession);
            
            //test validating the created session
            APIv1OrgSessionTest.testValidateCreateAPIOrgSession(testNumber++, apiOrgSession);
            
            //test destroying the created session
            APIv1OrgSessionTest.testDestroyAPIOrgSession(testNumber++, apiOrgSession);
            
            //test creating session
            APIv1OrgSessionTest.testValidateCreateAPIOrgSession(testNumber++, apiOrgSession);
            
            //testing validating the created session
            APIv1OrgSessionTest.testValidateAPIOrgSession(testNumber++, apiOrgSession);
            
            try{
                //test sending organisation notifications
                System.out.println("Test Sending Organisation Notification ("+ESDocumentConstants.ESD_VALUE_YES+" or "+ESDocumentConstants.ESD_VALUE_NO+"):");
                String testNotify = reader.readLine();
                if(testNotify.equalsIgnoreCase(ESDocumentConstants.ESD_VALUE_YES)){
                    APIv1OrgEndpointTest.testEndpointCreateOrganisationNotification(testNumber++, apiOrgSession);
                }
                
                //test validating organisation security certificate
                System.out.println("Test Validating Organisation Security Certificate ("+ESDocumentConstants.ESD_VALUE_YES+" or "+ESDocumentConstants.ESD_VALUE_NO+"):");
                testNotify = reader.readLine();
                if(testNotify.equalsIgnoreCase(ESDocumentConstants.ESD_VALUE_YES)){
                    APIv1OrgEndpointTest.testEndpointOrgValidateSecurityCertificate(testNumber++, apiOrgSession);
                }
                
                //test importing organisation data (stored in a Ecommerce Standards Document)
                System.out.println("Test Importing Organisation Data ("+ESDocumentConstants.ESD_VALUE_YES+" or "+ESDocumentConstants.ESD_VALUE_NO+"):");
                testNotify = reader.readLine();
                if(testNotify.equalsIgnoreCase(ESDocumentConstants.ESD_VALUE_YES)){
                    APIv1OrgEndpointTest.testEndpointOrgImportESDocument(testNumber++, apiOrgSession);
                }
            }catch(Exception ex){
                System.out.println("Unable to read line.");
            }
            
            //test destroying the created session
            APIv1OrgSessionTest.testDestroyAPIOrgSession(testNumber++, apiOrgSession);
        }
        
        System.out.println(CONSOLE_LINE);
        System.out.println("All Testing Finished");
    }
}
