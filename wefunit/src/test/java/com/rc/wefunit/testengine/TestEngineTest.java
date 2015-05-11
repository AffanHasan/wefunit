package com.rc.wefunit.testengine;

import com.bowstreet.util.SystemProperties;
import com.bowstreet.webapp.WebAppAccess;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rc.wefunit.Factories;
import com.rc.wefunit.Runner;
import com.rc.wefunit.TestClassStats;
import com.rc.wefunit.annotations.BeforeClass;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Created by Affan Hasan on 4/24/15.
 */
public class TestEngineTest {

    private final String _webInfDirPath = "samplewefproject/WebContent/WEB-INF";

    private final TestEngine _testEngine = Factories.TestEngineFactory.getInstance();
    private final Runner _runner = Factories.RunnerFactory.getInstance();
    private TestClassStats _testClassStats;

    @Mocked SystemProperties systemProperties;
    @Mocked Factories.RunnerFactory runnerFactory;
    @Injectable WebAppAccess webAppAccess;
    @Injectable Runner runner;

    private final void expectations(){
        new Expectations(){
            {
                runnerFactory.getInstance();result = runner;
                runner.getWebAppAccess();result = webAppAccess;
                runner.getWebAppAccess().getModelInstance("test/SCBuildersFixture", null, true); result = webAppAccess;
                SystemProperties.getWebInfDir(); result = _webInfDirPath;
            }
        };
    }

    private final void setTestClassStats(){
        try {
            this._testClassStats = Factories.TestClassStatsFactory.getInstance(this._runner.getTestClassesSet());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    private Class getClassObject(){
        try {
            return Class.forName("com.rc.wefunit.testengine.TestEngine");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
            return null;
        }
    }

    @Test
    public void singleton_test(){
        Assert.assertTrue(_testEngine == Factories.TestEngineFactory.getInstance());
    }

    @Test
    public void is_exist_Interface_TestEngine(){
        Assert.assertNotNull(getClassObject());//Test Not Null
    }

    @Test
    public void method_executeTests_parameters_Queue(){
        try {
            Class testEngine = getClassObject();
            Method m = testEngine.getMethod("executeTests", Queue.class);
            Assert.assertNotNull(m);//Test Not Null
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
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
        Assert.assertEquals(actualExecutedTestsCount, totalExecutableTests);
    }

    @Test
    public void method_executeTests_total_test_for_explicitly_failed_tests(){
        this.expectations();
        this.setTestClassStats();
        Queue<Object> queue = this._runner.getExecutableTestObjectsQueue();
        this._testEngine.executeTests(queue);//Execute the tests
        Map<String, Object> testScores = this._testEngine.getTestScores();
        int totalFailedTestPresent = 14;//All are defined in GetAccountsDetailSOTest.java in "samplewefproject"
        int actualTestFailuresCount = (Integer) ((Map<String, Object>) testScores.get("score")).get("totalTestFailures");
        Assert.assertEquals(actualTestFailuresCount, totalFailedTestPresent);
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
        Assert.assertEquals(reportedFailedTests.size(), totalFailedTests);
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
        Assert.assertEquals(reportedPassedTests.size(), totalExecutedTests - totalFailedTests);//Total passed tests = total_executed_tests - total_failed_tests
    }
}