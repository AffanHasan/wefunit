package com.rc.wefunit;

import java.util.SortedSet;

/**
 * Created by Affan Hasan on 4/6/15.
 */
public interface DependencyScanner {

    public Object getDependency(DependencySignature ds);
}