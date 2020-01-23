package helpers;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryMechanism implements IRetryAnalyzer 
{
    private int retryCount;
    private int maxRetryCount;

    public boolean retry(ITestResult result) 
    {

        if (retryCount < maxRetryCount) 
        {
            retryCount++;
            return true;
        }
        return false;
    }
}
