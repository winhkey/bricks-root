package org.bricks.test.utils;

import org.bricks.utils.FunctionUtils;
import org.junit.Test;

public class FunctionUtilsTest {

    @Test
    public void testAcceptEmpty() {
        FunctionUtils.accept(null, null);
    }

}
