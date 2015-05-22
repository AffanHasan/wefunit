package com.rc.wefunit;

import com.bowstreet.util.SystemProperties;
import com.bowstreet.webapp.WebAppAccess;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;

/**
 * Created by Affan Hasan on 5/14/15.
 */
public class TestRunnerBaseClass {

    @Injectable
    private WebAppAccess _webAppAccess;

    @Mocked
    private SystemProperties _systemProperties;

    public void runTests(final String webINFPath){

//        new Expectations(){{
//            SystemProperties.getWebInfDir(); result = webINFPath;
//        }};

//        Running the tests
        final Runner runner = Factories.RunnerFactory.getInstance();
        runner.run(_webAppAccess, this.getClass().getClassLoader());
    }
}