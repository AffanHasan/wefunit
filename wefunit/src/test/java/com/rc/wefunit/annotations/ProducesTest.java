package com.rc.wefunit.annotations;

import org.testng.Assert;

import java.lang.annotation.*;

/**
 * Created by Affan Hasan on 4/3/15.
 */
public class ProducesTest {

    @Test
    public void m1(){
        Class producesAnnotation = null;
        try {
            producesAnnotation = Class.forName("com.rc.wefunit.annotations.Produces");
            Assert.assertNotNull(producesAnnotation);
            Assert.assertTrue(producesAnnotation.isAnnotationPresent(Retention.class));
            Retention retention = (Retention) producesAnnotation.getAnnotation(Retention.class);
            Assert.assertTrue(retention.value().equals(RetentionPolicy.RUNTIME));
            Target target = (Target) producesAnnotation.getAnnotation(Target.class);
            Assert.assertTrue(target.value().equals(ElementType.METHOD));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Assert.fail("Annotation \"com.rc.wefunit.annotations.Produces\" not found");
        }
    }
}
