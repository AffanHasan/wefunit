package com.rc.wefunit.testengine;

import com.bowstreet.util.SystemProperties;
import com.bowstreet.webapp.WebAppAccess;
import com.rc.wefunit.*;
import com.rc.wefunit.probes.Assert;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Affan Hasan on 5/13/15.
 */
public class TestEngineTest extends TestRunnerBaseClass {

    private final String _webInfDirPath;

    private final Runner _runner = Factories.RunnerFactory.getInstance();
    private TestClassStats _testClassStats;
    private CommonUtils _commonUtils = Factories.CommonUtilsFactory.getInstance();

    @Parameters({CommonTestFixtures.WEB_INF_PATH_NAME_FIXTURE})
    public TestEngineTest(String webInfDirPath){
        this._webInfDirPath = webInfDirPath;

        //        Setting the Class Loader for runner instance
        try {
            Field field = this._runner.getClass().getDeclaredField("_classLoader");
            field.setAccessible(true);
            field.set(this._runner, this.getClass().getClassLoader());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Mocked Factories.RunnerFactory runnerFactory;

    @Injectable
    WebAppAccess webAppAccess;

    @Injectable Runner runner;

    private final TestEngine _testEngine = Factories.TestEngineFactory.getInstance();
    private final Map<String, Object> _scoresMap = this._testEngine.getTestScores();

    private final void expectations(){
        new Expectations(){
            {
                SystemProperties.getWebInfDir(); result = _webInfDirPath;
                runnerFactory.getInstance();result = runner;
                runner.getWebAppAccess();result = webAppAccess;
                runner.getWebAppAccess().getModelInstance("test/SCBuildersFixture", null, true); result = webAppAccess;
            }
        };
    }

    private final void setTestClassStats(){
        try {
            this._testClassStats = Factories.TestClassStatsFactory.getInstance(this._runner.getTestClassesSet());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            org.testng.Assert.fail(e.getMessage());
        }
    }

    private Class getClassObject(){
        try {
            return Class.forName("com.rc.wefunit.testengine.TestEngine");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            org.testng.Assert.fail(e.getMessage());
            return null;
        }
    }

    @Test
    public void method_getTestScores_returned_map_must_contain_score_document(){
        /*
        * {
        *   "score" : {
        *                   "totalExecutedTests" : 0,
        *                   "totalTestFailures" : 0
        *               }
        *   ...
        * }
        * */
        Assert.assertTrue(((Map<String, Object>) _scoresMap.get("score")) instanceof Map);//Document "score"
        Assert.assertTrue(((Map<String, Object>) _scoresMap.get("score")).get("totalExecutedTests") instanceof Integer);//Executed test count
        Assert.assertTrue(((Map<String, Object>) _scoresMap.get("score")).get("totalTestFailures") instanceof Integer);//Failed Test Count
    }

    @Test
    public void method_getTestScores_returned_map_must_contain_report_document(){
        /*
        * {
        *   "report" : {
        *                   "passed" : [ { "class_name" : "...", "test_name" : "..." } ],
        *                   "failed" : [ { "class_name" : "...", "test_name" : "...", "stack_trace" : [ StackTrace, ... ] } ]
        *               }
        *   ...
        * }
        * */
        final Map<String, Object> reportMap = (Map<String, Object>) _scoresMap.get("report");
        final List<Map<String, Object>> failedArr = (List<Map<String, Object>>) reportMap.get("failed");
        final List<Map<String, Object>> passedArr = (List<Map<String, Object>>) reportMap.get("passed");
    }

    @Test
    @Parameters({CommonTestFixtures.WEB_INF_PATH_NAME_FIXTURE})
    public void method_getTestScores_returned_map_validating_failed_test_objects_structures_inside_report_failed_array(String _WEBINFPath){

        super.runTests(_WEBINFPath);//Running tests

        final Map<String, Object> reportMap = (Map<String, Object>) _scoresMap.get("report");
        final List<Map<String, Object>> failedArr = (List<Map<String, Object>>) reportMap.get("failed");



        Assert.assertTrue(failedArr.size() > 0);//There must be some failed tests

        final Pattern stackTracePattern = Pattern.compile(".+\n\t at .+");
        Matcher matcher;

//        Checking Failed Tests Structure
        for(Map<String, Object> obj : failedArr){
            Assert.assertTrue(obj.get("class_name") instanceof String);
            Assert.assertTrue(obj.get("test_name") instanceof String);
            matcher = stackTracePattern.matcher((String)obj.get("stack_trace"));
            Assert.assertTrue(matcher.matches());//Matching string representation for StackTrace
        }
    }

    @Test
    public void method_executeTests_total_executed_tests_should_equal_to_total_no_of_tests_present(){
        this.expectations();
        this.setTestClassStats();
        Queue<Object> queue = this._runner.getExecutableTestObjectsQueue();
        this._testEngine.executeTests(queue);//Execute the tests
        int totalExecutableTests = this._testClassStats.getTotalExecutableTestsCount();
        Map<String, Object> testScores = this._testEngine.getTestScores();
        int actualExecutedTestsCount = (Integer) ((Map<String, Object>)testScores.get("score")).get("totalExecutedTests");
        org.testng.Assert.assertEquals(actualExecutedTestsCount, totalExecutableTests);
    }

    @Test
    public void method_executeTests_total_test_for_explicitly_failed_tests(){
        this.expectations();
        this.setTestClassStats();
        Queue<Object> queue = this._runner.getExecutableTestObjectsQueue();
        this._testEngine.executeTests(queue);//Execute the tests
        Map<String, Object> testScores = this._testEngine.getTestScores();
        int totalFailedTestPresent = 4;//All are defined in GetAccountsDetailSOTest.java in "samplewefproject"
        int actualTestFailuresCount = (Integer) ((Map<String, Object>) testScores.get("score")).get("totalTestFailures");
        org.testng.Assert.assertEquals(actualTestFailuresCount, totalFailedTestPresent);
    }

    @Test
    public void method_executeTests_tests_reports_failed_tests_arrays_size_should_be_equal_to_total_failed_tests(){
        this.expectations();
        this.setTestClassStats();
        Queue<Object> queue = this._runner.getExecutableTestObjectsQueue();
        this._testEngine.executeTests(queue);//Execute the tests
        Map<String, Object> testScores = this._testEngine.getTestScores();
        int totalFailedTests = (Integer) ((Map<String, Object>) testScores.get("score")).get("totalTestFailures");
        List<Map<String, Object>> reportedFailedTests = (List<Map<String, Object>>) ( (Map<String, Object>) testScores.get("report") ).get("failed");
        org.testng.Assert.assertEquals(reportedFailedTests.size(),  totalFailedTests);
    }

    @Test
    public void method_executeTests_total_passed_tests_should_be_equal_to_total_tests_minus_total_failed_tests(){
        this.expectations();
        this.setTestClassStats();
        Queue<Object> queue = this._runner.getExecutableTestObjectsQueue();
        this._testEngine.executeTests(queue);//Execute the tests
        Map<String, Object> testScores = this._testEngine.getTestScores();
        final int totalFailedTests = (Integer) ((Map<String, Object>) testScores.get("score")).get("totalTestFailures");
        final int totalExecutedTests = (Integer) ((Map<String, Object>) testScores.get("score")).get("totalExecutedTests");
        List<Map<String, Object>> reportedPassedTests = (List<Map<String, Object>>) ( (Map<String, Object>) testScores.get("report") ).get("passed");
        org.testng.Assert.assertEquals(reportedPassedTests.size(), totalExecutedTests - totalFailedTests);//Total passed tests = total_executed_tests - total_failed_tests
    }

    @Test
    public void method_getTestScores_validating_the_test_classes_names_inside_report_section_of_test_scoring_map(){
        this.expectations();
        this.setTestClassStats();

        Queue<Object> queue = this._runner.getExecutableTestObjectsQueue();
        this._testEngine.executeTests(queue);//Execute the tests

        Map<String, List<String>> classesSet = new LinkedHashMap<String, List<String>>();//Test class name to test methods mapping
        try {
            Set<Class> testClasses =  this._runner.getTestClassesSet();
            for( Class testClassItem : testClasses ){
                List<String> methodNames = new ArrayList<String>();
                for(Method m : this._commonUtils.getTestMethodsArrayFromTestClass(testClassItem)){
                    methodNames.add(m.getName());
                }
                classesSet.put(testClassItem.getName(), methodNames);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        List<Map<String, Object>> failedTestsList = (List<Map<String, Object>>) ( ( (Map<String, Object>) this._testEngine.getTestScores().get("report") ).get("failed") );
        List<Map<String, Object>> passedTestsList = ( List<Map<String, Object>> ) ( (Map<String, Object>) this._testEngine.getTestScores().get("report")).get("passed");

        for(String className :  classesSet.keySet() ){
            int estTestCount = classesSet.get(className).size();
            int testCount = 0;

            for(Map<String, Object> item : failedTestsList){//Failed Tests
                if(((List<String>) classesSet.get(className)) .contains((String )item.get("test_name"))
                        && ((String )item.get("class_name")).equals(className)) {//Matching method name
                    testCount++;
                }
            }

            for(Map<String, Object> item : passedTestsList) {//Passed Tests
                if(((List<String>) classesSet.get(className)) .contains((String) item.get("test_name"))
                        && ((String )item.get("class_name")).equals(className)) {//Matching method name
                    testCount++;
                }
            }
            org.testng.Assert.assertEquals(testCount, estTestCount);
        }
    }

}