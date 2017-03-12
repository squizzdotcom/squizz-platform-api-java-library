/**
* Copyright (C) 2017 Squizz PTY LTD
* This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
*/
package org.squizz.api.v1.example;

import org.squizz.api.v1.*;
import org.squizz.api.v1.endpoint.*;
import org.esd.EcommerceStandardsDocuments.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Shows an example of creating a organisation session with the SQUIZZ.com platform's API, then sends a organisation's purchase order data to supplier
 */
public class APIv1ExampleRunnerProcurePurchaseOrderFromSupplier 
{
	public static void main(String[] args)
    {
        //check that the required arguments have been given
        if(args.length < 4){
            System.out.println("Set the following arguments: [orgID] [orgAPIKey] [orgAPIPass] [supplierOrgID]");
            return;
        }
        
		//obtain or load in an organisation's API credentials, in this example from command line arguments
		String orgID = args[0];
		String orgAPIKey = args[1];
		String orgAPIPass = args[2];
        int sessionTimeoutMilliseconds = 20000;
		
		//create an API session instance
		APIv1OrgSession apiOrgSession = new APIv1OrgSession(orgID, orgAPIKey, orgAPIPass, sessionTimeoutMilliseconds, APIv1Constants.SUPPORTED_LOCALES_EN_AU);
		
		//call the platform's API to request that a session is created
		APIv1EndpointResponse endpointResponse = apiOrgSession.createOrgSession();
		
		//check if the organisation's credentials were correct and that a session was created in the platform's API
		if(endpointResponse.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS))
		{
			//session has been created so now can call other API endpoints
			System.out.println("SUCCESS - API session has successfully been created.");
		}
		else
		{
			//session failed to be created
			System.out.println("FAIL - API session failed to be created. Reason: " + endpointResponse.result_message  + " Error Code: " + endpointResponse.result_code);
		}
		
		//sand and procure purchsae order if the API was successfully created
		if(apiOrgSession.sessionExists())
		{
            //create purchase order record to import
            ESDRecordOrderPurchase purchaseOrderRecord = new ESDRecordOrderPurchase();
            
            //set data within the purchase order
            purchaseOrderRecord.keyPurchaseOrderID = "111";
            purchaseOrderRecord.purchaseOrderCode = "POEXAMPLE-345";
            purchaseOrderRecord.purchaseOrderNumber = "345";
            purchaseOrderRecord.purchaseOrderNumber = "345";
            purchaseOrderRecord.instructions = "Leave goods at the back entrance";
            purchaseOrderRecord.keySupplierAccountID = "2";
            purchaseOrderRecord.supplierAccountCode = "ACM-002";
            
            //set delivery address that ordered goods will be delivered to
            purchaseOrderRecord.deliveryAddress1 = "32";
            purchaseOrderRecord.deliveryAddress2 = "Main Street";
            purchaseOrderRecord.deliveryAddress3 = "Melbourne";
            purchaseOrderRecord.deliveryRegionName = "Victoria";
            purchaseOrderRecord.deliveryCountryName = "Australia";
            purchaseOrderRecord.deliveryPostcode = "3000";
            purchaseOrderRecord.deliveryOrgName = "Acme Industries";
            purchaseOrderRecord.deliveryContact = "Jane Doe";
            
            //set billing address that the order will be billed to for payment
            purchaseOrderRecord.billingAddress1 = "43";
            purchaseOrderRecord.billingAddress2 = " High Street";
            purchaseOrderRecord.billingAddress3 = "Melbourne";
            purchaseOrderRecord.billingRegionName = "Victoria";
            purchaseOrderRecord.billingCountryName = "Australia";
            purchaseOrderRecord.billingPostcode = "3000";
            purchaseOrderRecord.billingOrgName = "Acme Industries International";
            purchaseOrderRecord.billingContact = "John Citizen";
            
            //create an array of purchase order lines
            ArrayList<ESDRecordOrderPurchaseLine> orderLines = new ArrayList<ESDRecordOrderPurchaseLine>();
            
            //create purchase order line record
            ESDRecordOrderPurchaseLine orderProduct = new ESDRecordOrderPurchaseLine();
            orderProduct.lineType = ESDocumentConstants.ORDER_LINE_TYPE_PRODUCT;
            orderProduct.productCode = "TEA-TOWEL-GREEN";
            orderProduct.productName = "Green tea towel - 30 x 6 centimetres";
            orderProduct.keySellUnitID = "2";
            orderProduct.unitName = "EACH";
            orderProduct.quantity = 4;
            orderProduct.sellUnitBaseQuantity = 4;
            orderProduct.salesOrderProductCode = "ACME-TTGREEN"; 
            orderProduct.priceExTax = 5.00;
            orderProduct.priceIncTax = 5.50;
            orderProduct.priceTax = 0.50;
            orderProduct.priceTotalIncTax = 22.00;
            orderProduct.priceTotalExTax = 20.00;
            orderProduct.priceTotalTax = 2.00;
            
            //add order line to lines list
            orderLines.add(orderProduct);
            
            //add order lines to the order
            purchaseOrderRecord.lines = orderLines;
		
			//create purchase order records list and add purchase order to it
            ArrayList<ESDRecordOrderPurchase> purchaseOrderRecords = new ArrayList<ESDRecordOrderPurchase>();
			purchaseOrderRecords.add(purchaseOrderRecord);
		
            //specify the supplier organisation based on its ID within the platform, in this example the ID comes from command line arguments
            String supplierOrgID = args[3];
			
			//after 60 seconds give up on waiting for a response from the API when creating the notification
			int timeoutMilliseconds = 60000;
			
			//create purchase order Ecommerce Standards document and add purchse order records to the document
			ESDocumentOrderPurchase orderPurchaseESD = new ESDocumentOrderPurchase(ESDocumentConstants.RESULT_SUCCESS, "successfully obtained data", purchaseOrderRecords.toArray(new ESDRecordOrderPurchase[0]), new HashMap<String, String>());

			//send purchase order document to the API for procurement by the supplier organisation
			APIv1EndpointResponseESD endpointResponseESD = APIv1EndpointOrgProcurePurchaseOrderFromSupplier.call(apiOrgSession, timeoutMilliseconds, supplierOrgID, "", orderPurchaseESD);
			ESDocumentOrderSale esDocumentOrderSale = (ESDocumentOrderSale)endpointResponseESD.esDocument;
            
            //check the result of procuring the purchase orders
            if(endpointResponseESD.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS)){
                System.out.println("SUCCESS - organisation purchase orders have successfully been sent to supplier organisation.");
                
                //iterate through each of the returned sales orders and output the details of the sales orders
                if(esDocumentOrderSale.dataRecords != null){
                    for (ESDRecordOrderSale salesOrderRecord : esDocumentOrderSale.dataRecords){
                        System.out.println("\nSales Order Returned, Order Details: ");
                        System.out.println("Sales Order Code: " + salesOrderRecord.salesOrderCode);
                        System.out.println("Sales Order Total Cost: " + salesOrderRecord.totalPriceIncTax + " (" + salesOrderRecord.currencyISOCode + ")");
                        System.out.println("Sales Order Total Taxes: " + salesOrderRecord.totalTax + " (" + salesOrderRecord.currencyISOCode + ")");
                        System.out.println("Sales Order Customer Account: " + salesOrderRecord.customerAccountCode);
                        System.out.println("Sales Order Total Lines: " + salesOrderRecord.totalLines);
                    }
                }
            }else{
                System.out.println("FAIL - organisation purchase orders failed to be processed. Reason: " + endpointResponseESD.result_message  + " Error Code: " + endpointResponseESD.result_code);
                
                //if one or more products in the purchase order could not match a product for the supplier organisation then find out the order lines caused the problem
                if(endpointResponseESD.result_code.equals(APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_ORDER_PRODUCT_NOT_MAPPED) && esDocumentOrderSale != null)
                {   
                    if(esDocumentOrderSale.configs.containsKey(APIv1EndpointResponseESD.ESD_CONFIG_ORDERS_WITH_UNMAPPED_LINES))
                    {
                        //get comma separated list of order record indicies and line indicies that indicate the unmapped order lines
                        String unmappedOrderLineCSV = esDocumentOrderSale.configs.get(APIv1EndpointResponseESD.ESD_CONFIG_ORDERS_WITH_UNMAPPED_LINES);
                        
                        //get the index of the order record and line that contained the unmapped product
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
                                        
                                        //check that the order can be found that contains the problematic line
                                        if(orderIndex < orderPurchaseESD.dataRecords.length && lineIndex < orderPurchaseESD.dataRecords[orderIndex].lines.size()){
                                            System.out.println("For purchase order: "+ orderPurchaseESD.dataRecords[orderIndex].purchaseOrderCode + " a matching supplier product for line number: " + (lineIndex+1) + " could not be found.");
                                        }
                                        
                                    }catch(Exception ex){
                                        System.out.println("Error when trying to specify the purchase order that contains an unmapped order line. Error: " + ex.getLocalizedMessage());
                                    }
                                }
                            }
                        }
                    }
                }
                //if one or more products in the purchase order could not be priced by the supplier organisation then find the order line that caused the problem
                else if(endpointResponseESD.result_code.equals(APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_ORDER_MAPPED_PRODUCT_PRICE_NOT_FOUND) && esDocumentOrderSale != null)
                {
                    if(esDocumentOrderSale.configs.containsKey(APIv1EndpointResponseESD.ESD_CONFIG_ORDERS_WITH_UNPRICED_LINES))
                    {
                        //get comma separated list of order record indicies and line indicies that indicate the unpriced order lines
                        String unmappedOrderLineCSV = esDocumentOrderSale.configs.get(APIv1EndpointResponseESD.ESD_CONFIG_ORDERS_WITH_UNPRICED_LINES);
                        
                        //get the index of the order record and line that contained the unpriced product
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
                                        
                                        //check that the order can be found that contains the problematic line
                                        if(orderIndex < orderPurchaseESD.dataRecords.length && lineIndex < orderPurchaseESD.dataRecords[orderIndex].lines.size()){
                                            System.out.println("For purchase order: "+ orderPurchaseESD.dataRecords[orderIndex].purchaseOrderCode + " the supplier has not set pricing for line number: " + (lineIndex+1));
                                        }
                                        
                                    }catch(Exception ex){
                                        System.out.println("Error when trying to specify the purchase order that contains an unpriced order line. Error: " + ex.getLocalizedMessage());
                                    }
                                }
                            }
                        }
                    }
                }
            }
		}
		
		//next steps
		//call other API endpoints...
		//destroy API session when done...
    }
}
