package pageobjects;

import java.text.ParseException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.CommonFunctions;
import helpers.WindowHandleSupport;
import pageobjects.TraineeOptometrist;
import testdata.ConfigurationData;

public class TraineeOptometristResults {
	
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(xpath="//a[contains(@id, 'oph_traineeoptometrist.NewRecord-Large')]")
	WebElement createTraineeOptometristButton;
	
	@FindBy(css="div#oph_gocnumber")
	WebElement GOCNumberDiv;
	
	@FindBy(xpath="//div[@id='oph_gocnumber']//input[@id='oph_gocnumber_i']")
	WebElement GOCNumberTxtField;
	
	@FindBy(xpath="//div[@id='oph_name']")
	WebElement ophNameDiv;
	
	@FindBy(xpath="//div[@id='oph_name']//input[@id='oph_name_i']")
	WebElement ophNameTxtField;
	
	@FindBy(xpath="//div[@id='oph_familyname']")
	WebElement familyNameDiv;
	
	@FindBy(xpath="//div[@id='oph_familyname']//input")
	WebElement familyNametxtField;
	
	@FindBy(css="div[id='oph_contratorcode']")
	WebElement contCodeDiv;
	
	@FindBy(xpath="//div[@id='oph_contratorcode']//input")
	WebElement contCodeTxtField;
	
	@FindBy(xpath="//div[@id='oph_enddateoftrainingatthispractice']")
	WebElement endDateDiv;
	
	@FindBy(xpath="//div[@id='oph_enddateoftrainingatthispractice']//input")
	WebElement endDateTxtField;
	
	@FindBy(xpath="//div[@id='oph_startdateoftrainingatthispractice']")
	WebElement startDateDiv;
	
	@FindBy(xpath="//div[@id='oph_startdateoftrainingatthispractice']//input")
	WebElement startDateTxtField;
	
	@FindBy(css="div[id='oph_gocnumberofsupervisor']")
	WebElement supGOCDiv;
	
	@FindBy(xpath="//div[@id='oph_gocnumberofsupervisor']//input")
	WebElement supGOCTxtField;
	
	@FindBy(css="div[id='oph_givennamesofsupervisor']")
	WebElement supNameDiv;
	
	@FindBy(xpath="//div[@id='oph_givennamesofsupervisor']//input")
	WebElement supNameTxtField;
	
	@FindBy(css="div[id='oph_familynameofsupervisor']")
	WebElement supLastNameDiv;
	
	@FindBy(xpath="//div[@id='oph_familynameofsupervisor']//input")
	WebElement supLastNameTxtField;
	
	@FindBy(css="div[id='crmRibbonManager']")
	WebElement crmRibbon;
	
	@FindBy(xpath="//div[@id='crmRibbonManager']//li[1]/span")
	WebElement saveButton;
	
	public TraineeOptometristResults(WebDriver driver){

		this.driver = driver;
		//driver.switchTo().frame("resultFrame");

		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}
	
	public TraineeOptometrist clickOnCreateTraineeOptometrist() throws InterruptedException
	{
		Thread.sleep(2000);
		wait.until(ExpectedConditions.elementToBeClickable(createTraineeOptometristButton)).click();
		Thread.sleep(2000);
		return new TraineeOptometrist(driver);
		
	}

	public String createTraineeOptometrist(String environment) throws InterruptedException, ParseException {
		String value= "";
		WindowHandleSupport.getRequiredWindowDriver(driver, "Trainee Optometrist");
		driver.switchTo().defaultContent();
		driver.switchTo().frame("contentIFrame0");
		wait.until(ExpectedConditions.elementToBeClickable(GOCNumberDiv)).click();
		wait.until(ExpectedConditions.elementToBeClickable(GOCNumberTxtField)).clear();
		String randomNumber= CommonFunctions.generateRandomNo(4);
		value= "GP"+randomNumber;
		GOCNumberTxtField.sendKeys(value);
		
		wait.until(ExpectedConditions.elementToBeClickable(ophNameDiv)).click();
		wait.until(ExpectedConditions.elementToBeClickable(ophNameTxtField)).clear();
		value= "AutoTrainee"+randomNumber;
		ophNameTxtField.sendKeys(value);
				
		wait.until(ExpectedConditions.elementToBeClickable(familyNameDiv)).click();
		wait.until(ExpectedConditions.elementToBeClickable(familyNametxtField)).clear();
		familyNametxtField.sendKeys("Optometrist");
		
		String contractorCode= ConfigurationData.getRefDataDetails(environment, "OPContractorCode");
		wait.until(ExpectedConditions.elementToBeClickable(contCodeDiv)).click();
		wait.until(ExpectedConditions.elementToBeClickable(contCodeTxtField)).clear();
		contCodeTxtField.sendKeys(contractorCode);
		
		String startDate= CommonFunctions.getFirstDayOfYear("dd/MM/yyyy", 0);
		String endDate= CommonFunctions.getLastDayOfYear("dd/MM/yyyy", 0);
		
		wait.until(ExpectedConditions.elementToBeClickable(endDateDiv)).click();
		wait.until(ExpectedConditions.elementToBeClickable(endDateTxtField)).clear();
		endDateTxtField.sendKeys(endDate);
		
		wait.until(ExpectedConditions.elementToBeClickable(startDateDiv)).click();
		wait.until(ExpectedConditions.elementToBeClickable(startDateTxtField)).clear();
		startDateTxtField.sendKeys(startDate);
		
		String performerNumber= ConfigurationData.getRefDataDetails(environment, "OPPerformerNumber");
		wait.until(ExpectedConditions.elementToBeClickable(supGOCDiv)).click();
		wait.until(ExpectedConditions.elementToBeClickable(supGOCTxtField)).clear();
		supGOCTxtField.sendKeys(performerNumber);
		
		String supName= ConfigurationData.getRefDataDetails(environment, "OpthoPerformer");
		String[]supNameArray= supName.split(" ");
		String firstName= supNameArray[0].toString();
		String familyName= supNameArray[1].toString();
		
		wait.until(ExpectedConditions.elementToBeClickable(supNameDiv)).click();
		wait.until(ExpectedConditions.elementToBeClickable(supNameTxtField)).clear();
		supNameTxtField.sendKeys(firstName);
		
		wait.until(ExpectedConditions.elementToBeClickable(supLastNameDiv)).click();
		wait.until(ExpectedConditions.elementToBeClickable(supLastNameTxtField)).clear();
		supLastNameTxtField.sendKeys(familyName);
		
		driver.switchTo().defaultContent();
		wait.until(ExpectedConditions.elementToBeClickable(crmRibbon)).click();
		wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
		Thread.sleep(3000);
		driver.close();
		WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
		return value;
	}

}
