package com.rc.wefunit.annotations;

import com.rc.wefunit.enums.GenericSOInjectables;
import org.testng.Assert;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by Affan Hasan on 4/2/15.
 */
public class GenericSODependencyTest {

    @org.testng.annotations.Test
    public void is_annotation_GenericSODependency(){
        try {
            Class genericSODependency = Class.forName("com.rc.wefunit.annotations.GenericSODependency");
            Assert.assertTrue(genericSODependency.isAnnotation());
        } catch (ClassNotFoundException e) {

        }
    }

    @org.testng.annotations.Test
    public void is_a_Qualifier(){
        try {
            Class genericSODependency = Class.forName("com.rc.wefunit.annotations.GenericSODependency");
            Assert.assertTrue(genericSODependency.isAnnotationPresent(Qualifier.class));
        } catch (ClassNotFoundException e) {
        }
    }

    @org.testng.annotations.Test
    public void target(){
        try {
            Class genericSODependency = Class.forName("com.rc.wefunit.annotations.GenericSODependency");
            Assert.assertTrue(genericSODependency.isAnnotationPresent(Target.class));
        } catch (ClassNotFoundException e) {
        }
    }

    @org.testng.annotations.Test
    public void target_METHOD_AND_FIELD(){
        try {
            Class genericSODependency = Class.forName("com.rc.wefunit.annotations.GenericSODependency");
            Target target = (Target) genericSODependency.getAnnotation(Target.class);
            Assert.assertTrue(Arrays.equals(target.value(), new ElementType[]{ElementType.FIELD, ElementType.METHOD}));
        } catch (ClassNotFoundException e) {
            Assert.fail(e.getMessage());
        }
    }

    @org.testng.annotations.Test
    public void is_value_method_present(){
        try {
            Class genericSODependency = Class.forName("com.rc.wefunit.annotations.GenericSODependency");
            Method methodValue = genericSODependency.getMethod("value");
            Assert.assertTrue(methodValue.getReturnType().equals(GenericSOInjectables.class));
        } catch (ClassNotFoundException e) {

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}