package pageobjects;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import helpers.UITableDataSupport;


public class Cohortselect {
	WebDriver driver;

	WebDriverWait wait;

	@FindBy(css="input[id*=crmGrid_findCriteria]")
	WebElement srcCriTxt;
	@FindBy(xpath="\\li[contains(@id='scr_cohortselectionrule.SaveAndClose')]")
	WebElement saveAndCloseLink;

	@FindBy(xpath="//li[@id='scr_cohorttoorganisation|NoRelationship|Form|Mscrm.Form.scr_cohorttoorganisation.Save']")
	WebElement cohortToOrgSave;


	@FindBy(css="img[id*=crmGrid_findCriteriaImg]")
	WebElement srcCriIcon;

	@FindBy(xpath="//table[@id='gridBodyTable']/tbody")
	WebElement gridTable;

	@FindBy(id="Tabscr_cohortselectionrule-main")
	WebElement cohortSelectionRuleTab;

	@FindBy(id="scr_cohorttoorganisation|NoRelationship|Form|Mscrm.Form.scr_cohorttoorganisation.SaveAndClose")
	WebElement SaveonpageButton;

	@FindBy(id="scr_priority_i")
	WebElement Priority;
	@FindBy(xpath="//*[@id='scr_priority']")
	WebElement Priorityfield;

	@FindBy(xpath="//*[@id='scr_cohortselectionrule|NoRelationship|HomePageGrid|Mscrm.HomepageGrid.scr_cohortselectionrule.NewRecord']")
	WebElement Newbutton;

	@FindBy(css="li[id*=scr_cohortselectionrule.NewRecord]")
	WebElement Newbutton1;

	@FindBy(xpath="//*[@id='scr_organisation_ledit']")
	WebElement Organizationname;
	
	@FindBy(xpath="//table[@id='scr_organisation_lookupTable']")
	WebElement Organizationnamefield;
	

	@FindBy(id="footer_statuscontrol")
	WebElement Savebuttononpagefield;

	@FindBy(id="savefooter_statuscontrol")
	WebElement Savebuttononpage;

	@FindBy(id="scr_name_i")
	WebElement Newrulename;

	@FindBy(id="scr_loweragelimit")
	WebElement Loweragefield;
	@FindBy(id="scr_loweragelimit_i")
	WebElement Lowerage;

	@FindBy(id="scr_upperagelimit")
	WebElement Upperagefield;
	@FindBy(id="scr_upperagelimit_i")
	WebElement Upperage;

	@FindBy(id="scr_testduedays")
	WebElement Duedatefield;
	@FindBy(id="scr_testduedays_i")
	WebElement Duedate;

	@FindBy(id="scr_testduedaysbeforeclose")
	WebElement Beforetestduefield;
	@FindBy(id="scr_testduedaysbeforeclose_i")
	WebElement Beforetestdue;

	@FindBy(id="scr_letterdeliverymethod")
	WebElement Deliverymethodfield;
	@FindBy(id="scr_letterdeliverymethod_i")
	WebElement Deliverymethod;

	@FindBy(id="scr_daystoreminder")
	WebElement Reminderfield;
	@FindBy(id="scr_daystoreminder_i")
	WebElement Reminder;

	@FindBy(id="scr_daystononresponder")
	WebElement NonResponderfield;
	@FindBy(id="scr_daystononresponder_i")
	WebElement NonResponder;

	@FindBy(id="scr_cohortselectionrule|NoRelationship|Form|Mscrm.Form.scr_cohortselectionrule.Save")
	WebElement SaveButton;

	@FindBy(id="ApplicableCCGs_addImageButton")
	WebElement AddinghapplicableCCG;


	@FindBy(id="navTabLogoTextId")
	WebElement CRMlogo;

	@FindBy(xpath="/div/div[3]/div/div/ul/li/span/span[2]/span/li[2]/a/span[2]")
	WebElement settingbutton;

	@FindBy(id="navTabModuleButtonTextId")
	WebElement settinghover;

	@FindBy(css="div[id*='InlineDialog']")
	WebElement inlineDialog;

	@FindBy(id="scr_cohortselectionrule")
	WebElement Cohortbutton;


	@FindBy(id="rightNavLink")
	WebElement Rightclick;

	@FindBy(xpath="//table[@id='gridBodyTable']//td[2]")
	WebElement firstRecord;

	//@FindBy(id*="scr_cohortselectionrule.Delete")
	@FindBy(xpath = "//li[@id='scr_cohortselectionrule|NoRelationship|Form|Mscrm.Form.scr_cohortselectionrule.Delete']")
	WebElement ruleDelete;

	@FindBy(xpath = "//li[@id='scr_cohortselectionrule|NoRelationship|Form|Mscrm.Form.scr_cohortselectionrule.Delete']/span/a")
	WebElement ruleDeleteLink;


	@FindBy(css = "button[id*='butBegin']")
	WebElement deleteButton;

	@FindBy(xpath = "//span[@id='form_title_div']")
	WebElement formTitle;


	@FindBy(id="fixedrow")
	WebElement CheckboxDiv;


	@FindBy(xpath= "//*[@id='crmGrid_gridBar']/td[1]")
	WebElement firstCheckbOx;

	@FindBy(xpath= "//*[@id='chkAll']")
	WebElement AllCheckbox;
	//*[@id="chkAll"]

	@FindBy(xpath = "//*[@id='scr_cohortselectionrule|NoRelationship|HomePageGrid|Mscrm.HomepageGrid.scr_cohortselectionrule.DeleteMenu']")
	WebElement DeleteButton;


	@FindBy(xpath = "//img[@id='crmGrid_gridBodyTable_checkBox_Image_All']")
	WebElement CheckallResult;

	@FindBy(id ="InlineDialog")
	WebElement DeleteButtonPopUP; 




	public Cohortselect(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 70);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}

	public boolean rulePresent(String name)
	{
		Boolean flag = false;

		try{
			wait.until(ExpectedConditions.elementToBeClickable(cohortSelectionRuleTab)).click();
			driver.switchTo().frame("contentIFrame0");
			wait.until(ExpectedConditions.elementToBeClickable(srcCriTxt));
			srcCriTxt.clear();

			srcCriTxt.sendKeys(name);
			srcCriIcon.click();


			flag = UITableDataSupport.getDataFromColumnInTable(gridTable, 1, name);
		}
		catch(Exception e)
		{
			System.out.println("Found Exception : " + e);
		}

		//driver.switchTo().defaultContent();
		return flag;
	}

	public void deleteCreatedRule() throws InterruptedException
	{
		wait.until(ExpectedConditions.elementToBeClickable(firstRecord));
		firstRecord.click();

		Thread.sleep(2000);
		//wait.until(ExpectedConditions.elementToBeClickable(formTitle));

		try{

			driver.switchTo().defaultContent();


			wait.until(ExpectedConditions.elementToBeClickable(ruleDelete)).click();
			wait.until(ExpectedConditions.elementToBeClickable(ruleDeleteLink)).click();


			Thread.sleep(2000);
			//System.out.println(driver.getTitle());
			System.out.println(inlineDialog.isDisplayed());

			if (inlineDialog.isDisplayed())

			{
				wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("InlineDialog_Iframe")));
				//driver.switchTo().frame("InlineDialog_Iframe");
				wait.until(ExpectedConditions.visibilityOf(deleteButton));
				wait.until(ExpectedConditions.elementToBeClickable(deleteButton));
				deleteButton.click();	
				Thread.sleep(3000);
				//driver.switchTo().frame("contentIFrame1");
				//wait.until(ExpectedConditions.elementToBeClickable(srcCriTxt));


				driver.switchTo().defaultContent();
			}		
			//flag = CommonFunctions.TableData.getDataFromColumnInTable(gridTable, 1, name);
		}
		catch(Exception e)
		{
			System.out.println("Found Exception : " + e);
		}
	}

	public void deleteRule(String name)
	{


		try{
			wait.until(ExpectedConditions.elementToBeClickable(cohortSelectionRuleTab)).click();
			driver.switchTo().frame("contentIFrame0");
			wait.until(ExpectedConditions.elementToBeClickable(srcCriTxt));
			srcCriTxt.clear();

			srcCriTxt.sendKeys(name);
			srcCriIcon.click();

			wait.until(ExpectedConditions.elementToBeClickable(firstRecord)).click();

			Thread.sleep(3000);

			driver.switchTo().defaultContent();


			wait.until(ExpectedConditions.elementToBeClickable(ruleDelete)).click();
			wait.until(ExpectedConditions.elementToBeClickable(ruleDeleteLink)).click();


			Thread.sleep(2000);
			//System.out.println(driver.getTitle());
			System.out.println(inlineDialog.isDisplayed());

			if (inlineDialog.isDisplayed())

			{
				wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("InlineDialog_Iframe")));
				//driver.switchTo().frame("InlineDialog_Iframe");
				wait.until(ExpectedConditions.visibilityOf(deleteButton));
				wait.until(ExpectedConditions.elementToBeClickable(deleteButton));
				deleteButton.click();			  
				driver.switchTo().defaultContent();
			}		
			//flag = CommonFunctions.TableData.getDataFromColumnInTable(gridTable, 1, name);
		}
		catch(Exception e)
		{
			System.out.println("Found Exception : " + e);
		}

	}

	public Cohortselect clickNewRule() throws InterruptedException
	{
		wait.until(ExpectedConditions.visibilityOf(Newbutton));
		Actions action = new Actions(driver);
		action.doubleClick(Newbutton).perform();
		//Thread.sleep(4000);
		//wait.until(ExpectedConditions.elementToBeClickable(Newbutton1)).click();
		wait.until(ExpectedConditions.elementToBeClickable(SaveButton));
		return new Cohortselect(driver);
	}

	public Cohortselect setRulePriority(String name)
	{
		wait.until(ExpectedConditions.visibilityOf(Newbutton));
		Actions action = new Actions(driver);
		action.doubleClick(Newbutton).perform();
		wait.until(ExpectedConditions.elementToBeClickable(SaveButton));
		return new Cohortselect(driver);
	}

	public String Cohortselectrulename()
	{
		//String name = "AutoCohortRule";
		String name = utilities.ExcelUtilities.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Cohort", "RuleName");
		name = helpers.CommonFunctions.generateTS(name);
		return name;
	}

	public Cohortselect Addingcohortrule(String ruleName) throws InterruptedException
	{
		try {

			String Rulename = null;
			/*driver.navigate().refresh();
			wait.until(ExpectedConditions.visibilityOf(Newbutton));
			Actions action = new Actions(driver);
			action.doubleClick(Newbutton).perform();*/
			Thread.sleep(2000);

			wait.until(ExpectedConditions.elementToBeClickable(Newbutton)).click();
			//wait.until(ExpectedConditions.elementToBeClickable(saveAndCloseLink));

			driver.switchTo().frame("contentIFrame1");


			/* DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			 Date date = new Date();
			 String date1= dateFormat.format(date);*/


			/*String rule =  utilities.ExcelUtilities.getKeyValueFromExcel("CohortRule.xlsx", "Cohort", "NewTestname");
			//String Rulename = (NewTestnameexcel + date);

			Rulename = CommonFunctions.CommonFunc.generateTS(rule);*/
			wait.until(ExpectedConditions.elementToBeClickable(Newrulename)).sendKeys(ruleName);



			//String Surnameexcel =  utilities.ExcelUtilities.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Cervial", "Surname");
			//	System.out.println(Surnameexcel);
			String Lowerageexcel =  utilities.ExcelUtilities.getKeyValueFromExcel("CohortRule.xlsx", "Cohort", "Lowerage");
			wait.until(ExpectedConditions.elementToBeClickable(Loweragefield)).click();
			wait.until(ExpectedConditions.elementToBeClickable(Lowerage)).sendKeys(Lowerageexcel);

			String Upperageexcel =  utilities.ExcelUtilities.getKeyValueFromExcel("CohortRule.xlsx", "Cohort", "Upperage");
			wait.until(ExpectedConditions.elementToBeClickable(Upperagefield)).click();
			wait.until(ExpectedConditions.elementToBeClickable(Upperage)).sendKeys(Upperageexcel);

			wait.until(ExpectedConditions.elementToBeClickable(Deliverymethodfield)).click();
			new Select(Deliverymethod).selectByIndex(2);

			String Duedateexcel =  utilities.ExcelUtilities.getKeyValueFromExcel("CohortRule.xlsx", "Cohort", "Duedate");
			wait.until(ExpectedConditions.elementToBeClickable(Duedatefield)).click();
			wait.until(ExpectedConditions.elementToBeClickable(Duedate)).sendKeys(Duedateexcel);

			String Beforetestduedateexcel =  utilities.ExcelUtilities.getKeyValueFromExcel("CohortRule.xlsx", "Cohort", "Beforetestduedate");
			wait.until(ExpectedConditions.elementToBeClickable(Beforetestduefield)).click();
			wait.until(ExpectedConditions.elementToBeClickable(Beforetestdue)).sendKeys(Beforetestduedateexcel);

			String Reminderexcel =  utilities.ExcelUtilities.getKeyValueFromExcel("CohortRule.xlsx", "Cohort", "Reminder");
			wait.until(ExpectedConditions.elementToBeClickable(Reminderfield)).click();
			wait.until(ExpectedConditions.elementToBeClickable(Reminder)).sendKeys(Reminderexcel);

			String NonResponderexcel =  utilities.ExcelUtilities.getKeyValueFromExcel("CohortRule.xlsx", "Cohort", "NonResponder");
			wait.until(ExpectedConditions.elementToBeClickable(NonResponderfield)).click();
			wait.until(ExpectedConditions.elementToBeClickable(NonResponder)).sendKeys(NonResponderexcel);

			driver.switchTo().defaultContent();
			wait.until(ExpectedConditions.elementToBeClickable(SaveButton)).click();





		}

		catch (NoSuchElementException e){
			System.out.println("No Element found." + e);


		}
		driver.switchTo().defaultContent();

		return new Cohortselect(driver);


	}

	public Cohortselect addapplicableCCG( String PriorityNo) throws InterruptedException {
		// TODO Auto-generated method stub
		try {
			//System.out.println(PriorityNumber);

			//String PriorityNo =  utilities.ExcelUtilities.getKeyValueFromExcel("CohortRule.xlsx", "Cohort", PriorityName);
			System.out.println("Priority No. : "+PriorityNo);

			JavascriptExecutor jse = (JavascriptExecutor)driver;
			jse.executeScript("window.scrollBy(0,-250)", "");
			Thread.sleep(2000);
			driver.switchTo().frame("contentIFrame1");
			Actions actions = new Actions(driver);
			actions.moveToElement(AddinghapplicableCCG);
			actions.click().build().perform();
			Thread.sleep(2000);
		/*	String winHandleBefore = driver.getWindowHandle();
			System.out.println("Window Handler before:"+winHandleBefore);
			driver.switchTo().defaultContent();
			// Perform the click operation that opens new window

			// Switch to new window opened
			for(String winHandle : driver.getWindowHandles())
			{
				driver.switchTo().window(winHandle);
				System.out.println(winHandle);
				driver.switchTo().frame("contentIFrame0");
				if(!AddinghapplicableCCG.isDisplayed())
				break;
			}*/
			
				
		

			helpers.WindowHandleSupport.getRequiredWindowDriver(driver,"CohortToOrganisation");
		
			driver.switchTo().frame("contentIFrame0");
	
					//wait.until(ExpectedConditions.elementToBeClickable(cohortToOrgSave));
			wait.until(ExpectedConditions.elementToBeClickable(Organizationnamefield));
			
			wait.until(ExpectedConditions.elementToBeClickable(Organizationname)).sendKeys("Kate CCG 1");
			wait.until(ExpectedConditions.elementToBeClickable(Organizationname)).sendKeys(Keys.ENTER);
			wait.until(ExpectedConditions.elementToBeClickable(Organizationname)).sendKeys(Keys.ENTER);
			driver.switchTo().defaultContent();
			wait.until(ExpectedConditions.elementToBeClickable(SaveonpageButton)).click();

		//	driver.switchTo().window(winHandleBefore); 
			helpers.WindowHandleSupport.getRequiredWindowDriver(driver,"Cohort Selection Rule");
			
			
			driver.switchTo().frame("contentIFrame1");

			


			//	 wait.until(ExpectedConditions.elementToBeClickable(Savebuttononpage)).click();
			// driver.navigate().refresh();
			// wait.until(ExpectedConditions.elementToBeClickable(Savebuttononpagefield)).click();
			wait.until(ExpectedConditions.elementToBeClickable(Savebuttononpage)).click();
			// driver.switchTo().frame("contentIFrame1");
			//	 wait.until(ExpectedConditions.visibilityOf(Priorityfield));
			//	wait.until(ExpectedConditions.elementToBeClickable(Priorityfield)).click();
			wait.until(ExpectedConditions.elementToBeClickable(Priority)).click();
			String Name = wait.until(ExpectedConditions.elementToBeClickable(Priority)).getText();
			System.out.println(Name);
			wait.until(ExpectedConditions.elementToBeClickable(Priority)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(Priority)).sendKeys(PriorityNo);
			wait.until(ExpectedConditions.elementToBeClickable(Priority)).sendKeys(Keys.TAB);
			wait.until(ExpectedConditions.elementToBeClickable(Savebuttononpage)).click();
			Thread.sleep(2000);
			driver.switchTo().defaultContent();

			

		}


		catch (NoSuchElementException e){
			System.out.println("No added applicable CCG" + e);


		}
		return new Cohortselect(driver);
	}

	public CrmHome updateFirstCohortRule(String FirstPriorityRule){
		try{

			Thread.sleep(3000);
			driver.switchTo().frame("contentIFrame1");
			wait.until(ExpectedConditions.elementToBeClickable(Priorityfield)).click();

			String Name = wait.until(ExpectedConditions.elementToBeClickable(Priority)).getText();
			System.out.println(Name);
			wait.until(ExpectedConditions.elementToBeClickable(Priority)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(Priority)).sendKeys(FirstPriorityRule);
			wait.until(ExpectedConditions.elementToBeClickable(Priority)).sendKeys(Keys.TAB);
			wait.until(ExpectedConditions.elementToBeClickable(Savebuttononpage)).click();
			driver.switchTo().defaultContent();
		}             


		catch(Exception e)
		{
			System.out.println("Found Exception : " + e);
		}



		return new CrmHome(driver);



	}

	public Cohortselect DeleteexistingCohortrule(String CohortRule){
		try{

			wait.until(ExpectedConditions.elementToBeClickable(cohortSelectionRuleTab)).click();
			driver.switchTo().frame("contentIFrame0");
			wait.until(ExpectedConditions.elementToBeClickable(srcCriTxt));
			srcCriTxt.clear();

			srcCriTxt.sendKeys(CohortRule);
			srcCriTxt.sendKeys(Keys.ENTER);
			Thread.sleep(5000);
			Actions actions = new Actions(driver);

			//  wait.until(ExpectedConditions.elementToBeClickable(CheckboxDiv)).click();
			actions.moveToElement(CheckallResult);
			actions.click().build().perform();

			//   wait.until(ExpectedConditions.elementToBeClickable(CheckallResult)).click();

			Thread.sleep(5000);
			driver.switchTo().defaultContent();

			wait.until(ExpectedConditions.elementToBeClickable(DeleteButton)).click();

			if(DeleteButtonPopUP.isDisplayed())
			{

				/*    }


      wait.until(ExpectedConditions.elementToBeClickable(ruleDelete)).click();
      wait.until(ExpectedConditions.elementToBeClickable(ruleDeleteLink)).click();


      Thread.sleep(2000);
      //System.out.println(driver.getTitle());
      System.out.println(inlineDialog.isDisplayed());

      if (inlineDialog.isDisplayed())*/

				// {
				wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("InlineDialog_Iframe")));
				//driver.switchTo().frame("InlineDialog_Iframe");
				wait.until(ExpectedConditions.visibilityOf(deleteButton));
				wait.until(ExpectedConditions.elementToBeClickable(deleteButton));
				deleteButton.click();                     
				driver.switchTo().defaultContent();
			}             
			//flag = CommonFunctions.TableData.getDataFromColumnInTable(gridTable, 1, name);
		}
		catch(Exception e)
		{
			System.out.println("Found Exception : " + e);
		}



		return new Cohortselect(driver);



	}
	
		public Cohortselect searchFirstCohortRule(String FirstCohortRuleNameExcel){
				try{
					wait.until(ExpectedConditions.elementToBeClickable(cohortSelectionRuleTab)).click();
		            driver.switchTo().frame("contentIFrame0");
		            wait.until(ExpectedConditions.elementToBeClickable(srcCriTxt));
		            srcCriTxt.clear();
		            srcCriTxt.sendKeys(FirstCohortRuleNameExcel);
		            srcCriTxt.sendKeys(Keys.ENTER);
		            Thread.sleep(5000);
		        //    driver.switchTo().frame("contentIFrame1");
		            System.out.println(firstRecord.getText());
					Actions action = new Actions(driver);
					action.doubleClick(firstRecord).perform();
		          //  wait.until(ExpectedConditions.elementToBeClickable(firstRecord)).click();
		            }             
		           
		     
		     catch(Exception e)
		     {
		            System.out.println("Found Exception : " + e);
		     }

				
			
			return new Cohortselect(driver);
			


			} 

		
		


}





