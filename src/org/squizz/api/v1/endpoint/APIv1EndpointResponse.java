/**
* Copyright (C) 2017 Squizz PTY LTD
* This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
*/
package org.squizz.api.v1.endpoint;

/**
 * Represents the response returned from an endpoint in the platform's API
 */
public class APIv1EndpointResponse 
{
    public static String ENDPOINT_RESULT_SUCCESS = "SUCCESS";
    public static String ENDPOINT_RESULT_FAILURE = "FAILURE";
    public static String ENDPOINT_RESULT_CODE_ERROR_UNKNOWN = "SERVER_ERROR_UNKOWN";
    public static String ENDPOINT_RESULT_CODE_ERROR_MALFORMED_URL = "SERVER_ERROR_MALFORMED_URL";
    public static String ENDPOINT_RESULT_CODE_ERROR_RESPONSE = "SERVER_ERROR_RESPONSE";
    public static String ENDPOINT_RESULT_CODE_ERROR_REQUEST_PROTOCOL = "SERVER_ERROR_REQUEST_PROTOCOL";
    public static String ENDPOINT_RESULT_CODE_ERROR_CONNECTION = "SERVER_ERROR_CONNECTION";
    public static String ENDPOINT_RESULT_CODE_ERROR_IO = "SERVER_ERROR_IO";
    public static String ENDPOINT_RESULT_CODE_ERROR_ORG_NOT_FOUND = "SERVER_ERROR_ORG_NOT_FOUND";
    public static String ENDPOINT_RESULT_CODE_ERROR_INCORRECT_API_CREDENTIALS = "SERVER_ERROR_INCORRECT_API_CREDENTIALS";
    public static String ENDPOINT_RESULT_CODE_ERROR_ORG_INACTIVE = "SERVER_ERROR_ORG_INACTIVE";
    public static String ENDPOINT_RESULT_CODE_ERROR_SESSION_INVALID = "SERVER_ERROR_SESSION_INVALID";
    public static String ENDPOINT_RESULT_CODE_ERROR_INVALID_NOTIFICATION_CATEGORY = "SERVER_ERROR_INVALID_NOTIFICATION_CATEGORY";
    public static String ENDPOINT_RESULT_CODE_ERROR_NO_ORG_PEOPLE_TO_NOTIFY = "SERVER_ERROR_NO_ORG_PEOPLE_TO_NOTIFY";
    public static String ENDPOINT_RESULT_CODE_ERROR_INSUFFICIENT_CREDIT = "SERVER_ERROR_INSUFFICIENT_CREDIT";
    public static String ENDPOINT_RESULT_CODE_ERROR_SECURITY_CERTIFICATE_NOT_FOUND = "SERVER_ERROR_SECURITY_CERTIFICATE_NOT_FOUND";
    public static String ENDPOINT_RESULT_CODE_ERROR_SENDER_DOES_NOT_MATCH_CERTIFICATE_COMMON_NAME = "SERVER_ERROR_SENDER_DOES_NOT_MATCH_CERTIFICATE_COMMON_NAME";
    
    //import esd server errors
    public static String ENDPOINT_RESULT_CODE_ERROR_DATA_IMPORT_MISSING_IMPORT_TYPE = "SERVER_ERROR_DATA_IMPORT_MISSING_IMPORT_TYPE";
    public static String ENDPOINT_RESULT_CODE_ERROR_DATA_IMPORT_MAX_IMPORTS_RUNNING = "SERVER_ERROR_DATA_IMPORT_MAX_IMPORTS_RUNNING";
    public static String ENDPOINT_RESULT_CODE_ERROR_DATA_IMPORT_BUSY = "SERVER_ERROR_DATA_IMPORT_BUSY";
    public static String ENDPOINT_RESULT_CODE_ERROR_DATA_IMPORT_NOT_FOUND = "SERVER_ERROR_DATA_IMPORT_NOT_FOUND"; 
    public static String ENDPOINT_RESULT_CODE_ERROR_DATA_JSON_WRONG_CONTENT_TYPE = "SERVER_ERROR_DATA_JSON_WRONG_CONTENT_TYPE";
    public static String ENDPOINT_RESULT_CODE_ERROR_DATA_JSON_MALFORMED = "SERVER_ERROR_DATA_JSON_MALFORMED";
    public static String ENDPOINT_RESULT_CODE_ERROR_ESD_DOCUMENT_HEADER_MALFORMED = "SERVER_ERROR_ESD_DOCUMENT_HEADER_MALFORMED";
    public static String ENDPOINT_RESULT_CODE_ERROR_ESD_DOCUMENT_HEADER_MISSING_ATTRIBUTES = "SERVER_ERROR_ESD_DOCUMENT_HEADER_MISSING_ATTRIBUTES";
    public static String ENDPOINT_RESULT_CODE_ERROR_DATA_IMPORT_ABORTED = "SERVER_ERROR_DATA_IMPORT_ABORTED";
    public static String ENDPOINT_RESULT_CODE_ERROR_ESD_DOCUMENT_UNSUCCESSFUL = "SERVER_ERROR_ESD_DOCUMENT_UNSUCCESSFUL";
    public static String ENDPOINT_RESULT_CODE_ERROR_ESD_DOCUMENT_NO_RECORD = "SERVER_ERROR_ESD_DOCUMENT_NO_RECORD";
    
    //set default values for the response
    public String result = ENDPOINT_RESULT_FAILURE;
    public String result_code = ENDPOINT_RESULT_CODE_ERROR_UNKNOWN;
    public String result_message = "";
    public String api_version = "1.0.0.0";
    public String session_id = "";
    public String session_valid = "";
}