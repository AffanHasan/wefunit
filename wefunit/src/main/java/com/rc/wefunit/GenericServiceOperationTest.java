package com.rc.wefunit;

import com.bowstreet.webapp.DataService;
import com.bowstreet.webapp.ServiceOperation;
import com.bowstreet.webapp.WebAppAccess;
import com.rc.wefunit.annotations.*;
import com.rc.wefunit.enums.GenericSOInjectables;
import com.rc.wefunit.probes.Assert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Affan Hasan on 3/23/15.
 */
public abstract class GenericServiceOperationTest {

    @Inject
    @GenericSODependency(GenericSOInjectables.SERVICE_CONSUMER_BUILDERS_FIXTURE_MODEL)
    protected WebAppAccess webAppAccess;

    @Inject
    @GenericSODependency(GenericSOInjectables.SERVICE_OPERATION_NAME)
    protected String serviceOperationName;

    @Inject
    @GenericSODependency(GenericSOInjectables.DATA_SERVICE_NAME)
    protected String dataServiceName;

    @Inject
    @GenericSODependency(GenericSOInjectables.CALLABLE_SERVICE_OPERATION_NAME)
    protected String callableServiceOperationName;

    public String getServiceOperationName(){
        return this.serviceOperationName;
    }

    public String getDataServiceName(){
        return this.dataServiceName;
    }

    public WebAppAccess getWebAppAccessSCBuildersFixtureModel(){
        return this.webAppAccess;
    }

    public String getCallableServiceOperationName(){
        String soName = new String(this.getServiceOperationName());
        soName = soName.replaceFirst(soName.substring(0, 1), soName.substring(0, 1).toUpperCase());
        return this.getDataServiceName() + soName + "WithArgs";
    }

    @Test
    public void is_service_operation_name_is_in_correct_format(){
        Pattern soNamePattern = Pattern.compile("[a-z]\\w*SO");
        DataService ds = this.getWebAppAccessSCBuildersFixtureModel().getWebApp().getDataService(this.getDataServiceName());
        ServiceOperation so = ds.getOperation(this.getServiceOperationName());
        String soName = so.getName();
        Matcher matcher = soNamePattern.matcher(soName);
        Assert.assertTrue(matcher.matches());//Assert name matches the pattern
    }

    @Test
    public void is_service_operation_exists(){
        DataService ds = this.getWebAppAccessSCBuildersFixtureModel().getWebApp().getDataService(this.getDataServiceName());
        ServiceOperation so = ds.getOperation(this.getServiceOperationName());
        Assert.assertNotNull(so);
    }

//    TODO:To implement an abstract method for checking the total number of parameters in a service operation
    @Test
    public abstract void parameter_count_test();
}