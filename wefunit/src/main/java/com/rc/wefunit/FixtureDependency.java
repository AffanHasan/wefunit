package com.rc.wefunit;

import com.rc.wefunit.producers.FactoryProducers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Affan Hasan on 4/7/15.
 */
class FixtureDependency implements Comparable {

    private final FactoryProducers _fp = new FactoryProducers();

    @Override
    public int compareTo(Object o) {
        FixtureDependency fd;
        try{
            fd = ( FixtureDependency ) o;
        }catch (ClassCastException e){
            throw new IllegalArgumentException("Provided object is not of type com.rc.wefunit.FixtureDependency");
        }
        return this.getSignature().compareTo(fd.getSignature());
    }
    private final DependencySignature _dependencySignature;

    private final Method _producerMethod;

    public FixtureDependency(DependencySignature ds, Method producerMethod){
        this._dependencySignature = ds;
        this._producerMethod = producerMethod;
    }

    public DependencySignature getSignature(){
        return this._dependencySignature;
    }
    public Object getDependency(){
        try {
            return this._producerMethod.invoke(_fp);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}