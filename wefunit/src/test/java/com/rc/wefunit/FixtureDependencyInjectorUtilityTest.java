package com.rc.wefunit;

import com.bowstreet.util.IXml;
import com.bowstreet.util.SystemProperties;
import com.bowstreet.util.SystemTrace;
import com.bowstreet.util.cache.CacheManager;
import com.bowstreet.webapp.*;
import com.bowstreet.webapp.util.URLMapper;
import com.bowstreet.webapp.util.UserInfo;
import com.rc.wefunit.producers.FactoryProducers;
import mockit.*;
import mockit.integration.junit4.JMockit;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Affan Hasan on 4/3/15.
 */
public class FixtureDependencyInjectorUtilityTest {

    private final FixtureDependencyInjectorUtility _fdiu = Factories.FixtureDependencyInjectorUtilityFactory.getInstance();
    private final CommonUtils _commonUtils = Factories.CommonUtilsFactory.getInstance();

    @Test
    public void must_throw_IllegalStateException_when_a_non_injectable_field_is_provided_to_inject_method(){
        class ATest extends GenericServiceOperationTest{
            @Override
            public void parameter_count_test() {

            }

            public String name = "Hello";//Non Injectable Field

        }

        ATest aTest = new ATest();
        try {
            Field field = aTest.getClass().getDeclaredField("name");
            _fdiu.inject(field, aTest);
            Assert.fail();
        } catch (NoSuchFieldException e) {
            Assert.fail();
        } catch (IllegalStateException e){
            Assert.assertEquals(e.getMessage(), "Provided field is not Injectable");
            return;
        }
        Assert.fail();
    }

    @Test
    public void inject_method_with_2_parameters_Field_and_Object1_GenericServiceOperationTest_serviceOperationName_field_1(){
        try {
            Class classObj = Class.forName("com.rc.wefunit.FixtureDependencyInjectorUtility");
            Method injectMethod = classObj.getMethod("inject", Field.class, Object.class);
            Assert.assertTrue(injectMethod.getParameterCount() == 2);
            Assert.assertTrue(injectMethod.getReturnType().toString().equals("void"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Assert.fail("Class \"com.rc.wefunit.FixtureDependencyInjectorUtility\" do not exists");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void inject_method_with_2_parameters_Field_and_Object2_GenericServiceOperationTest_serviceOperationName_field_2(){
        try {
            final Class getAccountsDetailSOTest =
                    Class.forName("test.models.test.services.Service1Test.GetAccountsDetailSOTest");//SubCLass Of GenericServiceOperationTest
            GenericServiceOperationTest instance = (GenericServiceOperationTest) getAccountsDetailSOTest.newInstance();
            Field soName = getAccountsDetailSOTest.getSuperclass().getDeclaredField("serviceOperationName");
            _fdiu.inject(soName, instance);
            Assert.assertEquals(instance.getServiceOperationName(), "getAccountsDetailSO");//Verifying the value
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Assert.fail("Class \"com.rc.wefunit.FixtureDependencyInjectorUtility\" do not exists");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (InstantiationException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void inject_method_with_2_parameters_Field_and_Object__GenericServiceOperationTest_dataServiceName_field_injection_Test_1(){
        try {
            final Class getAccountsDetailSOTest =
                    Class.forName("test.models.test.services.Service1Test.GetAccountsDetailSOTest");//SubCLass Of GenericServiceOperationTest
            GenericServiceOperationTest instance = (GenericServiceOperationTest) getAccountsDetailSOTest.newInstance();
            Field dataServiceName = getAccountsDetailSOTest.getSuperclass().getDeclaredField("dataServiceName");
            _fdiu.inject(dataServiceName, instance);
            Assert.assertEquals(instance.getDataServiceName(), "Service1SC");//Verifying the value
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Assert.fail("Class \"com.rc.wefunit.FixtureDependencyInjectorUtility\" do not exists");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (InstantiationException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void inject_method_with_2_parameters_Field_and_Object__GenericServiceOperationTest_dataServiceName_field_injection_Test_2(){
        try {
            final Class service2FirstSOTest =
                    Class.forName("test.models.test.services.Service2Test.Service2FirstSOTest");//SubCLass Of GenericServiceOperationTest
            GenericServiceOperationTest instance = (GenericServiceOperationTest) service2FirstSOTest.newInstance();
            Field dataServiceName = service2FirstSOTest.getSuperclass().getDeclaredField("dataServiceName");
            _fdiu.inject(dataServiceName, instance);
            Assert.assertEquals(instance.getDataServiceName(), "Service2SC");//Verifying the value
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Assert.fail("Class \"com.rc.wefunit.FixtureDependencyInjectorUtility\" do not exists");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (InstantiationException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void method_inject_should_inject_WebAppAccess_For_SCBuildersFixture_model(@Mocked final Factories.RunnerFactory runnerFactory, @Injectable final WebAppAccess webAppAccess, @Injectable final Runner runner){
        //        Fixtures starts
        class ABbcSO extends GenericServiceOperationTest{
            @Override
            public void parameter_count_test() {

            }
        } ABbcSO aBbcSO = new ABbcSO();
        Annotation[] annArr = null;
        Field webAppAccessField = null;
        try {
            annArr = _commonUtils.filterQualifierAnnotations(aBbcSO.getClass().getSuperclass().getDeclaredField("webAppAccess").getAnnotations());
            webAppAccessField = aBbcSO.getClass().getSuperclass().getDeclaredField("webAppAccess");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
        DependencySignature ds = new DependencySignature(WebAppAccess.class, annArr);
//        Fixtures Ends

        new Expectations(){
            {
                runnerFactory.getInstance();result = runner;
                runner.getWebAppAccess();result = webAppAccess;
                runner.getWebAppAccess().getModelInstance("test/SCBuildersFixture", null, true); result = webAppAccess;
            }
        };
        _fdiu.inject(webAppAccessField, aBbcSO);
        Assert.assertNotNull(aBbcSO.getWebAppAccessSCBuildersFixtureModel());//Not Null
    }
}