package pageobjects.GMP;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Screenshot;
import helpers.Support;

public class VarianceReportDetail extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(id="PaymentType")
	WebElement PaymentTypeFilter;
	
	public VarianceReportDetail(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 80);	
		PageFactory.initElements(this.driver, this);
	}

	public boolean verifyPaymentLine(String paycode,String keyname) throws InterruptedException, IOException {
		boolean isPaymtLine= false;
		List<WebElement> detailsGrid= driver.findElements(By.xpath("//table[@id='VarianceReportDetailDataTable']//tbody/tr"));
		int recordCount= detailsGrid.size();
		for(int i=1;i<=recordCount;i++){
			WebElement varDetail= driver.findElement(By.xpath("//table[@id='VarianceReportDetailDataTable']//tbody/tr["+i+"]/td[2]"));
			String strPaycode= varDetail.getText().toString();
			if(strPaycode.equalsIgnoreCase(paycode)){
				isPaymtLine=true;
				varianceDetailsSnap(keyname+"_POS Payment Line_"+strPaycode);
				break;
			}
		}
		return isPaymtLine;
	}
	
	public void varianceDetailsSnap(String note) throws InterruptedException, IOException
	{
		scrolltoElement(driver, PaymentTypeFilter);
		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,500)", "");
		Screenshot.TakeSnap(driver, note+"_2");
		
	}
}
