package pageobjects.CS;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import helpers.Screenshot;
import helpers.Support;

public class PatientpersonaldetailPortal extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(xpath= "//div[@id='pendingCeaseDiv']")
	WebElement ActionPendingcancel;
	
	@FindBy(id="showCancelCeasePopupButton")
	WebElement CancelCease;
	
	@FindBy(id="showCeasePopupButton")
	WebElement Ceasebutton;
	
	@FindBy(xpath= "//*[@id='genericmodal']/div/div/div[2]")
	WebElement ConfirmationWarningMessage;
	
	@FindBy(xpath="//*[@id='genericmodal']/div/div")
	WebElement ConfirmationDialogueBox;
	
	@FindBy(xpath= "//button[@class='btn btn-primary pcss-submit-genericmodal']")
	WebElement SaveandSubmit;
	
	@FindBy(id= "showDeferPopupButton")
	WebElement DeferButton;
	
	@FindBy(xpath= "//div[@class='modal-dialog']")
	WebElement unceasedialguebox;
	
	@FindBy(xpath= "//*[@id='genericmodal']/div/div/div[3]/button[2]")
	WebElement SaveSubmitonInline;
	
	@FindBy(css="div[class*='modal fade in']")
	WebElement ceaseModalWindowActive;
	
	@FindBy(id= "ceasedDiv")
	WebElement CeaseMessage;
	
	
	@FindBy(xpath= "//div[@class='panel panel-default']//table//tr[5]//i")
	WebElement IconforDOB;
	
	@FindBy(linkText="HPV Vaccinations")
	WebElement HPVTab;
	

	
	public PatientpersonaldetailPortal(WebDriver driver){

		this.driver = driver;
		
		driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 60);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);
		
		}


	public Boolean VerifyactionPendingcancel() {
		
		boolean actionpendingcancel = false;
		if (ActionPendingcancel.isDisplayed())
		{
			String Actionbeforecancel = wait.until(ExpectedConditions.elementToBeClickable(ActionPendingcancel)).getText();
			if(Actionbeforecancel.equalsIgnoreCase("Pending Cease"))
			{
				actionpendingcancel = true;
			}
			else
			{
				actionpendingcancel = false;
				
			}
		}
		
			
			else {
				actionpendingcancel = false;
			}
		
			
		
		return actionpendingcancel;	
	}


	public boolean VerifyCancelCease() {
		boolean f1 = false;	
		String ButtonValue = wait.until(ExpectedConditions.elementToBeClickable(CancelCease)).getText();
		System.out.println(ButtonValue);
		if(ButtonValue.equalsIgnoreCase("Cancel Cease"))
		{
			f1 = true;
			
		}
		else
		{
			f1= false;
		}
			
		
		return f1;
	}


	
		public PatientpersonaldetailPortal ClickOnCancelCease() throws InterruptedException
		{
				
			wait.until(ExpectedConditions.elementToBeClickable(CancelCease)).click();	
			Thread.sleep(3000);
			
			return new PatientpersonaldetailPortal(driver);
		}
		
		public CeasePopup ClickonCease() throws InterruptedException
		{
				
			wait.until(ExpectedConditions.elementToBeClickable(Ceasebutton)).click();	
			Thread.sleep(3000);
			
			return new CeasePopup(driver);
		}
		
		public PatientpersonaldetailPortal ConfirmationDialgoueBox()
		{
			
			if(ConfirmationDialogueBox.isDisplayed())
			{
				String WarningMessage = ConfirmationWarningMessage.getText();
				System.out.println(WarningMessage);
				String ExpectedWarningMessage1 = "Are you sure you want to Cancel the Pending Cease Request including all attached documents?";
				String ExpectedWarningMessage2 ="Click Yes to Proceed and Cancel to return to the previous Page.";
				String ActualWarningMessage1 = WarningMessage.split("\n")[0].trim();
				System.out.println(ActualWarningMessage1);
				if(ActualWarningMessage1.equalsIgnoreCase(ExpectedWarningMessage1))
				{
					String ActualWarningMessage2 = WarningMessage.split("\n")[1].trim();	
					System.out.println(ActualWarningMessage2);
					
					if(ActualWarningMessage2.equalsIgnoreCase(ExpectedWarningMessage2))
					{
						System.out.println("Warning Message is correct");
					}
				}
				
				
			}
			return new PatientpersonaldetailPortal(driver);
		}
	
		public PatientpersonaldetailPortal ClickOnSaveSubmit() {
			
			wait.until(ExpectedConditions.elementToBeClickable(SaveandSubmit)).click();
			
			return new PatientpersonaldetailPortal(driver);
		}
		
		public Boolean VerifyDeferButton() {
			
			boolean VerifyDeferButton = false;	
				
				String DeferButtonActualValue = wait.until(ExpectedConditions.elementToBeClickable(DeferButton)).getText();
				if(DeferButtonActualValue.equalsIgnoreCase("Defer"))
				{
					VerifyDeferButton = true;
				}
				else 
				{
					VerifyDeferButton = false;
				}
				return VerifyDeferButton;
			}


		public Boolean VerifyCease_clickonuncease() {
			boolean ceasebutton = false;
			try {
			       List<WebElement> buttons=driver.findElements(By.xpath("//button[@type='button']"));
                   System.out.println("total buttons "+buttons.size());
                  for (WebElement button : buttons)
                  {                   
                	//  String ButtonValue = wait.until(ExpectedConditions.elementToBeClickable(button)).getText();
                	   String ButtonValue = button.getText(); 
                	   System.out.println(ButtonValue);
                	  if(ButtonValue.equalsIgnoreCase("Cease"))
                	  {
                		  ceasebutton =  true;
                	  }
                	  if (ButtonValue.equalsIgnoreCase("Un-cease"))
                	  {
                		  button.click();
                		  Thread.sleep(3000);
                		  if (unceasedialguebox.isDisplayed()){
                			  wait.until(ExpectedConditions.elementToBeClickable(SaveSubmitonInline)).click();  
                			  break;
                		  }
                		  
                		  
                	  }
                	  
                   }

				
			}
			catch (Exception e){
				e.printStackTrace();
			}
			return ceasebutton;
			
		}


		public boolean VerifyUncease() {
			boolean unceasebutton = false;
			try {
			       List<WebElement> buttons=driver.findElements(By.xpath("//button[@type='button']"));
                   System.out.println("total buttons "+buttons.size());
                  for (WebElement button : buttons)
                  {                   
                	//  String ButtonValue = wait.until(ExpectedConditions.elementToBeClickable(button)).getText();
                	   String ButtonValue = button.getText(); 
                	   System.out.println(ButtonValue);
                	 
                	  if (ButtonValue.equalsIgnoreCase("Un-cease"))
                	  {
                		 
                		  unceasebutton = true;  
                		  break;
                		  
                	  }
                  }
                   

				
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return unceasebutton;
		}


		public DeferPopup clickondefer() {
			try {
			wait.until(ExpectedConditions.elementToBeClickable(DeferButton)).click();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return new DeferPopup(driver);
		}


		public Boolean VerifyChangedefer() {
			boolean Changedefer = false;
			try {
			       List<WebElement> buttons=driver.findElements(By.xpath("//button[@type='button']"));
                   System.out.println("total buttons "+buttons.size());
                  for (WebElement button : buttons)
                  {                   
                	//  String ButtonValue = wait.until(ExpectedConditions.elementToBeClickable(button)).getText();
                	   String ButtonValue = button.getText(); 
                	   System.out.println(ButtonValue);
                	               	 
                	  if (ButtonValue.contains("Change Deferral"))
                	  {
                		 
                		  Changedefer = true;  
                		  break;
                		  
                	  }
                  }
                   
		
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return Changedefer;
		}
	
		
		public Boolean Verifydefer() {
			boolean defer = false;
			try {
			       List<WebElement> buttons=driver.findElements(By.xpath("//button[@type='button']"));
                   System.out.println("total buttons "+buttons.size());
                  for (WebElement button : buttons)
                  {                   
                	//  String ButtonValue = wait.until(ExpectedConditions.elementToBeClickable(button)).getText();
                	   String ButtonValue = button.getText(); 
                	 
                	 
                	  if (ButtonValue.equalsIgnoreCase("Defer"))
                	  {
                		 
                		  defer = true;  
                		  break;
                		  
                	  }
                  }
                   
		
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return defer;
		}


		public boolean Verifyceasemessage() {
			boolean verifyCeasemessage= false;
			try{
				String ActualCeaseMessage = wait.until(ExpectedConditions.elementToBeClickable(CeaseMessage)).getText();
				System.out.println(ActualCeaseMessage);
				DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
				Date dateobj = new Date();
				System.out.println(df.format(dateobj));
				String d = (df.format(dateobj));
				String ExpectedCeaseMessage =  "Ceased on " +d;
				System.out.println(ExpectedCeaseMessage);
				if(ActualCeaseMessage.equalsIgnoreCase(ExpectedCeaseMessage))
				{
					verifyCeasemessage = true;
				}
				
		}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return verifyCeasemessage;
		}


		public String verifyToolTipValue() {
			String toolTipText = null;
			try{
				wait.until(ExpectedConditions.elementToBeClickable(IconforDOB));
				if(IconforDOB.isDisplayed())
				{
					Actions action = new Actions(driver);
					action.moveToElement(IconforDOB).build().perform();
					WebElement toolTipElement = driver.findElement(By.xpath("//div[@class='popover-content']"));
					

					// To get the tool tip text and assert
					toolTipText = toolTipElement.getText();
					System.out.println(toolTipText);
				}
				
				
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return toolTipText;
		}


		public Boolean verifyToolTipvalue(String ActualToolTipValue ,String expectedToolTipValue) {
			boolean ToolTip = false;
			try{
				if(ActualToolTipValue.equalsIgnoreCase(expectedToolTipValue))
				{
					ToolTip = true;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return ToolTip;
		}
		
		public PatientpersonaldetailPortal ClickOnHPVTab() throws InterruptedException
		{
			wait.until(ExpectedConditions.elementToBeClickable(HPVTab)).click();	
			Thread.sleep(3000);
			return new PatientpersonaldetailPortal(driver);
		}


		public boolean getHPVDataStatus(List<String> vaccineData) {
			boolean isVerified= false;
			int count=0;
			scrolltoElement(driver, HPVTab);
			String vaccineType= vaccineData.get(1).toString();
			String vaccineDate= vaccineData.get(2).toString();
			String username= driver.findElement(By.xpath("//div[@class='container']/ul/li[1]")).getText().toString();
			String organisation= driver.findElement(By.xpath("//div[@class='container']/ul/li[2]")).getText().toString();
			String strOrganisation= organisation.substring(0, 9);
			List<String> actualData= Arrays.asList(vaccineType,vaccineDate,username,strOrganisation);
			for(int i=0;i<actualData.size();i++){
				String actual= actualData.get(i);
				int colCount= driver.findElements(By.xpath("//table[@id='hpvvaccinationstable']//tbody/tr[1]/td")).size();
				for(int j=1;j<=colCount;j++){
					WebElement columnText= driver.findElement(By.xpath("//table[@id='hpvvaccinationstable']//tbody/tr[1]/td["+j+"]"));
					String expected= columnText.getText().toString();
					if(actual.equalsIgnoreCase(expected)){
						count=count+1;
						break;
					}
				}
			}
			
			if(actualData.size()==count){
				isVerified=true;
			}else{
				isVerified=false;
			}
			return isVerified;
		}


		public void capturePatientDetailsOnHPVTab(String note) throws InterruptedException, IOException {
			scrolltoElement(driver, HPVTab);
			Screenshot.TakeSnap(driver, note);
			Thread.sleep(1000);
			
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
			Screenshot.TakeSnap(driver, note+"_2");
		}
}
