package com.rc.wefunit;

import com.bowstreet.util.SystemProperties;
import mockit.Expectations;
import org.testng.Assert;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Created by Affan Hasan on 5/14/15.
 */
public class CommonTestFixtures {

    public static final String WEB_INF_PATH_NAME_FIXTURE = "WEB-INF-path-fixture";

    public static final String DOCUMENT_ROOT_LINUX = "document-root-linux";
    public static final String DOCUMENT_ROOT_WINOWS = "document-root-win";
    public static final String CONFIG_FILE_NAME = "config-file-name";
    public static final String PROJECT_NAME = "wef-project-name";
    public static final String LOGGING_BASE_DIR_NAME = "logging-base-dir-name";
    public static final String LOGGING_TEST_REPORTING_DIR_NAME = "logging-reporting-dir-name";

    public static Document _document;

    /**
     * It sets mock paths for "WEB-INF" directory
     * @param webInfPath
     */
    public static void webInfPathExpectations(final String webInfPath){
        new Expectations(){{
            SystemProperties.getWebInfDir(); result = webInfPath;
        }};
    }

    /**
     * It sets mock paths for "Document Root" directory
     * @param documentRoot
     */
    public static void docRootPathExpectations(final String documentRoot){
        new Expectations(){{
            SystemProperties.getDocumentRoot(); result = documentRoot;
        }};
    }

    public static Document getConfigFile(){
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        Document document = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(new File("samplewefproject/WebContent/WEB-INF/wefunit.xml"));
            return document;
        } catch (ParserConfigurationException e) {
            Assert.fail();
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
        return document;
    }
}