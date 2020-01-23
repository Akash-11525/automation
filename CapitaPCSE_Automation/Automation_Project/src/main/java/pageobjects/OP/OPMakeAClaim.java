package pageobjects.OP;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Support;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import testdata.ConfigurationData;

public class OPMakeAClaim extends Support{

	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="SelectPerformer")
	WebElement selectPerformer;
	
	@FindBy(css="button[class='btn btn-success']")
	WebElement submitButton;
	
	@FindBy(css="input[class='form-control form-control input-sm']")
	WebElement searchTxtBox;
	
	@FindBy(xpath="//table[@id='myTable']/tbody/tr[1]/td[4]")
	WebElement firstClaimStaus;
	
	@FindBy(xpath="//table[@id='myTable']/tbody/tr[1]/td[1]")
	WebElement firstClaimNo;
	
	@FindBy(xpath="//button[contains(@onclick,'GOSOne')]")
	WebElement GOSONEButton;
	
	//@FindBy(xpath="//button[contains(@onclick, 'GOSThree')]")
	@FindBy(css="button#btnCreateGos3")
	WebElement GOSTHREEButton;

	@FindBy(xpath="//button[contains(@onclick,'GOSFour')]")
	WebElement GOSFOURButton;
	
	@FindBy(xpath="//button[contains(@onclick,'GosSixOptions')]")
	WebElement GOSSIXButton;
	
	@FindBy(xpath="//button[contains(@onclick,'GOSFive')]")
	WebElement GOSFIVEButton;
	
	@FindBy(xpath="//button[contains(@onclick,'PRT')]")
	WebElement GOSPRTButton;
	
	@FindBy(xpath="//button[contains(@onclick,'Cet')]")
	WebElement GOSCETButton;
	
	@FindBy(css = "button[class*='button-image1']")
	WebElement makeAClaimButton;
	
	@FindBy(css = "button[class*='button-image2']")
	WebElement searchClaimButton;
	
	@FindBy(css = "button[class*='button-image3']")
	WebElement statementsButton;
	
	@FindBy(css = "button[class*='button-image4']")
	WebElement hC5Button;
	
	//@FindBy(css= "button[id*='btnCreateGos']")
	@FindBy(xpath = "//div[@id='modalGosThree']//Button[@id='btnCreateGos']")
	WebElement createGOS3VoucherButton;
	
	//@FindBy(id="btnCreateGosSix")
	@FindBy(xpath="//button[contains(@data-ajax-url, '/OphthalmicGosSixPvn/GosSixPvn')]")
	WebElement createGosSixButton;
	
	@FindBy(id="btnSearchGos")
	WebElement searchExistingGOSSix;

	public OPMakeAClaim(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}
	
	public OPPatientDetails selectPerformer(String environment) throws InterruptedException
	{
		String performer = ConfigurationData.getRefDataDetails(environment, "OpthoPerformer");
		helpers.CommonFunctions.selectOptionFromDropDown(selectPerformer, performer);
		wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
		return new OPPatientDetails(driver);
		
	}
	
	public OPPatientDetails clickSubmitWithoutSelectingPerformer() throws InterruptedException
	{
		wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
		return new OPPatientDetails(driver);
		
	}
	
	public String getStatusfromClaim(String clm)
	{
		String status = null;
		wait.until(ExpectedConditions.elementToBeClickable(searchTxtBox)).click();
		searchTxtBox.sendKeys(clm);
		
		wait.until(ExpectedConditions.elementToBeClickable(firstClaimStaus));
		status = firstClaimStaus.getText();
		
		
		
		return status;
		
	}
	
	public String getFirstClaimNo()
	{
		String ClmNo = null;
		wait.until(ExpectedConditions.elementToBeClickable(firstClaimNo));
		ClmNo = firstClaimNo.getText();
		System.out.println(ClmNo);
		return ClmNo;
	}
	
	public OPPatientDetails clickGOSONEButton() throws InterruptedException
	{
		wait.until(ExpectedConditions.elementToBeClickable(GOSONEButton)).click();
		System.out.println("Click on GOSONE Button is successful.");
		return new OPPatientDetails(driver);
		
	}
	
	public OPGOS3PatientDetails clickGOSThreeButton() throws InterruptedException
	{
		scrolltoElement(driver, GOSTHREEButton);
		wait.until(ExpectedConditions.elementToBeClickable(GOSTHREEButton)).click();
		wait.until(ExpectedConditions.elementToBeClickable(createGOS3VoucherButton)).click();
		Thread.sleep(3000);
		return new OPGOS3PatientDetails(driver);
	}
	
	public OPGOS5PatientDetails clickGOSFIVEButton() throws InterruptedException
	{
		wait.until(ExpectedConditions.elementToBeClickable(GOSFIVEButton)).click();
		return new OPGOS5PatientDetails(driver);
		
	}

	public OPGOS4PatientDetails clickGOSFOURButton() throws InterruptedException
	{
		wait.until(ExpectedConditions.elementToBeClickable(GOSFOURButton)).click();
		return new OPGOS4PatientDetails(driver);
		
	}
	
	public OPGOS6Options clickGOSSIXButton() throws InterruptedException
	{
		wait.until(ExpectedConditions.elementToBeClickable(GOSSIXButton)).click();
		//wait.until(ExpectedConditions.elementToBeClickable(createGosSixButton)).click();
		helpers.CommonFunctions.PageLoadExternalwait(driver);
		return new OPGOS6Options(driver);
		
	}

	public OPTraineeDetails clickGOSPRTButton() throws InterruptedException
	{
		wait.until(ExpectedConditions.elementToBeClickable(GOSPRTButton)).click();
		return new OPTraineeDetails(driver);
		
	}
	
	public OPCETPerformerSignatory clickGOSCETButton() throws InterruptedException
	{
		wait.until(ExpectedConditions.elementToBeClickable(GOSCETButton)).click();
		return new OPCETPerformerSignatory(driver);
		
	}
	


}
