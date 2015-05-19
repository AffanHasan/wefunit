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

    @Override
    public void generateHTMLTestReporting() {
//        Check if base directory is writable
        File baseDir = new File(this._configReader.getBaseDirPathForLogging());
        if(!baseDir.canWrite())
            throw new IllegalStateException(this._configReader.getBaseDirPathForLogging() + " is not writable");

        String[] path = new String[5];
        path[0] = "wefunitlogs";
        path[1] = this._configReader.getProjectName();
        path[2] = "testreports";
        path[3] = "htmlreport";
        path[4] = "TestReporting.html";//Reporting file

//        HTML reporting directory path
        File reportingDirPath = new File( this._configReader.getBaseDirPathForLogging() +
                this._commonUtils.createPath(Arrays.copyOfRange(path, 0, 4)) );
//        HTML test reporting file
        File htmlFile = new File( this._configReader.getBaseDirPathForLogging() +
                this._commonUtils.createPath(Arrays.copyOfRange(path, 0, 5)) );

//        Check that if directory do not exists
        if(!reportingDirPath.isDirectory()){
//            Create it
            reportingDirPath.mkdirs();
        }
//                Delete "TestReporting.html" file (if present)
        if(htmlFile.isFile()){//If present
//            Delete it
            htmlFile.delete();
        }else {//If not present
            try {
                htmlFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}