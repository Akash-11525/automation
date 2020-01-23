package pageobjects.OP;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.codehaus.plexus.util.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Screenshot;
import helpers.Support;
import utilities.ExcelUtilities;

public class OPGOS6ContractorDeclaration extends Support {
	
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(xpath="//div[@class='h5']/strong")
	WebElement claimNumberHeader;
	
	@FindBy(id="btnRevert")
	WebElement revertButton;
	
	@FindBy(id="btnClose")
	WebElement closeButton;
	
	@FindBy(id="btnCancelClaim")
	WebElement cancelClaimButton;
	
	@FindBy(id="btnSubmit")
	WebElement submitClaimButton;
	
	@FindBy(id="modalResult")
	WebElement modalResultPopUp;

	//@FindBy(xpath="("//div[@id='modalResult']//div[@class='modal-body']")") == btn btn-default
	@FindBy(xpath="//div[@id='modalResult']//div[@class='modal-body']")
	WebElement modalResultPopupText;

/*	@FindBy(css = "button[class='btn btn-default']")
	WebElement modalResultCloseBtn;*/
	@FindBy(xpath = "//div[@id='modalResult']//button[@class='btn btn-default']")
	WebElement modalResultCloseBtn;

	@FindBy(xpath="//div[@id='modalCancelClaim']//div[@class='modal-body']")
	WebElement modalCancelPopupBody;

	@FindBy(xpath="//div[@id='modalCancelClaim']//button[@class='btn btn-success']")
	WebElement modalCancelPopupYesBtn;

	@FindBy(xpath="//div[@id='modalCancelClaim']//button[@class='btn btn-default']")
	WebElement modalCancelPopupNoBtn;

	/*@FindBy(xpath="//ul[@class='list-group row']//li[1]")*/
	@FindBy(xpath="//h2")
	WebElement lblSightTestDate;
	
	@FindBy(xpath="//div[contains(@class,'h3')]")
	WebElement contractDeclaratonHeader;
	
	@FindBy(id="sigCanvas")
	WebElement signatureCanvasbox;
	
	@FindBy(css="button#btnRevert")
	WebElement revertToDraft;
	
	
	public OPGOS6ContractorDeclaration(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}

	public String getClaimNumber(String key) {
		wait.until(ExpectedConditions.visibilityOf(claimNumberHeader));
		scrolltoElement(driver, claimNumberHeader);
		String clmNo = claimNumberHeader.getText();
		clmNo = clmNo.substring(14);
		System.out.println(clmNo);
		/*ExcelUtilities.setKeyValueinExcel("OPGOS5TESTDATA.xlsx", "CLAIMS", key, clmNo);*/
		ExcelUtilities.setKeyValueByPosition("OPGOS6TESTDATA.xlsx", "EXPECTEDCLAIMDETAILS", clmNo, key, "CLAIMID");
		return clmNo;
	}

	public Boolean clickOnSubmitButton() throws InterruptedException {
		boolean flag = false;
		scrolltoElement(driver, submitClaimButton);
		wait.until(ExpectedConditions.elementToBeClickable(submitClaimButton)).click();
		helpers.CommonFunctions.PageLoadExternalwait(driver);
		
		while (!flag){

		if (modalResultPopUp.isDisplayed())
		{
			flag = true;
		}
		}

		return flag;
	}
	
	public void submitAction() throws InterruptedException
	{

		scrolltoElement(driver, submitClaimButton);
		wait.until(ExpectedConditions.elementToBeClickable(submitClaimButton)).click();
		Thread.sleep(3000);
	}


	public OPHomePage clickCloseOnResultPopup()
	{
		wait.until(ExpectedConditions.elementToBeClickable(modalResultCloseBtn)).click();
		return new OPHomePage(driver);
	}

	public String getMsgTxtOnPopup()
	{
		String msg = null;
		msg = wait.until(ExpectedConditions.elementToBeClickable(modalResultPopupText)).getText();

		return msg;
	}

	public OPHomePage clickOnCloseButton() throws InterruptedException
	{
		scrolltoElement(driver, closeButton);
		wait.until(ExpectedConditions.elementToBeClickable(closeButton)).click();
		Thread.sleep(2000);
		return new OPHomePage(driver);
	}
	
	public void clickOnCancelClaimButton() throws InterruptedException
	{
		scrolltoElement(driver, cancelClaimButton);
		wait.until(ExpectedConditions.elementToBeClickable(cancelClaimButton)).click();
		Thread.sleep(2000);
	}

	public String getCancelPopupMessage()
	{
		String msg = null;
		wait.until(ExpectedConditions.elementToBeClickable(modalCancelPopupBody));
		msg = modalCancelPopupBody.getText();

		return msg;

	}

	public OPGOS6ContractorDeclaration clickOnNoButton() throws InterruptedException
	{
		scrolltoElement(driver, modalCancelPopupNoBtn);
		wait.until(ExpectedConditions.elementToBeClickable(modalCancelPopupNoBtn)).click();
		System.out.println("The Click on No Button Action is successful");
		Thread.sleep(2000);
		return new OPGOS6ContractorDeclaration(driver);
	}
	
	public void enterSignatoryDetails() throws InterruptedException
	{
		wait.until(ExpectedConditions.visibilityOf(signatureCanvasbox));
		scrolltoElement(driver, signatureCanvasbox);
		Thread.sleep(5000);
		enterSignatory(signatureCanvasbox, driver);
		System.out.println("Signatory done");
	}

	public OPHomePage clickOnYesButton() throws InterruptedException
	{
		scrolltoElement(driver, modalCancelPopupYesBtn);
		wait.until(ExpectedConditions.elementToBeClickable(modalCancelPopupYesBtn)).click();
		System.out.println("The Click on Yes Button Action is successful");
		Thread.sleep(2000);
		return new OPHomePage(driver);
	}

	public boolean checkClaimNoLength(String clmNo)
	{
		boolean fl = false;
		int len = clmNo.length();
		if (len == 8)
		{
			fl = true;
		}

		return fl;
	}

	public boolean checkalphanum(String clmNo)
	{
		boolean flg = true;
		char ch;
		char ch2;
		String clmChar = StringUtils.substring(clmNo, 0, 3);
		System.out.println("The First 3 characters from Claim: "+clmChar);

		String clmNum = StringUtils.substring(clmNo, 3,9);
		System.out.println("The last 5 characters from Claim: "+clmNum);

		for (int i = 0; i < clmChar.length(); i++)
		{
			ch = clmChar.charAt(i);

			if (!Character.isLetter(ch))
			{
				flg = false;
				break;

			}
		}  


		for (int j = 0; j < clmNum.length(); j++)
		{
			ch2 = clmNum.charAt(j);

			if (!Character.isDigit(ch2))
			{
				flg = false;
				break;

			}
		}  


		return flg;
	}

	public boolean checkClaimNumberIncremented(String clmNo, String newClmNo)
	{
		boolean flg = false;
		
		//String clmChar = StringUtils.substring(clmNo, 0, 3);
		//String newClmChar = StringUtils.substring(newClmNo, 0, 3);
		//	System.out.println("The First 3 characters from Claim: "+clmChar);

		String clmNum = StringUtils.substring(clmNo, 3,9);
		int clmNumber = Integer.parseInt(clmNum);
		
		String newClmNum = StringUtils.substring(newClmNo, 3,9);
		int newClmNumber = Integer.parseInt(newClmNum);
		//	System.out.println("The last 5 characters from Claim: "+clmNum);
		
		if (!(clmNumber==99999))
		{
			clmNumber++;
			if (clmNumber == newClmNumber)
		{
			flg = true;
		}
		}
		
		else
		{
			if(newClmNumber == 00001)
			{
				flg = true;
			}
		}

		return flg;
	}

	public void GOS6ClaimDetailssnaps(String string) throws InterruptedException, IOException {
		//wait.until(ExpectedConditions.visibilityOf(contractDeclaratonHeader));
		scrolltoElement(driver, contractDeclaratonHeader);
		Screenshot.TakeSnap(driver, string+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, string+"_2");
		Thread.sleep(2000);
	}
	
	public OPHomePage clickOnRevertToDraftButton() throws InterruptedException
	{
		scrolltoElement(driver, revertToDraft);
		wait.until(ExpectedConditions.elementToBeClickable(revertToDraft)).click();
		WebElement revertWindow= driver.findElement(By.cssSelector("div.modal-content"));
		WebElement yes= driver.findElement(By.id("yes"));
		if(revertWindow.isDisplayed()){
			scrolltoElement(driver, yes);
			wait.until(ExpectedConditions.elementToBeClickable(yes)).click();
		}
		System.out.println("The Click on Revert to Draft Button is successful");
		Support.verifyPageLoading(driver);
		Thread.sleep(3000);
		return new OPHomePage(driver);
	}	

}
