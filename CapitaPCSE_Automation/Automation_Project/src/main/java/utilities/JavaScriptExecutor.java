package utilities;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class JavaScriptExecutor 
{
	public static String getValueForJavaScript(WebDriver driver,String javaScript)
	{
    	JavascriptExecutor js = (JavascriptExecutor)driver;
    	return (String) js.executeScript(javaScript);
	}

	public static String executeJavaScriptOnSpecificElement(WebDriver driver,String javaScript, WebElement specificElement)
	{
    	JavascriptExecutor js = (JavascriptExecutor)driver;
    	return (String) js.executeScript(javaScript, specificElement);
	}
}
