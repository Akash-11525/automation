package pageobjects.GPP.CI;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Support;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class GPPPCSEPracticeCohort extends Support {
	static WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="cal")
	WebElement extendDeadlineCalendar;
	
	@FindBy(css="button#btn")
	WebElement btn_extendDeadline;
	
	@FindBy(xpath="//div[@class='btn-group']/button[1]")
	WebElement cohortPeriodOptions;
	
	@FindBy(id="PracticeCohortFilter_ContractorName")
	WebElement contractorTxtBox;
	
	@FindBy(id="PracticeCohortFilter_SelectedCohortStatus")
	WebElement cohortStatusOptions;
	
	@FindBy(id="PracticeCohortFilter_SelectedPaymentLineStatus")
	WebElement paymtLineOptions;
	
	@FindBy(xpath="//td[@class='padding-top-17 w35per td-Inline-padding']/a")
	WebElement selectContractorLink;
	
	@FindBy(xpath="//div[@class='modal-content']")
	WebElement selContModalWindow;
	
	@FindBy(css="h4#myModalLabel")
	static WebElement modalWindowHeader;
	
	@FindBy(xpath="//*[@id='divSelectContractorBody']/div[1]/div/div/div/span/div/button")
	WebElement selectRLT;
	
	@FindBy(xpath="//div[@class='btn-group open']//ul//div/input")
	WebElement searchRLTTxtBox;
	
	@FindBy(xpath="//div[@class='btn-group open']//ul//div/span[1]")
	WebElement btn_SearchRLT;
	
	@FindBy(xpath="//div[@class='btn-group open']//ul//div/span[2]")
	WebElement btn_CancelRLTSearch;

	@FindBy(xpath="//*[@id='divSelectContractorBody']/div[2]/div/div/div/span/div/button")
	WebElement selectCCG;
	
	@FindBy(xpath="//div[@class='input-group']/input[1]")
	WebElement SearchCCGTxtBox;
	
	@FindBy(xpath="//div[@class='btn-group open']//ul//div/span[1]")
	WebElement btn_SearchCCG;
	
	@FindBy(xpath="//div[@class='btn-group open']//ul//div/span[2]/button")
	WebElement btn_CancelCCGSearch;
	
	@FindBy(xpath="//*[@id='divSelectContractorBody']/div[3]/div/div/div/span/div/button")
	WebElement selectPractice;
	
	@FindBy(xpath="//div[@class='btn-group open']//ul//div/input")
	WebElement SearchPracticeTxtBox;
	
	@FindBy(xpath="//div[@class='btn-group open']//ul//div/span[1]")
	WebElement btn_SearchPractice;
	
	@FindBy(xpath="//div[@class='btn-group open']//ul//div/span[2]/button")
	WebElement btn_CancelPracticeSearch;
	
	@FindBy(xpath="//div[@class='modal-footer']/div[2]/button")
	WebElement btn_selectContractor;
	
	@FindBy(id="selectContractorCancelbtn")
	WebElement btn_cancelContractor;
			
	public GPPPCSEPracticeCohort(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}
	
	//this method is to be modified later after making it in dynamic way
	public void selectContractor() throws InterruptedException{
		wait.until(ExpectedConditions.elementToBeClickable(selectContractorLink)).click();
		wait.until(ExpectedConditions.elementToBeClickable(selectRLT)).click();
		selectRLTFromList();
		selectRLT.click();
		
		moveFocus(selectCCG);
		wait.until(ExpectedConditions.visibilityOf(selectCCG)).isDisplayed();
		wait.until(ExpectedConditions.elementToBeClickable(selectCCG)).click();
		selectCCGFromList();
		selectCCG.click();
		
		moveFocus(selectPractice);	
		wait.until(ExpectedConditions.elementToBeClickable(selectPractice)).click();
		selectPracticeFromList();
		selectPractice.click();
	}

	private void moveFocus(WebElement selectCCG2) {
		Actions moveFocus= new Actions(driver);
		moveFocus.moveToElement(selectCCG2).build().perform();
	}

	public static void selectRLTFromList(){
		//data will be read via excel sheet.
		String rltName="NHS ENGLAND NORTH,NHS ENGLAND EAST";
		
		List<WebElement> listOfRLT= driver.findElements(By.xpath("//ul[@class='multiselect-container dropdown-menu']/li"));
		/*List<String> selectedRLT=CommonFunctions.tickSelectedRLTFromList(listOfRLT, rltName, modalWindowHeader);*/
	}
	
	public static void selectCCGFromList(){
		//data will be read via excel sheet.
		String ccgName="NHS ENGLAND NORTH,New Medical Centre";
		List<WebElement> listOfRltGroup= driver.findElements(By.xpath("//ul[@class='multiselect-container dropdown-menu']/li[@class='multiselect-item multiselect-group']"));
		List<WebElement> listOfCCG= driver.findElements(By.xpath("//ul[@class='multiselect-container dropdown-menu']/li[@class='ChildOption']"));
		List<WebElement> finalCCGList= new ArrayList<WebElement>();
		finalCCGList.addAll(listOfRltGroup);
		finalCCGList.addAll(listOfCCG);
		/*List<String> selectedCCG=CommonFunctions.tickSelectedCCGFromList(finalCCGList, ccgName);*/
	}
	
	public static void selectPracticeFromList(){
		//data will be read via excel sheet.
		String practiceName=" Newlands Medical Centre, NHS WYRE FOREST CCG";
		List<WebElement> listOfCcgGroup= driver.findElements(By.xpath("//li[@class='multiselect-item multiselect-group']"));
		List<WebElement> listOfPractice= driver.findElements(By.xpath("//li[@class='ChildOption']"));
		List<WebElement> finalPracticeList= new ArrayList<WebElement>();
		finalPracticeList.addAll(listOfCcgGroup);
		finalPracticeList.addAll(listOfPractice);
		/*List<String> selectedPractice=CommonFunctions.tickSelectedPracticeFromList(finalPracticeList, practiceName);*/
	}
	
	
}
