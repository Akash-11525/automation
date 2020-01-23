package pageobjects.ME;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Support;
import utilities.ExcelUtilities;

public class ApplicationType extends Support { 
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

	@FindBy(xpath="//select[@id='OwnershipType_disabled']//option[@selected='selected']")
	WebElement typeOwnership;
	
	@FindBy(xpath="//div[@id='divLPS']//div[@class='radio checkbox-sg']//label")
	List<WebElement> LPS;	
	
	@FindBy(xpath="//div[@id='divRightToReturn']//div[@class='radio checkbox-sg']//label")
	List<WebElement> PLPS;
	
	
	
	public ApplicationType(WebDriver driver) 
	
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 60);
		PageFactory.initElements(this.driver, this);
	}
	
	public PremiseDetails verifyApplicationTypeDetails(String key) {
		try{
		//	String ExcelOwnership = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "EntryApp", "Ownership", key);
			
			helpers.CommonFunctions.ClickOnSubmitButton("Save & Next", driver);
			System.out.println("Applications Type is not able to enter the data");
		
		}
		catch(Exception e)
		{
			System.out.println("Applications Type is not able to enter the data" +e);
		}
		return new PremiseDetails(driver);
	}

	public ApplicationType VerifyApplicationTypeDetails(String excelOwnership, String excelLPS,
			String excelpharmaceutical, String excelappliancecontractor, String excelappliances,
			String excelpharmaceuticallistpremises, String excelpharmaceuticallistBoard, String excelAdditionalInfo,
			String excelProvisionAppliancestxt) {
		
		try{
			String typeownership=typeOwnership.getText();
			if(typeownership.equalsIgnoreCase(excelOwnership))
			{
				assertmsgs.add("Type of ownership field is read only mode with correct value "+typeownership);
			}
			else
			{
				assertmsgs.add("Type of ownership field is not read only mode or incorrect value "+typeownership);
			}	
			
			for (int i = 0; i < LPS.size(); i++)
			{
				if(excelLPS.equalsIgnoreCase(LPS.get(i).getText()))
				{
					String lpsselected=LPS.get(i).findElement(By.xpath("//input")).getAttribute("value");
					if(lpsselected.equalsIgnoreCase("True"))
					{
						assertmsgs.add("Local Pharmaceutical Services (LPS) provisions field is read only mode with correct value "+lpsselected);
						break;
					}
					else
					{
						assertmsgs.add("Local Pharmaceutical Services (LPS) provisions field is not read only mode or incorrect value "+lpsselected);
						break;
					}
				}
			}
			
			for (int i = 0; i < PLPS.size(); i++)
			{
				if(excelLPS.equalsIgnoreCase(PLPS.get(i).getText()))
				{
					String lpsselected=PLPS.get(i).findElement(By.xpath("//input")).getAttribute("value");
					if(lpsselected.equalsIgnoreCase("True"))
					{
						assertmsgs.add("Pharmaceutical and Local Pharmaceutical Services field is read only mode with correct value "+lpsselected);
						break;
					}
					else
					{
						assertmsgs.add("Pharmaceutical and Local Pharmaceutical Services field is not read only mode or incorrect value "+lpsselected);
						break;
					}
				}
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		
		return null;
	}

	public void verify_ApplicationType(int i) {
		// TODO Auto-generated method stub
		System.out.println("Verifying the Application Type Information");
		
	}

	public ApplicationType Create_ApplicationType(int position) {
		// TODO Auto-generated method stub
		try{			
				String Exl_AppType = ExcelUtilities.getKeyValueFromExcelWithPosition("MEAPP.xlsx", "EntryApp", "ApplicationType", position);
				String Exl_LPS = ExcelUtilities.getKeyValueFromExcelWithPosition("MEAPP.xlsx", "EntryApp", "LPS", position);
				String Exl_PharmaLPS = ExcelUtilities.getKeyValueFromExcelWithPosition("MEAPP.xlsx", "EntryApp", "PharmaLPS", position);
				String Exl_ApplinceContractor = ExcelUtilities.getKeyValueFromExcelWithPosition("MEAPP.xlsx", "EntryApp", "ApplianceContractor", position);
				String Exl_Appliances = ExcelUtilities.getKeyValueFromExcelWithPosition("MEAPP.xlsx", "EntryApp", "Appliances", position);
				String Exl_SpecifyAppliances = ExcelUtilities.getKeyValueFromExcelWithPosition("MEAPP.xlsx", "EntryApp", "SpecifyAppliances", position);
				String Exl_PharmaListPremises = ExcelUtilities.getKeyValueFromExcelWithPosition("MEAPP.xlsx", "EntryApp", "PharmaListPremises", position);
				//String Exl_PremisesAppliesApplication = ExcelUtilities.getKeyValueFromExcelWithPosition("MEAPP.xlsx", "EntryApp", "PremisesAppliesApplication", position);
				String Exl_PharmaHealth = ExcelUtilities.getKeyValueFromExcelWithPosition("MEAPP.xlsx", "EntryApp", "PharmaHealthWellbeingListBoard", position);
				String Exl_AdditionalInfo = ExcelUtilities.getKeyValueFromExcelWithPosition("MEAPP.xlsx", "EntryApp", "AdditionalInfo", position);
			
				
				Select dropdown = new Select(SelectOwnership);
				dropdown.selectByVisibleText(Exl_AppType);
				
				wait.until(ExpectedConditions.visibilityOfAllElements(LPSProvisions));
				helpers.CommonFunctions.ClickOnButtonWebElement(LPSProvisions, Exl_LPS, driver);
				
				if(!Exl_LPS.equalsIgnoreCase("Yes"))
				{
					//execute No block
					
					
				}
				else// execute Yes block
				{	
					wait.until(ExpectedConditions.visibilityOfAllElements(PharmaceuticalServices));
					helpers.CommonFunctions.ClickOnButtonWebElement(PharmaceuticalServices, Exl_PharmaLPS, driver);				
				}			
				if(Exl_PharmaLPS.equalsIgnoreCase("Yes"))
				{
					Select dropdown1 = new Select(SelectOutletType);
					dropdown1.selectByVisibleText(Exl_ApplinceContractor);
					
					if(Exl_ApplinceContractor.equalsIgnoreCase("Pharmacy"))
					{
						
					}				
						wait.until(ExpectedConditions.visibilityOfAllElements(Provisions));
						helpers.CommonFunctions.ClickOnButtonWebElement(Provisions, Exl_Appliances, driver);	
				
					if(Exl_Appliances.equalsIgnoreCase("Provision of Appliances"))
					{
						//assertmsgs.add("Please specify the appliances that you undertake to provide field rendered when Provision of Appliances is selected ");
						wait.until(ExpectedConditions.elementToBeClickable(provisionofAppliances_txtArea)).sendKeys(Exl_SpecifyAppliances);
					}
					
						wait.until(ExpectedConditions.visibilityOfAllElements(RelevantPharmParty));
						helpers.CommonFunctions.ClickOnButtonWebElement(RelevantPharmParty, Exl_PharmaListPremises, driver);
						
						wait.until(ExpectedConditions.visibilityOfAllElements(RelevantHealthList));
						helpers.CommonFunctions.ClickOnButtonWebElement(RelevantHealthList, Exl_PharmaHealth, driver);
						
						wait.until(ExpectedConditions.elementToBeClickable(AdditionalInfo_txtArea)).sendKeys(Exl_AdditionalInfo);
						
						helpers.CommonFunctions.ClickOnSubmitButton("Save & Next", driver);
						
						System.out.println("The Pharmacy market application is filled");
				
				}
				return new ApplicationType(driver);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ApplicationType(driver);
	}



}