package com.rc.wefunit.testengine;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rc.wefunit.CommonUtils;
import com.rc.wefunit.Factories;
import com.rc.wefunit.GenericServiceOperationTest;

import javax.json.Json;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Created by Affan Hasan on 4/24/15.
 */
public class DefaultTestEngine implements TestEngine {

    private final Map<String, Object> _testScores = new LinkedHashMap<String, Object>();
    private final CommonUtils _commonUtils = Factories.CommonUtilsFactory.getInstance();

    public DefaultTestEngine(){
        this._resetTestScoring();
    }

    @Override
    public void executeTests(Queue<Object> objectsQueue) {
        this._resetTestScoring();//Resetting the test scoring
        Object testObj;
        while (objectsQueue.peek() != null){
            testObj = objectsQueue.poll();//Fetching test class from the Queue
//            Getting test methods list
            Method[] testMethods = this._commonUtils.getTestMethodsArrayFromTestClass(testObj.getClass());
//            Executing test methods
            for( Method m : testMethods ){
                try {
                    if(Modifier.isPrivate(m.getModifiers()))//If it is a private test method
                        m.setAccessible(true);//Make it accessible
//                    Incrementing the executed test count
                    this._incrementTotalExecutedTests();
                    m.invoke(testObj);//Execute the test
//                    Incrementing the passed tests count
                    this._incrementPassedTests(m);
                } catch (Throwable e) {//If here it means that the test failed
//                    e.printStackTrace();
                    this._incrementTotalTestFailures(e);
                }
            }
        }
    }

    private void _resetTestScoring(){
        Map<String, Object> score = new LinkedHashMap<String, Object>();
        score.put("totalExecutedTests", 0);
        score.put("totalTestFailures", 0);
//        Reporting
        Map<String, Object> report = new LinkedHashMap<String, Object>();
        report.put("report", new LinkedHashMap<String, Object>());
        List<Map<String, Object>> failedList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> passedList = new ArrayList<Map<String, Object>>();
        report.put("failed", failedList);
        report.put("passed", passedList);

        this._testScores.put("score", score);
        this._testScores.put("report", report);
    }

    private void _incrementTotalExecutedTests(){
        Map<String, Object> score = (Map<String, Object> ) this._testScores.get("score");
        int totalExecutedTests = (Integer) score.get("totalExecutedTests");
        score.put("totalExecutedTests", ++totalExecutedTests);
    }

    private void _incrementTotalTestFailures(Throwable e){
//        Setting score
        Map<String, Object>  score = (Map<String, Object> ) this._testScores.get("score");
        int totalTestFailures = (Integer) score.get("totalTestFailures");
        score.put("totalTestFailures", ++totalTestFailures);
//        Reporting : Incrementing Total Test Failures
        List<Map<String, Object>> failedArr = (List<Map<String, Object>>) ( ( (Map<String, Object>) this._testScores.get("report") ).get("failed") );
        Map<String, Object> testItem = new LinkedHashMap<String, Object>();
        testItem.put("class_name", e.getStackTrace()[0].getClassName());
        testItem.put("test_name", e.getStackTrace()[0].getMethodName());
        List<StackTraceElement> stackTrace = Arrays.asList(e.getStackTrace());
        testItem.put("stack_trace", stackTrace);
        failedArr.add(testItem);
    }

    private void _incrementPassedTests(Method m){
        List<Map<String, Object>> passedTestsList = ( List<Map<String, Object>> ) ( (Map<String, Object>) this._testScores.get("report")).get("passed");
        Map<String, Object> passedTest = new LinkedHashMap<String, Object>();
        passedTest.put("class_name", m.getDeclaringClass().getName());
        passedTest.put("test_name", m.getName());
        passedTestsList.add(passedTest);
    }

    @Override
    public Map<String, Object> getTestScores() {
        return _testScores;
    }
}