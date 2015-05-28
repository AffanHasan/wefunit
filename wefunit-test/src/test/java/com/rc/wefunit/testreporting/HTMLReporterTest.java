package com.rc.wefunit.testreporting;

import com.bowstreet.util.SystemProperties;
import com.bowstreet.webapp.WebAppAccess;
import com.rc.wefunit.*;
import com.rc.wefunit.testengine.TestEngine;
import mockit.Expectations;
import mockit.Mocked;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * Created by Affan Hasan on 5/14/15.
 */
public class HTMLReporterTest extends TestRunnerBaseClass {

    private final CommonUtils _commonUtils = Factories.CommonUtilsFactory.getInstance();
    private final String _WEBINFPath;
    private final String _DOC_ROOT_LINUX;
    private final String _DOC_ROOT_WIN;
    private final String _WEF_PROJECT_NAME;
    private final String _LOGGING_BASE_DIR_NAME;
    private final String _LOGGING_TEST_REPORTING_DIR_NAME;
    private final String _htmlReportingDirName = "htmlreport";
    private final String _htmlReportFileName = "TestReporting.html";

    private final TestEngine _testEngine = Factories.TestEngineFactory.getInstance();
    private final Map<String, Object> _scoresMap = this._testEngine.getTestScores();

    private static class DirCleaner extends SimpleFileVisitor<Path> {

        String dirName;

        final List<String> fileList = new ArrayList<String>();

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            this.dirName = dir.toAbsolutePath().toString();
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            (new File(file.toAbsolutePath().toString())).delete();
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            (new File(dir.toAbsolutePath().toString())).delete();
            return FileVisitResult.CONTINUE;
        }
    }

    @Parameters({CommonTestFixtures.WEB_INF_PATH_NAME_FIXTURE, CommonTestFixtures.DOCUMENT_ROOT_LINUX
            , CommonTestFixtures.DOCUMENT_ROOT_WINOWS, CommonTestFixtures.PROJECT_NAME
            ,CommonTestFixtures.LOGGING_BASE_DIR_NAME, CommonTestFixtures.LOGGING_TEST_REPORTING_DIR_NAME})
    public HTMLReporterTest(String WEBINFPath, String docRootLinux, String docRootWin, String wefProjectName,
                            String logBaseDirName, String logReportingDirName){
        this._WEBINFPath = WEBINFPath;
        this._DOC_ROOT_LINUX=  docRootLinux;
        this._DOC_ROOT_WIN = docRootWin;
        this._WEF_PROJECT_NAME = wefProjectName;
        this._LOGGING_BASE_DIR_NAME = logBaseDirName;
        this._LOGGING_TEST_REPORTING_DIR_NAME = logReportingDirName;
    }

    public void initTest(){
        String wefunitDir = this._commonUtils.createPath(new String[]{this._LOGGING_BASE_DIR_NAME});
        final ConfigReader configReader = Factories.ConfigReaderFactory.getInstance();
        File file = new File(configReader.getBaseDirPathForLogging() + wefunitDir );
//        Deleting out the logging directory
        if(file.isDirectory()){
            try {
                Files.walkFileTree(Paths.get(file.getAbsolutePath().toString()), new DirCleaner());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void method_generateHTMLTestReporting_must_throw_IllegalStateException_when_reporting_dir_is_not_accessible(@Mocked final File file){
        CommonTestFixtures.docRootPathExpectations(this._DOC_ROOT_LINUX);
        this.initTest();
        final ConfigReader configReader = Factories.ConfigReaderFactory.getInstance();
        final HTMLReporter htmlReporter = Factories.HTMLReporterFactory.getInstance();

        new Expectations(){{
            file.canWrite(); result = false;
        }};

        try{
            htmlReporter.generateHTMLTestReporting();
        }catch (IllegalStateException e){
            Assert.assertEquals(e.getMessage(), configReader.getBaseDirPathForLogging() + " is not writable");
        }
    }

    @Test
    public void method_generateHTMLTestReporting_if_reporting_directories_are_not_present_then_create_them(){
        CommonTestFixtures.docRootPathExpectations(this._DOC_ROOT_LINUX);
//        CommonTestFixtures.webInfPathExpectations(this._WEBINFPath);
        this.initTest();
        final HTMLReporter htmlReporter = Factories.HTMLReporterFactory.getInstance();
        final ConfigReader configReader = Factories.ConfigReaderFactory.getInstance();
        String[] path = new String[]{this._LOGGING_BASE_DIR_NAME,
                                    this._WEF_PROJECT_NAME,
                                    this._LOGGING_TEST_REPORTING_DIR_NAME,
                                    this._htmlReportingDirName};
        File directory = new File(configReader.getBaseDirPathForLogging() + this._commonUtils.createPath(path));
        Assert.assertTrue(!directory.isDirectory());// Assert that no directory is present
        htmlReporter.generateHTMLTestReporting();
        Assert.assertTrue(directory.isDirectory());// Assert that directory is present now
    }

    @Test
    public void method_generateHTMLTestReporting_create_a_new_html_report_if_a_report_file_is_not_there(){
        CommonTestFixtures.docRootPathExpectations(this._DOC_ROOT_LINUX);
        this.initTest();
        final HTMLReporter htmlReporter = Factories.HTMLReporterFactory.getInstance();
        final ConfigReader configReader = Factories.ConfigReaderFactory.getInstance();
        String[] path = new String[]{this._LOGGING_BASE_DIR_NAME,
                                     this._WEF_PROJECT_NAME,
                                     this._LOGGING_TEST_REPORTING_DIR_NAME,
                                     this._htmlReportingDirName,
                                     this._htmlReportFileName};
        File file = new File(configReader.getBaseDirPathForLogging() + this._commonUtils.createPath(path));
        Assert.assertTrue(!file.isFile());// Assert that no file exist
        htmlReporter.generateHTMLTestReporting();
        Assert.assertTrue(file.isFile());// Assert that file is created now
    }

    @Test
    public void method_generateHTMLTestReporting_delete_existing_html_report_file_then_regenerate_a_new_file(){
        CommonTestFixtures.docRootPathExpectations(this._DOC_ROOT_LINUX);
        this.initTest();
        final HTMLReporter htmlReporter = Factories.HTMLReporterFactory.getInstance();
        final ConfigReader configReader = Factories.ConfigReaderFactory.getInstance();
        String[] path = new String[]{this._LOGGING_BASE_DIR_NAME,
                                     this._WEF_PROJECT_NAME,
                                     this._LOGGING_TEST_REPORTING_DIR_NAME,
                                     this._htmlReportingDirName,
                                     this._htmlReportFileName};
        File file = new File(configReader.getBaseDirPathForLogging() + this._commonUtils.createPath(path));
        htmlReporter.generateHTMLTestReporting();
        Assert.assertTrue(file.isFile());// Assert that file exists
        htmlReporter.generateHTMLTestReporting();
        Assert.assertTrue(file.isFile());// Assert that file still exists
    }

    @Test
    public void method_generateHTMLTestReporting_TestReporting_html_file_basic_structure(){
        CommonTestFixtures.docRootPathExpectations(this._DOC_ROOT_LINUX);
        this.initTest();
        final HTMLReporter htmlReporter = Factories.HTMLReporterFactory.getInstance();
        final ConfigReader configReader = Factories.ConfigReaderFactory.getInstance();
        String[] path = new String[]{this._LOGGING_BASE_DIR_NAME,
                                     this._WEF_PROJECT_NAME,
                                     this._LOGGING_TEST_REPORTING_DIR_NAME,
                                     this._htmlReportingDirName,
                                     this._htmlReportFileName};
        String base = null;
        switch (this._commonUtils.getOSPlatform()){
            case 1:
                base = "file://";
                break;
            case 2:
                base = "";
                break;
        }
        htmlReporter.generateHTMLTestReporting();
        File file = new File(base + configReader.getBaseDirPathForLogging() + this._commonUtils.createPath(path));
        WebDriver webDriver = new FirefoxDriver();
        webDriver.get(file.toString());
        Assert.assertEquals(webDriver.getTitle(), "WefUnit-" + this._WEF_PROJECT_NAME);//Verify title
        WebElement header = webDriver.findElement(By.cssSelector("header[id=\"report_header\"]"));
        Assert.assertEquals(header.findElement(By.tagName("h1")).getText(), this._WEF_PROJECT_NAME);//Verifying the heading

        webDriver.quit();
    }

    @Test
    public void method_generateHTMLTestReporting_TestReporting_html_file_test_report_content(){
        CommonTestFixtures.docRootPathExpectations(this._DOC_ROOT_LINUX);

        super.runTests(this._WEBINFPath);//Running the tests

        final int totalExecutedTests = (Integer) ((Map<String, Object>)this._scoresMap.get("score")).get("totalExecutedTests");
        final int totalTestFailures = (Integer) ((Map<String, Object>)this._scoresMap.get("score")).get("totalTestFailures");

        this.initTest();
        final HTMLReporter htmlReporter = Factories.HTMLReporterFactory.getInstance();
        final ConfigReader configReader = Factories.ConfigReaderFactory.getInstance();
        String[] path = new String[]{this._LOGGING_BASE_DIR_NAME,
                                     this._WEF_PROJECT_NAME,
                                     this._LOGGING_TEST_REPORTING_DIR_NAME,
                                     this._htmlReportingDirName,
                                     this._htmlReportFileName};
        String base = null;
        switch (this._commonUtils.getOSPlatform()){
            case 1:
                base = "file://";
                break;
            case 2:
                base = "";
                break;
        }
        htmlReporter.generateHTMLTestReporting();//Generating html test reports
        File file = new File(base + configReader.getBaseDirPathForLogging() + this._commonUtils.createPath(path));
        WebDriver webDriver = new FirefoxDriver();
        webDriver.get(file.toString());

//        Categorizing test classes as passed & failed classes. From scoring.
        Map<String, Object> report = (Map<String, Object>) this._scoresMap.get("report");
        List<Map<String, Object>> failedTests = (List<Map<String, Object>>) report.get("failed");//Failed tests list
        List<Map<String, Object>> passedTests = (List<Map<String, Object>>) report.get("passed");//Passed tests list
        Map<String, List<Map<String, Object>>> passedClassItems = new LinkedHashMap<String, List<Map<String, Object>>>();
        Map<String, List<Map<String, Object>>> failedClassItems = new LinkedHashMap<String, List<Map<String, Object>>>();

        String _tmp;

//        Categorizing test classes as passed
        for(Map<String , Object> item : passedTests ){
            _tmp = (String) item.get("class_name");
            if(passedClassItems.get(_tmp) == null)
                passedClassItems.put(_tmp, new ArrayList<Map<String, Object>>());

            passedClassItems.get(_tmp).add(item);
        }
//        Categorizing test classes as failed
        for(Map<String , Object> item : failedTests ){
            _tmp = (String) item.get("class_name");
            if(failedClassItems.get(_tmp) == null)
                failedClassItems.put(_tmp, new ArrayList<Map<String, Object>>());

            failedClassItems.get(_tmp).add(item);
        }

        List<WebElement> _testsLst;
        WebElement _test;
        boolean flag = false;

//        Verifying the fieldsets containing passed classes names
        for(String item : passedClassItems.keySet()){

//            Exactly one field set per passed test class
            Assert.assertEquals(webDriver.findElements(
                    By.cssSelector("fieldset[id=\"" + item + "_" + "passed" + "\"]")).size(), 1);

//            Getting the "li" list
            _testsLst = webDriver.findElements(
                    By.cssSelector("fieldset[id=\"" + item + "_" + "passed" + "\"]"))
                        .get(0)
                            .findElement(By.tagName("ul")).findElements(By.tagName("li"));

//            Verifying the tests count
            Assert.assertEquals(passedClassItems.get(item).size(), _testsLst.size());

            for(Map<String, Object> test : passedClassItems.get(item)){
                flag = false;
                for(WebElement li : _testsLst){
//                    Getting span
                    _test = li.findElement(By.cssSelector("span[class=\"test_name\"]"));

//                    If test found
                    if( ((String)test.get("test_name")).equals(_test.getText()) ) {
                        flag = true;
                        break;
                    }
                }
//                Assert that test is found
                Assert.assertTrue(flag);
            }
        }

//         Verifying the fieldsets containing failed classes names
        for(String item : failedClassItems.keySet()){
//            Exactly one field set per passed test class
            Assert.assertEquals(webDriver.findElements(
                    By.cssSelector("fieldset[id=\"" + item + "_" + "failed" + "\"]")).size(), 1);

//            Getting the "li" list
            _testsLst = webDriver.findElements(
                    By.cssSelector("fieldset[id=\"" + item + "_" + "failed" + "\"]"))
                        .get(0)
                            .findElement(By.tagName("ul")).findElements(By.tagName("li"));

//            Verifying the tests count
            Assert.assertEquals(failedClassItems.get(item).size(), _testsLst.size());

            for(Map<String, Object> test : failedClassItems.get(item)){
                flag = false;
                for(WebElement li : _testsLst){
//                    Getting span
                    _test = li.findElement(By.cssSelector("span[class=\"test_name\"]"));

//                    If test found
                    if( ((String)test.get("test_name")).equals(_test.getText()) ){
                        flag = true;
//                        Then match the stack trace
                        Assert.assertEquals(
                                li.findElement(By.cssSelector("span[class=\"stack_trace\"]")).getText(),
                                (String) test.get("stack_trace")
                        );
                        break;
                    }
                }
//                Assert that test is found
                Assert.assertTrue(flag);
            }
        }

        List<WebElement> failedTestsList = webDriver.findElements(By.cssSelector("li[name=\"failed_test_item\"]"));
        List<WebElement> passedTestsList = webDriver.findElements(By.cssSelector("li[name=\"passed_test_item\"]"));

        Assert.assertEquals(passedTestsList.size() + failedTestsList.size(), totalExecutedTests);//Total tests count
        Assert.assertEquals(failedTestsList.size(), totalTestFailures);//Failed Tests Count
        Assert.assertEquals(passedTestsList.size(), totalExecutedTests - totalTestFailures);//Passed Tests Count

        webDriver.quit();
    }

}