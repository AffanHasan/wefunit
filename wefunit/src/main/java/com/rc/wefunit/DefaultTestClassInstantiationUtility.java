package com.rc.wefunit;

import com.rc.wefunit.annotations.Inject;

import java.lang.reflect.Field;

/**
 * Created by Affan Hasan on 4/1/15.
 */
class DefaultTestClassInstantiationUtility implements TestClassInstantiationUtility {

    private Object _instance = null;

    private final FixtureDependencyInjectorUtility _dInjector = Factories.FixtureDependencyInjectorUtilityFactory.getInstance();

    @Override
    public Object instantiateTestClass(Class testClass) {

        final class InjectableFieldsFilter {

            public Field[] getInjectableFields(Field[] fields){
                Field[] injectableFields = new Field[fields.length];
                for( int c = 0; c < fields.length ; c++){
                    if(fields[c].isAnnotationPresent(Inject.class))
                        injectableFields[c] = fields[c];

                }
                return  injectableFields;
            }
        }

        try {
//            Instantiate object
            _instance = testClass.newInstance();
//            Now inject dependencies For class "GenericServiceOperationTest"
            if(_instance instanceof GenericServiceOperationTest){
//                  Get those fields which are annotated @Inject
                Field[] injectableFields = new InjectableFieldsFilter().getInjectableFields(testClass.getSuperclass().getDeclaredFields());
                    for( Field item : injectableFields ){
                        _dInjector.inject(item, _instance);
                    }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return _instance;
    }
}