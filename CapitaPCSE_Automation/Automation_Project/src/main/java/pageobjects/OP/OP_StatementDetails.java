package pageobjects.OP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Screenshot;
import helpers.Support;

public class OP_StatementDetails extends Support {
	
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy (xpath="//table[@class='table statement-grid']/tbody/tr/td/a")
	WebElement adjustmentCollapseLink;
	
	@FindBy(id="BtnExpandAll")
	WebElement expandAllBtn;
	
	@FindBy(css="table[class='table statement-grid']")
	WebElement tableBody;
	
	public OP_StatementDetails(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);
		//This initElements method will create all WebElements
		PageFactory.initElements(this.driver, this);
	}
	
	public void clickOnAdjustmentLink()
	{
		wait.until(ExpectedConditions.elementToBeClickable(adjustmentCollapseLink)).click();
	}
	
	public void OPStatementDetailsSnaps(String note) throws InterruptedException, IOException
	{
		Thread.sleep(2000);
		Screenshot.TakeSnap(driver, note);
		Thread.sleep(1000);
	}

	public boolean verifyPresenceOfDocument(String claimNo,String GOSType) throws InterruptedException {
		boolean isDocPresent= false;
		//Support.waitTillProcessing(driver);
		List<WebElement> records= new ArrayList<WebElement>();
		Thread.sleep(1000);
		wait.until(ExpectedConditions.elementToBeClickable(expandAllBtn)).click();
		Thread.sleep(6000);
		wait.until(ExpectedConditions.visibilityOf(tableBody));
		List<WebElement> rows= driver.findElements(By.xpath("//table[@class='table statement-grid']//tr[@class='statement-header']"));
		wait.until(ExpectedConditions.visibilityOfAllElements(rows));
		int rowCount= rows.size();
		parentLoop:
		for(int i=1;i<=rowCount;i++){
			WebElement row= driver.findElement(By.xpath("//table[@class='table statement-grid']//tr[@class='statement-header']["+i+"]/td[1]/a[@class='trigger']"));
			scrolltoElement(driver, row);
			wait.until(ExpectedConditions.visibilityOf(row));
			scrolltoElement(driver, row);
			String rowText= row.getText().toString();
			if(rowText.contains(GOSType)){
				row.click();
				Thread.sleep(3000);
				wait.until(ExpectedConditions.elementToBeClickable(row));
				row.click();
				Thread.sleep(3000);
				String id= "collapseTR_"+Integer.toString(i);
				records= driver.findElements(By.xpath("//tr[@id='"+id+"']//tr[@class='odd']|//tr[@class='even']"));
				wait.until(ExpectedConditions.visibilityOfAllElements(records));
				int recordsCount= records.size();
				System.out.println("Number of records are: "+recordsCount);
				
				//This will verify document number by using paginations
				boolean flag= true;
				while(flag){
					for(int j=1;j<=recordsCount;j++){
						WebElement record= driver.findElement(By.xpath("//tr[@id='"+id+"']//tr["+j+"]/td[2]/a"));
						scrolltoElement(driver, record);
						wait.until(ExpectedConditions.visibilityOf(record));
						String recordText= record.getText().toString();
						if(recordText.equalsIgnoreCase(claimNo)){
							isDocPresent= true;
							flag= false;
							break parentLoop;
						}
					}
					WebElement nextBtn= driver.findElement(By.xpath("//tr[@id='"+id+"']//li[@class='paginate_button next']"));
					if(nextBtn.isEnabled()){
						scrolltoElement(driver, nextBtn);
						wait.until(ExpectedConditions.elementToBeClickable(nextBtn)).click();
					}
				}
			}
		}
		return isDocPresent;
	}
	
	

}
