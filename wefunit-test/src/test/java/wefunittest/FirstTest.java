package wefunittest;

import com.bowstreet.util.SystemProperties;
import com.bowstreet.webapp.WebAppAccess;
import com.rc.wefunit.Factories;
import com.rc.wefunit.Runner;
import com.rc.wefunit.TestClassStats;
import com.rc.wefunit.probes.Assert;
import com.rc.wefunit.testengine.TestEngine;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import org.testng.annotations.Test;

/**
 * Created by Affan Hasan on 5/12/15.
 */
public class FirstTest {

    @Test
    public void test1(@Injectable WebAppAccess webAppAccess, @Mocked SystemProperties systemProperties){

        new Expectations(){{
            SystemProperties.getWebInfDir(); result = "samplewefproject/WebContent/WEB-INF";
        }};

        Runner runner = Factories.RunnerFactory.getInstance();
        runner.run(webAppAccess, this.getClass().getClassLoader());
        TestEngine testEngine = Factories.TestEngineFactory.getInstance();
        System.out.println(testEngine.getTestScores());
    }
}