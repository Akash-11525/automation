set projectLocation=D:\CapitaPCSEAutomation_Akshay\TestAutomation\PCSE_Automation_Maven\Automation_Project
cd /D %projectLocation%
call mvn -f overnight_pom.xml clean install
call mvn exec:java@RunExcelReportGenerator
echo "Generating Report..."
timeout /t 5
pause