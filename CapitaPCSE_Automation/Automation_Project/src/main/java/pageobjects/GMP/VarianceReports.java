package pageobjects.GMP;
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
import testdata.ConfigurationData;

public class VarianceReports extends Support {

	WebDriver driver;
	WebDriverWait wait;
  
	@FindBy(id="BtnGeneratePaymentsInstructionFile")
	WebElement GeneratePayment;
	
	@FindBy(xpath="//div[@id='ModalContainer']/div/div")
	WebElement FileVerificationwindow;
	
	@FindBy(xpath="//*[@id='ModalContainer']/div/div/div[3]/div/div[2]/button")
	WebElement ConfirmButton;
	
	@FindBy(xpath="//*[@id='ResponseModalContainer']//div[@class='modal-content']")
	WebElement OKDialogueBox;
	
	@FindBy(xpath="//div[@id='ResponseModalContainer']//div[@class='modal-footer']/a")
	WebElement OKbutton;
	
	@FindBy(xpath="//div[@class='col-xs-12 col-sm-8 text-right margin-top-10']/a")
	WebElement PaymentInstructionFile;	
	
	@FindBy(xpath="//table[@id='VarianceReportDataTable']//tbody//td[2]/a")
	WebElement ContractorName;	

	@FindBy(css="a[role='button']")
	WebElement Backbutton;
	
	
	public VarianceReports(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 80);	
		PageFactory.initElements(this.driver, this);
	}


	public VarianceReports clickonGeneratePaymentFile() {
	try{
		Thread.sleep(9000);
		scrolltoElement(driver, GeneratePayment);
		wait.until(ExpectedConditions.elementToBeClickable(GeneratePayment)).click();
		wait.until(ExpectedConditions.elementToBeClickable(FileVerificationwindow));
		Thread.sleep(7000);
		if(FileVerificationwindow.isDisplayed())
		{
			wait.until(ExpectedConditions.elementToBeClickable(ConfirmButton)).click();
			
		}
		wait.until(ExpectedConditions.elementToBeClickable(OKDialogueBox));
		
		if(OKDialogueBox.isDisplayed())
		{
			wait.until(ExpectedConditions.elementToBeClickable(OKbutton)).click();	
			Thread.sleep(7000);
		}
		
	}
	   catch(Exception e)
    	{
		   System.out.println("The Generated Payment instruction file is not clicked" + e);  
    	}
		return new VarianceReports (driver);
	}


	public PaymentInstructionFile ClickonPaymentInstructionFile() {
		try{
			boolean ispresent = driver.findElements(By.xpath("//div[@class='col-xs-12 col-sm-8 text-right margin-top-10']/a")).size() != 0;
			System.out.println(ispresent);
			while(!ispresent)
			{
				System.out.println(ispresent);
				RefreshPageGMP();
				Thread.sleep(2000);
				ispresent = driver.findElements(By.xpath("//div[@class='col-xs-12 col-sm-8 text-right margin-top-10']/a")).size() != 0;
			}
			
			wait.until(ExpectedConditions.elementToBeClickable(PaymentInstructionFile));
			wait.until(ExpectedConditions.elementToBeClickable(PaymentInstructionFile)).click();		
		}
		catch(Exception e)
		    {
				System.out.println("The View Generated Payment instruction file is not clicked" + e);
		     }
		return new PaymentInstructionFile (driver);
	}


	private void RefreshPageGMP() {
		try{
			scrolltoElement(driver, ContractorName);
			wait.until(ExpectedConditions.elementToBeClickable(ContractorName)).click();
			Thread.sleep(7000);
			scrolltoElement(driver, Backbutton);
			wait.until(ExpectedConditions.elementToBeClickable(Backbutton)).click();
			Thread.sleep(7000);
		}
		catch(Exception e)
		{
			System.out.println("The page Refresh is not done" +e);
		}
	}


	public boolean verifyPaymentHeaderPresence() {
		boolean isHeader= true;
		List<WebElement> headerGrid= driver.findElements(By.xpath("//table[@id='VarianceReportDataTable']//tbody/tr"));
		int headerCount= headerGrid.size();
		if(headerCount==0){
			isHeader=false;
		}else{
			isHeader=true;
		}
		return isHeader;
	}
	
	public VarianceReportDetail clickOnContractorName(String environment) {

		String contractorName= ConfigurationData.getRefDataDetails(environment, "OpthoContractor");
		List<WebElement> paymentHeaderGrid= driver.findElements(By.xpath("//table[@id='VarianceReportDataTable']/tbody/tr"));
		int headerCount= paymentHeaderGrid.size();
		for(int i=1;i<=headerCount;i++){
			WebElement elmContractor= driver.findElement(By.xpath("//table[@id='VarianceReportDataTable']/tbody/tr["+i+"]/td[2]"));
			scrolltoElement(driver, elmContractor);
			String strContractor= elmContractor.getText().toString();
			if(strContractor.equalsIgnoreCase(contractorName)){
				elmContractor.click();
				break;
			}	
		}
		return new VarianceReportDetail (driver);
	}
}
