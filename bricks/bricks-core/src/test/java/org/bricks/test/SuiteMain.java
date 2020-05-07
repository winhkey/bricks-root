package org.bricks.test;

import org.bricks.test.utils.FunctionUtilsTest;
import org.bricks.test.utils.ObjectUtilsTest;
import org.bricks.test.utils.ValidationUtilsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ObjectUtilsTest.class, ValidationUtilsTest.class, FunctionUtilsTest.class})
public class SuiteMain {

}
