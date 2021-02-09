/*
 * Copyright 2020 fuzy(winhkey) (https://github.com/winhkey/bricks-root)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bricks.test.utils;

import static org.bricks.utils.PasswordUtils.encodePassword;
import static org.bricks.utils.PasswordUtils.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PasswordUtilsTest
{

    @Test
    public void testVerifyTrue()
    {
        assertTrue(verify("12345", encodePassword("12345")));
    }

    @Test
    public void testVerifyFalse()
    {
        assertFalse(verify("1234", encodePassword("12345")));
    }

}
