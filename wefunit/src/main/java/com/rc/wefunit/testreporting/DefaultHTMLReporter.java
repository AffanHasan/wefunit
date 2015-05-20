package com.rc.wefunit.testreporting;

import com.rc.wefunit.CommonUtils;
import com.rc.wefunit.ConfigReader;
import com.rc.wefunit.Factories;
import com.rc.wefunit.testengine.TestEngine;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

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
        }else {//If not present
            this.createNewReportingFile();
        }
    }

    private class TestReportingGenerator{

        private final String _svgImgPassed =  "<svg name=\"failed_icon\" width=\"25\" height=\"25\">" +
                                "<circle cx=\"13\" cy=\"13\" r=\"10\" stroke=\"black\" stroke-width=\"1\" fill=\"red\" />" +
                            "</svg>";
        private final String _svgImgFailed =  "<svg name=\"failed_icon\" width=\"25\" height=\"25\">" +
                                "<circle cx=\"13\" cy=\"13\" r=\"10\" stroke=\"black\" stroke-width=\"1\" fill=\"green\" />" +
                            "</svg>";

        private String getFieldSet(boolean passed, Map<String, String> testItem){
            String fieldSet = new String("<fieldset>" +
                                            "<legend>"
                                                + ( passed ? _svgImgPassed : _svgImgFailed )//SVG Image
                                                +  testItem.get("class_name") //Test class name
                                            +"</legend>"
                                            +""+
                                         "</fieldset>");
            return fieldSet;
        }

        public String testReportingFileContent(){
            for( _testEngine.getTestScores().get("") ){

            }
            String report = new String(
                    "<!DOCTYPE html>" +
                    "<html>"
                            +"<head>"
                            +"</head>"
                            +"<body>"

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