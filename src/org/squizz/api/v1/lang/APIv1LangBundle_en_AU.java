/**
* Copyright (C) 2017 Squizz PTY LTD
* This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
*/
package org.squizz.api.v1.lang;
import java.util.ListResourceBundle;
import java.util.ResourceBundle;
import org.squizz.api.v1.endpoint.APIv1EndpointResponse;

/**
 * Defines the language used for the platform's API, by default in English Australian
 */
public class APIv1LangBundle_en_AU extends ListResourceBundle
{
    /**
     * defines key value pairs of language used for the API
     * @return 
     */
    protected Object[][] getContents() {
        return new Object[][] {
            {APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS, "API endpoint was successfully called."},
            {APIv1EndpointResponse.ENDPOINT_RESULT_FAILURE, "An error occurred when the API endpoint was called."},
            {APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_UNKNOWN, "An unknown or non-specified error occurred when calling the API endpoint."},
            {APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_MALFORMED_URL, "An error occurred when calling the API endpoint due to URL being not correctly set."},
            {APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_RESPONSE, "An error occurred when calling the API endpoint due to the server returning a bad response. The platform's API may be unavailable, under heavy load, or a network connection error could be occurring."},
            {APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_REQUEST_PROTOCOL, "An error occurred when calling the API endpoint due to an issue with the protocol used to call the endpoint."},
            {APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_CONNECTION, "An error occurred when calling the API endpoint due to an issue with connecting to the platform's servers. Check that your internet connection is available and no other networking issues are occurring."},
            {APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_IO, "An error occurred when calling the API endpoint due to an IO (Reading or Writing) error."},
            {APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_ORG_NOT_FOUND, "An error occurred when calling the API endpoint since no organisation could be found matching the ID given."},
            {APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_INCORRECT_API_CREDENTIALS, "An error occurred when calling the API endpoint due to incorrect API credentials being given for the organisation."},
            {APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_ORG_INACTIVE, "An error occurred when calling the API endpoint due to the organisation being inactive or deleted from the platform."},
            {APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_SESSION_INVALID, "An error occurred when calling the API endpoint due to the API session not existing or previously destroyed."},
            {APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_INVALID_NOTIFICATION_CATEGORY, "An error occurred when calling the API endpoint due to an incorrect notification category been given to it."},
            {APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_NO_ORG_PEOPLE_TO_NOTIFY, "An error occurred when calling the API endpoint due to no people being configured in the organisation to receive the notification."},
            {APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_INSUFFICIENT_CREDIT, "An error occurred when calling the API endpoint due to the organisation having insufficient trading tokens in the platform to process the request."},
            {APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_SECURITY_CERTIFICATE_NOT_FOUND, "An error occurred when calling the API endpoint due to the organisation's security certificate not able to found or does not exist."},
            {APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_SENDER_DOES_NOT_MATCH_CERTIFICATE_COMMON_NAME, "An error occurred when calling the API endpoint, due to the common name set in the organisation's security certificate not matching the IP address of the internet connection used to call the endpoint"},
                
            {APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_DATA_IMPORT_MISSING_IMPORT_TYPE,"An error occurred when calling the API endpoint due to no data type set indicating the kind of data being imported."},
            {APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_DATA_IMPORT_MAX_IMPORTS_RUNNING,"An error occurred when calling the API endpoint due to the maximum number of data imports being run over a short period of time. Wait a while before calling the endpoint again and consider calling the endpoint less often."},
            {APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_DATA_IMPORT_BUSY,"An error occurred when calling the API endpoint due to another data import already running or the API is busy processing other requests."},
            {APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_DATA_IMPORT_NOT_FOUND,"An error occurred when calling the API endpoint due to an incorrect or unsupported data type being set."},
            {APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_DATA_JSON_WRONG_CONTENT_TYPE,"An error occurred when calling the API endpoint due to the content type in the request headers not being set to application/json, specifying that data imported is in the JSON data format."},
            {APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_DATA_JSON_MALFORMED,"An error occurred when calling the API endpoint due to the data being imported in the JSON data format not able to be processed, because it has not been correctly formed. Check for syntax errors with the JSON data."},
            {APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_ESD_DOCUMENT_HEADER_MALFORMED,"An error occurred when calling the API endpoint due to the Ecommerce Standards Document uploaded missing an opening bracket in its JSON data."},
            {APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_ESD_DOCUMENT_HEADER_MISSING_ATTRIBUTES,"An error occurred when calling the API endpoint due to the Ecommerce Standards Document being uploaded missing the dataRecords attribute that should contain the record data to import."},
            {APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_DATA_IMPORT_ABORTED,"An error occurred when calling the API endpoint due to the data import being aborted. It may have been aborted by a person or by the platform."},
            {APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_ESD_DOCUMENT_UNSUCCESSFUL,"An error occurred when calling the API endpoint due to Ecommerce Standards Document failing to import. Check that the document was correctly formed."},
            {APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_ESD_DOCUMENT_NO_RECORD,"An error occurred when calling the API endpoint due to  Ecommerce Standards Document not containing any records to import. Look to add one or more records to the document."}
        };
    }
}