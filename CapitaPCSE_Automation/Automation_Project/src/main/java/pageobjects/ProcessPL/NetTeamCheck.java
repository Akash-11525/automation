package pageobjects.ProcessPL;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
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
public class NetTeamCheck extends Support {

	WebDriver driver;
	WebDriverWait wait;	
	
	@FindBy(id="ddlNetTeamSelected")
	WebElement NetTeamResponsible;
	
	@FindBy(id="btnConfirmAppointment")
	WebElement ConfirmAppointment;
	
	@FindBy(id="btnManualAddress")
	WebElement ManuallyAddressbutton;
	
	@FindBy(id="ResidentialAddress_AddressLine1")
	WebElement Address1;
	
	@FindBy(id="ResidentialAddress_City")
	WebElement City;
	
	@FindBy(id="ResidentialAddress_Postcode")
	WebElement PostCode;
	
	@FindBy(xpath="//button[@id='ResiAddress']")
	WebElement SaveOnAddress;
	
	@FindBy(id="AppointmentDate")
	WebElement AppointmentDatefield;
	
	@FindBy(id="AppointmentTime")
	WebElement AppointmentTimeField;
	
	@FindBy(id="LocationSelected")
	WebElement AppointmentLocation;	

	@FindBy(name="btnCancelAppointment")
	WebElement CancelAppointment;
	
	@FindBy(id="FaceToFaceChecks")
	WebElement FaceToFaceApp;
	
	@FindBy(id="NetTeamAppointment")
	WebElement NetTeamCheck;
	
	@FindBy(xpath="//div[@class='display-flex search']//input[@placeholder='PostCode']")
	WebElement AddressTextbox;
	
	@FindBy(id="BtnsearchRecAdd")
	WebElement SearchButton;	

	@FindBy(xpath="//ul[@class='results']/li")
	WebElement FirstAddress;
	
	
	
	//String AppointDate = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "NetTeamAppointment", "AppointmentDate",1);
	String AppointDate = CommonFunctions.getTodayDate();
	String AppointTime = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "NetTeamAppointment", "AppointmentTime",1);
	String AppointLocation = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "NetTeamAppointment", "AppointmentLocation",1);
	String NetResponsible = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "NetTeamAppointment", "NetTeamResponsible",1);
	String Address1_Process = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "NetTeamAppointment", "ADDRESS1",1);
	String CITY_Process = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "NetTeamAppointment", "CITY",1);
	String PostalCode_Process = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("ProcessPortal.xlsx", "NetTeamAppointment", "POSTALCODE",1);
	
	
	public NetTeamCheck(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}
	

	public NetTeamCheck FillNetTeamAppointment() {
		try{
			Thread.sleep(1000);
			scrolltoElement(driver, AppointmentDatefield);
			wait.until(ExpectedConditions.elementToBeClickable(AppointmentDatefield));
			wait.until(ExpectedConditions.elementToBeClickable(AppointmentDatefield)).clear();
			Thread.sleep(1000);
			wait.until(ExpectedConditions.elementToBeClickable(AppointmentDatefield)).sendKeys(AppointDate);
			Thread.sleep(2000);
			wait.until(ExpectedConditions.elementToBeClickable(AppointmentDatefield)).sendKeys(Keys.TAB);
			Thread.sleep(1000);
			wait.until(ExpectedConditions.elementToBeClickable(AppointmentTimeField)).clear();
			Thread.sleep(1000);
			wait.until(ExpectedConditions.elementToBeClickable(AppointmentTimeField)).sendKeys(AppointTime);
			Thread.sleep(1000);
			wait.until(ExpectedConditions.elementToBeClickable(AppointmentTimeField)).sendKeys(Keys.TAB);
			
			Select dropdown = new Select(AppointmentLocation);
			List<WebElement> dropdownLists =dropdown.getOptions();
			for (WebElement dropdownList :dropdownLists)
			{
				System.out.println(dropdownList.getText());
				if(dropdownList.getText().equalsIgnoreCase(AppointLocation))
				{
					dropdownList.click();
				}
			}
			//dropdown.selectByVisibleText(AppointLocation);			
			Select dropdown1 = new Select(NetTeamResponsible);
			dropdown1.selectByVisibleText(NetResponsible);
			wait.until(ExpectedConditions.elementToBeClickable(AddressTextbox)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(AddressTextbox)).sendKeys(PostalCode_Process);
			Actions actions = new Actions(driver);
	    	actions.moveToElement(SearchButton);
	    	actions.doubleClick().build().perform();
	    	Thread.sleep(2000);
			Actions actions1 = new Actions(driver);
	    	actions1.moveToElement(FirstAddress);
	    	actions1.doubleClick().build().perform();
			
			/*Actions actions = new Actions(driver);
	    	actions.moveToElement(ManuallyAddressbutton);
	    	actions.doubleClick().build().perform();
			//wait.until(ExpectedConditions.elementToBeClickable(ManuallyAddressbutton)).click();
			wait.until(ExpectedConditions.elementToBeClickable(Address1));
			if(Address1.isDisplayed())
			{
				wait.until(ExpectedConditions.elementToBeClickable(Address1)).clear();
				wait.until(ExpectedConditions.elementToBeClickable(Address1)).sendKeys(Address1_Process);
				wait.until(ExpectedConditions.elementToBeClickable(City)).clear();
				wait.until(ExpectedConditions.elementToBeClickable(City)).sendKeys(CITY_Process);
				wait.until(ExpectedConditions.elementToBeClickable(PostCode)).clear();
				wait.until(ExpectedConditions.elementToBeClickable(PostCode)).sendKeys(PostalCode_Process);
				Actions actions1 = new Actions(driver);
		    	actions1.moveToElement(SaveOnAddress);
		    	actions1.doubleClick().build().perform();
			//	wait.until(ExpectedConditions.elementToBeClickable()).click();
				
				//helpers.CommonFunctions.ClickOnButton("Save", driver);
				//wait.until(ExpectedConditions.elementToBeClickable(SaveonResidental)).click();
				
			}*/
		
			Thread.sleep(2000);
			scrolltoElement(driver, ConfirmAppointment);
			wait.until(ExpectedConditions.elementToBeClickable(ConfirmAppointment));
			Actions actions2 = new Actions(driver);
	    	actions2.moveToElement(ConfirmAppointment);
	    	actions2.doubleClick().build().perform();
	    	helpers.CommonFunctions.PageLoadExternalwait_Performer(driver);
			
		}
		catch(Exception e)
		{
			System.out.println("The Net Team Check is not filled Properly");
		}		
		return new NetTeamCheck(driver);
	}


	public boolean verifyCancelappointment() {
		boolean isdisplayed = false;
		try
		{
			
			wait.until(ExpectedConditions.elementToBeClickable(CancelAppointment));
			scrolltoElement(driver, CancelAppointment);
			if(CancelAppointment.isDisplayed())
			{
				isdisplayed = true;
			}
			
		}
		catch(Exception e)
		{
			System.out.println("The cancel appointment is not captured");
		}
		return isdisplayed;
	}


	public FacetoFaceAppointment clickonFaceApp() {
		try{
			scrolltoElement(driver, FaceToFaceApp);
		wait.until(ExpectedConditions.elementToBeClickable(FaceToFaceApp));
		Actions actions1 = new Actions(driver);
    	actions1.moveToElement(FaceToFaceApp);
    	actions1.doubleClick().build().perform();
		}
		catch(Exception e)
		{
			System.out.println("The Click on face to face appointment is happend");
		}
		return new FacetoFaceAppointment(driver) ;
	}
	
	public NetTeamCheck clickonnetTeamcheck() {
		try{
			scrolltoElement(driver, NetTeamCheck);
			wait.until(ExpectedConditions.elementToBeClickable(NetTeamCheck));
			Actions actions = new Actions(driver);
	    	actions.moveToElement(NetTeamCheck);
	    	actions.doubleClick().build().perform();
			wait.until(ExpectedConditions.elementToBeClickable(NetTeamCheck));	
		}
		catch(Exception e)
		{
			System.out.println("The Net Team Check is not clicked");
		}
		return new NetTeamCheck(driver);
	}


	public void AppointmentScreenshot(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, AppointmentDatefield);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		
	}


	public void ScreenshotofFaceapp(String note)throws InterruptedException, IOException {
		scrolltoElement(driver, AppointmentDatefield);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		
	}


}
