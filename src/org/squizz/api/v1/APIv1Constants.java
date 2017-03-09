/**
* Copyright (C) 2017 Squizz PTY LTD
* This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
*/
package org.squizz.api.v1;

import java.util.Locale;

/**
 * Stores constant variables required for accessing the platform's API
 */
public class APIv1Constants
{
    /**
     * HTTP protocol to use when calling the platform's API
     */
    public static final String API_PROTOCOL = "http://";
    
    /**
     * internet domain to use when calling the platform's API
     */
    public static final String API_DOMAIN = "api.squizz.com";
    
    /**
     * URL path within the platform's API to call organisation endpoints
     */
    public static final String API_ORG_PATH = "/rest/1/org/";
    
    /**
     * slash character to set within URL path to API endpoint requests
     */
    public static final String API_PATH_SLASH = "/";
    
    /**
     * name of the platform's API endpoint to call to create a session in the API for an organisation
     */
    public static final String API_ORG_ENDPOINT_CREATE_SESSION = "create_session";
    
    /**
     * name of the platform's API endpoint to call to destroy a session in the API for an organisation
     */
    public static final String API_ORG_ENDPOINT_DESTROY_SESSION = "destroy_session";
    
    /**
     * name of the platform's API endpoint to call to validate a session in the API for an organisation
     */
    public static final String API_ORG_ENDPOINT_VALIDATE_SESSION = "validate_session";
    
    /**
     * name of the platform's API endpoint to call to create an organisation notification
     */
    public static final String API_ORG_ENDPOINT_CREATE_NOTIFCATION = "create_notification";
    
    /**
     * name of the platform's API endpoint to call to create an organisation notification
     */
    public static final String API_ORG_ENDPOINT_VALIDATE_CERT = "validate_cert";
    
    /**
     * name of the platform's API endpoint to call to import organisation data stored within an Ecommerce Standards Documents
     */
    public static final String API_ORG_ENDPOINT_IMPORT_ESD = "import_esd";
    
    /**
     * name of the platform's API endpoint to call to send a purchase order to a supplier organisation for procurement
     */
    public static final String API_ORG_ENDPOINT_PROCURE_PURCHASE_ORDER_FROM_SUPPLIER = "procure_purchase_order_from_supplier";
    
    /**
     * name of the endpoint attribute in the API endpoint response that contains the result code
     */
    public static final String API_ORG_ENDPOINT_ATTRIBUTE_RESULT_CODE = "result_code";
    
    /**
     * HTTP request method used to post data
     */
    public static final String HTTP_REQUEST_METHOD_POST = "POST";
    
    /**
     * HTTP request method to get data
     */
    public static final String HTTP_REQUEST_METHOD_GET = "GET";
    
    /**
     * HTTP request content type header name
     */
    public static final String HTTP_REQUEST_CONTENT_TYPE = "Content-Type";
    
    /**
     * HTTP request content type header for specifying that the request body consists of JSON data
     */
    public static final String HTTP_REQUEST_CONTENT_TYPE_JSON = "application/json";
    
    /**
     * English australian locale that the API supports returning messages in
     */
    public static final Locale SUPPORTED_LOCALES_EN_AU = new Locale("en", "AU");
    
    /**
     * Name of the package that contains the language bundles used for storing locale languages
     */
    public static final String LANG_BUNDLE_NAME = "org.squizz.api.v1.lang.APIv1LangBundle";
}
