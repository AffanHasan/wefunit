package com.rc.wefunit.testengine;

import com.rc.wefunit.CommonTestFixtures;
import com.rc.wefunit.Factories;
import com.rc.wefunit.TestRunnerBaseClass;
import com.rc.wefunit.probes.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Affan Hasan on 5/13/15.
 */
public class TestEngineTest extends TestRunnerBaseClass {

    private final TestEngine _testEngine = Factories.TestEngineFactory.getInstance();
    private final Map<String, Object> _scoresMap = this._testEngine.getTestScores();

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
}