package pageobjects.XMLParseHelpers;
import java.awt.AWTException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import helpers.CommonFunctions;
import helpers.Screenshot;
import helpers.Support;
import pageobjects.AdvancedFilter;
import pageobjects.AdvancedFindResult;
import pageobjects.CrmHome;
import pageobjects.PerformerPL.ActivitiesQueues;
import utilities.ExcelUtilities;
import verify.Verify;

public final class BSXMLhelpers extends Support {
	WebDriver driver;
	WebDriverWait wait;	
	public boolean Process;
	public List<String> AssetMessage; 
	public String PVNReference;
	
		public BSXMLhelpers(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}
	 public BSXMLhelpers(boolean Process , List<String> AssetMessage )
	 {
		 this.Process = Process;
		 this.AssetMessage = AssetMessage;
		 
	 }
	 
	 public BSXMLhelpers(boolean Process , List<String> AssetMessage, String PVNReference )
	 {
		 this.Process = Process;
		 this.AssetMessage = AssetMessage;
		 this.PVNReference = PVNReference;
		 
	 }
	
	public static String GetResponsecodeBSXML(String FileName, String Key, String environment) {
		String Response = null;
		try{
			String XMLUniqueMsgID, XMLPCode, UpdatedMsgID, UpdatedPCode, remark, newremark, Status, ExpStatus, ClaimID;
			FileName = XMLUniqueMsgID =  XMLPCode =  UpdatedMsgID =  UpdatedPCode =  remark = newremark = Status = ExpStatus =  ClaimID = null;
			Boolean flag = false;	
			System.out.println(Key);
		
	/*	FileName = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", Key,1);
		System.out.println(FileName);*/
		XMLUniqueMsgID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", Key, "UNIQUE MSG ID");
		System.out.println(XMLUniqueMsgID);
		XMLPCode = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", Key, "P CODE");
		System.out.println(XMLPCode);
		UpdatedMsgID = helpers.CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLUniqueMsgID);
		System.out.println(UpdatedMsgID);
		utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", UpdatedMsgID, Key, "UNIQUE MSG ID");
		helpers.CommonFunctions.ReadXML_UpdateCodeValue(FileName, "Claim-Message", "Unique-Message-ID", UpdatedMsgID, Key);
		if(XMLPCode.length()>0)
		{
		UpdatedPCode = helpers.CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLPCode);
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
	
	public static String GetResponsecodeXML(String Key, String environment) {
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
	
	public static String GetResponsecodeXML(String FileName , String Key, String environment) {
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
		UpdatedMsgID = helpers.CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLUniqueMsgID);		
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
	
	public static String GetClaimsCRM_BS(WebDriver driver,String Key) {
		String ClaimstatusCRM = null;
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
	

	public static BSXMLhelpers XMLProcess_GOS156(String key , boolean evidence, String environment) throws IOException
	{
		boolean StagingProcess = false;
		boolean JobRun = false;
		List<String> AssertMessage = new ArrayList<String>();
		
		String FileNames = ExcelUtilities.getKeyValueFromExcelWithPosition("XMLFILEDATA.xlsx", "XML", key,1);
	
		List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "UpdatedAttribute", 1);
		for(String AttributeName : AttributeNames )
		{
	//	 helpers.CommonFunctions.ReadXML_UpdateCodeValue_Attribute(FileNames,"UpdatedAttribute", AttributeName,key);
		}
		System.out.println(FileNames);
		String Responsecode = BSXMLhelpers.GetResponsecodeXML(FileNames,key);
		if(Responsecode != null)
		{
			System.out.println("The Response code are " + Responsecode);
			AssertMessage.add("The Response code are " + Responsecode);
			String StagingStatus = BSXMLhelpers.getStagingStatus(key, environment);
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
						int count = BSXMLhelpers.StagingStatus_Rejected(key,environment);
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
		}
		return new BSXMLhelpers(StagingProcess,AssertMessage);
	}
	
	public static BSXMLhelpers XMLProcess_GOS156_CRM(WebDriver driver, String key , boolean evidence) throws IOException, InterruptedException, AWTException

	
	{
		boolean CRMProcess = false;
		List<String> AssertMessage = new ArrayList<String>();
		String ClaimStatusCRM = BSXMLhelpers.GetClaimsCRM_BS(driver, key);
		String ClaimStatus = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",key,"Expected CLAIM STATUS IN CRM");
		AdvancedFindResult ObjAdvancedFindResult = new AdvancedFindResult(driver);
		String ClaimID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",key,"CLAIM ID");
		String ClaimStaCRM =  ObjAdvancedFindResult.getClaimCRM();
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
		}
		else 
		{
			AssertMessage.add("The inCorrect Claims status is appered on CRM " + ClaimStaCRM);
		}
		if(evidence)
		{
			ObjAdvancedFindResult.screenshots_CRMXML(key + ClaimStaCRM+"PaymentLines");
		}
		boolean PaymentLine = ObjAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);	
		if(PaymentLine)
		{						
			System.out.println("The Payment Line is generated " +ClaimID );
			AssertMessage.add("The Payment Line is generated" +ClaimID);
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
				System.out.println("The correct CRM error is displayed " +ClaimID );
				AssertMessage.add("The correct CRM error is displayed " +ClaimID);
			}
			else 
			{
			AssertMessage.add("The incorrect CRM error is displayed "+ClaimID);
			}
	}
	else
	{
		System.out.println("The CRM claims status is  " +ClaimStatusCRM );
		AssertMessage.add("The CRM claims status is " +ClaimStatusCRM);
	
	}
	return new BSXMLhelpers(CRMProcess,AssertMessage);
}
	
	public static BSXMLhelpers XMLProcess_GOS6_PVNReference (String key, String MainFile, String environment) throws ParseException, IOException
	{
		boolean Process = false;
		List<String> AssertMessage = new ArrayList<String>();
		//AssertMessage = null;
		String PVNReference;
				
		String XMLUniqueMsgID, XMLPCode, UpdatedMsgID, UpdatedPCode, remark, newremark,  ExpStatus, XMLValue, XMLTYPE;
		XMLUniqueMsgID =  XMLPCode =  UpdatedMsgID =  UpdatedPCode =  remark = newremark =  ExpStatus =  null;
		
		String outputFileName = ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "FILE NAME");
		
		String[] outputFileNames = outputFileName.split(",");
		String FileNames = outputFileNames[0];
		
		XMLTYPE = CommonFunctions.getXMLType(FileNames);
		XMLUniqueMsgID = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "UNIQUE MSG ID");
		System.out.println(XMLUniqueMsgID);
		/*XMLPCode = utilities.ExcelUtilities.getKeyValueByPosition(MainFile, "XML", key, "P CODE");
		System.out.println(XMLPCode);*/
		for (int i = 0;i< 5;i++)
		{
			UpdatedMsgID = CommonFunctions.GenerateUniqueAplphaNumeric_XML(XMLUniqueMsgID , i);
			XMLUniqueMsgID = UpdatedMsgID;
		}
		System.out.println("The Updated Unique Message id " + UpdatedMsgID);
		utilities.ExcelUtilities.setKeyValueByPosition(MainFile, "XML", UpdatedMsgID, key, "UNIQUE MSG ID");
		helpers.CommonFunctions.BULKReadXML_UpdateCodeValue(FileNames, "Claim-Message", "Unique-Message-ID", UpdatedMsgID, key, MainFile);
	/*	if(XMLPCode.length()>0)
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
		List<String> AttributeNames = ExcelUtilities.getCellValuesInExcel(MainFile, "PVNTagValues", 1);
		for(String AttributeName : AttributeNames )
		{
			if(!AttributeName.equalsIgnoreCase("Attribute"))
			{
			XMLValue = ExcelUtilities.getKeyValueByPosition(MainFile, "PVNTagValues", AttributeName, FileNames);
			if(XMLValue != null && !XMLValue.isEmpty())
				{
					helpers.CommonFunctions.BULKReadXML_UpdateCodeValue_Attribute(MainFile,XMLTYPE,FileNames,"PVNTagValues", AttributeName,key);
				}
			}
		}
		System.out.println(FileNames);
		
		//String Responsecode = XMLhelpers.GetResponsecodeBSXML(FileNames,key, environment);
		String Responsecode = helpers.PostXML.getResultCodeByPostXML(FileNames,key,environment);
		
		
		
		return new BSXMLhelpers(Process, AssertMessage, Responsecode);
	}
}
