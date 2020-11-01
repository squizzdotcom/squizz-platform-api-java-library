/**
* Copyright (C) Squizz PTY LTD
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
import javafx.util.Pair;

/**
 * Shows an example of creating a organisation session with the SQUIZZ.com platform's API, then imports a organisation's sales order record into the SQUIZZ.com platform
 */
public class APIv1ExampleRunnerImportOrgESDDataOrderSales 
{
	public static void main(String[] args)
    {
        //check that the required arguments have been given
        if(args.length < 4){
            System.out.println("Set the following arguments: [orgID] [orgAPIKey] [orgAPIPass] [rePriceOrder]");
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
		
		//import sales order if the API was successfully created
		if(apiOrgSession.sessionExists())
		{
            //create sales order record to import
            ESDRecordOrderSale salesOrderRecord = new ESDRecordOrderSale();
            
            //set data within the sales order
            salesOrderRecord.keySalesOrderID = "111";
            salesOrderRecord.salesOrderCode = "SOEXAMPLE-678";
            salesOrderRecord.salesOrderNumber = "678";
            salesOrderRecord.salesOrderNumber = "678";
            salesOrderRecord.instructions = "Leave goods at the back entrance";
            salesOrderRecord.keyCustomerAccountID = "3";
            salesOrderRecord.customerAccountCode = "CUS-003";
			salesOrderRecord.customerAccountName = "Acme Industries";
			salesOrderRecord.customerEntity = ESDocumentConstants.ENTITY_TYPE_ORG;
			salesOrderRecord.customerOrgName = "Acme Industries Pty Ltd";
            
            //set delivery address that ordered goods will be delivered to
            salesOrderRecord.deliveryAddress1 = "32";
            salesOrderRecord.deliveryAddress2 = "Main Street";
            salesOrderRecord.deliveryAddress3 = "Melbourne";
            salesOrderRecord.deliveryRegionName = "Victoria";
            salesOrderRecord.deliveryCountryName = "Australia";
            salesOrderRecord.deliveryPostcode = "3000";
            salesOrderRecord.deliveryOrgName = "Acme Industries";
            salesOrderRecord.deliveryContact = "Jane Doe";
            
            //set billing address that the order will be billed to for payment
            salesOrderRecord.billingAddress1 = "43";
            salesOrderRecord.billingAddress2 = " High Street";
            salesOrderRecord.billingAddress3 = "Melbourne";
            salesOrderRecord.billingRegionName = "Victoria";
            salesOrderRecord.billingCountryName = "Australia";
            salesOrderRecord.billingPostcode = "3000";
            salesOrderRecord.billingOrgName = "Acme Industries International";
            salesOrderRecord.billingContact = "John Citizen";
            
            //create an array of sales order lines
            ArrayList<ESDRecordOrderSaleLine> orderLines = new ArrayList<>();
            
            //create sales order line record
            ESDRecordOrderSaleLine orderProduct = new ESDRecordOrderSaleLine();
            orderProduct.lineType = ESDocumentConstants.ORDER_LINE_TYPE_PRODUCT;
            orderProduct.productCode = "TEA-TOWEL-GREEN";
            orderProduct.productName = "Green tea towel - 30 x 6 centimetres";
            orderProduct.unitName = "EACH";
			orderProduct.keySellUnitID = "EA";
            orderProduct.quantity = 4;
            orderProduct.sellUnitBaseQuantity = 4;
            //pricing data only needs to be set if the order isn't being repriced
            orderProduct.priceExTax = 5.00;
            orderProduct.priceIncTax = 5.50;
            orderProduct.priceTax = 0.50;
            orderProduct.priceTotalIncTax = 22.00;
            orderProduct.priceTotalExTax = 20.00;
            orderProduct.priceTotalTax = 2.00;
            orderLines.add(orderProduct);
			
			//add taxes to the line
			orderProduct.taxes = new ArrayList<>();
			ESDRecordOrderLineTax orderProductTax = new ESDRecordOrderLineTax();
			orderProductTax.keyTaxcodeID = "TAXCODE-1";
			orderProductTax.taxcode = "GST";
			orderProductTax.taxcodeLabel = "Goods And Services Tax";
			//pricing data only needs to be set if the order isn't being repriced
			orderProductTax.priceTax = 0.50;
			orderProductTax.taxRate = 10;
			orderProductTax.quantity = 4;
			orderProductTax.priceTotalTax = 2.00;
			orderProduct.taxes.add(orderProductTax);
            
            //add a 2nd sales order line record that is a text line
            orderProduct = new ESDRecordOrderSaleLine();
            orderProduct.lineType = ESDocumentConstants.ORDER_LINE_TYPE_TEXT;
            orderProduct.productCode = "TEA-TOWEL-BLUE";
            orderProduct.textDescription = "Please bundle tea towels into a box";
            orderLines.add(orderProduct);
            
            //add a 3rd sales order line record
            orderProduct = new ESDRecordOrderSaleLine();
            orderProduct.lineType = ESDocumentConstants.ORDER_LINE_TYPE_PRODUCT;
            orderProduct.productCode = "TEA-TOWEL-BLUE";
            orderProduct.productName = "Blue tea towel - 30 x 6 centimetres";
			orderProduct.unitName = "BOX";
			orderProduct.keySellUnitID = "BOX-OF-10";
            orderProduct.quantity = 2;
			orderProduct.sellUnitBaseQuantity = 20;
			//pricing data only needs to be set if the order isn't being repriced
            orderProduct.priceExTax = 10.00;
            orderProduct.priceIncTax = 11.00;
            orderProduct.priceTax = 1.00;
            orderProduct.priceTotalIncTax = 22.00;
            orderProduct.priceTotalExTax = 20.00;
            orderProduct.priceTotalTax = 2.00;
            orderLines.add(orderProduct);
			
			//add taxes to the line
			orderProduct.taxes = new ArrayList<>();
			orderProductTax = new ESDRecordOrderLineTax();
			orderProductTax.keyTaxcodeID = "TAXCODE-1";
			orderProductTax.taxcode = "GST";
			orderProductTax.taxcodeLabel = "Goods And Services Tax";
			orderProductTax.quantity = 2;
			//pricing data only needs to be set if the order isn't being repriced
			orderProductTax.priceTax = 1.00;
			orderProductTax.taxRate = 10;
			orderProductTax.priceTotalTax = 2.00;
			orderProduct.taxes.add(orderProductTax);
            
            //add order lines to the order
            salesOrderRecord.lines = orderLines;
			
			//create an array of sales order surcharges
            ArrayList<ESDRecordOrderSurcharge> orderSurcharges = new ArrayList<>();
            
            //create sales order surcharge record
            ESDRecordOrderSurcharge orderSurcharge = new ESDRecordOrderSurcharge();
            orderSurcharge.surchargeCode = "FREIGHT-FEE";
            orderSurcharge.surchargeLabel = "Freight Surcharge";
			orderSurcharge.surchargeDescription = "Cost of freight delivery";
			orderSurcharge.keySurchargeID = "SURCHARGE-1";
			//pricing data only needs to be set if the order isn't being repriced
            orderSurcharge.priceExTax = 3.00;
            orderSurcharge.priceIncTax = 3.30;
            orderSurcharge.priceTax = 0.30;
            orderSurcharges.add(orderSurcharge);
			
			//add taxes to the surcharge
			orderSurcharge.taxes = new ArrayList<>();
			ESDRecordOrderLineTax orderSurchargeTax = new ESDRecordOrderLineTax();
			orderSurchargeTax.keyTaxcodeID = "TAXCODE-1";
			orderSurchargeTax.taxcode = "GST";
			orderSurchargeTax.taxcodeLabel = "Goods And Services Tax";
			orderSurchargeTax.quantity = 1;
			//pricing data only needs to be set if the order isn't being repriced
			orderSurchargeTax.priceTax = 0.30;
			orderSurchargeTax.taxRate = 10;
			orderSurchargeTax.priceTotalTax = 0.30;
			orderSurcharge.taxes.add(orderSurchargeTax);
			
			//create 2nd sales order surcharge record
            orderSurcharge = new ESDRecordOrderSurcharge();
            orderSurcharge.surchargeCode = "PAYMENT-FEE";
            orderSurcharge.surchargeLabel = "Credit Card Surcharge";
			orderSurcharge.surchargeDescription = "Cost of Credit Card Payment";
			orderSurcharge.keySurchargeID = "SURCHARGE-2";
			//pricing data only needs to be set if the order isn't being repriced
            orderSurcharge.priceExTax = 5.00;
            orderSurcharge.priceIncTax = 5.50;
            orderSurcharge.priceTax = 0.50;
            orderSurcharges.add(orderSurcharge);
			
			//add taxes to the 2nd surcharge
			orderSurcharge.taxes = new ArrayList<>();
			orderSurchargeTax = new ESDRecordOrderLineTax();
			orderSurchargeTax.keyTaxcodeID = "TAXCODE-1";
			orderSurchargeTax.taxcode = "GST";
			orderSurchargeTax.taxcodeLabel = "Goods And Services Tax";
			//pricing data only needs to be set if the order isn't being repriced
			orderSurchargeTax.priceTax = 0.50;
			orderSurchargeTax.taxRate = 10;
			orderSurchargeTax.quantity = 1;
			orderSurchargeTax.priceTotalTax = 5.00;
			orderSurcharge.taxes.add(orderSurchargeTax);
			
			//add surcharges to the order
            salesOrderRecord.surcharges = orderSurcharges;
			
			//create an array of sales order payments
            ArrayList<ESDRecordOrderPayment> orderPayments = new ArrayList<>();
            
            //create sales order payment record
            ESDRecordOrderPayment orderPayment = new ESDRecordOrderPayment();
            orderPayment.paymentMethod = ESDocumentConstants.PAYMENT_METHOD_CREDIT;
            orderPayment.paymentProprietaryCode = "Freight Surcharge";
			orderPayment.paymentReceipt = "3422ads2342233";
			orderPayment.keyPaymentTypeID = "VISA-CREDIT-CARD";
            orderPayment.paymentAmount = 11.80;
            orderPayments.add(orderPayment);
			
			//create 2nd sales order payment record
            orderPayment = new ESDRecordOrderPayment();
            orderPayment.paymentMethod = ESDocumentConstants.PAYMENT_METHOD_PROPRIETARY;
            orderPayment.paymentProprietaryCode = "PAYPAL";
			orderPayment.paymentReceipt = "2323432341231";
			orderPayment.keyPaymentTypeID = "PP";
            orderPayment.paymentAmount = 30.00;
            orderPayments.add(orderPayment);
			
			//add payments to the order and set overall payment details
            salesOrderRecord.payments = orderPayments;
			salesOrderRecord.paymentAmount = 41.00;
			salesOrderRecord.paymentStatus = ESDocumentConstants.PAYMENT_STATUS_PAID;
			
			//set order totals, pricing data only needs to be set if the order isn't being repriced
			salesOrderRecord.totalPriceIncTax = 41.80;
			salesOrderRecord.totalPriceExTax = 38.00;
			salesOrderRecord.totalTax = 3.80;
			salesOrderRecord.totalSurchargeExTax = 8.00;
			salesOrderRecord.totalSurchargeIncTax = 8.80;
			salesOrderRecord.totalSurchargeTax = 8.00;
		
			//create sales order records list and add purchase order to it
            ArrayList<ESDRecordOrderSale> salesOrderRecords = new ArrayList<>();
			salesOrderRecords.add(salesOrderRecord);
		
            //specify if the order's lines should be repriced or not, in this example either Y-Yes or N-No value comes from command line arguments
            String repriceOrder = args[3];
			
			//after 60 seconds give up on waiting for a response from the API when creating the notification
			int timeoutMilliseconds = 60000;
			
			//create sales order Ecommerce Standards document and add sales order records to the document
			ESDocumentOrderSale orderSaleESD = new ESDocumentOrderSale(ESDocumentConstants.RESULT_SUCCESS, "successfully obtained data", salesOrderRecords.toArray(new ESDRecordOrderSale[0]), new HashMap<String, String>());

			//send sales order document to the API to import against the logged in orgqanisation
			APIv1EndpointResponseESD endpointResponseESD = APIv1EndpointOrgImportSalesOrder.call(apiOrgSession, timeoutMilliseconds, orderSaleESD, repriceOrder.equalsIgnoreCase(ESDocumentConstants.ESD_VALUE_YES));
			ESDocumentOrderSale esDocumentOrderSaleReturned = (ESDocumentOrderSale)endpointResponseESD.esDocument;
            
            //check the result of importing the sales orders
            if(endpointResponseESD.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS)){
                System.out.println("SUCCESS - organisation sales orders have successfully been imported.");
                
                //iterate through each of the returned sales orders and output the details of the sales orders
                if(esDocumentOrderSaleReturned.dataRecords != null){
                    for (ESDRecordOrderSale salesOrderRecordFinal : esDocumentOrderSaleReturned.dataRecords){
                        System.out.println("\nSales Order Returned, Order Details: ");
                        System.out.println("Sales Order Code: " + salesOrderRecordFinal.salesOrderCode);
                        System.out.println("Sales Order Total Cost: " + salesOrderRecordFinal.totalPriceIncTax + " (" + salesOrderRecordFinal.currencyISOCode + ")");
                        System.out.println("Sales Order Total Taxes: " + salesOrderRecordFinal.totalTax + " (" + salesOrderRecordFinal.currencyISOCode + ")");
                        System.out.println("Sales Order Customer Account: " + salesOrderRecordFinal.customerAccountCode);
                        System.out.println("Sales Order Total Lines: " + salesOrderRecordFinal.totalLines);
                    }
                }
            }else{
                System.out.println("FAIL - organisation sales order(s) failed to be processed. Reason: " + endpointResponseESD.result_message  + " Error Code: " + endpointResponseESD.result_code);
                
				//check if the server response returned back a Ecommerce Standards Document
				if(esDocumentOrderSaleReturned != null)
				{
					switch(endpointResponseESD.result_code)
					{
						//if one or more products in the sales order could not match the organisation's products then find out the order lines caused the problem
						case APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_ORDER_PRODUCT_NOT_MATCHED:
							//get a list of order lines that could not be mapped
							ArrayList<Pair<Integer, Integer>> unmatchedLines = APIv1EndpointOrgImportSalesOrder.getUnmatchedOrderLines(esDocumentOrderSaleReturned);

							//iterate through each unmatched order line
							for(Pair<Integer, Integer> unmatchedLine : unmatchedLines){
								//get the index of the sales order and line that contained the unmatched product
								int orderIndex = unmatchedLine.getKey();
								int lineIndex = unmatchedLine.getValue();

								//check that the order can be found that contains the problematic line
								if(orderIndex < orderSaleESD.dataRecords.length && lineIndex < orderSaleESD.dataRecords[orderIndex].lines.size()){
									System.out.println("For sales order: "+ orderSaleESD.dataRecords[orderIndex].salesOrderCode + " a matching product for line number: " + (lineIndex+1) + " could not be found.");
								}
							}

							break;

						//if one or more products in the sales order could not be priced then find the order line that caused the problem
						case APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_ORDER_LINE_PRICING_MISSING:
							//get a list of order lines that could not be priced
							ArrayList<Pair<Integer, Integer>> unpricedLines = APIv1EndpointOrgImportSalesOrder.getUnpricedOrderLines(esDocumentOrderSaleReturned);

							//iterate through each unpriced order line
							for(Pair<Integer, Integer> unmappedLine : unpricedLines){
								//get the index of the sales order and line that contained the unpriced product
								int orderIndex = unmappedLine.getKey();
								int lineIndex = unmappedLine.getValue();

								//check that the order can be found that contains the problematic line
								if(orderIndex < orderSaleESD.dataRecords.length && lineIndex < orderSaleESD.dataRecords[orderIndex].lines.size()){
									System.out.println("For sales order: "+ orderSaleESD.dataRecords[orderIndex].salesOrderCode + " has not set pricing for line number: " + (lineIndex+1));
								}
							}

							break;

						case APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_ORDER_SURCHARGE_NOT_FOUND:
							//get a list of order surcharges that could not be matched
							ArrayList<Pair<Integer, Integer>> unmappedSurcharges = APIv1EndpointOrgImportSalesOrder.getUnmatchedOrderSurcharges(esDocumentOrderSaleReturned);

							//iterate through each unmatched order surcharge
							for(Pair<Integer, Integer> unmappedSurcharge : unmappedSurcharges){
								//get the index of the sales order and surcharge that contained the unmapped surcharge
								int orderIndex = unmappedSurcharge.getKey();
								int surchargeIndex = unmappedSurcharge.getValue();

								//check that the order can be found that contains the problematic surcharge
								if(orderIndex < orderSaleESD.dataRecords.length && surchargeIndex < orderSaleESD.dataRecords[orderIndex].surcharges.size()){
									System.out.println("For sales order: "+ orderSaleESD.dataRecords[orderIndex].salesOrderCode + " a matching surcharge for surcharge number: " + (surchargeIndex+1) + " could not be found.");
								}
							}

							break;

						//if one or more surcharges in the sales order could not be priced then find the order surcharge that caused the problem
						case APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_ORDER_SURCHARGE_PRICING_MISSING:

							//get a list of order surcharges that could not be priced
							ArrayList<Pair<Integer, Integer>> unpricedSurcharges = APIv1EndpointOrgImportSalesOrder.getUnpricedOrderSurcharges(esDocumentOrderSaleReturned);

							//iterate through each unpriced order surcharge
							for(Pair<Integer, Integer> unmappedSurcharge : unpricedSurcharges){
								//get the index of the purchase order and surcharge that contained the unpriced surcharge
								int orderIndex = unmappedSurcharge.getKey();
								int surchargeIndex = unmappedSurcharge.getValue();

								//check that the order can be found that contains the problematic surcharge
								if(orderIndex < orderSaleESD.dataRecords.length && surchargeIndex < orderSaleESD.dataRecords[orderIndex].surcharges.size()){
									System.out.println("For sales order: "+ orderSaleESD.dataRecords[orderIndex].salesOrderCode + " has not set pricing for surcharge number: " + (surchargeIndex+1));
								}
							}

							break;

						case APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_ORDER_PAYMENT_NOT_MATCHED:
							//get a list of order payments that could not be mapped
							ArrayList<Pair<Integer, Integer>> unmappedPayments = APIv1EndpointOrgImportSalesOrder.getUnmatchedOrderPayments(esDocumentOrderSaleReturned);

							//iterate through each unmapped order payment
							for(Pair<Integer, Integer> unmappedPayment : unmappedPayments){
								//get the index of the purchase order and payment that contained the unmapped payment
								int orderIndex = unmappedPayment.getKey();
								int paymentIndex = unmappedPayment.getValue();

								//check that the order can be found that contains the problematic payment
								if(orderIndex < orderSaleESD.dataRecords.length && paymentIndex < orderSaleESD.dataRecords[orderIndex].payments.size()){
									System.out.println("For sales order: "+ orderSaleESD.dataRecords[orderIndex].salesOrderCode + " a matching payment for payment number: " + (paymentIndex+1) + " could not be found.");
								}
							}

							break;
					}
				}
            }
		}
		
		//next steps
		//call other API endpoints...
		//destroy API session when done...
    }
}