/**
* Copyright (C) 2017 Squizz PTY LTD
* This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
*/
package org.squizz.api.v1.example;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.squizz.api.v1.endpoint.APIv1EndpointOrgSearchCustomerAccountRecords;
import org.squizz.api.v1.*;
import org.squizz.api.v1.endpoint.*;
import org.esd.EcommerceStandardsDocuments.*;

/**
 * Shows an example of creating a organisation session with the SQUIZZ.com platform's API, then retrieve the details of a record from a conencted organisation's customer account in the platform
 */
public class APIv1ExampleRetrieveSearchCustomerAccountRecord 
{
    public static void main(String[] args)
    {
        //check that the required arguments have been given
        if(args.length < 4){
            System.out.println("Set the following arguments: [orgID] [orgAPIKey] [orgAPIPass] [supplierOrgID] [customerAccountCode] [recordType] [keyRecordID]");
            return;
        }
        
		//obtain or load in an organisation's API credentials, in this example from command line arguments
		String orgID = args[0];
		String orgAPIKey = args[1];
		String orgAPIPass = args[2];
        
        //specify the supplier organisation to get data from based on its ID within the platform, in this example the ID comes from command line arguments
        String supplierOrgID = args[3];
            
        //get the customer account code if required based on the type of data being retrived, in this example the account code comes from command line arguments
        String customerAccountCode = args[4];
        String recordType = args[5].toUpperCase();
		String keyRecordID = args[6].toUpperCase();
        
        long beginDateTime = 0;
        long endDateTime = System.currentTimeMillis();
        int pageNumber = 1;
        int recordsMaxAmount = 100;
        boolean outstandingRecordsOnly = false;
        String searchString="";
        String keyRecordIDs="";
        String searchType="";
        
        //set date time objects that can be used to convert long date times to calendar dates
        int sessionTimeoutMilliseconds = 20000;
        Calendar calendar = Calendar.getInstance();
		
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
		
		//import organisation data if the API was successfully created
		if(apiOrgSession.sessionExists())
		{   
			//after 60 seconds give up on waiting for a response from the API when creating the notification
			int timeoutMilliseconds = 60000;
			
			//call the platform's API to retrieve the details of the customer account record
			APIv1EndpointResponseESD endpointResponseESD = 
                APIv1EndpointOrgRetrieveCustomerAccountRecord.call(
                    apiOrgSession, 
                    timeoutMilliseconds, 
                    recordType,
                    supplierOrgID, 
                    customerAccountCode,
                    keyRecordID
                );
            ESDocumentCustomerAccountEnquiry esDocumentCustomerAccountEnquiry = (ESDocumentCustomerAccountEnquiry)endpointResponseESD.esDocument;
			
			//check that the data successfully imported
			if(endpointResponseESD.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS)){
                //output records based on the record type
                switch(recordType)
                {
                    case ESDocumentConstants.RECORD_TYPE_INVOICE:
                        System.out.println("SUCCESS - account invoice record data successfully obtained from the platform");
                        System.out.println("\nInvoice Records Returned: " + esDocumentCustomerAccountEnquiry.totalDataRecords);

                        //check that invoice record has been placed into the standards document
                        if(esDocumentCustomerAccountEnquiry.invoiceRecords != null){
                            System.out.println("Invoice Records:");

                            //display the details of the record stored within the standards document
                            for(ESDRecordCustomerAccountEnquiryInvoice invoiceRecord: esDocumentCustomerAccountEnquiry.invoiceRecords)
                            {
                                //convert invoice date time milliseconds into calendar representation
                                calendar.setTimeInMillis(invoiceRecord.invoiceDate);

                                //output details of the invoice record
                                System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
                                System.out.println("       Key Invoice ID: " + invoiceRecord.keyInvoiceID);
                                System.out.println("           Invoice ID: " + invoiceRecord.invoiceID);
                                System.out.println("       Invoice Number: " + invoiceRecord.invoiceNumber);
                                System.out.println("         Invoice Date: " + new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));
                                System.out.println("Total Price (Inc Tax): " + invoiceRecord.totalIncTax + " " + invoiceRecord.currencyCode);
                                System.out.println("           Total Paid: " + invoiceRecord.totalPaid + " " + invoiceRecord.currencyCode);
                                System.out.println("           Total Owed: " + invoiceRecord.balance + " " + invoiceRecord.currencyCode);
								System.out.println("          Description: " + invoiceRecord.description);
								System.out.println("              Comment: " + invoiceRecord.comment);
								System.out.println("     Reference Number: " + invoiceRecord.referenceNumber);
								System.out.println("       Reference Type: " + invoiceRecord.referenceType);
								System.out.println("");
								System.out.println("     Delivery Address: ");
								System.out.println("    Organisation Name: " + invoiceRecord.deliveryOrgName);
								System.out.println("              Contact: " + invoiceRecord.deliveryContact);
								System.out.println("            Address 1: " + invoiceRecord.deliveryAddress1);
								System.out.println("            Address 2: " + invoiceRecord.deliveryAddress2);
								System.out.println("            Address 3: " + invoiceRecord.deliveryAddress3);
								System.out.println("State/Province/Region: " + invoiceRecord.deliveryStateProvince);
								System.out.println("              Country: " + invoiceRecord.deliveryCountry);
								System.out.println("     Postcode/Zipcode: " + invoiceRecord.deliveryPostcode);
								System.out.println("");
								System.out.println("      Billing Address: ");
								System.out.println("    Organisation Name: " + invoiceRecord.billingOrgName);
								System.out.println("              Contact: " + invoiceRecord.billingContact);
								System.out.println("            Address 1: " + invoiceRecord.billingAddress1);
								System.out.println("            Address 2: " + invoiceRecord.billingAddress2);
								System.out.println("            Address 3: " + invoiceRecord.billingAddress3);
								System.out.println("State/Province/Region: " + invoiceRecord.billingStateProvince);
								System.out.println("              Country: " + invoiceRecord.billingCountry);
								System.out.println("     Postcode/Zipcode: " + invoiceRecord.billingPostcode);
								System.out.println("");
								System.out.println("      Freight Details: ");
								System.out.println("     Consignment Code: " + invoiceRecord.freightCarrierConsignCode);
								System.out.println("        Tracking Code: " + invoiceRecord.freightCarrierTrackingCode);
								System.out.println("         Carrier Name: " + invoiceRecord.freightCarrierName);
                                System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
								
								//output the details of each line
								if(invoiceRecord.lines != null)
								{
									System.out.println("                Lines: ");
									int i=0;
									for(ESDRecordCustomerAccountEnquiryInvoiceLine invoiceLineRecord: invoiceRecord.lines)
									{
										i++;
										System.out.println("          Line Number: " + i);
										
										if(invoiceLineRecord.lineType.equalsIgnoreCase(ESDocumentConstants.RECORD_LINE_TYPE_ITEM)){
											System.out.println("             Line Type: ITEM");
											System.out.println("          Line Item ID: " + invoiceLineRecord.lineItemID);
											System.out.println("        Line Item Code: " + invoiceLineRecord.lineItemCode);
											System.out.println("           Description: " + invoiceLineRecord.description);
											System.out.println("      Quantity Ordered: " + invoiceLineRecord.quantityOrdered + " " + invoiceLineRecord.unit);
											System.out.println("    Quantity Delivered: " + invoiceLineRecord.quantityDelivered + " " + invoiceLineRecord.unit);
											System.out.println(" Quantity Back Ordered: " + invoiceLineRecord.quantityBackordered + " " + invoiceLineRecord.unit);
											System.out.println("   Unit Price (Ex Tax): " + invoiceLineRecord.priceExTax + " " + invoiceLineRecord.currencyCode);
											System.out.println("  Total Price (Ex Tax): " + invoiceLineRecord.totalPriceExTax + " " + invoiceLineRecord.currencyCode);
											System.out.println("             Total Tax: " + invoiceLineRecord.totalPriceTax + " Inclusive of " + invoiceLineRecord.taxCode + " " + invoiceLineRecord.taxCodeRatePercent+"%");
											System.out.println(" Total Price (Inc Tax): " + invoiceLineRecord.totalPriceIncTax + " " + invoiceLineRecord.currencyCode);
										}else if(invoiceLineRecord.lineType.equalsIgnoreCase(ESDocumentConstants.RECORD_LINE_TYPE_TEXT))
										{
											System.out.println("            Line Type: TEXT");
											System.out.println("          Description: " + invoiceLineRecord.description);
										}
										
										System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
									}
								}
								
								break;
                            }
                        }
                        break;
                    case ESDocumentConstants.RECORD_TYPE_ORDER_SALE:
                        System.out.println("SUCCESS - account sales order record data successfully obtained from the platform");
                        System.out.println("Sales Order Records Returned: " + esDocumentCustomerAccountEnquiry.totalDataRecords);

                        //check that sales order records have been placed into the standards document
                        if(esDocumentCustomerAccountEnquiry.orderSaleRecords != null){
                            System.out.println("Sales Order Records:");

                            //display the details of the record stored within the standards document
                            for(ESDRecordCustomerAccountEnquiryOrderSale salesOrderRecord: esDocumentCustomerAccountEnquiry.orderSaleRecords)
                            {
                                //convert order date time milliseconds into calendar representation
                                calendar.setTimeInMillis(salesOrderRecord.orderDate);

                                //output details of the sales order record
                                System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
                                System.out.println("    Key Order Sale ID: " + salesOrderRecord.keyOrderSaleID);
                                System.out.println("             Order ID: " + salesOrderRecord.orderID);
                                System.out.println("         Order Number: " + salesOrderRecord.orderNumber);
                                System.out.println("           Order Date: " + new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));
                                System.out.println("Total Price (Inc Tax): " + salesOrderRecord.totalIncTax + " " + salesOrderRecord.currencyCode);
                                System.out.println("           Total Paid: " + salesOrderRecord.totalPaid + " " + salesOrderRecord.currencyCode);
                                System.out.println("           Total Owed: " + salesOrderRecord.balance + " " + salesOrderRecord.currencyCode);
								System.out.println("          Description: " + salesOrderRecord.description);
								System.out.println("              Comment: " + salesOrderRecord.comment);
								System.out.println("     Reference Number: " + salesOrderRecord.referenceNumber);
								System.out.println("       Reference Type: " + salesOrderRecord.referenceType);
								System.out.println("");
								System.out.println("     Delivery Address: ");
								System.out.println("    Organisation Name: " + salesOrderRecord.deliveryOrgName);
								System.out.println("              Contact: " + salesOrderRecord.deliveryContact);
								System.out.println("            Address 1: " + salesOrderRecord.deliveryAddress1);
								System.out.println("            Address 2: " + salesOrderRecord.deliveryAddress2);
								System.out.println("            Address 3: " + salesOrderRecord.deliveryAddress3);
								System.out.println("State/Province/Region: " + salesOrderRecord.deliveryStateProvince);
								System.out.println("              Country: " + salesOrderRecord.deliveryCountry);
								System.out.println("     Postcode/Zipcode: " + salesOrderRecord.deliveryPostcode);
								System.out.println("");
								System.out.println("      Billing Address: ");
								System.out.println("    Organisation Name: " + salesOrderRecord.billingOrgName);
								System.out.println("              Contact: " + salesOrderRecord.billingContact);
								System.out.println("            Address 1: " + salesOrderRecord.billingAddress1);
								System.out.println("            Address 2: " + salesOrderRecord.billingAddress2);
								System.out.println("            Address 3: " + salesOrderRecord.billingAddress3);
								System.out.println("State/Province/Region: " + salesOrderRecord.billingStateProvince);
								System.out.println("              Country: " + salesOrderRecord.billingCountry);
								System.out.println("     Postcode/Zipcode: " + salesOrderRecord.billingPostcode);
								System.out.println("");
								System.out.println("      Freight Details: ");
								System.out.println("     Consignment Code: " + salesOrderRecord.freightCarrierConsignCode);
								System.out.println("        Tracking Code: " + salesOrderRecord.freightCarrierTrackingCode);
								System.out.println("         Carrier Name: " + salesOrderRecord.freightCarrierName);
                                System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
								
								//output the details of each line
								if(salesOrderRecord.lines != null)
								{
									System.out.println("                Lines: ");
									int i=0;
									for(ESDRecordCustomerAccountEnquiryOrderSaleLine orderLineRecord: salesOrderRecord.lines)
									{
										i++;
										System.out.println("          Line Number: " + i);
										
										if(orderLineRecord.lineType.equalsIgnoreCase(ESDocumentConstants.RECORD_LINE_TYPE_ITEM)){
											System.out.println("             Line Type: ITEM");
											System.out.println("          Line Item ID: " + orderLineRecord.lineItemID);
											System.out.println("        Line Item Code: " + orderLineRecord.lineItemCode);
											System.out.println("           Description: " + orderLineRecord.description);
											System.out.println("      Quantity Ordered: " + orderLineRecord.quantityOrdered + " " + orderLineRecord.unit);
											System.out.println("    Quantity Delivered: " + orderLineRecord.quantityDelivered + " " + orderLineRecord.unit);
											System.out.println(" Quantity Back Ordered: " + orderLineRecord.quantityBackordered + " " + orderLineRecord.unit);
											System.out.println("   Unit Price (Ex Tax): " + orderLineRecord.priceExTax + " " + orderLineRecord.currencyCode);
											System.out.println("  Total Price (Ex Tax): " + orderLineRecord.totalPriceExTax + " " + orderLineRecord.currencyCode);
											System.out.println("             Total Tax: " + orderLineRecord.totalPriceTax + " Inclusive of " + orderLineRecord.taxCode + " " + orderLineRecord.taxCodeRatePercent+"%");
											System.out.println(" Total Price (Inc Tax): " + orderLineRecord.totalPriceIncTax + " " + orderLineRecord.currencyCode);
										}else if(orderLineRecord.lineType.equalsIgnoreCase(ESDocumentConstants.RECORD_LINE_TYPE_TEXT))
										{
											System.out.println("            Line Type: TEXT");
											System.out.println("          Description: " + orderLineRecord.description);
										}
										
										System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
									}
								}
								
								break;
                            }
                        }
                        break;
                    case ESDocumentConstants.RECORD_TYPE_BACKORDER:
                        System.out.println("SUCCESS - account back order record data successfully obtained from the platform");
                        System.out.println("Back Order Records Returned: " + esDocumentCustomerAccountEnquiry.totalDataRecords);

                        //check that back order records have been placed into the standards document
                        if(esDocumentCustomerAccountEnquiry.backOrderRecords != null){
                            System.out.println("Back Order Records:");

                            //display the details of the record stored within the standards document
                            for(ESDRecordCustomerAccountEnquiryBackOrder backOrderRecord: esDocumentCustomerAccountEnquiry.backOrderRecords)
                            {
                                //convert order date time milliseconds into calendar representation
                                calendar.setTimeInMillis(backOrderRecord.backOrderDate);

                                //output details of the back order record
                                System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
                                System.out.println("    Key Back Order ID: " + backOrderRecord.keyBackOrderID);
                                System.out.println("        Back Order ID: " + backOrderRecord.backOrderID);
                                System.out.println("    Back Order Number: " + backOrderRecord.backOrderNumber);
                                System.out.println("           Order Date: " + new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));
                                System.out.println("Total Price (Inc Tax): " + backOrderRecord.totalIncTax + " " + backOrderRecord.currencyCode);
                                System.out.println("           Total Paid: " + backOrderRecord.totalPaid + " " + backOrderRecord.currencyCode);
                                System.out.println("           Total Owed: " + backOrderRecord.balance + " " + backOrderRecord.currencyCode);
								System.out.println("          Description: " + backOrderRecord.description);
								System.out.println("              Comment: " + backOrderRecord.comment);
								System.out.println("     Reference Number: " + backOrderRecord.referenceNumber);
								System.out.println("       Reference Type: " + backOrderRecord.referenceType);
								System.out.println("");
								System.out.println("     Delivery Address: ");
								System.out.println("    Organisation Name: " + backOrderRecord.deliveryOrgName);
								System.out.println("              Contact: " + backOrderRecord.deliveryContact);
								System.out.println("            Address 1: " + backOrderRecord.deliveryAddress1);
								System.out.println("            Address 2: " + backOrderRecord.deliveryAddress2);
								System.out.println("            Address 3: " + backOrderRecord.deliveryAddress3);
								System.out.println("State/Province/Region: " + backOrderRecord.deliveryStateProvince);
								System.out.println("              Country: " + backOrderRecord.deliveryCountry);
								System.out.println("     Postcode/Zipcode: " + backOrderRecord.deliveryPostcode);
								System.out.println("");
								System.out.println("      Billing Address: ");
								System.out.println("    Organisation Name: " + backOrderRecord.billingOrgName);
								System.out.println("              Contact: " + backOrderRecord.billingContact);
								System.out.println("            Address 1: " + backOrderRecord.billingAddress1);
								System.out.println("            Address 2: " + backOrderRecord.billingAddress2);
								System.out.println("            Address 3: " + backOrderRecord.billingAddress3);
								System.out.println("State/Province/Region: " + backOrderRecord.billingStateProvince);
								System.out.println("              Country: " + backOrderRecord.billingCountry);
								System.out.println("     Postcode/Zipcode: " + backOrderRecord.billingPostcode);
                                System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
								
								//output the details of each line
								if(backOrderRecord.lines != null)
								{
									System.out.println("                Lines: ");
									int i=0;
									for(ESDRecordCustomerAccountEnquiryBackOrderLine orderLineRecord: backOrderRecord.lines)
									{
										i++;
										System.out.println("          Line Number: " + i);
										
										if(orderLineRecord.lineType.equalsIgnoreCase(ESDocumentConstants.RECORD_LINE_TYPE_ITEM)){
											System.out.println("             Line Type: ITEM");
											System.out.println("          Line Item ID: " + orderLineRecord.lineItemID);
											System.out.println("        Line Item Code: " + orderLineRecord.lineItemCode);
											System.out.println("           Description: " + orderLineRecord.description);
											System.out.println("      Quantity Ordered: " + orderLineRecord.quantityOrdered + " " + orderLineRecord.unit);
											System.out.println("    Quantity Delivered: " + orderLineRecord.quantityDelivered + " " + orderLineRecord.unit);
											System.out.println(" Quantity Back Ordered: " + orderLineRecord.quantityBackordered + " " + orderLineRecord.unit);
											System.out.println("   Unit Price (Ex Tax): " + orderLineRecord.priceExTax + " " + orderLineRecord.currencyCode);
											System.out.println("  Total Price (Ex Tax): " + orderLineRecord.totalPriceExTax + " " + orderLineRecord.currencyCode);
											System.out.println("             Total Tax: " + orderLineRecord.totalPriceTax + " Inclusive of " + orderLineRecord.taxCode + " " + orderLineRecord.taxCodeRatePercent+"%");
											System.out.println(" Total Price (Inc Tax): " + orderLineRecord.totalPriceIncTax + " " + orderLineRecord.currencyCode);
										}else if(orderLineRecord.lineType.equalsIgnoreCase(ESDocumentConstants.RECORD_LINE_TYPE_TEXT))
										{
											System.out.println("            Line Type: TEXT");
											System.out.println("          Description: " + orderLineRecord.description);
										}
										
										System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
									}
								}
								
								break;
                            }
                        }
                        break;
                    case ESDocumentConstants.RECORD_TYPE_TRANSACTION:
                        System.out.println("SUCCESS - account transaction record data successfully obtained from the platform");
                        System.out.println("Transaction Records Returned: " + esDocumentCustomerAccountEnquiry.totalDataRecords);

                        //check that transaction records have been placed into the standards document
                        if(esDocumentCustomerAccountEnquiry.transactionRecords != null){
                            System.out.println("Transaction Records:");

                            //iterate through each transaction record stored within the standards document
                            int i=0;
                            for(ESDRecordCustomerAccountEnquiryTransaction transactionRecord: esDocumentCustomerAccountEnquiry.transactionRecords)
                            {    
                                //convert transaction date time milliseconds into calendar representation
                                calendar.setTimeInMillis(transactionRecord.transactionDate);

                                //output details of the transaction record
                                System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
                                System.out.println(" Transaction Record #: " + i);
                                System.out.println("   Key Transaction ID: " + transactionRecord.keyTransactionID);
                                System.out.println("       Transaction ID: " + transactionRecord.transactionID);
                                System.out.println("   Transaction Number: " + transactionRecord.transactionNumber);
                                System.out.println("     Transaction Date: " + new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));
                                if(transactionRecord.debitAmount > 0){
                                    System.out.println("       Amount Debited: " + transactionRecord.debitAmount + " " + transactionRecord.currencyCode);
                                }else if(transactionRecord.creditAmount > 0){
                                    System.out.println("      Amount Credited: " + transactionRecord.creditAmount + " " + transactionRecord.currencyCode);
                                }
                                System.out.println("              Balance: " + transactionRecord.balance + " " + transactionRecord.currencyCode);
                                System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
                                break;
                            }
                        }
                        break;
                    case ESDocumentConstants.RECORD_TYPE_CREDIT:
                        System.out.println("SUCCESS - account credit record data successfully obtained from the platform");
                        System.out.println("Credit Records Returned: " + esDocumentCustomerAccountEnquiry.totalDataRecords);

                        //check that credit records have been placed into the standards document
                        if(esDocumentCustomerAccountEnquiry.creditRecords != null){
                            System.out.println("Credit Records:");

                            //display the details of the record stored within the standards document
                            for(ESDRecordCustomerAccountEnquiryCredit creditRecord: esDocumentCustomerAccountEnquiry.creditRecords)
                            {
                                //convert credit date time milliseconds into calendar representation
                                calendar.setTimeInMillis(creditRecord.creditDate);

                                //output details of the credit record
                                System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
                                System.out.println("        Key Credit ID: " + creditRecord.keyCreditID);
                                System.out.println("            Credit ID: " + creditRecord.creditID);
                                System.out.println("        Credit Number: " + creditRecord.creditNumber);
                                System.out.println("          Credit Date: " + new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));
                                System.out.println("      Amount Credited: " + creditRecord.appliedAmount + " " + creditRecord.currencyCode);
								System.out.println("          Description: " + creditRecord.description);
								System.out.println("              Comment: " + creditRecord.comment);
								System.out.println("     Reference Number: " + creditRecord.referenceNumber);
								System.out.println("       Reference Type: " + creditRecord.referenceType);
								System.out.println("");
								System.out.println("     Delivery Address: ");
								System.out.println("    Organisation Name: " + creditRecord.deliveryOrgName);
								System.out.println("              Contact: " + creditRecord.deliveryContact);
								System.out.println("            Address 1: " + creditRecord.deliveryAddress1);
								System.out.println("            Address 2: " + creditRecord.deliveryAddress2);
								System.out.println("            Address 3: " + creditRecord.deliveryAddress3);
								System.out.println("State/Province/Region: " + creditRecord.deliveryStateProvince);
								System.out.println("              Country: " + creditRecord.deliveryCountry);
								System.out.println("     Postcode/Zipcode: " + creditRecord.deliveryPostcode);
								System.out.println("");
								System.out.println("      Billing Address: ");
								System.out.println("    Organisation Name: " + creditRecord.billingOrgName);
								System.out.println("              Contact: " + creditRecord.billingContact);
								System.out.println("            Address 1: " + creditRecord.billingAddress1);
								System.out.println("            Address 2: " + creditRecord.billingAddress2);
								System.out.println("            Address 3: " + creditRecord.billingAddress3);
								System.out.println("State/Province/Region: " + creditRecord.billingStateProvince);
								System.out.println("              Country: " + creditRecord.billingCountry);
								System.out.println("     Postcode/Zipcode: " + creditRecord.billingPostcode);
                                System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
								
								//output the details of each line
								if(creditRecord.lines != null)
								{
									System.out.println("                Lines: ");
									int i=0;
									for(ESDRecordCustomerAccountEnquiryCreditLine creditLineRecord: creditRecord.lines)
									{
										i++;
										System.out.println("          Line Number: " + i);
										
										if(creditLineRecord.lineType.equalsIgnoreCase(ESDocumentConstants.RECORD_LINE_TYPE_ITEM)){
											System.out.println("             Line Type: ITEM");
											System.out.println("          Line Item ID: " + creditLineRecord.lineItemID);
											System.out.println("        Line Item Code: " + creditLineRecord.lineItemCode);
											System.out.println("           Description: " + creditLineRecord.description);
											System.out.println("      Reference Number: " + creditLineRecord.referenceNumber);
											System.out.println("        Reference Type: " + creditLineRecord.referenceType);
											System.out.println("      Reference Key ID: " + creditLineRecord.referenceKeyID);
											System.out.println(" Total Price (Inc Tax): " + creditLineRecord.totalPriceIncTax + " " + creditLineRecord.currencyCode);
										}else if(creditLineRecord.lineType.equalsIgnoreCase(ESDocumentConstants.RECORD_LINE_TYPE_TEXT))
										{
											System.out.println("            Line Type: TEXT");
											System.out.println("          Description: " + creditLineRecord.description);
										}
										
										System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
									}
								}
								
								break;
                            }
                        }
                        break;
                    case ESDocumentConstants.RECORD_TYPE_PAYMENT:
                        System.out.println("SUCCESS - account payment record data successfully obtained from the platform");
                        System.out.println("Payment Records Returned: " + esDocumentCustomerAccountEnquiry.totalDataRecords);

                        //check that payment records have been placed into the standards document
                        if(esDocumentCustomerAccountEnquiry.paymentRecords != null){
                            System.out.println("Payment Records:");

                            //display the details of the record stored within the standards document
                            for(ESDRecordCustomerAccountEnquiryPayment paymentRecord: esDocumentCustomerAccountEnquiry.paymentRecords)
                            {
                                //convert payment date time milliseconds into calendar representation
                                calendar.setTimeInMillis(paymentRecord.paymentDate);

                                //output details of the payment record
                                System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
                                System.out.println("       Key Payment ID: " + paymentRecord.keyPaymentID);
                                System.out.println("           Payment ID: " + paymentRecord.paymentID);
                                System.out.println("       Payment Number: " + paymentRecord.paymentNumber);
                                System.out.println("         Payment Date: " + new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));
                                System.out.println("    Total Amount Paid: " + paymentRecord.totalAmount + " " + paymentRecord.currencyCode);
								System.out.println("          Description: " + paymentRecord.description);
								System.out.println("              Comment: " + paymentRecord.comment);
								System.out.println("     Reference Number: " + paymentRecord.referenceNumber);
								System.out.println("       Reference Type: " + paymentRecord.referenceType);
								System.out.println("     Reference Key ID: " + paymentRecord.referenceKeyID);
                                System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
								
								//output the details of each line
								if(paymentRecord.lines != null)
								{
									System.out.println("                Lines: ");
									int i=0;
									for(ESDRecordCustomerAccountEnquiryPaymentLine paymentLineRecord: paymentRecord.lines)
									{
										i++;
										System.out.println("          Line Number: " + i);
										
										if(paymentLineRecord.lineType.equalsIgnoreCase(ESDocumentConstants.RECORD_LINE_TYPE_ITEM)){
											System.out.println("             Line Type: ITEM");
											System.out.println("          Line Item ID: " + paymentLineRecord.lineItemID);
											System.out.println("        Line Item Code: " + paymentLineRecord.lineItemCode);
											System.out.println("           Description: " + paymentLineRecord.description);
											System.out.println("      Reference Number: " + paymentLineRecord.referenceNumber);
											System.out.println("        Reference Type: " + paymentLineRecord.referenceType);
											System.out.println("      Reference Key ID: " + paymentLineRecord.referenceKeyID);
											System.out.println("        Payment Amount: " + paymentLineRecord.amount + " " + paymentLineRecord.currencyCode);
										}else if(paymentLineRecord.lineType.equalsIgnoreCase(ESDocumentConstants.RECORD_LINE_TYPE_TEXT))
										{
											System.out.println("            Line Type: TEXT");
											System.out.println("          Description: " + paymentLineRecord.description);
										}
										
										System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
									}
								}
								
								break;
                            }
                        }
                        break;
                }
            }else{
                System.out.println("FAIL - account record data failed to be obtained from the platform. Reason: " + endpointResponseESD.result_message  + " Error Code: " + endpointResponseESD.result_code);
            }
            
            //next steps
            //call other API endpoints...
            //destroy API session when done...
            apiOrgSession.destroyOrgSession();
		}
    }
}