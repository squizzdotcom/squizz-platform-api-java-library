/**
* Copyright (C) 2019 Squizz PTY LTD
* This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
*/
package org.squizz.api.v1.endpoint;

import org.esd.EcommerceStandardsDocuments.ESDocument;
import org.esd.EcommerceStandardsDocuments.ESDocumentSupplierInvoice;
import org.esd.EcommerceStandardsDocuments.ESDocumentCustomerInvoice;
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
 * Class handles calling the SQUIZZ.com API endpoint to send one more of an organisation's customer invoices to the platform, where they are then converted into supplier invoices and sent to a customer organisation for importing and processing.
 * This endpoint allows the invoicing of goods and services supplied by a supplying organisation logged into the API session. to be sent their chosen customer organisation
 */
public class APIv1EndpointOrgSendCustomerInvoiceToCustomer 
{
    /**
     * Calls the platform's API endpoint and pushes up and import organisation data in a Ecommerce Standards Document of a specified type
     * @param apiOrgSession existing organisation API session
     * @param endpointTimeoutMilliseconds amount of milliseconds to wait after calling the the API before giving up, set a positive number
     * @param customerOrgID unique ID of the customer organisation in the SQUIZZ.com platform
     * @param supplierAccountCode code of the customer organisation's supplier account. Supplier account only needs to be set if the customer organisation has assigned multiple accounts to the supplier organisation logged into the API session (supplier org)
     * @param esDocumentCustomerInvoice Customer Invoice Ecommerce Standards Document that contains one or more customer invoice records
     * @return response from calling the API endpoint
     */
    public static APIv1EndpointResponseESD call(APIv1OrgSession apiOrgSession, int endpointTimeoutMilliseconds, String customerOrgID, String supplierAccountCode, ESDocumentCustomerInvoice esDocumentCustomerInvoice)
	{
		ArrayList<Pair<String, String>> requestHeaders = new ArrayList<>();
		APIv1EndpointResponseESD endpointResponse = new APIv1EndpointResponseESD();

		try
		{
			//set notification parameters
			String endpointParams = "customer_org_id="+ URLEncoder.encode(customerOrgID, StandardCharsets.UTF_8.name()) + "&supplier_account_code="+URLEncoder.encode(supplierAccountCode, StandardCharsets.UTF_8.name());
			
			//create JSON deserializer to interpret the response from the endpoint
            ObjectMapper jsonMapper = new ObjectMapper();
            jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            ObjectReader endpointJSONReader = jsonMapper.readerFor(ESDocumentSupplierInvoice.class);

			//make a HTTP request to the platform's API endpoint to send the ESD containing the customer invoices
			endpointResponse = APIv1HTTPRequest.sendESDocumentHTTPRequest(APIv1Constants.HTTP_REQUEST_METHOD_POST, APIv1Constants.API_ORG_ENDPOINT_SEND_CUSTOMER_INVOICE_TO_CUSTOMER+APIv1Constants.API_PATH_SLASH+apiOrgSession.getSessionID(), endpointParams, requestHeaders, "", esDocumentCustomerInvoice, endpointTimeoutMilliseconds, apiOrgSession.getLangBundle(), endpointJSONReader, endpointResponse);
				
			//check that the data was successfully pushed up
			if(!endpointResponse.result.equalsIgnoreCase(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS))
			{
				//check if the session still exists
				if(endpointResponse.result.equalsIgnoreCase(APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_SESSION_INVALID)){
					//mark that the session has expired
					apiOrgSession.markSessionExpired();
				}
			}
		}
		catch (Exception ex)
		{
			endpointResponse.result = APIv1EndpointResponse.ENDPOINT_RESULT_FAILURE;
			endpointResponse.result_code = APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_UNKNOWN;
			endpointResponse.result_message = apiOrgSession.getLangBundle().getString(endpointResponse.result_code) + "\n" + ex.getLocalizedMessage();
		}

		return endpointResponse;
	}
    
    /**
     * gets a list of invoice indexes that contain invoice lines that could not be mapped to a customer organisation's products
     * @param esDocument Ecommerce standards document containing configuration that specifies unmapped invoice lines
     * @return an array containing pairs. Each pair has the index of the invoice, and the index of the invoice line that could not be mapped
     */
    public static ArrayList<Pair<Integer, Integer>> getUnmappedInvoiceLines(ESDocument esDocument)
    {
        ArrayList<Pair<Integer, Integer>> upmappedInvoiceLines = new ArrayList<>();
        
        //check that the ecommerce standards document's configs contains a key specifying the unmapped invoice lines
        if(esDocument.configs.containsKey(APIv1EndpointResponseESD.ESD_CONFIG_INVOICES_WITH_UNMAPPED_LINES))
        {
            //get comma separated list of invoice record indicies and line indicies that indicate the unmapped invoice lines
            String unmappedInvoiceLineCSV = esDocument.configs.get(APIv1EndpointResponseESD.ESD_CONFIG_INVOICES_WITH_UNMAPPED_LINES);

            //get the index of the invoice record and line that contained the unmapped product
            if(!unmappedInvoiceLineCSV.trim().isEmpty()){
                String[] unmappedInvoiceLineIndices = unmappedInvoiceLineCSV.trim().split(",");

                //iterate through each invoice-line index
                for(int i=0; i < unmappedInvoiceLineIndices.length; i++){
                    //get invoice index and line index
                    String[] invoiceLineIndex = unmappedInvoiceLineIndices[i].split(":");
                    if(invoiceLineIndex.length == 2){
                        try{
                            int invoiceIndex = Integer.parseInt(invoiceLineIndex[0]);
                            int lineIndex = Integer.parseInt(invoiceLineIndex[1]);
                            Pair<Integer, Integer> invoiceLinePair = new Pair<>(invoiceIndex, lineIndex);
                            upmappedInvoiceLines.add(invoiceLinePair);

                        }catch(Exception ex){
                        }
                    }
                }
            }
        }
        
        return upmappedInvoiceLines;
    }
}
