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

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static org.bricks.test.TestConstants.Child;
import static org.bricks.test.TestConstants.Color;
import static org.bricks.utils.ObjectUtils.buildString;
import static org.bricks.utils.ObjectUtils.convertData;
import static org.bricks.utils.ObjectUtils.convertMapList;
import static org.bricks.utils.ObjectUtils.copy;
import static org.bricks.utils.ObjectUtils.getEnumValue;
import static org.bricks.utils.ObjectUtils.getFieldValue;
import static org.bricks.utils.ObjectUtils.setFieldValue;
import static org.bricks.utils.ReflectionUtils.addDeclaredFields;
import static org.bricks.utils.ReflectionUtils.getComponentClass;
import static org.bricks.utils.ReflectionUtils.getComponentClassList;
import static org.bricks.utils.ReflectionUtils.getDeclaredField;
import static org.bricks.utils.ReflectionUtils.getDeclaredMethod;
import static org.bricks.utils.ReflectionUtils.invokeMethod;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.bricks.enums.ValueEnum;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class ObjectUtilsTest {

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
        addDeclaredFields(Child.class, list, true);
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

    @Test
    public void testBuildStringCollectionNull() {
        StringBuilder builder = new StringBuilder();
        buildString(builder, (Collection<?>) null);
        assertEquals("null", builder.toString());
    }

    @Test
    public void testBuildStringCollectionEmpty() {
        StringBuilder builder = new StringBuilder();
        buildString(builder, newArrayList());
        assertEquals("[]", builder.toString());
    }

    @Test
    public void testBuildStringCollection() {
        StringBuilder builder = new StringBuilder();
        buildString(builder, newArrayList(1));
        assertEquals("[1]", builder.toString());
    }

    @Test
    public void testBuildStringCollection1() {
        StringBuilder builder = new StringBuilder();
        List<List<Integer>> list = newArrayList();
        list.add(newArrayList(1));
        buildString(builder, list);
        assertEquals("[[1]]", builder.toString());
    }

    @Test
    public void testBuildStringCollection2() {
        StringBuilder builder = new StringBuilder();
        List<Map<String, Integer>> list = newArrayList();
        list.add(ImmutableMap.of("1", 1));
        buildString(builder, list);
        assertEquals("[{1=1}]", builder.toString());
    }

    @Test
    public void testBuildStringMapNull() {
        StringBuilder builder = new StringBuilder();
        buildString(builder, (Map<?, ?>) null);
        assertEquals("null", builder.toString());
    }

    @Test
    public void testBuildStringMapEmpty() {
        StringBuilder builder = new StringBuilder();
        buildString(builder, newHashMap());
        assertEquals("{}", builder.toString());
    }

    @Test
    public void testBuildStringMap() {
        StringBuilder builder = new StringBuilder();
        Map<String, Integer> map = newHashMap();
        map.put("1", 1);
        buildString(builder, map);
        assertEquals("{1=1}", builder.toString());
    }

    @Test
    public void testBuildStringArrayNull() {
        StringBuilder builder = new StringBuilder();
        buildString(builder, (Integer[]) null);
        assertEquals("null", builder.toString());
    }


    @Test
    public void testBuildStringArrayEmpty() {
        StringBuilder builder = new StringBuilder();
        buildString(builder, new int[0]);
        assertEquals("[]", builder.toString());
    }

    @Test
    public void testBuildStringArray() {
        StringBuilder builder = new StringBuilder();
        buildString(builder, new int[]{1});
        assertEquals("[1]", builder.toString());
    }

    @Test
    public void testGetEnumValue() {
        assertEquals(Color.RED, getEnumValue(Color.class, 0));
    }

    @Test
    public void testGetComponentClassList() {
        assertEquals(1, getComponentClassList(Color.class, ValueEnum.class).size());
    }

    @Test
    public void testGetComponentClass() {
        assertEquals(Integer.class, getComponentClass(Color.class, ValueEnum.class, 0));
    }

}
