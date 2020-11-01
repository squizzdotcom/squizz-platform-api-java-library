/**
* Copyright (C) Squizz PTY LTD
* This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
*/
package org.squizz.api.v1.endpoint;

import org.esd.EcommerceStandardsDocuments.ESDocument;
import org.esd.EcommerceStandardsDocuments.ESDocumentOrderPurchase;
import org.esd.EcommerceStandardsDocuments.ESDocumentOrderSale;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import javafx.util.Pair;
import org.esd.EcommerceStandardsDocuments.ESDocumentConstants;
import org.squizz.api.v1.APIv1Constants;
import org.squizz.api.v1.APIv1HTTPRequest;
import org.squizz.api.v1.APIv1OrgSession;

/**
 * Class handles calling the SQUIZZ.com API endpoint to send one more of an organisation's sales orders into the platform, where they are then validated, optionally re-priced and raised against the organisation for processing and dispatch.
 * This endpoint allows an organisation to import its own sales orders from its own selling system(s) into SQUIZZ.com, via an API session that the organisation has logged into
 */
public class APIv1EndpointOrgImportSalesOrder 
{
    /**
     * Calls the platform's API endpoint and pushes up and import organisation sales order record in a Ecommerce Standards Document of a specified type
     * @param apiOrgSession existing organisation API session
     * @param endpointTimeoutMilliseconds amount of milliseconds to wait after calling the the API before giving up, set a positive number
     * @param esDocumentOrderSale Sales Order Ecommerce Standards Document that contains one or more sales order records
	 * @param repriceOrder if true then allow the order lines and surcharges to be repriced on import
     * @return response from calling the API endpoint
     */
    public static APIv1EndpointResponseESD call(APIv1OrgSession apiOrgSession, int endpointTimeoutMilliseconds, ESDocumentOrderSale esDocumentOrderSale, boolean repriceOrder)
    {
        ArrayList<Pair<String, String>> requestHeaders = new ArrayList<>();
        APIv1EndpointResponseESD endpointResponse = new APIv1EndpointResponseESD();
        
        try{
            //set notification parameters
            String endpointParams = "reprice_order="+ (repriceOrder? ESDocumentConstants.ESD_VALUE_YES: ESDocumentConstants.ESD_VALUE_NO);
            
            //create JSON deserializer to interpret the response from the endpoint
            ObjectMapper jsonMapper = new ObjectMapper();
            jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            ObjectReader endpointJSONReader = jsonMapper.readerFor(ESDocumentOrderSale.class);
            
            //make a HTTP request to the platform's API endpoint to send the ESD containing the sales orders
            endpointResponse = APIv1HTTPRequest.sendESDocumentHTTPRequest(APIv1Constants.HTTP_REQUEST_METHOD_POST, APIv1Constants.API_ORG_ENDPOINT_SALES_ORDER_ESD+APIv1Constants.API_PATH_SLASH+apiOrgSession.getSessionID(), endpointParams, requestHeaders, "", esDocumentOrderSale, endpointTimeoutMilliseconds, apiOrgSession.getLangBundle(), endpointJSONReader, endpointResponse);
            
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
        catch(Exception ex)
        {
            endpointResponse.result = APIv1EndpointResponse.ENDPOINT_RESULT_FAILURE;
            endpointResponse.result_code = APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_UNKNOWN;
			endpointResponse.result_message = apiOrgSession.getLangBundle().getString(endpointResponse.result_code) + "\n" + ex.getLocalizedMessage();
        }
        
        return endpointResponse;
    }
    
    /**
     * gets a list of order indexes that contain order lines that could not be matched up to the organisation's own products
     * @param esDocument Ecommerce standards document containing configuration that specifies unmatched order lines
     * @return an array containing pairs. Each pair has the index of the order, and the index of the order line that could not be mapped
     */
    public static ArrayList<Pair<Integer, Integer>> getUnmatchedOrderLines(ESDocument esDocument)
    {
        ArrayList<Pair<Integer, Integer>> upmappedOrderLines = new ArrayList<>();
        
        //check that the ecommerce standards document's configs contains a key specifying the unmatcjed order lines
        if(esDocument.configs.containsKey(APIv1EndpointResponseESD.ESD_CONFIG_ORDERS_WITH_UNMATCHED_LINES))
        {
            //get comma separated list of order record indicies and line indicies that indicate the unmatched order lines
            String unmappedOrderLineCSV = esDocument.configs.get(APIv1EndpointResponseESD.ESD_CONFIG_ORDERS_WITH_UNMATCHED_LINES);

            //get the index of the order record and line that contained the unmatched product
            if(!unmappedOrderLineCSV.trim().isEmpty()){
                String[] unmappedOrderLineIndices = unmappedOrderLineCSV.trim().split(",");

                //iterate through each order-line index
                for(int i=0; i < unmappedOrderLineIndices.length; i++){
                    //get order index and line index
                    String[] orderLineIndex = unmappedOrderLineIndices[i].split(":");
                    if(orderLineIndex.length == 2){
                        try{
                            int orderIndex = Integer.parseInt(orderLineIndex[0]);
                            int lineIndex = Integer.parseInt(orderLineIndex[1]);
                            Pair<Integer, Integer> orderLinePair = new Pair<>(orderIndex, lineIndex);
                            upmappedOrderLines.add(orderLinePair);

                        }catch(Exception ex){
                        }
                    }
                }
            }
        }
        
        return upmappedOrderLines;
    }
    
    /**
     * gets a list of order indexes that contain order lines that could not be priced for the organisation's own products
     * @param esDocument Ecommerce standards document containing configuration that specifies unpriced order lines
     * @return an array containing pairs. Each pair has the index of the order, and the index of the order line that could not be priced
     */
    public static ArrayList<Pair<Integer, Integer>> getUnpricedOrderLines(ESDocument esDocument)
    {
        ArrayList<Pair<Integer, Integer>> unpricedOrderLines = new ArrayList<>();
        
        //check that the ecommerce standards document's configs contains a key specifying the unpriced order lines
        if(esDocument.configs.containsKey(APIv1EndpointResponseESD.ESD_CONFIG_ORDERS_WITH_UNPRICED_LINES))
        {
            //get comma separated list of order record indicies and line indicies that indicate the unpriced order lines
            String unpricedOrderLineCSV = esDocument.configs.get(APIv1EndpointResponseESD.ESD_CONFIG_ORDERS_WITH_UNPRICED_LINES);

            //get the index of the order record and line that contained the unpriced product
            if(!unpricedOrderLineCSV.trim().isEmpty()){
                String[] unmappedOrderLineIndices = unpricedOrderLineCSV.trim().split(",");

                //iterate through each order-line index
                for(int i=0; i < unmappedOrderLineIndices.length; i++){
                    //get order index and line index
                    String[] orderLineIndex = unmappedOrderLineIndices[i].split(":");
                    if(orderLineIndex.length == 2){
                        try{
                            int orderIndex = Integer.parseInt(orderLineIndex[0]);
                            int lineIndex = Integer.parseInt(orderLineIndex[1]);
                            Pair<Integer, Integer> orderLinePair = new Pair<>(orderIndex, lineIndex);
                            unpricedOrderLines.add(orderLinePair);

                        }catch(Exception ex){
                        }
                    }
                }
            }
        }
        
        return unpricedOrderLines;
    }
	
	/**
     * gets a list of order indexes that contain order surcharges that could not be matched to the organisation's own surcharges
     * @param esDocument Ecommerce standards document containing configuration that specifies unmatched order surcharges
     * @return an array containing pairs. Each pair has the index of the order, and the index of the order surcharge that could not be matched
     */
    public static ArrayList<Pair<Integer, Integer>> getUnmatchedOrderSurcharges(ESDocument esDocument)
    {
        ArrayList<Pair<Integer, Integer>> unmatchedOrderSurcharges = new ArrayList<>();
        
        //check that the ecommerce standards document's configs contains a key specifying the unmapped order surcharges
        if(esDocument.configs.containsKey(APIv1EndpointResponseESD.ESD_CONFIG_ORDERS_WITH_UNMATCHED_SURCHARGES))
        {
            //get comma separated list of order record indicies and surcharge record indicies that indicate the unmatched order surcharge
            String unmappedOrderSurchargeCSV = esDocument.configs.get(APIv1EndpointResponseESD.ESD_CONFIG_ORDERS_WITH_UNMATCHED_SURCHARGES);

            //get the index of the order record and surcharge that contained the unmatched surcharge
            if(!unmappedOrderSurchargeCSV.trim().isEmpty()){
                String[] unmappedOrderSurchargeIndices = unmappedOrderSurchargeCSV.trim().split(",");

                //iterate through each order-surcharge index
                for(int i=0; i < unmappedOrderSurchargeIndices.length; i++){
                    //get order index and surcharge index
                    String[] orderSurchargeIndex = unmappedOrderSurchargeIndices[i].split(":");
                    if(orderSurchargeIndex.length == 2){
                        try{
                            int orderIndex = Integer.parseInt(orderSurchargeIndex[0]);
                            int surcahrgeRecordIndex = Integer.parseInt(orderSurchargeIndex[1]);
                            Pair<Integer, Integer> orderSurchargePair = new Pair<>(orderIndex, surcahrgeRecordIndex);
                            unmatchedOrderSurcharges.add(orderSurchargePair);
                        }
						catch(Exception ex){
                        }
                    }
                }
            }
        }
        
        return unmatchedOrderSurcharges;
    }
	
	/**
     * gets a list of order indexes that contain order surcharges that could not be priced for the organisation's own surcharges
     * @param esDocument Ecommerce standards document containing configuration that specifies unpriced order surcharges
     * @return an array containing pairs. Each pair has the index of the order, and the index of the order surcharge that could not be priced
     */
    public static ArrayList<Pair<Integer, Integer>> getUnpricedOrderSurcharges(ESDocument esDocument)
    {
        ArrayList<Pair<Integer, Integer>> unpricedOrderSurcharges = new ArrayList<>();
        
        //check that the ecommerce standards document's configs contains a key specifying the unpriced order surcharges
        if(esDocument.configs.containsKey(APIv1EndpointResponseESD.ESD_CONFIG_ORDERS_WITH_UNPRICED_SURCHARGES))
        {
            //get comma separated list of order record indicies and surcharge indicies that indicate the unpriced order surcharges
            String unpricedOrderSurchargeCSV = esDocument.configs.get(APIv1EndpointResponseESD.ESD_CONFIG_ORDERS_WITH_UNPRICED_SURCHARGES);

            //get the index of the order record and surcharge that contained the unpriced product
            if(!unpricedOrderSurchargeCSV.trim().isEmpty()){
                String[] unmappedOrderSurchargeIndices = unpricedOrderSurchargeCSV.trim().split(",");

                //iterate through each order-surcharge index
                for(int i=0; i < unmappedOrderSurchargeIndices.length; i++){
                    //get order index and surcharge index
                    String[] orderSurchargeIndex = unmappedOrderSurchargeIndices[i].split(":");
                    if(orderSurchargeIndex.length == 2){
                        try{
                            int orderIndex = Integer.parseInt(orderSurchargeIndex[0]);
                            int surchargeIndex = Integer.parseInt(orderSurchargeIndex[1]);
                            Pair<Integer, Integer> orderSurchargePair = new Pair<>(orderIndex, surchargeIndex);
                            unpricedOrderSurcharges.add(orderSurchargePair);

                        }catch(Exception ex){
                        }
                    }
                }
            }
        }
        
        return unpricedOrderSurcharges;
    }
	
	/**
     * gets a list of order indexes that contain order payments that could not be matched to the organisation's own payment types
     * @param esDocument Ecommerce standards document containing configuration that specifies unmatched order payments
     * @return an array containing pairs. Each pair has the index of the order, and the index of the order payment that could not be matched
     */
    public static ArrayList<Pair<Integer, Integer>> getUnmatchedOrderPayments(ESDocument esDocument)
    {
        ArrayList<Pair<Integer, Integer>> unmatchedOrderPayments = new ArrayList<>();
        
        //check that the ecommerce standards document's configs contains a key specifying the unmapped order paymenys
        if(esDocument.configs.containsKey(APIv1EndpointResponseESD.ESD_CONFIG_ORDERS_WITH_UNMATCHED_PAYMENTS))
        {
            //get comma separated list of order record indicies and payment record indicies that indicate the unmatched order payment
            String unmappedOrderPaymentCSV = esDocument.configs.get(APIv1EndpointResponseESD.ESD_CONFIG_ORDERS_WITH_UNMATCHED_PAYMENTS);

            //get the index of the order record and payment that contained the unmatched payment
            if(!unmappedOrderPaymentCSV.trim().isEmpty()){
                String[] unmappedOrderPaymentIndices = unmappedOrderPaymentCSV.trim().split(",");

                //iterate through each order-payment index
                for(int i=0; i < unmappedOrderPaymentIndices.length; i++){
                    //get order index and payment index
                    String[] orderPaymentIndex = unmappedOrderPaymentIndices[i].split(":");
                    if(orderPaymentIndex.length == 2){
                        try{
                            int orderIndex = Integer.parseInt(orderPaymentIndex[0]);
                            int paymentRecordIndex = Integer.parseInt(orderPaymentIndex[1]);
                            Pair<Integer, Integer> orderPaymentPair = new Pair<>(orderIndex, paymentRecordIndex);
                            unmatchedOrderPayments.add(orderPaymentPair);
                        }
						catch(Exception ex){
                        }
                    }
                }
            }
        }
        
        return unmatchedOrderPayments;
    }
}