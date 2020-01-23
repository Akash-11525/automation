package pageobjects.GPP.Pensions.PracticeJoiner;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
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
import pageobjects.Adjustments.AdjustmentBatch;
import testdata.ConfigurationData;
import utilities.ExcelUtilities;

public class GPJoinerRetainerEntry extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="NewJoiner")
	WebElement newJoinerBtn;
	
	@FindBy(id="ReturningRetainer")
	WebElement returningRetainerBtn;
	
	@FindBy(id="JoinerSearch")
	WebElement joinerSearchTxtBox;
	
	@FindBy(id="btnjoinerSearch")
	WebElement joinerSearchBtn;
	
	@FindBy(id="contact_NINumber")
	WebElement NINumberTxtBox;
	
	@FindBy(id="contact_NHSPensionSchemeNumber")
	WebElement pensionSchemeTxtBox;
	
	@FindBy(id="EmployeePayrollNumber")
	WebElement payrollNoTxtBox;
	
	@FindBy(id="rdbSalaried")
	WebElement isSalaried;
	
	@FindBy(id="rdbPartnered")
	WebElement isPartnered;
	
	@FindBy(id="rdbBoth")
	WebElement isBoth;
	
	@FindBy(id="EstimatedAnnualIncome")
	WebElement annualIncomeTxtBox;
	
	@FindBy(css="input[value='Opt In']")
	WebElement isOptIn;
	
	@FindBy(css="input[value='Opt out']")
	WebElement isOptOut;
	
	@FindBy(id="TickToConfirmjoiner")
	WebElement tickToConfirm;
	
	@FindBy(id="btnSaveForLater")
	WebElement saveForLaterBtn;
	
	@FindBy(xpath="//div[@class='modal-footer']/div[1]/button")
	WebElement cancelDraft;
	
	@FindBy(xpath="//div[@class='modal-footer']//button[@type='submit']")
	WebElement confirmDraft;
	
	@FindBy(id="btnSubmit")
	WebElement submitBtn;
	
	@FindBy(xpath="//div[@id='divSubmission']/div/div/div[3]/div[1]/button")
	WebElement cancelSubmit;
	
	@FindBy(xpath="//div[@id='divSubmission']/div/div/div[3]/div[2]/button")
	WebElement confirmSubmit;
	
	@FindBy(linkText="Cancel")
	WebElement cancelBtn;
	
	@FindBy(id="CreateNewrecord")
	WebElement createRecord;
	
	@FindBy(id="contact_Title")
	WebElement title;
	
	@FindBy(id="contact_FirstName")
	WebElement firstName;
	
	@FindBy(id="contact_MiddleName")
	WebElement middleName;
	
	@FindBy(id="contact_LastName")
	WebElement lastName;
	
	@FindBy(id="contact_DOB")
	WebElement DOB;
	
	@FindBy(id="contact_EmailAddress")
	WebElement emailAddress;
	
	@FindBy(id="rdbGendermale")
	WebElement isMale;
	
	@FindBy(xpath="//div[@id='divJoiner']/div[6]/div[2]/button")
	WebElement enterAddressButton;
	
	@FindBy(css="div[id='AddressModal']")
	WebElement AddressModalWindow;
	
	@FindBy(css="input[id='AddressLine1']")
	WebElement addressLine1;
	
	@FindBy(css="input[id='AddressLine2']")
	WebElement addressLine2;
	
	@FindBy(css="input[id='AddressLine3']")
	WebElement addressLine3;
	
	@FindBy(css="input[id='City']")
	WebElement addressCity;
	
	@FindBy(css="input[id='Postcode']")
	WebElement addressPostcode;
	
	@FindBy(css="select#Country")
	WebElement addressCountry;
	
	@FindBy(xpath="//div[@class='modal-footer']/div/div/button[1]")
	WebElement addressWindowSaveButton;
	
	@FindBy(id="contact_MobileNumber")
	WebElement mobileNumber;
	
	@FindBy(id="JoiningDate")
	WebElement joiningDate;
	
	@FindBy(id="btnConfirmJoiningDate")
	WebElement joiningDateConfirmWindow;
	
	@FindBy(id="rdbPrudential")
	WebElement prudential;
	
	@FindBy(xpath="//div[@id='divJoiningDate']//div[@class='modal-content']")
	WebElement VCDateWindow;
	
	@FindBy(css="button#btnConfirmJoiningDate")
	WebElement VCConfirmButton;
	
	@FindBy(xpath="//div[@id='dvJoinerForm']/div/div[3]/div[2]//tbody/tr/td[2]")
	WebElement refNo;
	
	@FindBy(xpath="//div[@id='divJoinerSavedraft']//div[@class='modal-content']")
	WebElement confirmDraftWindow;
	
	public GPJoinerRetainerEntry(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}
	
	public void enterAddressManually(int colNumber) throws InterruptedException
	{
        try{
	        	scrolltoElement(driver, enterAddressButton);
	        	wait.until(ExpectedConditions.elementToBeClickable(enterAddressButton)).click();
	        	Thread.sleep(2000);
	            wait.until(ExpectedConditions.elementToBeClickable(AddressModalWindow));
	            //to be changed as per test data sheet
	            String addressline1 = ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPensionsTestData.xlsx", "Test Data", "Addressline1", colNumber);
	    		String addressline2 = ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPensionsTestData.xlsx", "Test Data", "Addressline2", colNumber);
	    		String addressline3 = ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPensionsTestData.xlsx", "Test Data", "Addressline3", colNumber);
	    		String addresscity = ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPensionsTestData.xlsx", "Test Data", "AddressCity", colNumber);
	    		String addresscountry = ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPensionsTestData.xlsx", "Test Data", "AddressCountry", colNumber);
	    		String addresspostcode = ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPensionsTestData.xlsx", "Test Data", "AddressPostCode", colNumber);
	            if(AddressModalWindow.isDisplayed())
	            {
	               enterDataInTextField(addressLine1,addressline1,wait);
	               enterDataInTextField(addressLine2,addressline2,wait);
	               enterDataInTextField(addressLine3,addressline3,wait);
	               enterDataInTextField(addressCity,addresscity,wait);
	               wait.until(ExpectedConditions.elementToBeClickable(addressCountry));
	               CommonFunctions.selectOptionFromDropDown(addressCountry, addresscountry);
	               Support.enterDataInTextField(addressPostcode,addresspostcode,wait);
	               wait.until(ExpectedConditions.elementToBeClickable(addressWindowSaveButton)).click();
	               Thread.sleep(2000);
	                 // helpers.CommonFunctions.ClickOnButton("Save", driver);   
	            }
	     }
	     catch(NoSuchElementException e)
	     {
	    	 System.out.println("The element is not found on Address Pop up box" +e);
	     }
	}

	public GPJoinerRetainerEntry enterNewJoinerData(int colNumber) throws InterruptedException, ParseException {
		
		String strAnnualIncome= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPensionsTestData.xlsx", "Test Data", "AnnualIncome", colNumber);
		String strFirstName= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPensionsTestData.xlsx", "Test Data", "FirstName", colNumber);
		
		scrolltoElement(driver, joinerSearchTxtBox);
		enterDataInTextField(joinerSearchTxtBox, CommonFunctions.generateTS(strFirstName), wait);
		wait.until(ExpectedConditions.elementToBeClickable(joinerSearchBtn)).click();
		
		Actions focus= new Actions(driver);
		focus.moveToElement(NINumberTxtBox).build().perform();
		focus.click();
		focus.moveToElement(createRecord).build().perform();
		wait.until(ExpectedConditions.elementToBeClickable(createRecord)).click();
		wait.until(ExpectedConditions.elementToBeClickable(title));
		
		enterJoinerPersonalData(colNumber);
		enterAddressManually(colNumber);
		Thread.sleep(2000);
		
		Date date= CommonFunctions.getDateAfterDays(0);
		String joinDate= CommonFunctions.convertDateToString(date, "dd/MM/yyyy");
		String[]dateDetails= {joinDate,"dd/MM/yyyy","1"};
		String workingDay= CommonFunctions.getNextWorkingDayExcludingWeekends(dateDetails);
		enterDataInTextField(joiningDate, workingDay, wait);
		joiningDate.sendKeys(Keys.TAB);
		Thread.sleep(4000);
		boolean flag= false;
		int count=0;
		while(!flag){
			System.out.println("Entered in loop for count: "+count);
			try{
				if(joiningDateConfirmWindow.isDisplayed()){
					wait.until(ExpectedConditions.elementToBeClickable(joiningDateConfirmWindow)).click();
					Thread.sleep(2000);
					flag= true;
					System.out.println("Window has appeared after count: "+count);
				}
				count++;
				//flag= joiningDateConfirmWindow.isDisplayed();
				System.out.println("Next iteration in loop for count: "+count);
			}catch(Exception e){
				System.out.println("Confirmation window is not displayed");
			}
		}
		
		enterDataInTextField(annualIncomeTxtBox, strAnnualIncome, wait);
		
		wait.until(ExpectedConditions.elementToBeClickable(tickToConfirm)).click();
		return new GPJoinerRetainerEntry(driver);
	}
	
	private void enterJoinerPersonalData(int colNumber) throws InterruptedException {
		String strTitle= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPensionsTestData.xlsx", "Test Data", "Title", colNumber);
		String strFirstName= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPensionsTestData.xlsx", "Test Data", "FirstName", colNumber);
		String strSurname= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPensionsTestData.xlsx", "Test Data", "Surname", colNumber);
		String strAge= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPensionsTestData.xlsx", "Test Data", "PatientAge", colNumber);
		String strMobileNo= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPensionsTestData.xlsx", "Test Data", "MobileNumber", colNumber);
		String strEmail= "test"+CommonFunctions.generateRandomNo(6)+"@test.com";
		
		CommonFunctions.selectOptionFromDropDown(title,strTitle);
		enterDataInTextField(firstName, CommonFunctions.generateTS(strFirstName), wait);
		
		enterDataInTextField(lastName, strSurname, wait);
		
		String age = CommonFunctions.getDOB(strAge);
		enterDataInTextField(DOB, age, wait);
		
		wait.until(ExpectedConditions.elementToBeClickable(isMale)).click();
		
		enterDataInTextField(emailAddress, strEmail.toString(), wait);
		
		enterDataInTextField(mobileNumber, strMobileNo, wait);
		
		String NI_Number= CommonFunctions.generateRandomNo(6);
		String finalNI_Number= "PJ"+NI_Number+"P";
		enterDataInTextField(NINumberTxtBox, finalNI_Number, wait);
	}

	public GPJoinerRetainerEntry clickOnSaveForLater() throws InterruptedException {
		scrolltoElement(driver, saveForLaterBtn);
		wait.until(ExpectedConditions.elementToBeClickable(saveForLaterBtn)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on save for later button on joiner entry form");
		return new GPJoinerRetainerEntry(driver);
	}
	
	public GPJoinerRetainerEntry clickOnCancelDraft() throws InterruptedException {
		scrolltoElement(driver, cancelDraft);
		wait.until(ExpectedConditions.elementToBeClickable(cancelDraft)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on cancel draft on joiner entry form");
		return new GPJoinerRetainerEntry(driver);
	}
	
	public GPJoinerRetainerEntry clickOnConfirmDraft() throws InterruptedException {
		wait.until(ExpectedConditions.visibilityOf(confirmDraftWindow));
		if(confirmDraftWindow.isDisplayed()){
			scrolltoElement(driver, confirmDraft);
			Thread.sleep(3000);
			System.out.println("Entered in confirm draft on joiner entry form");
			wait.until(ExpectedConditions.elementToBeClickable(confirmDraft)).click();
			Thread.sleep(2000);
			System.out.println("Clicked on confirm draft on joiner entry form");
		}
		return new GPJoinerRetainerEntry(driver);
	}
	
	public GPJoinerRetainerEntry clickOnSubmit() throws InterruptedException {
		scrolltoElement(driver, submitBtn);
		wait.until(ExpectedConditions.elementToBeClickable(submitBtn)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on submit button on joiner entry form");
		return new GPJoinerRetainerEntry(driver);
	}
	
	public GPJoinerRetainerEntry clickOnCancelSubmit() throws InterruptedException {
		scrolltoElement(driver, cancelSubmit);
		wait.until(ExpectedConditions.elementToBeClickable(cancelSubmit)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on cancel submit on joiner entry form");
		return new GPJoinerRetainerEntry(driver);
	}
	
	public GPJoinerRetainerEntry clickOnConfirmSubmit() throws InterruptedException {
		scrolltoElement(driver, confirmSubmit);
		wait.until(ExpectedConditions.elementToBeClickable(confirmSubmit)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on confirm submit on joiner entry form");
		return new GPJoinerRetainerEntry(driver);
	}

	public String getApplicationRefNo(String key) {
		String appRefNo="";
		wait.until(ExpectedConditions.visibilityOf(refNo));
		appRefNo= refNo.getText().toString();
		ExcelUtilities.setKeyValueByPosition("GPPPensionsTestData.xlsx", "ApplicationKeyMapping", appRefNo, key, "CLAIMID");
		return appRefNo;
	}

	public boolean verifyAppRefFormat(String appRefNo) {
		boolean isFormatmatched=false;
		String[]refNo= appRefNo.split("-");
		String finYear="";
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		int monthIndex = cal.get(Calendar.MONTH); // 1
		int currentYear = cal.get(Calendar.YEAR); // 2018
		String strCurrentYear= Integer.toString(currentYear);
		System.out.println("Current month index is: "+monthIndex);
		System.out.println("Current year is: "+currentYear);
		
		if(monthIndex<3){
			finYear= Integer.toString(currentYear-1)+"/"+strCurrentYear.substring(2);
			if(finYear.equalsIgnoreCase(refNo[0])){
				if("JPR".equalsIgnoreCase(refNo[1])){
					if(refNo[2].length()==5){
						isFormatmatched=true;
					}
				}else if("JNP".equalsIgnoreCase(refNo[1])){
					if(refNo[2].length()==5){
						isFormatmatched=true;
					}
				}else if("RET".equalsIgnoreCase(refNo[1])){
					if(refNo[2].length()==5){
						isFormatmatched=true;
					}
				}else if("LVR".equalsIgnoreCase(refNo[1])){
					if(refNo[2].length()==5){
						isFormatmatched=true;
					}
				}else if("JNR".equalsIgnoreCase(refNo[1])){
					if(refNo[2].length()==5){
						isFormatmatched=true;
					}
				}
			}else{
				isFormatmatched=false;
			}
		}else{
			int nxtYear= currentYear+1;
			String strNxtYear= Integer.toString(nxtYear);
			finYear= Integer.toString(currentYear)+"/"+strNxtYear.substring(2);
			if(finYear.equalsIgnoreCase(refNo[0])){
				if("JPR".equalsIgnoreCase(refNo[1])){
					if(refNo[2].length()==5){
						isFormatmatched=true;
					}
				}else if("JNP".equalsIgnoreCase(refNo[1])){
					if(refNo[2].length()==5){
						isFormatmatched=true;
					}
				}else if("RET".equalsIgnoreCase(refNo[1])){
					if(refNo[2].length()==5){
						isFormatmatched=true;
					}
				}else if("LVR".equalsIgnoreCase(refNo[1])){
					if(refNo[2].length()==5){
						isFormatmatched=true;
					}
				}else if("JNR".equalsIgnoreCase(refNo[1])){
					if(refNo[2].length()==5){
						isFormatmatched=true;
					}
				}
			}else{
				isFormatmatched=false;
			}
		}
		return isFormatmatched;
	}
	
	public void captureJoinerFormSnaps(String note) throws InterruptedException, IOException {

		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,800)", "");
		Screenshot.TakeSnap(driver, note+"_2");
	}

	public GPJoinerRetainerEntry enterPractitionerData(int colNumber,String environment, String refDataKey) throws ParseException, InterruptedException {
		String strAnnualIncome= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPensionsTestData.xlsx", "Test Data", "AnnualIncome", colNumber);
		String practiceCode= ConfigurationData.getRefDataDetails(environment, refDataKey);
		//ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPensionsTestData.xlsx", "Test Data", "PracticeCode", colNumber);
		
		scrolltoElement(driver, joinerSearchTxtBox);
		enterDataInTextField(joinerSearchTxtBox, practiceCode, wait);
		wait.until(ExpectedConditions.elementToBeClickable(joinerSearchBtn)).click();
		WebElement firstRecord= driver.findElement(By.xpath("//ul[@id='joinerSearchResult']/li[1]/a"));
		firstRecord.click();
		
		Date date= CommonFunctions.getDateAfterDays(0);
		String joinDate= CommonFunctions.convertDateToString(date, "dd/MM/yyyy");
		String[]dateDetails= {joinDate,"dd/MM/yyyy","1"};
		String workingDay= CommonFunctions.getNextWorkingDayExcludingWeekends(dateDetails);
		enterDataInTextField(joiningDate, workingDay, wait);
		joiningDate.sendKeys(Keys.TAB);
		//Thread.sleep(2000);
		boolean flag= false;
		int count=0;
		while(!flag){
			System.out.println("Entered in loop for count: "+count);
			try{
				if(joiningDateConfirmWindow.isDisplayed()){
					wait.until(ExpectedConditions.elementToBeClickable(joiningDateConfirmWindow)).click();
					Thread.sleep(2000);
					flag= true;
					System.out.println("Window has appeared after count: "+count);
				}
				count++;
				//flag= joiningDateConfirmWindow.isDisplayed();
				System.out.println("Next iteration in loop for count: "+count);
			}catch(Exception e){
				System.out.println("Confirmation window is not displayed");
			}
		}
/*		try{
			if(joiningDateConfirmWindow.isDisplayed()){
				wait.until(ExpectedConditions.elementToBeClickable(joiningDateConfirmWindow)).click();
				Thread.sleep(2000);
			}
		}catch(Exception e){
			System.out.println("Confirmation window is not displayed");
		}*/
		
		wait.until(ExpectedConditions.elementToBeClickable(annualIncomeTxtBox)).click();
		annualIncomeTxtBox.clear();
		annualIncomeTxtBox.sendKeys(strAnnualIncome);
		
		wait.until(ExpectedConditions.elementToBeClickable(tickToConfirm)).click();
		return new GPJoinerRetainerEntry(driver);
	}

	public GPJoinerRetainerEntry enterVoluntaryContributions(int colNumber,boolean isMPVACRate) throws ParseException, InterruptedException {
		String strVoluntaryPercentage= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPensionsTestData.xlsx", "Test Data", "VoluntaryPercentage", colNumber);
		String ERRBOMonthlyRate= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPensionsTestData.xlsx", "Test Data", "ERRBOMonthlyRate", colNumber);
		String MPVACMonthlyRate= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPensionsTestData.xlsx", "Test Data", "MPVACMonthlyRate", colNumber);
		String strMPVACPercentage= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPensionsTestData.xlsx", "Test Data", "MPVACPercentage", colNumber);
		String strValidForDays= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPensionsTestData.xlsx", "Test Data", "ValidForDays", colNumber);
		float voluntaryPercentage= Float.parseFloat(strVoluntaryPercentage);
		float MPVACPercentage= Float.parseFloat(strMPVACPercentage);
		int validForDays= Integer.parseInt(strValidForDays);
		Actions focus= new Actions(driver);
		
		String addVolContNum= CommonFunctions.generateRandomNo(6);
		WebElement contNumWindow;
		WebElement elmAddVolContNum= driver.findElement(By.xpath("//table[@id='tblGSUMRun']//tbody/tr[1]/td[2]/input"));
		enterDataInTextField(elmAddVolContNum, addVolContNum, wait);
		elmAddVolContNum.sendKeys(Keys.TAB);
		Thread.sleep(2000);
		WebElement modalWindow= driver.findElement(By.xpath("//div[@id='modal-container']//div[@class='modal-content']"));
		Thread.sleep(3000);
		boolean flag= true;
		int count=0;
		while(flag){
			if(modalWindow.isDisplayed()){
				contNumWindow= driver.findElement(By.xpath("//*[@id='modal-container']/div/div/div[3]/div/button"));
				focus.moveToElement(contNumWindow).moveToElement(contNumWindow).build().perform();
				wait.until(ExpectedConditions.elementToBeClickable(contNumWindow));
				contNumWindow.click();
				flag= false;
				System.out.println("Window has appeared for Add. contribution after count: "+count);
				Thread.sleep(3000);
			}
			count++;
		}
/*		if(modalWindow.isDisplayed()){
			contNumWindow= driver.findElement(By.xpath("//*[@id='modal-container']/div/div/div[3]/div/button"));
			focus.moveToElement(contNumWindow).moveToElement(contNumWindow).build().perform();
			wait.until(ExpectedConditions.elementToBeClickable(contNumWindow));
			contNumWindow.click();
			Thread.sleep(3000);
		}*/
		String ERRBOContNum= CommonFunctions.generateRandomNo(6);
		WebElement elmERRBOContNum= driver.findElement(By.xpath("//table[@id='tblGSUMRun']//tbody/tr[1]/td[3]/input"));
		enterDataInTextField(elmERRBOContNum, ERRBOContNum, wait);
		elmAddVolContNum.sendKeys(Keys.TAB);
		Thread.sleep(3000);
		modalWindow= driver.findElement(By.xpath("//div[@id='modal-container']//div[@class='modal-content']"));
		Thread.sleep(3000);
		flag= true;
		count=0;
		while(flag){
			if(modalWindow.isDisplayed()){
				contNumWindow= driver.findElement(By.xpath("//*[@id='modal-container']/div/div/div[3]/div/button"));
				focus.moveToElement(contNumWindow).moveToElement(contNumWindow).build().perform();
				wait.until(ExpectedConditions.elementToBeClickable(contNumWindow));
				contNumWindow.click();
				flag= false;
				System.out.println("Window has appeared for ERRBO after count: "+count);
				Thread.sleep(3000);
			}
			count++;
		}
/*		if(modalWindow.isDisplayed()){
			contNumWindow= driver.findElement(By.xpath("//*[@id='modal-container']/div/div/div[3]/div/button"));
			focus.moveToElement(contNumWindow).moveToElement(contNumWindow).build().perform();
			wait.until(ExpectedConditions.elementToBeClickable(contNumWindow));
			contNumWindow.click();
			Thread.sleep(3000);
		}*/
		String MPVACContNum= CommonFunctions.generateRandomNo(6);
		WebElement elmMPVACContNum= driver.findElement(By.xpath("//table[@id='tblGSUMRun']//tbody/tr[1]/td[4]/input"));
		enterDataInTextField(elmMPVACContNum, MPVACContNum, wait);
		elmMPVACContNum.sendKeys(Keys.TAB);
		Thread.sleep(3000);
		modalWindow= driver.findElement(By.xpath("//div[@id='modal-container']//div[@class='modal-content']"));
		Thread.sleep(3000);
		flag= true;
		count=0;
		while(flag){
			if(modalWindow.isDisplayed()){
				contNumWindow= driver.findElement(By.xpath("//*[@id='modal-container']/div/div/div[3]/div/button"));
				focus.moveToElement(contNumWindow).moveToElement(contNumWindow).build().perform();
				wait.until(ExpectedConditions.elementToBeClickable(contNumWindow));
				contNumWindow.click();
				flag= false;
				System.out.println("Window has appeared for MPAVC after count: "+count);
				Thread.sleep(3000);
			}
			count++;
		}
/*		if(modalWindow.isDisplayed()){
			contNumWindow= driver.findElement(By.xpath("//*[@id='modal-container']/div/div/div[3]/div/button"));
			focus.moveToElement(contNumWindow).moveToElement(contNumWindow).build().perform();
			wait.until(ExpectedConditions.elementToBeClickable(contNumWindow));
			contNumWindow.click();
			Thread.sleep(3000);
		}*/
		WebElement elmERRBORate= driver.findElement(By.xpath("//table[@id='tblGSUMRun']//tbody/tr[2]/td[3]/input"));
		enterDataInTextField(elmERRBORate, ERRBOMonthlyRate, wait);
		
		String strAnnualIncome= Support.getValueByJavaScript(driver, "EstimatedAnnualIncome");
		float annualIncome= Float.parseFloat(strAnnualIncome);
		float monthlyIncome= annualIncome/12;
		
		if(isMPVACRate){
			WebElement elmMPVACRate= driver.findElement(By.xpath("//table[@id='tblGSUMRun']//tbody/tr[2]/td[4]/input"));
			enterDataInTextField(elmMPVACRate, MPVACMonthlyRate, wait);
		}else{
			float volLumpsumAmt= (monthlyIncome*MPVACPercentage);
			WebElement elmVolLumpsumAmt= driver.findElement(By.xpath("//table[@id='tblGSUMRun']//tbody/tr[3]/td[4]/input"));
			enterDataInTextField(elmVolLumpsumAmt, Float.toString(volLumpsumAmt), wait);
		}
		
		float volLumpsumAmt= (monthlyIncome*voluntaryPercentage);
		WebElement elmVolLumpsumAmt= driver.findElement(By.xpath("//table[@id='tblGSUMRun']//tbody/tr[3]/td[2]/input"));
		wait.until(ExpectedConditions.elementToBeClickable(elmVolLumpsumAmt)).clear();
		elmVolLumpsumAmt.sendKeys(Float.toString(volLumpsumAmt));
		enterDataInTextField(elmVolLumpsumAmt, Float.toString(volLumpsumAmt), wait);
		
		String fromDate= CommonFunctions.getTodayDate();
		Date date= CommonFunctions.getDateAfterDays(validForDays);
		String toDate= CommonFunctions.convertDateToString(date, "dd/MM/yyyy");
		int rowCount= driver.findElements(By.xpath("//table[@id='tblGSUMRun']//tbody/tr")).size();
		for(int i=4;i<=(rowCount-1);i++){
			int colCount= driver.findElement(By.xpath("//table[@id='tblGSUMRun']//tbody/tr["+i+"]")).findElements(By.tagName("td")).size();
			for(int j=2;j<=colCount;j++){
				WebElement column= driver.findElement(By.xpath("//table[@id='tblGSUMRun']//tbody/tr["+i+"]/td["+j+"]/div/input"));
				wait.until(ExpectedConditions.elementToBeClickable(column)).clear();
				if(i==(rowCount-1)){
					column.sendKeys(toDate);
					flag= true;
					count=0;
					while(flag){
						if(VCDateWindow.isDisplayed()){
							focus.moveToElement(VCConfirmButton).moveToElement(VCConfirmButton).build().perform();
							wait.until(ExpectedConditions.elementToBeClickable(VCConfirmButton));
							VCConfirmButton.click();
							flag= false;
							System.out.println("Window has appeared for MPAVC after count: "+count);
							Thread.sleep(3000);
						}
						count++;
					}
				}else{
					column.sendKeys(fromDate);
					flag= true;
					count=0;
					while(flag){
						if(VCDateWindow.isDisplayed()){
							focus.moveToElement(VCConfirmButton).moveToElement(VCConfirmButton).build().perform();
							wait.until(ExpectedConditions.elementToBeClickable(VCConfirmButton));
							VCConfirmButton.click();
							flag= false;
							System.out.println("Window has appeared for MPAVC after count: "+count);
							Thread.sleep(3000);
						}
						count++;
					}
				}
			}
		}
		focus.moveToElement(prudential).build().perform();
		wait.until(ExpectedConditions.elementToBeClickable(prudential)).click();
		
		return new GPJoinerRetainerEntry(driver);
	}

	public GPJoinerRetainerEntry enterRetainerData(int colNumber,String environment,String refDataKey) throws ParseException, InterruptedException {
		String practiceCode= ConfigurationData.getRefDataDetails(environment, refDataKey);
		String strAnnualIncome= ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPensionsTestData.xlsx", "Test Data", "AnnualIncome", colNumber);
		wait.until(ExpectedConditions.elementToBeClickable(returningRetainerBtn)).click();
		
		scrolltoElement(driver, joinerSearchTxtBox);
		enterDataInTextField(joinerSearchTxtBox, practiceCode, wait);
		wait.until(ExpectedConditions.elementToBeClickable(joinerSearchBtn)).click();
		WebElement firstRecord= driver.findElement(By.xpath("//ul[@id='joinerSearchResult']/li[1]/a"));
		firstRecord.click();
		
		Date date= CommonFunctions.getDateAfterDays(0);
		String joinDate= CommonFunctions.convertDateToString(date, "dd/MM/yyyy");
		String[]dateDetails= {joinDate,"dd/MM/yyyy","1"};
		String workingDay= CommonFunctions.getNextWorkingDayExcludingWeekends(dateDetails);
		enterDataInTextField(joiningDate, workingDay, wait);
		joiningDate.sendKeys(Keys.TAB);
		
/*		Thread.sleep(2000);
		wait.until(ExpectedConditions.elementToBeClickable(joiningDateConfirmWindow)).click();
		Thread.sleep(2000);*/
		
		try{
			if(joiningDateConfirmWindow.isDisplayed()){
				wait.until(ExpectedConditions.elementToBeClickable(joiningDateConfirmWindow)).click();
				Thread.sleep(2000);
			}
		}catch(Exception e){
			System.out.println("Confirmation window is not displayed");
		}
		
		enterDataInTextField(annualIncomeTxtBox, strAnnualIncome, wait);
		
		wait.until(ExpectedConditions.elementToBeClickable(tickToConfirm)).click();
		
		return new GPJoinerRetainerEntry(driver);
	}
	
	public GPJoinerRetainerEntry enterPractitionerData(String colName,String file,String sheet,String environment,String refDataKey) throws ParseException, InterruptedException {
		String strAnnualIncome= ExcelUtilities.getKeyValueByPosition(file, sheet, "AnnualIncome", colName);
		String practiceCode= ConfigurationData.getRefDataDetails(environment, refDataKey);
		//ExcelUtilities.getKeyValueByPosition(file, sheet,"PracticeCode", colName);
		
		scrolltoElement(driver, joinerSearchTxtBox);
		enterDataInTextField(joinerSearchTxtBox, practiceCode, wait);
		wait.until(ExpectedConditions.elementToBeClickable(joinerSearchBtn)).click();
		//WebElement firstRecord= driver.findElement(By.xpath("//ul[@id='joinerSearchResult']/li[1]/a"));
		//firstRecord.click(); changes made by akash
		
		Date date= CommonFunctions.getDateAfterDays(0);
		String joinDate= CommonFunctions.convertDateToString(date, "dd/MM/yyyy");
		String[]dateDetails= {joinDate,"dd/MM/yyyy","1"};
		String workingDay= CommonFunctions.getNextWorkingDayExcludingWeekends(dateDetails);
		enterDataInTextField(joiningDate, workingDay, wait);
		joiningDate.sendKeys(Keys.TAB);
		//boolean flag= true;
		int count=0;
		//while(flag){
			try{
				if(joiningDateConfirmWindow.isDisplayed()){
					wait.until(ExpectedConditions.elementToBeClickable(joiningDateConfirmWindow)).click();
					Thread.sleep(2000);
					//flag= false;
					System.out.println("Window has appeared after count: "+count);
				}
				count++;
			}catch(Exception e){
				System.out.println("Confirmation window is not displayed");
			}
		//}
		
		enterDataInTextField(annualIncomeTxtBox, strAnnualIncome, wait);
		
		wait.until(ExpectedConditions.elementToBeClickable(tickToConfirm)).click();
		return new GPJoinerRetainerEntry(driver);
	}

}
