![alt tag](https://www.squizz.com/ui/resources/images/logos/squizz_logo_mdpi.png)

# SQUIZZ.com Platform API Java Library

The [SQUIZZ.com](https://www.squizz.com) Platform API Java Library can be used by Java applications to access the SQUIZZ.com platform's Application Programming Interface (API), allowing data to be pushed and pulled from the API's endpoints in a clean and elegant way. The kinds of data pushed and pulled from the API using the library can include organisational data such as products, sales orders, purchase orders, customer accounts, supplier accounts, notifications, and other data that the platform supports.

This library removes the need for Java software developers to write boilerplate code for connecting and accessing the platform's API, allowing Java software using the platform's API to be writen faster and simpler. The library provides classes and objects that can be directly referenced within a Java application, making it easy to manipulate data retreived from the platform, or create and send data to platform.

If you are a software developer writing a Java application then we recommend that you use this library instead of directly calling the platform's APIs, since it will simplify your development times and allow you to easily incorporate new functionality from the API by simply updating this library.

- You can find more information about the SQUIZZ.com platform by visiting [https://www.squizz.com/docs/squizz](https://www.squizz.com/docs/squizz)
- To find more information about developing software for the SQUIZZ.com visit [https://www.squizz.com/docs/squizz/Integrate-Software-Into-SQUIZZ.com-Platform.html](https://www.squizz.com/docs/squizz/Integrate-Software-Into-SQUIZZ.com-Platform.html)
- To find more information about the platform's API visit [https://www.squizz.com/docs/squizz/Platform-API.html](https://www.squizz.com/docs/squizz/Platform-API.html)
- Examples on how use this library can be found within the files in the [example](https://github.com/squizzdotcom/squizz-platform-api-java-library/tree/master/test/org/squizz/api/v1/example/) directory or in the sections below.

## Contents

  * [Getting Started](#getting-started)
  * [Example Usages](#example-usages)
    * [Create Organisation API Session Endpoint](#create-organisation-api-session-endpoint)
    * [Create Organisation Notification Endpoint](#create-organisation-notification-endpoint)
    * [Import Organisation Data Endpoint](#import-organisation-data-endpoint)
    * [Import Organisation Sales Order Endpoint](#import-organisation-sales-order-endpoint)
    * [Retrieve Organisation Data Endpoint](#retrieve-organisation-data-endpoint)
    * [Search Customer Account Records Endpoint](#search-customer-account-records-endpoint)
    * [Retrieve Customer Account Record Endpoint](#retrieve-customer-account-record-endpoint)
    * [Send and Procure Purchase Order From Supplier Endpoint](#send-and-procure-purchase-order-from-supplier-endpoint)
    * [Validate Organisation API Session Endpoint](#validate-organisation-api-session-endpoint)
    * [Validate/Create Organisation API Session Endpoint](#validatecreate-organisation-api-session-endpoint)
    * [Destroy Organisation API Session Endpoint](#destroy-organisation-api-session-endpoint)

## Getting Started

To get started using the library within Java applications, download the Java API library and its dependent libraries from the [Release page](https://github.com/squizzdotcom/squizz-platform-api-java-library/releases) and add references to the JAR libraries in your application using your most preferred way (such as within a class path).
The library contains dependencies on the [Jackson JSON Java Library](https://github.com/FasterXML/jackson) as well as the [Ecommerce Standards Documents Java Library](https://github.com/squizzdotcom/ecommerce-standards-documents-java-library)
Once the library is referenced within your Java application then to use it within a Java class you can use the following import syntax:

```
import org.squizz.api.v1.*;
import org.squizz.api.v1.endpoint.*;
```

## Example Usages
### Create Organisation API Session Endpoint
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

### Create Organisation Notification Endpoint
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

### Import Organisation Data Endpoint
The SQUIZZ.com platform's API has an endpoint that allows a wide variety of different types of data to be imported into the platform against an organisation. 
This organisational data includes taxcodes, products, customer accounts, supplier accounts. pricing, price levels, locations, maker, and many other kinds of data.
This data is used to allow the organisation to buy and sell products, as well manage customers, suppliers, employees, and other people.
Each type of data needs to be imported as an "Ecommerce Standards Document" that contains one or more records. Use the Ecommerce Standards library to easily create these documents and records.
When importing one type of organisational data, it is important to import the full data set, otherwise the platform will deactivate unimported data.
For example if 3 products are imported, then another products import is run that only imports 2 records, then 1 product will become deactivated and no longer be able to be sold.
Read [https://www.squizz.com/docs/squizz/Platform-API-Endpoint:-Import-Organisation-Data.html](https://www.squizz.com/docs/squizz/Platform-API-Endpoint:-Import-Organisation-Data.html) for more documentation about the endpoint and its requirements.
See the example below on how the call the Import Organisation ESD Data endpoint. Note that a session must first be created in the API before calling the endpoint.

Other examples exist in this repository's examples folder on how to import serveral different types of data:
 - Import Products [APIv1ExampleRunnerImportOrgESDDataProducts.java](https://github.com/squizzdotcom/squizz-platform-api-java-library/tree/master/test/org/squizz/api/v1/example/APIv1ExampleRunnerImportOrgESDDataProducts.java)
 - Import Taxcodes [APIv1ExampleRunnerImportOrgESDDataTaxcodes.java](https://github.com/squizzdotcom/squizz-platform-api-java-library/tree/master/test/org/squizz/api/v1/example/APIv1ExampleRunnerImportOrgESDDataTaxcodes.java)
 - Import Categories [APIv1ExampleRunnerImportOrgESDDataCategories.java](https://github.com/squizzdotcom/squizz-platform-api-java-library/tree/master/test/org/squizz/api/v1/example/APIv1ExampleRunnerImportOrgESDDataCategories.java)
 - Import Attributes [APIv1ExampleRunnerImportOrgESDDataAttributes.java](https://github.com/squizzdotcom/squizz-platform-api-java-library/tree/master/test/org/squizz/api/v1/example/APIv1ExampleRunnerImportOrgESDDataAttributes.java)
 - Import Makers [APIv1ExampleRunnerImportOrgESDDataMakers.java](https://github.com/squizzdotcom/squizz-platform-api-java-library/tree/master/test/org/squizz/api/v1/example/APIv1ExampleRunnerImportOrgESDDataMakers.java)
 - Import Maker Models [APIv1ExampleRunnerImportOrgESDDataMakerModels.java](https://github.com/squizzdotcom/squizz-platform-api-java-library/tree/master/test/org/squizz/api/v1/example/APIv1ExampleRunnerImportOrgESDDataMakerModels.java)
 - Import Maker Model Mappings [APIv1ExampleRunnerImportOrgESDDataMakerModelMappings.java](https://github.com/squizzdotcom/squizz-platform-api-java-library/tree/master/test/org/squizz/api/v1/example/APIv1ExampleRunnerImportOrgESDDataMakerModelMappings.java)

```java
	import org.squizz.api.v1.*;
	import org.squizz.api.v1.endpoint.*;
	import org.esd.EcommerceStandardsDocuments.*;
	import java.util.ArrayList;
	import java.util.HashMap;

	public class APIv1ExampleRunnerImportOrgESDDataProducts
	{
		public static void main(String[] args)
		{
			//check that the required arguments have been given
			if(args.length < 3){
				System.out.println("Set the following arguments: [orgID] [orgAPIKey] [orgAPIPass]");
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
			
			//import organisation product data if the API was successfully created
			if(apiOrgSession.sessionExists())
			{
				//create product records
				ArrayList<ESDRecordProduct> productRecords = new ArrayList<>();
				
				//create 1st product record
				ESDRecordProduct productRecord = new ESDRecordProduct();
				productRecord.keyProductID = "PROD-002";
				productRecord.productCode = "PROD-123";
				productRecord.keyTaxcodeID = "FREE";
				
				//add product to list of products
				productRecords.add(productRecord);
				
				//create 2nd product record
				productRecord = new ESDRecordProduct();
				productRecord.keyProductID = "PROD-001";
				productRecord.productCode = "PRODUCT001";
				productRecord.keyTaxcodeID = "GST";
				productRecord.productSearchCode = "Green-Recycled-Paper-Swisho-001";
				productRecord.barcode = "03423404230";
				productRecord.barcodeInner = "234234";
				productRecord.brand = "Swisho Paper";
				productRecord.name = "Swisho Green Paper";
				productRecord.description1 = "Swisho green coloured paper is the ultimate green paper.";
				productRecord.description2 = "Paper built strong and tough by Swisho";
				productRecord.description3 = "Recommended to be used with dark inks.";
				productRecord.description4 = "";
				productRecord.productClass = "paper";
				productRecord.unit = "REAM";
				productRecord.weight = 20.1;
				productRecord.width= 21;
				productRecord.height = 29.7;
				productRecord.depth = 10;
				productRecord.averageCost = 10.00;
				productRecord.warehouse = "Swisho Warehouse";
				productRecord.supplier = "Swisho";
				productRecord.deliveryTimeNoStock = "112112";
				productRecord.deliveryTimeInStock = "1212";
				productRecord.stockQuantity = 200;
				productRecord.stockNoneQuantity = 0;
				productRecord.stockLowQuantity = 10;
				productRecord.isPriceTaxInclusive = ESDocumentConstants.ESD_VALUE_NO;
				productRecord.isKitted = ESDocumentConstants.ESD_VALUE_NO;
				productRecord.kitProductsSetPrice = ESDocumentConstants.ESD_VALUE_NO;
				productRecord.keySellUnitID = "2";
				productRecord.ordering = 1;
				
				//add sell units to the product to indicate the different ways its quantities can be bundled up as
				ArrayList<ESDRecordSellUnit> sellUnits = new ArrayList<>();
				
				//add 1st sell unit, representing the product being sold in individual "REAM" units
				ESDRecordSellUnit sellUnitRecord = new ESDRecordSellUnit();
				sellUnitRecord.keySellUnitID = "2";
				sellUnitRecord.baseQuantity = 1;
				sellUnitRecord.sellUnitLabel = "Ream";
				sellUnits.add(sellUnitRecord);
				
				//add 2nd sell unit, where product is being bundled as a pack of 6 reams
				sellUnitRecord = new ESDRecordSellUnit();
				sellUnitRecord.keySellUnitID = "3";
				sellUnitRecord.keySellUnitParentID = "2";
				sellUnitRecord.baseQuantity = 6;
				sellUnitRecord.sellUnitLabel = "6 pack of Reams";
				sellUnits.add(sellUnitRecord);
				
				//add 3rd sell unit, where product is being bundled as a carton containing 4 packs of reams, containing 24 individual ream units in total
				sellUnitRecord = new ESDRecordSellUnit();
				sellUnitRecord.keySellUnitID = "4";
				sellUnitRecord.keySellUnitParentID = "3";
				sellUnitRecord.baseQuantity = 24;
				sellUnitRecord.parentQuantity = 4;
				sellUnitRecord.sellUnitLabel = "Carton of ream packs";
				sellUnits.add(sellUnitRecord);
				
				//add sell units to the product record
				productRecord.sellUnits = sellUnits.toArray(new ESDRecordSellUnit[0]);
				
				//add the 2nd product record to the list of products
				productRecords.add(productRecord);
				
				//create a hashmap containing configurations of the organisation product data
				HashMap<String, String> configs = new HashMap<>();
				
				//add a dataFields attribute that contains a comma delimited list of product record fields that the API is allowed to insert, update in the platform
				configs.put("dataFields", "keyProductID,productCode,keyTaxcodeID,productSearchCode,barcode,barcodeInner,brand,name,description1,description2,description3,description4,productClass,keySellUnitID,unit,weight,width,height,depth,averageCost,warehouse,supplier,deliveryTimeNoStock,deliveryTimeInStock,stockQuantity,stockNoneQuantity,stockLowQuantity,stockLowQuantity,isPriceTaxInclusive,isKitted,kitProductsSetPrice");
				
				//create a Ecommerce Standards Document that stores an array of product records
				ESDocumentProduct productESD = new ESDocumentProduct(ESDocumentConstants.RESULT_SUCCESS, "successfully obtained data", productRecords.toArray(new ESDRecordProduct[0]), configs);
				
				//after 30 seconds give up on waiting for a response from the API when creating the notification
				int timeoutMilliseconds = 30000;
				
				//call the platform's API to import in the organisation's product data
				APIv1EndpointResponseESD endpointResponseESD = APIv1EndpointOrgImportESDocument.call(apiOrgSession, timeoutMilliseconds, APIv1EndpointOrgImportESDocument.IMPORT_TYPE_ID_PRODUCTS, productESD);
				
				//check that the product data successfully imported for the organisation
				if(endpointResponseESD.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS)){
					System.out.println("SUCCESS - product data successfully imported into the platform against the organisation");
				}else{
					System.out.println("FAIL - product data failed to be imported into the platform against the organisation. Reason: " + endpointResponseESD.result_message  + " Error Code: " + endpointResponseESD.result_code);
				}
			}
			
			//next steps
			//call other API endpoints...
			//destroy API session when done...
			apiOrgSession.destroyOrgSession();
		}
	}
```

### Import Organisation Sales Order Endpoint
The SQUIZZ.com platform's API has an endpoint that allows an organisation to import a sales order into the SQUIZZ.com platform, against its own organisation.
This endpoint is typically used by an organisation to import sales orders from any systems, websites or services it uses to capture sales orders from, including Ecommerce websites, online marketplaces, Customer Relationship Management (CRM) systems, quoting software tools, or any other business systems and software. 
Note that this endpoint should not be used by customer organisations to send orders to supplying organisations. For that use case the [Send and Procure Purchase Order From Supplier Endpoint](#send-and-procure-purchase-order-from-supplier-endpoint) should be called instead.
When calling the endpoint there is an parameter that can optionally allow the order to be re-priced or not. This allows the most up-to-date pricing to be set in the sales order when imported.
Each sales order needs to be imported as an "Ecommerce Standards Document" that contains one or more records. Use the Ecommerce Standards library to easily create the Sales Order documents and Sales Order records.
Read [https://www.squizz.com/docs/squizz/Platform-API-Endpoint:-Import-Organisation-Sales-Order.html](https://www.squizz.com/docs/squizz/Platform-API-Endpoint:-Import-Organisation-Sales-Order.html) for more documentation about the endpoint and its requirements.
See the example below on how the call the Import Organisation Sales Order ESD Data endpoint. Note that a session must first be created in the API before calling the endpoint.

```java
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
		apiOrgSession.destroyOrgSession();
    }
}
```

### Retrieve Organisation Data Endpoint
The SQUIZZ.com platform's API has an endpoint that allows a variety of different types of data to be retrieved from another organisation stored on the platform.
The organisational data that can be retrieved includes products, product stock quantities, product pricing, categories, attributes, makers, maker models and maker model mappings.
The data retrieved can be used to allow an organisation to set additional information about products being bought or sold, as well as being used in many other ways.
Each kind of data retrieved from endpoint is formatted as JSON data conforming to the "Ecommerce Standards Document" standards, with each document containing an array of zero or more records. Use the Ecommerce Standards library to easily read through these documents and records, to find data natively using Java classes.
Read [https://www.squizz.com/docs/squizz/Platform-API-Endpoint:-Retrieve-Organisation-Data.html](https://www.squizz.com/docs/squizz/Platform-API-Endpoint:-Retrieve-Organisation-Data.html) for more documentation about the endpoint and its requirements.
See the example below on how the call the Retrieve Organisation ESD Data endpoint. Note that a session must first be created in the API before calling the endpoint.

Other examples exist in this repository's examples folder on how to retrieve serveral different types of data. Note that some of these examples show how to different types of data can be retrieved and combined together, showing the interconnected nature of several data types:
 - Retrieve Products [APIv1ExampleRunnerRetrieveOrgESDData.java](https://github.com/squizzdotcom/squizz-platform-api-java-library/tree/master/test/org/squizz/api/v1/example/APIv1ExampleRunnerRetrieveOrgESDData.java)
 - Retrieve Product Stock Quantities [APIv1ExampleRunnerRetrieveOrgESDData.java](https://github.com/squizzdotcom/squizz-platform-api-java-library/tree/master/test/org/squizz/api/v1/example/APIv1ExampleRunnerRetrieveOrgESDData.java)
 - Retrieve Product Pricing [APIv1ExampleRunnerRetrieveOrgESDData.java](https://github.com/squizzdotcom/squizz-platform-api-java-library/tree/master/test/org/squizz/api/v1/example/APIv1ExampleRunnerRetrieveOrgESDData.java)
 - Retrieve Categories [APIv1ExampleRunnerRetrieveOrgESDDataCategories.java](https://github.com/squizzdotcom/squizz-platform-api-java-library/tree/master/test/org/squizz/api/v1/example/APIv1ExampleRunnerRetrieveOrgESDDataCategories.java)
 - Retrieve Attributes [APIv1ExampleRunnerRetrieveOrgESDDataAttributes.java](https://github.com/squizzdotcom/squizz-platform-api-java-library/tree/master/test/org/squizz/api/v1/example/APIv1ExampleRunnerRetrieveOrgESDDataAttributes.java)
 - Retrieve Makers [APIv1ExampleRunnerRetrieveOrgESDDataMakers.java](https://github.com/squizzdotcom/squizz-platform-api-java-library/tree/master/test/org/squizz/api/v1/example/APIv1ExampleRunnerRetrieveOrgESDDataMakers.java)
 - Retrieve Maker Models [APIv1ExampleRunnerRetrieveOrgESDDataMakerModels.java](https://github.com/squizzdotcom/squizz-platform-api-java-library/tree/master/test/org/squizz/api/v1/example/APIv1ExampleRunnerRetrieveOrgESDDataMakerModels.java)
 - Retrieve Maker Model Mappings [APIv1ExampleRunnerRetrieveOrgESDDataMakerModelMappings.java](https://github.com/squizzdotcom/squizz-platform-api-java-library/tree/master/test/org/squizz/api/v1/example/APIv1ExampleRunnerRetrieveOrgESDDataMakerModelMappings.java)

```java
	import java.util.Calendar;
	import java.util.HashMap;
	import org.squizz.api.v1.endpoint.APIv1EndpointOrgRetrieveESDocument;
	import org.squizz.api.v1.*;
	import org.squizz.api.v1.endpoint.*;
	import org.esd.EcommerceStandardsDocuments.*;

	/**
	* Shows an example of creating a organisation session with the SQUIZZ.com platform's API, then retrieves Category data from a connected organisation in the platform
	*/
	public class APIv1ExampleRunnerRetrieveOrgESDDataCategories
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
			int recordsMaxAmount = 5000;
			int recordsStartIndex = 0;
			boolean getMoreRecords = true;
			int recordNumber = 0;
			int pageNumber = 0;
			Calendar calendar = Calendar.getInstance();
			String result = "FAIL";
			HashMap<String, ESDRecordProduct> productsRecordIndex = new HashMap<>();
			HashMap<String, ESDRecordCategory> categoriesRecordIndex = new HashMap<>();
			
			//specify the supplier organisation to get data from based on its ID within the platform, in this example the ID comes from command line arguments
			String supplierOrgID = args[3];
			
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
			
			//retrieve product data, since it is used to find details of products assigned to categories
			if(apiOrgSession.sessionExists())
			{
				System.out.println("Attempting to obtain product data.");
				recordsStartIndex = 0;
				pageNumber = 0;
				getMoreRecords = true;
				
				//after 120 seconds give up on waiting for a response from the API
				int timeoutMilliseconds = 120000;

				//get the next page of records if needed
				while(getMoreRecords)
				{
					pageNumber++;
					getMoreRecords = false;
					
					//call the platform's API to retrieve the organisation's product data
					APIv1EndpointResponseESD endpointResponseESD = APIv1EndpointOrgRetrieveESDocument.call(apiOrgSession, timeoutMilliseconds, APIv1EndpointOrgRetrieveESDocument.RETRIEVE_TYPE_ID_PRODUCTS, supplierOrgID, "", recordsMaxAmount, recordsStartIndex, "");

					System.out.println("Attempt made to obtain product data. Page number: "+pageNumber);

					//check that the data successfully retrieved
					if(endpointResponseESD.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS))
					{	
						ESDocumentProduct esDocumentProduct = (ESDocumentProduct)endpointResponseESD.esDocument;
						
						//check that records have been placed into the standards document
						if(esDocumentProduct.dataRecords != null)
						{
							//iterate through each product record stored within the standards document
							for (ESDRecordProduct productRecord: esDocumentProduct.dataRecords){
								productsRecordIndex.putIfAbsent(productRecord.keyProductID, productRecord);
							}

							//check if there are more records to retrieve
							if(esDocumentProduct.dataRecords.length >= recordsMaxAmount)
							{
								recordsStartIndex = recordsStartIndex + recordsMaxAmount;
								getMoreRecords = true;
							}
						}else{
							System.out.println("No more records obtained. Page number: "+pageNumber);
						}
						
						result = "SUCCESS";
					}else{
						result = "FAIL";
						System.out.println("FAIL - not all organisation product data could be obtained from the platform. Reason: " + endpointResponseESD.result_message  + " Error Code: " + endpointResponseESD.result_code);
					}
				}
			}
			
			//retrieve category data. Initially index it so that lookups can be done between linked categories, regardless of the order in which category records were obtained
			if(result.equals("SUCCESS"))
			{
				System.out.println("Attempting to obtain category data.");
				recordsStartIndex = 0;
				pageNumber = 0;
				getMoreRecords = true;
				
				//after 120 seconds give up on waiting for a response from the API
				int timeoutMilliseconds = 120000;

				//get the next page of records if needed
				while(getMoreRecords)
				{
					pageNumber++;
					getMoreRecords = false;
					
					//call the platform's API to retrieve the organisation's category data
					APIv1EndpointResponseESD endpointResponseESD = APIv1EndpointOrgRetrieveESDocument.call(apiOrgSession, timeoutMilliseconds, APIv1EndpointOrgRetrieveESDocument.RETRIEVE_TYPE_ID_CATEGORIES, supplierOrgID, "", recordsMaxAmount, recordsStartIndex, "");

					System.out.println("Attempt made to obtain category data. Page number: "+pageNumber);

					//check that the data successfully retrieved
					if(endpointResponseESD.result.equals(APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS))
					{	
						System.out.println("SUCCESS - maker model mapping data successfully obtained from the platform for page number: "+pageNumber);
						
						ESDocumentCategory esDocumentCategory = (ESDocumentCategory)endpointResponseESD.esDocument;
						
						//check that records have been placed into the standards document
						if(esDocumentCategory.dataRecords != null)
						{
							System.out.println("Category Records Returned: " + esDocumentCategory.totalDataRecords);
							
							//iterate through each category record stored within the standards document
							for (ESDRecordCategory categoryRecord: esDocumentCategory.dataRecords){
								categoriesRecordIndex.putIfAbsent(categoryRecord.keyCategoryID, categoryRecord);
							}

							//check if there are more records to retrieve
							if(esDocumentCategory.dataRecords.length >= recordsMaxAmount)
							{
								recordsStartIndex = recordsStartIndex + recordsMaxAmount;
								getMoreRecords = true;
							}
						}else{
							System.out.println("No more records obtained. Page number: "+pageNumber);
						}
						
						result = "SUCCESS";
					}else{
						result = "FAIL";
						System.out.println("FAIL - not all organisation category data could be obtained from the platform. Reason: " + endpointResponseESD.result_message  + " Error Code: " + endpointResponseESD.result_code);
					}
				}
			}
			
			//output the details of each category
			if(result.equals("SUCCESS"))
			{
				System.out.println("Outputting category data.");
				
				//iterate through and output each category previously obtained
				for(String keyCategoryID: categoriesRecordIndex.keySet())
				{
					ESDRecordCategory categoryRecord = categoriesRecordIndex.get(keyCategoryID);
					String parentCategoryCode = "";

					//find the parent category that the category may be assigned to
					if(categoriesRecordIndex.containsKey(categoryRecord.keyCategoryParentID)){
						parentCategoryCode = categoriesRecordIndex.get(categoryRecord.keyCategoryParentID).categoryCode;
					}

					//output details of the category record
					System.out.println(APIv1OrgTestRunner.CONSOLE_LINE);
					System.out.println("     Category Record #: " + recordNumber);
					System.out.println("       Key Category ID: " + categoryRecord.keyCategoryID);
					System.out.println("         Category Code: " + categoryRecord.categoryCode);
					System.out.println("         Category Name: " + categoryRecord.name);
					System.out.println("Key Category Parent ID: " + categoryRecord.keyCategoryParentID);
					System.out.println("  Parent Category Code: " + parentCategoryCode);
					System.out.println("          Description1: " + categoryRecord.description1);
					System.out.println("          Description2: " + categoryRecord.description2);
					System.out.println("          Description3: " + categoryRecord.description3);
					System.out.println("          Description4: " + categoryRecord.description4);
					System.out.println("            Meta Title: " + categoryRecord.metaTitle);
					System.out.println("         Meta Keywords: " + categoryRecord.metaKeywords);
					System.out.println("      Meta Description: " + categoryRecord.metaDescription);
					System.out.println("              Ordering: " + categoryRecord.ordering);
					
					//check if the category contains any products
					if(categoryRecord.keyProductIDs != null && categoryRecord.keyProductIDs.length > 0)
					{
						System.out.println("        Products Count: " + categoryRecord.keyProductIDs.length);

						//output each product assigned to the category
						int productCount = 0;
						for (String keyProductID: categoryRecord.keyProductIDs)
						{
							productCount++;
							
							//check that the product has been previously obtained
							if(productsRecordIndex.containsKey(keyProductID))
							{
								ESDRecordProduct productRecord = productsRecordIndex.get(keyProductID);

								//output details of the product assigned to the category
								System.out.println();
								System.out.println("             Product #: " + productCount);
								System.out.println("        Key Product ID: " + keyProductID);
								System.out.println("          Product Code: " + productRecord.productCode);
								System.out.println("          Product Name: " + productRecord.name);
							}
						}
					}

					recordNumber++;
				}
			}
			
			//next steps
			//call other API endpoints...
			//destroy API session when done
			apiOrgSession.destroyOrgSession();
		}
	}
```

### Search Customer Account Records Endpoint
The SQUIZZ.com platform's API has an endpoint that allows an organisation to search for records within another connected organisation's business sytem, based on records associated to an assigned customer account.
This endpoint allows an organisation to securely search for invoice, sales order, back order, transactions. credit and payment records, retrieved in realtime from a supplier organisation's connected business system.
The records returned from endpoint is formatted as JSON data conforming to the "Ecommerce Standards Document" standards, with each document containing an array of zero or more records. Use the Ecommerce Standards library to easily read through these documents and records, to find data natively using Java classes.
Read [https://www.squizz.com/docs/squizz/Platform-API.html#section1473](https://www.squizz.com/docs/squizz/Platform-API.html#section1473) for more documentation about the endpoint and its requirements.
See the example below on how the call the Search Customer Account Records endpoint. Note that a session must first be created in the API before calling the endpoint.

```java
package org.squizz.api.v1.example;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.squizz.api.v1.endpoint.APIv1EndpointOrgSearchCustomerAccountRecords;
import org.squizz.api.v1.*;
import org.squizz.api.v1.endpoint.*;
import org.esd.EcommerceStandardsDocuments.*;

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
            
        //optionally get the customer account code if required based on the type of data being retrieved, in this example the account code comes from command line arguments
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
```

### Retrieve Customer Account Record Endpoint
The SQUIZZ.com platform's API has an endpoint that allows an organisation to retrieve the details and lines for a record from another connected organisation's business sytem, based on a record associated to an assigned customer account.
This endpoint allows an organisation to securely get the details for a invoice, sales order, back order, transactions. credit or payment record, retrieved in realtime from a supplier organisation's connected business system.
The record returned from endpoint is formatted as JSON data conforming to the "Ecommerce Standards Document" standards, with the document containing an array of zero or one records. Use the Ecommerce Standards library to easily read through the documents and records, to find data natively using Java classes.
Read [https://www.squizz.com/docs/squizz/Platform-API.html#section1474](https://www.squizz.com/docs/squizz/Platform-API.html#section1474) for more documentation about the endpoint and its requirements.
See the example below on how the call the Retrieve Customer Account Records endpoint. Note that a session must first be created in the API before calling the endpoint.

```java

package org.squizz.api.v1.example;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.squizz.api.v1.endpoint.APIv1EndpointOrgSearchCustomerAccountRecords;
import org.squizz.api.v1.*;
import org.squizz.api.v1.endpoint.*;
import org.esd.EcommerceStandardsDocuments.*;

public class APIv1ExampleRunnerRetrieveCustomerAccountRecord 
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

```

### Send and Procure Purchase Order From Supplier Endpoint

The SQUIZZ.com platform's API has an endpoint that allows an orgnisation to import a purchase order. and have it procured/converted into a sales order of a designated supplier organisation. 
This endpoint allows a customer organisation to commit to buy goods and services of an organisation, and have the order processed, and delivered by the supplier organisation.
The endpoint relies upon a connection first being made between organisations within the SQUIZZ.com platform.
The endpoint relies upon being able to find matching supplier products as what has been ordered.
The endpoint has a number of other requirements. See the endpoint documentation for more details on these requirements.

Each purchase order needs to be imported within a "Ecommerce Standards Document" that contains a record for each purchase order. Use the Ecommerce Standards library to easily create these documents and records.
It is recommended to only import one purchase order at a time, since if an array of purchase orders is imported and one order failed to be procured, then no other orders in the list will be attempted to import.
Read [https://www.squizz.com/docs/squizz/Platform-API.html#section961](https://www.squizz.com/docs/squizz/Platform-API.html#section961) for more documentation about the endpoint and its requirements.
See the example below on how the call the Send and Procure Purchase order From Supplier endpoint. Note that a session must first be created in the API before calling the endpoint.

![alt tag](https://attach.squizz.com/doc_centre/1/files/images/masters/SQUIZZ-Customer-Purchase-Order-Procurement-Supplier[124].png)

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
			
			//add a 2nd purchase order line record that is a text line
            orderProduct = new ESDRecordOrderPurchaseLine();
            orderProduct.lineType = ESDocumentConstants.ORDER_LINE_TYPE_TEXT;
            orderProduct.productCode = "TEA-TOWEL-BLUE";
            orderProduct.textDescription = "Please bundle tea towels into a box";
            orderLines.add(orderProduct);
            
            //add a 3rd purhase order line record
            orderProduct = new ESDRecordOrderPurchaseLine();
            orderProduct.lineType = ESDocumentConstants.ORDER_LINE_TYPE_PRODUCT;
            orderProduct.productCode = "PINKDOU";
            orderProduct.productName = "Blue tea towel - 30 x 6 centimetres";
            orderProduct.quantity = 2;
            orderProduct.salesOrderProductCode = "ACME-SUPPLIER-TTBLUE"; 
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
					//get a list of order lines that could not be mapped
					ArrayList<Pair<Integer, Integer>> unmappedLines = APIv1EndpointOrgProcurePurchaseOrderFromSupplier.getUnmappedOrderLines(esDocumentOrderSale);
					
					//iterate through each unmapped order line
					for(Pair<Integer, Integer> unmappedLine : unmappedLines){
						//get the index of the purchase order and line that contained the unmapped product
						int orderIndex = unmappedLine.getKey();
						int lineIndex = unmappedLine.getValue();
						
						//check that the order can be found that contains the problematic line
						if(orderIndex < orderPurchaseESD.dataRecords.length && lineIndex < orderPurchaseESD.dataRecords[orderIndex].lines.size()){
							System.out.println("For purchase order: "+ orderPurchaseESD.dataRecords[orderIndex].purchaseOrderCode + " a matching supplier product for line number: " + (lineIndex+1) + " could not be found.");
						}
					}
				}
				//if one or more products in the purchase order could not be priced by the supplier organisation then find the order line that caused the problem
				else if(endpointResponseESD.result_code.equals(APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_ORDER_MAPPED_PRODUCT_PRICE_NOT_FOUND) && esDocumentOrderSale != null)
				{
					if(esDocumentOrderSale.configs.containsKey(APIv1EndpointResponseESD.ESD_CONFIG_ORDERS_WITH_UNPRICED_LINES))
					{
						//get a list of order lines that could not be priced
						ArrayList<Pair<Integer, Integer>> unmappedLines = APIv1EndpointOrgProcurePurchaseOrderFromSupplier.getUnpricedOrderLines(esDocumentOrderSale);

						//iterate through each unpriced order line
						for(Pair<Integer, Integer> unmappedLine : unmappedLines){
							//get the index of the purchase order and line that contained the unpriced product
							int orderIndex = unmappedLine.getKey();
							int lineIndex = unmappedLine.getValue();

							//check that the order can be found that contains the problematic line
							if(orderIndex < orderPurchaseESD.dataRecords.length && lineIndex < orderPurchaseESD.dataRecords[orderIndex].lines.size()){
								System.out.println("For purchase order: "+ orderPurchaseESD.dataRecords[orderIndex].purchaseOrderCode + " the supplier has not set pricing for line number: " + (lineIndex+1));
							}
						}
					}
				}
				//if one or more products in the purchase order did not have stock available by the supplier organisation then find the order line that caused the problem
                else if(endpointResponseESD.result_code.equals(APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_ORDER_MAPPED_PRODUCT_STOCK_NOT_AVAILABLE) && esDocumentOrderSale != null)
                {
                    if(esDocumentOrderSale.configs.containsKey(APIv1EndpointResponseESD.ESD_CONFIG_ORDERS_WITH_UNSTOCKED_LINES))
                    {
                        //get a list of order lines that could not be stocked
                        ArrayList<Pair<Integer, Integer>> unstockedLines = APIv1EndpointOrgProcurePurchaseOrderFromSupplier.getUnstockedOrderLines(esDocumentOrderSale);

                        //iterate through each unstocked order line
                        for(Pair<Integer, Integer> unstockedLine : unstockedLines){
                            //get the index of the purchase order and line that contained the unstocked product
                            int orderIndex = unstockedLine.getKey();
                            int lineIndex = unstockedLine.getValue();

                            //check that the order can be found that contains the problematic line
                            if(orderIndex < orderPurchaseESD.dataRecords.length && lineIndex < orderPurchaseESD.dataRecords[orderIndex].lines.size()){
                                System.out.println("For purchase order: "+ orderPurchaseESD.dataRecords[orderIndex].purchaseOrderCode + " the supplier has no stock available for line number: " + (lineIndex+1));
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

### Validate Organisation API Session Endpoint

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

### Validate/Create Organisation API Session Endpoint

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

### Destroy Organisation API Session Endpoint

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
