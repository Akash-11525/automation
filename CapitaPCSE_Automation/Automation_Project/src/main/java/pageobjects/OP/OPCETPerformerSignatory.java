package pageobjects.OP;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Screenshot;
import helpers.Support;

public class OPCETPerformerSignatory extends Support{
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy (id="referenceNumber")
	WebElement referneceNumberTxt;
	
	@FindBy (id="btnSaveNext")
	WebElement btnSaveNext;
	
	@FindBy (id="signature-pad")
	WebElement signatureCanvasbox;
	
	@FindBy (linkText = "01")
	WebElement performerSignatoryHeader;
	
	public OPCETPerformerSignatory(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}
	
	public OPCETContractorSignatory clickOnSaveAndNextButton() throws InterruptedException
	{
		scrolltoElement(driver, signatureCanvasbox);
		enterSignatory(signatureCanvasbox, driver);
		
		wait.until(ExpectedConditions.elementToBeClickable(btnSaveNext)).click();
		return new OPCETContractorSignatory(driver);
	}
	
	public void CETPerformerSignatorySnaps(String note) throws InterruptedException, IOException
	{
		scrolltoElement(driver, performerSignatoryHeader);
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);		
	}
	
	

}
