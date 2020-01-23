package pageobjects;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

public class GoogleAdvanceSearch 
{
	WebDriver driver;

	public GoogleAdvanceSearch(WebDriver driver)
	{
		this.driver = driver;
		// PageFactory.initElements( driver, this);
		PageFactory.initElements(new AjaxElementLocatorFactory(driver, 20), this);
	}

	@FindBy(id = "_dKg")
	public WebElement words;

	@FindBy(id = "_aKg")
	public WebElement phrase;

	@FindBy(id = "_cKg")
	public WebElement anywords;

	@FindBy(id = "_bKg")
	public WebElement nonewords;

	@FindBy(id = "_RKg")
	public WebElement startRange;

	@FindBy(xpath = "html/body/div[1]/div[4]/form/div[2]/div[5]/div[2]/div/div[3]/input")
	public WebElement endRange;

	@FindBy(xpath = "//div[@id='lr_button']")
	public WebElement languageList;

	@FindBy(xpath = "//ul[@id='lr_menu']/li")
	public List<WebElement> language;

	@FindBy(id = "cr_button")
	public WebElement regionList;

	@FindBy(xpath = "//ul[@id='cr_menu']/li")
	public List<WebElement> region;

	@FindBy(id = "as_qdr_button")
	public WebElement updateList;

	@FindBy(xpath = "//ul[@id='as_qdr_menu']/li")
	public List<WebElement> update;

	@FindBy(id = "_SKg")
	public WebElement Domain;

	@FindBy(id = "as_occt_button")
	public WebElement termList;

	@FindBy(xpath = "//ul[@id='as_occt_menu']/li")
	public List<WebElement> term;

	@FindBy(id = "as_safesearch_button")
	public WebElement safesearchList;

	@FindBy(xpath = "//ul[@id='as_safesearch_menu']/li")
	public List<WebElement> safesearch;

	@FindBy(id = "as_filetype_button")
	public WebElement filetypeList;

	@FindBy(xpath = "//ul[@id='as_filetype_menu']/li")
	public List<WebElement> filetype;

	@FindBy(id = "as_rights_button")
	public WebElement rightsList;

	@FindBy(xpath = "//ul[@id='as_rights_menu']/li")
	public List<WebElement> rights;

	public void setWord(String setrword) {
		words.sendKeys(setrword);
	}

	public void setphrase(String setphrase) {
		phrase.sendKeys(setphrase);
	}

	public void setanywords(String setanywords) {
		anywords.sendKeys(setanywords);
	}

	public void setnonewords(String setnonewords) {
		nonewords.sendKeys(setnonewords);
	}

	public void setstartRange(String setstartRange) {
		startRange.sendKeys(setstartRange);
	}

	public void setendRange(String setendRange) {
		endRange.sendKeys(setendRange);
	}

	public void setlanguage(String setlanguage) {

		languageList.click();

		System.out.println("Size" + language.size());

		for (WebElement element : language) {

			if (element.getText().equalsIgnoreCase(setlanguage)) {

				System.out.println("Lanuage setlanguage" + setlanguage);
				element.click();
				System.out.println("Done selection");

			}

		}

	}

	public void setregion(String setregion) {

		regionList.click();

		System.out.println("Size" + region.size());

		for (WebElement element : region) {

			if (element.getText().equalsIgnoreCase(setregion)) {

				System.out.println("region setlanguage" + setregion);
				element.click();
				System.out.println("Done selection");

			}

		}

	}

	public void setupdate(String setupdate) {

		updateList.click();

		// System.out.println("Size" + update.size());

		for (WebElement element : update) {

			if (element.getText().equalsIgnoreCase(setupdate)) {

				System.out.println("Lanuage setlanguage" + setupdate);
				element.click();
				System.out.println("Done selection");

			}

		}

	}

	public void setdomain(String setdomain) {
		Domain.sendKeys(setdomain);

	}

	public void setterm(String setterm) {

		termList.click();

		for (WebElement element : term) {

			if (element.getText().equalsIgnoreCase(setterm)) {

				System.out.println("Term setlanguage" + setterm);
				element.click();
				System.out.println("Done selection");

			}

		}

	}

	public void setsafesearch(String setsafesearch) {

		safesearchList.click();

		System.out.println("Size" + safesearch.size());

		for (WebElement element : safesearch) {

			if (element.getText().equalsIgnoreCase(setsafesearch)) {

				element.click();
				System.out.println("Done selection");

			}

		}

	}

	public void setfiletype(String setfiletype) {

		filetypeList.click();

		for (WebElement element : filetype) {

			if (element.getText().equalsIgnoreCase(setfiletype)) {

				System.out.println("filetype setlanguage" + setfiletype);
				element.click();
				System.out.println("Done selection");

			}

		}

	}

	public void setrights(String setrights) {

		rightsList.click();

		System.out.println("Size" + rights.size());

		for (WebElement element : rights) {

			if (element.getText().equalsIgnoreCase(setrights)) {

				element.click();
				System.out.println("Done selection");

			}

		}

	}
}
