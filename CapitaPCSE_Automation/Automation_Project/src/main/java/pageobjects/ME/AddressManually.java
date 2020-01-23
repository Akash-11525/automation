package pageobjects.ME;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utilities.ExcelUtilities;

public class AddressManually {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(xpath=ConstantOR.Address1_txtbox)
	WebElement FRM_Address1;

	@FindBy(xpath=ConstantOR.Address2_txtbox)
	WebElement FRM_Address2;

	@FindBy(xpath=ConstantOR.Address3_txtbox)
	WebElement FRM_Address3;

	@FindBy(xpath=ConstantOR.City_txtbox)
	WebElement FRM_City;

	@FindBy(xpath=ConstantOR.PostalCode_txtbox)
	WebElement FRM_PostalCode;

	@FindBy(xpath=ConstantOR.ConfirmAddress_butn)
	WebElement FRM_ConfirmAddress;
	
	@FindBy(xpath=ConstantOR.ManuallyAddressbutton_butn)
	WebElement enterAddressManually;

	public AddressManually(WebDriver driver) 
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 60);
		PageFactory.initElements(this.driver, this);
	}
	
	public AddressManually clickonEnterAddressManually() 
	{
		try{
				wait.until(ExpectedConditions.elementToBeClickable(enterAddressManually)).click();		
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("The Enter Address Manually button is not found ");
			}	
			return new AddressManually(driver);
	}

	public MarketEntryApplication enterAddressOnAddressPopUp(String key)
	{
	try{	
			String exl_Address1 = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "ManuallyAddress", "Address1", key);
			String exl_Address2 = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "ManuallyAddress", "Address2", key);
			String exl_Address3 = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "ManuallyAddress", "Address3", key);
			String exl_City = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "ManuallyAddress", "City", key);
			String exl_PostalCode = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "ManuallyAddress", "PostalCode", key);
	
			wait.until(ExpectedConditions.elementToBeClickable(FRM_Address1)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(FRM_Address1)).sendKeys(exl_Address1);
			wait.until(ExpectedConditions.elementToBeClickable(FRM_Address2)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(FRM_Address2)).sendKeys(exl_Address2);
			wait.until(ExpectedConditions.elementToBeClickable(FRM_Address3)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(FRM_Address3)).sendKeys(exl_Address3);
			wait.until(ExpectedConditions.elementToBeClickable(FRM_City)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(FRM_City)).sendKeys(exl_City);
			wait.until(ExpectedConditions.elementToBeClickable(FRM_PostalCode)).clear();			
			wait.until(ExpectedConditions.elementToBeClickable(FRM_PostalCode)).sendKeys(exl_PostalCode);
			helpers.CommonFunctions.ClickOnButton("Confirm Address", driver);	
			
	}catch(Exception e)
	{
		e.printStackTrace();
	}
	return new MarketEntryApplication(driver);
	}

}
