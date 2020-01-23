package pageobjects.PL;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Support;

public class HealthClearance extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="OccupationalHealthClearance")
	WebElement HealthPreferenceTab;
	
	@FindBy(id="upload1")
	WebElement Uploadfilebutton;
	
	/*@FindBy(xpath="//*[@class='btn btn-primary']")
	WebElement Uploadfilebutton;*/
	
	@FindBy(id="browseBtn")
	WebElement BrowseButton;
	
	@FindBy(id="IsOccupationalCertificateEPPStandard")
	WebElement CertificateCheckBox;
	
	
	@FindBy(name="btnSaveNext")
	WebElement Save_Submit;
	

	public HealthClearance(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 60);
		PageFactory.initElements(this.driver, this);
	}

	public String EnterHealthClerancedetails(int position ) throws InterruptedException, IOException {
		String ActualTablename_HealthClerance = null;
		try{
			wait.until(ExpectedConditions.elementToBeClickable(HealthPreferenceTab));
			ActualTablename_HealthClerance = HealthPreferenceTab.getAttribute("id");
			//scrolltoElement(driver, Uploadfilebutton);
			
			/*wait.until(ExpectedConditions.elementToBeClickable(Uploadfilebutton)).click();
			Thread.sleep(3000);
			String filePath = System.getProperty("user.dir") + "\\Upload\\sample.txt";			
			helpers.CommonFunctions.Uploadfile(filePath, driver);
			Thread.sleep(3000);*/
			
			scrolltoElement(driver, Uploadfilebutton);
			JavascriptExecutor jse = (JavascriptExecutor)driver; 
			jse.executeScript("arguments[0].scrollIntoView();", Uploadfilebutton);
		//	jse.executeScript("arguments[0].click();", Uploadfilebutton);
			String filePath = System.getProperty("user.dir") + "\\Upload\\sample.txt";			
			//helpers.CommonFunctions.Uploadfile(filePath, driver);
			Uploadfilebutton.sendKeys(filePath);
			Thread.sleep(1000);
			
		//	wait.until(ExpectedConditions.elementToBeClickable(Uploadfilebutton)).clear();
			
/*			String filePath = System.getProperty("user.dir") + "\\Upload\\FileUpload.exe";
			System.out.println(filePath);
			Runtime.getRuntime().exec(filePath);*/
			
			wait.until(ExpectedConditions.elementToBeClickable(BrowseButton)).click();	
			helpers.Support.PageLoadExternalwait(driver);
			String Medicalperfomer = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx","Capacity","Medical Performer", position);
			if(!(Medicalperfomer.equalsIgnoreCase("Ophthalmic medical practitioner")))
			{
				if(CertificateCheckBox.isDisplayed())
				{
					if(!CertificateCheckBox.isSelected())
					{
						wait.until(ExpectedConditions.elementToBeClickable(CertificateCheckBox)).click();
					}
				}
			}
			}
		catch (NoSuchElementException e)
		{
		System.out.println("The information on healthclerance is not filled properly "+e);
		}
	
	
		return ActualTablename_HealthClerance;
	}

	public CreateNewApp ClickOnSave_Submit() throws InterruptedException {
		try{
			
			scrolltoElement(driver, Save_Submit);
			Actions action = new Actions(driver);
			action.moveToElement(Save_Submit);
			action.doubleClick().build().perform();
			helpers.CommonFunctions.PageLoadExternalwait(driver);
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found Savebutton - Healthclearance tab" +e);
		}	
		return new CreateNewApp(driver);
	}

}
