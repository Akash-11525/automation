package pageobjects.OP;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.CommonFunctions;
import helpers.Support;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import testdata.ConfigurationData;

public class OPSearchClaim extends Support {

	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="SelectPerformer")
	WebElement selectPerformer;
	
	@FindBy(css="button[class='btn btn-success']")
	WebElement submitButton;
	
	@FindBy(css="input[class='form-control form-control input-sm']")
	WebElement searchTxtBox;
	
	@FindBy(xpath="//table[@id='SearchTable']/tbody/tr[1]/td[5]")
	WebElement firstClaimStaus;
	
	@FindBy(xpath="//table[@id='SearchTable']/tbody/tr[1]/td[1]")
	WebElement firstClaimNo;
	
	@FindBy(xpath="//button[contains(@onclick, 'GOSOne')]")
	WebElement GOSONEButton;
	
	@FindBy(xpath="//button[contains(@onclick, 'GOSThree')]")
	WebElement GOSTHREEButton;
	
	@FindBy(xpath="//button[contains(@onclick, 'GOSFive')]")
	WebElement GOSFIVEButton;
	
	@FindBy(css = "button[class*='button-image1']")
	WebElement makeAClaimButton;
	
	@FindBy(css = "button[class*='button-image2']")
	WebElement searchClaimButton;
	
	@FindBy(css = "button[class*='button-image3']")
	WebElement statementsButton;
	
	@FindBy(css = "button[class*='button-image4']")
	WebElement hC5Button;
	
	@FindBy(linkText = "Ophthalmic")
	WebElement opthalmicLink;
	
	@FindBy(css = "button[id*='btnSearch']")
	WebElement searchButton;
	
	@FindBy(css="input[id*='txtClaimNumber']")
	WebElement claimNumberTxt;
	
	@FindBy(css="div[id*='SearchTable_processing']")
	WebElement searchTable;
	
	@FindBy(css="button[data-ajax-url*='OphthalmicSearch']")
	WebElement ClaimSearchButton;
	
	@FindBy(css="button[data-ajax-url*='VoucherOnlySearch']")
	WebElement VoucherSearchButton;
	
	@FindBy(xpath="//table[@id='SearchTable']/tbody/tr[1]/td[8]/input[@id='openSearch']")
	WebElement OpenClaimButton;
	
	@FindBy(css="select[id='drpclaimType']")
	WebElement claimTypeDropdown;

	public OPSearchClaim(WebDriver driver){

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
		wait.until(ExpectedConditions.visibilityOf(claimNumberTxt));
		wait.until(ExpectedConditions.elementToBeClickable(claimNumberTxt)).sendKeys(clm);
		wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
		//searchTxtBox.sendKeys(clm);
		wait.until(ExpectedConditions.attributeToBe(searchTable, "style", "display: none;"));
		
		wait.until(ExpectedConditions.elementToBeClickable(firstClaimStaus));
		status = firstClaimStaus.getText();
		return status;
	}

	public String getFirstClaimNo() throws InterruptedException
	{
		String ClmNo = null;
		wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
		Thread.sleep(4000);
		wait.until(ExpectedConditions.attributeToBe(searchTable, "style", "display: none;"));
		wait.until(ExpectedConditions.elementToBeClickable(firstClaimNo));
		scrolltoElement(driver, firstClaimNo);
		
		ClmNo = firstClaimNo.getText();
		System.out.println(ClmNo);
		return ClmNo;
	}
	
	public OPPatientDetails clickGOSONEButton() throws InterruptedException
	{
		wait.until(ExpectedConditions.elementToBeClickable(GOSONEButton)).click();
		return new OPPatientDetails(driver);
		
	}
	
	public OPMakeAClaim clickOnMakeAClaimButton()
	{
		wait.until(ExpectedConditions.elementToBeClickable(makeAClaimButton)).click();
		return new OPMakeAClaim(driver);
	}

	public OPMakeAClaim clickOnSearchClaimButton()
	{
		wait.until(ExpectedConditions.elementToBeClickable(makeAClaimButton)).click();
		return new OPMakeAClaim(driver);
	}
	
	public OPHomePage clickonOpthalmicLink()
	{
		scrolltoElement(driver, opthalmicLink);
		wait.until(ExpectedConditions.elementToBeClickable(opthalmicLink)).click();
		return new OPHomePage(driver);
	}
	
	public OPMakeAClaim clickOnSearchVoucherButton()
	{
		wait.until(ExpectedConditions.elementToBeClickable(VoucherSearchButton)).click();
		return new OPMakeAClaim(driver);
	}

	
	private <T> T selectAndOpenClaim(String clm,  Class<T> expectedPage) throws InterruptedException
	{
		wait.until(ExpectedConditions.elementToBeClickable(claimNumberTxt)).sendKeys(clm);
		wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
		//searchTxtBox.sendKeys(clm);
		wait.until(ExpectedConditions.attributeToBe(searchTable, "style", "display: none;"));
		wait.until(ExpectedConditions.elementToBeClickable(OpenClaimButton)).click();
		//return new OPPatientDetails(driver);
		Thread.sleep(3000);
		return PageFactory.initElements(driver, expectedPage);
		
	}
	
	public OPCETPerformerSignatory searchAndOpenClaimByType(String clmType) throws InterruptedException
	{
		wait.until(ExpectedConditions.elementToBeClickable(claimTypeDropdown));
		CommonFunctions.selectOptionFromDropDown(claimTypeDropdown, clmType);
		wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
		//searchTxtBox.sendKeys(clm);
		wait.until(ExpectedConditions.attributeToBe(searchTable, "style", "display: none;"));
		wait.until(ExpectedConditions.elementToBeClickable(OpenClaimButton)).click();
		//return new OPPatientDetails(driver);
		Thread.sleep(3000);
		//return PageFactory.initElements(driver, expectedPage);
		return new OPCETPerformerSignatory(driver);
		
	}
	
	public OPPatientDetails selectAndOpenClaim_GOS1(String clm) throws InterruptedException
	{
		return selectAndOpenClaim(clm, OPPatientDetails.class);
		
	}
	
	public OPGOS3Prescription selectAndOpenClaim_GOS3(String clm) throws InterruptedException
	{
		return selectAndOpenClaim(clm, OPGOS3Prescription.class);	
		
	}
	
	public OPGOS4PatientDetails selectAndOpenClaim_GOS4(String clm) throws InterruptedException
	{
		return selectAndOpenClaim(clm, OPGOS4PatientDetails.class);	
		
	}
	
}
