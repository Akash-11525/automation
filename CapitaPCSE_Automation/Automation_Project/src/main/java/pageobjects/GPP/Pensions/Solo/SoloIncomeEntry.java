package pageobjects.GPP.Pensions.Solo;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.CommonFunctions;
import helpers.GPPHelpers;
import helpers.Screenshot;
import helpers.Support;
import testdata.ConfigurationData;
import utilities.ExcelUtilities;

public class SoloIncomeEntry extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="SoloSearchApplicantFilter_GPName")
	WebElement soloSearchTxtField;
	
	@FindBy(id="btnApplicantSearch")
	WebElement applicantSearch;
	
	@FindBy(id="txtSololFromDate")
	WebElement soloStartDate;
	
	@FindBy(id="txtSoloTODate")
	WebElement soloEndDate;
	
	@FindBy(id="SOLODetails_ExcludingNHSPSContributions")
	WebElement payAmount;
	
	@FindBy(id="SOLODetails_ProfessionalNHSExpenses")
	WebElement professionalExp;
	
	@FindBy(id="SOLODetails_NHSPensionablePay")
	WebElement pensionablePay;
	
	@FindBy(id="SOLODetails_NHSPSEmployeeContributionsInAmount")
	WebElement empContributions;
	
	@FindBy(id="SOLODetails_AdditionalContributionsForAdditionalPension")
	WebElement AVCAmount;
	
	@FindBy(id="SOLODetails_AdditionalContributionsForEarlyRetirementReduction")
	WebElement ERRBOAmount;
	
	@FindBy(id="SOLODetails_AdditionalContributionsMPAVCs")
	WebElement MPAVCAmount;
	
	@FindBy(id="SOLODetails_NHSPSEmployerContributionsAndAdministrationLevy")
	WebElement empContriAndAdminLevy;
	
	@FindBy(id="SOLODetails_TotalNHSPSContributions")
	WebElement totalContribution;
	
	@FindBy(id="TickToConfirmSolo")
	WebElement tickToConfirm;
	
	@FindBy(id="btnSubmit")
	WebElement submitBtn;
	
	@FindBy(xpath="//div[@id='divSubmission']//button[@class='btn btn-default']")
	WebElement cancelSubmit;
	
	@FindBy(xpath="//div[@id='divSubmission']//button[@class='btn btn-success']")
	WebElement confirmSubmit;
	
	@FindBy(xpath="//div[@id='dvSoloForm']//td[2]/div[@class='search']")
	WebElement refNo;
	
	public SoloIncomeEntry(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}

	public SoloIncomeEntry enterSoloMonthlyDetails(String colName, String file, String sheet,String environment) throws ParseException, InterruptedException {
		String strGMCCode= ConfigurationData.getRefDataDetails(environment, "PracticeGMCCode");
		//ExcelUtilities.getKeyValueByPosition(file, sheet, "GMCCode", colName);
		String strGPSoloName= ConfigurationData.getRefDataDetails(environment, "PracticeName");
		//ExcelUtilities.getKeyValueByPosition(file, sheet, "GPSoloName", colName);
		String strMonthsToAdd= ExcelUtilities.getKeyValueByPosition(file, sheet, "MonthsToAdd", colName);
		int monthsToAdd= Integer.parseInt(strMonthsToAdd);
		
		String strPayExcludingNHSPS= ExcelUtilities.getKeyValueByPosition(file, sheet, "PayExcludingNHSPS", colName);
		String strProfessionalExpenses= ExcelUtilities.getKeyValueByPosition(file, sheet, "ProfessionalExpenses", colName);
		
		String fromDate= CommonFunctions.getFirstDayOfMonth("dd/MM/yyyy", monthsToAdd);
		String toDate= CommonFunctions.getLastDayOfMonth("dd/MM/yyyy", monthsToAdd);
		
		scrolltoElement(driver, soloSearchTxtField);
		wait.until(ExpectedConditions.elementToBeClickable(soloSearchTxtField)).clear();
		CommonFunctions.setText(soloSearchTxtField, strGMCCode);
		List<WebElement> records= driver.findElements(By.xpath("//ul[@id='ApplicantSearchResult']/li"));
		for(int i=1;i<=records.size();i++){
			WebElement record= driver.findElement(By.xpath("//ul[@id='ApplicantSearchResult']/li["+i+"]/a"));
			String name= record.getText().toString();
			String[]nameArray= name.split("-");
			String GMC_Code= nameArray[0].toString().trim();
			String GPSoloName= nameArray[1].toString().trim();
			if((strGMCCode.equalsIgnoreCase(GMC_Code))&&(strGPSoloName.equalsIgnoreCase(GPSoloName))){
				record.click();
				Thread.sleep(2000);
				break;
			}
		}
		wait.until(ExpectedConditions.elementToBeClickable(applicantSearch)).click();
		Thread.sleep(2000);
		scrolltoElement(driver, soloStartDate);
		enterDataInTextField(soloStartDate, fromDate, wait);
		Thread.sleep(1000);
		enterDataInTextField(soloEndDate, toDate, wait);
		scrolltoElement(driver, payAmount);
		enterDataInTextField(payAmount, strPayExcludingNHSPS, wait);
		Thread.sleep(1000);
		enterDataInTextField(professionalExp, strProfessionalExpenses, wait);
		professionalExp.sendKeys(Keys.TAB);
		CommonFunctions.PageLoadExternalwait_OP(driver);
		Thread.sleep(2000);
		wait.until(ExpectedConditions.elementToBeClickable(tickToConfirm)).click();
		return new SoloIncomeEntry(driver);
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
		strPayExcludingNHSPS_Portal= strPayExcludingNHSPS_Portal.replaceAll(",", "");
		double payExcludingNHSPS_Portal= Double.parseDouble(strPayExcludingNHSPS_Portal);
		Thread.sleep(500);
		String strProfessionalExpenses_Portal= Support.getValueByJavaScript(driver, "SOLODetails_ProfessionalNHSExpenses");
		strProfessionalExpenses_Portal= strProfessionalExpenses_Portal.replaceAll(",", "");
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
	
	public void captureSoloIncomeEntrySnaps(String note) throws InterruptedException, IOException {

		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,800)", "");
		Screenshot.TakeSnap(driver, note+"_2");
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
		strPensionableAmt= strPensionableAmt.replaceAll(",", "");
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
			Thread.sleep(500);
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
		contributionAmt= (pensionableAmt*criteria)/100;
		contributionAmt= GPPHelpers.convertValueToDecimals(contributionAmt,2);
		System.out.println("Expected contribution amount is: "+contributionAmt);
		String strActualContriAmt= Support.getValueByJavaScript(driver, "SOLODetails_NHSPSEmployeeContributionsInAmount");
		strActualContriAmt= strActualContriAmt.replaceAll(",", "");
		double actualContriAmt= Double.parseDouble(strActualContriAmt);
		Thread.sleep(500);
		actualContriAmt= GPPHelpers.convertValueToDecimals(actualContriAmt,2);
		System.out.println("Actual contribution amount is: "+actualContriAmt);
		values.add(contributionAmt);
		values.add(actualContriAmt);
		return values;
	}

	public boolean verifyContributionAndLevy(String colName,String file,String sheet) throws InterruptedException {
		boolean contriAndAdminLevy= false;
		String strAdminLevyCharge= ExcelUtilities.getKeyValueByPosition(file, sheet, "AdminLevyCharge", colName);
		double adminLevyCharge= Double.parseDouble(strAdminLevyCharge);
		scrolltoElement(driver, empContriAndAdminLevy);
		
		String strPayExcludingNHSPS= ExcelUtilities.getKeyValueByPosition(file, sheet, "PayExcludingNHSPS", colName);
		double payExcludingNHSPS= Double.parseDouble(strPayExcludingNHSPS);
		Thread.sleep(500);
		String strProfessionalExpenses= ExcelUtilities.getKeyValueByPosition(file, sheet, "ProfessionalExpenses", colName);
		double professionalExpenses= Double.parseDouble(strProfessionalExpenses);
		Thread.sleep(500);
		double expAmt= payExcludingNHSPS-professionalExpenses;
		expAmt= GPPHelpers.convertValueToDecimals(expAmt,2);
		double expValue= (expAmt*adminLevyCharge)/100;
		expValue= GPPHelpers.convertValueToDecimals(expValue,2);
		System.out.println("Expected admin levy amount is: "+expValue);
		
		String strActualAmt= Support.getValueByJavaScript(driver, "SOLODetails_NHSPSEmployerContributionsAndAdministrationLevy");
		strActualAmt= strActualAmt.replaceAll(",", "");
		double actualAmt= Double.parseDouble(strActualAmt);
		Thread.sleep(500);
		actualAmt= GPPHelpers.convertValueToDecimals(actualAmt,2);
		System.out.println("Actual admin levy amount is: "+actualAmt);
		
		if(expValue==actualAmt){
			contriAndAdminLevy= true;
		}
		return contriAndAdminLevy;
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
		
		//Added by Akshay on 25th Feb 2019
		String strEmployerNLevyAmt= Support.getValueByJavaScript(driver, "SOLODetails_NHSPSEmployerContributionsAndAdministrationLevy");
		strEmployerNLevyAmt= strEmployerNLevyAmt.replaceAll(",", "");
		double employerNLevyAmt= Double.parseDouble(strEmployerNLevyAmt);
		Thread.sleep(500);
		employerNLevyAmt= GPPHelpers.convertValueToDecimals(employerNLevyAmt,2);
		
		double totalContriAmount= actualContriAmt+AVCAmt+ERRBOAmt+MPAVCAmt+employerNLevyAmt;
		
		String strActTotalContriAmt= Support.getValueByJavaScript(driver, "SOLODetails_TotalNHSPSContributions");
		strActTotalContriAmt= strActTotalContriAmt.replaceAll(",", "");
		double actTotalContriAmt= Double.parseDouble(strActTotalContriAmt);
		if(totalContriAmount==actTotalContriAmt){
			isTotalNHSContribution= true;
		}
		return isTotalNHSContribution;
	}
	
	public SoloIncomeEntry clickOnSubmit() throws InterruptedException {
		scrolltoElement(driver, submitBtn);
		wait.until(ExpectedConditions.elementToBeClickable(submitBtn)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on submit button on SOLO income form");
		return new SoloIncomeEntry(driver);
	}
	
	public SoloIncomeEntry clickOnCancelSubmit() throws InterruptedException {
		WebElement modalWindow= driver.findElement(By.xpath("//div[@id='divSubmission']//div[@class='modal-content']"));
		wait.until(ExpectedConditions.visibilityOf(modalWindow));
		if(modalWindow.isDisplayed()){
			scrolltoElement(driver, cancelSubmit);
			wait.until(ExpectedConditions.elementToBeClickable(cancelSubmit)).click();
			Thread.sleep(2000);
			System.out.println("Clicked on cancel submit on SOLO income form");
		}
		return new SoloIncomeEntry(driver);
	}
	
	public SoloIncomeEntry clickOnConfirmSubmit() throws InterruptedException {
		WebElement modalWindow= driver.findElement(By.xpath("//div[@id='divSubmission']//div[@class='modal-content']"));
		wait.until(ExpectedConditions.visibilityOf(modalWindow));
		if(modalWindow.isDisplayed()){
			scrolltoElement(driver, confirmSubmit);
			wait.until(ExpectedConditions.elementToBeClickable(confirmSubmit)).click();
			Thread.sleep(8000);
			System.out.println("Clicked on confirm submit on SOLO income form");
		}
		return new SoloIncomeEntry(driver);
	}

	public String getIncomeRefNo() {
		String refNumber= "";
		wait.until(ExpectedConditions.visibilityOf(refNo));
		refNumber= refNo.getText().toString();
		return refNumber;
	}

}
