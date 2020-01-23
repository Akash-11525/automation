package pageobjects.CS;

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

public class DeferPopup {
	WebDriver driver;
	WebDriverWait wait;
	
	String fileFolderPath = System.getProperty("user.dir") + "/Upload/";
	
	@FindBy(xpath="//table[@id='pcss-patient-non-responders']/tbody/tr[1]/td[9]/button")
	WebElement submitButton;
	
	@FindBy(css="div[class*='modal fade in']")
	WebElement ceaseModalWindowActive;
	
	@FindBy(xpath="//table[@class='table table-responsive table-striped']/tbody//select[@id='delayReason']")
	WebElement selectDeferReasonsOptions;
	
	@FindBy(id="deferNHSNumber")
	WebElement deferNHSNumber;
	
	@FindBy(id="deferPatientName")
	WebElement deferPatientName;
	
	@FindBy(id="delayReason")
	WebElement delayReasonDD;
	
	@FindBy(id="delayDuration")
	WebElement delayDurationDD;
	
	@FindBy(id="deferFileUpload")
	WebElement deferFileUpload;
	
	@FindBy(id="cancelDefer")
	WebElement deferPatientCancelButton;
	
	@FindBy(xpath="//h2")
	WebElement notificationListHeader;
	
	@FindBy(id="deferSubmit")
	WebElement deferPatientSubmitButton;
	
	@FindBy(xpath = "//table[@id='pcss-patient-non-responders']/tbody/tr[1]/td[9]/span")
	WebElement ceasedTickMarkOnNRL;
	
	
	String DeferOption1 = utilities.ExcelUtilities.getKeyValueFromExcel("CSTESTDATA.xlsx", "Options", "DeferReason1");
	String DeferOption2 = utilities.ExcelUtilities.getKeyValueFromExcel("CSTESTDATA.xlsx", "Options", "DeferReason2");
	String DeferOption3 = utilities.ExcelUtilities.getKeyValueFromExcel("CSTESTDATA.xlsx", "Options", "DeferReason3");
	
	String DeferDelayDuration1 = utilities.ExcelUtilities.getKeyValueFromExcel("CSTESTDATA.xlsx", "Options", "DeferDelayDuration1");
	String DeferDelayDuration2 = utilities.ExcelUtilities.getKeyValueFromExcel("CSTESTDATA.xlsx", "Options", "DeferDelayDuration2");
	String DeferDelayDuration3 = utilities.ExcelUtilities.getKeyValueFromExcel("CSTESTDATA.xlsx", "Options", "DeferDelayDuration3");

	public DeferPopup(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		PageFactory.initElements(this.driver, this);

	}
	
	public boolean CheckDeferPopupUp() throws InterruptedException
	{
		boolean flag = false;
		try
		{
				
		if (ceaseModalWindowActive.isDisplayed())
			flag = true;
		
				
		}
		catch(NoSuchElementException e)
		{
			System.out.println("Either Submit button is not enabled or Cease Submit Modal window is not present.");
		}
		
		return flag;
	}
	
	public List<String> getReasonForDefer()
	{
		List<String> options = helpers.CommonFunctions.getAllOptions(selectDeferReasonsOptions);
		return options;
				
	}
	
	public String getNHSNoOfDeferPatient() {
		String NHSNo = deferNHSNumber.getText().replaceAll("\\s+","");
		System.out.println("The NHSNUMBER on Cease Modal Window: "+NHSNo);
		return NHSNo;
	}
	
	
	public String getDeferPatientName() {
		String patientName = deferPatientName.getText();
		System.out.println("The Patient name on Cease Modal Window: "+patientName);
		return patientName;
	}
	
	public void selectReasonAndUploadFile(String reason,String duration, String fileName ) throws InterruptedException
	{
		selectDeferReason(reason);
		selectDeferDuration(duration);
		chooseDeferFile(fileName);
	}
	
	public void selectDeferReason(String reason) throws InterruptedException
	{
		Select dropdown = new Select(delayReasonDD);
		dropdown.selectByVisibleText(reason);
			
		}
	
	public void selectDeferDuration(String duration)
	{
		Select dropdown = new Select(delayDurationDD);
		dropdown.selectByVisibleText(duration);
	}
	
	public void chooseDeferFile(String fileName)
	{
		wait.until(ExpectedConditions.elementToBeClickable(deferFileUpload));
		String file = fileFolderPath+fileName;
		deferFileUpload.sendKeys(file);
	}
	
	public NotificationLists clickOnDeferCeaseCancelButton() throws InterruptedException
	{
		
		Thread.sleep(2000);
		wait.until(ExpectedConditions.elementToBeClickable(deferPatientCancelButton)).click();
		wait.until(ExpectedConditions.visibilityOf(notificationListHeader));
		//Thread.sleep(2000);
		return new NotificationLists(driver);
		
		}
	
	 public Boolean Deferoptions()
	 {
		 Boolean optionFlag = false;
		 List<String> options = getReasonForDefer();
		 
		 if(options.contains(DeferOption1) && options.contains(DeferOption2) && options.contains(DeferOption3))
			{
			 optionFlag = true;
			}
				 
		 return optionFlag;
		 
	 }
	 
	 public NotificationLists clickOnDeferCeaseSubmitButton() throws InterruptedException
		{
			
			try{
			Thread.sleep(2000);
			wait.until(ExpectedConditions.elementToBeClickable(deferPatientSubmitButton)).click();
			//Thread.sleep(5000);
			//wait.until(ExpectedConditions.attributeContains(ceasePatientSubmitButton, "disabled", "disabled"));
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("cancelDefer")));
			}
			
			catch (Exception e){
				e.printStackTrace();
			}
			return new NotificationLists(driver);
			}
	
	 public Boolean VerifyDeferConfirmNRL() {
		  boolean f0 = false;
      try
      {
      Thread.sleep(3000);
      String Stylevalue = ceasedTickMarkOnNRL.getAttribute("style");
      System.out.println(Stylevalue);
      if (Stylevalue.equalsIgnoreCase("display: block;"))
      {
                  f0 = true;        
              
            
      }
      else
      {
    	  System.out.println("Ceasing is not performed sucessfully due to some error");
      }
      }
      
      catch(Exception e)
      {
             System.out.println("Found Exception : " + e);
      }
      
      return f0;
	}
	 public PatientpersonaldetailPortal clickOnDeferSubmitButton() throws InterruptedException
		{
			
			try{
			Thread.sleep(2000);
			wait.until(ExpectedConditions.elementToBeClickable(deferPatientSubmitButton)).click();
			//Thread.sleep(5000);
			//wait.until(ExpectedConditions.attributeContains(ceasePatientSubmitButton, "disabled", "disabled"));
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("cancelDefer")));
			}
			
			catch (Exception e){
				e.printStackTrace();
			}
			return new PatientpersonaldetailPortal(driver);
			}

	 public NonResponders clickOnDeferSubmitButton_NonResponder() throws InterruptedException
		{
			
			try{
			Thread.sleep(2000);
			wait.until(ExpectedConditions.elementToBeClickable(deferPatientSubmitButton)).click();
			//Thread.sleep(5000);
			//wait.until(ExpectedConditions.attributeContains(ceasePatientSubmitButton, "disabled", "disabled"));
			
			}
			
			catch (Exception e){
				e.printStackTrace();
			}
			return new NonResponders(driver);
			}

}
