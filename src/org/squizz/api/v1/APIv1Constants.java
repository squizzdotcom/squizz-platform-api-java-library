/**
* Copyright (C) 2017 Squizz PTY LTD
* This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
*/
package org.squizz.api.v1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

/**
 * Stores constant variables required for accessing the platform's API
 */
public class APIv1Constants
{
    public static final String API_PROTOCOL = "https://";
    public static final String API_DOMAIN = "api.squizz.com";
    public static final String API_ORG_PATH = "/rest/1/org/";
    public static final String API_PATH_SLASH = "/";
    public static final String API_ORG_ENDPOINT_CREATE_SESSION = "create_session";
    public static final String API_ORG_ENDPOINT_DESTROY_SESSION = "destroy_session";
    public static final String API_ORG_ENDPOINT_VALIDATE_SESSION = "validate_session";
    public static final String API_ORG_ENDPOINT_CREATE_NOTIFCATION = "create_notification";
    public static final String API_ORG_ENDPOINT_VALIDATE_CERT = "validate_cert";
    public static final String API_ORG_ENDPOINT_IMPORT_ESD = "import_esd";
    public static final String API_ORG_ENDPOINT_PROCURE_PURCHASE_ORDER_FROM_SUPPLIER = "procure_purchase_order_from_supplier";
    
    public static final String HTTP_REQUEST_METHOD_POST = "POST";
    public static final String HTTP_REQUEST_METHOD_GET = "GET";
    public static final String HTTP_REQUEST_CONTENT_TYPE = "Content-Type";
    public static final String HTTP_REQUEST_CONTENT_TYPE_JSON = "application/json";
    
    public static final Locale SUPPORTED_LOCALES_EN_AU = new Locale("en", "AU");
    
    public static final String LANG_BUNDLE_NAME = "org.squizz.api.v1.lang.APIv1LangBundle";
}
