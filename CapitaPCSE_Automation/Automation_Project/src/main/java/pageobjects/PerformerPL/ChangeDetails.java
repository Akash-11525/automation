package pageobjects.PerformerPL;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import helpers.Screenshot;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Support;
public class ChangeDetails extends Support {
	
	WebDriver driver;
	WebDriverWait wait;	
	
	@FindBy(id="btnApprove")
	WebElement AppproveButton;
	
	@FindBy(id="SavePCSE_PerformerType")
	WebElement AppprovePerformerTypeButton;
	
	@FindBy(xpath="//*[@id='dvPCSE_PerformerTypeContainer']/form/div[1]/div[3]/div/div/strong")
	WebElement Status;
	
	@FindBy(xpath="//*[@id='divpcse_Performer']/div[2]/div[3]/div/div/strong")
	WebElement PerformerTypeStatus;
	
	@FindBy(xpath="//div[@class='confirmation-buttons text-center']//a[2]")
	WebElement OKConfirmation;
	
	@FindBy(id="isLegacyUpdated")
	WebElement Legacysystem;
	
	@FindBy(id="btnconfirmLegacy")
	WebElement ConfirmUpdate;
	
	@FindBy(id="IsLegacyUpdated")
	WebElement Legacysystem_Pratice;
	
	
	public ChangeDetails(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}



	public boolean verifystatus(String Expectedstatus) {
		boolean status = false;
		try{
		//	helpers.Support.PageLoadExternalwait_Performer(driver);
			scrolltoElement(driver, Status);
			String ActualStaus = wait.until(ExpectedConditions.elementToBeClickable(Status)).getText();
			if(Expectedstatus.equalsIgnoreCase(ActualStaus))
			{
				status = true;
			}
		}
		catch(Exception e)
		{
			System.out.println("The Status on change details - Performer is not captured");
		}
		return status;
	}
	
	public boolean verifystatus_PerformerType(String Expectedstatus) {
		boolean status = false;
		try{
			boolean ispresent1 = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
			while(ispresent1)
			{
				Thread.sleep(2000);
				ispresent1 = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
			}
			scrolltoElement(driver, PerformerTypeStatus);
			String ActualStaus = wait.until(ExpectedConditions.elementToBeClickable(PerformerTypeStatus)).getText();
			if(Expectedstatus.equalsIgnoreCase(ActualStaus))
			{
				status = true;
			}
		}
		catch(Exception e)
		{
			System.out.println("The Status on change details - Performer is not captured");
		}
		return status;
	}



	public ChangeDetails clickonApprove_performer() {
	try{
		Thread.sleep(2000);
		scrolltoElement(driver, AppproveButton);
		Actions actions = new Actions(driver);
		actions.moveToElement(AppproveButton);
    	actions.doubleClick().build().perform();
    	Thread.sleep(3000);    	
    	scrolltoElement(driver, AppproveButton);
		Actions actions1 = new Actions(driver);
		actions1.moveToElement(OKConfirmation);
    	actions1.doubleClick().build().perform();
    	helpers.Support.PageLoadExternalwait_Performer(driver);
    	
	}
	catch(Exception e)
	{
		System.out.println("The approve button on Change details is not captured");
	}
		return new ChangeDetails(driver);
	}
	
	public ChangeDetails clickonApprove_performerType() {
	try{
		Thread.sleep(2000);
		scrolltoElement(driver, AppprovePerformerTypeButton);
		Actions actions = new Actions(driver);
		actions.moveToElement(AppprovePerformerTypeButton);
    	actions.doubleClick().build().perform();
    	helpers.Support.PageLoadExternalwait_Performer(driver);
    	scrolltoElement(driver, AppprovePerformerTypeButton);
		Actions actions1 = new Actions(driver);
		actions1.moveToElement(OKConfirmation);
    	actions1.doubleClick().build().perform();
    	helpers.Support.PageLoadExternalwait_Performer(driver);
    	
    	
	}
	catch(Exception e)
	{
		System.out.println("The approve button on Change details is not captured");
	}
		return new ChangeDetails(driver);
	}



	public ChangeDetails ClickonLegacySystem() {
		try{
			//helpers.Support.PageLoadExternalwait_Performer(driver);
			scrolltoElement(driver, Legacysystem);			
			wait.until(ExpectedConditions.elementToBeClickable(Legacysystem)).click();
			Thread.sleep(1000);
			scrolltoElement(driver, ConfirmUpdate);
			Actions actions = new Actions(driver);
			actions.moveToElement(ConfirmUpdate);
	    	actions.doubleClick().build().perform();
	    	Thread.sleep(2000);
	    	Actions actions1 = new Actions(driver);
			actions1.moveToElement(OKConfirmation);
	    	actions1.doubleClick().build().perform();
	    	helpers.Support.PageLoadExternalwait_Performer(driver);
		}
		catch(Exception e)
		{
			System.out.println("The Legacy system checkbox is not captured");
		}
		return new ChangeDetails(driver);
	}


	public ChangeDetails ClickonLegacySystem_Pratice() {
		try{
			Thread.sleep(3000);
			scrolltoElement(driver, Legacysystem_Pratice);			
			wait.until(ExpectedConditions.elementToBeClickable(Legacysystem_Pratice)).click();
			Thread.sleep(1000);
			scrolltoElement(driver, ConfirmUpdate);
			Actions actions = new Actions(driver);
			actions.moveToElement(ConfirmUpdate);
	    	actions.doubleClick().build().perform();
	    	Thread.sleep(2000);
	    	Actions actions1 = new Actions(driver);
			actions1.moveToElement(OKConfirmation);
	    	actions1.doubleClick().build().perform();
	    	helpers.Support.PageLoadExternalwait_Performer(driver);
		}
		catch(Exception e)
		{
			System.out.println("The Legacy system checkbox is not captured");
		}
		return new ChangeDetails(driver);
	}


	public boolean legecyCheckbox() {
		boolean LegecyUpdate = false;
		try{
		
				Thread.sleep(3000);
				boolean ispresent = driver.findElements(By.xpath("//*[@id='isLegacyUpdated']")).size() != 0;
				if(ispresent)
				{
					LegecyUpdate = true;
				}
		}
			
	catch(Exception e)
		{
			System.out.println("The Legecy system update checkbox is not captured");
		}
		return LegecyUpdate;
	}
	

	public boolean legecyCheckbox_Pratice() {
		boolean LegecyUpdate = false;
	
			try{
				helpers.CommonFunctions.PageLoadExternalwait(driver);
				System.out.println("The Legacy system checkbox Displayed: " + Legacysystem_Pratice.isDisplayed());
				if (Legacysystem_Pratice.isDisplayed())
				{
					LegecyUpdate = true;
				}
				else
					System.out.println("The PendingEmailApprovalDialog dialog not found.");
			}
				
				
	catch(Exception e)
		{
			System.out.println("The Legecy system update checkbox is not captured");
		}
		return LegecyUpdate;
	}




	public void Screenshot(String note) throws InterruptedException, IOException { 
		
		scrolltoElement(driver, Status);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");

	}
	
	public void Screenshot_Perfomer(String note) throws InterruptedException, IOException { 
		
		scrolltoElement(driver, PerformerTypeStatus);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");

	}



	public void Screenshot_Legacy(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, Legacysystem);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		
	}



	public void Screenshot_Legacy_Pratice(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, Legacysystem_Pratice);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		
	}

	
	
}
