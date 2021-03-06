/**
* Copyright (C) 2017 Squizz PTY LTD
* This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
*/
package org.squizz.api.v1.endpoint;

import org.esd.EcommerceStandardsDocuments.ESDocumentConstants;
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

/**
 *
 * Class handles calling the SQUIZZ.com API endpoint for raising organisation notifications within the platform. 
 * Organisation notifications are sent to selected people assigned to the organisation's notification categories
 */
public class APIv1EndpointOrgCreateNotification 
{
    public static final String NOTIFY_CATEGORY_ORG = "org";
    public static final String NOTIFY_CATEGORY_ACCOUNT = "account";
    public static final String NOTIFY_CATEGORY_ORDER_SALE = "order_sale";
    public static final String NOTIFY_CATEGORY_ORDER_PURCHASE = "order_purchase";
    public static final String NOTIFY_CATEGORY_FEED = "feed";
    public static final int MAX_MESSAGE_PLACEHOLDERS = 5;
    
    /**
     * Calls the platform's API endpoint to create an organisation notification and notify selected people assigned to an organisation's notification category
     * To allow notifications to be sent to the platform the organisation must have sufficient trading tokens
     * @param apiOrgSession existing organisation API session
     * @param endpointTimeoutMilliseconds amount of milliseconds to wait after calling the the API before giving up, set a positive number
     * @param notifyCategory notification category that the notification appears within for the organisation's people. Set to one of the NOTIFY_CATEGORY_ constants
     * @param message message to display in the notification. Put placeholders in message {1}, {2}, {3}, {4}, {5} to replace with links or labels
     * @param linkURLs ordered array of URLs to replace in each of the place holders of the message. Set empty strings to ignore placing values into place holders
     * @param linkLabels ordered array of labels to replace in each of the place holders of the message. Set empty strings to ignore placing values into place holders
     * @return response from calling the API endpoint
     */
    public static APIv1EndpointResponseESD call(APIv1OrgSession apiOrgSession, int endpointTimeoutMilliseconds, String notifyCategory, String message, String[] linkURLs, String[] linkLabels)
    {
        String endpointParams = "";
        ArrayList<Pair<String, String>> requestHeaders = new ArrayList<>();
        requestHeaders.add(new Pair<>(APIv1HTTPRequest.HTTP_HEADER_CONTENT_TYPE, APIv1HTTPRequest.HTTP_HEADER_CONTENT_TYPE_FORM_URL_ENCODED));
        APIv1EndpointResponseESD endpointResponse = new APIv1EndpointResponseESD();
        
        try{
            String linkURLParams = "";
            String linkLabelParams = "";
            
            //generate parameters for link URLs to be placed in the message
            for(int i=0; i < linkURLs.length && i < MAX_MESSAGE_PLACEHOLDERS; i++){
                if(!linkURLs[i].trim().isEmpty()){
                    linkURLParams += "&link"+(i+1)+"_url="+URLEncoder.encode(linkURLs[i], StandardCharsets.UTF_8.name());
                }
            }
            
            //generate parameters for link labels to be placed in the message
            for(int i=0; i < linkLabels.length && i < MAX_MESSAGE_PLACEHOLDERS; i++){
                if(!linkLabels[i].trim().isEmpty()){
                    linkLabelParams += "&link"+(i+1)+"_label="+URLEncoder.encode(linkLabels[i], StandardCharsets.UTF_8.name());
                }
            }
            
            //set notification parameters
            String requestPostBody = "notify_category="+URLEncoder.encode(notifyCategory, StandardCharsets.UTF_8.name())+"&message="+ URLEncoder.encode(message, StandardCharsets.UTF_8.name()) + linkURLParams + linkLabelParams;
            
            //create JSON deserializer to interpret the response from the endpoint
            ObjectMapper jsonMapper = new ObjectMapper();
            jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            ObjectReader endpointJSONReader = jsonMapper.readerFor(APIv1EndpointResponse.class);
            
            //make a HTTP request to the platform's API endpoint to create the organisation notifications
            endpointResponse = APIv1HTTPRequest.sendESDocumentHTTPRequest(APIv1Constants.HTTP_REQUEST_METHOD_POST, APIv1Constants.API_ORG_ENDPOINT_CREATE_NOTIFCATION+APIv1Constants.API_PATH_SLASH+apiOrgSession.getSessionID(), endpointParams, requestHeaders, requestPostBody, null, endpointTimeoutMilliseconds, apiOrgSession.getLangBundle(), endpointJSONReader, endpointResponse);
            
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
