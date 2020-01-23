package pageobjects.XMLParseHelpers;
import java.awt.AWTException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.annotation.XmlType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import helpers.CommonFunctions;
import helpers.DatabaseHelper;
import helpers.Support;
import pageobjects.AdvancedFilter;
import pageobjects.AdvancedFindResult;
import pageobjects.CrmHome;
import testdata.ConfigurationData;
import utilities.ExcelUtilities;
import verify.Verify;

public final class XMLhelpers extends Support {
	WebDriver driver;
	static WebDriverWait wait;	
	public boolean Process;
	public List<String> AssetMessage;
	public boolean VerifyStagingError; 
	


	
	
	public XMLhelpers(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}
	public XMLhelpers(boolean Process , List<String> AssetMessage )
	{
		this.Process = Process;
		this.AssetMessage = AssetMessage;

	}

	public XMLhelpers(boolean Process , boolean VerifyStagingError,  List<String> AssetMessage )
	{
		this.Process = Process;
		this.AssetMessage = AssetMessage;
		this.VerifyStagingError = VerifyStagingError;

	}
	
	public static String GetResponsecodeBSXML(String FileName, String Key, String environment) {
		String Response = null;
		try{
			String XMLUniqueMsgID, XMLPCode, UpdatedMsgID, UpdatedPCode, remark, newremark, Status, ExpStatus, ClaimID;
			XMLUniqueMsgID =  XMLPCode =  UpdatedMsgID =  UpdatedPCode =  remark = newremark = Status = ExpStatus =  ClaimID = null;
			Boolean flag = false;	
			System.out.println(Key);
			XMLUniqueMsgID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", Key, "UNIQUE MSG ID");
			System.out.println(XMLUniqueMsgID);
			XMLPCode = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", Key, "P CODE");
		
			for (int i = 0;i< 5;i++)
			{
				UpdatedMsgID = CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLUniqueMsgID , i);
				XMLUniqueMsgID = UpdatedMsgID;
			}
			System.out.println("The Updated  Unique Message id is " + UpdatedMsgID);
			utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", UpdatedMsgID, Key, "UNIQUE MSG ID");
			helpers.CommonFunctions.ReadXML_UpdateCodeValue(FileName, "Claim-Message", "Unique-Message-ID", UpdatedMsgID, Key);
		//	System.out.println("The Pcode before "+XMLPCode);
			if(XMLPCode.length()>0)
			{
				for (int i = 0;i< 5;i++)
				{
					UpdatedPCode = helpers.CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLPCode,i);
					XMLPCode = UpdatedPCode;
				}
				System.out.println("The updated P Code is "+UpdatedPCode );
				utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", UpdatedPCode, Key, "P CODE");
				helpers.CommonFunctions.ReadXML_UpdateCodeValue(FileName, "Claim-Message", "P-Code", UpdatedPCode, Key);
			}
			else
			{
				System.out.println("The PCode is blank");
				remark = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", Key, "REMARKS");
				newremark = remark+",The Pcode is blank";
				utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", newremark, Key, "REMARKS"); 
			}
			String responseCode = helpers.PostXML.getResultCodeByPostXML(FileName,Key,environment);
			System.out.println(responseCode);			
			Response = String.valueOf(responseCode);

		}
		catch(Exception e)
		{
			System.out.println("The Response code is not captured" + e);
		}

		return Response;
	}

		public static String GetResponsecodeXML(String FileName, String Key, String environment) {
		String Response = null;
		try{
			String XMLUniqueMsgID, XMLPCode, UpdatedMsgID, UpdatedPCode, remark, newremark, Status, ExpStatus, ClaimID;
			XMLUniqueMsgID =  XMLPCode =  UpdatedMsgID =  UpdatedPCode =  remark = newremark = Status = ExpStatus =  ClaimID = null;
			Boolean flag = false;	
			System.out.println(Key);
			XMLUniqueMsgID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", Key, "UNIQUE MSG ID");
			System.out.println(XMLUniqueMsgID);
			XMLPCode = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", Key, "P CODE");
			for (int i = 0;i< 5;i++)
			{
				UpdatedMsgID = CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLUniqueMsgID , i);
				XMLUniqueMsgID = UpdatedMsgID;
			}
			System.out.println("The Updated Message ID: "+UpdatedMsgID);
			utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", UpdatedMsgID, Key, "UNIQUE MSG ID");
			helpers.CommonFunctions.ReadXML_UpdateCodeValue(FileName, "Claim-Message", "Unique-Message-ID", UpdatedMsgID, Key);
			String responseCode = helpers.PostXML.getResultCodeByPostXML(FileName,Key, environment);
			System.out.println(responseCode);			
			Response = String.valueOf(responseCode);

		}
		catch(Exception e)
		{
			System.out.println("The Response code is not captured" + e);
		}

		return Response;
	}
	public static String GetResponsecodeXMLGOS6(String FileName, String Key, String environment) {
		String Response = null;
		try{

			String responseCode = helpers.PostXML.getResultCodeByPostXML(FileName,Key, environment);
			System.out.println(responseCode);			
			Response = String.valueOf(responseCode);

		}
		catch(Exception e)
		{
			System.out.println("The Response code is not captured" + e);
		}

		return Response;
	}

/*	public static String GetResponsecodeXML(String Key) {
		String Response = null;
		try{
			String FileName,XMLUniqueMsgID, XMLPCode, UpdatedMsgID, UpdatedPCode, remark, newremark, Status, ExpStatus, ClaimID;
			FileName = XMLUniqueMsgID =  XMLPCode =  UpdatedMsgID =  UpdatedPCode =  remark = newremark = Status = ExpStatus =  ClaimID = null;
			Boolean flag = false;	
			System.out.println(Key);

			FileName = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", Key,1);
			System.out.println(FileName);
			XMLUniqueMsgID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", Key, "UNIQUE MSG ID");
			System.out.println(XMLUniqueMsgID);
			UpdatedMsgID = helpers.CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLUniqueMsgID);		
			System.out.println(UpdatedMsgID);
			utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", UpdatedMsgID, Key, "UNIQUE MSG ID");
			helpers.CommonFunctions.ReadXML_UpdateCodeValue(FileName, "Claim-Message", "Unique-Message-ID", UpdatedMsgID, Key);
			String  responseCode = helpers.PostXML.getResultCodeByPostXML(FileName,Key);
			System.out.println(responseCode);			
			Response = String.valueOf(responseCode);

		}
		catch(Exception e)
		{
			System.out.println("The Response code is not captured" + e);
		}

		return Response;
	}*/



	/*public static String GetResponsecodeXML(String FileName , String Key) {
		String Response = null;
		try{
			String XMLUniqueMsgID, XMLPCode, UpdatedMsgID, UpdatedPCode, remark, newremark, Status, ExpStatus, ClaimID;
			XMLUniqueMsgID =  XMLPCode =  UpdatedMsgID =  UpdatedPCode =  remark = newremark = Status = ExpStatus =  ClaimID = null;
			Boolean flag = false;	
			System.out.println(Key);

			System.out.println(FileName);
			XMLUniqueMsgID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", Key, "UNIQUE MSG ID");
			System.out.println(XMLUniqueMsgID);
			UpdatedMsgID = helpers.CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLUniqueMsgID);		
			System.out.println(UpdatedMsgID);
			utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", UpdatedMsgID, Key, "UNIQUE MSG ID");
			helpers.CommonFunctions.ReadXML_UpdateCodeValue(FileName, "Claim-Message", "Unique-Message-ID", UpdatedMsgID, Key);
			String responseCode = helpers.PostXML.getResultCodeByPostXML(FileName,Key);
			System.out.println(responseCode);			
			Response = String.valueOf(responseCode);

		}
		catch(Exception e)
		{
			System.out.println("The Response code is not captured" + e);
		}

		return Response;
	}*/

	public static String GetResponsecodeXML_GOS3(String FileName , String Key, String environment) {
		String Response = null;
		try{
			String XMLUniqueMsgID, XMLPCode, UpdatedMsgID, UpdatedPCode, remark, newremark, Status, ExpStatus, ClaimID;
			XMLUniqueMsgID =  XMLPCode =  UpdatedMsgID =  UpdatedPCode =  remark = newremark = Status = ExpStatus =  ClaimID = null;
			Boolean flag = false;	
			System.out.println(Key);

			System.out.println(FileName);
			XMLUniqueMsgID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", Key, "UNIQUE MSG ID");
			System.out.println(XMLUniqueMsgID);
			for (int i = 0;i< 5;i++)
			{
				UpdatedMsgID = CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLUniqueMsgID , i);
				XMLUniqueMsgID = UpdatedMsgID;
			}	
			System.out.println(UpdatedMsgID);
			utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", UpdatedMsgID, Key, "UNIQUE MSG ID");
			helpers.CommonFunctions.ReadXML_UpdateCodeValue(FileName, "Claim-Message", "Unique-Message-ID", UpdatedMsgID, Key);
			String responseCode = helpers.PostXML.getResultCodeByPostXML(FileName,Key, environment);
			System.out.println(responseCode);			
			Response = String.valueOf(responseCode);

		}
		catch(Exception e)
		{
			System.out.println("The Response code is not captured" + e);
		}

		return Response;
	}
	public static String GetResponsecodeXML_GOS3(String MainFile, String FileName , String Key, String environment) {
		String Response = null;
		try{
			String XMLUniqueMsgID, XMLPCode, UpdatedMsgID, UpdatedPCode, remark, newremark, Status, ExpStatus, ClaimID;
			XMLUniqueMsgID =  XMLPCode =  UpdatedMsgID =  UpdatedPCode =  remark = newremark = Status = ExpStatus =  ClaimID = null;
			Boolean flag = false;	
			System.out.println(Key);

			System.out.println(FileName);
			XMLUniqueMsgID = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML", Key, "UNIQUE MSG ID");
			System.out.println(XMLUniqueMsgID);
			for (int i = 0;i< 5;i++)
			{
				UpdatedMsgID = CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLUniqueMsgID , i);
				XMLUniqueMsgID = UpdatedMsgID;
			}	
			System.out.println(UpdatedMsgID);
			utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", UpdatedMsgID, Key, "UNIQUE MSG ID");
			helpers.CommonFunctions.ReadXML_UpdateCodeValue(FileName, "Claim-Message", "Unique-Message-ID", UpdatedMsgID, Key);
			String responseCode = helpers.PostXML.getResultCodeByPostXML(MainFile, FileName,Key, environment);
			System.out.println(responseCode);			
			Response = String.valueOf(responseCode);

		}
		catch(Exception e)
		{
			System.out.println("The Response code is not captured" + e);
		}

		return Response;
	}


	public static String  getStagingStatus(String Key, String environment) 
	{
		String  Status =  null;
		try
		{

			Boolean flag = false;	
			String schemaName = "StagingDB";
			String dbEnvironment = "Staging";

			// Get Claim Status from Staging Table
			Status = helpers.DatabaseHelper.getStatusFromClaim(schemaName, dbEnvironment,Key, environment);
			System.out.println(Status);
		}
		catch(Exception e)
		{
			System.out.println("The Staging status is not captured" + e);
		}
		return Status;
	}
	
		public static String  getStagingStatus(String Key, String environment, String ClaimID) 
	{
		String  Status =  null;
		try
		{

			Boolean flag = false;	
			String schemaName = "StagingDB";
			String dbEnvironment = "Staging";

			// Get Claim Status from Staging Table
			Status = helpers.DatabaseHelper.getStatusFromClaim(schemaName, dbEnvironment,Key, environment,ClaimID);
			System.out.println(Status);
		}
		catch(Exception e)
		{
			System.out.println("The Staging status is not captured" + e);
		}
		return Status;
	}
	public static String  BULKgetStagingStatus(String Key, String environment, String MainFile) 
	{
		String  Status =  null;
		try
		{

			Boolean flag = false;	
			String schemaName = "StagingDB";
			String dbEnvironment = "Staging";
			Status = helpers.DatabaseHelper.BULKgetStatusFromClaim(schemaName, dbEnvironment,Key, environment, MainFile);
			System.out.println(Status);
		}
		catch(Exception e)
		{
			System.out.println("The Staging status is not captured" + e);
		}
		return Status;
	}
	
	public static String  BULKgetCRMStatus(String Key, String environment, String MainFile, String ClaimID) 
	{
		String  Status =  null;
		try
		{

			Boolean flag = false;	
			String schemaName = "PCSEFAJune_MSCRM";
			String dbEnvironment = "CRMDB";

			// Get Claim Status from Staging Table
			String query = "select oph_claimStatusName from oph_ophthalmicclaim where oph_UniqueClaimIdentifier='"+ClaimID+"'";
			
			Status = helpers.DatabaseHelper.getCRMValueFromQuery(query, "oph_claimStatusName", dbEnvironment, environment);
			System.out.println(Status);
		}
		catch(Exception e)
		{
			System.out.println("The CRM status is not captured" + e);
		}
		return Status;
	}
	
	
	public static String  BULKgetValueFromCRMEntity(String Key, String environment, String MainFile, String entity, String query) 
	{
		String  Value =  null;
		try
		{

			Boolean flag = false;	
			String schemaName = "PCSEFAJune_MSCRM";
			String dbEnvironment = "CRMDB";
			String columnName = null;

			// Get Claim Status from Staging Table
			//String query = "select oph_claimStatusName from oph_ophthalmicclaim where oph_UniqueClaimIdentifier='"+ClaimID+"'";
			switch (entity.toUpperCase())
			{
			case("CRM STATUS"):
				columnName = "oph_claimStatusName";
				
				break;
				
			case("CRM AMOUNT DUE"):
				columnName = "oph_CalculatedClaimValue";
				
				break;
				
			case("CRMPVN AMOUNT DUE"):
				columnName = "oph_CalculatedClaimValue";
				
				break;
			case("EXPECTED ERROR"):
				columnName = "pcs_message";
				break;
				
			case("CRM PAYMENTLINE"):
				columnName = "oph_paymentLineName";
				break;
			case("CLAIM ID"):
				columnName = "oph_UniqueClaimIdentifier";		
				break;
			case("CONTRACTORNUMBER"):
				columnName = "oph_ContractorNumber";		
				break;
			case("PERFORMERNUMBER"):
				columnName = "oph_PerformerNumber";		
				break;
			case("POINTOFSERVICE"):
				columnName = "oph_PointofServiceDate";		
				break;
			case("SUBMITTEDCLAIMVALUE"):
				columnName = "oph_SubmittedClaimValue";		
				break;
			case("CLAIMTYPE"):
				columnName = "oph_ClaimTypeName";		
				break;
			case("ICODE"):
				columnName = "oph_ICode";		
				break;
			case("PCODE"):
				columnName = "oph_PCode";	
				break;
			case("CALCULATEDCLAIMVALUE"):
				columnName = "oph_CalculatedClaimValue";	
				break;
			case("ORGANIZATIONCODE"):
				columnName = "oph_OrganisationCode";
				break;
			case("UNIQUEMESSAGEID"):
				columnName = "oph_egosUniqueMessageID";
				break;
			case("CLAIMVERSIONID"):
				columnName = "oph_ClaimFormVersionID";
				break;
			case("POINTOFSERVICEDATE"):
				columnName = "oph_PointofServiceDate";
				break;
			case("GOS3AUTHORISATIONCODE"):
				columnName="oph_gos3AuthorisationCode";
				break;
			case("GOS3VOUCHERCODE"):
				columnName="oph_gos3VoucherCode";
				break;
			case("PRESRIGHTDISTSPH"):
				columnName="oph_PrescriptionRightDistanceSph";
				break;
			case("PRESRIGHTDISTCYL"):
				columnName="oph_prescriptionRightDistanceCyl";
				break;
			case("PRESRIGHTNEARSPH"):
				columnName="oph_prescriptionRightNearSph";
				break;
			case("PRESLEFTDISTSPH"):
				columnName="oph_prescriptionLeftDistanceSph";
				break;
			case("PRESLEFTDISTCYL"):
				columnName="oph_prescriptionLeftDistanceCyl";
				break;
			case("PRESLEFTNEARSPH"):
				columnName="oph_prescriptionLeftNearSph";
				break;
			case("OPHCLAIMID"):
				columnName="oph_ophthalmicclaimid";
				break;
		/*	case("OPH SERVICE RATE"):
				columnName = "oph_ophthalmicServicerate";
				break;*/
			 default:
				System.out.println("The entity is not present");
				break;
				
			}
			
			if(columnName!=null && !columnName.isEmpty())
			{
			Value = helpers.DatabaseHelper.getCRMValueFromQuery(query, columnName, dbEnvironment, environment);
			}
			System.out.println("The entity value is: "+Value);
		}
		catch(Exception e)
		{
			System.out.println("The expected entity is not captured." + e);
		}
		return Value;
	}
	
	
	public static String  getPVNStatus(String Key, String pvnReference, String environment) 
	{
		String  Status =  null;
		try
		{

			Boolean flag = false;	
			String schemaName = "StagingDB";
			String dbEnvironment = "Staging";

			// Get Claim Status from Staging Table
			Status = helpers.DatabaseHelper.getPVNStatusFromClaim(schemaName, dbEnvironment,Key,pvnReference,environment);
			System.out.println(Status);
		}
		catch(Exception e)
		{
			System.out.println("The Staging status is not captured" + e);
		}
		return Status;
	}
	
	public static String  getPVNStatus(String MainFile, String Key, String pvnReference, String environment) 
	{
		String  Status =  null;
		try
		{

			Boolean flag = false;	
			String schemaName = "StagingDB";
			String dbEnvironment = "Staging";

			// Get Claim Status from Staging Table
			Status = helpers.DatabaseHelper.getPVNStatusFromClaim(MainFile, schemaName, dbEnvironment,Key,pvnReference,environment);
			System.out.println(Status);
		}
		catch(Exception e)
		{
			System.out.println("The Staging status is not captured" + e);
		}
		return Status;
	}

	public static void  StagingProcess(String Status , String Key, String environment) 
	{
		String remark,newremark,  ExpStatus, ClaimID;
		remark = newremark =  ExpStatus =  ClaimID = null;
		String schemaName = "StagingDB";
		String dbEnvironment = "Staging";
		Boolean flag = false;
		try{

			utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML",Status, Key, "Actual CLAIM STATUS IN STG");
			ExpStatus = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",Key, "Expected CLAIM STATUS IN STG");

			if((Status != null)){

				if (ExpStatus.equalsIgnoreCase(Status))
				{
					System.out.println("The expected & actual Staging Status are matching.");
					utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML","PASS", Key, "STGCLMSTATUSVERIFICATION");
				}
				else
				{
					utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML","FAIL", Key, "STGCLMSTATUSVERIFICATION");
				}

				if((Status != null))
				{
					if(Status.equalsIgnoreCase("Rejected"))
					{

						List<String> Errors = helpers.DatabaseHelper.getErrorsFromClaim(schemaName, dbEnvironment,Key,environment);
						String ActualErrorString = Errors.toString();
						List<String> ActualErrors = helpers.CommonFunctions.getactualerrors(ActualErrorString);
						String ActualError = ActualErrors.toString();

						String listString = String.join(", ", Errors);
						System.out.println(Errors);
						ActualError = ActualError.replaceAll("[\\[\\](){}]","");
						System.out.println("Actual Errors: "+ActualError);
						utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", ActualError, Key, "Actual Error Details IN STG");

						String ExpErrorMsg = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", Key, "Expected Error Details IN STG");
						System.out.println("Exp Errors: "+ExpErrorMsg);
						System.out.println("Exp Error Length: "+ExpErrorMsg.length());


						List<String> ExpectedErrors = helpers.CommonFunctions.getactualerrors(ExpErrorMsg);

						if (ExpectedErrors == null && ActualErrors == null)
						{
							flag = true;
						}

						if (!flag)
						{


							int Count =  helpers.CommonFunctions.VerifyErrorMessage(ExpectedErrors,ActualErrors,Key);
							if (Count == 0)
							{
								System.out.println("The errors are matching with expected errors.");
								utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", "PASS", Key, "STGVALIDATIONERRVERIFICATION");

							}

							else
							{
								System.out.println("Actual error list is not matching with expected list.");
								remark = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", Key, "REMARKS");
								newremark = remark+",Actual validation error list is not matching with expected list.";
								utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", newremark, Key, "REMARKS"); 
								utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", "FAIL", Key, "STGVALIDATIONERRVERIFICATION");

							}

							if (ExpectedErrors.size()!=ActualErrors.size())
							{
								System.out.println("Size of Actual error list is not matching with expected list size.");
								remark = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", Key, "REMARKS");
								newremark = remark+",Size of Actual error list is not matching with expected list size.";
								utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", newremark, Key, "REMARKS"); 
							}

						}
					}

				}
				else
				{
					System.out.println("The Claims status found null");
					remark = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", Key, "REMARKS");
					newremark = remark+",The Claims status found null";
					utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", newremark, Key, "REMARKS"); 
					ClaimID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", Key, "CLAIM ID");

					if (Status==null && ClaimID.equalsIgnoreCase("") && ExpStatus.equalsIgnoreCase("Rejected"))
					{
						System.out.println("Status & Claim ID is found null but expected status is rejected. This is case of rejection due to invalid schema.");
						utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML","PASS", Key, "STGCLMSTATUSVERIFICATION");
					}

				}
			}

			else 
			{
				System.out.println(" The response code is null");
				remark = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", Key, "REMARKS");
				newremark = remark+",The Response code is Null";
				utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", newremark, Key, "REMARKS"); 
			}

			System.out.println("The Process has been completed");

		}
		catch(Exception e)
		{
			System.out.println("The staging Process is not completed " + e);
		}
	}

	public static int StagingStatus_Rejected(String Key, String environment) {
		String remark,newremark,  ExpStatus, ClaimID;
		remark = newremark =  ExpStatus =  ClaimID = null;
		String schemaName = "StagingDB";
		String dbEnvironment = "Staging";
		int Count = 0;
		boolean flag = false;
		try{

			List<String> Errors = helpers.DatabaseHelper.getErrorsFromClaim(schemaName, dbEnvironment,Key,environment);
			String ActualErrorString = Errors.toString();
			List<String> ActualErrors = helpers.CommonFunctions.getactualerrors(ActualErrorString);
			String ActualError = ActualErrors.toString();

			String listString = String.join(", ", Errors);
			System.out.println(Errors);
			ActualError = ActualError.replaceAll("[\\[\\](){}]","");
			System.out.println("Actual Errors: "+ActualError);
			utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", ActualError, Key, "Actual Error Details IN STG");

			String ExpErrorMsg = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", Key, "Expected Error Details IN STG");
			System.out.println("Exp Errors: "+ExpErrorMsg);
			System.out.println("Exp Error Length: "+ExpErrorMsg.length());


			List<String> ExpectedErrors = helpers.CommonFunctions.getactualerrors(ExpErrorMsg);

			if (ExpectedErrors == null && ActualErrors == null)
			{
				flag = true;
			}

			if (!flag)
			{
				Count =  helpers.CommonFunctions.VerifyErrorMessage(ExpectedErrors,ActualErrors,Key);	

			}
		}
		catch(Exception e)
		{
			System.out.println("The Error message is not captured" + e);

		}

		return Count;

	}
	
	public static int StagingStatus_Rejected_PMS(String Key, String environment) {
		String remark,newremark,  ExpStatus, ClaimID;
		remark = newremark =  ExpStatus =  ClaimID = null;
		String schemaName = "StagingDB";
		String dbEnvironment = "Staging";
		int Count = 0;
		boolean flag = false;
		try{

			List<String> Errors = helpers.DatabaseHelper.getErrorsFromClaim_PMS(schemaName, dbEnvironment,Key, environment);
			String ActualErrorString = Errors.toString();
			List<String> ActualErrors = helpers.CommonFunctions.getactualerrors(ActualErrorString);
			String ActualError = ActualErrors.toString();

			String listString = String.join(", ", Errors);
			System.out.println(Errors);
			ActualError = ActualError.replaceAll("[\\[\\](){}]","");
			System.out.println("Actual Errors: "+ActualError);
			utilities.ExcelUtilities.setKeyValueByPosition("BULKXMLFILEDATA.xlsx", "XML", ActualError, Key, "Actual Error Details IN STG");

			String ExpErrorMsg = utilities.ExcelUtilities.getKeyValueByPosition("BULKXMLFILEDATA.xlsx", "XML", Key, "Expected Error Details IN STG");
			System.out.println("Exp Errors: "+ExpErrorMsg);
			System.out.println("Exp Error Length: "+ExpErrorMsg.length());


			List<String> ExpectedErrors = helpers.CommonFunctions.getactualerrors(ExpErrorMsg);

			if (ExpectedErrors == null && ActualErrors == null)
			{
				flag = true;
			}

			if (!flag)
			{
				Count =  helpers.CommonFunctions.VerifyErrorMessage(ExpectedErrors,ActualErrors,Key);	

			}
		}
		catch(Exception e)
		{
			System.out.println("The Error message is not captured" + e);

		}

		return Count;

	}
	
	public static int StagingStatus_Rejected(String Key, String ClaimID, String environment) {
		String remark,newremark,  ExpStatus;
		remark = newremark =  ExpStatus =  null;
		String schemaName = "StagingDB";
		String dbEnvironment = "Staging";
		int Count = 0;
		boolean flag = false;
		try{

			List<String> Errors = helpers.DatabaseHelper.getErrorsFromClaim(schemaName, dbEnvironment,Key,ClaimID, environment);
			String ActualErrorString = Errors.toString();
			List<String> ActualErrors = helpers.CommonFunctions.getactualerrors(ActualErrorString);
			String ActualError = ActualErrors.toString();

			String listString = String.join(", ", Errors);
			System.out.println(Errors);
			ActualError = ActualError.replaceAll("[\\[\\](){}]","");
			System.out.println("Actual Errors: "+ActualError);
			utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", ActualError, Key, "Actual Error Details IN STG");

			String ExpErrorMsg = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", Key, "Expected Error Details IN STG");
			System.out.println("Exp Errors: "+ExpErrorMsg);
			System.out.println("Exp Error Length: "+ExpErrorMsg.length());


			List<String> ExpectedErrors = helpers.CommonFunctions.getactualerrors(ExpErrorMsg);

			if (ExpectedErrors == null && ActualErrors == null)
			{
				flag = true;
			}

			if (!flag)
			{
				Count =  helpers.CommonFunctions.VerifyErrorMessage(ExpectedErrors,ActualErrors,Key);	

			}
		}
		catch(Exception e)
		{
			System.out.println("The Error message is not captured" + e);

		}

		return Count;

	}

	public static int PVNStatus_Rejected(String Key, String environment) {
		String remark,newremark,  ExpStatus, ClaimID;
		remark = newremark =  ExpStatus =  ClaimID = null;
		String schemaName = "StagingDB";
		String dbEnvironment = "Staging";
		int Count = 0;
		boolean flag = false;
		try{

			List<String> Errors = helpers.DatabaseHelper.getErrorsFrompvnStatus(schemaName, dbEnvironment,Key, environment);
			String ActualErrorString = Errors.toString();
			List<String> ActualErrors = helpers.CommonFunctions.getactualerrors(ActualErrorString);
			String ActualError = ActualErrors.toString();

			String listString = String.join(", ", Errors);
			System.out.println(Errors);
			ActualError = ActualError.replaceAll("[\\[\\](){}]","");
			System.out.println("Actual Errors: "+ActualError);
			utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", ActualError, Key, "Actual Error Details IN STG");

			String ExpErrorMsg = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", Key, "Expected Error Details IN STG");
			System.out.println("Exp Errors: "+ExpErrorMsg);
			System.out.println("Exp Error Length: "+ExpErrorMsg.length());


			List<String> ExpectedErrors = helpers.CommonFunctions.getactualerrors(ExpErrorMsg);

			if (ExpectedErrors == null && ActualErrors == null)
			{
				flag = true;
			}

			if (!flag)
			{
				Count =  helpers.CommonFunctions.VerifyErrorMessage(ExpectedErrors,ActualErrors,Key);	

			}
		}
		catch(Exception e)
		{
			System.out.println("The Error message is not captured" + e);

		}

		return Count;

	}
	
	public static int PVNStatus_Rejected(String Key, String environment, String MainFile) {
		String remark,newremark,  ExpStatus, ClaimID;
		remark = newremark =  ExpStatus =  ClaimID = null;
		String schemaName = "StagingDB";
		String dbEnvironment = "Staging";
		int Count = 0;
		boolean flag = false;
		try{

			List<String> Errors = helpers.DatabaseHelper.getErrorsFrompvnStatus(schemaName, dbEnvironment,Key, environment);
			String ActualErrorString = Errors.toString();
			List<String> ActualErrors = helpers.CommonFunctions.getactualerrors(ActualErrorString);
			String ActualError = ActualErrors.toString();

			String listString = String.join(", ", Errors);
			System.out.println(Errors);
			ActualError = ActualError.replaceAll("[\\[\\](){}]","");
			System.out.println("Actual Errors: "+ActualError);
			utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", ActualError, Key, "Actual Error Details IN STG");

			String ExpErrorMsg = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML", Key, "Expected Error Details IN STG");
			System.out.println("Exp Errors: "+ExpErrorMsg);
			System.out.println("Exp Error Length: "+ExpErrorMsg.length());


			List<String> ExpectedErrors = helpers.CommonFunctions.getactualerrors(ExpErrorMsg);

			if (ExpectedErrors == null && ActualErrors == null)
			{
				flag = true;
			}

			if (!flag)
			{
				Count =  helpers.CommonFunctions.VerifyErrorMessage(ExpectedErrors,ActualErrors,Key);	

			}
		}
		catch(Exception e)
		{
			System.out.println("The Error message is not captured" + e);

		}

		return Count;

	}

	public static String GetClaimsCRM_BS(WebDriver driver,String Key) {
		String ClaimstatusCRM = "";
		try{
			CrmHome ObjCrmHome  = new CrmHome(driver);
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();

			String ClaimID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",Key,"CLAIM ID");
			String ClaimIDOnStge = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",Key,"Expected CLAIM STATUS IN STG");
			String ClaimStatus = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",Key,"Expected CLAIM STATUS IN CRM");
			String primaryEntity = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "AdvancedSearch", "selectPrimaryEntity","ClaimID");
			String GroupTypeValue = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "AdvancedSearch", "selectoptGroupType","ClaimID");
			String FieldValue = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "AdvancedSearch", "selectField","ClaimID");
			String ConditionValue = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "AdvancedSearch", "selectfilterCondition","ClaimID");
			/*	String ClaimID = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", Key,1);
			System.out.println(ClaimID);*/

			if(!(ClaimID.length()==0))
			{

				AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();		
				AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ClaimID);
				boolean flag = ObjAdvancedFindResult.resultRecordFound();
				if (flag)
				{
					String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
					if(title!= null)
					{
						System.out.println(ClaimID);
						ClaimstatusCRM = ObjAdvancedFindResult.getClaimCRM();
					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("The Claims status is not captured" +e );
		}
		return ClaimstatusCRM;
	}
	
	public static String GetPVNStatusCRM_BS(WebDriver driver,String Key, String pvnReference) {
		String ClaimstatusCRM = "";
		try{
			CrmHome ObjCrmHome  = new CrmHome(driver);
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();

			String ClaimID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",Key,"CLAIM ID");
			//String ClaimIDOnStge = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",Key,"Expected CLAIM STATUS IN STG");
			//String ClaimStatus = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",Key,"Expected CLAIM STATUS IN CRM");
			String primaryEntity = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "AdvancedSearch", "selectPrimaryEntity","PVNID");
			String GroupTypeValue = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "AdvancedSearch", "selectoptGroupType","PVNID");
			String FieldValue = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "AdvancedSearch", "selectField","PVNID");
			String ConditionValue = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "AdvancedSearch", "selectfilterCondition","PVNID");
			/*	String ClaimID = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", Key,1);
			System.out.println(ClaimID);*/

			if(!(ClaimID.length()==0))
			{

				AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();		
				AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, pvnReference);
				boolean flag = ObjAdvancedFindResult.resultRecordFound();
				if (flag)
				{
					String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
					if(title!= null)
					{
						System.out.println(ClaimID);
						ClaimstatusCRM = ObjAdvancedFindResult.getPVNStatusCRM();
					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("The Claims status is not captured" +e );
		}
		return ClaimstatusCRM;
	}


	public static void VerifyCRM_BS(WebDriver driver,String Key) {
		try{
			CrmHome ObjCrmHome  = new CrmHome(driver);
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();

			String ClaimID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",Key,"CLAIM ID");
			String ClaimIDOnStge = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",Key,"Expected CLAIM STATUS IN STG");
			String ClaimStatus = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",Key,"Expected CLAIM STATUS IN CRM");
			String primaryEntity = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "AdvancedSearch", "selectPrimaryEntity","ClaimID");
			String GroupTypeValue = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "AdvancedSearch", "selectoptGroupType","ClaimID");
			String FieldValue = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "AdvancedSearch", "selectField","ClaimID");
			String ConditionValue = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "AdvancedSearch", "selectfilterCondition","ClaimID");
			/*	String ClaimID = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", Key,1);
				System.out.println(ClaimID);*/

			if(!(ClaimID.length()==0))
			{

				AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();		
				AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ClaimID);
				boolean flag = ObjAdvancedFindResult.resultRecordFound();
				if (flag)
				{
					String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
					if(title!= null)
					{
						System.out.println(ClaimID);
						String ClaimstatusCRM = ObjAdvancedFindResult.getClaimCRM();
						boolean ClaimstatusOnCRM = ObjAdvancedFindResult.VerifyClaimsStatus(ClaimID,ClaimStatus);
						if(ClaimstatusOnCRM)
						{						
							System.out.println("The Correct Claims Status is displayed on CRM" +ClaimID );
							utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML","Pass",Key,"CRMCLMSTATUSVERIFICATION");
							//setAssertMessage("The Correct Claims Status is displayed on CRM" +ClaimID, 4);	
						}
						else
						{
							System.out.println("The incorrect Claims Status is displayed on CRM" +ClaimID );
							utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML","Fail",Key,"CRMCLMSTATUSVERIFICATION");
						}
						String ClaimStatusCRM = ObjAdvancedFindResult.ClaimsStatus(ClaimID);
						utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML",ClaimStatusCRM,Key,"Actual CLAIM STATUS IN CRM");
						// Verify Payment Line generated
						boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);

						if(PaymentLine)
						{						
							System.out.println("The Payment Line is generated " +ClaimID );
							//setAssertMessage("The Payment Line is generated" +ClaimID, 4);	
							utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML","Yes",Key,"Payment Line");
							Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
							if(AlertPresent)
							{
								ObjAdvancedFindResult.ClickSpaceBar();
							}
						}
						else
						{
							System.out.println("The Payment Line is not generated " +ClaimID );
							//setAssertMessage("The Payment Line is not generated" +ClaimID, 4);	
							utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML","No",Key,"Payment Line");
							Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
							if(AlertPresent)
							{
								ObjAdvancedFindResult.ClickSpaceBar();
							}
						}

						// IF Rejected					
						if(ClaimStatusCRM.equalsIgnoreCase("Rejected"))
						{
							boolean flag2 = false;
							String UniquemessageID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",Key,"UNIQUE MSG ID");
							String ExpErrorMessage = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",Key,"Expected Error Details IN CRM");
							String primaryEntity_MesID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "AdvancedSearch", "selectPrimaryEntity","MessageID");
							String GroupTypeValue_MesID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "AdvancedSearch", "selectoptGroupType","MessageID");
							String FieldValue_MesID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "AdvancedSearch", "selectField","MessageID");
							String ConditionValue_MesID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "AdvancedSearch", "selectfilterCondition","MessageID");
							ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
							ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity_MesID, GroupTypeValue_MesID,FieldValue_MesID,ConditionValue_MesID, ClaimID);					
							boolean flag1 = ObjAdvancedFindResult.resultRecordFound();
							int Records = ObjAdvancedFindResult.resultRecordCount();
							if (flag1)
							{
								for(int RowNumber = 0; RowNumber< Records; RowNumber++)
								{					
									String title1 = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(RowNumber,2);
									if(title1!= null)
									{
										System.out.println(UniquemessageID);
										ObjAdvancedFindResult = ObjAdvancedFindResult.AddErrorMessage(UniquemessageID,ExpErrorMessage,Key, RowNumber);
									}	

								}
								Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
								if(AlertPresent)
								{
									ObjAdvancedFindResult.ClickSpaceBar();
								}
							}

							else
							{				
								Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
								if(AlertPresent)
								{
									ObjAdvancedFindResult.ClickSpaceBar();
								}
							}

							String ExpErrorMsg = ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", Key, "Expected Error Details IN CRM");
							System.out.println("Exp Errors: "+ExpErrorMsg);
							System.out.println("Exp Error Length: "+ExpErrorMsg.length());
							String ActErrorMsg = ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", Key, "Actual Error Details IN CRM");

							List<String> ExpectedErrors = helpers.CommonFunctions.getactualerrors(ExpErrorMsg);
							List<String> ActualErrors = helpers.CommonFunctions.getactualerrors(ActErrorMsg);
							if (ExpectedErrors == null && ActualErrors == null)
							{
								flag2 = true;
							}

							if (!flag2)
							{


								int Count =  helpers.CommonFunctions.VerifyErrorMessage(ExpectedErrors,ActualErrors, Key);
								if (Count == 0)
								{
									System.out.println("The errors are matching with expected errors.");
									ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", "PASS", Key, "CRMVALIDATIONERRVERIFICATION");

								}

								else
								{
									//Assert.assertTrue(unmatchedList.isEmpty(), "The Actual error list on Patient Details is not matching with Expected Error list.");
									System.out.println("Actual error list is not matching with expected list.");
									String remark = ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", Key, "REMARKS");
									String newremark = remark+",Actual validation error list is not matching with expected list.";
									ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", newremark, Key, "REMARKS"); 
									ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", "FAIL", Key, "CRMVALIDATIONERRVERIFICATION");

								}

								if (ExpectedErrors.size()!=ActualErrors.size())
								{
									System.out.println("Size of Actual error list is not matching with expected list size.");
									String remark = ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", Key, "REMARKS");
									String newremark = remark+",Size of Actual error list is not matching with expected list size.";
									ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", newremark, Key, "REMARKS"); 
								}

							}




						}
					}

				}
				else
				{				
					String remark = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", Key, "REMARKS");
					String newremark = remark+",No Claim ID is found in CRM.";
					ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", newremark, Key, "REMARKS");
					Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
					if(AlertPresent)
					{
						ObjAdvancedFindResult.ClickSpaceBar();
					}
				}





			}
			else
			{
				String remark = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", Key, "REMARKS");
				String newremark = remark+",No Claim ID is found in Excel.";
				ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", newremark, Key, "REMARKS");

			}




			System.out.println("The CRM Claims verification Process has been completed");
			//	tearDown(browser);



		}
		catch(Exception e)
		{
			System.out.println("The Verification of CRM is done");
		}
	}


	public static XMLhelpers XMLProcess_GOS156(String XMLTYPE,String key , boolean evidence, String environment) throws IOException, InterruptedException, ParseException
	{
		boolean StagingProcess = false;
		String XMLValue = null;
		String XMLUniqueMsgID, XMLPCode, UpdatedMsgID, UpdatedPCode, remark, newremark,  ExpStatus;
		XMLUniqueMsgID =  XMLPCode =  UpdatedMsgID =  UpdatedPCode =  remark = newremark =  ExpStatus =   null;
		List<String> AssertMessage = new ArrayList<String>();
		
		XMLhelpers objXMLhelpersStg = new XMLhelpers(StagingProcess, AssertMessage);

		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		XMLUniqueMsgID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", key, "UNIQUE MSG ID");
		System.out.println(XMLUniqueMsgID);
		for (int i = 0;i< 5;i++)
		{
			UpdatedMsgID = CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLUniqueMsgID , i);
			XMLUniqueMsgID = UpdatedMsgID;
		}
		System.out.println("The Updated Unique Message id " + UpdatedMsgID);
		utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", UpdatedMsgID, key, "UNIQUE MSG ID");
		helpers.CommonFunctions.ReadXML_UpdateCodeValue(FileNames, "Claim-Message", "Unique-Message-ID", UpdatedMsgID, key);
		List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "DuplicateAttribute", 1);
		for(String AttributeName : AttributeNames )
		{
			if(!AttributeName.equalsIgnoreCase("Attribute"))
			{
			XMLValue = ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeName, key);
			if(XMLValue != null && !XMLValue.isEmpty())
				{
					helpers.CommonFunctions.ReadXML_UpdateCodeValue_Attribute(XMLTYPE,FileNames,"DuplicateAttribute", AttributeName,key);
				}
			}
		}
		System.out.println(FileNames);
		//String Responsecode = XMLhelpers.GetResponsecodeXML(FileNames,key, environment);
		String Responsecode = helpers.PostXML.getResultCodeByPostXML(FileNames,key,environment);
		
		objXMLhelpersStg = XMLProcess_StagingStatusVerification_PMS(Responsecode, key, environment);
		StagingProcess = objXMLhelpersStg.Process;
		for (String msg: objXMLhelpersStg.AssetMessage)
		{
			AssertMessage.add(msg);
		}
		
	/*	if(Responsecode != null)
		{
			System.out.println("The Response code are " + Responsecode);
			AssertMessage.add("The Response code are " + Responsecode);
			helpers.CommonFunctions.haltExecution_XML();
			String StagingStatus = XMLhelpers.getStagingStatus(key);
			if(StagingStatus != null)
			{
				System.out.println("The Staging status is  " + StagingStatus);
				AssertMessage.add("The Staging status is  " + StagingStatus);
				String ExpStatus = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",key, "Expected CLAIM STATUS IN STG");
				if(StagingStatus.equalsIgnoreCase(ExpStatus))
				{
					System.out.println("The Correct staging status is displayed " + StagingStatus);
					AssertMessage.add("The Correct staging status is displayed " + StagingStatus);
					if(StagingStatus.equalsIgnoreCase("Rejected"))
					{
						int count = XMLhelpers.StagingStatus_Rejected_PMS(key);
						if (count == 0)
						{
							StagingProcess = true;
							System.out.println("The errors are matching with expected errors.");
							AssertMessage.add("The errors are matching with expected errors.");

						}
						else 
						{
							AssertMessage.add("The errors are not matching with expected errors - Staging Process.");
						}
						
						Verify.verifyTrue(count == 0, "The errors are not matching with expected errors"+StagingStatus);
					}
					if(StagingStatus.equalsIgnoreCase("Accepted"))
					{
						StagingProcess = true;
						System.out.println("The Staging status is  " + StagingStatus);
						AssertMessage.add("The Staging status is  " + StagingStatus);
					}
					System.out.println("The Staging Process is Completed  " + StagingStatus);
					AssertMessage.add("The Staging Process is Completed  " + StagingStatus);
					//setAssertMessage("The Staging Process is Completed " + StagingStatus, 5);
				}
			}
			else 
			{
				System.out.println("The Response code are null   " + Responsecode);
				AssertMessage.add("The Response code are null " + Responsecode);
			}
		}*/
		return new XMLhelpers(StagingProcess, AssertMessage);
	}
	
	

	public static XMLhelpers BSXMLProcess_GOS156(String XMLTYPE,String key , boolean evidence, String environment) throws IOException, InterruptedException, ParseException
	{
		boolean StagingProcess = false;
		String XMLValue = null;
		String XMLUniqueMsgID, XMLPCode, UpdatedMsgID, UpdatedPCode, remark, newremark,  ExpStatus;
		XMLUniqueMsgID =  XMLPCode =  UpdatedMsgID =  UpdatedPCode =  remark = newremark =  ExpStatus =   null;
		List<String> AssertMessage = new ArrayList<String>();
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		XMLUniqueMsgID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", key, "UNIQUE MSG ID");
		System.out.println(XMLUniqueMsgID);
		XMLPCode = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", key, "P CODE");
		for (int i = 0;i< 5;i++)
		{
			UpdatedMsgID = CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLUniqueMsgID , i);
			XMLUniqueMsgID = UpdatedMsgID;
		}
		System.out.println("The Updated Unique Message id " + UpdatedMsgID);
		utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", UpdatedMsgID, key, "UNIQUE MSG ID");
		helpers.CommonFunctions.ReadXML_UpdateCodeValue(FileNames, "Claim-Message", "Unique-Message-ID", UpdatedMsgID, key);
		if(XMLPCode.length()>0)
		{
			for(int i =0;i<5;i++)
			{
				UpdatedPCode = helpers.CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLPCode , i);
				XMLPCode = UpdatedPCode;
			}
			System.out.println("The updated P Code is "+UpdatedPCode );
			utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", UpdatedPCode, key, "P CODE");
			helpers.CommonFunctions.ReadXML_UpdateCodeValue(FileNames, "Claim-Message", "P-Code", UpdatedPCode, key);
		}
		else
		{
			System.out.println("The PCode is blank");
			remark = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", key, "REMARKS");
			newremark = remark+",The Pcode is blank";
			utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", newremark, key, "REMARKS"); 
		}
		List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "DuplicateAttribute", 1);
		for(String AttributeName : AttributeNames )
		{
			if(!AttributeName.equalsIgnoreCase("Attribute"))
			{
			XMLValue = ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeName, key);
			if(XMLValue != null && !XMLValue.isEmpty())
				{
					helpers.CommonFunctions.ReadXML_UpdateCodeValue_Attribute(XMLTYPE,FileNames,"DuplicateAttribute", AttributeName,key);
				}
			}
		}
		System.out.println(FileNames);
		
		//String Responsecode = XMLhelpers.GetResponsecodeBSXML(FileNames,key, environment);
		String Responsecode = helpers.PostXML.getResultCodeByPostXML(FileNames,key,environment);
		if(Responsecode != null)
		{
			System.out.println("The Response code are " + Responsecode);
			AssertMessage.add("The Response code are " + Responsecode);
			helpers.CommonFunctions.haltExecution_XML();
			String StagingStatus = XMLhelpers.getStagingStatus(key, environment);
			utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", StagingStatus, key, "Actual CLAIM STATUS IN STG");
			ExpStatus = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",key, "Expected CLAIM STATUS IN STG");
			if(StagingStatus != null && (StagingStatus.equalsIgnoreCase(ExpStatus)))
			{
				
				System.out.println("The Staging status is  " + StagingStatus);
				AssertMessage.add("The Staging status is  " + StagingStatus);
				
				
				if(StagingStatus.equalsIgnoreCase("Accepted"))
				{
					StagingProcess = true;
					System.out.println("The Staging status is  " + StagingStatus);
					AssertMessage.add("The Staging status is  " + StagingStatus);
				}
				if(StagingStatus.equalsIgnoreCase("Rejected"))
				{
					int count = XMLhelpers.StagingStatus_Rejected(key, environment);
					if (count == 0)
					{
						StagingProcess = true;
						System.out.println("The errors are matching with expected errors.");
						AssertMessage.add("The errors are matching with expected errors.");

					}
					else 
					{
						AssertMessage.add("The errors are not matching with expected errors - Staging Process.");
					}
					Verify.verifyTrue(count == 0, "The errors are not matching with expected errors"+StagingStatus);
				}
				
			/*	if(StagingStatus.equalsIgnoreCase(ExpStatus))
				{
					StagingProcess = true;
					System.out.println("The Correct staging status is displayed " + StagingStatus);
					AssertMessage.add("The Correct staging status is displayed " + StagingStatus);

				}	*/
				System.out.println("The Staging Process is Completed  " + StagingStatus);
				AssertMessage.add("The Staging Process is Completed  " + StagingStatus);
				//setAssertMessage("The Staging Process is Completed " + StagingStatus, 5);

			}
			else 
			{
				System.out.println("The Staging status is either NULL or Expected Status is not matching with Actual Status. Actual Status:  " + StagingStatus + "Expected Status: "+ExpStatus);
				AssertMessage.add("The Staging status is either NULL or Expected Status is not matching with Actual Status.   " + StagingStatus + "Expected Status: "+ExpStatus);
			}
		}
		else 
		{
			System.out.println("The Response code are null   " + Responsecode);
			AssertMessage.add("The Response code are null " + Responsecode);
		}
		
		return new XMLhelpers(StagingProcess,AssertMessage);
	}
	
	public static XMLhelpers BSXMLProcess_GOS156(String MainFile,String XMLTYPE,String key , boolean evidence, String environment) throws IOException, InterruptedException, ParseException
	{
		boolean StagingProcess = false;
		String XMLValue = null;
		String XMLUniqueMsgID, XMLPCode, UpdatedMsgID, UpdatedPCode, remark, newremark,  ExpStatus;
		XMLUniqueMsgID =  XMLPCode =  UpdatedMsgID =  UpdatedPCode =  remark = newremark =  ExpStatus =   null;
		List<String> AssertMessage = new ArrayList<String>();
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		XMLUniqueMsgID = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "UNIQUE MSG ID");
		System.out.println(XMLUniqueMsgID);
		XMLPCode = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "P CODE");
		for (int i = 0;i< 5;i++)
		{
			UpdatedMsgID = CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLUniqueMsgID , i);
			XMLUniqueMsgID = UpdatedMsgID;
		}
		System.out.println("The Updated Unique Message id " + UpdatedMsgID);
		utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", UpdatedMsgID, key, "UNIQUE MSG ID");
		helpers.CommonFunctions.BULKReadXML_UpdateCodeValue(FileNames, "Claim-Message", "Unique-Message-ID", UpdatedMsgID, key, MainFile);
		if(XMLPCode.length()>0)
		{
			for(int i =0;i<5;i++)
			{
				UpdatedPCode = helpers.CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLPCode , i);
				XMLPCode = UpdatedPCode;
			}
			System.out.println("The updated P Code is "+UpdatedPCode );
			utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", UpdatedPCode, key, "P CODE");
			helpers.CommonFunctions.BULKReadXML_UpdateCodeValue(FileNames, "Claim-Message", "P-Code", UpdatedPCode, key, MainFile);
		}
		else
		{
			System.out.println("The PCode is blank");
			remark = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "REMARKS");
			newremark = remark+",The Pcode is blank";
			utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", newremark, key, "REMARKS"); 
		}
		List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel(MainFile, "UpdateTagValues", 1);
		for(String AttributeName : AttributeNames )
		{
			if(!AttributeName.equalsIgnoreCase("Attribute"))
			{
			XMLValue = ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
			if(XMLValue != null && !XMLValue.isEmpty())
				{
					helpers.CommonFunctions.ReadXML_UpdateCodeValue_Attribute(MainFile,XMLTYPE,FileNames,"UpdateTagValues", AttributeName,key);
				}
			}
		}
		System.out.println(FileNames);
		
		//String Responsecode = XMLhelpers.GetResponsecodeBSXML(FileNames,key, environment);
		String Responsecode = helpers.PostXML.getResultCodeByPostXML(FileNames,key,environment);
		if(Responsecode != null)
		{
			System.out.println("The Response code are " + Responsecode);
			AssertMessage.add("The Response code are " + Responsecode);
			helpers.CommonFunctions.haltExecution_XML();
			String StagingStatus = XMLhelpers.getStagingStatusFromDataFile(key, environment,MainFile);
			utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", StagingStatus, key, "Actual CLAIM STATUS IN STG");
			ExpStatus = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML",key, "Expected CLAIM STATUS IN STG");
			if(StagingStatus != null && (StagingStatus.equalsIgnoreCase(ExpStatus)))
			{
				
				System.out.println("The Staging status is  " + StagingStatus);
				AssertMessage.add("The Staging status is  " + StagingStatus);
				
				
				if(StagingStatus.equalsIgnoreCase("Accepted"))
				{
					StagingProcess = true;
					System.out.println("The Staging status is  " + StagingStatus);
					AssertMessage.add("The Staging status is  " + StagingStatus);
				}
				if(StagingStatus.equalsIgnoreCase("Rejected"))
				{
					int count = XMLhelpers.StagingStatus_Rejected(key, environment);
					if (count == 0)
					{
						StagingProcess = true;
						System.out.println("The errors are matching with expected errors.");
						AssertMessage.add("The errors are matching with expected errors.");

					}
					else 
					{
						AssertMessage.add("The errors are not matching with expected errors - Staging Process.");
					}
					Verify.verifyTrue(count == 0, "The errors are not matching with expected errors"+StagingStatus);
				}
				
			/*	if(StagingStatus.equalsIgnoreCase(ExpStatus))
				{
					StagingProcess = true;
					System.out.println("The Correct staging status is displayed " + StagingStatus);
					AssertMessage.add("The Correct staging status is displayed " + StagingStatus);

				}	*/
				System.out.println("The Staging Process is Completed  " + StagingStatus);
				AssertMessage.add("The Staging Process is Completed  " + StagingStatus);
				//setAssertMessage("The Staging Process is Completed " + StagingStatus, 5);

			}
			else 
			{
				System.out.println("The Staging status is either NULL or Expected Status is not matching with Actual Status.   " + StagingStatus);
				AssertMessage.add("The Staging status is either NULL or Expected Status is not matching with Actual Status.   " + StagingStatus);
			}
		}
		else 
		{
			System.out.println("The Response code are null   " + Responsecode);
			AssertMessage.add("The Response code are null " + Responsecode);
		}
		
		return new XMLhelpers(StagingProcess,AssertMessage);
	}
 	
	public static XMLhelpers BSXMLProcess_GOS156_Duplicate(String key , boolean evidence, String environment) throws IOException, InterruptedException
	{
		boolean StagingProcess = false;
		boolean JobRun = false;
		List<String> AssertMessage = new ArrayList<String>();

		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);

		/*List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "UpdatedAttribute", 1);
		for(String AttributeName : AttributeNames )
		{
			helpers.CommonFunctions.ReadXML_UpdateCodeValue_Attribute(FileNames,"UpdatedAttribute", AttributeName,key);
		}*/
		System.out.println(FileNames);
		String Responsecode = XMLhelpers.GetResponsecodeBSXML(FileNames,key,environment);
		if(Responsecode != null)
		{
			System.out.println("The Response code are " + Responsecode);
			AssertMessage.add("The Response code are " + Responsecode);
			helpers.CommonFunctions.haltExecution_XML();
			String StagingStatus = XMLhelpers.getStagingStatus(key, environment);
			utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", StagingStatus, key, "Actual CLAIM STATUS IN STG");
			String ExpStatus = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",key, "Expected CLAIM STATUS IN STG");
			if(StagingStatus != null && (StagingStatus.equalsIgnoreCase(ExpStatus)))
			{
				
				System.out.println("The Staging status is  " + StagingStatus);
				AssertMessage.add("The Staging status is  " + StagingStatus);
				
				
				if(StagingStatus.equalsIgnoreCase("Accepted"))
				{
					StagingProcess = true;
					System.out.println("The Staging status is  " + StagingStatus);
					AssertMessage.add("The Staging status is  " + StagingStatus);
				}
				if(StagingStatus.equalsIgnoreCase("Rejected"))
				{
					int count = XMLhelpers.StagingStatus_Rejected(key, environment);
					if (count == 0)
					{
						StagingProcess = true;
						System.out.println("The errors are matching with expected errors.");
						AssertMessage.add("The errors are matching with expected errors.");

					}
					else 
					{
						AssertMessage.add("The errors are not matching with expected errors - Staging Process.");
					}
					Verify.verifyTrue(count == 0, "The errors are not matching with expected errors"+StagingStatus);
				}
				
			/*	if(StagingStatus.equalsIgnoreCase(ExpStatus))
				{
					StagingProcess = true;
					System.out.println("The Correct staging status is displayed " + StagingStatus);
					AssertMessage.add("The Correct staging status is displayed " + StagingStatus);

				}	*/
				System.out.println("The Staging Process is Completed  " + StagingStatus);
				AssertMessage.add("The Staging Process is Completed  " + StagingStatus);
				//setAssertMessage("The Staging Process is Completed " + StagingStatus, 5);

			}
			else 
			{
				System.out.println("The Staging status is either NULL or Expected Status is not matching with Actual Status.   " + StagingStatus);
				AssertMessage.add("The Staging status is either NULL or Expected Status is not matching with Actual Status.   " + StagingStatus);
			}
		}
		else 
		{
			System.out.println("The Response code are null   " + Responsecode);
			AssertMessage.add("The Response code are null " + Responsecode);
		}
		
		return new XMLhelpers(StagingProcess,AssertMessage);
	}

		public static XMLhelpers BSXMLProcess_GOS6(String XMLTYPE,String key , boolean evidence, String environment) throws IOException, InterruptedException, ParseException
	{
		boolean StagingProcess = false;
		String XMLValue = null;
		String XMLUniqueMsgID, XMLPCode, UpdatedMsgID, UpdatedPCode, remark, newremark,  ExpStatus;
		XMLUniqueMsgID =  XMLPCode =  UpdatedMsgID =  UpdatedPCode =  remark = newremark =  ExpStatus =   null;
		List<String> AssertMessage = new ArrayList<String>();

		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
		XMLUniqueMsgID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", key, "UNIQUE MSG ID");
		System.out.println(XMLUniqueMsgID);
		XMLPCode = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", key, "P CODE");
		for (int i = 0;i< 5;i++)
		{
			UpdatedMsgID = CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLUniqueMsgID , i);
			XMLUniqueMsgID = UpdatedMsgID;
		}
		System.out.println("The Updated Unique Message id " + UpdatedMsgID);
		utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", UpdatedMsgID, key, "UNIQUE MSG ID");
		helpers.CommonFunctions.ReadXML_UpdateCodeValue(FileNames, "Claim-Message", "Unique-Message-ID", UpdatedMsgID, key);
		if(XMLPCode.length()>0)
		{
			for (int i = 0;i< 5;i++)
			{
				UpdatedPCode = helpers.CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLPCode , i);
				XMLPCode = UpdatedPCode;
			}
				System.out.println("The updated P Code is "+UpdatedPCode );
				utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", UpdatedPCode, key, "P CODE");
				helpers.CommonFunctions.ReadXML_UpdateCodeValue(FileNames, "Claim-Message", "P-Code", UpdatedPCode, key);
		}
		else
		{
			System.out.println("The PCode is blank");
			remark = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", key, "REMARKS");
			newremark = remark+",The Pcode is blank";
			utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", newremark, key, "REMARKS"); 
		}

		List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "DuplicateAttribute", 1);
		for(String AttributeName : AttributeNames )
		{
			if(!AttributeName.equalsIgnoreCase("Attribute"))
			{
			XMLValue = ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeName, key);
			if(XMLValue != null && !XMLValue.isEmpty())
				{
					helpers.CommonFunctions.ReadXML_UpdateCodeValue_Attribute(XMLTYPE,FileNames,"DuplicateAttribute", AttributeName,key);
				}
			}
		}
		System.out.println(FileNames);
		String Responsecode = XMLhelpers.GetResponsecodeBSXML(FileNames,key,environment);
		if(Responsecode != null)
		{
			System.out.println("The Response code are " + Responsecode);
			AssertMessage.add("The Response code are " + Responsecode);
			helpers.CommonFunctions.haltExecution_XML();
			String StagingStatus = XMLhelpers.getStagingStatus(key, environment);
			//utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", StagingStatus, key, "Actual CLAIM STATUS IN STG");
			if(StagingStatus != null)
			{
				System.out.println("The Staging status is  " + StagingStatus);
				AssertMessage.add("The Staging status is  " + StagingStatus);
				if(StagingStatus.equalsIgnoreCase("Rejected"))
				{
					int count = XMLhelpers.StagingStatus_Rejected(key,environment);
					if (count == 0)
					{
						StagingProcess = true;
						System.out.println("The errors are matching with expected errors.");
						AssertMessage.add("The errors are matching with expected errors.");

					}
					else 
					{
						AssertMessage.add("The errors are not matching with expected errors - Staging Process.");
					}
					Verify.verifyTrue(count == 0, "The errors are not matching with expected errors"+StagingStatus);
				}

				if(StagingStatus.equalsIgnoreCase("Accepted"))
				{
					System.out.println("The Staging status for Claims 1  " + StagingStatus);
					AssertMessage.add("The Staging status for Claims 1  " + StagingStatus);
					AssertMessage.add("The correct Staging status is apperaed   " + StagingStatus);
					System.out.println(FileNames);
					String ResponsecodeDouble = XMLhelpers.GetResponsecodeXMLGOS6(FileNames,key, environment);
					if(ResponsecodeDouble != null)
					{
						System.out.println("The Response code are " + Responsecode);
						AssertMessage.add("The Response code are " + Responsecode);
						helpers.CommonFunctions.haltExecution_XML();
						String StagingStatusDouble = XMLhelpers.getStagingStatus(key, environment);
						utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", StagingStatusDouble, key, "Actual CLAIM STATUS IN STG");
						if(StagingStatusDouble != null)
						{
							System.out.println("The Staging status for Claims 2  " + StagingStatus);
							AssertMessage.add("The Staging status for Claims 2  " + StagingStatus);
							ExpStatus = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",key, "Expected CLAIM STATUS IN STG");
							if(StagingStatusDouble.equalsIgnoreCase(ExpStatus))
							{
								System.out.println("The Correct staging status is displayed " + StagingStatusDouble);
								AssertMessage.add("The Correct staging status is displayed " + StagingStatusDouble);
								if(StagingStatus.equalsIgnoreCase("Rejected"))
								{
									int count = XMLhelpers.StagingStatus_Rejected(key, environment);
									if (count == 0)
									{
										StagingProcess = true;
										System.out.println("The errors are matching with expected errors.");
										AssertMessage.add("The errors are matching with expected errors.");							
									}
									else 
									{
										AssertMessage.add("The errors are not matching with expected errors - Staging Process.");
									}
									Verify.verifyTrue(count == 0, "The errors are not matching with expected errors"+StagingStatus);
								}
							}
							if(StagingStatusDouble.equalsIgnoreCase("Accepted"))
							{
								StagingProcess = true;
								System.out.println("The Staging status is  " + StagingStatusDouble);
								AssertMessage.add("The Staging status is  " + StagingStatusDouble);
							}
							System.out.println("The Staging Process is Completed  " + StagingStatusDouble);
							AssertMessage.add("The Staging Process is Completed  " + StagingStatusDouble);					
						}
					}
					else 
					{
						System.out.println("The Response code are null after Duplicate response  " + ResponsecodeDouble);
						AssertMessage.add("The Response code are null after Duplicate response  " + ResponsecodeDouble);
					}
				}
			}
			else 
			{
				System.out.println("The Staging Status is NULL   " + StagingStatus);
				AssertMessage.add("The Staging Status is NULL  " + StagingStatus);
			}
		}
		else 
		{
			System.out.println("The Response code are null   " + Responsecode);
			AssertMessage.add("The Response code are null " + Responsecode);
		}
		return new XMLhelpers(StagingProcess,AssertMessage);
	}
	


	public static XMLhelpers BSXMLProcess_GOS6_Duplicate(String XMLTYPE,String FileNames, String key , boolean evidence, String environment) throws IOException, InterruptedException
	{

		boolean StagingProcess = false;
		boolean JobRun = false;
		String XMLValue = null;
		int count = 0;
		List<String> AssertMessage = new ArrayList<String>();

		//String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);

		List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "DuplicateAttribute", 1);
		for(String AttributeName : AttributeNames )
		{
			if(!AttributeName.equalsIgnoreCase("Attribute"))
			{
			XMLValue = ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeName, key);
			if(XMLValue != null && !XMLValue.isEmpty())
				{
					helpers.CommonFunctions.ReadXML_UpdateCodeValue_Attribute(XMLTYPE, FileNames,"DuplicateAttribute", AttributeName,key);
				}
			}
		}
		System.out.println(FileNames);
		String Responsecode = XMLhelpers.GetResponsecodeBSXML(FileNames,key, environment);
		
		if(Responsecode != null)
		{
			System.out.println("The Response code are " + Responsecode);
			AssertMessage.add("The Response code are " + Responsecode);
			String clmID = helpers.CommonFunctions.GetValueFormParagaraph(Responsecode, "Unique-Claim-ID=");
			//String clmID_updated = pvnReference.substring(2);
			System.out.println("Claim ID: "+clmID);
			helpers.CommonFunctions.haltExecution_XML();
			String StagingStatus = XMLhelpers.getStagingStatus(key, environment, clmID);
			//utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", StagingStatus, key, "Actual CLAIM STATUS IN STG");
			if(StagingStatus != null)
			{
				System.out.println("The Staging status is  " + StagingStatus);
				AssertMessage.add("The Staging status is  " + StagingStatus);
				/*if(StagingStatus.equalsIgnoreCase("Rejected"))
				{
					int count = XMLhelpers.StagingStatus_Rejected(key);
					if (count == 0)
					{
						StagingProcess = true;
						System.out.println("The errors are matching with expected errors.");
						AssertMessage.add("The errors are matching with expected errors.");

					}
					else 
					{
						AssertMessage.add("The errors are not matching with expected errors - Staging Process.");
					}
					Verify.verifyTrue(count == 0, "The errors are not matching with expected errors"+StagingStatus);
				}*/

				if(StagingStatus.equalsIgnoreCase("Accepted"))
				{
					System.out.println("The Staging status for Claims 1  " + StagingStatus);
					AssertMessage.add("The Staging status for Claims 1  " + StagingStatus);
					AssertMessage.add("The correct Staging status is apperaed   " + StagingStatus);
					System.out.println(FileNames);
					//helpers.CommonFunctions.haltExecution_XML();
					String ResponsecodeDouble = XMLhelpers.GetResponsecodeXMLGOS6(FileNames,key, environment);
					if(ResponsecodeDouble != null)
					{
						System.out.println("The Response code for second message is " + ResponsecodeDouble);
						AssertMessage.add("The Response code for second message is  " + ResponsecodeDouble);
						String clmID2 = helpers.CommonFunctions.GetValueFormParagaraph(ResponsecodeDouble, "Unique-Claim-ID=");
						//String clmID_updated = pvnReference.substring(2);
						System.out.println("Claim ID: "+clmID2);
						ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", clmID2, key, "CLAIM ID");
						helpers.CommonFunctions.haltExecution_XML();
						String StagingStatusDouble = XMLhelpers.getStagingStatus(key, environment, clmID2);
						utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", StagingStatusDouble, key, "Actual CLAIM STATUS IN STG");
						if(StagingStatusDouble != null)
						{
							System.out.println("The Staging status for Claims 2  " + StagingStatusDouble);
							AssertMessage.add("The Staging status for Claims 2  " + StagingStatusDouble);

							if(StagingStatusDouble.equalsIgnoreCase("Rejected"))
							{
								count = XMLhelpers.StagingStatus_Rejected(key, environment);
							}

							String ExpStatus = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",key, "Expected CLAIM STATUS IN STG");
							if(StagingStatusDouble.equalsIgnoreCase(ExpStatus))
							{
								System.out.println("The Correct staging status is displayed " + StagingStatusDouble);
								AssertMessage.add("The Correct staging status is displayed " + StagingStatusDouble);
								if(StagingStatusDouble.equalsIgnoreCase("Rejected"))
								{
									//int count = XMLhelpers.StagingStatus_Rejected(key);
									if (count == 0)
									{
										StagingProcess = true;
										System.out.println("The errors are matching with expected errors.");
										AssertMessage.add("The errors are matching with expected errors.");							
									}
									else 
									{
										AssertMessage.add("The errors are not matching with expected errors - Staging Process.");
									}
									Verify.verifyTrue(count == 0, "The errors are not matching with expected errors"+StagingStatus);
								}

								if(StagingStatusDouble.equalsIgnoreCase("Accepted"))
								{
									StagingProcess = true;
									System.out.println("The Staging status is  " + StagingStatusDouble);
									AssertMessage.add("The Staging status is  " + StagingStatusDouble);
								}
							}
							else
							{
								System.out.println("The expected Staging Status for duplicate message is not matching. The expected status is " + ExpStatus+". But Actual Status is "+StagingStatusDouble);
								AssertMessage.add("The expected Staging Status for duplicate message is not matching. The expected status is " + ExpStatus+". But Actual Status is "+StagingStatusDouble);
							}

							System.out.println("The Staging Process is Completed  " + StagingStatusDouble);
							AssertMessage.add("The Staging Process is Completed  " + StagingStatusDouble);					
						}
					}
					else 
					{
						System.out.println("The Response code are null after Duplicate response  " + ResponsecodeDouble);
						AssertMessage.add("The Response code are null after Duplicate response  " + ResponsecodeDouble);
					}

				}
				else 
				{
					System.out.println("The status of first message is not accepted  " + StagingStatus);
					AssertMessage.add("The status of first message is not accepted " + StagingStatus);
				}
			}
			else 
			{
				System.out.println("The Response code are null   " + Responsecode);
				AssertMessage.add("The Response code are null " + Responsecode);
			}
		}
		return new XMLhelpers(StagingProcess,AssertMessage);
	}

	public static XMLhelpers BSXMLProcess_Duplicate(String XMLTYPE, String FileNames, String key , boolean evidence ,String XMLType, String environment) throws IOException, InterruptedException
	{

		boolean StagingProcess = false;
		boolean VerifyStagingError = false;
		boolean JobRun = false;
		String XMLValue = null;
		int count = 0;
		List<String> AssertMessage = new ArrayList<String>();

		//String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);

		List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "DuplicateAttribute", 1);
		for(String AttributeName : AttributeNames )
		{
			if(!AttributeName.equalsIgnoreCase("Attribute"))
			{
			XMLValue = ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeName, key);
			if(XMLValue != null && !XMLValue.isEmpty())
				{
					helpers.CommonFunctions.ReadXML_UpdateCodeValue_Attribute(XMLTYPE,FileNames,"DuplicateAttribute", AttributeName,key);
				}
			}
		}
		System.out.println(FileNames);
		String Responsecode = XMLhelpers.GetResponsecodeBSXML(FileNames,key,environment);
		
		if(Responsecode != null)
		{
			System.out.println("The Response code are " + Responsecode);
			AssertMessage.add("The Response code are " + Responsecode);
			String clmID = helpers.CommonFunctions.GetValueFormParagaraph(Responsecode, "Unique-Claim-ID=");
			//String clmID_updated = pvnReference.substring(2);
			System.out.println("Claim ID: "+clmID);
			helpers.CommonFunctions.haltExecution_XML();
			String StagingStatus = XMLhelpers.getStagingStatus(key, environment);
			utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", clmID, key, "CLAIM ID");
			if(StagingStatus != null)
			{
				System.out.println("The Staging status is  " + StagingStatus);
				AssertMessage.add("The Staging status is  " + StagingStatus);
				if(StagingStatus.equalsIgnoreCase("Accepted"))
				{
					StagingProcess = true;
					System.out.println("The Staging status for Claims 1  " + StagingStatus);
					AssertMessage.add("The Staging status for Claims 1  " + StagingStatus);
					AssertMessage.add("The correct Staging status is apperaed   " + StagingStatus);
					System.out.println(FileNames);
				}
				else 
				{
					StagingProcess = false;
					if(StagingStatus.equalsIgnoreCase("Rejected"))
					{
						StagingProcess = true;
						int countError = XMLhelpers.StagingStatus_Rejected(key, environment);
						if (countError == 0)
						{
							VerifyStagingError = true;
							System.out.println("The errors are matching with expected errors.");
							AssertMessage.add("The errors are matching with expected errors.");

						}
						else 
						{
							AssertMessage.add("The errors are not matching with expected errors - Staging Process.");
						}
						Verify.verifyTrue(count == 0, "The errors are not matching with expected errors"+StagingStatus);
					}
				}
			}
			else 
			{
				System.out.println("The Response code are null   " + Responsecode);
				AssertMessage.add("The Response code are null " + Responsecode);
			}
		}
		return new XMLhelpers(StagingProcess,VerifyStagingError, AssertMessage);
	}
	
	public static XMLhelpers XMLProcess_Duplicate(String FileNames, String key , boolean evidence,String XMLType, String environment)  throws IOException, InterruptedException
	{

		boolean StagingProcess = false;
		boolean JobRun = false;
		boolean VerifyStagingError = false;
		int count = 0;
		String XMLValue = null;
		List<String> AssertMessage = new ArrayList<String>();

		//String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);

		List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "DuplicateAttribute", 1);
		for(String AttributeName : AttributeNames )
		{
			if(!AttributeName.equalsIgnoreCase("Attribute"))
			{			
				XMLValue = ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeName, key);
				if(XMLValue != null && !XMLValue.isEmpty())
					{
						helpers.CommonFunctions.ReadXML_UpdateCodeValue_Attribute_Duplicate(FileNames,"DuplicateAttribute", AttributeName,key , XMLType);
					}
			}
		}
		System.out.println(FileNames);
		String Responsecode = XMLhelpers.GetResponsecodeXML(FileNames,key, environment);
		
		if(Responsecode != null)
		{
			System.out.println("The Response code are " + Responsecode);
			AssertMessage.add("The Response code are " + Responsecode);
			String clmID = helpers.CommonFunctions.GetValueFormParagaraph(Responsecode, "Unique-Claim-ID=");
			//String clmID_updated = pvnReference.substring(2);
			System.out.println("Claim ID: "+clmID);
			utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", clmID, key, "CLAIM ID");
			helpers.CommonFunctions.haltExecution_XML();
			String StagingStatus = XMLhelpers.getStagingStatus(key, environment);
			utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", StagingStatus, key, "Actual CLAIM STATUS IN STG");
			if(StagingStatus != null)
			{
				System.out.println("The Staging status is  " + StagingStatus);
				AssertMessage.add("The Staging status is  " + StagingStatus);
				if(StagingStatus.equalsIgnoreCase("Accepted"))
				{
					StagingProcess = true;
					System.out.println("The Staging status for Claims 1  " + StagingStatus);
					AssertMessage.add("The Staging status for Claims 1  " + StagingStatus);
					AssertMessage.add("The correct Staging status is apperaed   " + StagingStatus);
					System.out.println(FileNames);
					//sdf
					String ResponsecodeDouble = XMLhelpers.GetResponsecodeXMLGOS6(FileNames,key, environment);
					if(ResponsecodeDouble != null)
					{
						System.out.println("The Response code for second message is " + ResponsecodeDouble);
						AssertMessage.add("The Response code for second message is  " + ResponsecodeDouble);
						String clmID2 = helpers.CommonFunctions.GetValueFormParagaraph(ResponsecodeDouble, "Unique-Claim-ID=");
						//String clmID_updated = pvnReference.substring(2);
						System.out.println("Claim ID: "+clmID2);
						ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", clmID2, key, "CLAIM ID");
						helpers.CommonFunctions.haltExecution_XML();
						String StagingStatusDouble = XMLhelpers.getStagingStatus(key, environment);
						utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", StagingStatusDouble, key, "Actual CLAIM STATUS IN STG");
						if(StagingStatusDouble != null)
						{
							System.out.println("The Staging status for Claims 2  " + StagingStatusDouble);
							AssertMessage.add("The Staging status for Claims 2  " + StagingStatusDouble);

							if(StagingStatusDouble.equalsIgnoreCase("Rejected"))
							{
								count = XMLhelpers.StagingStatus_Rejected(key, environment);
							}

							String ExpStatus = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",key, "Expected CLAIM STATUS IN STG");
							if(StagingStatusDouble.equalsIgnoreCase(ExpStatus))
							{
								System.out.println("The Correct staging status is displayed " + StagingStatusDouble);
								AssertMessage.add("The Correct staging status is displayed " + StagingStatusDouble);
								if(StagingStatusDouble.equalsIgnoreCase("Rejected"))
								{
									//int count = XMLhelpers.StagingStatus_Rejected(key);
									if (count == 0)
									{
										StagingProcess = true;
										System.out.println("The errors are matching with expected errors.");
										AssertMessage.add("The errors are matching with expected errors.");							
									}
									else 
									{
										AssertMessage.add("The errors are not matching with expected errors - Staging Process.");
									}
									Verify.verifyTrue(count == 0, "The errors are not matching with expected errors"+StagingStatus);
								}

								if(StagingStatusDouble.equalsIgnoreCase("Accepted"))
								{
									StagingProcess = true;
									System.out.println("The Staging status is  " + StagingStatusDouble);
									AssertMessage.add("The Staging status is  " + StagingStatusDouble);
								}
							}
							else
							{
								System.out.println("The expected Staging Status for duplicate message is not matching. The expected status is " + ExpStatus+". But Actual Status is "+StagingStatusDouble);
								AssertMessage.add("The expected Staging Status for duplicate message is not matching. The expected status is " + ExpStatus+". But Actual Status is "+StagingStatusDouble);
							}

							System.out.println("The Staging Process is Completed  " + StagingStatusDouble);
							AssertMessage.add("The Staging Process is Completed  " + StagingStatusDouble);					
						}
					}
					else 
					{
						System.out.println("The Response code are null after Duplicate response  " + ResponsecodeDouble);
						AssertMessage.add("The Response code are null after Duplicate response  " + ResponsecodeDouble);
					}
				

				}
				else 
				{
					System.out.println("The status of first message is not accepted  " + StagingStatus);
					AssertMessage.add("The status of first message is not accepted " + StagingStatus);
				}
			}
			else 
			{
				System.out.println("The Response code are null   " + Responsecode);
				AssertMessage.add("The Response code are null " + Responsecode);
			}
		}
		return new XMLhelpers(StagingProcess,AssertMessage);
	}
					
				/*}
				else 
				{
						StagingProcess = false;
						if(StagingStatus.equalsIgnoreCase("Rejected"))
						{
							StagingProcess = true;
							int countError = XMLhelpers.StagingStatus_Rejected(key, environment);
							if (countError == 0)
							{
								VerifyStagingError = true;
								System.out.println("The errors are matching with expected errors.");
								AssertMessage.add("The errors are matching with expected errors.");

							}
							else 
							{
								AssertMessage.add("The errors are not matching with expected errors - Staging Process.");
							}
							Verify.verifyTrue(count == 0, "The errors are not matching with expected errors"+StagingStatus);
						}
					}
				
			}
			else 
			{
				System.out.println("The Response code are null   " + Responsecode);
				AssertMessage.add("The Response code are null " + Responsecode);
			}
		}
		return new XMLhelpers(StagingProcess,VerifyStagingError,AssertMessage);
	}*/

	
	public static XMLhelpers BSXMLProcess_GOS6_Valid(String XMLTYPE , String FileNames, String key , boolean evidence, String environment) throws IOException, InterruptedException
	{

		boolean StagingProcess = false;
		boolean JobRun = false;
		String XMLValue = null;
		int count = 0;
		List<String> AssertMessage = new ArrayList<String>();

		//String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);

		List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "DuplicateAttribute", 1);
		for(String AttributeName : AttributeNames )
		{
			if(!AttributeName.equalsIgnoreCase("Attribute"))
			{
			XMLValue = ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeName, key);
			if(XMLValue != null && !XMLValue.isEmpty())
				{
					helpers.CommonFunctions.ReadXML_UpdateCodeValue_Attribute(XMLTYPE,FileNames,"DuplicateAttribute", AttributeName,key);
				}
			}
		}
		System.out.println(FileNames);
		String Responsecode = XMLhelpers.GetResponsecodeBSXML(FileNames,key, environment);
		if(Responsecode != null)
		{
			System.out.println("The Response code are " + Responsecode);
			AssertMessage.add("The Response code are " + Responsecode);
			helpers.CommonFunctions.haltExecution_XML();
			String StagingStatus = XMLhelpers.getStagingStatus(key, environment);
			//utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", StagingStatus, key, "Actual CLAIM STATUS IN STG");
			if(StagingStatus != null)
			{
				System.out.println("The Staging status is  " + StagingStatus);
				AssertMessage.add("The Staging status is  " + StagingStatus);
				/*if(StagingStatus.equalsIgnoreCase("Rejected"))
				{
					int count = XMLhelpers.StagingStatus_Rejected(key);
					if (count == 0)
					{
						StagingProcess = true;
						System.out.println("The errors are matching with expected errors.");
						AssertMessage.add("The errors are matching with expected errors.");

					}
					else 
					{
						AssertMessage.add("The errors are not matching with expected errors - Staging Process.");
					}
					Verify.verifyTrue(count == 0, "The errors are not matching with expected errors"+StagingStatus);
				}*/

			/*	if(StagingStatus.equalsIgnoreCase("Accepted"))
				{
					System.out.println("The Staging status for PVN Claim" + StagingStatus);
					AssertMessage.add("The Staging status for PVN Claim" + StagingStatus);
					AssertMessage.add("The correct Staging status is appeared:   " + StagingStatus);
					//System.out.println(FileNames);
					String ResponsecodeDouble = XMLhelpers.GetResponsecodeXMLGOS6(FileNames,key);
					if(ResponsecodeDouble != null)
					{*/
						System.out.println("The Response code for second message is " + Responsecode);
						AssertMessage.add("The Response code for second message is  " + Responsecode);
						//helpers.CommonFunctions.haltExecution_XML();
						//StagingStatus = XMLhelpers.getStagingStatus(key);
						utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", StagingStatus, key, "Actual CLAIM STATUS IN STG");
						if(StagingStatus != null)
						{
							System.out.println("The Staging status for Claims 2  " + StagingStatus);
							AssertMessage.add("The Staging status for Claims 2  " + StagingStatus);

							if(StagingStatus.equalsIgnoreCase("Rejected"))
							{
								count = XMLhelpers.StagingStatus_Rejected(key, environment);
							}

							String ExpStatus = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",key, "Expected CLAIM STATUS IN STG");
							if(StagingStatus.equalsIgnoreCase(ExpStatus))
							{
								System.out.println("The Correct staging status is displayed " + StagingStatus);
								AssertMessage.add("The Correct staging status is displayed " + StagingStatus);
								if(StagingStatus.equalsIgnoreCase("Rejected"))
								{
									//int count = XMLhelpers.StagingStatus_Rejected(key);
									if (count == 0)
									{
										StagingProcess = true;
										System.out.println("The errors are matching with expected errors.");
										AssertMessage.add("The errors are matching with expected errors.");							
									}
									else 
									{
										AssertMessage.add("The errors are not matching with expected errors - Staging Process.");
									}
									Verify.verifyTrue(count == 0, "The errors are not matching with expected errors"+StagingStatus);
								}

								if(StagingStatus.equalsIgnoreCase("Accepted"))
								{
									StagingProcess = true;
									System.out.println("The Staging status is  " + StagingStatus);
									AssertMessage.add("The Staging status is  " + StagingStatus);
								}
							}
							else
							{
								System.out.println("The expected Staging Status for GOS6 BS message is not matching. The expected status is " + ExpStatus+". But Actual Status is "+StagingStatus);
								AssertMessage.add("The expected Staging Status for GOS6 BS message is not matching. The expected status is " + ExpStatus+". But Actual Status is "+StagingStatus);
							}

							System.out.println("The Staging Process is Completed  " + StagingStatus);
							AssertMessage.add("The Staging Process is Completed  " + StagingStatus);					
						}
						
						else{
							System.out.println("The Staging status is null   " + StagingStatus);
							AssertMessage.add("The Staging status is null  " + StagingStatus);
						}
					}
					/*else 
					{
						System.out.println("The Response code are null after Duplicate response  " + ResponsecodeDouble);
						AssertMessage.add("The Response code are null after Duplicate response  " + ResponsecodeDouble);
					}

				}
				else 
				{
					System.out.println("The status of first message is not accepted  " + StagingStatus);
					AssertMessage.add("The status of first message is not accepted " + StagingStatus);
				}
			}*/
			
			else 
			{
				System.out.println("The Response code are null   " + Responsecode);
				AssertMessage.add("The Response code are null " + Responsecode);
			}
		}
		return new XMLhelpers(StagingProcess,AssertMessage);
	}

	public static XMLhelpers XMLProcess_VerifyONCRM(WebDriver driver, String key , boolean evidence) throws IOException, InterruptedException, AWTException
	{
		Thread.sleep(20000);
		boolean CRMProcess = false;
		boolean CompareAmtDue = false;
		List<String> AssertMessage = new ArrayList<String>();
		List<String> paymentLineDetailsList = new ArrayList<String>();
		String ClaimStatusCRM = XMLhelpers.GetClaimsCRM_BS(driver, key);
		if(!ClaimStatusCRM.isEmpty() && ClaimStatusCRM != null)
		{
			String ClaimStatus = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",key,"Expected CLAIM STATUS IN CRM");
			AdvancedFindResult ObjAdvancedFindResult = new AdvancedFindResult(driver);
			String ClaimID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",key,"CLAIM ID");
			String ClaimStaCRM =  ObjAdvancedFindResult.getClaimCRM();
			utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", ClaimStaCRM, key , "Actual CLAIM STATUS IN CRM"); 
			AssertMessage.add("The Claims status on CRM is  " + ClaimStaCRM);
			if(evidence)
			{
				ObjAdvancedFindResult.screenshots_CRMXML(key + ClaimStaCRM+"CRMStatus");
			}
			boolean ClaimstatusOnCRM = ObjAdvancedFindResult.VerifyClaimsStatus(ClaimID,ClaimStatus);
			if(ClaimstatusOnCRM)
			{
				CRMProcess = true;
				System.out.println("The Correct Claims status is appered on CRM  " + ClaimStaCRM);
				AssertMessage.add("The Correct Claims status is appered on CRM " + ClaimStaCRM);

				boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);	
				if(PaymentLine)
				{						
					System.out.println("The Payment Line is generated " +ClaimID );
					AssertMessage.add("The Payment Line is generated" +ClaimID);
					paymentLineDetailsList = ObjAdvancedFindResult.paymentlineStatus(evidence, key+"_PaymentLineStatus", key+"_GMPAmountDue");
					String PaymentLineStatus = paymentLineDetailsList.get(0);
					System.out.println(PaymentLineStatus);
					String ActAmountDue = paymentLineDetailsList.get(1);
					System.out.println(ActAmountDue);
					utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", ActAmountDue, key, "Actual Amount Due");
					String ExpectedAmountDue = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", key, "Expected Amount Due");
					CompareAmtDue = helpers.CommonFunctions.ComparetoString(ExpectedAmountDue,ActAmountDue);
					System.out.println("The Actual Payment Amount Due is: "+ActAmountDue);
					AssertMessage.add("The Actual Payment Amount Due is: "+ActAmountDue);
					/*if(evidence)
					{
						ObjAdvancedFindResult.screenshots_CRMXML(key + ClaimStaCRM+"PaymentLines");
					}*/
					if(CompareAmtDue)
					{
						AssertMessage.add("The Payment Amount Due is matching with expected amount.");
					}
					else
					{
						
					}
					Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
					if(AlertPresent)
					{
						ObjAdvancedFindResult.ClickSpaceBar();
					}
				}
				else
				{
					System.out.println("The Payment Line is not generated " +ClaimID );
					AssertMessage.add("The Payment Line is not generated" +ClaimID);
					Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
					if(AlertPresent)
					{
						ObjAdvancedFindResult.ClickSpaceBar();
					}
				}


				if(ClaimStatusCRM.equalsIgnoreCase("Rejected"))
				{
					ObjAdvancedFindResult.XMLCRM_Rejected(key ,ClaimID, evidence);
					int count = ObjAdvancedFindResult.verifyXMLerrorCRM(key);
					if(count == 0)
					{
						CRMProcess = true;
						System.out.println("The correct CRM error is displayed for claim" +ClaimID );
						AssertMessage.add("The correct CRM error is displayed for claim" +ClaimID);
					}
					else 
					{
						CRMProcess = false;
						AssertMessage.add("The incorrect CRM error is displayed "+ClaimID);
					}
				}
			}
			else 
			{
				AssertMessage.add("The inCorrect Claims status is appered on CRM. The claim staus appeared as:  " + ClaimStaCRM);
			}

		}
		/*

			else
			{
				System.out.println("The CRM claims status is  " +ClaimStatusCRM );
				AssertMessage.add("The CRM claims status is " +ClaimStatusCRM);


			}*/
	
			else
			{
				System.out.println("No Claim record found in CRM." );
				AssertMessage.add("No Claim record found in CRM. ");
			}


			return new XMLhelpers(CRMProcess,AssertMessage);
		}
	
	
	
	// Amit : Added below funtion to verify PVN Status on CRM.
	public static XMLhelpers XMLProcess_VerifyONCRM_PVNStatus(WebDriver driver, String key , boolean evidence, String pvnReference) throws IOException, InterruptedException, AWTException
	{
		
		boolean CRMProcess = false;
		List<String> AssertMessage = new ArrayList<String>();
		String ClaimStatusCRM = XMLhelpers.GetPVNStatusCRM_BS(driver, key, pvnReference);
		if(!ClaimStatusCRM.isEmpty() && ClaimStatusCRM != null)
		{
			String ClaimStatus = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",key,"Expected CLAIM STATUS IN CRM");
			AdvancedFindResult ObjAdvancedFindResult = new AdvancedFindResult(driver);
			String ClaimID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",key,"CLAIM ID");
		//	String ClaimStaCRM =  ObjAdvancedFindResult.getPVNStatusCRM();
			AssertMessage.add("The PVN status on CRM is  " + ClaimStatusCRM);
			if(evidence)
			{
				ObjAdvancedFindResult.screenshots_PVNStatus(key + ClaimStatusCRM+"CRMPVNStatus");
				ObjAdvancedFindResult.closeScreen("GOS6 Pre Visit Notification");
				ObjAdvancedFindResult.closeAdvanceFindWindow();
			}
			boolean ClaimstatusOnCRM = ObjAdvancedFindResult.VerifyPVNStatus(ClaimStatusCRM,ClaimStatus);
			if(ClaimstatusOnCRM)
			{
				CRMProcess = true;
				System.out.println("The Correct Claims status is appered on CRM  " + ClaimStatusCRM);
				AssertMessage.add("The Correct Claims status is appered on CRM " + ClaimStatusCRM);

			/*	boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);	
				if(PaymentLine)
				{						
					System.out.println("The Payment Line is generated " +ClaimID );
					AssertMessage.add("The Payment Line is generated" +ClaimID);
					if(evidence)
					{
						ObjAdvancedFindResult.screenshots_CRMXML(key + ClaimStaCRM+"PaymentLines");
					}
					Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
					if(AlertPresent)
					{
						ObjAdvancedFindResult.ClickSpaceBar();
					}
				}
				else
				{
					System.out.println("The Payment Line is not generated " +ClaimID );
					AssertMessage.add("The Payment Line is not generated" +ClaimID);
					Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
					if(AlertPresent)
					{
						ObjAdvancedFindResult.ClickSpaceBar();
					}
				}*/


				if(ClaimStatusCRM.equalsIgnoreCase("Rejected"))
				{
				
					ObjAdvancedFindResult.XMLCRM_Rejected_PVN(key, ClaimID, evidence);
					int count = ObjAdvancedFindResult.verifyXMLerrorCRM(key);
					if(count == 0)
					{
						CRMProcess = true;
						System.out.println("The correct CRM error is displayed " +ClaimID );
						AssertMessage.add("The correct CRM error is displayed " +ClaimID);
					}
					else 
					{
						CRMProcess = false;
						AssertMessage.add("The incorrect CRM error is displayed "+ClaimID);
					}
				}
			}
			else 
			{
				AssertMessage.add("The inCorrect Claims status is appered on CRM. The claim staus appeared as:  " + ClaimStatusCRM);
			}

		}
		/*

			else
			{
				System.out.println("The CRM claims status is  " +ClaimStatusCRM );
				AssertMessage.add("The CRM claims status is " +ClaimStatusCRM);


			}*/
	
			else
			{
				System.out.println("No Claim record found in CRM." );
				AssertMessage.add("No Claim record found in CRM. ");
			}


			return new XMLhelpers(CRMProcess,AssertMessage);
		}

		public static XMLhelpers XMLProcess_GOS3(String XMLTYPE, String key , boolean evidence, String environment) throws IOException, InterruptedException
		{
			boolean StagingProcess = false;
			boolean JobRun = false;
			String XMLValue = null;
			List<String> AssertMessage = new ArrayList<String>();		
			String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
			XMLhelpers objXMLhelpersStg = new XMLhelpers(StagingProcess, AssertMessage);
			String[] filenameList = FileNames.split(",");
			System.out.println(filenameList[0]);
			String GetVocherCodefile = filenameList[0];
			List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "DuplicateAttribute", 1);
			for(String AttributeName : AttributeNames )
			{
				if(!AttributeName.equalsIgnoreCase("Attribute"))
				{
					XMLValue = ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeName, key);
					if(XMLValue != null && !XMLValue.isEmpty())
						{
						helpers.CommonFunctions.ReadXML_UpdateCodeValue_Attribute(XMLTYPE,filenameList[0],"DuplicateAttribute", AttributeName,key);
						}
				}
			}
			System.out.println(filenameList[0]);
			String Responsecode = XMLhelpers.GetResponsecodeXML_GOS3(filenameList[0],key, environment);
			if(Responsecode != null)
			{
				System.out.println("The Response code are " + Responsecode);
				AssertMessage.add("The Response code is generated " + Responsecode);
				String VoucherCode = helpers.CommonFunctions.GetValueFormParagaraph(Responsecode, "Voucher-Code=");
				String AuthorisationCode = helpers.CommonFunctions.GetValueFormParagaraph(Responsecode, "Authorisation-Code=");
				System.out.println("The VoucherCode " + VoucherCode);
				System.out.println("The Authorization code " +AuthorisationCode); 				
				ExcelUtilities.setKeyValueinExcelWithPosition("XMLStagingCRM.xlsx", "VocAuthCode", "GOS3-Voucher-Code", VoucherCode, 2);
				ExcelUtilities.setKeyValueinExcelWithPosition("XMLStagingCRM.xlsx", "VocAuthCode", "GOS3-Authorisation-Code", AuthorisationCode, 2);
				List<String> AttributeNamesVocAuthCodes = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "VocAuthCode", 1);
				for(String AttributeNamesVocAuthCode : AttributeNamesVocAuthCodes )
				{			
					helpers.CommonFunctions.ReadXML_UpdateCodeValue_Attribute(XMLTYPE, filenameList[1],"VocAuthCode" ,AttributeNamesVocAuthCode,key);
				}
				List<String> AttributeNamesFiles = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "DuplicateAttribute", 1);
				for(String AttributeNamesFile : AttributeNamesFiles )
				{
					if(!AttributeNamesFile.equalsIgnoreCase("Attribute"))
					{
						XMLValue = ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeNamesFile, key);
						if(XMLValue != null && !XMLValue.isEmpty())
							{
							helpers.CommonFunctions.ReadXML_UpdateCodeValue_Attribute(XMLTYPE,filenameList[1],"DuplicateAttribute", AttributeNamesFile,key);
							}
					}
				}
				String ResponsecodeUpdate = XMLhelpers.GetResponsecodeXML_GOS3(filenameList[1],key, environment);
				/*if(ResponsecodeUpdate != null)
				{
					System.out.println("The Response code are " + ResponsecodeUpdate);
					AssertMessage.add("The Response code is generated " + ResponsecodeUpdate);
					helpers.CommonFunctions.haltExecution_XML();
					String StagingStatus = XMLhelpers.getStagingStatus(key);
					if(StagingStatus != null)
					{
						System.out.println("The Staging status is  " + StagingStatus);
						AssertMessage.add("The Staging status is  " + StagingStatus);
						String ExpStatus = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",key, "Expected CLAIM STATUS IN STG");
						if(StagingStatus.equalsIgnoreCase(ExpStatus))
						{
							System.out.println("The Correct staging status is displayed " + StagingStatus);
							AssertMessage.add("The Correct staging status is displayed " + StagingStatus);
							if(StagingStatus.equalsIgnoreCase("Rejected"))
							{
								int count = XMLhelpers.StagingStatus_Rejected(key);
								if (count == 0)
								{
									StagingProcess = true;
									System.out.println("The errors are matching with expected errors.");
									AssertMessage.add("The errors are matching with expected errors.");

								}
								else 
								{
									AssertMessage.add("The errors are not matching with expected errors - Staging Process.");
								}
								Verify.verifyTrue(count == 0, "The errors are not matching with expected errors"+StagingStatus);
							}
							if(StagingStatus.equalsIgnoreCase("Accepted"))
							{
								StagingProcess = true;
								System.out.println("The Staging status is  " + StagingStatus);
								AssertMessage.add("The Staging status is  " + StagingStatus);
							}
							System.out.println("The Staging Process is Completed  " + StagingStatus);
							AssertMessage.add("The Staging Process is Completed  " + StagingStatus);
						}
						Verify.verifyNotEquals(StagingStatus.equalsIgnoreCase(ExpStatus), "The Incorrect staging status is appered"+StagingStatus);				
					}
					else 
					{
						System.out.println("The Response code are null   " + ResponsecodeUpdate);
						AssertMessage.add("The Response code are null " + ResponsecodeUpdate);
					}
				}*/
				objXMLhelpersStg = XMLProcess_StagingStatusVerification(ResponsecodeUpdate, key, environment);
				StagingProcess = objXMLhelpersStg.Process;
				for (String msg: objXMLhelpersStg.AssetMessage)
				{
					AssertMessage.add(msg);
				}
				Verify.verifyNotEquals(Responsecode != null, "The Incorrect Response code is "+Responsecode);
			}
			return new XMLhelpers(StagingProcess,AssertMessage);

		}
		public static XMLhelpers BULKXMLProcess_GOS3(String MainFile, String XMLTYPE, String key , boolean evidence, String environment) throws IOException, InterruptedException
		{
			boolean StagingProcess = false;
			boolean JobRun = false;
			String XMLValue = null;
			List<String> AssertMessage = new ArrayList<String>();		
			String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
			XMLhelpers objXMLhelpersStg = new XMLhelpers(StagingProcess, AssertMessage);
			String[] filenameList = FileNames.split(",");
			System.out.println(filenameList[0]);
			String GetVocherCodefile = filenameList[0];
			List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel(MainFile, "VocTagValues", 1);
			for(String AttributeName : AttributeNames )
			{
				if(!AttributeName.equalsIgnoreCase("Attribute"))
				{
					XMLValue = ExcelUtilities.getKeyValueByPosition(MainFile, "VocTagValues", AttributeName, key);
					if(XMLValue != null && !XMLValue.isEmpty())
						{
						helpers.CommonFunctions.BULKReadXML_UpdateCodeValue_Attribute(XMLTYPE,filenameList[0],"VocTagValues", AttributeName,key,MainFile);
						}
				}
			}
			System.out.println(filenameList[0]);
			String Responsecode = XMLhelpers.GetResponsecodeXML_GOS3(filenameList[0],key, environment);
			if(Responsecode != null)
			{
				System.out.println("The Response code are " + Responsecode);
				AssertMessage.add("The Response code is generated " + Responsecode);
				String VoucherCode = helpers.CommonFunctions.GetValueFormParagaraph(Responsecode, "Voucher-Code=");
				String AuthorisationCode = helpers.CommonFunctions.GetValueFormParagaraph(Responsecode, "Authorisation-Code=");
				System.out.println("The VoucherCode " + VoucherCode);
				System.out.println("The Authorization code " +AuthorisationCode); 				
				ExcelUtilities.setKeyValueByPosition(MainFile, "UpdateTagValues",VoucherCode, "GOS3-Voucher-Code", key);
				ExcelUtilities.setKeyValueByPosition(MainFile, "UpdateTagValues",VoucherCode, "GOS3-Authorisation-Code", key);
				List<String> AttributeNamesFiles = ExcelUtilities.getCellValuesInExcel(MainFile, "UpdateTagValues", 1);
				for(String AttributeNamesFile : AttributeNamesFiles )
				{
					if(!AttributeNamesFile.equalsIgnoreCase("Attribute"))
					{
						XMLValue = ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeNamesFile, key);
						if(XMLValue != null && !XMLValue.isEmpty())
							{
							helpers.CommonFunctions.BULKReadXML_UpdateCodeValue_Attribute(XMLTYPE,filenameList[1],"UpdateTagValues", AttributeNamesFile,key,MainFile);
							}
					}
				}
				String ResponsecodeUpdate = XMLhelpers.GetResponsecodeXML_GOS3(filenameList[1],key, environment);
			
				objXMLhelpersStg = XMLProcess_StagingStatusVerification(ResponsecodeUpdate, key, environment);
				StagingProcess = objXMLhelpersStg.Process;
				for (String msg: objXMLhelpersStg.AssetMessage)
				{
					AssertMessage.add(msg);
				}
				Verify.verifyNotEquals(Responsecode != null, "The Incorrect Response code is "+Responsecode);
			}
			return new XMLhelpers(StagingProcess,AssertMessage);

		}

		public static XMLhelpers XMLProcess_GOS4(String XMLTYPE, String key , boolean evidence, String environment) throws IOException, InterruptedException
		{
			boolean StagingProcess = false;
			boolean JobRun = false;
			String XMLValue = null;
			List<String> AssertMessage = new ArrayList<String>();		
			XMLhelpers objXMLhelpersStg = new XMLhelpers(StagingProcess, AssertMessage);
			String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
			String[] filenameList = FileNames.split(",");
			System.out.println(filenameList[0]);
			String GetVocherCodefile = filenameList[0];
			List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "UpdatedAttribute", 1);
			for(String AttributeName : AttributeNames )
			{
				if(!AttributeName.equalsIgnoreCase("Attribute"))
				{
				XMLValue = ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeName, key);
				if(XMLValue != null && !XMLValue.isEmpty())
					{
						helpers.CommonFunctions.ReadXML_UpdateCodeValue_Attribute(XMLTYPE,filenameList[0],"DuplicateAttribute", AttributeName,key);
					}
				}
			}
			System.out.println(filenameList[0]);
			String Responsecode = XMLhelpers.GetResponsecodeXML_GOS3(filenameList[0],key, environment);
			if(Responsecode != null)
			{
				System.out.println("The Response code are " + Responsecode);
				AssertMessage.add("The Response code is generated " + Responsecode);
				String VoucherCode = helpers.CommonFunctions.GetValueFormParagaraph(Responsecode, "Voucher-Code=");
				String AuthorisationCode = helpers.CommonFunctions.GetValueFormParagaraph(Responsecode, "Authorisation-Code=");
				ExcelUtilities.setKeyValueinExcelWithPosition("XMLStagingCRM.xlsx", "VocAuthCode", "GOS3-Voucher-Code", VoucherCode, 2);
				ExcelUtilities.setKeyValueinExcelWithPosition("XMLStagingCRM.xlsx", "VocAuthCode", "GOS3-Authorisation-Code", AuthorisationCode, 2);
				List<String> AttributeNamesVocAuthCodes = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "VocAuthCode", 1);
				for(String AttributeNamesVocAuthCode : AttributeNamesVocAuthCodes )
				{
					helpers.CommonFunctions.ReadXML_UpdateCodeValue_Attribute(XMLTYPE,filenameList[1],"VocAuthCode" ,AttributeNamesVocAuthCode,key);
				}
				List<String> GOS4AttributeNames = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "UpdatedAttribute", 1);
				for(String GOS4AttributeName : GOS4AttributeNames )
				{
					helpers.CommonFunctions.ReadXML_UpdateCodeValue_AttributeGOS4(filenameList[2],"UpdatedAttribute", GOS4AttributeName,key);
				}
				String ResponsecodeUpdate = XMLhelpers.GetResponsecodeXML_GOS3(filenameList[2],key, environment);
				/*if(ResponsecodeUpdate != null)
				{
					System.out.println("The Response code are " + ResponsecodeUpdate);
					AssertMessage.add("The Response code is generated " + ResponsecodeUpdate);
					helpers.CommonFunctions.haltExecution_XML();
					String StagingStatus = XMLhelpers.getStagingStatus(key);
					if(StagingStatus != null)
					{
						System.out.println("The Staging status is  " + StagingStatus);
						AssertMessage.add("The Staging status is  " + StagingStatus);
						String ExpStatus = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",key, "Expected CLAIM STATUS IN STG");
						if(StagingStatus.equalsIgnoreCase(ExpStatus))
						{
							System.out.println("The Correct staging status is displayed " + StagingStatus);
							AssertMessage.add("The Correct staging status is displayed " + StagingStatus);
							if(StagingStatus.equalsIgnoreCase("Rejected"))
							{
								int count = XMLhelpers.StagingStatus_Rejected(key);
								if (count == 0)
								{
									StagingProcess = true;
									System.out.println("The errors are matching with expected errors.");
									AssertMessage.add("The errors are matching with expected errors.");

								}
								else 
								{
									AssertMessage.add("The errors are not matching with expected errors - Staging Process.");
								}
								Verify.verifyTrue(count == 0, "The errors are not matching with expected errors"+StagingStatus);
							}
							if(StagingStatus.equalsIgnoreCase("Accepted"))
							{
								StagingProcess = true;
								System.out.println("The Staging status is  " + StagingStatus);
								AssertMessage.add("The Staging status is  " + StagingStatus);
							}
							System.out.println("The Staging Process is Completed  " + StagingStatus);
							AssertMessage.add("The Staging Process is Completed  " + StagingStatus);
						}
						Verify.verifyNotEquals(StagingStatus.equalsIgnoreCase(ExpStatus), "The Incorrect staging status is appered"+StagingStatus);				
					}
					else 
					{
						System.out.println("The Response code are null   " + ResponsecodeUpdate);
						AssertMessage.add("The Response code are null " + ResponsecodeUpdate);
					}
				}*/
				objXMLhelpersStg = XMLProcess_StagingStatusVerification(ResponsecodeUpdate, key, environment);
				StagingProcess = objXMLhelpersStg.Process;
				for (String msg: objXMLhelpersStg.AssetMessage)
				{
					AssertMessage.add(msg);
				}
				Verify.verifyNotEquals(Responsecode != null, "The Incorrect Response code is "+Responsecode);
			}
			return new XMLhelpers(StagingProcess,AssertMessage);

		}

		public static XMLhelpers BSXMLProcess_GOS3(String XMLTYPE, String key , boolean evidence, String environment) throws IOException, InterruptedException
		{
			boolean StagingProcess = false;
			boolean JobRun = false;
			String XMLValue = null;
			List<String> AssertMessage = new ArrayList<String>();		
			String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
			XMLhelpers objXMLhelpersStg = new XMLhelpers(StagingProcess, AssertMessage);
			String[] filenameList = FileNames.split(",");
			System.out.println(filenameList[0]);
			String GetVocherCodefile = filenameList[0];
			List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "DuplicateAttribute", 1);
			for(String AttributeName : AttributeNames )
			{
				if(!AttributeName.equalsIgnoreCase("Attribute"))
				{
					XMLValue = ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeName, key);
					if(XMLValue != null && !XMLValue.isEmpty())
						{
						helpers.CommonFunctions.ReadXML_UpdateCodeValue_Attribute(XMLTYPE, filenameList[0],"DuplicateAttribute", AttributeName,key);
						}
				}
			}
			System.out.println(filenameList[0]);
			String Responsecode = XMLhelpers.GetResponsecodeBSXML(filenameList[0],key, environment);
			if(Responsecode != null)
			{
				System.out.println("The Response code are " + Responsecode);
				AssertMessage.add("The Response code is generated " + Responsecode);
				String VoucherCode = helpers.CommonFunctions.GetValueFormParagaraph(Responsecode, "Voucher-Code=");
				String AuthorisationCode = helpers.CommonFunctions.GetValueFormParagaraph(Responsecode, "Authorisation-Code=");
				ExcelUtilities.setKeyValueinExcelWithPosition("XMLStagingCRM.xlsx", "VocAuthCode", "GOS3-Voucher-Code", VoucherCode, 2);
				ExcelUtilities.setKeyValueinExcelWithPosition("XMLStagingCRM.xlsx", "VocAuthCode", "GOS3-Authorisation-Code", AuthorisationCode, 2);
				List<String> AttributeNamesVocAuthCodes = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "VocAuthCode", 1);
				for(String AttributeNamesVocAuthCode : AttributeNamesVocAuthCodes )
				{			
					helpers.CommonFunctions.ReadXML_UpdateCodeValue_Attribute(XMLTYPE, filenameList[1],"VocAuthCode" ,AttributeNamesVocAuthCode,key);
				}
				String ResponsecodeUpdate = XMLhelpers.GetResponsecodeBSXML(filenameList[1],key, environment);
				/*if(ResponsecodeUpdate != null)
				{
					System.out.println("The Response code are " + ResponsecodeUpdate);
					AssertMessage.add("The Response code is generated " + ResponsecodeUpdate);
					helpers.CommonFunctions.haltExecution_XML();
					String StagingStatus = XMLhelpers.getStagingStatus(key);
					if(StagingStatus != null)
					{
						System.out.println("The Staging status is  " + StagingStatus);
						AssertMessage.add("The Staging status is  " + StagingStatus);
						String ExpStatus = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",key, "Expected CLAIM STATUS IN STG");
						if(StagingStatus.equalsIgnoreCase(ExpStatus))
						{
							System.out.println("The Correct staging status is displayed " + StagingStatus);
							AssertMessage.add("The Correct staging status is displayed " + StagingStatus);
							if(StagingStatus.equalsIgnoreCase("Rejected"))
							{
								int count = XMLhelpers.StagingStatus_Rejected(key);
								if (count == 0)
								{
									StagingProcess = true;
									System.out.println("The errors are matching with expected errors.");
									AssertMessage.add("The errors are matching with expected errors.");

								}
								else 
								{
									AssertMessage.add("The errors are not matching with expected errors - Staging Process.");
								}
								Verify.verifyTrue(count == 0, "The errors are not matching with expected errors"+StagingStatus);
							}
							if(StagingStatus.equalsIgnoreCase("Accepted"))
							{
								StagingProcess = true;
								System.out.println("The Staging status is  " + StagingStatus);
								AssertMessage.add("The Staging status is  " + StagingStatus);
							}
							System.out.println("The Staging Process is Completed  " + StagingStatus);
							AssertMessage.add("The Staging Process is Completed  " + StagingStatus);
						}
						Verify.verifyNotEquals(StagingStatus.equalsIgnoreCase(ExpStatus), "The Incorrect staging status is appered"+StagingStatus);				
					}
					else 
					{
						System.out.println("The Response code are null   " + ResponsecodeUpdate);
						AssertMessage.add("The Response code are null " + ResponsecodeUpdate);
					}
				}*/
				objXMLhelpersStg = XMLProcess_StagingStatusVerification(ResponsecodeUpdate, key, environment);
				StagingProcess = objXMLhelpersStg.Process;
				for (String msg: objXMLhelpersStg.AssetMessage)
				{
					AssertMessage.add(msg);
				}
				Verify.verifyNotEquals(Responsecode != null, "The Incorrect Response code is "+Responsecode);
			}
			return new XMLhelpers(StagingProcess,AssertMessage);

		}


		public static XMLhelpers BSXMLProcess_GOS4(String XMLTYPE, String key , boolean evidence, String environment) throws IOException, InterruptedException
		{
			boolean StagingProcess = false;
			boolean JobRun = false;
			String XMLValue = null;
			List<String> AssertMessage = new ArrayList<String>();		
			String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
			String[] filenameList = FileNames.split(",");
			System.out.println(filenameList[0]);
			String GetVocherCodefile = filenameList[0];
			List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "DuplicateAttribute", 1);
			XMLhelpers objXMLhelpersStg = new XMLhelpers(StagingProcess, AssertMessage);
			for(String AttributeName : AttributeNames )
			{
				if(!AttributeName.equalsIgnoreCase("Attribute"))
				{
				XMLValue = ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeName, key);
				if(XMLValue != null && !XMLValue.isEmpty())
					{
						helpers.CommonFunctions.ReadXML_UpdateCodeValue_Attribute(XMLTYPE ,filenameList[0],"DuplicateAttribute", AttributeName,key);
					}
				}
			}
			System.out.println(filenameList[0]);
			String Responsecode = XMLhelpers.GetResponsecodeBSXML(filenameList[0],key, environment);
			if(Responsecode != null)
			{
				System.out.println("The Response code are " + Responsecode);
				AssertMessage.add("The Response code is generated " + Responsecode);
				String VoucherCode = helpers.CommonFunctions.GetValueFormParagaraph(Responsecode, "Voucher-Code=");
				String AuthorisationCode = helpers.CommonFunctions.GetValueFormParagaraph(Responsecode, "Authorisation-Code=");
				ExcelUtilities.setKeyValueinExcelWithPosition("XMLStagingCRM.xlsx", "VocAuthCode", "GOS3-Voucher-Code", VoucherCode, 2);
				ExcelUtilities.setKeyValueinExcelWithPosition("XMLStagingCRM.xlsx", "VocAuthCode", "GOS3-Authorisation-Code", AuthorisationCode, 2);
				List<String> AttributeNamesVocAuthCodes = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "VocAuthCode", 1);
				for(String AttributeNamesVocAuthCode : AttributeNamesVocAuthCodes )
				{
					helpers.CommonFunctions.ReadXML_UpdateCodeValue_Attribute(XMLTYPE, filenameList[1],"VocAuthCode" ,AttributeNamesVocAuthCode,key);
				}
				List<String> GOS4AttributeNames = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "UpdatedAttribute", 1);
				for(String GOS4AttributeName : GOS4AttributeNames )
				{
					helpers.CommonFunctions.ReadXML_UpdateCodeValue_AttributeGOS4(filenameList[2],"UpdatedAttribute", GOS4AttributeName,key);
				}
				String ResponsecodeUpdate = XMLhelpers.GetResponsecodeBSXML(filenameList[2],key, environment);
			/*	if(ResponsecodeUpdate != null)
				{
					System.out.println("The Response code are " + ResponsecodeUpdate);
					AssertMessage.add("The Response code is generated " + ResponsecodeUpdate);
					helpers.CommonFunctions.haltExecution_XML();
					String StagingStatus = XMLhelpers.getStagingStatus(key);
					if(StagingStatus != null)
					{
						System.out.println("The Staging status is  " + StagingStatus);
						AssertMessage.add("The Staging status is  " + StagingStatus);
						String ExpStatus = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",key, "Expected CLAIM STATUS IN STG");
						if(StagingStatus.equalsIgnoreCase(ExpStatus))
						{
							System.out.println("The Correct staging status is displayed " + StagingStatus);
							AssertMessage.add("The Correct staging status is displayed " + StagingStatus);
							if(StagingStatus.equalsIgnoreCase("Rejected"))
							{
								int count = XMLhelpers.StagingStatus_Rejected(key);
								if (count == 0)
								{
									StagingProcess = true;
									System.out.println("The errors are matching with expected errors.");
									AssertMessage.add("The errors are matching with expected errors.");

								}
								else 
								{
									AssertMessage.add("The errors are not matching with expected errors - Staging Process.");
								}
								Verify.verifyTrue(count == 0, "The errors are not matching with expected errors"+StagingStatus);
							}
							if(StagingStatus.equalsIgnoreCase("Accepted"))
							{
								StagingProcess = true;
								System.out.println("The Staging status is  " + StagingStatus);
								AssertMessage.add("The Staging status is  " + StagingStatus);
							}
							System.out.println("The Staging Process is Completed  " + StagingStatus);
							AssertMessage.add("The Staging Process is Completed  " + StagingStatus);
						}
						Verify.verifyNotEquals(StagingStatus.equalsIgnoreCase(ExpStatus), "The Incorrect staging status is appered"+StagingStatus);				
					}
					else 
					{
						System.out.println("The Response code are null   " + ResponsecodeUpdate);
						AssertMessage.add("The Response code are null " + ResponsecodeUpdate);
					}
				}
				Verify.verifyNotEquals(Responsecode != null, "The Incorrect Response code is "+Responsecode);
			}*/
				objXMLhelpersStg = XMLProcess_StagingStatusVerification(ResponsecodeUpdate, key, environment);
				StagingProcess = objXMLhelpersStg.Process;
				for (String msg: objXMLhelpersStg.AssetMessage)
				{
					AssertMessage.add(msg);
				}
		//	
			}
			Verify.verifyNotEquals(Responsecode != null, "The Incorrect Response code is "+Responsecode);
			return new XMLhelpers(StagingProcess,AssertMessage);
		}
		

	
		public static String BSXMLProcess_GOS156_SchFailure(String XMLTYPE, String key , boolean evidence, String environment) throws IOException, InterruptedException
		{
			boolean StagingProcess = false;
			boolean JobRun = false;
			String XMLValue = null;
			String FailureMessage = "";
			List<String> AssertMessage = new ArrayList<String>();

			String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);

			List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "DuplicateAttribute", 1);
			for(String AttributeName : AttributeNames )
			{
				if(!AttributeName.equalsIgnoreCase("Attribute"))
				{
				XMLValue = ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeName, key);
				if(XMLValue != null && !XMLValue.isEmpty())
					{
						helpers.CommonFunctions.ReadXML_UpdateCodeValue_Attribute(XMLTYPE,FileNames,"DuplicateAttribute", AttributeName,key);
					}
				}
			}
			System.out.println(FileNames);
			String Responsecode = XMLhelpers.GetResponsecodeBSXML(FileNames,key, environment);
			if(Responsecode != null)
			{
				System.out.println("The Response code are " + Responsecode);
				//AssertMessage.add("The Response code are " + Responsecode);
				FailureMessage = helpers.CommonFunctions.GetFailureMessage(Responsecode, "File-Validation-Failure-Description=");
				System.out.println("The failure message appeared as: "+FailureMessage);
				if (FailureMessage.length() == 0)
				{
					System.out.println("The failure message is not present.");
				}
				
				//Swrystem.out.println("The Staging Process is Completed  " + StagingStatus);
					//AssertMessage.add("The Staging Process is Completed  " + StagingStatus);
					//setAssertMessage("The Staging Process is Completed " + StagingStatus, 5);

				}
				else 
				{
					System.out.println("The Response code are null   " + Responsecode);
					//AssertMessage.add("The Response code are null " + Responsecode);
				}
			
			return FailureMessage;
		}
		
		
		public static String XMLProcess_GOS156_SchFailure(String XMLTYPE, String key , boolean evidence, String environment) throws IOException, InterruptedException
		{
			boolean StagingProcess = false;
			boolean JobRun = false;
			String XMLValue = null;
			List<String> AssertMessage = new ArrayList<String>();
			String FailureMessage = "";

			String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);

			List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "DuplicateAttribute", 1);
			for(String AttributeName : AttributeNames )
			{
				if(!AttributeName.equalsIgnoreCase("Attribute"))
				{
				XMLValue = ExcelUtilities.getKeyValueByPosition("XMLStagingCRM.xlsx", "DuplicateAttribute", AttributeName, key);
				if(XMLValue != null && !XMLValue.isEmpty())
					{
						helpers.CommonFunctions.ReadXML_UpdateCodeValue_Attribute(XMLTYPE, FileNames,"DuplicateAttribute", AttributeName,key);
					}
				}
			}
			System.out.println(FileNames);
			String Responsecode = XMLhelpers.GetResponsecodeXML_GOS3(FileNames,key, environment);
			if(Responsecode != null)
			{
				System.out.println("The Response code are " + Responsecode);
				//AssertMessage.add("The Response code are " + Responsecode);
				FailureMessage = helpers.CommonFunctions.GetFailureMessage(Responsecode, "File-Validation-Failure-Description=");
				System.out.println("The failure message appeared as: "+FailureMessage);
				if (FailureMessage.length() == 0)
				{
					System.out.println("The failure message is not present.");
				}
				
				//Swrystem.out.println("The Staging Process is Completed  " + StagingStatus);
					//AssertMessage.add("The Staging Process is Completed  " + StagingStatus);
					//setAssertMessage("The Staging Process is Completed " + StagingStatus, 5);

				}
				else 
				{
					System.out.println("The Response code are null   " + Responsecode);
					//AssertMessage.add("The Response code are null " + Responsecode);
				}
			
			return FailureMessage;
		}

		
			public static XMLhelpers BSXMLProcess_NotUpdateAttribute(String FileNames,String key , boolean evidence, String environment) throws IOException, InterruptedException
		{
			boolean StagingProcess = false;
			boolean JobRun = false;
			int count = 0;
			List<String> AssertMessage = new ArrayList<String>();
			System.out.println(FileNames);
			String Responsecode = XMLhelpers.GetResponsecodeBSXML(FileNames,key, environment);			
			if(Responsecode != null)
			{
				System.out.println("The Response code are " + Responsecode);
				AssertMessage.add("The Response code are " + Responsecode);
				String clmID = helpers.CommonFunctions.GetValueFormParagaraph(Responsecode, "Unique-Claim-ID=");
				//String clmID_updated = pvnReference.substring(2);
				System.out.println("Claim ID: "+clmID);
				helpers.CommonFunctions.haltExecution_XML();
				String StagingStatus = XMLhelpers.getStagingStatus(key, clmID);
				//utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", StagingStatus, key, "Actual CLAIM STATUS IN STG");
				if(StagingStatus != null)
				{
					System.out.println("The Staging status is  " + StagingStatus);
					AssertMessage.add("The Staging status is  " + StagingStatus);
					if(StagingStatus.equalsIgnoreCase("Accepted"))
					{
						System.out.println("The Staging status for Claims 1  " + StagingStatus);
						AssertMessage.add("The Staging status for Claims 1  " + StagingStatus);
						AssertMessage.add("The correct Staging status is apperaed   " + StagingStatus);
						System.out.println(FileNames);
						//helpers.CommonFunctions.haltExecution_XML();
						String ResponsecodeDouble = XMLhelpers.GetResponsecodeXMLGOS6(FileNames,key, environment);
						if(ResponsecodeDouble != null)
						{
							System.out.println("The Response code for second message is " + ResponsecodeDouble);
							AssertMessage.add("The Response code for second message is  " + ResponsecodeDouble);
							String clmID2 = helpers.CommonFunctions.GetValueFormParagaraph(ResponsecodeDouble, "Unique-Claim-ID=");
							//String clmID_updated = pvnReference.substring(2);
							System.out.println("Claim ID: "+clmID2);
							ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", clmID2, key, "CLAIM ID");
							helpers.CommonFunctions.haltExecution_XML();
							String StagingStatusDouble = XMLhelpers.getStagingStatus(key, clmID2);
							utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", StagingStatusDouble, key, "Actual CLAIM STATUS IN STG");
							if(StagingStatusDouble != null)
							{
								System.out.println("The Staging status for Claims 2  " + StagingStatusDouble);
								AssertMessage.add("The Staging status for Claims 2  " + StagingStatusDouble);

								if(StagingStatusDouble.equalsIgnoreCase("Rejected"))
								{
									count = XMLhelpers.StagingStatus_Rejected(key, clmID2);
								}

								String ExpStatus = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",key, "Expected CLAIM STATUS IN STG");
								if(StagingStatusDouble.equalsIgnoreCase(ExpStatus))
								{
									System.out.println("The Correct staging status is displayed " + StagingStatusDouble);
									AssertMessage.add("The Correct staging status is displayed " + StagingStatusDouble);
									if(StagingStatusDouble.equalsIgnoreCase("Rejected"))
									{
										//int count = XMLhelpers.StagingStatus_Rejected(key);
										if (count == 0)
										{
											StagingProcess = true;
											System.out.println("The errors are matching with expected errors.");
											AssertMessage.add("The errors are matching with expected errors.");							
										}
										else 
										{
											AssertMessage.add("The errors are not matching with expected errors - Staging Process.");
										}
										Verify.verifyTrue(count == 0, "The errors are not matching with expected errors"+StagingStatus);
									}

									if(StagingStatusDouble.equalsIgnoreCase("Accepted"))
									{
										StagingProcess = true;
										System.out.println("The Staging status is  " + StagingStatusDouble);
										AssertMessage.add("The Staging status is  " + StagingStatusDouble);
									}
								}
								else
								{
									System.out.println("The expected Staging Status for duplicate message is not matching. The expected status is " + ExpStatus+". But Actual Status is "+StagingStatusDouble);
									AssertMessage.add("The expected Staging Status for duplicate message is not matching. The expected status is " + ExpStatus+". But Actual Status is "+StagingStatusDouble);
								}

								System.out.println("The Staging Process is Completed  " + StagingStatusDouble);
								AssertMessage.add("The Staging Process is Completed  " + StagingStatusDouble);					
							}
						}
						else 
						{
							System.out.println("The Response code are null after Duplicate response  " + ResponsecodeDouble);
							AssertMessage.add("The Response code are null after Duplicate response  " + ResponsecodeDouble);
						}

					}
					else 
					{
						System.out.println("The status of first message is not accepted  " + StagingStatus);
						AssertMessage.add("The status of first message is not accepted " + StagingStatus);
					}
				}
				else 
				{
					System.out.println("The Response code are null   " + Responsecode);
					AssertMessage.add("The Response code are null " + Responsecode);
				}
			}
			return new XMLhelpers(StagingProcess,AssertMessage);

		}
		
		public static XMLhelpers XMLProcess_NotUpdateAttribute(String FileNames,String key , boolean evidence, String environment) throws IOException, InterruptedException
		{
			boolean StagingProcess = false;
			boolean JobRun = false;
			int count = 0;
			List<String> AssertMessage = new ArrayList<String>();
			System.out.println(FileNames);
			String Responsecode = XMLhelpers.GetResponsecodeXML(FileNames,key,environment);			
			if(Responsecode != null)
			{
				System.out.println("The Response code are " + Responsecode);
				AssertMessage.add("The Response code are " + Responsecode);
				String clmID = helpers.CommonFunctions.GetValueFormParagaraph(Responsecode, "Unique-Claim-ID=");
				//String clmID_updated = pvnReference.substring(2);
				System.out.println("Claim ID: "+clmID);
				helpers.CommonFunctions.haltExecution_XML();
				String StagingStatus = XMLhelpers.getStagingStatus(key, clmID);
				//utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", StagingStatus, key, "Actual CLAIM STATUS IN STG");
				if(StagingStatus != null)
				{
					System.out.println("The Staging status is  " + StagingStatus);
					AssertMessage.add("The Staging status is  " + StagingStatus);
					if(StagingStatus.equalsIgnoreCase("Accepted"))
					{
						System.out.println("The Staging status for Claims 1  " + StagingStatus);
						AssertMessage.add("The Staging status for Claims 1  " + StagingStatus);
						AssertMessage.add("The correct Staging status is apperaed   " + StagingStatus);
						System.out.println(FileNames);
						//helpers.CommonFunctions.haltExecution_XML();
						String ResponsecodeDouble = XMLhelpers.GetResponsecodeXMLGOS6(FileNames,key,environment);
						if(ResponsecodeDouble != null)
						{
							System.out.println("The Response code for second message is " + ResponsecodeDouble);
							AssertMessage.add("The Response code for second message is  " + ResponsecodeDouble);
							String clmID2 = helpers.CommonFunctions.GetValueFormParagaraph(ResponsecodeDouble, "Unique-Claim-ID=");
							//String clmID_updated = pvnReference.substring(2);
							System.out.println("Claim ID: "+clmID2);
							ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", clmID2, key, "CLAIM ID");
							helpers.CommonFunctions.haltExecution_XML();
							String StagingStatusDouble = XMLhelpers.getStagingStatus(key, clmID2);
							utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", StagingStatusDouble, key, "Actual CLAIM STATUS IN STG");
							if(StagingStatusDouble != null)
							{
								System.out.println("The Staging status for Claims 2  " + StagingStatusDouble);
								AssertMessage.add("The Staging status for Claims 2  " + StagingStatusDouble);

								if(StagingStatusDouble.equalsIgnoreCase("Rejected"))
								{
									count = XMLhelpers.StagingStatus_Rejected(key, clmID2);
								}

								String ExpStatus = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",key, "Expected CLAIM STATUS IN STG");
								if(StagingStatusDouble.equalsIgnoreCase(ExpStatus))
								{
									System.out.println("The Correct staging status is displayed " + StagingStatusDouble);
									AssertMessage.add("The Correct staging status is displayed " + StagingStatusDouble);
									if(StagingStatusDouble.equalsIgnoreCase("Rejected"))
									{
										//int count = XMLhelpers.StagingStatus_Rejected(key);
										if (count == 0)
										{
											StagingProcess = true;
											System.out.println("The errors are matching with expected errors.");
											AssertMessage.add("The errors are matching with expected errors.");							
										}
										else 
										{
											AssertMessage.add("The errors are not matching with expected errors - Staging Process.");
										}
										Verify.verifyTrue(count == 0, "The errors are not matching with expected errors"+StagingStatus);
									}

									if(StagingStatusDouble.equalsIgnoreCase("Accepted"))
									{
										StagingProcess = true;
										System.out.println("The Staging status is  " + StagingStatusDouble);
										AssertMessage.add("The Staging status is  " + StagingStatusDouble);
									}
								}
								else
								{
									System.out.println("The expected Staging Status for duplicate message is not matching. The expected status is " + ExpStatus+". But Actual Status is "+StagingStatusDouble);
									AssertMessage.add("The expected Staging Status for duplicate message is not matching. The expected status is " + ExpStatus+". But Actual Status is "+StagingStatusDouble);
								}

								System.out.println("The Staging Process is Completed  " + StagingStatusDouble);
								AssertMessage.add("The Staging Process is Completed  " + StagingStatusDouble);					
							}
						}
						else 
						{
							System.out.println("The Response code are null after Duplicate response  " + ResponsecodeDouble);
							AssertMessage.add("The Response code are null after Duplicate response  " + ResponsecodeDouble);
						}

					}
					else 
					{
						System.out.println("The status of first message is not accepted  " + StagingStatus);
						AssertMessage.add("The status of first message is not accepted " + StagingStatus);
					}
				}
				else 
				{
					System.out.println("The Response code are null   " + Responsecode);
					AssertMessage.add("The Response code are null " + Responsecode);
				}
			}
			return new XMLhelpers(StagingProcess,AssertMessage);

		}
		public static XMLhelpers XMLProcess_StagingStatusVerification_PMS(String Responsecode, String key, String environment) throws InterruptedException
		{
			List<String> Message = new ArrayList<>(); 
			Boolean StagingProcess = false;
			if(Responsecode != null)
			{
				System.out.println("The Response code are " + Responsecode);
				Message.add("The Response code are " + Responsecode);
				helpers.CommonFunctions.haltExecution_XML();
				String StagingStatus = XMLhelpers.getStagingStatus(key, environment);
				utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", StagingStatus, key, "Actual CLAIM STATUS IN STG");
				String ExpStatus = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",key, "Expected CLAIM STATUS IN STG");
				if(StagingStatus != null && (StagingStatus.equalsIgnoreCase(ExpStatus)))
				{
					
					System.out.println("The Staging status is  " + StagingStatus);
					Message.add("The Staging status is  " + StagingStatus);
					
					
					if(StagingStatus.equalsIgnoreCase("Accepted"))
					{
						StagingProcess = true;
						System.out.println("The Staging status is  " + StagingStatus);
						Message.add("The Staging status is  " + StagingStatus);
					}
					if(StagingStatus.equalsIgnoreCase("Rejected"))
					{
						int count = XMLhelpers.StagingStatus_Rejected_PMS(key, environment);
						if (count == 0)
						{
							StagingProcess = true;
							System.out.println("The errors are matching with expected errors.");
							Message.add("The errors are matching with expected errors.");

						}
						else 
						{
							Message.add("The errors are not matching with expected errors - Staging Process.");
						}
						Verify.verifyTrue(count == 0, "The errors are not matching with expected errors"+StagingStatus);
					}
					
			
					System.out.println("The Staging Process is Completed  " + StagingStatus);
					Message.add("The Staging Process is Completed  " + StagingStatus);
					

				}
				else 
				{
					System.out.println("The Staging status is either NULL or Expected Status is not matching with Actual Status.   " + StagingStatus);
					Message.add("The Staging status is either NULL or Expected Status is not matching with Actual Status.   " + StagingStatus);
				}
			}
			return new XMLhelpers (StagingProcess, Message);
		}
		
	
		public static XMLhelpers XMLProcess_StagingStatusVerification(String Responsecode, String key, String environment) throws InterruptedException
		{
			List<String> Message = new ArrayList<>(); 
			Boolean StagingProcess = false;
			if(Responsecode != null)
			{
				System.out.println("The Response code are " + Responsecode);
				Message.add("The Response code are " + Responsecode);
				helpers.CommonFunctions.haltExecution_XML();
				String StagingStatus = XMLhelpers.getStagingStatus(key, environment);
				utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", StagingStatus, key, "Actual CLAIM STATUS IN STG");
				String ExpStatus = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",key, "Expected CLAIM STATUS IN STG");
				if(StagingStatus != null && (StagingStatus.equalsIgnoreCase(ExpStatus)))
				{
					
					System.out.println("The Staging status is  " + StagingStatus);
					Message.add("The Staging status is  " + StagingStatus);
					
					
					if(StagingStatus.equalsIgnoreCase("Accepted"))
					{
						StagingProcess = true;
						System.out.println("The Staging status is  " + StagingStatus);
						Message.add("The Staging status is  " + StagingStatus);
					}
					if(StagingStatus.equalsIgnoreCase("Rejected"))
					{
						int count = XMLhelpers.StagingStatus_Rejected(key, environment);
						if (count == 0)
						{
							StagingProcess = true;
							System.out.println("The errors are matching with expected errors.");
							Message.add("The errors are matching with expected errors.");

						}
						else 
						{
							Message.add("The errors are not matching with expected errors - Staging Process.");
						}
						Verify.verifyTrue(count == 0, "The errors are not matching with expected errors"+StagingStatus);
					}
					
			
					System.out.println("The Staging Process is Completed  " + StagingStatus);
					Message.add("The Staging Process is Completed  " + StagingStatus);
					

				}
				else 
				{
					System.out.println("The Staging status is either NULL or Expected Status is not matching with Actual Status.   " + StagingStatus);
					Message.add("The Staging status is either NULL or Expected Status is not matching with Actual Status.   " + StagingStatus);
				}
			}
			return new XMLhelpers (StagingProcess, Message);
		}
		
		public static void CopyExcelData(String SourceFile, String Sourcesheet , int Sourcecolumn,  String DestinartionFile , String Destinationsheet , String key ) throws InterruptedException
		{
			try
			{
				String FirstName_XML = ExcelUtilities.getKeyValueFromExcelWithPosition(SourceFile , Sourcesheet, "Firstname",Sourcecolumn);
				String Surname_XML = ExcelUtilities.getKeyValueFromExcelWithPosition(SourceFile, Sourcesheet, "Surname",Sourcecolumn);
				String PostalCode_XML = ExcelUtilities.getKeyValueFromExcelWithPosition(SourceFile, Sourcesheet, "AddressPostCode",Sourcecolumn);
				PostalCode_XML = PostalCode_XML.trim();
				String FinalPostalCode = PostalCode_XML.replaceAll("\\s+","");
				String NHSNumber_XML = ExcelUtilities.getKeyValueFromExcelWithPosition(SourceFile, Sourcesheet, "NHSNumber",Sourcecolumn);
		    	utilities.ExcelUtilities.setKeyValueByPosition(DestinartionFile, Destinationsheet, FirstName_XML, "First-Names", key);  
		    	utilities.ExcelUtilities.setKeyValueByPosition(DestinartionFile, Destinationsheet, Surname_XML, "Surname", key); 
		    	utilities.ExcelUtilities.setKeyValueByPosition(DestinartionFile, Destinationsheet, NHSNumber_XML, "NHSNumber", key); 
		  		utilities.ExcelUtilities.setKeyValueByPosition(DestinartionFile, Destinationsheet, FinalPostalCode, "PostCode", key); 
				String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
			}
			catch(Exception e)
			{
				System.out.println("The is issue while transferring portal data to XML excelsheet");
			}
			
		}
		
		public static void ChangeDateCRM (String ClaimsNumber, WebDriver driver)
		{
			try{
					
				CrmHome ObjCrmHome  = new CrmHome(driver);
				ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
				AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
				int colNum = 1;
				String primaryEntity = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectPrimaryEntity",colNum);
				String GroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",colNum);
				String FieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",colNum);
				String ConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",colNum);
				String ValueForFieldValue = ClaimsNumber;
				AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);
				boolean flag = ObjAdvancedFindResult.resultRecordFound();
				if (flag)
				{
					String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
					if(title!= null)
					{
						String clmStatus = ObjAdvancedFindResult.getDetailsFromResultRecordScreen();
						ObjAdvancedFindResult = ObjAdvancedFindResult.changedateclaims();
						Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
						if(AlertPresent)
						{
							ObjAdvancedFindResult.ClickSpaceBar();
						}					
						
					}
				}
				
				}
			catch(Exception e)
			{
				System.out.println("The change date is not happen on CRM " +e ); 
				
			}
		}
		
		public void ReadAllAttribute(String FileNames, String key , boolean evidence)  throws IOException, InterruptedException
		{
			
			try
			{
				List<String> AssertMessage = new ArrayList<String>();
				List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel("XMLFILEDATA.xlsx", "GetAllAttribute", 1);
				for(String AttributeName : AttributeNames )
				{
					if(!AttributeName.equalsIgnoreCase("Attribute"))
					{
						helpers.CommonFunctions.ReadXML_AllAttribute(FileNames,"GetAllAttribute", AttributeName,key);
					}
				}
			}
			catch(Exception e)
			{
				System.out.println("There is issue while copying XML data into Excel Sheet " + e); 
				
			}
		}
		
		
	public XMLhelpers UpdateMasterAttribute(String environment, String FileNames, String XMLType,String Key) 
	{
		int TagValueAttribute = 0;
		int count = 0;
		int UpdatedTagValue = 0;
		int ActualupdateAttr = 0;
		String UpdatedAttributeValue = null;
		boolean MasterAttributeupdate = false;
		List<String> AssertMessage = new ArrayList<String>();		
		XMLhelpers objXMLhelpersStg = new XMLhelpers(MasterAttributeupdate, AssertMessage);
			try{
			String MainAttributes = ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "MasterAttribute", XMLType, "TagLevel");
			List<String> MainAttribute = Arrays.asList(MainAttributes.split("\\s*,\\s*"));
			TagValueAttribute = MainAttribute.size();
			if(!MainAttributes.isEmpty() && MainAttributes != null)
			{
			for(String MainAttributeName : MainAttribute)
	            {
	            	System.out.println(MainAttributeName);
	            	String AttributeName = MainAttributeName;
	            	UpdatedTagValue =helpers.CommonFunctions.ReadXML_UpdatetagValue(environment, XMLType, FileNames,AttributeName, "XMLFILEDATA.xlsx","MasterAttribute", "TagLevelPath",TagValueAttribute,count);
	            	count = UpdatedTagValue;
	            }
			if(UpdatedTagValue== TagValueAttribute)
			{
				MasterAttributeupdate = true;
				System.out.println("The Master Attribute ( Tag Value ) updated Sucessfully " +UpdatedTagValue);
				AssertMessage.add("The Master Attribute ( Tag Value ) updated Sucessfully " +UpdatedTagValue);
			}
			}
			String XMLMainAttributes = ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "MasterAttribute", XMLType, "Attribute");
			List<String> XMLMainAttribute = Arrays.asList(XMLMainAttributes.split("\\s*,\\s*"));
			TagValueAttribute = XMLMainAttribute.size();
			if(!XMLMainAttributes.isEmpty() && XMLMainAttributes != null)
				{
				for(String Attr : XMLMainAttribute)
				{
					UpdatedAttributeValue = helpers.CommonFunctions.getUpdateAttributeValue(environment ,Attr, FileNames);
					helpers.CommonFunctions.updateMainAttribute(FileNames ,XMLType, Attr,UpdatedAttributeValue ,ActualupdateAttr,TagValueAttribute,Key );
					MasterAttributeupdate = true;
					System.out.println("The Main Attribute updated Sucessfully " +Attr);
					AssertMessage.add("The Master Attribute updated Sucessfully " +Attr);
				}
				}
			}
			catch(Exception e)
			{
				System.out.println("The Main Attribute on XML is not changed Properly");
			}
			return new XMLhelpers(MasterAttributeupdate, AssertMessage);
		}

	public XMLhelpers BULKXMLUpdateMasterAttribute(String MainFile, String environment, String FileNames, String XMLType,String Key) 
	{
		int TagValueAttribute = 0;
		int count = 0;
		int UpdatedTagValue = 0;
		int ActualupdateAttr = 0;
		String UpdatedAttributeValue = null;
		boolean MasterAttributeupdate = false;
		String TagPaths = null;
		List<String> AssertMessage = new ArrayList<String>();		
		XMLhelpers objXMLhelpersStg = new XMLhelpers(MasterAttributeupdate, AssertMessage);
			try{
				String MainAttributes = ExcelUtilities.getKeyValueByPosition(MainFile, "MasterAttribute", XMLType, "TagLevel");
				List<String> MainAttribute = Arrays.asList(MainAttributes.split("\\s*,\\s*"));
				TagValueAttribute = MainAttribute.size();
				if(!MainAttributes.isEmpty() && MainAttributes != null)
				{
					for(String MainAttributeName : MainAttribute)
					{
						System.out.println("Main Attribute: "+MainAttributeName);
						String AttributeName = MainAttributeName;
						UpdatedTagValue =helpers.CommonFunctions.BULKReadXML_UpdatetagValue(environment, XMLType, FileNames,AttributeName, MainFile,"MasterAttribute", "TagLevelPath",TagValueAttribute,count);
						AssertMessage.add("The attribute "+AttributeName+"has updated.");
						count = UpdatedTagValue;
					}
					if(UpdatedTagValue== TagValueAttribute)
					{
						MasterAttributeupdate = true;
						System.out.println("The "+UpdatedTagValue+" Master Attributes ( Tag Values ) updated Sucessfully.");
						AssertMessage.add("The "+UpdatedTagValue+" Master Attributes ( Tag Values ) updated Sucessfully.");
					}
				}
			
			String XMLMainAttributes = ExcelUtilities.getKeyValueByPosition(MainFile, "MasterAttribute", XMLType, "Attribute");
			List<String> XMLMainAttribute = Arrays.asList(XMLMainAttributes.split("\\s*,\\s*"));
			TagValueAttribute = XMLMainAttribute.size();
			if(!XMLMainAttributes.isEmpty() && XMLMainAttributes != null)
				{
				for(String Attr : XMLMainAttribute)
				{
					UpdatedAttributeValue = helpers.CommonFunctions.getUpdateAttributeValue(environment ,Attr, FileNames);
					helpers.CommonFunctions.BulkupdateMainAttribute(FileNames ,XMLType, Attr,UpdatedAttributeValue ,ActualupdateAttr,TagValueAttribute,Key,MainFile );
					MasterAttributeupdate = true;
					System.out.println("The Main Attribute updated Sucessfully " +Attr);
					AssertMessage.add("The Master Attribute updated Sucessfully " +Attr);
				}
				}
			}
			catch(Exception e)
			{
				System.out.println("The Main Attribute on XML is not changed Properly");
			}
			return new XMLhelpers(MasterAttributeupdate, AssertMessage);
		}
		
	
	public XMLhelpers BULKBSXMLProcess_GOS156(String XMLTYPE, String key , boolean evidence, String environment, String MainFile) throws IOException, InterruptedException, ParseException
	{
		boolean StagingProcess = false;
		String XMLValue = null;
		String XMLUniqueMsgID, XMLPCode, UpdatedMsgID, UpdatedPCode, remark, newremark,  ExpStatus;
		XMLUniqueMsgID =  XMLPCode =  UpdatedMsgID =  UpdatedPCode =  remark = newremark =  ExpStatus =   null;
		List<String> AssertMessage = new ArrayList<String>();
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		XMLUniqueMsgID = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "UNIQUE MSG ID");
		System.out.println(XMLUniqueMsgID);
		XMLPCode = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "P CODE");
		for (int i = 0;i< 5;i++)
		{
			UpdatedMsgID = CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLUniqueMsgID , i);
			XMLUniqueMsgID = UpdatedMsgID;
		}
		System.out.println("The Updated Unique Message id " + UpdatedMsgID);
		utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", UpdatedMsgID, key, "UNIQUE MSG ID");
		helpers.CommonFunctions.BULKReadXML_UpdateCodeValue(FileNames, "Claim-Message", "Unique-Message-ID", UpdatedMsgID, key, MainFile);
		if(XMLPCode.length()>0)
		{
			for(int i =0;i<5;i++)
			{
				UpdatedPCode = helpers.CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLPCode , i);
				XMLPCode = UpdatedPCode;
			}
			System.out.println("The updated P Code is "+UpdatedPCode );
			utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", UpdatedPCode, key, "P CODE");
			helpers.CommonFunctions.BULKReadXML_UpdateCodeValue(FileNames, "Claim-Message", "P-Code", UpdatedPCode, key, MainFile);
		}
		else
		{
			System.out.println("The PCode is blank");
			remark = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "REMARKS");
			newremark = remark+",The Pcode is blank";
			utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", newremark, key, "REMARKS"); 
		}
		List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel(MainFile, "UpdateTagValues", 1);
		for(String AttributeName : AttributeNames )
		{
			if(!AttributeName.equalsIgnoreCase("Attribute"))
			{
			XMLValue = ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
			if(XMLValue != null && !XMLValue.isEmpty())
				{
					helpers.CommonFunctions.BULKReadXML_UpdateCodeValue_Attribute(XMLTYPE, FileNames,"UpdateTagValues", AttributeName,key,MainFile);
				}
			}
		}
		System.out.println(FileNames);
		//String Responsecode = XMLhelpers.BULKGetResponsecodeBSXML(FileNames,key, environment);
		String Responsecode = helpers.PostXML.BULKgetResultCodeByPostXML(FileNames,key,environment,MainFile);
		if(Responsecode != null)
		{
			System.out.println("The Response code are " + Responsecode);
			AssertMessage.add("The Response code are " + Responsecode);
			//helpers.CommonFunctions.haltExecution_XML(); //Commented by Akshay to optimize execution time
			String postCode= ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "ResponseCodeFromPost");
			if(postCode.equalsIgnoreCase("200")){
				String StagingStatus = XMLhelpers.BULKgetStagingStatus(key, environment,MainFile);
				utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", StagingStatus, key, "Actual CLAIM STATUS IN STG");
				ExpStatus = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML",key, "Expected CLAIM STATUS IN STG");
				if(StagingStatus != null && (StagingStatus.equalsIgnoreCase(ExpStatus)))
				{
					
					utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", "PASS", key, "STGCLMSTATUSVERIFICATION");
					System.out.println("The Staging status is  " + StagingStatus);
					AssertMessage.add("The Staging status is  " + StagingStatus);
					
					
					if(StagingStatus.equalsIgnoreCase("Accepted"))
					{
						StagingProcess = true;
						
						System.out.println("The Staging status is  " + StagingStatus);
						AssertMessage.add("The Staging status is  " + StagingStatus);
					}
					if(StagingStatus.equalsIgnoreCase("Rejected"))
					{
						int count = XMLhelpers.StagingStatus_Rejected_PMS(key, environment,MainFile);
						if (count == 0)
						{
							utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", "PASS", key, "STGVALIDATIONERRVERIFICATION");
							StagingProcess = true;
							System.out.println("The errors are matching with expected errors.");
							AssertMessage.add("The errors are matching with expected errors.");

						}
						else 
						{
							utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", "FAIL", key, "STGVALIDATIONERRVERIFICATION");
							AssertMessage.add("The errors are not matching with expected errors - Staging Process.");
						}
						Verify.verifyTrue(count == 0, "The errors are not matching with expected errors"+StagingStatus);
					}
					
				/*	if(StagingStatus.equalsIgnoreCase(ExpStatus))
					{
						StagingProcess = true;
						System.out.println("The Correct staging status is displayed " + StagingStatus);
						AssertMessage.add("The Correct staging status is displayed " + StagingStatus);

					}	*/
					System.out.println("The Staging Process is Completed  " + StagingStatus);
					AssertMessage.add("The Staging Process is Completed  " + StagingStatus);
					//setAssertMessage("The Staging Process is Completed " + StagingStatus, 5);

				}
				else 
				{
					utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", "FAIL", key, "STGCLMSTATUSVERIFICATION");
					System.out.println("The Staging status is either NULL or Expected Status is not matching with Actual Status.   " + StagingStatus);
					AssertMessage.add("The Staging status is either NULL or Expected Status is not matching with Actual Status.   " + StagingStatus);
				}
			}
		}
		else 
		{
			System.out.println("The Response code are null   " + Responsecode);
			AssertMessage.add("The Response code are null " + Responsecode);
		}
		
		return new XMLhelpers(StagingProcess,AssertMessage);
	}
	
	public XMLhelpers BULKBSXMLProcess_GOS6(String XMLTYPE, String key , boolean evidence, String environment, String MainFile, String FileNames, String pvnReference) throws IOException, InterruptedException, ParseException
	{
		boolean StagingProcess = false;
		String XMLValue = null;
		String XMLUniqueMsgID, XMLPCode, UpdatedMsgID, UpdatedPCode, remark, newremark,  ExpStatus;
		XMLUniqueMsgID =  XMLPCode =  UpdatedMsgID =  UpdatedPCode =  remark = newremark =  ExpStatus =   null;
		List<String> AssertMessage = new ArrayList<String>();
		//String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		XMLUniqueMsgID = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "UNIQUE MSG ID");
		System.out.println(XMLUniqueMsgID);
		XMLPCode = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "P CODE");
		for (int i = 0;i< 5;i++)
		{
			UpdatedMsgID = CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLUniqueMsgID , i);
			XMLUniqueMsgID = UpdatedMsgID;
		}
		System.out.println("The Updated Unique Message id " + UpdatedMsgID);
		utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", UpdatedMsgID, key, "UNIQUE MSG ID");
		helpers.CommonFunctions.BULKReadXML_UpdateCodeValue(FileNames, "Claim-Message", "Unique-Message-ID", UpdatedMsgID, key, MainFile);
		if(XMLPCode.length()>0)
		{
			for(int i =0;i<5;i++)
			{
				UpdatedPCode = helpers.CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLPCode , i);
				XMLPCode = UpdatedPCode;
			}
			System.out.println("The updated P Code is "+UpdatedPCode );
			utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", UpdatedPCode, key, "P CODE");
			helpers.CommonFunctions.BULKReadXML_UpdateCodeValue(FileNames, "Claim-Message", "P-Code", UpdatedPCode, key, MainFile);
		}
		else
		{
			System.out.println("The PCode is blank");
			remark = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "REMARKS");
			newremark = remark+",The Pcode is blank";
			utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", newremark, key, "REMARKS"); 
		}
		List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel(MainFile, "UpdateTagValues", 1);
		for(String AttributeName : AttributeNames )
		{
			if(!AttributeName.equalsIgnoreCase("Attribute"))
			{
			XMLValue = ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
			if(XMLValue != null && !XMLValue.isEmpty())
				{
					helpers.CommonFunctions.BULKReadXML_UpdateCodeValue_Attribute(XMLTYPE, FileNames,"UpdateTagValues", AttributeName,key,MainFile);
				}
			}
		}
		System.out.println(FileNames);
		//String Responsecode = XMLhelpers.BULKGetResponsecodeBSXML(FileNames,key, environment);
		String Responsecode = helpers.PostXML.BULKgetResultCodeByPostXML(FileNames,key,environment,MainFile);
		if(Responsecode != null)
		{
			System.out.println("The Response code are " + Responsecode);
			AssertMessage.add("The Response code are " + Responsecode);
			//helpers.CommonFunctions.haltExecution_XML(); //Commented by Akshay to optimize execution time
			//String StagingStatus = XMLhelpers.BULKgetStagingStatus(key, environment,MainFile);
			String StagingStatus = XMLhelpers.getPVNStatus(MainFile,key, pvnReference, environment);
			utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", StagingStatus, key, "Actual CLAIM STATUS IN STG");
			ExpStatus = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML",key, "Expected CLAIM STATUS IN STG");
			if(StagingStatus != null && (StagingStatus.equalsIgnoreCase(ExpStatus)))
			{
				
				utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", "PASS", key, "STGCLMSTATUSVERIFICATION");
				System.out.println("The Staging status is  " + StagingStatus);
				AssertMessage.add("The Staging status is  " + StagingStatus);
				
				
				if(StagingStatus.equalsIgnoreCase("Accepted"))
				{
					StagingProcess = true;
					
					System.out.println("The Staging status is  " + StagingStatus);
					AssertMessage.add("The Staging status is  " + StagingStatus);
				}
				if(StagingStatus.equalsIgnoreCase("Rejected"))
				{
					int count = XMLhelpers.PVNStatus_Rejected(key, environment, MainFile);
					if (count == 0)
					{
						utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", "PASS", key, "STGVALIDATIONERRVERIFICATION");
						StagingProcess = true;
						System.out.println("The errors are matching with expected errors.");
						AssertMessage.add("The errors are matching with expected errors.");

					}
					else 
					{
						utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", "FAIL", key, "STGVALIDATIONERRVERIFICATION");
						AssertMessage.add("The errors are not matching with expected errors - Staging Process.");
					}
					Verify.verifyTrue(count == 0, "The errors are not matching with expected errors"+StagingStatus);
				}
				
			/*	if(StagingStatus.equalsIgnoreCase(ExpStatus))
				{
					StagingProcess = true;
					System.out.println("The Correct staging status is displayed " + StagingStatus);
					AssertMessage.add("The Correct staging status is displayed " + StagingStatus);

				}	*/
				System.out.println("The Staging Process is Completed  " + StagingStatus);
				AssertMessage.add("The Staging Process is Completed  " + StagingStatus);
				//setAssertMessage("The Staging Process is Completed " + StagingStatus, 5);

			}
			else 
			{
				utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", "FAIL", key, "STGCLMSTATUSVERIFICATION");
				System.out.println("The Staging status is either NULL or Expected Status is not matching with Actual Status.   " + StagingStatus);
				AssertMessage.add("The Staging status is either NULL or Expected Status is not matching with Actual Status.   " + StagingStatus);
			}
		}
		else 
		{
			System.out.println("The Response code are null   " + Responsecode);
			AssertMessage.add("The Response code are null " + Responsecode);
		}
		
		return new XMLhelpers(StagingProcess,AssertMessage);
	}
	
	public XMLhelpers BULKPMSXMLProcess_GOS6(String XMLTYPE, String key , boolean evidence, String environment, String MainFile, String FileNames, String pvnReference) throws IOException, InterruptedException, ParseException
	{
		boolean StagingProcess = false;
		String XMLValue = null;
		String XMLUniqueMsgID, XMLPCode, UpdatedMsgID, UpdatedPCode, remark, newremark,  ExpStatus;
		XMLUniqueMsgID =  XMLPCode =  UpdatedMsgID =  UpdatedPCode =  remark = newremark =  ExpStatus =   null;
		List<String> AssertMessage = new ArrayList<String>();
		//String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		XMLUniqueMsgID = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "UNIQUE MSG ID");
		System.out.println(XMLUniqueMsgID);
		//XMLPCode = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "P CODE");
		for (int i = 0;i< 5;i++)
		{
			UpdatedMsgID = CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLUniqueMsgID , i);
			XMLUniqueMsgID = UpdatedMsgID;
		}
		System.out.println("The Updated Unique Message id " + UpdatedMsgID);
		utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", UpdatedMsgID, key, "UNIQUE MSG ID");
		helpers.CommonFunctions.BULKReadXML_UpdateCodeValue(FileNames, "Claim-Message", "Unique-Message-ID", UpdatedMsgID, key, MainFile);
		/*if(XMLPCode.length()>0)
		{
			for(int i =0;i<5;i++)
			{
				UpdatedPCode = helpers.CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLPCode , i);
				XMLPCode = UpdatedPCode;
			}
			System.out.println("The updated P Code is "+UpdatedPCode );
			utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", UpdatedPCode, key, "P CODE");
			helpers.CommonFunctions.BULKReadXML_UpdateCodeValue(FileNames, "Claim-Message", "P-Code", UpdatedPCode, key, MainFile);
		}
		else
		{
			System.out.println("The PCode is blank");
			remark = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "REMARKS");
			newremark = remark+",The Pcode is blank";
			utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", newremark, key, "REMARKS"); 
		}*/
		List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel(MainFile, "UpdateTagValues", 1);
		for(String AttributeName : AttributeNames )
		{
			if(!AttributeName.equalsIgnoreCase("Attribute"))
			{
			XMLValue = ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
			if(XMLValue != null && !XMLValue.isEmpty())
				{
					helpers.CommonFunctions.BULKReadXML_UpdateCodeValue_Attribute(XMLTYPE, FileNames,"UpdateTagValues", AttributeName,key,MainFile);
				}
			}
		}
		System.out.println(FileNames);
		//String Responsecode = XMLhelpers.BULKGetResponsecodeBSXML(FileNames,key, environment);
		String Responsecode = helpers.PostXML.BULKgetResultCodeByPostXML(FileNames,key,environment,MainFile);
		if(Responsecode != null)
		{
			System.out.println("The Response code are " + Responsecode);
			AssertMessage.add("The Response code are " + Responsecode);
			//helpers.CommonFunctions.haltExecution_XML();//Commented by Akshay to optimize execution time
			
			String StagingStatus = XMLhelpers.BULKgetStagingStatus(key, environment, MainFile);
			utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", StagingStatus, key, "Actual CLAIM STATUS IN STG");
			
			
			//String StagingStatus = XMLhelpers.BULKgetStagingStatus(key, environment,MainFile);
			//String StagingStatus = XMLhelpers.getPVNStatus(MainFile,key, pvnReference, environment);
			//utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", StagingStatus, key, "Actual CLAIM STATUS IN STG");
			ExpStatus = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML",key, "Expected CLAIM STATUS IN STG");
			if(StagingStatus != null && (StagingStatus.equalsIgnoreCase(ExpStatus)))
			{
				
				utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", "PASS", key, "STGCLMSTATUSVERIFICATION");
				System.out.println("The Staging status is  " + StagingStatus);
				AssertMessage.add("The Staging status is  " + StagingStatus);
				
				
				if(StagingStatus.equalsIgnoreCase("Accepted"))
				{
					StagingProcess = true;
					
					System.out.println("The Staging status is  " + StagingStatus);
					AssertMessage.add("The Staging status is  " + StagingStatus);
				}
				if(StagingStatus.equalsIgnoreCase("Rejected"))
				{
					int count = XMLhelpers.PVNStatus_Rejected(key, environment, MainFile);
					if (count == 0)
					{
						utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", "PASS", key, "STGVALIDATIONERRVERIFICATION");
						StagingProcess = true;
						System.out.println("The errors are matching with expected errors.");
						AssertMessage.add("The errors are matching with expected errors.");

					}
					else 
					{
						utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", "FAIL", key, "STGVALIDATIONERRVERIFICATION");
						AssertMessage.add("The errors are not matching with expected errors - Staging Process.");
					}
					Verify.verifyTrue(count == 0, "The errors are not matching with expected errors"+StagingStatus);
				}
				
			/*	if(StagingStatus.equalsIgnoreCase(ExpStatus))
				{
					StagingProcess = true;
					System.out.println("The Correct staging status is displayed " + StagingStatus);
					AssertMessage.add("The Correct staging status is displayed " + StagingStatus);

				}	*/
				System.out.println("The Staging Process is Completed  " + StagingStatus);
				AssertMessage.add("The Staging Process is Completed  " + StagingStatus);
				//setAssertMessage("The Staging Process is Completed " + StagingStatus, 5);

			}
			else 
			{
				utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", "FAIL", key, "STGCLMSTATUSVERIFICATION");
				System.out.println("The Staging status is either NULL or Expected Status is not matching with Actual Status.   " + StagingStatus);
				AssertMessage.add("The Staging status is either NULL or Expected Status is not matching with Actual Status.   " + StagingStatus);
			}
		}
		else 
		{
			System.out.println("The Response code are null   " + Responsecode);
			AssertMessage.add("The Response code are null " + Responsecode);
		}
		
		return new XMLhelpers(StagingProcess,AssertMessage);
	}
	
	public XMLhelpers BULKXMLProcess_GOS156(String XMLTYPE, String key , boolean evidence, String environment, String MainFile) throws IOException, InterruptedException, ParseException
	{
		boolean StagingProcess = false;
		String XMLUniqueMsgID, XMLPCode, UpdatedMsgID, UpdatedPCode, remark, newremark,  ExpStatus, AttributeValue;
		XMLUniqueMsgID =  XMLPCode =  UpdatedMsgID =  UpdatedPCode =  remark = newremark =  ExpStatus = AttributeValue=  null;
		List<String> AssertMessage = new ArrayList<String>();
		
		XMLhelpers objXMLhelpersStg = new XMLhelpers(StagingProcess, AssertMessage);

		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		XMLUniqueMsgID = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "UNIQUE MSG ID");
		System.out.println(XMLUniqueMsgID);
		for (int i = 0;i< 5;i++)
		{
			UpdatedMsgID = CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLUniqueMsgID , i);
			XMLUniqueMsgID = UpdatedMsgID;
		}
		System.out.println("The Updated Unique Message id " + UpdatedMsgID);
		utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", UpdatedMsgID, key, "UNIQUE MSG ID");
		helpers.CommonFunctions.BULKReadXML_UpdateCodeValue(FileNames, "Claim-Message", "Unique-Message-ID", UpdatedMsgID, key, MainFile);
		List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel(MainFile, "UpdateTagValues", 1);
		for(String AttributeName : AttributeNames )
		{
			if(!AttributeName.equalsIgnoreCase("Attribute"))
			{
				AttributeValue = ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
			if(AttributeValue != null && !AttributeValue.isEmpty())
				{
					helpers.CommonFunctions.BULKReadXML_UpdateCodeValue_Attribute(XMLTYPE, FileNames,"UpdateTagValues", AttributeName,key,MainFile);
				}
			}
			
			//helpers.CommonFunctions.BULKReadXML_UpdateCodeValue_Attribute(XMLTYPE, FileNames,"UpdateTagValues", AttributeName,key, MainFile);
		}
		System.out.println(FileNames);
		//String Responsecode = XMLhelpers.BULKGetResponsecodeXML(FileNames,key, environment);
		String Responsecode = helpers.PostXML.BULKgetResultCodeByPostXML(FileNames,key, environment,MainFile);
		String postCode= ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "ResponseCodeFromPost");
		if(postCode.equalsIgnoreCase("200")){
			objXMLhelpersStg = BULKXMLProcess_StagingStatusVerification_PMS(Responsecode, key, environment, MainFile);
			StagingProcess = objXMLhelpersStg.Process;
			for (String msg: objXMLhelpersStg.AssetMessage)
			{
				AssertMessage.add(msg);
			}
		}
/*		objXMLhelpersStg = BULKXMLProcess_StagingStatusVerification_PMS(Responsecode, key, environment, MainFile);
		StagingProcess = objXMLhelpersStg.Process;
		for (String msg: objXMLhelpersStg.AssetMessage)
		{
			AssertMessage.add(msg);
		}*/

		return new XMLhelpers(StagingProcess, AssertMessage);
	}
	
	public static XMLhelpers BULKXMLProcess_StagingStatusVerification_PMS(String Responsecode, String key, String environment, String MainFile) throws InterruptedException
	{
		List<String> Message = new ArrayList<>(); 
		Boolean StagingProcess = false;
		if(Responsecode != null)
		{
			System.out.println("The Response code are " + Responsecode);
			Message.add("The Response code are " + Responsecode);
			//helpers.CommonFunctions.haltExecution_XML(); //Commented by Akshay to optimize script execution
			String StagingStatus = XMLhelpers.BULKgetStagingStatus(key, environment, MainFile);
			utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", StagingStatus, key, "Actual CLAIM STATUS IN STG");
			String ExpStatus = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML",key, "Expected CLAIM STATUS IN STG");
			if(StagingStatus != null && (StagingStatus.equalsIgnoreCase(ExpStatus)))
			{
				
				utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", "PASS", key, "STGCLMSTATUSVERIFICATION");
				System.out.println("The Staging status is  " + StagingStatus);
				Message.add("The Staging status is  " + StagingStatus);
				
				
				if(StagingStatus.equalsIgnoreCase("Accepted"))
				{
					StagingProcess = true;
				System.out.println("The Staging status is  " + StagingStatus);
				Message.add("The Staging status is  " + StagingStatus);
				}
				if(StagingStatus.equalsIgnoreCase("Rejected"))
				{
					int count = XMLhelpers.StagingStatus_Rejected_PMS(key, environment,MainFile);
					if (count == 0)
					{
						utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", "PASS", key, "STGVALIDATIONERRVERIFICATION");
						StagingProcess = true;
						System.out.println("The errors are matching with expected errors.");
						Message.add("The errors are matching with expected errors.");

					}
					else 
					{
						utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", "FAIL", key, "STGVALIDATIONERRVERIFICATION");
						Message.add("The errors are not matching with expected errors - Staging Process.");
					}
					Verify.verifyTrue(count == 0, "The errors are not matching with expected errors"+StagingStatus);
				}
				
		
				System.out.println("The Staging Process is Completed  " + StagingStatus);
				Message.add("The Staging Process is Completed  " + StagingStatus);
				

			}
			else 
			{
				utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", "FAIL", key, "STGCLMSTATUSVERIFICATION");
				System.out.println("The Staging status is either NULL or Expected Status is not matching with Actual Status.   " + StagingStatus);
				Message.add("The Staging status is either NULL or Expected Status is not matching with Actual Status.   " + StagingStatus);
			}
		}
		return new XMLhelpers (StagingProcess, Message);
	}
	
	
	public static String BULKGetResponsecodeXML(String FileName, String Key, String environment) {
		String Response = null;
		try{
			String XMLUniqueMsgID, XMLPCode, UpdatedMsgID, UpdatedPCode, remark, newremark, Status, ExpStatus, ClaimID;
			XMLUniqueMsgID =  XMLPCode =  UpdatedMsgID =  UpdatedPCode =  remark = newremark = Status = ExpStatus =  ClaimID = null;
			Boolean flag = false;	
			System.out.println(Key);
			XMLUniqueMsgID = utilities.ExcelUtilities.getKeyValueByPosition("BULKXMLFILEDATA.xlsx", "XML", Key, "UNIQUE MSG ID");
			System.out.println(XMLUniqueMsgID);
			XMLPCode = utilities.ExcelUtilities.getKeyValueByPosition("BULKXMLFILEDATA.xlsx", "XML", Key, "P CODE");
			for (int i = 0;i< 5;i++)
			{
				UpdatedMsgID = CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLUniqueMsgID , i);
				XMLUniqueMsgID = UpdatedMsgID;
			}
			System.out.println("The Updated Message ID: "+UpdatedMsgID);
			utilities.ExcelUtilities.setKeyValueByPosition("BULKXMLFILEDATA.xlsx", "XML", UpdatedMsgID, Key, "UNIQUE MSG ID");
			helpers.CommonFunctions.ReadXML_UpdateCodeValue(FileName, "Claim-Message", "Unique-Message-ID", UpdatedMsgID, Key);
			String responseCode = helpers.PostXML.getResultCodeByPostXML(FileName,Key, environment);
			System.out.println(responseCode);			
			Response = String.valueOf(responseCode);

		}
		catch(Exception e)
		{
			System.out.println("The Response code is not captured" + e);
		}

		return Response;
	}
	
	public static XMLhelpers BULKXMLProcess_VerifyONCRM(WebDriver driver, String key , boolean evidence, String MainFile) throws IOException, InterruptedException, AWTException
	{
		boolean CRMProcess = false;
		List<String> AssertMessage = new ArrayList<String>();
		String ClaimID = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML",key,"CLAIM ID");
		String ClaimStatusCRM = XMLhelpers.BULKGetClaimsCRM_BS(driver, key, MainFile);
		if(!ClaimStatusCRM.isEmpty() && ClaimStatusCRM != null)
		{
			String ClaimStatus = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML",key,"Expected CLAIM STATUS IN CRM");
			AdvancedFindResult ObjAdvancedFindResult = new AdvancedFindResult(driver);
			//String ClaimID = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML",key,"CLAIM ID");
			String ClaimStaCRM =  ObjAdvancedFindResult.getClaimCRM();
			utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", ClaimStaCRM, key , "Actual CLAIM STATUS IN CRM"); 
			AssertMessage.add("The Claims status on CRM is  " + ClaimStaCRM);
			if(evidence)
			{
				ObjAdvancedFindResult.screenshots_CRMXML(key + ClaimStaCRM+"CRMStatus");
			}
			boolean ClaimstatusOnCRM = ObjAdvancedFindResult.VerifyClaimsStatus(ClaimID,ClaimStatus);
			if(ClaimstatusOnCRM)
			{
				CRMProcess = true;
				utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", "PASS", key, "CRMCLMSTATUSVERIFICATION");
				System.out.println("The Correct Claims status is appered on CRM  " + ClaimStaCRM);
				AssertMessage.add("The Correct Claims status is appered on CRM " + ClaimStaCRM);

				boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);	
				if(PaymentLine)
				{						
					System.out.println("The Payment Line is generated " +ClaimID );
					AssertMessage.add("The Payment Line is generated" +ClaimID);
					if(evidence)
					{
						ObjAdvancedFindResult.screenshots_CRMXML(key + ClaimStaCRM+"PaymentLines");
					}
					Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
					if(AlertPresent)
					{
						ObjAdvancedFindResult.ClickSpaceBar();
					}
				}
				else
				{
					System.out.println("The Payment Line is not generated " +ClaimID );
					AssertMessage.add("The Payment Line is not generated" +ClaimID);
					Boolean AlertPresent = ObjAdvancedFindResult.isAlertPresent();
					if(AlertPresent)
					{
						ObjAdvancedFindResult.ClickSpaceBar();
					}
				}


				if(ClaimStatusCRM.equalsIgnoreCase("Rejected"))
				{
					ObjAdvancedFindResult.XMLCRM_Rejected(key ,ClaimID, evidence);
					int count = ObjAdvancedFindResult.verifyXMLerrorCRM(key);
					if(count == 0)
					{
						CRMProcess = true;
						utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", "PASS", key, "CRMVALIDATIONERRVERIFICATION");
						System.out.println("The correct CRM error is displayed for claim" +ClaimID );
						AssertMessage.add("The correct CRM error is displayed for claim" +ClaimID);
					}
					else 
					{
						CRMProcess = false;
						utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", "FAIL", key, "CRMVALIDATIONERRVERIFICATION");
						AssertMessage.add("The incorrect CRM error is displayed "+ClaimID);
					}
				}
			}
			else 
			{
				utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", "FAIL", key, "CRMCLMSTATUSVERIFICATION");
				AssertMessage.add("The inCorrect Claims status is appered on CRM. The claim staus appeared as:  " + ClaimStaCRM);
			}

		}
	
	
			else
			{
				System.out.println("No Claim record found in CRM against Claim: "+ClaimID );
				AssertMessage.add("No Claim record found in CRM against Claim: "+ClaimID);
			}


			return new XMLhelpers(CRMProcess,AssertMessage);
		}
	
	
	public static XMLhelpers BULKXMLProcess_Verification(String key , String ExpectedValue, String MainFile) throws IOException, InterruptedException, AWTException
	{
		boolean CRMProcess = false;
		List<String> AssertMessage = new ArrayList<String>();
		
		List<String> ExpectedItemList = ExcelUtilities.getCellValuesInExcel(MainFile, "ExpectedValues", 1);
		{
			
		}

			return new XMLhelpers(CRMProcess,AssertMessage);
		}

	public static String BULKGetClaimsCRM_BS(WebDriver driver,String Key, String FileName) {
		String ClaimstatusCRM = "";
		try{
			CrmHome ObjCrmHome  = new CrmHome(driver);
			ObjCrmHome = ObjCrmHome.clickOnInlineDialog();

			String ClaimID = utilities.ExcelUtilities.getKeyValueByPosition(FileName, "XML",Key,"CLAIM ID");
			String ClaimIDOnStge = utilities.ExcelUtilities.getKeyValueByPosition(FileName, "XML",Key,"Expected CLAIM STATUS IN STG");
			String ClaimStatus = utilities.ExcelUtilities.getKeyValueByPosition(FileName, "XML",Key,"Expected CLAIM STATUS IN CRM");
			String primaryEntity = utilities.ExcelUtilities.getKeyValueByPosition(FileName, "AdvancedSearch", "selectPrimaryEntity","ClaimID");
			String GroupTypeValue = utilities.ExcelUtilities.getKeyValueByPosition(FileName, "AdvancedSearch", "selectoptGroupType","ClaimID");
			String FieldValue = utilities.ExcelUtilities.getKeyValueByPosition(FileName, "AdvancedSearch", "selectField","ClaimID");
			String ConditionValue = utilities.ExcelUtilities.getKeyValueByPosition(FileName, "AdvancedSearch", "selectfilterCondition","ClaimID");
			/*	String ClaimID = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", Key,1);
			System.out.println(ClaimID);*/

			if(!(ClaimID.length()==0))
			{

				AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();		
				AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity, GroupTypeValue,FieldValue,ConditionValue, ClaimID);
				boolean flag = ObjAdvancedFindResult.resultRecordFound();
				if (flag)
				{
					String title = ObjAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
					if(title!= null)
					{
						System.out.println(ClaimID);
						ClaimstatusCRM = ObjAdvancedFindResult.getClaimCRM();
					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("The Claims status is not captured" +e );
		}
		return ClaimstatusCRM;
	}
	
	public static String BULKGetResponsecodeBSXML(String FileName, String Key, String environment, String MainFile) {
		String Response = null;
		try{
			String XMLUniqueMsgID, XMLPCode, UpdatedMsgID, UpdatedPCode, remark, newremark, Status, ExpStatus, ClaimID;
			XMLUniqueMsgID =  XMLPCode =  UpdatedMsgID =  UpdatedPCode =  remark = newremark = Status = ExpStatus =  ClaimID = null;
			Boolean flag = false;	
			System.out.println(Key);
			XMLUniqueMsgID = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML", Key, "UNIQUE MSG ID");
			System.out.println(XMLUniqueMsgID);
			XMLPCode = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML", Key, "P CODE");
		
			for (int i = 0;i< 5;i++)
			{
				UpdatedMsgID = CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLUniqueMsgID , i);
				XMLUniqueMsgID = UpdatedMsgID;
			}
			System.out.println(UpdatedMsgID);
			utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", UpdatedMsgID, Key, "UNIQUE MSG ID");
			helpers.CommonFunctions.ReadXML_UpdateCodeValue(FileName, "Claim-Message", "Unique-Message-ID", UpdatedMsgID, Key);
			System.out.println("The Pcode before "+XMLPCode);
			if(XMLPCode.length()>0)
			{
				for (int i = 0;i< 5;i++)
				{
					UpdatedPCode = helpers.CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLPCode,i);
					XMLPCode = UpdatedPCode;
				}
				System.out.println("The updated P Code is "+UpdatedPCode );
				utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", UpdatedPCode, Key, "P CODE");
				helpers.CommonFunctions.ReadXML_UpdateCodeValue(FileName, "Claim-Message", "P-Code", UpdatedPCode, Key);
			}
			else
			{
				System.out.println("The PCode is blank");
				remark = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML", Key, "REMARKS");
				newremark = remark+",The Pcode is blank";
				utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", newremark, Key, "REMARKS"); 
			}
			String responseCode = helpers.PostXML.BULKgetResultCodeByPostXML(FileName,Key,environment,MainFile);
			System.out.println(responseCode);			
			Response = String.valueOf(responseCode);

		}
		catch(Exception e)
		{
			System.out.println("The Response code is not captured" + e);
		}

		return Response;
	}
	
	public XMLhelpers BULKXMLProcess_VerifyExpected(String key, String ExpectedItem , String MainFile, String environment, String xmlType) throws IOException, InterruptedException, AWTException
	{
		//helpers.CommonFunctions.haltExecution_XML();
		if(!ExpectedItem.equalsIgnoreCase("STAGING STATUS")){
			Thread.sleep(60000);
		}
		boolean CRMProcess = false;
		String actItemValue = null;
		List<String> AssertMessage = new ArrayList<String>();
		String expectedItemValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", ExpectedItem, key);
		String ClaimID, query, CRMStatus, CRMAmountDue, CRMAmountDueFromDB, DBQuery,PVNRef  = null;
		//if(expectedItemValue!=null)
		//{
		
		switch (ExpectedItem.toUpperCase())
		{
		case "STAGING STATUS":
			actItemValue = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "Actual CLAIM STATUS IN STG");
			if(expectedItemValue.equalsIgnoreCase(actItemValue))
			{
				CRMProcess = true;
				System.out.println("The expected & actual "+ExpectedItem+" are matching. Expected Status: "+expectedItemValue+" Actual status: "+actItemValue);
				AssertMessage.add("The expected & actual "+ExpectedItem+" are matching. Expected Status: "+expectedItemValue+" Actual status: "+actItemValue);
			}
			else
			{
				AssertMessage.add("The expected & actual "+ExpectedItem+" are not matching. Expected Status: "+expectedItemValue+" Actual status: "+actItemValue);
				System.out.println("The expected & actual "+ExpectedItem+" are not matching. Expected Status: "+expectedItemValue+" Actual status: "+actItemValue);
			}
			
			break;
			
		case "CRM AMOUNT DUE":
			//actItemValue = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "Expected CLAIM STATUS IN CRM");
			ClaimID = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "CLAIM ID");
			query = "select oph_CalculatedClaimValue from oph_ophthalmicclaim where oph_UniqueClaimIdentifier='"+ClaimID+"'";
			System.out.println(query);
			/*if (xmlType.contains("GOS1"))
			{
				DBQuery = "select oph_ophthalmicServicerate from oph_opthalmicpaymentrateBase where oph_name like '%GOS1%';";
			}
			else if  (xmlType.contains("GOS4"))
			{
				DBQuery = "select oph_ophthalmicServicerate from oph_opthalmicpaymentrateBase where oph_name like '%GOS4%';";
			}
			else if  (xmlType.contains("GOS5"))
			{
				DBQuery = "select oph_ophthalmicServicerate from oph_opthalmicpaymentrateBase where oph_name like '%GOS5%';";
			}
			else if  (xmlType.contains("GOS6"))
			{
				DBQuery = "select oph_ophthalmicServicerate from oph_opthalmicpaymentrateBase where oph_name like '%GOS6%';";
			}
			else if  (xmlType.contains("PRT"))
			{
				DBQuery = "select oph_ophthalmicServicerate from oph_opthalmicpaymentrateBase where oph_name like '%PRT%';";
			}
			else if  (xmlType.contains("CET"))
			{
				DBQuery = "select oph_ophthalmicServicerate from oph_opthalmicpaymentrateBase where oph_name like '%CET%';";
			}
			
			CRMAmountDueFromDB = XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "OPH SERVICE RATE", DBQuery);
			ExcelUtilities.setKeyValueByPosition(MainFile, "ExpectedValues", CRMAmountDueFromDB, "CRM AMOUNT DUE", key);
			expectedItemValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", ExpectedItem, key);*/
			//String StagingStatus = XMLhelpers.BULKgetCRMStatus(key, environment, MainFile, ClaimID);
			CRMAmountDue = XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "CRM AMOUNT DUE", query);
			//CRMAmountDue = String.format("%.2f", CRMAmountDue);
			CRMAmountDue = CRMAmountDue.substring(0, (CRMAmountDue.length()-2));
			if(expectedItemValue.equalsIgnoreCase(CRMAmountDue))
			{
				CRMProcess = true;
				AssertMessage.add("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+CRMAmountDue);
				System.out.println("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+CRMAmountDue);;
			}
			else
			{
				AssertMessage.add("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+CRMAmountDue);
				System.out.println("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+CRMAmountDue);
			}
			
			break;	
			
		case "CRM STATUS":
			//actItemValue = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "Expected CLAIM STATUS IN CRM");
			ClaimID = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "CLAIM ID");
			
			
			query = "select oph_claimStatusName from oph_ophthalmicclaim where oph_UniqueClaimIdentifier='"+ClaimID+"'";
			System.out.println(query);
			
			//String StagingStatus = XMLhelpers.BULKgetCRMStatus(key, environment, MainFile, ClaimID);
			CRMStatus = XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "CRM STATUS", query);
			if(expectedItemValue.equalsIgnoreCase(CRMStatus))
			{
				CRMProcess = true;
				AssertMessage.add("The expected & actual "+ExpectedItem+" are matching. Expected Status: "+expectedItemValue+" Actual status: "+CRMStatus);
				System.out.println("The expected & actual "+ExpectedItem+" are matching. Expected Status: "+expectedItemValue+" Actual status: "+CRMStatus);;
			}
			else
			{
				AssertMessage.add("The expected & actual "+ExpectedItem+" are not matching. Expected Status: "+expectedItemValue+" Actual status: "+CRMStatus);
				System.out.println("The expected & actual "+ExpectedItem+" are not matching. Expected Status: "+expectedItemValue+" Actual status: "+CRMStatus);
			}
			
			break;
			
		case "CRM PAYMENTLINE":
			//actItemValue = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "Expected CLAIM STATUS IN CRM");
			ClaimID = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "CLAIM ID");
			
			
			query = "select oph_paymentLineName from oph_ophthalmicclaim where oph_UniqueClaimIdentifier='"+ClaimID+"'";
			System.out.println(query);
			
			//String StagingStatus = XMLhelpers.BULKgetCRMStatus(key, environment, MainFile, ClaimID);
			String PaymentLine = XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "CRM PAYMENTLINE", query);
			//if(expectedItemValue.equalsIgnoreCase(PaymentLine))
			if(PaymentLine!=null && !PaymentLine.isEmpty())
			{
				CRMProcess = true;
				AssertMessage.add("The payment line is generated. "+PaymentLine);
				System.out.println("The payment line is generated. "+PaymentLine);;
			}
			else
			{
				AssertMessage.add("The payment line is not generated. "+PaymentLine);
				System.out.println("The payment line is not generated. "+PaymentLine);
			}
			
			break;
			
		case "EXPECTED ERROR":
			//actItemValue = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "Expected CLAIM STATUS IN CRM");
			ClaimID = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "CLAIM ID");
			
			
			query = "select pcs_message from pcs_logBase where pcs_messageType like 'ConsolidatedError' and pcs_key= '"+ClaimID+"'";
			System.out.println(query);
			
			//String StagingStatus = XMLhelpers.BULKgetCRMStatus(key, environment, MainFile, ClaimID);
			CRMStatus = XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "Expected Error", query);
			if(expectedItemValue.equalsIgnoreCase(CRMStatus))
			{
				CRMProcess = true;
				AssertMessage.add("The expected & actual "+ExpectedItem+" are matching. Expected Status: "+expectedItemValue+" Actual status: "+CRMStatus);
				System.out.println("The expected & actual "+ExpectedItem+" are matching. Expected Status: "+expectedItemValue+" Actual status: "+CRMStatus);;
			}
			else
			{
				AssertMessage.add("The expected & actual "+ExpectedItem+" are not matching. Expected Status: "+expectedItemValue+" Actual status: "+CRMStatus);
				System.out.println("The expected & actual "+ExpectedItem+" are not matching. Expected Status: "+expectedItemValue+" Actual status: "+CRMStatus);
			}
			
			break;
			
		case "PVN STATUS":
			actItemValue = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "Actual CLAIM STATUS IN STG");
			if(expectedItemValue.equalsIgnoreCase(actItemValue))
			{
				CRMProcess = true;
				System.out.println("The expected & actual "+ExpectedItem+" are matching. Expected Status: "+expectedItemValue+" Actual status: "+actItemValue);
				AssertMessage.add("The expected & actual "+ExpectedItem+" are matching. Expected Status: "+expectedItemValue+" Actual status: "+actItemValue);
			}
			else
			{
				AssertMessage.add("The expected & actual "+ExpectedItem+" are not matching. Expected Status: "+expectedItemValue+" Actual status: "+actItemValue);
				System.out.println("The expected & actual "+ExpectedItem+" are not matching. Expected Status: "+expectedItemValue+" Actual status: "+actItemValue);
			}
			
			break;
			
		/*case "PVN CRM STATUS":
			//actItemValue = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "Expected CLAIM STATUS IN CRM");
			ClaimID = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "CLAIM ID");
			PVNRef = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "PVN REF");
			//query = "select oph_claimStatusName from oph_ophthalmicclaim where oph_UniqueClaimIdentifier='"+ClaimID+"'";
			query = "select oph_claimStatusName from oph_ophthalmicclaim where oph_gos6PreVisitNotificationName like '"+PVNRef+"'";
			System.out.println(query);
			//String StagingStatus = XMLhelpers.BULKgetCRMStatus(key, environment, MainFile, ClaimID);
			CRMStatus = XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "CRM STATUS", query);
			if(expectedItemValue.equalsIgnoreCase(CRMStatus))
			{
				CRMProcess = true;
				AssertMessage.add("The expected & actual "+ExpectedItem+" are matching. Expected Status: "+expectedItemValue+" Actual status: "+CRMStatus);
				System.out.println("The expected & actual "+ExpectedItem+" are matching. Expected Status: "+expectedItemValue+" Actual status: "+CRMStatus);;
			}
			else
			{
				AssertMessage.add("The expected & actual "+ExpectedItem+" are not matching. Expected Status: "+expectedItemValue+" Actual status: "+CRMStatus);
				System.out.println("The expected & actual "+ExpectedItem+" are not matching. Expected Status: "+expectedItemValue+" Actual status: "+CRMStatus);
			}
			
			break;
			
			
		case "CRMPVN AMOUNT DUE":
			//actItemValue = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "Expected CLAIM STATUS IN CRM");
			ClaimID = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "CLAIM ID");
			PVNRef = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "PVN REF");
			query = "select oph_CalculatedClaimValue from oph_ophthalmicclaim where oph_gos6PreVisitNotificationName like '"+PVNRef+"'";
			System.out.println(query);
			
			CRMAmountDue = XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "CRM AMOUNT DUE", query);
			if(expectedItemValue.equalsIgnoreCase(CRMAmountDue))
			{
				CRMProcess = true;
				AssertMessage.add("The expected & actual "+ExpectedItem+" are matching. Expected Status: "+expectedItemValue+" Actual status: "+CRMAmountDue);
				System.out.println("The expected & actual "+ExpectedItem+" are matching. Expected Status: "+expectedItemValue+" Actual status: "+CRMAmountDue);;
			}
			else
			{
				AssertMessage.add("The expected & actual "+ExpectedItem+" are not matching. Expected Status: "+expectedItemValue+" Actual status: "+CRMAmountDue);
				System.out.println("The expected & actual "+ExpectedItem+" are not matching. Expected Status: "+expectedItemValue+" Actual status: "+CRMAmountDue);
			}
			
			break;*/
		case "CONTRACTORNUMBER":
			//String contractorCode= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "CONTRACTORNUMBER", key);
			ClaimID = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "CLAIM ID");
			query = "select oph_ContractorNumber from oph_ophthalmicclaim where oph_UniqueClaimIdentifier='"+ClaimID+"'";
			System.out.println(query);
			String contractorNumber = XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "CONTRACTORNUMBER", query);
			if(expectedItemValue.equalsIgnoreCase(contractorNumber))
			{
				CRMProcess = true;
				AssertMessage.add("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+contractorNumber);
				System.out.println("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+contractorNumber);;
			}
			else
			{
				AssertMessage.add("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+contractorNumber);
				System.out.println("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+contractorNumber);
			}
			break;
		case "PERFORMERNUMBER":
			//String contractorCode= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "CONTRACTORNUMBER", key);
			ClaimID = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "CLAIM ID");
			query = "select oph_PerformerNumber from oph_ophthalmicclaim where oph_UniqueClaimIdentifier='"+ClaimID+"'";
			System.out.println(query);
			String performerNumber = XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "PERFORMERNUMBER", query);
			if(expectedItemValue.equalsIgnoreCase(performerNumber))
			{
				CRMProcess = true;
				AssertMessage.add("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+performerNumber);
				System.out.println("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+performerNumber);;
			}
			else
			{
				AssertMessage.add("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+performerNumber);
				System.out.println("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+performerNumber);
			}
			break;
		case "SUBMITTEDCLAIMVALUE":
			//actItemValue = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "Expected CLAIM STATUS IN CRM");
			ClaimID = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "CLAIM ID");
			query = "select oph_SubmittedClaimValue from oph_ophthalmicclaim where oph_UniqueClaimIdentifier='"+ClaimID+"'";
			System.out.println(query);
			/*if (xmlType.contains("GOS1"))
			{
				DBQuery = "select oph_ophthalmicServicerate from oph_opthalmicpaymentrateBase where oph_name like '%GOS1%';";
			}
			else if  (xmlType.contains("GOS4"))
			{
				DBQuery = "select oph_ophthalmicServicerate from oph_opthalmicpaymentrateBase where oph_name like '%GOS4%';";
			}
			else if  (xmlType.contains("GOS5"))
			{
				DBQuery = "select oph_ophthalmicServicerate from oph_opthalmicpaymentrateBase where oph_name like '%GOS5%';";
			}
			else if  (xmlType.contains("GOS6"))
			{
				DBQuery = "select oph_ophthalmicServicerate from oph_opthalmicpaymentrateBase where oph_name like '%GOS6%';";
			}
			else if  (xmlType.contains("PRT"))
			{
				DBQuery = "select oph_ophthalmicServicerate from oph_opthalmicpaymentrateBase where oph_name like '%PRT%';";
			}
			else if  (xmlType.contains("CET"))
			{
				DBQuery = "select oph_ophthalmicServicerate from oph_opthalmicpaymentrateBase where oph_name like '%CET%';";
			}*/
			
			/*CRMAmountDueFromDB = XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "OPH SERVICE RATE", DBQuery);
			ExcelUtilities.setKeyValueByPosition(MainFile, "ExpectedValues", CRMAmountDueFromDB, "CRM AMOUNT DUE", key);
			expectedItemValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", ExpectedItem, key);*/
			//String StagingStatus = XMLhelpers.BULKgetCRMStatus(key, environment, MainFile, ClaimID);
			CRMAmountDue = XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "SUBMITTEDCLAIMVALUE", query);
			//CRMAmountDue = String.format("%.2f", CRMAmountDue);
			CRMAmountDue = CRMAmountDue.substring(0, (CRMAmountDue.length()-2));
			if(expectedItemValue.equalsIgnoreCase(CRMAmountDue))
			{
				CRMProcess = true;
				AssertMessage.add("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+CRMAmountDue);
				System.out.println("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+CRMAmountDue);;
			}
			else
			{
				AssertMessage.add("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+CRMAmountDue);
				System.out.println("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+CRMAmountDue);
			}
			
			break;	
		case "CALCULATEDCLAIMVALUE":
			//actItemValue = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "Expected CLAIM STATUS IN CRM");
			ClaimID = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "CLAIM ID");
			query = "select oph_CalculatedClaimValue from oph_ophthalmicclaim where oph_UniqueClaimIdentifier='"+ClaimID+"'";
			System.out.println(query);
			/*if (xmlType.contains("GOS1"))
			{
				DBQuery = "select oph_ophthalmicServicerate from oph_opthalmicpaymentrateBase where oph_name like '%GOS1%';";
			}
			else if  (xmlType.contains("GOS4"))
			{
				DBQuery = "select oph_ophthalmicServicerate from oph_opthalmicpaymentrateBase where oph_name like '%GOS4%';";
			}
			else if  (xmlType.contains("GOS5"))
			{
				DBQuery = "select oph_ophthalmicServicerate from oph_opthalmicpaymentrateBase where oph_name like '%GOS5%';";
			}
			else if  (xmlType.contains("GOS6"))
			{
				DBQuery = "select oph_ophthalmicServicerate from oph_opthalmicpaymentrateBase where oph_name like '%GOS6%';";
			}
			else if  (xmlType.contains("PRT"))
			{
				DBQuery = "select oph_ophthalmicServicerate from oph_opthalmicpaymentrateBase where oph_name like '%PRT%';";
			}
			else if  (xmlType.contains("CET"))
			{
				DBQuery = "select oph_ophthalmicServicerate from oph_opthalmicpaymentrateBase where oph_name like '%CET%';";
			}*/
			
			/*CRMAmountDueFromDB = XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "OPH SERVICE RATE", DBQuery);
			ExcelUtilities.setKeyValueByPosition(MainFile, "ExpectedValues", CRMAmountDueFromDB, "CRM AMOUNT DUE", key);
			expectedItemValue = ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", ExpectedItem, key);*/
			//String StagingStatus = XMLhelpers.BULKgetCRMStatus(key, environment, MainFile, ClaimID);
			CRMAmountDue = XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "CALCULATEDCLAIMVALUE", query);
			//CRMAmountDue = String.format("%.2f", CRMAmountDue);
			CRMAmountDue = CRMAmountDue.substring(0, (CRMAmountDue.length()-2));
			if(expectedItemValue.equalsIgnoreCase(CRMAmountDue))
			{
				CRMProcess = true;
				AssertMessage.add("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+CRMAmountDue);
				System.out.println("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+CRMAmountDue);;
			}
			else
			{
				AssertMessage.add("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+CRMAmountDue);
				System.out.println("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+CRMAmountDue);
			}
			
			break;	
		case "CLAIMTYPE":
			//String contractorCode= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "CONTRACTORNUMBER", key);
			ClaimID = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "CLAIM ID");
			query = "select oph_ClaimTypeName from oph_ophthalmicclaim where oph_UniqueClaimIdentifier='"+ClaimID+"'";
			System.out.println(query);
			String claimType = XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "CLAIMTYPE", query);
			if(expectedItemValue.equalsIgnoreCase(claimType))
			{
				CRMProcess = true;
				AssertMessage.add("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+claimType);
				System.out.println("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+claimType);;
			}
			else
			{
				AssertMessage.add("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+claimType);
				System.out.println("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+claimType);
			}
			break;
		case "ICODE":
			//String contractorCode= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "CONTRACTORNUMBER", key);
			ClaimID = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "CLAIM ID");
			query = "select oph_ICode from oph_ophthalmicclaim where oph_UniqueClaimIdentifier='"+ClaimID+"'";
			System.out.println(query);
			String iCode = XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "ICODE", query);
			if(expectedItemValue.equalsIgnoreCase(iCode))
			{
				CRMProcess = true;
				AssertMessage.add("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+iCode);
				System.out.println("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+iCode);;
			}
			else
			{
				AssertMessage.add("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+iCode);
				System.out.println("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+iCode);
			}
			break;
		case "PCODE":
			//String contractorCode= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "CONTRACTORNUMBER", key);
			ClaimID = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "CLAIM ID");
			query = "select oph_PCode from oph_ophthalmicclaim where oph_UniqueClaimIdentifier='"+ClaimID+"'";
			System.out.println(query);
			String pCode = XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "PCODE", query);
			if(expectedItemValue.equalsIgnoreCase(pCode))
			{
				CRMProcess = true;
				AssertMessage.add("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+pCode);
				System.out.println("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+pCode);;
			}
			else
			{
				AssertMessage.add("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+pCode);
				System.out.println("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+pCode);
			}
			break;
		case "ORGANIZATIONCODE":
			//String contractorCode= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "CONTRACTORNUMBER", key);
			ClaimID = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "CLAIM ID");
			query = "select oph_OrganisationCode from oph_ophthalmicclaim where oph_UniqueClaimIdentifier='"+ClaimID+"'";
			System.out.println(query);
			String orgCode = XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "ORGANIZATIONCODE", query);
			if(expectedItemValue.equalsIgnoreCase(orgCode))
			{
				CRMProcess = true;
				AssertMessage.add("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+orgCode);
				System.out.println("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+orgCode);;
			}
			else
			{
				AssertMessage.add("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+orgCode);
				System.out.println("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+orgCode);
			}
			break;
		case "UNIQUEMESSAGEID":
			//String contractorCode= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "CONTRACTORNUMBER", key);
			ClaimID = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "CLAIM ID");
			query = "select oph_egosUniqueMessageID from oph_ophthalmicclaim where oph_UniqueClaimIdentifier='"+ClaimID+"'";
			System.out.println(query);
			String messageIDCode = XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "UNIQUEMESSAGEID", query);
			if(expectedItemValue.equalsIgnoreCase(messageIDCode))
			{
				CRMProcess = true;
				AssertMessage.add("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+messageIDCode);
				System.out.println("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+messageIDCode);;
			}
			else
			{
				AssertMessage.add("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+messageIDCode);
				System.out.println("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+messageIDCode);
			}
			break;
		case "CLAIMVERSIONID":
			//String contractorCode= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "CONTRACTORNUMBER", key);
			ClaimID = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "CLAIM ID");
			query = "select oph_ClaimFormVersionID from oph_ophthalmicclaim where oph_UniqueClaimIdentifier='"+ClaimID+"'";
			System.out.println(query);
			String versionID = XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "CLAIMVERSIONID", query);
			if(expectedItemValue.equalsIgnoreCase(versionID))
			{
				CRMProcess = true;
				AssertMessage.add("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+versionID);
				System.out.println("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+versionID);;
			}
			else
			{
				AssertMessage.add("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+versionID);
				System.out.println("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+versionID);
			}
			break;
		case "POINTOFSERVICEDATE":
			//String contractorCode= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "CONTRACTORNUMBER", key);
			ClaimID = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "CLAIM ID");
			query = "select cast(oph_PointofServiceDate as date) as oph_PointofServiceDate from oph_ophthalmicclaim where oph_UniqueClaimIdentifier='"+ClaimID+"'";
			System.out.println(query);
			actItemValue = XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "POINTOFSERVICEDATE", query);
			if(expectedItemValue.equalsIgnoreCase(actItemValue))
			{
				CRMProcess = true;
				AssertMessage.add("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);
				System.out.println("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);;
			}
			else
			{
				AssertMessage.add("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);
				System.out.println("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);
			}
			break;
		case "GOS3AUTHORISATIONCODE":
			//String contractorCode= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "CONTRACTORNUMBER", key);
			ClaimID = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "CLAIM ID");
			query = "select oph_gos3AuthorisationCode from oph_ophthalmicclaim where oph_UniqueClaimIdentifier='"+ClaimID+"'";
			System.out.println(query);
			actItemValue = XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "GOS3AUTHORISATIONCODE", query);
			if(expectedItemValue.equalsIgnoreCase(actItemValue))
			{
				CRMProcess = true;
				AssertMessage.add("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);
				System.out.println("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);;
			}
			else
			{
				AssertMessage.add("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);
				System.out.println("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);
			}
			break;
		case "GOS3VOUCHERCODE":
			//String contractorCode= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "CONTRACTORNUMBER", key);
			ClaimID = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "CLAIM ID");
			query = "select oph_gos3VoucherCode from oph_ophthalmicclaim where oph_UniqueClaimIdentifier='"+ClaimID+"'";
			System.out.println(query);
			actItemValue = XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "GOS3VOUCHERCODE", query);
			if(expectedItemValue.equalsIgnoreCase(actItemValue))
			{
				CRMProcess = true;
				AssertMessage.add("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);
				System.out.println("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);;
			}
			else
			{
				AssertMessage.add("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);
				System.out.println("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);
			}
			break;
		case "PRESRIGHTDISTSPH":
			//String contractorCode= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "CONTRACTORNUMBER", key);
			ClaimID = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "CLAIM ID");
			query = "select oph_ophthalmicclaimid from oph_ophthalmicclaim where oph_UniqueClaimIdentifier='"+ClaimID+"'";
			System.out.println(query);
			String ophClaimID= XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "OPHCLAIMID", query);
			query = "select oph_prescriptionRightDistanceSph from oph_sighttestoutcome WHERE oph_OphthalmicClaim='"+ophClaimID+"'";
			System.out.println(query);
			actItemValue = XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "PRESRIGHTDISTSPH", query);
			String[]actItemValueArray= actItemValue.split("\\.");
			if(actItemValueArray.length>1){
				if(!(actItemValueArray[1].length()>1)){
					String decimalValue= actItemValueArray[1]+"0";
					actItemValue= actItemValueArray[0]+"."+decimalValue;
				}
			}
			//actItemValue = actItemValue.substring(0, (actItemValue.length()-2));
			if(expectedItemValue.equalsIgnoreCase(actItemValue))
			{
				CRMProcess = true;
				AssertMessage.add("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);
				System.out.println("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);;
			}
			else
			{
				AssertMessage.add("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);
				System.out.println("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);
			}
			break;
		case "PRESRIGHTDISTCYL":
			//String contractorCode= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "CONTRACTORNUMBER", key);
			ClaimID = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "CLAIM ID");
			query = "select oph_ophthalmicclaimid from oph_ophthalmicclaim where oph_UniqueClaimIdentifier='"+ClaimID+"'";
			System.out.println(query);
			ophClaimID= XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "OPHCLAIMID", query);
			query = "select oph_prescriptionRightDistanceCyl from oph_sighttestoutcome WHERE oph_OphthalmicClaim='"+ophClaimID+"'";
			System.out.println(query);
			actItemValue = XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "PRESRIGHTDISTCYL", query);
			actItemValueArray= actItemValue.split("\\.");
			if(actItemValueArray.length>1){
				if(!(actItemValueArray[1].length()>1)){
					String decimalValue= actItemValueArray[1]+"0";
					actItemValue= actItemValueArray[0]+"."+decimalValue;
				}
			}
			//actItemValue = actItemValue.substring(0, (actItemValue.length()-2));
			if(expectedItemValue.equalsIgnoreCase(actItemValue))
			{
				CRMProcess = true;
				AssertMessage.add("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);
				System.out.println("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);;
			}
			else
			{
				AssertMessage.add("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);
				System.out.println("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);
			}
			break;
		case "PRESRIGHTNEARSPH":
			//String contractorCode= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "CONTRACTORNUMBER", key);
			ClaimID = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "CLAIM ID");
			query = "select oph_ophthalmicclaimid from oph_ophthalmicclaim where oph_UniqueClaimIdentifier='"+ClaimID+"'";
			System.out.println(query);
			ophClaimID= XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "OPHCLAIMID", query);
			query = "select oph_prescriptionRightNearSph from oph_sighttestoutcome WHERE oph_OphthalmicClaim='"+ophClaimID+"'";
			System.out.println(query);
			actItemValue = XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "PRESRIGHTNEARSPH", query);
			actItemValueArray= actItemValue.split("\\.");
			if(actItemValueArray.length>1){
				if(!(actItemValueArray[1].length()>1)){
					String decimalValue= actItemValueArray[1]+"0";
					actItemValue= actItemValueArray[0]+"."+decimalValue;
				}
			}
			//actItemValue = actItemValue.substring(0, (actItemValue.length()-2));
			if(expectedItemValue.equalsIgnoreCase(actItemValue))
			{
				CRMProcess = true;
				AssertMessage.add("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);
				System.out.println("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);;
			}
			else
			{
				AssertMessage.add("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);
				System.out.println("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);
			}
			break;
		case "PRESLEFTDISTSPH":
			//String contractorCode= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "CONTRACTORNUMBER", key);
			ClaimID = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "CLAIM ID");
			query = "select oph_ophthalmicclaimid from oph_ophthalmicclaim where oph_UniqueClaimIdentifier='"+ClaimID+"'";
			System.out.println(query);
			ophClaimID= XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "OPHCLAIMID", query);
			query = "select oph_prescriptionLeftDistanceSph from oph_sighttestoutcome WHERE oph_OphthalmicClaim='"+ophClaimID+"'";
			System.out.println(query);
			actItemValue = XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "PRESLEFTDISTSPH", query);
			actItemValueArray= actItemValue.split("\\.");
			if(actItemValueArray.length>1){
				if(!(actItemValueArray[1].length()>1)){
					String decimalValue= actItemValueArray[1]+"0";
					actItemValue= actItemValueArray[0]+"."+decimalValue;
				}
			}
			//actItemValue = actItemValue.substring(0, (actItemValue.length()-2));
			if(expectedItemValue.equalsIgnoreCase(actItemValue))
			{
				CRMProcess = true;
				AssertMessage.add("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);
				System.out.println("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);;
			}
			else
			{
				AssertMessage.add("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);
				System.out.println("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);
			}
			break;
		case "PRESLEFTDISTCYL":
			//String contractorCode= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "CONTRACTORNUMBER", key);
			ClaimID = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "CLAIM ID");
			query = "select oph_ophthalmicclaimid from oph_ophthalmicclaim where oph_UniqueClaimIdentifier='"+ClaimID+"'";
			System.out.println(query);
			ophClaimID= XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "OPHCLAIMID", query);
			query = "select oph_prescriptionLeftDistanceCyl from oph_sighttestoutcome WHERE oph_OphthalmicClaim='"+ophClaimID+"'";
			System.out.println(query);
			actItemValue = XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "PRESLEFTDISTCYL", query);
			actItemValueArray= actItemValue.split("\\.");
			if(actItemValueArray.length>1){
				if(!(actItemValueArray[1].length()>1)){
					String decimalValue= actItemValueArray[1]+"0";
					actItemValue= actItemValueArray[0]+"."+decimalValue;
				}
			}
			//actItemValue = actItemValue.substring(0, (actItemValue.length()-2));
			if(expectedItemValue.equalsIgnoreCase(actItemValue))
			{
				CRMProcess = true;
				AssertMessage.add("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);
				System.out.println("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);;
			}
			else
			{
				AssertMessage.add("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);
				System.out.println("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);
			}
			break;
		case "PRESLEFTNEARSPH":
			//String contractorCode= ExcelUtilities.getKeyValueByPosition(MainFile, "ExpectedValues", "CONTRACTORNUMBER", key);
			ClaimID = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "CLAIM ID");
			query = "select oph_ophthalmicclaimid from oph_ophthalmicclaim where oph_UniqueClaimIdentifier='"+ClaimID+"'";
			System.out.println(query);
			ophClaimID= XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "OPHCLAIMID", query);
			query = "select oph_prescriptionLeftNearSph from oph_sighttestoutcome WHERE oph_OphthalmicClaim='"+ophClaimID+"'";
			System.out.println(query);
			actItemValue = XMLhelpers.BULKgetValueFromCRMEntity(key, environment, MainFile, "PRESLEFTNEARSPH", query);
			actItemValueArray= actItemValue.split("\\.");
			if(actItemValueArray.length>1){
				if(!(actItemValueArray[1].length()>1)){
					String decimalValue= actItemValueArray[1]+"0";
					actItemValue= actItemValueArray[0]+"."+decimalValue;
				}
			}
			//actItemValue = actItemValue.substring(0, (actItemValue.length()-2));
			if(expectedItemValue.equalsIgnoreCase(actItemValue))
			{
				CRMProcess = true;
				AssertMessage.add("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);
				System.out.println("The expected & actual "+ExpectedItem+" are matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);;
			}
			else
			{
				AssertMessage.add("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);
				System.out.println("The expected & actual "+ExpectedItem+" are not matching. Expected : "+expectedItemValue+" Actual : "+actItemValue);
			}
			break;
			
		default :
			
			System.out.println("The expected Item Value does not appear");
			
		
			break;
		
			
		}

		//}	

		return new XMLhelpers(CRMProcess,AssertMessage);
		}
		
	public static XMLhelpers BSXMLProcess_GOS6(String MainFile,String FileNames, String XMLTYPE,String key , boolean evidence, String environment) throws IOException, InterruptedException, ParseException
	{
		boolean StagingProcess = false;
		String XMLValue = null;
		String XMLUniqueMsgID, XMLPCode, UpdatedMsgID, UpdatedPCode, remark, newremark,  ExpStatus;
		XMLUniqueMsgID =  XMLPCode =  UpdatedMsgID =  UpdatedPCode =  remark = newremark =  ExpStatus =   null;
		List<String> AssertMessage = new ArrayList<String>();
		//String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		XMLUniqueMsgID = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "UNIQUE MSG ID");
		System.out.println(XMLUniqueMsgID);
		XMLPCode = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "P CODE");
		for (int i = 0;i< 5;i++)
		{
			UpdatedMsgID = CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLUniqueMsgID , i);
			XMLUniqueMsgID = UpdatedMsgID;
		}
		System.out.println("The Updated Unique Message id " + UpdatedMsgID);
		utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", UpdatedMsgID, key, "UNIQUE MSG ID");
		helpers.CommonFunctions.BULKReadXML_UpdateCodeValue(FileNames, "Claim-Message", "Unique-Message-ID", UpdatedMsgID, key, MainFile);
		if(XMLPCode.length()>0)
		{
			for(int i =0;i<5;i++)
			{
				UpdatedPCode = helpers.CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLPCode , i);
				XMLPCode = UpdatedPCode;
			}
			System.out.println("The updated P Code is "+UpdatedPCode );
			utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", UpdatedPCode, key, "P CODE");
			helpers.CommonFunctions.BULKReadXML_UpdateCodeValue(FileNames, "Claim-Message", "P-Code", UpdatedPCode, key, MainFile);
		}
		else
		{
			System.out.println("The PCode is blank");
			remark = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "REMARKS");
			newremark = remark+",The Pcode is blank";
			utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", newremark, key, "REMARKS"); 
		}
		List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel(MainFile, "DuplicateAttribute", 1);
		for(String AttributeName : AttributeNames )
		{
			if(!AttributeName.equalsIgnoreCase("Attribute"))
			{
			XMLValue = ExcelUtilities.getKeyValueByPosition(MainFile, "DuplicateAttribute", AttributeName, key);
			if(XMLValue != null && !XMLValue.isEmpty())
				{
					helpers.CommonFunctions.ReadXML_UpdateCodeValue_Attribute(MainFile,XMLTYPE,FileNames,"DuplicateAttribute", AttributeName,key);
				}
			}
		}
		System.out.println(FileNames);
		
		//String Responsecode = XMLhelpers.GetResponsecodeBSXML(FileNames,key, environment);
		String Responsecode = helpers.PostXML.getResultCodeByPostXML(FileNames,key,environment);
		if(Responsecode != null)
		{
			System.out.println("The Response code are " + Responsecode);
			AssertMessage.add("The Response code are " + Responsecode);
			helpers.CommonFunctions.haltExecution_XML();
			String StagingStatus = XMLhelpers.getStagingStatus(key, environment);
			
			utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", StagingStatus, key, "Actual CLAIM STATUS IN STG");
			ExpStatus = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML",key, "Expected CLAIM STATUS IN STG");
			if(StagingStatus != null && (StagingStatus.equalsIgnoreCase(ExpStatus)))
			{
				
				System.out.println("The Staging status is  " + StagingStatus);
				AssertMessage.add("The Staging status is  " + StagingStatus);
				
				
				if(StagingStatus.equalsIgnoreCase("Accepted"))
				{
					StagingProcess = true;
					System.out.println("The Staging status is  " + StagingStatus);
					AssertMessage.add("The Staging status is  " + StagingStatus);
				}
				if(StagingStatus.equalsIgnoreCase("Rejected"))
				{
					int count = XMLhelpers.StagingStatus_Rejected(key, environment);
					if (count == 0)
					{
						StagingProcess = true;
						System.out.println("The errors are matching with expected errors.");
						AssertMessage.add("The errors are matching with expected errors.");

					}
					else 
					{
						AssertMessage.add("The errors are not matching with expected errors - Staging Process.");
					}
					Verify.verifyTrue(count == 0, "The errors are not matching with expected errors"+StagingStatus);
				}
				
			/*	if(StagingStatus.equalsIgnoreCase(ExpStatus))
				{
					StagingProcess = true;
					System.out.println("The Correct staging status is displayed " + StagingStatus);
					AssertMessage.add("The Correct staging status is displayed " + StagingStatus);

				}	*/
				System.out.println("The Staging Process is Completed  " + StagingStatus);
				AssertMessage.add("The Staging Process is Completed  " + StagingStatus);
				//setAssertMessage("The Staging Process is Completed " + StagingStatus, 5);

			}
			else 
			{
				System.out.println("The Staging status is either NULL or Expected Status is not matching with Actual Status.   " + StagingStatus);
				AssertMessage.add("The Staging status is either NULL or Expected Status is not matching with Actual Status.   " + StagingStatus);
			}
		}
		else 
		{
			System.out.println("The Response code are null   " + Responsecode);
			AssertMessage.add("The Response code are null " + Responsecode);
		}
		
		return new XMLhelpers(StagingProcess,AssertMessage);
	}
	
	public static String  getStagingStatusFromDataFile(String Key, String environment,String file)
	{
		String  Status =  null;
		try
		{

			Boolean flag = false;	
			String schemaName = "StagingDB";
			String dbEnvironment = "Staging";

			// Get Claim Status from Staging Table
			Status = helpers.DatabaseHelper.getStatusFromClaim_New(schemaName, dbEnvironment,Key, environment,file);
			System.out.println(Status);
		}
		catch(Exception e)
		{
			System.out.println("The Staging status is not captured" + e);
		}
		return Status;
	}
	
	public static int StagingStatus_Rejected_PMS(String Key, String environment,String MainFile) {
		String remark,newremark,  ExpStatus, ClaimID;
		remark = newremark =  ExpStatus =  ClaimID = null;
		String schemaName = "StagingDB";
		String dbEnvironment = "Staging";
		int Count = 0;
		boolean flag = false;
		try{

			List<String> Errors = helpers.DatabaseHelper.getErrorsFromClaim_PMS(schemaName, dbEnvironment,Key, environment,MainFile);
			String ActualErrorString = Errors.toString();
			List<String> ActualErrors = helpers.CommonFunctions.getactualerrors(ActualErrorString);
			String ActualError = ActualErrors.toString();

			String listString = String.join(", ", Errors);
			System.out.println(Errors);
			ActualError = ActualError.replaceAll("[\\[\\](){}]","");
			System.out.println("Actual Errors: "+ActualError);
			utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", ActualError, Key, "Actual Error Details IN STG");

			String ExpErrorMsg = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML", Key, "Expected Error Details IN STG");
			System.out.println("Exp Errors: "+ExpErrorMsg);
			System.out.println("Exp Error Length: "+ExpErrorMsg.length());


			List<String> ExpectedErrors = helpers.CommonFunctions.getactualerrors(ExpErrorMsg);

			if (ExpectedErrors == null && ActualErrors == null)
			{
				flag = true;
			}

			if (!flag)
			{
				Count =  helpers.CommonFunctions.VerifyErrorMessage(ExpectedErrors,ActualErrors,Key);	

			}
		}
		catch(Exception e)
		{
			System.out.println("The Error message is not captured" + e);

		}

		return Count;

	}
	public static void readAndSaveXMLAttributeValues(String key, String mainFile,String targetSheet,String referenceSheet,String childAttributeSheet) throws FileNotFoundException {
		String xmlFileName = ExcelUtilities.getKeyValueFromExcelWithPosition(mainFile, "XML", key,1);
		if(!xmlFileName.isEmpty()&&xmlFileName!=null){
			String []xmlFileNames= xmlFileName.split(",");
			if(xmlFileNames.length>1){
				xmlFileName= xmlFileNames[1].toString();
			}
			List<String> attributes= ExcelUtilities.getCellValuesByPosition(mainFile, referenceSheet, "Attribute");
			if(!attributes.isEmpty()){
				int attributeCount= attributes.size();
				List<String> ExpectedDataKeys= ExcelUtilities.getCellValuesByPosition(mainFile, referenceSheet, "Entity");
				List<String> tags= ExcelUtilities.getCellValuesByPosition(mainFile, referenceSheet, "Path");
				for (int i=0;i<attributeCount;i++) {
					String tagName= tags.get(i);
					String AttributeName= attributes.get(i);
					String expectedDataKey= ExpectedDataKeys.get(i);
					CommonFunctions.BULKReadXML_GetAndSaveValue_Attribute(xmlFileName, AttributeName, key, mainFile, referenceSheet,targetSheet,expectedDataKey,tagName);
				}
			}
		}
		readAndSaveXMLChildTagValues(key, mainFile, targetSheet, childAttributeSheet);
	}
	public static void readAndSaveXMLChildTagValues(String key, String mainFile, String targetSheet,String referenceSheet) {
		String xmlFileName = ExcelUtilities.getKeyValueFromExcelWithPosition(mainFile, "XML", key,1);
		if(!xmlFileName.isEmpty()&&xmlFileName!=null){
			String []xmlFileNames= xmlFileName.split(",");
			if(xmlFileNames.length>1){
				xmlFileName= xmlFileNames[1].toString();
			}
			List<String> attributes= ExcelUtilities.getCellValuesByPosition(mainFile, referenceSheet, "Attribute");
			if(!attributes.isEmpty()){
				int attributeCount= attributes.size();
				List<String> ExpectedDataKeys= ExcelUtilities.getCellValuesByPosition(mainFile, referenceSheet, "Entity");
				List<String> tagTravelPaths= ExcelUtilities.getCellValuesByPosition(mainFile, referenceSheet, "Path");
				for(int i=0;i<attributeCount;i++){
					String tagTravelPath= tagTravelPaths.get(i);
					String AttributeName= attributes.get(i);
					String expectedDataKey= ExpectedDataKeys.get(i);
					CommonFunctions.BULKReadXML_GetAndSaveValue_ChildTagAttribute(xmlFileName, AttributeName, key, mainFile, referenceSheet,targetSheet,expectedDataKey,tagTravelPath);
				}
			}
		}
	}
	
	public XMLhelpers BULKXMLProcess_GOS3(String MainFile, String XMLTYPE, String key , boolean evidence, String environment,String voucherFile) throws IOException, InterruptedException
	{
		boolean StagingProcess = false;
		boolean JobRun = false;
		String XMLValue = null;
		List<String> AssertMessage = new ArrayList<String>();		
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		XMLhelpers objXMLhelpersStg = new XMLhelpers(StagingProcess, AssertMessage);
		String[] filenameList = FileNames.split(",");
		System.out.println(filenameList[0]);
		String GetVocherCodefile = filenameList[0];
		List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel(voucherFile, "UpdateTagValues", 1);
		String voucherKey= key;//+"_VO"
		for(String AttributeName : AttributeNames )
		{
			if(!AttributeName.equalsIgnoreCase("Attribute"))
			{
				XMLValue = ExcelUtilities.getKeyValueByPosition(voucherFile, "UpdateTagValues", AttributeName, voucherKey);
				if(XMLValue != null && !XMLValue.isEmpty())
					{
						helpers.CommonFunctions.BULKReadXML_UpdateCodeValue_Attribute(XMLTYPE,filenameList[0],"UpdateTagValues", AttributeName,voucherKey,voucherFile);
					}
			}
		}
		System.out.println(filenameList[0]);
		String Responsecode = XMLhelpers.GetResponsecodeXML_GOS3(voucherFile,filenameList[0],key, environment,"Voucher-Only-Message");
		
		if(Responsecode != null)
		{
			String postCode= ExcelUtilities.getKeyValueByPosition(voucherFile, "XML", key, "ResponseCodeFromPost");
			if(postCode.equalsIgnoreCase("200")){
				System.out.println("The Response code are " + Responsecode);
				AssertMessage.add("The Response code is generated " + Responsecode);
				String VoucherCode = helpers.CommonFunctions.GetValueFormParagaraph(Responsecode, "Voucher-Code=");
				String AuthorisationCode = helpers.CommonFunctions.GetValueFormParagaraph(Responsecode, "Authorisation-Code=");
				System.out.println("The VoucherCode " + VoucherCode);
				System.out.println("The Authorization code " +AuthorisationCode); 
				if((VoucherCode!=null)&&(AuthorisationCode!=null)){
					ExcelUtilities.setKeyValueByPosition(voucherFile, "ExpectedValues", VoucherCode, "VOUCHER CODE", key);
					ExcelUtilities.setKeyValueByPosition(voucherFile, "ExpectedValues", AuthorisationCode, "AUTHORISATION CODE", key);
					ExcelUtilities.setKeyValueByPosition(MainFile, "UpdateTagValues",VoucherCode, "GOS3-Voucher-Code", key);
					ExcelUtilities.setKeyValueByPosition(MainFile, "UpdateTagValues",AuthorisationCode, "GOS3-Authorisation-Code", key);
					List<String> AttributeNamesFiles = ExcelUtilities.getCellValuesInExcel(MainFile, "UpdateTagValues", 1);
					for(String AttributeNamesFile : AttributeNamesFiles )
					{
						if(!AttributeNamesFile.equalsIgnoreCase("Attribute"))
						{
							XMLValue = ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeNamesFile, key);
							if(XMLValue != null && !XMLValue.isEmpty())
								{
								helpers.CommonFunctions.BULKReadXML_UpdateCodeValue_Attribute(XMLTYPE,filenameList[1],"UpdateTagValues", AttributeNamesFile,key,MainFile);
								}
						}
					}
					String ResponsecodeUpdate = XMLhelpers.GetResponsecodeXML_GOS3(MainFile,filenameList[1],key, environment);
					postCode= ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "ResponseCodeFromPost");
					if(postCode.equalsIgnoreCase("200")){
						objXMLhelpersStg = BULKXMLProcess_StagingStatusVerification_PMS(Responsecode, key, environment, MainFile);
						//XMLProcess_StagingStatusVerification(ResponsecodeUpdate, key, environment);
						StagingProcess = objXMLhelpersStg.Process;
						for (String msg: objXMLhelpersStg.AssetMessage)
						{
							AssertMessage.add(msg);
						}
					}
				}
			}

			Verify.verifyNotEquals(Responsecode != null, "The Incorrect Response code is "+Responsecode);
		}
		return new XMLhelpers(StagingProcess,AssertMessage);

	}
	
	public static String GetResponsecodeXML_GOS3(String MainFile, String FileName , String Key, String environment,String nodeName) {
		String Response = null;
		try{
			String XMLUniqueMsgID, XMLPCode, UpdatedMsgID, UpdatedPCode, remark, newremark, Status, ExpStatus, ClaimID;
			XMLUniqueMsgID =  XMLPCode =  UpdatedMsgID =  UpdatedPCode =  remark = newremark = Status = ExpStatus =  ClaimID = null;
			Boolean flag = false;	
			System.out.println(Key);

			System.out.println(FileName);
			XMLUniqueMsgID = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML", Key, "UNIQUE MSG ID");
			System.out.println(XMLUniqueMsgID);
			for (int i = 0;i< 5;i++)
			{
				UpdatedMsgID = CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLUniqueMsgID , i);
				XMLUniqueMsgID = UpdatedMsgID;
			}	
			System.out.println(UpdatedMsgID);
			utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", UpdatedMsgID, Key, "UNIQUE MSG ID");
			helpers.CommonFunctions.ReadXML_UpdateCodeValue(FileName, nodeName, "Unique-Message-ID", UpdatedMsgID, Key);
			String responseCode = helpers.PostXML.getResultCodeByPostXML(MainFile, FileName,Key, environment);
			System.out.println(responseCode);			
			Response = String.valueOf(responseCode);

		}
		catch(Exception e)
		{
			System.out.println("The Response code is not captured" + e);
		}

		return Response;
	}
	
	public XMLhelpers BULKXMLProcess_VerifyExpectedFromDatabase(String key, String mainFile, String environment, String entitiesToVerify) throws InterruptedException, SQLException {
		//Thread.sleep(60000); //Commented by Akshay to optimize execution time
		boolean CRMProcess = false;
		boolean flag= false;
		List<String> AssertMessage = new ArrayList<String>();
		List<String>actualValues= new ArrayList<String>();
		List<String>expectedValues= new ArrayList<String>();
		String ClaimID, query= null;
		
		String dbEnvironment = "CRMDB";
		String CRMDbColumns= ExcelUtilities.getKeyValueByPosition(mainFile, "ExpectedValues", "CRMDBCOLUMNS", key);
		String tableName= ExcelUtilities.getKeyValueByPosition(mainFile, "ExpectedValues", "CRMTABLENAME", key);
		ClaimID = ExcelUtilities.getKeyValueByPosition(mainFile, "XML", key, "CLAIM ID");
		
		if((!CRMDbColumns.isEmpty()&& CRMDbColumns!=null)&&(!tableName.isEmpty()&&tableName!=null)){
			String columnEquals = ExcelUtilities.getKeyValueByPosition(mainFile, "ExpectedValues", "COLUMNEQUALS", key);
			String[]columnArray= CRMDbColumns.split(",");
			List<String>columns= Arrays.asList(columnArray);
			StringBuilder buildquery = new StringBuilder();
			buildquery.append("select top 1 "+CRMDbColumns+" from "+tableName);
			buildquery.append(" where "+columnEquals+"="+"'"+ClaimID+"'");
			query= buildquery.toString();
			System.out.println("Query is: "+query);
			while(!flag){
				actualValues = DatabaseHelper.CreateCRMDataListForAListOfColumns(query,columns,dbEnvironment, environment);
				System.out.println("Actual values from CRM database: "+actualValues);
				if(!actualValues.isEmpty()&&actualValues!=null){
					flag=true;
				}
			}
			//actualValues = DatabaseHelper.CreateCRMDataListForAListOfColumns(query,columns,dbEnvironment, environment);
			actualValues= CommonFunctions.modifyNumericValuesForOPProcessing(actualValues);// this modifies the numeric values as per standard
		}
		//this will get all the expected values in a list
		List<String> entityList= Arrays.asList(entitiesToVerify.split(","));
		int entityCount= entityList.size();
		for(int i=0;i<entityCount;i++){
			String expectedValue= ExcelUtilities.getKeyValueByPosition(mainFile, "ExpectedValues", entityList.get(i), key);
			expectedValues.add(expectedValue);
		}
		//this will verify expected and actual values
		int verifiedCount=0;
		if(expectedValues.size()==actualValues.size()){
			for(int i=0;i<actualValues.size();i++){
				String expValue= expectedValues.get(i);
				String actualValue=  actualValues.get(i);
				String expectedEntity= entityList.get(i);
				if(expValue.equalsIgnoreCase(actualValue)){
					System.out.println("The expected & actual "+expectedEntity+" are matching. Expected value: "+expValue+" Actual value: "+actualValues.get(i));
					AssertMessage.add("The expected & actual "+expectedEntity+" are matching. Expected value: "+expValue+" Actual value: "+actualValues.get(i));
					verifiedCount++;
				}else{
					System.out.println("The expected & actual "+expectedEntity+" are not matching. Expected value: "+expValue+" Actual value: "+actualValues.get(i));
					AssertMessage.add("The expected & actual "+expectedEntity+" are not matching. Expected value: "+expValue+" Actual value: "+actualValues.get(i));
				}
			}
		}
		
		if(expectedValues.size()==verifiedCount){
			CRMProcess= true;
		}
		
		return new XMLhelpers(CRMProcess,AssertMessage);
	}
	
	public XMLhelpers BULKXMLProcess_VerifyExpectedFromDatabase(String key, String mainFile, String environment, String entitiesToVerify,String typeOfData) throws InterruptedException, SQLException {
		//Thread.sleep(60000); //Commented by Akshay to optimize execution time
		boolean CRMProcess = false;
		boolean flag= false;
		List<String> AssertMessage = new ArrayList<String>();
		List<String>actualValues= new ArrayList<String>();
		List<String>expectedValues= new ArrayList<String>();
		List<String>castValues= new ArrayList<String>();
		String ClaimID, query, value= null;
		
		String dbEnvironment = "CRMDB";
		switch(typeOfData.toUpperCase()){
			case "CLAIMDETAILS":
				String CRMDbColumns= ExcelUtilities.getKeyValueByPosition(mainFile, "ExpectedValues", "CRMDBCOLUMNS", key);
				String tableName= ExcelUtilities.getKeyValueByPosition(mainFile, "ExpectedValues", "CRMTABLENAME", key);
				ClaimID = ExcelUtilities.getKeyValueByPosition(mainFile, "XML", key, "CLAIM ID");
				
				if((!CRMDbColumns.isEmpty()&& CRMDbColumns!=null)&&(!tableName.isEmpty()&&tableName!=null)){
					String columnEquals = ExcelUtilities.getKeyValueByPosition(mainFile, "ExpectedValues", "COLUMNEQUALS", key);
					String columnsToCast= ExcelUtilities.getKeyValueByPosition(mainFile, "ExpectedValues", "CASTCOLUMNSFROMDB", key);
					String castType= ExcelUtilities.getKeyValueByPosition(mainFile, "ExpectedValues", "CASTTYPE", key);
					
					if((!columnsToCast.isEmpty()&&columnsToCast!=null)&&(!castType.isEmpty()&&castType!=null)){
						List<String> columnList= Arrays.asList(columnsToCast.split(","));
						int columnCount= columnList.size();
						List<String> castTypeList= Arrays.asList(castType.split(","));
						int castCount= castTypeList.size();
						List<String> tableList= Arrays.asList(tableName.split(","));
						List<String> columnEqualsList= Arrays.asList(columnEquals.split(","));
						//List<String> valueList= Arrays.asList(ClaimID.split(","));
						if(columnCount==castCount){
							for(int i=0;i<columnCount;i++){
								String columnToCast= columnList.get(i);
								castType= castTypeList.get(i);
								tableName= tableList.get(i);
								columnEquals= columnEqualsList.get(i);
								//ClaimID= valueList.get(i);
								while(!flag){
									value= DatabaseHelper.getValueByCastingDataFromColumn(columnToCast,castType,tableName,columnEquals,ClaimID,dbEnvironment, environment);
									System.out.println("Actual cast values from CRM database: "+value);
									if(value!=null){
										flag=true;
									}
								}
								//value= DatabaseHelper.getValueByCastingDataFromColumn(columnToCast,castType,tableName,columnEquals,ClaimID,dbEnvironment, environment);
								castValues.add(value);
							}
						}else{
							Assert.fail("Column count and cast count is not matching. Kindly check the data and execute script again.");
						}
					}
					
					String[]columnArray= CRMDbColumns.split(",");
					List<String>columns= Arrays.asList(columnArray);
					StringBuilder buildquery = new StringBuilder();
					buildquery.append("select top 1 "+CRMDbColumns+" from "+tableName);
					buildquery.append(" where "+columnEquals+"="+"'"+ClaimID+"'");
					query= buildquery.toString();
					System.out.println(query);
					flag= false;
					while(!flag){
						actualValues = DatabaseHelper.CreateCRMDataListForAListOfColumns(query,columns,dbEnvironment, environment);
						System.out.println("Actual claim values from CRM database: "+actualValues);
						if(!actualValues.isEmpty()&&actualValues!=null){
							flag=true;
						}
					}
					//actualValues = DatabaseHelper.CreateCRMDataListForAListOfColumns(query,columns,dbEnvironment, environment);
					System.out.println("Actual Values from database: "+actualValues);
					actualValues= CommonFunctions.modifyNumericValuesForOPProcessing(actualValues);
					System.out.println("Actual Values from database post number modification: "+actualValues);
					List<String> castColumnList= Arrays.asList(columnsToCast.split(","));
					actualValues= setCastValues(actualValues,castValues,columns,castColumnList);
					System.out.println("Actual Values from database post updating cast values: "+actualValues);
				}
				break;
			case "PRESCRIPTIONDETAILS":
				CRMDbColumns= ExcelUtilities.getKeyValueByPosition(mainFile, "ExpectedValues", "PRESCRMDBCOLUMNS", key);
				tableName= ExcelUtilities.getKeyValueByPosition(mainFile, "ExpectedValues", "PRESCRMTABLENAME", key);
				ClaimID = ExcelUtilities.getKeyValueByPosition(mainFile, "XML", key, "CLAIM ID");
				query = "select oph_ophthalmicclaimid from oph_ophthalmicclaim where oph_UniqueClaimIdentifier='"+ClaimID+"'";
				System.out.println(query);
				String ophClaimID= XMLhelpers.BULKgetValueFromCRMEntity(key, environment, mainFile, "OPHCLAIMID", query);
				
				if((!CRMDbColumns.isEmpty()&& CRMDbColumns!=null)&&(!tableName.isEmpty()&&tableName!=null)){
					String columnEquals = ExcelUtilities.getKeyValueByPosition(mainFile, "ExpectedValues", "CRMTABLENAME", key);
					String[]columnArray= CRMDbColumns.split(",");
					List<String>columns= Arrays.asList(columnArray);
					StringBuilder buildquery = new StringBuilder();
					buildquery.append("select top 1 "+CRMDbColumns+" from "+tableName);
					buildquery.append(" where "+columnEquals+"="+"'"+ophClaimID+"'");
					query= buildquery.toString();
					System.out.println(query);
					flag= false;
					while(!flag){
						actualValues = DatabaseHelper.CreateCRMDataListForAListOfColumns(query,columns,dbEnvironment, environment);
						System.out.println("Actual Prescription values from CRM database: "+actualValues);
						if(actualValues!=null){
							flag=true;
						}
					}
					//actualValues = DatabaseHelper.CreateCRMDataListForAListOfColumns(query,columns,dbEnvironment, environment);
					System.out.println("Actual Values from database: "+actualValues);
					actualValues= CommonFunctions.modifyNumericValuesForOPProcessing(actualValues);// this modifies the numeric values as per standard
					System.out.println("Actual Values from database post number modification: "+actualValues);
				}	
				break;
			default:
				Assert.fail("Type of data "+typeOfData+" is not found.");
				break;
		}
		
		//this will get all the expected values in a list
		List<String> entityList= Arrays.asList(entitiesToVerify.split(","));
		int entityCount= entityList.size();
		for(int i=0;i<entityCount;i++){
			String expectedValue= ExcelUtilities.getKeyValueByPosition(mainFile, "ExpectedValues", entityList.get(i), key);
			expectedValues.add(expectedValue);
		}
		//this will verify expected and actual values
		int verifiedCount=0;
		if(expectedValues.size()==actualValues.size()){
			for(int i=0;i<actualValues.size();i++){
				String expValue= expectedValues.get(i);
				String actualValue=  actualValues.get(i);
				String expectedEntity= entityList.get(i);
				if(expValue.equalsIgnoreCase(actualValue)){
					System.out.println("The expected & actual "+expectedEntity+" are matching. Expected value: "+expValue+" Actual value: "+actualValues.get(i));
					AssertMessage.add("The expected & actual "+expectedEntity+" are matching. Expected value: "+expValue+" Actual value: "+actualValues.get(i));
					verifiedCount++;
				}else{
					System.out.println("The expected & actual "+expectedEntity+" are not matching. Expected value: "+expValue+" Actual value: "+actualValues.get(i));
					AssertMessage.add("The expected & actual "+expectedEntity+" are not matching. Expected value: "+expValue+" Actual value: "+actualValues.get(i));
				}
			}
		}
		
		if(expectedValues.size()==verifiedCount){
			CRMProcess= true;
		}
		
		return new XMLhelpers(CRMProcess,AssertMessage);
	}
	private List<String> setCastValues(List<String> actualValues, List<String>castValues, List<String>columns, List<String> castColumnList) {
		int actualDataCount= actualValues.size();
		outerloop:
		for(int j=0;j<castColumnList.size();j++){
			String columnName_Cast= castColumnList.get(j);
			innerloop:
			for(int i=0;i<actualDataCount;i++){
				String columnName= columns.get(i);
				if(columnName.equalsIgnoreCase(columnName_Cast)){
					actualValues.set(i, castValues.get(j));
					break innerloop;
				}
			}
		}
		
		return actualValues;
	}

	public XMLhelpers BULKXMLProcess_GOS3(String MainFile, String XMLTYPE, String key , boolean evidence, String environment,String voucherFile,String XMLType_VOC, String voucherKey) throws IOException, InterruptedException
	{
		boolean StagingProcess = false;
		boolean JobRun = false;
		String XMLValue = null;
		List<String> AssertMessage = new ArrayList<String>();		
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		XMLhelpers objXMLhelpersStg = new XMLhelpers(StagingProcess, AssertMessage);
		String[] filenameList = FileNames.split(",");
		System.out.println(filenameList[0]);
		String GetVocherCodefile = filenameList[0];
		List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel(voucherFile, "UpdateTagValues", 1);
		//String voucherKey= key;//+"_VO"
		for(String AttributeName : AttributeNames )
		{
			if(!AttributeName.equalsIgnoreCase("Attribute"))
			{
				XMLValue = ExcelUtilities.getKeyValueByPosition(voucherFile, "UpdateTagValues", AttributeName, voucherKey);
				if(XMLValue != null && !XMLValue.isEmpty())
					{
						helpers.CommonFunctions.BULKReadXML_UpdateCodeValue_Attribute(XMLType_VOC,filenameList[0],"UpdateTagValues", AttributeName,voucherKey,voucherFile);
					}
			}
		}
		System.out.println(filenameList[0]);
		String Responsecode = XMLhelpers.GetResponsecodeXML_GOS3(voucherFile,filenameList[0],voucherKey, environment,"Voucher-Only-Message");
		//postcode condition is to be added here
		
		/*objXMLhelpersStg = BULKXMLProcess_VoucherStagingStatusVerification_PMS(Responsecode, key, environment, voucherFile);
		StagingProcess = objXMLhelpersStg.Process;
		for (String msg: objXMLhelpersStg.AssetMessage)
		{
			AssertMessage.add(msg);
		}*/
		
		if(Responsecode != null)
		{
			System.out.println("The Response code are " + Responsecode);
			AssertMessage.add("The Response code is generated " + Responsecode);
			String VoucherCode = helpers.CommonFunctions.GetValueFormParagaraph(Responsecode, "Voucher-Code=");
			String AuthorisationCode = helpers.CommonFunctions.GetValueFormParagaraph(Responsecode, "Authorisation-Code=");
			System.out.println("The VoucherCode " + VoucherCode);
			System.out.println("The Authorization code " +AuthorisationCode); 
			if((VoucherCode!=null)&&(AuthorisationCode!=null)){
				ExcelUtilities.setKeyValueByPosition(voucherFile, "ExpectedValues", VoucherCode, "VOUCHER CODE", voucherKey);
				ExcelUtilities.setKeyValueByPosition(voucherFile, "ExpectedValues", AuthorisationCode, "AUTHORISATION CODE", voucherKey);
				ExcelUtilities.setKeyValueByPosition(MainFile, "UpdateTagValues",VoucherCode, "GOS3-Voucher-Code", key);
				ExcelUtilities.setKeyValueByPosition(MainFile, "UpdateTagValues",AuthorisationCode, "GOS3-Authorisation-Code", key);
				List<String> AttributeNamesFiles = ExcelUtilities.getCellValuesInExcel(MainFile, "UpdateTagValues", 1);
				for(String AttributeNamesFile : AttributeNamesFiles )
				{
					if(!AttributeNamesFile.equalsIgnoreCase("Attribute"))
					{
						XMLValue = ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeNamesFile, key);
						if(XMLValue != null && !XMLValue.isEmpty())
							{
							helpers.CommonFunctions.BULKReadXML_UpdateCodeValue_Attribute(XMLTYPE,filenameList[1],"UpdateTagValues", AttributeNamesFile,key,MainFile);
							}
					}
				}
				String ResponsecodeUpdate = XMLhelpers.GetResponsecodeBSXML(MainFile,filenameList[1],key, environment);
				String postCode= ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "ResponseCodeFromPost");
				if(postCode.equalsIgnoreCase("200")){
					objXMLhelpersStg = BULKXMLProcess_StagingStatusVerification_PMS(Responsecode, key, environment, MainFile);
					StagingProcess = objXMLhelpersStg.Process;
					for (String msg: objXMLhelpersStg.AssetMessage)
					{
						AssertMessage.add(msg);
					}
				}
				Verify.verifyNotEquals(Responsecode != null, "The Incorrect Response code is "+Responsecode);
			}
		}
		return new XMLhelpers(StagingProcess,AssertMessage);

	}
	
	public static String GetResponsecodeBSXML(String mainFile, String FileName, String Key, String environment) {
		String Response = null;
		try{
			String XMLUniqueMsgID, XMLPCode, UpdatedMsgID, UpdatedPCode, remark, newremark, Status, ExpStatus, ClaimID;
			XMLUniqueMsgID =  XMLPCode =  UpdatedMsgID =  UpdatedPCode =  remark = newremark = Status = ExpStatus =  ClaimID = null;
			Boolean flag = false;	
			System.out.println(Key);
			XMLUniqueMsgID = utilities.ExcelUtilities.getKeyValueByPosition(mainFile, "XML", Key, "UNIQUE MSG ID");
			System.out.println(XMLUniqueMsgID);
			XMLPCode = utilities.ExcelUtilities.getKeyValueByPosition(mainFile, "XML", Key, "P CODE");
		
			for (int i = 0;i< 5;i++)
			{
				UpdatedMsgID = CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLUniqueMsgID , i);
				XMLUniqueMsgID = UpdatedMsgID;
			}
			System.out.println("The Updated  Unique Message id is " + UpdatedMsgID);
			utilities.ExcelUtilities.setKeyValueByPosition(mainFile, "XML", UpdatedMsgID, Key, "UNIQUE MSG ID");
			helpers.CommonFunctions.ReadXML_UpdateCodeValue(FileName, "Claim-Message", "Unique-Message-ID", UpdatedMsgID, Key);
		//	System.out.println("The Pcode before "+XMLPCode);
			if(XMLPCode.length()>0)
			{
				for (int i = 0;i< 5;i++)
				{
					UpdatedPCode = helpers.CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLPCode,i);
					XMLPCode = UpdatedPCode;
				}
				System.out.println("The updated P Code is "+UpdatedPCode );
				utilities.ExcelUtilities.setKeyValueByPosition(mainFile, "XML", UpdatedPCode, Key, "P CODE");
				helpers.CommonFunctions.ReadXML_UpdateCodeValue(FileName, "Claim-Message", "P-Code", UpdatedPCode, Key);
			}
			else
			{
				System.out.println("The PCode is blank");
				remark = utilities.ExcelUtilities.getKeyValueByPosition(mainFile, "XML", Key, "REMARKS");
				newremark = remark+",The Pcode is blank";
				utilities.ExcelUtilities.setKeyValueByPosition(mainFile, "XML", newremark, Key, "REMARKS"); 
			}
			String responseCode = helpers.PostXML.getResultCodeByPostXML(mainFile,FileName,Key,environment);
			System.out.println(responseCode);			
			Response = String.valueOf(responseCode);

		}
		catch(Exception e)
		{
			System.out.println("The Response code is not captured" + e);
		}

		return Response;
	}
	
	//Akshay S: This will get the claim number for desired unique claim identifier
	public static String getClaimNumber(String mainFile, String key, String environment, String dbEnvironment) throws InterruptedException, SQLException {
		String claimNumber="";
		String uniqueClaimID= ExcelUtilities.getKeyValueByPosition(mainFile, "XML", key, "CLAIM ID");
		StringBuilder buildquery = new StringBuilder();
		buildquery.append("select top 1 oph_name from oph_ophthalmicclaim");
		buildquery.append(" where oph_UniqueClaimIdentifier= "+"'"+uniqueClaimID+"'");
		String query= buildquery.toString();
		claimNumber= DatabaseHelper.getCRMValueFromQuery(query, "oph_name", dbEnvironment, environment);
		return claimNumber;
	}
	
	//Akshay S: This will remove the tags from xml file
	public static int removeTagfromXMLFile(String fileName,String tagName) throws ParserConfigurationException, SAXException, IOException, TransformerException {
		String projectPath = System.getProperty("user.dir");
		String xmlFileOutputPath = projectPath+"\\XML\\"+fileName;
		int countAfterDelete=0;
		NodeList nList=null;
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document sourceDocument = documentBuilder.parse(xmlFileOutputPath);
		if(sourceDocument != null)
		{
			nList = sourceDocument.getElementsByTagName(tagName);
			System.out.println("Node count is: "+nList.getLength());
			for(int i=0;i<nList.getLength();i++){
				Element element= (Element) nList.item(i);
				org.w3c.dom.Node parent = element.getParentNode();
			    parent.removeChild(element);
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer= transformerFactory.newTransformer();
			DOMSource source = new DOMSource(sourceDocument);
			StreamResult result=new StreamResult(new File(xmlFileOutputPath));
			transformer.transform(source, result);
			System.out.println("Tag has been deleted for a file: "+xmlFileOutputPath);
			
			countAfterDelete= sourceDocument.getElementsByTagName(tagName).getLength();
			System.out.println("Count after deleting all "+tagName+" tag is: "+countAfterDelete );
		}
		return countAfterDelete;
	}
	
	//Akshay S: This will copy the tag from one document to another document
	public static int copyTagsToOtherXMLFile(String destfile,String sourceFile,String tagName) throws ParserConfigurationException, SAXException, IOException, TransformerException {
		
		String projectPath = System.getProperty("user.dir");
		String xmlFileSourcePath = projectPath+"\\XML\\"+sourceFile;
		String xmlFileDestPath = projectPath+"\\XML\\"+destfile;
		
		NodeList nList=null;
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document sourceDocument = documentBuilder.parse(xmlFileSourcePath);
		Document destDocument = documentBuilder.parse(xmlFileDestPath);
		if(sourceDocument != null)
		{
			nList = sourceDocument.getElementsByTagName(tagName);
			System.out.println("Node count is: "+nList.getLength());
			Element element= (Element) nList.item(0);
			org.w3c.dom.Node copiedNode= (org.w3c.dom.Node) destDocument.importNode(element, true);
			destDocument.getDocumentElement().appendChild(copiedNode);
			System.out.println("Tag copied for file path: "+xmlFileDestPath);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer= transformerFactory.newTransformer();
			DOMSource source = new DOMSource(destDocument);
			StreamResult result=new StreamResult(new File(xmlFileDestPath));
			transformer.transform(source, result);
			System.out.println("Tag has been copied for a file: "+xmlFileSourcePath);
		}
		
		int copiedTagCount= destDocument.getElementsByTagName(tagName).getLength();
		System.out.println("Copy tag count for file: "+xmlFileDestPath+" is: "+copiedTagCount);
		return copiedTagCount;
	}
	
	//Akshay S: This will copy a new xml file and remove existing sample tag from it
	public static boolean copyAndRemoveSampleTagFromXML(String MainFile, String masterFileName,String DestinationFile , String key, String tagName) throws IOException, ParserConfigurationException, SAXException, TransformerException {
		boolean allTagsDeleted= false;
		CommonFunctions.copyFileDestinationFile(MainFile, masterFileName, DestinationFile, "OPT_GOS1_XML_VR_062");
		int tagCount= XMLhelpers.removeTagfromXMLFile(DestinationFile, "Claim-Message");
		if(tagCount==0){
			System.out.println("All tags have been deleted from file "+DestinationFile);
			allTagsDeleted= true;
		}else{
			System.out.println("All tags have not been deleted from file "+DestinationFile);
			allTagsDeleted= false;
		}
		return allTagsDeleted;
	}
	
	//This will update the xml file as per the test data given
	public static void BULKXMLProcess_GOS156_UpdateXML(String XMLTYPE, String key , boolean evidence, String environment, String MainFile) throws IOException, InterruptedException, ParseException
	{
		boolean StagingProcess = false;
		String XMLUniqueMsgID, XMLPCode, UpdatedMsgID, UpdatedPCode, remark, newremark,  ExpStatus, AttributeValue;
		XMLUniqueMsgID =  XMLPCode =  UpdatedMsgID =  UpdatedPCode =  remark = newremark =  ExpStatus = AttributeValue=  null;
		List<String> AssertMessage = new ArrayList<String>();
		
		XMLhelpers objXMLhelpersStg = new XMLhelpers(StagingProcess, AssertMessage);

		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		XMLUniqueMsgID = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "UNIQUE MSG ID");
		System.out.println(XMLUniqueMsgID);
		for (int i = 0;i< 5;i++)
		{
			UpdatedMsgID = CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLUniqueMsgID , i);
			XMLUniqueMsgID = UpdatedMsgID;
		}
		System.out.println("The Updated Unique Message id " + UpdatedMsgID);
		utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", UpdatedMsgID, key, "UNIQUE MSG ID");
		helpers.CommonFunctions.BULKReadXML_UpdateCodeValue(FileNames, "Claim-Message", "Unique-Message-ID", UpdatedMsgID, key, MainFile);
		List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel(MainFile, "UpdateTagValues", 1);
		for(String AttributeName : AttributeNames )
		{
			if(!AttributeName.equalsIgnoreCase("Attribute"))
			{
				AttributeValue = ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
			if(AttributeValue != null && !AttributeValue.isEmpty())
				{
					helpers.CommonFunctions.BULKReadXML_UpdateCodeValue_Attribute(XMLTYPE, FileNames,"UpdateTagValues", AttributeName,key,MainFile);
				}
			}
			
			//helpers.CommonFunctions.BULKReadXML_UpdateCodeValue_Attribute(XMLTYPE, FileNames,"UpdateTagValues", AttributeName,key, MainFile);
		}
		System.out.println(FileNames);
	}
	
	//Akshay S: This will post multiple messages to a web service
	public XMLhelpers BULKXMLProcess_GOS156_verifyStagingStatus(String key, String environment, String MainFile,String Responsecode) throws IOException, InterruptedException, ParseException
	{
		boolean StagingProcess = false;
		List<String> AssertMessage = new ArrayList<String>();
		
		XMLhelpers objXMLhelpersStg = new XMLhelpers(StagingProcess, AssertMessage);

		//String Responsecode = helpers.PostXML.BULKgetResultCodeByPostXML(fileName,key, environment,MainFile);
		
		objXMLhelpersStg = BULKXMLProcess_StagingStatusVerification_PMS(Responsecode, key, environment, MainFile);
		StagingProcess = objXMLhelpersStg.Process;
		for (String msg: objXMLhelpersStg.AssetMessage)
		{
			AssertMessage.add(msg);
		}

		return new XMLhelpers(StagingProcess, AssertMessage);
	}
	
	//Akshay S: This will update the BS xml file as per input data given
	public static void BULKBSXMLProcess_GOS156_UpdateXML(String XMLTYPE, String key , boolean evidence, String environment, String MainFile) throws IOException, InterruptedException, ParseException
	{
		boolean StagingProcess = false;
		String XMLValue = null;
		String XMLUniqueMsgID, XMLPCode, UpdatedMsgID, UpdatedPCode, remark, newremark,  ExpStatus;
		XMLUniqueMsgID =  XMLPCode =  UpdatedMsgID =  UpdatedPCode =  remark = newremark =  ExpStatus =   null;
		List<String> AssertMessage = new ArrayList<String>();
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition(MainFile, "XML", key,1);
		XMLUniqueMsgID = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "UNIQUE MSG ID");
		System.out.println(XMLUniqueMsgID);
		XMLPCode = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "P CODE");
		for (int i = 0;i< 5;i++)
		{
			UpdatedMsgID = CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLUniqueMsgID , i);
			XMLUniqueMsgID = UpdatedMsgID;
		}
		System.out.println("The Updated Unique Message id " + UpdatedMsgID);
		utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", UpdatedMsgID, key, "UNIQUE MSG ID");
		helpers.CommonFunctions.BULKReadXML_UpdateCodeValue(FileNames, "Claim-Message", "Unique-Message-ID", UpdatedMsgID, key, MainFile);
		if(XMLPCode.length()>0)
		{
			for(int i =0;i<5;i++)
			{
				UpdatedPCode = helpers.CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLPCode , i);
				XMLPCode = UpdatedPCode;
			}
			System.out.println("The updated P Code is "+UpdatedPCode );
			utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", UpdatedPCode, key, "P CODE");
			helpers.CommonFunctions.BULKReadXML_UpdateCodeValue(FileNames, "Claim-Message", "P-Code", UpdatedPCode, key, MainFile);
		}
		else
		{
			System.out.println("The PCode is blank");
			remark = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "REMARKS");
			newremark = remark+",The Pcode is blank";
			utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", newremark, key, "REMARKS"); 
		}
		List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel(MainFile, "UpdateTagValues", 1);
		for(String AttributeName : AttributeNames )
		{
			if(!AttributeName.equalsIgnoreCase("Attribute"))
			{
			XMLValue = ExcelUtilities.getKeyValueByPosition(MainFile, "UpdateTagValues", AttributeName, key);
			if(XMLValue != null && !XMLValue.isEmpty())
				{
					helpers.CommonFunctions.BULKReadXML_UpdateCodeValue_Attribute(XMLTYPE, FileNames,"UpdateTagValues", AttributeName,key,MainFile);
				}
			}
		}
		System.out.println(FileNames);
	}
	
	//Akshay S: This will post multiple BS messages to a web service
	public XMLhelpers BULKBSXMLProcess_GOS156_verifyStagingStatus(String key, String environment, String MainFile,String Responsecode) throws IOException, InterruptedException, ParseException
	{
		boolean StagingProcess = false;
		List<String> AssertMessage = new ArrayList<String>();
		
		XMLhelpers objXMLhelpersStg = new XMLhelpers(StagingProcess, AssertMessage);

		String StagingStatus = XMLhelpers.BULKgetStagingStatus(key, environment,MainFile);
		utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", StagingStatus, key, "Actual CLAIM STATUS IN STG");
		String ExpStatus = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML",key, "Expected CLAIM STATUS IN STG");
		if(StagingStatus != null && (StagingStatus.equalsIgnoreCase(ExpStatus)))
		{
			utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", "PASS", key, "STGCLMSTATUSVERIFICATION");
			System.out.println("The Staging status is  " + StagingStatus);
			AssertMessage.add("The Staging status is  " + StagingStatus);
			
			
			if(StagingStatus.equalsIgnoreCase("Accepted"))
			{
				StagingProcess = true;
				System.out.println("The Staging status is  " + StagingStatus);
				AssertMessage.add("The Staging status is  " + StagingStatus);
			}
			if(StagingStatus.equalsIgnoreCase("Rejected"))
			{
				int count = XMLhelpers.StagingStatus_Rejected_PMS(key, environment,MainFile);
				if (count == 0)
				{
					utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", "PASS", key, "STGVALIDATIONERRVERIFICATION");
					StagingProcess = true;
					System.out.println("The errors are matching with expected errors.");
					AssertMessage.add("The errors are matching with expected errors.");
				}
				else 
				{
					utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", "FAIL", key, "STGVALIDATIONERRVERIFICATION");
					AssertMessage.add("The errors are not matching with expected errors - Staging Process.");
				}
				Verify.verifyTrue(count == 0, "The errors are not matching with expected errors"+StagingStatus);
			}
			System.out.println("The Staging Process is Completed  " + StagingStatus);
			AssertMessage.add("The Staging Process is Completed  " + StagingStatus);
		}
		else 
		{
			utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", "FAIL", key, "STGCLMSTATUSVERIFICATION");
			System.out.println("The Staging status is either NULL or Expected Status is not matching with Actual Status.   " + StagingStatus);
			AssertMessage.add("The Staging status is either NULL or Expected Status is not matching with Actual Status.   " + StagingStatus);
		}
		return new XMLhelpers(StagingProcess, AssertMessage);
	}
	
}

