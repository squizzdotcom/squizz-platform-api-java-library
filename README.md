![alt tag](https://www.squizz.com/ui/resources/images/logos/squizz_logo_mdpi.png)

# SQUIZZ.com Platform API Java Library

The [SQUIZZ.com](https://www.squizz.com) Platform API Java Library can be used by Java applications to access the SQUIZZ.com platform's Application Programming Interface (API), allowing data to be pushed and pulled from the API's endpoints in a clean and elegant way. The kinds of data pushed and pulled from the API using the library can include organisational data such as products, sales orders, purchase orders, customer accounts, supplier accounts, notifications, and other data that the platform supports.

This library removes the need for Java software developers to write boilerplate code for connecting and accessing the platform's API, allowing Java software using the platform's API to be writen faster and simpler. The library provides classes and objects that can be directly referenced within a Java application, making it easy to manipulate data retreived from the platform, or create and send data to platform.

If you are a software developer writing a Java application then we recommend that you use this library instead of directly calling the platform's APIs, since it will simplify your development times and allow you to easily incorporate new functionality from the API by simplying updating this library.

- You can find more information about the SQUIZZ.com platform by visiting [https://www.squizz.com/docs/squizz](https://www.squizz.com/docs/squizz)
- To find more information about developing software for the SQUIZZ.com visit [https://www.squizz.com/docs/squizz/Integrate-Software-Into-SQUIZZ.com-Platform.html](https://www.squizz.com/docs/squizz/Integrate-Software-Into-SQUIZZ.com-Platform.html)
- To find more information about the platform's API visit [https://www.squizz.com/docs/squizz/Platform-API.html](https://www.squizz.com/docs/squizz/Platform-API.html)

## Getting Started

To get started using the library within Java applications, download and reference the JAR library to your application using your most preferred way.
To use the library within a Java class using the following import syntax:

```
import org.squizz.api.v1.*;
```

## Create Organisation API Session Endpoint
To start using the SQUIZZ.com platform's API a session must first be created. A session can only be created after credentials for a specified organisation have been given to the API and have been verified.
Once the session has been created then all other endpoints in the API can be called.
Read [https://www.squizz.com/docs/squizz/Platform-API.html#section840](https://www.squizz.com/docs/squizz/Platform-API.html#section840) for more documentation about the endpoint.

```java
import org.squizz.api.v1.*;
import org.squizz.api.v1.endpoint.APIv1EndpointResponse;
import org.esd.EcommerceStandardsDocuments.ESDocumentConstants;

public class ExampleRunner 
{
	public static void main(String[] args)
    	{
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
		
		//next steps
		//call API endpoints...
		//destroy API session when done...
	}
}
```

## Create Organisation Notification Endpoint
The SQUIZZ.com platform's API has an endpoint that allows organisation notifications to be created in the platform. allowing people assigned to an organisation's notification category to receive a notification. 
This can be used to advise such people of events happening external to the platform, such as sales, enquires, tasks completed through websites and other software.
See the example below on how the call the Create Organisation Notification endpoint. Note that a session must first be created in the API before calling the endpoint.
Read [https://www.squizz.com/docs/squizz/Platform-API.html#section854](https://www.squizz.com/docs/squizz/Platform-API.html#section854) for more documentation about the endpoint.


```java
import org.squizz.api.v1.*;
import org.squizz.api.v1.endpoint.*;

public class ExampleRunner 
{
	public static void main(String[] args)
    	{
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
		
		//create organisation notification if the API was successfully created
		if(apiOrgSession.sessionExists())
		{
			//set the notification category that the organisation will display under in the platform, in this case the sales order category
			String notifyCategory = APIv1EndpointOrgCreateNotification.NOTIFY_CATEGORY_ORDER_SALE;
			
			//after 20 seconds give up on waiting for a response from the API when creating the notification
			int timeoutMilliseconds = 20000;
			
			//set the message that will appear in the notification, note the placeholders {1} and {2} that will be replaced with data values
			String message = "A new {1} was created in {2} Website";
			
			//set labels and links to place within the placeholders of the message
			String[] linkLabels = new String[]{"Sales Order","Acme Industries"};
			String[] linkURLs = new String[]{"","http://www.example.com/acmeindustries"};
			
			//call the platform's API to create the organistion notification and have people assigned to organisation's notification category receive it
			APIv1EndpointResponseESD endpointResponseESD = APIv1EndpointOrgCreateNotification.call(apiOrgSession, timeoutMilliseconds, notifyCategory, message, linkURLs, linkLabels);
			
			if(endpointResponseESD.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS)){
				System.out.println("SUCCESS - organisation notification successfully created in the platform");
			}else{
				System.out.println("FAIL - organisation notification failed to be created. Reason: " + endpointResponseESD.result_message  + " Error Code: " + endpointResponseESD.result_code);
			}
		}
		
		//next steps
		//call other API endpoints...
		//destroy API session when done...
	}
}
```

## Import Organisation Data Endpoint
The SQUIZZ.com platform's API has an endpoint that allows a wide variety of different types of data to be imported into the platform against an organisation. 
This organisational data includes taxcodes, products, customer accounts, supplier accounts. pricing, price levels, locations, and many other kinds of data.
This data is used to allow the organisation to buy and sell products, as well manage customers, suppliers, employees, and other people.
Each type of data needs to be imported as an "Ecommerce Standards Document" that contains one or more records. Use the Ecommerce Standards library to easily create these documents and records.
When importing one type of organisational data, it is important to import the full data set, otherwise the platform will deactivate unimported data.
For example if 3 products are imported, then a another products import is run that only imports 2 records, then 1 product will become deactivated and no longer be able to be sold.
Read [https://www.squizz.com/docs/squizz/Platform-API.html#section843](https://www.squizz.com/docs/squizz/Platform-API.html#section843) for more documentation about the endpoint and its requirements.
See the example below on how the call the Import Organisation ESD Data endpoint. Note that a session must first be created in the API before calling the endpoint.

```java
import org.squizz.api.v1.*;
import org.squizz.api.v1.endpoint.*;
import org.esd.EcommerceStandardsDocuments.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ExampleRunner 
{
	public static void main(String[] args)
	{
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
		
		//import organisation data if the API was successfully created
		if(apiOrgSession.sessionExists())
		{
			//create taxcode records
			ArrayList<ESDRecordTaxcode> taxcodeRecords = new ArrayList<>();
			ESDRecordTaxcode taxcodeRecord = new ESDRecordTaxcode();
			taxcodeRecord.keyTaxcodeID = "1";
			taxcodeRecord.taxcode = "GST";
			taxcodeRecord.taxcodeLabel = "GST";
			taxcodeRecord.description = "Goods And Services Tax";
			taxcodeRecord.taxcodePercentageRate = 10;
			taxcodeRecords.add(taxcodeRecord);
            
			taxcodeRecord = new ESDRecordTaxcode();
			taxcodeRecord.keyTaxcodeID = "2";
			taxcodeRecord.taxcode = "FREE";
			taxcodeRecord.taxcodeLabel = "Tax Free";
			taxcodeRecord.description = "Free from Any Taxes";
			taxcodeRecord.taxcodePercentageRate = 0;
			taxcodeRecords.add(taxcodeRecord);
			
			taxcodeRecord = new ESDRecordTaxcode();
			taxcodeRecord.keyTaxcodeID = "3";
			taxcodeRecord.taxcode = "NZGST";
			taxcodeRecord.taxcodeLabel = "New Zealand GST Tax";
			taxcodeRecord.description = "New Zealand Goods and Services Tax";
			taxcodeRecord.taxcodePercentageRate = 15;
			taxcodeRecords.add(taxcodeRecord);
            
			//create a hashmap containing configurations of the organisation taxcode data
			HashMap<String, String> configs = new HashMap<>();
            
			//add a dataFields attribute that contains a comma delimited list of tacode record fields that the API is allowed to insert, update in the platform
			configs.put("dataFields", "keyTaxcodeID,taxcode,taxcodeLabel,description,taxcodePercentageRate");

			//create a Ecommerce Standards Document that stores an array of taxcode records
			ESDocumentTaxcode taxcodeESD = new ESDocumentTaxcode(ESDocumentConstants.RESULT_SUCCESS, "successfully obtained data", taxcodeRecords.toArray(new ESDRecordTaxcode[0]), configs);
			
			//after 30 seconds give up on waiting for a response from the API when creating the notification
			int timeoutMilliseconds = 30000;
			
			//call the platform's API to import in the organisation's data
			APIv1EndpointResponseESD endpointResponseESD = APIv1EndpointOrgImportESDocument.call(apiOrgSession, timeoutMilliseconds, APIv1EndpointOrgImportESDocument.IMPORT_TYPE_ID_TAXCODES, taxcodeESD);
			
			//check that the data successfully imported
			if(endpointResponseESD.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS)){
				System.out.println("SUCCESS - organisation data successfully imported into the platform");
			}else{
				System.out.println("FAIL - organisation data successfully imported into the platform. Reason: " + endpointResponseESD.result_message  + " Error Code: " + endpointResponseESD.result_code);
			}
		}
		
		//next steps
		//call other API endpoints...
		//destroy API session when done...
	}
}
```

## Send and Procure Purchase Order From Supplier Endpoint

The SQUIZZ.com platform's API has an endpoint that allows an orgnisation to import a purchase order. and have it procured/converted into a sales order of a designated supplier organisation. 
This endpoint allows a customer organisation to commit to buy goods and services of an organisation, and have the order processed, and delivered by the supplier organisation.
The endpoint relies upon a connection first being made between organisations within the SQUIZZ.com platform.
The endpoint relies upon being able to find matching supplier products as what has been ordered.
The endpoint has a number of other requirements. See the endpoint documentation for more details on these requirements.

Each purchase order needs to be imported within a "Ecommerce Standards Document" that contains a record for each purchase order. Use the Ecommerce Standards library to easily create these documents and records.
It is recommended to only import one purchase order at a time, since if an array of purchase orders is imported and one order failed to be procured, then no other orders in the list will be attempted to import.
Read [https://www.squizz.com/docs/squizz/Platform-API.html#section961](https://www.squizz.com/docs/squizz/Platform-API.html#section961) for more documentation about the endpoint and its requirements.
See the example below on how the call the Send and Procure Purchase order From Supplier endpoint. Note that a session must first be created in the API before calling the endpoint.


```java
import org.squizz.api.v1.*;
import org.squizz.api.v1.endpoint.*;
import org.esd.EcommerceStandardsDocuments.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ExampleRunner 
{
	public static void main(String[] args)
	{
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
			APIv1EndpointResponseESD endpointResponseESD = APIv1EndpointOrgProcurePurchaseOrderFromSupplier.call(apiOrgSession, timeoutMilliseconds, supplierOrgID, customerAccountCode, orderPurchaseESD);
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
```

## Validate Organisation API Session Endpoint

After a session has been created with SQUIZZ.com platform's API, if the same session is persistently being used over a long period time, then its worth validating that the session has not been destroyed by the API.
The SQUIZZ.com platform's API will automatically expire and destory sessions that have existed for a long period of time.
Read [https://www.squizz.com/docs/squizz/Platform-API.html#section842](https://www.squizz.com/docs/squizz/Platform-API.html#section842) for more documentation about the endpoint.

```java
import org.squizz.api.v1.*;
import org.squizz.api.v1.endpoint.APIv1EndpointResponse;

public class ExampleRunner 
{
	public static void main(String[] args)
	{
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
		
		//next steps
		//call API endpoints...
		
		//check if the session still is valid
		endpointResponse = apiOrgSession.validateOrgSession();
		
		//check the result of validating the session
		if(endpointResponse.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS)){
			System.out.println("SUCCESS - API session successfully validated.");
		}else{
			System.out.println("FAIL - API session failed to be validated. Reason: " + endpointResponse.result_message  + " Error Code: " + endpointResponse.result_code);
		}
		
		//destroy API session when done...
    }
}
```

## Validate/Create Organisation API Session Endpoint

After a session has been created with SQUIZZ.com platform's API, if the same session is persistently being used over a long period time, then a helper method in the library can be used to check if the API session is still valid, then if not have a new session be created.
The SQUIZZ.com platform's API will automatically expire and destory sessions that have existed for a long period of time.

```java
import org.squizz.api.v1.*;
import org.squizz.api.v1.endpoint.APIv1EndpointResponse;

public class ExampleRunner 
{
	public static void main(String[] args)
	{
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
		
		//next steps
		//call API endpoints...
		
		//check if the session still is valid, if not have a new session created with the same organisation API credentials
		endpointResponse = apiOrgSession.validateCreateOrgSession();
		
		//check the result of validating or creating a new session
		if(endpointResponse.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS)){
			System.out.println("SUCCESS - API session successfully validated/created.");
		}else{
			System.out.println("FAIL - API session failed to be validated or created. Reason: " + endpointResponse.result_message  + " Error Code: " + endpointResponse.result_code);
		}
		
		//destroy API session when done...
    }
}
```

## Destroy Organisation API Session Endpoint

After a session has been created with SQUIZZ.com platform's API, if after calling other endpoints there no need for the session anymore, then it's advisable to destroy the session as soon as possible.
Read [https://www.squizz.com/docs/squizz/Platform-API.html#section841](https://www.squizz.com/docs/squizz/Platform-API.html#section841) for more documentation about the endpoint.

```java
import org.squizz.api.v1.*;
import org.squizz.api.v1.endpoint.APIv1EndpointResponse;

public class ExampleRunner 
{
	public static void main(String[] args)
	{
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
		
		//next steps
		//call API endpoints...
		
		//destroy the session in the platform's API
		endpointResponse = apiOrgSession.destroyOrgSession();
		
		//check the result of destroying the session
		if(endpointResponse.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS)){
			System.out.println("SUCCESS - API session successfully destroyed.");
		}else{
			System.out.println("FAIL - API session failed to be destroyed. Reason: " + endpointResponse.result_message  + " Error Code: " + endpointResponse.result_code);
		}
	}
}
```
