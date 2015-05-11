package com.rc.wefunit;

import com.rc.wefunit.annotations.Produces;
import com.rc.wefunit.annotations.Qualifier;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Affan Hasan on 4/6/15.
 */
class DefaultDependencyScanner implements DependencyScanner {

    private final SortedSet<FixtureDependency> _fd = new TreeSet<FixtureDependency>();

    private final CommonUtils _cu = new CommonUtils();

    public DefaultDependencyScanner(){
        Class returnType = null;
        Class classObj;
        Object instance;
//        Scanning factory dependencies
        try {
            classObj = Class.forName("com.rc.wefunit.producers.FactoryProducers");
            instance = classObj.newInstance();
            for( Method m : classObj.getMethods() ){
                if(m.isAnnotationPresent(Produces.class)){
//                    Create signature
                    returnType = m.getReturnType();
                    DependencySignature ds = new DependencySignature(returnType, this._cu.filterQualifierAnnotations(m.getDeclaredAnnotations()));
                    _fd.add(new FixtureDependency(ds, m));
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getDependency(DependencySignature ds){
        for( FixtureDependency fd : this._fd){
            if(fd.getSignature().equals(ds))
                return fd.getDependency();
        }
        return null;
    }
}