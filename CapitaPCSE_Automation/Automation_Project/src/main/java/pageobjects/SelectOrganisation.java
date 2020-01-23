package pageobjects;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.CommonFunctions;
import helpers.Support;
import pageobjects.GPP.CI.GPPHomePage;
import pageobjects.OP.OPHomePage;
import testdata.ConfigurationData;

public class SelectOrganisation extends Support {
	
WebDriver driver;
WebDriverWait wait;
    
    @FindBy(id="searchTxt")
    WebElement searchText;
    
	@FindBy(css="ul[id='organisationResults']")
	WebElement OrganisationNameResult;
    
	@FindBy(css="input[name='Update']")
	WebElement updateButton;

	@FindBy(css="select#Organisation")
	WebElement selectOrganisation;
	
	@FindBy(xpath="//select[@id='Organisation']")
	WebElement Organisation;
	

  
    public SelectOrganisation(WebDriver driver)
    {
           this.driver = driver;
           this.driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
           wait = new WebDriverWait(this.driver, 30);
           //this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
           PageFactory.initElements(this.driver, this);
    }
    
    
    public OPHomePage selectOrganisation(String environment) throws InterruptedException
	{
		
		System.out.println("We are on select organisation screen");
    	//String contractor = ConfigurationData.OPContractor;
		String contractor = ConfigurationData.getRefDataDetails(environment, "OpthoContractor");
		wait.until(ExpectedConditions.elementToBeClickable(searchText));
		CommonFunctions.setText(searchText, contractor);
		System.out.println("We are on select organisation screen");
		helpers.CommonFunctions.selectOptionFromList(OrganisationNameResult, contractor );
		wait.until(ExpectedConditions.visibilityOf(updateButton));
		wait.until(ExpectedConditions.elementToBeClickable(updateButton)).click();
		System.out.println("Update button on select organisation screen clicked.");
		
		Thread.sleep(5000);
		return new OPHomePage(driver);
		
		
	}
    
    public OPHomePage selectOrganisations_dropdown(String environment) throws InterruptedException
  	{
  		
  		String contractor = ConfigurationData.getRefDataDetails(environment, "OpthoContractor");
  		
  		//CommonFunctions.setText(searchText, contractor);
  		
  		wait.until(ExpectedConditions.elementToBeClickable(selectOrganisation));
  		helpers.CommonFunctions.selectOptionFromDropDown(selectOrganisation, contractor);
  		wait.until(ExpectedConditions.visibilityOf(updateButton));
  		wait.until(ExpectedConditions.elementToBeClickable(updateButton)).click();
  		Thread.sleep(5000);
  		return new OPHomePage(driver);
  		
  		
  	}
    
    public GPPHomePage selectOrganisation_GP() throws InterruptedException
	{
		String contractor = ConfigurationData.GPPContractor;
		wait.until(ExpectedConditions.elementToBeClickable(selectOrganisation));
		CommonFunctions.selectOptionFromDropDown(selectOrganisation, contractor);
		wait.until(ExpectedConditions.visibilityOf(updateButton));
		wait.until(ExpectedConditions.elementToBeClickable(updateButton)).click();
		Thread.sleep(5000);
		return new GPPHomePage(driver);
	}
    
    public OPHomePage selectOrganisation_dropdown(String contractor) throws InterruptedException
  	{

  		//CommonFunctions.setText(searchText, contractor);
  		
  		//helpers.CommonFunctions.selectOptionFromList(selectOrganisation, contractor );
    	wait.until(ExpectedConditions.elementToBeClickable(selectOrganisation));
  		helpers.CommonFunctions.selectOptionFromDropDown(selectOrganisation, contractor);
  		wait.until(ExpectedConditions.visibilityOf(updateButton));
  		wait.until(ExpectedConditions.elementToBeClickable(updateButton)).click();
  		Thread.sleep(5000);
  		return new OPHomePage(driver);
  		
  		
  	}
	
    public <T> T selectOrganisation_DropDown(String contractor,Class<T> expectedPage) throws InterruptedException
  	{
    	wait.until(ExpectedConditions.elementToBeClickable(selectOrganisation));
  		helpers.CommonFunctions.selectOptionFromDropDown(selectOrganisation, contractor);
  		wait.until(ExpectedConditions.visibilityOf(updateButton));
  		wait.until(ExpectedConditions.elementToBeClickable(updateButton)).click();
  		Thread.sleep(5000);
  		return PageFactory.initElements(driver, expectedPage);
  	}
    
    public <T> T selectOrganisation(String contractor,Class<T> expectedPage) throws InterruptedException
	{
    	wait.until(ExpectedConditions.elementToBeClickable(searchText));
		if(searchText.isDisplayed()){
			CommonFunctions.setText(searchText, contractor);
			helpers.CommonFunctions.selectOptionFromList(OrganisationNameResult, contractor );
			wait.until(ExpectedConditions.visibilityOf(updateButton));
			wait.until(ExpectedConditions.elementToBeClickable(updateButton)).click();
			Thread.sleep(5000);
		}else if(selectOrganisation.isDisplayed()){
			wait.until(ExpectedConditions.elementToBeClickable(selectOrganisation));
			helpers.CommonFunctions.selectOptionFromDropDown(selectOrganisation, contractor);
			wait.until(ExpectedConditions.visibilityOf(updateButton));
	  		wait.until(ExpectedConditions.elementToBeClickable(updateButton)).click();
	  		Thread.sleep(5000);
		}
		return PageFactory.initElements(driver, expectedPage);
	}
    
    
    public <T> T selectOrganisation_DropDown(String environment,Class<T> expectedPage,String refDataKey) throws InterruptedException
  	{
    	String contractor= ConfigurationData.getRefDataDetails(environment, refDataKey);
    	wait.until(ExpectedConditions.elementToBeClickable(selectOrganisation));
  		helpers.CommonFunctions.selectOptionFromDropDown(selectOrganisation, contractor);
  		wait.until(ExpectedConditions.visibilityOf(updateButton));
  		wait.until(ExpectedConditions.elementToBeClickable(updateButton)).click();
  		Thread.sleep(5000);
  		return PageFactory.initElements(driver, expectedPage);
  	}

}
