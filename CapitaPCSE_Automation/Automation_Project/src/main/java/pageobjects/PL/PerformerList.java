
package pageobjects.PL;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Screenshot;
import helpers.Support;
import pageobjects.PerformerPL.ChangeDetails;
import pageobjects.PerformerPL.EmpDetails;
import pageobjects.PerformerPL.HourRetirement;
import pageobjects.PerformerPL.Localoffice;
import pageobjects.PerformerPL.PerformerType;
import pageobjects.PerformerPL.PersonalDetails_performer;
import pageobjects.PerformerPL.WithdrawPerformer;
import pageobjects.ProcessPL.CaseofficerReview;
import pageobjects.ProcessPL.FacetoFaceAppointment;
import pageobjects.ProcessPL.MangerApproval;
import pageobjects.ProcessPL.NetTeamCheck;
import pageobjects.ProcessPL.PCSECheck;
import pageobjects.ProcessPL.ThirdPartyCheck;

public class PerformerList extends Support
{
	WebDriver driver;
	WebDriverWait wait;


	@FindBy(id="PCSEChecks")
	WebElement PCSECheck;	
	
	@FindBy(id="ThirdPartyChecks")
	WebElement ThirdPartyCheck;
	
	@FindBy(id="SelectPerformer")
	WebElement SelectPerfomerdropdown;

	@FindBy(xpath="//button[@class='btn btn-success']")
	WebElement Submitbutton;

	@FindBy(xpath="//*[@id='dvViewandEdit']/div/div/div[3]/input")
	WebElement ClickOnOK;
	
/*	@FindBy(xpath="//*[@id='dvViewandEdit']/div/div/div[3]/a")
	WebElement ClickOnOK;*/

	@FindBy(xpath="//div[@class='pcse-modal-body']")
	WebElement PopUpAfterCreateApp;
	

	@FindBy(xpath="//table[@id='statuses']//tr[1]/td[2]")
	WebElement ApplicationStatus;
	

	@FindBy(xpath="//table[@id='statuses']//tr[1]/td[1]")
	WebElement ApplicationNumber;
	
	@FindBy(xpath="//div[@class='modal-content']")
	WebElement Withdrawbox;
	
	@FindBy(id="reasonForWithdrawl")
	WebElement WithdrawTextbox;
	
	@FindBy(id="btnReason")
	WebElement OkonWithdrawApp;
	
	@FindBy(xpath="//table[@id='statuses']//tr[1]/td[6]/input")
	WebElement Viewactiononresult;
	
	@FindBy(xpath="//*[@class='confirmation-buttons text-center']/div/a[2]/i")
	WebElement ContinueButton;
	
	@FindBy(xpath="//table[@id='statuses']//tr[1]/td[6]/input")
	WebElement EditButton;

	@FindBy(xpath="//table[@id='statuses']")
	WebElement mytable;
	
	@FindBy(id="NetTeamAppointment")
	WebElement NetTeamCheck;
	
	@FindBy(id="FaceToFaceChecks")
	WebElement FaceToFaceApp;	
	
	@FindBy(name="CaseOfficerReview")
	WebElement Caseofficereviewtab;
	
	@FindBy(name="NHSEFacetoFaceChecks")
	WebElement ManagerApproval;
	
	@FindBy(id="PersonalDetails")
	WebElement Personal_performer;
	
	@FindBy(name="IsConfirmGMCGDC")
	WebElement ConfimredGMC;
	
	@FindBy(name="isConfirmedOnGMC")
	WebElement ConfimredGMC_PerformerType;
	
	@FindBy(name="PerformerType")
	WebElement PerformerTypeTab;
	
	@FindBy(id="HourRetirement")
	WebElement HourRetirementTab;

	@FindBy(id="EmploymentDetails")
	WebElement EmpDetailsTab;
	
	@FindBy(xpath="//table[@id='tblPracticeUsertable']")
	WebElement PraticeUsertable;
	
	@FindBy(xpath="//*[@id='tblPracticeUsertable_next']/a")
	WebElement PraticeUsertableNext;
	
	@FindBy(xpath="//*[@id='BtnGainingAcceptChange']/input")
	WebElement AcceptStartdate;
	
	@FindBy(id="LocalOfficeDetails")
	WebElement LocalofficeTab;
	
	@FindBy(id="WithdrawalFromPL")
	WebElement WithdrawPLTab;
	
	
	

	public PerformerList(WebDriver driver) 

	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}



	public NewAppPersonalDetail clickonOK() throws InterruptedException {
		try
		{

		//	wait.until(ExpectedConditions.elementToBeClickable(ClickOnOK)).click();
			
			wait.until(ExpectedConditions.elementToBeClickable(ClickOnOK));
			
			Actions action = new Actions(driver);
			action.moveToElement(ClickOnOK);
			action.click().build().perform(); 
			helpers.CommonFunctions.PageLoadExternalwait(driver);
		
	}
		catch (NoSuchElementException e)
		{
			System.out.println("The OK button is not found After clicking on create application."+e);
		}


		return new NewAppPersonalDetail(driver);
	}

	public boolean VerifyPopUp() {

		boolean Popupdiolgue = false;
		try{
			wait.until(ExpectedConditions.elementToBeClickable(PopUpAfterCreateApp));
			if(PopUpAfterCreateApp.isDisplayed())
			{
				Popupdiolgue = true;
			}
		}
		catch (NoSuchElementException e)
		{
			System.out.println("The Pop Up Diolague box is not displayed ."+e);
		}


		return Popupdiolgue;

	}
	
	



	public int VerifyPopupText() {
		int count = 0;


		List<WebElement> ParagraphText = driver.findElements(By.xpath("//div[@class='pcse-modal-body']//p"));	
		System.out.println(ParagraphText.size());
		int i = 1;
		for (WebElement ParaText : ParagraphText)
		{  	
			String ActualPara =ParaText.getText();		
			System.out.println(ActualPara);
			String ExpectedPara = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "PopUP_CreateApp", "P"+i+"",1);
			System.out.println(ExpectedPara);
			i++;
			if(ExpectedPara.equalsIgnoreCase(ActualPara))
			{
				count = 0;		
			}
			else 
			{
				count = 1;
			}

		}

		return count;

	}



	public CreateNewApp clickonResult() {
	driver.findElement(By.xpath("//*[@id='0']/td[6]/a"));
	return new CreateNewApp (driver);
	
	}



	public Boolean verifyButtonEnabled(String Text) {
	boolean buttonenabledflag = false;
	try {
		Thread.sleep(3000);
	       List<WebElement> buttons=driver.findElements(By.xpath("//button[@type='button']"));
           System.out.println("total buttons "+buttons.size());
          for (WebElement button : buttons)
          {                   
        	//  String ButtonValue = wait.until(ExpectedConditions.elementToBeClickable(button)).getText();
        	   String ButtonValue = button.getText(); 
        	 
        	 
        	  if (ButtonValue.equalsIgnoreCase(Text))
        		  if(button.isEnabled())
        	  {
        		 
        			  buttonenabledflag = true;
        			  
        		  break;
        		  
        	  }
          }
           

	}
	catch (Exception e)
	{
		e.printStackTrace();
	}
	return buttonenabledflag; 
	
	}



	public String getApplicationNumberwithstatus(String Status) {
		String Application_Number = null;
		try{
			helpers.CommonFunctions.PageLoadExternalwait(driver);
		String AppStatus = wait.until(ExpectedConditions.elementToBeClickable(ApplicationStatus)).getText();

		if(AppStatus.equalsIgnoreCase(Status))
		{
			Application_Number = ApplicationNumber.getText();
			
		}
			
				
		}
		// TODO Auto-generated method stub
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return Application_Number; 
	}





	public PerformerList EnterWithdrawapplicationDetails() {
		try{
			List<WebElement> ContentDialogueBoxes = driver.findElements(By.xpath("//div[@class='modal-content']"));
	           System.out.println("total buttons "+ContentDialogueBoxes.size());
	          for (WebElement ContentDialogueBoxe : ContentDialogueBoxes)
	          {
	        	  Thread.sleep(2000);
	        	 if(WithdrawTextbox.isDisplayed()) 
	        	 {
	        		 wait.until(ExpectedConditions.elementToBeClickable(WithdrawTextbox)).sendKeys("Automation Purpose"); 
	        		 wait.until(ExpectedConditions.elementToBeClickable(OkonWithdrawApp)).click(); 
	        		  Thread.sleep(1000);
	        		 break;
	        	 }
	          }
	          Thread.sleep(2000);
	      	Actions action = new Actions(driver);
			action.moveToElement(ContinueButton);
			action.doubleClick().build().perform();
	     	// wait.until(ExpectedConditions.elementToBeClickable(ContinueButton)).click(); 
	          
			/*	List<WebElement> Radiobuttons=driver.findElements(By.xpath("//button[@class='btn btn-primary']"));
				for (WebElement Radiobutton : Radiobuttons)
				{  
					String RadioValue = Radiobutton.getText(); 
					System.out.println(RadioValue);
					if (RadioValue.equalsIgnoreCase("Yes"))
					{
						Radiobutton.click();
						break;
					}
				}*/
			
		}
		// TODO Auto-generated method stub
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return new PerformerList(driver); 
	}



	public Boolean verfiyviewaction(String applicationNumber2, String LinkName) {
		boolean Viewlink = false;
		try {
		String Viewaction	=  wait.until(ExpectedConditions.elementToBeClickable(Viewactiononresult)).getAttribute("value"); 
		String ApplicationAfterwithdraw	=  wait.until(ExpectedConditions.elementToBeClickable(ApplicationNumber)).getText(); 
		
		if(applicationNumber2.equalsIgnoreCase(ApplicationAfterwithdraw) && Viewaction.equalsIgnoreCase(LinkName))	
		{
			Viewlink = true;
		}
		}
		
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return Viewlink; 
	}



	public CreateNewApp clickonEdit() {
		try{
			scrolltoElement(driver, EditButton);
			
			Actions action = new Actions(driver);
			action.moveToElement(EditButton);
			action.doubleClick().build().perform();
			helpers.Support.PageLoadExternalwait(driver);
		}
		catch(Exception e)
		{
			System.out.println("The edit is not clicked on performer list");
		}
		return new CreateNewApp(driver) ;
	}


	public PerformerList clickonEdit_draft() {
		try{
			scrolltoElement(driver, EditButton);
			
			Actions action = new Actions(driver);
			action.moveToElement(EditButton);
			action.doubleClick().build().perform();
		//	ProcessorPLHelpers.PageLoadExternalwait();
			helpers.CommonFunctions.PageLoadExternalwait(driver);
		}
		catch(Exception e)
		{
			System.out.println("The edit is not clicked on performer list");
		}
		return new PerformerList(driver) ;
	}


	public void Applicationstatus(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, ApplicationStatus);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");

		
	}



	public boolean getapplicationstatus(String Status) {
		String Appstatus = null;
		boolean draftstatus = false;
		try{
			String firstcolumndata = driver.findElement(By.xpath("//table[@id='statuses']//tr[1]/td[1]")).getText().trim();
					
			if(!firstcolumndata.equalsIgnoreCase("No data found."))
			{
			scrolltoElement(driver, ApplicationStatus);
			wait.until(ExpectedConditions.elementToBeClickable(ApplicationStatus));
			Appstatus = wait.until(ExpectedConditions.elementToBeClickable(ApplicationStatus)).getText();
			if(Appstatus.equalsIgnoreCase(Status))
					{
						draftstatus = true;
					}
					
			}
			
		}
		catch(Exception e)
		{
			System.out.println("The Application status is not captured");
		}
		
		return draftstatus;
	}



	public void screenshotAppStatus_portal(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, ApplicationStatus);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		
	}



	public void Screenshots(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, ApplicationNumber);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		
	}
	
	public PCSECheck ClickonPCSECheck() {
		try{
			helpers.Support.PageLoadExternalwait_ProcessApp(driver);
			Actions actions = new Actions(driver);
	    	actions.moveToElement(PCSECheck);
	    	actions.doubleClick().build().perform();
	    	helpers.Support.PageLoadExternalwait_ProcessApp(driver);
		//	wait.until(ExpectedConditions.elementToBeClickable(PCSECheck)).click();	
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("The PCSECheck is not clicked");
		}
		return new PCSECheck(driver);
	}
	
	public ThirdPartyCheck clickonThirdParty() {
		try{
			wait.until(ExpectedConditions.elementToBeClickable(ThirdPartyCheck));
			scrolltoElement(driver, ThirdPartyCheck);
			Actions actions = new Actions(driver);
	    	actions.moveToElement(ThirdPartyCheck);
	    	actions.doubleClick().build().perform();
			wait.until(ExpectedConditions.elementToBeClickable(ThirdPartyCheck));	
			helpers.CommonFunctions.PageLoadExternalwait(driver);
		}
		catch(Exception e)
		{
			System.out.println("The Net Team Check is not clicked");
		}
		return new ThirdPartyCheck(driver);
	}

	public NetTeamCheck clickonnetTeamcheck() {
		try{
			
			scrolltoElement(driver, NetTeamCheck);
			helpers.Support.PageLoadExternalwait_ProcessApp(driver);
			wait.until(ExpectedConditions.elementToBeClickable(NetTeamCheck));
			Actions actions = new Actions(driver);
	    	actions.moveToElement(NetTeamCheck);
	    	actions.doubleClick().build().perform();
	    	helpers.Support.PageLoadExternalwait_ProcessApp(driver);
			//wait.until(ExpectedConditions.elementToBeClickable(NetTeamCheck)).click();	
		}
		catch(Exception e)
		{
			System.out.println("The Net Team Check is not clicked");
		}
		return new NetTeamCheck(driver);
	}
	
	public CaseofficerReview clickonCaseofficerreview() {
		try{
			scrolltoElement(driver, Caseofficereviewtab);
			wait.until(ExpectedConditions.elementToBeClickable(Caseofficereviewtab));
			Actions actions = new Actions(driver);
	    	actions.moveToElement(Caseofficereviewtab);
	    	actions.doubleClick().build().perform();
	    	helpers.Support.PageLoadExternalwait_ProcessApp(driver);
		}
		catch(Exception e)
		{
			System.out.println("The case officer review is not clicked");
		}
		// TODO Auto-generated method stub
		return new CaseofficerReview(driver);
	}
	
	public MangerApproval clickonMangerApproval() {
		try{
			scrolltoElement(driver, ManagerApproval);
			wait.until(ExpectedConditions.elementToBeClickable(ManagerApproval));
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(ManagerApproval);
	    	actions1.doubleClick().build().perform();
	    	helpers.Support.PageLoadExternalwait_ProcessApp(driver);
		
		}
		catch(Exception e)
		{
			System.out.println("The Manager Aprroval button is not clicked");
		}
		
		return new MangerApproval(driver);
		
	}



	public PersonalDetails_performer clickonPersonal_Pef() {
		try{
			Thread.sleep(2000);
			wait.until(ExpectedConditions.elementToBeClickable(Personal_performer));
			scrolltoElement(driver, Personal_performer);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(Personal_performer);
	    	actions1.doubleClick().build().perform();
	    	helpers.Support.PageLoadExternalwait_ProcessApp(driver);
		}
		catch(Exception e)
		{
			System.out.println("The perosnal info button is not clicked");
		}
		return new PersonalDetails_performer(driver);
	}



	public ChangeDetails clickonconfirmGMC() {
		try{
			Thread.sleep(3000);
			scrolltoElement(driver, ConfimredGMC);			
			wait.until(ExpectedConditions.elementToBeClickable(ConfimredGMC)).click();			
		}
		catch(Exception e)
		{
			System.out.println("The Change details page - The confimred check box is not clicked");
		}
		return new ChangeDetails(driver) ;
	}
	
	public ChangeDetails clickonconfirmGMC_PerformerType() {
		try{
			helpers.Support.PageLoadExternalwait_ProcessApp(driver);
			scrolltoElement(driver, ConfimredGMC_PerformerType);			
			wait.until(ExpectedConditions.elementToBeClickable(ConfimredGMC_PerformerType)).click();			
		}
		catch(Exception e)
		{
			System.out.println("The Change details page - The confimred check box is not clicked");
		}
		return new ChangeDetails(driver) ;
	}



	public PerformerType clickonPerformerType() {
		try{
			Thread.sleep(2000);
			scrolltoElement(driver, PerformerTypeTab);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(PerformerTypeTab);
	    	actions1.doubleClick().build().perform();
	    	
	    	boolean ispresent1 = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
			while(ispresent1)
			{
				Thread.sleep(2000);
				ispresent1 = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
			}
		}
		catch(Exception e)
		{
			System.out.println("The performer Type is not clicked");
		}
		return new PerformerType(driver);
	}



	public HourRetirement clickonHourRetirement() {
		try{
			Thread.sleep(2000);
			scrolltoElement(driver, HourRetirementTab);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(HourRetirementTab);
	    	actions1.doubleClick().build().perform();
	    	helpers.Support.PageLoadExternalwait_ProcessApp(driver);
		}
		catch(Exception e)
		{
			System.out.println("The 24 Hours Retirement is not clicked");
		}
		return new HourRetirement(driver) ;
	}



	public EmpDetails clickonEmpDetails() {
		try{
			Thread.sleep(2000);
			scrolltoElement(driver, EmpDetailsTab);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(EmpDetailsTab);
	    	actions1.doubleClick().build().perform();
	    	helpers.Support.PageLoadExternalwait(driver);
	 		
		}
		catch(Exception e)
		{
			System.out.println("The Emp details tab is not clicked");
		}
		return new EmpDetails(driver);
	}



	public PerformerList clickonReviewpractice(String ExpectedRefNo) {
		try{
			int ResultCount = 0;
			wait.until(ExpectedConditions.elementToBeClickable(PraticeUsertable));
			scrolltoElement(driver, PraticeUsertable);
			List<WebElement> listOfRows = PraticeUsertable.findElements(By.tagName("tr"));
			System.out.println("Rows: "+listOfRows.size());
			Thread.sleep(1000);
			for (int i =1 ;i<(listOfRows.size());i++)
			{	
				
				WebElement RefNHS = driver.findElement(By.xpath("//table[@id='tblPracticeUsertable']//tbody/tr["+i+"]/td[1]"));
				WebElement ActionPerform = driver.findElement(By.xpath("//table[@id='tblPracticeUsertable']//tbody/tr["+i+"]/td[6]/button"));
				scrolltoElement(driver, RefNHS);
				String RefNo = driver.findElement(By.xpath("//table[@id='tblPracticeUsertable']//tbody/tr["+i+"]/td[1]")).getText();
				String ActionPerformText = driver.findElement(By.xpath("//table[@id='tblPracticeUsertable']//tbody/tr["+i+"]/td[6]/button")).getText();
				if (ExpectedRefNo.equalsIgnoreCase(RefNo) && ActionPerformText.equalsIgnoreCase("Review Practice Change"))
				{
					ResultCount = 1;
					scrolltoElement(driver, RefNHS);
					wait.until(ExpectedConditions.elementToBeClickable(ActionPerform));
					Actions actions1 = new Actions(driver);
			    	actions1.moveToElement(ActionPerform);
			    	actions1.doubleClick().build().perform();
			    	break;
				}
			}
			while(ResultCount == 0 && PraticeUsertableNext.isEnabled())  
			{
				Actions actions = new Actions(driver);
		    	actions.moveToElement(PraticeUsertableNext);
		    	actions.click().build().perform();
			//	wait.until(ExpectedConditions.elementToBeClickable(PraticeUsertableNext)).click();
				List<WebElement> listOfRows1 = PraticeUsertable.findElements(By.tagName("tr"));
				System.out.println("Rows: "+listOfRows1.size());
				for (int i =0 ;i<(listOfRows.size());i++)
				{	
					
					WebElement RefNHS = driver.findElement(By.xpath("//tbody[@id='tblPracticeUsertable']//tbody/tr["+i+"]/td[1]"));
					WebElement ActionPerform = driver.findElement(By.xpath("//table[@id='tblPracticeUsertable']//tbody/tr["+i+"]/td[6]/button"));
					scrolltoElement(driver, RefNHS);
					String RefNo = driver.findElement(By.xpath("//table[@id='tblPracticeUsertable']//tbody/tr["+i+"]/td[1]")).getText();
					String ActionPerformText = driver.findElement(By.xpath("//table[@id='tblPracticeUsertable']//tbody/tr["+i+"]/td[6]/button")).getText();
					if (ExpectedRefNo.equalsIgnoreCase(RefNo) && ActionPerformText.equalsIgnoreCase("Review Practice Change"))
					{
						ResultCount = 1;
						scrolltoElement(driver, RefNHS);
						wait.until(ExpectedConditions.elementToBeClickable(ActionPerform));
						Actions actions1 = new Actions(driver);
				    	actions1.moveToElement(ActionPerform);
				    	actions1.doubleClick().build().perform();
				    	break;
					}
				}
			}
					
		}
		catch(Exception e)
		{
			System.out.println("The Review Practice change is not clicked");
		}
		return new PerformerList(driver) ;
	}



	public PerformerList clickonacceptStartdate() {
		try{
			Thread.sleep(3000);
			scrolltoElement(driver, AcceptStartdate);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(AcceptStartdate);
	    	actions1.doubleClick().build().perform();		
	    	Thread.sleep(3000);
		}
		catch(Exception e)
		{
			System.out.println("The Accept Start date button is not clicked");
		}
		return new PerformerList(driver) ;
	}



	public Localoffice clickonLocaloffice() {
		try{
			Thread.sleep(2000);
			scrolltoElement(driver, LocalofficeTab);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(LocalofficeTab);
	    	actions1.doubleClick().build().perform();
			
	    	boolean ispresent1 = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
			while(ispresent1)
			{
				Thread.sleep(2000);
				ispresent1 = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
			}
		}
		catch(Exception e)
		{
			System.out.println("The local office is not clicked");
		}
		return new Localoffice(driver);
	}



	public WithdrawPerformer clickonWithdrawPerformer() {
		try{
		Thread.sleep(2000);
		scrolltoElement(driver, WithdrawPLTab);
		Actions actions1 = new Actions(driver);
    	actions1.moveToElement(WithdrawPLTab);
    	actions1.doubleClick().build().perform();
		
    	boolean ispresent1 = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
		while(ispresent1)
		{
			Thread.sleep(1000);
			ispresent1 = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
		}
	}
	catch(Exception e)
	{
		System.out.println("The Withdraw PL Tab is not clicked");
	}
	return new WithdrawPerformer(driver);
	}



	public FacetoFaceAppointment clickonFaceApp() {
		try{
		scrolltoElement(driver, FaceToFaceApp);
		wait.until(ExpectedConditions.elementToBeClickable(FaceToFaceApp));
		Actions actions1 = new Actions(driver);
    	actions1.moveToElement(FaceToFaceApp);
    	actions1.doubleClick().build().perform();
    	helpers.Support.PageLoadExternalwait_ProcessApp(driver);
		}
		catch(Exception e)
		{
			System.out.println("The Click on face to face appointment is happend");
		}
		return new FacetoFaceAppointment(driver) ;
	}






	




	


}
