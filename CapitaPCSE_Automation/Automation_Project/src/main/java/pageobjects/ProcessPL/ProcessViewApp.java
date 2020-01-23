package pageobjects.ProcessPL;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Support;

public class ProcessViewApp extends Support{
	WebDriver driver;
	WebDriverWait wait;
	

	

	
	@FindBy(id="PCSEChecks")
	WebElement PCSECheck;	
	
	@FindBy(xpath="//*[@id='accordion-process']/div/div[1]/div/a")
	WebElement ProcessIndexLink;
	
	@FindBy(id="UploadedDocuments")
	WebElement UploaddocumentsLink;	
	
	@FindBy(id="NHSDecision")
	WebElement NHSDecisionTab;	
	

	
	
	
	
	public ProcessViewApp(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		//This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);

	}
	
	
	
	
	
	public PCSECheck ClickonPCSECheck() {
		try{
			Thread.sleep(2000);
			Actions actions = new Actions(driver);
	    	actions.moveToElement(PCSECheck);
	    	actions.doubleClick().build().perform();
		//	wait.until(ExpectedConditions.elementToBeClickable(PCSECheck)).click();	
		}
		catch(Exception e)
		{
			System.out.println("The PCSECheck is not clicked");
		}
		return new PCSECheck(driver);
	}
	public ProcessIndex clickonProcessIndex() {
		try{
			Thread.sleep(2000);
			Actions actions = new Actions(driver);
	    	actions.moveToElement(ProcessIndexLink);
	    	actions.doubleClick().build().perform();
			helpers.Support.PageLoadExternalwait_ProcessApp(driver);
			
		}
		catch(Exception e)
		{
		System.out.println("The Process index is not clicked");	
		}

		return new ProcessIndex(driver);
	}





	public Uploaddocument clickonUploaddocument() {
		try{
			wait.until(ExpectedConditions.elementToBeClickable(UploaddocumentsLink));
			scrolltoElement(driver, UploaddocumentsLink);
			Actions actions = new Actions(driver);
	    	actions.moveToElement(UploaddocumentsLink);
	    	actions.doubleClick().build().perform();
	    	helpers.Support.PageLoadExternalwait_ProcessApp(driver);
			
		}
		catch(Exception e)
		{
			System.out.println("ThE upload document link is not clicked");
		}
		// TODO Auto-generated method stub
		return new Uploaddocument(driver) ;
	}





	public NHSDecision clickonNHSDecision() {
		try{
			Thread.sleep(3000);
			wait.until(ExpectedConditions.elementToBeClickable(NHSDecisionTab));
			scrolltoElement(driver, NHSDecisionTab);
			Actions actions = new Actions(driver);
	    	actions.moveToElement(NHSDecisionTab);
	    	actions.doubleClick().build().perform();
	    	helpers.Support.PageLoadExternalwait_ProcessApp(driver);
			
		}
		catch(Exception e)
		{
			System.out.println("The NHS decison is not clicked");
		}
		
		return new NHSDecision(driver);
	}





	
	
	 
	


}
