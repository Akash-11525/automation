package pageobjects.ME;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Support;
import pageobjects.PL.PerformerHome;
import utilities.ExcelUtilities;
public class MarketEntryApplication extends Support{
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

	@FindBy(id="SelectPerformer")
	WebElement SelectPerfomerdropdown;
	
	@FindBy(xpath="//button[@class='btn btn-success']")
	WebElement Submitbutton;
	
	@FindBy(xpath="//a[text()='Applicant Representative']")
	WebElement applicantLink;
	
	@FindBy(xpath="//a[text()='Sole Trader Pharmacist']")
	WebElement SoleTraderLink;
	
	
	public MarketEntryApplication(WebDriver driver) 
	
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 60);
		PageFactory.initElements(this.driver, this);
	}
	
	public PerformerHome SelectPerformer(String reason) throws InterruptedException 
	{
		Select dropdown = new Select(SelectPerfomerdropdown);
		dropdown.selectByVisibleText(reason);
	
	
		return new PerformerHome(driver);
	}

	public CreatePharmacy clickonButton(String Text) {
		try{
			System.out.println(Text);
			helpers.CommonFunctions.ClickOnButton(Text, driver);
			}
			catch(Exception e)
			{
				System.out.println("The button is not present on ME create App page "+Text);
		
			}
			return new CreatePharmacy(driver);
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
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ApplicationType(driver);
	}

		public Boolean VerifyTickMark(String TabName) 
	{
		boolean TickMarkflag = false;
		boolean ispresent = false;
		try{		
			Thread.sleep(2000);			
			System.out.println(TabName);
			String ActualidTick = TabName.replaceAll("\\s+","");
		
			//System.out.println((driver.findElements(By.xpath("//*[@id='"+ActualidTick+"']/i")).size()));
			ispresent = driver.findElements(By.xpath("//*[@name='"+ActualidTick+"']/i")).size() != 0;
			System.out.println(ispresent);
			if(ispresent)	
			{
				if(driver.findElement(By.xpath("//*[@name='"+ActualidTick+"']/i")).isDisplayed())			
				{
					//WebElement TickMarkOntab = driver.findElement(By.xpath("//*[@name='"+ActualidTick+"']/i"));
					//scrolltoElement(driver, TickMarkOntab);
					TickMarkflag = true;				
				}				
			
			}	
		}
			catch(Exception e)
			{
				System.out.println("The Resident address manually button is not found ");
				e.printStackTrace();
				
			}			
		
			return TickMarkflag;
	
}

		public OrganizationDetailsApplicant clickApplicantRepresentative() {
		
			try{
				WebDriverWait wait=new WebDriverWait(driver,30);			
				wait.until(ExpectedConditions.elementToBeClickable(applicantLink)).click();	
				
				helpers.Support.PageLoadExternalwait(driver);
				//helpers.CommonFunctions.spinnerwait(driver);
			
				//wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='loader']")));
		
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			return new OrganizationDetailsApplicant(driver);
			}

		public OrganizationDetailsSoleTraderPharma clickSoleTraderPharma() {
		
			try{
				WebDriverWait wait=new WebDriverWait(driver,30);			
				wait.until(ExpectedConditions.elementToBeClickable(SoleTraderLink)).click();	
			
				helpers.Support.PageLoadExternalwait(driver);
				//helpers.CommonFunctions.spinnerwait(driver);
				//wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@class='loader hide-spinner']")));
		
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			return new OrganizationDetailsSoleTraderPharma(driver);
			}
}
