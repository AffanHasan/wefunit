package com.rc.wefunit;

import com.rc.wefunit.annotations.Qualifier;
import com.rc.wefunit.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by root on 4/16/15.
 */
public class CommonUtils {

    public final Annotation[] filterQualifierAnnotations(Annotation[] annotations){
        List<Annotation> list = new ArrayList<Annotation>();
        for(Annotation item : annotations){
            if(item.annotationType().isAnnotationPresent(Qualifier.class))
                list.add(item);
        }
        Annotation[] arr = new Annotation[list.size()];
        return list.toArray(arr);
    }

    public final void getSuperClassesHierarchy(Class classObject, Set<Class> theSet){
        if(classObject.getSuperclass().equals(Object.class)) {
            theSet.add(classObject.getSuperclass());
            return;
        }else {
            theSet.add(classObject.getSuperclass());
            getSuperClassesHierarchy(classObject.getSuperclass(), theSet);
            return;
        }
    }

    public final Method[] getTestMethodsArrayFromTestClass(Class classObj){
        Set<Method> methods = new LinkedHashSet<Method>();
        for(Method method : classObj.getDeclaredMethods()){
            if(method.isAnnotationPresent(Test.class))
                methods.add(method);
        }
//        Getting a Set of super classes
        Set<Class> superClasses = new LinkedHashSet<Class>();
        this.getSuperClassesHierarchy(classObj, superClasses);
        if(superClasses.contains(GenericServiceOperationTest.class))//If this is a Service Operation Test then add inherited methods
            for(Method method :  classObj.getSuperclass().getDeclaredMethods() ){
                    if(method.isAnnotationPresent(Test.class))
                        methods.add(method);
            }
        Method[] arr = new Method[methods.size()];
        return methods.toArray(arr);
    }
}