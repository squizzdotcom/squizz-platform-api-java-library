/**
* Copyright (C) Squizz PTY LTD
* This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
*/
package org.squizz.api.v1;

import org.squizz.api.v1.endpoint.APIv1EndpointResponse;
import org.esd.EcommerceStandardsDocuments.ESDocumentConstants;
import com.fasterxml.jackson.databind.DeserializationFeature;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import javafx.util.Pair;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.esd.EcommerceStandardsDocuments.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.squizz.api.v1.endpoint.APIv1EndpointResponseESD;

/**
 * A generic class that can be used to send HTTP requests to the platform's API and return HTTP a response as an endpoint object
 */
public class APIv1HTTPRequest
{
    public static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HTTP_HEADER_CONTENT_TYPE_FORM_URL_ENCODED = "application/x-www-form-urlencoded";
    public static final String HTTP_HEADER_CONTENT_TYPE_JSON = "application/json";
    public static final String HTTP_HEADER_CONTENT_ENCODING = "Content-Encoding";
    public static final String HTTP_HEADER_CONTENT_ENCODING_GZIP = "gzip";
    
    /**
     * Sends a HTTP request with a specified URL, headers and optionally post data to the SQUIZZ.com platform's API. Parses JSON data returned into a HTTP response
     * @param <T> The endpoint response class used to de-serialize the JSON response. This class should contain the properties that are expected to be returned from the API's endpoint
     * @param requestMethod method to send the HTTP request with. Set to POST to push up data
     * @param endpointName name of the endpoint in the platform's API to send the request to
     * @param endpointParams list of parameters to append to the end of the request's URL
     * @param requestHeaders list of key value pairs to add to the request's headers
     * @param endpointPostData data to place in the body of the HTTP request and post up
     * @param timeoutMilliseconds amount of milliseconds to wait before giving up waiting on receiving a response from the API. For larger amounts of data posted increase the timeout time
     * @param langBundle language bundle to use to return result messages in
     * @param endpointJSONReader the reader used to deserialize the JSON response from the request. ensure that the reader can deserialize the same generic class set when calling the method
     * @param endpointResponse the response object that may be used to report the response from the server
     * @return a type of endpoint response based on the type of endpoint being called.
     */
    public static <T extends APIv1EndpointResponse> T sendHTTPRequest(String requestMethod, String endpointName, String endpointParams, ArrayList<Pair<String, String>> requestHeaders, String endpointPostData, int timeoutMilliseconds, ResourceBundle langBundle, ObjectReader endpointJSONReader, T endpointResponse)
    {
        //make a request to login to the API with the credentials
        HttpURLConnection webConnection = null;
        OutputStreamWriter httpRequestContentWriter = null;
        BufferedReader httpResponseBufferReader = null;
        StringBuilder builder = null;
        int responseCode = 0;
        
        try 
		{
            //create a new HTTP connection
            URL serverAddress = new URL(APIv1Constants.API_PROTOCOL + APIv1Constants.API_DOMAIN + APIv1Constants.API_ORG_PATH + endpointName + "?" + endpointParams);
            webConnection = (HttpURLConnection)serverAddress.openConnection();
            webConnection.setRequestMethod(requestMethod);
            webConnection.setReadTimeout(timeoutMilliseconds);
            
            //add the header properties to the request
            for(int i=0; i < requestHeaders.size(); i++){
                webConnection.setRequestProperty(requestHeaders.get(i).getKey(), requestHeaders.get(i).getValue());
            }
            
            //set the body of the request
            if(requestMethod.equalsIgnoreCase(APIv1Constants.HTTP_REQUEST_METHOD_POST)){
                webConnection.setDoOutput(true);
                webConnection.setRequestProperty("Content-Length", String.valueOf(endpointPostData.length()));
                
                httpRequestContentWriter = new OutputStreamWriter(webConnection.getOutputStream());
                httpRequestContentWriter.write(endpointPostData);
                httpRequestContentWriter.flush();
            }
            
            //send HTTP request
			webConnection.connect();
            
            //get the output of the HTTP response
			responseCode = webConnection.getResponseCode();
			if(responseCode == HttpURLConnection.HTTP_OK)
			{	
				//obtain the encoding returned by the server
				String encoding = webConnection.getContentEncoding();
				
				//The Content-Type can be used later to determine the nature of the content regardless of compression
				String contentType = webConnection.getContentType();
                
                //get the body of the response based from the encoding type
				InputStream resultingInputStream = null;
				if (encoding != null && encoding.equalsIgnoreCase(HTTP_HEADER_CONTENT_ENCODING_GZIP)) {
					resultingInputStream = new GZIPInputStream((InputStream)webConnection.getContent());
				}
				else if (encoding != null && encoding.equalsIgnoreCase("deflate")) {
					resultingInputStream = new InflaterInputStream((InputStream)webConnection.getContent(), new Inflater(true));
				}
				else {
					resultingInputStream = (InputStream)webConnection.getContent();
				}
                
                //build a string from the content of the HTTP response
                httpResponseBufferReader  = new BufferedReader( new InputStreamReader(resultingInputStream));
                builder = new StringBuilder();
                String line = null;
                while ((line = httpResponseBufferReader.readLine()) != null)
                {
                    builder.append(line);
                }
                
                //deserialize HTTP response from JSON into the endpoint response object
                endpointResponse = endpointJSONReader.readValue(builder.toString());
                
                //get the message that corresponds with the result code
                if(langBundle.containsKey(endpointResponse.result_code)){
                    endpointResponse.result_message = langBundle.getString(endpointResponse.result_code);
                }
            }else{
                endpointResponse.result = APIv1EndpointResponse.ENDPOINT_RESULT_FAILURE;
                endpointResponse.result_code = APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_RESPONSE;
                endpointResponse.result_message = langBundle.getString(endpointResponse.result_code) + "\n" + responseCode;
			}
            
        }catch (MalformedURLException e) {
            endpointResponse.result = APIv1EndpointResponse.ENDPOINT_RESULT_FAILURE;
            endpointResponse.result_code = APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_MALFORMED_URL;
			endpointResponse.result_message = langBundle.getString(endpointResponse.result_code) + "\n" + e.getLocalizedMessage();
		}catch (ProtocolException e) {
            endpointResponse.result = APIv1EndpointResponse.ENDPOINT_RESULT_FAILURE;
            endpointResponse.result_code = APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_REQUEST_PROTOCOL;
			endpointResponse.result_message = langBundle.getString(endpointResponse.result_code) + "\n" + e.getLocalizedMessage();
		}catch(ConnectException e){
            endpointResponse.result = APIv1EndpointResponse.ENDPOINT_RESULT_FAILURE;
            endpointResponse.result_code = APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_CONNECTION;
			endpointResponse.result_message = langBundle.getString(endpointResponse.result_code) + "\n" + e.getLocalizedMessage();
		}catch (IOException e) {
            endpointResponse.result = APIv1EndpointResponse.ENDPOINT_RESULT_FAILURE;
            endpointResponse.result_code = APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_IO;
			endpointResponse.result_message = langBundle.getString(endpointResponse.result_code) + "\n" + e.getLocalizedMessage();
		}
		catch (Exception e){
            endpointResponse.result = APIv1EndpointResponse.ENDPOINT_RESULT_FAILURE;
            endpointResponse.result_code = APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_UNKNOWN;
			endpointResponse.result_message = langBundle.getString(endpointResponse.result_code) + "\n" + e.getLocalizedMessage();
		}
		
		finally
		{
			//close the connection, set all objects to null
			if(webConnection != null){
				webConnection.disconnect();
			}
			httpResponseBufferReader = null;
			builder = null;
			httpRequestContentWriter = null;
			webConnection = null;
		}
        
        return endpointResponse;
    }
    
    /**
     * Sends a HTTP request with a specified URL, headers and data of a Ecommerce Standards Document to the SQUIZZ.com platform's API. 
     * Parses JSON data returned from a HTTP response into an Ecommerce Standards Document of a specified type
     * Note that data uploaded is compressed using GZIP
     * @param requestMethod method to send the HTTP request as, either GET or POST
     * @param endpointName name of the endpoint in the platform's API to send the request to
     * @param endpointParams list of parameters to append to the end of the request's URL
     * @param requestHeaders list of key value pairs to add to the request's headers
     * @param postData content to send in the body of the request, this text is ignored if esDocument is not null
     * @param esDocument Ecommerce Standards Document containing the records and data to push up to the platform's API
     * @param timeoutMilliseconds amount of milliseconds to wait before giving up waiting on receiving a response from the API. For larger amounts of data posted increase the timeout time
     * @param langBundle language bundle to use to return result messages in
     * @param endpointJSONReader the reader used to deserialize the JSON response from the request. ensure that the reader can deserialize the same generic class set when calling the method
     * @param endpointResponse the response object that may be used to report the response from the server
     * @return a type of endpoint response based on the type of endpoint being called, with the response containing the ESDocument
     */
    public static APIv1EndpointResponseESD sendESDocumentHTTPRequest(String requestMethod, String endpointName, String endpointParams, ArrayList<Pair<String, String>> requestHeaders, String postData, ESDocument esDocument, int timeoutMilliseconds, ResourceBundle langBundle, ObjectReader endpointJSONReader, APIv1EndpointResponseESD endpointResponse)
    {
        //make a request to login to the API with the credentials
        HttpURLConnection webConnection = null;
        OutputStreamWriter httpRequestContentWriter = null;
        BufferedReader httpResponseBufferReader = null;
        StringBuilder builder = null;
        int responseCode = 0;
        
        try 
		{
            //create a new HTTP connection
            URL serverAddress = new URL(APIv1Constants.API_PROTOCOL + APIv1Constants.API_DOMAIN + APIv1Constants.API_ORG_PATH + endpointName + "?" + endpointParams);
            webConnection = (HttpURLConnection)serverAddress.openConnection();
            webConnection.setRequestMethod(requestMethod);
            webConnection.setReadTimeout(timeoutMilliseconds);
            
            //add the header properties to the request
            for(int i=0; i < requestHeaders.size(); i++){
                webConnection.setRequestProperty(requestHeaders.get(i).getKey(), requestHeaders.get(i).getValue());
            }
            
            //set the body of the request
            if(requestMethod.equalsIgnoreCase(APIv1Constants.HTTP_REQUEST_METHOD_POST)){
                if(esDocument != null)
                {
                    //set the that the data has been compressed within the request
                    webConnection.setRequestProperty(HTTP_HEADER_CONTENT_ENCODING, HTTP_HEADER_CONTENT_ENCODING_GZIP);
                    webConnection.setRequestProperty(HTTP_HEADER_CONTENT_TYPE, HTTP_HEADER_CONTENT_TYPE_JSON);
                    webConnection.setDoOutput(true);
                    
                    //create JSON mapper for serialisation ofESDocument into JSON and compress with GZIP
                    ObjectMapper jsonObjectMapper = new ObjectMapper();

                    //dont serialize properties that contain default values
                    jsonObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
                    
                    //serialise and compress the ESDocument
                    GZIPOutputStream gzipOutputStream = new GZIPOutputStream(webConnection.getOutputStream());
                    jsonObjectMapper.writeValue(gzipOutputStream, esDocument);
                    gzipOutputStream.flush();
                }else{
                    //add post body text to request
                    webConnection.setDoOutput(true);
                    webConnection.setRequestProperty("Content-Length", String.valueOf(postData.length()));
                    httpRequestContentWriter = new OutputStreamWriter(webConnection.getOutputStream());
                    httpRequestContentWriter.write(postData);
                    httpRequestContentWriter.flush();
                }
            }
            
            //send HTTP request
			webConnection.connect();
            
            //get the output of the HTTP response
			responseCode = webConnection.getResponseCode();
			if(responseCode == HttpURLConnection.HTTP_OK)
			{	
				//obtain the encoding returned by the server
				String encoding = webConnection.getContentEncoding();
				
				//The Content-Type can be used later to determine the nature of the content regardless of compression
				String contentType = webConnection.getContentType();
                
                //get the body of the response based from the encoding type
				InputStream resultingInputStream = null;
				if (encoding != null && encoding.equalsIgnoreCase(HTTP_HEADER_CONTENT_ENCODING_GZIP)) {
					resultingInputStream = new GZIPInputStream((InputStream)webConnection.getContent());
				}
				else if (encoding != null && encoding.equalsIgnoreCase("deflate")) {
					resultingInputStream = new InflaterInputStream((InputStream)webConnection.getContent(), new Inflater(true));
				}
				else {
					resultingInputStream = (InputStream)webConnection.getContent();
				}
                
                //build a string from the content of the HTTP response
                httpResponseBufferReader  = new BufferedReader( new InputStreamReader(resultingInputStream));
                builder = new StringBuilder();
                String line = null;
                while ((line = httpResponseBufferReader.readLine()) != null)
                {
                    builder.append(line);
                }
                
                //deserialize HTTP response from JSON into the endpoint response object
                endpointResponse.esDocument = endpointJSONReader.readValue(builder.toString());
                endpointResponse.result = APIv1EndpointResponse.ENDPOINT_RESULT_FAILURE;
                
                //get result status and result code from document
                if(endpointResponse.esDocument != null){
                    //get the result status from the esDocument
                    if(endpointResponse.esDocument.resultStatus == ESDocumentConstants.RESULT_SUCCESS){
                        endpointResponse.result = APIv1EndpointResponse.ENDPOINT_RESULT_SUCCESS;
                    }
                    
                    //get the result code from the ESdocument's configs if possible
                    if(endpointResponse.esDocument.configs != null){
                        endpointResponse.result_code = endpointResponse.esDocument.configs.getOrDefault(APIv1Constants.API_ORG_ENDPOINT_ATTRIBUTE_RESULT_CODE, APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_UNKNOWN);
                    }
                }
                
                //get the message that corresponds with the result code
                if(langBundle.containsKey(endpointResponse.result_code)){
                    endpointResponse.result_message = langBundle.getString(endpointResponse.result_code);
                }
            }else{
                endpointResponse.result = APIv1EndpointResponse.ENDPOINT_RESULT_FAILURE;
                endpointResponse.result_code = APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_RESPONSE;
                endpointResponse.result_message = langBundle.getString(endpointResponse.result_code) + "\n" + responseCode;
			}
            
        }catch (MalformedURLException e) {
            endpointResponse.result = APIv1EndpointResponse.ENDPOINT_RESULT_FAILURE;
            endpointResponse.result_code = APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_MALFORMED_URL;
			endpointResponse.result_message = langBundle.getString(endpointResponse.result_code) + "\n" + e.getLocalizedMessage();
		}catch (ProtocolException e) {
            endpointResponse.result = APIv1EndpointResponse.ENDPOINT_RESULT_FAILURE;
            endpointResponse.result_code = APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_REQUEST_PROTOCOL;
			endpointResponse.result_message = langBundle.getString(endpointResponse.result_code) + "\n" + e.getLocalizedMessage();
		}catch(ConnectException e){
            endpointResponse.result = APIv1EndpointResponse.ENDPOINT_RESULT_FAILURE;
            endpointResponse.result_code = APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_CONNECTION;
			endpointResponse.result_message = langBundle.getString(endpointResponse.result_code) + "\n" + e.getLocalizedMessage();
		}catch (IOException e) {
            endpointResponse.result = APIv1EndpointResponse.ENDPOINT_RESULT_FAILURE;
            endpointResponse.result_code = APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_IO;
			endpointResponse.result_message = langBundle.getString(endpointResponse.result_code) + "\n" + e.getLocalizedMessage();
		}
		catch (Exception e){
            endpointResponse.result = APIv1EndpointResponse.ENDPOINT_RESULT_FAILURE;
            endpointResponse.result_code = APIv1EndpointResponse.ENDPOINT_RESULT_CODE_ERROR_UNKNOWN;
			endpointResponse.result_message = langBundle.getString(endpointResponse.result_code) + "\n" + e.getLocalizedMessage();
		}
		
		finally
		{
			//close the connection, set all objects to null
			if(webConnection != null){
				webConnection.disconnect();
			}
			httpResponseBufferReader = null;
			builder = null;
			httpRequestContentWriter = null;
			webConnection = null;
		}
        
        return endpointResponse;
    }
}
