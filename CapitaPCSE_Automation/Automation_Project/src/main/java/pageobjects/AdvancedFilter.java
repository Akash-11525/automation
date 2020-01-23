package pageobjects;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import utilities.ReadCSV;
import helpers.CommonFunctions;
import helpers.Support;
import helpers.WindowHandleSupport;
import utilities.ExcelUtilities;

public class AdvancedFilter extends Support{
	
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy (id="Mscrm.AdvancedFind.Groups.Show.Results-Large")
	WebElement showResults;
	
	@FindBy(xpath="//a[@id='Mscrm.AdvancedFind.Groups.Show.Results-Large']/span[1]")
	//@FindBy(linkText="Results")
	WebElement showResultsButton;
	
	@FindBy(xpath="//a[@id='Mscrm.AdvancedFind.Groups.Show.Results-Large']/span/span[1]")
	//@FindBy(linkText="Results")
	WebElement showResultsButton1;
	
	@FindBy (id="Mscrm.AdvancedFind.Groups.Show.SavedViews-Large")
	WebElement savedViews;
	
	
	@FindBy (id="Mscrm.AdvancedFind.Groups.Show.Query-Large")
	WebElement showQuery;
	
	@FindBy (id="slctPrimaryEntity")
	WebElement selectPrimaryEntity;
	
	@FindBy(id = "advFindE_fieldListFLDLBL")
	WebElement selectField;
	
	@FindBy(id="advFindE_fieldListFLDCTL")
	WebElement selectFieldName;
	
	
	@FindBy(id="advFindEFGRP0FFLD0OPASCTL")
	WebElement selectFilterCondition; 
	
	@FindBy(id="advFindEFGRP0FFLD0CCVALLBL")
	WebElement selectFieldValue;
	
	
	@FindBy (css="input[id*='advFindEFGRP0FFLD0CCVALCTL']")
	WebElement selectFieldValueTxt;
	
	@FindBy (xpath="//table[@id='advFindEFGRP0FFLD0CCVALCTL']//td/input")
	WebElement selectFieldValueTxt1;
	
	@FindBy (id="_ledit")
	WebElement selectFieldValueTxt2;
	
	@FindBy(css="div[id='ms-crm-Lookup']")
	WebElement selectFieldValue1;
	
		

	
	@FindBy(id="multipicklist_button")
	WebElement multipicklistButton;
	
	@FindBy(xpath="//div[@id='objList']/table")
	WebElement selectValuesTable;
	
	@FindBy(id="btnRight")
	WebElement buttonRight;
	
	@FindBy(id="btnLeft")
	WebElement buttonLeft;
	
	@FindBy(id="butBegin")
	WebElement buttonBegin;
	
	@FindBy(id="cmdDialogCancel")
	WebElement buttonCancel;
	
	@FindBy(id="advFindEFGRP0FFLD0_filterList")
	WebElement fieldList;
	
	@FindBy(xpath="//div[@title='Enter Value']//span")
	WebElement EnterValue;
	
	@FindBy(xpath="//div[@role='list']")
	WebElement EnterValueText;
	
	@FindBy(xpath="//input[@class='ms-crm-Input ms-crm-Text']")
	WebElement EnterPerformer;

	@FindBy(id="advFindEFE2EFE1EFGRP0FFLD0CCVALCTL_container")
	WebElement selectField_CS;
	
	@FindBy(css="input[id*='advFindEFE2EFE1EFGRP0FFLD0CCVALCTL']")
	WebElement selectFieldTxt_CS;

	@FindBy(xpath="//div[contains(@id, 'advFindEFE') and contains(@id, 'fieldListFLDCNTRL')]")
	WebElement contactFilterMenu;
	
	@FindBy(id="advFindEFE0E_fieldListFLDCNTRL")
	WebElement selectSecondFilterField;
	
	@FindBy(xpath="//div[contains(@id, 'advFindEFG') and contains(@id, 'PASLBL')]/span")
	WebElement cervicalFilterCondition;
	
	@FindBy(xpath="//select[contains(@id, 'advFindEFG') and contains(@id, 'PASCTL')]")
	WebElement cervicalFilterCondnValue;
	
	// changed original path after adding an extra filter
	//@FindBy(xpath="//div[@id='advFindEFE0E_fieldListFLDLBL']/span")
	@FindBy(xpath="//div[contains(@id, 'advFindEFE') and contains(@id, 'fieldListFLDLBL')]")
	WebElement contactLink;
	
	@FindBy(xpath="//select[contains(@id, 'advFindEFE') and contains(@id, 'fieldListFLDCTL')]")
	WebElement contactFilterValue;
	
	@FindBy(css="div.ms-crm-AdvFind-AutoShow")
	WebElement contactFilterConditionMenu;
	
	@FindBy(xpath="//div[contains(@id, 'advFindEFE') and contains(@id, 'PASLBL')]/span")
	WebElement contactConditionLink;
	
	@FindBy(xpath="//select[contains(@id, 'advFindEFE') and contains(@id, 'PASCTL')]")
	WebElement contactFilterCondition;
	
	@FindBy(xpath="//div[contains(@id, 'advFindEFE') and contains(@id, 'VALLBL')]/span")
	WebElement contactFilterValueLink;
	
	@FindBy(id="multipicklist_button")
	WebElement multiPickList;
	
	@FindBy(id="multipicklist_input")
	WebElement multiPickListInput;
	
	@FindBy(xpath="//div[@id='InlineDialog']")
	WebElement multiSelectWindow;
	
	@FindBy(xpath="//div[contains(@id, 'advFindEFE') and contains(@id, 'fieldListFLDCNTRL')]")
	WebElement companyFilterField;
	
	//@FindBy(xpath="//div[@id='advFindEFE0EFE1E_fieldListFLDCNTRL']")
	@FindBy(xpath="//div[@id='advFindEFE1EFE1E_fieldListFLDCNTRL']")
	WebElement companyFilterField2;
	
	//@FindBy(css="//div[@class='ms-crm-AdvFind-AutoShowEmptyControlLabel']/span")
	@FindBy(xpath="//div[contains(@id, 'advFindEFE') and contains(@id, 'fieldListFLDLBL')]/span")
	WebElement companyEntityLink;
	
	// changed original path after adding an extra filter
	//@FindBy(xpath="//*[@id='advFindEFE0EFE1E_fieldList']//div[contains(@id, 'advFindEFE') and contains(@id, 'fieldListFLDLBL')]/span")
	@FindBy(xpath="//div[@id='advFindEFE1EFE1E_fieldListFLDLBL']")	
	WebElement companyLink;
	
	@FindBy(css="input[id='advFindEFE0EFE1EFGRP0FFLD0CCVALCTL']")
	WebElement fourthFieldTxt;
	
	@FindBy(xpath="//select[contains(@id, 'advFindEFE') and contains(@id, 'fieldListFLDCTL')]")
	WebElement companyFieldValue;
	
	@FindBy(xpath="//div[contains(@id, 'advFindEFE') and contains(@id, 'fieldListFLDLBL')]/span")
	WebElement selectThirdFilterMenu;
	
	@FindBy(xpath="//select[@id='advFindEFE1EFE1E_fieldListFLDCTL']")
	WebElement companyFilterNewValue;
	
	@FindBy(css="div.ms-crm-AdvFind-AutoShow")
	WebElement companyFilterConditionMenu;
	
	@FindBy(xpath="//div[contains(@id, 'advFindEFE') and contains(@id, 'PASLBL')]/span")
	WebElement companyConditionLink;
	
	@FindBy(xpath="//select[contains(@id, 'advFindEFE') and contains(@id, 'PASCTL')]")
	WebElement companyFilterCondition;
	
	@FindBy(css="div.ms-crm-AdvFind-AutoShow")
	WebElement companyFilterValueMenu;
	
	// changed original path after adding an extra filter
	//@FindBy(xpath="//div[contains(@id, 'advFindEFE0EFE') and contains(@id, 'VALLBL')]/span")
	@FindBy(xpath="//div[contains(@id, 'advFindEFE1EFE') and contains(@id, 'VALLBL')]/span")
	WebElement companyFilterValueLink;
	// changed original path after adding an extra filter
	//@FindBy(xpath="//div[contains(@id, 'advFindEFE0EFE') and contains(@id, 'VALCTL')]/input")
	@FindBy(xpath="//div[contains(@id, 'advFindEFE1EFE') and contains(@id, 'VALCTL')]/input")
	WebElement companyFilterValue;
	
	@FindBy(css="div.ms-crm-AdvFind-FilterField")
	WebElement FilterFieldMainMenu;
	
	@FindBy(xpath="//li[@id='Mscrm.AdvancedFind.Groups.View']/span/span")
	WebElement viewMainMenu;
	
	@FindBy(css="div.wizText")
	WebElement addColumnsMenu;
	
	@FindBy(xpath="//div[@id='_TBAddColumnsoActivefalse']//div[@class='wizText']/a")
	WebElement addColumnsText;
	
	@FindBy(xpath="//div[@id='InlineDialog1']")
	WebElement addColumnsInlineWindow;
	
	@FindBy(xpath="//div[@id='firstname']")
	WebElement forenameField;
	
	@FindBy(xpath="//div[@id='firstname']/div/span")
	WebElement forenameTxtField;
	
	@FindBy(xpath="//div[@id='lastname']")
	WebElement lastnameField;
	
	@FindBy(xpath="//div[@id='lastname']/div/span")
	WebElement lastnameTxtField;
	
	@FindBy(xpath="//div[@id='birthdate']")
	WebElement birthdateField;
	
	@FindBy(xpath="//div[@id='birthdate']/div/span")
	WebElement birthdateTxtField;
	
	// added for HPV adv find
	@FindBy(xpath="//div[@id='advFindE_fieldListFLDLBL']/span")
	WebElement companyEntityLink_HPV;

	@FindBy(xpath="//div[@id='advFindE_fieldListFLDCNTRL']")
	WebElement companyFilterField_HPV;
	
	@FindBy(xpath="//select[@id='advFindE_fieldListFLDCTL']")
	WebElement companyFieldValue_HPV;
	
	@FindBy(xpath="//div[contains(@id, 'advFindEFE') and contains(@id, 'fieldListFLDLBL')]")	
	WebElement companyLink_HPV;
	
	@FindBy(xpath="//div[contains(@id, 'advFindEFE') and contains(@id, 'fieldListFLDCNTRL')]")
	WebElement companyFilterField2_HPV;

	@FindBy(xpath="//select[contains(@id, 'advFindEFE') and contains(@id, 'fieldListFLDCTL')]")
	WebElement companyFilterNewValue_HPV;
	
	@FindBy(xpath="//div[contains(@id, 'advFindEFE') and contains(@id, 'PASLBL')]/span")
	WebElement companyConditionLink_HPV;
	
	@FindBy(xpath="//select[contains(@id, 'advFindEFE') and contains(@id, 'PASCTL')]")
	WebElement companyFilterCondition_HPV;
	
	@FindBy(xpath="//div[contains(@id, 'advFindEFE') and contains(@id, 'VALLBL')]/span")
	WebElement companyFilterValueLink_HPV;
	
	@FindBy(xpath="//div[contains(@id, 'advFindEFE') and contains(@id, 'VALCTL')]/input")
	WebElement companyFilterValue_HPV;
	
	//@FindBy(xpath="//div[contains(@id, 'advFindEFG') and contains(@id, 'PASLBL')]/span") old value
	@FindBy(xpath="//div[@id='advFindEFGRP0FFLD1OPASLBL']")
	WebElement contactLink_HPV;
	
	//@FindBy(xpath="//select[contains(@id, 'advFindEFG') and contains(@id, 'PASCTL')]")
	@FindBy(id="//select[@id='advFindEFGRP0FFLD1OPASCTL']")
	WebElement contactFilterCondition_HPV;
	
	//@FindBy(xpath="//div[contains(@id, 'advFindEFGRP') and contains(@id, 'VALLBL')]/span")
	@FindBy(xpath="//div[@id='advFindEFGRP0FFLD1CCVALLBL']")
	WebElement contactFilterValueLink_HPV;
	
	@FindBy(xpath="//li[@id='Mscrm.AdvancedFind-title']/a")
	WebElement AdvanceFindLink;
	
	public AdvancedFilter(WebDriver driver){

		this.driver = driver;
		
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);
		
		}
	
	public void selectPrimaryEntityofCriteria(String selectPrimaryEntityValue){
		
		switchToRequiredIframe(selectPrimaryEntity, driver);
		CommonFunctions.selectOptionFromDropDown(selectPrimaryEntity,selectPrimaryEntityValue);
		if (isAlertPresent())
		{
			driver.switchTo().alert();
			driver.switchTo().alert().accept();
		
		}
		driver.switchTo().defaultContent();
		
	}
	
	public void enterFilterCriteria(String GroupTypeValue,String FieldValue,String ConditionValue, String ValueForFieldValue) throws InterruptedException{
		
		switchToRequiredIframe(selectField, driver);
		wait.until(ExpectedConditions.elementToBeClickable(selectField)).click();
		CommonFunctions.selectOptionFromOptGroupDropDown(selectFieldName,GroupTypeValue,FieldValue);
		//Thread.sleep(1000);
		//fieldList.sendKeys(Keys.TAB);
		Thread.sleep(2000);
		CommonFunctions.selectOptionFromDropDown(selectFilterCondition,ConditionValue);
		Thread.sleep(1000);
		//selectFilterCondition.sendKeys(Keys.TAB);
		Actions actions = new Actions(driver);
		actions.moveToElement(selectFieldValue).build().perform();
		actions.click();
		
		wait.until(ExpectedConditions.elementToBeClickable(selectFieldValueTxt)).clear();
		selectFieldValueTxt.sendKeys(ValueForFieldValue);
		//Thread.sleep(1000);
		//selectFieldValueTxt.sendKeys(Keys.TAB);
		
		driver.switchTo().defaultContent();
	
	}
	
	
public void enterFilterCriteria_GMP(String GroupTypeValue,String FieldValue,String ConditionValue, String ValueForFieldValue) throws InterruptedException{
		
		
		wait.until(ExpectedConditions.elementToBeClickable(selectField)).click();
		CommonFunctions.selectOptionFromOptGroupDropDown(selectFieldName,GroupTypeValue,FieldValue);
		//Thread.sleep(1000);
		//fieldList.sendKeys(Keys.TAB);
	
		CommonFunctions.selectOptionFromDropDown(selectFilterCondition,ConditionValue);
		Thread.sleep(1000);
		//selectFilterCondition.sendKeys(Keys.TAB);
	/*	Actions actions = new Actions(driver);
		actions.moveToElement(selectFieldValue).build().perform();
		actions.click();
		Thread.sleep(2000);
		
		Actions actions1 = new Actions(driver);
		actions1.moveToElement(EnterValue).build().perform();
		actions1.doubleClick();
		Actions actions1 = new Actions(driver);
		actions1.moveToElement(selectFilterCondition).build().perform();
		actions1.click();*/
		//Thread.sleep(1000);
		wait.until(ExpectedConditions.elementToBeClickable(selectFilterCondition)).sendKeys(Keys.TAB);
		
		wait.until(ExpectedConditions.elementToBeClickable(EnterValueText)).sendKeys(ValueForFieldValue);
		Thread.sleep(2000);
		//Thread.sleep(1000);
		//selectFieldValueTxt.sendKeys(Keys.TAB);
		
		driver.switchTo().defaultContent();
	
	}
	
		public void enterFilterCriteriaWithValueButton() throws InterruptedException{
		
		
		wait.until(ExpectedConditions.elementToBeClickable(selectField)).click();
	
		String selectOptGroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectoptGroupType",1);
		String selectFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectField",1);
		CommonFunctions.selectOptionFromOptGroupDropDown(selectFieldName,selectOptGroupTypeValue,selectFieldValue);
		Thread.sleep(1000);
		String selectfilterConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",1);
		CommonFunctions.selectOptionFromDropDown(selectFilterCondition,selectfilterConditionValue);
		Thread.sleep(1000);
		selectFilterCondition.sendKeys(Keys.TAB);
	
		wait.until(ExpectedConditions.elementToBeClickable(multipicklistButton)).click();
		
		Thread.sleep(3000);
		driver.switchTo().defaultContent();
	
			
	}
		
		public void enterFilterCriteriaWithValueButton_PL() throws InterruptedException{
			
			
			wait.until(ExpectedConditions.elementToBeClickable(selectField)).click();
		
			String selectOptGroupTypeValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvancedFind", "selectoptGroupType",1);
			String selectFieldValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx", "AdvancedFind", "selectField",1);
			CommonFunctions.selectOptionFromOptGroupDropDown(selectFieldName,selectOptGroupTypeValue,selectFieldValue);
			Thread.sleep(1000);
			String selectfilterConditionValue = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("CRMTESTDATA.xlsx", "AdvancedFind", "selectfilterCondition",1);
			CommonFunctions.selectOptionFromDropDown(selectFilterCondition,selectfilterConditionValue);
			Thread.sleep(1000);
			selectFilterCondition.sendKeys(Keys.TAB);
		
			wait.until(ExpectedConditions.elementToBeClickable(multipicklistButton)).click();
			
			Thread.sleep(3000);
			driver.switchTo().defaultContent();
		
				
		}
		
		public void selectValueFromPopup(String value) throws InterruptedException{
			
			
			driver.switchTo().frame("InlineDialog_Iframe");
			CommonFunctions.selectOptionFromTable(selectValuesTable, value);
			wait.until(ExpectedConditions.elementToBeClickable(buttonRight)).click();
			wait.until(ExpectedConditions.elementToBeClickable(buttonBegin)).click();
		
			Thread.sleep(3000);
			driver.switchTo().defaultContent();
			
		
				
		}
	
	public void clickResults() throws InterruptedException
	{
		Thread.sleep(2000);
		wait.until(ExpectedConditions.elementToBeClickable(showResultsButton)).click();
	}
	
	
	public AdvancedFindResult enterSearchCriteriaAndClickOnResult(String primaryEntity, String GroupTypeValue,String FieldValue,String ConditionValue, String ValueForFieldValue ) throws InterruptedException
	{
		selectPrimaryEntityofCriteria(primaryEntity);
		enterFilterCriteria(GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);
		clickResults();		
		return new AdvancedFindResult(driver);
	}
	
	public <T> T enterPrimaryEntityAndClickOnResult(String primaryEntity , Class<T> expectedPage) throws InterruptedException
	{
		selectPrimaryEntityofCriteria(primaryEntity);
		clickResults();
	
		return PageFactory.initElements(driver, expectedPage);
	}
	
	public AdvancedFindResult enterSearchCriteriaAndClickOnResult_multiple(String primaryEntity, List<String> GroupTypeValue,List<String> FieldValue,List<String> ConditionValue, List<String> ValueForFieldValue ) throws InterruptedException
	{
		selectPrimaryEntityofCriteria(primaryEntity);
		enterFilterCriteria_multiple(GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);
		clickResults();		
		return new AdvancedFindResult(driver);
	}
	
	public AdvancedFindResult enterSearchCriteriaAndClickOnResult_multiple_Test(String primaryEntity, List<String> GroupTypeValue,List<String> FieldValue,List<String> ConditionValue,List<String> ValueType, List<String> ValueForFieldValue ) throws InterruptedException
	{
		selectPrimaryEntityofCriteria(primaryEntity);
		enterFilterCriteria_multiple_test(GroupTypeValue,FieldValue,ConditionValue, ValueType, ValueForFieldValue);
		clickResults();		
		return new AdvancedFindResult(driver);
	}
	
	public AdvancedFindResult enterSearchCriteriaAndClickOnResult_GMP(String primaryEntity, String GroupTypeValue,String FieldValue,String ConditionValue, String ValueForFieldValue ) throws InterruptedException
	{
		selectPrimaryEntityofCriteria(primaryEntity);
		enterFilterCriteria_GMP(GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);
		clickResults();		
		return new AdvancedFindResult(driver);
	}
	
	public AdvancedFindResult enterSearchCriteriaAndClickOnResult_Process(String primaryEntity) throws InterruptedException
	{
		selectPrimaryEntityofCriteria(primaryEntity);
		//enterFilterCriteria(GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);
		driver.switchTo().defaultContent();
		clickResults();		
		return new AdvancedFindResult(driver);
	}
	
	public AdvancedFindResult enterSearchCriteriaAndClickOnResult_PL(String primaryEntity, String GroupTypeValue,String FieldValue,String ConditionValue, String ValueForFieldValue ) throws InterruptedException
	{
		selectPrimaryEntityofCriteria(primaryEntity);
		enterFilterCriteriaPL(GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);
		clickResults();		
		return new AdvancedFindResult(driver);
	}
		
	
	public AdvancedFindResult enterSearchCriteriaAndClickOnResult_Performer(String primaryEntity, String GroupTypeValue,String FieldValue,String ConditionValue, String ValueForFieldValue ) throws InterruptedException
	{
		selectPrimaryEntityofCriteria(primaryEntity);
		enterFilterCriteriaPerfomer(GroupTypeValue,FieldValue,ConditionValue, ValueForFieldValue);
		clickResults();		
		return new AdvancedFindResult(driver);
	}
	
	public void enterFilterCriteriaPerfomer(String GroupTypeValue,String FieldValue,String ConditionValue, String ValueForFieldValue) throws InterruptedException{
		
		
		switchToRequiredIframe(selectField, driver);
		wait.until(ExpectedConditions.elementToBeClickable(selectField)).click();
		CommonFunctions.selectOptionFromOptGroupDropDown(selectFieldName,GroupTypeValue,FieldValue);
		//Thread.sleep(1000);
		//fieldList.sendKeys(Keys.TAB);
	
		CommonFunctions.selectOptionFromDropDown(selectFilterCondition,ConditionValue);
		Thread.sleep(1000);
		wait.until(ExpectedConditions.elementToBeClickable(selectFilterCondition)).sendKeys(Keys.TAB);
		Thread.sleep(1000);
		wait.until(ExpectedConditions.visibilityOf(EnterPerformer)).sendKeys(ValueForFieldValue);
		
		//selectFilterCondition.sendKeys(Keys.TAB);
		
		/*Actions actions = new Actions(driver);
		actions.moveToElement(selectFieldValue).build().perform();
		actions.click();
		
		JavascriptExecutor javascript = (JavascriptExecutor) driver;
		String toenable = "document.getElementById('_ledit').setAttribute('disabled', false);";
		javascript.executeScript(toenable);
			
		System.out.println("Java Script executed Successfully");
		actions.moveToElement(selectFieldValue).build().perform();
		actions.click();
		//Actions actions = new Actions(driver);
		actions.moveToElement(selectFieldValue1).build().perform();
		actions.click();
		Thread.sleep(1000);
		actions.moveToElement(selectFieldValue).build().perform();
		actions.doubleClick();
		Thread.sleep(1000);
		wait.until(ExpectedConditions.visibilityOf(selectFieldValueTxt2)).sendKeys(ValueForFieldValue);
		//selectFieldValueTxt2.click();
		System.out.println("Click Event is executed Successfully");
	//	selectFieldValueTxt2.sendKeys(ValueForFieldValue);
		//selectFieldValueTxt2.
		//Thread.sleep(1000);
		//selectFieldValueTxt.sendKeys(Keys.TAB);
*/		
		driver.switchTo().defaultContent();
	
	}
/*	
public void enterFilterCriteria(String FieldValue,String ConditionValue, String ValueForFieldValue) throws InterruptedException{
		
		
		wait.until(ExpectedConditions.elementToBeClickable(selectPrimaryEntity)).click();
		//CommonFunctions.selectOptionFromOptGroupDropDown(selectFieldName,GroupTypeValue,FieldValue);
		CommonFunctions.selectOptionFromDropDown(selectPrimaryEntity, FieldValue);
		//Thread.sleep(1000);
		//fieldList.sendKeys(Keys.TAB);
		wait.until(ExpectedConditions.elementToBeClickable(selectField)).click();
		CommonFunctions.selectOptionFromDropDown(selectFieldselectFilterCondition,ConditionValue);
		Thread.sleep(1000);
		//selectFilterCondition.sendKeys(Keys.TAB);
		Actions actions = new Actions(driver);
		actions.moveToElement(selectFieldValue).build().perform();
		actions.click();
		
		wait.until(ExpectedConditions.elementToBeClickable(selectFieldValueTxt)).sendKeys(ValueForFieldValue);
		//Thread.sleep(1000);
		//selectFieldValueTxt.sendKeys(Keys.TAB);
		
		driver.switchTo().defaultContent();
	
	}
	*/


	public boolean isAlertPresent() 
	{ 
	    try 
	    { 
	        driver.switchTo().alert(); 
	        return true; 
	    }  
	    catch (NoAlertPresentException Ex) 
	    { 
	        return false; 
	    }   
	}  
	
	public void enterFilterCriteriaPL(String GroupTypeValue,String FieldValue,String ConditionValue, String ValueForFieldValue) throws InterruptedException{
		
		
		wait.until(ExpectedConditions.elementToBeClickable(selectField)).click();
		CommonFunctions.selectOptionFromOptGroupDropDown(selectFieldName,GroupTypeValue,FieldValue);
		//Thread.sleep(1000);
		//fieldList.sendKeys(Keys.TAB);
	
		CommonFunctions.selectOptionFromDropDown(selectFilterCondition,ConditionValue);
		Thread.sleep(1000);
		//selectFilterCondition.sendKeys(Keys.TAB);
		
		Actions actions = new Actions(driver);
		actions.moveToElement(selectFieldValue).build().perform();
		actions.click();
		
		JavascriptExecutor javascript = (JavascriptExecutor) driver;
		String toenable = "document.getElementById('_ledit').setAttribute('disabled', false);";
		javascript.executeScript(toenable);
			
		System.out.println("Java Script executed Successfully");
		/*actions.moveToElement(selectFieldValue).build().perform();
		actions.click();*/
		/*//Actions actions = new Actions(driver);
		actions.moveToElement(selectFieldValue1).build().perform();
		actions.click();*/
		Thread.sleep(1000);
		actions.moveToElement(selectFieldValue).build().perform();
		actions.doubleClick();
		Thread.sleep(1000);
		wait.until(ExpectedConditions.visibilityOf(selectFieldValueTxt2)).sendKeys(ValueForFieldValue);
		//selectFieldValueTxt2.click();
		System.out.println("Click Event is executed Successfully");
	//	selectFieldValueTxt2.sendKeys(ValueForFieldValue);
		//selectFieldValueTxt2.
		//Thread.sleep(1000);
		//selectFieldValueTxt.sendKeys(Keys.TAB);
		
		driver.switchTo().defaultContent();
	
	}
	
	public AdvancedFindResult enterRelatedSearchCriteriaAndClickOnResult(String primaryEntity, List<Object> finalList, List<Integer> listSize) throws InterruptedException, IOException
	{
		List<String> NHSNumbers= new ArrayList<String>();
		// This method can be used to break a list in multiple lists of different sizes. All child list should be of Object type
		List<List<Object>> multiList= CommonFunctions.breakListInDifferentLists(finalList,listSize);
		List<Object> contactFilter= multiList.get(0);
		List<Object> organisationFilter= multiList.get(1);
		selectPrimaryEntityofCriteria(primaryEntity);
		// this will set the condition for primary entity
		wait.until(ExpectedConditions.elementToBeClickable(selectField)).click();
		CommonFunctions.selectOptionFromOptGroupDropDown(selectFieldName,"fld","NHS Number");
		wait.until(ExpectedConditions.elementToBeClickable(contactFilterConditionMenu)).click();
		wait.until(ExpectedConditions.elementToBeClickable(cervicalFilterCondition)).click();
		CommonFunctions.selectOptionFromDropDown(cervicalFilterCondnValue,"Contains Data");
		
		enterFilterCriteriaForRelatedEntity(contactFilter,"contact");
		enterFilterCriteriaForRelatedEntity(organisationFilter,"Company Name (Organisation)");
		WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
		clickOnEditColumnsMenu("CSTESTDATA.xlsx", "CSOptions",1);
		WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
		clickResults();
		
		return new AdvancedFindResult(driver);
	}


	private void enterFilterCriteriaForRelatedEntity(List<Object> entityFilter, String entity) throws InterruptedException {
		
		switch (entity) {
		case "contact":
			Thread.sleep(3000);
			// this will select the related entity
			wait.until(ExpectedConditions.elementToBeClickable(selectField)).click();
			CommonFunctions.selectOptionFromOptGroupDropDown(selectFieldName,(String)entityFilter.get(0),(String)entityFilter.get(1));

			// this will select the related entity field
			scrolltoElement(driver, contactFilterMenu);
			wait.until(ExpectedConditions.elementToBeClickable(contactLink)).click();
			wait.until(ExpectedConditions.elementToBeClickable(contactFilterMenu)).click();
			CommonFunctions.selectOptionFromOptGroupDropDown(contactFilterValue,(String)entityFilter.get(2),(String)entityFilter.get(3));
			
			// this will select the related entity condition
			wait.until(ExpectedConditions.elementToBeClickable(contactFilterConditionMenu)).click();
			wait.until(ExpectedConditions.elementToBeClickable(contactConditionLink)).click();
			CommonFunctions.selectOptionFromDropDown(contactFilterCondition,(String)entityFilter.get(4));
			
			// this will select the related entity value
			Thread.sleep(1000);
			Actions actions = new Actions(driver);
			actions.moveToElement(contactFilterValueLink).build().perform();
			actions.click();
			wait.until(ExpectedConditions.elementToBeClickable(multiPickList)).click();
			Thread.sleep(2000);
		    driver.switchTo().defaultContent();
			if(multiSelectWindow.isDisplayed()){
				selectMultiSelectValue((String)entityFilter.get(5));
								
			}
			driver.switchTo().defaultContent();
			break;
		case "Company Name (Organisation)":
			// this will select the related entity
			switchToRequiredIframe(selectPrimaryEntity, driver);
			wait.until(ExpectedConditions.elementToBeClickable(companyEntityLink)).click();
			wait.until(ExpectedConditions.elementToBeClickable(companyFilterField)).click();
			CommonFunctions.selectOptionFromOptGroupDropDown(companyFieldValue,(String)entityFilter.get(0),(String)entityFilter.get(1));
			
			wait.until(ExpectedConditions.elementToBeClickable(companyLink)).click();
			wait.until(ExpectedConditions.elementToBeClickable(companyFilterField2)).click();
			
			// this will select the related entity field
			new AdvancedFilter(driver);
			wait.until(ExpectedConditions.elementToBeClickable(companyFilterNewValue));
			CommonFunctions.selectOptionFromOptGroupDropDown(companyFilterNewValue,(String)entityFilter.get(2),(String)entityFilter.get(3));

			// this will select the related entity condition
			wait.until(ExpectedConditions.elementToBeClickable(companyFilterConditionMenu)).click();
			wait.until(ExpectedConditions.elementToBeClickable(companyConditionLink)).click();
			CommonFunctions.selectOptionFromDropDown(companyFilterCondition,(String)entityFilter.get(4));
			
			// this will select the related entity value
			wait.until(ExpectedConditions.elementToBeClickable(companyFilterValueMenu)).click();
			wait.until(ExpectedConditions.elementToBeClickable(companyFilterValueLink)).click();
			wait.until(ExpectedConditions.elementToBeClickable(companyFilterValue)).sendKeys((String)entityFilter.get(5));
			break;
		default:
			Assert.fail(entity+ " name doesn't exists.");
		}

	}

	private void selectMultiSelectValue(String value) throws InterruptedException {
		//driver.switchTo().activeElement();
		Actions moveFocus= new Actions(driver);
		moveFocus.moveToElement(driver.findElement(By.xpath("//div[@id='InlineDialog']"))).build().perform();
		driver.switchTo().frame("InlineDialog_Iframe");
		//WebElement inlineDialogue= driver.findElement(By.xpath("//div[@id='InlineDialog']"));
		WebElement activeElm= driver.findElement(By.xpath("//div[@id='objList']//tbody/tr[1]/td"));
			//List<WebElement> values= driver.findElements(By.id("objList"));
			//for (WebElement webElement : values) {
				if(activeElm.getText().equalsIgnoreCase(value)){
					activeElm.click();
					WebElement navigateElm= driver.findElement(By.id("btnRight"));
					navigateElm.click();
					WebElement okButton= driver.findElement(By.id("butBegin"));
					okButton.click();
					//break;
				}
		//}
			
	}

	private void clickOnEditColumnsMenu(String fileName, String sheet,int colNum) throws InterruptedException, IOException {
		wait.until(ExpectedConditions.elementToBeClickable(viewMainMenu)).click();
		wait.until(ExpectedConditions.visibilityOf(multiSelectWindow));
	    driver.switchTo().defaultContent();
		if(multiSelectWindow.isDisplayed()){
			driver.switchTo().frame("InlineDialog_Iframe");
			selectColumnsFromWindow(fileName,sheet,colNum);
			driver.switchTo().frame("InlineDialog_Iframe");
			WebElement editColumnOkButton= driver.findElement(By.id("butBegin"));
			editColumnOkButton.click();
							
		}
	
	}

	private void selectColumnsFromWindow(String fileName, String sheet,int colNum) throws InterruptedException, IOException {

		driver.switchTo().frame("viewEditor");
		wait.until(ExpectedConditions.elementToBeClickable(addColumnsText)).click();
		Thread.sleep(3000);
		driver.switchTo().defaultContent();
		if(addColumnsInlineWindow.isDisplayed()){
			driver.switchTo().frame("InlineDialog1_Iframe");
			System.out.println("Moved to 2nd frame for selcting columns");
			List<String> CellValues = ExcelUtilities.getCellValuesInExcel(fileName, sheet, colNum);
			for (int i = 0; i < CellValues.size(); i++) {
				String CellValue = CellValues.get(i);
				System.out.println("The cell value is: "+CellValue);
				WebElement ele = driver.findElement(By.xpath("//*[@id='"+CellValue+"']"));
				scrolltoElement(driver, ele);
				ele.click();
/*				Thread.sleep(1000);
				WebElement addColumnOkButton= driver.findElement(By.id("butBegin"));
				addColumnOkButton.click();*/
			}
			Thread.sleep(1000);
			WebElement addColumnOkButton= driver.findElement(By.id("butBegin"));
			addColumnOkButton.click();

		}	
	}

	public boolean getContactDetails(String primaryEntity, String GroupTypeValue, String FieldValue, String ConditionValue,
			List<String> nHSNumbers) throws InterruptedException, IOException, ParseException {
		
		boolean isDataSaved= false;
		List<String> NHSDetails= new ArrayList<String>();
		AdvancedFindResult objAdvancedFindResult= new AdvancedFindResult(driver);
		List<Integer> colIndexValues= Arrays.asList(0,1,2,3);
		
		for(int i=0;i<nHSNumbers.size();i++){
			
			String ValueForFieldValue= nHSNumbers.get(i);
			NHSDetails.add(ValueForFieldValue);
			selectPrimaryEntityofCriteria(primaryEntity);
			wait.until(ExpectedConditions.elementToBeClickable(selectField)).click();
			CommonFunctions.selectOptionFromOptGroupDropDown(selectFieldName,GroupTypeValue,FieldValue);
			
/*			wait.until(ExpectedConditions.elementToBeClickable(cervicalFilterCondition)).click();
			CommonFunctions.selectOptionFromDropDown(cervicalFilterCondnValue,ConditionValue);*/
			
			CommonFunctions.selectOptionFromDropDown(selectFilterCondition,ConditionValue);
			Thread.sleep(1000);
			Actions actions = new Actions(driver);
			actions.moveToElement(selectFieldValue).build().perform();
			actions.click();
			
			wait.until(ExpectedConditions.elementToBeClickable(selectFieldValueTxt)).clear();
			selectFieldValueTxt.sendKeys(ValueForFieldValue);
	
			driver.switchTo().defaultContent();
			clickResults();
			objAdvancedFindResult.clickOnLinkFromFirstRecord(0, 1);
			WindowHandleSupport.getRequiredWindowDriver(driver, "Contact:");
			driver.switchTo().frame("contentIFrame0");
			wait.until(ExpectedConditions.elementToBeClickable(forenameField));
			wait.until(ExpectedConditions.elementToBeClickable(forenameTxtField));
			String forename= forenameTxtField.getText().trim();
			NHSDetails.add(forename);
			
			wait.until(ExpectedConditions.elementToBeClickable(lastnameField));
			wait.until(ExpectedConditions.elementToBeClickable(lastnameTxtField));
			String lastname= lastnameTxtField.getText().trim();
			NHSDetails.add(lastname);
			
			wait.until(ExpectedConditions.elementToBeClickable(birthdateField));
			wait.until(ExpectedConditions.elementToBeClickable(birthdateTxtField));
			String birthdate= birthdateTxtField.getText().trim();
			Date date= CommonFunctions.convertStringtoCalDate(birthdate, "MM/dd/yyyy");
			String strDate= CommonFunctions.convertDateToString(date, "dd/MM/yyyy");
			NHSDetails.add(strDate);
			if(i==0){
				//isDataSaved= savePatientDetailsinExcel(NHSDetails);
			}else{
				// this will create a CSV file for bulk upload of records
				String sourcePath= ReadCSV.getSourcePathForModule("Colposcopy");
				isDataSaved= ReadCSV.generateAndCopyCSVFile(NHSDetails,4,6,colIndexValues,sourcePath,"Colposcopy",',');
			}
			// this will create a CSV file for bulk upload of records
/*			String sourcePath= ReadCSV.getSourcePathForModule("Colposcopy");
			isDataSaved= ReadCSV.generateColposcopyCSVFile(NHSDetails,4,6,colIndexValues,sourcePath,"Colposcopy",',');*/
			
			NHSDetails.removeAll(NHSDetails);
			driver.close();
			WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
			objAdvancedFindResult.clickonAdvancedFind();
			driver.close();
			CrmHome ObjCrmHome= new CrmHome(driver);
			ObjCrmHome.clickAdvanceFind();
		}
		
		return isDataSaved;
	}

/*Commented by Rupesh
 * 	private boolean savePatientDetailsinExcel(List<String> nHSDetails) throws IOException {
		boolean isDataSaved= false;
			
			for(int i=2;i<=3;i++){
				ExcelUtilities.setKeyValueinExcelWithPosition("CSTESTDATA.xlsx", "DynamicColposcopyTestData", "NHSNo", nHSDetails.get(0), i);
				ExcelUtilities.setKeyValueinExcelWithPosition("CSTESTDATA.xlsx", "DynamicColposcopyTestData", "Forename", nHSDetails.get(1), i);
				ExcelUtilities.setKeyValueinExcelWithPosition("CSTESTDATA.xlsx", "DynamicColposcopyTestData", "Surname", nHSDetails.get(2), i);
				ExcelUtilities.setKeyValueinExcelWithPosition("CSTESTDATA.xlsx", "DynamicColposcopyTestData", "DateofBirth", nHSDetails.get(3), i);
			}
			isDataSaved=true;

		return isDataSaved;
	}*/

	public AdvancedFindResult enterRelatedSearchCriteriaAndClickOnResult_HPV(String colPrimaryEntity,List<Object> organisationFilter) throws InterruptedException, IOException {
		WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
		
		selectPrimaryEntityofCriteria(colPrimaryEntity);
		wait.until(ExpectedConditions.elementToBeClickable(selectField)).click();
		CommonFunctions.selectOptionFromOptGroupDropDown(selectFieldName,"fld","NHS Number");
		wait.until(ExpectedConditions.elementToBeClickable(contactFilterConditionMenu)).click();
		wait.until(ExpectedConditions.elementToBeClickable(cervicalFilterCondition)).click();
		CommonFunctions.selectOptionFromDropDown(cervicalFilterCondnValue,"Contains Data");
		//
		wait.until(ExpectedConditions.elementToBeClickable(companyEntityLink_HPV)).click();
		wait.until(ExpectedConditions.elementToBeClickable(companyFilterField_HPV)).click();
		CommonFunctions.selectOptionFromOptGroupDropDown(companyFieldValue_HPV,"fld","Status");
		
		// this will select the related entity condition
		//wait.until(ExpectedConditions.elementToBeClickable(contactFilterConditionMenu)).click();
/*		wait.until(ExpectedConditions.elementToBeClickable(contactLink_HPV)).click();
		CommonFunctions.selectOptionFromDropDown(contactFilterCondition_HPV,"Equals");*/
		// this will select the related entity value
		Thread.sleep(2000);
		Actions actions = new Actions(driver);
		actions.moveToElement(contactFilterValueLink_HPV).build().perform();
		actions.click();
//		wait.until(ExpectedConditions.elementToBeClickable(contactFilterValueLink_HPV)).click();
		wait.until(ExpectedConditions.elementToBeClickable(multiPickList)).click();
		Thread.sleep(2000);
	    driver.switchTo().defaultContent();
		if(multiSelectWindow.isDisplayed()){
			selectMultiSelectValue("Active");
							
		}
		driver.switchTo().defaultContent();
		//
		switchToRequiredIframe(selectPrimaryEntity, driver);
		wait.until(ExpectedConditions.elementToBeClickable(companyEntityLink_HPV)).click();
		wait.until(ExpectedConditions.elementToBeClickable(companyFilterField_HPV)).click();
		CommonFunctions.selectOptionFromOptGroupDropDown(companyFieldValue_HPV,(String)organisationFilter.get(0),(String)organisationFilter.get(1));
		
		wait.until(ExpectedConditions.elementToBeClickable(companyLink_HPV)).click();
		wait.until(ExpectedConditions.elementToBeClickable(companyFilterField2_HPV)).click();
		
		// this will select the related entity field
		new AdvancedFilter(driver);
		wait.until(ExpectedConditions.elementToBeClickable(companyFilterNewValue_HPV));
		CommonFunctions.selectOptionFromOptGroupDropDown(companyFilterNewValue_HPV,(String)organisationFilter.get(2),(String)organisationFilter.get(3));

		// this will select the related entity condition
		wait.until(ExpectedConditions.elementToBeClickable(companyFilterConditionMenu)).click();
		wait.until(ExpectedConditions.elementToBeClickable(companyConditionLink_HPV)).click();
		CommonFunctions.selectOptionFromDropDown(companyFilterCondition_HPV,(String)organisationFilter.get(4));
		
		// this will select the related entity value
		wait.until(ExpectedConditions.elementToBeClickable(companyFilterValueMenu)).click();
		wait.until(ExpectedConditions.elementToBeClickable(companyFilterValueLink_HPV)).click();
		wait.until(ExpectedConditions.elementToBeClickable(companyFilterValue_HPV)).sendKeys((String)organisationFilter.get(5));
		
		WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
		clickOnEditColumnsMenu("CSTESTDATA.xlsx", "CSOptions",3);
		WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
		clickResults();
		
		return new AdvancedFindResult(driver);
	}
	
	public boolean getContactDetails_HPV(String primaryEntity, String GroupTypeValue, String FieldValue, String ConditionValue,
			List<String> nHSNumbers) throws InterruptedException, IOException, ParseException {
		
		boolean isDataSaved= false;
		List<String> NHSDetails= new ArrayList<String>();
		AdvancedFindResult objAdvancedFindResult= new AdvancedFindResult(driver);
		List<Integer> colIndexValues= Arrays.asList(0,1,2,3);
		
		for(int i=0;i<nHSNumbers.size();i++){
			
			String ValueForFieldValue= nHSNumbers.get(i);
			NHSDetails.add(ValueForFieldValue);
			selectPrimaryEntityofCriteria(primaryEntity);
			wait.until(ExpectedConditions.elementToBeClickable(selectField)).click();
			CommonFunctions.selectOptionFromOptGroupDropDown(selectFieldName,GroupTypeValue,FieldValue);
			
/*			wait.until(ExpectedConditions.elementToBeClickable(cervicalFilterCondition)).click();
			CommonFunctions.selectOptionFromDropDown(cervicalFilterCondnValue,ConditionValue);*/
			
			CommonFunctions.selectOptionFromDropDown(selectFilterCondition,ConditionValue);
/*			Thread.sleep(1000);
			Actions actions = new Actions(driver);
			actions.moveToElement(selectFieldValue).build().perform();
			actions.click();*/
			wait.until(ExpectedConditions.elementToBeClickable(selectFieldValue)).click();
			
			wait.until(ExpectedConditions.elementToBeClickable(selectFieldValueTxt)).clear();
			selectFieldValueTxt.sendKeys(ValueForFieldValue);
	
			driver.switchTo().defaultContent();
			clickResults();
			objAdvancedFindResult.clickOnLinkFromFirstRecord(0, 1);
			WindowHandleSupport.getRequiredWindowDriver(driver, "Contact:");
			driver.switchTo().frame("contentIFrame0");
			wait.until(ExpectedConditions.elementToBeClickable(forenameField));
			wait.until(ExpectedConditions.elementToBeClickable(forenameTxtField));
			String forename= forenameTxtField.getText().trim();
			NHSDetails.add(forename);
			
			wait.until(ExpectedConditions.elementToBeClickable(lastnameField));
			wait.until(ExpectedConditions.elementToBeClickable(lastnameTxtField));
			String lastname= lastnameTxtField.getText().trim();
			NHSDetails.add(lastname);
			
			wait.until(ExpectedConditions.elementToBeClickable(birthdateField));
			wait.until(ExpectedConditions.elementToBeClickable(birthdateTxtField));
			String birthdate= birthdateTxtField.getText().trim();
			//Date date= CommonFunctions.convertStringtoCalDate(birthdate, "M/dd/yyyy");
			//String strDate= CommonFunctions.convertDateToString(date, "dd/MM/yyyy");
			NHSDetails.add(birthdate);
			if(i==0){
				//isDataSaved= savePatientDetailsinExcel(NHSDetails);
			}else{
				// this will create a CSV file for bulk upload of records
				String sourcePath= ReadCSV.getSourcePathForModule("HPV");
				isDataSaved= ReadCSV.generateAndCopyCSVFile(NHSDetails,2,3,colIndexValues,sourcePath,"HPV",',');
			}
			// this will create a CSV file for bulk upload of records
/*			String sourcePath= ReadCSV.getSourcePathForModule("Colposcopy");
			isDataSaved= ReadCSV.generateColposcopyCSVFile(NHSDetails,4,6,colIndexValues,sourcePath,"Colposcopy",',');*/
			
			NHSDetails.removeAll(NHSDetails);
			driver.close();
			WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
			objAdvancedFindResult.clickonAdvancedFind();
			driver.close();
			CrmHome ObjCrmHome= new CrmHome(driver);
			ObjCrmHome.clickAdvanceFind();
		}
		
		return isDataSaved;
	}
	
	public AdvancedFindResult enterRelatedSearchCriteriaAndClickOnResult_Ceasing(String colPrimaryEntity,List<Object> organisationFilter) 
			throws InterruptedException, IOException, ParseException {
		WebElement element;
		WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
		
		selectPrimaryEntityofCriteria(colPrimaryEntity);
		wait.until(ExpectedConditions.elementToBeClickable(selectField)).click();
		CommonFunctions.selectOptionFromOptGroupDropDown(selectFieldName,"fld","NHS Number");
		wait.until(ExpectedConditions.elementToBeClickable(contactFilterConditionMenu)).click();
		wait.until(ExpectedConditions.elementToBeClickable(cervicalFilterCondition)).click();
		CommonFunctions.selectOptionFromDropDown(cervicalFilterCondnValue,"Contains Data");
		//
		wait.until(ExpectedConditions.elementToBeClickable(companyEntityLink_HPV)).click();
		wait.until(ExpectedConditions.elementToBeClickable(companyFilterField_HPV)).click();
		CommonFunctions.selectOptionFromOptGroupDropDown(companyFieldValue_HPV,"fld","Status");
		
		// this will select the related entity condition
		//wait.until(ExpectedConditions.elementToBeClickable(contactFilterConditionMenu)).click();
/*		wait.until(ExpectedConditions.elementToBeClickable(contactLink_HPV)).click();
		CommonFunctions.selectOptionFromDropDown(contactFilterCondition_HPV,"Equals");*/
		// this will select the related entity value
		Thread.sleep(2000);
		Actions actions = new Actions(driver);
		actions.moveToElement(contactFilterValueLink_HPV).build().perform();
		actions.click();
//		wait.until(ExpectedConditions.elementToBeClickable(contactFilterValueLink_HPV)).click();
		wait.until(ExpectedConditions.elementToBeClickable(multiPickList)).click();
		Thread.sleep(2000);
	    driver.switchTo().defaultContent();
		if(multiSelectWindow.isDisplayed()){
			selectMultiSelectValue("Active");
							
		}
		driver.switchTo().frame("contentIFrame0");
		//selection addtional filters for age,sex and curent posting
		element= selectField();
		CommonFunctions.selectOptionFromOptGroupDropDown(element,"fld","Patient Date of Birth");
		element= selectCondition("0","2");
		CommonFunctions.selectOptionFromDropDown(element,"On or After");
		element= enterDateValue("0","2");
		Date date= CommonFunctions.getDateBeforeDays(9125);//date should be before 25 years from today's date
		String strDate= CommonFunctions.convertDateToString(date, "M/dd/yyyy");
		element.sendKeys(strDate);

		//selection ends
		driver.switchTo().defaultContent();
		//
		switchToRequiredIframe(selectPrimaryEntity, driver);
		wait.until(ExpectedConditions.elementToBeClickable(companyEntityLink_HPV)).click();
		wait.until(ExpectedConditions.elementToBeClickable(companyFilterField_HPV)).click();
		CommonFunctions.selectOptionFromOptGroupDropDown(companyFieldValue_HPV,(String)organisationFilter.get(0),(String)organisationFilter.get(1));
		
		wait.until(ExpectedConditions.elementToBeClickable(companyLink_HPV)).click();
		wait.until(ExpectedConditions.elementToBeClickable(companyFilterField2_HPV)).click();
		
		// this will select the related entity field
		new AdvancedFilter(driver);
		wait.until(ExpectedConditions.elementToBeClickable(companyFilterNewValue_HPV));
		CommonFunctions.selectOptionFromOptGroupDropDown(companyFilterNewValue_HPV,(String)organisationFilter.get(2),(String)organisationFilter.get(3));

		// this will select the related entity condition
		wait.until(ExpectedConditions.elementToBeClickable(companyFilterConditionMenu)).click();
		wait.until(ExpectedConditions.elementToBeClickable(companyConditionLink_HPV)).click();
		CommonFunctions.selectOptionFromDropDown(companyFilterCondition_HPV,(String)organisationFilter.get(4));
		
		// this will select the related entity value
		wait.until(ExpectedConditions.elementToBeClickable(companyFilterValueMenu)).click();
		wait.until(ExpectedConditions.elementToBeClickable(companyFilterValueLink_HPV)).click();
		wait.until(ExpectedConditions.elementToBeClickable(companyFilterValue_HPV)).sendKeys((String)organisationFilter.get(5));
		
		WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
		clickOnEditColumnsMenu("CSTESTDATA.xlsx", "CSOptions",3);
		WindowHandleSupport.getRequiredWindowDriver(driver, "Advanced Find");
		clickResults();
		
		return new AdvancedFindResult(driver);
	}

	private WebElement enterDateValue(String grpIndex, String condnIndex) throws InterruptedException {
		WebElement element;
		String startChar="advFindEFGRP"+grpIndex;
		String middleChar= "FFLD"+condnIndex;
		String link= "CCVALLBL";
		String finalLinkString= startChar+middleChar+link;
		String divChar= "CCVALCNTRL";
		String div= startChar+middleChar+divChar;
/*		String inputParentTag="CCVALCTL";
		String inputDiv= startChar+middleChar+inputParentTag+"_container";*/
		Thread.sleep(2000);
		
		element= driver.findElement(By.xpath("//div[@id='"+finalLinkString+"']"));
		Thread.sleep(5000);
		wait.until(ExpectedConditions.elementToBeClickable(element)).click();
		Thread.sleep(2000);
		
		element= driver.findElement(By.xpath("//div[@id='"+div+"']"));
		wait.until(ExpectedConditions.elementToBeClickable(element)).click();
		Thread.sleep(2000);
		
		element= driver.findElement(By.xpath("//div[@id='"+div+"']/table//tbody/tr[1]/td[1]/input"));
		wait.until(ExpectedConditions.elementToBeClickable(element)).click();
		return element;
	}

	private WebElement enterTextValue(String grpIndex, String condnIndex) throws InterruptedException {
		WebElement element;
		String startChar="advFindEFGRP"+grpIndex;
		String middleChar= "FFLD"+condnIndex;
		String finalLinkString= startChar+middleChar+"CCVALLBL";
		String div= startChar+middleChar+"CCVALCNTRL";
		
		String inputDiv= startChar+middleChar+"CCVALCTL"+"_container";
		Thread.sleep(2000);
		
		element= driver.findElement(By.xpath("//div[@id='"+finalLinkString+"']"));
		Thread.sleep(5000);
		wait.until(ExpectedConditions.elementToBeClickable(element)).click();
		Thread.sleep(2000);
		
		element= driver.findElement(By.xpath("//div[@id='"+div+"']"));
		wait.until(ExpectedConditions.elementToBeClickable(element)).click();
		Thread.sleep(2000);
		
		element= driver.findElement(By.xpath("//div[@id='"+inputDiv+"']/input"));
		wait.until(ExpectedConditions.elementToBeClickable(element)).click();
		return element;
	}
	
	private WebElement enterLookupValue(String grpIndex, String condnIndex) throws InterruptedException {
		WebElement element;
		String startChar="advFindEFGRP"+grpIndex;
		String middleChar= "FFLD"+condnIndex;
		String finalLinkString= startChar+middleChar+"CCVALLBL";
		String div= startChar+middleChar+"CCVALCNTRL";
		
		String inputDiv= startChar+middleChar+"CCVALCTL";
		//Thread.sleep(2000);
		
		element= driver.findElement(By.xpath("//div[@id='"+finalLinkString+"']"));
		//Thread.sleep(5000);
		wait.until(ExpectedConditions.elementToBeClickable(element)).click();
		//Thread.sleep(2000);
		
		element= driver.findElement(By.xpath("//div[@id='"+div+"']"));
		wait.until(ExpectedConditions.elementToBeClickable(element)).click();
		//Thread.sleep(2000);
		
		element= driver.findElement(By.xpath("//table[@id='"+inputDiv+"']//input"));
		wait.until(ExpectedConditions.elementToBeClickable(element)).click();
		return element;
	}

	private WebElement selectCondition(String grpIndex, String condnIndex) throws InterruptedException {
		WebElement element;
		Actions actions= new Actions(driver);
		String startChar="advFindEFGRP"+grpIndex;
		String middleChar= "FFLD"+condnIndex;
		String finalLinkString= startChar+middleChar+"OPASLBL";
		String div= startChar+middleChar+"OPASCNTRL";
		String selectTag= startChar+middleChar+"OPASCTL";
		Thread.sleep(1000);
		element= driver.findElement(By.xpath("//div[@id='"+finalLinkString+"']/span"));
		actions.moveToElement(driver.findElement(By.xpath("//div[@class='ms-crm-AdvFind-AutoShow']"))).moveToElement(element).moveToElement(element);
		actions.click().build().perform();

		new AdvancedFilter(driver);
		Thread.sleep(2000);
		wait.until(ExpectedConditions.elementToBeClickable(element)).click();
		Thread.sleep(1000);
		element= driver.findElement(By.xpath("//div[@id='"+div+"']"));
		//Thread.sleep(5000);
		wait.until(ExpectedConditions.elementToBeClickable(element)).click();
		Thread.sleep(1000);
		element= driver.findElement(By.xpath("//select[@id='"+selectTag+"']"));
		//Thread.sleep(5000);
		wait.until(ExpectedConditions.elementToBeClickable(element)).click();
		return element;
	}

	private WebElement selectField() throws InterruptedException {
		WebElement element;
		Thread.sleep(2000);
		element= driver.findElement(By.xpath("//div[@id='advFindE_fieldListFLDLBL']/span"));
		wait.until(ExpectedConditions.elementToBeClickable(element)).click();
		
		Thread.sleep(2000);
		element= driver.findElement(By.xpath("//div[@id='advFindE_fieldListFLDCNTRL']"));
		wait.until(ExpectedConditions.elementToBeClickable(element)).click();
		
		Thread.sleep(2000);
		element= driver.findElement(By.xpath("//select[@id='advFindE_fieldListFLDCTL']"));
		wait.until(ExpectedConditions.elementToBeClickable(element)).click();
		return element;
	}

public void enterFilterCriteria_multiple(List<String> GroupTypeValue,List<String> FieldValue,List<String> ConditionValue, List<String> ValueForFieldValue) throws InterruptedException{
		
		for (int i = 0; i<GroupTypeValue.size();i++)
		{
		/*wait.until(ExpectedConditions.elementToBeClickable(selectField)).click();
		CommonFunctions.selectOptionFromOptGroupDropDown(selectFieldName,GroupTypeValue.get(i),FieldValue.get(i));
		//Thread.sleep(1000);
		//fieldList.sendKeys(Keys.TAB);
		String conditionLbl = "advFindEFGRP0FFLD"+i+"OPASLBL";
	//	driver.findElement(By.id(conditionLbl)).click();
		Actions actions1 = new Actions(driver);
		actions1.moveToElement(driver.findElement(By.xpath("//div[@class='ms-crm-AdvFind-AutoShow']"))).moveToElement(driver.findElement(By.id(conditionLbl))).build().perform();
		actions1.click();
		Actions actions2 = new Actions(driver);
		String selectCondition = "advFindEFGRP0FFLD"+i+"OPASCNTRL";
		actions2.moveToElement(driver.findElement(By.id(selectCondition))).build().perform();
		actions2.click();
		
		String condition = "advFindEFGRP0FFLD"+i+"OPASCTL";
		CommonFunctions.selectOptionFromDropDown(driver.findElement(By.xpath("//select[@id='"+condition+"']")),ConditionValue.get(i));
		Thread.sleep(1000);
		//selectFilterCondition.sendKeys(Keys.TAB);
		String selectVal = "advFindEFGRP0FFLD"+i+"CCVALLBL";
		Actions actions = new Actions(driver);
		actions.moveToElement(driver.findElement(By.id(selectVal))).build().perform();
		actions.click();
		String selectField = "advFindEFGRP0FFLD"+i+"CCVALCTL";
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id(selectField)))).clear();
		driver.findElement(By.id(selectField)).sendKeys(ValueForFieldValue.get(i));*/
		//Thread.sleep(1000);
		//selectFieldValueTxt.sendKeys(Keys.TAB);
			
			WebElement select = selectField();
			CommonFunctions.selectOptionFromOptGroupDropDown(select,GroupTypeValue.get(i),FieldValue.get(i));
			String fieldIndex = Integer.toString(i);
			WebElement condition = selectCondition("0",fieldIndex );
			CommonFunctions.selectOptionFromDropDown(condition,ConditionValue.get(i));
			WebElement value = enterTextValue("0", fieldIndex);
			value.sendKeys(ValueForFieldValue.get(i));
			
			
		}
		driver.switchTo().defaultContent();
	
	}

public void enterFilterCriteria_multiple_test(List<String> GroupTypeValue,List<String> FieldValue,List<String> ConditionValue, List<String> ValueType, List<String> ValueForFieldValue) throws InterruptedException{
	switchToRequiredIframe(selectPrimaryEntity, driver);
	WebElement select = selectField();
	boolean flag=true;
	int count=0;
	for (int i = 0; i<GroupTypeValue.size();i++)
	{
		Actions actions= new Actions(driver);
		CommonFunctions.selectOptionFromOptGroupDropDown(select,GroupTypeValue.get(i),FieldValue.get(i));
		String fieldIndex = Integer.toString(i);
		WebElement condition = selectCondition("0",fieldIndex );
		CommonFunctions.selectOptionFromDropDown(condition,ConditionValue.get(i));
		String type = ValueType.get(i);
		WebElement value;
		
		switch(type)
		{
			case "Lookup" :
				value = enterLookupValue("0", fieldIndex);
				CommonFunctions.setText(value, ValueForFieldValue.get(count));
				System.out.println("Value is: "+ValueForFieldValue.get(count));
				Thread.sleep(4000);
				value.sendKeys(Keys.TAB);
				Thread.sleep(3000);
				flag= verifySpanValue("0", fieldIndex,ValueForFieldValue.get(count));
				Thread.sleep(3000);
				count=count+1;
				break;
			case "Date":
				value= enterDateValue("0", fieldIndex);
				CommonFunctions.setText(value, ValueForFieldValue.get(count));
				System.out.println("Value is: "+ValueForFieldValue.get(count));
				Thread.sleep(4000);
				//value.sendKeys(ValueForFieldValue.get(i));
				value.sendKeys(Keys.TAB);
				Thread.sleep(3000);
				flag= verifySpanValue("0", fieldIndex,ValueForFieldValue.get(count));
				Thread.sleep(3000);
				count=count+1;
				break;
			case "Multiselect":
				value= getMultiselectLinkElement("0", fieldIndex);
				actions.moveToElement(value).build().perform();
				actions.click();
				wait.until(ExpectedConditions.elementToBeClickable(multiPickList)).click();
				Thread.sleep(2000);
			    driver.switchTo().defaultContent();
				if(multiSelectWindow.isDisplayed()){
					selectValueFromWindow(ValueForFieldValue.get(count));		
				}
				driver.switchTo().defaultContent();
				count=count+1;
				break;
			case "NoValue":
				condition.sendKeys(Keys.TAB);
				Thread.sleep(2000);
				break;
			default :
				value = enterTextValue("0", fieldIndex);
				CommonFunctions.setText(value, ValueForFieldValue.get(count));
				System.out.println("Value is: "+ValueForFieldValue.get(count));
				Thread.sleep(2000);
				value.sendKeys(Keys.TAB);
				Thread.sleep(3000);
				count=count+1;
				break;
		}
	}
	//Thread.sleep(2000);
	driver.switchTo().defaultContent();

}

	private boolean verifySpanValue(String grpIndex, String fieldIndex,String expectedValue) throws InterruptedException {
		WebElement element;
		Actions actions= new Actions(driver);
		boolean flag=true;
		int count=0;
		String strValue="";
		String startChar="advFindEFGRP"+grpIndex;
		String middleChar= "FFLD"+fieldIndex;
		String finalLinkString= startChar+middleChar+"CCVALLBL";
		element= driver.findElement(By.xpath("//div[@id='"+finalLinkString+"']"));
		Thread.sleep(2000);
		while(flag){
			actions.moveToElement(element).build().perform();
			strValue= element.findElement(By.tagName("span")).getText();
			Thread.sleep(1000);
			System.out.println("Span value is: "+strValue+" for count "+count);
			if(expectedValue.equalsIgnoreCase(strValue)){
				System.out.println("Value"+" "+strValue+" is matched for count- "+count);
				flag=false;
				break;
			}
			count++;
		}
		return flag;
	}

	public AdvancedFilter clickOnAdvanceFindLink()
	{
		wait.until(ExpectedConditions.elementToBeClickable(AdvanceFindLink)).click();
		return new AdvancedFilter(driver);
	}
	
	private void selectValueFromWindow(String value) throws InterruptedException {
		Actions moveFocus= new Actions(driver);
		moveFocus.moveToElement(driver.findElement(By.xpath("//div[@id='InlineDialog']"))).build().perform();
		driver.switchTo().frame("InlineDialog_Iframe");
		List<WebElement> statusList= driver.findElements(By.xpath("//div[@id='objList']//tbody/tr"));
		for(int i=1;i<=statusList.size();i++){
			WebElement row= driver.findElement(By.xpath("//div[@id='objList']//tbody/tr["+i+"]/td"));
			String status= row.getText().toString();
			if(status.equalsIgnoreCase(value)){
				row.click();
				WebElement navigateElm= driver.findElement(By.id("btnRight"));
				navigateElm.click();
				WebElement okButton= driver.findElement(By.id("butBegin"));
				okButton.click();
				break;
			}
		}
	}
	
	private WebElement getMultiselectLinkElement(String grpIndex, String condnIndex) {
		WebElement element;
		String startChar="advFindEFGRP"+grpIndex;
		String middleChar= "FFLD"+condnIndex;
		String finalLinkString= startChar+middleChar+"CCVALLBL";
		element= driver.findElement(By.xpath("//div[@id='"+finalLinkString+"']"));
		return element;
	}
	
/*	private WebElement locateHiddenValueField(String grpIndex, String condnIndex) throws InterruptedException {
		WebElement element;
		String startCRMID="advFindEFGRP"+grpIndex;
		String endCRMID= "FFLD"+condnIndex;
		String finalCRMID= startCRMID+endCRMID;
		String hiddenElm= finalCRMID+"FLDPRM";
		
		element= driver.findElement(By.xpath("//*[@id='"+finalCRMID+"']/div"));
		wait.until(ExpectedConditions.elementToBeClickable(element)).click();
		element= driver.findElement(By.xpath("//*[@id='"+finalCRMID+"']/div/span[@id='"+hiddenElm+"']"));
		System.out.println("Element has been located which is hidden for group index: "+grpIndex+" and field index: "+condnIndex);
		return element;
	}*/

}
