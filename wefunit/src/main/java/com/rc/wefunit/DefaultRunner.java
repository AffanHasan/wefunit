package com.rc.wefunit;

import com.bowstreet.util.SystemProperties;
import com.bowstreet.webapp.WebAppAccess;
import com.rc.wefunit.testengine.TestEngine;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * Created by Affan Hasan on 3/24/15.
 */
class DefaultRunner implements Runner {

    private WebAppAccess _webAppAccess;
    private ClassLoader _classLoader;
    private final TestClassInstantiationUtility _testClassInstantiationUtility = Factories.TestClassInstantiationUtilityFactory.getInstance();
    private final TestEngine _testEngine = Factories.TestEngineFactory.getInstance();

    private boolean isWindows(){
        return System.getProperty("os.name").contains("Windows");
    }

    @Override
    public String getWebInfDirPath() {
        return SystemProperties.getWebInfDir();
    }

    private boolean isValidWefWebInfDirectory(){

        String path = this.getWebInfDirPath();
        boolean isModelsFolderPresent = false, isWorkFolderPresent = false;
        File file = new File(path);
        if(file.isDirectory() && path.endsWith("WEB-INF")){
            for(File item : file.listFiles()){
                if(item.getName().equals("models"))
                    isModelsFolderPresent = true;
                if(item.getName().equals("work"))
                    isWorkFolderPresent = true;
            }
            if( isModelsFolderPresent && isWorkFolderPresent )
                return true;
        }
        return false;
    }

    private final Set<String> _qualifiedFileNames = new LinkedHashSet<String>();

    @Override
    public Set<String> scanTestClasses() {

        if(!this.isValidWefWebInfDirectory()) {
            throw new IllegalStateException("Scan for test classes failed; no valid Web Experience Factory WEB-INF path found");
        }
        _qualifiedFileNames.clear();
        new FileTreeWalker().walkFileTree(
                new File( getWebInfDirPath() + ( isWindows() ? "\\work\\classes\\test" : "/work/classes/test" )),
                new TestClassFileVisitor());

        return _qualifiedFileNames;
    }

    private class TestClassFileVisitor{

        private String parseClassQualifiedNameFromPath(String path){
            String absolutePath = path.split((isWindows() ? "work\\\\classes" : "work/classes"))[1];
            absolutePath = absolutePath.replace((isWindows() ? '\\' : '/'), '.');
            absolutePath = absolutePath.replaceFirst("\\Q.\\E", "").trim();
            absolutePath = absolutePath.split("\\Q.\\Eclass")[0];
            return absolutePath;
        }

        public void visitFile(File file) throws IOException {

            if(str.frmt.validators.Factories.JavaTestClasscFileNameValidatorFactory.getInstance().
                    isJavaTestClassFileNameValid(file.getName())){
                _qualifiedFileNames.add(parseClassQualifiedNameFromPath(file.getPath()));
            }
        }
    }

    private class FileTreeWalker{

        class DirectoryFilter implements FilenameFilter{

            @Override
            public boolean accept(File dir, String name) {
                return dir.isDirectory();
            }
        }

        public void walkFileTree(File rootDir, TestClassFileVisitor testClassFileVisitor){

            File file = rootDir;

            if(file.isDirectory()){

                for( File item : file.listFiles() ){
                    try {
                        testClassFileVisitor.visitFile(item);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                File[] directories = file.listFiles(new DirectoryFilter());
                for( File dir : directories ){
                    this.walkFileTree(dir, testClassFileVisitor);//Recursion
                }
            }
        }
    }

    @Override
    public Set<Class> getTestClassesSet() throws ClassNotFoundException {
        Set<Class> classesSet = new LinkedHashSet<Class>();
        for( String className : this.scanTestClasses()){
            classesSet.add( Class.forName(className, true, this._classLoader));
        }
        return classesSet;
    }

    @Override
    public void run(WebAppAccess webAppAccess, ClassLoader classLoader) {
        this._webAppAccess = webAppAccess;
        this._classLoader = classLoader;
        _testEngine.executeTests(this.getExecutableTestObjectsQueue());
    }

    @Override
    public WebAppAccess getWebAppAccess() {
        if(this._webAppAccess == null)
            throw new IllegalStateException("Method \"getWebAppAccess\" is called before calling the \"run\" method");
        return this._webAppAccess;
    }

    @Override
    public PriorityQueue<Class> getTestClassesExecutionPriorityQueue(){
//        This comparator gives class objects of GenericServiceOperationTest the least priority so that they can be executed first
        final class TestClassesComparator implements Comparator<Class>{

            @Override
            public int compare(Class o1, Class o2) {
                int flag = 0;
//                Check For Equality
                if(o1.getSuperclass().equals(GenericServiceOperationTest.class) && o2.getSuperclass().equals(GenericServiceOperationTest.class)){
                    flag = 0;
                }
//                Check If o1 is Smaller
                else if(o1.getSuperclass().equals(GenericServiceOperationTest.class) && !o2.getSuperclass().equals(GenericServiceOperationTest.class)){
                    flag = -1;
                }
//                Check If o1 is Greater
                else if(!o1.getSuperclass().equals(GenericServiceOperationTest.class) && o2.getSuperclass().equals(GenericServiceOperationTest.class)){
                    flag = 1;
                }
                return flag;
            }
        }
        PriorityQueue<Class> pq = new PriorityQueue<Class>(11, new TestClassesComparator());
        try {
            for(Class classItem : this.getTestClassesSet()){
                    pq.add(classItem);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return pq;
    }

    @Override
    public Queue<Object> getExecutableTestObjectsQueue() {
        Queue<Object> queue = new LinkedList<Object>();
        PriorityQueue<Class> priorityQueue = this.getTestClassesExecutionPriorityQueue();
        while( priorityQueue.peek() != null ){
            queue.add(this._testClassInstantiationUtility.instantiateTestClass(priorityQueue.poll()));
        }
        return queue;
    }
}