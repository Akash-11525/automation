package reporting;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.internal.Utils;

import com.aventstack.extentreports.Status;

import ExtentReports.ExtentManager;
import ExtentReports.ExtentTestManager;
import verify.TestMethodErrorBuffer;
import browsersetup.BaseClass;


public class ListenerClass extends BaseClass implements ITestListener,IInvokedMethodListener{


	private String createDirectory()
	{
		File dir = new File(System.getProperty("user.dir") + "/Screenshots/" + new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime()));

		if(dir.exists())
		{
			System.out.println("A folder with name '"+new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime())+"' already exist");
		}
		else
		{
			dir.mkdir();
		}
		return dir.getPath();
	}

	public void onStart(ITestContext result) {
		System.out.println("Start Of Execution(TEST)->"+result.getName());
	}

	public void onTestStart(ITestResult result) {
		System.out.println("Test Started->"+result.getName());
		//Start operation for extentreports.
        //ExtentTestManager.startTest(result.getMethod().getMethodName(),""); //commented by Akshay as it is not in use
		String className= result.getInstanceName().toString();
		className= className.split("\\.")[1].toString();
        ExtentTestManager.startTest(className+" - "+result.getMethod().getMethodName(),"");
	}

	public void onTestSuccess(ITestResult result) {
		Object currentClass = result.getInstance();
		System.out.println(getAssertMessage());
		if(((BaseClass) currentClass).getAssertMessage() != null)
		{
			for(String eachString : ((BaseClass) currentClass).getAssertMessage())
				Reporter.log(eachString);
		}
		//Extentreports log operation for passed tests.
        ExtentTestManager.getTest().log(Status.PASS, "Test passed");
		for(String eachString : ((BaseClass) currentClass).getAssertMessage()){
			ExtentTestManager.getTest().log(Status.PASS, eachString);
		}
		System.out.println("Test Pass->"+result.getName());
		TestMethodErrorBuffer.remove();
	}

	public void onTestFailure(ITestResult result) {
		System.out.println("Test Failed->"+result.getName());
		//WebDriver driver = BaseClass.driver;
		Object currentClass = result.getInstance();
		WebDriver driver = ((BaseClass) currentClass).getDriver();
		try
		{
			String path = createDirectory() + "\\";
			String file = path + result.getName() + "_" + new SimpleDateFormat("ddMMyyyy_HHmmss").format(Calendar.getInstance().getTime()) +".png";
			if(driver!=null){
				String sessionId= BaseClass.getSessionIdFromDriver(driver);
				if(sessionId!=null){
					ReportBaseClass.takeSnapShot(driver,file);
					Reporter.log(file);
				}
			}
			//ReportBaseClass.takeSnapShot(driver,file);//Commented by Akshay for fetching snapshot based on session ID
			System.out.println(getAssertMessage());
			if(((BaseClass) currentClass).getAssertMessage() != null)
			{
				for(String eachString : ((BaseClass) currentClass).getAssertMessage())
					Reporter.log(eachString);
			}
			//Reporter.log(file);//Commented by Akshay for fetching snapshot based on session ID

			if(!result.getName().equalsIgnoreCase("setup") && !result.getName().equalsIgnoreCase("quit"))
			{
				List<Throwable> lThrowable = TestMethodErrorBuffer.get();

				//if there are verification failures 
				if(lThrowable != null) //lThrowable.size() > 0
				{
					int size = lThrowable.size();

					StringBuffer failureMessage = new StringBuffer("Steps Failed (").append(size).append(")\n");
					StringBuffer fullStack = new StringBuffer();

					for(int i =0 ; i < size-1; i++)
					{	
						failureMessage.append("(").append(i+1).append(")").append(lThrowable.get(i).getClass().getName()).append(":").append(lThrowable.get(i).getMessage()).append("\n");						
						fullStack.append("Failure ").append(i+1).append(" of ").append(size).append("\n");	
						fullStack.append(Utils.stackTrace(lThrowable.get(i),false)[1]).append("\n");
					}

					fullStack.append("Failure ").append(size).append(" of ").append(size).append("\n");
					Throwable last = lThrowable.get(size-1);					
					failureMessage.append("(").append(size).append(")").append(last.getClass().getName()).append(":").append(last.getMessage()).append("\n\n");

					fullStack.append(last.toString());

					result.setThrowable(new Throwable(failureMessage.toString() + fullStack.toString()));
					result.getThrowable().setStackTrace(last.getStackTrace());

				}
				else
				{
					if(result.getThrowable() != null)
					{
						lThrowable = new ArrayList<Throwable>();
						lThrowable.add(result.getThrowable());
						int size = lThrowable.size();
						if(size == 1)
						{
							StringBuffer failureMessage = new StringBuffer("Steps Failed (").append(size).append(")\n");
							result.setThrowable(new Throwable(failureMessage.toString() + lThrowable.get(0).toString().replace("java.lang.AssertionError:", "")));
						}
					}
				}

				TestMethodErrorBuffer.remove(); // remove stale
			}

	        //Extentreports log and screenshot operations for failed tests.
			if(driver!=null){
				String sessionId= BaseClass.getSessionIdFromDriver(driver);
				if(sessionId!=null){
					//ExtentTestManager.getTest().log(Status.FAIL,"Test Failed",ExtentTestManager.getTest().addBase64ScreenShot(file));
					ExtentTestManager.getTest().log(Status.FAIL,"Test Failed");
					ExtentTestManager.getTest().addScreenCaptureFromPath(file);
				}
				ExtentTestManager.getTest().log(Status.FAIL, result.getThrowable());
			}
			if(/*!((BaseClass) currentClass).getAssertMessage().isEmpty()&&*/((BaseClass) currentClass).getAssertMessage()!=null){
				for(String eachString : ((BaseClass) currentClass).getAssertMessage()){
					ExtentTestManager.getTest().log(Status.PASS, eachString);
				}
			}
			ExtentTestManager.getTest().log(Status.FAIL, result.getThrowable());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void onTestSkipped(ITestResult result) {
		System.out.println("Test Skipped->"+result.getName());
		TestMethodErrorBuffer.remove();
		//Extentreports log operation for skipped tests.
        ExtentTestManager.getTest().log(Status.SKIP, "Test Skipped");
	}

	public void onFinish(ITestContext result) {
		System.out.println("END Of Execution(TEST)->"+result.getName());
		//Do tier down operations for extentreports reporting!
        ExtentTestManager.endTest();
        ExtentManager.getReporter().flush();
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
	}

	public void afterInvocation(IInvokedMethod arg0, ITestResult arg1) 
	{
		// TODO Auto-generated method stub
	}

	public void beforeInvocation(IInvokedMethod method, ITestResult iTResult) 
	{
		if(method.isTestMethod())
		{	
			if(!iTResult.getName().equalsIgnoreCase("setup") && !iTResult.getName().equalsIgnoreCase("quit"))
			{
				if(TestMethodErrorBuffer.get()!=null)
				{
					TestMethodErrorBuffer.set(new ArrayList<Throwable>());
					throw new RuntimeException("Stale error buffer detected!");
				}
				System.out.println("Error Buffer Cleared Before Invocation for -> "+iTResult.getName());
				//TestMethodErrorBuffer.set(new ArrayList<Throwable>()); // each test method will have its own error buffer
			}
		}

	}



}
