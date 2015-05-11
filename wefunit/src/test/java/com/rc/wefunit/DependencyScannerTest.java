package com.rc.wefunit;

import com.bowstreet.webapp.WebAppAccess;
import com.rc.wefunit.annotations.GenericSODependency;
import com.rc.wefunit.annotations.Inject;
import com.rc.wefunit.annotations.Qualifier;
import com.rc.wefunit.producers.FactoryProducers;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;

/**
 * Created by Affan Hasan on 4/6/15.
 */
public class DependencyScannerTest {

    private final DependencyScanner _dependencyScanner = Factories.DependencyScannerFactory.getInstance();
    private final CommonUtils _commonUtils = Factories.CommonUtilsFactory.getInstance();

    @Test
    public void interface_DependencyScanner_existence(){
        try {
            Class classObj = Class.forName("com.rc.wefunit.DependencyScanner");
            Assert.assertNotNull(classObj);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void method_getDependency_exists(){

        try {
            Class classObj = Class.forName("com.rc.wefunit.DependencyScanner");
            Assert.assertNotNull(classObj.getMethod("getDependency", DependencySignature.class));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }
    
    @Test
    public void method_getDependency_DependencySignature_Object_as_return_type(){

        try {
            Class classObj = Class.forName("com.rc.wefunit.DependencyScanner");
            Method m = classObj.getMethod("getDependency", DependencySignature.class);
            Assert.assertEquals(m.getReturnType(), Object.class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }
    
    @Test
    public void method_getDependency_DependencySignature_as_first_parameter(){

        try {
            Class classObj = Class.forName("com.rc.wefunit.DependencyScanner");
            Method m = classObj.getMethod("getDependency", DependencySignature.class);
            Assert.assertNotNull(m);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void method_getDependency_return_WebAppAccess_For_SCBuildersFixture_model(@Mocked final Factories.RunnerFactory runnerFactory, @Injectable final WebAppAccess webAppAccess, @Injectable final Runner runner){
//        Fixtures starts
        class ABbcSO extends GenericServiceOperationTest{
            @Override
            public void parameter_count_test() {

            }
        } ABbcSO aBbcSO = new ABbcSO();
        Annotation[] annArr = null;
        try {
            annArr = _commonUtils.filterQualifierAnnotations(aBbcSO.getClass().getSuperclass().getDeclaredField("webAppAccess").getAnnotations());
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

        WebAppAccess webAppAccessObj = (WebAppAccess)_dependencyScanner.getDependency(ds);
        Assert.assertNotNull(webAppAccessObj);//Not Null
    }
}