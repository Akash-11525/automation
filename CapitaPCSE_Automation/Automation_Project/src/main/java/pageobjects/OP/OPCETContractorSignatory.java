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
import utilities.ExcelUtilities;

public class OPCETContractorSignatory extends Support{
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy (id="referenceNumber")
	WebElement referneceNumberTxt;
	
	@FindBy (id="btnSubmit")
	WebElement submitButton;
	
	@FindBy (xpath="//div[@class='h5']/Strong" )
	WebElement claimNumberHeader;
	
	@FindBy(xpath="//ul[@class='list-group row']/li[1]")
	WebElement ContractorSignatoryHeader;
	
	@FindBy(id="modalResult")
	WebElement modalResultPopUp;
	
	@FindBy(xpath="//div[@id='modalResult']//div[@class='modal-body']")
	WebElement modalResultPopupText;
	
	@FindBy(xpath = "//div[@id='modalResult']//button[@class='btn btn-default']")
	WebElement modalResultCloseBtn;
	
	@FindBy(css="div[id='signature-pad']")
	WebElement signatureCanvasbox;
	
	
	public OPCETContractorSignatory(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}
	
	public String getClaimNumber(String key)
	{
		scrolltoElement(driver, claimNumberHeader);
		String clmNo = wait.until(ExpectedConditions.elementToBeClickable(claimNumberHeader)).getText();
		clmNo = clmNo.substring(14);
		System.out.println(clmNo);
		//ExcelUtilities.setKeyValueinExcel("OPTESTDATA.xlsx", "CLAIMS", key, clmNo);
		ExcelUtilities.setKeyValueByPosition("OPTESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", clmNo, key, "CLAIMID");
		return clmNo;
		
	}
	


	public void CETContractorSignatorySnaps(String note) throws InterruptedException, IOException
	{
		scrolltoElement(driver, ContractorSignatoryHeader);
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);		
	}
	
	public boolean clickOnSubmitButton() throws InterruptedException
	{
		boolean flag = false;
		scrolltoElement(driver, signatureCanvasbox);
		enterSignatory(signatureCanvasbox, driver);
		
		scrolltoElement(driver, submitButton);
		wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
		Thread.sleep(8000);
		wait.until(ExpectedConditions.elementToBeClickable(modalResultPopupText));

		if (modalResultPopUp.isDisplayed())
		{
			flag = true;
		}

		return flag;
	}
	
	public String getMsgTxtOnPopup()
	{
		String msg = null;
		msg = wait.until(ExpectedConditions.elementToBeClickable(modalResultPopupText)).getText();

		return msg;
	}
	
	public OPHomePage clickCloseOnResultPopup()
	{
		scrolltoElement(driver, modalResultCloseBtn);
		wait.until(ExpectedConditions.elementToBeClickable(modalResultCloseBtn)).click();
		return new OPHomePage(driver);
	}
}
