package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.openqa.selenium.NoSuchElementException;

import testdata.ConfigurationData;

public class PostXML {

	static String projectFolderPath = System.getProperty("user.dir");
	// Get target URL
	// String strURL = "http://172.16.251.29/eGOS/Claims.svc";
	

	public static String posingxml(String XMLFile, String environment) throws HttpException, IOException
	{

		// Get file to be posted
		String responseBody;
		String xmlFolder = projectFolderPath + File.separator + "XML";
		String xmlFile = "XMLGOS1BS_success.xml";
		String strXMLFileName = xmlFolder+"\\"+xmlFile;
		//String strXMLFilename = "C:\\Users\\amitkumarr\\Documents\\XML\\XMLGOS1BS_success.xml";
		File input = new File(strXMLFileName);
		String strURL = ConfigurationData.getRefDataDetails(environment, "WEBSERVICEURL");
		// Prepare HTTP post
		PostMethod post = new PostMethod(strURL);

		// Request content will be retrieved directly from the input stream.Per default, the request content needs to be buffered.
		// in order to determine its length. Request body buffering can be avoided when content length is explicitly specified
		
		post.setRequestEntity(new InputStreamRequestEntity(
				new FileInputStream(input), input.length()));

		// Specify content type and encoding If content encoding is not explicitly specified. ISO-8859-1 is assumed
		post.setRequestHeader(
				"Content-type", "text/xml; charset=ISO-8859-1");

		// Get HTTP client
		HttpClient httpclient = new HttpClient();

		// Execute request
		try {

			int result = httpclient.executeMethod(post);

			// Display status code
			System.out.println("Response status code: " + result);

			// Display response
			System.out.println("Response body: ");
			responseBody = post.getResponseBodyAsString();
			System.out.println(responseBody);

			

		} finally {
			// Release current connection to the connection pool once you are done
			post.releaseConnection();
		}

		return responseBody;

	}
	
	public static String getClaimIDFromResponseBody(String body, String StartwithElement){
		
		String ApplicationNumber = null;
		try {
			
			//String str = SumittedAppConfirmatonParagraph.getText();
			//Pattern p = Pattern.compile(""+StartwithElement+"\\w+");
			/*body = body.replaceAll("[^a-zA-Z0-9]", " ");
			System.out.println("New body: "+body);*/
			/*Pattern p = Pattern.compile("<");
			//Pattern pattern = Pattern.compile("\\w+")
			java.util.regex.Matcher matcher = p.matcher(body);
			while (matcher.find())
				//matcher.find(10)
			{
			    ApplicationNumber = (matcher.group());
			    System.out.println(ApplicationNumber);
			}
			*/
			String[] body1 = body.split("<ns0:");
		//	String body2 = body.split("<ns0:")[1].split("Unique");
			System.out.println(body);
			System.out.println(body1);
			//System.out.println(body2);
			
		}
		
			catch(NoSuchElementException e)
			{
				System.out.println("The SumittedAppButton is not click it  " +e);
			}
			return ApplicationNumber;
		}
	
	
	public static int getResultCodeByPostXML(String FileName, String Key, String environment)
	{
		int result = 0;
			try{
			
				String xmlFolder = projectFolderPath + File.separator + "XML";
				//String xmlFile = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", Key,1);
				String xmlFile = ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", Key,"FILE NAME");
				String strXMLFileName = xmlFolder+"\\"+xmlFile;
				System.out.println(strXMLFileName);
				//String strXMLFilename = "C:\\Users\\amitkumarr\\Documents\\XML\\XMLGOS1BS_success.xml";
				File input = new File(strXMLFileName);
				String strURL = ConfigurationData.getRefDataDetails(environment, "WEBSERVICEURL");
	        // Prepare HTTP post
	        PostMethod post = new PostMethod(strURL);

	        // Request content will be retrieved directly from the input stream Per default, the request content needs to be buffered in order to determine its length. Request body buffering can be avoided when content length is explicitly specified
	        post.setRequestEntity(new InputStreamRequestEntity(
	         new FileInputStream(input), input.length()));

	        // Specify content type and encoding. If content encoding is not explicitly specified ISO-8859-1 is assumed
	        post.setRequestHeader(
	                "Content-type", "text/xml; charset=ISO-8859-1");

	        // Get HTTP client
	        HttpClient httpclient = new HttpClient();

	        // Execute request
	        try {

	            
				
				result = httpclient.executeMethod(post);
				System.out.println("Response status code: " + result);
				String responseCode = String.valueOf(result);
				ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", responseCode, Key, "ResponseCodeFromPost");
	            // Display status code
	           

	            // Display response
	            System.out.println("Response body: ");
	            String responseBody;
				
				responseBody = post.getResponseBodyAsString();				
				System.out.println(responseBody);
				ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", responseBody, Key, "ResponseBodyFromPost");
					
	        }         
	            
	        

	         finally {
	            // Release current connection to the connection pool 
	            // once you are done
	            post.releaseConnection();
	        }
	       
			}
			catch (FileNotFoundException e){
			    // do stuff here..
			   System.out.println("The XML file mentioned is not found.");
			   ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", "XML file is not found.", Key, "REMARKS");
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 return result;
		       
	}
	
}



