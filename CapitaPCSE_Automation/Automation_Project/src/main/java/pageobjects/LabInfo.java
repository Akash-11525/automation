package pageobjects;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LabInfo {

	WebDriver driver;

	WebDriverWait wait;
	
	
	@FindBy(xpath="//*[@id='scr_localdistrictcode_c']/span/span")
	WebElement LabelDistrictcodeTextField;
	
	//*[@id="scr_lbchpv_c"]/span/span
	
	@FindBy(id="scr_localdistrictcode_warn")
	WebElement ErrorImage;

	@FindBy(xpath="//*[@id='scr_lbchpv_c']/span/span")
	WebElement LabelLBCHPV;
	
	//*[@id="scr_lbchpv_c"]/span/span
	@FindBy(xpath="//*[@id='scr_localdistrictcode_i']")
	WebElement DistrictcodeTextField;
	
	@FindBy (xpath=("//table[@ologicalname='scr_cervicaltestresult']"))
	WebElement cervialTestResult;
	
	@FindBy(xpath="//*[@id='scr_localdistrictcode']")
	WebElement BeforeLocaldistrictTextfield;
	
	@FindBy(xpath="//img[@ src='/_imgs/inlineedit/save.png']")
	WebElement SaveButton;
	
	
	@FindBy(xpath="//*[@id='footer_statuscontrol']")
	WebElement BeforeSaveimage;
	
	@FindBy(xpath="//*[@id='scr_localdistrictcode_err']")
	WebElement TooltipText;
	
	@FindBy(xpath="//*[@id='scr_lbchpv']")
	WebElement BeforeLBCHPVField;
	
	@FindBy(xpath="//*[@id='scr_lbchpv_i']")
	WebElement LBCHPVField;
	
	
	
	
	
	
	public LabInfo(WebDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait = new WebDriverWait(this.driver, 30);

		//This initElements method will create all WebElements

		PageFactory.initElements(this.driver, this);
// TODO Auto-generated constructor stub
	}
	

	public boolean VerifyDistrictTextField() throws InterruptedException 
	{
				Boolean f2 = false;
		
				driver.switchTo().frame("contentIFrame1");
		try {
			String Districtlabel = "Local District Code";
			String ActualDistrictlabel = LabelDistrictcodeTextField.getText();
			System.out.println(ActualDistrictlabel);
			if (ActualDistrictlabel.equalsIgnoreCase(Districtlabel))
				{
						BeforeLocaldistrictTextfield.click();
						DistrictcodeTextField.sendKeys("123ASD");
						BeforeSaveimage.click();
						SaveButton.click();
						Actions action = new Actions(driver);
						action.moveToElement(ErrorImage).build().perform();
						ErrorImage.click();
						Thread.sleep(1000);
						
						String Errormessage = TooltipText.getText();
						System.out.println(Errormessage);
						String ExpectedMessage = "You must enter a whole number between 0 and 2,147,483,647.";
						if (Errormessage.equalsIgnoreCase(ExpectedMessage) )
							{
								f2 = true;
					
							}
							else 
							{
								f2 = false;
					
							}
						
				
				}
			driver.switchTo().defaultContent();
		}
		
		catch (NoSuchElementException e){
			System.out.println("Element is not found.");
			
			
		}
		//driver.switchTo().defaultContent();
		return f2;
		

	
	}
	

	public boolean LBC_HPV() throws InterruptedException 
	{
		
		boolean f1 = false;
		
		driver.switchTo().frame("contentIFrame1");
		try {
			
			String ExpectedLabelLBCHPV = "LBC/HPV";
			String ActualLabelLBCHPV = LabelLBCHPV.getText();
			System.out.println(ActualLabelLBCHPV);
				if (ActualLabelLBCHPV.equalsIgnoreCase(ExpectedLabelLBCHPV))
		
		{
			BeforeLBCHPVField.click();
			//LBCHPVField.click();
			
			List<WebElement> dropdownlist = new Select(LBCHPVField).getOptions();
			System.out.println("total list "+dropdownlist.size());
			
			
			for (int i=0;i<dropdownlist.size();i++)
			{
			String Optionvalue = (dropdownlist.get(i).getText());
			System.out.println(Optionvalue);
			if(Optionvalue.equals("LBC") ||Optionvalue.equals("HPV Primary")||Optionvalue.equals(""))
			{
				   f1 = true;
			}
			else
			{
				f1 = false;
			}
			} 
		}
		}
	
				
			
			/*for(WebElement option : dropdownlist){
				System.out.println(option.getText());
				
		        if(option.getText().equals("LBC") ||option.getText().equals("HPV Primary")||option.getText().equals(""))
		        {
		         
		        }
		        else 
		        {
		        	f1 = false;
		        }
		       break;   
			}
		}*/

	//	}
		catch (NoSuchElementException e){
			System.out.println("Element is not found.");
			
			
		}
		return f1;
	}
}

		
	
	
	//}



	

