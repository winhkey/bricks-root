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

import static com.google.common.collect.Lists.newArrayList;
import static org.bricks.utils.EntityUtils.convertData;
import static org.bricks.utils.EntityUtils.convertMapList;
import static org.bricks.utils.EntityUtils.copy;
import static org.bricks.utils.EntityUtils.getDeclaredField;
import static org.bricks.utils.EntityUtils.getDeclaredFields;
import static org.bricks.utils.EntityUtils.getDeclaredMethod;
import static org.bricks.utils.EntityUtils.getFieldValue;
import static org.bricks.utils.EntityUtils.invokeMethod;
import static org.bricks.utils.EntityUtils.setFieldValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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

    @Test
    public void testGetDeclaredFieldNull() {
    	assertNull(getDeclaredField(null, null));
    }

    @Test
    public void testGetDeclaredFieldObject() {
    	assertNull(getDeclaredField(Object.class, null));
    }

    @Test
    public void testGetDeclaredFieldsStatic() {
    	List<Field> list = newArrayList();
        getDeclaredFields(Child.class, list, true);
        assertTrue(list.stream().anyMatch(field -> "serialVersionUID".equals(field.getName())));
    }

    @Test
    public void testGetFieldValue() {
        assertEquals("abc", getFieldValue(new Child().setSurname("abc"), "surname"));
    }

    @Test
    public void testSetFieldValue() {
    	Child child = new Child();
        setFieldValue(child, "surname", "abc");
        assertEquals("abc", child.getSurname());
    }

    @Test
    public void testCopy() {
    	Child dest = new Child();
    	copy(new Child().setSurname("abc"), dest);
        assertEquals("abc", dest.getSurname());
    }

    @Test
    public void testConvertData() {
        List<Child> list = convertMapList(newArrayList(ImmutableMap.of("surname", "abc")), Child.class);
        assertNotNull(list);
        assertEquals("abc", list.get(0).getSurname());
    }

    @Test
    public void testConvertDataEmpty() {
    	List<Child> list = convertMapList(newArrayList(), Child.class);
    	assertNull(list);
    }

    @Test
    public void testAddEntityToMap() {
        Child child = new Child();
        child.setSurname("a").setName("b").setAge(1);
        Map<String, Object> map = convertData(child);
        assertFalse(map.isEmpty());
    }

    @Test
    public void testAddEntityToMapExlude() {
        Child child = new Child();
        child.setSurname("a").setName("b").setAge(1);
        Map<String, Object> map = convertData(child, "surname");
        assertNull(map.get("surname"));
        assertNotNull(map.get("name"));
    }

    @Setter
    @Getter
    @Accessors(chain = true)
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
