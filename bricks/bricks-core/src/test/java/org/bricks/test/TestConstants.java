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

package org.bricks.test;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.bricks.enums.ValueEnum;
import org.hibernate.validator.constraints.Range;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestConstants
{

    @Setter
    @Getter
    @Accessors(chain = true)
    public static class Parent implements Serializable
    {

        private static final long serialVersionUID = 1L;

        @NotNull
        protected String surname;

        @NotNull
        protected String name;

        @NotNull
        @Range(min = 1, max = 200)
        protected int age;

    }

    @Setter
    @Getter
    public static class Child extends Parent
    {

        private static final long serialVersionUID = 1L;

    }

    @Getter
    @AllArgsConstructor
    public enum Color implements ValueEnum<Integer>
    {

        RED(0);

        private final Integer value;

    }

}
