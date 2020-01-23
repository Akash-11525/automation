package helpers;

import java.awt.AWTException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import pageobjects.AdvancedFilter;
import pageobjects.AdvancedFindResult;
import pageobjects.CrmHome;
import pageobjects.LoginScreen;
import pageobjects.GMP.GMPHome;
import pageobjects.GMP.PaymentInstructionFile;
import pageobjects.GMP.VarianceReports;
import pageobjects.OP.OPHomePage;
import pageobjects.OP.OPStatement;
import pageobjects.OP.OP_StatementDetails;
import testdata.ConfigurationData;
import utilities.ExcelUtilities;
import verify.Verify;

public class OPHelpers extends Support {
	static WebDriver driver;
	static WebDriverWait wait;
	public boolean Process;
	public List<String> AssertMessage;
	public String paymentRunName;
	
	@FindBy(css="button[class='btn btn-success']")
	static WebElement addressWindowSaveButton;
	
	@FindBy(css="button[class*='btn btn-info']")
	static WebElement enterAddressButton;
	
	@FindBy(css="div[id='AddressModal']")
	static WebElement AddressModalWindow;
	
	@FindBy(css="input[id='AddressLine1']")
	static WebElement addressLine1;
	
	@FindBy(css="input[id='AddressLine2']")
	static WebElement addressLine2;
	
	@FindBy(css="input[id='AddressLine3']")
	static WebElement addressLine3;
	
	@FindBy(css="input[id='City']")
	static WebElement addressCity;
	
	@FindBy(css="input[id='County']")
	static WebElement addressCounty;
	
	@FindBy(css="input[id='Postcode']")
	static WebElement addressPostcode;
	
	@FindBy(xpath="//div[@class='h5']/strong")
	static WebElement claimNumberHeader;
	
	@FindBy(id="sigCanvas")
	static WebElement signatureCanvasbox;
	
	@FindBy(xpath="//div[@id='QRImage']/a/img")
	static WebElement QRImage;
	
	@FindBy(css="button#btnSubmit")
	static WebElement submitSignatory;
	
	@FindBy(xpath="//*[@id='modalSignSuccess']/div/div/div[3]/button")
	static WebElement closeSignWindow;
	
	public OPHelpers(WebDriver driver){

		//this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(driver, this);

	}
	
	public OPHelpers(boolean Process , List<String> AssertMessage )
	{
		this.Process = Process;
		this.AssertMessage = AssertMessage;
	}
	

	public OPHelpers(List<String> AssertMessage,String paymentRunName) {
		this.AssertMessage = AssertMessage;
		this.paymentRunName= paymentRunName;
	}

	public static void enterAddressManually(WebDriver driver) throws InterruptedException
	{
		new OPHelpers(driver);
        try{
            wait.until(ExpectedConditions.elementToBeClickable(enterAddressButton)).click();
            wait.until(ExpectedConditions.elementToBeClickable(AddressModalWindow));
            String addressline1 = ExcelUtilities.getKeyValueFromExcel("OPTESTDATA.xlsx", "AddressDetails", "Addressline1");
    		String addressline2 = ExcelUtilities.getKeyValueFromExcel("OPTESTDATA.xlsx", "AddressDetails", "Addressline2");
    		String addressline3 = ExcelUtilities.getKeyValueFromExcel("OPTESTDATA.xlsx", "AddressDetails", "Addressline3");
    		String addresscity = ExcelUtilities.getKeyValueFromExcel("OPTESTDATA.xlsx", "AddressDetails", "AddressCity");
    		String addresscounty = ExcelUtilities.getKeyValueFromExcel("OPTESTDATA.xlsx", "AddressDetails", "AddressCounty");
    		String addresspostcode = ExcelUtilities.getKeyValueFromExcel("OPTESTDATA.xlsx", "AddressDetails", "AddressPostCode");
            if(AddressModalWindow.isDisplayed())
            {
               Support.enterDataInTextField(addressLine1,addressline1,wait);
               Support.enterDataInTextField(addressLine2,addressline2,wait);
               Support.enterDataInTextField(addressLine3,addressline3,wait);
               Support.enterDataInTextField(addressCity,addresscity,wait);
               Support.enterDataInTextField(addressCounty,addresscounty,wait);
               Support.enterDataInTextField(addressPostcode,addresspostcode,wait);
               
               wait.until(ExpectedConditions.elementToBeClickable(addressWindowSaveButton)).click();
               Thread.sleep(2000);
                  
            }
	     }
	     catch(NoSuchElementException e)
	     {
	            System.out.println("The element is not found on GMC Address Pop up box" +e);
	     }
     
	}
	
	public static void selectProvidedOptions(String colName,String file,String sheet,WebDriver driver) throws IOException, InterruptedException 
	{
		new OPHelpers(driver);
		List<String> CellValues = ExcelUtilities.getCellValuesByPosition(file, sheet, colName);
		for (int i = 0; i < CellValues.size(); i++) {
		String CellValue = CellValues.get(i);
		System.out.println("The cell value is: "+CellValue);
		//WebElement ele = driver.findElement(By.id(CellValue));
		Thread.sleep(300);
		WebElement ele = driver.findElement(By.xpath("//*[contains(@id, '"+CellValue+"')]"));
		Support support= new Support();
		support.scrolltoElement(driver, ele);
		if(!(ele.isSelected())){
			ele.click();
		}
		Thread.sleep(1000);
		
		}
		
	}
	
	public static String getClaimNumber(String key,String file,WebDriver driver,WebElement element) {
		new OPHelpers(driver);
		Support support= new Support();
		support.scrolltoElement(driver, element);
		String clmNo = element.getText();
		clmNo = clmNo.substring(14);
		System.out.println(clmNo);
		/*ExcelUtilities.setKeyValueinExcel("OPGOS5TESTDATA.xlsx", "CLAIMS", key, clmNo);*/
		ExcelUtilities.setKeyValueByPosition(file, "EXPECTEDCLAIMDETAILS", clmNo, key, "CLAIMID");
		return clmNo;
	}
	
	public static void enterPerformerDetails(WebDriver driver, WebElement performerName, WebElement result, String environment) throws InterruptedException
	{
		new OPHelpers(driver);
		wait.until(ExpectedConditions.visibilityOf(performerName));
		if (!(CommonFunctions.isAttribtuePresent(performerName, "readonly")))
		{
			String performer = ConfigurationData.getRefDataDetails(environment, "OpthoPerformer");
			wait.until(ExpectedConditions.elementToBeClickable(performerName)).clear();
			CommonFunctions.setText(performerName, performer);//performer
			helpers.CommonFunctions.selectOptionFromList(result, performer);
		}
	}
	
	public static void enterPatDecSignatoryDetails(WebDriver driver) throws InterruptedException
	{
		new OPHelpers(driver);
		wait.until(ExpectedConditions.elementToBeClickable(QRImage)).click();
		Thread.sleep(3000);
		WindowHandleSupport.getRequiredWindowDriver(driver, "172");
		Thread.sleep(3000);
		Support support= new Support();
		wait.until(ExpectedConditions.visibilityOf(signatureCanvasbox));
		support.scrolltoElement(driver, signatureCanvasbox);
		enterSignatory(signatureCanvasbox, driver);
		wait.until(ExpectedConditions.elementToBeClickable(submitSignatory)).click();
		Thread.sleep(4000);
		wait.until(ExpectedConditions.elementToBeClickable(closeSignWindow)).click();
		Thread.sleep(1000);
		WindowHandleSupport.getRequiredWindowDriver(driver, "PCSE");
	}

	//Akshay S: This will verify the PL status after GMP and will update the Payment file status in CRM
	public OPHelpers verifyClaimDetailsForBSPMSInCRM(List<Object>parameters) 
			throws InterruptedException, IOException, AWTException {
		String claimNo="", mainFile=""; WebDriver driver=null;String key=""; 
		boolean evidence; String action=""; boolean updatePaymtFileStatus=false; 
		String paymentRunName="";
		
		if(parameters.size()==6){
			claimNo= (String)parameters.get(0);
			mainFile= (String)parameters.get(1);
			driver= (WebDriver)parameters.get(2);
			key= (String)parameters.get(3);
			evidence= (boolean)parameters.get(4);
			action= (String)parameters.get(5);
		}else{
			claimNo= (String)parameters.get(0);
			mainFile= (String)parameters.get(1);
			driver= (WebDriver)parameters.get(2);
			key= (String)parameters.get(3);
			evidence= (boolean)parameters.get(4);
			action= (String)parameters.get(5);
			updatePaymtFileStatus= (boolean)parameters.get(6);
			paymentRunName= (String)parameters.get(7);
		}
		
		boolean isMatched=false;
		List<String> message= new ArrayList<String>();
		List<String>keys= Arrays.asList(key);
		new OPHelpers(driver);
		CrmHome ObjCrmHome  = new CrmHome(driver);
		ObjCrmHome = ObjCrmHome.clickOnInlineDialog();
		AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
		List<String>GroupTypeValueList= Arrays.asList("fld");
		List<String>FieldValueList= Arrays.asList("Claim Number");
		List<String>ConditionValueList= Arrays.asList("Equals");
		List<String>ValueTypeList= Arrays.asList("Text");
		List<String>ValueForFieldValueList= Arrays.asList(claimNo);
		ObjAdvancedFilter.selectPrimaryEntityofCriteria("Ophthalmic Claims");
		ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupTypeValueList, FieldValueList, ConditionValueList, ValueTypeList, ValueForFieldValueList);
		ObjAdvancedFilter.clickResults();
		AdvancedFindResult objAdvancedFindResult= new AdvancedFindResult(driver);
		//String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition(mainFile, "ExpectedValues", "CRM STATUS", key);
		String ExpectedpaymentLineStatus = "Pending";
		//String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition(mainFile, "ExpectedValues","CALCULATEDCLAIMVALUE", key);
		String paymentDueDate="";

		boolean flag = objAdvancedFindResult.resultRecordFound();
		if (flag)
		{
			String title = objAdvancedFindResult.clickOnLinkFromFirstRecord(0,2);
			if(title!= null)
			{
				/*String clmStatus = objAdvancedFindResult.getDetailsFromResultRecordScreen();
				if (clmStatus.equalsIgnoreCase(claimStatus))
				{
					message.add("The claim status in CRM is Accepted For Payment.");
					System.out.println("The claim status in CRM is Accepted For Payment.");*/			
					String ClaimStatusCRM = objAdvancedFindResult.ClaimsStatus(claimNo, evidence, keys,"_ClaimStatus");
					
					// Verify Payment Line generated
					boolean PaymentLine = objAdvancedFindResult.VerifyPaymentLineCreated(ClaimStatusCRM);
					List<String> paymentLineDetailsList = new ArrayList<String>();
					if(PaymentLine)
					{						
						System.out.println("The Payment Line is generated for claim:  " +claimNo );
						paymentLineDetailsList = objAdvancedFindResult.paymentlineStatus(evidence,keys, "_PaymentLineStatus", "_GMPAmountDue");
						String PaymentLineStatus = paymentLineDetailsList.get(0);
						paymentDueDate= paymentLineDetailsList.get(2);
						System.out.println(PaymentLineStatus);
						if(action.equalsIgnoreCase("AfterGMP")){
							ExpectedpaymentLineStatus= "Payment Instruction Issued";
						}
						if (PaymentLineStatus.equalsIgnoreCase(ExpectedpaymentLineStatus))
						{
							if(action.equalsIgnoreCase("AfterGMP")){
								System.out.println("The GMP run is sucessfully completed");
								message.add("The GMP run is sucessfully completed");
								isMatched= true;
							}else{
								System.out.println("The Payment Line status is Pending");
								message.add("The Payment Line status is Pending.");
								isMatched= true;
							}
						}
						else
						{
							if(action.equalsIgnoreCase("AfterGMP")){
								Verify.verifyNotEquals((PaymentLineStatus.equalsIgnoreCase("Payment Instruction Issued")), "The GMP is not run executed sucessfully ");	
							}else{
								System.out.println("The Payment Line status is not Pending");
								Assert.assertEquals(PaymentLineStatus, ExpectedpaymentLineStatus, "The Payment Line status is not matching with Expected Status. Expected Status: "+ExpectedpaymentLineStatus+"But Actual Status: "+PaymentLineStatus);
							}
						}	
						
						/*// Verify Amount Due on Payment Line
						if(!action.equalsIgnoreCase("AfterGMP")){
							String PaymentLineAmountDue = paymentLineDetailsList.get(1);
							System.out.println(PaymentLineAmountDue);
							if (PaymentLineAmountDue.equalsIgnoreCase(ExpectedgmpAmountDue))
							{
								System.out.println("The Payment Line amount due is matching");
								message.add("The Payment Line amount due is matching.");
								isMatched=true;
							}
							else
							{
								System.out.println("The Payment Line amount due is not matching");
								//setAssertMessage("The Payment Line status is not Pending.", 1);
								Assert.assertEquals(PaymentLineAmountDue, ExpectedgmpAmountDue, "The Payment Line amount due is not matching with Expected . Expected Amount due:"+ExpectedgmpAmountDue+"But Actual Status: "+PaymentLineAmountDue);
							}
						}*/
					}
					else
					{
						System.out.println("The Payment Line is not generated for claim: " +claimNo );
						Assert.assertEquals(PaymentLine, "True", "The Payment Line is not generated for claim: " +claimNo);
					}
					Boolean AlertPresent = objAdvancedFindResult.isAlertPresent();
					if(AlertPresent)
					{
						objAdvancedFindResult.ClickSpaceBar();
					}

				/*}
				else{
					Assert.assertEquals(clmStatus, claimStatus, "The claim status in CRM is not Accepted for Payment.");
				}*/

			}

			else
			{
				Assert.assertNotNull(title, "Title extracted from result record is NULL.");
			}
		}
		else
		{

			Assert.assertEquals(flag, true, "No records found under results");

		}
/*		driver.close();
		WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
		
		Boolean AlertPresent = objAdvancedFindResult.isAlertPresent();
		if(AlertPresent)
		{
			objAdvancedFindResult.ClickSpaceBar();
		}*/
		if(updatePaymtFileStatus){
			isMatched= false;
			ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			GroupTypeValueList.remove(GroupTypeValueList);
			GroupTypeValueList= Arrays.asList("fld");
			FieldValueList.remove(FieldValueList);
			FieldValueList= Arrays.asList("Payment Run");
			ConditionValueList.remove(ConditionValueList);
			ConditionValueList= Arrays.asList("Contains");
			ValueTypeList.remove(ValueTypeList);
			ValueTypeList= Arrays.asList("Text");
			ValueForFieldValueList.remove(ValueForFieldValueList);
			ValueForFieldValueList= Arrays.asList(paymentRunName); //paymentRunName
			ObjAdvancedFilter.selectPrimaryEntityofCriteria("Payment Files");
			ObjAdvancedFilter.enterFilterCriteria_multiple_test(GroupTypeValueList, FieldValueList, ConditionValueList, ValueTypeList, ValueForFieldValueList);
			ObjAdvancedFilter.clickResults();	
			objAdvancedFindResult= new AdvancedFindResult(driver);
			flag = objAdvancedFindResult.resultRecordFound();
			if (flag)
			{
				int records= objAdvancedFindResult.getRecordCount();
				for(int i=0;i<records;i++){
					String title = objAdvancedFindResult.clickOnLinkFromFirstRecord(i,2);
					if(title!= null)
					{
						objAdvancedFindResult= objAdvancedFindResult.updatePaymentFileStatus("ISFE Validated");
						System.out.println("Payment file status has been updated for index: "+i);
						message.add("Payment file status has been updated for index: "+i);
						isMatched=true;
					}
				}
			}
		}
		return new OPHelpers(isMatched,message);
	}

	//Akshay S: This will create GMP for PMS claim
	public OPHelpers CreateGMP(WebDriver driver, String environment, boolean evidence, String key, String strDueDate) throws InterruptedException, IOException {
		List<String> message= new ArrayList<String>();
		LoginScreen ObjLoginScreen = new LoginScreen(driver);
		GMPHome ObjGMPHome = ObjLoginScreen.logintoGMP("PCSEPAYMENTSPROCESSOR", environment);
		ObjGMPHome = ObjGMPHome.clickOnNewCreateGMP(strDueDate, 1);
		ObjGMPHome = ObjGMPHome.clickonSearch(driver);
		String paymentRunName= ObjGMPHome.getPaymentRunName();
		System.out.println("Payment run name is: "+paymentRunName);
		VarianceReports objVarianceReports = ObjGMPHome.VerifyPaymentstatus();
		objVarianceReports = objVarianceReports.clickonGeneratePaymentFile();
		PaymentInstructionFile objPaymentInstructionFile = objVarianceReports.ClickonPaymentInstructionFile();
		objPaymentInstructionFile = objPaymentInstructionFile.ClickOnDownload(driver);
		System.out.println("The Dot 1 file is generated");
		message.add("The Dot 1 file is generated");
		if(evidence)
		{
			objPaymentInstructionFile.takescreenshots(key+"_Download_Dot1file");
		}
		return new OPHelpers(message,paymentRunName);
	}

	//Akshay S: This will verify the OP claim in statement
	public OPHelpers verifyEntryInStatement(WebDriver driver,String environment,boolean evidence, String key, String claimNo, String GOSType) 
			throws InterruptedException, IOException {
		List<String> message= new ArrayList<String>();
		
		LoginScreen ObjLoginScreen = new LoginScreen(driver);
		OPHomePage ObjHomePage = ObjLoginScreen.logintoOP_Home("PCSEPAYMENTSPROCESSOR", environment);
		OPStatement ObjOPStatement = ObjHomePage.redirectToStatementPage(environment);
		ObjOPStatement= ObjOPStatement.enterFilterData();
		if (evidence)
		{
			ObjOPStatement.OPStatementSnaps(key+"_StatementSearchRecord");
		}
		//String ClaimNo= "ADA00926";
		OP_StatementDetails ObjOP_StatementDetails = ObjOPStatement.clickOnStatementRefLink(environment,"PMS_OrganizationCode","PMS_ContractorName");
		boolean isDocPresent= ObjOP_StatementDetails.verifyPresenceOfDocument(claimNo,GOSType);
		Assert.assertEquals(isDocPresent, true,"Claim ID "+claimNo+" is not present inside statement details page");
		System.out.println("Claim number "+claimNo+" has been picked by Statement and is visible on portal");
		message.add("Claim number "+claimNo+" has been picked by Statement and is visible on portal");
		if(evidence)
		{
			ObjOP_StatementDetails.OPStatementDetailsSnaps(key+"_StatementDetails for claim number_"+claimNo);
		}
		System.out.println("Statement has been generated against a Payment File");
		message.add("Statement has been generated against a Payment File");
		return new OPHelpers(isDocPresent,message);
	}

	public OPHelpers verifyOPClaimDetailsFromDB(String environment,String dbEnvironment,String claimNo,List<String>expectedValues,
			List<String>expectedAttributes,String type,String fileName) throws InterruptedException, SQLException {
		boolean isDataMatched= false;
		List<String> actualValues= new ArrayList<String>(),assertMessage= new ArrayList<String>();
		StringBuilder buildquery = new StringBuilder();
		int temp=0;
		String PLName=null,tableName=null,columnEquals=null;
		
		switch(type){
		case "ClaimDetails":
			String CRMDBColumns= ExcelUtilities.getKeyValueByPosition(fileName, "DatabaseDetails", "ClaimDataColumns", "Value");
			tableName= ExcelUtilities.getKeyValueByPosition(fileName, "DatabaseDetails", "OPClaimTblName", "Value");
			columnEquals= ExcelUtilities.getKeyValueByPosition(fileName, "DatabaseDetails", "ColumnEquals", "Value");
			List<String> columns= Arrays.asList(CRMDBColumns.split(","));
			
			buildquery = new StringBuilder();
			buildquery.append("select top 1 "+CRMDBColumns+" from "+tableName);
			buildquery.append(" where "+columnEquals+"='"+claimNo+"'");
			String query= buildquery.toString();
			System.out.println("Query is: "+query);
			
			boolean flag= false;
			while(!flag){
				actualValues = DatabaseHelper.CreateCRMDataListForAListOfColumns(query,columns,dbEnvironment, environment);
				System.out.println("Actual claim values from CRM database: "+actualValues);
				if(!actualValues.isEmpty()&&actualValues!=null){
					flag=true;
				}
			}
			actualValues= CommonFunctions.modifyNumericValuesForOPProcessing(actualValues);
			System.out.println("Claim Details post processing data are: "+actualValues);
			break;
		case "PLDetails":
			CRMDBColumns= ExcelUtilities.getKeyValueByPosition(fileName, "DatabaseDetails", "PLDataColumns", "Value");
			tableName= ExcelUtilities.getKeyValueByPosition(fileName, "DatabaseDetails", "OPClaimTblName", "Value");
			columnEquals= ExcelUtilities.getKeyValueByPosition(fileName, "DatabaseDetails", "ColumnEquals", "Value");
			columns= Arrays.asList(CRMDBColumns.split(","));
			String PLColumnName= ExcelUtilities.getKeyValueByPosition(fileName, "DatabaseDetails", "PLColumnName", "Value");
			buildquery = new StringBuilder();
			buildquery.append("select "+PLColumnName+" from "+tableName);
			buildquery.append(" where "+columnEquals+"='"+claimNo+"'");
			query= buildquery.toString();
			System.out.println("PL verification Query is: "+query);
			
			flag= false;
			while(!flag){
				PLName= DatabaseHelper.getCRMValueFromQuery(query, PLColumnName, dbEnvironment, environment);
				System.out.println("PL Name for claim "+claimNo+" is "+PLName);
				if(PLName==null){
					flag= false;
				}else{
					flag= true;
				}
			}
			assertMessage.add("PL Name for claim "+claimNo+" is "+PLName);
			if(PLName!=null){
				tableName= ExcelUtilities.getKeyValueByPosition(fileName, "DatabaseDetails", "PLTblName", "Value");
				columnEquals= ExcelUtilities.getKeyValueByPosition(fileName, "DatabaseDetails", "PLColumnEquals", "Value");
				
				buildquery = new StringBuilder();
				buildquery.append("select top 1 "+CRMDBColumns+" from "+tableName);
				buildquery.append(" where "+columnEquals+"='"+claimNo+"'");
				query= buildquery.toString();
				System.out.println("PL data Query is: "+query);
				
				flag= false;
				while(!flag){
					actualValues = DatabaseHelper.CreateCRMDataListForAListOfColumns(query,columns,dbEnvironment, environment);
					System.out.println("Actual PL values from CRM database: "+actualValues);
					if(!actualValues.isEmpty()&&actualValues!=null){
						flag=true;
					}
				}
				actualValues= CommonFunctions.modifyNumericValuesForOPProcessing(actualValues);
				System.out.println("PL Details post processing data are: "+actualValues);
			}else{
				Assert.fail("PL is not created for Claim Number: "+claimNo);
			}
			
			break;
		default:
			Assert.fail(type+ " is not found for data verification.");
		}

		for(int i=0;i<expectedValues.size();i++){
			String attribute= expectedAttributes.get(i);
			String expectedValue= expectedValues.get(i);
			String actualValue= actualValues.get(i);
			if(expectedValue.equalsIgnoreCase(actualValue)){
				temp= temp+1;
				assertMessage.add("Expected and Actual "+attribute+" is matching. Expected: "+expectedValue+" Actual: "+actualValue);
				System.out.println("Expected and Actual "+attribute+" is matching. Expected: "+expectedValue+" Actual: "+actualValue);
			}else{
				assertMessage.add("Expected and Actual "+attribute+" is not matching. Expected: "+expectedValue+" Actual: "+actualValue);
				System.out.println("Expected and Actual "+attribute+" is not matching. Expected: "+expectedValue+" Actual: "+actualValue);
			}
		}
		if(expectedValues.size()==temp){
			isDataMatched= true;
		}
		return new OPHelpers(isDataMatched, assertMessage);
	}

	public OPHelpers validateOPClaimDetails(String fileName, String keyname, String environment,String dbEnvironment, String claimNo, String type) throws InterruptedException, SQLException {
		boolean isDataMatched= false;
		List<String> assertMessage= new ArrayList<String>();
		String claimStatus = utilities.ExcelUtilities.getKeyValueByPosition(fileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPCLAIMSTATUS");
		String ExpectedpaymentLineStatus = utilities.ExcelUtilities.getKeyValueByPosition(fileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPPAYMENTLINESTATUS");
		String ExpectedgmpAmountDue = utilities.ExcelUtilities.getKeyValueByPosition(fileName, "EXPECTEDCLAIMDETAILS", keyname, "EXPGMPAMOUNTDUE");
		
		List<String>expectedValues= Arrays.asList(claimStatus,ExpectedgmpAmountDue);
		String claimAttributes= ExcelUtilities.getKeyValueByPosition(fileName, "DatabaseDetails", "ClaimAttributes", "Value");
		List<String>expectedAttributes= Arrays.asList(claimAttributes.split(","));
		
		List<String>PLexpectedValues= Arrays.asList(ExpectedpaymentLineStatus,ExpectedgmpAmountDue);
		String PLAttributes= ExcelUtilities.getKeyValueByPosition(fileName, "DatabaseDetails", "PLAttributes", "Value");
		List<String>PLexpectedAttributes= Arrays.asList(PLAttributes.split(","));
		
		OPHelpers objOPHelpers= new OPHelpers(isDataMatched, assertMessage);
		switch(type){
		case "ClaimDetails":
			objOPHelpers= objOPHelpers.verifyOPClaimDetailsFromDB(environment,dbEnvironment,claimNo,expectedValues,expectedAttributes,type,fileName);
			break;
		case "PLDetails":
			objOPHelpers= objOPHelpers.verifyOPClaimDetailsFromDB(environment,dbEnvironment,claimNo,PLexpectedValues,PLexpectedAttributes,type,fileName);
			break;
		default:
			Assert.fail(type+ " is not found for data verification.");
		}
		
		return new OPHelpers(objOPHelpers.Process, objOPHelpers.AssertMessage);
	}
}
