package com.rc.wefunit;

import com.bowstreet.util.SystemProperties;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Affan Hasan on 5/18/15.
 */
public class DefaultConfigReader implements ConfigReader {

    private final Runner _runner = Factories.RunnerFactory.getInstance();
    private final CommonUtils _commonUtils = Factories.CommonUtilsFactory.getInstance();
    private Document _document;
    private final Pattern _projectNamePattern = Pattern.compile("(.*[\\Q\\\\E/].*)([\\Q\\\\E/])(.+)(\\Q.\\Ewar)");

    public DefaultConfigReader(){
//      Creating config file object
        String[] fileName = new String[1];
        fileName[0] = "wefunit.xml";
        File wefUnitXML = new File(this._runner.getWebInfDirPath() + this._commonUtils.createPath(fileName));

        if(!wefUnitXML.isFile()){//If no file is present
            this._document = null;
        }else{//If file is present
            try {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                this._document = documentBuilder.parse(wefUnitXML);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBaseDirPathForLogging() {
        if(this._document == null){//If no file is present, then return user home
            return System.getProperty("user.home");
        }else if(this._document != null){//If file is present
            if(this._document.getElementsByTagName("logging-dir").getLength() == 0)//But no "<logging-dir/>" element then return user.home
                return System.getProperty("user.home");
            else{
                return ( this._document.getElementsByTagName("logging-dir").item(0) ).getTextContent();
            }
        }
        return null;
    }

    @Override
    public String getProjectName() {
        final Matcher matcher = this._projectNamePattern.matcher(SystemProperties.getDocumentRoot());
        matcher.find();
        return matcher.group(3);
    }
}