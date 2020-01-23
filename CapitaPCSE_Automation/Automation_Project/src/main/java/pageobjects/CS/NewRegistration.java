package pageobjects.CS;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import helpers.*;

public class NewRegistration extends Support{

	WebDriver driver;
	WebDriverWait wait;

	
	@FindBy(css="table[id='pcss-patient-new-registration']")
	WebElement newRegTable;
	
	@FindBy(css="button[id='newRegistrationCSV']")
	WebElement newRegCSVBtn;

	@FindBy(xpath="//table[@id='pcss-patient-new-registration']/tbody")
	WebElement tblNewReg;
	
	@FindBy(id="newRegistrationsBadge")
	WebElement newRegBadge;
	
	public NewRegistration(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}
	
	public List<ArrayList<String>> GetNewRegData(){
		
		List<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		
		data = UITableDataSupport.getUITableData(newRegTable);
		
		return data;
		
	}
	
	public NewRegistration GetNewRegCSV() throws InterruptedException
	{
		
		scrolltoElement(driver, newRegCSVBtn);
		newRegCSVBtn.click();
		Thread.sleep(10000);
		return new NewRegistration(driver);
	}
	
	public List<ArrayList<String>> getNewRegData()
	{
		List<ArrayList<String>> tblData = new ArrayList<ArrayList<String>>();
	//	Week4Tab.click();
		//tblData = UITableDataSupport.getUITableData(tblToDOWeek4);
		 List<Integer> colnums = new ArrayList<Integer>();
		 colnums.add(1);
		 colnums.add(2);
		 colnums.add(3);
		 colnums.add(4);
		 colnums.add(5);
		 colnums.add(6);
		 colnums.add(7);
		 
		

		tblData = UITableDataSupport.getSpecificColumnDataInTable(tblNewReg,colnums );
		
		
		
		return tblData;
	}
	
	public boolean newRegPatients()
	{
		Boolean fl = false;
		String NewRegCount = newRegBadge.getText();
		//String PC = CurrentWeekPatientsToDo.getText();
		System.out.println("New Reg Count: "+NewRegCount);
				
		int count = Integer.parseInt(NewRegCount);
		System.out.println(count);
		
		if(count > 0)
			fl = true;
		
		return fl;
				
		//return new PNLPage(driver);
	}

	








}
