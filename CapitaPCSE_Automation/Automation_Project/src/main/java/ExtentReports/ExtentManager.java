
package ExtentReports;
 
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

//import com.relevantcodes.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import helpers.CommonFunctions;
 
//OB: ExtentReports extent instance created here. That instance can be reachable by getReporter() method.
 
public class ExtentManager {
 
    private static ExtentReports extent;
    public static ExtentHtmlReporter htmlReporter;
 
    public synchronized static ExtentReports getReporter(){
        if(extent == null){
            //Set HTML reporting file location
        	String workingDir= createExtentReportDirectory();
        	String suffix= CommonFunctions.getDate("MMM_dd_yyyy_HH_mm");
            //extent = new ExtentReports(workingDir+"\\ExtentReport_"+suffix+".html", true);
        	htmlReporter = new ExtentHtmlReporter(workingDir+"\\ExtentReport_"+suffix+".html");
        	 extent = new ExtentReports();
        	 extent.attachReporter(htmlReporter);
        }
        return extent;
    }
    
	private static String createExtentReportDirectory()
	{
		File dir = new File(System.getProperty("user.dir") + "/ExtentReports/" + new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime()));

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
}