/**
* Copyright (C) 2017 Squizz PTY LTD
* This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
*/
package org.squizz.api.v1.endpoint;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import javafx.util.Pair;
import org.squizz.api.v1.APIv1Constants;
import org.squizz.api.v1.APIv1HTTPRequest;
import org.squizz.api.v1.APIv1OrgSession;
import static org.squizz.api.v1.endpoint.APIv1EndpointOrgCreateNotification.MAX_MESSAGE_PLACEHOLDERS;

/**
 * Class handles calling the SQUIZZ.com API endpoint for verifying a security certificate created for an organisation within the platform. 
 * Security certificates are used to secure organisational data transferred across the Internet and computer networks
 */
public class APIv1EndpointOrgValidateSecurityCertificate 
{
    /**
     * Calls the platform's API endpoint to validate the organisation's security certificate
     * The public Internet connection used to call the endpoint will be used to validate against the domain or IP address set for the security certificate
     * @param apiOrgSession existing organisation API session
     * @param endpointTimeoutMilliseconds amount of milliseconds to wait after calling the the API before giving up, set a positive number
     * @param orgSecurityCertificateID ID of the orgnisation's security certificate in the platform
     * @return response from calling the API endpoint
     */
    public static APIv1EndpointResponseESD call(APIv1OrgSession apiOrgSession, int endpointTimeoutMilliseconds, String orgSecurityCertificateID)
    {
        String endpointParams = "";
        ArrayList<Pair<String, String>> requestHeaders = new ArrayList<>();
        requestHeaders.add(new Pair<>(APIv1HTTPRequest.HTTP_HEADER_CONTENT_TYPE, APIv1HTTPRequest.HTTP_HEADER_CONTENT_TYPE_FORM_URL_ENCODED));
        APIv1EndpointResponseESD endpointResponse = new APIv1EndpointResponseESD();
        
        try{
            //set notification parameters
            String requestPostBody = "org_security_certificate_id="+URLEncoder.encode(orgSecurityCertificateID, StandardCharsets.UTF_8.name());
            
            //create JSON deserializer to interpret the response from the endpoint
            ObjectMapper jsonMapper = new ObjectMapper();
            jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            ObjectReader endpointJSONReader = jsonMapper.readerFor(APIv1EndpointResponse.class);
            
            //make a HTTP request to the platform's API endpoint to validate the security certificate
            endpointResponse = APIv1HTTPRequest.sendESDocumentHTTPRequest(APIv1Constants.HTTP_REQUEST_METHOD_POST, APIv1Constants.API_ORG_ENDPOINT_VALIDATE_CERT+APIv1Constants.API_PATH_SLASH+apiOrgSession.getSessionID(), endpointParams, requestHeaders, requestPostBody, null, endpointTimeoutMilliseconds, apiOrgSession.getLangBundle(), endpointJSONReader, endpointResponse);
            
            //check that the notification were successfully sent
            if(!endpointResponse.result.equalsIgnoreCase(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS))
            {
                //check if the session still exists
                if(endpointResponse.result.equalsIgnoreCase(APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_SESSION_INVALID)){
                    //mark that the session has expired
                    apiOrgSession.markSessionExpired();
                }
            }
        }
        catch(Exception ex)
        {
            endpointResponse.result = APIv1EndpointResponse.ENDPOINT_RESULT_FAILURE;
            endpointResponse.result_code = APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_UNKNOWN;
			endpointResponse.result_message = apiOrgSession.getLangBundle().getString(endpointResponse.result_code) + "\n" + ex.getLocalizedMessage();
        }
        
        return endpointResponse;
    }
}
