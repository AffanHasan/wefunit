package com.rc.wefunit.producers;

import com.bowstreet.webapp.WebAppAccess;
import com.rc.wefunit.DependencyScanner;
import com.rc.wefunit.DependencySignature;
import com.rc.wefunit.Factories;
import com.rc.wefunit.GenericServiceOperationTest;
import com.rc.wefunit.annotations.GenericSODependency;
import com.rc.wefunit.annotations.Produces;
import com.rc.wefunit.annotations.Qualifier;
import com.rc.wefunit.enums.GenericSOInjectables;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Affan Hasan on 4/3/15.
 */
public class FactoryProducersTest {

    private final DependencyScanner _dependencyScanner = Factories.DependencyScannerFactory.getInstance();

    private final FactoryProducers _fp = new FactoryProducers();

    private Class _fpClass;

    public FactoryProducersTest(){
        try {
            _fpClass = Class.forName("com.rc.wefunit.producers.FactoryProducers");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test(enabled = false)
    public void getGSOTServiceOperationName(){
        try {
            Method method = _fpClass.getMethod("getGSOTServiceOperationName");
            Assert.assertTrue(method.getReturnType().equals(String.class));
            Assert.assertTrue(method.isAnnotationPresent(Produces.class));
            Assert.assertTrue(method.isAnnotationPresent(GenericSODependency.class));
            Assert.assertTrue(method.getAnnotation(GenericSODependency.class).value() == GenericSOInjectables.SERVICE_OPERATION_NAME);

            class ABC extends GenericServiceOperationTest {
                @Override
                public void parameter_count_test() {

                }
            }
            ABC abc = new ABC();
            Field f = Class.forName("com.rc.wefunit.GenericServiceOperationTest").getField("serviceOperationName");
            Class type = f.getType();
            List<Annotation> list = new ArrayList<Annotation>();
            for( Annotation ann : f.getDeclaredAnnotations() ){
                if(ann.getClass().isAnnotationPresent(Qualifier.class))
                    list.add(ann);
            }
//            new DependencySignature(type, list.toArray());
//            _dependencyScanner.getDependency();

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void method_getSCBuildersFixturesModel(){
        try {
            Method method = _fpClass.getMethod("getSCBuildersFixturesModel");
            Assert.assertTrue(method.getReturnType().equals(WebAppAccess.class));//Check Return Type
            Assert.assertTrue(method.isAnnotationPresent(Produces.class));
            Assert.assertTrue(method.isAnnotationPresent(GenericSODependency.class));
            Assert.assertTrue(method.getAnnotation(GenericSODependency.class).value() == GenericSOInjectables.SERVICE_CONSUMER_BUILDERS_FIXTURE_MODEL);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void method_getSCBuildersFixturesModel_throw_IllegalStateException_when_a_model_named_SCBuildersFixture_is_not_found(){
        try{
            _fp.getSCBuildersFixturesModel();
        }catch(IllegalStateException e){
            Assert.assertEquals(e.getMessage(), "Model named \"SCBuildersFixture\" not found in WEB-INF/models/test directory");
            return;
        }
        Assert.fail();
    }
}