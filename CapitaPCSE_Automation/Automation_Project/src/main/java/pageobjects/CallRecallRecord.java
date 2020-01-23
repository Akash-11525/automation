package pageobjects;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utilities.ExcelUtilities;;


public class CallRecallRecord {
	WebDriver driver;

	WebDriverWait wait;

	@FindBy(xpath="//*[@id='scr_cohortselectionrule']/div[1]/span[1]")
	WebElement Cohortselectionrule;

	@FindBy(xpath="//span[@id='notescontrol']//a[@tabid='NotesTab']")
	WebElement notesTab;

	@FindBy(xpath="//table[@id='firstRunContent']//td/div/div")
	WebElement notesTxt;

	@FindBy(xpath="//*[@id='scr_patientstatus']/div[1]/span[1]")
	WebElement PortalAction;

	@FindBy(xpath="//input[@title='Patient Ceased - Confirmation']")
	WebElement NotesPatientCeasedTitle;
	
	@FindBy(xpath="//input[@title='Patient Deferred - Confirmation']")
	WebElement NotesPatientDeferTitle;

	@FindBy(css="iframe[class*=notesAttachmentIframe]")
	WebElement iframeNotesAttachment;

	@FindBy(partialLinkText = "Cease Patient Consent Form")
	WebElement partialLinkTextCeasePatientConcentForm;
	
	@FindBy(partialLinkText = "Defer Patient Consent Form")
	WebElement partialLinkTextDeferPatientConcentForm;
	
	@FindBy(xpath="//li[@id='incident|NoRelationship|Form|scr.incident.ApproveCease.Button']//a")
	WebElement ApproveCeaseButton;
	
	@FindBy(xpath="//li[@id='incident|NoRelationship|Form|scr.incident.RejectCease.Button']//a")
	WebElement RejectCeaseButton;
	
	@FindBy(xpath="//li[@id='incident|NoRelationship|Form|Mscrm.Form.incident.NewRecord']//a")
	WebElement NewRecordButton;
	
	@FindBy(id="Tabnav_screening-main")
	WebElement ScreeningTab;
	
	@FindBy(xpath = "//div[@id='scr_patientstatus']/div/span")
	WebElement PortalActionfield; 
	
	@FindBy(id="TabHome")
	WebElement HomeTab;	
	
	@FindBy(xpath="//*[@id='customerid']/div[1]/span[2]")
	WebElement FullName1;
	
	@FindBy(id="scr_acknowledgementdate")
	WebElement Acknowledgementfield;
	
	@FindBy(xpath="//*[@id='scr_acknowledgementdate']/div[1]/span")
	WebElement Acknowledgement;	
	
	@FindBy(id="scr_previouscallrecallprogress")
	WebElement PreviouscallRecallStatusfield;
	
	@FindBy(xpath="//*[@id='scr_previouscallrecallprogress']/div[1]/span")
	WebElement PreviouscallRecallStatus;
	
	@FindBy(id="scr_ceaseapprovaldate")
	WebElement CeaseApprovedatefield;
	
	@FindBy(xpath="//*[@id='scr_ceaseapprovaldate']/div[1]/span")
	WebElement CeaseApprovedate;
	
	@FindBy(id="scr_lastceasedtime")
	WebElement CeasereqDatefield;
	
	@FindBy(xpath="//*[@id='scr_lastceasedtime']/div[1]/span")
	WebElement CeasereqDate;
	
	@FindBy(id="scr_submittedbygp")
	WebElement SubmittedByGPField;
	
	@FindBy(xpath="//*[@id='scr_submittedbygp']/div[1]/span")
	WebElement SubmittedByGP;
	
	@FindBy(id="scr_previousportalaction")
	WebElement Previuosportalactionfield;
	
	@FindBy(xpath="//*[@id='scr_previousportalaction']/div[1]/span")
	WebElement Previuosportalaction;
	
	@FindBy(xpath="//*[@id='customerid']/div[1]/span[1]")
	WebElement Fullname;
	
	@FindBy(id="customerid")
	WebElement FullNamefield;
	
	@FindBy(xpath="//*[@id='ownerid']/div[1]/span[1]")
	WebElement Owner;
	
	@FindBy(xpath="//*[@id='ownerid']")
	WebElement Ownerfield;
	
	@FindBy(xpath="//*[@id='scr_screeningrecord']/div[1]/span[1]")
	WebElement ScreeningRecord;
	
	@FindBy(xpath="//*[@id='scr_screeningrecord']/div[1]/span[1]")
	WebElement ScreeningRecordfield;
	@FindBy(xpath="//*[@id='scr_recalltype']/div[1]/span")
	WebElement CallRecallType;
	
	@FindBy(id="scr_recalltype")
	WebElement CallRecallTypefield;
	@FindBy(xpath="//*[@id='statecode']/div[1]/span")
	WebElement Status;
	
	@FindBy(id="statecode")
	WebElement Statusfield;
	
	@FindBy(xpath="//*[@id='ownerid1']/div[1]/span[1]")
	WebElement Ownernext;
	
	@FindBy(id="ownerid1")
	WebElement Ownernextfield;
	
	@FindBy(id="scr_delayduration")
	WebElement DelayDurationfield;
	
	@FindBy(xpath ="//div[@id='scr_delayduration']/div/span")
	WebElement DelayDuration;
	
	@FindBy(id="scr_delayreason")
	WebElement DelayReasonfield;
	
	@FindBy(xpath="//*[@id='scr_delayreason']/div[1]/span[1]")
	WebElement DelayReason;	

	@FindBy(id="scr_nonresponder")
	WebElement NonResponderfield;
	
	@FindBy(xpath ="//div[@id='scr_nonresponder']/div/span")
	WebElement NonResponder;
	
	@FindBy(xpath ="//*[@id='savefooter_statuscontrol']")
	WebElement Savebutton;
	

	@FindBy(xpath ="//div[@id='scr_callrecallstatus']/div/span")
	WebElement CallRecallprogress;
	
	@FindBy(xpath ="//*[@id='scr_callrecallstatus']")
	WebElement CallRecallprogressfield;
	
	

	public CallRecallRecord(WebDriver driver) {
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 60);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);
		// TODO Auto-generated constructor stub
	}

	public boolean VerifyCohortPriorityRule() throws InterruptedException{
		boolean f1 = false;
		Thread.sleep(5000);
		driver.switchTo().frame("contentIFrame1");
		try {




			String CohortSelectionrule = wait.until(ExpectedConditions.elementToBeClickable(Cohortselectionrule)).getText();
			System.out.println(CohortSelectionrule);
			String FirstCohortPriorityName =  ExcelUtilities.getKeyValueFromExcel("CohortRule.xlsx", "Cohort", "FirstCohortruleName");
			System.out.println(FirstCohortPriorityName);
			if(CohortSelectionrule.equalsIgnoreCase(FirstCohortPriorityName))
			{
				f1 = true;
			}
			else
			{
				f1 = false;
			}


		}
		catch (NoSuchElementException e){
			System.out.println("No element found call Recall summary page.");


		}

		return f1;



	}

	public boolean ExistingCohortRuleVerify() throws InterruptedException{
		boolean f1 = false;
		Thread.sleep(5000);
		driver.switchTo().frame("contentIFrame1");
		try {        

			String CohortSelectionrule = wait.until(ExpectedConditions.elementToBeClickable(Cohortselectionrule)).getText();
			System.out.println(CohortSelectionrule);
			String FirstCohortPriorityName =  ExcelUtilities.getKeyValueFromExcel("CohortRule.xlsx", "Cohort", "FirstCohortruleName");
			System.out.println(FirstCohortPriorityName);
			if(CohortSelectionrule.equals(FirstCohortPriorityName))
			{
				f1 = true;
			}
			else
			{
				f1 = false;
			}


		}
		catch (NoSuchElementException e){
			System.out.println("No element found call Recall summary page.");


		}

		return f1;



	}

	public String CohortRuleApplyOnPatient() throws InterruptedException{
		String CohortSelectionruleName = null;
		Thread.sleep(5000);
		driver.switchTo().frame("contentIFrame1");
		try {        

			CohortSelectionruleName = wait.until(ExpectedConditions.elementToBeClickable(Cohortselectionrule)).getText();
			System.out.println(CohortSelectionruleName);  
			driver.switchTo().defaultContent();

		}
		catch (NoSuchElementException e){
			System.out.println("No element found call Recall summary page.");


		}

		return  CohortSelectionruleName;



	}

	public boolean verifyCeasedNotes() throws InterruptedException{
		boolean f1 = false;
		Thread.sleep(5000);
		driver.switchTo().frame("contentIFrame1");
		try {        

			wait.until(ExpectedConditions.elementToBeClickable(notesTab)).click();

			String NotesTxt =  notesTxt.getText();
			System.out.println(NotesTxt);
			if(NotesTxt.equals("No Notes found."))
			{
				f1 = true;
			}
			else
			{
				f1 = false;
			}


		}
		catch (NoSuchElementException e){
			System.out.println("No element found call Recall summary page.");


		}
		driver.switchTo().defaultContent();
		return f1;



	}

	public boolean verifyCeasedPatientUploadFile() throws InterruptedException{
		boolean f1 = false;
		Thread.sleep(2000);
		driver.switchTo().frame("contentIFrame1");
		try {        
			wait.until(ExpectedConditions.elementToBeClickable(notesTab)).click();
			String title = NotesPatientCeasedTitle.getAttribute("value");

			if (title.contains("Patient Ceased - Confirmation"))
			{
				System.out.println("Patient Ceased Title appeared correctly.");
			}
			else
			{
				System.out.println("Patient Ceased Title is not matching.");
			}

			if (partialLinkTextCeasePatientConcentForm.isDisplayed())
			{
				System.out.println("The uploaded Patinet Consent Form is available");
				f1 = true;
			}
			else
			{
				System.out.println("The uploaded Patinet Consent Form is not available");
			}




		}
		catch (NoSuchElementException e){
			System.out.println("No element found call Recall summary page.");


		}
		driver.switchTo().defaultContent();
		return f1;



	}
	
	
	public boolean verifyDeferPatientUploadFile() throws InterruptedException{
		boolean f1 = false;
		Thread.sleep(2000);
		driver.switchTo().frame("contentIFrame1");
		try {        
			wait.until(ExpectedConditions.elementToBeClickable(notesTab)).click();
			String title = NotesPatientDeferTitle.getAttribute("value");

			if (title.contains("Patient Deferred - Confirmation"))
			{
				System.out.println("Patient Deferred Title appeared correctly.");
			}
			else
			{
				System.out.println("Patient Deferred Title is not matching.");
			}

			if (partialLinkTextDeferPatientConcentForm.isDisplayed())
			{
				System.out.println("The uploaded Patient Consent Form is available");
				f1 = true;
			}
			else
			{
				System.out.println("The uploaded Patient Consent Form is not available");
			}




		}
		catch (NoSuchElementException e){
			System.out.println("No element found call Recall summary page.");


		}

		driver.switchTo().defaultContent();
		return f1;
		


	}

	public Boolean verifyPortalAction(String Portalaction) throws InterruptedException {
		boolean f1 = false;
		Thread.sleep(5000);
		driver.switchTo().frame("contentIFrame1");
		try {

			//driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			//driver.switchTo().frame(iframeNotesAttachment);
			wait.until(ExpectedConditions.elementToBeClickable(PortalActionfield));
			String PortalAction1 = wait.until(ExpectedConditions.elementToBeClickable(PortalActionfield)).getText();
			//String PortalAction1 = CommonFunctions.getSelectedOptionFromDropdown(PortalActionfield);
			System.out.println(PortalAction1); 

			/*String PortalAction1 = wait.until(ExpectedConditions.elementToBeClickable(PortalAction)).getText();
			System.out.println(PortalAction1);*/

			if(PortalAction1.equalsIgnoreCase(Portalaction))
			{
				f1 = true;
			}
			else
			{
				f1 = false;
			}



		}
		catch (NoSuchElementException e){
			System.out.println("No element found call Recall summary page.");


		}
		driver.switchTo().defaultContent();

		return f1;
	}
	
	public CallRecallRecord clickApproveCease() throws InterruptedException
	{
		//driver.switchTo().defaultContent();
		wait.until(ExpectedConditions.elementToBeClickable(ApproveCeaseButton)).click();
		wait.until(ExpectedConditions.elementToBeClickable(NewRecordButton));
		System.out.println("Click on Approve Cease button is successful.");
		Thread.sleep(10000);
		return new CallRecallRecord(driver);
		
	}
	
	public Screening clickOnScreening()
	{
		wait.until(ExpectedConditions.elementToBeClickable(ScreeningTab)).click();
		return new Screening(driver);
		
	}
	
	public CrmHome clickOnCrmHome()
	{
		//driver.switchTo().defaultContent();
		wait.until(ExpectedConditions.elementToBeClickable(HomeTab)).click();
		return new CrmHome(driver);
		
	}

	public Boolean verifyPortalActionAfterCancelCease() throws InterruptedException {
		boolean f1 = false;
		Thread.sleep(3000);
		driver.switchTo().frame("contentIFrame1");
		try {
			
			
	
			wait.until(ExpectedConditions.elementToBeClickable(PortalActionfield));
			Thread.sleep(2000);
			String PortalAction1 = wait.until(ExpectedConditions.elementToBeClickable(PortalAction)).getText();
			System.out.println(PortalAction1);
			
			if(PortalAction1.equalsIgnoreCase(" ") || PortalAction1.equalsIgnoreCase("--") )
			{
				   f1 = true;
			}
			else
			{
				f1 = false;
			}
			
			
		}
		catch (NoSuchElementException e){
			System.out.println("No element found call Recall summary page.");
			
			
		}
	
		return f1;
	}

	public Boolean verifyPortalActionAfterAction(String Text) throws InterruptedException {
		boolean f1 = false;
		
		driver.switchTo().frame("contentIFrame1");
		try {
			
			
	
			wait.until(ExpectedConditions.elementToBeClickable(PortalActionfield));
			String PortalAction1 = wait.until(ExpectedConditions.elementToBeClickable(PortalActionfield)).getText();
			System.out.println(PortalAction1);
			
			if(PortalAction1.equalsIgnoreCase(Text) )
			{
				   f1 = true;
			}
			else
			{
				f1 = false;
			}
			
			
		}
		catch (NoSuchElementException e){
			System.out.println("No element found call Recall summary page.");
			
			
		}
	driver.switchTo().defaultContent();
		return f1;
	}


	public CallRecallRecord StorePatientDetail() {
		driver.switchTo().frame("contentIFrame1");
		
		try {
			
			

			
			wait.until(ExpectedConditions.elementToBeClickable(PreviouscallRecallStatusfield));
			String PreviousCallRecallstatus = wait.until(ExpectedConditions.elementToBeClickable(PreviouscallRecallStatus)).getText();
			System.out.println(PreviousCallRecallstatus);
			utilities.ExcelUtilities.setKeyValueinExcel("CRMTESTOUTDATA.xlsx", "Contact", "Previouscallrecall",PreviousCallRecallstatus);
			
			wait.until(ExpectedConditions.elementToBeClickable(CeaseApprovedatefield));			
			String AcknowledgementActualDate = wait.until(ExpectedConditions.elementToBeClickable(CeaseApprovedate)).getText();
			System.out.println(AcknowledgementActualDate);
			utilities.ExcelUtilities.setKeyValueinExcel("CRMTESTOUTDATA.xlsx", "Contact", "CeaseApprovedate",AcknowledgementActualDate);
		
			wait.until(ExpectedConditions.elementToBeClickable(CeasereqDatefield));			
			String CeaseRequestDate = wait.until(ExpectedConditions.elementToBeClickable(CeasereqDatefield)).getText();
			System.out.println(CeaseRequestDate);
			utilities.ExcelUtilities.setKeyValueinExcel("CRMTESTOUTDATA.xlsx", "Contact", "CeaseRequestdate",CeaseRequestDate);
		
			
		
		}
		catch (NoSuchElementException e)
			{
			System.out.println("The patient data is not stored properly."+e);
			}
	driver.switchTo().defaultContent();
		return new CallRecallRecord (driver); 
	}

	public Boolean VerifyAcknowledgedate() throws InterruptedException {
		boolean AcknowledgementDate = false;
		Thread.sleep(2000);
		driver.switchTo().frame("contentIFrame1");
		try {
			
			
	
			wait.until(ExpectedConditions.elementToBeClickable(Acknowledgementfield));
			
			String AcknowledgementActualDate = wait.until(ExpectedConditions.elementToBeClickable(Acknowledgement)).getText();
			System.out.println(AcknowledgementActualDate);
			 Date myDate = new Date();
             String AcknowledgementExpectedDate = new SimpleDateFormat("M/d/yyyy").format(myDate);
             System.out.println(AcknowledgementExpectedDate);
			
			if(AcknowledgementActualDate.equalsIgnoreCase (AcknowledgementExpectedDate))
			{
				AcknowledgementDate = true;
			}
			else
			{
				AcknowledgementDate = false;
			}
			
			
		}
		catch (NoSuchElementException e){
			System.out.println("No element found call Recall summary page.");
			
			
		}
		driver.switchTo().defaultContent();
		
		return AcknowledgementDate;
	}

	public boolean VerifyPreviouscallstatus(String Text) {
		boolean Previouscallrecallstatus = false;
	
		driver.switchTo().frame("contentIFrame1");
		try {
			
			wait.until(ExpectedConditions.elementToBeClickable(PreviouscallRecallStatusfield));
			String PreviousCallRecallstatus = wait.until(ExpectedConditions.elementToBeClickable(PreviouscallRecallStatus)).getText();
			if(PreviousCallRecallstatus.equalsIgnoreCase (Text))
			{
				Previouscallrecallstatus = true;
			}
			else
			{
				Previouscallrecallstatus = false;
			}
			
			
		}
		catch (NoSuchElementException e){
			System.out.println("No element found call Recall summary page.");
			
			
		}
		driver.switchTo().defaultContent();
	
	
	return Previouscallrecallstatus;
}

	public boolean VerifyGP() {

		boolean GPOnCRM = false;
		
		driver.switchTo().frame("contentIFrame1");
		try {
			
			wait.until(ExpectedConditions.elementToBeClickable(SubmittedByGPField));
			String GPOnCRMAfterCease = wait.until(ExpectedConditions.elementToBeClickable(SubmittedByGP)).getText();
			String GP = testdata.ConfigurationData.GP;
			System.out.println(GP);
			if(GPOnCRMAfterCease.equalsIgnoreCase (GP))
			{
				GPOnCRM = true;
			}
			else
			{
				GPOnCRM = false;
			}
			
			
		}
		catch (NoSuchElementException e){
			System.out.println("No element found call Recall summary page.");
			
			
		}
		driver.switchTo().defaultContent();
	
	
	return GPOnCRM;
	}

	public boolean verifypreviousportalaction(String Text) {
	boolean Previousportalaction = false;
		
		driver.switchTo().frame("contentIFrame1");
		try {
			
			wait.until(ExpectedConditions.elementToBeClickable(Previuosportalactionfield));
			String PRportalaction = wait.until(ExpectedConditions.elementToBeClickable(Previuosportalaction)).getText();
		
			if(PRportalaction.equalsIgnoreCase (Text))
			{
				Previousportalaction = true;
			}
			else
			{
				Previousportalaction = false;
			}
			
			
		}
		catch (NoSuchElementException e){
			System.out.println("No element found call Recall summary page.");
			
			
		}
		driver.switchTo().defaultContent();
	
	
	return Previousportalaction;
	}

	public boolean Verifyceasebydate() 
	{
			boolean Ceasebyrequestdate = false;		
		driver.switchTo().frame("contentIFrame1");
		try {
			
			wait.until(ExpectedConditions.elementToBeClickable(CeasereqDatefield));
			String Ceasebydate = wait.until(ExpectedConditions.elementToBeClickable(CeasereqDate)).getText();
			Date myDate = new Date();
            String Ceaserequestdate = new SimpleDateFormat("M/dd/yyyy").format(myDate);
            System.out.println(Ceaserequestdate);
			if(Ceasebydate.contains(Ceaserequestdate))
			{
				Ceasebyrequestdate = true;
			}
			else
			{
				Ceasebyrequestdate = false;
			}
			
			
		}
		catch (NoSuchElementException e){
			System.out.println("No element found call Recall summary page.");
			
			
		}
		driver.switchTo().defaultContent();
	
	
	return Ceasebyrequestdate;
	}

	public int VerifyMandatoryfield() throws InterruptedException {
		int countMandatoryfield =0;		
		driver.switchTo().frame("contentIFrame1");
		ArrayList<String> mylist = new ArrayList<String>();
		
		wait.until(ExpectedConditions.elementToBeClickable(FullNamefield));
		Thread.sleep(2000);
		String FullNameCallRecall = Fullname.getText();
		System.out.println(FullNameCallRecall);
		mylist.add(FullNameCallRecall);
		
		wait.until(ExpectedConditions.elementToBeClickable(Ownerfield));
		Thread.sleep(2000);
		String OwnerCallrecall = Owner.getText();
		System.out.println(OwnerCallrecall);
		mylist.add(OwnerCallrecall);
		
		wait.until(ExpectedConditions.elementToBeClickable(ScreeningRecordfield));
		String ScreeningCallrecall = wait.until(ExpectedConditions.elementToBeClickable(ScreeningRecord)).getText();
		System.out.println(ScreeningCallrecall);
		mylist.add(ScreeningCallrecall);
		
		wait.until(ExpectedConditions.elementToBeClickable(CallRecallTypefield));
		String TypeCallrecall = wait.until(ExpectedConditions.elementToBeClickable(CallRecallType)).getText();
		System.out.println(TypeCallrecall);
		mylist.add(TypeCallrecall);
		
		wait.until(ExpectedConditions.elementToBeClickable(Statusfield));
		String StatusCallrecall = wait.until(ExpectedConditions.elementToBeClickable(Status)).getText();
		System.out.println(StatusCallrecall);
		mylist.add(StatusCallrecall);
		
		wait.until(ExpectedConditions.elementToBeClickable(Ownernextfield));
		String OwnernextCallrecall = wait.until(ExpectedConditions.elementToBeClickable(Ownernext)).getText();
		System.out.println(OwnernextCallrecall);
		mylist.add(OwnernextCallrecall);
		
		try {
			for (String ExpectedResult:mylist){
				if(ExpectedResult.startsWith("-")||ExpectedResult.isEmpty()){
					countMandatoryfield++;
				}
				else
				{
					countMandatoryfield = 0;
				}
			}
			
			
			
			
		}
		catch (NoSuchElementException e){
			System.out.println("No element found call Recall summary page.");
			
			
		}
		driver.switchTo().defaultContent();
	
	
	return countMandatoryfield;
	}

	public Boolean VerifyDelayDuration_Reason(String Text, String Text1) throws InterruptedException {
		
		boolean Delayduration_reason = false;
		try {
			driver.switchTo().frame("contentIFrame1");
			wait.until(ExpectedConditions.elementToBeClickable(DelayDurationfield));
			String Delayduration = wait.until(ExpectedConditions.elementToBeClickable(DelayDuration)).getText();
			wait.until(ExpectedConditions.elementToBeClickable(DelayReasonfield));
			String DelayReasonBeforecease = wait.until(ExpectedConditions.elementToBeClickable(DelayReason)).getText();
			Thread.sleep(4000);
			if(Delayduration.equalsIgnoreCase(Text))
			{
				if (DelayReasonBeforecease.equalsIgnoreCase(Text1))
				{
					Delayduration_reason=true;
					
				}
			}
		}
		catch (NoSuchElementException e){
			System.out.println("No element found call Recall summary page ( Delay duration and Reason field ).");
		
	}
		driver.switchTo().defaultContent();
		return Delayduration_reason;

	


}

	public Boolean ClickonNonResponder() throws InterruptedException {
		boolean NonResponderOncallRecall = false;
		try {
			driver.switchTo().frame("contentIFrame1");
			wait.until(ExpectedConditions.elementToBeClickable(NonResponderfield));
			String NonResponderValue  = wait.until(ExpectedConditions.elementToBeClickable(NonResponder)).getText();
			if(NonResponderValue.equalsIgnoreCase("No"))
			{
		//	wait.until(ExpectedConditions.elementToBeClickable(NonResponder)).click();
			Actions actions = new Actions(driver);
			actions.moveToElement(NonResponder);
			actions.click().build().perform();
			Thread.sleep(4000);
			}
			//wait.until(ExpectedConditions.elementToBeClickable(NonResponder)).click();
			NonResponderValue  = wait.until(ExpectedConditions.elementToBeClickable(NonResponder)).getText();
			
			if(NonResponderValue.equalsIgnoreCase("Yes"))
			{
				NonResponderOncallRecall = true;
			}
		
			
		}
		catch (NoSuchElementException e){
			System.out.println("No element found call Recall summary page for Non Responder field.");
		}
		driver.switchTo().defaultContent();
		return NonResponderOncallRecall;
	}

	public CallRecallRecord clickonSave() throws InterruptedException {
		try {
			driver.switchTo().frame("contentIFrame1");
			wait.until(ExpectedConditions.elementToBeClickable(Savebutton));
		//	wait.until(ExpectedConditions.elementToBeClickable(NonResponder)).click();
			Actions actions = new Actions(driver);
			actions.moveToElement(Savebutton);
			actions.doubleClick().build().perform();
			Thread.sleep(4000);
	}
		
		catch (NoSuchElementException e){
			System.out.println("No element found call Recall summary page for Save button.");
		}
		driver.switchTo().defaultContent();
		return new CallRecallRecord (driver);
}

	public Boolean verifycallrecallType(String Text) {
		boolean callrecallType = false;
		
		driver.switchTo().frame("contentIFrame1");
		try {
			
			
	
			wait.until(ExpectedConditions.elementToBeClickable(CallRecallTypefield));
			String CallType = wait.until(ExpectedConditions.elementToBeClickable(CallRecallType)).getText();
			System.out.println(CallType);
			
			if(CallType.equalsIgnoreCase(Text) )
			{
				callrecallType = true;
			}
				
			
		}
		catch (NoSuchElementException e){
			System.out.println("The element is not found for Call Recall type field.");
			
			
		}
	driver.switchTo().defaultContent();
		return callrecallType;
	}

	public Boolean verifyCallRecallProgress(String Text) {
	boolean CallRecallProgress = false;
		
		driver.switchTo().frame("contentIFrame1");
		try {
	
			wait.until(ExpectedConditions.elementToBeClickable(CallRecallprogressfield));
			String Callprogress = wait.until(ExpectedConditions.elementToBeClickable(CallRecallprogress)).getText();
			System.out.println(Callprogress);
			
			if(Callprogress.equalsIgnoreCase(Text) )
			{
				CallRecallProgress = true;
			}
				
			
		}
		catch (NoSuchElementException e){
			System.out.println("The element is not found for Call Recall type field.");
			
			
		}
	driver.switchTo().defaultContent();
		return CallRecallProgress;
	}

	public CallRecallRecord clickRejectCease() throws InterruptedException

	{
		//driver.switchTo().defaultContent();
		wait.until(ExpectedConditions.elementToBeClickable(RejectCeaseButton)).click();
		wait.until(ExpectedConditions.elementToBeClickable(NewRecordButton));
		System.out.println("Click on Reject Cease button is successful.");
		Thread.sleep(2000);
		return new CallRecallRecord(driver);
		
	}
	
	




	public Boolean VerifyCallRecallstatus(String Text) {
		Boolean callrecallstatus = false;
		driver.switchTo().frame("contentIFrame1");
		
		try{	
			wait.until(ExpectedConditions.elementToBeClickable(Statusfield));
			String StatusCallrecall = wait.until(ExpectedConditions.elementToBeClickable(Status)).getText();
			System.out.println(StatusCallrecall);	
			if(StatusCallrecall.equalsIgnoreCase(Text))
			{
				callrecallstatus = true;
			}
		}
		catch (NoSuchElementException e)
		{
			System.out.println("The element is not found for Call Recall type field.");	
		}
		driver.switchTo().defaultContent();
		return callrecallstatus;
		
	}
	
	
	
}




