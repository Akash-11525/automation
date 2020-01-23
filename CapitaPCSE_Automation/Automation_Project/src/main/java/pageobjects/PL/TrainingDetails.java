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

public class TrainingDetails extends Support {
	
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="ChildProtectionTrainingLevel")
	WebElement TrainingLevel;	
	
	@FindBy(name="ChildProtectionTrainingDate")
	WebElement TrainingDate;
	
	@FindBy(name="AdultSafeguardingTrainingDate")
	WebElement AdultSafeTrainingDate;
	
	@FindBy(name="CardioPulmonaryRApplicationContext_PerformerTypeCodeesuscitationTrainingDate")
	WebElement CardioTrainingDate;
	
	@FindBy(id="CPDComplianceDetails")
	WebElement ComplianceDetails;
	
	@FindBy(id="TrainingDetails")
	WebElement TrainingDetails;
	
	@FindBy(name="btnSaveNext")
	WebElement Save_Submit;
	
	@FindBy(id="upload1")
	WebElement Uploadfilebutton;
	
	@FindBy(id="browseBtn")
	WebElement BrowseButton;
	
	@FindBy(xpath = "//*[@id='tblUploadedviewDataChildProtectionLevelDocument']//tbody")
	WebElement Uploadtable;
	
	String TrainingLevel_TrainingDetails = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "TrainingDetails", "Training Level",1);
	String ChildTrainingDate_TrainingDetails = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "TrainingDetails", "Child Training date",1);	
	String AdultTrainingDate_TrainingDetails = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "TrainingDetails", "Adult Training date",1);
	String CardioDate_TrainingDetails = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "TrainingDetails", "Cardio date",1);
	String ComplianceDetail_TrainingDetails = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "TrainingDetails", "Compliance Detail",1);
			
	public TrainingDetails(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 60);
		PageFactory.initElements(this.driver, this);
	}


	public String EnterTrainingDetails(int Position) throws InterruptedException, IOException {
		String ActualTablename_TrainingDetails = null;
		try{
			String PerformerList_cap = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Capacity", "PerformerList",Position);
			wait.until(ExpectedConditions.elementToBeClickable(TrainingDetails));
			ActualTablename_TrainingDetails = TrainingDetails.getAttribute("id");
			System.out.println(ActualTablename_TrainingDetails);
			scrolltoElement(driver, TrainingLevel);
			wait.until(ExpectedConditions.elementToBeClickable(TrainingLevel)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(TrainingLevel)).sendKeys(TrainingLevel_TrainingDetails);
			wait.until(ExpectedConditions.elementToBeClickable(TrainingDate)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(TrainingDate)).sendKeys(ChildTrainingDate_TrainingDetails);
			wait.until(ExpectedConditions.elementToBeClickable(TrainingDate)).sendKeys(Keys.TAB);
			wait.until(ExpectedConditions.elementToBeClickable(AdultSafeTrainingDate)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(AdultSafeTrainingDate)).sendKeys(AdultTrainingDate_TrainingDetails);
			wait.until(ExpectedConditions.elementToBeClickable(AdultSafeTrainingDate)).sendKeys(Keys.TAB);
			if(PerformerList_cap.startsWith("Medical")||PerformerList_cap.startsWith("Dental"))
			{	
			wait.until(ExpectedConditions.elementToBeClickable(CardioTrainingDate)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(CardioTrainingDate)).sendKeys(CardioDate_TrainingDetails);
			wait.until(ExpectedConditions.elementToBeClickable(CardioTrainingDate)).sendKeys(Keys.TAB);
			}
			wait.until(ExpectedConditions.elementToBeClickable(ComplianceDetails)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(ComplianceDetails)).sendKeys(ComplianceDetail_TrainingDetails);
			
			// Amit : Upload file of Child Protection Training Attended
			
			Thread.sleep(2000);
			List<String> Extensions = ExcelUtilities.getCellValuesInExcel("PerformerPortal.xlsx", "FileExtension", 1);
			for(String Extension : Extensions)
			{
			scrolltoElement(driver, Uploadfilebutton);
			JavascriptExecutor jse = (JavascriptExecutor)driver; 
			jse.executeScript("arguments[0].scrollIntoView();", Uploadfilebutton);
	//		jse.executeScript("arguments[0].click();", Uploadfilebutton);
			String filePath = System.getProperty("user.dir") + "\\Upload\\"+Extension;			
			//helpers.CommonFunctions.Uploadfile(filePath, driver);
			Uploadfilebutton.sendKeys(filePath);
			wait.until(ExpectedConditions.elementToBeClickable(BrowseButton)).click();
			//driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			helpers.CommonFunctions.PageLoadExternalwait(driver);
			}
		//	wait.until(ExpectedConditions.elementToBeClickable(BrowseButton)).click();	
		//	Thread.sleep(5000);
		
		
		}
		catch(NoSuchElementException e)
		{
			e.printStackTrace();
			System.out.println("The element is not found on Training Details" +e);
		}	
		return ActualTablename_TrainingDetails;		
	}

	public CreateNewApp ClickOnSave_Submit() throws InterruptedException {
		try{
			Thread.sleep(3000);
			Actions action = new Actions(driver);
			action.moveToElement(Save_Submit);
			action.doubleClick().build().perform();
			helpers.CommonFunctions.PageLoadExternalwait(driver);
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found Savebutton - Training tab" +e);
		}	
		return new CreateNewApp(driver);
	}


	public Boolean verifyCount(int expecteddownload) {
		boolean Uploadcount = false;
		try{
			 List<WebElement> rows = Uploadtable.findElements(By.tagName("tr"));
			 int Actualcount = rows.size();
			 if(expecteddownload == Actualcount)
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


	public void screenshot(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, TrainingLevel);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		
		
	}
	

}
