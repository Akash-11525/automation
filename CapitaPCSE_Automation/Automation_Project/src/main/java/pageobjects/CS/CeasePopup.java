package pageobjects.CS;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import utilities.*;

public class CeasePopup {
	WebDriver driver;
	WebDriverWait wait;
	
	 String CeaseReason1 = ExcelUtilities.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Options", "CeaseReason1");
	 String CeaseReason2 = ExcelUtilities.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Options", "CeaseReason3");
	 String CeaseReason3 = ExcelUtilities.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Options", "CeaseReason3");
	 String CeaseReason4 = ExcelUtilities.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Options", "CeaseReason4");
	 String CeaseReason5 = ExcelUtilities.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Options", "CeaseReason5");
	
	@FindBy(css="div[class*='modal fade in']")
	WebElement ceaseModalWindowActive;
	
    @FindBy(xpath="//table[@class='table table-responsive table-striped']/tbody//select[@id='CeaseReason']")
	WebElement selectReasonsOptions;
    
	@FindBy(id="ceaseFileUpload")
	WebElement ceaseFileUpload;	
    
    @FindBy(id="ceaseSubmit")
    WebElement Save_submitOnInline;
    
    @FindBy(xpath ="//button[@class='close cease-cancel']")
    WebElement ClosebuttonOnInline;
	
	public CeasePopup(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		PageFactory.initElements(this.driver, this);

	}
	
	 public boolean clickSubmitAndCheckPopupUp() throws InterruptedException
	    {
	           boolean flag = false;
	           try
	           {
	           
	        //   wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
	           Thread.sleep(2000);
	           
	           if (ceaseModalWindowActive.isDisplayed())
	                  flag = true;
	           
	           //wait.until(ExpectedConditions.visibilityOf(ceaseModalWindowActive));
	           
	                        
	           
	           }
	           catch(Exception e)
	           {
	                  System.out.println("Found Exception : " + e);
	           }
	           
	           return flag;
	           
		
	    }
	 
	 public Boolean Ceaseoptions()
	 {
		 Boolean optionFlag = false;
		 List<String> options = getReasonForCease();
		
		 if(options.contains(CeaseReason1) && options.contains(CeaseReason2) && options.contains(CeaseReason3))
			{
			 optionFlag = true;
			}
				 
		 return optionFlag;
		 
	 }
	 
	 public int GetCeaseOptionsCount()
	 {
		 Boolean optionFlag = false;
		 int count =0;
		 List<String> options = getReasonForCease();
		 
		for(String option:options)
		{
		// if(options.contains(CeaseReason1) && options.contains(CeaseReason2) && options.contains(CeaseReason3)&& options.contains(CeaseReason4) &&options.contains(CeaseReason5) )
			 if(option.equalsIgnoreCase(CeaseReason1)|| option.equalsIgnoreCase(CeaseReason2) || option.equalsIgnoreCase(CeaseReason3) || option.equalsIgnoreCase(CeaseReason4) || option.equalsIgnoreCase(CeaseReason5) )	
		 {
			 System.out.println("The option is dispalyed : "+option);
			}
			 else 
			 {
				 count = 1;
			 }
		}
				 
		 return count;
		 
	 }
	 
	  public List<String> getReasonForCease()
		{
		
			List<String> options = helpers.CommonFunctions.getAllOptions(selectReasonsOptions);
			
			return options;
			
			
		}
	  
		public CeasePopup selectCeasingReasonandUploadFile(String reason) throws InterruptedException {
			Select dropdown = new Select(driver.findElement(By.id("CeaseReason")));
			dropdown.selectByVisibleText(reason);
			
			if (reason.contentEquals("Informed Consent"))
			{
			
			wait.until(ExpectedConditions.elementToBeClickable(ceaseFileUpload));
			
			String filePath = System.getProperty("user.dir") + "\\Upload\\Sample.pdf";
			System.out.println(filePath);
			ceaseFileUpload.sendKeys(filePath);
			Thread.sleep(5000);
		
			}
			return new CeasePopup(driver);
		}
		
		
		public CeasePopup selectCeasingReason(String reason) throws InterruptedException {
			Select dropdown = new Select(driver.findElement(By.id("CeaseReason")));
			dropdown.selectByVisibleText(reason);
			
			return new CeasePopup(driver);
		}
		
		
		 public PNLPage submitCease() {
				
			 try
	        {
	        
				// new Select(DropdownOnInline).selectByVisibleText("Due to Age"); 
				 
	        wait.until(ExpectedConditions.elementToBeClickable(Save_submitOnInline)).click();
	       
	        Actions actions = new Actions(driver);
	    	actions.moveToElement(ClosebuttonOnInline);
	    	actions.click().build().perform();
	        
	                     
	        
	        }
	        catch(Exception e)
	        {
	               System.out.println("The Element is not found in Inline Diolague Box " + e);
	        }
	        
			// TODO Auto-generated method stub
			 return new PNLPage(driver);
		}
	    
		 public PatientpersonaldetailPortal submitCease_Patientpersonaldetail() {
				
			 try
	        {
	        
				// new Select(DropdownOnInline).selectByVisibleText("Due to Age"); 
				 
	        wait.until(ExpectedConditions.elementToBeClickable(Save_submitOnInline)).click();
	       
	        Actions actions = new Actions(driver);
	    	actions.moveToElement(ClosebuttonOnInline);
	    	actions.click().build().perform();
	        Thread.sleep(5000);
	                     
	        
	        }
	        catch(Exception e)
	        {
	               System.out.println("The Element is not found in Inline Diolague Box " + e);
	        }
	        
			// TODO Auto-generated method stub
			 return new PatientpersonaldetailPortal(driver);
		}
}
