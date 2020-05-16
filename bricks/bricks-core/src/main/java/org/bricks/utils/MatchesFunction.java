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

import static org.bricks.constants.Constants.NumberConstants.NUMBER_2;

import java.util.List;

import org.jaxen.Context;
import org.jaxen.Function;
import org.jaxen.FunctionCallException;
import org.jaxen.Navigator;
import org.jaxen.function.StringFunction;

/**
 * xpath的match方法实现
 *
 * @author fuzy
 *
 */
public class MatchesFunction implements Function
{

    @SuppressWarnings("rawtypes")
    @Override
    public Object call(Context context, List list) throws FunctionCallException
    {
        if (list.size() == NUMBER_2)
        {
            return evaluate(list.get(0), list.get(1), context.getNavigator());
        }
        throw new FunctionCallException("matches() requires two arguments.");
    }

    /**
     * 正则匹配
     *
     * @param strArg xml
     * @param matchArg 正则
     * @param navigator 导航
     * @return 结果
     */
    private boolean evaluate(Object strArg, Object matchArg, Navigator navigator)
    {
        String str = StringFunction.evaluate(strArg, navigator);
        String match = StringFunction.evaluate(matchArg, navigator);
        return str.matches(match);
    }

}
