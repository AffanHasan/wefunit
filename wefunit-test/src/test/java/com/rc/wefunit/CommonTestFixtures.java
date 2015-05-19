package com.rc.wefunit;

import com.bowstreet.util.SystemProperties;
import mockit.Expectations;

/**
 * Created by Affan Hasan on 5/14/15.
 */
public class CommonTestFixtures {

    public static final String WEB_INF_PATH_NAME_FIXTURE = "WEB-INF-path-fixture";

    public static final String DOCUMENT_ROOT_LINUX = "document-root-linux";
    public static final String DOCUMENT_ROOT_WINOWS = "document-root-win";
    public static final String CONFIG_FILE_NAME = "config-file-name";
    public static final String PROJECT_NAME = "wef-project-name";

    public static void cmnExpectations(final String webInfPath){
        new Expectations(){{
            SystemProperties.getWebInfDir(); result = webInfPath;
        }};
    }
}