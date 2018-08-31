package listener;

import com.relevantcodes.extentreports.LogStatus;
import com.setup.BaseTest;
import com.setup.GetIp;
import io.qameta.allure.Attachment;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.Story;
import io.qameta.allure.model.Label;
import io.qameta.allure.model.Link;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import io.qameta.allure.model.TestResult;
import io.qameta.allure.util.ResultsUtils;
import listener.extendManager.ExtendManager;
import listener.extendManager.ExtendTestManager;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class TestListener extends BaseTest implements ITestListener {

    private String testResult;
    private Logger log = LogManager.getLogger(getClass().getName());


    @Attachment(value = "{0}", type = "text/plain")
    private static String saveTextLog(String message) {
        return message;
    }

    @Attachment(value = "{0}", type = "text/html")
    public static String attachHtml(String html) {
        return html;
    }

    private String getTestResult() {
        return testResult;
    }

    private void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    @Attachment(value = "Page screenshot", type = "image/png")
    private byte[] saveScreenshotPNG(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtendTestManager.startTest(result.getMethod().getMethodName(), "");
        MDC.put("testMethodName",result.getName());
        MDC.put("deviceName","Ahmet");
        MDC.put("platformVersion","10.2");
        MDC.put("appVersion", "3.4.1");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtendTestManager.getTest().log(LogStatus.PASS, "Test passed");
        setTestResult("PASSED");
        setTestFailedMsg("-");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        setTestResult("FAILED");
        setTestFailedMsg(result.getThrowable().getMessage());
        Object testClass = result.getInstance();
        WebDriver driver = ((BaseTest) testClass).getDriver();
        //Allure ScreenShotRobot and SaveTestLog
        if (driver != null) {saveScreenshotPNG(driver);}
        //Save a log on allure.
        saveTextLog(getTestCaseName() + " failed and screenshot taken!");
        //Take base64Screenshot screenshot for extent reports
        assert ((TakesScreenshot) driver) != null;
        String screenshot = "data:image/png;base64," + ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        //Extentreports log and screenshot operations for failed tests.
        ExtendTestManager.getTest().log(LogStatus.FAIL, "Test Failed",ExtendTestManager.getTest().addBase64ScreenShot(screenshot));
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtendTestManager.getTest().log(LogStatus.SKIP, "Test Skipped");
        setTestResult("SKIPPED");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        result.getMethod().getConstructorOrMethod().getName();
    }

//    @Override
//    public void onStart(ITestContext context) {
//
//    }

    @Override
    public void onStart(ITestContext context) {
        GetIp ip = new GetIp();
        setTestCaseName(context.getName());
        setTestStartTime(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
        MDC.put("ip", ip.getIP());
        MDC.put("testCaseName", getTestCaseName());
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtendTestManager.endTest();
        ExtendManager.getReporter().flush();
        //Log4j
        setTestFinishTime(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
        setTestDurationTime(getTestFinishTime() - getTestStartTime());
        MDC.put("testFailedMsg",getTestFailedMsg());
        MDC.put("testDuration", getTestDurationTime());
        if (Objects.equals(getTestResult(), "PASSED")) {
            log.info(getTestResult());
        } else if (Objects.equals(getTestResult(), "FAILED")) {
            log.error(getTestResult());
        } else {
            log.warn(getTestResult());
        }
    }
}
