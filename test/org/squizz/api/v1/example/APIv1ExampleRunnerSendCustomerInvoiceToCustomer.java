/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.squizz.api.v1.example;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.util.Pair;
import org.esd.EcommerceStandardsDocuments.ESDRecordCustomerInvoice;
import org.esd.EcommerceStandardsDocuments.ESDRecordCustomerInvoiceLine;
import org.esd.EcommerceStandardsDocuments.ESDRecordInvoiceLineTax;
import org.esd.EcommerceStandardsDocuments.ESDRecordSupplierInvoice;
import org.esd.EcommerceStandardsDocuments.ESDocumentConstants;
import org.esd.EcommerceStandardsDocuments.ESDocumentSupplierInvoice;
import org.esd.EcommerceStandardsDocuments.ESDocumentCustomerInvoice;
import org.squizz.api.v1.APIv1Constants;
import org.squizz.api.v1.APIv1OrgSession;
import org.squizz.api.v1.endpoint.APIv1EndpointOrgSendCustomerInvoiceToCustomer;
import org.squizz.api.v1.endpoint.APIv1EndpointResponse;
import org.squizz.api.v1.endpoint.APIv1EndpointResponseESD;

/**
 * Shows an example of creating a organisation session with the SQUIZZ.com platform's API, then sends an organisation's customer invoice to a customer organisation
 */
public class APIv1ExampleRunnerSendCustomerInvoiceToCustomer 
{
	public static void main(String[] args)
    {
        //check that the required arguments have been given
        if(args.length < 4){
            System.out.println("Set the following arguments: [orgID] [orgAPIKey] [orgAPIPass] [customerOrgID] (optional)[supplierAccountCode]");
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
            //create customer invoice record to import
			ESDRecordCustomerInvoice customerInvoiceRecord = new ESDRecordCustomerInvoice();

			//set data within the customer invoice
			customerInvoiceRecord.keyCustomerInvoiceID = "111";
			customerInvoiceRecord.customerInvoiceCode = "CINV-22";
			customerInvoiceRecord.customerInvoiceNumber = "22";
			customerInvoiceRecord.salesOrderCode = "SO-332";
			customerInvoiceRecord.purchaseOrderNumber = "PO-345";
			customerInvoiceRecord.instructions = "Please pay within 30 days";
			customerInvoiceRecord.keyCustomerAccountID = "2";
			customerInvoiceRecord.customerAccountCode = "ACM-002";

			//set dates within the invoice, in unix time, milliseconds since the 01/01/1970 12AM UTC epoch
			customerInvoiceRecord.paymentDueDate = 	System.currentTimeMillis();
			customerInvoiceRecord.createdDate = 	System.currentTimeMillis();
			customerInvoiceRecord.dispatchedDate = 	System.currentTimeMillis();

			//set delivery address that invoice goods were delivered to
			customerInvoiceRecord.deliveryAddress1 = "32";
			customerInvoiceRecord.deliveryAddress2 = "Main Street";
			customerInvoiceRecord.deliveryAddress3 = "Melbourne";
			customerInvoiceRecord.deliveryRegionName = "Victoria";
			customerInvoiceRecord.deliveryCountryName = "Australia";
			customerInvoiceRecord.deliveryPostcode = "3000";
			customerInvoiceRecord.deliveryOrgName = "Acme Industries";
			customerInvoiceRecord.deliveryContact = "Jane Doe";

			//set billing address that the invoice is billed to for payment
			customerInvoiceRecord.billingAddress1 = "43";
			customerInvoiceRecord.billingAddress2 = " Smith Street";
			customerInvoiceRecord.billingAddress3 = "Melbourne";
			customerInvoiceRecord.billingRegionName = "Victoria";
			customerInvoiceRecord.billingCountryName = "Australia";
			customerInvoiceRecord.billingPostcode = "3000";
			customerInvoiceRecord.billingOrgName = "Supplier Industries International";
			customerInvoiceRecord.billingContact = "Lee Lang";

			//create an array of customer invoice lines
			ArrayList<ESDRecordCustomerInvoiceLine> invoiceLines = new ArrayList<ESDRecordCustomerInvoiceLine>();
			
			//specify the customer organisation based on its ID within the platform, in this example the ID comes from command line arguments
            String customerOrgID = args[3];
			
			//specify the supplier account if the customer organisation has assigned multiple supplier organisations to the account
			String supplierAccountCode = (args.length > 4? args[4]: "");

			//create invoice line record 1
			ESDRecordCustomerInvoiceLine invoicedProduct = new ESDRecordCustomerInvoiceLine();
			invoicedProduct.lineType = ESDocumentConstants.INVOICE_LINE_TYPE_PRODUCT;
			invoicedProduct.productCode = "ACME-SUPPLIER-TTGREEN";
			invoicedProduct.productName = "Green tea towel - 30 x 6 centimetres";
			invoicedProduct.keySellUnitID = "2";
			invoicedProduct.unitName = "EACH";
			invoicedProduct.quantityInvoiced = 4;
			invoicedProduct.sellUnitBaseQuantity = 4;
			invoicedProduct.priceExTax = 5.00;
			invoicedProduct.priceIncTax = 5.50;
			invoicedProduct.priceTax = 0.50;
			invoicedProduct.priceTotalIncTax = 22.00;
			invoicedProduct.priceTotalExTax = 20.00;
			invoicedProduct.priceTotalTax = 2.00;
			//optionally specify customer's product code in purchaseOrderProductCode if it is different to the line's productCode field and the supplier org. knows the customer's codes
			invoicedProduct.purchaseOrderProductCode = "TEA-TOWEL-GREEN";

			//add tax details to the product invoice line
			ESDRecordInvoiceLineTax productTax = new ESDRecordInvoiceLineTax();
			productTax.priceTax = invoicedProduct.priceTax;
			productTax.priceTotalTax = invoicedProduct.priceTotalTax;
			productTax.quantity = invoicedProduct.quantityInvoiced;
			productTax.taxRate = 10.00;
			productTax.taxcode = "GST";
			productTax.taxcodeLabel = "Goods And Services Tax";
			invoicedProduct.taxes = new ArrayList<ESDRecordInvoiceLineTax>(){};
			invoicedProduct.taxes.add(productTax);

			//add 1st invoice line to lines list
			invoiceLines.add(invoicedProduct);

			//add a 2nd invoice line record that is a text line
			invoicedProduct = new ESDRecordCustomerInvoiceLine();
			invoicedProduct.lineType = ESDocumentConstants.INVOICE_LINE_TYPE_TEXT;
			invoicedProduct.textDescription = "Please bundle tea towels into a box";
			invoiceLines.add(invoicedProduct);

			//add a 3rd invoice line product record to the invoice
			invoicedProduct = new ESDRecordCustomerInvoiceLine();
			invoicedProduct.lineType = ESDocumentConstants.INVOICE_LINE_TYPE_PRODUCT;
			invoicedProduct.productCode = "ACME-TTBLUE";
			invoicedProduct.quantityInvoiced = 10;
			invoicedProduct.priceExTax = 10.00;
			invoicedProduct.priceIncTax = 1.10;
			invoicedProduct.priceTax = 1.00;
			invoicedProduct.priceTotalIncTax = 110.00;
			invoicedProduct.priceTotalExTax = 100.00;
			invoicedProduct.priceTotalTax = 10.00;
			invoiceLines.add(invoicedProduct);

			//add lines to the invoice
			customerInvoiceRecord.lines = invoiceLines;

			//set invoice totals
			customerInvoiceRecord.totalPriceIncTax = 132.00;
			customerInvoiceRecord.totalPriceExTax = 120.00;
			customerInvoiceRecord.totalTax = 12.00;
			customerInvoiceRecord.totalLines = invoiceLines.size();
			customerInvoiceRecord.totalProducts = 2;

			//create customer invoices records list and add customer invoice to it
			ArrayList<ESDRecordCustomerInvoice> customerInvoiceRecords = new ArrayList<ESDRecordCustomerInvoice>();
			customerInvoiceRecords.add(customerInvoiceRecord);

			//after 60 seconds give up on waiting for a response from the API when creating the notification
			int timeoutMilliseconds = 60000;

			//create customer invoice Ecommerce Standards document and add customer invoice records to the document
			ESDocumentCustomerInvoice customerInvoiceESD = new ESDocumentCustomerInvoice(ESDocumentConstants.RESULT_SUCCESS, "successfully obtained data", customerInvoiceRecords.toArray(new ESDRecordCustomerInvoice[0]), new HashMap<String, String>());

			//send customer invoice document to the API and onto the customer organisation
			APIv1EndpointResponseESD endpointResponseESD = APIv1EndpointOrgSendCustomerInvoiceToCustomer.call(apiOrgSession, timeoutMilliseconds, customerOrgID, supplierAccountCode, customerInvoiceESD);
			ESDocumentSupplierInvoice esDocumentSupplierInvoice = (ESDocumentSupplierInvoice)endpointResponseESD.esDocument;

			//check the result of sending the supplier invoice
			if (endpointResponseESD.result.toUpperCase()== APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS)
			{
				System.out.println("SUCCESS - organisation customer invoice(s) have successfully been sent to customer organisation.");

				//iterate through each of the returned supplier invoice(s) that the customer invoice(s) were converted from and output the details of the supplier invoices
				if (esDocumentSupplierInvoice.dataRecords != null)
				{
					for (ESDRecordSupplierInvoice supplierInvoiceRecord : esDocumentSupplierInvoice.dataRecords)
					{
						System.out.println("\nCustomer Invoice Returned, Invoice Details: ");
						System.out.println("Supplier Invoice Code: " + supplierInvoiceRecord.supplierInvoiceCode);
						System.out.println("Supplier Invoice Total Cost: " + supplierInvoiceRecord.totalPriceIncTax + " (" + supplierInvoiceRecord.currencyISOCode + ")");
						System.out.println("Supplier Invoice Total Taxes: " + supplierInvoiceRecord.totalTax + " (" + supplierInvoiceRecord.currencyISOCode + ")");
						System.out.println("Supplier Invoice Supplier Account: " + supplierInvoiceRecord.supplierAccountCode);
						System.out.println("Supplier Invoice Total Lines: " + supplierInvoiceRecord.totalLines);
					}
				}
			}
			else {
				System.out.println("FAIL - organisation customer invoice(s) failed to be processed. Reason: " + endpointResponseESD.result_message + " Error Code: " + endpointResponseESD.result_code);

				//check that a Ecommerce standards document was returned
				if (esDocumentSupplierInvoice != null && esDocumentSupplierInvoice.configs != null)
				{
					//if one or more lines in the customer invoice could not match a line to the customer organisation then find out the invoice lines that caused the problem
					HashMap documentConfigs = esDocumentSupplierInvoice.configs;
					if (documentConfigs.containsKey(APIv1EndpointResponseESD.ESD_CONFIG_INVOICES_WITH_UNMAPPED_LINES) || documentConfigs.containsKey(APIv1EndpointResponseESD.ESD_CONFIG_INVOICES_WITH_UNMAPPED_LINE_TAXCODES))
					{
						//get a list of invoice lines that could not be mapped
						ArrayList<Pair<Integer, Integer>> unmappedLines = APIv1EndpointOrgSendCustomerInvoiceToCustomer.getUnmappedInvoiceLines(esDocumentSupplierInvoice);

						//iterate through each unmapped invoice line
						for (Pair<Integer, Integer> unmappedLine : unmappedLines)
						{
							//get the index of the customer invoice and line that contained the unmapped product
							int invoiceIndex = unmappedLine.getKey();
							int lineIndex = unmappedLine.getValue();

							//check that the invoice can be found that contains the problematic line
							if (invoiceIndex < customerInvoiceESD.dataRecords.length && lineIndex < customerInvoiceESD.dataRecords[invoiceIndex].lines.size())
							{
								if(documentConfigs.containsKey(APIv1EndpointResponseESD.ESD_CONFIG_INVOICES_WITH_UNMAPPED_LINE_TAXCODES)){
									System.out.println("For customer invoice: " + customerInvoiceESD.dataRecords[invoiceIndex].customerInvoiceCode + " a matching customer's taxcode for line number: " + (lineIndex + 1) + " could not be matched up or handled.");
								}else{
									System.out.println("For customer invoice: " + customerInvoiceESD.dataRecords[invoiceIndex].customerInvoiceCode + " a matching customer product for line number: " + (lineIndex + 1) + " could not be matched up or handled.");
								}
							}
						}
					}
					//if one or more surcharges in the customer invoice could not match a surcharge to the customer organisation then find out the invoice lines that caused the problem
					else if (esDocumentSupplierInvoice.configs.containsKey(APIv1EndpointResponseESD.ESD_CONFIG_INVOICES_WITH_UNMAPPED_SURCHARGES) || esDocumentSupplierInvoice.configs.containsKey(APIv1EndpointResponseESD.ESD_CONFIG_INVOICES_WITH_UNMAPPED_SURCHARGE_TAXCODES)){
						//get a list of invoice surcharges that could not be mapped
						ArrayList<Pair<Integer, Integer>> unmappedSurcharges = APIv1EndpointOrgSendCustomerInvoiceToCustomer.getUnmappedInvoiceLines(esDocumentSupplierInvoice);

						//iterate through each unmapped invoice surcharge
						for (Pair<Integer, Integer> unmappedSurcharge : unmappedSurcharges)
						{
							//get the index of the customer invoice and surcharge that contained the unmapped surcharge
							int invoiceIndex = unmappedSurcharge.getKey();
							int surchargeIndex = unmappedSurcharge.getValue();

							//check that the invoice can be found that contains the problematic line
							if (invoiceIndex < customerInvoiceESD.dataRecords.length && surchargeIndex < customerInvoiceESD.dataRecords[invoiceIndex].surcharges.size())
							{
								if(documentConfigs.containsKey(APIv1EndpointResponseESD.ESD_CONFIG_INVOICES_WITH_UNMAPPED_SURCHARGE_TAXCODES)){
									System.out.println("For customer invoice: " + customerInvoiceESD.dataRecords[invoiceIndex].customerInvoiceCode + " a matching customer's taxcode for surcharge line number: " + (surchargeIndex + 1) + " could not be matched up or handled.");
								}else{
									System.out.println("For customer invoice: " + customerInvoiceESD.dataRecords[invoiceIndex].customerInvoiceCode + " a matching customer surcharge for surcharge line number: " + (surchargeIndex + 1) + " could not be matched up or handled.");
								}
							}
						}
					}
				}
			}

			//next steps
			//call other API endpoints...
			//destroy API session when done...
			apiOrgSession.destroyOrgSession();
		}
    }
}
