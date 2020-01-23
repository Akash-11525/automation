package pageobjects.Levies;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.CommonFunctions;
import helpers.Support;
import utilities.ExcelUtilities;

public class CreateLocalCommittee extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="txtLocalCommName")
	WebElement committeeName;
	
	@FindBy(id="txtLocalCommODSCode")
	WebElement ODSCode;
	
	@FindBy(css="select#ddlPaymentDayType")
	WebElement paymentDayType;
	
	@FindBy(css="select#ddlPaymentDay")
	WebElement paymentDayOfMonth;
	
	@FindBy(id="txtEffectiveFrom")
	WebElement effectiveFrom;
	
	@FindBy(css="button[class*='btn btn-info']")
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
	
	@FindBy(css="button[class='btn btn-success']")
	WebElement addressWindowSaveButton;
	
	@FindBy(id="txtPhoneNumber")
	WebElement telephoneTxtBox;
	
	@FindBy(id="txtEmailAddress")
	WebElement emailTxtBox;
	
	@FindBy(xpath="//div[@id='divMainContainer']/form/div[11]/div[6]/input")
	WebElement submit;
	
	@FindBy(xpath="//div[@id='divMainContainer']/form/div[11]/div[1]/button")
	WebElement cancel;
	
	//web elements for terminate committee
	@FindBy(xpath="//div[@id='divMainContainer']/form/div[13]/div[1]/button")
	WebElement terminateCommittee;
	
	@FindBy(xpath="//div[@class='modal-content']/div[3]/div[2]/input")
	WebElement confirmTerminate;
	
	@FindBy(xpath="//div[@id='divConfTerminateCommittee']/div/div/div[3]/div[1]/button")
	WebElement cancelTerminate;

	
	public CreateLocalCommittee(WebDriver driver) {
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		// This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}
	
	public void enterAddressManually(int colNumber) throws InterruptedException
	{
        try{

        	wait.until(ExpectedConditions.elementToBeClickable(enterAddressButton)).click();
            wait.until(ExpectedConditions.elementToBeClickable(AddressModalWindow));
            //to be changed as per test data sheet
            String addressline1 = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "Addressline1", colNumber);
    		String addressline2 = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "Addressline2", colNumber);
    		String addressline3 = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "Addressline3", colNumber);
    		String addresscity = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "AddressCity", colNumber);
    		String addresscountry = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "AddressCounty", colNumber);
    		String addresspostcode = ExcelUtilities.getKeyValueFromExcelWithPosition("OPGOS3TESTDATA.xlsx", "PATIENTDETAILS", "AddressPostCode", colNumber);
            if(AddressModalWindow.isDisplayed())
            {
                   wait.until(ExpectedConditions.elementToBeClickable(addressLine1)).clear();
                   wait.until(ExpectedConditions.elementToBeClickable(addressLine1)).sendKeys(addressline1);
                   wait.until(ExpectedConditions.elementToBeClickable(addressLine2)).clear();
                   wait.until(ExpectedConditions.elementToBeClickable(addressLine2)).sendKeys(addressline2);
                   wait.until(ExpectedConditions.elementToBeClickable(addressLine3)).clear();
                   wait.until(ExpectedConditions.elementToBeClickable(addressLine3)).sendKeys(addressline3);
                   wait.until(ExpectedConditions.elementToBeClickable(addressCity)).clear();
                   wait.until(ExpectedConditions.elementToBeClickable(addressCity)).sendKeys(addresscity);
                   wait.until(ExpectedConditions.elementToBeClickable(addressCountry));
                   CommonFunctions.selectOptionFromDropDown(addressCountry, addresscountry);
                   wait.until(ExpectedConditions.elementToBeClickable(addressPostcode)).clear();
                   wait.until(ExpectedConditions.elementToBeClickable(addressPostcode)).sendKeys(addresspostcode);
                   wait.until(ExpectedConditions.elementToBeClickable(addressWindowSaveButton)).click();
                   Thread.sleep(2000);
                 // helpers.CommonFunctions.ClickOnButton("Save", driver);
                  
            }
     }
     catch(NoSuchElementException e)
     {
            System.out.println("The element is not found on GMC Address Pop up box" +e);
     }
     
    

	}
}
