package pageobjects.GPP.Pensions.AVC;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Screenshot;
import helpers.Support;
import helpers.WindowHandleSupport;

public class ERRBOApprovalForm  extends Support{
	WebDriver driver;
	WebDriverWait wait;

	@FindBy(xpath="//div[@id='dvErrboApprovalForm']//div[3]/div[1]/strong")
	WebElement refNo;
	
	@FindBy(id="TickToConfirmErrbo")
	WebElement tickToConfirm;
	
	@FindBy(id="submitbtn")
	WebElement submitBtn;
	
	@FindBy(xpath="//div[@id='divConfirmSubmit']//button[@class='btn btn-success']")
	WebElement confirmSubmit;
	
	@FindBy(xpath="//div[@id='divConfirmSubmit']//button[@class='btn btn-default']")
	WebElement cancelSubmit;
	
	public ERRBOApprovalForm(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}
	
	public ERRBOApprovalForm clickOnSubmit() throws InterruptedException {
		scrolltoElement(driver, tickToConfirm);
		wait.until(ExpectedConditions.elementToBeClickable(tickToConfirm));
		if(!(tickToConfirm.isSelected())){
			tickToConfirm.click();
		}
		scrolltoElement(driver, submitBtn);
		wait.until(ExpectedConditions.elementToBeClickable(submitBtn)).click();
		Thread.sleep(2000);
		System.out.println("Clicked on submit button under ERRBO approval form");
		return new ERRBOApprovalForm(driver);
	}
	
	public ERRBOApprovalForm clickOnCancelSubmit() throws InterruptedException {
		WebElement modalWindow= driver.findElement(By.xpath("//div[@id='divConfirmSubmit']//div[@class='modal-content']"));
		wait.until(ExpectedConditions.visibilityOf(modalWindow));
		if(modalWindow.isDisplayed()){
			scrolltoElement(driver, cancelSubmit);
			wait.until(ExpectedConditions.elementToBeClickable(cancelSubmit)).click();
			Thread.sleep(2000);
			System.out.println("Clicked on cancel submit under ERRBO approval form");
		}
		return new ERRBOApprovalForm(driver);
	}
	
	public ERRBOApprovalForm clickOnConfirmSubmit() throws InterruptedException {
		WebElement modalWindow= driver.findElement(By.xpath("//div[@id='divConfirmSubmit']//div[@class='modal-content']"));
		wait.until(ExpectedConditions.visibilityOf(modalWindow));
		if(modalWindow.isDisplayed()){
			scrolltoElement(driver, confirmSubmit);
			wait.until(ExpectedConditions.elementToBeClickable(confirmSubmit)).click();
			Thread.sleep(7000);
			System.out.println("Clicked on confirm submit under ERRBO approval form");
		}
		return new ERRBOApprovalForm(driver);
	}
	
	public boolean verifyPresenceOfDocument(String refNumber) throws InterruptedException {
		Thread.sleep(5000);
		WindowHandleSupport.getRequiredWindowDriverWithIndex(driver, 1);
		boolean isApplicationPresent= false;
		String docNo= refNo.getText().toString();
		if(refNumber.equalsIgnoreCase(docNo)){
			isApplicationPresent= true;
		}
		return isApplicationPresent;
	}
	
	public void captureERRBOApprovalSnaps(String note) throws InterruptedException, IOException {

		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,800)", "");
		Screenshot.TakeSnap(driver, note+"_2");
	}
}
