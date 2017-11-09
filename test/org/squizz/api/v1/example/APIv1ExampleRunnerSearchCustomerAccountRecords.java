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
 * Shows an example of creating a organisation session with the SQUIZZ.com platform's API, then search for records from a conencted organisation's customer account in the platform
 */
public class APIv1ExampleRunnerSearchCustomerAccountRecords 
{
    public static void main(String[] args)
    {
        //check that the required arguments have been given
        if(args.length < 4){
            System.out.println("Set the following arguments: [orgID] [orgAPIKey] [orgAPIPass] [supplierOrgID] [customerAccountCode] [recordType]");
            return;
        }
        
		//obtain or load in an organisation's API credentials, in this example from command line arguments
		String orgID = args[0];
		String orgAPIKey = args[1];
		String orgAPIPass = args[2];
        
        //specify the supplier organisation to get data from based on its ID within the platform, in this example the ID comes from command line arguments
        String supplierOrgID = args[3];
            
        //optionally get the customer account code if required based on the type of data being retrived, in this example the account code comes from command line arguments
        String customerAccountCode = args[4];
        String recordType = args[5].toUpperCase();
        
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
			
			//call the platform's API to search for the customer account account records
			APIv1EndpointResponseESD endpointResponseESD = 
                APIv1EndpointOrgSearchCustomerAccountRecords.call(
                    apiOrgSession, 
                    timeoutMilliseconds, 
                    recordType,
                    supplierOrgID, 
                    customerAccountCode,
                    beginDateTime,
                    endDateTime,
                    pageNumber,
                    recordsMaxAmount,
                    outstandingRecordsOnly,
                    searchString,
                    keyRecordIDs,
                    searchType
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

                        //check that invoice records have been placed into the standards document
                        if(esDocumentCustomerAccountEnquiry.invoiceRecords != null){
                            System.out.println("Invoice Records:");

                            //iterate through each invoice record stored within the standards document
                            int i=0;
                            for(ESDRecordCustomerAccountEnquiryInvoice invoiceRecord: esDocumentCustomerAccountEnquiry.invoiceRecords)
                            {    
                                //convert invoice date time milliseconds into calendar representation
                                calendar.setTimeInMillis(invoiceRecord.invoiceDate);

                                //output details of the invoice record
                                System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
                                System.out.println("     Invoice Record #: " + i);
                                System.out.println("       Key Invoice ID: " + invoiceRecord.keyInvoiceID);
                                System.out.println("           Invoice ID: " + invoiceRecord.invoiceID);
                                System.out.println("       Invoice Number: " + invoiceRecord.invoiceNumber);
                                System.out.println("         Invoice Date: " + new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));
                                System.out.println("Total Price (Inc Tax): " + invoiceRecord.totalIncTax + " " + invoiceRecord.currencyCode);
                                System.out.println("           Total Paid: " + invoiceRecord.totalPaid + " " + invoiceRecord.currencyCode);
                                System.out.println("           Total Owed: " + invoiceRecord.balance + " " + invoiceRecord.currencyCode);
                                System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
                                i++;
                            }
                        }
                        break;
                    case ESDocumentConstants.RECORD_TYPE_ORDER_SALE:
                        System.out.println("SUCCESS - account sales order record data successfully obtained from the platform");
                        System.out.println("Sales Order Records Returned: " + esDocumentCustomerAccountEnquiry.totalDataRecords);

                        //check that sales order records have been placed into the standards document
                        if(esDocumentCustomerAccountEnquiry.orderSaleRecords != null){
                            System.out.println("Sales Order Records:");

                            //iterate through each sales order record stored within the standards document
                            int i=0;
                            for(ESDRecordCustomerAccountEnquiryOrderSale orderSaleRecord: esDocumentCustomerAccountEnquiry.orderSaleRecords)
                            {    
                                //convert sales order date time milliseconds into calendar representation
                                calendar.setTimeInMillis(orderSaleRecord.orderDate);

                                //output details of the sales order record
                                System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
                                System.out.println(" Sales Order Record #: " + i);
                                System.out.println("    Key Order Sale ID: " + orderSaleRecord.keyOrderSaleID);
                                System.out.println("             Order ID: " + orderSaleRecord.orderID);
                                System.out.println("         Order Number: " + orderSaleRecord.orderNumber);
                                System.out.println("           Order Date: " + new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));
                                System.out.println("Total Price (Inc Tax): " + orderSaleRecord.totalIncTax + " " + orderSaleRecord.currencyCode);
                                System.out.println("           Total Paid: " + orderSaleRecord.totalPaid + " " + orderSaleRecord.currencyCode);
                                System.out.println("           Total Owed: " + orderSaleRecord.balance + " " + orderSaleRecord.currencyCode);
                                System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
                                i++;
                            }
                        }
                        break;
                    case ESDocumentConstants.RECORD_TYPE_BACKORDER:
                        System.out.println("SUCCESS - account back order record data successfully obtained from the platform");
                        System.out.println("Back Order Records Returned: " + esDocumentCustomerAccountEnquiry.totalDataRecords);

                        //check that back order records have been placed into the standards document
                        if(esDocumentCustomerAccountEnquiry.backOrderRecords != null){
                            System.out.println("Back Order Records:");

                            //iterate through each back order record stored within the standards document
                            int i=0;
                            for(ESDRecordCustomerAccountEnquiryBackOrder backOrderRecord: esDocumentCustomerAccountEnquiry.backOrderRecords)
                            {    
                                //convert back order date time milliseconds into calendar representation
                                calendar.setTimeInMillis(backOrderRecord.backOrderDate);

                                //output details of the back order record
                                System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
                                System.out.println("  Back Order Record #: " + i);
                                System.out.println("    Key Back Order ID: " + backOrderRecord.keyBackOrderID);
                                System.out.println("             Order ID: " + backOrderRecord.backOrderID);
                                System.out.println("    Back Order Number: " + backOrderRecord.backOrderNumber);
                                System.out.println("           Order Date: " + new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));
                                System.out.println("Total Price (Inc Tax): " + backOrderRecord.totalIncTax + " " + backOrderRecord.currencyCode);
                                System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
                                i++;
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
                                i++;
                            }
                        }
                        break;
                    case ESDocumentConstants.RECORD_TYPE_CREDIT:
                        System.out.println("SUCCESS - account credit record data successfully obtained from the platform");
                        System.out.println("Credit Records Returned: " + esDocumentCustomerAccountEnquiry.totalDataRecords);

                        //check that credit records have been placed into the standards document
                        if(esDocumentCustomerAccountEnquiry.creditRecords != null){
                            System.out.println("Credit Records:");

                            //iterate through each credit record stored within the standards document
                            int i=0;
                            for(ESDRecordCustomerAccountEnquiryCredit creditRecord: esDocumentCustomerAccountEnquiry.creditRecords)
                            {    
                                //convert credit date time milliseconds into calendar representation
                                calendar.setTimeInMillis(creditRecord.creditDate);

                                //output details of the credit record
                                System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
                                System.out.println("      Credit Record #: " + i);
                                System.out.println("        Key Credit ID: " + creditRecord.keyCreditID);
                                System.out.println("            Credit ID: " + creditRecord.creditID);
                                System.out.println("        Credit Number: " + creditRecord.creditNumber);
                                System.out.println("          Credit Date: " + new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));
                                System.out.println("      Amount Credited: " + creditRecord.appliedAmount + " " + creditRecord.currencyCode);
                                System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
                                i++;
                            }
                        }
                        break;
                    case ESDocumentConstants.RECORD_TYPE_PAYMENT:
                        System.out.println("SUCCESS - account payment record data successfully obtained from the platform");
                        System.out.println("Payment Records Returned: " + esDocumentCustomerAccountEnquiry.totalDataRecords);

                        //check that payment records have been placed into the standards document
                        if(esDocumentCustomerAccountEnquiry.paymentRecords != null){
                            System.out.println("Payment Records:");

                            //iterate through each payment record stored within the standards document
                            int i=0;
                            for(ESDRecordCustomerAccountEnquiryPayment paymentRecord: esDocumentCustomerAccountEnquiry.paymentRecords)
                            {    
                                //convert payment date time milliseconds into calendar representation
                                calendar.setTimeInMillis(paymentRecord.paymentDate);

                                //output details of the payment record
                                System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
                                System.out.println("     Payment Record #: " + i);
                                System.out.println("       Key Payment ID: " + paymentRecord.keyPaymentID);
                                System.out.println("           Payment ID: " + paymentRecord.paymentID);
                                System.out.println("       Payment Number: " + paymentRecord.paymentNumber);
                                System.out.println("         Payment Date: " + new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));
                                System.out.println("    Total Amount Paid: " + paymentRecord.totalAmount + " " + paymentRecord.currencyCode);
                                System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
                                i++;
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