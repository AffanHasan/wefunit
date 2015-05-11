package com.rc.wefunit.testengine;

import com.google.gson.JsonObject;

import java.util.Map;
import java.util.Queue;

/**
 * Created by Affan Hasan on 4/24/15.
 */
public interface TestEngine {

    public void executeTests(Queue<Object> objectsQueue);

    public Map<String, Object> getTestScores();
}