/**
 * Copyright (c) 2013-2014, Rinc Liu (http://rincliu.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rincliu.library.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RLStrUtil
{
    private static final String URL_REG_EXPRESSION = "^(https?://)?([a-zA-Z0-9_-]+\\.[a-zA-Z0-9_-]+)+(/*[A-Za-z0-9/\\-_&:?\\+=//.%]*)*";

    private static final String EMAIL_REG_EXPRESSION = "\\w+(\\.\\w+)*@\\w+(\\.\\w+)+";

    /**
     * @param str
     * @return
     */
    public static String replaceBlank(String str)
    {
        String dest = "";
        if (str != null)
        {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * @param s
     * @return
     */
    public static boolean isUrl(String s)
    {
        if (s == null)
        {
            return false;
        }
        return Pattern.matches(URL_REG_EXPRESSION, s);
    }

    /**
     * @param s
     * @return
     */
    public static boolean isEmail(String s)
    {
        if (s == null)
        {
            return true;
        }
        return Pattern.matches(EMAIL_REG_EXPRESSION, s);
    }

    /**
     * @param s
     * @return
     */
    public static boolean isBlank(String s)
    {
        if (s == null)
        {
            return true;
        }
        return Pattern.matches("\\s*", s);
    }
}
