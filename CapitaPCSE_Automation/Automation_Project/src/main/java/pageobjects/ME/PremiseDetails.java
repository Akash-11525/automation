package pageobjects.ME;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

public class PremiseDetails extends Support{
	
WebDriver driver;
WebDriverWait wait;

@FindBy(xpath=ConstantOR.PremisesName_txtbox)
WebElement PremisesName;

@FindBy(xpath=ConstantOR.WellbeingBoard_dropdown)
WebElement WellbeingBoard;

@FindBy(xpath=ConstantOR.Closeproximity_radioBtn)
List<WebElement> selectCloseproximity_radioBtn;

@FindBy(xpath=ConstantOR.Regulation31_txtarea)
WebElement enterRegulation31_txtarea;

@FindBy(xpath=ConstantOR.Pharma_GPPractice_radioBtn)
List<WebElement> selectPharma_GPPractice_radioBtn;

@FindBy(xpath=ConstantOR.Regulation25_txtarea)
WebElement enterRegulation25_txtarea;

@FindBy(xpath=ConstantOR.Confirm_PharmaceuticalList_chekbox)
List<WebElement> selectConfirm_PharmaceuticalList;

//*[@id='UploadLetterCopy']/div/input
@FindBy(id=ConstantOR.File_txt_box)
WebElement enterfilePath;

@FindBy(xpath=ConstantOR.File_Upload_btn)
WebElement clickFile_Upload_btn;

@FindBy(xpath=ConstantOR.Confirm_Exerciserightofreturn_chekbox)
List<WebElement> selectExerciserightofreturn;

@FindBy(xpath=ConstantOR.Confirm_LocalPharmaceuticalsServices_rdiobtn)
List<WebElement> selectLocalPharmaceuticalsServices;

@FindBy(xpath=ConstantOR.Confirm_DispensingPrescriptions)
List<WebElement> selectDispensingPrescriptions;

@FindBy(xpath=ConstantOR.ReasonsForConfirmed_txtArea)
WebElement enterReasonsForConfirmed;

@FindBy(xpath=ConstantOR.Save_Next_Buttn_xpath)
WebElement nextSaveButton;

@FindBy(xpath="//div[@id='divLPS']//div[@class='radio checkbox-sg']//label")
List<WebElement> LPS;


	
public PremiseDetails(WebDriver driver) 
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 60);
		PageFactory.initElements(this.driver, this);
	}

public AddressManually EnterPremiseDetails(String key) {
	// TODO Auto-generated method stub
	try{
		String Exl_PremisesName = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "PremiseDetails", "PremisesName", key);
		String Exl_WellbeingBoard = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "PremiseDetails", "WellbeingBoard", key);		
		
		wait.until(ExpectedConditions.elementToBeClickable(PremisesName)).sendKeys(Exl_PremisesName);
		
		Select dropdown = new Select(WellbeingBoard);
		dropdown.selectByVisibleText(Exl_WellbeingBoard);		
		
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	return new AddressManually(driver);
}

public MarketEntryApplication selectPremisesOptions(String key) {
	try{
		//String exl_ProposedLocation = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "PremiseDetails", "ProposedLocation", key);
		//String Exl_AdditionalInfo = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "PremiseDetails", "AdditionalInfo", key);
		String exl_Closeproximity = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "PremiseDetails", "Closeproximity", key);
		String exl_Regulation31 = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "PremiseDetails", "Regulation31", key);
		String exl_GPPractice = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "PremiseDetails", "GPPractice", key);
		String exl_Regulation25 = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "PremiseDetails", "Regulation25", key);
		String exl_ConfirmPharmaList = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "PremiseDetails", "ConfirmPharmaList", key);
		String exl_ConfirmExercise  = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "PremiseDetails", "ConfirmExercise", key);
		String exl_ConfirmPharmaProvide = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "PremiseDetails", "ConfirmPharmaProvide", key);
		String exl_ConfirmDispensingPrescriptions = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "PremiseDetails", "ConfirmDispensingPrescriptions", key);
		String exl_NotConfirmedPrescriptions = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "PremiseDetails", "NotConfirmedPrescriptions", key);	
	
	
		helpers.CommonFunctions.ClickOnButtonWebElement(selectCloseproximity_radioBtn, exl_Closeproximity, driver);	
	
		if(exl_Closeproximity.equalsIgnoreCase("Yes"))
		{
			wait.until(ExpectedConditions.elementToBeClickable(enterRegulation31_txtarea)).sendKeys(exl_Regulation31);
		}
		
		helpers.CommonFunctions.ClickOnButtonWebElement(selectPharma_GPPractice_radioBtn, exl_GPPractice, driver);
		
		if(exl_GPPractice.equalsIgnoreCase("Yes"))
		{
			wait.until(ExpectedConditions.elementToBeClickable(enterRegulation25_txtarea)).sendKeys(exl_Regulation25);
		}
		
		helpers.CommonFunctions.ClickOnButtonWebElement(selectConfirm_PharmaceuticalList, exl_ConfirmPharmaList, driver);	
		
		uploadFile(enterfilePath,clickFile_Upload_btn);
		
		helpers.CommonFunctions.ClickOnButtonWebElement(selectExerciserightofreturn, exl_ConfirmExercise, driver);
		
		helpers.CommonFunctions.ClickOnButtonWebElement(selectLocalPharmaceuticalsServices, exl_ConfirmPharmaProvide, driver);
		
		helpers.CommonFunctions.ClickOnButtonWebElement(selectDispensingPrescriptions, exl_ConfirmDispensingPrescriptions, driver);
		
		if(exl_ConfirmDispensingPrescriptions.equalsIgnoreCase("Not Confirmed"))
		{
			wait.until(ExpectedConditions.elementToBeClickable(enterReasonsForConfirmed)).sendKeys(exl_NotConfirmedPrescriptions);
		}
		
		helpers.CommonFunctions.clickElementByXpath(nextSaveButton, driver);
		
	}catch(Exception e)
	{
		e.printStackTrace();
		System.out.println("Primises Details completed entred");
	}
	return new MarketEntryApplication(driver);
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
