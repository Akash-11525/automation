package pageobjects.GPP.SC;

import java.util.List;
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

public class StdClaimsPreApprovalWindow extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="SCPreApprovalFilter_ContractorName")
	WebElement scPreApp_ContractorTxtBox;
	
	@FindBy(id="SCPreApprovalFilter_ClaimStatus")
	WebElement scPreApp_ClaimStatus;
	
	@FindBy(id="SCPreApprovalFilter_ClaimStartDate")
	WebElement scPreApp_StartDateTxtBox;
	
	@FindBy(id="SCPreApprovalFilter_ClaimEndDate")
	WebElement scPreApp_EndDateTxtBox;
	
	@FindBy(id="btnSearchPreApproval")
	WebElement btn_SearchScPreAppClaim;
	
	@FindBy(xpath="//div[@id='divConfPreApprovalApprove']//div[@class='modal-content']")
	WebElement scPreApp_ModalWindow;
	
	@FindBy(xpath="//div[@id='divConfPreApprovalApprove']//div[@class='modal-content']//button[@class='btn btn-success']")
	WebElement scPreApp_ConfApprove;
	
	@FindBy(xpath="//div[@id='divConfPreApprovalApprove']//div[@class='modal-content']//button[@class='btn btn-default']")
	WebElement scPreApp_CancelApprove;
	
	@FindBy(xpath="//div[@id='divConfPreApprovalReject']//div[@class='modal-content']")
	WebElement scPreApp_RejModalWindow;
	
	@FindBy(xpath="//div[@id='divConfPreApprovalReject']//div[@class='modal-content']//button[@class='btn btn-success']")
	WebElement scPreApp_ConfReject;
	
	@FindBy(xpath="//div[@id='divConfPreApprovalReject']//div[@class='modal-content']//button[@class='btn btn-default']")
	WebElement scPreApp_CancelReject;
	
	public StdClaimsPreApprovalWindow(WebDriver driver)
	{
		
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}
	
	public void searchPreAppClaims() throws InterruptedException{
			
		String fromDate= CommonFunctions.getTodayDate();
		enterDataInTextField(scPreApp_StartDateTxtBox, fromDate, wait);
		System.out.println("from date criteria entered for SC pre approval window portal");
		
		String toDate= CommonFunctions.getTodayDate();
		enterDataInTextField(scPreApp_EndDateTxtBox, toDate, wait);
		System.out.println("to date criteria entered for SC pre approval window portal");
		
		wait.until(ExpectedConditions.elementToBeClickable(btn_SearchScPreAppClaim)).click();
		System.out.println("clicked on search button for SC pre approval window portal");
		Thread.sleep(4000);
	}

	public void approveLocumPreAppClaim(String claimNo, String sheet, int colNumber) throws InterruptedException {
		searchPreAppClaims();
		WebElement elmRemark= driver.findElement(By.xpath("//table[@id='tblSCPreApproval']//tbody/tr[1]/td[8]/textarea"));
		WebElement elmApprove= driver.findElement(By.xpath("//table[@id='tblSCPreApproval']//tbody/tr[1]/td[9]/button[1]"));
		
		String remark= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", sheet, "AuthJustification", colNumber);
		String expClaimNo= driver.findElement(By.xpath("//table[@id='tblSCPreApproval']//tbody/tr[1]/td[1]")).getText().toString();
		
			if(claimNo.equalsIgnoreCase(expClaimNo)){
			
				scrolltoElement(driver, elmRemark);
				enterDataInTextField(elmRemark, remark, wait);
				elmRemark.sendKeys(Keys.TAB);
				System.out.println("Remark entered");
				
				wait.until(ExpectedConditions.elementToBeClickable(elmApprove)).click();
				//scrolltoElement(driver, scPreApp_ContractorTxtBox);
				Thread.sleep(2000);
				wait.until(ExpectedConditions.elementToBeClickable(scPreApp_CancelApprove));
				scPreApp_CancelApprove.click();
				System.out.println("*********Clicked on Cancel button*************");
				Thread.sleep(2000);
				scrolltoElement(driver, scPreApp_ContractorTxtBox);
				elmApprove.click();
				Thread.sleep(2000);
				wait.until(ExpectedConditions.elementToBeClickable(scPreApp_ConfApprove));
				scPreApp_ConfApprove.click();
				System.out.println("***********Clicked on Approve button**********");
				Thread.sleep(8000);
			}
		
	}
	
	public void rejectLocumPreAppClaim(String claimNo, String sheet, int colNumber) throws InterruptedException {
		searchPreAppClaims();
		WebElement elmRemark= driver.findElement(By.xpath("//table[@id='tblSCPreApproval']//tbody/tr[1]/td[8]/textarea"));
		WebElement elmReject= driver.findElement(By.xpath("//table[@id='tblSCPreApproval']//tbody/tr[1]/td[9]/button[2]"));
		
		String remark= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", sheet, "AuthJustification", colNumber);
		String expClaimNo= driver.findElement(By.xpath("//table[@id='tblSCPreApproval']//tbody/tr[1]/td[1]")).getText().toString();
		
			if(claimNo.equalsIgnoreCase(expClaimNo)){
			
				scrolltoElement(driver, elmRemark);
				enterDataInTextField(elmRemark, remark, wait);
				elmRemark.sendKeys(Keys.TAB);
				System.out.println("Remark entered");
				
				wait.until(ExpectedConditions.elementToBeClickable(elmReject)).click();
				//scrolltoElement(driver, scPreApp_ContractorTxtBox);
				Thread.sleep(2000);
				wait.until(ExpectedConditions.elementToBeClickable(scPreApp_CancelReject));
				scPreApp_CancelReject.click();
				System.out.println("*********Clicked on Cancel button*************");
				Thread.sleep(2000);
				scrolltoElement(driver, scPreApp_ContractorTxtBox);
				elmReject.click();
				Thread.sleep(2000);
				wait.until(ExpectedConditions.elementToBeClickable(scPreApp_ConfReject));
				scPreApp_ConfReject.click();
				System.out.println("***********Clicked on Approve button**********");
				Thread.sleep(8000);
			}
		
	}

}
