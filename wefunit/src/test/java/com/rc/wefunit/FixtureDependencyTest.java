package com.rc.wefunit;

import com.bowstreet.webapp.WebAppAccess;
import com.rc.wefunit.annotations.GenericSODependency;
import com.rc.wefunit.annotations.Inject;
import com.rc.wefunit.annotations.Qualifier;
import com.rc.wefunit.enums.GenericSOInjectables;
import com.rc.wefunit.producers.FactoryProducers;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by Affan Hasan on 4/7/15.
 */
public class FixtureDependencyTest {

    private final String _dependencySignatureFieldName = "_dependencySignature";
    private final String _producerMethodFieldName = "_producerMethod";
    private final String _getSignatureMethodName = "getSignature";
    private final String _getDependencyMethodName = "getDependency";
    private final String _containingObjFieldName = "containingObj";

    private final Class getClassObject(){
        try{
            return Class.forName("com.rc.wefunit.FixtureDependency");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Assert.fail("Class com.rc.wefunit.FixtureDependency do not exists");
        }
        return null;
    }

    private final Field getFieldObject(String fieldName){
        try{
            return  getClassObject().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Assert.fail("Field \""+ fieldName +"\" do not exists");
        }
        return null;
    }

    private final Method getMethodObject(String methodName){
        try{
            return  getClassObject().getMethod(methodName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Assert.fail("Field \""+ methodName +"\" do not exists");
        }
        return null;
    }

    @Test
    public void implements_java_lang_comparable(){
        Class holder = null;
        for( Class i : getClassObject().getInterfaces() ){
            if( i.equals(Comparable.class) )
                holder = i;
        }
        Assert.assertNotNull(holder);
    }

    @Test
    public void method_compare_to_throw_IllegalArgumentException_when_argument_is_not_of_type_FixtureDependency(){
        class ABCSOTest extends GenericServiceOperationTest{
            @Override
            public void parameter_count_test() {

            }
        }
        ABCSOTest so = new ABCSOTest();
        try {
            Field f = so.getClass().getSuperclass().getDeclaredField("serviceOperationName");
            DependencySignature ds = new DependencySignature(f.getType(), f.getDeclaredAnnotation(GenericSODependency.class));
            Class fpClass = FactoryProducers.class;
            Method m =fpClass.getMethod("getSCBuildersFixturesModel");
            new FixtureDependency(ds ,m).compareTo(new Integer(2));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } catch (IllegalArgumentException e){
            Assert.assertEquals(e.getMessage(), "Provided object is not of type com.rc.wefunit.FixtureDependency");
            return;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Assert.fail();
    }

    @Test
    public void constructorTest(){
        Assert.assertTrue(getClassObject().getConstructors().length == 1);
        Assert.assertTrue(getClassObject().getConstructors()[0].getParameterCount() == 2);
        Assert.assertTrue(getClassObject().getConstructors()[0].getParameters()[0].getType().equals(DependencySignature.class));
        Assert.assertTrue(getClassObject().getConstructors()[0].getParameters()[1].getType().equals(Method.class));
//        Assert.assertTrue(getClassObject().getConstructors()[0].getParameters()[2].getType().equals(Object.class));
    }

    @Test
    public void existence_of_class(){
        Assert.assertNotNull(getClassObject());
    }

    @Test
    public void dependencySignature_field(){
        Assert.assertNotNull(getFieldObject(_dependencySignatureFieldName));
    }

    @Test
    public void dependencySignature_field_is_private(){
        Assert.assertTrue(Modifier.isPrivate(getFieldObject(_dependencySignatureFieldName).getModifiers()));
    }
    ;
    @Test
    public void dependencySignature_field_is_private_final(){
        Assert.assertTrue(Modifier.isFinal(getFieldObject(_dependencySignatureFieldName).getModifiers()));
    }

    @Test
    public void dependencySignature_field_is_private_is_of_type_DependencySignature(){
        Assert.assertEquals(getFieldObject(_dependencySignatureFieldName).getType(), DependencySignature.class);
    }

    @Test
    public void producerMethod_field(){
        Assert.assertNotNull(getFieldObject(_producerMethodFieldName));
    }

    @Test
    public void producerMethod_field_is_private(){
        Assert.assertTrue(Modifier.isPrivate(getFieldObject(_producerMethodFieldName).getModifiers()));
    }

    @Test
    public void producerMethod_field_is_private_final(){
        Assert.assertTrue(Modifier.isFinal(getFieldObject(_producerMethodFieldName).getModifiers()));
    }

    @Test
    public void producerMethod_field_is_private_is_of_type_Method(){
        Assert.assertEquals(getFieldObject(_producerMethodFieldName).getType(), Method.class);
    }

    @Test
    public void getSignature(){
        Assert.assertNotNull(getMethodObject(_getSignatureMethodName));
    }

    @Test
    public void getSignature_method_is_public(){
        Assert.assertTrue(Modifier.isPublic(getMethodObject(_getSignatureMethodName).getModifiers()));
    }

    @Test
    public void getSignature_method_return_type_is_DependencySignature(){
        Assert.assertTrue(getMethodObject(_getSignatureMethodName).getReturnType().equals(DependencySignature.class));
    }

    @Test
    public void method_getDependency(){
        Assert.assertNotNull(getMethodObject(_getDependencyMethodName));
    }

    @Test
    public void method_getDependency_method_is_public(){
        Assert.assertTrue(Modifier.isPublic(getMethodObject(_getDependencyMethodName).getModifiers()));
    }

    @Test
    public void method_getDependencyMethodName_return_type_is_Object(){
        Assert.assertTrue(getMethodObject(_getDependencyMethodName).getReturnType().equals(Object.class));
    }

    @Test
    public void method_getDependencyMethodName_return_WebAppAccess_For_SCBuildersFixture_model(@Mocked final FactoryProducers fp, @Injectable final WebAppAccess webAppAccess){
//        Fixtures starts
        class AbcSO extends GenericServiceOperationTest{
            @Override
            public void parameter_count_test() {

            }
        }; AbcSO abcSO = new AbcSO();
        Annotation[] qualifiers = null;
        try {
            qualifiers = Factories.CommonUtilsFactory.getInstance().filterQualifierAnnotations(abcSO.getClass().getSuperclass().getDeclaredField("webAppAccess").getAnnotations());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }

        DependencySignature ds = new DependencySignature(WebAppAccess.class, qualifiers);
        Method m = null;
        try {
            m = FactoryProducers.class.getDeclaredMethod("getSCBuildersFixturesModel");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
//        Fixtures ends

        new Expectations(){{
//            runner.getWebAppAccess(); result = webAppAccess;
            fp.getSCBuildersFixturesModel(); result = webAppAccess;
        }};
        FixtureDependency fd = new FixtureDependency(ds, m);
        WebAppAccess webAppAccessObj = (WebAppAccess) fd.getDependency();
        Assert.assertNotNull(webAppAccessObj);//Not Null
    }
}