package com.rc.wefunit.testreporting;

import com.rc.wefunit.CommonUtils;
import com.rc.wefunit.ConfigReader;
import com.rc.wefunit.Factories;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Affan Hasan on 5/14/15.
 */
public class DefaultHTMLReporter implements HTMLReporter {

    private final ConfigReader _configReader = Factories.ConfigReaderFactory.getInstance();
    private final CommonUtils _commonUtils = Factories.CommonUtilsFactory.getInstance();
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