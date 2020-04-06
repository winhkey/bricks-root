/*
 * Copyright 2020 fuzy(winhkey) (https://github.com/winhkey/bricks)
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

package org.bricks.utils;

import static com.google.common.collect.ImmutableMap.of;
import static com.google.common.collect.Lists.newArrayList;
import static org.bricks.TestConstants.Child;
import static org.bricks.utils.ValidationUtils.validate;

import org.bricks.exception.BaseException;
import org.junit.Test;

public class ValidationUtilsTest {

    @Test(expected = BaseException.class)
    public void testValidateObjectException() {
        validate(new Child());
    }

    @Test
    public void testValidateObject() {
        validate(new Child().setName("name").setSurname("surname").setAge(1));
    }

    @Test(expected = BaseException.class)
    public void testValidateListException() {
        validate(newArrayList(new Child()));
    }

    @Test
    public void testValidateList() {
        validate(newArrayList(new Child().setName("name").setSurname("surname").setAge(1)));
    }

    @Test(expected = BaseException.class)
    public void testValidateMapException() {
        validate(of("", new Child()));
    }

    @Test
    public void testValidateMap() {
        validate(of("", new Child().setName("name").setSurname("surname").setAge(1)));
    }

}
