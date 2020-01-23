package pageobjects.ME;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Support;
import utilities.ExcelUtilities;

public class OpeningHours extends Support{
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(xpath=ConstantOR.SelectApplicableDays_chkbox)
	List<WebElement> selectApplicableDays;
	
	@FindBy(xpath=ConstantOR.TypeOfHours_radiobtn)
	List<WebElement> selectHours_rdiobtn;
	
	@FindBy(xpath=ConstantOR.TypeOfHours_ClosedAllDay_chkbox)
	List<WebElement> selectClosed_chkbx;
	
	@FindBy(xpath=ConstantOR.OpeningTime_txtbox)
	WebElement openingTime_txtbox;
	
	@FindBy(xpath=ConstantOR.Closingtime_txtbox)
	WebElement closingtime_txtbox;
	
	@FindBy(xpath=ConstantOR.AddTimePeriod_btn)
	List<WebElement> addTimePeriod_btn;
	
	@FindBy(xpath=ConstantOR.FloorPlan_txtarea)
	WebElement floorPlan_txtarea;
	
	@FindBy(id=ConstantOR.Upload_txt_box)
	WebElement upload_txt_box;

	@FindBy(xpath=ConstantOR.Upload_btn)
	WebElement clickFile_Upload_btn;
	
	@FindBy(xpath=ConstantOR.ContinueOnConfirm_btn)
	WebElement continueOnConfirm_btn;
	
		
	public OpeningHours(WebDriver driver) 
		{
			this.driver = driver;
			driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			wait = new WebDriverWait(this.driver, 60);
			PageFactory.initElements(this.driver, this);
		}


	public void selectDaysForCoreHoursType(String key) {
	try{
			String exl_DaysForCoreHoursType = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "OpeningHours", "CoreHoursApplicableDays", key);
			//String exl_CoreHoursType = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "OpeningHours", "CoreHoursApplicableDays", key);
									
			List<String> daysCoreHoursType=helpers.MEHelpers.getListofValue(exl_DaysForCoreHoursType);	
			
			for (String days : daysCoreHoursType) 
			{				
				helpers.CommonFunctions.ClickOnButtonWebElement(selectApplicableDays, days, driver);	
			}	
			
				helpers.CommonFunctions.ClickOnButtonWebElement(selectHours_rdiobtn, "Core Hours", driver);
				System.out.println("Added the Days For Core Hours Type");
			
	}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}


	public void addOpeningClosingTimePeriodForCoreHours(String key) {
		try{
			
			String exl_OpeningH = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "OpeningHours", "CoreHoursOpeningTime", key);
			String exl_ClosingH = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "OpeningHours", "CoreHoursClosingTime", key);
			
			wait.until(ExpectedConditions.elementToBeClickable(openingTime_txtbox)).sendKeys(exl_OpeningH);
			wait.until(ExpectedConditions.elementToBeClickable(closingtime_txtbox)).sendKeys(exl_ClosingH);
			
			helpers.CommonFunctions.ClickOnButtonWebElement(addTimePeriod_btn,"Add Time Period", driver);
			
			helpers.Support.PageLoadExternalwait(driver);
			System.out.println("Added the Opening Closing Time Period For Core Hours");
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}


	public void selectDaysForSupplementaryHoursType(String key) {
		try{
			String exl_SupplementaryHoursType = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "OpeningHours", "SupplementaryHoursApplicableDays", key);
			//String exl_CoreHoursType = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "OpeningHours", "CoreHoursApplicableDays", key);
									
			List<String> daysCoreHoursType=helpers.MEHelpers.getListofValue(exl_SupplementaryHoursType);	
			
			for (String days : daysCoreHoursType) 
			{				
				helpers.CommonFunctions.ClickOnButtonWebElement(selectApplicableDays, days, driver);	
			}	
			
				helpers.CommonFunctions.ClickOnButtonWebElement(selectHours_rdiobtn, "Supplementary Hours", driver);
				
				System.out.println("Added the Days For Supplementary Hours Type");
				
			
	}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}


	public void addOpeningClosingTimePeriodForSupplementaryHours(String key) {
		try{
			
			String exl_OpeningH = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "OpeningHours", "SupplementaryHoursOpeningTime", key);
			String exl_ClosingH = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "OpeningHours", "SupplementaryHoursClosingTime", key);
			
			wait.until(ExpectedConditions.elementToBeClickable(openingTime_txtbox)).sendKeys(exl_OpeningH);
			wait.until(ExpectedConditions.elementToBeClickable(closingtime_txtbox)).sendKeys(exl_ClosingH);
			
			helpers.CommonFunctions.ClickOnButtonWebElement(addTimePeriod_btn,"Add Time Period", driver);
			
			helpers.Support.PageLoadExternalwait(driver);
			System.out.println("Added the Opening Closing Time Period For SupplementaryHours");
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}


	public void selectDaysForClosedallDayType(String key) {
		// TODO Auto-generated method stub
		try{
			String exl_ClosedallDayType = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "OpeningHours", "ClosedallDayApplicableDays", key);
			//String exl_CoreHoursType = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "OpeningHours", "CoreHoursApplicableDays", key);
									
			List<String> daysCoreHoursType=helpers.MEHelpers.getListofValue(exl_ClosedallDayType);	
			
			for (String days : daysCoreHoursType) 
			{				
				helpers.CommonFunctions.ClickOnButtonWebElement(selectApplicableDays, days, driver);	
			}	
			
				helpers.CommonFunctions.ClickOnButtonWebElement(selectClosed_chkbx, "Closed all Day", driver);
				
				helpers.CommonFunctions.ClickOnButtonWebElement(addTimePeriod_btn,"Add Time Period", driver);
				
				helpers.Support.PageLoadExternalwait(driver);
				System.out.println("Added the DaysForClosedallDayType");
				
			
	}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public EnhancedServices selectFloorPlanAndInformation(String key) {
		// TODO Auto-generated method stub
		try{
			String exl_FloorPlanInformation = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "OpeningHours", "FloorPlanInformation", key);
			
				uploadFile(upload_txt_box,clickFile_Upload_btn);
				
				wait.until(ExpectedConditions.elementToBeClickable(floorPlan_txtarea)).sendKeys(exl_FloorPlanInformation);
				
				helpers.CommonFunctions.ClickOnButton("Save & Next", driver);				
				
				helpers.Support.PageLoadExternalwait(driver);
				System.out.println("Added the select Floor Plan And Information");
				
			
	}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new EnhancedServices(driver);
		
	}
	
	public void uploadFile(WebElement fileTxtBox,WebElement uploadButton) 
	{	
		try{

			List<String> Extensions = ExcelUtilities.getCellValuesInExcel("MEAPP.xlsx", "FileExtension", 1);
			for(String Extension : Extensions)
			{
				//scrolltoElement(driver, Uploadfilebutton);
				
				String filePath = System.getProperty("user.dir") + "\\Upload\\"+Extension;			
				//helpers.CommonFunctions.Uploadfile(filePath, driver);
				fileTxtBox.sendKeys(filePath);
				Thread.sleep(3000);
				wait.until(ExpectedConditions.elementToBeClickable(uploadButton)).click();
				helpers.Support.PageLoadExternalwait(driver);
				//wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='loader']")));
			
			}		
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		}

}
