package pageobjects.PerformerPL;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Screenshot;
import helpers.Support;
public class PerformerType extends Support{
	
	WebDriver driver;
	WebDriverWait wait;	
	
	@FindBy(id="EvidenceOfTrainingMedical")
	WebElement TrainingMedicalUpload;
	
	@FindBy(id="browseBtn")
	WebElement Uploadbutton;
	
	@FindBy(id="EvidenceOfInsuranceMedical")
	WebElement InsuranceUpload;
	
	@FindBy(name="Submit_Decision")
	WebElement Submit;
	
	@FindBy(xpath="//div[@class='popover confirmation fade top in']")
	WebElement Popupmessage;
	
	@FindBy(xpath="//a[@class='btn btn-danger']")
	WebElement Cancel_Submit;
	
	@FindBy(xpath="//a[@class='btn btn-success']")
	WebElement Ok_Submit;
	
	@FindBy(id="EvidenceOfInsuranceDental")
	WebElement EvidenceInsuranceUpload;
	
	@FindBy(id="EvidenceOfFoundationTrainingDental")
	WebElement TrainingDentalUpload;
	
	@FindBy(name="PerformerType")
	WebElement PerformerTypeTab;
	
	public PerformerType(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}



	public PerformerType changeperformerType(String RadioText) {
		try{
			helpers.CommonFunctions.ClickOnRadioButton(RadioText, driver);
			
			scrolltoElement(driver, TrainingMedicalUpload);
			JavascriptExecutor jse = (JavascriptExecutor)driver; 
			jse.executeScript("arguments[0].scrollIntoView();", TrainingMedicalUpload);
			jse.executeScript("arguments[0].click();", TrainingMedicalUpload);
			String filePath = System.getProperty("user.dir") + "\\Upload\\sample.pdf";	
			TrainingMedicalUpload.sendKeys(filePath);
			
			scrolltoElement(driver, InsuranceUpload);
			JavascriptExecutor jse1 = (JavascriptExecutor)driver; 
			jse1.executeScript("arguments[0].scrollIntoView();", InsuranceUpload);
			jse1.executeScript("arguments[0].click();", InsuranceUpload);
			String filePath1 = System.getProperty("user.dir") + "\\Upload\\sample.pdf";	
			InsuranceUpload.sendKeys(filePath1);
			
			
		     List<WebElement> Uploadbuttons = driver.findElements(By.id("browseBtn"));
	            System.out.println(Uploadbuttons.size());
	            for (WebElement Uploadbutton:Uploadbuttons)
	            {
	            	Thread.sleep(1000);
	            	scrolltoElement(driver,Uploadbutton);
	            	if(Uploadbutton.isDisplayed())
	            	{
	            		Uploadbutton.click();
	            		Thread.sleep(500);
	            	}
	            }
		}
		catch(Exception e)
		{
			System.out.println("The performer type information is not filled");
		}
		return new PerformerType(driver);
	}

	
	public PerformerType changeperformerType_Dental(String RadioText) {
		try{
			helpers.CommonFunctions.ClickOnRadioButton(RadioText, driver);
			
			scrolltoElement(driver, EvidenceInsuranceUpload);
			JavascriptExecutor jse = (JavascriptExecutor)driver; 
			jse.executeScript("arguments[0].scrollIntoView();", EvidenceInsuranceUpload);
			jse.executeScript("arguments[0].click();", EvidenceInsuranceUpload);
			String filePath = System.getProperty("user.dir") + "\\Upload\\sample.pdf";	
			EvidenceInsuranceUpload.sendKeys(filePath);
			
			scrolltoElement(driver, TrainingDentalUpload);
			JavascriptExecutor jse1 = (JavascriptExecutor)driver; 
			jse1.executeScript("arguments[0].scrollIntoView();", TrainingDentalUpload);
			jse1.executeScript("arguments[0].click();", TrainingDentalUpload);
			String filePath1 = System.getProperty("user.dir") + "\\Upload\\sample.pdf";	
			TrainingDentalUpload.sendKeys(filePath1);
			
			
		     List<WebElement> Uploadbuttons = driver.findElements(By.id("browseBtn"));
	            System.out.println(Uploadbuttons.size());
	            for (WebElement Uploadbutton:Uploadbuttons)
	            {
	            	Thread.sleep(1000);
	            	scrolltoElement(driver,Uploadbutton);
	            	if(Uploadbutton.isDisplayed())
	            	{
	            		Uploadbutton.click();
	            		Thread.sleep(5000);
	            	}
	            }
		}
		catch(Exception e)
		{
			System.out.println("The performer type information is not filled");
		}
		return new PerformerType(driver);
	}



	public PerformerType clickonSubmit() {
		try{
			scrolltoElement(driver, Submit);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(Submit);
	    	actions1.click().build().perform();
	    	helpers.Support.PageLoadExternalwait_Performer(driver);
		}
		catch(Exception e)
		{
			System.out.println("The Submit button is not clicked");
		}
		return new PerformerType(driver) ;
	}

	public boolean verifypopupmessage() {
		boolean popupmessage = false;
		try{
			boolean ispresent = driver.findElements(By.xpath("//div[@class='popover confirmation fade top in']")).size() != 0;
			System.out.println(ispresent);
			//scrolltoElement(driver, Popupmessage);
			if(ispresent)
			{
				popupmessage = true;
			}
		}
		catch (Exception e)
		{
			System.out.println("The Pop up message is not captured");
		}
		return popupmessage;
	}

	public PerformerType clickonCancel_Submit() {
		try{
			Thread.sleep(2000);
			scrolltoElement(driver, Cancel_Submit);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(Cancel_Submit);
	    	actions1.doubleClick().build().perform();
			
		}
		catch(Exception e)
		{
			System.out.println("The Cancel button on Submit ");
		}
		return new PerformerType (driver);
	}
	
	public PerformerType clickonOK_Submit() {
		try{
			Thread.sleep(2000);
			scrolltoElement(driver, Ok_Submit);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(Ok_Submit);
	    	actions1.doubleClick().build().perform();
			
		}
		catch(Exception e)
		{
			System.out.println("The Cancel button on Submit ");
		}
		return new PerformerType (driver);
	}



	public void Screenshots(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, PerformerTypeTab);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");

		
	}


}
