package com.rc.wefunit;

import com.bowstreet.util.SystemProperties;
import mockit.Expectations;
import mockit.Mocked;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;

/**
 * Created by Affan Hasan on 5/14/15.
 */
public class CommonUtilsTest {

    private final CommonUtils _commonUtils = Factories.CommonUtilsFactory.getInstance();
    private final String _webINFPATH;
    private final String _CONFIG_FILE_NAME;

    @Parameters({CommonTestFixtures.WEB_INF_PATH_NAME_FIXTURE, CommonTestFixtures.CONFIG_FILE_NAME})
    public CommonUtilsTest(String webINFPath, String configFileName){
        this._webINFPATH = webINFPath;
        this._CONFIG_FILE_NAME = configFileName;
    }
}