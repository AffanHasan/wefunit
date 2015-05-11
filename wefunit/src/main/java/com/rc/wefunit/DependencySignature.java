package com.rc.wefunit;

import com.rc.wefunit.annotations.Qualifier;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * Created by Affan Hasan on 4/6/15.
 */
public class DependencySignature implements Comparable {

    @Override
    public int compareTo(Object o) {
        DependencySignature ds = null;
        try {
            ds = (DependencySignature) o;
        }catch (ClassCastException e){
            throw new IllegalArgumentException("Provided object is not of type DependencySignature");
        }
        return this._returnedObjectType.getSimpleName().compareTo(((DependencySignature) o).getReturnedObjectType().getSimpleName());
    }

    private final Class _returnedObjectType;

    private final Annotation[] _qualifiers;

    public DependencySignature(Class returnedObjectType, Annotation... qualifier){

        for( int c = 0; c <  qualifier.length ; c++){
            if(!qualifier[c].annotationType().isAnnotationPresent(Qualifier.class))
                throw new IllegalStateException("Provided annotation at index \"" + c + "\" is not a \"com.rc.wefunit.annotations.Qualifier\" annotation");
        }
        this._returnedObjectType = returnedObjectType;
        this._qualifiers = qualifier;
    }

    public Class getReturnedObjectType(){
        return this._returnedObjectType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DependencySignature that = (DependencySignature) o;

        if (!Arrays.equals(_qualifiers, that._qualifiers)) return false;
        if (!_returnedObjectType.equals(that._returnedObjectType)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _returnedObjectType.hashCode();
        result = 31 * result + (_qualifiers != null ? Arrays.hashCode(_qualifiers) : 0);
        return result;
    }
}