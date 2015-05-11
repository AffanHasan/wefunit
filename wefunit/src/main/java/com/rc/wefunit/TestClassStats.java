package com.rc.wefunit;

import com.rc.wefunit.annotations.Test;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by root on 4/27/15.
 */
public final class TestClassStats{

    private final Set<Class> _testClassesSet;
    private final CommonUtils _commonUtils = Factories.CommonUtilsFactory.getInstance();

    TestClassStats(Set<Class> testClassesSet){
        this._testClassesSet = testClassesSet;
    }

    public int getSOTestClassesCount(){
        int count = 0;
        for(Class classItem : this._testClassesSet){
            if(classItem.getSuperclass().equals(GenericServiceOperationTest.class))
                count++;
        }
        return count;
    }

    public int getNonSOTestClassesCount(){
        int count = 0;
        for(Class classItem : this._testClassesSet){
            if(!classItem.getSuperclass().equals(GenericServiceOperationTest.class))
                count++;
        }
        return count;
    }

    public int getTotalTestClasses(){
        return this._testClassesSet.size();
    }

    public int getTotalExecutableTestsCount(){
        int count = 0;
        for( Class classObj : _testClassesSet ){
            for(Method m : classObj.getDeclaredMethods()){
                if(m.isAnnotationPresent(Test.class)){
                    if(m.getAnnotation(Test.class).enabled())
                        count++;
                }
            }
            Set<Class> superClassesSet = new LinkedHashSet<Class>();
            this._commonUtils.getSuperClassesHierarchy(classObj, superClassesSet);
            if(superClassesSet.contains(GenericServiceOperationTest.class))//If this is a GenericServiceOperationTest then add super class methods also
                for(Method m : classObj.getSuperclass().getDeclaredMethods()){
                    if(m.isAnnotationPresent(Test.class)){
                        if(m.getAnnotation(Test.class).enabled())
                            count++;
                    }
                }

        }
        return count;
    }

}