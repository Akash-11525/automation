package pageobjects.PL;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

import helpers.Screenshot;
import helpers.Support;

public class Capacity extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	
	@FindBy(xpath="//select[@id='ListTypeCode']")
	WebElement Capacitydropdown;
	
	@FindBy(id="ListSubTypeCode")
	WebElement Performerdropdown;
	
	@FindBy(name="FirstDate")
	WebElement RegisterDate;
	
/*	@FindBy(name="SecondDate")
	WebElement RegisterDate;*/
	
	@FindBy(name="SecondDate")
	WebElement FullRegDate;
	
	@FindBy(name="GpRegistrarDate")
	WebElement GpRegistrarDate;
	
	
	@FindBy(id="ProfessionalCapacity")
	WebElement CapacityTab;
	
	@FindBy(id="FlexField1")
	WebElement VTNumber;
	
	
	@FindBy(name="btnSaveNext")
	WebElement Save_Submit;	
	
	@FindBy(id="AdditionalProfessionalNumber")
	WebElement OQCNumber;

	
	//*[@id="WillWorkAsLocum"]
	
	
/*	@FindBy(id="ProfessionalCouncilRegistrationNumber")
	WebElement CouncilNo;*/
	
	@FindBy(id="GeneralCouncilNumber")
	WebElement CouncilNo;
	
	@FindBy(id="NoLicenseToPracticeExplanation")
	WebElement PraticeExplanation;
	
	
	
	
	
	
	public Capacity(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		PageFactory.initElements(this.driver, this);
	}

	public String selectCapacity(int Position, String GMC) throws InterruptedException {
		String ActualTableName = null;
		try{
			String PerformerList_cap = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Capacity", "PerformerList",Position);
			String MedPerformer_cap = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Capacity", "Medical Performer",Position);
			//String GMC_Cap = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Capacity", "GMCNo",Position);
			//String GMC_Cap = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Register", "GMCNumber",Position);
			String GMC_Cap = GMC;
			String SchemeApplies_cap = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Capacity", "Scheme",Position);
			String LocumWork_cap = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Capacity", "Locum",Position);
			String LicensePratice_cap = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Capacity", "License",Position);
			String RegDate_cap = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Capacity", "RegDate",Position);	
			String FullRegDate_cap = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Capacity", "FullRegDate",Position);
			String GPREGDate = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Capacity", "GpRegistrarDate",Position);
			String VTNumber_Cap = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Capacity", "VTNumber",Position);
			String OQCNumber_Cap = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Capacity", "OQCNumber",Position);
			Thread.sleep(500);
			wait.until(ExpectedConditions.elementToBeClickable(CapacityTab));
			ActualTableName = CapacityTab.getAttribute("id");
			System.out.println(ActualTableName);
		/*	Select dropdown = new Select(Capacitydropdown);
			dropdown.selectByVisibleText(PerformerList_cap);*/
			if(PerformerList_cap.startsWith("Medical"))
			{	
				System.out.println(Capacitydropdown.isEnabled());
				if((Capacitydropdown.isEnabled()))
				/*boolean ispresent = driver.findElements(By.xpath("//select[@id='ListTypeCode']")).size() != 0;
				System.out.println(ispresent);
				if(!ispresent)*/
						{
				Select dropdown = new Select(Capacitydropdown);
				dropdown.selectByVisibleText(PerformerList_cap);
				}
				Thread.sleep(7000);
				Select dropdown1 = new Select(Performerdropdown);
				dropdown1.selectByVisibleText(MedPerformer_cap);			
				wait.until(ExpectedConditions.elementToBeClickable(CouncilNo)).clear();
				wait.until(ExpectedConditions.elementToBeClickable(CouncilNo)).sendKeys(GMC_Cap);
				if(MedPerformer_cap.equalsIgnoreCase("GP Performer"))
				{					
						List<WebElement> Schemes=driver.findElements(By.xpath("//*[@class='radio margin-top-10']//label"));
						System.out.println("total Radio Button "+Schemes.size());
				          for (WebElement Scheme : Schemes)
				          {                   
				        	   String SchemeName = Scheme.getText();  
				        	//   String SchemeName = SchemeNamewithSpaces.replaceAll("\\s+","");
				        	   System.out.println(SchemeName);
				        	 
				        	  if (SchemeName.equalsIgnoreCase(SchemeApplies_cap))
				        	  {        		 
				        		  scrolltoElement(driver, Scheme);
				        		  wait.until(ExpectedConditions.elementToBeClickable(Scheme)).click();
				        		break;	        		  
				        	  }	
						
				          	}
				}
		          
		          helpers.CommonFunctions.ClickOnRadioButton("Yes", driver);  
		          scrolltoElement(driver, RegisterDate);
		        wait.until(ExpectedConditions.elementToBeClickable(RegisterDate)).clear(); 
		  		wait.until(ExpectedConditions.elementToBeClickable(RegisterDate)).sendKeys(RegDate_cap);
		  		wait.until(ExpectedConditions.elementToBeClickable(RegisterDate)).sendKeys(Keys.TAB); 
		  		scrolltoElement(driver, FullRegDate);
		  		wait.until(ExpectedConditions.elementToBeClickable(FullRegDate)).clear();
		 		wait.until(ExpectedConditions.elementToBeClickable(FullRegDate)).sendKeys(FullRegDate_cap); 
		 		wait.until(ExpectedConditions.elementToBeClickable(FullRegDate)).sendKeys(Keys.TAB); 
		 		//Added by Rupesh - DATE ON GP REGISTER new field is added in WOP.
		 		/*scrolltoElement(driver, GpRegistrarDate);
		  		wait.until(ExpectedConditions.elementToBeClickable(GpRegistrarDate)).clear();
		 		wait.until(ExpectedConditions.elementToBeClickable(GpRegistrarDate)).sendKeys(GPREGDate); 
		 		wait.until(ExpectedConditions.elementToBeClickable(GpRegistrarDate)).sendKeys(Keys.TAB); */
			}

			if(PerformerList_cap.startsWith("Dental"))
			{
			if((Capacitydropdown.isEnabled()))
				{
				Select dropdown = new Select(Capacitydropdown);
				dropdown.selectByVisibleText(PerformerList_cap);
				}
				Thread.sleep(2000);
				Select dropdown1 = new Select(Performerdropdown);
				dropdown1.selectByVisibleText(MedPerformer_cap);
				scrolltoElement(driver, CouncilNo);
				wait.until(ExpectedConditions.elementToBeClickable(CouncilNo)).clear();
				wait.until(ExpectedConditions.elementToBeClickable(CouncilNo)).sendKeys(GMC_Cap);
				helpers.CommonFunctions.ClickOnRadioButton("Yes", driver);  
				scrolltoElement(driver, VTNumber);
				wait.until(ExpectedConditions.elementToBeClickable(VTNumber)).clear();
				wait.until(ExpectedConditions.elementToBeClickable(VTNumber)).sendKeys(GMC_Cap);
			}
			if(PerformerList_cap.startsWith("Ophthalmic") && MedPerformer_cap.equalsIgnoreCase("Ophthalmic performer"))
			{	
				if((Capacitydropdown.isEnabled())){
				Select dropdown = new Select(Capacitydropdown);
				dropdown.selectByVisibleText(PerformerList_cap);
				}
				Select dropdown1 = new Select(Performerdropdown);
				dropdown1.selectByVisibleText(MedPerformer_cap);			
				wait.until(ExpectedConditions.elementToBeClickable(CouncilNo)).clear();
				wait.until(ExpectedConditions.elementToBeClickable(CouncilNo)).sendKeys(GMC_Cap);	
				 helpers.CommonFunctions.ClickOnRadioButton("Yes", driver);
			}
			
			if(PerformerList_cap.startsWith("Ophthalmic") && MedPerformer_cap.equalsIgnoreCase("Ophthalmic medical practitioner"))
			{	
				if((Capacitydropdown.isEnabled())){
				Select dropdown = new Select(Capacitydropdown);
				dropdown.selectByVisibleText(PerformerList_cap);
				}
				Select dropdown1 = new Select(Performerdropdown);
				dropdown1.selectByVisibleText(MedPerformer_cap);			
				wait.until(ExpectedConditions.elementToBeClickable(CouncilNo)).clear();
				wait.until(ExpectedConditions.elementToBeClickable(CouncilNo)).sendKeys(GMC_Cap);	
				wait.until(ExpectedConditions.elementToBeClickable(OQCNumber)).clear();
				wait.until(ExpectedConditions.elementToBeClickable(OQCNumber)).sendKeys(OQCNumber_Cap);	
							 
				/* List<WebElement> Schemes=driver.findElements(By.xpath("//*[@class='radio margin-bottom-15']"));
					System.out.println("total Radio Button "+Schemes.size());
			          for (WebElement Scheme : Schemes)
			          {                   
			        	   String SchemeName = Scheme.getText();  
			        	//   String SchemeName = SchemeNamewithSpaces.replaceAll("\\s+","");
			        	   System.out.println(SchemeName);
			        	 
			        	  if (SchemeName.equalsIgnoreCase(SchemeApplies_cap))
			        	  {        		 
			        		  scrolltoElement(driver, Scheme);
			        		  wait.until(ExpectedConditions.elementToBeClickable(Scheme)).click();
			        		break;	        		  
			        	  }	
					
			          	}	*/
			          
			          helpers.CommonFunctions.ClickOnRadioButton("Yes", driver);  
			          scrolltoElement(driver, RegisterDate);
			        wait.until(ExpectedConditions.elementToBeClickable(RegisterDate)).clear(); 
			  		wait.until(ExpectedConditions.elementToBeClickable(RegisterDate)).sendKeys(RegDate_cap); 
			  		scrolltoElement(driver, FullRegDate);
			  		wait.until(ExpectedConditions.elementToBeClickable(FullRegDate)).clear();
			 		wait.until(ExpectedConditions.elementToBeClickable(FullRegDate)).sendKeys(FullRegDate_cap);  
			}
			
	 		
		}
		
		catch (NoSuchElementException e)
		{
			System.out.println("The Data is not filled properly on capacity Tab ."+e);
		}


		return ActualTableName;
	}

	public CreateNewApp ClickOnSave_Submit() throws InterruptedException {
		try{
			Thread.sleep(3000);
			Actions action = new Actions(driver);
			action.moveToElement(Save_Submit);
			action.doubleClick().build().perform();
			boolean ispresent = driver.findElements(By.xpath("//div[@class='loader']")).size() != 0;	
			helpers.CommonFunctions.PageLoadExternalwait(driver);
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found Savebutton - Capacitytab" +e);
		}	
		return new CreateNewApp(driver);
	}

	public String SelectNoforLicensed(int Position) throws InterruptedException
	{
		String ActualTableName = null;
		try{
			String PerformerList_cap = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Capacity", "PerformerList",Position);
			String MedPerformer_cap = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Capacity", "Medical Performer",Position);
			String GMC_Cap = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Capacity", "GMCNo",Position);
			String SchemeApplies_cap = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Capacity", "Scheme",Position);
			String LocumWork_cap = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Capacity", "Locum",Position);
			String LicensePratice_cap = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Capacity", "License",Position);
			String RegDate_cap = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Capacity", "RegDate",Position);	
			String FullRegDate_cap = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Capacity", "FullRegDate",Position);
			String VTNumber_Cap = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Capacity", "VTNumber",Position);
			String OQCNumber_Cap = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "Capacity", "OQCNumber",Position);
			Thread.sleep(3000);
			wait.until(ExpectedConditions.elementToBeClickable(CapacityTab));
			ActualTableName = CapacityTab.getAttribute("id");
			//System.out.println(ActualTableName);

			if(PerformerList_cap.equalsIgnoreCase("Medical List"))
			{	
				System.out.println(Capacitydropdown.isEnabled());
				if((Capacitydropdown.isEnabled()))
				{
				Select dropdown = new Select(Capacitydropdown);
				dropdown.selectByVisibleText(PerformerList_cap);
				}
				Select dropdown1 = new Select(Performerdropdown);
				dropdown1.selectByVisibleText(MedPerformer_cap);			
				wait.until(ExpectedConditions.elementToBeClickable(CouncilNo)).clear();
				wait.until(ExpectedConditions.elementToBeClickable(CouncilNo)).sendKeys(GMC_Cap);
				if(MedPerformer_cap.equalsIgnoreCase("GP Performer"))
				{					
						List<WebElement> Schemes=driver.findElements(By.xpath("//*[@class='radio']"));
						System.out.println("total Radio Button "+Schemes.size());
				          for (WebElement Scheme : Schemes)
				          {                   
				        	   String SchemeName = Scheme.getText();  
				          	   System.out.println(SchemeName);
				        	 
				        	  if (SchemeName.equalsIgnoreCase(SchemeApplies_cap))
				        	  {        		 
				        		  scrolltoElement(driver, Scheme);
				        		  wait.until(ExpectedConditions.elementToBeClickable(Scheme)).click();
				        		break;	        		  
				        	  }	
						
				          	}
				      	List<WebElement> NoButtons=driver.findElements(By.xpath("//*[@class='radio ipad-radio-btn-margin']"));
						System.out.println("total Radio Button "+Schemes.size());
				          for (WebElement NoButton : NoButtons)
				          {                   
				        	   String SchemeName = NoButton.getText();  
				          	   System.out.println(SchemeName);
				        	 
				        	  if (SchemeName.equalsIgnoreCase("No"))
				        	  {        		 
				        		  scrolltoElement(driver, NoButton);
				        		  wait.until(ExpectedConditions.elementToBeClickable(NoButton)).click();
				        			        		  
				        	  }	
						
				          	}
				}
		          
		         // helpers.CommonFunctions.ClickOnRadioButton("No", driver);  
		        /*  scrolltoElement(driver, RegisterDate);
		          wait.until(ExpectedConditions.elementToBeClickable(PraticeExplanation)).sendKeys("Test"); */
				wait.until(ExpectedConditions.elementToBeClickable(PraticeExplanation)).clear();  
		        wait.until(ExpectedConditions.elementToBeClickable(RegisterDate)).clear(); 
		  		wait.until(ExpectedConditions.elementToBeClickable(RegisterDate)).sendKeys(RegDate_cap); 
		  		scrolltoElement(driver, FullRegDate);
		  		wait.until(ExpectedConditions.elementToBeClickable(FullRegDate)).clear();
		 		wait.until(ExpectedConditions.elementToBeClickable(FullRegDate)).sendKeys(FullRegDate_cap);  
			}
		}
			
			catch(NoSuchElementException e)
			{
				System.out.println("The element is not found Savebutton - Capacitytab" +e);
			}	
			return  ActualTableName;
		
	
	}

	public void ScreenshotofCapacity(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, Capacitydropdown);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		
	}


	

}
