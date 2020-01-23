package helpers;

import java.awt.AWTException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import pageobjects.AdvancedFilter;
import pageobjects.AdvancedFindResult;
import pageobjects.CrmHome;
import pageobjects.LoginScreen;
import pageobjects.GMP.GMPHome;
import pageobjects.GMP.PaymentInstructionFile;
import pageobjects.GMP.VarianceReports;
import pageobjects.OP.OPHomePage;
import pageobjects.OP.OPStatement;
import pageobjects.OP.OP_StatementDetails;
import testdata.ConfigurationData;
import utilities.ExcelUtilities;
import verify.Verify;

public class MEHelpers extends Support {
	static WebDriver driver;
	static WebDriverWait wait;
	public boolean Process;
	public List<String> AssertMessage;
	public String paymentRunName;
	
	@FindBy(css="button[class='btn btn-success']")
	static WebElement addressWindowSaveButton;
	
	
	
	public MEHelpers(WebDriver driver)
	{
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(driver, 30);

		//This initElements method will create all WebElements
		PageFactory.initElements(driver, this);
	}
	
	public MEHelpers(boolean Process , List<String> AssertMessage )
	{
		this.Process = Process;
		this.AssertMessage = AssertMessage;
	}	

	public MEHelpers(List<String> AssertMessage,String paymentRunName) 
	{
		this.AssertMessage = AssertMessage;
		this.paymentRunName= paymentRunName;
	}

	public static void uploadFile(WebElement fileTxtBox,WebElement uploadButton) 
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
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='loader']")));
			
			}		
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		}

	public static List<String> getListofValue(String exl_DaysForCoreHoursType) {
		// TODO Auto-generated method stub
		List<String> list =null;
		try{
			 String[] arrSplit = exl_DaysForCoreHoursType.split(",");
			    for (int i=0; i < arrSplit.length; i++)
			    {
			      System.out.println("Array Is : "+arrSplit[i]);
			    }
			   list = Arrays.asList(arrSplit);
			
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}


	
	


	
	
}
