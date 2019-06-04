/**
* Copyright (C) 2019 Squizz PTY LTD
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
 * Shows an example of creating a organisation session with the SQUIZZ.com platform's API, then imports product data into the platform against the specified organisation
 */
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
			
			//create the 4th product record
            productRecord = new ESDRecordProduct();
            productRecord.keyProductID = "TAB-1";
            productRecord.productCode = "TABLE001";
			productRecord.name = "Solid Wooden Redgum Table";
			productRecord.productSearchCode = "Solid-Wood-Redgum-Table-001";
			productRecord.keyTaxcodeID = "GST";
			productRecord.stockQuantity = -20;
			productRecord.stockLowQuantity = -5;
			productRecord.stockNoneQuantity = -50;
			
			//add sell unit to 4th product
			sellUnits.clear();
			sellUnitRecord = new ESDRecordSellUnit();
			sellUnitRecord.keySellUnitID = "2";
			sellUnitRecord.baseQuantity = 1;
			sellUnitRecord.sellUnitLabel = "Each";
			sellUnits.add(sellUnitRecord);
			productRecord.sellUnits = sellUnits.toArray(new ESDRecordSellUnit[0]);
			productRecords.add(productRecord);
            
			//create the 4th product record
            productRecord = new ESDRecordProduct();
            productRecord.keyProductID = "CHAIR-5";
            productRecord.productCode = "CHAIR 5";
			productRecord.name = "Solid Wooden Redgum Chair";
			productRecord.productSearchCode = "Solid-Wood-Redgum-Chair-5";
			productRecord.productClass = "FURNITURE";
			productRecord.keyTaxcodeID = "GST";
			productRecord.stockQuantity = 30;
			productRecord.stockLowQuantity = 50;
			productRecord.stockNoneQuantity = 10;
			
			//add sell units to 4th product
			sellUnits.clear();
			sellUnitRecord = new ESDRecordSellUnit();
			sellUnitRecord.keySellUnitID = "2";
			sellUnitRecord.baseQuantity = 1;
			sellUnitRecord.sellUnitLabel = "Each";
			sellUnits.add(sellUnitRecord);
			
			sellUnitRecord = new ESDRecordSellUnit();
			sellUnitRecord.keySellUnitID = "3";
			sellUnitRecord.baseQuantity = 4;
			sellUnitRecord.sellUnitLabel = "Set Of 4 Chairs";
			sellUnits.add(sellUnitRecord);
			
			productRecord.sellUnits = sellUnits.toArray(new ESDRecordSellUnit[0]);
			productRecords.add(productRecord);
			
			//create the 5th product record
			productRecord = new ESDRecordProduct();
            productRecord.keyProductID = "CAR-TYRE-CHEAP";
            productRecord.productCode = "CARTYRECHEAP1";
			productRecord.name = "Cheap Car Tyre";
			productRecord.productClass = "CAR-PARTS";
			productRecord.keyTaxcodeID = "GST";
			productRecords.add(productRecord);
			
			//create the 6th product record
			productRecord = new ESDRecordProduct();
            productRecord.keyProductID = "CAR-TYRE-LONG-LASTING";
            productRecord.productCode = "CARTYRELONG--1";
			productRecord.name = "Car Type Long Lasting";
			productRecord.productClass = "CAR-PARTS";
			productRecord.keyTaxcodeID = "GST";
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
