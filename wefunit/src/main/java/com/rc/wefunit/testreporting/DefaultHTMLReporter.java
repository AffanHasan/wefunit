package com.rc.wefunit.testreporting;

import com.bowstreet.methods.CollectionUtil;
import com.rc.wefunit.CommonUtils;
import com.rc.wefunit.ConfigReader;
import com.rc.wefunit.Factories;
import com.rc.wefunit.testengine.TestEngine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by Affan Hasan on 5/14/15.
 */
public class DefaultHTMLReporter implements HTMLReporter {

    private final ConfigReader _configReader = Factories.ConfigReaderFactory.getInstance();
    private final CommonUtils _commonUtils = Factories.CommonUtilsFactory.getInstance();
    private final TestEngine _testEngine = Factories.TestEngineFactory.getInstance();
    private final String[] _path = new String[5];

    public DefaultHTMLReporter(){
        _path[0] = "wefunitlogs";
        _path[1] = this._configReader.getProjectName();
        _path[2] = "testreports";
        _path[3] = "htmlreport";
        _path[4] = "TestReporting.html";//Reporting file
    }

    @Override
    public void generateHTMLTestReporting() {
//        Check if base directory is writable
        File baseDir = new File(this._configReader.getBaseDirPathForLogging());
        if(!baseDir.canWrite())
            throw new IllegalStateException(this._configReader.getBaseDirPathForLogging() + " is not writable");

//        HTML reporting directory _path
        File reportingDirPath = this.getHtmlReportingDir();
//        HTML test reporting file
        File htmlFile = this.getReportingFile();

//        Check that if directory do not exists
        if(!reportingDirPath.isDirectory()){
//            Create it
            reportingDirPath.mkdirs();
        }
//                Delete "TestReporting.html" file (if present)
        if(htmlFile.isFile()){//If present
//            Delete it
            htmlFile.delete();
//            Then create a new one
            this.createNewReportingFile();
        }else {//If not present, then create a new file
            this.createNewReportingFile();
        }

//        Writing the test reports to html file
        try {
            TestReportingGenerator testReportingGenerator = new TestReportingGenerator();
            BufferedWriter bw = new BufferedWriter(new FileWriter(htmlFile));
            for(char character : testReportingGenerator.testReportingFileContent().toCharArray()){
                bw.write(character);
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class TestReportingGenerator{

        private final String _svgImgPassed =  "<svg name=\"passed_icon\" width=\"25\" height=\"25\">" +
                                "<circle cx=\"13\" cy=\"13\" r=\"7\" stroke=\"black\" stroke-width=\"1\" fill=\"green\" />" +
                            "</svg>";
        private final String _svgImgFailed =  "<svg name=\"failed_icon\" width=\"25\" height=\"25\">" +
                                "<circle cx=\"13\" cy=\"13\" r=\"7\" stroke=\"black\" stroke-width=\"1\" fill=\"red\" />" +
                            "</svg>";

        private String getFieldSet(boolean passed, String className, List<Map<String, Object>> testsList){

            StringBuilder fieldSet = new StringBuilder();

            fieldSet.append(new String("<fieldset id=\""+ className + "_" + ( passed ? "passed" : "failed" ) +"\" class=\"" + ( passed ? "passed_test_item" : "failed_test_item" ) +"\">"));
    //            Appending Legend
                fieldSet.append(new String("<legend>"));
    //            Adding Image
                    fieldSet.append (new String(passed ? _svgImgPassed : _svgImgFailed ));//SVG Image
//                Class Name
                    fieldSet.append((String) className); //Test class name
                fieldSet.append(new String("</legend>"));
//                Adding UL
                fieldSet.append(new String("<ul>"));
//                    Adding LI
                for(Map<String, Object> item : testsList){
                    fieldSet.append(new String("<li name=\""+ ( passed ? "passed_test_item" : "failed_test_item" ) +"\">"));
                        fieldSet.append(new String((String) item.get("test_name")));
                    fieldSet.append(new String("</li>"));
                }
                fieldSet.append(new String("</ul>"));
    //            Legend End
            fieldSet.append(new String("</fieldset>"));

            return fieldSet.toString();
        }

        public String testReportingFileContent(){
//            Getting failed test Array
            List<Map<String, Object>> failedTestArr = ( (List<Map<String, Object>>) ((Map<String, Object>)_testEngine.getTestScores().get("report")).get("failed") );
//            Getting passed test Array
            List<Map<String, Object>> passedTestArr = ( (List<Map<String, Object>>) ((Map<String, Object>)_testEngine.getTestScores().get("report")).get("passed") );

            Map<String, List<Map<String, Object>>> passedClassesMap = new LinkedHashMap<String, List<Map<String, Object>>>();
            Map<String, List<Map<String, Object>>> failedClassesMap = new LinkedHashMap<String, List<Map<String, Object>>>();

//        Categorizing test classes as passed
            for(Map<String , Object> item : passedTestArr ){
                if(!passedClassesMap.containsKey((String)item.get("class_name")))
                    passedClassesMap.put((String)item.get("class_name"), new ArrayList<Map<String, Object>>());
                ( (List<Map<String, Object>>) passedClassesMap.get((String)item.get("class_name")) )//List
                        .add(item);
            }
//        Categorizing test classes as failed
            for(Map<String , Object> item : failedTestArr ){
                if(!failedClassesMap.containsKey((String)item.get("class_name")))
                    failedClassesMap.put((String)item.get("class_name"), new ArrayList<Map<String, Object>>());
                ( (List<Map<String, Object>>) failedClassesMap.get((String)item.get("class_name")) )//List
                        .add(item);
            }

//            Creating field sets for failed tests
            StringBuilder failedFieldSets = new StringBuilder();
            for( String failedItem : failedClassesMap.keySet()){
                failedFieldSets.append(this.getFieldSet(false, failedItem, failedClassesMap.get(failedItem) ));//Append the field set
            }
//            Creating field sets for passed tests
            StringBuilder passedFieldSets = new StringBuilder();
            for( String passedItem : passedClassesMap.keySet()){
                passedFieldSets.append(this.getFieldSet(true, passedItem, passedClassesMap.get(passedItem)));//Append the field set
            }
            String report = new String(
                    "<!DOCTYPE html>" +
                    "<html>"
                            +"<head>"
                                +"<title>WefUnit-"+ _configReader.getProjectName() +"</title>" +
                            "</head>"
                            +"<body>"
                                +failedFieldSets.toString()//Failed Field Sets
                                +passedFieldSets.toString()//Passed Field Sets
                            +"</body>"
                   +"</html>"
            );
            return report;
        }

    }

    private void createNewReportingFile(){
        try {
            this.getReportingFile().createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getReportingFile(){
        return new File( this._configReader.getBaseDirPathForLogging() + this._commonUtils.createPath(Arrays.copyOfRange(_path, 0, 5)) );
    }

    private File getHtmlReportingDir(){
        return new File( this._configReader.getBaseDirPathForLogging() + this._commonUtils.createPath(Arrays.copyOfRange(_path, 0, 4)) );
    }
}