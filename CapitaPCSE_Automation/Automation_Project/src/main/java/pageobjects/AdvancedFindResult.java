package pageobjects;

import java.awt.AWTException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import helpers.CommonFunctions;
import helpers.GPPHelpers;
import helpers.Screenshot;
import helpers.Support;
import helpers.UITableDataSupport;
import helpers.WindowHandleSupport;
import utilities.ExcelUtilities;

public class AdvancedFindResult extends Support{

	WebDriver driver;
	WebDriverWait wait;

	@FindBy(xpath="//table[@id='gridBodyTable']/tbody")
	WebElement resultTable;

	@FindBy(id="resultFrame")
	WebElement resultFrame;

	@FindBy(xpath="//table[@id='gridBodyTable']/tbody/tr[1]/td[3]")
	WebElement firstRecord;

	@FindBy(xpath="//div[@id='oph_uniqueclaimidentifier']/div/span")
	WebElement uniqueClmIdValue;

	@FindBy(xpath="//div[@id='oph_contractornumber']/div/span")
	WebElement contractorNumberValue;

	@FindBy(xpath="//div[@id='oph_organisationcode']/div/span")
	WebElement orgCodeValue;

	@FindBy(xpath="//div[@id='oph_performernumber']/div/span")
	WebElement perfNumValue;

	@FindBy(xpath="//div[@id='oph_pointofservicedate']/div/span")
	WebElement posDateValue;

	@FindBy(xpath="//div[@id='oph_claimreceiveddate']/div/span")
	WebElement clmRcvDateValue;

	@FindBy(xpath="//div[@id='oph_submittedclaimvalue']/div/span")
	WebElement subClmValue;

	@FindBy(xpath="//div[@id='oph_calculatedclaimvalue']/div/span")
	WebElement calClmValue;

	@FindBy(xpath="//div[@id='oph_claimtype']/div/span")
	WebElement clmTypeValue;

	@FindBy(xpath="//div[@id='oph_ophthalmicclaimsubmissionchannel']/div/span")
	WebElement optClmValue;

	@FindBy(xpath="//div[@id='oph_icode']/div/span")
	WebElement ICodeValue;

	@FindBy(xpath="//div[@id='oph_pcode']/div/span")
	WebElement PCodeValue;

	@FindBy(xpath="//div[@id='oph_ocode']/div/span")
	WebElement clmValue;

	@FindBy(xpath="//div[@id='oph_scannedclaimreference']/div/span")
	WebElement ScnclmRefValue;

	@FindBy(xpath="//div[@id='oph_uniquemessageid']/span")
	WebElement UnqMsgIdValue;

	@FindBy(xpath="//div[@id='oph_claimformversionid']/div/span")
	WebElement ClmFormVersionValue;

	@FindBy(xpath="//div[@id='oph_name']/div/span")
	WebElement nameValue;

	@FindBy(xpath="//div[@id='ownerid']/div/span")
	WebElement ownerValue;

	@FindBy(xpath="//div[@id='oph_claimstatus']/div/span")
	WebElement clmStatusValue;

	@FindBy(xpath="//div[@id='oph_paymentline']/div/span")
	WebElement payLineValue;

	@FindBy(xpath="//div[@id='oph_chequebookreferencenumber']/div/span")
	WebElement chqBookRefValue;

	@FindBy(xpath="//div[@id='pli_name']/div/span")
	WebElement ApplicationNumberOnText_PL;

	@FindBy(xpath="//div[@id='pli_name']")
	WebElement ApplicationNumberOnfield_PL;	

	@FindBy(xpath="//*[@id='oph_claimstatus']")
	WebElement Claimsstatus_OP;	

	@FindBy(xpath="//div[@id='oph_claimstatus']/div/span")
	WebElement ClaimsstatusOnText_OP;
	
	@FindBy(xpath="//div[@id='oph_pvnstatus']/div/span")
	WebElement pvnStatus;

	@FindBy(xpath="//*[@id='pcs_message']")
	WebElement ErrorMessage_OP;	

	/*	@FindBy(xpath="//*[@id='pcs_message_i']")
	WebElement ErrorMessageOnText_OP;*/

	@FindBy(xpath="//div[@id='pcs_message']/div/span")
	WebElement ErrorMessageOnText_OP;

	@FindBy(xpath="//*[@id='oph_paymentline']/div/span")
	WebElement PaymentOnText_OP;

	@FindBy(xpath="//*[@id='oph_paymentline']/div")
	WebElement PaymentOnTextRej_OP;

	@FindBy(xpath="//*[@id='oph_paymentline']")
	WebElement Payment_OP;

	@FindBy(xpath="//*[@id='Mscrm.AdvancedFind-title']/a/span")
	WebElement AdvancedFind;

	@FindBy(xpath="//div[@id='formselectorcontainer']/div/h1")
	WebElement ClaimNumberHeader;
	
	@FindBy(xpath="//div[@id='oph_paymentline']/div/span[1]")
	WebElement PaymentLineLink;
	
	@FindBy(xpath="//div[@id='header_crmFormSelector']/nobr/span")
	WebElement PaymentLineFormHeader;
	
	@FindBy(xpath="//div[@id='gmp_paymentlinestatus']/div/span")
	WebElement PaymentLineStatus;
	
	@FindBy(xpath="//div[@id='gmp_amountdue']/div/span")
	WebElement gmpAmountDue;
	
	@FindBy(xpath="//div[@id='gmp_paymentduedate']/div/span")
	WebElement gmpDueDate;
	
	
	//Akshay S: added to ldate claim status for SC module in GPP
	@FindBy(xpath="//div[@id='gpp_claimstatus']/div/span[1]")
	WebElement ClaimsstatusOnText_GPPSC;
	
	@FindBy(xpath="//div[@id='gpp_paymentline']/div/span[1]")
	WebElement PaymentLineLink_GPPSC;
	
	
	@FindBy(xpath="//div[@id='gmp_paymentlinestatus']/div/span[1]")
	WebElement PaymentLineStatus_GPPSC;
	
	@FindBy(xpath="//div[@id='gpp_claimstatus']")
	WebElement Claimsstatus_GPPSC;
	
	@FindBy(xpath="//*[@id='gpp_paymentline']/div/span[1]")
	WebElement PaymentOnTextRej_GPPSC;
	
	@FindBy(xpath="//*[@id='gpp_paymentline']/div/span[1]")
	WebElement PaymentOnText_GPPSC;

	@FindBy(xpath="//div[@id='gmp_paymentduedate']/div/span")
	WebElement PaymentDueDate;
	
	@FindBy(xpath="//*[@id='crmGridControl_gridBar']/tbody/tr/th[2]/table/tbody/tr/td/a/nobr/img")
	WebElement CreatedOnArrow;
	
		@FindBy(xpath="//*[@id='TabNode_tab0Tab']/a")
	WebElement TopArrowbutton;
	
	@FindBy(xpath="//a[@title='Organisation Payment Dates']")
	WebElement OrgPaymentdates;
	
	//Added by akshay to delete all the entries in entity
	
	@FindBy(id="butBegin")
	WebElement confirmDelete_btn;
	
	@FindBy(xpath="//div[@id='gpp_paymentline']")
	WebElement PaymentLine;
	
	@FindBy(xpath="//div[@id='gpp_paymentline']/div/span[1]")
	WebElement PaymentOnText_GPPQOF;
	
	@FindBy(xpath="//*[@id='gpp_paymentline']/div/span[1]")
	WebElement PaymentLineLink_GPPQOF;
	
	@FindBy(xpath="//div[@id='gpp_name']")
	WebElement NameField;
	
	@FindBy(xpath="//div[@id='gpp_name']/div/span")
	WebElement NameFieldTxt;
	
	@FindBy(xpath="//div[@id='gpp_financialyear']")
	WebElement finYearField;
	
	@FindBy(xpath="//div[@id='gpp_financialyear']/div/span[1]")
	WebElement finYearFieldTxt;
	
	@FindBy(xpath="//div[@id='gpp_aspirationalannualvalue']")
	WebElement aspValueField;
	
	@FindBy(xpath="//div[@id='gpp_aspirationalannualvalue']/div/span[1]")
	WebElement aspValueFieldTxt;
	
	@FindBy(xpath="//div[@id='gpp_achievedvalue']")
	WebElement achValueField;
	
	@FindBy(xpath="//div[@id='gpp_achievedvalue']/div/span[1]")
	WebElement achValueFieldTxt;
	
	@FindBy(xpath="//div[@id='gpp_achievedvaluereceiveddate']")
	WebElement achValueDateField;
	
	@FindBy(xpath="//div[@id='gpp_achievedvaluereceiveddate']/div/span[1]")
	WebElement achValueDateFieldTxt;
	
	@FindBy(xpath="//div[@id='gpp_aspirationalvaluereceiveddate']")
	WebElement aspValueDateField;
	
	@FindBy(xpath="//div[@id='gpp_aspirationalvaluereceiveddate']/div/span[1]")
	WebElement aspValueDateFieldTxt;
	
/*	@FindBy(xpath="//div[@id='navTabGroupDiv']/span[4]/span[2]/a")
	WebElement navigateToEntity;
	
	@FindBy(css="a[title='Annual QOF Histories']")
	WebElement navAnnualQOFHistories;
	
	@FindBy(css="a[title='Monthly QOF Payments']")
	WebElement navMonthlyQOFPayments;*/
	
	@FindBy(xpath="//div[@id='scr_nationalcodestring']")
	WebElement nationalCodeField;
	
	@FindBy(xpath="//div[@id='scr_nationalcodestring']/div/span")
	WebElement nationalCodeFieldTxt;
	
	@FindBy(xpath="//div[@id='crmGridControl']")
	WebElement resultGrid;
	
	@FindBy(xpath="//div[@id='fixedrow']")
	WebElement gridFixedRow;
	
	@FindBy(xpath="//table[@id='crmGridControl_gridBar']")
	WebElement columnGrid;
	
	@FindBy(xpath="//div[@id='InlineDialog']")
	WebElement deleteWindow;
	
	@FindBy(xpath="//div[@class='ms-crm-grid-databodycontainer']")
	WebElement resultDataBody;
	
	@FindBy(xpath="//div[@id='crmGridControl_divDataArea']")
	WebElement resultDataArea;
	
	@FindBy(xpath="//table[@id='gridBodyTable']")
	WebElement gridTable;
	
	@FindBy(xpath="//table[@id='gridBodyTable']//tbody")
	WebElement gridTableMain;
	
	@FindBy(xpath="//li[contains(@id,'MainTab.Management')]")
	WebElement recordsTab;
	
	@FindBy(xpath="//span[contains(@id,'Management-LargeMedium-1')]/span[3]/a")
	WebElement deleteOption;
	
	//******HPV elements started******//
	@FindBy(xpath="//div[@id='scr_dateofbirth']")
	WebElement dateOfBirthField;
	
	@FindBy(xpath="//div[@id='scr_dateofbirth']/div/span")
	WebElement dateOfBirthText;
	
	@FindBy(xpath="//div[@id='scr_nhsno']")
	WebElement NHSNumberField;
	
	@FindBy(xpath="//div[@id='scr_nhsno']/div/span")
	WebElement NHSNumberFieldText;
	
	@FindBy(xpath="//div[@id='scr_patient']")
	WebElement patientField;
	
	@FindBy(xpath="//div[@id='scr_patient']/div/span")
	WebElement patientText;
	
	@FindBy(xpath="//div[@id='scr_vaccinationdate']")
	WebElement vacciDateField;
	
	@FindBy(xpath="//div[@id='scr_vaccinationdate']/div/span")
	WebElement vacciDateText;
	
	@FindBy(xpath="//div[@id='scr_vaccinationtype']")
	WebElement vacciTypeField;
	
	@FindBy(xpath="//div[@id='scr_vaccinationtype']/div/span")
	WebElement vacciTypeText;
	
	@FindBy(xpath="//div[@id='scr_matchstatus']")
	WebElement matchStatusField;
	
	@FindBy(xpath="//div[@id='scr_matchstatus']/div/span")
	WebElement matchStatusText;
	
	@FindBy(xpath="//div[@class='ms-crm-tabcolumn2']/div[3]")
	WebElement vaccineDiv;
	
	@FindBy(xpath="//div[@id='Vaccination_d']")
	WebElement vaccineDiv2;
	
	@FindBy(xpath="//div[@id='Vaccination_compositeControl']")
	WebElement vaccineDiv3;
	
	@FindBy(xpath="//div[@id='Vaccination_crmGridTD']")
	WebElement vaccineDiv4;
	
	@FindBy(xpath="//div[@id='Vaccination']")
	WebElement vaccineDiv5;
	
	@FindBy(xpath="//div[@id='Vaccination_divDataBody']")
	WebElement vaccinationMain;
	
	@FindBy(xpath="//div[@id='Vaccination_divDataArea']//table[@id='gridBodyTable']")
	WebElement tableMain;
	
	@FindBy(xpath="//div[@id='Vaccination_divDataArea']//table[@id='gridBodyTable']//tbody")
	WebElement tableBody;
	
	@FindBy(xpath="//div[@id='Vaccination_divDataArea']//table[@id='gridBodyTable']//tbody/tr[1]/td[2]/nobr")
	WebElement vaccinationType;
	
	@FindBy(xpath="//div[@id='Vaccination_divDataArea']//table[@id='gridBodyTable']//tbody/tr[1]/td[3]/div")
	WebElement vaccinationDate;
	
	@FindBy(xpath="//div[@class='ms-crm-tabcolumn2']")
	WebElement tabColumns;
	
	@FindBy(xpath="//*[@id='crmGridControl_gridBar']/tbody/tr/th[3]/table/tbody/tr/td/a/nobr/label")
	WebElement CreatedOnArrow_HPV;
	
	@FindBy(xpath="//div[@id='oph_ophthalmicservicerate']/div/span")
	WebElement ophServiceRate;
	
	@FindBy(xpath="//div[@id='oph_pvnstatus']/div/span")
	WebElement pvnStatusValue;

	//******HPV elements ended******//

	@FindBy(xpath="//div[@id='gpp_status']/div/span")
	WebElement Claimsstatus_GPPAdj;
		
	//Akshay S: Added webelements for Portal User entity
	@FindBy(xpath="//div[@id='pcs_portalrole']")
	WebElement portalRoleDiv;
	
	@FindBy(xpath="//div[@id='pcs_portalrole']/div/span")
	WebElement portalRoleText;
	
	//Akshay S: added elements for payment run count for GMP
	@FindBy(xpath="//div[@id='gmp_paymentruncount']")
	WebElement paymentRunCountDiv;
	
	@FindBy(xpath="//div[@id='gmp_paymentruncount']/div/span")
	WebElement paymentRunCountTextField;
	
	@FindBy(xpath="//div[@id='gmp_paymentfilestatus']/div")
	WebElement paymentFileStatusDiv;
	
	@FindBy(xpath="//img[@id='gmp_paymentfilestatus_i']")
	WebElement Search;
	 
	@FindBy(xpath="//a[@title='Look Up More Records']")
	WebElement LookUpMoreRecords;
	
	@FindBy(xpath="//input[@id='crmGrid_findCriteria']")
	WebElement SearchTextBox;

	@FindBy(xpath="//button[@id='butBegin']")
	WebElement AddButton;
	
	@FindBy(xpath="//*[@id='savefooter_statuscontrol']")
	WebElement SaveButton;
	
	//Added by Akshay to update POS payyment flag in CRM
	@FindBy(css="div.ms-crm-Inline-Edit.ms-crm-Inline-CheckBox")
	WebElement receivePOSPaymentDiv;
	
	@FindBy(xpath="//div[@class='ms-crm-Inline-Edit ms-crm-Inline-CheckBox']/input")
	WebElement receivePOSPayment;
	
	//Added by Akshay to delete entries from entity containing lengthy name
	@FindBy(xpath="//*[contains(@id,'Delete-Small')]/span/span/img")
	WebElement deleteOption2;
	
	//Added by Akshay to update estimate status for pensions
	@FindBy(id="gpp_estimatestatus")
	WebElement estimateStatusDiv;
	
	@FindBy(xpath="//img[@id='gpp_estimatestatus_i']")
	WebElement Search_GPBIS;

	@FindBy(xpath="//*[@id='oph_pointofservicedate']/div[1]/span")
	WebElement Pointofservice;
	
	@FindBy(xpath="//*[@aria-labelledby='oph_pointofservicedate_c oph_pointofservicedate_w']")
	WebElement PointofserviceText;
	
	@FindBy(xpath="//*[@id='oph_claimreceiveddate']/div[1]/span")
	WebElement ClaimReceiveddate;
	
	@FindBy(xpath="//*[@aria-labelledby='oph_claimreceiveddate_c oph_claimreceiveddate_w']")
	WebElement ClaimReceiveddateText;
	
	@FindBy(xpath="//*[@id='oph_dateclaimsubmitted']/div[1]/span")
	WebElement ClaimSubmitted;
	
	@FindBy(xpath="//*[@aria-labelledby='oph_dateclaimsubmitted_c oph_dateclaimsubmitted_w']")
	WebElement ClaimSubmittedText;
	
	//Added by Akshay on 19 June 2018 to locate delete button at leftmost corner of the page
	@FindBy(xpath="//*[contains(@id,'Delete-Medium')]/span[1]/span/img")
	WebElement deleteOption3;
	
	//Added by Akshay on 28June 2018 to locate AVC amount field
	@FindBy(xpath="//div[@id='gpp_additionalvoluntarycontributionmonthlyamo']/div/span")
	WebElement purchaseAmt;
	
	//Added by Akshay to locate portal user fields
	@FindBy(xpath="//div[@id='pcs_portalusertype']/div[1]")
	WebElement portalUserTypeDiv;
	
	@FindBy(css="img[id='pcs_portalusertype_i']")
	WebElement searchUserType;
	
	@FindBy(xpath="//div[@id='pcs_portalrole']/div[1]")
	WebElement portalRole;
	
	@FindBy(css="img#pcs_portalrole_i")
	WebElement searchPortalRole;
	
	@FindBy(xpath="//div[@id='pcs_organisation']/div[1]")
	WebElement roleOrgDiv;
	
	@FindBy(css="img#pcs_organisation_i")
	WebElement searchOrg;
	
	@FindBy(xpath="//div[@id='pcs_performerlisttype']/div[1]")
	WebElement performerListTypeDiv;
	
	@FindBy(css="img#pcs_performerlisttype_i")
	WebElement searchPerformerListType;
	
	@FindBy(xpath="//div[@id='pcs_performertype']/div[1]")
	WebElement performerTypeDiv;
	
	@FindBy(css="img#pcs_performertype_i")
	WebElement searchPerformerType;
	
	@FindBy(xpath="//div[@id='pcs_starting']/div[1]")
	WebElement assignmentStartDateDiv;
	
	@FindBy(xpath="//div[@id='pcs_starting']//img[@id='pcs_starting_iimg']")
	WebElement dateControl;
	
	//Added by Akshay on 5th Nov 2018 to locate ERRBO amount field
	@FindBy(xpath="//div[@id='gpp_errbomonthlyamount']/div/span")
	WebElement ERRBOAmt;
	
	@FindBy(xpath="//div[@id='gpp_moneypurchasemonthlyamount']/div/span")
	WebElement MPAVCAmt;
	
	public AdvancedFindResult(WebDriver driver){

		this.driver = driver;
		//driver.switchTo().frame("resultFrame");

		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}

	public String clickOnLinkFromFirstRecord(int rowIndex, int colIndex) throws InterruptedException
	{
		//switchToRequiredIframe(resultTable, driver);
		String recordTitle = null;
		driver.switchTo().frame("contentIFrame0");
		driver.switchTo().frame(resultFrame);
		Actions action1 = new Actions(driver);
		action1.doubleClick(CreatedOnArrow).perform();
		Thread.sleep(2000);
		Actions action2 = new Actions(driver);
		action2.doubleClick(CreatedOnArrow).perform();
		Thread.sleep(2000);
		System.out.println("Control moved to resultFrame.");
		WebElement firstRecordLink = UITableDataSupport.getWebElementFromTable(resultTable, rowIndex, colIndex);
		recordTitle = firstRecordLink.getText();
		System.out.println(recordTitle);

		Actions action = new Actions(driver);
		action.doubleClick(firstRecordLink).perform();
		driver.switchTo().defaultContent();

		return recordTitle;


	}
	
	
	
	public EmailDescription clickOnLinkFromRecord(int rowIndex, int colIndex) throws InterruptedException
	{
		//switchToRequiredIframe(resultTable, driver);
		String recordTitle = null;
		driver.switchTo().frame("contentIFrame0");
		driver.switchTo().frame(resultFrame);
		/*Actions action1 = new Actions(driver);
		action1.doubleClick(CreatedOnArrow1).perform();*/
		/*Actions action2 = new Actions(driver);
		action2.doubleClick(CreatedOnArrow1).perform();*/
		System.out.println("Control moved to resultFrame.");
		WebElement firstRecordLink = UITableDataSupport.getWebElementFromTable(resultTable, rowIndex, colIndex);
		recordTitle = firstRecordLink.getText();
		System.out.println(recordTitle);

		Actions action = new Actions(driver);
		action.doubleClick(firstRecordLink).perform();
		driver.switchTo().defaultContent();

		return new EmailDescription(driver);


	}
	
	public PerformerAppCase clickOnLinkFromFirstRecord_Process(int rowIndex, int colIndex) throws InterruptedException
	{
		//switchToRequiredIframe(resultTable, driver);
		String recordTitle = null;
		driver.switchTo().frame("contentIFrame0");
		driver.switchTo().frame(resultFrame);
		System.out.println("Control moved to resultFrame.");
		Actions action1 = new Actions(driver);
		action1.doubleClick(CreatedOnArrow).perform();
		Actions action2 = new Actions(driver);
		action2.doubleClick(CreatedOnArrow).perform();
		WebElement firstRecordLink = UITableDataSupport.getWebElementFromTable(resultTable, rowIndex, colIndex);
		recordTitle = firstRecordLink.getText();
		System.out.println("Result Record Title: "+recordTitle);

		Actions action = new Actions(driver);
		action.doubleClick(firstRecordLink).perform();
		driver.switchTo().defaultContent();
		


		return new PerformerAppCase(driver);

	}
	public boolean resultRecordFound()
	{
		driver.switchTo().frame("contentIFrame0");
		driver.switchTo().frame(resultFrame);
		System.out.println("Control moved to resultFrame.");
		boolean flag = false;
		boolean ispresent = driver.findElement(By.xpath("//table[@id='gridBodyTable']/tbody/tr/td")).getAttribute("class").contains("ms-crm-List-Empty-Cell");
		System.out.println(ispresent);
		if(!ispresent)
		{
			List<WebElement> rowElements = resultTable.findElements(By.tagName("tr"));
			int records = rowElements.size();
			System.out.println(records);
			if (records>0)
			{
				flag = true;
			}
		}
		
		driver.switchTo().defaultContent();
		//driver.close();
		return flag;
		
	}

	public String getDetailsFromResultRecordScreen() throws InterruptedException
	{

		Thread.sleep(4000);
		WindowHandleSupport.getRequiredWindowDriver(driver, "Ophthalmic Claim");
		//switchToRequiredIframe(uniqueClmIdValue, driver);
		driver.switchTo().frame("contentIFrame0");
		scrolltoElement(driver, clmStatusValue);
		String clmStatus = clmStatusValue.getText();
		//ExcelUtilities.setKeyValueinExcel("OPTESTDATA.xlsx", "OPCLAIM", "Claim Status", clmStatus);
		driver.switchTo().defaultContent();
		return clmStatus;


	}
	
	public String getDetailsFromOpthClaimTypeResultRecordScreen(String key) throws InterruptedException
	{

		Thread.sleep(4000);
		WindowHandleSupport.getRequiredWindowDriver(driver, "Ophthalmic Payment Rate");
		//switchToRequiredIframe(uniqueClmIdValue, driver);
		driver.switchTo().frame("contentIFrame0");
		scrolltoElement(driver, ophServiceRate);
		String payRate = ophServiceRate.getText().substring(1);
		System.out.println(payRate);
		ExcelUtilities.setKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", payRate, key, "EXPGMPAMOUNTDUE");
		//ExcelUtilities.setKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", clmNo, key, "CLAIMID");
		driver.switchTo().defaultContent();
		return payRate;


	}
	

	public boolean VerifyApplicationNumber(String Application_Number) throws IOException {
		String ApplicationNumberOnCRM = null;
		boolean VerifyAppNumber = false;

		try {
			Thread.sleep(5000);
			WindowHandleSupport.getRequiredWindowDriver(driver, "Performer Application");
			System.out.println(driver.getTitle());
			switchToFrame(ApplicationNumberOnText_PL, driver);

			//wait.until(ExpectedConditions.elementToBeClickable(ApplicationNumberOnfield_PL)).click();
			ApplicationNumberOnCRM = wait.until(ExpectedConditions.elementToBeClickable(ApplicationNumberOnText_PL)).getText();
			if(ApplicationNumberOnCRM.equalsIgnoreCase(Application_Number))
			{
				System.out.println("The Correct Application Number is displayed on CRM");
				VerifyAppNumber = true;
			}

		} catch (InterruptedException e) {
			System.out.println("The Application Number is not found on CRM");
		}


		// TODO Auto-generated method stub
		return VerifyAppNumber;
	}

	public boolean VerifyClaimsStatus(String claimID, String claimStatus) throws IOException {

		boolean VerifyClaimsstatus = false;

		try {
			Thread.sleep(5000);
			WindowHandleSupport.getRequiredWindowDriver(driver, "Ophthalmic Claim:");
			System.out.println(driver.getTitle());
			switchToFrame(ClaimsstatusOnText_OP, driver);

			//wait.until(ExpectedConditions.elementToBeClickable(ClaimsstatusOnText_OP)).click();
			String ClaimsStatusonCRM = wait.until(ExpectedConditions.elementToBeClickable(ClaimsstatusOnText_OP)).getText();
			if(ClaimsStatusonCRM.equalsIgnoreCase(claimStatus))
			{

				System.out.println("The Correct claims status is displayed on CRM");
				VerifyClaimsstatus = true;

			}

		} catch (InterruptedException e) {
			System.out.println("The incorrect claims status is not found on CRM");
		}
		return VerifyClaimsstatus;


	}
	
	public boolean VerifyPVNStatus(String CRMClaimStatus, String claimStatus) throws IOException {

		boolean VerifyClaimsstatus = false;

		try {
			/*Thread.sleep(5000);
			WindowHandleSupport.getRequiredWindowDriver(driver, "GOS6 Pre Visit Notification");
			//switchToRequiredIframe(uniqueClmIdValue, driver);
			driver.switchTo().frame("contentIFrame0");
			scrolltoElement(driver, pvnStatusValue);
			String clmStatus = pvnStatusValue.getText();
			WindowHandleSupport.getRequiredWindowDriver(driver, "GOS6 Pre Visit Notification");
			System.out.println(driver.getTitle());
			switchToFrame(pvnStatusValue, driver);

			//wait.until(ExpectedConditions.elementToBeClickable(ClaimsstatusOnText_OP)).click();
			String ClaimsStatusonCRM = wait.until(ExpectedConditions.elementToBeClickable(pvnStatusValue)).getText();*/
			if(CRMClaimStatus.equalsIgnoreCase(claimStatus))
			{

				System.out.println("The Correct claims status is displayed on CRM");
				VerifyClaimsstatus = true;
			}
			else
			{
				System.out.println("The Correct claims status is not matching on CRM");
			}

		} catch (Exception e) {
			System.out.println("The incorrect claims status is not found on CRM");
		}
		return VerifyClaimsstatus;


	}

	public boolean VerifyErrorMessage(String uniquemessageID, String errorMessage,String Key) throws IOException {
		boolean VerifyErrorMessage = false;

		try {
			Thread.sleep(5000);
			WindowHandleSupport.getRequiredWindowDriver(driver, "Logs:");
			System.out.println(driver.getTitle());
			switchToFrame(ErrorMessageOnText_OP, driver);

			//wait.until(ExpectedConditions.elementToBeClickable(ErrorMessageOnText_OP)).click();
			//String ErrorMessageonCRM = wait.until(ExpectedConditions.elementToBeClickable(ErrorMessageOnText_OP)).getAttribute("defaultvalue");
			String ErrorMessageonCRM = wait.until(ExpectedConditions.elementToBeClickable(ErrorMessageOnText_OP)).getText();
			utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", ErrorMessageonCRM,Key,"Actual Error Details IN CRM");
			if(ErrorMessageonCRM.equalsIgnoreCase(errorMessage))
			{

				System.out.println("The Correct Error Message is displayed on CRM");
				VerifyErrorMessage = true;
			}
			driver.close();	
		} catch (InterruptedException e) {
			System.out.println("The incorrect Error Message is not found on CRM");
			String remark = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", Key, "REMARKS");
			String newremark = remark+",Expected Error Messages are not matching with Expected Error Messages.";
			ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", newremark, Key, "REMARKS"); 
		}
		return VerifyErrorMessage;

	}

	public boolean VerifyPaymentLine(String claimID, String claimStatus, String ClaimstatusOnCRM) throws IOException {
		boolean VerifyPaymentLine = false;

		try {
			Thread.sleep(2000);
			WindowHandleSupport.getRequiredWindowDriver(driver, "Ophthalmic Claim:");
			System.out.println(driver.getTitle());
			switchToFrame(Payment_OP, driver);
			if(ClaimstatusOnCRM.equalsIgnoreCase("Rejected"))
			{

				//wait.until(ExpectedConditions.elementToBeClickable(Payment_OP)).click();
				//String ErrorMessageonCRM = wait.until(ExpectedConditions.elementToBeClickable(ErrorMessageOnText_OP)).getAttribute("defaultvalue");
				String PaymentLineOnCRM = wait.until(ExpectedConditions.elementToBeClickable(PaymentOnTextRej_OP)).getText();
				if(!(PaymentLineOnCRM.equalsIgnoreCase("--")||PaymentLineOnCRM.equalsIgnoreCase(" ")))
				{
					System.out.println("The Payment Line is generated");
					VerifyPaymentLine = true;
					//	driver.close();
				}
			}
			else
			{
				String PaymentLineOnCRM = wait.until(ExpectedConditions.elementToBeClickable(PaymentOnText_OP)).getText();
				if(!(PaymentLineOnCRM.equalsIgnoreCase("--")||PaymentLineOnCRM.equalsIgnoreCase(" ")))
				{
					System.out.println("The Payment Line is generated");
					VerifyPaymentLine = true;
					//	driver.close();
				}
			}


			driver.close();


		} catch (InterruptedException e) {
			System.out.println("The Payment Line is not generated");
		}
		return VerifyPaymentLine;
	}

	public String ClaimsStatus(String claimID, Boolean ev_flag, String note) throws InterruptedException, IOException {
		String ClaimsStatusonCRM = null;
		try{
			WindowHandleSupport.getRequiredWindowDriver(driver, "Ophthalmic Claim:");
			switchToFrame(ClaimsstatusOnText_OP, driver);
			if(ev_flag)
			{
			Screenshot.TakeSnap(driver, note);
			}

			//wait.until(ExpectedConditions.elementToBeClickable(ClaimsstatusOnText_OP)).click();
			ClaimsStatusonCRM = wait.until(ExpectedConditions.elementToBeClickable(ClaimsstatusOnText_OP)).getText();
		}
		catch (InterruptedException e) {
			System.out.println("The claims status is not captured");
		}
		return ClaimsStatusonCRM;
	}

	public AdvancedFindResult clickonAdvancedFind() {
		try{
			WindowHandleSupport.getRequiredWindowDriver(driver, "Dashboards: Sales");
			System.out.println(driver.getTitle());
			wait.until(ExpectedConditions.elementToBeClickable(AdvancedFind)).click();			
		}
		catch (Exception e) {
			System.out.println("The advanced find button is not clicked");
		}
		// TODO Auto-generated method stub
		return new AdvancedFindResult(driver) ;
	}

	public boolean isAlertPresent() throws InterruptedException 
	{ 
		try 
		{ 	
			WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
			Thread.sleep(500);
			driver.close();
			Thread.sleep(2000);
		//	driver.switchTo().alert(); 
			return true; 
		}  
		catch (NoAlertPresentException Ex) 
		{ 
			return false; 
		}   
	} 
	
	public void closeScreen(String pageName) throws InterruptedException 
	{ 
		try 
		{ 	
			WindowHandleSupport.getRequiredWindowDriver(driver, pageName);
			Thread.sleep(500);
			driver.close();
			Thread.sleep(2000);
		//	driver.switchTo().alert(); 
			//return true; 
		}  
		catch (NoAlertPresentException Ex) 
		{ 
			//return false; 
		}   
	} 
	public void ClickSpaceBar()
	{
		try{

			//	alert.accept();
			//	driver.findElement(By.xpath("//*[@id='EntityTemplateTab.pcs_log.NoRelationship.SubGridStandard.Mscrm.SubGrid.pcs_log.MainTab-title']/a/span")).sendKeys(Keys.SPACE);
			//	WindowHandleSupport.getRequiredWindowDriver(driver, "Windows");

			driver.switchTo().alert();
			driver.switchTo().alert().accept();
			WindowHandleSupport.getRequiredWindowDriver(driver, "Ophthalmic Claim:");
			/*	Actions action = new Actions(driver);
		//	 action.sendKeys(Keys.SPACE).build().perform();
			 action.sendKeys(Keys.TAB);
			 action.sendKeys(Keys.ENTER).click().build().perform();*/
		}
		catch(Exception e)
		{
			System.out.println("The Alert is not closed ");
		}
	}


	public boolean VerifyPaymentLineCreated(String ClaimstatusOnCRM) throws IOException, AWTException {
		boolean VerifyPaymentLine = false;

		try {
			Thread.sleep(2000);
			/*	WindowHandleSupport.getRequiredWindowDriver(driver, "Ophthalmic Claim:");
			System.out.println(driver.getTitle());
			switchToFrame(Payment_OP, driver);*/
			if(ClaimstatusOnCRM.equalsIgnoreCase("Rejected"))
			{

				//wait.until(ExpectedConditions.elementToBeClickable(Payment_OP)).click();
				//String ErrorMessageonCRM = wait.until(ExpectedConditions.elementToBeClickable(ErrorMessageOnText_OP)).getAttribute("defaultvalue");
				String PaymentLineOnCRM = wait.until(ExpectedConditions.elementToBeClickable(PaymentOnTextRej_OP)).getText();
				if(!(PaymentLineOnCRM.equalsIgnoreCase("--")||PaymentLineOnCRM.equalsIgnoreCase(" ")))
				{
					System.out.println("The Payment Line is generated");
					VerifyPaymentLine = true;
					//driver.close();
					//closeOpenClaim();
					
				}
				else
				{
					System.out.println("The Payment Line is not generated");
					//VerifyPaymentLine = true;
				//	driver.close();
					//closeOpenClaim();
					/*Thread.sleep(2000);
					WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
					System.out.println(driver.getTitle());
					wait.until(ExpectedConditions.elementToBeClickable(AdvancedFind)).click();*/
				}
			}
			else
			{
				String PaymentLineOnCRM = wait.until(ExpectedConditions.elementToBeClickable(PaymentOnText_OP)).getText();
			
				System.out.println("The Payment Line is generated");
				VerifyPaymentLine = true;
				//driver.close();
			
				//closeOpenClaim();
				/*Thread.sleep(2000);
				WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
				System.out.println(driver.getTitle());
				wait.until(ExpectedConditions.elementToBeClickable(AdvancedFind)).click();*/

			}

			Thread.sleep(2000);
			//WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
			//System.out.println(driver.getTitle());
		//	wait.until(ExpectedConditions.elementToBeClickable(AdvancedFind)).click();
		//	driver.close();


		} catch (InterruptedException e) {
			System.out.println("The Payment Line is not generated");
		}
		return VerifyPaymentLine;
	}

	/*public void closeOpenClaim() throws AWTException
	{

		String close = Keys.chord(Keys.ALT,Keys.SPACE, "c");
		ClaimNumberHeader.sendKeys(close);
		try
		{
		Robot robot = new Robot(); 
		robot.delay(2000);
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_F4);
		robot.keyRelease(KeyEvent.VK_ALT);
		robot.keyRelease(KeyEvent.VK_F4);
		robot.delay(2000);
		
		}
		
		catch (AWTException e)
		{
			e.printStackTrace(); 
		}
		
		
	}*/

	public List<String> paymentlineStatus(Boolean ev_flag, String note1, String note2) throws InterruptedException, IOException {
		List<String> paymentLineDetailsList = new ArrayList<String>();
		try{
			WindowHandleSupport.getRequiredWindowDriver(driver, "Ophthalmic Claim:");
			switchToFrame(PaymentLineLink, driver);
			scrolltoElement(driver, PaymentLineLink);

			//wait.until(ExpectedConditions.elementToBeClickable(ClaimsstatusOnText_OP)).click();
			wait.until(ExpectedConditions.elementToBeClickable(PaymentLineLink)).click();
			WindowHandleSupport.getRequiredWindowDriver(driver, "Payment Line:");
			switchToFrame(PaymentLineStatus, driver);
			
			scrolltoElement(driver, PaymentLineStatus);
			
			String paymentLineStatus = wait.until(ExpectedConditions.elementToBeClickable(PaymentLineStatus)).getText();
			paymentLineDetailsList.add(paymentLineStatus);
			if (ev_flag)
			{
			Screenshot.TakeSnap(driver, note1);
			}
			
			scrolltoElement(driver, gmpAmountDue);
			
			String gmpAmtDue = wait.until(ExpectedConditions.elementToBeClickable(gmpAmountDue)).getText().substring(1);
			System.out.println(gmpAmtDue);
			paymentLineDetailsList.add(gmpAmtDue);
			if (ev_flag)
			{
			Screenshot.TakeSnap(driver, note2);
			}
			
			//Suraj G :  Capture the Payment Due date and store in GMP Test Data file 
			String PaymentDuedateExcel = wait.until(ExpectedConditions.elementToBeClickable(PaymentDueDate)).getText();
			System.out.println("The Payment Due date is " +PaymentDuedateExcel );
			utilities.ExcelUtilities.setKeyValueinExcelWithPosition("GMPTESTDATA.xlsx", "ClaimDetails", "PaymentDuedate",PaymentDuedateExcel, 1);
			
			
		}
		catch (InterruptedException e) {
			System.out.println("The paymentLine link is not clickable.");
		}
		driver.close();
		WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
		System.out.println(driver.getTitle());
		return paymentLineDetailsList;
	}
	
	//Added by Akshay for SC module
	public String ClaimsStatus_GPPSC(String claimID, Boolean ev_flag, String note) throws InterruptedException, IOException {
		String ClaimsStatusonCRM = null;
		try{
			WindowHandleSupport.getRequiredWindowDriver(driver, "Standard Claim:");
			switchToFrame(ClaimsstatusOnText_GPPSC, driver);
			if(ev_flag)
			{
			Screenshot.TakeSnap(driver, note);
			}

			//wait.until(ExpectedConditions.elementToBeClickable(ClaimsstatusOnText_OP)).click();
			ClaimsStatusonCRM = wait.until(ExpectedConditions.elementToBeClickable(ClaimsstatusOnText_GPPSC)).getText();
		}
		catch (InterruptedException e) {
			System.out.println("The claims status is not captured");
		}
		return ClaimsStatusonCRM;
	}
	
	//Added by Akshay for SC module
	public List<String> paymentlineStatus_GPPSC(Boolean ev_flag, String note1, String note2) throws InterruptedException, IOException {
		List<String> paymentLineDetailsList = new ArrayList<String>();
		try{
			WindowHandleSupport.getRequiredWindowDriver(driver, "Standard Claim:");
			switchToFrame(PaymentLineLink_GPPSC, driver);
			scrolltoElement(driver, PaymentLineLink_GPPSC);

			//wait.until(ExpectedConditions.elementToBeClickable(ClaimsstatusOnText_OP)).click();
			wait.until(ExpectedConditions.elementToBeClickable(PaymentLineLink_GPPSC)).click();
			WindowHandleSupport.getRequiredWindowDriver(driver, "Payment Line:");
			switchToFrame(PaymentLineStatus_GPPSC, driver);
			
			scrolltoElement(driver, PaymentLineStatus_GPPSC);
			
			String paymentLineStatus = wait.until(ExpectedConditions.elementToBeClickable(PaymentLineStatus_GPPSC)).getText();
			paymentLineDetailsList.add(paymentLineStatus);
			if (ev_flag)
			{
			Screenshot.TakeSnap(driver, note1);
			}
			
			scrolltoElement(driver, gmpAmountDue);
			
			String gmpAmtDue = wait.until(ExpectedConditions.elementToBeClickable(gmpAmountDue)).getText().substring(1);
			System.out.println(gmpAmtDue);
			paymentLineDetailsList.add(gmpAmtDue);
			if (ev_flag)
			{
			Screenshot.TakeSnap(driver, note2);
			}
			
			//Akshay S :  Capture the Payment Due date and store in GMP Test Data file 
			String PaymentDuedateExcel = wait.until(ExpectedConditions.elementToBeClickable(PaymentDueDate)).getText();
			System.out.println("The Payment Due date is " +PaymentDuedateExcel );
			utilities.ExcelUtilities.setKeyValueinExcelWithPosition("GMPTESTDATA.xlsx", "ClaimDetails", "PaymentDuedate",PaymentDuedateExcel, 2);
			
		}
		catch (InterruptedException e) {
			System.out.println("The paymentLine link is not clickable.");
		}
		driver.close();
		WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
		System.out.println(driver.getTitle());
		return paymentLineDetailsList;
	}
	
	public String getDetailsFromResultRecordScreen_GPPSC() throws InterruptedException
	{

		Thread.sleep(4000);
		WindowHandleSupport.getRequiredWindowDriver(driver, "Ophthalmic Claim");
		//switchToRequiredIframe(uniqueClmIdValue, driver);
		driver.switchTo().frame("contentIFrame0");
		scrolltoElement(driver, Claimsstatus_GPPSC);
		String clmStatus = Claimsstatus_GPPSC.getText();
		//ExcelUtilities.setKeyValueinExcel("OPTESTDATA.xlsx", "OPCLAIM", "Claim Status", clmStatus);
		driver.switchTo().defaultContent();
		return clmStatus;
	}
	
	public boolean VerifyPaymentLineCreated_GPPSC(String ClaimstatusOnCRM) throws IOException, AWTException {
		boolean VerifyPaymentLine = false;

		try {
			Thread.sleep(2000);
			/*	WindowHandleSupport.getRequiredWindowDriver(driver, "Ophthalmic Claim:");
			System.out.println(driver.getTitle());
			switchToFrame(Payment_OP, driver);*/
			if(ClaimstatusOnCRM.equalsIgnoreCase("Rejected"))
			{

				//wait.until(ExpectedConditions.elementToBeClickable(Payment_OP)).click();
				//String ErrorMessageonCRM = wait.until(ExpectedConditions.elementToBeClickable(ErrorMessageOnText_OP)).getAttribute("defaultvalue");
				String PaymentLineOnCRM = wait.until(ExpectedConditions.elementToBeClickable(PaymentOnTextRej_GPPSC)).getText();
				if(!(PaymentLineOnCRM.equalsIgnoreCase("--")||PaymentLineOnCRM.equalsIgnoreCase(" ")))
				{
					System.out.println("The Payment Line is generated");
					VerifyPaymentLine = true;
					//driver.close();
					//closeOpenClaim();
					
				}
				else
				{
					System.out.println("The Payment Line is not generated");
					//VerifyPaymentLine = true;
					driver.close();
					//closeOpenClaim();
					/*Thread.sleep(2000);
					WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
					System.out.println(driver.getTitle());
					wait.until(ExpectedConditions.elementToBeClickable(AdvancedFind)).click();*/
				}
			}
			else
			{
				String PaymentLineOnCRM = wait.until(ExpectedConditions.elementToBeClickable(PaymentOnText_GPPSC)).getText();
			
				System.out.println("The Payment Line is generated");
				VerifyPaymentLine = true;
				//driver.close();
			
				//closeOpenClaim();
				/*Thread.sleep(2000);
				WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
				System.out.println(driver.getTitle());
				wait.until(ExpectedConditions.elementToBeClickable(AdvancedFind)).click();*/

			}

			Thread.sleep(2000);
			//WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
			//System.out.println(driver.getTitle());
		//	wait.until(ExpectedConditions.elementToBeClickable(AdvancedFind)).click();
			//driver.close();


		} catch (InterruptedException e) {
			System.out.println("The Payment Line is not generated");
		}
		return VerifyPaymentLine;
	}
	
	public boolean VerifyPaymentLineCreated_GPPQOF(String ClaimstatusOnCRM) throws IOException, AWTException {
		boolean VerifyPaymentLine = false;

		try {
			Thread.sleep(2000);
			WindowHandleSupport.getRequiredWindowDriver(driver, "Monthly QOF Payment");
			/*	WindowHandleSupport.getRequiredWindowDriver(driver, "Ophthalmic Claim:");
			System.out.println(driver.getTitle());
			switchToFrame(Payment_OP, driver);*/
			if(ClaimstatusOnCRM.equalsIgnoreCase("Rejected"))
			{

				//wait.until(ExpectedConditions.elementToBeClickable(Payment_OP)).click();
				//String ErrorMessageonCRM = wait.until(ExpectedConditions.elementToBeClickable(ErrorMessageOnText_OP)).getAttribute("defaultvalue");
				String PaymentLineOnCRM = wait.until(ExpectedConditions.elementToBeClickable(PaymentOnText_GPPQOF)).getText();
				if(!(PaymentLineOnCRM.equalsIgnoreCase("--")||PaymentLineOnCRM.equalsIgnoreCase(" ")))
				{
					System.out.println("The Payment Line is generated");
					VerifyPaymentLine = true;
					//driver.close();
					//closeOpenClaim();
					
				}
				else
				{
					System.out.println("The Payment Line is not generated");
					//VerifyPaymentLine = true;
					driver.close();
					//closeOpenClaim();
					/*Thread.sleep(2000);
					WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
					System.out.println(driver.getTitle());
					wait.until(ExpectedConditions.elementToBeClickable(AdvancedFind)).click();*/
				}
			}
			else
			{
				driver.switchTo().frame("contentIFrame0");
				
				
				//Thread.sleep(3000);
				scrolltoElement(driver, NameField);
				Thread.sleep(2000);
				String name = wait.until(ExpectedConditions.elementToBeClickable(NameFieldTxt)).getText();
				System.out.println("Name: "+name);
				
				//wait.until(ExpectedConditions.elementToBeClickable(PaymentLine)).click();
				//scrolltoElement(driver, PaymentLine);
				String PaymentLineOnCRM = wait.until(ExpectedConditions.elementToBeClickable(PaymentOnText_GPPQOF)).getText();
				
				System.out.println("The Payment Line is generated : "+PaymentLineOnCRM);
				VerifyPaymentLine = true;
				//driver.close();
			
				//closeOpenClaim();
				/*Thread.sleep(2000);
				WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
				System.out.println(driver.getTitle());
				wait.until(ExpectedConditions.elementToBeClickable(AdvancedFind)).click();*/

			}

			Thread.sleep(2000);
			//WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
			//System.out.println(driver.getTitle());
		//	wait.until(ExpectedConditions.elementToBeClickable(AdvancedFind)).click();
			//driver.close();


		} catch (InterruptedException e) {
			System.out.println("The Payment Line is not generated");
		}
		return VerifyPaymentLine;
	}
	
	public List<String> paymentlineStatus_GPPQOF(String fileName, String sheet, String key,int colIndex, Boolean ev_flag, String note1, String note2) throws InterruptedException, IOException {
		List<String> paymentLineDetailsList = new ArrayList<String>();
		try{
			WindowHandleSupport.getRequiredWindowDriver(driver, "Standard Claim:");
			switchToFrame(PaymentLineLink_GPPQOF, driver);
			scrolltoElement(driver, PaymentLineLink_GPPQOF);

			//wait.until(ExpectedConditions.elementToBeClickable(ClaimsstatusOnText_OP)).click();
			wait.until(ExpectedConditions.elementToBeClickable(PaymentLineLink_GPPQOF)).click();
			WindowHandleSupport.getRequiredWindowDriver(driver, "Payment Line:");
			switchToFrame(PaymentLineStatus_GPPSC, driver);
			
			scrolltoElement(driver, PaymentLineStatus_GPPSC);
			
			String paymentLineStatus = wait.until(ExpectedConditions.elementToBeClickable(PaymentLineStatus_GPPSC)).getText();
			paymentLineDetailsList.add(paymentLineStatus);
			if (ev_flag)
			{
			Screenshot.TakeSnap(driver, note1);
			}
			
			scrolltoElement(driver, gmpAmountDue);
			
			String gmpAmtDue = wait.until(ExpectedConditions.elementToBeClickable(gmpAmountDue)).getText().substring(1);
			System.out.println(gmpAmtDue);
			paymentLineDetailsList.add(gmpAmtDue);
			if (ev_flag)
			{
			Screenshot.TakeSnap(driver, note2);
			}
			
			//Akshay S :  Capture the Payment Due date and store in GMP Test Data file 
			String PaymentDuedateExcel = wait.until(ExpectedConditions.elementToBeClickable(PaymentDueDate)).getText();
			System.out.println("The Payment Due date is " +PaymentDuedateExcel );
			utilities.ExcelUtilities.setKeyValueinExcelWithPosition(fileName, sheet, key,PaymentDuedateExcel, colIndex);
			
		}
		catch (InterruptedException e) {
			System.out.println("The paymentLine link is not clickable.");
		}
		driver.close();
		WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
		System.out.println(driver.getTitle());
		return paymentLineDetailsList;
	}

	public String getPaymentlinestatus() {
		String Paymentlinestatus = null;
		try{
			WindowHandleSupport.getRequiredWindowDriver(driver, "Ophthalmic Claim:");
			switchToFrame(PaymentLineLink, driver);
			scrolltoElement(driver, PaymentLineLink);
			wait.until(ExpectedConditions.elementToBeClickable(PaymentLineLink)).click();
			WindowHandleSupport.getRequiredWindowDriver(driver, "Payment Line:");
			switchToFrame(PaymentLineStatus, driver);
			
			scrolltoElement(driver, PaymentLineStatus);			
			Paymentlinestatus = wait.until(ExpectedConditions.elementToBeClickable(PaymentLineStatus)).getText();
			

		}
		catch(Exception e)
		{
			System.out.println("The Payment line status is not captured");
		}
	
		return Paymentlinestatus;
	}

	public void takescreenshots(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, gmpAmountDue);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
			}
	
		public OrgPaymentdetails clickonorgenizationpaymentdetails() {
	try{
		WindowHandleSupport.getRequiredWindowDriver(driver, "Organisation Payment Detail:");
		Actions action = new Actions(driver);
		action.doubleClick(TopArrowbutton).perform();
		wait.until(ExpectedConditions.elementToBeClickable(OrgPaymentdates));
		Actions action2 = new Actions(driver);
		action2.doubleClick(OrgPaymentdates).perform();
	}
	catch(Exception e)
	{
		System.out.println("The Organization payment details is not clicked");
	}
		return new OrgPaymentdetails(driver);	
	}
	
		public String ClaimsStatus(String claimID, Boolean ev_flag, List<String> keys, String note) throws InterruptedException, IOException {
			String ClaimsStatusonCRM = null;
			try{
				WindowHandleSupport.getRequiredWindowDriver(driver, "Ophthalmic Claim:");
				switchToFrame(ClaimsstatusOnText_OP, driver);
				if(ev_flag)
				{
				for (String key: keys)
				Screenshot.TakeSnap(driver, key+"_"+note);
				}

				//wait.until(ExpectedConditions.elementToBeClickable(ClaimsstatusOnText_OP)).click();
				ClaimsStatusonCRM = wait.until(ExpectedConditions.elementToBeClickable(ClaimsstatusOnText_OP)).getText();
			}
			catch (InterruptedException e) {
				System.out.println("The claims status is not captured");
			}
			return ClaimsStatusonCRM;
		}
		
		public List<String> paymentlineStatus(Boolean ev_flag, List<String> keys, String note1, String note2) throws InterruptedException, IOException {
			List<String> paymentLineDetailsList = new ArrayList<String>();
			try{
				WindowHandleSupport.getRequiredWindowDriver(driver, "Ophthalmic Claim:");
				switchToFrame(PaymentLineLink, driver);
				scrolltoElement(driver, PaymentLineLink);

				//wait.until(ExpectedConditions.elementToBeClickable(ClaimsstatusOnText_OP)).click();
				wait.until(ExpectedConditions.elementToBeClickable(PaymentLineLink)).click();
				WindowHandleSupport.getRequiredWindowDriver(driver, "Payment Line:");
				switchToFrame(PaymentLineStatus, driver);
				
				scrolltoElement(driver, PaymentLineStatus);
				
				String paymentLineStatus = wait.until(ExpectedConditions.elementToBeClickable(PaymentLineStatus)).getText();
				paymentLineDetailsList.add(paymentLineStatus);
				if (ev_flag)
				{
				for (String key:keys)
				Screenshot.TakeSnap(driver, key+"_"+note1);
				}
				
				scrolltoElement(driver, gmpAmountDue);
				
				String gmpAmtDue = wait.until(ExpectedConditions.elementToBeClickable(gmpAmountDue)).getText().substring(1);
				System.out.println(gmpAmtDue);
				paymentLineDetailsList.add(gmpAmtDue);
				if (ev_flag)
				{
					for (String key:keys)
						Screenshot.TakeSnap(driver, key+"_"+note2);
				}
				
				scrolltoElement(driver, gmpDueDate);
				
				String gmDuedt = wait.until(ExpectedConditions.elementToBeClickable(gmpDueDate)).getText();
				System.out.println(gmDuedt);
				paymentLineDetailsList.add(gmDuedt);
				
				//Akshay S :  Capture the Payment Due date and store in GMP Test Data file 
				String PaymentDuedateExcel = wait.until(ExpectedConditions.elementToBeClickable(PaymentDueDate)).getText();
				System.out.println("The Payment Due date is " +PaymentDuedateExcel );
				utilities.ExcelUtilities.setKeyValueinExcelWithPosition("GMPTESTDATA.xlsx", "ClaimDetails", "PaymentDuedate",PaymentDuedateExcel, 1);
			/*	if (ev_flag)
				{
					for (String key:keys)
						Screenshot.TakeSnap(driver, key+"_"+note2);
				}*/
				
			}
			catch (InterruptedException e) {
				System.out.println("The paymentLine link is not clickable.");
			}
			driver.close();
			WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
			System.out.println(driver.getTitle());
			return paymentLineDetailsList;
		}

		public void screenshots(String note) throws InterruptedException, IOException {
			scrolltoElement(driver, ApplicationNumberOnText_PL);
			Screenshot.TakeSnap(driver, note+"_1");
			Thread.sleep(1000);
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
			Screenshot.TakeSnap(driver, note+"_2");
			
		}

		public void screenshots_CRMXML(String note) throws InterruptedException, IOException {
			scrolltoElement(driver, ClaimsstatusOnText_OP);
			Screenshot.TakeSnap(driver, note+"_1");
			Thread.sleep(1000);
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
			Screenshot.TakeSnap(driver, note+"_2");
			
		}
		
		public void screenshots_PVNStatus(String note) throws InterruptedException, IOException {
			scrolltoElement(driver, pvnStatus);
			Screenshot.TakeSnap(driver, note+"_1");
			Thread.sleep(1000);
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
			Screenshot.TakeSnap(driver, note+"_2");
			
		}
		
		public void screenshots_CRMErrorXML(String note) throws InterruptedException, IOException {
			scrolltoElement(driver, ErrorMessageOnText_OP);
			Screenshot.TakeSnap(driver, note+"_1");
			Thread.sleep(1000);
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
			Screenshot.TakeSnap(driver, note+"_2");
			
		}
		public AdvancedFindResult deleteEntriesFromEntity() throws InterruptedException {
			//String Handle = driver.getWindowHandle();
			WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
			Thread.sleep(3000);
			driver.switchTo().frame("contentIFrame0");
			driver.switchTo().frame("resultFrame");
			
			//wait.until(ExpectedConditions.elementToBeClickable(resultGrid));
			wait.until(ExpectedConditions.elementToBeClickable(columnGrid)).click();
			scrolltoElement(driver, columnGrid);
			wait.until(ExpectedConditions.elementToBeClickable(gridFixedRow)).click();
			scrolltoElement(driver, gridFixedRow);
			driver.findElement(By.xpath("//table[@id='crmGridControl_gridBar']/tbody")).click();
			driver.findElement(By.xpath("//table[@id='crmGridControl_gridBar']/tbody/tr[1]/td[1]")).click();
			try{
				driver.switchTo().defaultContent();
				wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//div[@id='crmTopBar']//div[@id='Mscrm.Ribbon']"))));
				wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//div[@id='Mscrm.Ribbon']/div[2]"))));
				wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//div[@id='Mscrm.Ribbon']"))));
				wait.until(ExpectedConditions.elementToBeClickable(recordsTab));
				wait.until(ExpectedConditions.elementToBeClickable(deleteOption)).click();
				if(deleteWindow.isDisplayed()){
					Actions moveFocus= new Actions(driver);
					moveFocus.moveToElement(driver.findElement(By.xpath("//div[@id='InlineDialog']"))).build().perform();
					driver.switchTo().frame("InlineDialog_Iframe");
					Thread.sleep(2000);
					wait.until(ExpectedConditions.elementToBeClickable(confirmDelete_btn)).click();
					Thread.sleep(20000);
					//driver.close();
				}

				
			}catch(Exception e){
				System.out.println("Records are not present in entity.");
			}
			/*driver.close();
			driver.switchTo().alert();
			driver.switchTo().alert().accept();
			WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");*/
			return new AdvancedFindResult(driver);
		}
		
		

		public boolean verifyQOFData(String key,List<String> inputData) throws InterruptedException, IOException {
			WindowHandleSupport.getRequiredWindowDriver(driver, "Annual QOF Value");
			driver.switchTo().frame("contentIFrame0");
			boolean isDataVerified;
			List<String> verifiedFields= new ArrayList<String>();
			List<WebElement> fields= Arrays.asList(NameField,finYearField,aspValueField,achValueField,achValueDateField,aspValueDateField);
			List<WebElement> txtFields= Arrays.asList(NameFieldTxt,finYearFieldTxt,aspValueFieldTxt,achValueFieldTxt,achValueDateFieldTxt,aspValueDateFieldTxt);
			int inputCount= inputData.size();
			int fieldCount= txtFields.size();
			for(int i=0;i<inputCount;i++){
				String inputValue= inputData.get(i);
				for(int j=i;j<fieldCount;j++){
					WebElement element= fields.get(j);
					scrolltoElement(driver, element);
					WebElement elementTxt= txtFields.get(j);
					String actualValue= elementTxt.getText().toString();
					if(i==1){
						if(!inputValue.equalsIgnoreCase(actualValue)){
							int endYear= Integer.parseInt(inputValue)+1;
							String strEndYear= Integer.toString(endYear);
							strEndYear= strEndYear.substring(2);
							Pattern p = Pattern.compile("/");
							Matcher m = p.matcher(actualValue);
							if (m.find()){
								inputValue= inputValue+"/"+strEndYear;
							}else{
								p = Pattern.compile("-");
								m = p.matcher(actualValue);
								if (m.find()){
									inputValue= inputValue+"-"+strEndYear;
								}
							}
						}
					}
					if(inputValue.equalsIgnoreCase(actualValue)){
						verifiedFields.add(actualValue);
						break;
					}
				}
			}
			
			if(inputCount==verifiedFields.size()){
				isDataVerified=true;
			}else{
				isDataVerified=false;
			}
			Screenshot.TakeSnap(driver, key+"_records captured");
			closeWindow();
			WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
			/*AdvancedFindResult objAdvancedFindResult= new AdvancedFindResult(driver);
			objAdvancedFindResult.clickonAdvancedFind();*/
			driver.close();
			return isDataVerified;
		}

		public String getGPCodeForCS() throws InterruptedException {
			String GPCode="";
			WindowHandleSupport.getRequiredWindowDriver(driver, "Organisation:");
			driver.switchTo().frame("contentIFrame0");
			scrolltoElement(driver, nationalCodeField);
			GPCode= wait.until(ExpectedConditions.elementToBeClickable(nationalCodeFieldTxt)).getText().toString();
			ExcelUtilities.setKeyValueinExcel("ConfigurationDetails.xlsx", "Config", "GPCode", GPCode);
			System.out.println("National Code is: "+GPCode);
			closeWindow();
			WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
			AdvancedFindResult objAdvancedFindResult= new AdvancedFindResult(driver);
			objAdvancedFindResult.clickonAdvancedFind();
			driver.close();
			return GPCode;
		}

		public void closeWindow() {
			driver.close();
			
		}

		public List<String> getNHSNumbers() throws InterruptedException, IOException {
			List<String> NHSNumbers= new ArrayList<String>();
			driver.switchTo().frame("contentIFrame0");
			driver.switchTo().frame(resultFrame);
			Actions action1 = new Actions(driver);
			action1.doubleClick(CreatedOnArrow).perform();
			Actions action2 = new Actions(driver);
			action2.doubleClick(CreatedOnArrow).perform();
			System.out.println("Control moved to resultFrame.");
			wait.until(ExpectedConditions.elementToBeClickable(resultDataBody));
			wait.until(ExpectedConditions.elementToBeClickable(resultDataArea));
			wait.until(ExpectedConditions.elementToBeClickable(gridTable)).click();
			wait.until(ExpectedConditions.elementToBeClickable(gridTableMain)).click();
			
			for(int i=1; i<=2;i++){
				WebElement element= driver.findElement(By.xpath("//table[@id='gridBodyTable']//tbody/tr["+i+"]/td[4]"));
				scrolltoElement(driver, element);
				String NHSNo= element.getText().toString();
				System.out.println("NHS number is: "+NHSNo);
				NHSNumbers.add(NHSNo);
			}
			closeWindow();
			/*WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
			AdvancedFindResult objAdvancedFindResult= new AdvancedFindResult(driver);
			objAdvancedFindResult.clickonAdvancedFind();*/
/*			CrmHome ObjCrmHome= new CrmHome(driver);
			ObjCrmHome.clickAdvanceFind();*/
			
			return NHSNumbers;
		}

		public boolean verifyHPVData(List<String> inputList, String matchStatus, boolean evidence) throws ParseException, InterruptedException, IOException {
			List<String> matchList= Arrays.asList("4723841687","Maria","19","05/08/1989","G - Gardasil","24/02/2018");
			boolean isVerified= false;
			String actualNHS= inputList.get(0).replaceAll(" ", "").toString();
			String forename= inputList.get(1).toString();
			String surname= inputList.get(2).toString();
			
			String dateofBirth= inputList.get(3).toString();
			Date date= CommonFunctions.convertStringtoCalDate(dateofBirth, "dd/MM/yyyy");
			String strDate= CommonFunctions.convertDateToString(date, "M/d/yyyy");
			
			String vacciType= inputList.get(4).toString();
			
			String vacciDate= inputList.get(5).toString();
			Date date2= CommonFunctions.convertStringtoCalDate(vacciDate, "dd/MM/yyyy");
			String strVacciDate= CommonFunctions.convertDateToString(date2, "M/d/yyyy");
			
			WindowHandleSupport.getRequiredWindowDriver(driver, "Vaccination:");
			driver.switchTo().frame("contentIFrame0");
			
		//	List<WebElement> fields= Arrays.asList(dateOfBirthField,NHSNumberField,vacciDateField,vacciTypeField,patientField,matchStatusField);
			//List<WebElement> textFields= Arrays.asList(dateOfBirthText,NHSNumberFieldText,vacciDateText,vacciTypeText,patientText,matchStatusText);
			
			switch(matchStatus){
			
			case "Matched":
				wait.until(ExpectedConditions.elementToBeClickable(dateOfBirthField));
				String expdateOfBirth= wait.until(ExpectedConditions.elementToBeClickable(dateOfBirthText)).getText().toString();
				if(strDate.equalsIgnoreCase(expdateOfBirth)){
					wait.until(ExpectedConditions.elementToBeClickable(NHSNumberField));
					String expNHS= wait.until(ExpectedConditions.elementToBeClickable(NHSNumberFieldText)).getText().toString();
					if(actualNHS.equalsIgnoreCase(expNHS)){
						wait.until(ExpectedConditions.elementToBeClickable(vacciDateField));
						String expVacciDate= wait.until(ExpectedConditions.elementToBeClickable(vacciDateText)).getText().toString();
						if(strVacciDate.equalsIgnoreCase(expVacciDate)){
							wait.until(ExpectedConditions.elementToBeClickable(vacciTypeField));
							String expVacciType= wait.until(ExpectedConditions.elementToBeClickable(vacciTypeText)).getText().toString();
							if(vacciType.equalsIgnoreCase(expVacciType)){
								wait.until(ExpectedConditions.elementToBeClickable(patientField));
								String expPatient= wait.until(ExpectedConditions.elementToBeClickable(patientText)).getText().toString();
								if(expPatient.equalsIgnoreCase((forename+" "+surname))){
									wait.until(ExpectedConditions.elementToBeClickable(matchStatusField));
									String expMatchStatus= wait.until(ExpectedConditions.elementToBeClickable(matchStatusText)).getText().toString();
									if(expMatchStatus.equalsIgnoreCase("Yes")){
										isVerified=true;
									}
								}/*else if(expPatient.isEmpty()){
									wait.until(ExpectedConditions.elementToBeClickable(matchStatusField));
									String expMatchStatus= wait.until(ExpectedConditions.elementToBeClickable(matchStatusText)).getText().toString();
									if(expMatchStatus.equalsIgnoreCase("No")){
										isVerified=true;
									}
								}*/
							}
						}
					}
				}
				scrolltoElement(driver, dateOfBirthField);
				if(evidence)
				{
				Screenshot.TakeSnap(driver, matchStatus+" records captured");
				}
				driver.close();
				WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find:");
				break;
				
			case "Unmatched":
				wait.until(ExpectedConditions.elementToBeClickable(dateOfBirthField));
				String expdateOfBirth_Unmatched= wait.until(ExpectedConditions.elementToBeClickable(dateOfBirthText)).getText().toString();
				if(strDate.equalsIgnoreCase(expdateOfBirth_Unmatched)){
					wait.until(ExpectedConditions.elementToBeClickable(patientField)).click();
					WebElement patient= driver.findElement(By.xpath("//div[@id='scr_patient']/div"));
					wait.until(ExpectedConditions.elementToBeClickable(patient)).click();
					String expPatient= patient.getText().toString();
					if(expPatient.equalsIgnoreCase("--")){
						wait.until(ExpectedConditions.elementToBeClickable(matchStatusField));
						String expMatchStatus= wait.until(ExpectedConditions.elementToBeClickable(matchStatusText)).getText().toString();
						if(expMatchStatus.equalsIgnoreCase("No")){
							isVerified=true;
						}
					}		
				}
				scrolltoElement(driver, dateOfBirthField);
				if(evidence)
				{
				Screenshot.TakeSnap(driver, matchStatus+" records captured");
				}
				driver.close();
				WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find:");
				break;
			default:
				System.out.println("Status is not found");
				break;
		}
/*			wait.until(ExpectedConditions.elementToBeClickable(dateOfBirthField));
			String expdateOfBirth= wait.until(ExpectedConditions.elementToBeClickable(dateOfBirthText)).getText().toString();
			if(strDate.equalsIgnoreCase(expdateOfBirth)){
				wait.until(ExpectedConditions.elementToBeClickable(NHSNumberField));
				String expNHS= wait.until(ExpectedConditions.elementToBeClickable(NHSNumberFieldText)).getText().toString();
				if(actualNHS.equalsIgnoreCase(expNHS)){
					wait.until(ExpectedConditions.elementToBeClickable(vacciDateField));
					String expVacciDate= wait.until(ExpectedConditions.elementToBeClickable(vacciDateText)).getText().toString();
					if(strVacciDate.equalsIgnoreCase(expVacciDate)){
						wait.until(ExpectedConditions.elementToBeClickable(vacciTypeField));
						String expVacciType= wait.until(ExpectedConditions.elementToBeClickable(vacciTypeText)).getText().toString();
						if(vacciType.equalsIgnoreCase(expVacciType)){
							wait.until(ExpectedConditions.elementToBeClickable(patientField));
							String expPatient= wait.until(ExpectedConditions.elementToBeClickable(patientText)).getText().toString();
							if(expPatient.equalsIgnoreCase((forename+" "+surname))){
								wait.until(ExpectedConditions.elementToBeClickable(matchStatusField));
								String expMatchStatus= wait.until(ExpectedConditions.elementToBeClickable(matchStatusText)).getText().toString();
								if(expMatchStatus.equalsIgnoreCase("Yes")){
									isVerified=true;
								}
							}else if(expPatient.isEmpty()){
								wait.until(ExpectedConditions.elementToBeClickable(matchStatusField));
								String expMatchStatus= wait.until(ExpectedConditions.elementToBeClickable(matchStatusText)).getText().toString();
								if(expMatchStatus.equalsIgnoreCase("No")){
									isVerified=true;
								}
							}
						}
					}
				}
			}*/
			
			return isVerified;
		}

		public boolean verifyContactData(List<String> matchList,boolean evidence) throws InterruptedException, ParseException, IOException {
			String vaccineType= matchList.get(4).toString();
			String vaccineDate= matchList.get(5).toString();
			Date date= CommonFunctions.convertStringtoCalDate(vaccineDate, "dd/MM/yyyy");
			String strVacciDate= CommonFunctions.convertDateToString(date, "M/d/yyyy");
			String actualData="";
			boolean isVerified= false;
			WindowHandleSupport.getRequiredWindowDriver(driver, "Contact:");
			driver.switchTo().frame("contentIFrame0");
			scrolltoElement(driver, vaccineDiv);
			wait.until(ExpectedConditions.elementToBeClickable(vaccineDiv)).click();
			wait.until(ExpectedConditions.elementToBeClickable(vaccineDiv2)).click();
			wait.until(ExpectedConditions.elementToBeClickable(vaccineDiv3)).click();
			wait.until(ExpectedConditions.elementToBeClickable(vaccineDiv4)).click();
			wait.until(ExpectedConditions.elementToBeClickable(vaccineDiv5)).click();
			wait.until(ExpectedConditions.elementToBeClickable(tableMain)).click();
			wait.until(ExpectedConditions.elementToBeClickable(tableBody)).click();
			wait.until(ExpectedConditions.elementToBeClickable(vaccinationType)).click();
			actualData= vaccinationType.getText().toString();
			if(vaccineType.equalsIgnoreCase(actualData)){
				wait.until(ExpectedConditions.elementToBeClickable(vaccinationDate)).click();
				actualData= vaccinationDate.getText().toString();
				if(actualData.equalsIgnoreCase(strVacciDate)){
					isVerified=true;
				}
			}
			if(evidence)
			{
			Screenshot.TakeSnap(driver, "Contact records captured for NHS Number-"+matchList.get(0));
			}
			driver.close();
			WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find:");
			return isVerified;
		}
		
		public List<String> getNHSNumbers_HPV() throws InterruptedException, IOException {
			List<String> NHSNumbers= new ArrayList<String>();
			driver.switchTo().frame("contentIFrame0");
			driver.switchTo().frame(resultFrame);
			Actions action1 = new Actions(driver);
			action1.doubleClick(CreatedOnArrow_HPV).perform();
			Actions action2 = new Actions(driver);
			action2.doubleClick(CreatedOnArrow_HPV).perform();
			System.out.println("Control moved to resultFrame.");
			wait.until(ExpectedConditions.elementToBeClickable(resultDataBody));
			wait.until(ExpectedConditions.elementToBeClickable(resultDataArea));
			wait.until(ExpectedConditions.elementToBeClickable(gridTable)).click();
			wait.until(ExpectedConditions.elementToBeClickable(gridTableMain)).click();
			
			for(int i=1; i<=2;i++){
				WebElement element= driver.findElement(By.xpath("//table[@id='gridBodyTable']//tbody/tr["+i+"]/td[5]"));
				scrolltoElement(driver, element);
				String NHSNo= element.getText().toString();
				System.out.println("NHS number is: "+NHSNo);
				NHSNumbers.add(NHSNo);
			}
			closeWindow();
			WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find:");
			/*WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
			AdvancedFindResult objAdvancedFindResult= new AdvancedFindResult(driver);
			objAdvancedFindResult.clickonAdvancedFind();*/
/*			CrmHome ObjCrmHome= new CrmHome(driver);
			ObjCrmHome.clickAdvanceFind();*/
			
			return NHSNumbers;
		}
		
		public List<String> getNHSNumbers(int count) throws InterruptedException, IOException {
			WebElement element;
			Actions actions = new Actions(driver);
			List<String> NHSNumbers= new ArrayList<String>();
			driver.switchTo().frame("contentIFrame0");
			driver.switchTo().frame(resultFrame);
			actions.doubleClick(CreatedOnArrow_HPV).perform();
			actions.doubleClick(CreatedOnArrow_HPV).perform();
			System.out.println("Control moved to resultFrame.");
			wait.until(ExpectedConditions.elementToBeClickable(resultDataBody));
			wait.until(ExpectedConditions.elementToBeClickable(resultDataArea));
			wait.until(ExpectedConditions.elementToBeClickable(gridTable)).click();
			wait.until(ExpectedConditions.elementToBeClickable(gridTableMain)).click();
			element= driver.findElement(By.xpath("//table[@id='gridBodyTable']//tbody/tr[1]/td[5]"));
			scrolltoElement(driver, element);
			String nhs= element.getText().toString();
			Thread.sleep(3000);
			for(int i=1; i<=count;i++){
				Thread.sleep(3000);
				element= driver.findElement(By.xpath("//table[@id='gridBodyTable']//tbody/tr["+i+"]/td[5]"));
				Thread.sleep(3000);
				scrolltoElement(driver, element);
				String NHSNo= element.getText().toString();
				System.out.println("NHS number is: "+NHSNo);
				NHSNumbers.add(NHSNo);
				if(nhs.equalsIgnoreCase(NHSNo)){
					System.out.println("dynamic numbers are matching.");
				}
			}
			closeWindow();
			WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find:");
			return NHSNumbers;
		}

		public CrmHome closeAdvanceFindWindow()
		{
			try {
				WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
			
				driver.close();
				WindowHandleSupport.getRequiredWindowDriver(driver, "Dashboards: Sales");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return new CrmHome(driver);
			
		}

		
		public String getDetailsFromResultRecordScreen_GPPAdj() throws InterruptedException
		{

			Thread.sleep(4000);
			WindowHandleSupport.getRequiredWindowDriver(driver, "Adjustment:");
			//switchToRequiredIframe(uniqueClmIdValue, driver);
			driver.switchTo().frame("contentIFrame0");
			scrolltoElement(driver, Claimsstatus_GPPAdj);
			String clmStatus = Claimsstatus_GPPAdj.getText();
			//ExcelUtilities.setKeyValueinExcel("OPTESTDATA.xlsx", "OPCLAIM", "Claim Status", clmStatus);
			driver.switchTo().defaultContent();
			return clmStatus;
		}
		
		//Added by Akshay for Adj module
		public String ClaimsStatus_GPPAdj(String claimID, Boolean ev_flag, String note) throws InterruptedException, IOException {
			String ClaimsStatusonCRM = null;
			try{
				WindowHandleSupport.getRequiredWindowDriver(driver, "Standard Claim:");
				switchToFrame(Claimsstatus_GPPAdj, driver);
				if(ev_flag)
				{
				Screenshot.TakeSnap(driver, note);
				}

				//wait.until(ExpectedConditions.elementToBeClickable(ClaimsstatusOnText_OP)).click();
				ClaimsStatusonCRM = wait.until(ExpectedConditions.elementToBeClickable(Claimsstatus_GPPAdj)).getText();
			}
			catch (InterruptedException e) {
				System.out.println("The claims status is not captured");
			}
			return ClaimsStatusonCRM;
		}
		
		public int getRecordCount()
		{
			int records=0;
			driver.switchTo().frame("contentIFrame0");
			driver.switchTo().frame(resultFrame);
			System.out.println("Control moved to resultFrame.");
			boolean ispresent = driver.findElement(By.xpath("//table[@id='gridBodyTable']/tbody/tr/td")).getAttribute("class").contains("ms-crm-List-Empty-Cell");
			System.out.println(ispresent);
			if(!ispresent)
			{
				List<WebElement> rowElements = resultTable.findElements(By.tagName("tr"));
				records = rowElements.size();
				System.out.println("Number of records are: "+records);
			}
			driver.switchTo().defaultContent();
			return records;

		}

		public AdvancedFindResult clickOnPortalRole(String note) throws InterruptedException, IOException {
			WindowHandleSupport.getRequiredWindowDriver(driver, "Portal User Role:");
			driver.switchTo().frame("contentIFrame0");
			wait.until(ExpectedConditions.elementToBeClickable(portalRoleDiv));
			wait.until(ExpectedConditions.elementToBeClickable(portalRoleText)).click();
			Thread.sleep(5000);
			Screenshot.TakeSnap(driver, note);
			return new AdvancedFindResult(driver);
		}
		
		
		public void takeScreenshot (String winTitle, String note) throws InterruptedException, IOException
		{
			WindowHandleSupport.getRequiredWindowDriver(driver, winTitle);
			driver.switchTo().frame("contentIFrame0");
			Thread.sleep(2000);
			Screenshot.TakeSnap(driver, note);
			driver.switchTo().defaultContent();
		}
		

		public String getDetailsFromGOS6PVNResultRecordScreen(boolean evidence, List<String> keys, String note) throws InterruptedException, IOException {
			// TODO Auto-generated method stub
			Thread.sleep(4000);
			WindowHandleSupport.getRequiredWindowDriver(driver, "GOS6 Pre Visit Notification");
			//switchToRequiredIframe(uniqueClmIdValue, driver);
			driver.switchTo().frame("contentIFrame0");
			scrolltoElement(driver, pvnStatusValue);
			String clmStatus = pvnStatusValue.getText();
			if(evidence)
			{
			for (String key: keys)
			Screenshot.TakeSnap(driver, key+"_"+note);
			}
			//ExcelUtilities.setKeyValueinExcel("OPTESTDATA.xlsx", "OPCLAIM", "Claim Status", clmStatus);
			driver.switchTo().defaultContent();
			return clmStatus;
		}

		public int resultRecordCount()
		{
			int records = 0;
			driver.switchTo().frame("contentIFrame0");
			driver.switchTo().frame(resultFrame);
			System.out.println("Control moved to resultFrame.");
			
			boolean ispresent = driver.findElement(By.xpath("//table[@id='gridBodyTable']/tbody/tr/td")).getAttribute("class").contains("ms-crm-List-Empty-Cell");
			System.out.println(ispresent);
			if(!ispresent)
			{
				List<WebElement> rowElements = resultTable.findElements(By.tagName("tr"));
				records = rowElements.size();
				System.out.println(records);
			}
			driver.switchTo().defaultContent();
			return records;

		}
		
		public AdvancedFindResult AddErrorMessage(String uniquemessageID, String errorMessage,String Key, int RowNumber) throws IOException {
			// boolean VerifyErrorMessage = false;
			String ActualErrorMessageOnCRM = null;
			String ErrorMessageonCRM = null;
			String NewActualErrorMessageOnCRM = null;
			int rownumber = 0;
			try {
				Thread.sleep(2000);
				WindowHandleSupport.getRequiredWindowDriver(driver, "Log:");
				System.out.println(driver.getTitle());
				
				switchToFrame(ErrorMessageOnText_OP, driver);

				//wait.until(ExpectedConditions.elementToBeClickable(ErrorMessageOnText_OP)).click();
				//String ErrorMessageonCRM = wait.until(ExpectedConditions.elementToBeClickable(ErrorMessageOnText_OP)).getAttribute("defaultvalue");
				ErrorMessageonCRM = wait.until(ExpectedConditions.elementToBeClickable(ErrorMessageOnText_OP)).getText();
				
				rownumber = RowNumber+1;
				helpers.CommonFunctions.TakeScreenshots(driver, Key, "ValidationError", rownumber);
				ActualErrorMessageOnCRM = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", Key, "Actual Error Details IN CRM");
				if(ActualErrorMessageOnCRM.equals(""))
				{
				utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", ErrorMessageonCRM,Key,"Actual Error Details IN CRM");
				}
				else
				{
					NewActualErrorMessageOnCRM  = ActualErrorMessageOnCRM + "," + ErrorMessageonCRM;
					utilities.ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", NewActualErrorMessageOnCRM,Key,"Actual Error Details IN CRM");
				}
			/*	if(ErrorMessageonCRM.equalsIgnoreCase(errorMessage))
				{

					System.out.println("The Correct Error Message is displayed on CRM");
					VerifyErrorMessage = true;
				}
				else
				{
					System.out.println("Expected Error Messages are not matching with Expected Error Messages");
					String remark = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML", Key, "REMARKS");
					String newremark = remark+",Expected Error Messages are not matching with Expected Error Messages.";
					ExcelUtilities.setKeyValueByPosition("XMLFILEDATA.xlsx", "XML", newremark, Key, "REMARKS"); 
				}*/
			/*	driver.close();	
				Thread.sleep(2000);
				WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
				System.out.println(driver.getTitle());
				driver.switchTo().defaultContent();	*/		
				
			} 
			
			catch (InterruptedException e) {
				e.printStackTrace();
				
			}
			return new AdvancedFindResult(driver);

		}
		
		public String ClaimsStatus(String claimID) throws InterruptedException, IOException {
			String ClaimsStatusonCRM = null;
			try{
				WindowHandleSupport.getRequiredWindowDriver(driver, "Ophthalmic Claim:");
				switchToFrame(ClaimsstatusOnText_OP, driver);

				//wait.until(ExpectedConditions.elementToBeClickable(ClaimsstatusOnText_OP)).click();
				ClaimsStatusonCRM = wait.until(ExpectedConditions.elementToBeClickable(ClaimsstatusOnText_OP)).getText();
			}
			catch (InterruptedException e) {
				System.out.println("The claims status is not captured");
			}
			return ClaimsStatusonCRM;
		}

		public String getClaimCRM() throws IOException {
			String ClaimsstatusCRM = null;
			try {
				WindowHandleSupport.getRequiredWindowDriver(driver, "Ophthalmic Claim:");
				System.out.println(driver.getTitle());
				switchToFrame(ClaimsstatusOnText_OP, driver);

				//wait.until(ExpectedConditions.elementToBeClickable(ClaimsstatusOnText_OP)).click();
				ClaimsstatusCRM = wait.until(ExpectedConditions.elementToBeClickable(ClaimsstatusOnText_OP)).getText();
			

			} catch (InterruptedException e) {
				System.out.println("The claims status is not Captured on CRM");
			}
			return ClaimsstatusCRM;

		}
		
		public String getPVNStatusCRM() throws IOException {
			String PVNstatusCRM = "";
			try {
				WindowHandleSupport.getRequiredWindowDriver(driver, "GOS6 Pre Visit Notification:");
				System.out.println(driver.getTitle());
				
				switchToFrame(pvnStatus, driver);

				//wait.until(ExpectedConditions.elementToBeClickable(ClaimsstatusOnText_OP)).click();
				PVNstatusCRM = wait.until(ExpectedConditions.elementToBeClickable(pvnStatus)).getText();
				
				/*Boolean AlertPresent = isAlertPresent("GOS6 Pre Visit Notification:");
				if(AlertPresent)
				{
					ClickSpaceBar();
				}*/
			

			} catch (InterruptedException e) {
				System.out.println("The claims status is not Captured on CRM");
			}
			return PVNstatusCRM;

		}

		public void XMLCRM_Rejected(String Key , String ClaimID ,boolean evidence) {
			
			boolean flag2 = false;
			try{
			String UniquemessageID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",Key,"UNIQUE MSG ID");
			String ExpErrorMessage = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",Key,"Expected Error Details IN CRM");
			String primaryEntity_MesID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "AdvancedSearch", "selectPrimaryEntity","MessageID");
			String GroupTypeValue_MesID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "AdvancedSearch", "selectoptGroupType","MessageID");
			String FieldValue_MesID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "AdvancedSearch", "selectField","MessageID");
			String ConditionValue_MesID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "AdvancedSearch", "selectfilterCondition","MessageID");
			CrmHome ObjCrmHome = new CrmHome(driver);
			AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity_MesID, GroupTypeValue_MesID,FieldValue_MesID,ConditionValue_MesID, ClaimID);					
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
						if(evidence)
						{
							ObjAdvancedFindResult.screenshots_CRMErrorXML(Key + "ErrorMessage_"+RowNumber);
						}
						driver.close();	
						Thread.sleep(2000);
						WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
						System.out.println(driver.getTitle());
						driver.switchTo().defaultContent();	
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
			}
			catch(Exception e)
			{
				System.out.println("The Error message is not captured on CRM ");
			}
			
		}
		
		
	public void XMLCRM_Rejected_PVN(String Key , String ClaimID ,boolean evidence) {
			
			boolean flag2 = false;
			try{
			String UniquemessageID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",Key,"UNIQUE MSG ID");
			String ExpErrorMessage = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "XML",Key,"Expected Error Details IN CRM");
			String primaryEntity_MesID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "AdvancedSearch", "selectPrimaryEntity","MessageID");
			String GroupTypeValue_MesID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "AdvancedSearch", "selectoptGroupType","MessageID");
			String FieldValue_MesID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "AdvancedSearch", "selectField","MessageID");
			String ConditionValue_MesID = utilities.ExcelUtilities.getKeyValueByPosition("XMLFILEDATA.xlsx", "AdvancedSearch", "selectfilterCondition","MessageID");
			CrmHome ObjCrmHome = new CrmHome(driver);
			AdvancedFilter ObjAdvancedFilter = ObjCrmHome.clickAdvanceFind();
			AdvancedFindResult ObjAdvancedFindResult = ObjAdvancedFilter.enterSearchCriteriaAndClickOnResult(primaryEntity_MesID, GroupTypeValue_MesID,FieldValue_MesID,ConditionValue_MesID, ClaimID);					
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
						if(evidence)
						{
							ObjAdvancedFindResult.screenshots_CRMErrorXML(Key + "ErrorMessage_"+RowNumber);
						}
						driver.close();	
						Thread.sleep(2000);
						WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
						System.out.println(driver.getTitle());
						driver.switchTo().defaultContent();	
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
			}
			catch(Exception e)
			{
				System.out.println("The Error message is not captured on CRM ");
			}
			
		}

	

		public int verifyXMLerrorCRM(String Key) throws IOException {
			boolean flag2 = false;
			int Count = 1;
			try{
			
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
				Count =  helpers.CommonFunctions.VerifyErrorMessage(ExpectedErrors,ActualErrors, Key);
			}
			}
		catch(Exception e)
			{
				System.out.println("The Error is not captured from CRM" +e);
			}

			
		
		return Count;
		}

		public AdvancedFindResult updatePaymentFileStatus(String status) throws InterruptedException, IOException {
			WindowHandleSupport.getRequiredWindowDriver(driver, "Payment File:");
			driver.switchTo().frame("contentIFrame0");
			//updatePaymentRunCount();
			
			wait.until(ExpectedConditions.elementToBeClickable(paymentFileStatusDiv)).click();
			Actions actions = new Actions(driver);
			actions.moveToElement(Search);
	    	actions.click().build().perform();
	    	Thread.sleep(1000);
	    	actions = new Actions(driver);
	    	actions.moveToElement(LookUpMoreRecords);
	    	actions.click().build().perform();
	    	Thread.sleep(1000);
	    	driver.switchTo().defaultContent();
	    	driver.switchTo().frame("InlineDialog_Iframe");
	    	actions.moveToElement(SearchTextBox);
	    	actions.click().build().perform();
	    	//wait.until(ExpectedConditions.elementToBeClickable(SearchTextBox)).click();
	    	wait.until(ExpectedConditions.elementToBeClickable(SearchTextBox)).click();
	    	wait.until(ExpectedConditions.elementToBeClickable(SearchTextBox)).sendKeys(status);
	    	wait.until(ExpectedConditions.elementToBeClickable(SearchTextBox)).sendKeys(Keys.ENTER);
	    	actions = new Actions(driver);
	    	actions.moveToElement(AddButton);
	    	actions.doubleClick().build().perform();
	    	driver.switchTo().defaultContent();
	    	Thread.sleep(2000);
			switchToFrame(SaveButton, driver);
			wait.until(ExpectedConditions.elementToBeClickable(SaveButton)).click();
			Thread.sleep(3000);
			driver.close();
			WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
			return new AdvancedFindResult(driver);
		}

		private void updatePaymentRunCount() {
			scrolltoElement(driver, paymentRunCountDiv);
			Actions focus= new Actions(driver);
			focus.moveToElement(paymentRunCountTextField).build().perform();
			focus.click();
			focus.sendKeys("1").build().perform();
		}

		public boolean updateReceivePOSPaymentFlag(String action) throws InterruptedException {
			boolean isSelected= false;
			WindowHandleSupport.getRequiredWindowDriver(driver, "Ophthalmic Claim Type:");
			driver.switchTo().frame("contentIFrame0");
			Actions focus= new Actions(driver);
			focus.moveToElement(receivePOSPaymentDiv).build().perform();
			focus.click();
			
			if(receivePOSPayment.isSelected()){
				if(action.equalsIgnoreCase("Untick")){
					focus.moveToElement(receivePOSPayment).build().perform();
					focus.click();
					isSelected=false;
				}else{
					isSelected=true;
				}
				
			}else if(!(receivePOSPayment.isSelected())){
				if(action.equalsIgnoreCase("Tick")){
					focus.moveToElement(receivePOSPayment).build().perform();
					focus.click();
					isSelected=true;
				}else{
					isSelected=false;
				}
			}

			
/*			if(receivePOSPayment.isSelected()){
				focus.moveToElement(receivePOSPayment).build().perform();
				focus.click();
				isSelected=true;
			}else if(!(receivePOSPayment.isSelected())){
				isSelected=false;
			}*/
			wait.until(ExpectedConditions.elementToBeClickable(SaveButton)).click();
			return isSelected;
		}
		
		public String clickOnLinkFromFirstRecordWithoutSorting(int rowIndex, int colIndex) throws InterruptedException
		{
			//switchToRequiredIframe(resultTable, driver);
			String recordTitle = null;
			driver.switchTo().frame("contentIFrame0");
			driver.switchTo().frame(resultFrame);
			System.out.println("Control moved to resultFrame.");
			WebElement firstRecordLink = UITableDataSupport.getWebElementFromTable(resultTable, rowIndex, colIndex);
			recordTitle = firstRecordLink.getText();
			System.out.println(recordTitle);

			Actions action = new Actions(driver);
			action.doubleClick(firstRecordLink).perform();
			driver.switchTo().defaultContent();

			return recordTitle;


		}
		
		public AdvancedFindResult deleteEntriesFromEntityWithLongName() throws InterruptedException {
			//String Handle = driver.getWindowHandle();
			WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
			Thread.sleep(3000);
			driver.switchTo().frame("contentIFrame0");
			driver.switchTo().frame("resultFrame");
			
			//wait.until(ExpectedConditions.elementToBeClickable(resultGrid));
			wait.until(ExpectedConditions.elementToBeClickable(columnGrid)).click();
			scrolltoElement(driver, columnGrid);
			wait.until(ExpectedConditions.elementToBeClickable(gridFixedRow)).click();
			scrolltoElement(driver, gridFixedRow);
			driver.findElement(By.xpath("//table[@id='crmGridControl_gridBar']/tbody")).click();
			driver.findElement(By.xpath("//table[@id='crmGridControl_gridBar']/tbody/tr[1]/td[1]")).click();
			try{
				driver.switchTo().defaultContent();
				wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//div[@id='crmTopBar']//div[@id='Mscrm.Ribbon']"))));
				wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//div[@id='Mscrm.Ribbon']/div[2]"))));
				wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//div[@id='Mscrm.Ribbon']"))));
				wait.until(ExpectedConditions.elementToBeClickable(recordsTab));
				wait.until(ExpectedConditions.elementToBeClickable(deleteOption2)).click();
				if(deleteWindow.isDisplayed()){
					Actions moveFocus= new Actions(driver);
					moveFocus.moveToElement(driver.findElement(By.xpath("//div[@id='InlineDialog']"))).build().perform();
					driver.switchTo().frame("InlineDialog_Iframe");
					Thread.sleep(2000);
					wait.until(ExpectedConditions.elementToBeClickable(confirmDelete_btn)).click();
					Thread.sleep(20000);
					//driver.close();
				}
			}catch(Exception e){
				System.out.println("Records are not present in entity.");
			}
			return new AdvancedFindResult(driver);
		}

		public AdvancedFindResult updatePensionEstimateStatusForAnnualIncome(String status,String statusType) throws InterruptedException, IOException {
			Actions actions= new Actions(driver);
			WindowHandleSupport.getRequiredWindowDriver(driver, "Annual Income and Pension");
			driver.switchTo().frame("contentIFrame0");
			wait.until(ExpectedConditions.elementToBeClickable(estimateStatusDiv)).click();
			actions.moveToElement(Search_GPBIS);
	    	actions.click().build().perform();
	    	Thread.sleep(1000);
	    	actions.moveToElement(LookUpMoreRecords);
	    	scrolltoElement(driver, LookUpMoreRecords);
	    	actions.click().build().perform();
	    	Thread.sleep(1000);
	    	driver.switchTo().defaultContent();
	    	driver.switchTo().frame("InlineDialog_Iframe");
	    	actions.moveToElement(SearchTextBox);
	    	actions.click().build().perform();
	    	wait.until(ExpectedConditions.elementToBeClickable(SearchTextBox)).click();
	    	wait.until(ExpectedConditions.elementToBeClickable(SearchTextBox)).sendKeys(status);
	    	wait.until(ExpectedConditions.elementToBeClickable(SearchTextBox)).sendKeys(Keys.ENTER);
			Thread.sleep(2000);
			List<WebElement> statusList= driver.findElements(By.xpath("//table[@id='gridBodyTable']//tbody/tr"));
			gridLoop:
			for(int i=1;i<=statusList.size();i++){
				WebElement elmStatus= driver.findElement(By.xpath("//table[@id='gridBodyTable']//tbody/tr["+i+"]/td[2]/nobr/a"));
				wait.until(ExpectedConditions.elementToBeClickable(elmStatus));
				String strActualStatus= elmStatus.getText().toString();
				
				WebElement elmStatusType= driver.findElement(By.xpath("//table[@id='gridBodyTable']//tbody/tr["+i+"]/td[4]/nobr"));
				wait.until(ExpectedConditions.elementToBeClickable(elmStatusType));
				String strActualStatusType= elmStatusType.getText().toString();
				
				if(strActualStatus.equalsIgnoreCase(status)&&strActualStatusType.equalsIgnoreCase(statusType)){
					WebElement row= driver.findElement(By.xpath("//table[@id='gridBodyTable']//tbody/tr["+i+"]/td[2]/nobr"));
					actions.moveToElement(row);
					actions.doubleClick().build().perform();
					break gridLoop;
				}
			}
	    	driver.switchTo().defaultContent();
	    	Thread.sleep(2000);
			switchToFrame(SaveButton, driver);
			wait.until(ExpectedConditions.elementToBeClickable(SaveButton)).click();
			Thread.sleep(3000);
			return new AdvancedFindResult(driver);
		}

		public AdvancedFindResult changedateclaims() {
			try{
				Thread.sleep(1000);
				switchToFrame(Pointofservice, driver);
				scrolltoElement(driver, Pointofservice);
				Date DatePointofservice = helpers.CommonFunctions.getDateBeforeDays(365);
				String Newpointofservicedate= CommonFunctions.convertDateToString(DatePointofservice, "dd/MM/yyyy");
				System.out.println(Newpointofservicedate);
				wait.until(ExpectedConditions.elementToBeClickable(Pointofservice)).click();
				wait.until(ExpectedConditions.elementToBeClickable(PointofserviceText)).sendKeys(Newpointofservicedate);
				Thread.sleep(2000);
				wait.until(ExpectedConditions.elementToBeClickable(ClaimReceiveddate)).click();
				wait.until(ExpectedConditions.elementToBeClickable(ClaimReceiveddateText)).sendKeys(Newpointofservicedate);
				Thread.sleep(2000);
				wait.until(ExpectedConditions.elementToBeClickable(ClaimSubmitted)).click();
				wait.until(ExpectedConditions.elementToBeClickable(ClaimSubmittedText)).sendKeys(Newpointofservicedate);
				Thread.sleep(2000);
				wait.until(ExpectedConditions.elementToBeClickable(SaveButton)).click();
				Thread.sleep(2000);
				driver.switchTo().defaultContent();
				System.out.println("The CRM date is changed sucessfully");
				driver.close();
				
				
			}
			catch(Exception e)
			{
				System.out.println("The claims date is not updated " +e);
				
			}
			return new AdvancedFindResult(driver);
		}
		
	public AdvancedFindResult deleteEntriesFromEntityWithOptionAtLeftSide() throws InterruptedException {
		WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
		Thread.sleep(3000);
		driver.switchTo().frame("contentIFrame0");
		driver.switchTo().frame("resultFrame");
		wait.until(ExpectedConditions.elementToBeClickable(columnGrid)).click();
		scrolltoElement(driver, columnGrid);
		wait.until(ExpectedConditions.elementToBeClickable(gridFixedRow)).click();
		scrolltoElement(driver, gridFixedRow);
		driver.findElement(By.xpath("//table[@id='crmGridControl_gridBar']/tbody")).click();
		driver.findElement(By.xpath("//table[@id='crmGridControl_gridBar']/tbody/tr[1]/td[1]")).click();
		try{
			driver.switchTo().defaultContent();
			wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//div[@id='crmTopBar']//div[@id='Mscrm.Ribbon']"))));
			wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//div[@id='Mscrm.Ribbon']/div[2]"))));
			wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//div[@id='Mscrm.Ribbon']"))));
			wait.until(ExpectedConditions.elementToBeClickable(recordsTab));
			wait.until(ExpectedConditions.elementToBeClickable(deleteOption3)).click();
			if(deleteWindow.isDisplayed()){
				Actions moveFocus= new Actions(driver);
				moveFocus.moveToElement(driver.findElement(By.xpath("//div[@id='InlineDialog']"))).build().perform();
				driver.switchTo().frame("InlineDialog_Iframe");
				Thread.sleep(2000);
				wait.until(ExpectedConditions.elementToBeClickable(confirmDelete_btn)).click();
				Thread.sleep(20000);
			}
		}catch(Exception e){
			System.out.println("Records are not present in entity.");
		}
		return new AdvancedFindResult(driver);
	}

	public boolean verifyPurchaseAmount(String AVCType,String file, String sheet, String columnName) throws InterruptedException, ParseException {
		boolean isAmtPresent= false;
		WebElement AVCamount= null;
		String expectedValue=null;
		switch(AVCType){
			case "AVC":
				AVCamount=purchaseAmt;
				expectedValue= ExcelUtilities.getKeyValueByPosition(file, sheet, "MonthlyContributionAmt", columnName);
				expectedValue= CommonFunctions.convertToCurrencyFormat(expectedValue, "en","GB",false);
				break;
			case "MPAVC":
				AVCamount=MPAVCAmt;
				expectedValue= ExcelUtilities.getKeyValueByPosition(file, sheet, "MonthlyContributionAmt", columnName);
				expectedValue= CommonFunctions.convertToCurrencyFormat(expectedValue, "en","GB",false);
				break;
			case "ERRBO":
				AVCamount=ERRBOAmt;
				String strAnnualIncome= ExcelUtilities.getKeyValueByPosition(file, sheet, "AnnualIncome", columnName);
				String strContributonRate= ExcelUtilities.getKeyValueByPosition(file, sheet, "ContributionRate", columnName);
				Double contributonRate= Double.parseDouble(strContributonRate);
				Double annualIncome= Double.parseDouble(strAnnualIncome);
				Double monthlyIncome= annualIncome/12;
				
				String firstDate= CommonFunctions.getFirstDayOfMonth("dd/MM/yyyy", 0);
				String lastDate= CommonFunctions.getLastDayOfMonth("dd/MM/yyyy", 0);
				long days= CommonFunctions.getNumberOfDays(firstDate, lastDate, "dd/MM/yyyy");
				Double perDayIncome= monthlyIncome/(days+1);
				String todayDate= CommonFunctions.getTodayDate();
				days= CommonFunctions.getNumberOfDays(todayDate, lastDate, "dd/MM/yyyy");
				monthlyIncome= perDayIncome*days;
				
				expectedValue= Double.toString(monthlyIncome);
				Double value= Double.parseDouble(expectedValue);
				value= (value*contributonRate)/100;
				value= Math.round(value*100.0)/100.0;
				//value= GPPHelpers.convertValueToDecimals(value, 2);
				expectedValue= Double.toString(value);
				expectedValue= CommonFunctions.convertToCurrencyFormat(expectedValue, "en","GB",false);
				Actions actions= new Actions(driver);
				WindowHandleSupport.getRequiredWindowDriver(driver, "Monthly Income");
				driver.switchTo().frame("contentIFrame0");
				actions.moveToElement(AVCamount).build().perform();
				Thread.sleep(2000);
				wait.until(ExpectedConditions.elementToBeClickable(AVCamount));
				String strPurchaseAmt= AVCamount.getText().toString();
				if(!(strPurchaseAmt.isEmpty()||strPurchaseAmt.equalsIgnoreCase("--"))){
					if(!expectedValue.equalsIgnoreCase(strPurchaseAmt)){
						monthlyIncome= annualIncome/12;
						expectedValue= Double.toString(monthlyIncome);
						value= Double.parseDouble(expectedValue);
						value= (value*contributonRate)/100;
						value= Math.round(value*100.0)/100.0;
						//value= GPPHelpers.convertValueToDecimals(value, 2);
						expectedValue= Double.toString(value);
						expectedValue= CommonFunctions.convertToCurrencyFormat(expectedValue, "en","GB",false);
					}
				}	
				break;
			default:
				System.out.println("AVC type is not found "+AVCType);
		}
		Thread.sleep(5000);
		Actions actions= new Actions(driver);
		WindowHandleSupport.getRequiredWindowDriver(driver, "Monthly Income");
		driver.switchTo().frame("contentIFrame0");
		actions.moveToElement(AVCamount).build().perform();
		Thread.sleep(2000);
		wait.until(ExpectedConditions.elementToBeClickable(AVCamount));
		String strPurchaseAmt= AVCamount.getText().toString();
		System.out.println(AVCType+" value is: "+strPurchaseAmt);
		if(!(strPurchaseAmt.isEmpty()||strPurchaseAmt.equalsIgnoreCase("--"))){
			if(expectedValue.equalsIgnoreCase(strPurchaseAmt)){
				isAmtPresent=true;
			}else{
				System.out.println(AVCType+" value is not matching. Expected- "+expectedValue+" and Actual- "+strPurchaseAmt);
			}
		}
		return isAmtPresent;
	}

	public boolean changePortalUserType(String userType) throws InterruptedException, IOException {
		boolean isUpdated= false;
		Actions actions = new Actions(driver);
		WindowHandleSupport.getRequiredWindowDriver(driver, "Portal User:");
		driver.switchTo().defaultContent();
		driver.switchTo().frame("contentIFrame0");
		try{
			wait.until(ExpectedConditions.elementToBeClickable(portalUserTypeDiv)).click();
			wait.until(ExpectedConditions.elementToBeClickable(searchUserType));
			actions.moveToElement(searchUserType);
	    	actions.click().build().perform();
	    	Thread.sleep(1000);
	    	actions.moveToElement(LookUpMoreRecords);
	    	actions.click().build().perform();
	    	Thread.sleep(1000);
	    	driver.switchTo().defaultContent();
	    	driver.switchTo().frame("InlineDialog_Iframe");
	    	actions.moveToElement(SearchTextBox);
	    	actions.click().build().perform();
	    	//wait.until(ExpectedConditions.elementToBeClickable(SearchTextBox)).click();
	    	wait.until(ExpectedConditions.elementToBeClickable(SearchTextBox)).click();
	    	wait.until(ExpectedConditions.elementToBeClickable(SearchTextBox)).sendKeys(userType);
	    	wait.until(ExpectedConditions.elementToBeClickable(SearchTextBox)).sendKeys(Keys.ENTER);
	    	actions = new Actions(driver);
	    	actions.moveToElement(AddButton);
	    	actions.doubleClick().build().perform();
	    	driver.switchTo().defaultContent();
	    	Thread.sleep(2000);
			switchToFrame(SaveButton, driver);
			wait.until(ExpectedConditions.elementToBeClickable(SaveButton)).click();
			Thread.sleep(3000);
			driver.close();
			WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
			isUpdated= true;
		}catch(Exception e){
			isUpdated= false;
		}
		return isUpdated;
	}

	public boolean updatePortalUserRolesValues(List<String> portalUserRoleValues) throws InterruptedException {
		boolean isUpdated= false;
		int updateCount= 0;
		WindowHandleSupport.getRequiredWindowDriver(driver, "Portal User Role:");
		driver.switchTo().defaultContent();
		driver.switchTo().frame("contentIFrame0");
		Actions actions = new Actions(driver);
		int roleValueCount= portalUserRoleValues.size();
		try{
			List<WebElement> lookupFields= Arrays.asList(portalRole,roleOrgDiv);
			List<WebElement> searchFields= Arrays.asList(searchPortalRole,searchOrg);
			//int fieldCount= lookupFields.size();
			try{
				for(int i=0;i<roleValueCount;i++){
					String value= portalUserRoleValues.get(i);
					WebElement element= lookupFields.get(i);
					WebElement searchField= searchFields.get(i);
					Thread.sleep(2000);
					wait.until(ExpectedConditions.elementToBeClickable(element)).click();
					wait.until(ExpectedConditions.elementToBeClickable(searchField));
					actions.moveToElement(searchField);
			    	actions.click().build().perform();
			    	Thread.sleep(1000);
			    	actions.moveToElement(LookUpMoreRecords);
			    	actions.click().build().perform();
			    	Thread.sleep(1000);
			    	driver.switchTo().defaultContent();
			    	driver.switchTo().frame("InlineDialog_Iframe");
			    	actions.moveToElement(SearchTextBox);
			    	actions.click().build().perform();
			    	//wait.until(ExpectedConditions.elementToBeClickable(SearchTextBox)).click();
			    	wait.until(ExpectedConditions.elementToBeClickable(SearchTextBox)).click();
			    	wait.until(ExpectedConditions.elementToBeClickable(SearchTextBox)).sendKeys(value);
			    	wait.until(ExpectedConditions.elementToBeClickable(SearchTextBox)).sendKeys(Keys.ENTER);
			    	actions = new Actions(driver);
			    	actions.moveToElement(AddButton);
			    	actions.doubleClick().build().perform();
			    	driver.switchTo().defaultContent();
			    	Thread.sleep(2000);
					switchToFrame(SaveButton, driver);
					wait.until(ExpectedConditions.elementToBeClickable(SaveButton)).click();
					Thread.sleep(3000);
					updateCount= updateCount+1;
				}
			}catch(IndexOutOfBoundsException iobe){
				System.out.println("Value is not present at index to update Portal User Roles values");
			}catch(Exception e){
				e.printStackTrace();
			}
			if(updateCount==portalUserRoleValues.size()){
				isUpdated= true;
			}
			driver.close();
			WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
		}catch(Exception e){
			isUpdated= false;
		}
		return isUpdated;
	}

	public boolean updateContactDetails(List<String> contactValues) throws InterruptedException {
		boolean isUpdated= false;
		int updateCount= 0;
		WindowHandleSupport.getRequiredWindowDriver(driver, "Contact:");
		driver.switchTo().defaultContent();
		driver.switchTo().frame("contentIFrame0");
		Actions actions = new Actions(driver);
		try{
			List<WebElement> lookupFields= Arrays.asList(performerListTypeDiv,performerTypeDiv);
			List<WebElement> searchFields= Arrays.asList(searchPerformerListType,searchPerformerType);
			int fieldCount= lookupFields.size();
			try{
				for(int i=0;i<fieldCount;i++){
					String value= contactValues.get(i);
					WebElement element= lookupFields.get(i);
					WebElement searchField= searchFields.get(i);
					Thread.sleep(2000);
					wait.until(ExpectedConditions.elementToBeClickable(element)).click();
					wait.until(ExpectedConditions.elementToBeClickable(searchField));
					actions.moveToElement(searchField);
			    	actions.click().build().perform();
			    	Thread.sleep(1000);
			    	actions.moveToElement(LookUpMoreRecords);
			    	actions.click().build().perform();
			    	Thread.sleep(1000);
			    	driver.switchTo().defaultContent();
			    	driver.switchTo().frame("InlineDialog_Iframe");
			    	actions.moveToElement(SearchTextBox);
			    	actions.click().build().perform();
			    	//wait.until(ExpectedConditions.elementToBeClickable(SearchTextBox)).click();
			    	wait.until(ExpectedConditions.elementToBeClickable(SearchTextBox)).click();
			    	wait.until(ExpectedConditions.elementToBeClickable(SearchTextBox)).sendKeys(value);
			    	wait.until(ExpectedConditions.elementToBeClickable(SearchTextBox)).sendKeys(Keys.ENTER);
			    	actions = new Actions(driver);
			    	actions.moveToElement(AddButton);
			    	actions.doubleClick().build().perform();
			    	driver.switchTo().defaultContent();
			    	Thread.sleep(2000);
					switchToFrame(SaveButton, driver);
					wait.until(ExpectedConditions.elementToBeClickable(SaveButton)).click();
					Thread.sleep(3000);
					updateCount= updateCount+1;
				}
			}catch(IndexOutOfBoundsException iobe){
				System.out.println("Value is not present at index to update Contact values");
			}catch(Exception e){
				e.printStackTrace();
			}
			if(updateCount==contactValues.size()){
				isUpdated= true;
			}
			driver.close();
			WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
		}catch(Exception e){
			isUpdated= false;
		}
		return isUpdated;
	}

	public AdvancedFindResult setTodayDateAsStartDateInAssignmentsEntity() throws InterruptedException, IOException, ParseException {
		WindowHandleSupport.getRequiredWindowDriver(driver, "Assignment");
		driver.switchTo().defaultContent();
		driver.switchTo().frame("contentIFrame0");
		scrolltoElement(driver, assignmentStartDateDiv);
		Actions actions= new Actions(driver);
		actions.moveToElement(assignmentStartDateDiv).build().perform();
		wait.until(ExpectedConditions.elementToBeClickable(assignmentStartDateDiv)).click();
		System.out.println("Clicked on assignment start date division under set today date method");
		Thread.sleep(3000);
		actions.moveToElement(dateControl).build().perform();
		wait.until(ExpectedConditions.elementToBeClickable(dateControl)).click();
		Thread.sleep(1000);
		System.out.println("Clicked on date control under set today date method");
		WebElement calendarBox= driver.findElement(By.xpath("//div[contains(@id,'miniCal')]"));
		wait.until(ExpectedConditions.visibilityOf(calendarBox));
		Thread.sleep(2000);
		WebElement todayElement= driver.findElement(By.xpath("//div[@class='ms-crm-MiniCal-TodayBar']"));
		wait.until(ExpectedConditions.elementToBeClickable(todayElement)).click();
		Thread.sleep(1000);
		wait.until(ExpectedConditions.elementToBeClickable(SaveButton)).click();
		Thread.sleep(1000);
		System.out.println("Date has been updated in Assignments entity");
		driver.close();
		WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
		return new AdvancedFindResult(driver);
	}

	public boolean verifySalaryChangeDetails(String fileName, String sheetName, String scriptKey,String amtTierSheet) throws InterruptedException {
		WindowHandleSupport.getRequiredWindowDriver(driver, "Monthly Income");
		driver.switchTo().frame("contentIFrame0");
		boolean isDataMatched= false;
		
		//method written by Akshay on 21 nov 2018 to get monthly employee contribution amount
		String strPensionableIncome= getMonthlyEmployeeContributionAmt(fileName, sheetName, "PensionableIncome", scriptKey);//to be compared
		String strPensionableIncome_Excel= ExcelUtilities.getKeyValueByPosition(fileName, sheetName, "PensionableIncome", scriptKey);
		double pensionableIncome= Double.parseDouble(strPensionableIncome_Excel);
		double expMonthlyIncome= Double.parseDouble(strPensionableIncome_Excel)/12;
		expMonthlyIncome= GPPHelpers.convertValueToDecimals(expMonthlyIncome, 2);
		
		//method written by Akshay on 21 nov 2018 to get monthly employer contribution amount
		String strEmployerContriRate= getMonthlyEmployerContributionAmt(fileName, sheetName, "EmployerContribution", scriptKey,expMonthlyIncome);//to be compared
		
		//method written by Akshay on 21 nov 2018 to get employee pension contribution amount
		String strContriAmt= GPPHelpers.getEmployeePenContriAmtFromTiers(fileName, amtTierSheet, "Range", "Criteria", pensionableIncome, expMonthlyIncome);
		
		WebElement monthlyAmtDiv= driver.findElement(By.cssSelector("div#gpp_estimatedmonthlyincomeamount"));
		wait.until(ExpectedConditions.visibilityOf(monthlyAmtDiv));
		WebElement monthlyAmt= driver.findElement(By.xpath("//div[@id='gpp_estimatedmonthlyincomeamount']/div/span"));
		String strActualMonthlyAmt= monthlyAmt.getText().toString();
		
		if(!strPensionableIncome.equalsIgnoreCase(strActualMonthlyAmt)){
			//strPensionableIncome= getMonthlyEmployeeContributionAmt(fileName, sheetName, "PensionableIncome", scriptKey);
			strPensionableIncome_Excel= ExcelUtilities.getKeyValueByPosition(fileName, sheetName, "PensionableIncome", scriptKey);
			pensionableIncome= Double.parseDouble(strPensionableIncome_Excel);
			expMonthlyIncome= Double.parseDouble(strPensionableIncome_Excel)/12;
			expMonthlyIncome= GPPHelpers.convertValueToDecimals(expMonthlyIncome, 2);
			double elevenMonthValue= expMonthlyIncome*11;
			elevenMonthValue= GPPHelpers.convertValueToDecimals(elevenMonthValue, 2);
			expMonthlyIncome= pensionableIncome-elevenMonthValue;
			expMonthlyIncome= GPPHelpers.convertValueToDecimals(expMonthlyIncome, 2);
			strPensionableIncome= Double.toString(expMonthlyIncome);
			strPensionableIncome= CommonFunctions.convertToCurrencyFormat(strPensionableIncome, "en","GB",false);
			
			strEmployerContriRate= getMonthlyEmployerContributionAmt(fileName, sheetName, "EmployerContribution", scriptKey,expMonthlyIncome);
			strContriAmt= GPPHelpers.getEmployeePenContriAmtFromTiers(fileName, amtTierSheet, "Range", "Criteria", pensionableIncome, expMonthlyIncome);
		}
		
		WebElement monthlyEmployerAmtDiv= driver.findElement(By.cssSelector("div#gpp_estimatedmonthlyemployerpensioncontributi"));
		wait.until(ExpectedConditions.visibilityOf(monthlyEmployerAmtDiv));
		WebElement monthlyEmployerAmt= driver.findElement(By.xpath("//div[@id='gpp_estimatedmonthlyemployerpensioncontributi']/div/span"));
		String strActualMonthlyEmployerAmt= monthlyEmployerAmt.getText().toString();
		
		WebElement monthlyEmployeeDiv= driver.findElement(By.cssSelector("div#gpp_estimatedmonthlyemployeepensioncontributi"));
		wait.until(ExpectedConditions.visibilityOf(monthlyEmployeeDiv));
		WebElement monthlyEmployeeAmt= driver.findElement(By.xpath("//div[@id='gpp_estimatedmonthlyemployeepensioncontributi']/div/span"));
		String strActualMonthlyEmployeeAmt= monthlyEmployeeAmt.getText().toString();
		
		if(strPensionableIncome.equalsIgnoreCase(strActualMonthlyAmt)){
			System.out.println("Expected and Actual monthly income amount is matching. Expected: "+strPensionableIncome+" Actual: "+strActualMonthlyAmt);
			Thread.sleep(1000);
			if(strEmployerContriRate.equalsIgnoreCase(strActualMonthlyEmployerAmt)){
				System.out.println("Expected and Actual monthly employer amount is matching. Expected: "+strEmployerContriRate+" Actual: "+strActualMonthlyEmployerAmt);
				Thread.sleep(1000);
				if(strContriAmt.equalsIgnoreCase(strActualMonthlyEmployeeAmt)){
					System.out.println("Expected and Actual monthly employee contribution amount is matching. Expected: "+strContriAmt+" Actual: "+strActualMonthlyEmployeeAmt);
					isDataMatched= true;
				}else{
					System.out.println("Expected and Actual monthly employee contribution amount is not matching. Expected: "+strContriAmt+" Actual: "+strActualMonthlyEmployeeAmt);
				}
			}else{
				System.out.println("Expected and Actual monthly employer amount is not matching. Expected: "+strEmployerContriRate+" Actual: "+strActualMonthlyEmployerAmt);
			}
		}else{
			System.out.println("Expected and Actual monthly income amount is not matching. Expected: "+strPensionableIncome+" Actual: "+strActualMonthlyAmt);
		}
		
		return isDataMatched;
	}

	private String getMonthlyEmployeeContributionAmt(String fileName, String sheetName, String key, String colName) {
		
		String strPensionableIncome= ExcelUtilities.getKeyValueByPosition(fileName, sheetName, key, colName);
		double expMonthlyIncome= Double.parseDouble(strPensionableIncome)/12;
		expMonthlyIncome= GPPHelpers.convertValueToDecimals(expMonthlyIncome, 2);
		strPensionableIncome= Double.toString(expMonthlyIncome);
		strPensionableIncome= CommonFunctions.convertToCurrencyFormat(strPensionableIncome, "en","GB",false);
		System.out.println("Expected monthly contribution amount is: "+strPensionableIncome);
		return strPensionableIncome;
	}
	
	private String getMonthlyEmployerContributionAmt(String fileName, String sheetName, String key, String colName, double expMonthlyIncome) {
			
		String strEmployerContriRate= ExcelUtilities.getKeyValueByPosition(fileName, sheetName, key, colName);
		double employerContributionRate= Double.parseDouble(strEmployerContriRate);
		double expEmployerContriAmt= (expMonthlyIncome*employerContributionRate)/100;
		expEmployerContriAmt= GPPHelpers.convertValueToDecimals(expEmployerContriAmt, 2);
		strEmployerContriRate= Double.toString(expEmployerContriAmt);
		strEmployerContriRate= CommonFunctions.convertToCurrencyFormat(strEmployerContriRate, "en","GB",false);
		System.out.println("Expected employer monthly contribution amount is: "+strEmployerContriRate);
		return strEmployerContriRate;
	}
			
}
