package pageobjects.GPP.Pensions.AVC;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Support;
import pageobjects.GPP.Pensions.PracticeJoiner.CommissionerAppListing;

public class AVCHomePage extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(xpath="//div[@id='divMainContainer']/div[3]/div[2]/div[1]/a")
	WebElement AVCForm;
	
	@FindBy(xpath="//div[@id='divMainContainer']/div[3]/div[2]/div[2]/a")
	WebElement MPAVCForm;
	
	@FindBy(xpath="//div[@id='divMainContainer']/div[3]/div[2]/div[3]/a")
	WebElement ERRBOForm;
	
	public AVCHomePage(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}
	
	public AVCPurchaseForm ClickOnAVCAppForm() throws InterruptedException {
		Thread.sleep(2000);
		scrolltoElement(driver, AVCForm);
		wait.until(ExpectedConditions.elementToBeClickable(AVCForm)).click();
		Thread.sleep(2000);
		return new AVCPurchaseForm(driver);
	}
	
	public MPAVCInstructionForm ClickOnMPAVC() throws InterruptedException {
		Thread.sleep(2000);
		scrolltoElement(driver, MPAVCForm);
		wait.until(ExpectedConditions.elementToBeClickable(MPAVCForm)).click();
		Thread.sleep(2000);
		return new MPAVCInstructionForm(driver);
	}
	
	public ERRBOApplication ClickOnERRBOForm() throws InterruptedException {
		Thread.sleep(2000);
		scrolltoElement(driver, ERRBOForm);
		wait.until(ExpectedConditions.elementToBeClickable(ERRBOForm)).click();
		Thread.sleep(2000);
		return new ERRBOApplication(driver);
	}

}
