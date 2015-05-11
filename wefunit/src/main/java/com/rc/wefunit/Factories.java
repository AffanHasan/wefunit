package com.rc.wefunit;

import com.rc.wefunit.testengine.DefaultTestEngine;
import com.rc.wefunit.testengine.TestEngine;

import java.util.Set;

/**
 * Created by Affan Hasan on 3/24/15.
 */
public class Factories {

    public static class RunnerFactory {

        private static final Runner _runner = new DefaultRunner();

        public static Runner getInstance(){
            return _runner;
        }
    }

    public static class TestClassInstantiationUtilityFactory {
        public static TestClassInstantiationUtility getInstance(){
            return new DefaultTestClassInstantiationUtility();
        }
    }

    public static class FixtureDependencyInjectorUtilityFactory {
        public static FixtureDependencyInjectorUtility getInstance(){
            return new DefaultFixtureDependencyInjectorUtility();
        }
    }

    public static class DependencyScannerFactory {
        public static DependencyScanner getInstance(){
            return new DefaultDependencyScanner();
        }
    }

    public static class CommonUtilsFactory {

        private final static CommonUtils _cu = new CommonUtils();

        public static CommonUtils getInstance(){
            return _cu;
        }
    }

    public static class TestEngineFactory {

        private static final TestEngine testEngine = new DefaultTestEngine();

        public static TestEngine getInstance(){
            return testEngine;
        }
    }

    public static class TestClassStatsFactory {

        public static TestClassStats getInstance(Set<Class> set){
            return new TestClassStats(set);
        }
    }
}