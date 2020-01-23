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

public class Declaration extends Support{
	
	WebDriver driver;
	WebDriverWait wait;

	@FindBy(id="browseBtn")
	WebElement BrowseButton;	
	
	@FindBy(id="Declaration")
	WebElement DeclarationTab;

	
	@FindBy(name="btnSaveNext")
	WebElement Save_Submit;

	
	public Declaration(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 60);
		PageFactory.initElements(this.driver, this);
	}


	public Declaration Selectdeclaration(int columnNumber) throws IOException, InterruptedException {
	
		try{

			List<String> CellValues = ExcelUtilities.getCellValuesInExcel("PerformerPortal.xlsx", "Declaration", columnNumber);
			List<String> CellValuesNextColumn = ExcelUtilities.getCellValuesInExcel("PerformerPortal.xlsx", "Declaration", columnNumber+1);
            for (int i = 0; i < CellValues.size(); i++)
            
            {
            String CellValue = CellValues.get(i);
            String CellValuenextColumn = CellValuesNextColumn.get(i);
            System.out.println(CellValuenextColumn);
            System.out.println("The cell value is: "+CellValue);
            System.out.println(i);
            //WebElement ele = driver.findElement(By.id(CellValue));
            
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
            	/*	WebElement Choosebutton = driver.findElement(By.xpath("//*[@id='dvisQuestion"+i+"']/div[3]/div[1]/div/label/span"));           	
            		scrolltoElement(driver, Choosebutton);            		
            		driver.findElement(By.xpath("//*[@id='dvisQuestion"+i+"']/div[3]/div[1]/div/label/span")).click();            	
            		Thread.sleep(1000);            		
            		String filePath = System.getProperty("user.dir") + "\\Upload\\sample.txt";        			
        			helpers.CommonFunctions.Uploadfile(filePath, driver);
            		Thread.sleep(1000);*/
            		List<String> Extensions = ExcelUtilities.getCellValuesInExcel("PerformerPortal.xlsx", "FileExtension", 1);
            		for(String Extension : Extensions)
            		{
            		WebElement Choosebutton = driver.findElement(By.xpath("//*[@id='dvisQuestion"+i+"']/div[3]/div[1]/div/label//input"));           	
            		scrolltoElement(driver, Choosebutton); 
            		scrolltoElement(driver, Choosebutton);
        			JavascriptExecutor jse = (JavascriptExecutor)driver; 
        			jse.executeScript("arguments[0].scrollIntoView();", Choosebutton);
        			//jse.executeScript("arguments[0].click();", Choosebutton);
        			String filePath = System.getProperty("user.dir") + "\\Upload\\"+Extension;			
        			//helpers.CommonFunctions.Uploadfile(filePath, driver);
        			Choosebutton.sendKeys(filePath);
        			/* List<WebElement> Uploadbuttons = driver.findElements(By.id("browseBtn"));
        	            System.out.println(Uploadbuttons.size());
        	            for (WebElement Uploadbutton:Uploadbuttons)
        	            {
        	            	Thread.sleep(500);
        	            	scrolltoElement(driver,Uploadbutton);
        	            	if(Uploadbutton.isDisplayed())
        	            	{
        	            		Uploadbutton.click();
        	            		Thread.sleep(3000);
        	            	}
        	            }*/
            		}
            	
            		
            		
//            		driver.findElement(By.name("uploadDeclaration"+i+"")).clear();
            		/*String filePath = System.getProperty("user.dir") + "\\Upload\\FileUpload.exe";
        			System.out.println(filePath);
        			Runtime.getRuntime().exec(filePath);*/
                 		
            	}
            	
            }
            }
            }
            
            List<WebElement> Uploadbuttons = driver.findElements(By.id("browseBtn"));
            System.out.println(Uploadbuttons.size());
            for (WebElement Uploadbutton:Uploadbuttons)
            {
            	Thread.sleep(500);
            	scrolltoElement(driver,Uploadbutton);
            	if(Uploadbutton.isDisplayed())
            	{
            		Uploadbutton.click();
            		Thread.sleep(5000);
            	}
            }
         
   
	
		
		}
		
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found on Declaration tab" +e);
		}	
		return new Declaration (driver) ;	
	}
	
	public boolean SelectdeclarationUploadcount(int columnNumber) throws IOException, InterruptedException {
		boolean uploadcount = false;
		try{

			List<String> CellValues = ExcelUtilities.getCellValuesInExcel("PerformerPortal.xlsx", "Declaration", 2);
			for (int i = 0; i < CellValues.size(); i++)
            
            {
            String CellValue = CellValues.get(i);
           System.out.println("The cell value is: "+CellValue);
    
            	if(CellValue.equalsIgnoreCase("Yes"))
            	{
            		//div[@id='dvisQuestion0']//table[contains(@id,'tblUploaded')]//tbody
            		WebElement Table = driver.findElement(By.xpath("//*[@id='dvisQuestion"+i+"']//table[contains(@id,'tblUploaded')]//tbody"));
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


	public String getactualtablename() {
		String ActualTablename_Declaration = null;
		try{
		wait.until(ExpectedConditions.elementToBeClickable(DeclarationTab));
		ActualTablename_Declaration = DeclarationTab.getAttribute("id");
		}
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found on Declaration tab" +e);
		}	
		return ActualTablename_Declaration ;	
	}



	public CreateNewApp ClickOnSave_Submit() throws InterruptedException {
		try{
			Thread.sleep(2000);
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


	public void screenshot(String note) throws InterruptedException, IOException {
		scrolltoElement(driver, DeclarationTab);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		
	}
	
	public Declaration SelectdeclarationUpload(int columnNumber) throws IOException, InterruptedException {
		
		try{

			List<String> CellValues = ExcelUtilities.getCellValuesInExcel("PerformerPortal.xlsx", "Declaration", columnNumber);
			List<String> CellValuesNextColumn = ExcelUtilities.getCellValuesInExcel("PerformerPortal.xlsx", "Declaration", columnNumber+1);
            for (int i = 0; i < CellValues.size(); i++)
            
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
            	Thread.sleep(3000);
            	elements.click();
            	if(CellValue.equalsIgnoreCase("Yes"))
            	{
            		driver.findElement(By.id("Declarations_"+i+"__Explanation")).clear();
            		driver.findElement(By.id("Declarations_"+i+"__Explanation")).sendKeys(CellValuenextColumn);
                 	List<String> Extensions = ExcelUtilities.getCellValuesInExcel("PerformerPortal.xlsx", "FileExtension", 1);
            		for(String Extension : Extensions)
            		{
            		WebElement Choosebutton = driver.findElement(By.xpath("//*[@id='dvisQuestion"+i+"']/div[3]/div[1]/div/label//input"));           	
            		scrolltoElement(driver, Choosebutton); 
            		scrolltoElement(driver, Choosebutton);
        			JavascriptExecutor jse = (JavascriptExecutor)driver; 
        			jse.executeScript("arguments[0].scrollIntoView();", Choosebutton);
        			jse.executeScript("arguments[0].click();", Choosebutton);
        			String filePath = System.getProperty("user.dir") + "\\Upload\\"+Extension;			
        			//helpers.CommonFunctions.Uploadfile(filePath, driver);
        			Choosebutton.sendKeys(filePath);
        			WebElement Uploadbutton = driver.findElement(By.xpath("//*[@id='dvisQuestion"+i+"']/div[3]/div[1]//div/div[1]//button"));
        	      
        	            	if(Uploadbutton.isDisplayed())
        	            	{
        	            		Uploadbutton.click();
        	            		helpers.CommonFunctions.PageLoadExternalwait(driver); 
        	            	}
        	            
            		}
            		Thread.sleep(9000);
                		
            	}
            }
            
            }
            }        
		} 
		catch(NoSuchElementException e)
		{
			System.out.println("The element is not found on Declaration tab" +e);
		}	
		return new Declaration (driver) ;
	}

	}
            




