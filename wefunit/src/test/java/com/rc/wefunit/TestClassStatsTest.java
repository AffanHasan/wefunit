package com.rc.wefunit;

import com.bowstreet.util.SystemProperties;
import com.rc.wefunit.annotations.BeforeClass;
import mockit.Expectations;
import mockit.Mocked;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Affan Hasan on 4/27/15.
 */
public class TestClassStatsTest {

    private final Class getClassObject(){
        try {
            return Class.forName("com.rc.wefunit.TestClassStats");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void method_getTotalExecutableTestsCount(){
        try {
            Method m = getClassObject().getDeclaredMethod("getTotalExecutableTestsCount");
            Assert.assertNotNull(m);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }
}