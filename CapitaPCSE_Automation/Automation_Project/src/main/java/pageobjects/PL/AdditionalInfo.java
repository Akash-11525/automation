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
import utilities.ExcelUtilities;

public class AdditionalInfo extends Support{
	
	WebDriver driver;
	WebDriverWait wait;

	@FindBy(id="browseBtn")
	WebElement BrowseButton;
	
	@FindBy(id="AdditionalInformation")
	WebElement AdditionalInfoTab;
	
	@FindBy(xpath="//*[@class ='form-control']")
	WebElement AdditionalInfoTextField;
	
	@FindBy(name="btnSaveNext")
	WebElement Save_Submit;
	
	@FindBy(id="EvidenceOfAdditionalDocumentDetails")
	WebElement Uploadfilebutton;
	
	@FindBy(xpath = "//*[@id='tblAdditionalDocumentDetails']//tbody")
	WebElement Uploadtable;


	
	String Info_AdditionalInfo = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdditionalInfo", "Info",1);
	
	
	public AdditionalInfo(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 60);
		PageFactory.initElements(this.driver, this);
	}





	public String EnterAdditionalInfo() {
		String ActualTablename_AdditionalInfo = null;
		try{
			wait.until(ExpectedConditions.elementToBeClickable(AdditionalInfoTab));
			ActualTablename_AdditionalInfo = AdditionalInfoTab.getAttribute("id");
			System.out.println(ActualTablename_AdditionalInfo);
			scrolltoElement(driver, AdditionalInfoTextField);
			wait.until(ExpectedConditions.elementToBeClickable(AdditionalInfoTextField)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(AdditionalInfoTextField)).sendKeys(Info_AdditionalInfo);	
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found on Additional Info tab" +e);
		}	
		return ActualTablename_AdditionalInfo;	
	}





	public CreateNewApp ClickOnSave_Submit() throws InterruptedException {
		try{
			Thread.sleep(1500);
			Actions action = new Actions(driver);
			action.moveToElement(Save_Submit);
			action.doubleClick().build().perform();
			helpers.CommonFunctions.PageLoadExternalwait(driver);
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found Savebutton - Capacitytab" +e);
		}	
		return new CreateNewApp(driver);
	}





	public AdditionalInfo upload() {
		try{
			List<String> Extensions = ExcelUtilities.getCellValuesInExcel("PerformerPortal.xlsx", "FileExtension", 1);
			for(String Extension : Extensions)
			{
		scrolltoElement(driver, Uploadfilebutton);
		JavascriptExecutor jse = (JavascriptExecutor)driver; 
		jse.executeScript("arguments[0].scrollIntoView();", Uploadfilebutton);
		//jse.executeScript("arguments[0].click();", Uploadfilebutton);
		String filePath = System.getProperty("user.dir") + "\\Upload\\"+Extension;			
		//helpers.CommonFunctions.Uploadfile(filePath, driver);
		Uploadfilebutton.sendKeys(filePath);
		wait.until(ExpectedConditions.elementToBeClickable(BrowseButton)).click();
		//driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		helpers.CommonFunctions.PageLoadExternalwait(driver); 
		}
		}
		catch(Exception e)
		{
			System.out.println("The upload functionlity on appraisal is not working" +e);
		}
		return new AdditionalInfo(driver);
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
		scrolltoElement(driver, AdditionalInfoTextField);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		
	}

}
