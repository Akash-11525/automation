package pageobjects.Levies;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Support;

public class LocalCommitteeSearch extends Support {
	WebDriver driver;
	WebDriverWait wait;	
	
	@FindBy(id="txtCommitteeCode")
	WebElement committeeCode;
	
	@FindBy(id="txtCommissionerCode")
	WebElement commissionerCode;
	
	@FindBy(id="txtContractorCode")
	WebElement contractorCode;
	
	@FindBy(css="button[class='btn btn-default local-comm-clear-search-button']")
	WebElement clearSearch;
	
	@FindBy(id="btnSearchCommittee")
	WebElement searchButton;
	
	@FindBy(xpath="//div[@id='divMainContainer']/div[6]/div[1]/button")
	WebElement searchLevyDetails;
	
	@FindBy(xpath="//div[@id='divMainContainer']/div[6]/div[3]/button")
	WebElement createCommittee;
	
	public LocalCommitteeSearch(WebDriver driver) {
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		// This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}
}
