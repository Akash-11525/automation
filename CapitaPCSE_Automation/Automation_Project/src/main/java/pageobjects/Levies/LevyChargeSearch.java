package pageobjects.Levies;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Support;

public class LevyChargeSearch extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="txtCommitteeCode")
	WebElement committeeTxtBox;
	
	@FindBy(id="txtCommissionerCode")
	WebElement commissionerTxtBox;
	
	@FindBy(css="select#ddlLevyType")
	WebElement levyType;
	
	@FindBy(id="txtSubmissionDateFrom")
	WebElement submissionFromDate;
	
	@FindBy(id="txtSubmissionDateTo")
	WebElement submissionToDate;
	
	@FindBy(css="select#txtStatus")
	WebElement status;
	
	@FindBy(id="txtEffectiveDateFrom")
	WebElement effectiveFromDate;
	
	@FindBy(id="txtEffectiveDateTo")
	WebElement effectiveToDate;
	
	@FindBy(css="button[class='btn btn-default local-comm-clear-search-button']")
	WebElement clearSearch;
	
	@FindBy(id="btnSearchCommittee")
	WebElement searchButton;
	
	public LevyChargeSearch(WebDriver driver) {
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		// This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}

}
