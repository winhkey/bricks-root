/*
  Copyright 2020 fuzy(winhkey) (https://github.com/winhkey/bricks)
  <p>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p>
  http://www.apache.org/licenses/LICENSE-2.0
  <p>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

package org.bricks.utils;

import static org.bricks.utils.EntityUtils.getDeclaredMethod;
import static org.bricks.utils.EntityUtils.invokeMethod;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.Serializable;

import org.junit.Test;

import lombok.Getter;
import lombok.Setter;

public class EntityUtilsTest {

    @Test
    public void testGetDeclaredMethodNull() {
        assertNull(getDeclaredMethod(null, null));
    }

    @Test
    public void testGetDeclaredMethodObject() {
    	assertNull(getDeclaredMethod(Object.class, null));
    }

    @Test
    public void testInvokeMethod() {
        Child child = new Child();
        invokeMethod(child, "setSurname", new Class<?>[]{String.class}, new Object[]{"abc"});
        assertEquals("abc", child.getSurname());
    }

    @Setter
    @Getter
    private static class Parent implements Serializable {

		private static final long serialVersionUID = 1L;

		protected String surname;

		protected String name;

		protected int age;

    }

    @Setter
    @Getter
    private static class Child extends Parent {

		private static final long serialVersionUID = 1L;

    }

}
