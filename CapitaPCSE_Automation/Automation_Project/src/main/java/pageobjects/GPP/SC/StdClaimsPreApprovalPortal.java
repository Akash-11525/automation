package pageobjects.GPP.SC;

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

import helpers.CommonFunctions;
import helpers.Screenshot;
import helpers.Support;
import utilities.ExcelUtilities;

public class StdClaimsPreApprovalPortal extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(css="a[href='/StandardClaims/SCApproval']")
	WebElement claimsApproval;
	
	@FindBy(css="a#tab1")
	WebElement stdClaimsTab;
	
	@FindBy(id="SCPortalPreApprovalFilter_ContractorName")
	WebElement preAppPortal_ContractorTxtBox;
	
	@FindBy(id="SCPortalPreApprovalFilter_PreApprovalStatus")
	WebElement preAppPortal_ClaimStatus;
	
	@FindBy(id="SCPortalPreApprovalFilter_ClaimStartDate")
	WebElement preAppPortal_StartDateTxtBox;
	
	@FindBy(id="SCPortalPreApprovalFilter_ClaimEndDate")
	WebElement preAppPortal_EndDateTxtBox;
	
	@FindBy(id="SCPortalPreApprovalFilter_ClaimType")
	WebElement preAppPortal_ClaimType;
	
	@FindBy(id="btnSCApprovalSearch")
	WebElement btn_SearchPreAppClaim;
	
	@FindBy(xpath="//div[@id='divConfRevertToDraftApproval']/div/div")
	WebElement preAppPortal_RecallModalWindow;
	
	@FindBy(xpath="//div[@id='divConfRevertToDraftApproval']//button[@class='btn btn-success']")
	WebElement preAppPortal_ConfirmRevert;
	
	@FindBy(xpath="//div[@id='divConfRevertToDraftApproval']//button[@class='btn btn-default']")
	WebElement preAppPortal_CancelRevert;
	
	@FindBy(xpath="//div[@id='tblSCClaimsApprovals_wrapper']//tbody/tr[2]/td[9]/a")
	WebElement preAppPortal_ClaimButton;
	
	public StdClaimsPreApprovalPortal(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}
	
	public StdClaimsPortal clickOnStdClaimPortalTab()
	{
		System.out.println("Entered in locum pre approval click method");
		scrolltoElement(driver, stdClaimsTab);
		wait.until(ExpectedConditions.elementToBeClickable(stdClaimsTab)).click();
		return new StdClaimsPortal(driver);
	}
	
	public StdClaimsApprovalWindow clickOnClaimApproval()
	{
		System.out.println("Entered in claim approval click method");
		scrolltoElement(driver, claimsApproval);
		wait.until(ExpectedConditions.elementToBeClickable(claimsApproval)).click();
		return new StdClaimsApprovalWindow(driver);
	}
	
	public SC_LocumCostClaim clickOnClaimButton() throws InterruptedException
	{
		searchStdClaims();
		WebElement elmClaim= driver.findElement(By.xpath("//table[@id='tblSCClaimsApprovals']//tbody/tr[1]/td[9]/a"));
		System.out.println("Entered in claim button click method");
		scrolltoElement(driver, elmClaim);
		wait.until(ExpectedConditions.elementToBeClickable(elmClaim)).click();
		return new SC_LocumCostClaim(driver);
	}
	

	public String getClaimNumber(String key) throws InterruptedException {
		searchStdClaims();
		String claimNo= driver.findElement(By.xpath("//table[@id='tblSCClaimsApprovals']//tbody/tr[1]/td[1]/a")).getText().trim();
		ExcelUtilities.setKeyValueByPosition("GPPSCTESTDATA.xlsx", "ExpectedClaimDetails", claimNo, key, "CLAIMID");
		return claimNo;
	}
	
	public String getClaimNumberWithClaimStatus(String key, String claimStatus) throws InterruptedException {
		searchStdClaims(claimStatus);
		String claimNo= driver.findElement(By.xpath("//table[@id='tblSCClaimsApprovals']//tbody/tr[1]/td[1]/a")).getText().trim();
		ExcelUtilities.setKeyValueByPosition("GPPSCTESTDATA.xlsx", "ExpectedClaimDetails", claimNo, key, "CLAIMID");
		return claimNo;
	}

	public void capturePreApprovalClaimPortalSnap(String string) throws InterruptedException, IOException {
		
		scrolltoElement(driver, preAppPortal_ContractorTxtBox);
		Screenshot.TakeSnap(driver, string+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, string+"_2");
		
		
	}

	public String verifyRevertToDraftClaim(String claimNo) throws InterruptedException {
		WebElement recallElm= driver.findElement(By.xpath("//table[@id='tblSCClaimsApprovals']//tbody/tr[1]/td[8]/button/em"));
		String expClaimNo= driver.findElement(By.xpath("//table[@id='tblSCClaimsApprovals']//tbody/tr[1]/td[1]")).getText().trim();
		String expStatus="";
		
			if(claimNo.equalsIgnoreCase(expClaimNo)){
				scrolltoElement(driver, preAppPortal_ContractorTxtBox);
				recallElm.click();
				Thread.sleep(2000);
				wait.until(ExpectedConditions.elementToBeClickable(preAppPortal_CancelRevert));
				preAppPortal_CancelRevert.click();
				Thread.sleep(2000);
				scrolltoElement(driver, stdClaimsTab);
				recallElm.click();
				Thread.sleep(2000);
				wait.until(ExpectedConditions.elementToBeClickable(preAppPortal_ConfirmRevert));
				preAppPortal_ConfirmRevert.click();
				Thread.sleep(8000);
				expStatus= getClaimStatus();
				//driver.findElement(By.xpath("//table[@id='tblSCClaimsApprovals']//tbody/tr[1]/td[7]")).getText().trim();
			
				}
			return expStatus;

	}

	public String getClaimStatus() throws InterruptedException {
		searchStdClaims();
		String expStatus= driver.findElement(By.xpath("//table[@id='tblSCClaimsApprovals']//tbody/tr[1]/td[7]")).getText().trim();
		return expStatus;
		
	}
	
	public String getClaimStatusByValue(String claimStatus) throws InterruptedException {
		searchStdClaims(claimStatus);
		String expStatus= driver.findElement(By.xpath("//table[@id='tblSCClaimsApprovals']//tbody/tr[1]/td[7]")).getText().trim();
		return expStatus;
		
	}
	
	public void searchStdClaims() throws InterruptedException{
		Thread.sleep(3000);		
		String fromDate= CommonFunctions.getTodayDate();
		enterDataInTextField(preAppPortal_StartDateTxtBox, fromDate, wait);
		System.out.println("from date criteria entered for SC pre approval portal");
		
		String toDate= CommonFunctions.getTodayDate();
		enterDataInTextField(preAppPortal_EndDateTxtBox, toDate, wait);
		System.out.println("to date criteria entered for SC pre approval portal");
		
		wait.until(ExpectedConditions.elementToBeClickable(btn_SearchPreAppClaim)).click();
		System.out.println("clicked on search button for SC pre approval portal");
		Thread.sleep(4000);
		
		}
	
	public StdClaimsPreApprovalPortal searchStdClaims(String claimStatus) throws InterruptedException{
		Thread.sleep(3000);
		CommonFunctions.selectOptionFromDropDown(preAppPortal_ClaimStatus, claimStatus);
		
		wait.until(ExpectedConditions.elementToBeClickable(btn_SearchPreAppClaim)).click();
		System.out.println("clicked on search button for SC pre approval portal");
		Thread.sleep(4000);
		return new StdClaimsPreApprovalPortal(driver);
		}

	public void verifySearchResult() throws InterruptedException {
		scrolltoElement(driver, btn_SearchPreAppClaim);
		wait.until(ExpectedConditions.elementToBeClickable(btn_SearchPreAppClaim)).click();
		System.out.println("Clicked on search button without criteria");
		searchStdClaims();
		System.out.println("Searched pre approval as per From & To date");
		searchStdClaims("Rejected");
		System.out.println("Searched pre approval as per status");
		
	}
	
	public void clickOnClaimNumber(String claimNo) throws InterruptedException {
		searchStdClaims();
		WebElement elmClaim = driver.findElement(By.xpath("//table[@id='tblSCClaimsApprovals']//tbody/tr[1]/td[1]"));
		String expClaimNo = elmClaim.getText().trim();
		if (claimNo.equalsIgnoreCase(expClaimNo)) {
			elmClaim.click();
		} else {
			System.out.println("Claim number is not found");
		}

	}

}
