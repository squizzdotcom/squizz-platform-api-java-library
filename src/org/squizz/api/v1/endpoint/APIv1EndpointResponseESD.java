/**
* Copyright (C) Squizz PTY LTD
* This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
*/
package org.squizz.api.v1.endpoint;
import org.esd.EcommerceStandardsDocuments.ESDocument;
import static org.squizz.api.v1.endpoint.APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_UNKNOWN;
import static org.squizz.api.v1.endpoint.APIv1EndpointResponse.ENDPOINT_RESULT_FAILURE;

/**
 * Represents the response returned from the procure purchase order from supplier endpoint in the platform's API
 * @param <T> Ecommerce Standards Document obtained from the endpoint response
 */
public class APIv1EndpointResponseESD
{
    public static final String ESD_CONFIG_ORDERS_WITH_UNMAPPED_LINES = "orders_with_unmapped_lines";
	public static final String ESD_CONFIG_ORDERS_WITH_UNMATCHED_LINES = "orders_with_unmatched_lines";
    public static final String ESD_CONFIG_ORDERS_WITH_UNPRICED_LINES = "orders_with_unpriced_lines";
    public static final String ESD_CONFIG_ORDERS_WITH_UNSTOCKED_LINES = "orders_with_unstocked_lines";
	public static final String ESD_CONFIG_ORDERS_WITH_UNMATCHED_SURCHARGES = "orders_with_unmatched_surcharges";
	public static final String ESD_CONFIG_ORDERS_WITH_UNPRICED_SURCHARGES = "orders_with_unpriced_surcharges";
	public static final String ESD_CONFIG_ORDERS_WITH_UNMATCHED_PAYMENTS = "orders_with_unmatched_payments";
	public static final String ESD_CONFIG_INVOICES_WITH_UNMAPPED_LINES = "invoices_with_unmapped_lines";
	public static final String ESD_CONFIG_INVOICES_WITH_UNMAPPED_SURCHARGES = "invoices_with_unmapped_surcharges";
	public static final String ESD_CONFIG_INVOICES_WITH_UNMAPPED_LINE_TAXCODES = "invoices_with_unmapped_line_taxcodes";
	public static final String ESD_CONFIG_INVOICES_WITH_UNMAPPED_SURCHARGE_TAXCODES = "invoices_with_unmapped_surcharge_taxcodes";
    
    //set default values for the response
    public String result = ENDPOINT_RESULT_FAILURE;
    public String result_code = ENDPOINT_RESULT_CODE_ERROR_UNKNOWN;
    public String result_message = "";
    public String api_version = "1.0.0.0";
    public String session_id = "";
    public String session_valid = "";
    public ESDocument esDocument;
}
