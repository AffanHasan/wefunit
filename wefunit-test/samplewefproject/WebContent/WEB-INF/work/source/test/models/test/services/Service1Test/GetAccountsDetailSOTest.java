package test.models.test.services.Service1Test;

import com.rc.wefunit.GenericServiceOperationTest;
import com.rc.wefunit.annotations.Test;
import com.rc.wefunit.probes.Assert;

import java.lang.reflect.Method;

/**
 * Created by Affan Hasan on 3/26/15.
 */
public class GetAccountsDetailSOTest extends GenericServiceOperationTest {

    @Override
    public void parameter_count_test() {

    }

    @Test
    public void failedTest1(){
        Assert.fail();
    }

    @Test
    public void failedTest2(){
        Assert.fail("Explicit failure");
    }

    @Test
    public void failedTest3(){
        Assert.fail("Explicit failure");
    }

    @Test
    public void failedTest4(){
        Assert.fail();
    }

    @Test
    public void failedTest5(){
        Assert.fail("Explicit failure");
    }

    @Test
    public void failedTest6(){
        Assert.fail("Explicit failure");
    }

}