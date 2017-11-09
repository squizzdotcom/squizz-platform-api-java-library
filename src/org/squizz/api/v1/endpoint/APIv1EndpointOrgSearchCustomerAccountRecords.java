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
import org.squizz.api.v1.endpoint.APIv1EndpointResponse;
import org.squizz.api.v1.endpoint.APIv1EndpointResponseESD;

/**
 * Class handles calling the SQUIZZ.com API endpoint to search for and retrieve records (such as invoices, sales orders, back orders, payments, credits, transactions) associated to a supplier organisation's customer account. See the full list at https://www.squizz.com/docs/squizz/Platform-API.html#section843
 * The data being retrieved is wrapped up in a Ecommerce Standards Document (ESD) that contains records storing data of a particular type
 */
public class APIv1EndpointOrgSearchCustomerAccountRecords
{
    /**
     * Calls the platform's API endpoint and searches for a connected organisation's customer account records retrieved live from their connected business system
     * @param apiOrgSession existing organisation API session
     * @param endpointTimeoutMilliseconds amount of milliseconds to wait after calling the the API before giving up, set a positive number
     * @param recordType type of record data to search for.
     * @param supplierOrgID unique ID of the organisation in the SQUIZZ.com platform that has supplies the customer account
     * @param customerAccountCode code of the account organisation's customer account. Customer account only needs to be set if the supplier organisation has assigned multiple accounts to the organisation logged into the API session (customer org) and account specific data is being obtained
     * @param beginDateTime earliest date time to search for records for. Date time set as milliseconds since 1/1/1970 12am UTC epoch
     * @param endDateTime latest date time to search for records up to. Date time set as milliseconds since 1/1/1970 12am UTC epoch
     * @param pageNumber page number to obtain records from
     * @param recordsMaxAmount maximum number of records to return
     * @param outstandingRecords if true then only search for records that are marked as outstanding (such as unpaid invoices)
     * @param searchString search text to match records on
     * @param keyRecordIDs comma delimited list of records unique key record ID to match on. Each Key Record ID value needs to be URI encoded
     * @param searchType specifies the field to search for records on, matching the record's field with the search string given
     * @return response from calling the API endpoint
     */
    public static APIv1EndpointResponseESD call(
        APIv1OrgSession apiOrgSession, 
        int endpointTimeoutMilliseconds, 
        String recordType, 
        String supplierOrgID, 
        String customerAccountCode,
        long beginDateTime,
        long endDateTime,
        int pageNumber,
        int recordsMaxAmount,
        boolean outstandingRecords, 
        String searchString,
        String keyRecordIDs, 
        String searchType)
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
                "&begin_date_time=" + beginDateTime+
                "&end_date_time=" + endDateTime+
                "&page_number=" + pageNumber+
                "&records_max_amount=" + recordsMaxAmount+
                "&outstanding_records=" + (outstandingRecords?"Y":"N")+
                "&search_string=" + URLEncoder.encode(searchString, StandardCharsets.UTF_8.name())+
                "&key_record_ids=" + URLEncoder.encode(keyRecordIDs, StandardCharsets.UTF_8.name())+
                "&search_type=" + URLEncoder.encode(searchType, StandardCharsets.UTF_8.name());
            
            //create JSON deserializer to interpret the response from the endpoint
            ObjectMapper jsonMapper = new ObjectMapper();
            jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            endpointJSONReader = jsonMapper.readerFor(ESDocumentCustomerAccountEnquiry.class);
            
            //make a HTTP request to the platform's API endpoint to search for the customer account records
            if(callEndpoint && endpointJSONReader != null)
            {
                endpointResponse = APIv1HTTPRequest.sendESDocumentHTTPRequest(APIv1Constants.HTTP_REQUEST_METHOD_GET, APIv1Constants.API_ORG_ENDPOINT_SEARCH_CUSTOMER_ACCOUNT_RECORDS_ESD+APIv1Constants.API_PATH_SLASH+apiOrgSession.getSessionID(), endpointParams, requestHeaders, "", null, endpointTimeoutMilliseconds, apiOrgSession.getLangBundle(), endpointJSONReader, endpointResponse);

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
