![alt tag](https://www.squizz.com/ui/resources/images/logos/squizz_logo_mdpi.png)

# SQUIZZ.com Platform API Java Library

The [SQUIZZ.com](https://www.squizz.com) Platform API Java Library can be used by Java applications to access the SQUIZZ.com platform's Application Programming Interface (API), allowing data to be pushed and pulled from the API's endpoints in a clean and elegant way. The kinds of data pushed and pulled from the API using the library can include organisational data such as products, sales orders, purchase orders, customer accounts, supplier accounts, notifications, and other data that the platform supports.

This library removes the need for Java software developers to write boilerplate code for connecting and accessing the platform's API, allowing Java software using the platform's API to be writen faster and simpler. The library provides classes and objects that can be directly referenced within a Java application, making it easy to manipulate data retreived from the platform, or create and send data to platform.

If you are a software developer writing a Java application then we recommend that you use this library instead of directly calling the platform's APIs, since it will simplify your development times and allow you to easily incorporate new functionality from the API by simplying updating this library.

- You can find more information about the SQUIZZ.com platform by visiting [https://www.squizz.com/docs/squizz](https://www.squizz.com/docs/squizz)
- To find more information about developing software for the SQUIZZ.com visit [https://www.squizz.com/docs/squizz/Integrate-Software-Into-SQUIZZ.com-Platform.html](https://www.squizz.com/docs/squizz/Integrate-Software-Into-SQUIZZ.com-Platform.html)
- To find more information about the platform's API visit [https://www.squizz.com/docs/squizz/Platform-API.html](https://www.squizz.com/docs/squizz/Platform-API.html)

##Getting Started##

To get started using the library within Java applications, download and reference the JAR library to your application using your most preferred way.
To use the library within a Java class using the following import syntax:

```
import org.squizz.api.v1.*;
```

##Create API Session Endpoint##
To start using the SQUIZZ.com platform's API a session must first be created. A session can only be created after credentials for a specified organisation have been given to the API and have been validated.
Once the session has been created then all other endpoints in the API can be called.

```
import org.squizz.api.v1.*;
import org.squizz.api.v1.endpoint.APIv1EndpointResponse;
import org.esd.EcommerceStandardsDocuments.ESDocumentConstants;

public class ExampleRunner {
	public static void main(String[] args)
    {
		//obtain or load in an organisation's API credentials, in this example from command line arguments
		String orgID = args[0];
		String orgAPIKey = args[1];
		String orgAPIPass = args[2];
		
		//create an API session instance
		APIv1OrgSession apiOrgSession = new APIv1OrgSession(orgID, orgAPIKey, orgAPIPass, 0, APIv1Constants.SUPPORTED_LOCALES_EN_AU);
		
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
    }
}
```