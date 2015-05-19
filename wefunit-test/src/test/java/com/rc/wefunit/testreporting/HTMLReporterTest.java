package com.rc.wefunit.testreporting;

import com.bowstreet.util.SystemProperties;
import com.bowstreet.webapp.WebAppAccess;
import com.rc.wefunit.CommonTestFixtures;
import com.rc.wefunit.CommonUtils;
import com.rc.wefunit.ConfigReader;
import com.rc.wefunit.Factories;
import mockit.Expectations;
import mockit.Mocked;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Affan Hasan on 5/14/15.
 */
public class HTMLReporterTest {

//    private final HTMLReporter _htmlReporter = Factories.HTMLReporterFactory.getInstance();
    private final CommonUtils _commonUtils = Factories.CommonUtilsFactory.getInstance();
//    private final ConfigReader _configReader = Factories.ConfigReaderFactory.getInstance();
    private final String _WEBINFPath;
    private final String _DOC_ROOT_LINUX;
    private final String _DOC_ROOT_WIN;
    @Mocked SystemProperties systemProperties;

    @Parameters({CommonTestFixtures.WEB_INF_PATH_NAME_FIXTURE, CommonTestFixtures.DOCUMENT_ROOT_LINUX, CommonTestFixtures.DOCUMENT_ROOT_WINOWS})
    public HTMLReporterTest(String WEBINFPath, String docRootLinux, String docRootWin){
        this._WEBINFPath = WEBINFPath;
        this._DOC_ROOT_LINUX=  docRootLinux;
        this._DOC_ROOT_WIN = docRootWin;
    }

    public void initTest(){
        String wefunitDir = this._commonUtils.createPath(new String[]{"wefunitlogs"});
        final ConfigReader configReader = Factories.ConfigReaderFactory.getInstance();
        File file = new File(configReader.getBaseDirPathForLogging() + wefunitDir );
//        Deleting out the logging directory
        if(file.isDirectory()){
            try {
                Files.walkFileTree(Paths.get(file.getAbsolutePath().toString()), new DirCleaner());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void method_generateHTMLTestReporting_must_throw_IllegalStateException_when_reporting_dir_is_not_accessible(@Mocked final File file){
//        CommonTestFixtures.cmnExpectations(this._WEBINFPath);
        this.initTest();
        final ConfigReader configReader = Factories.ConfigReaderFactory.getInstance();
        final HTMLReporter htmlReporter = Factories.HTMLReporterFactory.getInstance();

        new Expectations(){{
            file.canWrite(); result = false;
        }};

        try{
            htmlReporter.generateHTMLTestReporting();
        }catch (IllegalStateException e){
            Assert.assertEquals(e.getMessage(), configReader.getBaseDirPathForLogging() + " is not writable");
        }
    }

//  TODO
    @Test
    public void if_reporting_directories_are_not_present_then_create_them(){
        CommonTestFixtures.cmnExpectations(this._WEBINFPath);
        this.initTest();
        final HTMLReporter htmlReporter = Factories.HTMLReporterFactory.getInstance();
        final ConfigReader configReader = Factories.ConfigReaderFactory.getInstance();
        String[] path = new String[]{"wefunitlogs", "testreports"};
        File directory = new File(configReader.getBaseDirPathForLogging() + this._commonUtils.createPath(path));
        Assert.assertTrue(!directory.isDirectory());// Assert that no directory is present
        htmlReporter.generateHTMLTestReporting();
        Assert.assertTrue(directory.isDirectory());// Assert that directory is present now
    }

//  TODO
    @Test(enabled = false)
    public void create_a_new_html_report_if_a_report_file_is_not_there(){
        CommonTestFixtures.cmnExpectations(this._WEBINFPath);
        final HTMLReporter htmlReporter = Factories.HTMLReporterFactory.getInstance();
        this.initTest();
        Assert.fail();

    }

//  TODO
    @Test(enabled = false)
    public void delete_html_report_if_one_is_previously_present_there_and_then_create_a_new_html_report(){
        this.initTest();
        Assert.fail();

    }

    private static class DirCleaner extends SimpleFileVisitor<Path> {

        String dirName;

        final List<String> fileList = new ArrayList<String>();

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            this.dirName = dir.toAbsolutePath().toString();
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            (new File(file.toAbsolutePath().toString())).delete();
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            (new File(dir.toAbsolutePath().toString())).delete();
            return FileVisitResult.CONTINUE;
        }
    }
}