package pageobjects;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TraineeOptometrist {
	
	WebDriver driver;
	WebDriverWait wait;
	
	@FindBy(xpath="//a[contains(@id, 'oph_traineeoptometrist.NewRecord-Large')]")
	WebElement createTraineeOptometristButton;
	
	@FindBy(id="oph_gocnumber")
	WebElement gocnumberField;
	
	@FindBy(id="oph_gocnumber_i")
	WebElement gocnumberTxtField;
	
	@FindBy(id="oph_name")
	WebElement gocnameField;
	
	@FindBy(id="oph_gocname_i")
	WebElement gocnameTxtField;
	
	
	
	
	
	public TraineeOptometrist(WebDriver driver){

		this.driver = driver;
		//driver.switchTo().frame("resultFrame");

		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);

	}
	
	

}
