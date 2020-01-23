package pageobjects.PL;
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
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Screenshot;
import helpers.Support;
import utilities.ExcelUtilities;

public class InsuranceDetails extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	
	@FindBy(name="btnSaveNext")
	WebElement Save_Submit;

	@FindBy(id="InsuranceDetails")
	WebElement InsuranceTab;
	
	@FindBy(xpath ="//*[@id='HasNotProvidedEvidenceOfCover']")
	WebElement AgreeCheck;
	
	
	@FindBy(id ="CertificateNumber")
	WebElement CertificateNumebr;
	
	@FindBy(name ="CoverStartDate")
	WebElement StartDate;
	
	@FindBy(name ="CoverEndDate")
	WebElement EndDate;
	
	@FindBy(id ="upload1")
	WebElement ChooseButton;
	
/*	@FindBy(xpath="//*[@class='btn btn-primary']")
	WebElement ChooseButton;*/
	
/*	@FindBy(xpath ="//div[@id='UploadControlForEvidenceOfEntitlement']/div/div/label/span")
	WebElement ChooseButton;*/

	
	@FindBy(id ="browseBtn")
	WebElement Browsebutton;
	
	@FindBy(id ="ProviderName")
	WebElement InsProviderName;	
	
	@FindBy(xpath = "//*[@id='tblUploadedEvidenceofCover']//tbody")
	WebElement Uploadtable;
	
	
	String CoverTitle_Ins = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Insurance", "Cover",1);
	String InsuranceName_Ins = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Insurance", "InsuranceName",1);
	String StartDate_Ins = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Insurance", "StartDate",1);
	String EndDate_Ins = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Insurance", "EndDate",1);
	String CertificateNumber_Ins = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Insurance", "CertificateNumber",1);
	String IndemnityCover_Ins = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Insurance", "IndemnityCover",1);
	
	public InsuranceDetails(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}

	public String Selectcover() throws InterruptedException, IOException {
		String ActualTablename = null;
		try{
			scrolltoElement(driver, InsuranceTab);
		wait.until(ExpectedConditions.elementToBeClickable(InsuranceTab));
		ActualTablename = InsuranceTab.getAttribute("id");
		System.out.println(ActualTablename);
		helpers.CommonFunctions.ClickOnRadioButton_type(CoverTitle_Ins, driver);
		
		  /*List<WebElement> CoverRadios=driver.findElements(By.xpath("//*[@id='CoverTypeCode']"));
		  System.out.println("total Radio Button for Cover "+CoverRadios.size());
          for (WebElement CoverRadio : CoverRadios)
          {                   
        	   String CoverradioText = CoverRadio.getText();  
        	 
        	  if (CoverradioText.equalsIgnoreCase(CoverTitle_Ins))
        	  {        		 
        			wait.until(ExpectedConditions.elementToBeClickable(CoverRadio)).click();
        		break;
        		  
        	  }
          }*/
		scrolltoElement(driver, CertificateNumebr);
		wait.until(ExpectedConditions.elementToBeClickable(CertificateNumebr)).clear();
		wait.until(ExpectedConditions.elementToBeClickable(CertificateNumebr)).sendKeys(CertificateNumber_Ins);
		helpers.CommonFunctions.ClickOnCheckBox(driver, IndemnityCover_Ins);
		
		wait.until(ExpectedConditions.elementToBeClickable(InsProviderName)).clear();
		wait.until(ExpectedConditions.elementToBeClickable(InsProviderName)).sendKeys(InsuranceName_Ins);
		scrolltoElement(driver, StartDate);
		wait.until(ExpectedConditions.elementToBeClickable(StartDate)).clear();
		wait.until(ExpectedConditions.elementToBeClickable(StartDate)).sendKeys(StartDate_Ins);
		wait.until(ExpectedConditions.elementToBeClickable(StartDate)).sendKeys(Keys.TAB);
		scrolltoElement(driver, EndDate);
		wait.until(ExpectedConditions.elementToBeClickable(EndDate)).clear();
		wait.until(ExpectedConditions.elementToBeClickable(EndDate)).sendKeys(EndDate_Ins);
		wait.until(ExpectedConditions.elementToBeClickable(EndDate)).sendKeys(Keys.TAB);
		
		Thread.sleep(1000);
		List<String> Extensions = ExcelUtilities.getCellValuesInExcel("PerformerPortal.xlsx", "FileExtension", 1);
		for(String Extension : Extensions)
		{
		scrolltoElement(driver, ChooseButton);
		JavascriptExecutor jse = (JavascriptExecutor)driver; 
		jse.executeScript("arguments[0].scrollIntoView();", ChooseButton);
		//jse.executeScript("arguments[0].click();", ChooseButton);
		String filePath = System.getProperty("user.dir") + "\\Upload\\"+Extension;			
		//helpers.CommonFunctions.Uploadfile(filePath, driver);
		ChooseButton.sendKeys(filePath);
		wait.until(ExpectedConditions.elementToBeClickable(Browsebutton)).click();
		helpers.CommonFunctions.PageLoadExternalwait(driver);
		}

		
	/*	scrolltoElement(driver, ChooseButton);
		wait.until(ExpectedConditions.elementToBeClickable(ChooseButton)).click();
	//	wait.until(ExpectedConditions.elementToBeClickable(ChooseButton)).clear();
		Thread.sleep(500);
		String filePath = System.getProperty("user.dir") + "\\Upload\\sample.txt";		
		helpers.CommonFunctions.Uploadfile(filePath, driver);
		Thread.sleep(2000);
		wait.until(ExpectedConditions.elementToBeClickable(Browsebutton)).click();	*/
		Thread.sleep(1000);
		scrolltoElement(driver, AgreeCheck);
	/*	
		Boolean Checkboxstatus = AgreeCheck.isSelected();
		if(Checkboxstatus == false)
		{*/
		if(AgreeCheck.isEnabled())
		{
  		wait.until(ExpectedConditions.elementToBeClickable(AgreeCheck)).click();
  		if (!(AgreeCheck.isSelected()))
  		{
  	  		wait.until(ExpectedConditions.elementToBeClickable(AgreeCheck)).click();
  		}
		}
		}
		catch (NoSuchElementException e)
		{
			System.out.println("The Data is not filled properly on capacity Tab ."+e);
		}


		return ActualTablename;
	
	}

	public CreateNewApp ClickOnSave_Submit() throws InterruptedException {
		try{
			scrolltoElement(driver, Save_Submit);
			Thread.sleep(3000);
			Actions action = new Actions(driver);
			action.moveToElement(Save_Submit);
			action.doubleClick().build().perform();
			helpers.CommonFunctions.PageLoadExternalwait(driver);
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found Savebutton" +e);
		}	
		return new CreateNewApp(driver);
	}
	
	public Boolean verifyCount(int ExpectedCount) {
		boolean Uploadcount = false;
		try{
			Thread.sleep(4000);
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

	public void Screenshot(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, Uploadtable);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
	}

	
	
}
