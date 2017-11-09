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
import java.util.ResourceBundle;
import javafx.util.Pair;
import org.squizz.api.v1.APIv1Constants;
import org.squizz.api.v1.APIv1HTTPRequest;
import org.squizz.api.v1.APIv1OrgSession;
import org.esd.EcommerceStandardsDocuments.*;
import org.squizz.api.v1.APIv1Constants;
import org.squizz.api.v1.APIv1HTTPRequest;
import org.squizz.api.v1.APIv1OrgSession;
import org.squizz.api.v1.endpoint.APIv1EndpointResponse;
import org.squizz.api.v1.endpoint.APIv1EndpointResponseESD;

/**
 * Class handles calling the SQUIZZ.com API endpoint to retrieve details of a single record (such as invoice, sales order, back order, payment, credit, transaction) associated to a supplier organisation's customer account. See the full list at https://www.squizz.com/docs/squizz/Platform-API.html#section1036
 * The data being retrieved is wrapped up in a Ecommerce Standards Document (ESD) that contains records storing data of a particular type
 */
public class APIv1EndpointOrgRetrieveCustomerAccountRecord
{
    /**
     * Calls the platform's API endpoint and retrieves for a connected organisation a customer account record retrieved live from organisation's connected business system
     * @param apiOrgSession existing organisation API session
     * @param endpointTimeoutMilliseconds amount of milliseconds to wait after calling the the API before giving up, set a positive number
     * @param recordType type of record data to retrieve
     * @param supplierOrgID unique ID of the organisation in the SQUIZZ.com platform that has supplies the customer account
     * @param customerAccountCode code of the account organisation's customer account. Customer account only needs to be set if the supplier organisation has assigned multiple accounts to the organisation logged into the API session (customer org) and account specific data is being obtained
     * @param keyRecordID comma delimited list of records unique key record ID to match on. Each Key Record ID value needs to be URI encoded
     * @return response from calling the API endpoint
     */
    public static APIv1EndpointResponseESD call(
        APIv1OrgSession apiOrgSession, 
        int endpointTimeoutMilliseconds, 
        String recordType, 
        String supplierOrgID, 
        String customerAccountCode,
        String keyRecordID)
    {
        ArrayList<Pair<String, String>> requestHeaders = new ArrayList<>();
        APIv1EndpointResponseESD endpointResponse = new APIv1EndpointResponseESD();
        ObjectReader endpointJSONReader = null;
        boolean callEndpoint = true;
        
        try{
            //set endpoint parameters
            String endpointParams = 
                "record_type="+recordType + 
                "&supplier_org_id=" + URLEncoder.encode(supplierOrgID, StandardCharsets.UTF_8.name()) + 
                "&customer_account_code="+URLEncoder.encode(customerAccountCode, StandardCharsets.UTF_8.name()) +
                "&key_record_id=" + URLEncoder.encode(keyRecordID, StandardCharsets.UTF_8.name());
            
            //create JSON deserializer to interpret the response from the endpoint
            ObjectMapper jsonMapper = new ObjectMapper();
            jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            endpointJSONReader = jsonMapper.readerFor(ESDocumentCustomerAccountEnquiry.class);
            
            //make a HTTP request to the platform's API endpoint to retrieve the customer account record
            if(callEndpoint && endpointJSONReader != null)
            {
                endpointResponse = APIv1HTTPRequest.sendESDocumentHTTPRequest(APIv1Constants.HTTP_REQUEST_METHOD_GET, APIv1Constants.API_ORG_ENDPOINT_RETRIEVE_CUSTOMER_ACCOUNT_RECORD_ESD+APIv1Constants.API_PATH_SLASH+apiOrgSession.getSessionID(), endpointParams, requestHeaders, "", null, endpointTimeoutMilliseconds, apiOrgSession.getLangBundle(), endpointJSONReader, endpointResponse);

                //check that the data was successfully retrieved
                if(!endpointResponse.result.equalsIgnoreCase(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS))
                {
                    //check if the session still exists
                    if(endpointResponse.result.equalsIgnoreCase(APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_SESSION_INVALID)){
                        //mark that the session has expired
                        apiOrgSession.markSessionExpired();
                    }
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
