package pageobjects.OP;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Support;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class OPOpticalVoucherRetrieval extends Support {

	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(css="input[id='txtVoucherCode']")
	WebElement voucherCodeTxtInput;
	
	@FindBy(css="input[id='txtAuthorisationCode']")
	WebElement authCodeTxtInput;
	
	@FindBy(css="button[id='btnVoucherSearch'")
	WebElement voucherSearchButton;
	
	@FindBy(xpath="//table[@id='VoucherTable']//tbody//td[5]/input")
	WebElement createGOS3ClaimButton;
	
	@FindBy(css="div[id='VoucherTable_processing']")
	WebElement voucherTableProcessing;

	public OPOpticalVoucherRetrieval(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}
	
	public void searchVoucherDetails(String vCode, String aCode)
	{
		Support.enterDataInTextField(voucherCodeTxtInput, vCode, wait);
		Support.enterDataInTextField(authCodeTxtInput, aCode, wait);
		
		voucherSearchButton.click();
		boolean shouldContinue = true;
		//String value = voucherTableProcessing.getAttribute("style");
		while (shouldContinue == true)
		{
			String value = voucherTableProcessing.getAttribute("style");
			if (value.equalsIgnoreCase("display: none;"))
			{
				shouldContinue = false;
			}
			
		}
	}
	
	public OPGOS3Prescription clickOnSearchRecord() throws InterruptedException
	{
		wait.until(ExpectedConditions.elementToBeClickable(createGOS3ClaimButton));
		createGOS3ClaimButton.click();
		Thread.sleep(4000);
		return new OPGOS3Prescription(driver);
	}

}
