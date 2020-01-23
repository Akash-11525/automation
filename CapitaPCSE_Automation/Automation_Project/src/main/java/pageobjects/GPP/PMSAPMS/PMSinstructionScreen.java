package pageobjects.GPP.PMSAPMS;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import helpers.Screenshot;
import helpers.Support;
import utilities.ExcelUtilities;

public class PMSinstructionScreen extends Support{
		WebDriver driver;
		WebDriverWait wait;	
		
		@FindBy(id="txtContractorNameCode")
		WebElement ContractorName;
		
		@FindBy(xpath="//*[@id='divFinancialYears']/span/div/button")
		WebElement FinacialYear;

		@FindBy(id="ddlSource")
		WebElement Source;
		
		@FindBy(xpath="//button[@class='btn btn-success']")
		WebElement Searchbutton;
		
		@FindBy(xpath="//ul[@id='ContractorSearchResult']//li/a")
		WebElement FirstResult;
		
		@FindBy(xpath="//div[@class='dataTables_scrollHeadInner']/table[@class='table GPP-table-QOFTAB  dataTable no-footer']//th")
		WebElement LabelResultTable;
		
		@FindBy(xpath="//table[@id='tblAnnualPaymentSearch']//tbody/tr[1]/td[9]/a")
		WebElement ViewLink;
		
		@FindBy(xpath="//div[@id='modalPaymentHistory']//div[@class='modal-content']")
		WebElement Paymentwindow;
		
		@FindBy(xpath="//div[@id='modalPaymentHistory']//button[@class='close']")
		WebElement Closewindow;
		
		@FindBy(id="ddlSelectedBaseLineYear")
		WebElement Baselinefor;
		
		
		@FindBy(xpath="//a[@id='btnRedirectToBulkEntry']")
		WebElement ChangeBaselineAnnual;
		
		@FindBy(xpath="//table[@id='tblAnnualPaymentSearch']//tbody//tr[1]//td[5]")
		WebElement AnnualContValue;
		
		
		
		String ContractorName_excel = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPMSTESTDATA.xlsx","PMSScreen","ContractorName", 1);
		String FinacialYear_excel = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPMSTESTDATA.xlsx","PMSScreen","FinacialYear", 1);
		String Source_excel = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPMSTESTDATA.xlsx","PMSScreen","Source", 1);
		String BaselineFor_excel = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPMSTESTDATA.xlsx","PMSScreen","BaselineFor", 1);
		public PMSinstructionScreen(WebDriver driver)
		{
			this.driver = driver;
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			wait = new WebDriverWait(this.driver, 80);
			PageFactory.initElements(this.driver, this);
		}




		public PMSinstructionScreen Enterpaymentinstructiondata() {
			try{
				wait.until(ExpectedConditions.visibilityOf(ContractorName));
				scrolltoElement(driver, ContractorName);
				wait.until(ExpectedConditions.elementToBeClickable(ContractorName)).sendKeys(ContractorName_excel);
			Thread.sleep(2000);
				Actions action = new Actions(driver);
				action.moveToElement(FirstResult);
				action.click().build().perform(); 
				
				Actions action2 = new Actions(driver);
				action2.moveToElement(FinacialYear);
				action2.click().build().perform();
				Thread.sleep(2000);
				List<WebElement> Finacialyears =driver.findElements(By.xpath("//div[@id='divFinancialYears']//ul[@class='multiselect-container dropdown-menu']//label"));
						
				 for (WebElement Finacialyear:Finacialyears)
				 {
					 scrolltoElement(driver, Finacialyear);
					 String FinacialYear_Portal = Finacialyear.getText();
					 int year = Calendar.getInstance().get(Calendar.YEAR);
					 String currentyear = String.valueOf(year);
					 if(!FinacialYear_excel.equalsIgnoreCase(currentyear))
					 {					 
					 if(FinacialYear_Portal.equalsIgnoreCase(FinacialYear_excel) )
					 {
						 	Actions action1 = new Actions(driver);
							action1.moveToElement(Finacialyear);
							action1.click().build().perform(); 
					 }
					 }
				  	
				 }
				 scrolltoElement(driver, Source);
				 Select dropdown = new Select(Source);
				 dropdown.selectByVisibleText(Source_excel);
				 
			}
			catch(Exception e)
			{
				System.out.println("The Payment instruction info is not filled properly");
			}
			return new PMSinstructionScreen(driver);
		}

		public List<String> ExpectedResultLabel(String Sheet ,int Position) throws IOException {
			List<String> ExpectedResultLabelList = null;
			try{
				ExpectedResultLabelList = ExcelUtilities.getCellValuesInExcel("GPPPMSTESTDATA.xlsx", Sheet, Position);
			}
			catch(NoSuchElementException e)
			{
				System.out.println("The Expected Result Label is not captured " +e);
			}
			// TODO Auto-generated method stub
			return ExpectedResultLabelList;
		}


		public int verifyResultLabel(List<String> ExpectedList,List<String> ActualList ) {
			int count = 0;
		try{
			
			
				
				for(int i=0;i<ActualList.size();i++)
				{
					if(ActualList.contains(ExpectedList.get(i)))
					{
						System.out.println("Exist : "+ExpectedList.get(i));
					}
					else 
					{
						count = 1;
						System.out.println("Does not Exist : "+ExpectedList.get(i));
					}
				}
			
		}
		catch(Exception e)
		{
			System.out.println("The result label is not captured");
		}
			return count;
		}




		public List<String> ActualResultLabel(String Sheet ,int Position) throws IOException {
			List<WebElement> ActualResultLabelList = null;
			List<String> LabelResult = null;
			try{
				
				ActualResultLabelList=driver.findElements(By.xpath("//div[@class='dataTables_scrollHeadInner']/table[@class='table GPP-table-QOFTAB  dataTable no-footer']//th"));
					System.out.println(ActualResultLabelList);
					LabelResult = new ArrayList<String>();
					 for (WebElement ResultLabel:ActualResultLabelList)
					 {
						 scrolltoElement(driver, ResultLabel);
						 String labelname = ResultLabel.getText();
						 System.out.println(labelname);
						 if(!(labelname.equalsIgnoreCase("")))
						 {
							 LabelResult.add(labelname);
						 }
						
						 
					 }
								
			}
			catch(Exception e)
			{
				System.out.println("The Actual Result Label is not captured " +e);
			}
			// TODO Auto-generated method stub
			return LabelResult;
		}




		public PMSinstructionScreen clickonView() {
			try{
				scrolltoElement(driver, ViewLink);
				Actions action1 = new Actions(driver);
				action1.moveToElement(ViewLink);
				action1.doubleClick().build().perform(); 
			}
			catch(Exception e)
			{
				System.out.println("The View button is not clicked");
			}
			return new PMSinstructionScreen(driver);
		}
		
		public List<String> ActualPaymentwindowLabel(String Sheet ,int Position) throws IOException {
			List<WebElement> ActualPaymentlabelList = null;
			List<String> LabelResult = null;
			try{
				Thread.sleep(3000);
			if(Paymentwindow.isDisplayed())	
			{
				ActualPaymentlabelList=driver.findElements(By.xpath("//table[@id='tblPMSTable2']//tr[1]//th"));
					System.out.println(ActualPaymentlabelList);
					LabelResult = new ArrayList<String>();
					 for (WebElement ActualPaymentlabel:ActualPaymentlabelList)
					 {
						 scrolltoElement(driver, ActualPaymentlabel);
						 String labelname = ActualPaymentlabel.getText();
						 System.out.println(labelname);
						 if(!(labelname.equalsIgnoreCase("")))
						 {
							 LabelResult.add(labelname);
						 }
						
						 
					 }
			}
								
			}
			catch(Exception e)
			{
				System.out.println("The Actual Payment Label is not captured " +e);
			}
			// TODO Auto-generated method stub
			return LabelResult;
		}




		public PMSinstructionScreen clickonclosewindow() {
		try{
			
			if(Paymentwindow.isDisplayed())	
			{
				scrolltoElement(driver, Closewindow);
				wait.until(ExpectedConditions.elementToBeClickable(Closewindow));
				Actions action1 = new Actions(driver);
				action1.moveToElement(Closewindow);
				action1.doubleClick().build().perform(); 
			}
		}
		catch(Exception e)
		{
			System.out.println("The close button is not captured");
			
		}
			return new PMSinstructionScreen(driver);
		}




		public Boolean verifypaymentwindow() {
			boolean paymentwindowdispalyed = false;
			
			try{
			try{
				Thread.sleep(2000);
				if(Paymentwindow.isDisplayed())
			
				{
					paymentwindowdispalyed = true;
				}
				else{
					paymentwindowdispalyed = false;
				}
			}
			catch(NoSuchElementException e)
			{
				paymentwindowdispalyed = false;
			}
				
			}
			catch(Exception e)
			{
				System.out.println("The Payment window is not captured");
			}
		
			return paymentwindowdispalyed;
		}




		public PMSinstructionScreen selectBaseline() {
			try{
				scrolltoElement(driver, Baselinefor);
				 Select dropdown = new Select(Baselinefor);
				 dropdown.selectByVisibleText(BaselineFor_excel);
			}
			catch(Exception e)
			{
				System.out.println("The Baseline for option is not captured");
			}
			return new PMSinstructionScreen(driver) ;
		}




		public BaselineEntryScreen clickonAnnualBaseline() {
			try{
				scrolltoElement(driver, ChangeBaselineAnnual);
				wait.until(ExpectedConditions.elementToBeClickable(ChangeBaselineAnnual));
				Actions action1 = new Actions(driver);
				action1.moveToElement(ChangeBaselineAnnual);
				action1.doubleClick().build().perform();
			}
			catch(Exception e)
			{
				System.out.println("The Change annual Baseline is not captured");
			}
			
			return new BaselineEntryScreen(driver) ;
		}




		public void PMSScreenshots(String note) throws InterruptedException, IOException {
			scrolltoElement(driver, ContractorName);
			Screenshot.TakeSnap(driver, note+"_1");
			Thread.sleep(1000);
			
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
			Screenshot.TakeSnap(driver, note+"_2");
			
		}




		public String getAnnualContractValue() {
			String AnnualValue = null;
			try
			{
						
				
				scrolltoElement(driver, AnnualContValue);
				wait.until(ExpectedConditions.elementToBeClickable(AnnualContValue));
				AnnualValue = AnnualContValue.getText();
			}
			catch(Exception e)
			{
				System.out.println("The Annual Value is not captured"); 
				
			}
			return AnnualValue;
		}




		public boolean verifyannualContractvalue(String value) throws InterruptedException {
			boolean annualcontVal = false;
			Thread.sleep(3000);
			String Instructiontype_excel = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPMSTESTDATA.xlsx","BaselineEntry","Instructiontype", 1);
			String NoofPatient_excel = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPMSTESTDATA.xlsx","BaselineEntry","NoofPatient", 1);
			String CustomValue_excel = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPMSTESTDATA.xlsx","BaselineEntry","CustomValue", 1);
			String AnnualValue_excel = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GPPPMSTESTDATA.xlsx","BaselineEntry","AnnualValue", 1);
			try{
				
				if(Instructiontype_excel.equalsIgnoreCase("Flat Amount"))
				{
					String ActualAnnualContractvalue = value.replaceAll("\\.0*$", "");
					if(ActualAnnualContractvalue.equalsIgnoreCase(AnnualValue_excel))
					{
						annualcontVal = true;
					}
				}
				if(Instructiontype_excel.equalsIgnoreCase("Custom"))
				{
					int NoPatient = Integer.parseInt(NoofPatient_excel);
					int CustomValue = Integer.parseInt(CustomValue_excel);
					int ExpectedAnnualcontractvalue = NoPatient * CustomValue;
									
					System.out.println("The Expected AnnualContract value is " + ExpectedAnnualcontractvalue);
					String ExpectedAnnaualValue = String.valueOf(ExpectedAnnualcontractvalue);
					String ActualAnnualContractvalue = value.replaceAll("\\.0*$", "");
					if(ActualAnnualContractvalue.equalsIgnoreCase(ExpectedAnnaualValue))
					{
						annualcontVal = true;
					}
				}
			}
			catch(Exception e)
			{
				System.out.println("The Annual Contract value is not captured");
			}
			return annualcontVal;
		}





}
