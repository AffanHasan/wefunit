package com.rc.wefunit;

/**
 * Created by Affan Hasan on 5/18/15.
 */
public interface ConfigReader {

    /**
     *
     * @return A string path referring to the base directory where WEFUnit logs and reports will be generated. It returns this path by looking to a common source
     */
    public String getBaseDirPathForLogging();

    /**
     *
     * @return Name of the current Web Experience Factory project
     */
    public String getProjectName();
}