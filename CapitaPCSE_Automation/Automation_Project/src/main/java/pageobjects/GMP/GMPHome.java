package pageobjects.GMP;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.CommonFunctions;
import helpers.Support;

public class GMPHome extends Support {
	WebDriver driver;
	WebDriverWait wait;
  
	@FindBy(name="bttnSubmit")
	WebElement NewGMPButton;
	
	@FindBy(name="GMPLegislativeRegion")
	WebElement LEGISLATIVEREGION;
	
	@FindBy(xpath="//div[@class='modal-body']//button")
	//@FindBy(css="button[class='btn-datepicker datepicker']")
	WebElement DatePicker;
	
	@FindBy(xpath="//*[@class='datepicker-days']/table//tbody")
	WebElement DatePickerTable;
	
	@FindBy(name="GMPPCSType")
	WebElement PCSType;
	
	@FindBy(name="GMPPaymentDate")
	WebElement GMPPaymentDateField;
	
	
	
	//@FindBy(xpath="//button[@class='btn btn-success']")
	@FindBy(xpath="//div[@class='modal-footer']/div/div[2]/button")
	WebElement CreateButton;
	
	@FindBy(css="button[class='close']")
	WebElement ModalDialogCloseButton;
	
	@FindBy(xpath="//div[@id='AjaxModalContainer']/div/div")
	WebElement ModalDialogWebElement;
	
	@FindBy(xpath="//div[@class='modal-footer']/button[@id='mySubmit']")
	WebElement ModalDialogOkButton;
	
	@FindBy(id="BtnSearch")
	WebElement SearchButton;
	
	@FindBy(xpath="//*[@id='GmpRunDataTable']/tbody/tr[1]/td[7]")
	WebElement PaymentStatus;
	
	@FindBy(xpath="//*[@id='GmpRunDataTable']/tbody/tr[1]/td[8]/a")
	WebElement Action;
	
	@FindBy(xpath="//*[@id='GmpRunDataTable']/tbody/tr[1]/td[1]/a")
	WebElement PaymentRunName;
	
	@FindBy(xpath="//div[@class='datepicker-days']//table[@class='table-condensed']/thead//tr[2]/th[2]")
	WebElement Yearchangefield;
	
	@FindBy(xpath="//div[@class='datepicker-months']//table[@class='table-condensed']/thead//tr[2]/th[2]")
	WebElement ActualYearoncalender;
	
	@FindBy(xpath="//div[@class='datepicker-months']//table[@class='table-condensed']/thead//tr[2]/th[3]")
	WebElement ForwardYear;
	
	@FindBy(xpath="//div[@class='datepicker-months']//table[@class='table-condensed']//tr[1]//td[1]")
	WebElement MonthTable;
	
	@FindBy(id="PaymentRunFromDate")
	WebElement runDateFrom;
	
	@FindBy(id="PaymentRunToDate")
	WebElement runDateTo;
	
	@FindBy(id="GmpRunDataTable_processing")
	WebElement Processingtab;
	
		
	
	
			
	
		
	public GMPHome(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		PageFactory.initElements(this.driver, this);

	}
	
	public void clickOnNewCreateGMP1() {
		
			scrolltoElement(driver, NewGMPButton);
			wait.until(ExpectedConditions.elementToBeClickable(NewGMPButton)).click();
	}


	public GMPHome clickOnNewCreateGMP(String futureDate, int colNumber) {
		//String futureDate = null;
		try{
			String DropDownValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GMPTESTDATA.xlsx","ClaimDetails","PCS Type", colNumber);
			//String Date = "07/02/2020";
			scrolltoElement(driver, NewGMPButton);
			wait.until(ExpectedConditions.elementToBeClickable(NewGMPButton)).click();
			//wait.until(ExpectedConditions.elementToBeClickable(ModalDialogWebElement));
		//	wait.until(ExpectedConditions.elementToBeClickable(ModalDialogWebElement));
			scrolltoElement(driver, LEGISLATIVEREGION);
			Select dropdown = new Select(LEGISLATIVEREGION);
			dropdown.selectByVisibleText("England");
			Thread.sleep(2000);
			Actions action = new Actions(driver);
			wait.until(ExpectedConditions.elementToBeClickable(DatePicker)).click();
			action.moveToElement(DatePicker);
			action.moveToElement(DatePicker);
			wait.until(ExpectedConditions.elementToBeClickable(DatePicker)).click();
			action.click().build().perform();
			Thread.sleep(2000);
			/*String futureDate_Default= CommonFunctions.Tomorrowdate(Date, 7,"M/dd/yyyy");
			System.out.println(futureDate_Default);
			String Day = getday(futureDate_Default);
			if(Day.equalsIgnoreCase("Sunday"))
			{
				futureDate = CommonFunctions.Tomorrowdate(futureDate_Default, 1,"M/dd/yyyy");
			}
			else if(Day.equalsIgnoreCase("Saturday"))
			{
				futureDate = CommonFunctions.Tomorrowdate(futureDate_Default, 2,"M/dd/yyyy");
			}
			else
			{
				futureDate = futureDate_Default;				
			}*/
			String date = helpers.CommonFunctions.getdate(futureDate);
			String Month = helpers.CommonFunctions.getMonth(futureDate);
			String Year = helpers.CommonFunctions.getYear(futureDate);
			String MonthRequired = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GMPTESTDATA.xlsx","Months",Month, 1);
			Thread.sleep(2000);
			wait.until(ExpectedConditions.elementToBeClickable(Yearchangefield)).click();
			Thread.sleep(2000);
			String ActualYearondatepicker = ActualYearoncalender.getText();
			while(!ActualYearondatepicker.equalsIgnoreCase(Year))
			{
				Thread.sleep(400);
				action.moveToElement(ForwardYear).build().perform();
				action.moveToElement(ForwardYear).build().perform();
				wait.until(ExpectedConditions.elementToBeClickable(ForwardYear)).click();
				System.out.println("Clicked on Forward Year arrow under create GMP window");
				Thread.sleep(3000);
				wait.until(ExpectedConditions.visibilityOf(ActualYearoncalender));
				ActualYearondatepicker = ActualYearoncalender.getText();
			}
			
			if(ActualYearondatepicker.equalsIgnoreCase(Year))
			{
				wait.until(ExpectedConditions.visibilityOf(MonthTable));
				List<WebElement> MonthsCounts = (List<WebElement>) MonthTable.findElements(By.tagName("span"));
				int TotalMonths = MonthsCounts.size();
				System.out.println(TotalMonths);
				for (WebElement MonthsCount : MonthsCounts)
				{
					wait.until(ExpectedConditions.visibilityOf(MonthsCount));
					String spanclass = MonthsCount.getAttribute("class");
					if(spanclass.equalsIgnoreCase("month") ||spanclass.equalsIgnoreCase("month focused") )
					{
						if(MonthsCount.getText().equalsIgnoreCase(MonthRequired))
						{
							Actions action1 = new Actions(driver);
							action1.moveToElement(MonthsCount);
							action1.click().build().perform();
							Thread.sleep(2000);
							break;
						}
					}
				}
				if(DatePickerTable.isDisplayed())
				{
					int count=1;
					int activerow=1;
					List<WebElement> trCount = (List<WebElement>) DatePickerTable.findElements(By.tagName("tr"));
					int TotalRows = trCount.size();
					System.out.println(TotalRows);
					
					outerloop:
					for (int a=1; a<=TotalRows;a++)
					{
						List<WebElement> tdCountx = (List<WebElement>) DatePickerTable.findElements(By.tagName("td"));
						int Totalcolumnsx = tdCountx.size();
						for(int b = 1;b<=7; b++)
						{
							WebElement Date1x = driver.findElement(By.xpath("//*[@class='datepicker-days']/table//tbody/tr["+a+"]/td["+b+"]"));
							String ClassNamex = driver.findElement(By.xpath("//*[@class='datepicker-days']/table//tbody/tr["+a+"]/td["+b+"]")).getAttribute("Class");
							if(ClassNamex.equalsIgnoreCase("day"))
							{
								activerow = a;
								break outerloop;
							}
						
						}
					}
					
					for (int i = activerow;i<=TotalRows; i++)
					{
						List<WebElement> tdCount = (List<WebElement>) DatePickerTable.findElements(By.tagName("td"));
						int Totalcolumns = tdCount.size();
						for(int j = 1;j<=7; j++)
						{
							WebElement Date1 = driver.findElement(By.xpath("//*[@class='datepicker-days']/table//tbody/tr["+i+"]/td["+j+"]"));
							String ClassName = driver.findElement(By.xpath("//*[@class='datepicker-days']/table//tbody/tr["+i+"]/td["+j+"]")).getAttribute("Class");
							if(ClassName.equalsIgnoreCase("day") && Date1.getText().equalsIgnoreCase(date))
							{
								Date1.click();
								i = TotalRows + 1;
								break;
							}else if((ClassName.contains("disabled") && Date1.getText().equalsIgnoreCase(date))){
								count= 1;
								futureDate= CommonFunctions.Tomorrowdate(futureDate, count,"M/dd/yyyy");
								Date newDueDate= CommonFunctions.convertStringtoCalDate(futureDate, "M/dd/yyyy");
								String strDueDate= CommonFunctions.convertDateToString(newDueDate, "M/dd/yyyy");
								String newDate = helpers.CommonFunctions.getdate(strDueDate);
								date= newDate;
							}
						}
					}
				}
			}
			/*Actions action = new Actions(driver);
			action.moveToElement(DatePicker);
			action.click().build().perform();*/
		/*	JavascriptExecutor jse = (JavascriptExecutor)driver; 
			jse.executeScript("arguments[0].scrollIntoView();", GMPPaymentDateField);
			jse.executeScript("arguments[0].click();", GMPPaymentDateField);
			jse.executeScript("arguments[0].setAttribute('value', '07/02/2018')",GMPPaymentDateField);*/
			/*WebElement uploadWindow = driver.switchTo().activeElement();
			uploadWindow.sendKeys("07/02/2018");
			GMPPaymentDateField.sendKeys("07/02/2018");*/
			
		/*	wait.until(ExpectedConditions.elementToBeClickable(DatePicker)).click();
			WebElement uploadWindow = driver.switchTo().activeElement();
			uploadWindow.sendKeys("07/02/2018");*/
		/*	if(DatePickerTable.isDisplayed())
			{
				List<WebElement> trCount = (List<WebElement>) DatePickerTable.findElements(By.tagName("tr"));
				int TotalRows = trCount.size();
				System.out.println(TotalRows);
				for (int i = 1;i<=TotalRows; i++)
				{
					List<WebElement> tdCount = (List<WebElement>) DatePickerTable.findElements(By.tagName("td"));
					int Totalcolumns = tdCount.size();
					for(int j = 1;j<=7; j++)
					{
						WebElement Date = driver.findElement(By.xpath("//*[@class='datepicker-days']/table//tbody/tr["+i+"]/td["+j+"]"));
						String ClassName = driver.findElement(By.xpath("//*[@class='datepicker-days']/table//tbody/tr["+i+"]/td["+j+"]")).getAttribute("Class");
						if(ClassName.equalsIgnoreCase("day"))
						{
							Date.click();
							i = TotalRows + 1;
							break;
						}
					}
				}
			}*/
			scrolltoElement(driver, PCSType);
			Select PCSTypeDropdown = new Select(PCSType);
			PCSTypeDropdown.selectByVisibleText(DropDownValue);
			
			scrolltoElement(driver, CreateButton);
			wait.until(ExpectedConditions.elementToBeClickable(CreateButton)).click();
			Thread.sleep(6000);
			/*Thread.sleep(5000);
			wait.until(ExpectedConditions.elementToBeClickable(CreateButton))*/
			
			if(ModalDialogWebElement.isDisplayed())
			{
				System.out.println("Modal Window to display Modal Run has been successfully initiated is displayed");
				wait.until(ExpectedConditions.elementToBeClickable(ModalDialogOkButton)).click();
			}
			Thread.sleep(7000);
			
		}
		   catch(Exception e)
        {
               System.out.println("The create new GMP button is not clicked" + e);
               
        }
        
		return new GMPHome(driver);
	}
	
	public String getday(String futureDate) {
		String day = null;
		try{
			System.out.println(futureDate);
			String date = helpers.CommonFunctions.getdate(futureDate);
			String Month = helpers.CommonFunctions.getMonth(futureDate);
			String Year = helpers.CommonFunctions.getYear(futureDate);
			String MonthRequired = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("GMPTESTDATA.xlsx","Months",Month, 1);
			Thread.sleep(2000);
			wait.until(ExpectedConditions.elementToBeClickable(Yearchangefield)).click();
			Thread.sleep(2000);
			String ActualYearondatepicker = ActualYearoncalender.getText();
			while(!ActualYearondatepicker.equalsIgnoreCase(Year))
			{
				wait.until(ExpectedConditions.elementToBeClickable(ForwardYear)).click();
				Thread.sleep(3000);
				ActualYearondatepicker = ActualYearoncalender.getText();
			}
			
			if(ActualYearondatepicker.equalsIgnoreCase(Year))
			{
				List<WebElement> MonthsCounts = (List<WebElement>) MonthTable.findElements(By.tagName("span"));
				int TotalMonths = MonthsCounts.size();
				System.out.println(TotalMonths);
				for (WebElement MonthsCount : MonthsCounts)
				{
					String spanclass = MonthsCount.getAttribute("class");
					if(spanclass.equalsIgnoreCase("month") ||spanclass.equalsIgnoreCase("month focused") )
					{
						if(MonthsCount.getText().equalsIgnoreCase(MonthRequired))
						{
							Actions action1 = new Actions(driver);
							action1.moveToElement(MonthsCount);
							action1.click().build().perform();
							Thread.sleep(2000);
							break;
						}
					}
				}
				
				if(DatePickerTable.isDisplayed())
				{
					int count=1;
					int TotalRows = 0;
					List<WebElement> trCount = (List<WebElement>) DatePickerTable.findElements(By.tagName("tr"));
					TotalRows = trCount.size();
					System.out.println(TotalRows);
					outerloop:	for (int i = 1;i<=TotalRows; i++)
					{
						List<WebElement> tdCount = (List<WebElement>) DatePickerTable.findElements(By.tagName("td"));
						int Totalcolumns = tdCount.size();
						for(int j = 1;j<=7; j++)
						{
							WebElement Date1 = driver.findElement(By.xpath("//*[@class='datepicker-days']/table//tbody/tr["+i+"]/td["+j+"]"));
							System.out.println(Date1.getText());
							String ClassName = driver.findElement(By.xpath("//*[@class='datepicker-days']/table//tbody/tr["+i+"]/td["+j+"]")).getAttribute("Class");
							
							if(Date1.getText().equalsIgnoreCase(date))
							{
								String ColumnNumnber = String.valueOf(j);
								switch(ColumnNumnber) 
								{
									case "1":
										day = "Sunday";
										break outerloop;
									case "2":
										day = "Monday";
										break outerloop;
									case "3":
										day = "Tuesday";
										break outerloop;
									case "4":
										day = "Wednesday";
										break outerloop;
									case "5":
										day = "Thursday";
										break outerloop;
									case "6":
										day = "Friday";
										break outerloop;
									case "7":
										day = "Saturday";
										break outerloop;
										
								}
							}
						}
					}
			}
	
			
			}
		}
		   catch(Exception e)
        {
               System.out.println("The create new GMP button is not clicked" + e);
               
        }
        
		return day;
	}
	
	

	public VarianceReports VerifyPaymentstatus() {
		try{
			helpers.Support.waitTillProcessing(driver);
			while(!(PaymentStatus.getText().equalsIgnoreCase("Ready For Payment")))
			{	
				enterFilterData();
				helpers.Support.waitTillProcessing(driver);
				wait.until(ExpectedConditions.elementToBeClickable(SearchButton)).click();
				Thread.sleep(3000);
			}
			wait.until(ExpectedConditions.elementToBeClickable(PaymentRunName)).click();
			
		}
		   catch(Exception e)
        {
               System.out.println("The Payment status is not cpatured" + e);
               
        }
		return new VarianceReports (driver);
	}

	public GMPHome clickonSearch(WebDriver driver) {
		String ProcessingTab = null;
		try{
			Thread.sleep(4000);
			enterFilterData();
			
			wait.until(ExpectedConditions.elementToBeClickable(SearchButton)).click();
			helpers.Support.waitTillProcessing(driver);
		
		}
		   catch(Exception e)
        {
               System.out.println("The Search button is not clicked" + e);
               
        }
		return new GMPHome(driver);
	}

	private void enterFilterData() {
		String date= CommonFunctions.getTodayDate();
		scrolltoElement(driver, runDateFrom);
		wait.until(ExpectedConditions.elementToBeClickable(runDateFrom)).click();
		runDateFrom.clear();
		runDateFrom.sendKeys(date);
		scrolltoElement(driver, runDateTo);
		wait.until(ExpectedConditions.elementToBeClickable(runDateTo)).click();
		runDateTo.clear();
		runDateTo.sendKeys(date);
	}

	public String getPaymentRunName() {
		WebElement GMPName= driver.findElement(By.xpath("//*[@id='GmpRunDataTable']//tbody/tr[1]/td[1]"));
		String name=GMPName.getText().toString();
		return name;
	}
	
	

}
