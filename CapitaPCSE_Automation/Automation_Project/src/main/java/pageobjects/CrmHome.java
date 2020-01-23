package pageobjects;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.security.UserAndPassword;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import helpers.Support;
import helpers.WindowHandleSupport;

public class CrmHome extends Support{

	WebDriver driver;
	WebDriverWait wait;

	@FindBy(id="navTabModuleButtonTextId")
	WebElement screeningTab;

	@FindBy(id="Tabnav_dashboards-main")
	WebElement dashboardTab;

	@FindBy(id="TabHome")
	WebElement homeTab;

	@FindBy(id="Tab1")
	WebElement dyCRM;

	@FindBy(id="TabSFA")
	WebElement settingTab;
	
	
	@FindBy(id="Settings")
	WebElement settings;
	
	@FindBy(id="TabSettings")
	WebElement TabSettings;

	@FindBy(id="rightNavLink")
	WebElement rightNavLink;


	@FindBy(id="navTabLogoImage")
	WebElement logoTab;

	@FindBy(css="li[id*='SaveAs'] span[class*='Label']")
	WebElement saveAsLink;
	/*  //li[id*='SaveAs'] span[class*='Label'] --- css
	  ////li[contains(@id,'SaveAs')]//span[contains(@class,'Label')] --xpath
	 */	  	  

	@FindBy(css="li[id*='New'] span[class*='Label']")
	WebElement new1;

	@FindBy(css="li[id*='SetAsDefault'] span[class*='Label']")
	WebElement setAsDefault;

	@FindBy(css="li[id*='Refresh'] span[class*='Label']")
	WebElement refresh;

	@FindBy(css="span[id*='CreateTextId']")
	WebElement create;

	@FindBy(css="div[id*='InlineDialog']")
	WebElement inlineDialog;

	@FindBy(id="navTourPages")
	WebElement navTourDialog;

	@FindBy(css = "a[id*='buttonClose']")
	WebElement closeButton;

	@FindBy(css="a[title*='Organisation']")
	WebElement orgTile;
	
	@FindBy(id="nav_conts")
	WebElement contactsTile;

	//	@FindBy(css="li[id*='NewRecord']")
	@FindBy(id="account|NoRelationship|HomePageGrid|Mscrm.HomepageGrid.account.NewRecord")
	WebElement newOrgIcon;

	@FindBy(css="img[alt='New']")
	//@FindBy(xpath="//li[@id='account|NoRelationship|HomePageGrid|Mscrm.HomepageGrid.account.NewRecord']/span/a/span")
	WebElement newOrg;

	@FindBy(xpath = "//li[@id='account|NoRelationship|Form|Mscrm.Form.account.Save']/span/a/span")
	WebElement saveAsIkn;

	@FindBy(css="input[id*=crmGrid_findCriteria]")
	WebElement srcCriTxt;
	
	@FindBy(id="scr_cohortselectionrule")
	WebElement cohortSelectionRule;
	
	@FindBy(id="scr_job")
	WebElement adminjobs;


	@FindBy(css="img[id*=crmGrid_findCriteriaImg]")
	WebElement srcCriIcon;

	@FindBy(xpath="//table[@id='gridBodyTable']/tbody")
	WebElement gridTable;

	//button[@id*='butBegin'] old one
	@FindBy(css = "button[id*='butBegin']")
	WebElement clsButton;
	
	//@FindBy(id="InlineDialog_Background")- old value
	@FindBy(xpath="//iframe[@id='InlineDialog1_Iframe']")
	WebElement pendingEmailApprovalDialog;
	//WebElement table = driver.findElement(By.xpath("//table[@id='gridBodyTable']/tbody"));
	
	@FindBy(css ="li[id*='contact.NewRecord']")
	WebElement contactNewRecord;
	
	@FindBy(xpath = "//ul[@class='ms-crm-CommandBar-Menu']/li[1]/span/a")
	WebElement contactNewRecordField;
	
	@FindBy(id="navTabLogoTextId")
	WebElement CRMlogo;
	
	@FindBy(id="Settings")
	WebElement settingbutton;

	@FindBy(id="navTabModuleButtonTextId")
	WebElement settinghover;
	
	
	
	@FindBy(id="scr_cohortselectionrule")
	WebElement Cohortbutton;
	
	
	@FindBy(id="rightNavLink")
	WebElement Rightclick;


	@FindBy(id="SFA")
	WebElement screeningbutton;
	

	@FindBy(id="nav_conts")
	WebElement contactbutton;
	
	@FindBy(id="Tabscr_cohortselectionrule-main")
	WebElement cohortselectionruleTab;
	
	@FindBy(id="nav_activities")
	WebElement activitiesbutton;
	
	@FindBy(id="advancedFindImage")
	WebElement advancedFind;

	@FindBy(id="slctPrimaryEntity")
	WebElement primaryEntity;	

	@FindBy(xpath ="//*[@id='TabSFA']/a/span[2]/img")
	WebElement Salesbutton;
	
	@FindBy(xpath ="//*[@id='crmGrid_findCriteria']")
	WebElement ContanctTextbox;
	
/*	@FindBy(xpath ="//*[@id='crmGrid_findHintText']")
	WebElement ContanctTextbox;*/
	
	@FindBy(xpath ="//table[@id='gridBodyTable']//tbody/tr[1]/td[2]/nobr/a")
	WebElement ResultFirstrow;
	
	@FindBy(xpath ="//*[@id='navTabGlobalCreateImage']")
	WebElement Quicksearchicon;
	
	@FindBy(xpath ="//*[@id='screeningrecordsgrid_addImageButtonImage']")
	WebElement ScreeningrecordPlussign;
	
	
	@FindBy(xpath ="//*[@class='nav-subgroup']//li[6]/a")
	WebElement ScreenonQuicksearch;
	
	@FindBy(id ="scr_screeningstatus_i")
	WebElement Status_screening;
	
	@FindBy(id ="scr_testduedate")
	WebElement TestDueDate_screening;
	
	@FindBy(xpath ="//input[@id='DateInput']")
	WebElement TestDueDatefield_screening;
	
	@FindBy(id ="scr_healthauthority")
	WebElement Healthauth_screening;
	
	@FindBy(xpath ="//div[@id='scr_healthauthority']/div")
	WebElement Healthauthtable_screening;
	
	@FindBy(id ="scr_healthauthority_ledit")
	WebElement Healthauthfield_screening;
	
	@FindBy(id ="scr_healthauthority_i")
	WebElement Healthauthfield_screening_Icon;
		
	@FindBy(xpath="//*[@id='FormTitle']/h1")
	WebElement InlineNhsnoH1;
	
	@FindBy (id="scr_nhs_number")
	WebElement nhsNumber;
	
	@FindBy (id="scr_patient")
	WebElement Patient_screening;
	
	@FindBy(xpath ="//div[@id='scr_patient']/div")
	WebElement Patienttable_screening;
	
	@FindBy (id="scr_patient_ledit")
	WebElement Patientfield_screening;
	
	@FindBy (xpath="//button[@id='globalquickcreate_save_button_NavBarGloablQuickCreate']")
	WebElement Save_screening;
	
	@FindBy (xpath="//button[@id='globalquickcreate_save_button_screeningrecordsgrid_gridcontrol_quickcreate']")
	WebElement Save_screening_Plussign;
	
	
	@FindBy (xpath="//div[@id='scr_testduedate']/div/span")
	WebElement TestDueDatetable_screening;
	
	@FindBy (xpath="//*[@id='Tab1']/a/span[2]/img")
	WebElement MicrosoftCRM;
	//*[@id="Tab1"]/a/span[2]/img

	@FindBy (xpath="//span[@id='navTabModuleButtonTextId']")
	WebElement SalesCRM;
	

	
	
	


	public CrmHome(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 60);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}
	
	public CrmHome SignIn() throws InterruptedException
	{
		Thread.sleep(2000);
		Alert securityAlert = wait.until(ExpectedConditions.alertIsPresent()); 
    	securityAlert.authenticateUsing(new UserAndPassword("CAPITAPCSEDEV\\Amitkumarr","M8$tek12"));
    	
    	Thread.sleep(2000);
    	return new CrmHome(driver);
	}

	public void clickSaveAS (){
		wait.until(ExpectedConditions.elementToBeClickable(saveAsLink));
		saveAsLink.click();
	}

	public CrmHome clickHome(){

		wait.until(ExpectedConditions.visibilityOf(homeTab));
		wait.until(ExpectedConditions.elementToBeClickable(homeTab)).click();

		return new CrmHome(driver);
	}

	public CrmHome clickSettings(){

		Actions actions = new Actions(driver);
		actions.moveToElement(dyCRM);
		actions.click().build().perform();

		wait.until(ExpectedConditions.visibilityOf(settings));
		wait.until(ExpectedConditions.elementToBeClickable(settings)).click();


		return new CrmHome(driver);
	}

	public CrmHome clickSettingsTileNavigatorClick(int clicks) throws InterruptedException{

		Actions actions = new Actions(driver);
		actions.moveToElement(TabSettings).build().perform();
				
		Thread.sleep(2000);
	
		
		for (int i=0; i<clicks;i++)
		{
			wait.until(ExpectedConditions.visibilityOf(rightNavLink));
			wait.until(ExpectedConditions.elementToBeClickable(rightNavLink)).click();
			Thread.sleep(4000);
		}
		return new CrmHome(driver);
	}
	
	public CohortSelectionRules clickCohertSelectionRules()
	{
		wait.until(ExpectedConditions.visibilityOf(cohortSelectionRule));
		wait.until(ExpectedConditions.elementToBeClickable(cohortSelectionRule)).click();
		return new CohortSelectionRules(driver);
	}
	
	public AdminJobs clickAdminJobs()
	{
		wait.until(ExpectedConditions.visibilityOf(adminjobs));
		wait.until(ExpectedConditions.elementToBeClickable(adminjobs)).click();
		return new AdminJobs(driver);
	}
	
	public CrmHome clickScreening(){

		wait.until(ExpectedConditions.visibilityOf(screeningTab));
		Actions actions = new Actions(driver);
		actions.moveToElement(screeningTab).build().perform();
		//wait.until(ExpectedConditions.elementToBeClickable(screeningTab)).click();
		actions.moveToElement(screeningTab).click().build().perform();

		return new CrmHome(driver);
	}

	public CrmHome clickOnInlineDialog() throws InterruptedException
	{	
	
		
		//wait.until(ExpectedConditions.visibilityOf(inlineDialog));
		clickOnPendingEmailApprovalDialog();
		
		try{
			Thread.sleep(1000);
			//System.out.println(driver.getTitle());
			System.out.println(inlineDialog.isDisplayed());
			
			if (inlineDialog.isDisplayed())
			
			{
				wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("InlineDialog_Iframe")));
				//driver.switchTo().frame("InlineDialog_Iframe");
				wait.until(ExpectedConditions.visibilityOf(closeButton));
				wait.until(ExpectedConditions.elementToBeClickable(closeButton));
				closeButton.click();			  
				driver.switchTo().defaultContent();
			}
			
			else
				System.out.println("The inline dialog window is not found.");
		}
		catch (Exception e){
			
			System.out.println("The exception is found "+e);
		}
		
		/*Thread.sleep(10000);
		//System.out.println(driver.getTitle());
		String txt = driver.getTitle();
		System.out.println(txt);
		//inlineDialog.click();
		System.out.println(inlineDialog.isDisplayed());
		//driver.findElement(By.id(""))
		//wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("InlineDialog_Iframe")));
		driver.switchTo().frame("InlineDialog_Iframe");
		wait.until(ExpectedConditions.visibilityOf(closeButton));
		wait.until(ExpectedConditions.elementToBeClickable(closeButton));
		closeButton.click();			  
		driver.switchTo().defaultContent();*/
		return new CrmHome(driver);
	}

	public CrmHome clickOnPendingEmailApprovalDialog() throws InterruptedException
	{	
		try{
			boolean ispresent = IsElementPresent_EmailNotification(driver);
			//	boolean ispresent = driver.findElements(By.xpath("//iframe[@id='InlineDialog1_Iframe']")).size() != 0;
			System.out.println("The Email notification : " + ispresent);
			
			if(ispresent)
			{
				driver.switchTo().frame("InlineDialog1_Iframe");
				clsButton.click();			  
				driver.switchTo().defaultContent();
			}
				
			/*System.out.println("The Pending Email Approval Dialog Displayed: " + pendingEmailApprovalDialog.isDisplayed());
			if (pendingEmailApprovalDialog.isDisplayed())
			{
				//InlineDialog_Iframe- is the old ID
				driver.switchTo().frame("InlineDialog1_Iframe");
				clsButton.click();			  
				driver.switchTo().defaultContent();
			}
			else
				System.out.println("The PendingEmailApprovalDialog dialog not found.");*/
			
		}
		catch(Exception e){
			
			System.out.println("Found Exception : " + e);
			
		}
		
		return new CrmHome(driver);
	}
	
	public static boolean IsElementPresent_EmailNotification (WebDriver driver) throws InterruptedException
	{
		try{
						
			WebDriverWait wait = new WebDriverWait(driver, 1);
			JavascriptExecutor jse = (JavascriptExecutor)driver; 
			jse.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath("//iframe[@id='InlineDialog1_Iframe']")));
		//	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='loader hide-spinner']")));
			return true;
		}
	 catch (org.openqa.selenium.NoSuchElementException
	        | org.openqa.selenium.StaleElementReferenceException
	        | org.openqa.selenium.TimeoutException e) 
	{
	    return false;
	}
		catch(Exception e)
		{
			return false;
				}
			}
	
	public CrmHome clickOnOrgTile(){

		orgTile.click();
		return new CrmHome(driver);
	}
	
	public Contacts clickContactsTile(){
		
		wait.until(ExpectedConditions.visibilityOf(contactsTile));
		wait.until(ExpectedConditions.elementToBeClickable(contactsTile)).click();

		return new Contacts(driver);
		
	}



	public Organisation clickOnNewRecord() throws InterruptedException{

		Thread.sleep(3000);
		wait.until(ExpectedConditions.visibilityOf(newOrg));
		wait.until(ExpectedConditions.elementToBeClickable(newOrg)).click();



		return new Organisation(driver);

	}
	
	public Contacts clickOnContactNewRecord() throws InterruptedException{

		Thread.sleep(3000);
		wait.until(ExpectedConditions.visibilityOf(contactNewRecord));
		wait.until(ExpectedConditions.elementToBeClickable(contactNewRecord)).click();
		return new Contacts(driver);

	}

	public void clickOnSaveIcon(){
		wait.until(ExpectedConditions.elementToBeClickable(saveAsIkn));
		saveAsIkn.click();
	}

	public Organisation clickOnOrgTileForSearch() {
	
		orgTile.click();
		return new Organisation(driver);
	}


	public Contacts ScreenContactMousehover() throws InterruptedException
	{
		Thread.sleep(1000);
		WebElement element = CRMlogo;
		WebElement element1 = screeningbutton;
		//WebElement element2 = Cohortbutton;
		 
        Actions action = new Actions(driver);
 
        action.moveToElement(element).build().perform();
        Thread.sleep(5000);
        element1.click();
     
       String Tabname = settinghover.getText();
       System.out.println(Tabname);
       if ( Tabname.equalsIgnoreCase("Screening"))
       {
    	   settinghover.click();  
       }
		
		
	wait.until(ExpectedConditions.visibilityOf(Rightclick));
		//wait.until(ExpectedConditions.elementToBeClickable(Rightclick)).click();;
			
		while (Rightclick.isDisplayed())
		{
			if  (contactbutton.isDisplayed())
			{ 
				Thread.sleep(1000);
				contactbutton.click();
				break;
				
			}
		
			else 
			{
				Thread.sleep(1000);
				Rightclick.click();
			}

		}
		
		return new Contacts(driver);
			
	}
	
	public CrmHome Clickscreening() throws InterruptedException
	{
		Thread.sleep(2000);
		WebElement element = CRMlogo;
		WebElement element1 = settings;
		WebElement element2 = Cohortbutton;
		 
        Actions action = new Actions(driver);
 
        action.moveToElement(element).build().perform();
 
       element1.click();
       
		
		wait.until(ExpectedConditions.visibilityOf(Rightclick));
	//wait.until(ExpectedConditions.elementToBeClickable(Rightclick)).click();;
			
		while (Rightclick.isDisplayed())
		{
			if  (Cohortbutton.isDisplayed())
			{ 
				Thread.sleep(1000);
				Cohortbutton.click();
				wait.until(ExpectedConditions.elementToBeClickable(cohortselectionruleTab));
			}
		
			else 
			{
				Thread.sleep(1000);
				Rightclick.click();
			}

		}
		
		return new CrmHome(driver);
			
	}

	public Cohortselect Cohorttabselect() throws InterruptedException
	{
		Thread.sleep(2000);
		WebElement element = CRMlogo;
		WebElement element1 = settings;
		WebElement element2 = Cohortbutton;
		 
        Actions action = new Actions(driver);
 
        action.moveToElement(element).build().perform();
 
       element1.click();
       
       String Tabname = settinghover.getText();
       System.out.println(Tabname);
        if ( Tabname.equalsIgnoreCase("SETTINGS"))
        {
     	   settinghover.click();  
        }
		wait.until(ExpectedConditions.visibilityOf(Rightclick));
	//wait.until(ExpectedConditions.elementToBeClickable(Rightclick)).click();;
			
		while (Rightclick.isDisplayed())
		{
			if  (Cohortbutton.isDisplayed())
			{ 
				Thread.sleep(1000);
				Cohortbutton.click();
				break;
			}
		
			else 
			{
				Thread.sleep(1000);
				Rightclick.click();
			}

		}
		
		return new Cohortselect(driver);
			
	}

	public Activities ScreenActivitiesclick() throws InterruptedException
	{
		Thread.sleep(2000);
		WebElement element = CRMlogo;
		WebElement element1 = screeningbutton;
	//	WebElement element2 = Cohortbutton;
		 
        Actions action = new Actions(driver);
 
        action.moveToElement(element).build().perform();
 
       element1.click();
 
       String Tabname = settinghover.getText();
      System.out.println(Tabname);
       if ( Tabname.equalsIgnoreCase("Screening"))
       {
    	   settinghover.click();  
       }
		
		
	wait.until(ExpectedConditions.visibilityOf(Rightclick));
		//wait.until(ExpectedConditions.elementToBeClickable(Rightclick)).click();;
			
		while (Rightclick.isDisplayed())
		{
			if  (activitiesbutton.isDisplayed())
			{ 
				Thread.sleep(1000);
				activitiesbutton.click();
				break;
				
			}
		
			else 
			{
				Thread.sleep(1000);
				Rightclick.click();
			}

		}
		
		return new Activities(driver);
			
	}
	
	public AdvancedFilter clickAdvanceFind() throws InterruptedException, IOException{

		WindowHandleSupport.getRequiredWindowDriver(driver, "Dashboards:Sales");
		advancedFind.click();
		Thread.sleep(3000);
		System.out.println(driver.getTitle());
		
		WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find - Microsoft Dynamics CRM");
		/*String winHandleBefore = driver.getWindowHandle();
		System.out.println(winHandleBefore);
		
		// Perform the click operation that opens new window

		// Switch to new window opened
		for(String winHandle : driver.getWindowHandles()){
		    driver.switchTo().window(winHandle);
		    System.out.println(winHandle);
		    driver.switchTo().frame("contentIFrame0");
		    
		}*/
		switchToFrame(primaryEntity, driver);
		//driver.switchTo().frame("contentIFrame0");
		System.out.println(driver.getTitle());
		driver.switchTo().defaultContent();
		return new AdvancedFilter(driver);
	}

	public CrmHome clickonsales_Contanct() throws InterruptedException {
		try{
			driver.switchTo().defaultContent();
			  Actions action = new Actions(driver);			  
		      action.moveToElement(Salesbutton).build().perform();		 
		      Salesbutton.click();
		  	wait.until(ExpectedConditions.visibilityOf(Rightclick));
		      while (Rightclick.isDisplayed())
				{
					if  (contactsTile.isDisplayed())
					{ 
						Thread.sleep(1000);
						contactsTile.click();
						break;
						
					}				
					else 
					{
						Thread.sleep(1000);
						Rightclick.click();
					}

				}
				
		      
		      
			
		}
		catch (NoSuchElementException e)
		{
		System.out.println("The sales or contanct button is not found "+e);
		}
	
		return new CrmHome(driver);
	}

	public Contacts searchandclick(String performer_PerformerPortal) throws InterruptedException, IOException {
		try
		{
			switchToFrame(ContanctTextbox, driver);
			wait.until(ExpectedConditions.visibilityOf(ContanctTextbox));
			wait.until(ExpectedConditions.visibilityOf(ContanctTextbox)).click();
			wait.until(ExpectedConditions.visibilityOf(ContanctTextbox)).clear();
			wait.until(ExpectedConditions.visibilityOf(ContanctTextbox)).sendKeys(performer_PerformerPortal);
			Thread.sleep(2000);
			wait.until(ExpectedConditions.visibilityOf(ContanctTextbox)).sendKeys(Keys.ENTER);
			wait.until(ExpectedConditions.visibilityOf(ResultFirstrow));
			Actions action = new Actions(driver);			  
		    action.moveToElement(ResultFirstrow).build().perform();		 
		   action.doubleClick(ResultFirstrow).build().perform();
	
		}
	
		catch (NoSuchElementException e)
		{
		System.out.println("The search of performer list is not done properly "+e);
		}
		driver.switchTo().defaultContent();
		return new Contacts(driver);
	}

	public CrmHome clickonQuicksearch() {
		try{
			wait.until(ExpectedConditions.elementToBeClickable(Quicksearchicon)).click();
						wait.until(ExpectedConditions.elementToBeClickable(ScreenonQuicksearch)).click();
			
		}
		
		catch (NoSuchElementException e)
		{
		System.out.println("The quick search icon is not clicked "+e);
		}
		
		return new CrmHome(driver);
	}

	public CrmHome enterscreeningrecordmanually(String Text ,int position ,String PatientName) throws InterruptedException, IOException {
		try{
			String TestDue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "TestDuedate", position);
			String HealthAuthscreening = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "Healthauthority", position);
			switchToFrame(Status_screening, driver);
			Select status = new Select(Status_screening);
			status.selectByVisibleText(Text);
			wait.until(ExpectedConditions.elementToBeClickable(TestDueDate_screening)).click();
			//wait.until(ExpectedConditions.elementToBeClickable(TestDueDatetable_screening)).click();
			wait.until(ExpectedConditions.elementToBeClickable(TestDueDatefield_screening)).sendKeys(TestDue);
			wait.until(ExpectedConditions.elementToBeClickable(Healthauth_screening)).click();
	
			wait.until(ExpectedConditions.elementToBeClickable(Healthauthfield_screening)).sendKeys(HealthAuthscreening);
			wait.until(ExpectedConditions.elementToBeClickable(Healthauthfield_screening_Icon)).click();
			wait.until(ExpectedConditions.elementToBeClickable(Healthauthfield_screening)).sendKeys(Keys.TAB);
		
			wait.until(ExpectedConditions.elementToBeClickable(Patientfield_screening)).sendKeys(PatientName);
			wait.until(ExpectedConditions.elementToBeClickable(Patientfield_screening)).sendKeys(Keys.TAB);
			driver.switchTo().defaultContent();
			
			
					
		}
		
		catch (NoSuchElementException e)
		{
		System.out.println("The quick search icon is not clicked "+e);
		}
		
		return new CrmHome(driver);
	}
	
	
	public CrmHome enterscreeningrecordPlussign(String Text ,int position) throws InterruptedException, IOException {
		try{
			String TestDue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "TestDuedate", position);
			String HealthAuthscreening = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "Contact", "Healthauthority", position);
			Thread.sleep(5000);
			driver.switchTo().frame("screeningrecordsgrid_gridcontrol_quickcreate");
			Select status = new Select(Status_screening);
			status.selectByVisibleText(Text);
			wait.until(ExpectedConditions.elementToBeClickable(TestDueDate_screening)).click();
			//wait.until(ExpectedConditions.elementToBeClickable(TestDueDatetable_screening)).click();
			wait.until(ExpectedConditions.elementToBeClickable(TestDueDatefield_screening)).sendKeys(TestDue);
			wait.until(ExpectedConditions.elementToBeClickable(Healthauth_screening)).click();
	
			wait.until(ExpectedConditions.elementToBeClickable(Healthauthfield_screening)).sendKeys(HealthAuthscreening);
			wait.until(ExpectedConditions.elementToBeClickable(Healthauthfield_screening_Icon)).click();
			wait.until(ExpectedConditions.elementToBeClickable(Healthauthfield_screening)).sendKeys(Keys.TAB);
			driver.switchTo().defaultContent();
			
			
					
		}
		
		catch (NoSuchElementException e)
		{
		System.out.println("The quick search icon is not clicked "+e);
		}
		
		return new CrmHome(driver);
	}
	
	
	public String getpatientname() throws InterruptedException, IOException {
		String PatientName = null;
		try{
		switchToFrame(nhsNumber, driver);
		String ExpectedInlineNHSno = wait.until(ExpectedConditions.visibilityOf(InlineNhsnoH1)).getText();
		System.out.println(ExpectedInlineNHSno);
		String ExpInlineNHSno1 = ExpectedInlineNHSno.split("NHS No")[0].trim();
		PatientName = ExpInlineNHSno1.replace("(", "").trim();
		System.out.println(PatientName);
		}
		
		catch (NoSuchElementException e)
		{
		System.out.println("The Patient name is not getting on contact page "+e);
		}
		driver.switchTo().defaultContent();
		return PatientName;
	}

	public boolean verifylabelonscreening(String string, String string2, String string3) {
		boolean labelonscreening = false;
		try{
			driver.switchTo().frame("NavBarGloablQuickCreate");
			 List<WebElement> Labelonscreenings = driver.findElements(By.xpath("//*[@class='NNN ms-crm-InlineTabBody-Read']//td/span"));
			  for (WebElement Label : Labelonscreenings)
			  {		  
			  if(Label.getText().equalsIgnoreCase(string))
			  {
				  labelonscreening = true;
				  break;
			  }
			  if(Label.getText().equalsIgnoreCase(string2))
			  {
				  labelonscreening = true;
				  break;
			  }
			  if(Label.getText().equalsIgnoreCase(string3))
			  {
				  labelonscreening = true;
				  break;
			  }
			  
			  }
			
		}
		catch (NoSuchElementException e)
		{
		System.out.println("The  label is not found on screening"+e);
		}
		driver.switchTo().defaultContent();
		return labelonscreening;
	}

	public CrmHome clickSaveonScreening() throws InterruptedException {
		try{
			driver.switchTo().frame("NavBarGloablQuickCreate");
			driver.switchTo().defaultContent();
			Actions action = new Actions(driver);
			action.moveToElement(Save_screening);
			action.doubleClick().build().perform();
		//	helpers.CommonFunctions.ClickOnbutton_tag("Save",driver);
		Thread.sleep(3000);
			
		}
		catch (NoSuchElementException e)
		{
		System.out.println("The save button is not clicked"+e);
		}
		driver.switchTo().defaultContent();
		return new CrmHome(driver);
	}
	
	public CrmHome clickSaveonScreeningWithPlussign() throws InterruptedException {
		try{
			driver.switchTo().frame("screeningrecordsgrid_gridcontrol_quickcreate");
			driver.switchTo().defaultContent();
			Actions action = new Actions(driver);
			action.moveToElement(Save_screening_Plussign);
			action.doubleClick().build().perform();
		//	helpers.CommonFunctions.ClickOnbutton_tag("Save",driver);
		Thread.sleep(3000);
			
		}
		catch (NoSuchElementException e)
		{
		System.out.println("The save button is not clicked"+e);
		}
		driver.switchTo().defaultContent();
		return new CrmHome(driver);
	}

	public CrmHome clickonScreeningrecordPlussign() throws InterruptedException, IOException {
		try{
			switchToFrame(ScreeningrecordPlussign, driver);
			scrolltoElement(driver, ScreeningrecordPlussign);
			Actions action = new Actions(driver);
			action.moveToElement(ScreeningrecordPlussign);
			action.doubleClick().build().perform();
			driver.switchTo().defaultContent();
				
		}
		
		catch (NoSuchElementException e)
		{
		System.out.println("The + sign on screening record is not clicked "+e);
		}
		
		return new CrmHome(driver);
	}

	public CrmHome clickonCRM() {
		try{
			wait.until(ExpectedConditions.elementToBeClickable(MicrosoftCRM)).click();
			wait.until(ExpectedConditions.elementToBeClickable(screeningbutton)).click();
					
		}
		
		catch (NoSuchElementException e)
		{
		System.out.println("The MicrosoftCRM -> screening record is not clicked "+e);
		}
		
		return new CrmHome(driver);

	}

	public Contacts ClickOnContanctOnPL() {
try{
	wait.until(ExpectedConditions.elementToBeClickable(SalesCRM));	
	wait.until(ExpectedConditions.elementToBeClickable(SalesCRM)).click();
	wait.until(ExpectedConditions.elementToBeClickable(contactsTile));	
	wait.until(ExpectedConditions.elementToBeClickable(contactsTile)).click();
	Thread.sleep(3000);
		

	}
	catch(Exception e)
	{
		System.out.println("The Contanct on CRM Sales is not clicked");
	}
	return new Contacts(driver);
	}


	
	
}
