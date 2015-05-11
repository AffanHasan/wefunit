package com.rc.wefunit;

import com.rc.wefunit.annotations.GenericSODependency;
import com.rc.wefunit.annotations.Inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by Affan Hasan on 4/3/15.
 */
class DefaultFixtureDependencyInjectorUtility implements FixtureDependencyInjectorUtility {

    private final DependencyScanner _dependencyScanner = Factories.DependencyScannerFactory.getInstance();

    private Class _testClass;

    private final CommonUtils _commonUtils = Factories.CommonUtilsFactory.getInstance();

    @Override
    public void inject(Field field, Object instance){
        if(!field.isAnnotationPresent(Inject.class))//If field is not an injectable one
            throw new IllegalStateException("Provided field is not Injectable");

        _testClass = instance.getClass();
        if(instance instanceof GenericServiceOperationTest){//If this is a Service Operation Test
            try {
                switch (field.getAnnotation(GenericSODependency.class).value()){
                    case DATA_SERVICE_NAME:
                        String packageNameArray[] = _testClass.getPackage().getName().split("\\Q.\\E");
                        String dsName = packageNameArray[packageNameArray.length - 1];
                        dsName = dsName.split("Test")[0];
                        dsName = dsName + "SC";
                        field.set(instance, dsName);
                        break;
                    case SERVICE_OPERATION_NAME:
                        String classSimpleName = _testClass.getSimpleName();
                        String soName =  classSimpleName.split("Test")[0];
                        soName = soName.replaceFirst(soName.substring(0, 1), (soName.substring(0, 1).toLowerCase()));
                        field.set(instance, soName);
                        break;
                    default://Other Fields
//                        Annotation[] annArr = this._commonUtils.filterQualifierAnnotations(field.getAnnotations());
//                        DependencySignature ds = new DependencySignature(field.getType(), annArr);
//                        field.set(instance, _dependencyScanner.getDependency(ds));
                        _injectDependencyToField(field, instance);
                        break;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private final void _injectDependencyToField(Field field, Object object){
        Annotation[] annArr = this._commonUtils.filterQualifierAnnotations(field.getAnnotations());
        DependencySignature ds = new DependencySignature(field.getType(), annArr);
        try {
            field.set(object, _dependencyScanner.getDependency(ds));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}