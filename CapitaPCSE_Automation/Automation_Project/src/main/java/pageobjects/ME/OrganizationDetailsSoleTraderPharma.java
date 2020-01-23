package pageobjects.ME;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Support;
import utilities.ExcelUtilities;

public class OrganizationDetailsSoleTraderPharma extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(xpath=ConstantOR.STP_ApplicantRepresentative_dropdown)
	WebElement stpApplicantRepresentative_dropdown;
	
	@FindBy(xpath=ConstantOR.STP_BirthDate_txtbox)
	WebElement birthDate_txtbox;
	
	@FindBy(xpath=ConstantOR.STP_GPhC_txtbox)
	WebElement stp_GPhC_txtbox;
	
	@FindBy(xpath=ConstantOR.STP_Gender_rdiobtn)
	List<WebElement> stp_Gender_rdiobtn;
	
	@FindBy(xpath=ConstantOR.STP_Confirm_btn)
	WebElement stp_Confirm_btn;
	
	@FindBy(xpath=ConstantOR.ORG_Next_btn)
	WebElement org_Next_btn;
	
	@FindBy(xpath=ConstantOR.STP_ManuallyAddress_btn)
	WebElement stp_AddManually_btn;
	
	@FindBy(xpath=ConstantOR.STP_Address1_txtbox)
	WebElement stp_Address1_txtbox;
	
	@FindBy(xpath=ConstantOR.STP_Address2_txtbox)
	WebElement stp_Address2_txtbox;
	
	@FindBy(xpath=ConstantOR.STP_Address3_txtbox)
	WebElement stp_Address3_txtbox;
	
	@FindBy(xpath=ConstantOR.STP_City_txtbox)
	WebElement stp_City_txtbox;
	
	@FindBy(xpath=ConstantOR.STP_PostalCode_txtbox)
	WebElement stp_PostalCode_txtbox;
	
	
	
	
	public OrganizationDetailsSoleTraderPharma(WebDriver driver) 
		{
			this.driver = driver;
			driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			wait = new WebDriverWait(this.driver, 60);
			PageFactory.initElements(this.driver, this);
		}

	public MarketEntryApplication enterSoleTraderPharmacist(String key) {
		try{
		//	String ExcelOwnership = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "EntryApp", "Ownership", key);
			String exl_SoleTraderPharma = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "OrganizationDetailsSoleTraderPh", "SoleTraderPharma", key);
			String exl_DateBirthSoleTraderPharma = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "OrganizationDetailsSoleTraderPh", "DateBirthSoleTraderPharma", key);
			String exl_Gender = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "OrganizationDetailsSoleTraderPh", "GendeSoleTraderPharma", key);
			String exl_GPhCRegistrationNumber = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "OrganizationDetailsSoleTraderPh", "GPhCRegistrationNumber", key);
			String exl_Address1 = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "ManuallyAddress", "Address1", key);
			String exl_Address2 = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "ManuallyAddress", "Address2", key);
			String exl_Address3 = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "ManuallyAddress", "Address3", key);
			String exl_City = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "ManuallyAddress", "City", key);
			String exl_PostalCode = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "ManuallyAddress", "PostalCode", key);
	
			
			
			Select dropdown = new Select(stpApplicantRepresentative_dropdown);
			dropdown.selectByVisibleText(exl_SoleTraderPharma);
			
			helpers.Support.PageLoadExternalwait(driver);
			//helpers.CommonFunctions.spinnerwait(driver);
			
			wait.until(ExpectedConditions.elementToBeClickable(birthDate_txtbox)).sendKeys(exl_DateBirthSoleTraderPharma);
			
			helpers.CommonFunctions.ClickOnButtonWebElement(stp_Gender_rdiobtn,exl_Gender, driver);			
			
			wait.until(ExpectedConditions.elementToBeClickable(stp_GPhC_txtbox)).sendKeys(exl_GPhCRegistrationNumber);	
			
			helpers.CommonFunctions.clickElementByXpath(stp_AddManually_btn, driver);			
			
			wait.until(ExpectedConditions.elementToBeClickable(stp_Address1_txtbox)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(stp_Address1_txtbox)).sendKeys(exl_Address1);
			wait.until(ExpectedConditions.elementToBeClickable(stp_Address2_txtbox)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(stp_Address2_txtbox)).sendKeys(exl_Address2);
			wait.until(ExpectedConditions.elementToBeClickable(stp_Address3_txtbox)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(stp_Address3_txtbox)).sendKeys(exl_Address3);
			wait.until(ExpectedConditions.elementToBeClickable(stp_City_txtbox)).clear();
			wait.until(ExpectedConditions.elementToBeClickable(stp_City_txtbox)).sendKeys(exl_City);
			wait.until(ExpectedConditions.elementToBeClickable(stp_PostalCode_txtbox)).clear();			
			wait.until(ExpectedConditions.elementToBeClickable(stp_PostalCode_txtbox)).sendKeys(exl_PostalCode);
			Thread.sleep(2000);
			helpers.CommonFunctions.ClickOnButton("Confirm Address", driver);	
									
			/*if(exl_ApplicantRepresentativeContacted.equalsIgnoreCase("Yes"))
			{
				
			}
			*/	
			Thread.sleep(3000);
			helpers.CommonFunctions.clickElementByXpath(stp_Confirm_btn, driver);
			
			wait.until(ExpectedConditions.elementToBeClickable(org_Next_btn));			
			helpers.CommonFunctions.clickElementByXpath(org_Next_btn, driver);
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Sole Trader Pharmacist is not able to enter the data");
		}
		return new MarketEntryApplication(driver);
	}
	
	

}
