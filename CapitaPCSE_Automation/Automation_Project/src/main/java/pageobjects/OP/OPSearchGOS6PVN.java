package pageobjects.OP;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.CommonFunctions;
import testdata.ConfigurationData;

public class OPSearchGOS6PVN {
	
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="ContractorName")
	WebElement contractorNameTxt;
	
	@FindBy(id="ContractorNameResult")
	WebElement contratorNameResult;
	
	@FindBy(id="PVNReferenceNumber")
	WebElement pvnReferenceNumberTxt;
	
	@FindBy(id="btnPVNSearch")
	WebElement pvnSearchButton;
	
	@FindBy(xpath="//table[@id='SearchPVNTable']/tbody/tr/td[8]")
	WebElement pvnOpenButton;
	
	@FindBy(xpath="//table[@id='SearchPVNTable']/tbody/tr/td[6]/input")
	WebElement pvnAmendButton;

	public OPSearchGOS6PVN(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);
	}
	
	public OPGOS6PVNView searchGOS6VPN (String refNo, String environment) throws InterruptedException
	{
		String contractor = ConfigurationData.getRefDataDetails(environment, "OpthoContractor");
		CommonFunctions.setText(contractorNameTxt, contractor);
		helpers.CommonFunctions.selectOptionFromList(contratorNameResult, contractor );
		
		wait.until(ExpectedConditions.elementToBeClickable(pvnReferenceNumberTxt)).click();
		pvnReferenceNumberTxt.clear();
		pvnReferenceNumberTxt.sendKeys(refNo);
		
		wait.until(ExpectedConditions.elementToBeClickable(pvnSearchButton)).click();
		wait.until(ExpectedConditions.elementToBeClickable(pvnOpenButton)).click();
		return new OPGOS6PVNView(driver);
		
		
	}
	
	public OPCreateGOS6PVN searchGOS6VPNAndClickOnAmendBtn (String refNo, String environment) throws InterruptedException
	{
		wait.until(ExpectedConditions.visibilityOf(contractorNameTxt));
		if (!(CommonFunctions.isAttribtuePresent(contractorNameTxt, "disabled")))
		{
			String contractor = ConfigurationData.getRefDataDetails(environment, "OpthoContractor");
			CommonFunctions.setText(contractorNameTxt, contractor);
			helpers.CommonFunctions.selectOptionFromList(contratorNameResult, contractor );
		}
/*		String contractor = ConfigurationData.getRefDataDetails(environment, "OpthoContractor");
		CommonFunctions.setText(contractorNameTxt, contractor);
		helpers.CommonFunctions.selectOptionFromList(contratorNameResult, contractor );*/
		
		wait.until(ExpectedConditions.elementToBeClickable(pvnReferenceNumberTxt)).click();
		pvnReferenceNumberTxt.clear();
		pvnReferenceNumberTxt.sendKeys(refNo);
		
		wait.until(ExpectedConditions.elementToBeClickable(pvnSearchButton)).click();
		wait.until(ExpectedConditions.elementToBeClickable(pvnAmendButton)).click();
		return new OPCreateGOS6PVN(driver);
	}

}
