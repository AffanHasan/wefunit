package com.rc.wefunit;

import com.rc.wefunit.annotations.GenericSODependency;
import com.rc.wefunit.annotations.Qualifier;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Affan Hasan on 4/16/15.
 */
public class CommonUtilsTest {

    private final CommonUtils _commonUtils = Factories.CommonUtilsFactory.getInstance();

    private Class getClassObject(){
        try {
            return Class.forName("com.rc.wefunit.CommonUtils");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
            return null;
        }
    }

    @Test
    public void filterQualifierAnnotations(){
        try {
            Class classObj = getClassObject();
            Method m = classObj.getMethod("filterQualifierAnnotations", Annotation[].class);
            Assert.assertNotNull(m);
            Assert.assertTrue(m.getReturnType().equals(Annotation[].class));
            Assert.assertTrue(m.getParameterCount() == 1);
            Assert.assertTrue(m.getParameters()[0].getType().equals(Annotation[].class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void filterQualifierAnnotations_functional_test(){
        class AbcSOTest extends GenericServiceOperationTest{
            @Override
            public void parameter_count_test() {

            }
        }
        AbcSOTest abcSOTest = new AbcSOTest();
        try {
            Field soNameField = abcSOTest.getClass().getSuperclass().getDeclaredField("serviceOperationName");
            CommonUtils cu = Factories.CommonUtilsFactory.getInstance();
            Annotation[] arr = cu.filterQualifierAnnotations(soNameField.getAnnotations());
            Assert.assertTrue(arr.length == 1);
            Assert.assertTrue(arr[0].annotationType().equals(GenericSODependency.class));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void test_CommonUtils_is_singleton_when_get_from_factory(){
        CommonUtils _cu1 = Factories.CommonUtilsFactory.getInstance();
        CommonUtils _cu2 = Factories.CommonUtilsFactory.getInstance();
        Assert.assertTrue(_cu1.equals(_cu2));
    }

    @Test
    public void method_getSuperClassesHierarchy(){
        class AbcSO extends GenericServiceOperationTest{
            @Override
            public void parameter_count_test() {

            }
        }
        Set<Class> superClassesHierarchySet = new LinkedHashSet<Class>();
        this._commonUtils.getSuperClassesHierarchy(AbcSO.class, superClassesHierarchySet);

        Assert.assertTrue(superClassesHierarchySet.size() == 2);
        Assert.assertTrue(superClassesHierarchySet.contains(Object.class));
        Assert.assertTrue(superClassesHierarchySet.contains(GenericServiceOperationTest.class));
    }

    @Test
    public void method_getTestMethodsArray(){
        class AbcTest{

            @com.rc.wefunit.annotations.Test
            public void test1(){

            }

            @com.rc.wefunit.annotations.Test
            public void test2(){

            }

            @com.rc.wefunit.annotations.Test
            public void test3(){

            }
        }
        Method[] testMethods = this._commonUtils.getTestMethodsArrayFromTestClass(AbcTest.class);
        Assert.assertEquals(testMethods.length, 3);
        for(Method m : testMethods){
            Assert.assertTrue(m.isAnnotationPresent(com.rc.wefunit.annotations.Test.class));
        }
    }

    @Test
    public void method_getTestMethodsArray_test_for_GeneServiceOperationTest(){
        class AbcTest extends GenericServiceOperationTest{
            @Override
            public void parameter_count_test() {

            }

            @com.rc.wefunit.annotations.Test
            public void test1(){

            }

            @com.rc.wefunit.annotations.Test
            public void test2(){

            }

            @com.rc.wefunit.annotations.Test
            public void test3(){

            }
        }
        Method[] testMethods = this._commonUtils.getTestMethodsArrayFromTestClass(AbcTest.class);
        Assert.assertEquals(testMethods.length, 6);
        for(Method m : testMethods){
            Assert.assertTrue(m.isAnnotationPresent(com.rc.wefunit.annotations.Test.class));
        }
    }
}