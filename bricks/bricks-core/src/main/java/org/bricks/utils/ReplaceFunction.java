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

import static org.bricks.constants.Constants.NumberConstants.NUMBER_3;

import java.util.List;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;
import org.jaxen.function.StringFunction;

/**
 * xpath的replace方法实现
 *
 * @author fuzy
 *
 */
public class ReplaceFunction implements Function
{

    @SuppressWarnings("rawtypes")
    @Override
    public Object call(Context context, List list) throws FunctionCallException
    {
        if (list.size() == NUMBER_3)
        {
            return evaluate(list.get(0), list.get(1), list.get(2), context.getNavigator());
        }
        throw new FunctionCallException("replace() requires two arguments.");
    }

    /**
     * xpath替换字符串
     *
     * @param strArg xml
     * @param matchArg 源字符串
     * @param replaceArg 替换字符串
     * @param navigator 导航
     * @return 结果
     */
    private Object evaluate(Object strArg, Object matchArg, Object replaceArg, Navigator navigator)
    {
        String str = StringFunction.evaluate(strArg, navigator);
        String match = StringFunction.evaluate(matchArg, navigator);
        String replacement = StringFunction.evaluate(replaceArg, navigator);
        return str.replace(match, replacement);
    }

}
