package com.rc.wefunit;

import com.bowstreet.util.SystemProperties;
import mockit.Expectations;
import mockit.Mocked;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;

/**
 * Created by Affan Hasan on 5/18/15.
 */
public class ConfigReaderTest {

    private final CommonUtils _commonUtils = Factories.CommonUtilsFactory.getInstance();
    private final String _WEB_INF_PATH;
    private final String _CONFIG_FILE_NAME;
    @Mocked
    private SystemProperties _systemProperties;

    @Parameters({CommonTestFixtures.WEB_INF_PATH_NAME_FIXTURE, CommonTestFixtures.CONFIG_FILE_NAME})
    public ConfigReaderTest(String webINF, String configFileName){
        this._WEB_INF_PATH = webINF;
        this._CONFIG_FILE_NAME = configFileName;
    }

    @Test
    public void method_getBaseDirPathForLogging_must_return_user_home_when_no_wefunit_xml_file_is_present(){
//        CommonTestFixtures.cmnExpectations(this._WEB_INF_PATH);
        final ConfigReader configReader = Factories.ConfigReaderFactory.getInstance();
        Assert.assertEquals(configReader.getBaseDirPathForLogging(), System.getProperty("user.home"));
    }

    @Test
    public void method_getBaseDirPathForLogging_must_return_user_home_when_a_config_file_is_present_but_no_logging_dir_element_is_there(){
        final ConfigReader configReader = Factories.ConfigReaderFactory.getInstance();
        Assert.assertEquals(configReader.getBaseDirPathForLogging(), System.getProperty("user.home"));
    }
}