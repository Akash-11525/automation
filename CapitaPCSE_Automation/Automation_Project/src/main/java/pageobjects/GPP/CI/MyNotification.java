package pageobjects.GPP.CI;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import helpers.Screenshot;
import helpers.Support;

public class MyNotification extends Support {
	WebDriver driver;
	WebDriverWait wait;
	
	public MyNotification(WebDriver driver){

		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);
	}

	public <T> T clickOnReviewLink(String claimNo,Class<T> expectedPage,String note,String notificationBody) throws InterruptedException, IOException {
		Thread.sleep(3000);
		//String expectedText= "Claim "+claimNo+" "+notificationBody;
		List<WebElement> notifications= driver.findElements(By.xpath("//table[@id='NotificationTable']//tbody/tr"));
		int rowCount= notifications.size();
		for(int i=1;i<=rowCount;i++){
			WebElement row= driver.findElement(By.xpath("//table[@id='NotificationTable']//tbody/tr["+i+"]/td[2]/p"));
			String notificationText= row.getText().toString();
			if(notificationText.startsWith(notificationBody)){
				WebElement reviewLink= driver.findElement(By.xpath("//table[@id='NotificationTable']//tbody/tr["+i+"]/td[4]/a"));
				wait.until(ExpectedConditions.elementToBeClickable(reviewLink));
				if(reviewLink.isDisplayed()&&(reviewLink.isEnabled())){
					captureNotificationSnap(note);
					reviewLink.click();
					Thread.sleep(3000);
					//System.out.println("Clicked on review link for claim number: "+claimNo);
					break;
				}
			}
		}
		return PageFactory.initElements(driver, expectedPage);
	}
	
	public void captureNotificationSnap(String note) throws InterruptedException, IOException
	{

		Screenshot.TakeSnap(driver, note+"_1");
		Thread.sleep(1000);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,800)", "");
		Screenshot.TakeSnap(driver, note+"_2");
	}
	
	public boolean verifyNotificationText(String notificationBody,String note) throws InterruptedException, IOException {
		Thread.sleep(3000);
		boolean isVerified= false;
		//String expectedText= "Claim "+claimNo+" "+notificationBody;
		List<WebElement> notifications= driver.findElements(By.xpath("//table[@id='NotificationTable']//tbody/tr"));
		wait.until(ExpectedConditions.visibilityOfAllElements(notifications));
		int rowCount= notifications.size();
		for(int i=1;i<=rowCount;i++){
			WebElement row= driver.findElement(By.xpath("//table[@id='NotificationTable']//tbody/tr["+i+"]/td[2]/p"));
			wait.until(ExpectedConditions.visibilityOf(row));
			String notificationText= row.getText().toString();
			if(notificationText.equalsIgnoreCase(notificationBody)){
				captureNotificationSnap(note);
				isVerified=true;
				break;
			}else{
				isVerified=false;
			}
		}
		return isVerified;
	}
	
}
