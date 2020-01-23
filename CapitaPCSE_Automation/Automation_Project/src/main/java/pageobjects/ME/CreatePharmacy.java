package pageobjects.ME;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import helpers.Support;

//import browsersetup.BaseClass;
//import net.sourceforge.jtds.jdbc.Support;
import pageobjects.PL.PerformerHome;
import utilities.*;
import verify.Verify;
public class CreatePharmacy extends Support{ 
	WebDriver driver;
	WebDriverWait wait;
	List<String> assertmsgs=new ArrayList<String>();

	@FindBy(id=ConstantOR.OwnershipType_dropdown)
	WebElement SelectOwnership;
	
	@FindBy(xpath=ConstantOR.LocalPharmaceuticalServicesProvisions_rdiobutn)
	List<WebElement> LPSProvisions;
	
	@FindBy(xpath=ConstantOR.PharmaceuticalLocalPharmaceuticalServices_rdiobutn)
	List<WebElement> PharmaceuticalServices;
	
	@FindBy(xpath=ConstantOR.Pharmacy_DispensingAapplianceContractor_dropdown)
	WebElement SelectOutletType;
	
	@FindBy(xpath=ConstantOR.ProvisionOfDrugs_Provision_checkbox)
	List<WebElement> Provisions;
	
	@FindBy(xpath=ConstantOR.ProvisionofAppliances_txtArea)
	WebElement provisionofAppliances_txtArea;
	
	@FindBy(xpath=ConstantOR.RelevantPharmaceuticalList_rdiobtn)
	List<WebElement> RelevantPharmParty;
	
	@FindBy(xpath=ConstantOR.RelevantHealthWellbeingBoard_rdiobutn)
	List<WebElement> RelevantHealthList;	
	
	@FindBy(xpath=ConstantOR.AdditionalInfoSuportApp_txtArea)
	WebElement AdditionalInfo_txtArea;
	
	public CreatePharmacy(WebDriver driver) 
	
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 60);
		PageFactory.initElements(this.driver, this);
	}


//	public CreatePharmacy fillpharmacyMarket(String ExcelOwnership,String ExcelLPS,String Excelpharmaceutical,String Excelappliancecontractor,String Excelappliances,String Excelpharmaceuticallistpremises,String ExcelpharmaceuticallistBoard,String ExcelAdditionalInfo,String key) {
	public List<String> createPharmacyMarketEntryApplication(String ExcelOwnership,String ExcelLPS,String Excelpharmaceutical,String Excelappliancecontractor,String Excelappliances,String Excelpharmaceuticallistpremises,String ExcelpharmaceuticallistBoard,String ExcelAdditionalInfo,String ExcelProvisionAppliancestxt) {
		try{
			
			Select dropdown = new Select(SelectOwnership);
			dropdown.selectByVisibleText(ExcelOwnership);
			
			wait.until(ExpectedConditions.visibilityOfAllElements(LPSProvisions));
			helpers.CommonFunctions.ClickOnButtonWebElement(LPSProvisions, ExcelLPS, driver);
			
			if(!ExcelLPS.equalsIgnoreCase("Yes"))
			{
				//execute No block
				
				
			}
			else// execute Yes block
			{	
				wait.until(ExpectedConditions.visibilityOfAllElements(PharmaceuticalServices));
				helpers.CommonFunctions.ClickOnButtonWebElement(PharmaceuticalServices, Excelpharmaceutical, driver);				
			}				
			
			Select dropdown1 = new Select(SelectOutletType);
			dropdown1.selectByVisibleText(Excelappliancecontractor);
			
			if(Excelappliancecontractor.equalsIgnoreCase("Pharmacy"))
			{
				boolean checkBoxVal=verifyCheckBoxSelectedwithDisabled("Provision of Drugs");
				if(checkBoxVal)
				{
					assertmsgs.add("Provision of Drugs of appliances is pre-selected when Pharmacy appliance contractor is selected");
				}
				else
				{
					assertmsgs.add("Provision of Drugs of appliances is not pre-selected when Pharmacy appliance contractor is selected");
					assertEquals(checkBoxVal, true);
				}
				
			}
			
			wait.until(ExpectedConditions.visibilityOfAllElements(Provisions));
			helpers.CommonFunctions.ClickOnButtonWebElement(Provisions, Excelappliances, driver);	
			
			if(Excelappliances.equalsIgnoreCase("Provision of Appliances"))
			{
				assertmsgs.add("Please specify the appliances that you undertake to provide field rendered when Provision of Appliances is selected ");
				wait.until(ExpectedConditions.elementToBeClickable(provisionofAppliances_txtArea)).sendKeys(ExcelProvisionAppliancestxt);
			}
			else
			{
				assertmsgs.add("Please specify the appliances that you undertake to provide field not rendered when Provision of Appliances is selected ");
			}
			
			wait.until(ExpectedConditions.visibilityOfAllElements(RelevantPharmParty));
			helpers.CommonFunctions.ClickOnButtonWebElement(RelevantPharmParty, Excelpharmaceuticallistpremises, driver);
			
			wait.until(ExpectedConditions.visibilityOfAllElements(RelevantHealthList));
			helpers.CommonFunctions.ClickOnButtonWebElement(RelevantHealthList, ExcelpharmaceuticallistBoard, driver);
			
			wait.until(ExpectedConditions.elementToBeClickable(AdditionalInfo_txtArea)).sendKeys(ExcelAdditionalInfo);
			
			helpers.CommonFunctions.ClickOnSubmitButton("Save & Next", driver);
			
			System.out.println("The Pharmacy market application is filled");
			return assertmsgs;
			
		
		}
		catch(Exception e)
		{
		e.printStackTrace();
			System.out.println("The Pharmacy market application is not filled" +e);
		
		}
		return assertmsgs;
	}
	
	private boolean verifyCheckBoxSelectedwithDisabled(String checkboxName) {
		try{
		List<WebElement> listOption=driver.findElements(By.xpath(ConstantOR.ProvisionOfDrugs_Provision_checkbox));
		for(int i=0;i<=listOption.size();i++)
		{
			System.out.println("text--- : "+listOption.get(i).getText());
			if(listOption.get(i).getText().equalsIgnoreCase(checkboxName))
			{
				boolean disabledfield=listOption.get(i).findElement(By.xpath("//input[@id='ProvisionOfDrug']")).isEnabled();
				if(!disabledfield)
				{
					return true;
				}
				else
				{
					return false;	
				}
			}
			
		}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;	
		
}

}
