package pageobjects.CS;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class PNLSearchResultPage {
	
	WebDriver driver;
	WebDriverWait wait;

	   
    @FindBy(id="showCancelCeasePopupButton")
    WebElement CancelCease;
    
    @FindBy(xpath="//*[@id='genericmodal']/div/div")
    WebElement ConfirmationDialogueBox;
    
    @FindBy(xpath= "//*[@id='genericmodal']/div/div/div[2]")
    WebElement ConfirmationWarningMessage;
    
    @FindBy(xpath= "//button[@class='btn btn-primary pcss-submit-genericmodal']")
    WebElement SaveandSubmit;
    
    @FindBy(css="div[class*='modal fade in']")
    WebElement ceaseModalWindowActive;
    
    @FindBy(id= "showDeferPopupButton")
    WebElement DeferButton;
    
    @FindBy(id= "showCeasePopupButton")
    WebElement CeaseButton;
    
    @FindBy(xpath= "//button[@class='btn btn-default pcss-cancel-genericmodal']")
    WebElement CancelonCancelCease;
 
    
	
	public PNLSearchResultPage(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 60);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}
	public PNLSearchResultPage ClickOnCancelCease() throws InterruptedException
	{
			
		wait.until(ExpectedConditions.elementToBeClickable(CancelCease)).click();	
		Thread.sleep(3000);
		
		return new PNLSearchResultPage(driver);
	}
	
	public Boolean  VerifyCancelCease()
	{
		boolean f1 = false;	
		String ButtonValue = wait.until(ExpectedConditions.elementToBeClickable(CancelCease)).getText();
		System.out.println(ButtonValue);
		if(ButtonValue.equalsIgnoreCase("Cancel Cease"))
		{
			f1 = true;
			
		}
		else
		{
			f1= false;
		}
			
		
		return f1;
	}
	
	public PNLSearchResultPage ConfirmationDialgoueBox()
	{
		
		if(ConfirmationDialogueBox.isDisplayed())
		{
			String WarningMessage = ConfirmationWarningMessage.getText();
			System.out.println(WarningMessage);
			String ExpectedWarningMessage1 = "Are you sure you want to Cancel the Pending Cease Request including all attached documents?";
			String ExpectedWarningMessage2 ="Click Yes to Proceed and Cancel to return to the previous Page.";
			String ActualWarningMessage1 = WarningMessage.split("\n")[0].trim();
			System.out.println(ActualWarningMessage1);
			if(ActualWarningMessage1.equalsIgnoreCase(ExpectedWarningMessage1))
			{
				String ActualWarningMessage2 = WarningMessage.split("\n")[1].trim();	
				System.out.println(ActualWarningMessage2);
				
				if(ActualWarningMessage2.equalsIgnoreCase(ExpectedWarningMessage2))
				{
					System.out.println("Warning Message is correct");
				}
			}
			
			
		}
		return new PNLSearchResultPage(driver);
	}
	public PNLSearchResultPage ClickOnSaveSubmit() {
		
		wait.until(ExpectedConditions.elementToBeClickable(SaveandSubmit)).click();
		
		return new PNLSearchResultPage(driver);
	}
	
public Boolean VerifyDeferButton() {
	
	boolean VerifyDeferButton = false;	
		
		String DeferButtonActualValue = wait.until(ExpectedConditions.elementToBeClickable(DeferButton)).getText();
		if(DeferButtonActualValue.equalsIgnoreCase("Defer"))
		{
			VerifyDeferButton = true;
		}
		else 
		{
			VerifyDeferButton = false;
		}
		return VerifyDeferButton;
	}
	
public boolean ClickOnCease() {
	boolean flag = false;
	 try
     {
     
	wait.until(ExpectedConditions.elementToBeClickable(CeaseButton)).click();
	
	Thread.sleep(2000);
    
    if (ceaseModalWindowActive.isDisplayed())
           flag = true;
    
    //wait.until(ExpectedConditions.visibilityOf(ceaseModalWindowActive));
    
                 
    
    }
    catch(Exception e)
    {
           System.out.println("Found Exception : " + e);
    }
    
    return flag;
    
	

}

public PNLPage ClickOnCancel() {
	
	wait.until(ExpectedConditions.elementToBeClickable(CancelonCancelCease)).click();
	
	return new PNLPage(driver);
}

	
}
