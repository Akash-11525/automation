package pageobjects.GPP.QOF;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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

import helpers.Screenshot;
import helpers.Support;
import utilities.ExcelUtilities;

public class QOFPaymentScreen extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="txtContractorCode")
	WebElement contractorTxtBox;
	
	@FindBy(xpath="//div[@id='divFinancialYears']/span//div/button")
	WebElement financialYear;
	
	@FindBy(css="button#btnQofSearch")
	WebElement search;
	
	@FindBy(xpath="//ul[@id='ContractorSearchResult']//li/a")
	WebElement firstResult;
	
	@FindBy(xpath="//table[@id='tblQOFTable']//tbody/tr[1]/td[1]/a")
	WebElement finYearLink;
	
	@FindBy(xpath="//div[@id='modalMonthlyPayment']/div/div")
	WebElement montlyPaymentModalWindow;
	
	@FindBy(xpath="//table[@id='tblQOFTable']//tbody/tr[1]/td[7]/a")
	WebElement viewPaymentHistory;
	
	@FindBy(xpath="//div[@id='modalMonthlyPayment']//button")
	WebElement closeMonthlyPaymentWindow;
	
	
	public QOFPaymentScreen(WebDriver driver) {
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		// This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}
	
	public QOFPaymentScreen searchQOFPaymentInstructionData() {
		Actions action = new Actions(driver);
		String contractorName= ExcelUtilities.getKeyValueByPosition("GPPQOFTESTDATA.xlsx", "FileStatus", "Aspiration", "Name");
		String finYear= ExcelUtilities.getKeyValueByPosition("GPPQOFTESTDATA.xlsx", "FileStatus", "Aspiration", "FinancialYearStartDate");
		String [] finYearArray= finYear.split("-");
		String strFinYear= finYearArray[0].toString();
		String actualFinYearArray[];
		try{
			wait.until(ExpectedConditions.visibilityOf(contractorTxtBox));
			scrolltoElement(driver, contractorTxtBox);
			enterDataInTextField(contractorTxtBox, contractorName, wait);
			Thread.sleep(2000);
			action = new Actions(driver);
			action.moveToElement(firstResult);
			action.click().build().perform();

			action = new Actions(driver);
			action.moveToElement(financialYear);
			action.click().build().perform();
			Thread.sleep(2000);
			List<WebElement> Finacialyears =driver.findElements(By.xpath("//div[@id='divFinancialYears']//ul[@class='multiselect-container dropdown-menu']//label"));
					
			 for (int i=1;i<=Finacialyears.size();i++)
			 {	
				 WebElement Financialyear= driver.findElement(By.xpath("//div[@id='divFinancialYears']//ul[@class='multiselect-container dropdown-menu']/li["+i+"]/a/label"));
				 WebElement finYearCheckBox= driver.findElement(By.xpath("//div[@id='divFinancialYears']//ul[@class='multiselect-container dropdown-menu']/li["+i+"]/a/label/input"));
				 wait.until(ExpectedConditions.elementToBeClickable(finYearCheckBox));
				 scrolltoElement(driver, Financialyear);
				 String FinacialYear_Portal = Financialyear.getText();
				 if(i==1){
					 WebElement selectAll= driver.findElement(By.xpath("//div[@id='divFinancialYears']//ul[@class='multiselect-container dropdown-menu']/li[1]/a/label/input"));
					 wait.until(ExpectedConditions.elementToBeClickable(selectAll));
					 selectAll.click();
					 selectAll.click();
				 }else{
					 actualFinYearArray= FinacialYear_Portal.split("/");
					 String finStartYear= actualFinYearArray[0].toString();
					 if(finStartYear.equalsIgnoreCase(strFinYear)){
						 finYearCheckBox.click();
						 break;
					 }
				 }
			 } 
		}
		catch(Exception e)
		{
			System.out.println("The Payment instruction info is not filled properly");
		}
		return new QOFPaymentScreen(driver);
	}
	
	public QOFPaymentScreen clickOnSearchButton() throws InterruptedException {
		System.out.println("Entered in search click method");
		scrolltoElement(driver, search);
		wait.until(ExpectedConditions.elementToBeClickable(search)).click();
		Thread.sleep(3000);
		return new QOFPaymentScreen(driver);
	}

	public Map<String, Float> saveMonthlyDetails(List<String> fiscalMonths, float monthlyAspValue) {
		Map<String, Float> monthlyValues= new TreeMap<String, Float>();
		int fiscalMonthCount= fiscalMonths.size();
		for(int i=0;i<fiscalMonthCount;i++){
			monthlyValues.put(fiscalMonths.get(i), monthlyAspValue);
		}
		return monthlyValues;
	}
	
	public QOFPaymentScreen clickOnFinYearLink() throws InterruptedException {
		System.out.println("Entered in fin year click method");
		scrolltoElement(driver,finYearLink);
		wait.until(ExpectedConditions.elementToBeClickable(finYearLink)).click();
		Thread.sleep(3000);
		return new QOFPaymentScreen(driver);
	}
	
	public QOFPaymentScreen clickOnCloseWindow() throws InterruptedException {
		System.out.println("Entered in close click method");
		scrolltoElement(driver,closeMonthlyPaymentWindow);
		wait.until(ExpectedConditions.elementToBeClickable(closeMonthlyPaymentWindow)).click();
		Thread.sleep(2000);
		return new QOFPaymentScreen(driver);
	}

	public boolean verifyAspirationMonthlyAmount(Map<String, Float> monthlyAmount) {
		boolean isAspDataVerified=false;
		Map<String, Float> tableValues= new TreeMap<String, Float>();
		List<String>monthValue= new ArrayList<String>();
		List<WebElement> aspRecords= driver.findElements(By.xpath("//table[@id='tblPMSCollapse']//tbody/tr"));
		int aspRecordCount= aspRecords.size();
		
		for(int i=1;i<=aspRecordCount;i++){
			List<WebElement> rowData= driver.findElements(By.xpath("//table[@id='tblPMSCollapse']//tbody/tr["+i+"]/td"));
			
			for(int j=1;j<=rowData.size();j++){
				WebElement element= driver.findElement(By.xpath("//table[@id='tblPMSCollapse']//tbody/tr["+i+"]/td["+j+"]"));
				String value= element.getText().toString();
				if(j==2){
					String[] valueArray= value.split("\\.");
					String number= valueArray[0].toString();
					String decimalValue= valueArray[1].toString();
					String[] numericPartArray= number.split(",");
					value= numericPartArray[0]+numericPartArray[1]+"."+decimalValue;
				}
				monthValue.add(value);
			}
			tableValues.put(monthValue.get(0), Float.parseFloat(monthValue.get(1)));
			monthValue.removeAll(monthValue);
		
		}
		
		if(monthlyAmount.equals(tableValues)){
			isAspDataVerified=true;
		}else{
			isAspDataVerified= false;
		}

		return isAspDataVerified;
	}
	
	public void capturePortalSnap(String string) throws InterruptedException, IOException {
		scrolltoElement(driver, contractorTxtBox);
		Screenshot.TakeSnap(driver, string+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,800)", "");
		Screenshot.TakeSnap(driver, string+"_2");
	}
	

}
