package pageobjects.PerformerPL;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

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

import helpers.CommonFunctions;
import helpers.Screenshot;
import helpers.Support;
import testdata.ConfigurationData;
import utilities.ExcelUtilities;
public class Localoffice extends Support {
	
	WebDriver driver;
	WebDriverWait wait;
	
	String ErrorLocaloffice = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerList.xlsx", "LocalOffice", "Error1",1);
	
	@FindBy(xpath="//button[@id='EditLocalOfficeEmploymentDetails']")
	WebElement EditLocaloffice;
	
	
	@FindBy(xpath="//*[@id='LocalOfficeEmploymentDetails_0__ProposedEndDate']")
	WebElement EndDateFirstLocaloffice;
	
	@FindBy(id="LocalOfficeEmploymentDetails[0].ProposedLevelOfCommmitment")
	WebElement LeavinglocalOfficecommitement;
	
	
	@FindBy(id="btnAddLocalOffice")
	WebElement AddNewLocaloffice;
	
	@FindBy(xpath="//select[@id='LocalOfficeCode']")
	WebElement SelectLocalOfficeCode;
	
	@FindBy(id="StartDate")
	WebElement StartDate;
	
	@FindBy(id="LevelOfCommitment")
	WebElement Commitment;
	
	@FindBy(name="btnAddEditOfficeRecord")
	WebElement AddOffice;
	
	@FindBy(name="Submit_Decision")
	WebElement Submit;
	
	@FindBy(xpath="//a[@class='btn btn-success']")
	WebElement Ok_Submit;
	
	@FindBy(id="PerformerMain")
	WebElement HomeMaintab;
	
	@FindBy(xpath="//span[@class='field-validation-error']")
	WebElement ErrorMessage;
	
	
	
	String localOfficeCode = ConfigurationData.NewlocalOffice;
	
	
	public Localoffice(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}

	public Localoffice clickonEdit() {
		try{
		Thread.sleep(1000);
		scrolltoElement(driver, EditLocaloffice);
		Actions actions1 = new Actions(driver);
    	actions1.moveToElement(EditLocaloffice);
    	actions1.doubleClick().build().perform();
    	helpers.CommonFunctions.PageLoadExternalwait(driver);
		}
		catch(Exception e)
		{
			System.out.println("The Edit on local office is not clicked");
		}
		return new Localoffice(driver);
	}

	public Localoffice RemoveAddoffice() {
		try{
			Thread.sleep(2000);
			List<WebElement> Radiobuttons=driver.findElements(By.xpath("//div[@class='col-xs-12 col-sm-12']//div[@class='radio']"));
			System.out.println("total Radio buttons "+Radiobuttons.size());
			for (WebElement Radiobutton : Radiobuttons)
			{                   
				JavascriptExecutor jse = (JavascriptExecutor)driver;            	
				jse.executeScript("arguments[0].scrollIntoView();", Radiobutton);
				String RadioValue = Radiobutton.getText(); 

				System.out.println(RadioValue);
				if (RadioValue.equalsIgnoreCase("Yes"))
				{
					Radiobutton.click();
					break;
				}
			}
			scrolltoElement(driver, EndDateFirstLocaloffice);
			wait.until(ExpectedConditions.elementToBeClickable(EndDateFirstLocaloffice)).clear();
			String Date = helpers.CommonFunctions.getDate_UK();
			wait.until(ExpectedConditions.elementToBeClickable(EndDateFirstLocaloffice)).sendKeys(Date);
			//wait.until(ExpectedConditions.elementToBeClickable(LeavinglocalOfficecommitement)).clear();
			//wait.until(ExpectedConditions.elementToBeClickable(LeavinglocalOfficecommitement)).sendKeys("777");
			
		}
		catch(Exception e)
		{
			System.out.println("The End date is not added properly");
		}
		return new Localoffice(driver);
	}

	public Localoffice AddnewLocaloffice(String environment) {
		try{
			helpers.CommonFunctions.ClickOnRadioButton("Yes", driver);
			Thread.sleep(2000);
			List<WebElement> Radiobuttons=driver.findElements(By.xpath("//div[@class='col-xs-12 col-sm-8']//div[@class='radio']"));
			System.out.println("total Radio buttons "+Radiobuttons.size());
			for (WebElement Radiobutton : Radiobuttons)
			{                   
				JavascriptExecutor jse = (JavascriptExecutor)driver;            	
				jse.executeScript("arguments[0].scrollIntoView();", Radiobutton);
				String RadioValue = Radiobutton.getText(); 

				System.out.println(RadioValue);
				if (RadioValue.equalsIgnoreCase("Yes"))
				{
					Radiobutton.click();
					break;
				}
			}
			wait.until(ExpectedConditions.elementToBeClickable(AddNewLocaloffice));
			scrolltoElement(driver, AddNewLocaloffice);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(AddNewLocaloffice);
	    	actions1.doubleClick().build().perform();
	    	String localOfficeCode1 = testdata.ConfigurationData.getRefDataDetails(environment, "NewLocaloffice");
	    	CommonFunctions.selectOptionFromDropDownByValue(SelectLocalOfficeCode, localOfficeCode1);
	    	String Date = helpers.CommonFunctions.getDate_UK();
			wait.until(ExpectedConditions.elementToBeClickable(StartDate)).sendKeys(Date);
			wait.until(ExpectedConditions.elementToBeClickable(Commitment)).clear();
			Thread.sleep(500);
			wait.until(ExpectedConditions.elementToBeClickable(Commitment)).sendKeys("777");
			scrolltoElement(driver, AddOffice);
			Actions actions = new Actions(driver);
	    	actions.moveToElement(AddOffice);
	    	actions.doubleClick().build().perform();
			
		}
		catch(Exception e)
		{
			System.out.println("The New local office is not added sucessfully");
		}
		return new Localoffice(driver);
	}
	
	public Localoffice clickonSubmit() {
		try{
			scrolltoElement(driver, Submit);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(Submit);
	    	actions1.doubleClick().build().perform();
		}
		catch(Exception e)
		{
			System.out.println("The Submit button is not clicked");
		}
		return new Localoffice(driver) ;
	}
	
	public Localoffice clickonOK_Submit() {
		try{
			Thread.sleep(2000);
			scrolltoElement(driver, Ok_Submit);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(Ok_Submit);
	    	actions1.doubleClick().build().perform();
	    	boolean ispresent1 = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
			while(ispresent1)
			{
				Thread.sleep(2000);
				ispresent1 = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;
			}
		}
		catch(Exception e)
		{
			System.out.println("The Cancel button on Submit ");
		}
		return new Localoffice (driver);
	}
	
	public HomeTab clickonHometab() {
		try{
			Thread.sleep(2000);
			scrolltoElement(driver, HomeMaintab);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(HomeMaintab);
	    	actions1.doubleClick().build().perform();
	    	helpers.Support.PageLoadExternalwait_Performer(driver);
			
		}
		catch(Exception e)
		{
			System.out.println("The Home tab is not clicked");
		}
		return new HomeTab(driver);
	}

	public boolean verifyErrormessage() {
		boolean ErrorActualmessaage = false;
		try{
			Thread.sleep(2000);
			String ActualErrormessage = ErrorMessage.getText();
			if(ActualErrormessage.equalsIgnoreCase(ErrorLocaloffice))
			{
				ErrorActualmessaage = true;
			}
		}
		catch(Exception e)
		{
			System.out.println("The Local office error message is not captured");
		}
		return ErrorActualmessaage;
	}

	public void Screenshot(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, HomeMaintab);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		
	}

	public Localoffice LinkedNewLocaloffice(String Value) {
		try{
			List<WebElement> Radiobuttons=driver.findElements(By.xpath("//div[@class='col-xs-12 col-sm-8']//div[@class='radio']"));
			System.out.println("total Radio buttons "+Radiobuttons.size());
			for (WebElement Radiobutton : Radiobuttons)
			{                   
				JavascriptExecutor jse = (JavascriptExecutor)driver;            	
				jse.executeScript("arguments[0].scrollIntoView();", Radiobutton);
				String RadioValue = Radiobutton.getText(); 

				System.out.println(RadioValue);
				if (RadioValue.equalsIgnoreCase(Value))
				{
					Radiobutton.click();
					break;
				}
			}
			
		}
		catch(Exception e)
		{
			System.out.println("The Linked new local option is not clicked");
		}
		return new Localoffice(driver) ;
	}

}
