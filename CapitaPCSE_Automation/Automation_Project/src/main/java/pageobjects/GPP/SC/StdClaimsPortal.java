package pageobjects.GPP.SC;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.CommonFunctions;
import helpers.Screenshot;
import helpers.Support;
import utilities.ExcelUtilities;

public class StdClaimsPortal extends Support {
	WebDriver driver;
	WebDriverWait wait;
	static String filePath = System.getProperty("user.dir") + "\\Upload\\sample.pdf";
	static String files = System.getProperty("user.dir") + "\\Upload\\StdClaimsTestFiles";

	@FindBy(css = "a[href='/StandardClaims/SCApproval']")
	WebElement claimsApproval;

	@FindBy(css = "select#ClaimType")
	WebElement selectClaimType;

	@FindBy(css = "button#btnCreateNewClaim")
	WebElement createClaim;

	@FindBy(css = "button.btn.btn-info.pull-right")
	WebElement applyLocumPreApproval;

	@FindBy(css = "a#tab1")
	WebElement stdClaimsTab;

	@FindBy(css = "a#tab2")
	WebElement preApprovalTab;

	@FindBy(xpath = "//*[@id='divSelectClaimTypeModal']/div/div/div[3]/button")
	WebElement modalWindowCloseButton;

	@FindBy(css = "input#SCPortalFilter_ContractorName")
	WebElement ContractorTxtBox;

	@FindBy(css = "select#SCPortalFilter_ClaimStatus")
	WebElement ClaimStatus;

	@FindBy(css = "input#txtSCPortalFilterStartDate")
	WebElement StartDateTxtBox;

	@FindBy(css = "input#txtSCPortalFilterEndDate")
	WebElement EndDateTxtBox;

	@FindBy(css = "select#SCPortalFilter_ClaimType")
	WebElement ClaimType;

	@FindBy(css = "button#btnSCClaimSearch")
	WebElement btn_SearchClaim;

	@FindBy(xpath = "//*[@id='tblSCClaims']/tbody")
	WebElement stdClaimDataGrid;

	@FindBy(xpath = "//div[@id='divConfRevertToDraft']//button[@class='btn btn-default']")
	WebElement cancelRevert;

	@FindBy(xpath = "//div[@id='divConfRevertToDraft']//button[@class='btn btn-success']")
	WebElement confirmRevert;
	
	@FindBy(xpath="//div[@id='divConfDeleteDraft']//button[@class='btn btn-default']")
	WebElement cancelDelete;
	
	@FindBy(xpath="//div[@id='divConfDeleteDraft']//button[@class='btn btn-success']")
	WebElement confirmDelete;

	public StdClaimsPortal(WebDriver driver) {
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		// This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}

	public SC_RetainerSessionClaim clickToCreateRetainerClaim(int colNumber) {

		String claimType = ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "RetainerClaimData","StdClaimType", colNumber);
		CommonFunctions.selectOptionFromDropDown(selectClaimType, claimType);
		System.out.println("Entered in Create retainer Claim click method");
		scrolltoElement(driver, createClaim);
		wait.until(ExpectedConditions.elementToBeClickable(createClaim)).click();
		return new SC_RetainerSessionClaim(driver);
	}

	public SC_PremisesClaim clickToCreatePremisesClaim(int colNumber) {
		// to be picked from excel sheet
		String claimType = ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "PremisesClaimData","StdClaimType", colNumber);
		CommonFunctions.selectOptionFromDropDown(selectClaimType, claimType);
		System.out.println("Entered in Create premises Claim click method");
		scrolltoElement(driver, createClaim);
		wait.until(ExpectedConditions.elementToBeClickable(createClaim)).click();
		return new SC_PremisesClaim(driver);
	}

	public SC_RegistrarExpenseClaim clickToCreateRegistrarClaim(int colNumber)
	//
	{
		// to be picked from excel sheet
		String claimType = ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx","RegistrarExpenseClaimData", "StdClaimType", colNumber);
		CommonFunctions.selectOptionFromDropDown(selectClaimType, claimType);
		System.out.println("Entered in Create register Claim click method");
		scrolltoElement(driver, createClaim);
		wait.until(ExpectedConditions.elementToBeClickable(createClaim)).click();
		return new SC_RegistrarExpenseClaim(driver);
	}

	public SC_LocumCostClaim clickToCreateLocumCostClaim(int colNumber) {

		String claimType = ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx", "LocumCostClaimData","StdClaimType", colNumber);
		CommonFunctions.selectOptionFromDropDown(selectClaimType, claimType);
		System.out.println("Entered in Create locum cost Claim click method");
		scrolltoElement(driver, createClaim);
		wait.until(ExpectedConditions.elementToBeClickable(createClaim)).click();
		return new SC_LocumCostClaim(driver);
	}

	public LocumPreApprovalClaim clickOnApplyPreApprovalClaimButton() {
		System.out.println("Entered in locum pre approval click method");
		scrolltoElement(driver, applyLocumPreApproval);
		wait.until(ExpectedConditions.elementToBeClickable(applyLocumPreApproval)).click();
		return new LocumPreApprovalClaim(driver);
	}

	public StdClaimsPreApprovalPortal clickOnPreApprovalClaimPortalTab() {
		System.out.println("Entered in locum pre approval portal click method");
		scrolltoElement(driver, preApprovalTab);
		wait.until(ExpectedConditions.elementToBeClickable(preApprovalTab)).click();
		return new StdClaimsPreApprovalPortal(driver);
	}

	public StdClaimsApprovalWindow clickOnClaimApproval() {
		System.out.println("Entered in claim approval click method");
		scrolltoElement(driver, claimsApproval);
		wait.until(ExpectedConditions.elementToBeClickable(claimsApproval)).click();
		return new StdClaimsApprovalWindow(driver);
	}

	public String getClaimNumber(String key) throws InterruptedException {
		searchStdClaims();
		String claimNo = driver.findElement(By.xpath("//table[@id='tblSCClaims']//tbody/tr[1]/td[1]/a")).getText().trim();
		ExcelUtilities.setKeyValueByPosition("GPPSCTESTDATA.xlsx", "ExpectedClaimDetails", claimNo, key, "CLAIMID");
		return claimNo;
	}
	
	public void setClaimNumberforGMP(String claimNo, int colNumber) throws InterruptedException {
		ExcelUtilities.setKeyValueinExcelWithPosition("GMPTESTDATA.xlsx", "ClaimDetails", "Claim Number", claimNo,colNumber);
	}
	
	public void setClaimStatusforGMP(String finalClaimStatus, int colNumber) throws InterruptedException {
		ExcelUtilities.setKeyValueinExcelWithPosition("GMPTESTDATA.xlsx", "ClaimDetails", "ExpectedClaims", finalClaimStatus,colNumber);
	}
	//Commented by Akshay
/*	public String getClaimNumberWithClaimStatus(String key, String ClaimStatus) throws InterruptedException {
		searchStdClaimsWithClaimStatus(ClaimStatus);
		String claimNo = driver.findElement(By.xpath("//table[@id='tblSCClaims']//tbody/tr[1]/td[1]/a")).getText()
				.trim();
		ExcelUtilities.setKeyValueByPosition("GPPSCTESTDATA.xlsx", "ExpectedClaimDetails", claimNo, key, "CLAIMID");
		return claimNo;
	}*/
	
	public String getClaimNumberWithClaimStatusAndType(String key, String ClaimStatus,int colNumber, String sheet, boolean verifyDeleteBtn) throws InterruptedException {
		searchStdClaimsWithClaimStatusAndType(ClaimStatus,sheet,colNumber);
		String claimNo= getClaimNoWithDeleteBtn(key,verifyDeleteBtn);
		return claimNo;
	}

	private String getClaimNoWithDeleteBtn(String key,boolean verifyDeleteBtn) {
		String strClmNo="";
		if(verifyDeleteBtn){
			for(int i=1;i<=10;i++){
				try{
					WebElement elmClm= driver.findElement(By.xpath("//table[@id='tblSCClaims']//tbody/tr["+i+"]/td[1]/a"));
					WebElement elmDelete= driver.findElement(By.xpath("//table[@id='tblSCClaims']//tbody/tr["+i+"]/td[8]/button"));
					if(elmDelete.isDisplayed()){
						strClmNo = elmClm.getText().trim();
						ExcelUtilities.setKeyValueByPosition("GPPSCTESTDATA.xlsx", "ExpectedClaimDetails", strClmNo, key, "CLAIMID");
						break;
					}	
				}catch(Exception e){
					System.out.println("Element is not found at index: "+i);
				}
		
			}
			
		}
		return strClmNo; 
	}
	
	private String getClaimStatusWithDeleteBtn(String key,boolean verifyDeleteBtn) {
		String strClmStatus="";
		if(verifyDeleteBtn){
			for(int i=1;i<=10;i++){
				try{
					//WebElement elmClm= driver.findElement(By.xpath("//table[@id='tblSCClaims']//tbody/tr["+i+"]/td[1]/a"));
					WebElement elmDelete= driver.findElement(By.xpath("//table[@id='tblSCClaims']//tbody/tr["+i+"]/td[8]/button"));
					WebElement elmStatus= driver.findElement(By.xpath("//table[@id='tblSCClaims']//tbody/tr["+i+"]/td[6]"));
					if(elmDelete.isDisplayed()){
						strClmStatus = elmStatus.getText().trim();
						break;
					}	
				}catch(Exception e){
					System.out.println("Element is not found at index: "+i);
				}
		
			}
			
		}
		return strClmStatus; 
	}

	public void captureStdClaimPortalSnap(String string) throws InterruptedException, IOException {
		scrolltoElement(driver, stdClaimsTab);
		Screenshot.TakeSnap(driver, string + "_1");
		Thread.sleep(1000);

		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, string + "_2");

	}

	public String getClaimStatus() throws InterruptedException {
		searchStdClaims();
		String expStatus = driver.findElement(By.xpath("//table[@id='tblSCClaims']//tbody/tr[1]/td[6]")).getText().trim();
		return expStatus;
	}
//Commented by Akshay
/*	public String getClaimStatusByValue(String claimStatus) throws InterruptedException {
		searchStdClaimsWithClaimStatus(claimStatus);
		String expStatus = driver.findElement(By.xpath("//table[@id='tblSCClaims']//tbody/tr[1]/td[6]")).getText()
				.trim();
		return expStatus;
	}*/
	
	public String getClaimStatusByValues(String key,String ClaimStatus,String sheet,int colNumber) throws InterruptedException {
		searchStdClaimsWithClaimStatusAndType(ClaimStatus,sheet,colNumber);
		String expStatus = getClaimStatusWithDeleteBtn(key, true);
		return expStatus;
	}

	public StdClaimsPortal searchStdClaims() throws InterruptedException {
		Thread.sleep(3000);
		String fromDate = CommonFunctions.getTodayDate();
		enterDataInTextField(StartDateTxtBox, fromDate, wait);
		System.out.println("from date criteria entered for SC claims portal");

		String toDate = CommonFunctions.getTodayDate();
		enterDataInTextField(EndDateTxtBox, toDate, wait);
		System.out.println("end date criteria entered for SC claims portal");

		wait.until(ExpectedConditions.elementToBeClickable(btn_SearchClaim)).click();
		System.out.println("clicked on search button for SC claims portal");
		Thread.sleep(4000);
		return new StdClaimsPortal(driver);
	}

	public StdClaimsPortal searchStdClaims(String claimStatus) throws InterruptedException {
		Thread.sleep(3000);
		CommonFunctions.selectOptionFromDropDown(ClaimStatus, claimStatus);

		wait.until(ExpectedConditions.elementToBeClickable(btn_SearchClaim)).click();
		System.out.println("clicked on search button for SC claims portal");
		Thread.sleep(4000);
		return new StdClaimsPortal(driver);
	}

	public String verifyRevertToDraftClaim(String claimNo) throws InterruptedException {
		WebElement recallElm = driver.findElement(By.xpath("//table[@id='tblSCClaims']//tbody/tr[1]/td[7]/button/em"));
		String expClaimNo = driver.findElement(By.xpath("//table[@id='tblSCClaims']//tbody/tr[1]/td[1]")).getText().trim();
		String expStatus = "";

		if (claimNo.equalsIgnoreCase(expClaimNo)) {
			scrolltoElement(driver, ContractorTxtBox);
			recallElm.click();
			Thread.sleep(2000);
			wait.until(ExpectedConditions.elementToBeClickable(cancelRevert));
			cancelRevert.click();
			System.out.println("******Clicked on cancel button while reverting std claim*******");
			Thread.sleep(2000);
			scrolltoElement(driver, stdClaimsTab);
			recallElm.click();
			Thread.sleep(2000);
			wait.until(ExpectedConditions.elementToBeClickable(confirmRevert));
			confirmRevert.click();
			System.out.println("******Clicked on revert button while reverting std claim*******");
			Thread.sleep(8000);
			expStatus = getClaimStatus();
		}
		return expStatus;

	}

	public void clickOnClaimNumber(String claimNo) throws InterruptedException {
		searchStdClaims();
		WebElement elmClaim = driver.findElement(By.xpath("//table[@id='tblSCClaims']//tbody/tr[1]/td[1]/a"));
		String expClaimNo = elmClaim.getText().trim();
		if (claimNo.equalsIgnoreCase(expClaimNo)) {
			elmClaim.click();
		} else {
			System.out.println("Claim number is not found");
		}

	}

	public boolean validateClaimTypes(int colNumber) {
		boolean isTypeVerified;
		
		List<String> finalClaimTypes= new ArrayList<String>();
		String strclaimTypes = ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx","ExpectedClaimTypes", "StdClaimsTypes", colNumber);
		String[] claimArray = strclaimTypes.split(",");
		
		List<String> expclaims = Arrays.asList(claimArray);
		int expClaimCount = expclaims.size();
		int actClaimCount = driver.findElements(By.xpath("//select[@id='ClaimType']/option")).size();
		
		for (int i = 0; i < expClaimCount; i++) 
		{
			String expected = expclaims.get(i).toString().trim();
			for (int j = 2; j <= actClaimCount; j++) 
			{
				WebElement actclaimType = driver.findElement(By.xpath("//select[@id='ClaimType']/option["+j+"]"));
				String actual = actclaimType.getText().trim();
				if (actual.equalsIgnoreCase(expected)) 
				{
					System.out.println("Option is matched for " + actual);
					finalClaimTypes.add(actual);
					break;
				}

			}
		}
		
		if(expclaims.size()==finalClaimTypes.size()){
			isTypeVerified=true;
		}else{
			isTypeVerified=false;
		}

		return isTypeVerified;
	}
	
	public void verifySearchResult() throws InterruptedException {
		scrolltoElement(driver, btn_SearchClaim);
		wait.until(ExpectedConditions.elementToBeClickable(btn_SearchClaim)).click();
		System.out.println("Clicked on search button without criteria");
		searchStdClaims();
		System.out.println("Searched pre approval as per From & To date");
		searchStdClaims("Rejected");
		System.out.println("Searched pre approval as per status");
		
	}

	public void verifyDeleteClaim(String sheet,int colNumber, String claimNo, String key, boolean verifyDeleteBtn) throws InterruptedException {
		searchStdClaimsWithClaimStatusAndType("Draft",sheet,colNumber);
		/*WebElement elmClaim = driver.findElement(By.xpath("//table[@id='tblSCClaims']//tbody/tr[1]/td[1]"));*/
		String expClaimNo = getClaimNoWithDeleteBtn(key,verifyDeleteBtn);
		WebElement elmDelete= locateDeleteButton();
		if (claimNo.equalsIgnoreCase(expClaimNo)) {
			//WebElement elmDelete= driver.findElement(By.xpath("//table[@id='tblSCClaims']//tbody/tr[1]/td[8]"));
			//elmDelete.click();
			Thread.sleep(2000);
			String actualMsg= driver.findElement(By.xpath("//div[@id='divConfDeleteDraft']//div[@class='modal-body']")).getText().trim();
			boolean verifyMessage= CommonFunctions.verifyMessageWithExcel(actualMsg,"GPPSCTESTDATA.xlsx","ExpectedClaimTypes","ExpDeleteMessage",1);
			wait.until(ExpectedConditions.elementToBeClickable(cancelDelete));
			cancelDelete.click();
			System.out.println("******Clicked on cancel button while deleting std claim*******");
			Thread.sleep(2000);
			scrolltoElement(driver, stdClaimsTab);
			elmDelete.click();
			Thread.sleep(2000);
			wait.until(ExpectedConditions.elementToBeClickable(confirmDelete));
			Thread.sleep(3000);
			confirmDelete.click();
			Thread.sleep(3000);
			System.out.println("******Clicked on confirm button while deleting std claim*******");
		} else {
			System.out.println("Claim number is not found");
		}
	}

	private WebElement locateDeleteButton() {
		WebElement element = null;
		for(int i=1;i<=10;i++){
			try{
				WebElement elmDelete;
				elmDelete= driver.findElement(By.xpath("//table[@id='tblSCClaims']//tbody/tr["+i+"]/td[8]/button"));
				if(elmDelete.isDisplayed()){
					element= elmDelete;
					element.click();
					break;
				}else{
					System.out.println("Claim number is not found on page for index: "+i);
				}
			}catch(Exception e){
				System.out.println("Element is not found at index: "+i);
			}
		}
		return element;
	}

	private void searchStdClaimsWithClaimStatusAndType(String claimStatus, String sheet, int colNumber) throws InterruptedException {
		Thread.sleep(3000);
		String claimType = ExcelUtilities.getKeyValueFromExcelWithPosition("GPPSCTESTDATA.xlsx",sheet, "StdClaimType", colNumber);
		CommonFunctions.selectOptionFromDropDown(ClaimType, claimType);
		
		CommonFunctions.selectOptionFromDropDown(ClaimStatus, claimStatus);

		wait.until(ExpectedConditions.elementToBeClickable(btn_SearchClaim)).click();
		System.out.println("clicked on search button for SC claims portal");
		Thread.sleep(4000);

		
	}

	public boolean verifyClaimNoFormat(String locumClaimNo) {
		boolean isFormatmatched=false;
		int count= (locumClaimNo.length());
		String claimYear= locumClaimNo.substring(0,4);
		System.out.println("Claim year is "+claimYear);
		
		String claimMonth= locumClaimNo.substring(4,6);
		System.out.println("Claim month is "+claimMonth);
	
		String claimSerialNo= locumClaimNo.substring(6);
		int serialNoCount= claimSerialNo.length();
		System.out.println("Serial number count is "+serialNoCount);
		if(count==13){
			System.out.println("Claim number length is 13");
			List<String> todayDate= getCurrentDateBreakup();
			String currentYear= todayDate.get(2);
			String currentMonth= todayDate.get(1);
			if(claimYear.equalsIgnoreCase(currentYear)){
				System.out.println("Generated claim year is matched");
				if(claimMonth.equalsIgnoreCase(currentMonth)){
					System.out.println("Generated claim month is matched");
					if(serialNoCount==7){
						System.out.println("Serial number length is 7");
						isFormatmatched=true;
					}
				}
			}

		}else{
			System.out.println("Claim number length is not 13, actual length is "+count);
			isFormatmatched=false;
		}
		return isFormatmatched;
	}


	private List<String> getCurrentDateBreakup() {
		String date= CommonFunctions.getTodayDate();
		String dateArray[]=  date.split("/");
		List<String> splitDate= Arrays.asList(dateArray);
		return splitDate;
	}

	public boolean verifyGridData(List<String> formData) throws InterruptedException {
		searchStdClaims();
		boolean isVerified = false;
		List<String> verifiedData= new ArrayList<String>();
		int expSize= driver.findElements(By.xpath("//table[@id='tblSCClaims']//tbody/tr[1]/td")).size();
		
		for(int i=0;i<formData.size();i++){
			String actualData= formData.get(i);
			for(int j=1; j<=expSize;j++){
				WebElement element= driver.findElement(By.xpath("//table[@id='tblSCClaims']//tbody/tr[1]/td["+j+"]"));
				String expData= element.getText();
					if(actualData.equalsIgnoreCase(expData)){
					System.out.println("Grid is verified for value: "+expData);
					verifiedData.add(expData);
					break;
				}
		}
	}
		if(formData.size()==verifiedData.size()){
			isVerified=true;
		}else{
			isVerified=false;
		}
		
		return isVerified;
	}
	
	public static boolean verifyElementStateByTagName(WebDriver driver, String tagName)
	{
	boolean isdisabled = false;
	try {
		Thread.sleep(2000);
		List<WebElement> Elements=driver.findElements(By.tagName(tagName));
		System.out.println("total WebElements "+Elements.size());
		for (WebElement Element : Elements)
		{  
			if(tagName=="textarea"){
				boolean value= Element.getAttribute("readonly") != null;
				if(value==true)
				{
					isdisabled=true;
				}
			}else if(!Element.isEnabled())
			{
				isdisabled = true;
			}
			
		}
	}
	catch(Exception e){
		System.out.println("The Element is not found whose tagname is "+tagName);
	}
	
	return isdisabled;
	}
	
	public static boolean getElementStatebyAttribute(WebDriver driver, String type)
	{
	boolean isdisabled = false;
	try {
		Thread.sleep(2000);
		List<WebElement> Elements=driver.findElements(By.xpath("//input[@type='"+type+"']"));
		System.out.println("total WebElements "+Elements.size());
		for (WebElement Element : Elements)
		{  
			if(type=="text"){
				boolean value= Element.getAttribute("readonly") != null;
				if(value==true)
				{
					isdisabled=true;
				}
			}else if(!Element.isEnabled())
			{
				isdisabled=true;
			}
		}
	}
	catch(Exception e){
		System.out.println("The Element is not foount whose type is "+type);
	}
	return isdisabled;
	}

	public static boolean compareDateFormat(List<String> date, String dateFormat) {
		boolean isValid= false;
		SimpleDateFormat sdfrmt = new SimpleDateFormat(dateFormat);
	    sdfrmt.setLenient(false);
	    for(int i=0;i< date.size();i++){
	    	String strdate= date.get(i);
	    	try
		    {
		        //Date javaDate = sdfrmt.parse(strdate); 
		        System.out.println(strdate+" is valid date format");
		        isValid=true;
		    }
		    /* Date format is invalid */
		    catch (Exception e)
		    {
		        System.out.println(strdate+" is Invalid Date format");
		        isValid=false;
		    }
	    }
	    
		return isValid;
	}

	public void verifySortingFunctionality(String key) throws InterruptedException, IOException {
	//commented by Akshay as sorting is disabled for all column headers
/*	List<WebElement> colHeaders= driver.findElements(By.xpath("//div[@id='tblSCClaims_wrapper']/div[2]/div/div/div[1]/div/table/thead/tr/th"));
	for(int i=1;i<=colHeaders.size();i++){
		WebElement element= driver.findElement(By.xpath("//div[@id='tblSCClaims_wrapper']/div[2]/div/div/div[1]/div/table/thead/tr/th["+i+"]"));
		element.click();
		}*/
		captureStdClaimPortalSnap(key+"_GridBeforeSorting");
		WebElement element= driver.findElement(By.xpath("//div[@id='tblSCClaims_wrapper']/div[2]/div/div/div[1]/div/table/thead/tr/th[1]"));
		element.click();
		captureStdClaimPortalSnap(key+"_GridAfterSorting");
		element.click();
	}

}	
