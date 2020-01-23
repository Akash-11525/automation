package pageobjects.PL;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Screenshot;
import helpers.Support;
import utilities.ExcelUtilities;

public class DeclarationBody extends Support {
	
	
	WebDriver driver;
	WebDriverWait wait;

	@FindBy(id="PLI_BodyCorporateDeclarations")
	WebElement DeclarationBodyTab;
	
	
	@FindBy(name="btnSaveNext")
	WebElement Save_Submit;
	
	@FindBy(xpath="//div[@id='DivContainer']//div[starts-with(@class,'h3')]")
	WebElement Header;

	
	
	String SelectDeclarationBody_DecBody = utilities.ExcelUtilities.getKeyValueFromExcelWithPosition("PerformerPortal.xlsx","DeclarationBody","a director of a Body Corporate", 1);
	

	
	public DeclarationBody(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 60);
		PageFactory.initElements(this.driver, this);
	}


	public String SelectCorporateDeclaration(String Value) throws InterruptedException {
		String ActualTablename_DeclarationBody = null;
		try{
			wait.until(ExpectedConditions.elementToBeClickable(DeclarationBodyTab));
			ActualTablename_DeclarationBody = DeclarationBodyTab.getAttribute("id");
		//	scrolltoElement(driver, Header);
			Thread.sleep(1500);
		//	List<WebElement> Radiobuttons=driver.findElements(By.xpath("//div[@class='radio radio-right margin-right-0']//label"));
			
			List<WebElement> Radiobuttons=driver.findElements(By.xpath("//div[@class='radio radio-right margin-right-0 ipad-radio-btn-margin']//label"));
			System.out.println("total Radio buttons "+Radiobuttons.size());
			for (WebElement Radiobutton : Radiobuttons)
			{                   
				scrolltoElement(driver, Radiobutton);

				//  String ButtonValue = wait.until(ExpectedConditions.elementToBeClickable(button)).getText();
				String RadioValue = Radiobutton.getText(); 

				System.out.println(RadioValue);
				Thread.sleep(3000);

				if (RadioValue.equalsIgnoreCase(Value))
				{
									
					Radiobutton.click();
					break;

				}
			}


			
	
			//helpers.CommonFunctions.ClickOnRadioButton("No", driver);
		 /*      List<WebElement> DeclarationBodyButtons = driver.findElements(By.id("IsBodyCorportate"));
	            System.out.println(DeclarationBodyButtons.size());
	            for (WebElement DeclarationBodyButton:DeclarationBodyButtons)
	            {
	            	if((DeclarationBodyButton.getText()).equalsIgnoreCase(SelectDeclarationBody_DecBody))
	            			{
	            		DeclarationBodyButton.click();
	            		break;
	            			}
	            }*/
			Thread.sleep(3000);
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found on DeclarationBodyTab tab" +e);
		}	
		return ActualTablename_DeclarationBody ;
	}
	
	
	public String SelectCorporateDeclaration_Yes(String Value) throws InterruptedException {
		String ActualTablename_DeclarationBody = null;
		try{
			wait.until(ExpectedConditions.elementToBeClickable(DeclarationBodyTab));
			ActualTablename_DeclarationBody = DeclarationBodyTab.getAttribute("id");
		//	scrolltoElement(driver, Header);
			Thread.sleep(1500);
			List<WebElement> Radiobuttons=driver.findElements(By.xpath("//div[@class='radio radio-right margin-right-0']//label"));			

			System.out.println("total Radio buttons "+Radiobuttons.size());
			for (WebElement Radiobutton : Radiobuttons)
			{                   
				scrolltoElement(driver, Radiobutton);

				//  String ButtonValue = wait.until(ExpectedConditions.elementToBeClickable(button)).getText();
				String RadioValue = Radiobutton.getText(); 

				System.out.println(RadioValue);
				Thread.sleep(1500);

				if (RadioValue.equalsIgnoreCase(Value))
				{
									
					Radiobutton.click();
					break;

				}
			}

			Thread.sleep(3000);
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found on DeclarationBodyTab tab" +e);
		}	
		return ActualTablename_DeclarationBody ;
	}



	public CreateNewApp ClickOnSave_Submit() throws InterruptedException {
		try{
			Thread.sleep(1500);
			Actions action = new Actions(driver);
			action.moveToElement(Save_Submit);
			action.doubleClick().build().perform();
			helpers.CommonFunctions.PageLoadExternalwait(driver);
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found Savebutton - Capacitytab" +e);
		}	
		return new CreateNewApp(driver);
	}


	public DeclarationBody selectcorporateDeclarationUpload(int columnNumber) {
		try{
			List<String> CellValues = ExcelUtilities.getCellValuesInExcel("PerformerPortal.xlsx", "DeclarationBody", columnNumber);
			List<String> CellValuesNextColumn = ExcelUtilities.getCellValuesInExcel("PerformerPortal.xlsx", "DeclarationBody", columnNumber+1);
            for (int i = 1; i < CellValues.size()+1; i++)
            
            {
            String CellValue = CellValues.get(i);
            String CellValuenextColumn = CellValuesNextColumn.get(i);
            System.out.println(CellValuenextColumn);
            System.out.println("The cell value is: "+CellValue);
            System.out.println(i);
            List<WebElement> ele = driver.findElements(By.id("Declarations_"+i+"__Answer"));
            System.out.println(ele.size());
            for (WebElement elements:ele)
            {
            	scrolltoElement(driver, elements);
            if(elements.getAttribute("value").equalsIgnoreCase(CellValue))
            {
            	Thread.sleep(1000);
            	elements.click();
            	if(CellValue.equalsIgnoreCase("Yes"))
            	{
            		driver.findElement(By.id("Declarations_"+i+"__Explanation")).clear();
            		driver.findElement(By.id("Declarations_"+i+"__Explanation")).sendKeys(CellValuenextColumn);
                 	List<String> Extensions = ExcelUtilities.getCellValuesInExcel("PerformerPortal.xlsx", "FileExtension", 1);
            		for(String Extension : Extensions)
            		{
            		WebElement Choosebutton = driver.findElement(By.xpath("//*[@id='divExplanation"+i+"']/div[3]/div[1]/div/label//input"));           	
            		scrolltoElement(driver, Choosebutton); 
            		scrolltoElement(driver, Choosebutton);
        			JavascriptExecutor jse = (JavascriptExecutor)driver; 
        			jse.executeScript("arguments[0].scrollIntoView();", Choosebutton);
        		//	jse.executeScript("arguments[0].click();", Choosebutton);
        			String filePath = System.getProperty("user.dir") + "\\Upload\\"+Extension;			
        			//helpers.CommonFunctions.Uploadfile(filePath, driver);
        			Choosebutton.sendKeys(filePath);
        			WebElement Uploadbutton = driver.findElement(By.xpath("//*[@id='divExplanation"+i+"']/div[3]/div[1]//div/div[1]//button"));
        	      
        	            	if(Uploadbutton.isDisplayed())
        	            	{
        	            		Uploadbutton.click();
        	            		helpers.CommonFunctions.PageLoadExternalwait(driver); 
        	            	}
        	            
            		}
            	//	Thread.sleep(9000);
                		
            	}
            }
            
            }
            }        
			
		}
		catch(Exception e)
		{
			System.out.println("The declaration body is not capured after fisrt question answered as Yes" +e);
		}
		return new DeclarationBody(driver);
	}

	public boolean SelectdeclarationBodyUploadcount(int columnNumber) throws IOException, InterruptedException {
		boolean uploadcount = false;
		try{

			List<String> CellValues = ExcelUtilities.getCellValuesInExcel("PerformerPortal.xlsx", "DeclarationBody", 2);
			for (int i = 1; i < CellValues.size()+1; i++)
            
            {
            String CellValue = CellValues.get(i);
           System.out.println("The cell value is: "+CellValue);
    
            	if(CellValue.equalsIgnoreCase("Yes"))
            	{
            		//div[@id='dvisQuestion0']//table[contains(@id,'tblUploaded')]//tbody
            		WebElement Table = driver.findElement(By.xpath("//*[@id='divExplanation"+i+"']//table[contains(@id,'tblUploaded')]//tbody"));
            		List<WebElement> rows = Table.findElements(By.tagName("tr"));
       			 int Actualcount = rows.size();
       			List<String> Extensions = ExcelUtilities.getCellValuesInExcel("PerformerPortal.xlsx", "FileExtension", 1);
       			int expecteddownload = Extensions.size();
       			 if(expecteddownload == Actualcount)
       			 {
       				uploadcount = true;
       				System.out.println("The declartaion of "+i+" uploaded all expected files ");
       			 }
            
           
            	}
       		
            }
		}
		
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found on Declaration tab" +e);
		}	
		return uploadcount ;	
	}


	public void screenshot(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, Header);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		
	}



}
