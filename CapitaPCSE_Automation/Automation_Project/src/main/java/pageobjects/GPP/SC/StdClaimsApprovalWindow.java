package pageobjects.GPP.SC;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.CommonFunctions;
import helpers.Support;
import utilities.ExcelUtilities;

public class StdClaimsApprovalWindow extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(partialLinkText="Claims Portal")
	WebElement claimsPortal;
	
	@FindBy(css="StandardClaimApprovalFilter_ContractorName")
	WebElement scApp_ContractorTxtBox;
	
	@FindBy(css="select#StandardClaimApprovalFilter_ClaimStatus")
	WebElement scApp_ClaimStatus;
	
	@FindBy(css="input#StandardClaimApprovalFilter_ClaimStartDate")
	WebElement scApp_StartDateTxtBox;
	
	@FindBy(css="input#StandardClaimApprovalFilter_ClaimEndDate")
	WebElement scApp_EndDateTxtBox;
	
	@FindBy(id="btnSearchApproval")
	WebElement btn_SearchScAppClaim;
	
	@FindBy(id="tab2")
	WebElement scApp_PreAppTab;
	
	@FindBy(xpath="//div[@id='divConfApprove']//div[@class='modal-content']")
	WebElement scApp_ModalWindow;
	
	@FindBy(xpath="//div[@id='divConfApprove']//div[@class='modal-content']//button[@class='btn btn-success']")
	WebElement scApp_ConfApprove;
	
	@FindBy(xpath="//div[@id='divConfApprove']//div[@class='modal-content']//button[@class='btn btn-default']")
	WebElement scApp_CancelApprove;
	
	@FindBy(xpath="//div[@id='divConfReject']//div[@class='modal-content']")
	WebElement scApp_RejModalWindow;
	
	@FindBy(xpath="//div[@id='divConfReject']//div[@class='modal-content']//button[@class='btn btn-success']")
	WebElement scApp_ConfReject;
	
	@FindBy(xpath="//div[@id='divConfReject']//div[@class='modal-content']//button[@class='btn btn-default']")
	WebElement scApp_CancelReject;
	
	public StdClaimsApprovalWindow(WebDriver driver)
	{
		
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}
	
	public StdClaimsPortal clickOnClaimPortal()
	{
		System.out.println("Entered in claim portal click method");
		scrolltoElement(driver, claimsPortal);
		wait.until(ExpectedConditions.elementToBeClickable(claimsPortal)).click();
		return new StdClaimsPortal(driver);
	}

	public void approveStdClaim(String claimNo, String sheet, int colNumber) throws InterruptedException {
		searchStdClaims();
		WebElement elmAppAmount= driver.findElement(By.xpath("//table[@id='tblSCApproval']//tbody/tr[1]/td[8]/input"));
		WebElement elmRemark= driver.findElement(By.xpath("//table[@id='tblSCApproval']//tbody/tr[1]/td[9]/textarea"));
		WebElement elmApprove= driver.findElement(By.xpath("//table[@id='tblSCApproval']//tbody/tr[1]/td[10]/button[1]"));
		
		String approvedAmount= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", sheet, "ApprovedAmount", colNumber);
		String remark= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", sheet, "AuthJustification", colNumber);
		String expClaimNo= driver.findElement(By.xpath("//table[@id='tblSCApproval']//tbody/tr[1]/td[1]")).getText().toString();
		
			if(claimNo.equalsIgnoreCase(expClaimNo)){
				scrolltoElement(driver, elmAppAmount);
				enterDataInTextField(elmAppAmount, approvedAmount, wait);
				elmAppAmount.sendKeys(Keys.TAB);
				System.out.println("Amount entered");
				
				scrolltoElement(driver, elmRemark);
				enterDataInTextField(elmRemark, remark, wait);
				elmAppAmount.sendKeys(Keys.TAB);
				System.out.println("Remark entered");
				
				wait.until(ExpectedConditions.elementToBeClickable(elmApprove)).click();
				//scrolltoElement(driver, scApp_ContractorTxtBox);
				Thread.sleep(2000);
				String actualMsg= driver.findElement(By.xpath("//div[@id='divConfApprove']//div[@class='modal-body']")).getText().trim();
				boolean verifyMessage= CommonFunctions.verifyMessageWithExcel(actualMsg,"GPPSCTESTDATA.xlsx","ExpectedClaimTypes","ExpApproveMessage",1);
				wait.until(ExpectedConditions.elementToBeClickable(scApp_CancelApprove));
				scApp_CancelApprove.click();
				System.out.println("*********Clicked on Cancel button*************");
				Thread.sleep(2000);
				//scrolltoElement(driver,scApp_ContractorTxtBox );//scApp_PreAppTab
				wait.until(ExpectedConditions.elementToBeClickable(elmApprove)).click();
				Thread.sleep(2000);
				wait.until(ExpectedConditions.elementToBeClickable(scApp_ConfApprove));
				scApp_ConfApprove.click();
				System.out.println("***********Clicked on Approve button**********");
				Thread.sleep(4000);
			}
		
	}
	
	public void rejectStdClaim(String claimNo, String sheet, int colNumber) throws InterruptedException {
		searchStdClaims();
		WebElement elmRemark= driver.findElement(By.xpath("//table[@id='tblSCApproval']//tbody/tr[1]/td[9]/textarea"));
		WebElement elmReject= driver.findElement(By.xpath("//table[@id='tblSCApproval']//tbody/tr[1]/td[10]/button[2]"));
		
		String remark= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", sheet, "AuthJustification", colNumber);
		String expClaimNo= driver.findElement(By.xpath("//table[@id='tblSCApproval']//tbody/tr[1]/td[1]")).getText().toString();
		
			if(claimNo.equalsIgnoreCase(expClaimNo)){
		
				scrolltoElement(driver, elmRemark);
				enterDataInTextField(elmRemark, remark, wait);
				elmRemark.sendKeys(Keys.TAB);
				System.out.println("Remark entered");
				
				wait.until(ExpectedConditions.elementToBeClickable(elmReject)).click();
				Thread.sleep(2000);
				String actualMsg= driver.findElement(By.xpath("//div[@id='divConfReject']//div[@class='modal-body']")).getText().trim();
				boolean verifyMessage= CommonFunctions.verifyMessageWithExcel(actualMsg,"GPPSCTESTDATA.xlsx","ExpectedClaimTypes","ExpRejectMessage",1);
				wait.until(ExpectedConditions.elementToBeClickable(scApp_ConfReject));
				scApp_ConfReject.click();
				System.out.println("***********Clicked on Reject button**********");
				Thread.sleep(4000);
			}
		
	}
	
	public void searchStdClaims() throws InterruptedException{
		
		String fromDate= CommonFunctions.getTodayDate();
		Thread.sleep(4000);
		enterDataInTextField(scApp_StartDateTxtBox, fromDate, wait);
		System.out.println("from date criteria entered for SC claims approval portal");
		
		String toDate= CommonFunctions.getTodayDate();
		Thread.sleep(4000);
		enterDataInTextField(scApp_EndDateTxtBox, toDate, wait);
		System.out.println("to date criteria entered for SC claims approval portal");
		
		wait.until(ExpectedConditions.elementToBeClickable(btn_SearchScAppClaim)).click();
		System.out.println("clicked on search button for SC claims approval portal");
		Thread.sleep(4000);
		
		}

	public StdClaimsPreApprovalWindow clickOnPreApprovalTab() {
		System.out.println("Entered in pre approcal click method");
		scrolltoElement(driver, scApp_PreAppTab);
		wait.until(ExpectedConditions.elementToBeClickable(scApp_PreAppTab)).click();
		return new StdClaimsPreApprovalWindow(driver);
	}

}
