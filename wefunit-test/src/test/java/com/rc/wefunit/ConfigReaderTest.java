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
    private final String _DOCUMENT_ROOT_LINUX;
    private final String _DOCUMENT_ROOT_WIN;
    private final String _WEF_PROJECT_NAME;

    @Mocked
    private SystemProperties _systemProperties;

    @Parameters({CommonTestFixtures.WEB_INF_PATH_NAME_FIXTURE, CommonTestFixtures.CONFIG_FILE_NAME,
            CommonTestFixtures.DOCUMENT_ROOT_LINUX, CommonTestFixtures.DOCUMENT_ROOT_WINOWS
            ,CommonTestFixtures.PROJECT_NAME})
    public ConfigReaderTest(String webINF, String configFileName, String docRootLinux, String docRootWin, String projectName){
        this._WEB_INF_PATH = webINF;
        this._CONFIG_FILE_NAME = configFileName;
        this._DOCUMENT_ROOT_LINUX = docRootLinux;
        this._DOCUMENT_ROOT_WIN = docRootWin;
        this._WEF_PROJECT_NAME = projectName;
    }

    @Test
    public void method_getBaseDirPathForLogging_must_return_user_home_when_no_wefunit_xml_file_is_present(){
        final ConfigReader configReader = Factories.ConfigReaderFactory.getInstance();
        Assert.assertEquals(configReader.getBaseDirPathForLogging(), System.getProperty("user.home"));
    }

    @Test
    public void method_getBaseDirPathForLogging_must_return_user_home_when_a_config_file_is_present_but_no_logging_dir_element_is_there(){
        final ConfigReader configReader = Factories.ConfigReaderFactory.getInstance();
        Assert.assertEquals(configReader.getBaseDirPathForLogging(), System.getProperty("user.home"));
    }

    @Test
    public void method_getProjectName_tes_for_windows(){
        new Expectations(){{
            SystemProperties.getDocumentRoot(); result = _DOCUMENT_ROOT_WIN;
        }};
        final ConfigReader configReader = Factories.ConfigReaderFactory.getInstance();
        Assert.assertEquals(configReader.getProjectName(), this._WEF_PROJECT_NAME);
    }

    @Test
    public void method_getProjectName_tes_for_linux(){
        new Expectations(){{
            SystemProperties.getDocumentRoot(); result = _DOCUMENT_ROOT_LINUX;
        }};
        final ConfigReader configReader = Factories.ConfigReaderFactory.getInstance();
        Assert.assertEquals(configReader.getProjectName(), this._WEF_PROJECT_NAME);
    }
}