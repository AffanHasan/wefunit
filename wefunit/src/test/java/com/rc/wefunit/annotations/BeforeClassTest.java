package com.rc.wefunit.annotations;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

/**
 * Created by Affan Hasan on 4/2/15.
 */
public class BeforeClassTest {

    @Test
    public void BeforeClass_annotation(){
        try {
            Class beforeClassAnnotation = Class.forName("com.rc.wefunit.annotations.BeforeClass");
            Assert.assertTrue(beforeClassAnnotation.isAnnotation());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Assert.fail("Annotation com.rc.wefunit.annotations.BeforeClass do not exists");
        }
    }

    @Test
    public void BeforeClass_annotation_RetentionPolicy_RUNTIME() {
        try {
            Class beforeClassAnnotation = Class.forName("com.rc.wefunit.annotations.BeforeClass");

            Assert.assertTrue(beforeClassAnnotation.isAnnotationPresent(Retention.class));
            Retention retention = (Retention) beforeClassAnnotation.getAnnotation(Retention.class);
            Assert.assertTrue(retention.value().equals(RetentionPolicy.RUNTIME));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Assert.fail("Annotation com.rc.wefunit.annotations.BeforeClass do not exists");
        }
    }

    @Test
    public void BeforeClass_annotation_Target_METHOD(){
        try {
            Class beforeClassAnnotation = Class.forName("com.rc.wefunit.annotations.BeforeClass");

            Assert.assertTrue(beforeClassAnnotation.isAnnotationPresent(Target.class));
            Target target = (Target) beforeClassAnnotation.getAnnotation(Target.class);
            Assert.assertTrue(target.value()[0] == (ElementType.METHOD));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Assert.fail("Annotation com.rc.wefunit.annotations.BeforeClass do not exists");
        }
    }

    @Test
    public void BeforeClass_annotation_enabled_method_default_value_true(){
        try {
            Class beforeClassAnnotation = Class.forName("com.rc.wefunit.annotations.BeforeClass");
            Method enabledMethod = beforeClassAnnotation.getMethod("enabled");
            Assert.assertTrue(enabledMethod.getDefaultValue().equals(new Boolean(true)));
            Assert.assertTrue(beforeClassAnnotation.isAnnotation());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Assert.fail("Annotation com.rc.wefunit.annotations.BeforeClass do not exists");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Assert.fail("Method enabled do not exists");
        }
    }
}