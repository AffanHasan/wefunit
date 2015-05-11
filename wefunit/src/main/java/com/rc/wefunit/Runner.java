package com.rc.wefunit;

import com.bowstreet.webapp.WebAppAccess;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * Created by Affan Hasan on 3/24/15.
 */
public interface Runner {

    public String getWebInfDirPath();

    public Set<String> scanTestClasses();

    public Set<Class> getTestClassesSet() throws ClassNotFoundException;

    public void run(WebAppAccess webAppAccess, ClassLoader classLoader);

    public WebAppAccess getWebAppAccess();

    public PriorityQueue<Class> getTestClassesExecutionPriorityQueue();

    public Queue<Object> getExecutableTestObjectsQueue();
}