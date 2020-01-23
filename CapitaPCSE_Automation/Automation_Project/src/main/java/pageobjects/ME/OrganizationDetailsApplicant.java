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

import utilities.ExcelUtilities;

public class OrganizationDetailsApplicant {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(xpath=ConstantOR.ApplicantRepresentative_dropdown)
	WebElement applicantRepresentative_dropdown;
	
	@FindBy(xpath=ConstantOR.ApplicantRepresentativeContacted_radiobutn)
	List<WebElement> applicantRepresentativeContacted_radiobutn;
	
	@FindBy(xpath=ConstantOR.PD_Address1_txtbox)
	WebElement Address1_txtbox;
	
	@FindBy(xpath=ConstantOR.PD_Address2_txtbox)
	WebElement Address2_txtbox;
	
	@FindBy(xpath=ConstantOR.PD_Address3_txtbox)
	WebElement Address3_txtbox;
	
	@FindBy(xpath=ConstantOR.PD_City_txtbox)
	WebElement city_txtbox;
	
	@FindBy(xpath=ConstantOR.PD_PostalCode_txtbox)
	WebElement postalCode_txtbox;
	
	@FindBy(xpath=ConstantOR.ConfirmRepresentativeDetails_btn)
	WebElement confirmRepresentativeDetails_btn;
	
		
	public OrganizationDetailsApplicant(WebDriver driver) 
		{
			this.driver = driver;
			driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			wait = new WebDriverWait(this.driver, 60);
			PageFactory.initElements(this.driver, this);
		}
	
	public OrganizationDetailsSoleTraderPharma enterApplicantRepresentative(String key) {
		try{
		//	String ExcelOwnership = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "EntryApp", "Ownership", key);
			String exl_ApplicantRepresentative = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "OrganizationDetailsApplicant", "ApplicantRepresentative", key);
			String exl_ApplicantRepresentativeContacted = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "OrganizationDetailsApplicant", "ApplicantRepresentativeContacted", key);
				
			Select dropdown = new Select(applicantRepresentative_dropdown);
			dropdown.selectByVisibleText(exl_ApplicantRepresentative);
			
			helpers.Support.PageLoadExternalwait(driver);
			//helpers.CommonFunctions.spinnerwait(driver);
			
			helpers.CommonFunctions.ClickOnButtonWebElement(applicantRepresentativeContacted_radiobutn, exl_ApplicantRepresentativeContacted, driver);
			
			/*if(exl_ApplicantRepresentativeContacted.equalsIgnoreCase("Yes"))
			{
				
			}*/
				
			helpers.CommonFunctions.clickElementByXpath(confirmRepresentativeDetails_btn, driver);
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Applicant Representative is not able to enter the data");
		}
		return new OrganizationDetailsSoleTraderPharma(driver);
	}
	

}
