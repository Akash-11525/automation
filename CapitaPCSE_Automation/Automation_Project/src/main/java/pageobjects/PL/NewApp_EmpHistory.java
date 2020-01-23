package pageobjects.PL;
import java.awt.AWTException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.CommonFunctions;
import helpers.Screenshot;
import helpers.Support;
import utilities.ExcelUtilities;


public class NewApp_EmpHistory  extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="TitleCode")
	WebElement Title;
	
	@FindBy(id="browseBtn")
	WebElement BrowseButton;
	
	@FindBy(id="upload")
	WebElement UploadinputText;
	
	@FindBy(name="btnSaveNext")
	WebElement Save_Submit;
	
	@FindBy(id="upload1")
	WebElement Uploadfilebutton;
	
	@FindBy(xpath="//*[@class='btn btn-primary']")
	WebElement Browsebutton1;
	
	@FindBy(xpath="//*[@id='UploadControlFordivEmpCVUpload']//div[1]//input[@class='form-control']")
	WebElement BrowsebuttonTextField;
	
	@FindBy(id="btnNewRecord")
	WebElement AddNewEmpRecord;
	
	@FindBy(name="StartDate")
	WebElement Startdate;
	
	@FindBy(name="EndDate")
	WebElement Enddate;
	
	@FindBy(id="WasDismissed")
	WebElement DismissedCheck;
	
	@FindBy(id="OrganisationName")
	WebElement OrgName;	
	
	@FindBy(id="Specialty")
	WebElement SpecialtyName;
	
	@FindBy(id="RoleUndertaken")
	WebElement RoleUndertakenName;
	
	@FindBy(id="CapacityCode")
	WebElement CapacityCodeName;
	
	@FindBy(xpath="//button[@name='btnAddEditEmploymentRecord']")
	WebElement AddEmpHistoryRecord;
	
	@FindBy(id="DismissalExplanation")
	WebElement DismissExplanation;
	
	@FindBy(xpath = "//table[@class='table PCSE-WrapTable margin-bottom-15']")
	WebElement ResultTableid;
	
	@FindBy(xpath = "//table[@id='tblUploadedEmploymentHistory']//tr[1]/td[2]/button']")
	WebElement Deletebutton;
	
	@FindBy(xpath = "//div[@class='confirmation-buttons text-center']/div/a[2]']")
	WebElement Continuebutton;
	
	@FindBy(xpath = "//div[@class='toast-container']")
	WebElement UplaodMessage;
	
	@FindBy(xpath = "//div[@class='toast-container']//div[@class='toast-message']")
	WebElement UplaodSucessMessage;
	
	@FindBy(xpath = "//*[@id='tblUploadedEmploymentHistory']//tbody")
	WebElement Uploadtable;
	
	@FindBy(xpath = "//select[@id='OrganizationTypes']")
	WebElement OrgnizationType;
	
	@FindBy(xpath ="//table[@class='table PCSE-WrapTable margin-bottom-15']//tr[1]/td[4]/button[1]")
	WebElement EmpTable_Add;
	
	
	
	
	String Emp_StartDate_EmpHistory = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Emp_History", "Emp_StartDate",1);
	String Emp_EndDate_EmpHistory = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Emp_History", "Emp_EndDate",1);	
	String NHSCheckBox_EmpHistory = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Emp_History", "NHSCheckBox",1);
	String Org_Name_EmpHistory = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Emp_History", "Org_Name",1);
	String Specialty_EmpHistory = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Emp_History", "Specialty",1);
	String Role_EmpHistory = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Emp_History", "Role",1);
	String Capacity_EmpHistory = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Emp_History", "Capacity",1);
	String Details_Role_EmpHistory = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Emp_History", "Details_Role",1);
	String Update_Emp_StartDate_EmpHistory = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Emp_History", "Emp_StartDate",2);
	String Update_Emp_EndDate_EmpHistory = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Emp_History", "Emp_EndDate",2);	
		
	public NewApp_EmpHistory(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}
	
	
	public  NewApp_EmpHistory Upload() throws InterruptedException, AWTException, IOException {
		try{
			
			// This below code is used when we required active to element around choose file 
			// Link 9020
			/*Thread.sleep(1000);
			scrolltoElement(driver, Browsebutton1);
			wait.until(ExpectedConditions.elementToBeClickable(Browsebutton1)).getAttribute("id");
			wait.until(ExpectedConditions.elementToBeClickable(Browsebutton1)).click();
			String filePath = System.getProperty("user.dir") + "\\Upload\\sample.txt";			
			helpers.CommonFunctions.Uploadfile(filePath, driver);
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);			
			wait.until(ExpectedConditions.elementToBeClickable(BrowseButton)).click();*/
			
			
			
			// This is below code is used to Simple upload a file 
			// link 9030
			
		//	helpers.CommonFunctions.PageLoadExternalwait(driver);
			
			List<String> Extensions = ExcelUtilities.getCellValuesInExcel("PerformerPortal.xlsx", "FileExtension", 1);
			for(String Extension : Extensions)
			{
				scrolltoElement(driver, Uploadfilebutton);
				/*JavascriptExecutor jse = (JavascriptExecutor)driver;            	
				jse.executeScript("arguments[0].scrollIntoView();", Uploadfilebutton);*/ //Commented by Rupesh -We have changed the code for scroll screen.
				
			//	jse.executeScript("arguments[0].click();", Uploadfilebutton); 
				String filePath = System.getProperty("user.dir") + "\\Upload\\"+Extension;			
			//helpers.CommonFunctions.Uploadfile(filePath, driver);
				Uploadfilebutton.sendKeys(filePath);
				wait.until(ExpectedConditions.elementToBeClickable(BrowseButton)).click();
				helpers.CommonFunctions.PageLoadExternalwait(driver);
			}
			
			
			/*Thread.sleep(3000);
			String filePath = System.getProperty("user.dir") + "\\Upload\\FileUpload.exe";
			System.out.println(filePath);
		//	String filePath = System.getProperty("user.dir") + "\\Upload\\FileUpload.exe";
		//	System.out.println(filePath);
			Runtime.getRuntime().exec(filePath);

			Thread.sleep(5000);
			*/
		
		//	scrolltoElement(driver, Uploadfilebutton);
		//	JavascriptExecutor jse = (JavascriptExecutor)driver; 
		//jse.executeScript("document.getElementById('upload1'‌​).style.visibility='‌​visible'");
			//jse.executeScript("arguments[0].scrollIntoView();", button);
			//JavascriptExecutor js = (JavascriptExecutor)driver;
		//jse.executeScript("arguments[0].click();", Uploadfilebutton);
			//wait.until(ExpectedConditions.elementToBeClickable(Uploadfilebutton)).click();
		
		//	wait.until(ExpectedConditions.elementToBeClickable(Browsebutton1)).;
			
		/*	Actions action = new Actions(driver);
			action.moveToElement(BrowseButton);
			action.build().perform()*/
		//	jse.executeScript("arguments[0].clear();", Uploadfilebutton);
			
		//	wait.until(ExpectedConditions.elementToBeClickable(Uploadfilebutton)).sendKeys(filePath);
		//	wait.until(ExpectedConditions.elementToBeClickable(BrowsebuttonTextField)).sendKeys(filePath);
			
		/*	WebElement uploadWindow = driver.switchTo().activeElement();
			uploadWindow.sendKeys(filePath);*/
		
		
			
			
			/*driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			
			wait.until(ExpectedConditions.elementToBeClickable(BrowseButton)).click();	*/
		

			/*System.out.println(filePath);
			//Uploadfilebutton.sendKeys(filePath);
			

            //  StringSelection ss = new StringSelection("Your file path ");
			StringSelection ss = new StringSelection(filePath);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
			
			Robot r = new Robot();
			
			
			// Calling key press event to press Enter Key from keyboard to place cursor in window textbox

			r.keyPress(KeyEvent.VK_ENTER);
			//Releaseing the Enter Key
			r.keyRelease(KeyEvent.VK_ENTER);
			
			
			* Now we are going to trigger CTRL+V action so first we will press CTRL and then V and finally will
			* release these key.
			
			r.keyPress(KeyEvent.VK_CONTROL);    
			r.keyPress(KeyEvent.VK_V);
			
			r.keyRelease(KeyEvent.VK_V);    
			r.keyRelease(KeyEvent.VK_CONTROL);
			
			// After pasting path now we are going to clivck on Open and that can be 
			//triggered by pressing enter key from Keyboard.

			r.keyPress(KeyEvent.VK_ENTER);
			r.keyRelease(KeyEvent.VK_ENTER);
			//Release open 
*/		/*			Actions action = new Actions(driver);
					action.moveToElement(BrowseButton);
					action.doubleClick().build().perform();*/
			
		//	wait.until(ExpectedConditions.elementToBeClickable(BrowseButton));	
	//		helpers.CommonFunctions.ClickOnButton("Save & Next", driver);
			
			}
			catch(NoSuchElementException e)
			{
				System.out.println("The file is not loaded on Employment history tab " +e);
			}
			return new NewApp_EmpHistory(driver);
			
			
	}
	
	public  int UploadExtension() throws InterruptedException, AWTException, IOException {
		int count = 0;
		try{
	
			List<String> Extensions = ExcelUtilities.getCellValuesInExcel("XMLStagingCRM.xlsx", "SystemJob", 1);
			for(String Extension : Extensions)
			{
				scrolltoElement(driver, Uploadfilebutton);
				JavascriptExecutor jse = (JavascriptExecutor)driver; 
				jse.executeScript("arguments[0].scrollIntoView();", Uploadfilebutton);
				jse.executeScript("arguments[0].click();", Uploadfilebutton);
				String filePath = System.getProperty("user.dir") + "\\Upload\\sample.txt";			
			//helpers.CommonFunctions.Uploadfile(filePath, driver);
				Uploadfilebutton.sendKeys(filePath);
				wait.until(ExpectedConditions.elementToBeClickable(BrowseButton)).click();
				wait.until(ExpectedConditions.elementToBeClickable(BrowseButton));
				if(UplaodMessage.isDisplayed())
				{
					System.out.println(UplaodSucessMessage.getText());
				}
				helpers.CommonFunctions.PageLoadExternalwait(driver);
			}
			
			}
			catch(NoSuchElementException e)
			{
				System.out.println("The file is not loaded on Employment history tab " +e);
			}
			return count;
			
			
	}
	
	public  NewApp_EmpHistory UploadNewDocument() throws InterruptedException, AWTException, IOException {
		try{
			scrolltoElement(driver, Uploadfilebutton);
		//	wait.until(ExpectedConditions.elementToBeClickable(Uploadfilebutton)).click();
			wait.until(ExpectedConditions.elementToBeClickable(Uploadfilebutton)).clear();
	
			String filePath = System.getProperty("user.dir") + "\\Upload\\sample.txt";
			
			helpers.CommonFunctions.Uploadfile(filePath, driver);
			
			wait.until(ExpectedConditions.elementToBeClickable(BrowseButton)).click();	
	
			Thread.sleep(5000);
			}
			catch(NoSuchElementException e)
			{
				System.out.println("The file is not loaded on Employment history tab " +e);
			}
			return new NewApp_EmpHistory(driver);
			
			
	}


	public  CreateNewApp SubmitClick() throws InterruptedException {
		Actions action = new Actions(driver);
		action.moveToElement(Save_Submit);
		action.doubleClick().build().perform();
		helpers.CommonFunctions.PageLoadExternalwait(driver);
		return new CreateNewApp(driver);
		
	}


	public NewApp_EmpHistory AddEmpHistoryRecord() throws InterruptedException {
		try {
			Thread.sleep(3000);
			scrolltoElement(driver, AddNewEmpRecord);
			wait.until(ExpectedConditions.elementToBeClickable(AddNewEmpRecord)).click();
			wait.until(ExpectedConditions.elementToBeClickable(Startdate));
			wait.until(ExpectedConditions.elementToBeClickable(Startdate)).sendKeys(Emp_StartDate_EmpHistory);
			wait.until(ExpectedConditions.elementToBeClickable(Startdate)).sendKeys(Keys.TAB);
			wait.until(ExpectedConditions.elementToBeClickable(Enddate)).sendKeys(Emp_EndDate_EmpHistory);	
			wait.until(ExpectedConditions.elementToBeClickable(Enddate)).sendKeys(Keys.TAB);
			Thread.sleep(3000);
			Select dropdown1 = new Select(OrgnizationType);
			dropdown1.selectByVisibleText(NHSCheckBox_EmpHistory);
			
			CommonFunctions.ClickOnCheckBox(driver, NHSCheckBox_EmpHistory);
			scrolltoElement(driver, OrgName);
			wait.until(ExpectedConditions.elementToBeClickable(OrgName));	
			wait.until(ExpectedConditions.elementToBeClickable(OrgName)).sendKeys(Org_Name_EmpHistory);
			wait.until(ExpectedConditions.elementToBeClickable(SpecialtyName)).sendKeys(Specialty_EmpHistory);
			wait.until(ExpectedConditions.elementToBeClickable(RoleUndertakenName)).sendKeys(Role_EmpHistory);
			scrolltoElement(driver, CapacityCodeName);
			Select dropdown = new Select(CapacityCodeName);
			dropdown.selectByVisibleText(Capacity_EmpHistory);
			wait.until(ExpectedConditions.elementToBeClickable(DismissedCheck)).click();
			wait.until(ExpectedConditions.elementToBeClickable(DismissExplanation)).sendKeys(Details_Role_EmpHistory);			
			Thread.sleep(3000);
			String AddButtonText = wait.until(ExpectedConditions.elementToBeClickable(AddEmpHistoryRecord)).getText();
			  Actions actions = new Actions(driver);
		    	actions.moveToElement(AddEmpHistoryRecord);
		    	actions.doubleClick(AddEmpHistoryRecord).build().perform();
		/*	String AddButtonText = wait.until(ExpectedConditions.elementToBeClickable(AddEmpHistoryRecord)).getText();
			wait.until(ExpectedConditions.elementToBeClickable(AddEmpHistoryRecord)).click();*/
			Thread.sleep(3000);
			
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not find on employment history page " +e);
		}
		return new NewApp_EmpHistory(driver);
		
	}


	public int verifyrowisadded() throws InterruptedException {
		int rowcount = 0;
		int hiderow = 0;
		try 
		{
			scrolltoElement(driver, Uploadfilebutton);
			Thread.sleep(3000);
			if(ResultTableid.isDisplayed())
			{
				List<WebElement> rows = ResultTableid.findElements(By.tagName("tr"));
						
				rowcount = rows.size() - 1;
					
				
			}
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not find on added emp history table " +e);
		}
				
		return rowcount;
	}


	public boolean verifydataadded() throws InterruptedException {
		boolean Addeddata = false;
		try{
			Thread.sleep(2500);
			scrolltoElement(driver, Uploadfilebutton);
			String Start_date = driver.findElement(By.xpath("//table[@class='table PCSE-WrapTable margin-bottom-15']//tr[1]/td[1]")).getText();
			String End_date = driver.findElement(By.xpath("//table[@class='table PCSE-WrapTable margin-bottom-15']//tr[1]/td[2]")).getText();
			String Org_Name = driver.findElement(By.xpath("//table[@class='table PCSE-WrapTable margin-bottom-15']//tr[1]/td[3]")).getText();
			if(Start_date.equalsIgnoreCase(Emp_StartDate_EmpHistory))				
			{
				if (End_date.equalsIgnoreCase(Emp_EndDate_EmpHistory))
				{
					if(Org_Name.equalsIgnoreCase(Org_Name_EmpHistory))
					{
						Addeddata = true;
					}
				}
			}
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not find on added emp history table " +e);
		}
				
		return Addeddata;
	}


	public NewApp_EmpHistory editAddemphistory() throws InterruptedException {
		try {
			scrolltoElement(driver, Uploadfilebutton);
			wait.until(ExpectedConditions.elementToBeClickable(EmpTable_Add)).click();
			scrolltoElement(driver, Startdate);
			wait.until(ExpectedConditions.elementToBeClickable(Startdate));
			wait.until(ExpectedConditions.elementToBeClickable(Startdate)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(Startdate)).sendKeys(Update_Emp_StartDate_EmpHistory);
			wait.until(ExpectedConditions.elementToBeClickable(Enddate)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(Enddate)).sendKeys(Update_Emp_EndDate_EmpHistory);	
			Thread.sleep(3000);
			scrolltoElement(driver, AddEmpHistoryRecord);
			String AddButtonText = wait.until(ExpectedConditions.elementToBeClickable(AddEmpHistoryRecord)).getText();
			wait.until(ExpectedConditions.elementToBeClickable(AddEmpHistoryRecord)).click();
			Thread.sleep(3000);
		}
		// TODO Auto-generated method stub
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not find on added emp history table " +e);
		}
		return new NewApp_EmpHistory(driver);
	}
	
	public boolean verifydataupdatedadded() throws InterruptedException {
		boolean Updateddata = false;
		try{
			Thread.sleep(5000);
			scrolltoElement(driver, Uploadfilebutton);
			String Start_date = driver.findElement(By.xpath("//table[@class='table PCSE-WrapTable margin-bottom-15']//tr[1]/td[1]")).getText();
			System.out.println(Start_date);
			String End_date = driver.findElement(By.xpath("//table[@class='table PCSE-WrapTable margin-bottom-15']//tr[1]/td[2]")).getText();
			System.out.println(End_date);
			System.out.println(Update_Emp_EndDate_EmpHistory);
			
			if(Start_date.equalsIgnoreCase(Update_Emp_StartDate_EmpHistory))				
			{
				if (End_date.equalsIgnoreCase(Update_Emp_EndDate_EmpHistory))
				{
					
					Updateddata = true;
					
				}
			}
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not find on added emp history table " +e);
		}
				
		return Updateddata;
	}


	public NewApp_EmpHistory deletepdf() {
		try{
			wait.until(ExpectedConditions.elementToBeClickable(Deletebutton));
				 Actions actions = new Actions(driver);
				 actions.moveToElement(Deletebutton);
				 actions.doubleClick(Deletebutton).build().perform();
			wait.until(ExpectedConditions.elementToBeClickable(Continuebutton));
				 Actions actions1 = new Actions(driver);
				 actions1.moveToElement(Continuebutton);
				 actions1.doubleClick(Continuebutton).build().perform();	 
				 
			
		}
		catch(Exception e)
		{
			System.out.println("The delete on employment history is not done");
		}
		return null;
	}


	public void screenshots(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, Uploadfilebutton);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		
	}


	public Boolean verifyCount(int ExpectedCount) {
		boolean Uploadcount = false;
		try{
			 List<WebElement> rows = Uploadtable.findElements(By.tagName("tr"));
			 int Actualcount = rows.size();
			 if(ExpectedCount == Actualcount)
			 {
				 Uploadcount = true;
			 }
		}
		catch(Exception e)
		{
			System.out.println("The Upload count is not captured");
		}
				
		return Uploadcount;
	}


	
}
