package pageobjects;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
//import org.sikuli.script.Key;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import helpers.UITableDataSupport;

public class Organisation {

	WebDriver driver;

	WebDriverWait wait;
	

	@FindBy(css="li[id*='New'] span[class*='Label']")
	WebElement new1;

	@FindBy(css="li[id*='SetAsDefault'] span[class*='Label']")
	WebElement setAsDefault;

	@FindBy(css="li[id*='Refresh'] span[class*='Label']")
	WebElement refresh;

	@FindBy(css="span[id*='CreateTextId']")
	WebElement create;
	
	@FindBy(id="navTourPages")
	WebElement navTourDialog;

	@FindBy(id="InlineDialog")
	WebElement inlineDialog;

	@FindBy(css = "a[id*='buttonClose']")
	WebElement closeButton;

	@FindBy(css="a[title*='Organisation']")
	WebElement orgTile;

	@FindBy(css="li[id*='NewRecord']")
	WebElement newOrg;

	@FindBy(css="input[id*='name_i']")
	WebElement orgName;

	@FindBy(css="input[id*='accountnumber_i']")
	WebElement accNo;	
	
	
	@FindBy(css = "img[id*='scr_organisationtype_i']")
	WebElement orgType;

	@FindBy(xpath="//ul[@id='scr_organisationtype_i_IMenu']//a[2]//nobr/span")
	WebElement orgTypeList;

	@FindBy(css="div[id='scr_organisationtype']")
	WebElement orgTypeEdit;
	
	@FindBy(css = "div[id='primarycontactid']")
	WebElement emailIDbox;
	
	@FindBy(css = "img[id='primarycontactid_i']")
	WebElement emailIDlookup;
	
	@FindBy(css="ul[id='primarycontactid_i_IMenu'] li[id='item12']")
	WebElement firstEmailItem;
	

	@FindBy(css="ul[id='scr_organisationtype_i_IMenu'] li[id='item0']")
	WebElement firstListItem;
	
	//@FindBy(css="input[id*='address1_line1_i']")
	@FindBy(css="div[id='address1_line1']")
	WebElement orgAddLine1;
	
	@FindBy(css="input[id*='address1_line1_i']")
	WebElement orgAddLine1TxBox;
	
	@FindBy(css="div[id*='address1_city']")
	WebElement orgCity;

	@FindBy(css="input[id*='address1_city_i']")
	WebElement orgCityTxt;

	@FindBy(css="div[id*='address1_postalcode']")
	WebElement orgPostCode;
	
	@FindBy(css="input[id*='address1_postalcode_i']")
	WebElement orgPostCodeTxt;
	
	@FindBy(css="div[id='scr_lbchpv']")
	WebElement lbc;
	
	@FindBy(css="li[title*='Save this Organisation']")
	WebElement saveLink;

	@FindBy(css="li[id*='account.Delete']")
	WebElement deleteLink;
	
	@FindBy(css="input[id*=crmGrid_findCriteria]")
	WebElement srcCriTxt;
	
	@FindBy(css="img[id*=crmGrid_findCriteriaImg]")
	WebElement srcCriIcon;
	
	@FindBy(xpath="//table[@id='gridBodyTable']/tbody")
	WebElement gridTable;

	public Organisation(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(driver, 30);
		

		//This initElements method will create all WebElements

		PageFactory.initElements(driver, this);

	}

	
	public String EnterOrgAccDetails() throws IOException, InterruptedException
	{
		driver.switchTo().frame("contentIFrame1");
		String oName = utilities.ExcelUtilities.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Org", "OrgName");
				
		
		String name = helpers.CommonFunctions.generateTS(oName);
		
		
		
		
			
		// Enter Org Name Value.
		orgName.clear();
		orgName.sendKeys(name);
		 
		//Select OrgType as first Item value from lookup.
		orgTypeEdit.click();
		orgType.click();
		firstListItem.click();
		
		// Select Email ID
		emailIDbox.click();
		emailIDlookup.click();
		//Thread.sleep(2000);
		firstEmailItem.click();
		
		
		String addLine1 = utilities.ExcelUtilities.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Org", "Street1");				
		String city = utilities.ExcelUtilities.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Org", "City");				
		String postCode = utilities.ExcelUtilities.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Org", "PostCode");
		
		// Enter required address details.
		
				
	//	String email = CommonFunctions.ReadExcel.getKeyValueFromExcel("CRMTESTDATA.xlsx", "Org", "Email");
		
		orgAddLine1.click();
		orgAddLine1TxBox.sendKeys(addLine1);
				
		orgCity.click();
		orgCityTxt.sendKeys(city);
		
		orgPostCode.click();
		orgPostCodeTxt.sendKeys(postCode);
		
		
		driver.switchTo().defaultContent();
		return name;	
		
	}
	
	public CrmHome saveOrg()
	{
		
		saveLink.click();
		wait.until(ExpectedConditions.elementToBeClickable(deleteLink));
		return new CrmHome(driver);
		
		
		
			
	}
	
	public boolean searchForOrg(String name)
	{
		Boolean flag = false;
		try
		{
			
			driver.switchTo().frame("contentIFrame0");
			srcCriTxt.clear();
			
			srcCriTxt.sendKeys(name);
			srcCriIcon.click();
			
			
			//System.out.println((driver.findElement(By.xpath("//table[@id='gridBodyTable']/tbody/tr[1]/td[3]"))).getText());
			
	/*		List<String> OrgNames = CommonFunctions.TableData.getDataFromColumnInTable(gridTable, 2);
					//System.out.println(OrgNames);
					OrgNames.forEach(System.out::println);		  


			driver.switchTo().defaultContent();*/
			
			
			flag = UITableDataSupport.getDataFromColumnInTable(gridTable, 2, name);
			
			//System.out.println(ActVal);
			//System.out.println(ActVal);
			
		}
		catch(Exception e)
		{
			System.out.println("Found Exception : " + e);
		}
		return flag;
	}


	

}
