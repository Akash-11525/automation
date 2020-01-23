package pageobjects.GPP.Pensions.Solo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.CommonFunctions;
import helpers.GPPHelpers;
import helpers.Support;
import helpers.WindowHandleSupport;
import utilities.ExcelUtilities;

public class SoloIncomeApproval extends Support{
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(xpath="//*[@id='frmApproveRejectSolo']/div[1]/div[3]/div[1]/strong")
	WebElement refNo;
	
	@FindBy(id="SOLODetails_AdditionalContributionsForAdditionalPension")
	WebElement AVCAmount;
	
	@FindBy(id="SOLODetails_AdditionalContributionsForEarlyRetirementReduction")
	WebElement ERRBOAmount;
	
	@FindBy(id="SOLODetails_AdditionalContributionsMPAVCs")
	WebElement MPAVCAmount;

	@FindBy(css="select#ddlFundProvider")
	WebElement fundProvider;
	
	@FindBy(xpath="//button[@class='btn btn-success btn-xs table-button']")
	WebElement approveButton;
	
	@FindBy(xpath="//div[@id='divConfApprove']//button[@class='btn btn-default']")
	WebElement cancelApprove;
	
	@FindBy(xpath="//div[@id='divConfApprove']//button[@id='submitbtn']")
	WebElement confirmApprove;
	
	@FindBy(css="button[class*='danger']")
	WebElement rejectButton;
	
	@FindBy(css="input#ReasonForRejection")
	WebElement rejectionReason;
	
	public SoloIncomeApproval(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}

	public boolean verifyPresenceOfDocument(String refNumber) throws InterruptedException {
		Thread.sleep(5000);
		WindowHandleSupport.getRequiredWindowDriverWithIndex(driver, 1);
		boolean isApplicationPresent= false;
		String docNo= refNo.getText().toString();
		if(refNumber.equalsIgnoreCase(docNo)){
			isApplicationPresent= true;
		}
		return isApplicationPresent;
	}
	
	public boolean verifyPensionableAmount(String colName, String file, String sheet) throws InterruptedException{
		boolean matched=false;
		String strPayExcludingNHSPS= ExcelUtilities.getKeyValueByPosition(file, sheet, "PayExcludingNHSPS", colName);
		double payExcludingNHSPS= Double.parseDouble(strPayExcludingNHSPS);
		Thread.sleep(500);
		String strProfessionalExpenses= ExcelUtilities.getKeyValueByPosition(file, sheet, "ProfessionalExpenses", colName);
		double professionalExpenses= Double.parseDouble(strProfessionalExpenses);
		Thread.sleep(500);
		String strPayExcludingNHSPS_Portal= Support.getValueByJavaScript(driver, "SOLODetails_ExcludingNHSPSContributions");
		double payExcludingNHSPS_Portal= Double.parseDouble(strPayExcludingNHSPS_Portal);
		Thread.sleep(500);
		String strProfessionalExpenses_Portal= Support.getValueByJavaScript(driver, "SOLODetails_ProfessionalNHSExpenses");
		double professionalExpenses_Portal= Double.parseDouble(strProfessionalExpenses_Portal);
		Thread.sleep(500);
		double actualAmt= payExcludingNHSPS-professionalExpenses;
		actualAmt= GPPHelpers.convertValueToDecimals(actualAmt,2);
		double expAmt= payExcludingNHSPS_Portal-professionalExpenses_Portal;
		expAmt= GPPHelpers.convertValueToDecimals(expAmt,2);
		
		if(actualAmt==expAmt){
			matched=true;
		}
		return matched;
	}
	
	public boolean verifyEmployeeContribution(String scriptKey, String testDataFileName, String testDataSheet,String amtTierSheet) throws InterruptedException {
		boolean contributionMatched=false;
		List<Double>expActualValues= getCalculatedValues(scriptKey,testDataFileName,testDataSheet,amtTierSheet);
		double contributionAmt= expActualValues.get(0);
		double actualContriAmt= expActualValues.get(1);
		if(contributionAmt==actualContriAmt){
			contributionMatched=true;
		}
		return contributionMatched;
	}
	
	private List<Double> getCalculatedValues(String scriptKey, String testDataFileName, String testDataSheet,
			String amtTierSheet) throws InterruptedException {
		List<Double> values= new ArrayList<Double>();
		double criteria = 0.00,startRange=0.00,endRange,contributionAmt=0.00;
		String strPensionableAmt= Support.getValueByJavaScript(driver, "SOLODetails_NHSPensionablePay");
		double pensionableAmt= Double.parseDouble(strPensionableAmt);
		Thread.sleep(500);
		pensionableAmt= GPPHelpers.convertValueToDecimals(pensionableAmt,2);
		
		String strAnnualAmt= ExcelUtilities.getKeyValueByPosition(testDataFileName, testDataSheet, "AnnualIncome", scriptKey);
		double annualAmt= Double.parseDouble(strAnnualAmt);
		Thread.sleep(500);
		annualAmt= GPPHelpers.convertValueToDecimals(annualAmt,2);
		
		double finalAmount= pensionableAmt+annualAmt;
		
		List<String>ranges= ExcelUtilities.getCellValuesByPosition(testDataFileName, amtTierSheet, "Range");
		int rangeSize= ranges.size();
		List<String>critria= ExcelUtilities.getCellValuesByPosition(testDataFileName, amtTierSheet, "Criteria");
		
		for(int i=0;i<rangeSize;i++){
			String range= ranges.get(i);
			String[]rangeArray= range.split("-");
			String strStartRange= rangeArray[0].toString();
			startRange= Double.parseDouble(strStartRange);
			startRange= GPPHelpers.convertValueToDecimals(startRange,2);
			String strEndRange= rangeArray[1].toString();
			if(!(strEndRange.equalsIgnoreCase("over"))){
				endRange= Double.parseDouble(strEndRange);
				Thread.sleep(500);
				endRange= GPPHelpers.convertValueToDecimals(endRange,2);
				if((finalAmount>=startRange)&&(finalAmount<=endRange)){
					String strCriteria= critria.get(i);
					criteria= Double.parseDouble(strCriteria);
					Thread.sleep(500);
					criteria= GPPHelpers.convertValueToDecimals(criteria,2);
					break;
				}
			}else{
				Thread.sleep(500);
				if((finalAmount>=startRange)){
					String strCriteria= critria.get(i);
					criteria= Double.parseDouble(strCriteria);
					Thread.sleep(500);
					criteria= GPPHelpers.convertValueToDecimals(criteria,2);
					break;
				}
			}
		}
		contributionAmt= (finalAmount*criteria)/100;
		contributionAmt= GPPHelpers.convertValueToDecimals(contributionAmt,2);
		System.out.println("Expected contribution amount is: "+contributionAmt);
		String strActualContriAmt= Support.getValueByJavaScript(driver, "SOLODetails_NHSPSEmployeeContributionsInAmount");
		double actualContriAmt= Double.parseDouble(strActualContriAmt);
		actualContriAmt= GPPHelpers.convertValueToDecimals(actualContriAmt,2);
		System.out.println("Actual contribution amount is: "+actualContriAmt);
		values.add(contributionAmt);
		values.add(actualContriAmt);
		return values;
	}
	
	public boolean verifyTotalContribution(String scriptKey, String testDataFileName, String testDataSheet,String amtTierSheet) throws InterruptedException {
		boolean isTotalNHSContribution= false;
		List<Double>expActualValues= getCalculatedValues(scriptKey,testDataFileName,testDataSheet,amtTierSheet);
		double actualContriAmt= expActualValues.get(1);
		scrolltoElement(driver, AVCAmount);
		String strAVCAmt= Support.getValueByJavaScript(driver, "SOLODetails_AdditionalContributionsForAdditionalPension");
		double AVCAmt= Double.parseDouble(strAVCAmt);
		AVCAmt= GPPHelpers.convertValueToDecimals(AVCAmt,2);
		
		scrolltoElement(driver, ERRBOAmount);
		String strERRBOAmt= Support.getValueByJavaScript(driver, "SOLODetails_AdditionalContributionsForEarlyRetirementReduction");
		double ERRBOAmt= Double.parseDouble(strERRBOAmt);
		ERRBOAmt= GPPHelpers.convertValueToDecimals(ERRBOAmt,2);
		
		scrolltoElement(driver, MPAVCAmount);
		String strMPAVCAmt= Support.getValueByJavaScript(driver, "SOLODetails_AdditionalContributionsMPAVCs");
		double MPAVCAmt= Double.parseDouble(strMPAVCAmt);
		MPAVCAmt= GPPHelpers.convertValueToDecimals(MPAVCAmt,2);
		
		double totalContriAmount= actualContriAmt+AVCAmt+ERRBOAmt+MPAVCAmt;
		
		String strActTotalContriAmt= Support.getValueByJavaScript(driver, "SOLODetails_TotalNHSPSContributions");
		double actTotalContriAmt= Double.parseDouble(strActTotalContriAmt);
		if(totalContriAmount==actTotalContriAmt){
			isTotalNHSContribution= true;
		}
		return isTotalNHSContribution;
	}
	
	public SoloIncomeApproval clickOnApprove(String colName,String file,String sheet) throws InterruptedException {
		wait.until(ExpectedConditions.elementToBeClickable(MPAVCAmount)).clear();
		MPAVCAmount.sendKeys("0");
		
		String strFundProvider= ExcelUtilities.getKeyValueByPosition(file, sheet, "FundProvider", colName);
		CommonFunctions.selectOptionFromDropDown(fundProvider, strFundProvider);
		
		scrolltoElement(driver, approveButton);
		wait.until(ExpectedConditions.elementToBeClickable(approveButton)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on approve button on SOLO income approval form");
		return new SoloIncomeApproval(driver);
	}
	
	public SoloIncomeApproval clickOnCancelApprove() throws InterruptedException {
		WebElement modalWindow= driver.findElement(By.xpath("//div[@id='divConfApprove']//div[@class='modal-content']"));
		if(modalWindow.isDisplayed()){
			scrolltoElement(driver, cancelApprove);
			wait.until(ExpectedConditions.elementToBeClickable(cancelApprove)).click();
			Thread.sleep(2000);
			System.out.println("Clicked on cancel approve on SOLO income approval form");
		}
		return new SoloIncomeApproval(driver);
	}
	
	public SoloIncomeApproval clickOnConfirmApprove() throws InterruptedException {
		WebElement modalWindow= driver.findElement(By.xpath("//div[@id='divConfApprove']//div[@class='modal-content']"));
		if(modalWindow.isDisplayed()){
			scrolltoElement(driver, confirmApprove);
			wait.until(ExpectedConditions.elementToBeClickable(confirmApprove)).click();
			Thread.sleep(7000);
			System.out.println("Clicked on confirm approve on SOLO income form");
		}
		return new SoloIncomeApproval(driver);
	}
	
	public SoloIncomeApproval clickOnReject(String colName,String file,String sheet) throws InterruptedException {
		wait.until(ExpectedConditions.elementToBeClickable(MPAVCAmount)).clear();
		MPAVCAmount.sendKeys("0");
		
		String strFundProvider= ExcelUtilities.getKeyValueByPosition(file, sheet, "FundProvider", colName);
		CommonFunctions.selectOptionFromDropDown(fundProvider, strFundProvider);
		
		
		scrolltoElement(driver, rejectButton);
		wait.until(ExpectedConditions.elementToBeClickable(rejectButton)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on reject button on SOLO income approval form");
		return new SoloIncomeApproval(driver);
	}
	
	public SoloIncomeApproval clickOnConfirmReject() throws InterruptedException {
		WebElement modalWindow= driver.findElement(By.xpath("//div[@id='divReasonForRejection']//div[@class='modal-content']"));
		if(modalWindow.isDisplayed()){
			scrolltoElement(driver, rejectionReason);
			wait.until(ExpectedConditions.elementToBeClickable(rejectionReason)).clear();
			rejectionReason.sendKeys("Rejected");
			Thread.sleep(2000);
			System.out.println("Clicked on confirm reject button on SOLO income approval form");
		}
		return new SoloIncomeApproval(driver);
	}

	public boolean verifyButtonState() {
		boolean isButtonDisabled= false;
		int verifiedCount=0;
		List<WebElement> elements= Arrays.asList(approveButton,rejectButton);
		wait.until(ExpectedConditions.visibilityOfAllElements(elements));
		int count= elements.size();
		for(int i=0;i<count;i++){
			WebElement element= elements.get(i);
			if(!element.isEnabled()){
				verifiedCount++;
			}
		}
		if(count==verifiedCount){
			isButtonDisabled= true;
		}
		return isButtonDisabled;
	}
}
