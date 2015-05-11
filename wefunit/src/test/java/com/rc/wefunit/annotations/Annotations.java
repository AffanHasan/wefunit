package com.rc.wefunit.annotations;

import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.lang.annotation.Annotation;
import java.nio.file.Paths;

/**
 * Annotations
 */
public class Annotations {

    @org.testng.annotations.Test
    public void test(){
        try {
            Class c = Class.forName("com.rc.wefunit.annotations.Test");
            Assert.assertTrue(c.isAnnotation());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @org.testng.annotations.Test
    public void test_enabled_method(){
        try {
            Class c = Class.forName("com.rc.wefunit.annotations.Test");
            Assert.assertNotNull(c.getMethod("enabled"));
        } catch (ClassNotFoundException e) {
            Assert.fail("Class not found");
        } catch (NoSuchMethodException e) {
            Assert.fail("Method not present");
        }
    }
}