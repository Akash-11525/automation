package pageobjects.ME;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Support;
import utilities.ExcelUtilities;

public class FinalDeclaration extends Support{
	
	WebDriver driver;
	WebDriverWait wait;

	@FindBy(xpath=ConstantOR.RegisteredPharmacis_Checkbx)
	List<WebElement> pegisteredPharmacis_Checkbx;
	
	@FindBy(id=ConstantOR.FD_File_txt_box)
	WebElement enterfilePath;

	@FindBy(xpath=ConstantOR.FD_File_Upload_btn)
	WebElement clickFile_Upload_btn;
	
	public FinalDeclaration(WebDriver driver) 
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 60);
		PageFactory.initElements(this.driver, this);
	}
	
	public void selectDeclareRegisteredpharmacist(String key) {
		// TODO Auto-generated method stub
		try{
			
			String Exl_ChangeService = ExcelUtilities.getKeyValueByPosition("MEAPP.xlsx", "FinalDeclaration", "DeclareRegisteredpharmacist", key);
			
			helpers.CommonFunctions.ClickOnButtonWebElement(pegisteredPharmacis_Checkbx, Exl_ChangeService, driver);
			
			uploadFile(enterfilePath,clickFile_Upload_btn);
			
			helpers.CommonFunctions.ClickOnButton("Submit Application", driver);				
			
			helpers.Support.PageLoadExternalwait(driver);
			System.out.println("Added the Submit Application Done");
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void uploadFile(WebElement fileTxtBox,WebElement uploadButton) 
	{	
		try{

			List<String> Extensions = ExcelUtilities.getCellValuesInExcel("MEAPP.xlsx", "PhotoSignFile", 1);
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
