package pageobjects.OP;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import helpers.Support;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class OPSupervisorsDeclaration extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	
	
	@FindBy(css="input[name='btnSave']")
	WebElement saveButton;
	
		//@FindBy(css="button[value='Save']")
	@FindBy(css="input[value='Save and Next']")
	WebElement saveAndNextButton;
	
	@FindBy(css="div[id='signature-pad']")
	WebElement signatureCanvasbox;

	public OPSupervisorsDeclaration(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}

	public OPContractorDeclaration clickOnSaveandNextButton() throws InterruptedException
	{
		scrolltoElement(driver, signatureCanvasbox);
		enterSignatory(signatureCanvasbox, driver);
		Thread.sleep(2000);
		scrolltoElement(driver, saveAndNextButton);
		wait.until(ExpectedConditions.elementToBeClickable(saveAndNextButton)).click();
		Thread.sleep(2000);
		return new OPContractorDeclaration(driver);
	}
	
}
