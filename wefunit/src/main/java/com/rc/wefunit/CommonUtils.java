package com.rc.wefunit;

import com.rc.wefunit.annotations.Qualifier;
import com.rc.wefunit.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
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

    /**
     *
     * @return Returns <b>1</b> If platform is linux. Returns <b>2</b> if platform is Windows.
     */
    public final int getOSPlatform(){
        if( System.getProperty("os.name").contains("Windows") )
            return 2;//Windows
        else
            return 1;//Linux
    }


    public final String getAsUnixPath(String[] subPath){
        String path = null;
        Iterator<String> it = Arrays.asList(subPath).iterator();
        path = new String("/");
        while (it.hasNext()){
            path = path + it.next() + (it.hasNext() ? "/" : "");
        }
        return path;
    }

    public final String getAsWindowsPath(String[] subPath){
        String path = null;
        Iterator<String> it = Arrays.asList(subPath).iterator();
        path = new String("\\");
        while (it.hasNext()){
            path = path + it.next() + (it.hasNext() ? "\\" : "");
        }
        return path;
    }

    /**
     * This method creates path suitable for the current platform
     * @param subPath : Array containing path elements
     * @return Platform specific path
     */
    public final String createPath(String[] subPath){
        switch (this.getOSPlatform()){
            case 1://Linux
                return this.getAsUnixPath(subPath);
            case 2://Windows
                return this.getAsWindowsPath(subPath);
            default:
                throw new IllegalStateException();
        }
    }
}