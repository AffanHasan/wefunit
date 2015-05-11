package com.rc.wefunit;

import com.bowstreet.util.SystemProperties;
import com.bowstreet.webapp.DataService;
import com.bowstreet.webapp.ServiceOperation;
import com.bowstreet.webapp.WebApp;
import com.bowstreet.webapp.WebAppAccess;
import com.rc.wefunit.annotations.GenericSODependency;
import com.rc.wefunit.annotations.Inject;
import com.rc.wefunit.annotations.ServiceConsumerFixtures;
import com.rc.wefunit.enums.GenericSOInjectables;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import org.omg.PortableServer.SERVANT_RETENTION_POLICY_ID;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import test.models.test.services.Service1Test.GetUserInfoSOTest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Affan Hasan on 3/23/15.
 */
public class GenericServiceOperationTestTest {

    private Class _getClassObject(){
        try {
            return Class.forName("com.rc.wefunit.GenericServiceOperationTest");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
            Assert.fail(e.getMessage());
            return null;
        }
    }

    @Test
    public void is_service_operation_exists_Method_exists(){
        try {
            Method method = GenericServiceOperationTest.class.getMethod("is_service_operation_exists", null);
            Assert.assertNotNull(method);
        } catch (NoSuchMethodException e) {
            Assert.fail("Method \"is_service_operation_exists\" not found");
        }
    }

    @Test
    public void is_service_operation_exists_method_should_have_Test_annotation(){
        try {
            Method method = GenericServiceOperationTest.class.getMethod("is_service_operation_exists");
            Assert.assertTrue(method.isAnnotationPresent(com.rc.wefunit.annotations.Test.class));
        } catch (NoSuchMethodException e) {
            Assert.fail("Method \"is_service_operation_exists\" not found");
        }
    }

    @Test
    public void is_webAppAccess_field_with_GenericSODependency_annotation_present(){
        try {
            Field field = GenericServiceOperationTest.class.getDeclaredField("webAppAccess");
            Assert.assertTrue(field.isAnnotationPresent(GenericSODependency.class));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void is_webAppAccess_field_with_GenericSODependency_annotation_with_value_SERVICE_CONSUMER_BUILDER_FIXTURE_MODEL_present(){
        try {
            Field field = GenericServiceOperationTest.class.getDeclaredField("webAppAccess");
            System.out.println("---GenericSODependency Value : " + field.getAnnotation(GenericSODependency.class).value());
            Assert.assertTrue(field.getAnnotation(GenericSODependency.class).value() == GenericSOInjectables.SERVICE_CONSUMER_BUILDERS_FIXTURE_MODEL);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void is_webAppAccess_field_with_Inject_annotation_present(){
        try {
            Field field = GenericServiceOperationTest.class.getDeclaredField("webAppAccess");
            Assert.assertTrue(field.isAnnotationPresent(Inject.class));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void is_webAppAccess_field_with_service_consumer_fixtures_annotation_is_protected(){
        try {
            Field field = GenericServiceOperationTest.class.getDeclaredField("webAppAccess");
            Assert.assertTrue(Modifier.isProtected(field.getModifiers()));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void is_serviceOperationName_string_field_present(){
        try {
            Field field = GenericServiceOperationTest.class.getDeclaredField("serviceOperationName");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void is_serviceOperationName_string_field_is_protected(){
        try {
            Field field = GenericServiceOperationTest.class.getDeclaredField("serviceOperationName");
            Assert.assertTrue(Modifier.isProtected(field.getModifiers()));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void is_serviceOperationName_string_field_contains_Inject_annotation(){
        try {
            Field field = GenericServiceOperationTest.class.getDeclaredField("serviceOperationName");
            Assert.assertTrue(field.isAnnotationPresent(Inject.class));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void is_dataServiceName_string_field_is_present(){
        try {
            Field field = GenericServiceOperationTest.class.getDeclaredField("dataServiceName");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void is_dataServiceName_string_field_contains_Inject_annotation() {
        try {
            Field field = GenericServiceOperationTest.class.getDeclaredField("dataServiceName");
            Assert.assertTrue(field.isAnnotationPresent(Inject.class));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void getter_for_serviceOperationName(){
        try {
            Method method = GenericServiceOperationTest.class.getMethod("getServiceOperationName");
            Assert.assertTrue(method.getReturnType().equals(String.class));//Return string
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Assert.fail("Method getServiceOperationName() not found");
        }
    }

    @Test
    public void serviceOperationName_field_is_GenericSODependency_annotation_present(){
        try {
            Field field = GenericServiceOperationTest.class.getDeclaredField("serviceOperationName");
            Assert.assertTrue(field.isAnnotationPresent(GenericSODependency.class));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Assert.fail("Field serviceOperationName not found");
        }
    }

    @Test
    public void serviceOperationName_field_is_GenericSODependency_annotation_with_value_SERVICE_OPERATION_NAME_present(){
        try {
            Field field = GenericServiceOperationTest.class.getDeclaredField("serviceOperationName");
            Assert.assertTrue(field.getAnnotation(GenericSODependency.class).value() == GenericSOInjectables.SERVICE_OPERATION_NAME);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Assert.fail("Field serviceOperationName not found");
        }
    }

    @Test
    public void getter_for_dataServiceName(){
        try {
            Method method = GenericServiceOperationTest.class.getMethod("getDataServiceName");
            Assert.assertTrue(method.getReturnType().equals(String.class));//Return string
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Assert.fail("Method getDataServiceName() not found");
        }
    }

    @Test
    public void is_dataServiceName_field_with_DATA_SERVICE_NAME_annotation(){

        try {
            Field field = GenericServiceOperationTest.class.getDeclaredField("dataServiceName");
            Assert.assertTrue(field.isAnnotationPresent(GenericSODependency.class));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Assert.fail("Field dataServiceName not found");
        }
    }

    @Test
    public void is_dataServiceName_field_with_DATA_SERVICE_NAME_annotation_with_value_DATA_SERVICE_NAME_present(){

        try {
            Field field = GenericServiceOperationTest.class.getDeclaredField("dataServiceName");
            Assert.assertTrue(field.getAnnotation(GenericSODependency.class).value() == GenericSOInjectables.DATA_SERVICE_NAME);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Assert.fail("Field dataServiceName not found");
        }
    }

    @Test
    public void getter_for_webAppAccess(){
        try {
            Method method = GenericServiceOperationTest.class.getMethod("getWebAppAccessSCBuildersFixtureModel");
            Assert.assertTrue(method.getReturnType().equals(WebAppAccess.class));//Return WebAppAccess
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Assert.fail("Method getWebAppAccessSCBuildersFixtureModel() not found");
        }
    }

    @Test
    public void is_service_operation_name_is_in_correct_format_Method_exists(){
        try {
            Method method = GenericServiceOperationTest.class.getMethod("is_service_operation_name_is_in_correct_format", null);
            Assert.assertNotNull(method);
        } catch (NoSuchMethodException e) {
            Assert.fail("Method \"is_service_operation_name_is_in_correct_format\" not found");
        }
    }

    @Test
    public void method_is_service_operation_name_is_in_correct_format_contains_Test_annotation(){
        try {
            Method method = GenericServiceOperationTest.class.getMethod("is_service_operation_name_is_in_correct_format", null);
            Assert.assertTrue(method.isAnnotationPresent(com.rc.wefunit.annotations.Test.class));
        } catch (NoSuchMethodException e) {
            Assert.fail("Method \"is_service_operation_name_is_in_correct_format\" not found");
        }
    }

    @Test
    public void method_is_service_operation_name_is_in_correct_format_contains_Test_annotation_functional_tests(@Injectable final DataService dataService, @Injectable final ServiceOperation serviceOperation, @Injectable final WebAppAccess webAppAccess, @Injectable final WebApp webApp){
//        Test For Valid Service Operation Names
        Set<String> validSONames = new LinkedHashSet<String>();
        validSONames.add("abcSO");
        validSONames.add("aSO");
        validSONames.add("aBcBSO");
        validSONames.add("a10SO");
        validSONames.add("a_0SO");
        for(String validSoName : validSONames){
            try {
                this._validSONameTest(dataService, serviceOperation, webAppAccess, webApp, validSoName);
            }catch (Throwable e){
                e.printStackTrace();
                Assert.fail(e.getMessage());
            }
        }
//       Test For In Valid Service Operation Names
        Set<String> inValidSONames = new LinkedHashSet<String>();
        inValidSONames.add("SO");
        inValidSONames.add("1SO");
        inValidSONames.add("_SO");
        inValidSONames.add(".SO");
        inValidSONames.add("AbcSO");
        inValidSONames.add("ABCSO");
        inValidSONames.add("ZSO");
        inValidSONames.add("ZSo");
        inValidSONames.add("ZsO");
        for(String inValidSOName : inValidSONames){
            try {
                this._validSONameTest(dataService, serviceOperation, webAppAccess, webApp, inValidSOName);
            }catch (AssertionError e){
                continue;
            }
            Assert.fail();
        }
    }

    @Test
    public void method_is_service_operation_exists_functional_tests(@Injectable final DataService dataService, @Injectable final ServiceOperation serviceOperation, @Injectable final WebAppAccess webAppAccess, @Injectable final WebApp webApp){
        final String dsName = "dsSC";
        final String serviceOperationName = "myFirstSO";
        final class AbcSOTest extends GenericServiceOperationTest{
            @Override
            public void parameter_count_test() {

            }
        }
        AbcSOTest abcSOTest = new AbcSOTest();
        Class classObj = AbcSOTest.class;
        try {
//            Set SO Name
            Field serviceOperationNameField = classObj.getSuperclass().getDeclaredField("serviceOperationName");
            serviceOperationNameField.setAccessible(true);
            serviceOperationNameField.set(abcSOTest, serviceOperationName);
//            Set DS Name
            Field dataServiceName = classObj.getSuperclass().getDeclaredField("dataServiceName");
            dataServiceName.setAccessible(true);
            dataServiceName.set(abcSOTest, dsName);
//            Set WebAppAccess
            Field webAppAccessField = classObj.getSuperclass().getDeclaredField("webAppAccess");
            webAppAccessField.setAccessible(true);
            webAppAccessField.set(abcSOTest, webAppAccess);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
//        Setting Expectations
        new Expectations(){{
            webAppAccess.getWebApp();result = webApp;
            webApp.getDataService(dsName);result = dataService;
            dataService.getOperation(serviceOperationName);result = null;
        }};
        outer : {
            try {
                abcSOTest.is_service_operation_exists();
            }catch (AssertionError e){
                Assert.assertEquals(e.getMessage(), "Expected object not to be null");
                break outer;
            }
            Assert.fail("No Throwable Expected Here");
        }
        //        Setting Expectations
        new Expectations(){{
            webAppAccess.getWebApp();result = webApp;
            webApp.getDataService(dsName);result = dataService;
            dataService.getOperation(serviceOperationName);result = serviceOperation;
        }};
        try {
            abcSOTest.is_service_operation_exists();
        }catch (Throwable e){
            Assert.assertEquals(e.getMessage(), "Expected object not to be null");
        }
    }

    private final void _validSONameTest(final DataService dataService, final ServiceOperation serviceOperation, final WebAppAccess webAppAccess, final WebApp webApp, final String serviceOperationName){
        final class AbcSOTest extends GenericServiceOperationTest{
            @Override
            public void parameter_count_test() {

            }
        }
        AbcSOTest abcSOTest = new AbcSOTest();
        Class classObj = AbcSOTest.class;
        final String dsName = "dsSC";
        try {
//            Set SO Name
            Field serviceOperationNameField = classObj.getSuperclass().getDeclaredField("serviceOperationName");
            serviceOperationNameField.setAccessible(true);
            serviceOperationNameField.set(abcSOTest, serviceOperationName);
//            Set DS Name
            Field dataServiceName = classObj.getSuperclass().getDeclaredField("dataServiceName");
            dataServiceName.setAccessible(true);
            dataServiceName.set(abcSOTest, dsName);
//            Set WebAppAccess
            Field webAppAccessField = classObj.getSuperclass().getDeclaredField("webAppAccess");
            webAppAccessField.setAccessible(true);
            webAppAccessField.set(abcSOTest, webAppAccess);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
//        Setting Expectations
        new Expectations(){{
            webAppAccess.getWebApp();result = webApp;
            webApp.getDataService(dsName);result = dataService;
            dataService.getOperation(serviceOperationName);result = serviceOperation;
            serviceOperation.getName();result = serviceOperationName;
        }};
        abcSOTest.is_service_operation_name_is_in_correct_format();//Perform the test
        return;

    }

    @Test
    public void method_getCallableServiceOperationName_functional_test(){
        final String dsName = "OurServiceSC";
        final String serviceOperationName = "myFirstSO";
        final class AbcSOTest extends GenericServiceOperationTest{
            @Override
            public void parameter_count_test() {

            }
        }
        AbcSOTest abcSOTest = new AbcSOTest();
        Class classObj = AbcSOTest.class;
        try {
//            Set SO Name
            Field serviceOperationNameField = classObj.getSuperclass().getDeclaredField("serviceOperationName");
            serviceOperationNameField.setAccessible(true);
            serviceOperationNameField.set(abcSOTest, serviceOperationName);
//            Set DS Name
            Field dataServiceName = classObj.getSuperclass().getDeclaredField("dataServiceName");
            dataServiceName.setAccessible(true);
            dataServiceName.set(abcSOTest, dsName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
        String resultName = abcSOTest.getCallableServiceOperationName();
        final String expected = "OurServiceSCMyFirstSOWithArgs";
        Assert.assertEquals(resultName , expected);
    }

    @Test
    public void abstract_method_parameter_count_test(){
        Class classObj = _getClassObject();
        try {
            Method m = classObj.getMethod("parameter_count_test");

            Assert.assertNotNull(m);
            Assert.assertTrue(Modifier.isAbstract(m.getModifiers()));
            Assert.assertTrue(m.isAnnotationPresent(com.rc.wefunit.annotations.Test.class));

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

}